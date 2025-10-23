package program.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/MediaRequest"})
public class MediaRequestServ extends TopServlet{

    static Logger logger = LogManager.getLogger(MediaRequestServ.class);


    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{
	doPost(req, res);
    }

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	PrintWriter out;

	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name, value, action="", fromBrowse="";
	String id ="", program_id="", facility_id="";
		
	//
	// reinitialize to blank
	//
	String message = "", finalMessage="";

	out.println("<html><head><title>City of Bloomington Parks and "+
		    "Recs</title>"); 
	boolean actionSet = false, success=true;
	User user = null;
	HttpSession	session = req.getSession(false);
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	LeadList leads = null;
	LocationList locs = null;
	List<Location> locations = null;
	MediaRequest request = new MediaRequest(debug);
        String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("id")){
		if(value != null){
		    request.setId(value);
		    id = value;
		}
	    }
	    else if(name.equals("program_id")){
		if(value != null)
		program_id = value;
		request.setProgram_id(value);
	    }			
	    else if(name.equals("lead_id")){
		if(value != null)
		    request.setLead_id(value);
	    }
	    else if(name.equals("facility_id")){
		if(value != null){
		    request.setFacility_id(value);
		    facility_id = value;
		}
	    }	    
	    else if(name.equals("location_id")){
		request.setLocation_id(value);
	    }
	    else if(name.equals("locationDescription")){
		request.setLocationDescription(value);		
	    }			
	    else if(name.equals("contentSepecific")){
		request.setContentSepecific(value);
	    }			
	    else if(name.equals("requestType")){
		request.setRequestType(vals);
	    }
	    else if(name.equals("otherType")){
		request.setOtherType(value);
	    }
	    else if(name.equals("requestDate")){
		request.setRequestDate(value);
	    }
	    else if(name.equals("season")){
		request.setSeason(value);
	    }
	    else if(name.equals("requestYear")){
		request.setRequestYear(value);
	    }
	    else if(name.equals("orientation")){
		request.setOrientation(value);
	    }	 	    
	    else if(name.equals("notes")){
		request.setNotes(value);
	    }	    
	    else if(name.equals("fromBrowse")){
		if(value != null)
		    fromBrowse = value;
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}
	//
       	if(action.equals("Delete")){
	    if(user.canDelete()){
		String back = request.doDelete();
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
		String back = request.doUpdate();
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
		String back = request.doSave();
		if(back.equals("")){
		    id = request.getId();
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
	    String back = request.doSelect();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}
	if(id.isEmpty()){
	    if(request.hasProgram()){
		Program pp = request.getProgram();
		if(pp != null){
		    request.setSeason(pp.getSeason());
		    request.setRequestYear(pp.getYear());
		    request.setLead_id(pp.getLead_id());
		    request.setLocation_id(pp.getLocation_id());
		    request.setRequestDate(pp.getStartDate());
		}
	    }
	    else if(request.hasFacility()){
		Facility ff = request.getFacility();
		if(ff != null){
		    request.setLead_id(ff.getLead_id());		    
		    Market market = ff.findLatestMarket();
		    if(market != null){
			request.setSeason(market.getSeason());
			request.setRequestYear(market.getYear());
		    }
		}
	    }
	}
	if(true){
	    leads = new LeadList(debug);
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    locs = new LocationList(debug);
	    locs.setActiveOnly();
	    back = locs.find();
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
	out.println("<script>");
	out.println("    function validateTextarea(ss){                   ");
	out.println("      if (ss.value.length > 10000){                  ");
	out.println("       alert(\"Maximum Character number is 10000\"); ");
	out.println("        ss.value = ss.value.substring(0,9999);       ");
	out.println("                    }                                ");
	out.println("                }                                    ");
	out.println("     function validateForm2() {                      "); 
	out.println("				                          ");
	out.println(" answer = window.confirm (\"Delete This Record ?\"); ");  
	out.println("	if (answer == true)	                          ");
	out.println("	     return true;                                 ");
	out.println("	 return false;		                          ");
	out.println("	 }		                                  ");
	out.println("  function checkTextLen(ss,len) {                    ");
	out.println("   if (ss.value.length > len) {                      ");
	out.println("     alert(\"Maximum number of characters is \"+len); ");
	out.println("        ss.value = ss.value.substring(0,len);       ");
	out.println("                    }                               ");
	out.println("                }                                   ");
	out.println("  function validateForm(){                          "); 
	out.println("	     return true;	           ");
	out.println(" 	 }                                 ");
	out.println("	</script>                          ");   
	out.println("</head><body>");
	//
	out.println("<center>");
	Helper.writeTopMenu(out, url);		
	if(id.equals("")){
	    out.println("<h2>New Media Request</h2>");
	}
	else{
	    out.println("<h2>Edit Media Request "+id+"</h2>");
	}
	if(!message.equals("")){
	    out.println(message);
	    out.println("<br />");
	}
	//
	out.println("<form name=\"myForm\" method=\"post\" onsubmit=\"return "+
		    "validateForm()\" id=\"form_id\" >");
	if(!fromBrowse.equals("")){
	    out.println("<input type=\"hidden\" name=\"fromBrowse\" value=\"y\" />");
	}		
	if(!id.isEmpty()){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	    if(request.hasProgram()){
		out.println("<input type=\"hidden\" name=\"season\" value=\""+request.getSeason()+"\" />");
		out.println("<input type=\"hidden\" name=\"requestYear\" value=\""+request.getRequestYear()+"\" />");		
	    }
	}
	out.println("<input type=\"hidden\" name=\"program_id\" value=\""+request.getProgram_id()+"\" />");
	out.println("<input type=\"hidden\" name=\"facility_id\" value=\""+request.getFacility_id()+"\" />");	    
	out.println("<table width=\"90%\" border=\"1\">");
	out.println("<caption>Media Request</caption>");
	out.println("<tr><td align=\"left\" width=\"30%\"><label for=\"requestDate\">Request Date: </label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"requestDate\" value=\""+request.getRequestDate()+"\" size=\"10\" maxlength=\"10\' class=\"date\" id=\"requestDate\" />");
	out.println("</td></tr>");
	if(request.hasProgram()){
	    out.println("<tr><td align=\"left\"><b>Program: </b></td><td align=\"left\">");
	    
	    out.println("<a href=\""+url+"Program?id="+request.getProgram_id()+"\"> "+request.getProgram().getTitle()+"</a>");
	    out.println("</td></tr>");
	    out.println("<tr><td align=\"left\"><b>Year - Season </b></td><td align=\"left\">");
	    out.println(request.getRequestYear()+" - "+request.getSeason()+"</td></tr>");
	}
	if(request.hasFacility()){
	    out.println("<tr><td align=\"left\"><b>Facility: </b></td><td align=\"left\">");
	    
	    out.println("<a href=\""+url+"Facility?id="+request.getFacility_id()+"\">"+request.getFacility().getName()+"</a>");
	    out.println("</td></tr>");
	    out.println("<tr><td align=\"left\"><b>Season: </b></td><td align=\"left\"> <select name=\"season\">");
	    out.println("<option value=\""+request.getSeason()+"\" selected>"+
			request.getSeason()+"\n");
	    out.println(Helper.allSeasons);
	    out.println("</select><label for=\"ryear\"> Year: </label>");
	    out.println("<select name=\"requestYear\" id=\"ryear\">");
	    int years[] = Helper.getPrevYears();
	    for(int yy:years){
		String selected="";
		if(request.getRequestYear().equals(""+yy))
		    selected="selected=\"selected\"";
		out.println("<option "+selected+" value=\""+yy+"\">"+yy+"</option>");
	    }
	    out.println("</select></td></tr>");	    
	}	
	out.println("<tr><td align=\"left\"><label for=\"lead_id\">Lead: </label></td><td align=\"left\">");
	out.println("<select name=\"lead_id\" id=\"lead_id\">");
	out.println("<option value=\"\">Pick a Lead*</option>");	
	if(leads != null){
	    for(Lead one:leads){
		String selected = "";
		if(one.getId().equals(request.getLead_id())){
		    selected = "selected=\"selected\"";
		}
		else if(!one.isActive()) continue;
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}	
	out.println("</select>");
	out.println("</td></tr>");	
	out.println("<tr><td align=\"left\"><label for=\"loc_id\">Location/Parks Initiative:</label></td><td align=\"left\">");
	out.println("<select name=\"location_id\" id=\"loc_id\">");
	out.println("<option value=\"\">Pick a Location</option>");		
	if(locations != null){
	    for(Location one:locations){
		String selected = "";
		if(one.getId().equals(request.getLocation_id())){
		    selected = "selected=\"selected\"";
		}
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one.getName()+"</option>");
	    }
	}
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\"><label for=\"loc_sp\">Location Specifics </label><br> (such as 'near the playground')</td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\">");
	out.println("<textarea name=\"locationDescription\" row=\"5\" cols=\"60\" wrap=\"wrap\" id=\"loc_sp\">");
	out.println(request.getLocationDescription());
	out.println("</textarea></td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\"><label for=\"cont\">Content Specific </label> <br />Indicate if you need a specific season or date/time. Examples would be 'Griffy in the snow' or 'during program hours 5-7pm on mm/dd/yy', or even 'of staff helping guests'. </td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\">");
	out.println("<textarea name=\"contentSepecific\" row=\"5\" cols=\"60\" wrap=\"wrap\" id=\"cont\">");
	out.println(request.getContentSpecific());
	out.println("</textarea></td></tr>");
	out.println("</td></tr>");	
	out.println("<tr><td align=\"left\"><b>Media Requested </b>(Check all that apply) </td><td align=\"left\">");	
	String request_types[] = {"Photography","Videography","Other"};
	Set<String> typeSet = null;
	if(request.getRequestType() != null){
	    typeSet = Set.of(request.getRequestType());
	}

	for(String type:request_types){
	    String checked = "";
	    if(typeSet != null && typeSet.contains(type)){
		checked="checked=\"checked\"";
	    }
	    out.println("<input type=\"checkbox\" name=\"requestType\" id=\""+type+"\" value=\""+type+"\" "+checked+" id=\""+type+"\"/><label for=\""+type+"\" >"+type+"</label>");
	}
	out.println("</td></tr>");
	out.println("<tr><td align=\"left\"><b>What orientation is needed </b> </td><td align=\"left\">");			
	String orientation_types[] = {"Vertical","Horizental","Both"};
	for(String str:orientation_types){
	    String checked = "";
	    if(str.equals(request.getOrientation())){
		checked = "checked=\"checked\"";
	    }
	    out.println("<input type=\"radio\" name=\"orientation\" value=\""+str+"\" id=\""+str+"\" "+checked+" />Label for=\""+str+"\">"+str+"</label>");
	}
	out.println("</td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\"><label for=\"other\">Other Media Type </label></td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\">");
	out.println("<textarea name=\"otherType\" row=\"5\" cols=\"60\" wrap=\"wrap\" id=\"other\">");
	out.println(request.getOtherType());
	out.println("</textarea></td></tr>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\"><label for=\"note\">Notes <br /> We will be contacting you if there are any questions or clarifications. Please include any helpful details here:</label></td></tr>");
	out.println("<tr><td align=\"left\" colspan=\"2\">");
	out.println("<textarea name=\"notes\" row=\"5\" cols=\"60\" wrap=\"wrap\" id=\"notes\">");
	out.println(request.getNotes());
	out.println("</textarea></td></tr>");
	out.println("</td></tr>");	
	out.println("<tr>");
	if(!id.equals("")){
	    //
	    // if no program yet (it is new plan)
	    // can be duplicated only when a program is linked to it
	    //
	    out.println("<td valign=top align=\"right\">");				
	    out.println("<input type=\"submit\" "+
			"name=\"action\" value=\"Update\" /></td>");
	    if(user.canDelete()){
		out.println("<td valign=\"top\" align=\"right\">");				
		out.println("<form name=\"myForm2\" method=\"post\" "+
			    "onSubmit=\"return validateForm2()\">");
		out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
		out.println("</td><td valign=\"top\" align=\"right\">");
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Delete\">&nbsp;");
		out.println("</form></td>");
	    }
	}
	else { // delete startNew
	    out.println("<td valign=\"top\" align=\"right\">");
	    if(user.canEdit()){
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Save\" /></td>"+
			    "<td align=\"right\" valign=\"top\">" );
	    }
	    out.println("</td>");
	}
	out.println("</tr>");
	out.println("</table>");
	out.println("</form>");
	out.println("<br />");
	if(fromBrowse.equals("")){
	    out.println("<a href=javascript:window.close();>Close</a>");
	}
	Helper.writeWebFooter(out, url);
	String dateStr = "{ nextText: \"Next\",prevText:\"Prev\", buttonText: \"Pick Date\", showOn: \"both\", navigationAsDateFormat: true, buttonImage: \""+url+"js/calendar.gif\"}";
	out.println("<script>");
	out.println("  $( \"#requestDate\" ).datepicker("+dateStr+"); ");
	out.println("</script>");	
	//
	out.print("</center></body></html>");
	out.close();
    }

}





































