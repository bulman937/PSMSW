package com.picosms.hermash.impl;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import com.picosms.hermash.ifaces.IAuth;
import com.picosms.hermash.tools.Templates;


/**
 * SMS Gate wrapper API
 * 	
 * @author Vladislav Hermash
 *
 */
public class SMSGate {
	private URL targetAPIURL;
	private IAuth auth;
	
	/**
	 * Default constuctor that accepts Auth instnace
	 * 
	 * @param auth
	 */
	
	public SMSGate(IAuth auth) {
		this.auth = auth;
		try {
			targetAPIURL = new URL("https://sms-fly.com/api/api.php");
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
		return sendPost(Templates.BALANCE).toString();
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
		return sendPost(String.format(Templates.SENDSMS, text, number)).toString();
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
			batched += String.format(Templates.BATCHENTRY, number.get(i), text.get(i));
		}
		return sendPost(String.format(Templates.SENDSMSBATCH, batched)).toString();
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
			HttpsURLConnection con = (HttpsURLConnection) targetAPIURL.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", auth.getHtmlAuthCredentials());
			System.out.println(String.format("[REQUEST] SEND: %s", xmlPayload));
			con.setDoInput(true);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(xmlPayload.getBytes());
			wr.flush();
			wr.close();
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			System.out.println(String.format("[REQUEST] GOT: %s", response));

			in.close();
			return response;
	}
}