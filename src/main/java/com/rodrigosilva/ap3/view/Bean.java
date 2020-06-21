package com.rodrigosilva.ap3.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rodrigosilva.ap3.model.Message;
import com.rodrigosilva.ap3.service.MessageService;

@Named
@RequestScoped
public class Bean {
	private Message message = new Message();
	private List<Message> messages;

	@Inject
	private MessageService messageService;

	@PostConstruct
	public void init() {
		messages = messageService.list();
	}

	public String submit() {
		messageService.create(message);
		messages.add(message);
		message = new Message();
		return null;
	}

	//////////////////////////////////////////////

	public Message getMessage() {
		return message;
	}
	public List<Message> getMessages() {
		return messages;
	}
}
