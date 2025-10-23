package program.web;

import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
// import org.jasig.cas.client.authentication.AttributePrincipal;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/CasLogin"})
public class Login extends TopServlet{
    //
    String cookieName = ""; 
    String cookieValue = "";
    static Logger logger = LogManager.getLogger(Login.class);
	
    /**
     * Generates the login form for all users.
     *
     * @param req the request 
     * @param res the response
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	/**
	String username = "", ipAddress = "", message="", id="";
	boolean found = false;
	
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String source = "", action="", name="", value="";
	String userid = null;
	AttributePrincipal principal = null;				
	if (req.getUserPrincipal() != null) {
	    principal = (AttributePrincipal) req.getUserPrincipal();
	    userid = principal.getName();
	}
	if(userid == null || userid.isEmpty()){
	    userid = req.getRemoteUser();
	}
	String [] vals;
	Enumeration values = req.getParameterNames();		
	while (values.hasMoreElements()){

	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if(name.equals("source")){
		source = value;
	    }
	    if(name.equals("action")){
		action = value;
	    }
	    if(name.equals("id")){
		id = value;
	    }			
	}
	HttpSession session = null;
	if(userid != null){
	    session = req.getSession();			
	    // setCookie(req, res);
	    User user = getUser(userid);
	    if(user != null && session != null){
		session.setAttribute("user",user);
		String url2 = "";
		if(source.equals("")){
		    url2 = url+"IntroPage?";
		    if(!id.equals("")){
			url2 += "id="+id;
		    }
		}
		else{
		    url2 = url+source+"?action="+action+"&id="+id;
		}
		out.println("<head><title></title><META HTTP-EQUIV=\""+
			    "refresh\" CONTENT=\"0; URL=" + url2 +
			    "\"></head>");								
		out.println("<body>");
		out.println("</body>");
		out.println("</html>");
		out.flush();
		return;
	    }
	    else{
		message = " Unauthorized access";
	    }
	}
	else{
	    message += " You can not access this system, check with IT or try again later";
	}
	out.println("<head><title></title><body>");
	out.println("<p><font color=red>");
	out.println(message);
	out.println("</p>");
	out.println("</body>");
	out.println("</html>");
	out.flush();
	*/
    }
	
    void setCookie(HttpServletRequest req, 
		   HttpServletResponse res){ 
	Cookie cookie = null;
	boolean found = false;
	Cookie[] cookies = req.getCookies();
	if(cookies != null){
	    for(int i=0;i<cookies.length;i++){
		String name = cookies[i].getName();
		if(name.equals(cookieName)){
		    found = true;
		}
	    }
	}
	//
	// if not found create one with 0 time to live;
	//
	if(!found){
	    cookie = new Cookie(cookieName,cookieValue);
	    res.addCookie(cookie);
	}
    }
    /**
     * Procesesses the login and check for authontication.
     * 
     * @param username
     */		
    User getUser(String username){
				
	boolean success = true;
	User user = null;
	String fullName="",role="",dept="", message="";
	try{
	    User one = new User(debug, null, username);
	    String back = one.doSelect();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
	    }
	    else{
		user = one;
	    }
	}
	catch (Exception ex) {
	    success = false;
	    message += ex;
	    logger.error(ex);
	}
	return user;
    }

}






















































