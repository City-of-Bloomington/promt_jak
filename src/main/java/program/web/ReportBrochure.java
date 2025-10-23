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

@WebServlet(urlPatterns = {"/ReportBrochure"})
public class ReportBrochure extends TopServlet{

    String allCategory = "";
    static Logger logger = LogManager.getLogger(ReportBrochure.class);
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
     * Generates brochure report.
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
	String idList[] = null;
	
	Enumeration values = req.getParameterNames();
	String name, value;
	String id="",year="", season="", title="", category_id="", lead_id=""; 
	String addWhere = "", area_id="", nraccount="", sortby="", category2_id=""; 
	String message = "";
	boolean success = true;
	ProgramList plist = new ProgramList(debug);
	GeneralList gens = new GeneralList(debug);
	while (values.hasMoreElements()){

	    name = ((String)values.nextElement()).trim();
	    value = req.getParameter(name).trim();
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
	    else if(name.equals("id")){
		plist.setId(value);
		if(!value.equals(""))				
		    id = value;
	    }
	    else if(name.equals("id2")){
		plist.setId(value);
		if(!value.equals(""))
		    id = value;
	    }			
	    else if(name.equals("sortby")){
		plist.setSortby(value);
		sortby = value;
		System.err.println(" sortby "+sortby);
	    }
	    else if(name.equals("category_id")){
		plist.setCategory_id(value);
	    }
	    else if(name.equals("category2_id")){   
		category2_id = value;
	    }
	    else if(name.equals("lead_id")){   
		plist.setLead_id(value);
	    }
	    else if(name.equals("area_id")){   
		plist.setArea_id(value);
	    }
	    else if(name.equals("nraccount")){   
		plist.setNraccount(value);
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
	plist.forPublishOnly();
	message = plist.find();
	message += gens.find();
	out.println("<html>");
	out.println("<head><title>City of Bloomington Parks and "+
		    "Recreation</title>");
	Helper.writeWebCss(out, url);
	out.println("</head><body>");
	Helper.writeTopMenu(out, url);	
	if(!message.equals("")){
	    out.println("<b>Error "+message+"</b>");
	    out.println("<br /></body></html>");
	    out.close();
	    return;
	}
	if(plist.size() == 0 && gens.size() == 0){

	    Helper.sendMessage(out, "No Programs","No programs found, "+
			       "make sure you select the right year and season");
	}
	else{
	    // 
	    Helper.writeFirstPage(out,"Brochure Report",year, season);
	    if(id.equals("") && gens.size() > 0)
		Helper.writeGenerals(out, gens, url, "General Listings", true);
	    for(Program prog:plist){
		Helper.writeProgram(out, prog,
				    false,
				    false, // tasks, planInfo
				    true, // transaction, revert acct
				    url); 
		// write sessions
		if(prog.hasSessions()){
		    Helper.writeSessions(out,
					 prog.getSessions(),
					 prog.getSessionOpt(),
					 prog.getId(),
					 url,
					 debug);					
		}
		out.println("<br /><hr width=\"75%\"><br />");				
	    }
	}
	out.println("</body></html>");
	out.close();		
    }

}













































