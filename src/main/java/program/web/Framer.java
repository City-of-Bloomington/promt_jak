package program.web;
import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;

@WebServlet(urlPatterns = {"/Framer"})
public class Framer extends TopServlet{

    static Logger logger = LogManager.getLogger(Framer.class);

    /**
     * Generates the main interface
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	PrintWriter os = null;
	//
	Enumeration values = req.getParameterNames();
	String name, value, id="";
	User user = null;
	HttpSession	session = req.getSession();
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	//
	// check for the user
	//
	out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 "+
		    "Transitional//EN\"\"http://www.w3.org/TR/REC-html40/loose.dtd>");
	out.println("<!--NewPage-->");
	out.println("<HTML><HEAD><TITLE>User Menu </TITLE>");
	out.println("</HEAD>");
	out.println("<FRAMESET cols=\"13%,87%\" border=0>");
	out.println("<FRAME src=\""+url+"UserMenu?id="+id+
		    "\" name=leftFrame>");
	out.println("<FRAME src=\""+url+
		    "intro.html\" name=rightFrame>");
	//
	out.println("</FRAMESET>");
	out.println("<NOFRAMES>");
	out.println("<H2> Frame Alert</H2>");
	out.println("<P>This document is designed to be viewed using "+
		    " the frames feature. If you see this message, you "+
		    "are using a non-frame-capable web client.<BR>"+
		    "You need to use a different browser or upgrade to a"+
		    "New one. ITS<br>");
	//
	out.println("</HTML>");
	out.close();
    }				   
    /**
     * @link #doGet
     * @see #doGet
     */		  
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	doGet(req, res);
    }

}

