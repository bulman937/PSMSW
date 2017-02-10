package com.picosms.hermash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class Templates {
	static String BALANCE;
	static String SENDSMS;
	static {
		BufferedReader br ;
		try {
			br = new BufferedReader(new FileReader("res/APITemplates"));
			BALANCE = br.readLine().split(";")[1];
			SENDSMS = br.readLine().split(";")[1];
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	} 
}
