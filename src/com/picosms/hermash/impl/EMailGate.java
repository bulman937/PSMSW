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
	private Properties props;    //Properties для настройки почты
	private Session session;     //Cессия отправки сообщения
	private Auth auth;	         //Объект для авторизации
	
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
        props.put("mail.smtp.auth", "true");                  //установить необходимость авторизации
        props.put("mail.smtp.host", "smtp.gmail.com");        //... через smtp.gmail.com
        props.put("mail.smtp.port", "465");                   //... по порту 465
        props.put("mail.transport.protocol", "smtp");         // ... через smtp
        props.put("mail.smtp.starttls.enable", "true");       //.. с шифрованием
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // фабрика сокетов

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
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {  //создаем новую сессию
            protected PasswordAuthentication getPasswordAuthentication() {             
                return new PasswordAuthentication(auth.getUsername(), auth.getPassword()); //авторизуемся паролем
            }
        });
        
        Message msg = new MimeMessage(session);  //cоздаем объект сообщения
        msg.setFrom(new InternetAddress("test@local.resource"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(target, false)); //устанавливаем получателя
        msg.setSubject(header); //добавляем топик
        msg.setText(text); //и текст
        msg.setHeader("X-Mailer", "picoSMS"); //устанавливаем заголовок
        msg.setSentDate(new Date()); //ставим дату
        SMTPTransport t = //получем объект транспорта для отправки
            (SMTPTransport)session.getTransport("smtps");
        t.connect("smtp.gmail.com", auth.getUsername(), auth.getPassword()); //подключаемся
        t.sendMessage(msg, msg.getAllRecipients());  //отправляем сообщение
        t.close(); //закрываем транспорт
		return t.getLastServerResponse(); //и возвращаем пользователю последнее сообщение сервера
	}
	
}