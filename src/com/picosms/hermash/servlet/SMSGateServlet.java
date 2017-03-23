package com.picosms.hermash.servlet;

import java.io.BufferedReader;
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
@WebServlet("/sms")
@MultipartConfig
public class SMSGateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * Default constructor. 
     */
	private SMSGate gate;
    private String buffer;
    private boolean isLoggin = false;
    private FormatTool ft;
	public SMSGateServlet() throws IOException {
		
	    gate = new SMSGate(new Auth());
        buffer = "";

    }

    /**
     * 
     * 
     * Resolves SMS gate page
     * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 RequestDispatcher view = request.getRequestDispatcher("SMSGateView.jsp");
		 try {
			if(isLoggin) {
				request.setAttribute("lock", " ");
				request.setAttribute("balance", gate.getBalance());
				request.setAttribute("buffer", buffer);
			} else {
				request.setAttribute("lock", "disabled");
				request.setAttribute("buffer", "Login to continue");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			;
		}
		 view.forward(request, response);  

	}

	/**
	 * 
	 * Resolves form requests from SMS page
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String apiResponce = "";
			try {
				if(request.getParameter("type").equals("single")) {
					String text = request.getParameter("text");
					String tel = request.getParameter("tel");
					request.setAttribute("balance", gate.getBalance());
					apiResponce = gate.sendSMS(text, tel);
				}
				if(request.getParameter("type").equals("batch")){
					String fileresult;
					String rawText = request.getParameter("text");
					String targetColunum = request.getParameter("target");
					String separator = request.getParameter("separator");
				    InputStream filecontent = null;
				    final Part filePart = request.getPart("_file");
				    filecontent = filePart.getInputStream();
				    BufferedReader br = new BufferedReader(new InputStreamReader(filecontent));
				    fileresult = br.lines().collect(Collectors.joining("\n"));
				    ft = new FormatTool(rawText, fileresult, separator);
				    ArrayList<String> numbers = ft.getByFieldName(targetColunum);
				    ArrayList<String> texts = ft.format();
				    System.out.println("here?");
				    apiResponce = gate.sendSMSBatch(numbers, texts);
				    
				}
				if(request.getParameter("type").equals("login")) {
					String username = request.getParameter("username");
					String password = request.getParameter("password");
					Auth auth = new Auth(username, password);
					gate.authRenewal(auth);
					isLoggin = true;
				}if(request.getParameter("type").equals("logout")) {
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
			doGet(request, response);
			}
	}



