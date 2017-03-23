package com.picosms.hermash.tools;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Pattern;

import com.picosms.hermash.ifaces.IFormatTool;

/**
 * Formatter for batched sender
 * @author Bohdan Dehtyar
 * 
 */

public class FormatTool implements IFormatTool {
	private ArrayList<String> header;
	private String[] body;
	private String input;
	private String[] splited;
	private String separator;
	
	/**
	 * Accepts CSV file with ';' separator
	 * Splits to body and header
	 * @param input
	 * @param csv
	 * @param separator 
	 */
	public FormatTool(String input, String csv, String separator){	
		
		this.input = input.trim();
		this.separator = Pattern.quote(separator);
		header = new ArrayList<String>();
		csv = csv.trim();
		splited = csv.split("\n");
		for(String s : splited[0].split(separator)){
			header.add(s);
		}
		body = Arrays.copyOfRange(splited, 1, header.size());
	}
	
	/**
	 *  Get all values in colunum by name
	 *  
	 * 
	 * @param s
	 * @return
	 */

	public ArrayList<String> getByFieldName(String s){
		ArrayList<String> output = new ArrayList<String>();
		for(String element:body){
			output.add(element.split(separator)[header.indexOf(s)]);
		}
		return output;
	}
	
	/* (non-Javadoc)
	 * @see com.picosms.hermash.tools.IFormatTool#format()
	 */
	@Override
	public ArrayList<String> format(){
		ArrayList<String> output = new ArrayList<String>();
 		String input = this.input;
 		
		for(int i = 0;i<body.length;i++){
			for(String p:header){
					input = input.replaceAll("\\{"+p+"\\}", getByFieldName(p).get(i));
			}
			output.add(input);
			System.out.println(input);
			input = this.input;
		}
		return output;
	}
}