package program.web;

import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import program.model.*;
import program.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(urlPatterns = {"/IntroPage"})
public class IntroPage extends TopServlet{

    static Logger logger = LogManager.getLogger(IntroPage.class);

    public void doPost(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	doPost(req, res);
    }
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	
	User user = null;
	HttpSession	session = req.getSession(false);
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	out.println("<html><head><title>Promt</title>");
	Helper.writeWebCss(out, url);
	out.println("</head><body>");
	Helper.writeTopMenu(out, url);
	out.println("<h3>Welcome to Promt </h3>");	
	out.println(" Select one of the options in the top menu. ");
	out.println("<ul>");
    	out.println("<li> To enter a new planning, select 'New Plan'</li>");
	out.println("<li> To search for existing plans, select 'Plans'</li>");
	out.println("<li> To enter a new program, you need to enter a plan first, then you can add a new program</li>");
	out.println("<li> To search for existing programs, select 'Programs'</li>");
	out.println("<li> To enter a new facility, select 'New Facility'</li>");
	out.println("<li> To search for existing facilities, select 'Facilities'</li>");
	out.println("<li> The market option will take you to the marketing search page.</li>");
	out.println("<li> To do code entry for the programs and sessions that need codes select");
	out.println("Code Entry.</li>");
	out.println("<li> The Report option will bring the reports menu.</li>");
	out.println("<li> Instructors option will take you to 'Search Instructors' page.</li>");
	out.println("<li> The Calendar option will present a calendar options to show programs and events in certain month.</li> ");
	out.println("<li> When done, logout from the system.</li>");
	out.println("</ul>");
	out.println("<img src=\""+url+"/images/promt_intro_image.jpg\" alt=\"You can do this\" />");
	out.println("</center>");
	out.println("</body>");
	out.println("</html>");
	out.close();
    }
}
