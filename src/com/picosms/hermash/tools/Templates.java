package com.picosms.hermash.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * Static class for api templates
 * Can be edited via APITemplates files
 * 
 * @author Bohdan Dehtyar
 *
 */

public class Templates {
	public static String BALANCE;
	public static String SENDSMS;
	public static String SENDSMSBATCH;
	public static String BATCHENTRY;

	static {
		BufferedReader br ;
		try {
			br = new BufferedReader(new FileReader("res/APITemplates"));
			BALANCE = br.readLine().split(";")[1];
			SENDSMS = br.readLine().split(";")[1];
			SENDSMSBATCH = br.readLine().split(";")[1];
			BATCHENTRY = br.readLine().split(";")[1];

			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	} 
}
