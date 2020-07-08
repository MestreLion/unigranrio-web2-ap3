package com.rodrigosilva.ap3.view.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;


@Named
@SessionScoped
public class LocaleBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext context;  // Require a @FacesConfig class, see ./FaceConfig.java

	private Locale active;
	private List<Locale> supported;

	@PostConstruct
	public void init() {
		Application app = context.getApplication();

		supported = new ArrayList<Locale>();
		supported.add(app.getDefaultLocale());
		app.getSupportedLocales().forEachRemaining(supported::add);

		// Get current locale. Equivalent to match(context.getExternalContext().getRequestLocales(), supported)
		active = app.getViewHandler().calculateLocale(context);

		for (Locale l : supported) {
			System.out.printf("[%s] %s: %s [%s]\n",
				l, l.toLanguageTag(), l.getDisplayName(), nativeName(l));
		}
	}

	public Locale getActive() {
		return active;
	}

	public List<Locale> getSupported() {
		return supported;
	}

	public String getLanguage() {
		return active.toLanguageTag();
	}
	public void setLanguage(String language) {
		active = Locale.forLanguageTag(language);
		context.getViewRoot().setLocale(active);
	}

	// Capitalize first letter in native DisplayName()
	public String nativeName(Locale locale) {
		String name = locale.getDisplayName(locale);
		if (name.isEmpty()) {
			System.err.printf("Invalid Locale: %s\n", locale);
			return locale.toLanguageTag();
		}
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	public String nativeName(String language) {
		return nativeName(Locale.forLanguageTag(language));
	}

	// Reload page keeping nice History/Back/etc even for POST
	public void reload() {
		context.getPartialViewContext().getEvalScripts().add("location.replace(location)");
	}
}
