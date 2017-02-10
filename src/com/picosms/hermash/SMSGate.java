package com.picosms.hermash;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class SMSGate {
	private URL targetAPIURL;
	private Auth auth;
	
	public SMSGate(Auth auth) {
		this.auth = auth;
		try {
			targetAPIURL = new URL("https://sms-fly.com/api/api.php");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public String getBalance() throws Exception {
		return sendPost(Templates.BALANCE).toString();
	}
	
	public String sendSMS(String text, String number) throws Exception {
		System.out.println(String.format(Templates.SENDSMS, text, number));
		return sendPost(String.format(Templates.SENDSMS, text, number)).toString();
	}
	
	public String sendSMSBatch(String[] number, String[] text) throws Exception {
		return sendPost(Templates.BALANCE).toString();
	}
	
	
	private StringBuffer sendPost(String xmlPayload) throws Exception {
			HttpsURLConnection con = (HttpsURLConnection) targetAPIURL.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", auth.getHtmlAuthCredentials());
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
	
			in.close();
			return response;
	}
}