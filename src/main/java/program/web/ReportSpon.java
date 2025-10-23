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

@WebServlet(urlPatterns = {"/ReportSpon"})
public class ReportSpon extends TopServlet{

    String allCategory = "";
    static Logger logger = LogManager.getLogger(ReportSpon.class);


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
	String id="",year="", season="", title="", category_id="", lead_id=""; 
	String addWhere = "", area_id="", nraccount="", sortby="", message="";
	boolean success = true;

	ProgramList plist = new ProgramList(debug);
	plist.setHasSponsor();
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = Helper.escapeIt(req.getParameter(name).trim());
	    if(name.equals("season")){
		season = value;
		plist.setSeason(value);
	    }
	    else if(name.equals("year")){
		year = value;
		plist.setYear(value);
	    }
	    else if(name.equals("id")){  // progam id
		plist.setId(value);
	    }
	    else if(name.equals("id2")){  // progam id
		plist.setId(value);
	    }
	    else if(name.equals("category_id")){   // by zoom
		plist.setCategory_id(value);
	    }
	    else if(name.equals("lead_id")){   // by zoom
		plist.setLead_id(value);
	    }
	    else if(name.equals("area_id")){   // by zoom
		plist.setArea_id(value);
	    }
	    else if(name.equals("sortby")){   // by zoom
		sortby = value;
	    }
	    else if(name.equals("nraccount")){   // by zoom
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
	out.println("<html>");
	out.println("<head><title>City of Bloomington Parks and "+
		    "Recreation</title>");
	Helper.writeWebCss(out, url);
	out.println("</head><body><center>");	
	Helper.writeTopMenu(out, url);
	String back = plist.find();
	if(!back.equals("")){
	    message += back;
	}
	//
	if(plist.size() == 0){
	    //
	    if(!message.equals("")){
		out.println("<h3>Error "+message+"</h3>");
	    }
	    // send no programs found message and exit
	    Helper.sendMessage(out, "No Programs","No programs found, "+
			       "make sure you select the right year and season");
	}
	else{
	    Helper.writeFirstPage(out,"Sponsorships Report", year, season);
	    //
	    for(Program prog:plist){
		id = prog.getId();
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
		if(prog.hasMarket()){
		    Market market = prog.getMarket();
		    Helper.writeMarket(out, market, url);
		}				
		List<Sponsor> slist = prog.getSponsors();
		if(slist != null && slist.size() > 0)
		    Helper.writeSponsors(out,slist, url, debug);
		//
		out.println("<hr width='75%'>");
	    }			
	}
	out.println("<ceter></body></html>");
	out.close();
    }
    //
    /**
     * Writes pairs of item title in  table cells.
     * @param out 
     * @param that the item
     * @title the item title
     */
    void writeItem(PrintWriter out, String that, String title){

	String title2 = title;
	String that2 = that;
	if(title.indexOf("$") > 0){
	    title2 = title.substring(0,title.indexOf("$")-1);
	    that2 = "$"+that;
	}
	out.println("<tr><td align=\"right\" valign=\"top\"><b>");
	out.println(title2+":&nbsp;</b></td><td valign=\"bottom\">");
	out.println(that2+"</td></tr>");
    }

}























































