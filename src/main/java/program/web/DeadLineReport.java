package program.web;

import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/DeadLineReport"})
public class DeadLineReport extends TopServlet{

    static Logger logger = LogManager.getLogger(DeadLineReport.class);	
    /**
     * The main class method.
     *
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	doPost(req, res);
    }
    //
    // Mr Post
    /**
     * Create an html page for the program search engine form.
     * processes the request from the search engine page and presents 
     * the matching results as html in a table
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	PrintWriter os;
	String sortby = "";
	boolean connectDbOk = false;
	String[] titles = {"ID", 
	    "Reg. DeadLine",
	    "Lead Programmer",
	    "Title ", 
	    "Season ", 
	    "Year ",
	    "Code ",
	    "Start Date "
	};      // 35

	boolean show[] = { true,true,true,true,true,true,true,true
	};
	String sessTitles[] = {"ID",
	    "Code",
	    "Reg. Deadline",
	    "Start Date",
	    "End Date"
	};
	boolean sessShow[] = { true,true,true,true,true};

	boolean showAll = true;  
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	os = res.getWriter();
	int maxRecords = 100, minRecords = 0, row,incr=100;
	Enumeration values = req.getParameterNames();
	String name, value, wt="table";
	boolean success = true;
	String that="", id="", message=""; 
	String season="", year=""; 
	boolean showNext=false;
	os.println("<html>");
	ProgramList pl = new ProgramList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if (name.equals("id")){
		id = value;
		pl.setId(value);
	    }
	    else if (name.equals("lead_id")){
		pl.setLead_id(value);
	    }
	    else if (name.equals("year")){
		pl.setYear(value);
		year = value ;
	    }
	    else if (name.equals("season")){
		pl.setSeason(value);
		season = value;
	    }
	    else if (name.equals("category_id")){
		pl.setCategory_id(value);
	    }
	    else if(name.equals("area_id")){
		pl.setArea_id(value);
	    }
	}

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
	//
	String today = Helper.getToday2();
	pl.setDeadLineFrom(today);
	os.println("<head><title>Deadline Report " + 
		   "</title>");
	Helper.writeWebCss(os, url);
	os.println("</head><body><center>");
	Helper.writeTopMenu(os, url);		
	message = pl.find();
	if(!message.equals("")){
	    os.println(message+"<br />");
	}
	if(pl.size() == 0){
	    os.println("<h3> No record found </h3>");
	}
	else if(pl.size() > 0){
	    os.println("<font size=\"+1\">Park and Recreation <br />");
	    os.println("Registration Deadline  Report "+
		       "("+season+"/"+year+")</font>"+
		       "<br />");
	    os.println("Total records :"+ pl.size() + "<br />");
	    os.println("<hr width=\"75%\" /><br />");
				
	    for(Program prog:pl){
				
		os.println("<table width=\"100%\">");
		os.println("<caption>Deadline report </caption>");
		os.println("<tr><td colspan=\"2\" align=\"center\">");
		os.println(prog.getTitle());
		os.println("</td></tr>");
		that = prog.getId(); 
		String str = "<a href=\""+url+"Program.do?id="+that+ 
		    "&action=zoom\">"+
		    that+"</a>";
		Helper.writeItem(os,str,"ID");
		str = prog.getRegDeadLine();
		Helper.writeItem(os,str,"Dead Line Date");
		Type leader = prog.getLead();
		if(leader != null){
		    Helper.writeItem(os, leader.getName(),"Lead Programmer");
		}
		str = prog.getStartDate();
		if(!str.equals("")){
		    Helper.writeItem(os,str,"Start Date");
		}
		if(prog.hasSessions()){
		    os.println("<tr><td colspan=\"2\">");
		    Helper.writeSessions(os, prog.getSessions(), prog.getSessionOpt(), prog.getId(), url, debug);
		    os.println("</td></tr>");
		}
		os.println("</table>");
		os.println("<br /><hr width=\"75%\" /><br />");
	    }
	}
	//
	os.println("</center></body>");
	os.println("</html>");
	os.flush();
	os.close();
    }

}






















































