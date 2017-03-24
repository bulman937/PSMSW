package com.picosms.hermash.impl;

import com.picosms.hermash.ifaces.IAuth;

/**
 * Represents username and password auth
 * @author Vladislav Hermash;
 * 
 */

public class Auth implements IAuth {
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
	
	/* (non-Javadoc)
	 * @see com.picosms.hermash.IAuth#getUsername()
	 */
	@Override
	public String getUsername(){
		return username;
	}
	
	/* (non-Javadoc)
	 * @see com.picosms.hermash.IAuth#getPassword()
	 */
	@Override
	public String getPassword(){
		return password;
	}

	/* (non-Javadoc)
	 * @see com.picosms.hermash.IAuth#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String value){
		username = value;
	}
	
	/* (non-Javadoc)
	 * @see com.picosms.hermash.IAuth#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String value){
		password = value;
	}
	
	/* (non-Javadoc)
	 * @see com.picosms.hermash.IAuth#getHtmlAuthCredentials()
	 */
	
	@Override
	public String getHtmlAuthCredentials(){
		return  "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(       //"Basic " + username:password
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