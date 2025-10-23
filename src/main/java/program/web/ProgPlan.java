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
/**
 *
 */
@WebServlet(urlPatterns = {"/ProgPlan.do","/ProgPlan"})
public class ProgPlan extends TopServlet{

    static Logger logger = LogManager.getLogger(ProgPlan.class);
    String staffSelectArr[] = Helper.staffSelectArr;
    String [] allFreqArr = Helper.allFreqArr;

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
	//
	// reinitialize to blank
	//
	String message = "", finalMessage="";
	out.println("<!DOCTYPE html>");
	out.println("<html><head><title>City of Bloomington Parks and "+
		    "Recreation</title>"); 
	boolean actionSet = false, success=true;

	User user = null;
	HttpSession	session = req.getSession(false);
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	Plan pp = new Plan(debug);
	PrePlan prePlan = null;
	Contact instructor = new Contact(debug);
	List<Contact> instructors = null;
        String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("program_title")){
		pp.setProgram_title(value);
	    }
	    else if(name.equals("program_date")){
		pp.setProgram_date(value);
	    }	    
	    else if(name.equals("id")){ // change to id
		id = value;
		pp.setId(value);
	    }
	    else if(name.equals("lead_id")){
		pp.setLead_id(value);
	    }
	    else if(name.equals("goals")){
		pp.setGoals(value);
	    }
	    else if(name.equals("life_cycle")){
		pp.setLife_cycle(value);
	    }			
	    else if(name.equals("p_duration")){
		pp.setP_duration (value);
	    }
	    else if(name.equals("cont_id")){
		instructor.setId(value);
		pp.setInstructor(instructor);
	    }			
	    else if(name.equals("attendCount")){
		pp.setAttendCount (value);
	    }
	    else if(name.equals("email")){
		instructor.setEmail (value);
	    }
	    else if(name.equals("address")){
		instructor.setAddress (value);
	    }
	    else if(name.equals("ideas")){
		pp.setIdeas (value);
	    }
	    else if(name.equals("profit_obj")){
		pp.setProfit_obj (value);
	    }
	    else if(name.equals("objective")){
		pp.addObjectives(vals); // multiple
	    }
	    else if(name.equals("del_obj")){
		pp.deleteObjectives(vals); // multiple
	    }
	    else if(name.equals("delStaff")){
		pp.setDelStaff(vals); // multiple
	    }			
	    else if(name.equals("del_cont")){
		pp.setDeleteInstructors(vals); // multiple
	    }			
	    else if(name.startsWith("staff-type")){
		pp.addStaffType(name, value); // (staff-type_0, 1),(staff_type_1, 2),
	    }
	    else if(name.startsWith("quantity")){
		pp.addStaffQuantity(name, value); // (staff-type_0, 1),(staff_type_1, 2),
	    }			
	    else if(name.equals("partner")){
		pp.setPartner (value);
	    }
	    else if(name.equals("sponsor")){
		pp.setSponsor (value);
	    }
	    else if(name.equals("market")){
		pp.setMarket (value);
	    }
	    else if(name.equals("frequency")){
		pp.setFrequency (value);
	    }
	    else if(name.equals("min_max")){
		pp.setMin_max (value);
	    }
	    else if(name.equals("event_time")){
		pp.setEvent_time (value);
	    }
	    else if(name.equals("est_time")){
		pp.setEst_time (value);
	    }
	    else if(name.equals("year_season")){
		pp.setYear_season (value);
	    }
	    else if(name.equals("program_year")){
		pp.setProgram_year (value);
	    }
	    else if(name.equals("season")){
		pp.setSeason (value);
	    }	    
	    else if(name.equals("history")){ // 10k
		pp.setHistory (value);
	    }
	    else if(name.equals("supply")){   // 5k
		pp.setSupply (value);
	    }
	    else if(name.equals("timeline")){ // 5k
		pp.setTimeline (value);
	    }
	    else if(name.equals("action")){
		if(value != null && !value.equals(""))								
		    action = value;
	    }
	    else if(name.equals("action2")){
		if(value != null && !value.equals(""))
		    action = value;
	    }						
	}
	if(user == null){
	    String str = url+"Login?source=ProgPlan&action=zoom&id="+id;		
	    res.sendRedirect(str);
	    return;
	}
	LeadList leads = null;
	if(true){
	    leads = new LeadList(debug);
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    ContactList cont = new ContactList(debug);
	    back = cont.find();
	    if(back.isEmpty()){
		List<Contact> ones = cont.getContacts();
		if(ones != null && ones.size() > 0){
		    instructors = ones;
		}
	    }
	}
	//
       	if(action.equals("Delete")){
	    if(user.canDelete()){
		String back = pp.doDelete();
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
	else if(action.equals("zoom")){
	    String back = pp.doSelect();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		instructor = pp.getInstructor();
	    }
	}
	else if(action.equals("Save")){
	    String back = pp.doSave();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		id = pp.getId();
		message = "Saved Successfully";
		History one = new History(debug, id, "Saved","Plan",user.getId());
		one.doSave();	
	    }
	}				
	else if(action.equals("Update")){
	    if(user.canEdit()){
		pp.setInstructor(instructor);
		String back = pp.doUpdate();
		if(back.equals("")){
		    message = "Record updated successfully";
		    History one = new History(debug, id, "Updated","Plan",user.getId());
		    one.doSave();	
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
		if(!instructor.isEmpty()){
		    instructor.doSaveOrUpdate();
		}
		pp.setInstructor(instructor);
		String back = pp.doSave();
		if(back.equals("")){
		    id = pp.getId();
		    message = "Saved successfully";
		    History one = new History(debug, id, "Created","Plan",user.getId());
		    one.doSave();	
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
	else if(!id.isEmpty()){
	    pp.doSelect();
	    if(pp.hasPrePlan()){
		prePlan = pp.getPrePlan();
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
	out.println("     function validateDelete() {                     "); 
	out.println("				                                  ");
	out.println(" answer = window.confirm (\"Delete This Record ?\"); ");  
	out.println("	  if(answer){	                          ");
	out.println("  document.getElementById(\"action2\").value=\"Delete\"; ");
	out.println("  document.getElementById(\"form_id\").submit(); ");
	out.println("	     return true;                         ");
	out.println("	   }		                                  ");				
	out.println("	 return false;		                        ");
	out.println("	 }		                                    ");
	out.println("  function checkTextLen(ss,len) {                    ");
	out.println("   if (ss.value.length > len) {                      ");
	out.println("     alert(\"Maximum number of characters is \"+len); ");
	out.println("        ss.value = ss.value.substring(0,len);       ");
	out.println("                    }                               ");
	out.println("                }                                   ");
	out.println("  function validateForm(){                          "); 
	out.println("	if(document.myForm.program.value.length == 0){     ");  
	out.println("        alert(\"Title field must be entered\");     ");
	out.println("	     return false;	                 ");
	out.println(" 	    }                              ");
	out.println("	if(document.myForm.goals.value.trim() == ''){    ");  
	out.println("        alert(\"Goals is a required field\");     ");
	out.println("	     return false;	                 ");
	out.println(" 	    }                              ");
	out.println("	if(!document.myForm.del_obj && "); // no old obj				
	out.println("	  document.myForm.objective[0].value.trim() == ''){ ");  
	out.println("        alert(\"Objectives are required fields\");   ");
	out.println("	     return false;	                ");
	out.println(" 	 }                                ");				
	out.println("	   return true;	                    ");
	out.println(" 	}                                 ");
	out.println("  function checkNavigator(){		       ");
	out.println("  var appl = navigator.appName;       ");
	out.println("  return true; }			                 ");
	out.println("	</script>                            ");   
	out.println("</head><body>");
	//
	Helper.writeTopMenu(out, url);
	out.println("<center>");
	String tdWidth=" width=\"20%\" ";
	if(pp.isNew()){
	    out.println("<h2>Add New Plan</h2>");
	}
	else{
	    out.println("<h2>Edit Plan "+id+"</h2>");
	}
	out.println("</center>");
	if(!message.equals("")){
	    out.println(message);
	    out.println("<br />");
	}
	//
	out.println("<form name=\"myForm\" id=\"form_id\" method=\"post\" onsubmit=\"return validateForm()\" >");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	    out.println("<input type=\"hidden\" name=\"action2\" id=\"action2\" value=\"\" />");
	}
	//
	// we need this for auto_complete
	//
	
	// Plan
	out.println("* indicates a required field.<br />");	
	out.println("<table width=\"90%\" border=\"1\">");
	out.println("<caption>Plan Info</caption>");
	out.println("<tr><td "+tdWidth+" align=\"right\"><label for=\"ptitile\">*Program Title: </label></td><td>");
	out.println("<input type=\"text\" name=\"program_title\" id=\"ptitle\" "+
		    "value=\""+pp.getProgram_title()+"\" maxlength=\"128\" size=\"70\" required=\"required\" /></td></tr>");
	
	out.println("<tr><td align=\"right\"><label for=\"season\">Program Season:</label> </td><td>");
	out.println("<select name=\"season\" id=\"season\">");
	out.println("<option value=\""+pp.getSeason()+"\" selected>"+
		    pp.getSeason()+"\n");
	out.println(Helper.allSeasons);
	out.println("</select> ");	
	out.println(" <label for=\"year\">Year:</label>");
	out.println("<select name=\"program_year\" id=\"year\">");
	out.println("<option value=\"\">\n");
	int[] years = Helper.getFutureYears();
	for(int yy:years){
	    String selected="";
	    if(pp.getProgram_year().equals(""+yy))
		selected="selected=\"selected\"";
	    out.println("<option "+selected+">"+yy+"</option>");
	}
	out.println("</select>&nbsp;&nbsp;");
	//
	// lead
	out.println("<tr><td align=\"right\"><label for=\"lead\">Program Lead: </label></td><td>");
	out.println("<select name=\"lead_id\" id=\"lead\">");
	out.println("<option value=\"\"></option>");
	if(leads != null){
	    for(Lead one:leads){
		String selected = "";
		if(pp.getLead_id().equals(one.getId()))
		    selected = "selected=\"selected\"";
		else if(!one.isActive()) continue;
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td colspan=\"2\" align=\"center\" "+
		    "><b>"+
		    "Instructor Contact Info </b></td></tr>");
	if(!id.equals("") && pp.hasInstructors()){
	    List<Contact> ones = pp.getInstructors();
	    if(ones != null && ones.size() > 0){
		out.println("<tr><td colspan=\"2\"><table border=\"1\" width=\"100%\"><caption>Current Instructor(s)</caption>");
		out.println("<tr><th>&nbsp;</th><th>Name</th><th>Phones</th><th>Email</th><th>Address</th><th>&nbsp;</th></tr>");
		int jj=1;
		for(Contact one:ones){
		    out.println("<tr><td><input type=\"checkbox\" name=\"del_cont\" value=\""+one.getId()+"\" />"+(jj++)+"</td>");
		    out.println("<td>"+one.getName()+"</td>");
		    out.println("<td>"+one.getPhones()+"</td>");
		    out.println("<td>"+one.getEmail()+"</td>");
		    out.println("<td>"+one.getAddress()+"</td>");
		    out.println("<td><input type=button onclick=\""+
				"window.open('"+url+"Contact.do?id="+one.getId()+"&plan_id="+pp.getId()+"','Time',"+
				"'toolbar=0,location=0,"+
				"directories=0,status=0,menubar=0,"+
				"scrollbars=0,top=300,left=300,"+
				"resizable=1,width=600,height=400');\""+
				" value=\"Edit\" /></td>");	

		    out.println("</tr>");
		}
		out.println("</table></td></tr>");
	    }
	}
	
	out.println("<tr><td colspan=\"2\" align=\"center\"><b>Add New Instructor</b></td></tr>");
	out.println("<tr><td "+tdWidth+" align=\"right\"><label for=\"cont_id\">Name: </label></td><td>");
	out.println("<select name=\"cont_id\" id=\"cont_id\">");
	if(instructors != null){
	    for(Contact one:instructors){
		out.println("<option value=\""+one.getId()+"\">"+one.getInfo()+"</option>");
	    }
	}
	out.println("</select></td></tr></table>");
		
	//
	// Plan Objectives
	out.println("<table border=\"1\" width=\"90%\">");
	out.println("<caption>Program Ideas & Goals</caption>");
	//
	out.println("<tr><td valign=\"top\" "+tdWidth+" align=\"right\"><label for=\"obj\">Ideas to Program: </label></td><td>");
	out.println("<textarea name=\"ideas\" cols=\"50\" rows=\"4\" id=\"obj\""+
		    " onchange='checkTextLen(this,2000)' wrap=\"soft\">");
	out.println(pp.getIdeas());
	out.println("</textarea></td></tr>");
	//
	out.println("<tr><td valign=\"top\" align=\"right\"><label for=\"goals\">*Program Goals: </label></td><td>");
	out.println("goal is defined as one that is specific, measurable, achievable, results-focused, and time-bound 'S.M.A.R.T.");
	out.println("<textarea name=\"goals\" cols=\"50\" rows=\"4\" "+
		    " onChange=\"checkTextLen(this,1000)\" wrap=\"soft\" required=\"required\" id=\"goals\">");
	out.println(pp.getGoals());
	out.println("</textarea></td></tr>");
	out.println("<tr><td valign=\"top\" colspan=\"2\" align=\"center\"><b>*Program Objectives </b></td></tr>");
	List<Objective> objectives = pp.getObjectives();
	int jj=1;
	if(objectives != null && objectives.size() > 0){
	    for(Objective one:objectives){
		out.println("<tr><td align=\"right\">"+(jj++)+" - <input type=\"checkbox\" name=\"del_obj\" value=\""+one.getId()+"\" id=\"obj"+one.getId()+"\"/></td><td align=\"left\"><label for=\"obj"+one.getId()+"\">"+one+"</label></td></tr>");
	    }
	    // out.println("</table>");
	}
	out.println("<tr><td valign=\"top\" align=\"right\"><b>New Objectives:</b></td><td>");
	out.println("<table>");
	for(int i=0;i<3;i++){
	    out.println("<tr><td valign=top>"+(jj++)+" - </td><td>");
	    out.print("<textarea name=\"objective\" cols=\"50\" rows=\"4\" "+
		      " onchange=\"checkTextLen(this,1000)\" wrap=\"soft\">");
	    out.println("</textarea></td></tr>");
	}
	out.println("</table></td></tr>");
	//
	out.println("<tr><td valign=\"top\" align=\"right\"><label for=\"prof\">Profit Objective: </label></td><td>");
	out.println("<textarea name=\"profit_obj\" cols=\"50\" rows=\"4\" "+
		    " onChange=\"checkTextLen(this,200)\" wrap=\"soft\" id=\"prof\">");
	out.println(pp.getProfit_obj());
	out.println("</textarea></td></tr>");
	out.println("<tr><td>&nbsp;</td></tr>");
	//
	// partner
	out.println("<tr><td align=\"right\"><label for=\"part\">Potential Partnership:</label></td><td>");
	out.println("<input type=\"text\" name=\"partner\" "+
		    "value=\""+pp.getPartner()+"\" maxlength=\"80\" id=\"part\" size=\"50\" /></td></tr>");
	//
	// sponsor
	out.println("<tr><td align=\"right\"><label for=\"spon\">Potential Sponsorship:</label></td><td>");
	out.println("<input type=\"text\" name=\"sponsor\" id=\"spn\" "+
		    "value=\""+pp.getSponsor()+"\" maxlength=\"70\" size=\"50\" /></td></tr>");
	//
	// market
	out.println("<tr><td align=\"right\"><label for=\"target\">Target Market:</label></td><td>");
	out.println("<input type=\"text\" name=\"market\" id=\"target\" "+
		    "value=\""+pp.getMarket()+"\" maxlength=\"80\" size=\"50\" /></td></tr>");
	//
	// frequency
	out.println("<tr><td align=\"right\"><label for=\"freq\">Intended Frequency:</label></td><td>");
	out.println("<select name=\"frequency\" id=\"freq\">");
	out.println("<option selected value=\""+pp.getFrequency()+"\">"+pp.getFrequency()+"</option>\n");
	for(int i=0;i<allFreqArr.length;i++){
	    out.println("<option value=\""+allFreqArr[i]+"\">"+allFreqArr[i]+"</option>\n");
	}
	out.println("</select>&nbsp;&nbsp;");
	out.println("</td></tr>");
	//
	// # attendance
	out.println("<tr><td align=\"right\"><label for=\"att\">Estimated # Attendance:</label></td><td>");
	out.println("<input type=\"text\" name=\"attendCount\" id=\"att\" "+
		    "value=\""+pp.getAttendCount()+"\" maxlength=\"10\" size=\"10\" />"+
		    "<font color=\"green\" size=\"-1\">(e.g. in events)"+
		    "</font>");
	out.println("&nbsp;&nbsp;or <label for=\"min_max\"> Min/Max: </label>");
	out.println("<input type=\"text\" name=\"min_max\" id=\"min_max\" "+
		    "value=\""+pp.getMin_max()+"\" maxlength=\"20\" size=\"20\" />"+
		    "(e.g. in a class)</td></tr>");
	//
	// Event time
	out.println("<tr><td align=\"right\"><label for=\"pdate\">Program Date:</label></td><td>");
	out.println("<input type=\"text\" name=\"program_date\" id=\"pdate\" "+
		    "value=\""+pp.getProgram_date()+"\" maxlength=\"10\" size=\"10\" class=\"date\" />(mm/dd/yyyy)</td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"ptime\">Program Time:</label></td><td>");
	out.println("<input type=\"text\" name=\"event_time\" id=\"ptime\" "+
		    "value=\""+pp.getEvent_time()+"\" maxlength=\"20\" size=\"20\" />&nbsp;");
	//
	// duration
	out.println("<label for=\"pdur\">Program Duration:</label>");
	out.println("<input type=\"text\" name=\"p_duration\" id=\"pdur\" "+
		    "value=\""+pp.getP_duration()+
		    "\" maxlength=\"20\" size=\"20\" /></td></tr>"); 
	out.println("<tr><td align=\"right\"><label for=\"ttotal\">Total Estimated Time:</label></td><td>");
	out.println("<input type=\"text\" name=\"est_time\" id=\"ttotal\" "+
		    "value=\""+pp.getEst_time()+"\" maxlength=\"20\" size=\"20\" />"+
		    "(include setup to cleanup)"+
		    "</td></tr>");
	//
	out.println("<tr><td colspan=\"2\" bgcolor=\"navy\" "+
		    "align=\"center\"><b><font color=\"white\">"+
		    "Staff Consideration</font></b></td></tr>");
	out.println("<tr><td colspan=\"2\" align=\"center\"><table>");
	List<Staff> staffs = pp.getStaffs();
	jj=1;
	Set<Type> staffHash = new HashSet<Type>();
	if(staffs != null && staffs.size() > 0){
	    for(Staff one:staffs){
		staffHash.add(one.getStaff_type());
		out.println("<tr><td align=\"left\">"+(jj++)+" - <input type=\"checkbox\" name=\"delStaff\" value=\""+one.getId()+"\" />"+one.getStaff_type()+"</td><td align=\"left\">"+one.getQuantity()+"</td></tr>");
	    }
	}
	TypeList staffTypes = new TypeList(debug, "staff_types");
	staffTypes.find();
	if(staffTypes.size() > 0){
	    if(staffHash.size() > 0){
		for(Type one:staffHash){
		    staffTypes.remove(one);
		}
	    }
	}
	if(staffTypes.size() > 0){
	    for(int i=0;i<3;i++){
		out.println("<tr><td>"+(jj++)+" - <select name=\"staff-type_"+i+"\">");
		out.println("<option value=\"\">Pick One</option>");
		for(Type one:staffTypes){
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
		out.println("</select></td><td><input type=\"text\" name=\"quantity_"+i+"\" value=\"\" size=\"4\" /></td></tr>");
	    }
	}
	//
	//
	// Event history
	out.println("<tr><td>&nbsp;</td><td>");
	out.println("No more than 10,000 characters</td></tr>");
	out.println("<tr><td valign=\"top\" align=\"right\"><label for=\"hist\">Event History: </label></td><td>");
	out.println("<textarea name=\"history\" rows=\"20\" cols=\"70\" "+
		    " onchange=\"checkTextLen(this,10000)\" id=\"hist\" wrap=\"soft\">");
	out.println(pp.getHistory());
	out.println("</textarea></td></tr>");
	if(!(id.equals("") || pp.getHistory().equals(""))){
	    out.println("<tr><td></td><td align=\"center\">");
	    out.println("<input type=\"button\" "+
			" value=\"Printable History\" "+
			" onclick=\"javascript:if(checkNavigator()){window.open('javascript:document.write(opener.document.myForm.history.value);'"+
			",'Printable');}\" /></td></tr>");
	}
	//
	// Supplies
	out.println("<tr><td></td><td>");
	out.println("No more than 5,000 characters</td></tr>");
	out.println("<tr><td valign=\"top\" align=\"right\"><label for=\"supp\">Supplies: </label></td><td>");				
	out.println("<textarea name=\"supply\" rows=\"10\" cols=\"70\" "+
		    " onChange=\"checkTextLen(this,5000)\" id=\"supp\" wrap=\"soft\">");
	out.println(pp.getSupply());
	out.println("</textarea></td></tr>");
	if(!(id.equals("") || pp.getSupply().equals(""))){
	    out.println("<tr><td></td><td align=\"center\">");
	    out.println("<input type=\"button\" "+
			" value=\"Printable Supplies\" "+
			" onclick=\"javascript:if(checkNavigator()){window.open('javascript:document.write(opener.document.myForm.supply.value);'"+
			",'Printable');}\" /></td></tr>");
	}
	//
	// timeline
	out.println("<tr><td></td><td>");
	out.println("No more than 5,000 characters</td></tr>");
	out.println("<tr><td valign=\"top\" align=\"right\"><label for=\"tline\">Timeline:</label></td>");				
	out.println("<td>");
	out.println("<textarea name=\"timeline\" rows=\"10\" cols=\"70\" "+
		    " onchange=\"checkTextLen(this,5000)\" id=\"tline\" wrap=\"soft\">");
	out.println(pp.getTimeline());
	out.println("</textarea></td></tr>");
	if(!(id.equals("") || pp.getTimeline().equals(""))){
	    out.println("<tr><td></td><td align=\"center\">");
	    out.println("<input type=\"button\" "+
			" value=\"Printable Timeline\" "+
			" onclick=\"javascript:if(checkNavigator()){window.open('javascript:document.write(opener.document.myForm.timeline.value);'"+
			",'Printable');}\" /></td></tr>");
	}
	//
	if(pp.isNew()){
	    out.println("<tr>");
	    out.println("<td valign=\"top\" align=\"right\">");				
	    out.println("<input type=\"submit\" "+
			"name=\"action\" value=\"Save\" /></td>");
	    out.println("</tr>");
	}
	else{
	    out.println("<tr>");						
	    //
	    // if no program yet (it is new plan)
	    // can be duplicated only when a program is linked to it
	    //
	    out.println("<td valign=\"top\" align=\"right\">");				
	    out.println("<input type=\"submit\" "+
			"name=\"action\" value=\"Update\" /></td>");
	    //
	    out.println("</td><td>");	    
	    if(user.canDelete()){
		out.println("<input type=\"button\" "+
			    "name=\"action\" value=\"Delete\" onclick=\"return validateDelete();\" />");
	    }
	    // if no program is linked yet, user can add one
	    if(!pp.hasProgram()){
		out.println("<input type=\"button\" onclick=\"document.location.href='"+url+"Program.do?plan_id="+id+"'\" value=\"New Program\" />");
		out.println("<input type=\"button\" onclick=\"document.location.href='"+url+"Browse?plan_id="+id+"&needDuplication=y'\" value=\"New Program from Duplicate\" />");								
	    }
	    out.println("<input type=\"button\" value=\"Add Attachments\""+
			" onclick=\"document.location='"+url+
			"PromtFile.do?type=Plan&related_id="+id+
			"';\" />");						
	    out.println("</td></tr>");
	}
	out.println("</table>");
	out.println("</form>");
	//
	// list the last program linked to this plan (if any)
	if(!id.equals("")){
	    if(pp.hasFiles()){
		Helper.printFiles(out, url, pp.getFiles());
	    }								
	    Program prog = pp.getLastProgram();
	    if(prog  != null){
		out.println("<br>Last Program related to this plan: ");
		out.println("<a href=\""+url+"Program.do?id="+prog.getId()+
			    "&action=zoom\">"+prog.getTitle()+" ("+
			    prog.getSeason()+"/"+prog.getYear()+")</a><br />");
	    }
	}
	if(pp.hasHistory()){
	    Helper.writeHistory(out, "Plan Logs", pp.getHistorys()); 
	}			
	//
	out.println("<hr />");
	// Helper.writeWebFooter(out, url);
	out.print("</center></body></html>");
	out.close();
    }

}





































