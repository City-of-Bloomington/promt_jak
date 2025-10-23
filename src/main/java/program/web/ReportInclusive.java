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

@WebServlet(urlPatterns = {"/ReportInclusive"})
public class ReportInclusive extends TopServlet{

    String allCategory = "";
    static Logger logger = LogManager.getLogger(ReportInclusive.class);

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
    /**
     * Generates the output page for the sponsor's report.
     *
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String sid = "1";
	String username = "";
	Enumeration values = req.getParameterNames();
	String name, value;
	String id="",year="", season="", title="", 
	    category_id="", lead_id=""; 
	String addWhere = "", area_id="", nraccount="", sortby="", message="";
	boolean success = true;
	InclusionList ilist = new InclusionList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = Helper.escapeIt(req.getParameter(name).trim());
	    if(name.equals("season")){
		ilist.setSeason(value);
		season = value;
	    }
	    else if(name.equals("year")){
		ilist.setYear(value);
		year = value;
	    }
	    else if(name.equals("id")){  // progam id
		ilist.setId(value);
		id = value;
	    }
	    else if(name.equals("id2")){  // progam id
		ilist.setId(value);
		id = value;
	    }
	    else if(name.equals("category_id")){ 
		ilist.setCategory_id(value);
	    }
	    else if(name.equals("lead_id")){   
		ilist.setLead_id(value);
	    }
	    else if(name.equals("area_id")){   
		ilist.setArea_id(value);
	    }
	    else if(name.equals("sortby")){ 
		ilist.setSortby(value);
	    }
	    else if(name.equals("nraccount")){  
		ilist.setNraccount(value);
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
	out.println("<html>");
	out.println("<head><title>City of Bloomington Parks and "+
		    "Recreation</title>");
	Helper.writeWebCss(out, url);
	out.println("</head><body><center>");	
	Helper.writeTopMenu(out, url);	
	message = ilist.find();

	//
	if(ilist.size() == 0){
	    //
	    // send no programs found message and exit
	    Helper.sendMessage(out, "No Programs","No programs found, "+
			       "make sure you select the right year and season");
	}
	else{
	    Helper.writeFirstPage(out,"Inclusion Recreation Report", year, season);
	    for(Inclusion one:ilist){
		Program prog = one.getProgram();
		Helper.writeProgram(out,
				    prog,  
				    false,false, // tasks, planInfo
				    false, // transaction, revert acct
				    url);
		//
		// write sessions
		if(prog.hasSessions()){
		    Helper.writeSessions(out, prog.getSessions(), prog.getSessionOpt(), prog.getId(), url, debug);
		}
		//
		// volunteer shifts
		if(prog.hasShifts()){
		    Helper.writeVolShifts(out, prog.getShifts(), url,"Volunteer Shifts", false);
		}
		//
		// inclusive
		//
		out.println("<hr width=\"75%\" />");
	    }
	}
	out.println("</center>");
	out.println("</body></html>");
	out.close();
    }
}























































