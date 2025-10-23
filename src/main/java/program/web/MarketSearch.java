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

@WebServlet(urlPatterns = {"/MarketBrowse","/MarketSearch"})
public class MarketSearch extends TopServlet{

    static Logger logger = LogManager.getLogger(MarketSearch.class);
    /**
     * The search engine for marketing.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String program_area_options = "";

	String sortby = "",username="", message="";
	String startDateFrom="", startDateTo="";
	String endDateFrom="", endDateTo="";
	Enumeration values = req.getParameterNames();
	String allCategory = "";
	String allPrograms = "";
	TypeList categories = null;
	boolean success = true;
	String name, value;
	out.println("<html>");

	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if(name.equals("username")){
		username = value;
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
	List<Type> allAds = null;
	List<Type> allAnnounces = null;
	List<Type> marketTypes = null;
	if(true){
	    categories = new TypeList(debug, "categories");
	    String back = categories.find();
	    if(allAds == null){
		AdList ones = new AdList(debug);
		back = ones.find();
		if(!back.equals("")){
		    message += " Could not retreive data ";
		    success = false;
		}
		else{
		    allAds = ones;
		}
	    }
	    if(allAnnounces == null){
		AnnounceList ones = new AnnounceList(debug);
		back = ones.find();
		if(!back.equals("")){
		    message += " Could not retreive data ";
		    success = false;
		}
		else{
		    allAnnounces = ones;
		}				
	    }
	    if(marketTypes == null){
		TypeList ones = new TypeList(debug, "marketing_types");
		back = ones.find();
		if(!back.equals("")){
		    message += " Could not retreive data ";
		    success = false;
		}
		else{
		    marketTypes = ones;
		}					
	    }
	}
	//
	// Browsing the records
	//
	out.println("<head><title>Search Marketing</title>");
	//
	// This script validate 
	//
	Helper.writeWebCss(out, url);
	out.println("</head><body><center>");
	Helper.writeTopMenu(out, url);
	//
	out.println("<h2>Marketing Search</h2>");
	if(!message.equals("")){
	    out.println(message+"<br />");
	}
	out.println("<form method=\"post\" name=\"myForm\" "+
		    " action=\""+url+"MarketTable\" >");
	out.println("<center><table border=\"1\" width=\"90%\">");
	out.println("<caption>Search Options</caption>");
	out.println("<tr><td align=\"right\"><label for=\"sortby\">");
	out.println("Sort by: </label></td><td align=\"left\">");
	out.println("<select name=\"sortBy\" id=\"sortby\">");
	out.println("<option value=\"m.id\">ID");
	out.println("</select></td></tr>");
	//
	// Year season
	out.println("<tr><td align=\"right\"><label for=\"year\">Year:");
	out.println("</label></td><td align=\"left\">");
	out.println("<select name=\"year\" id=\"year\">");
	int years[] = Helper.getPrevYears();
	out.println("<option value=\"\">All\n");		
	for(int yy:years){
	    out.println("<option>"+yy+"</option>");
	}		
	out.println("</select>");
	out.println("&nbsp;&nbsp;<label for=\"season\">Season:");
	out.println("</label>");
	out.println("<select name=\"season\" id=\"season\">");
	out.println("<option selected value=\"\">All\n"); // all
	out.println(Helper.allSeasons);
	out.println("</select></td></tr>");
	//
	// category, programs
	out.println("<tr><td align=\"right\">");
	out.println("<label for=\"cat_id\">Guide Heading: </label></td><td>");
	out.println("<select name=\"category_id\" id=\"cat_id\">");
	out.println("<option value=\"\">All</option>");
	if(categories != null){
	    for(Type cat:categories){
		if(cat.isActive())
		    out.println("<option value=\""+cat.getId()+"\">"+cat+"</option>");
	    }
	}
	out.println("</select>");
	out.println("</td></tr>");
	//
	// Program Ad 
	out.println("<tr><td align=\"right\"><label for=\"ads\">Market Ads:");
	out.println("</label></td><td align=\"left\">");
	out.println("<select name=\"market_ad\" id=\"ads\">");
	out.println("<option value=\"\">All</option>");
	for(Type one:allAds){
	    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	}
	out.println("</select></td></tr>");
	//
	out.println("<tr><td align=\"right\"><label for=\"anno\">Announcements: ");
	out.println("</label></td><td>");
	out.println("<select name=\"market_announce\" id=\"anno\">");
	out.println("<option value=\"\">All</option>");
	for(Type one:allAnnounces){
	    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	}
	out.println("</select></td></tr>");		
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"mtype\">Marketing Type: ");
	out.println("</label></td><td>");
	out.println("<select name=\"market_type\" id=\"mtype\">");
	out.println("<option value=\"\">All</option>");
	for(Type one:marketTypes){
	    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	}
	out.println("</select></td></tr>");		
	out.println("</td></tr>");
	//
	// Start Dates
	//
	out.println("<tr><td align=\"left\" colspan=\"2\">Note:The following due date applies to marketing ads or maketing pieces due dates</td></tr>");		
	out.println("<tr><td align=\"right\"><b>Due Date </b>");
	out.println("</td><td align=\"left\">");
	out.println("<label for=\"dfrom\">from: </label>");
	out.println("<input type=text name=\"date_from\" maxlength=\"10\" id=\"dfrom\" "+
		    "value=\"\" size=\"10\" />");
	out.println("<label for=\"dto\">To: </label>");
	out.println("<input type=text name=date_to maxlength=10 "+
		    "value=\"\" size=10 id=\"dto\" />");
	out.println("(mm/dd/yyyy)</td></tr>");
	// 
	out.println("<tr><td align=\"right\"><input type=\"submit\" value=\"Find\" "+
		    "></td></tr>");
	out.println("</table>");
	out.println("<br />");
	out.println("</form>");		
	out.println("</center>");
	out.println("</body></html>");
	out.close();

    }
    /**
     * The main class method.
     *
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	doGet(req, res);
    }

}






















































