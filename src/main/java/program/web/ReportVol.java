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

@WebServlet(urlPatterns = {"/ReportVol"})
public class ReportVol extends TopServlet{

    static Logger logger = LogManager.getLogger(ReportVol.class);
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
     * Generates the output page for the volunteer report.
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
	String allCategory = "";
	String username = "";
	Enumeration values = req.getParameterNames();
	String name, value;

	String id="",year="", season="", title="", category_id="", lead_id=""; 
	String addWhere = "", area_id="", nraccount="", sortby="", message="";
	Type lead = null;
	// ProgramList plist = new ProgramList(debug);
	VolShiftList shifts = new VolShiftList(debug);		
	// plist.setHasShifts();
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = Helper.escapeIt(req.getParameter(name).trim());

	    if(name.equals("season")){
		// plist.setSeason(value);
		shifts.setSeason(value);
		season = value;
	    }
	    else if(name.equals("year")){
		// plist.setYear(value);
		shifts.setYear(value);
		year = value;
	    }
	    else if(name.equals("sortby")){
		shifts.setSortby(value);
	    }
	    else if(name.equals("id")){  // progam id
		// plist.setId(value);
	    }
	    else if(name.equals("id2")){  // progam id
		// plist.setId(value);
	    }
	    else if(name.equals("category_id")){
		// plist.setCategory(value);
	    }
	    else if(name.equals("lead_id")){   // by zoom
		// plist.setLead(value);
		shifts.setLead_id(value);
		lead_id = value;
	    }
	    else if(name.equals("area_id")){   // by zoom
		// plist.setArea(value);
	    }
	    else if(name.equals("nraccount")){   // by zoom
		// plist.setNraccount(value);
		nraccount = value;
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
	out.println("</head><body>");	
	Helper.writeTopMenu(out, url);
	//
	// first count the number of programs for this (season, year)
	//
	String back = "";// plist.find();
	back += shifts.find();	
	if(!back.equals("")){
	    out.println("<h3> Error "+back+"</b3>");
	    out.println("</body></html>");
	    out.close();
	    return;
	}
	if(!lead_id.equals("")){ // we list these only when there is a lead
	    lead = new Type(debug, lead_id,"","leads");
	    back += lead.doSelect();
	}
	if(shifts.size() == 0){
	    // send no programs message and exit
	    Helper.sendMessage(out, "No Programs","No programs found, "+
			       "make sure you select the right year and season");
	}
	else{
	    if(lead == null)
		Helper.writeFirstPage(out,"Volunteer Report", year, season);
	    else
		Helper.writeFirstPage(out,"Volunteer Report", lead.getName(), year, season);
	    if(shifts.size() > 0){
		Helper.writeVolShifts(out, shifts, url, "Volunteer Shifts", true);
				
	    }
	}
	out.println("</body></html>");
	out.close();
    }

}























































