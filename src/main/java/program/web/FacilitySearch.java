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

@WebServlet(urlPatterns = {"/FacilityBrowse","/FacilitySearch"})
public class FacilitySearch extends TopServlet{

    static Logger logger = LogManager.getLogger(FacilitySearch.class);
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

	PrintWriter out;

	String username="";
	String allFacility = "";
	boolean success = true;
	out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name, value, message="", lead_id="";
	out.println("<html><head><title>Facility Search</title>");
	Helper.writeWebCss(out, url);
	out.println("</head><body>");
	Helper.writeTopMenu(out, url);		
	FacilityList facils = null;
	allFacility = "";
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if (name.equals("lead_id")){
		lead_id = value;
	    }
	}
	LeadList leads = null;
	if(true){
	    leads = new LeadList(debug);
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    facils = new FacilityList(debug);
	    back = facils.lookFor();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }			
	}				
	//
	// get the options for the select widgets
	//
	out.println("<center><h2>Facilities Search</h2>");
	out.println("<form method=\"post\" action=\""+url+"FacilityTable\">");
	out.println("<table border=\"1\" width=\"80%\">");
	out.println("<caption>Search Options</caption>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td>");
	out.println("<table width=\"100%\">");
	out.println("<tr><td align=\"right\">");
	out.println("<label for=\"sort_id\">Sort by </label></td><td align=\"left\">");
	out.println("<select name=\"sortby\" id=\"sort_id\">");
	out.println("<option selected value=\"id\">ID</option>");
	out.println("<option value=\"facility\">Facility</option>");
	out.println("</select></td></tr>");
	out.println("<tr bgcolor=#CDC9A3><td align=\"right\"><label for=\"min_id\">Show "+
		    "Records From </label>"+
		    "</td><td>");
	out.println("<input type=\"text\" name=\"minRecords\" value=\"0\" size=\"6\"  id=\"min_id\"/><strong>"
		    +"<label for=\"max_id\"> To </label>");
	out.println("<input type=\"text\" name=\"maxRecords\" value=\"100\" size=\"6\" id=\"max_id\" //></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"lead_id\">Lead "+
		    "</label></td><td>");
	out.println("<select name=\"lead_id\" id=\"lead_id\">");
	out.println("<option value=\"\">All</option>");
	if(leads != null){
	    for(Lead one:leads){
		String selected = one.getId().equals(lead_id)?"selected=\"selected\"":""; 
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select></td></tr>");				
	out.println("<tr><td align=\"right\"><label for=\"name_id\">Facility Name "+
		    "</label></td><td>");
	out.println("<select name=\"id\" id=\"name_id\">");
	out.println("<option value=\"\">All</option>");
	if(facils != null && facils.size() > 0){
	    for(Facility one:facils){
		out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"type_id\">Type "+
		    "</label></td><td>");
	out.println("<select name=\"type\" id=\"type_id\">");
	out.println("<option value=\"\">All</option>");
	for(String str:Facility.facility_types){
	    out.println("<option value=\""+str+"\">"+str+"</option>");
	}
	out.println("</select></td></tr>");
	out.println("<tr><td align=\"left\">");
	out.println("<input type=\"submit\" value=\"Search\" /></td>");
	out.println("<td align=\"right\">");
	out.println("<input type=\"button\" onclick=\"window.location='"+url+"Facility'\" value=\"New Facility\" /></td>");	
	out.println("</td></tr></table>");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</center>");
	out.println("</form>");
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






















































