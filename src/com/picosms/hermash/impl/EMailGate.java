package com.picosms.hermash.impl;

import java.util.Properties;
import java.util.Stack;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Date;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Address;
import com.sun.mail.smtp.*;


public class EMailGate{
	
	private Properties props;
	private Session session;
	private Auth auth;
	
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
	
	public String sendMessageBatch(Stack<String> target, Stack<String> text, Stack<String> topic) {
		final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		    service.scheduleWithFixedDelay(new Runnable() {
		        @Override
		        public void run()
		        {
		          try {
					sendMessage(target.pop(), text.pop(), topic.pop());
				} catch (Exception e) {
					e.printStackTrace();
				}
		        }
		      }, 0, 20, TimeUnit.SECONDS);
		
		return "Added to Queue!";
	}

	public String sendMessage(String target, String text, String header) throws Exception {
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
		return  t.getLastServerResponse();
	}
	
}