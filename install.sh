#!/bin/bash

# Some commands and procedures to configure the environment for AP3

mydir=$(dirname "$(readlink -f "$0")")
envfile=$mydir/ap3.env
source "$envfile"

# Create a standalone.conf.d/ dir in JBOSS, patch standalone.conf
# to read all .conf files in it, and create a symlink to our env vars conf file
jbossdir=$AP3_JBOSS_DIR
jbossconf=$jbossdir/bin/standalone.conf
confd=${jbossconf}.d
config=$confd/ap3.conf
mkdir -p -- "$confd"
if ! grep -q 'standalone/configuration/conf\.d/' -- "$jbossconf"; then
	cat >> "$jbossconf" <<-'EOF'

		# @@ By Rodrigo
		for conf in "$JBOSS_HOME"/standalone/configuration/conf.d/*.conf; do
		    . "$conf"
		done
	EOF
fi
rm -f -- "$config" && ln -s -- "$envfile" "$config"

exit

# Install and setup MySQL database
if ! type mysqld @>/dev/null; then
	sudo apt install mysql-server
	sudo mysql_secure_installation
fi
sudo mysql <<-EOF
	CREATE DATABASE ${AP3_MYSQL_DATABASE};

	CREATE USER ${AP3_MYSQL_USERNAME} IDENTIFIED BY '${AP3_MYSQL_PASSWORD}';
	GRANT ALL PRIVILEGES ON ${AP3_MYSQL_DATABASE}.* TO ${AP3_MYSQL_USERNAME};

	FLUSH PRIVILEGES;
	EXIT
EOF

#	<data-source>
#		<name>java:global/H2</name>
#		<class-name>org.h2.jdbcx.JdbcDataSource</class-name>
#		<url>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;AUTO_RECONNECT=1</url>
#		<user>sa</user>
#		<password>sa</password>
#	</data-source>

#	<data-source>
#		<name>java:global/MySQL</name>
#		<class-name>com.mysql.cj.jdbc.MysqlDataSource</class-name>
#		<url>jdbc:mysql://${AP3_MYSQL_HOSTNAME}/${env.AP3_MYSQL_DATABASE}${env.AP3_MYSQL_EXTRAPAR}</url>
#		<user>${AP3_MYSQL_USERNAME}</user>
#		<password>${env.AP3_MYSQL_PASSWORD}</password>
#	</data-source>


#                <datasource jndi-name="java:jboss/datasources/ap3" pool-name="ap3" enabled="true" use-java-context="true" statistics-enabled="${wildfly.datasources.statistics-enabled:${wildfly.statistics-enabled:false}}">
#                    <connection-url>jdbc:h2:file:~/ap3.h2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</connection-url>
#                    <driver>h2</driver>
#                    <security>
#                        <user-name>sa</user-name>
#                        <password>sa</password>
#                    </security>
#                </datasource>
