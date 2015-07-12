package com.slgerkamp.daily.life.infra.message;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * <p>メール配信サービスです。
 *
 */
@Component
public class EmailService {

	@Autowired
	private MailSender mailSender;

	public void send(Collection<SimpleMailMessage> messageList) {
		mailSender.send(messageList.toArray(new SimpleMailMessage[messageList.size()]));
	}
}
