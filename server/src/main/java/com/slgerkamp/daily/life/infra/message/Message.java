package com.slgerkamp.daily.life.infra.message;

import java.util.Collection;
import java.util.function.Consumer;

import org.springframework.mail.SimpleMailMessage;


/**
 * <p> メッセージのヘッダーを格納します。
 * <p> Fluent Builder
 *
 */
public class Message {

	private String sender;
	private String[] addresses;
	private String subject;
	private String text;

	public Message sender(String paramSender) {
		this.sender = paramSender;
		return this;
	}
	public Message addresses(Collection<String> paramAddresses) {
		this.addresses = paramAddresses.toArray(new String[paramAddresses.size()]);
		return this;
	}
	public Message subject(String paramSubject) {
		this.subject = paramSubject;
		return this;
	}
	public Message content(MessageContent content) {
		this.text = content.content();
		return this;
	}

	public static SimpleMailMessage build(final Consumer<Message> builder) {
		Message m = new Message();
		builder.accept(m);
		if (m.sender == null || m.addresses == null || m.subject == null || m.text == null) {
			throw new NullPointerException();
		}
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(m.sender);
		message.setTo(m.addresses);
		message.setSubject(m.subject);
		message.setText(m.text);
		return message;
	}
}
