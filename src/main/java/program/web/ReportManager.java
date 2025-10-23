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

@WebServlet(urlPatterns = {"/ReportManager"})
public class ReportManager extends TopServlet{

    static Logger logger = LogManager.getLogger(ReportManager.class);
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
     * Generates the output page for the managers report.
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
	Enumeration values = req.getParameterNames();
	String name, value;
	String id="",id2="",
	    year="", season="", title="", category_id="", lead_id=""; 
	String addWhere = "", area_id="", nraccount="", sortby="", sessionSort="";
	String category2_id="", message="";
	
	String[] progId = null;
	boolean success = true, oldVersion = false;

	//
	// loop through input
	//
	Type lead = null;
	ProgramList plist = new ProgramList(debug);
	GeneralList gens = new GeneralList(debug);
	FacilityList facils = new FacilityList(debug);
	VolShiftList shifts = new VolShiftList(debug);
	shifts.setNoPid(); // shifts not linked to programs only
	while (values.hasMoreElements()){

	    name = ((String)values.nextElement()).trim();
	    value = Helper.escapeIt(req.getParameter(name).trim());
	    if(name.equals("season")){
		plist.setSeason(value);
		gens.setSeason(value);
		season = value;
	    }
	    else if(name.equals("year")){
		plist.setYear(value);
		gens.setYear(value);
		year = value;
	    }
	    else if(name.equals("sortby")){
		plist.setSortby(value);
	    }
	    else if(name.equals("id")){  // progam id
		if(!value.equals("")){				
		    plist.setId(value);
		    id = value;
		}
	    }
	    else if(name.equals("category_id")){
		plist.setCategory_id(value);
	    }
	    else if(name.equals("lead_id")){   // by zoom
		plist.setLead_id(value);
		gens.setLead_id(value);
		facils.setLead_id(value);
		shifts.setLead_id(value);
		lead_id = value;
	    }
	    else if(name.equals("area_id")){   // by zoom
		plist.setArea_id(value);
	    }
	    else if(name.equals("nraccount")){   // by zoom
		plist.setNraccount(value);
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
		    "Recs</title>");
	Helper.writeWebCss(out, url);
	out.println("</head><body>");
	Helper.writeTopMenu(out, url);
	int progCount = 0;
	//
	// first count the number of programs for this (season, year)
	//
	String back = plist.find();
	back += gens.find();
	if(!lead_id.equals("")){ // we list these only when there is a lead
	    lead = new Type(debug, lead_id,"","leads");
	    back += lead.doSelect();
	}
	else{
	    back += facils.lookFor();
	}
	back += shifts.find();		
	if(!back.equals("")){
	    out.println("<h3> Error "+back+"</b3>");
	    out.println("</body></html>");
	    out.close();
	    return;
	}
	try{
	    if(plist.size() == 0 && facils.size() == 0 && gens.size() == 0){
		//
		Helper.sendMessage(out, "No Programs","No programs found, "+
				   "make sure you select the right year and season");
	    }
	    else{
		if(lead != null){
		    Helper.writeFirstPage(out,"Manager Report", lead.getName(), year, season);
		}
		else{
		    Helper.writeFirstPage(out,"Manager Report", year, season);
		}
		if(id.equals("")){
		    //
		    if(facils.size() > 0){
			Helper.writeFacilities(out,
					       facils,  
					       url);

		    }
		    if(gens.size() > 0){
			Helper.writeGenerals(out,
					     gens,
					     url,
					     "General Listings",
					     false);
		    }
		    if(shifts.size() > 0){
			out.println("<center><h3> Volunteer Shifts (With No Program link) </b3></center>");					
			Helper.writeVolShifts(out, shifts, url, "Volunteer Shifts",false);

		    }
		}
		out.println("<center><h3> Programs </b3></center>");
		for(Program prog:plist){
		    id = prog.getId();
		    // prog.doSelect();
		    Helper.writeProgram(out,
					prog,  
					true,true, // tasks, planInfo
					true, // transaction, revert acct
					url);
		    //
		    // write sessions
		    if(prog.hasSessions()){
			Helper.writeSessions(out,
					     prog.getSessions(),
					     prog.getSessionOpt(),
					     prog.getId(),
					     url,
					     debug);
		    }
		    if(prog.hasFiles()){
			Helper.printFiles(out, url, prog.getFiles());
		    }
		    if(prog.hasMarket()){
			Market market = prog.getMarket();
			Helper.writeMarket(out, market, url);
			if(market.hasFiles()){
			    Helper.printFiles(out, url, market.getFiles());
			}												
		    }					
		    //
		    // volunteer shifts
		    //
		    if(prog.hasShifts()){
			Helper.writeVolShifts(out,prog.getShifts(), url, "Volunteer Shifts",false);
		    }
		    //
		    // sponsors
		    List<Sponsor> slist = prog.getSponsors();
		    if(slist != null && slist.size() > 0)
			Helper.writeSponsors(out,slist, url, debug);
		    //
		    out.println("<br /><hr width=\"75%\"><br />");
		    out.flush();
		}
	    }
	}catch(Exception ex){
	    logger.error(ex);
	}
	//
	out.println("</body></html>");
	out.close();	

    }

}













































