package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entities.Utilisateur;

public class Admin extends HttpServlet{
	public static final String VUE = "/WEB-INF/admin.jsp";
	public static final String ATT_SESSION_USER = "sessionUtilisateur";
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
        HttpSession session = request.getSession();
		if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
			Utilisateur u = (Utilisateur) session.getAttribute( ATT_SESSION_USER );
			if(u.getRole().equals("Admin"))
				this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
        }
		
	}
}
