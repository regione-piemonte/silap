/*-
 * ========================LICENSE_START=================================
 * colmirbff
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirbff.util;


import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.ejb.Asynchronous;
import javax.enterprise.context.Dependent;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import it.csi.silap.colmirbff.exception.BusinessException;


@Dependent
public class MailUtils {
	
	@ConfigProperty(name = "mail.smtp.auth")
	String mailSmtpAuth;
	@ConfigProperty(name = "mail.smtp.starttls.enable")
	String mailSmtpStarttlsEnable;
	@ConfigProperty(name = "mail.smtp.host")
	String mailSmtpHost;
	@ConfigProperty(name = "mail.smtp.port")
	String mailSmtpPort;
	
	@ConfigProperty(name = "mail.username")
	String mailUsername;
	@ConfigProperty(name = "mail.password")
	String mailPassword;
	
	
	@ConfigProperty(name = "mail.from")
	String mailFrom;
	@ConfigProperty(name = "mail.from.name")
	String mailFromName;
	

	private Session session;
	private InternetAddress innerFrom;
	
	
	

	
	
	public class AuthenticatorHelper extends Authenticator {
		private final String username;
		private final String password;

		public AuthenticatorHelper(String username, String password) {
			this.username = username;
			this.password = password;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}

	/**
	 * Sends a mail.
	 *
	 * @param to          the recipients
	 * @param subject     the subject
	 * @param text        the text
	 * @param attachments the attachments
	 */
	public void send(Collection<String> to, String subject, String text, List<Attachment> attachments) {
		innerSend(to, subject, text, MediaType.TEXT_PLAIN, attachments);
	}

	/**
	 * Sends a mail.
	 *
	 * @param to          the recipients
	 * @param subject     the subject
	 * @param html        the html
	 * @param attachments the attachments
	 */
	public void sendHtml(Collection<String> to, String subject, String html, List<Attachment> attachments) {
		innerSend(to, subject, html, MediaType.TEXT_HTML, attachments);
	}

	/**
	 * Sends a mail.
	 *
	 * @param to          the recipients
	 * @param subject     the subject
	 * @param text        the text
	 * @param mediaType   the media type
	 * @param attachments the attachments
	 */
	private void innerSend(Collection<String> to, String subject, String text, String mediaType, List<Attachment> attachments) {
		initMail();
		String addressList = to.stream().collect(Collectors.joining(","));
		if (addressList != null && subject != null && to != null) {
			Message message = new MimeMessage(session);
			try {
				message.setRecipients(RecipientType.TO, InternetAddress.parse(addressList));
				message.setSubject(subject);
				message.setFrom(innerFrom);
				handleContent(text, mediaType, attachments, message);
				Transport.send(message);
				Log.info("DA " + innerFrom + " " + "A " + addressList.toString());
			} catch (MessagingException e) {
				throw new BusinessException(composeErrorMessage(e), e);
			}
		}
	}

	/**
	 * Sends a mail asynchronously.
	 *
	 * @param to          the recipients
	 * @param subject     the subject
	 * @param text        the text
	 * @param attachments the attachments
	 */
	@Asynchronous
	public void sendAsync(Collection<String> to, String subject,  String text, List<Attachment> attachments) {
		send(to, subject, text, attachments);
	}


	/**
	 * Sends a mail asynchronously.
	 *
	 * @param to          the recipients
	 * @param subject     the subject
	 * @param html        the html
	 * @param attachments the attachments
	 */
	@Asynchronous
	public void sendHtmlAsync(Collection<String> to, String subject, String html, List<Attachment> attachments) {
		sendHtml(to, subject, html, attachments);
	}


	/**
	 * Handle the attachments to the message.
	 *
	 * @param text        the text
	 * @param mediaType   the media type
	 * @param attachments the attachments
	 * @param message     the message
	 * @throws MessagingException in case of an exception in content handling
	 */
	private void handleContent(String text, String mediaType, List<Attachment> attachments, Message message) throws MessagingException {
		if(attachments == null || attachments.isEmpty()) {
			// Send a plain email
			message.setContent(text, mediaType);
			return;
		}
		// Send a multipart message
		Multipart multipart = new MimeMultipart();
		// body
		MimeBodyPart textBodyPart = new MimeBodyPart();
		textBodyPart.setContent(text, mediaType);
		multipart.addBodyPart(textBodyPart);

		// Add attachments
		for(Attachment attachment : attachments) {
			MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			ByteArrayDataSource bads = new ByteArrayDataSource(attachment.getBytes(), attachment.getMediaType());
			attachmentBodyPart.setDataHandler(new DataHandler(bads));
			attachmentBodyPart.setFileName(attachment.getFileName());
			multipart.addBodyPart(attachmentBodyPart);
		}
		message.setContent(multipart);
	}
	
	/**
	 * Composes the error message
	 * @param e the error
	 * @return the message
	 */
	private String composeErrorMessage(Exception e) {
		return "Error in mail message composition: " + e.getMessage();
	}
	
	
	private void initMail() {
		
		Properties props = new Properties();
		
		props.put("mail.smtp.auth", mailSmtpAuth);
		props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
		props.put("mail.smtp.host", mailSmtpHost);
		props.put("mail.smtp.port", mailSmtpPort);
		
		session = Session.getInstance(props, new AuthenticatorHelper(mailUsername, mailPassword));
		
		try {
			innerFrom = new InternetAddress(mailFrom, mailFromName);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Illegal state in setting property for mail", e);
		}
		
	}
}
