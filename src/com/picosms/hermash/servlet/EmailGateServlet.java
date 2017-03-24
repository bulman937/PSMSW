package com.picosms.hermash.servlet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.picosms.hermash.impl.Auth;
import com.picosms.hermash.impl.EMailGate;
import com.picosms.hermash.impl.SMSGate;
import com.picosms.hermash.tools.FormatTool;


/**
 * Servlet implementation class SMSGateServlet
 */
@WebServlet("/email") //находится по адресу {appname}/email
@MultipartConfig  //аннотация необходима для чтения файлов из веба
public class EmailGateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    private String buffer;  //буффер для ответа
    private EMailGate gate; //используем гейт для отрпавки email
    private FormatTool ft; //форматтер
    private boolean isLoggin; //флаг логина
    
    /**
     * Reader of secret file
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    
	public EmailGateServlet() throws FileNotFoundException, IOException {
		gate = new EMailGate(new Auth()); //Создаем новый гейт с пустой аторизацией
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 RequestDispatcher view = request.getRequestDispatcher("EmailGateView.jsp"); //указываем что страница email отображает EmailGateView
		 try {            
			if(isLoggin) { 
				request.setAttribute("lock", " ");  //если залогиннен, пооменять все $lock на странице на " "
				request.setAttribute("buffer", buffer); //и выдать буфер
			} else {
				request.setAttribute("lock", "disabled");  //если нет, то заблокировать поля ввода
				request.setAttribute("buffer", "Login to continue"); //и предложить залогинится
			}
		} catch (Exception e) {
			;
		}
		 view.forward(request, response);   //отправить все пользователю

	}

	/**
	 * Servlet
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String apiResponce = ""; 
		try {
			if(request.getParameter("type").equals("single")) { //если отправлено из формы "single" 
				String target = request.getParameter("email");  
				String text = request.getParameter("text");    //получить все прамеетры из полей ввода
				String topic = request.getParameter("topic");
				apiResponce = gate.sendMessage(target, text, topic); //и вызвать sendMessage
			}
			if(request.getParameter("type").equals("batch")){  //если отправлено из формы batch
				String result;
				String targets = request.getParameter("target");
				String topic = request.getParameter("topic");      //получить все параметры
				String separator = request.getParameter("separator");
			    InputStream filecontent = null;
			    final Part filePart = request.getPart("_file"); //и получить файл
			    filecontent = filePart.getInputStream();
			    BufferedReader br = new BufferedReader(new InputStreamReader(filecontent)); 
			    result = br.lines().collect(Collectors.joining("\n")); //объеденить его в одну строку
			    ft = new FormatTool(request.getParameter("text"), result, separator); //инициализировать форматтер
			    ArrayList<String> textEntryes = ft.format(); //и поменять содержимое фигурных скобок на текст
			    gate.sendMessageBatch(ft.getByFieldName(targets), textEntryes, topic);
			}if(request.getParameter("type").equals("login")) {
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				Auth auth = new Auth(username, password);  //форма входа
				gate.authRenewal(auth); //обновить объект авторизации
				isLoggin = true;
			}if(request.getParameter("type").equals("logout")) { //и выхода
				System.out.println("[DEBUG]: Flushed account!");
				String username = "";
				String password = "";
				Auth auth = new Auth(username, password);
				gate.authRenewal(auth);
				isLoggin = false;
			}
		} catch (Exception e) {
				e.printStackTrace();
			}		
		buffer = apiResponce;
		doGet(request, response); //затем вернуть пользователю страницу
		}
	}



