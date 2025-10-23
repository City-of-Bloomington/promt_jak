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
    
@WebServlet(urlPatterns = {"/CodeBrowse","/CodeSearh"})
public class CodeSearch extends TopServlet{

    String allCategory = "";
    String allLead = "";
    String allArea = "";
    static Logger logger = LogManager.getLogger(CodeSearch.class);
    /**
     * The main class method doGet.
     *
     * Create an html page for the FacilityBrowse form.
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
	boolean success = true;
	Enumeration values = req.getParameterNames();

	String name, value;
	String season="",year="",nraccount="",lead_id="",area_id="",category_id="";
	String title="", message="";
	out.println("<html>");
	ProgramList programs = new ProgramList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if (name.equals("season")){
		season = value;
		programs.setSeason(value);
	    }
	    else if (name.equals("year")){
		year = value;
		programs.setYear(value);
	    }
	    else if (name.equals("nraccount")){
		nraccount = value;
	    }
	}

	User user = null;
	HttpSession	hsession = req.getSession(false);
	Control control = null;
	if(hsession != null){
	    user = (User)hsession.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	//
	LeadList leads = null;
	TypeList areas = null;
	TypeList locations = null;
	TypeList categories = null;
	if(true){
	    leads = new LeadList(debug);
	    if(!season.equals(""))
		leads.setSeason(season);
	    if(!year.equals(""))
		leads.setYear(year);			
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    areas = new TypeList(debug, "areas");
	    back = areas.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    categories = new TypeList(debug, "categories");
	    back = categories.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    locations = new TypeList(debug, "locations");
	    back = locations.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }			
	}				
	message = programs.findAbraviatedList();
	// Setting the browsing query
	//
	out.println("<html><head><title>Code Search</title>");
	Helper.writeWebCss(out, url);
	out.println("</head><body><center>");
	Helper.writeTopMenu(out, url);	
	out.println("<h2>Code Search</h2>");
	if(!message.equals(""))
	    out.println(message+"<br />");
	out.println("<form method=\"post\" action=\""+url+"CodeResult\" >");
	if(!year.equals("")){
	    out.println("<input type=\"hidden\" name=\"year\" value=\""+year+ 
			"\" />");
	}
	if(!season.equals("")){
	    out.println("<input type=\"hidden\" name=\"season\" value=\""+season+ 
			"\" />");
	}
	out.println("<center><table align=\"center\" border=\"1\" "+
		    "cellpadding=\"2\" cellspacing=\"1\">");
	out.println("<caption>Search Options</caption>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"right\"><label for=\"sortby\">Sort by</label></td>");
	out.println("<td><select name=\"sortby\" id=\"sortby\">");
	out.println("<option value=\"area\" selected>Area</option>");
	out.println("<option value=\"category\">Guide Headings</option>");
	out.println("<option value=\"program\">Program Title</option>");
	out.println("<option value=\"start_date\">Program Start Date</option>");
	out.println("</select></td></tr>");

	// category
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"right\"><b>Guide Heading</b>");
	out.println("</td><td align=\"left\">");
	out.println("<select name=\"category_id\">");
	out.println("<option value=\"\"></option>");
	if(categories != null){
	    for(Type one:categories){
		String selected = "";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}		
	out.println("</select></td></tr>");
	// area
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"right\"><label for=\"area_id\">");
	out.println(" Area</label></td><td align=\"left\">");
	out.println("<select name=\"area_id\" id=\"area_id\">");
	out.println("<option value=\"\"></option>");
	if(areas != null){
	    for(Type one:areas){
		out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}		
	out.println("</select>");
	out.println("</td></tr>");

	// lead
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"right\">"+
		    "<label for=\"lead_id\">Lead Programmer");
	out.println("</td></label><td align=\"left\">");
	out.println("<select name=\"lead_id\" id=\"lead_id\">");
	out.println("<option value=\"\"></option>");
	if(leads != null){
	    for(Lead one:leads){
		if(one.isActive())
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select>");
	out.println("</td></tr>");
	//
	// program
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"right\">"+
		    "<label for=\"prog_id\">Program</lable>");
	out.println("</td><td align=\"left\">");
	out.println("<select name=\"id\" id=\"prog_id\">");
	out.println("<option value=\"\"></option>");
	if(programs != null){
	    for(Program one:programs){
		out.println("<option value=\""+one.getId()+"\">"+one.getTitle()+"</option>");
	    }
	}
	out.println("</select>");
	out.println("</td></tr>");
	//
	// non-reverting account
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"right\"><label for=\"date_from\">Start Date, from "+
		    "</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"date_from\" id=\"date_from\" value=\""+
		    "\" size=\"10\" maxlength=\"10\" class=\"date\" /><label for=\"date_to\"> to</label> "); 
	out.println("<input type=\"text\" name=\"date_to\" id=\"date_to\" value=\""+
		    "\" size=\"10\" maxlength=\"10\" class=\"date\"/></td></tr>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"right\"><label for=\"account_id\">Non-reverting "+
		    "Account</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"nraccount\" id=\"account_id\" value=\""+nraccount+
		    "\" size=\"10\" maxlength=\"10\"></td></tr>");		
	out.println("</table>");
	//
	out.println("<input type=\"submit\" name=\"action\" "+
		    "value=\"Find\" />");
	out.println("</center>");
	out.println("</form>");
	Helper.writeWebFooter(out, url);
	out.println("</body></html>");
	out.close();

    }
    /**
     * The main class method.
     *
     * Create an html page for the facility browser form.
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






















































