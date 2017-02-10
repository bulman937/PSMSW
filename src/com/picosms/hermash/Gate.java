package com.picosms.hermash;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Gate
 */
@WebServlet("/gate")
public class Gate extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
	private SMSGate gate;
    private String buffer;
	public Gate() {
        gate = new SMSGate(new Auth("380939ХХХХХХ", "smspass937"));
        buffer = "";
    }

    /**
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String apiResponce = "";
			if(request.getParameter("type").equals("single")) {
				try {
					apiResponce = gate.sendSMS(request.getParameter("tel"), 
											   request.getParameter("text"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(request.getParameter("tel"));
			System.out.println(request.getParameter("text"));
			System.out.println(request.getParameter("type"));		
			buffer = apiResponce;
			doGet(request, response);
	}
}

