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

@WebServlet(urlPatterns = {"/Location.do","/Location"})
public class LocationServ extends TopServlet{

    static Logger logger = LogManager.getLogger(LocationServ.class);

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
	String id ="";
	List<Facility> facilities = null;
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
	Location loc = new Location(debug);
        String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("id")){
		loc.setId(value);
		id = value;
	    }
	    else if(name.equals("name")){
		loc.setName(value);
	    }
	    else if(name.equals("location_url")){
		loc.setLocation_url(value);
	    }						
	    else if(name.equals("active")){
		loc.setActive(value);
	    }
	    else if(name.equals("facility_id")){
		loc.setFacility_id(value);
	    }						
	    else if(name.equals("action")){
		action = value;
	    }
	}
	//
       	if(action.equals("Delete")){
	    if(user.canDelete()){
		String back = loc.doDelete();
		if(back.equals("")){
		    id="";
		}
		else{
		    message += back;
		    success = false;
		}
	    }
	    else{
		message = "You can not delete ";
		success = false;
	    }
	}
	else if(action.equals("Update")){
	    if(user.canEdit()){
		String back = loc.doUpdate();
		if(back.equals("")){
		    message = "Record updated successfully";
		}
		else{
		    message += back ;
		    success = false;
		}
	    }
	    else{
		message = "You can not update ";
		success = false;
	    }			
	}
	else if(action.equals("Save")){
	    //
	    if(user.canEdit()){
		String back = loc.doSave();
		if(back.equals("")){
		    id = loc.getId();
		    message = "Saved successfully";
		}
		else{
		    message += back ;
		    success = false;
		}
	    }
	    else{
		message = "You can not update ";
		success = false;
	    }						
	}
	else if(!id.equals("")){
	    String back = loc.doSelect();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}
	if(facilities == null){
	    FacilityList fl = new FacilityList(debug);
	    String back = fl.find();
	    if(back.equals("")){
		facilities = fl;
	    }
	}
		
	//
	Helper.writeWebCss(out, url);
	out.println("</head><body>");
	//
	out.println("<center>");
	Helper.writeTopMenu(out, url);		
	if(id.equals("")){
	    out.println("<h2>New Location</h2>");
	}
	else{
	    out.println("<h2>Edit Location "+id+"</h2>");
	}
	if(!message.equals("")){
	    out.println(message);
	    out.println("<br />");
	}
	//
	out.println("<form name=\"myForm\" method=\"post\" onsubmit=\"return "+
		    "validateForm()\">");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	}
	// Plan
	out.println("<table width=\"90%\" border=\"1\">");
	out.println("<caption>Location Info</caption>");
	out.println("<tr><td align=\"right\"><label for=\"name\">Name: </label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"name\" id=\"name\" "+
		    "value=\""+loc.getName()+"\" maxlength=\"90\" size=\"60\" /></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"url\">Location URL: </label></td><td align=\"left\">");				
	out.println("<input type=\"text\" name=\"location_url\" id=\"url\" "+
		    "value=\""+loc.getLocation_url()+"\" maxlength=\"150\" size=\"90\" /></td></tr>");
				
	out.println("<tr><td align=\"right\"><label for=\"f_id\">Related Facility: </label></td><td align=\"left\">");
	out.println("<select name=\"facility_id\" id=\"f_id\">");
	out.println("<option value=\"\"></option>");
	String selected = "";
	if(facilities != null){
	    for(Facility one:facilities){
		if(loc.getFacility_id().equals(one.getId()))
		    selected="selected=\"selected\"";
		out.println("<option value=\""+one.getId()+"\" "+selected+">"+one+"</option>");
	    }
	}
	out.println("</td></tr>");				
	String checked = loc.isActive()?"checked=\"checked\"":"";
	out.println("<tr><td align=\"right\"><label for=\"active\">Is Active?: </label></td><td align=\"left\">");				
	out.println("<input type=\"checkbox\" name=\"active\" "+
		    "value=\"y\" "+checked+" id=\"active\" />Yes</td></tr>");				

	if(!id.equals("")){
	    //
	    // if no program yet (it is new plan)
	    // can be duplicated only when a program is linked to it
	    //
	    out.println("<tr>");	    
	    out.println("<td>");				
	    out.println("<input type=\"submit\" "+
			"name=\"action\" value=\"Update\" /></td>");
	    if(user.canDelete()){
		out.println("<td valign=\"top\" align=\"right\">");				
		out.println("<form name=\"myForm2\" method=\"post\" "+
			    "onSubmit=\"return validateForm2()\">");
		out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\" />");
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Delete\" />&nbsp;");
		out.println("</form></td>");
	    }
	    out.println("</tr>");
	    out.println("</table>");
	    out.println("</form>");
	}
	else { // delete startNew
	    out.println("<tr><td>");
	    if(user.canEdit()){
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Save\" />");
	    }
	    out.println("</td></tr>");
	    out.println("</table>");
	    out.println("</form>");
	}
	if(!id.equals("")){
	    ProgramList plist = new ProgramList(debug);
	    plist.setLocation_id(id);
	    plist.setSortby("p.id desc ");
	    plist.setLimit("10");
	    String back = plist.findAbraviatedList();
	    if(plist.size() == 0){
		out.println("No program linked to this location");
	    }
	    if(plist.size() > 0){
		out.println("<table border=\"1\" width=\"90%\"><caption>Latest Programs & Sessions Linked to This Location</caption>");
		out.println("<tr><th>ID</th><th>Title</th><th>Year/Season</tr>");
		for(Program pp:plist){
		    out.println("<tr>");
		    out.println("<td><a href=\""+url+"Program.do?action=zoom&id="+pp.getId()+"\">"+pp.getId()+"</td>");
		    out.println("<td>"+pp.getTitle()+"</td>");
		    out.println("<td>"+pp.getYear()+"/"+pp.getSeason()+"</td>");
		    out.println("</tr>");
		}
	    }
	}
	out.println("<br />");
	out.print("</center></body></html>");
	out.close();
    }

}





































