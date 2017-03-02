package com.picosms.hermash.ifaces;

public interface IGate {
	
	public String getBalance() throws Exception;
	public String sendMessage(String number, String text) throws Exception;
	public String sendMessageBatch(String number[], String[] text) throws Exception;
	
}
