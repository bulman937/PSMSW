package com.picosms.hermash;

public interface SMSGateable {
	
	public String getBalance() throws Exception;
	public String sendSMS(String number, String text) throws Exception;
	public String sendSMSBatch(String number[], String[] text) throws Exception;
	
}
