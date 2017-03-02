package com.picosms.hermash.servlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private String auth_creds;
    private FormatTool ft;
	public SMSGateServlet() throws IOException {
		try(BufferedReader br = new BufferedReader(new FileReader("res/sms_auth_secret"))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line.trim());
		        line = br.readLine();
		    }
		   auth_creds = sb.toString();
		}
		System.out.println(auth_creds);
        gate = new SMSGate(new Auth(auth_creds.split(";")[0], 
        							auth_creds.split(";")[1]));
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
			request.setAttribute("balance", gate.getBalance());
			request.setAttribute("buffer", buffer);
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
					apiResponce = gate.sendSMS(request.getParameter("text"), 
											   request.getParameter("tel"));
				}
				else if(request.getParameter("type").equals("batch")){
					String result;
				    InputStream filecontent = null;
				    final Part filePart = request.getPart("_file");
				    filecontent = filePart.getInputStream();
				    BufferedReader br = new BufferedReader(new InputStreamReader(filecontent));
				    result = br.lines().collect(Collectors.joining("\n"));
				    ft = new FormatTool(request.getParameter("text"), result);
				    gate.sendSMSBatch(ft.getByFieldName("number"), ft.format());
				}
			} catch (Exception e) {
					e.printStackTrace();
				}		
			buffer = apiResponce;
			doGet(request, response);
			}
	}



