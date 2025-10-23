package program.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
    
/**
 *
 * used during testing phase only
 */
// @WebServlet(urlPatterns = {"/Login"}, loadOnStartup = 1)
public class AdLogin extends TopServlet{

    boolean available = true;
    static Logger logger = LogManager.getLogger(AdLogin.class);
	
    /**
     * Generate an html page for login form.
     *
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();

	out.println("<HTML><HEAD><TITLE>Promt User Login</TITLE>");
	out.println("</HEAD><body>");
	out.println("<br><br>");
	out.println("<center><h2>Log In to Promt </h2>");
	out.println("<form name=\"userform\" method=\"post\"> ");
	out.println("<table border=0>");
	out.println("<tr><td>Username</td><td><INPUT NAME=\"username\" type=\"text\" size=\"10\" /></td><tr>");
	out.println("<tr><td></td><td>");
	out.println("<input type=\"submit\" name=\"action\" value=\"Submit\" />");
	out.println("</td></tr>");
	out.println("</table></FORM> ");
	out.println("</BODY></HTML>");
	out.close();
    }									

    /**
     * Handles the process of loging in the system.
     * 
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */			
    public void doPost(HttpServletRequest req, HttpServletResponse res) 
	throws ServletException, IOException {
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	PrintWriter os = res.getWriter();
	String username = "", message = "", role="", admin_pass="";
	HttpSession session = null;
	session = req.getSession(true);
	boolean success = true;
	Enumeration values = req.getParameterNames();
	String name, value;
	os.println("<html>");
	//
	while (values.hasMoreElements()) {
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if (name.equals("username")) {
		username = value.toLowerCase();
	    }
	}
	try {
	    int cc = 0;
	    User user = null;
	    if(true){
		User user2 = new User(debug, null, username);
		message = user2.doSelect();
		if(!message.equals("")){
		    success = false;
		}
		else{
		    user = user2;
		}
	    }
	    //
	    if(user == null){
		os.println("<head><title>" + 
			   "</title></head>");
		os.println("<body>");
		// 
		// if the user does not exist 
		//
		if(!message.equals(""))
		    os.println(message);
		os.println("Login to Programs failed.<p>"); 
		os.println("If you believe that you received this "+
			   "message because you<br>"); 
		os.println("misstyped your username or password, hit "+
			   "BACK and re-try.<br>");
		os.println("If this is your first time running the "+
			   "program, you will want to ask the system "+
			   "administrator to add your user name to <br>");
		os.println("to the list of authorized users. Or contact "+
			   "the ITS department for further information.<br>");
		os.println("</body>");
		os.println("</html>");
	    }
	    else {
		if(session != null){
		    session.setAttribute("user",user);
		}
		os.println("<head><title></title><META HTTP-EQUIV=\""+
			   "refresh\" CONTENT=\"0; URL=" + url +
			   "Framer?"+
			   "\"></head>");
								
		os.println("<body>");
		os.println("</body>");
		os.println("</html>");
	    }
	}
	catch (Exception ex) {
	    os.println(ex);
	    logger.error(ex);
	}
	os.flush();
    }

}

















































