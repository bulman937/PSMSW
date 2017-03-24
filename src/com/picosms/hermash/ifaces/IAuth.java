package com.picosms.hermash.ifaces;

public interface IAuth {

	String getUsername();  

	String getPassword();

	void setUsername(String value);

	void setPassword(String value);

	/**
	 * Represent HTTP basic auth creds
	 * @return Base64encoded string
	 */

	String getHtmlAuthCredentials();  //username:password в base64

}