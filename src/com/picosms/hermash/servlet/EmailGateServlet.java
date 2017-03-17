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
@WebServlet("/email")
@MultipartConfig
public class EmailGateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    private String buffer;
    private EMailGate gate;
    private String auth_creds;
    private FormatTool ft;
    
    
    /**
     * Reader of secret file
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    
	public EmailGateServlet() throws FileNotFoundException, IOException {
		
		try(BufferedReader br = new BufferedReader(new FileReader("res/email_auth_secret"))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line+"\n");
		        line = br.readLine();
		    }
		   auth_creds = sb.toString();
		}
	    gate = new EMailGate(new Auth(auth_creds.split("\n")[0], 
							          auth_creds.split("\n")[1]));

    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 RequestDispatcher view = request.getRequestDispatcher("EmailGateView.jsp");
		 view.forward(request, response);  

	}

	/**
	 * Servlet
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String apiResponce = "";
		try {
			if(request.getParameter("type").equals("single")) {
<<<<<<< HEAD
				String target = request.getParameter("email");
				String text = request.getParameter("text");
				String topic = request.getParameter("topic");
				apiResponce = gate.sendMessage(target, text, topic);
=======
				apiResponce = gate.sendMessage(request.getParameter("email"),
												request.getParameter("text"), 
												"Topic");
>>>>>>> 3e2096322c255169e189b2a88f8e627f9b80df3b
			}
			else if(request.getParameter("type").equals("batch")){
				String result;
				String targets = request.getParameter("target");
				String topic = request.getParameter("topic");
				String separator = request.getParameter("separator");
			    InputStream filecontent = null;
			    final Part filePart = request.getPart("_file");
			    filecontent = filePart.getInputStream();
			    BufferedReader br = new BufferedReader(new InputStreamReader(filecontent));
			    result = br.lines().collect(Collectors.joining("\n"));
			    ft = new FormatTool(request.getParameter("text"), result, separator);
			    ArrayList<String> textEntryes = ft.format();
			    gate.sendMessageBatch(ft.getByFieldName(targets), textEntryes, topic);
			}
		} catch (Exception e) {
				e.printStackTrace();
			}		
		buffer = apiResponce;
		doGet(request, response);
		}
	}



