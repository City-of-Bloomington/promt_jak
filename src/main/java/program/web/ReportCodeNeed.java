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

@WebServlet(urlPatterns = {"/ReportCodeNeed"})
public class ReportCodeNeed extends TopServlet{

    static Logger logger = LogManager.getLogger(ReportCodeNeed.class);
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
     * Create a session report in html format.
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
	String allCategory = "";
	String username = "";

	String idList[] = null;
	
	boolean success = true;
	Enumeration values = req.getParameterNames();
	String name, value;
	String id="",year="", season="", title="", category_id="", lead_id=""; 
	String addWhere="", area_id="", nraccount="", sortby="", unsetCode="",
	    sessionSort="", message="";

	ProgramList plist = new ProgramList(debug);
	GeneralList glist = new GeneralList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = Helper.escapeIt(req.getParameter(name).trim());
	    if(name.equals("season")){
		plist.setSeason(value);
		glist.setSeason(value);
		season = value;
	    }
	    else if(name.equals("year")){
		plist.setYear(value);
		glist.setYear(value);
		year = value;
	    }
	    else if(name.equals("sortby")){
		plist.setSortby(value);
	    }
	    else if(name.equals("id")){  // progam id
		plist.setId(value);
	    }
	    else if(name.equals("id2")){  // progam id
		plist.setId(value);
	    }
	    else if(name.equals("category_id")){
		plist.setCategory_id(value);
	    }
	    else if(name.equals("lead_id")){   // by zoom
		plist.setLead_id(value);
	    }
	    else if(name.equals("area_id")){   // by zoom
		plist.setArea_id(value);
	    }
	    else if(name.equals("nraccount")){   // by zoom
		plist.setNraccount(value);
	    }
	    else if(name.equals("unsetCode")){
		plist.setCodeNeed("y");
		glist.setNeedCode();
		unsetCode = value;
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
		    "Recs</title>");
	Helper.writeWebCss(out, url);
	out.println("</head><body><center>");
	Helper.writeTopMenu(out, url);	
	//
	// first count the number of programs for this (season, year)
	//
	String back = plist.find();
	if(back.isEmpty()){
	    back = glist.find();
	}
	if(!back.isEmpty()){
	    out.println("<h3> Error "+back+"</b3>");
	    out.println("</center></body></html>");
	    out.close();
	    return;
	}
	try{
	    if(plist.size() == 0 && glist.size() == 0){
		//
		Helper.sendMessage(out, "No Programs","No programs found, "+
				   "make sure you select the right year and season");
	    }
	    else{
		if(plist.size() > 0){
		    Helper.writeFirstPage(out,"Code Report", year, season);
		    //
		    for(Program prog:plist){
			Helper.writeProgram(out, prog, false, false, true, url);
			if(prog.hasSessions()){
			    Helper.writeSessions(out, prog.getSessions(), prog.getSessionOpt(), prog.getId(), url, debug);
			}
			out.println("<br /><hr width=\"75%\" /><br />");
		    }
		}
		if(glist.size() > 0){
		    Helper.writeGenerals(out, glist, url, "General Listings", false);
		}
	    }
	}
	catch(Exception ex){
	    out.println(ex);
	    logger.error(ex);
	}
	out.println("</center></body></html>");
	out.close();
		
    }

}













































