package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entities.Utilisateur;

public class RestrictionFilter implements Filter {
    public static final String ACCES_ERROR     = "/connexion";
	public static final String ATT_SESSION_USER = "sessionUtilisateur";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        HttpSession session = request.getSession();

        if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
			Utilisateur u = (Utilisateur) session.getAttribute( ATT_SESSION_USER );
			if(u.getRole().equals("Admin"))
				chain.doFilter(request, response);
			else
	            response.sendRedirect( request.getContextPath() + ACCES_ERROR );
        }
        else
            response.sendRedirect( request.getContextPath() + ACCES_ERROR );

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub

	}

}
