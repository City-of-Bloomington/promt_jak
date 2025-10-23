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

@WebServlet(urlPatterns = {"/Sponsor.do","/Sponsor"})
public class SponsorServ extends TopServlet{

    static Logger logger = LogManager.getLogger(SponsorServ.class);
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
     * Generates and processes the sponsor form.
     * Handls view, add, update, delete operations on this form.
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
	String id = "", pid = "";
	PrintWriter os;
	Enumeration values = req.getParameterNames();
	String name, value;

	String pyear="", season=""; 
	String attendCount="",market="",market2="", monetary="",tangible="",
	    services="", signage="",exhibitSpace="",tshirt="", 
	    comments="", message="", ptitle="", lead="";

	boolean success = true, firstTime = true;
	String action="";
	Sponsor spon = new Sponsor(debug);
	String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if(name.equals("id")){
		id = value;
		spon.setId(value);
	    }
	    else if(name.equals("pid")){ // prog id
		pid = value;
		spon.setPid(value);
	    }
	    else if(name.equals("attendCount")){
		spon.setAttendCount(value);
	    }
	    else if(name.equals("market")){
		spon.setMarket(value);
	    }
	    else if(name.equals("monetary")){
		spon.setMonetary(value);
	    }
	    else if(name.equals("tangible")){
		spon.setTangible(value);
	    }
	    else if(name.equals("services")){
		spon.setServices(value);
	    }
	    else if(name.equals("signage")){
		spon.setSignage(value);
	    }
	    else if(name.equals("exhibitSpace")){
		spon.setExhibitSpace(value);
	    }
	    else if(name.equals("tshirt")){
		spon.setTshirt(value);
	    }
	    else if(name.equals("comments")){
		spon.setComments(value);
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}
	User user = null;
	HttpSession	session = req.getSession(false);
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login?source=Sponsor.do&action=zoom&id="+id;
	    res.sendRedirect(str);
	    return;
	}
	//
	// Get marketing info from plan
	//
	if(action.equals("Save")){
	    String back = spon.doSave();
	    if(!back.equals("")){
		message += " Could not save "+spon.getMessage();
		success = false;
	    }
	    else{
		message += spon.getMessage();
		id = spon.getId();
		History one = new History(debug, id, "Created","Sponsor",user.getId());
		one.doSave();					
	    }
	}
	else if (action.equals("Update")){
	    String back = spon.doUpdate();
	    if(!back.equals("")){
		message += " Could not update "+spon.getMessage();
		success = false;
	    }
	    else{
		message += spon.getMessage();
		History one = new History(debug, id, "Updated","Sponsor",user.getId());
		one.doSave();	
	    }
			
	}
	else if (action.equals("Delete")){
	    String back = spon.doDelete();
	    if(!back.equals("")){
		message += " Could not delete "+spon.getMessage();
		success = false;
	    }
	    else{
		message += spon.getMessage();
	    }
	}
	else if (!id.isEmpty()){
	    String back = spon.doSelect();
	    if(!back.equals("")){
		message += " Could not save "+spon.getMessage();
		success = false;
	    }
	    else{
		pid = spon.getPid();
	    }
	}
	Program prog = spon.getProgram();
	Plan plan = prog.getPlan();
	List<Sponsor> slist = prog.getSponsors();
	if(id.equals("") && plan != null){
	    attendCount = plan.getAttendCount();
	    spon.setAttendCount(attendCount);
	}
	if(slist != null && slist.size() > 0){
	    firstTime = false;
	}
	if(action.equals("Continue")){
	    firstTime = false;
	}
	//
	if(!spon.getMonetary().equals("")) monetary = "checked=\"checked\"";
	if(!spon.getTangible().equals("")) tangible = "checked=\"checked\"";
	if(!spon.getServices().equals("")) services = "checked=\"checked\"";
	if(!spon.getSignage().equals("")) signage = "checked=\"checked\"";
	if(!spon.getExhibitSpace().equals("")) exhibitSpace = "checked=\"checked\"";
	if(!spon.getTshirt().equals("")) tshirt = "checked=\"checked\"";

				
	out.println("<html>");
	out.println("<head><title>City of Bloomington Parks and "+
		    "Recreation</title>");
	Helper.writeWebCss(out, url);
       	out.println(" <script>");
	out.println("  function validateDeleteForm(){   ");            
	out.println("  var x = false ;                  ");
	out.println("   x = window.confirm(\"Are you sure you want to delete\"); ");
	out.println("   return x;                       ");
	out.println(" }	                                ");
	out.println("  function checkNext(){            ");   
	out.println("  with(document.myForm){           ");
	out.println("  if(ch0.checked && ch1.checked && ch2.checked && "+
		    " ch3.checked){      ");
	out.println("    action.disabled=false; }       ");
	out.println("    }	                            ");
	out.println(" }	                                ");
	out.println(" </script>                         ");   
	out.println("</head><body>");
	//
	out.println("<center>");
	Helper.writeTopMenu(out, url);
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\">");
	out.println("<input type=\"hidden\" name=\"pid\" value=\"" + pid + "\" />");
	if(firstTime){
			
	    out.println("<h2>Definition, Examples and Guidelines for "+
			"<br />Requesting Sponsorship</h2>");
	    if(!message.equals("")){
		    out.println(message+"<br />");
	    }
	    out.println("<table><caption>Definitions and Examples</caption><tr><td>");
	    out.println("<p><b>Definition of Sponsorship:</b>Sponsorship is "+
			"a paid effort from a business to tie its name to "+
			"information, an event, or a venue that reinforces "+
			"its brand in a positive, yet not overtly commercial "+
			" manner.<i><u>(Online Advertizing Glossary "+
			"Sponsorships).</u>"+
			" Janet Rayn and Nancy Whiteman (May 15, 2000)."+
			"</i></p>");
	    out.println("<p><b>Examples of Ideal Sponsorship:</b> ");
	    out.println("Examples of sponsorships vary, as the whole point "+
			"is to establish a more unique advertising "+
			"opportunity than afforded by traditional "+
			"advertisements. Ideally in the form of money, "+
			"but can also be in-kind, service or a combination."+
			"</p>");
	    out.println("</td></tr></table>");
	    out.println("<table><caption> Things to consider</caption>");
	    out.println("<tr><td><b>Programs</b></td>");
	    out.println("<td><b>Things to consider</b></td>");
	    out.println("<td><b>Something to consider</b></td></tr>");
	    out.println("<tr><td valign=\"top\">");
	    out.println("<li>Youth Sports</li>"+
			"<li>Community Events</li>"+
			"<li>Facilities-Golf Course, Pools, Frank Southern, "+
			"Ball Fields</li>"+
			"<li>Expos</li>"+
			"<li>Races</li>"+
			"<li>Tournments</li></td>");
	    out.println("<td valign=\"top\"># of participants<br />"+
			"History of Program<br />"+
			"Media Exposure<br />"+
			"Other Marketing Exposure</td>");
	    out.println("<td valign=\"top\">"+
			"Your program, event or facility"+
			"may NOT NEED sponsorship dollars, "+
			"but may be attractive to sponsorship "+
			"opportunity for businesses. "+
			"Think untagged $.</td></tr>");
	    out.println("</table>");
	    out.println("<table>");
	    out.println("<caption>Guidelines for Requesting Sponsorship</caption>");
	    out.println("<tr><td>");
	    out.println("If this is a first time event/program, contact the Special Services Coordinator to discuss sponsorship prior to entering formal request.<br />");
	    out.println("<ol>");
	    out.println("<li><input type=checkbox onChange=\"checkNext()\" "+
			"name=ch0>");
	    out.println("The event/program has low risk of cancelling due to lack of registration.</li>");
	    out.println("<li><input type=checkbox onChange=\"checkNext()\" "+
			"name=ch1>");
	    out.println("The event/program offers a minimum of three substantial marketing benefits.</li>");
	    out.println("<li><input type=checkbox onChange=\"checkNext()\" "+
			"name=ch2>");
	    out.println("Participation exceeds the 100 participant threshold. </li>");

	    out.println("<li><input type=checkbox onChange=\"checkNext()\" "+
			"name=ch3>");
	    out.println("This event/program continues to be in the upward slope of the program life cycle.</li>");
	    out.println("</ol>");
	    out.println("</td></tr>");
	    out.println("<tr><td align=right>");
	    out.println("<input type=\"submit\" name=\"action\" "+
			" disabled=\"disable\" value=\"Continue\">");
	    out.println("</td></tr></table>");
	}
	else{
	    if(id.equals("")){
		out.println("<h2>Add New Sponsorship</h2>");
	    }
	    else // Save, Update
		out.println("<h2>View/Edit Sponsorship</h2>");
	    if(!message.equals("")){
		    out.println(message+"<br />");
	    }
	    if(!id.equals("")){
		out.println("<input type=hidden name=\"id\" value=\""+id+"\" />");
	    }
	    if(!spon.getAttendCount().equals("")){
		out.println("<input type=hidden name=\"attendCount\" value=\""+spon.getAttendCount()+"\" />");
	    }			
	    out.println("<table border=\"1\">");
	    //
	    // program, year, season
	    out.println("<tr><td align=\"right\"><b>Program:");
	    out.println("</b></td><td align=\"left\">");
	    out.println(prog.getTitle()+" ("+prog.getSeason()+"/"+prog.getYear()+")</td></tr>");
	    out.println("<tr><td align=\"right\"><b>Program ID:");
	    out.println("</b></td><td align=\"left\">");			
	    out.println("<a href=\""+url+"Program.do?id="+pid+"&action=zoom\">"+pid+"</a></td></tr>");
	    //
	    // attendCount 
	    // one value for all records
	    // this should appear only once 
	    out.println("<tr><td align=\"right\">");
	    out.println("<b>Attendance:</b><td align=\"left\">");
			
	    out.println(spon.getAttendCount()+" (from plan info)");
	    out.println("</td></tr>");
	    //
	    // Market target
	    if(market.equals("")) market = market2;
	    out.println("<tr><td align=\"right\">");
	    out.println("<label for=\"market\">Target Market: </label></td><td align=\"left\">");
	    out.println("<input type=\"text\" name=\"market\" maxlength=\"80\" "+
			"value=\""+spon.getMarket() + "\" size=\"70\" id=\"market\" /></td></tr>");
	    //
	    out.println("</td></tr>");
	    out.println("<tr><td align=\"right\"><b>Category");
	    out.println("</b></td><td align=\"left\">");
	    out.println("<input type=\"checkbox\" name=\"monetary\" value=\"y\" "+monetary+
			" id=\"monetary\" /><label for=\"monetary\">Monetary</label>, ");
	    out.println("<input type=\"checkbox\" name=\"tangible\" value=\"y\" "+tangible+
			" id=\"tangible\" /><label for=\"tangible\">Tangible Goods</label>, ");
	    out.println("<input type=\"checkbox\" name=\"services\" value=\"y\" "+services+
			" id=\"service\" /><label for=\"service\">Services</label> ");
	    out.println("</td></tr>");
	    //
	    //
	    out.println("<tr><td align=\"right\"><b>Sponsor Benefits: </b>");
	    out.println("</td><td align=\"left\">");
	    out.println("<input type=\"checkbox\" name=\"signage\" value=\"y\" "+
			signage+" id=\"signage\"/><label for=\"signage\">Signage at Event</lable> ");
	    out.println("<input type=\"checkbox\" name=\"exhibitSpace\" value=\"y\""+
			" "+exhibitSpace +" id=\"space\"/><label for=\"space\">Exhibitor Space</label> ");
	    out.println("<input type=\"checkbox\" name=\"tshirt\" value=\"checked\" "+
			tshirt +" id=\"tshirt\" /><label for=\"tshirt\">TShirt.</label>");
	    out.println("</td></tr>");
	    //
	    // Comments
	    out.println("<tr><td align=\"right\" valign=\"top\"><label for=\"comm\">Comments"+
			" & Ideas</label>");
	    out.println("</td><td align=\"left\">");
	    out.print("<textarea rows=\"7\" cols=\"50\" name=\"comments\" wrap "+
		      " id=\"comm\">"); 
	    out.println(spon.getComments()+"</textarea>");
	    out.println("</td></tr>");
	    //
	    if(id.equals("")){
		if(user.canEdit()){
		    out.println("<tr><td align=\"right\"><input type=\"submit\"  name=\"action\" value=\"Save\" />&nbsp;&nbsp;"+
				"</td></tr>");
		}
		out.println("</form>");
	    }
	    else{ // add zoom update
		out.println("<tr>");
		if(user.canEdit()){
		    out.println("<td>"+
				"<input type=\"submit\" "+
				"name=\"action\" value=\"Update\"></td>");
		}
		out.println("</form>");
		if(user.canDelete()){
		    out.println("<td>");
		    //
		    out.println("<form name=\"myForm2\" method=\"post\" "+
				"onSubmit=\"return validateDeleteForm()\">");
		    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+ "\" />");
		    out.println("<input type=\"hidden\" name=\"pid\" value=\""+pid+ "\" />");
		
		    out.println("&nbsp;&nbsp;<input type=\"submit\" name=\"action\" "+
				"value=\"Delete\">");
		    out.println("</form></td>");
		}
		out.println("</tr></table>");
	    }	    
	    //
	    // put all the old vol-sponsor stuff in one table
	    //
	    if(slist != null && slist.size() > 0){
		out.println("<br /># of sponsorship requests:"+slist.size());
		out.println("<table border=\"1\">");
		out.println("<caption>Previous Sponsorships</caption>");
		out.println("<tr><th>ID</th><th>Attendance</th><th>Market</th><th>Categories</th><th>Benefits</th><th>Ideas</th></tr>");
		for(Sponsor one:slist){
		    out.println("<tr><td>"+
				"<a href=\""+url+"Sponsor.do?id="+one.getId()+"&action=zoom\">"+one.getId()+"</a></td>");
		    out.println("<td>"+one.getAttendCount()+"</td>");
		    out.println("<td> "+one.getMarket()+"</td>");
		    out.println("<td> "+one.getCategories()+"</td>");
		    out.println("<td> "+one.getBenefits()+"</td>");
		    out.println("<td> "+one.getComments()+"</td>");
		    out.println("</tr>");
		}
		out.println("</table>");					
	    }
			
	}
	if(spon.hasHistory()){
	    Helper.writeHistory(out, "Sponsorship Logs", spon.getHistory()); 
	}
	Helper.writeWebFooter(out, url);
	out.println("<hr />");
	out.println("</center>");
	out.print("</body></html>");
	out.flush();
	out.close();
    }

}























































