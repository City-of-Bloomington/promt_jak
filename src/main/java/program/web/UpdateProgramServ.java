package program.web;

import java.util.*;
import java.text.*;
import java.util.Date;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/UpdateProgram.do","/UpdateProgram"})
public class UpdateProgramServ extends TopServlet {

    static Logger logger = LogManager.getLogger(UpdateProgramServ.class);	
    /**
     * Generates time string
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
	res.setContentType("text/html");
	Enumeration values = req.getParameterNames();

	String name= "", market_id="";
	String value = "", message="";
	String action = "";
	HttpSession session = null;
	boolean success = true;
	String id = "";
	PrintWriter out = res.getWriter();
        String [] vals;
	session = req.getSession(false);
	User user = null;
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	control = new Control(debug, user);
	//
	Program prog = new Program(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("id")){
		if(value != null){
		    id = value;
		    prog.setId(value);
		}
	    }			
	    else if (name.equals("year")){
		if(value != null){								
		    prog.setYear(value);
		}
	    }
	    else if (name.equals("season")){
		if(value != null)								
		    prog.setSeason(value);
	    }
	    else if (name.equals("season2")){
		if(value != null)								
		    prog.setSeason2(value);
	    }						
	    else if(name.equals("action")){
		action = value;
	    }
	}
	if(user != null && user.isAdmin()){
	    if(!action.equals("")){
		String back = prog.updateSeasonYear();
		if(back.equals("")){
		    message = "Changed Successfully";
		}
		else{
		    message = "Error "+back;
		    success = false;
		}
	    }
	}
	if(true){
	    String back = prog.doSelect();
	    if(!back.equals("")){
		message = "Error "+back;
		success = false;
	    }
	}
	out.println("<html><head><title>Change Program Season & Year</title>");
	Helper.writeWebCss(out, url);
	out.println("</head><body>");
	out.println("<center>");
	Helper.writeTopMenu(out, url);
	out.println("<h3>Edit Marketing Ad</h3>");
	if(!message.equals("")){
	    out.println("<h4>"+message+"</h4>");
	}
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\">");
	out.println("<input type=\"hidden\" name=\"id\" value=\""+prog.getId()+"\" />");
	out.println("<table border=\"1\" width=\"90%\"><caption>Change Program Year/Season</caption>");
	out.println("<tr><th>Program ID:</th><td align=\"left\"><a href=\""+url+"Program.do?id="+prog.getId()+"\">"+prog.getId()+"</td></tr>");
	out.println("<tr><th>Program:</th><td align=\"left\"> "+prog.getTitle()+"</td></tr>");
	int[] years = Helper.getFutureYears();
	out.println("<tr><th><label for=\"year\">Program Year:</label></th><td align=\"left\">");
	out.println("<select name=\"year\" id=\"year\">");				
	for(int yy:years){
	    String selected = "";
	    if(prog.getYear().equals(""+yy))
		selected = "selected=\"selected\"";
	    out.println("<option "+selected+" value=\""+yy+"\">"+yy+"</option>");
	}
	out.println("</select></td></tr>");
	out.println("<tr><th><label for=\"searon\">Season:</label></th><td align=\"left\">");
	out.println("<select name=\"season\" id=\"season\">");				
	for(String str:Helper.seasonsArr){
	    String selected = "";
	    if(prog.getSeason().equals(str))
		selected = "selected=\"selected\"";
	    out.println("<option "+selected+" value=\""+str+"\">"+str+"</option>");
	}
	out.println("</select></td></tr>");
	out.println("<tr><th><label for=\"season2\">Season 2:</label></th><td align=\"left\">");
	out.println("<select name=\"season2\" id=\"season2\">");
	out.println("<option value=\"\">&nbsp;</option>");
	for(String str:Helper.seasonsArr){
	    String selected = "";
	    if(prog.getSeason2().equals(str))
		selected = "selected=\"selected\"";
	    out.println("<option "+selected+" value=\""+str+"\">"+str+"</option>");
	}
	out.println("</select></td></tr>");				
	out.println("<tr><th></th><td align=\"left\">");				
	out.println("<input type=\"submit\" "+
		    "name=\"action\" value=\"Change\" /></td></tr>");				
	out.println("</table>");
	out.println("</form>");		
	out.println("<br />");
	out.println("</center>");
	out.println("</body></html>");
	out.flush();
	out.close();

    }
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req, res);
    }

}






















































