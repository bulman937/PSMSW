package com.picosms.hermash.impl;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;

import com.picosms.hermash.ifaces.IAuth;
import com.picosms.hermash.tools.Templates;


/**
 * SMS Gate wrapper API
 * 	
 * @author Vladislav Hermash
 *
 */
public class SMSGate {
	private URL targetAPIURL;    //URL для API
	private IAuth auth;          //объект авторизации
	
	/**
	 * Default constuctor that accepts Auth instnace
	 * 
	 * @param auth
	 */
	
	public SMSGate(IAuth auth) {
		this.auth = auth;       
		try {
			targetAPIURL = new URL("https://sms-fly.com/api/api.php"); //URL бросает исключение, потому необходимо его ловить
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Get balance from gate
	 * 
	 * @return String with balance
	 * @throws Exception
	 */
	
	public String getBalance() throws Exception {
		System.out.println("[DEBUG] User is:"+auth.getUsername());
		return sendPost(Templates.BALANCE).toString();        //отправляем на api строчку BALANCE из templates
	}
	
	
	public void authRenewal(Auth auth) {
		this.auth = auth;         //обновить авторизацию
	}
	
	
	/**
	 * 
	 * Send sms via gate
	 * 
	 * @param text Text to send
	 * @param number Number of receprient
	 * @return Status
	 * @throws Exception
	 */
	
	public String sendSMS(String text, String number) throws Exception {
		System.out.println(String.format(Templates.SENDSMS, text, number));  
		return sendPost(String.format(Templates.SENDSMS,  text, number)).toString(); //изменяем SENDSMS, заменяя в нем текст и номер
	}
	
	/**
	 * 
	 * Batched SMS sender
	 * 
	 * @see sendSMS(String text, String number)
	 * @param number
	 * @param text
	 * @return
	 * @throws Exception
	 */
	
	public String sendSMSBatch(ArrayList<String> number, ArrayList<String> text) throws Exception {
		String batched = "";
		for(int i = 0;i<number.size();i++) {
			batched += String.format(Templates.BATCHENTRY, number.get(i), text.get(i)); //форматируем BATCHENTRY добавляя в каждый номер и текст
		}
		return sendPost(String.format(Templates.SENDSMSBATCH, batched)).toString(); //добавляем в SENDSMSBATCH содержимое BATCHENTRY
	}
	
	/**
	 * 
	 * API Request Dispatcher
	 * 
	 * Accepts XML Payload with valid XML request
	 * 
	 * @param xmlPayload
	 * @return
	 * @throws Exception
	 */
	
	
	private StringBuffer sendPost(String xmlPayload) throws Exception {
			HttpsURLConnection con = (HttpsURLConnection) targetAPIURL.openConnection();  //Создаем новое подключение
			con.setRequestMethod("POST"); //устанавливаем метод запроса(POST)
			con.setRequestProperty("Authorization", auth.getHtmlAuthCredentials()); //ставим в свойствах реквеста необходимость авторизации
			System.out.println(String.format("[REQUEST] SEND: %s", xmlPayload));  
			con.setDoInput(true); //
			con.setDoOutput(true);//читаем и пишем данные
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(xmlPayload.getBytes());  //записываем cообщение для отправку в api
			wr.flush(); 
			wr.close(); //и закрыаем поток
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream(), "UTF-8")); //читаем ответ
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);  //если он есть, доабавляем построчно
			}
			System.out.println(String.format("[REQUEST] GOT: %s", response));

			in.close(); //закрываем поток чтения
			return response; //возврващем пользователю резурльтат
	}
}