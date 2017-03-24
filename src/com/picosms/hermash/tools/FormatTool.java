package com.picosms.hermash.tools;



import java.nio.file.Path;
import java.nio.file.Paths;
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
	private ArrayList<String> header; //заголовок CSV фала
	private String[] body;  //тело
	private String input;   //входящий документ 
	private String[] splited;  //...разбитый на строки
	private String separator;  //используя в качеств разделителя
	
	/**
	 * Accepts CSV file with ';' separator
	 * Splits to body and header
	 * @param input
	 * @param csv
	 * @param separator 
	 */
	public FormatTool(String input, String csv, String separator){	

		this.input = input.trim();                //обрезаем лишние пробелы и переносы
		this.separator = Pattern.quote(separator);//делаем так, чтобы разделтиетль не был регулярным выражением
		header = new ArrayList<String>();         //создаме заголовок
		csv = csv.trim();                         //отрезаем мусор из входного цсв
		splited = csv.split("\n");                //режем его построчно
		for(String s : splited[0].split(separator)){ //делим заголовок на элементы
			header.add(s);
		}
		body = Arrays.copyOfRange(splited, 1, header.size()); //и копируем тело в другое поле
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
			output.add(element.split(separator)[header.indexOf(s)]); //смотрим индекс колонки с именем s и возврващем его содержимое(всю колонку)
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
					input = input.replaceAll("\\{"+p+"\\}", getByFieldName(p).get(i)); //заемняем содержимое документа делая все замены
			}
			output.add(input);
			System.out.println(input);
			input = this.input;
		}
		return output;
	}
}