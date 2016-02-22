package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entities.Utilisateur;
import forms.ConnexionForm;

public class Connexion extends HttpServlet {
	public static final String ATT_USER = "utilisateur";
	public static final String ATT_FORM = "form";
	public static final String ATT_SESSION_USER = "sessionUtilisateur";
	public static final String VUE = "/WEB-INF/connexion.jsp";
	public static final String URL_REDIRECTION_INSCRIPTION = "/JEE/inscription";
	public static final String URL_REDIRECTION_ADMIN = "/JEE/admin";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException { /* Affichage de la page de connexion */
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("inscription") != null) {
			response.sendRedirect(URL_REDIRECTION_INSCRIPTION);
		} else {
			ConnexionForm form = new ConnexionForm();

			Utilisateur utilisateur = form.connecterUtilisateur(request);

			HttpSession session = request.getSession();

			if (form.getErreurs().isEmpty()) {
				session.setAttribute(ATT_SESSION_USER, utilisateur);
			} else {
				session.setAttribute(ATT_SESSION_USER, null);
			}

			request.setAttribute(ATT_FORM, form);
			request.setAttribute(ATT_USER, utilisateur);

			if(utilisateur.getRole().equals("Admin")){
				response.sendRedirect(URL_REDIRECTION_ADMIN);
			}
			else{
				this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
			}
		}
	}
}