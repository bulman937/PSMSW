package com.picosms.hermash;

/**
 * Represents username and password auth
 * @author Vladislav Hermash;
 * 
 */

public class Auth {
	private String username;
	private String password;
	
	public Auth(){
		username = "";
		password = "";
	}
	
	public Auth(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}

	public void setUsername(String value){
		username = value;
	}
	
	public void setPassword(String value){
		password = value;
	}
	
	/**
	 * Represent HTTP basic auth creds
	 * @return Base64encoded string
	 */
	
	public String getHtmlAuthCredentials(){
		return  "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(
		        String.format("%s:%s", getUsername(), getPassword()).getBytes());
				
	}
	
	/**
	 * Represent HTTP basic auth creds in static context
	 * @param username Username
	 * @param password Password
	 * @return Base64encoded string
	 */
	
	public static String getHtmlAuthCredentials(String username, String password){

		return  "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(
		        String.format("%s:%s", username, password).getBytes());
	}	
	
}