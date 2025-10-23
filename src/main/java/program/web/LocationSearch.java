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

@WebServlet(urlPatterns = {"/LocationBrowse","/LocationSearch"})
public class LocationSearch extends TopServlet{

    static Logger logger = LogManager.getLogger(LocationSearch.class);

    /**
     * The main class method doGet.
     *
     * Create an html page for the Facility form.
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
     * The main class method doPost.
     *
     * Create an html page for the planning form.
     * @param req request input parameters
     * @param res reponse output parameters
     * 
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	PrintWriter out;

	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name, value, action="";
	String id ="", name2="";
		
	//
	// reinitialize to blank
	//
	String message = "", finalMessage="";

	out.println("<html><head><title>City of Bloomington Parks and "+
		    "Recs</title>"); 
	boolean actionSet = false, success=true;
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
	LocationList locs = new LocationList(debug);
	List<Location> locations = null;
        String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("id")){
		locs.setId(value);
		id = value;
	    }
	    if(name.equals("facility_id")){
		locs.setFacility_id(value);
	    }						
	    else if(name.equals("name")){
		locs.setName(value);
		name2 = value;
	    }
	    else if(name.equals("active")){
		locs.setActive(value);
	    }						
	    else if(name.equals("action")){
		action = value;
	    }
	}

	//
       	if(!action.equals("")){
	    String back = locs.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		List<Location> ones = locs.getLocations();
		if(ones != null && ones.size() > 0){
		    locations = ones;
		}
	    }
	}
	//
	// This script validates textareas and facility
	//
	Helper.writeWebCss(out, url);
	out.println("</head><body>");
	//
	out.println("<center>");
	Helper.writeTopMenu(out, url);		
	out.println("<h2>Locations Search</h2>");
	if(!message.equals("")){
	    out.println(message);
	    out.println("<br />");
	}
	//
	out.println("<form name=\"myForm\" method=\"post\">");
	if(action.equals("")){
	    out.println("<p>Before you decide to add a new Location, Please make sure it is not already in the system by searching in this form</p>");
	}
	out.println("<p>Locations are used in programs and sessions</p>");
	out.println("<table width=\"90%\" border=\"1\">");
	out.println("<caption>Location Search</caption>");
	out.println("<tr><td align=\"right\"><label for=\"id\">Location ID: </label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"id\" "+
		    "value=\"\" maxlength=\"10\" size=\"10\" id=\"id\" /></td></tr>"); 
	out.println("<tr><td align=\"right\"><label for=\"name\" >Name: </label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"name\" id=\"name\" "+
		    "value=\""+name2+"\" maxlength=\"30\" size=\"30\" />*</td></tr>");
	out.println("<tr><td align=\"right\"><b>Active Status: </b></td><td align=\"left\">");
	String checked = locs.getActiveStatus().equals("All")?"checked=\"checked\"":"";
	out.println("<input type=\"radio\" name=\"activeStatus\" "+
		    "value=\"All\" "+checked+" id=\"sall\" /><label for=\"sall\">All </label>");
	checked = locs.getActiveStatus().equals("active")?"checked=\"checked\"":"";				
	out.println("<input type=\"radio\" name=\"activeStatus\" "+
		    "value=\"active\" "+checked+" id=\"cstatus\"/><label for=\"cstatus\">Active Only</label> ");				
	out.println("</td></tr>");
	//
	out.println("<td align=\"right\">");				
	out.println("<input type=\"submit\" "+
		    "name=\"action\" value=\"Search\" /></td>");
	out.println("<td align=\"right\">");			
	out.println("<input type=\"button\" onclick=\"document.location='"+url+"Location.do';\" value=\"New Location\" /></td>");		
	out.println("</tr></table>");
	out.println("<p>*<font size=\"-1\"> Notice:you can enter partial name. </font></p>");
	if(!action.equals("") && locations == null){
	    out.println("<h3> No match found </h3>");
	}
	else if(locations.size() > 0){
	    out.println("<b> Found "+locations.size()+" records </b>");
	    out.println("<table border=\"1\"><caption>Search Results</caption>");
	    out.println("<tr><th>ID</th>"+
			"<th>Name</th>"+
			"<th>Url</th>"+
			"<th>Related Facility</th>"+
			"<th>Active?</th></tr>");
	    for(Location one:locations){
		out.println("<tr>");
		out.println("<td><a href=\""+url+"Location.do?action=zoom&id="+one.getId()+"\">"+one.getId()+"</a></td>");
		out.println("<td>"+one.getName()+"</td>");
		out.println("<td>"+one.getLocation_url()+"&nbsp;</td>");								
		out.println("<td>"+one.getFacility_name()+"&nbsp;</td>");
		String active = one.isActive()?"Yes":"No";
		out.println("<td>"+active+"</td>");
		out.println("</tr>");
	    }
	    out.println("</table>");
	}
	out.println("<br />");
	out.print("</center></body></html>");
	out.close();
    }

}





































