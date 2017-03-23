package com.picosms.hermash.impl;

import java.util.Properties;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import javax.mail.*;
import javax.mail.internet.*;

import org.jsoup.Jsoup;

import javax.mail.Address;
import com.sun.mail.smtp.*;


public class EMailGate{
	
	/**
	 * Stuff for mailx library and auth object
	 * 
	 */
	private Properties props;
	private Session session;
	private Auth auth;
	
	public void authRenewal(Auth auth) {
		this.auth = auth;
	}
	
	
	/**
	 * Java MailX reques initalization via java.properties
	 * 
	 * @param auth
	 */
	
	
	
	public EMailGate(Auth auth){
		this.auth = auth;
        props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

	}
	
	/**
	 * Batched message sender
	 * 
	 * @param target
	 * @param text
	 * @param topic
	 * @return result
	 * @throws Exception
	 */
	
	public String sendMessageBatch(ArrayList<String> target, ArrayList<String> text, String topic) throws Exception {
		String output = "";

		for(int i = 0;i < target.size();i++) {
			output = output + sendMessage(target.get(i), text.get(i), topic);
		}
		return output;
	}
	
	/**
	 * Send single message
	 * 
	 * @param target
	 * @param text
	 * @param header
	 * @return
	 * @throws Exception
	 */

	public String sendMessage(String target, String text, String header) throws Exception {
		System.out.println("[DEBUG]: Sending via " + auth.getUsername());
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(auth.getUsername(), auth.getPassword());
            }
        });
        
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("test@local.resource"));;
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(target, false));
        msg.setSubject(header);
        msg.setText(text);
        msg.setHeader("X-Mailer", "picoSMS");
        msg.setSentDate(new Date());
        SMTPTransport t =
            (SMTPTransport)session.getTransport("smtps");
        t.connect("smtp.gmail.com", auth.getUsername(), auth.getPassword());
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();
		return t.getLastServerResponse();
	}
	
}