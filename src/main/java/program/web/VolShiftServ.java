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

@WebServlet(urlPatterns = {"/VolShift.do","/VolShift"})
public class VolShiftServ extends TopServlet{

    static Logger logger = LogManager.getLogger(VolShiftServ.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	doPost(req, res);
    }
    /**
     * Create a volunteer shifts form.
     * Handles view, add, update and delete operations on the form
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
	boolean success = true;

	Enumeration values = req.getParameterNames();
	String name, value;
	String id="", pid="", pre_train="", onsite="";
	String action="", message="";
	String d_sun="",d_mon="",d_tue="",d_wed="",d_thu="",d_fri="",
	    d_sat="",d_all="",d_mon_fri="", days="";
	//
	// shifts
	String [] vals;
	TypeList categories =new TypeList(debug, "categories");
	message = categories.find();
	VolShift shift = new VolShift(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	

	    if(name.equals("id")){
		id = value;
		shift.setId(value);
	    }
	    else if(name.equals("pid")){
		pid = value;
		shift.setPid(value);
	    }
	    else if(name.equals("lead_id")){
		shift.setLead_id(value);
	    }			
	    else if(name.equals("duties")){
		shift.setDuties(value);
	    }
	    else if(name.equals("training")){
		if(value.equals("onsite"))
		    shift.setOnsite("y");
		else
		    shift.setPre_train("y");
	    }
	    else if(name.equals("days")){
		shift.setDays(value);
	    }
	    else if(name.equals("notes")){
		shift.setNotes(value);
	    }
	    else if(name.equals("volCount")){
		shift.setVolCount(value);
	    }
	    else if(name.equals("category_id")){
		shift.setCategory_id(value);
	    }
	    else if(name.equals("date")){
		shift.setDate(value);
	    }
	    else if(name.equals("title")){
		shift.setTitle(value);
	    }			
	    else if(name.equals("startTime")){
		shift.setStartTime(value);
	    }
	    else if(name.equals("endTime")){
		shift.setEndTime(value);
	    }
	    else if(name.equals("d_sun")){
		d_sun = value;
	    }
	    else if(name.equals("d_mon")){
		d_mon = value;
	    }
	    else if(name.equals("d_tue")){
		d_tue = value;
	    }
	    else if(name.equals("d_wed")){
		d_wed = value;
	    }
	    else if(name.equals("d_thu")){
		d_thu = value;
	    }
	    else if(name.equals("d_fri")){
		d_fri = value;
	    }
	    else if(name.equals("d_sat")){
		d_sat = value;
	    }
	    else if(name.equals("d_mon_fri")){
		d_mon_fri = value;
	    }
	    else if(name.equals("d_all")){
		d_all = value;
	    }			
	    else if(name.equals("action")){
		action = value;
	    }
	}
	if(!d_all.equals("")){
	    days="M - Su";
	    shift.setDays(days);
	}
	else if(!d_mon_fri.equals("")){
	    days="Mon. - Fri.";
	    days="M - F";
	    shift.setDays(days);
	}
	else{
	    boolean in = false;
	    if(!d_sun.equals("")){
		days = "Su";
		in = true;
	    }
	    if(!d_mon.equals("")){
		if(in) days += " ";
		days += "M";				
		in = true;
	    }
	    if(!d_tue.equals("")){
		if(in) days += " ";
		days += "Tu";				
		in = true;
	    }
	    if(!d_wed.equals("")){
		if(in) days += " ";
		days += "W";				
		in = true;
	    }
	    if(!d_thu.equals("")){
		if(in) days += " ";
		days += "Th";				
		in = true;
	    }
	    if(!d_fri.equals("")){
		if(in) days += " ";
		days += "F";				
		in = true;
	    }
	    if(!d_sat.equals("")){
		if(in) days += " ";
		days += "Sa";				
	    }
	    shift.setDays(days);
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
	if(action.equals("Save")){
	    String back = shift.doSave();
	    if(!back.equals("")){
		message += " Could not save "+shift.getMessage();
		success = false;
	    }
	    else{
		message += shift.getMessage();
		id = shift.getId();
		History one = new History(debug, id, "Created","Volunteer",user.getId());
		one.doSave();					
	    }
	}
	else if (action.equals("Update")){
	    String back = shift.doUpdate();
	    if(!back.equals("")){
		message += " Could not update "+shift.getMessage();
		success = false;
	    }
	    else{
		message += shift.getMessage();
		History one = new History(debug, id, "Updated","Volunteer",user.getId());
		one.doSave();					
	    }
	}
	else if (action.equals("Delete")){
	    String back = shift.doSelect();
	    pid = shift.getPid();
	    back = shift.doDelete();
	    if(!back.equals("")){
		message += " Could not delete "+shift.getMessage();
		success = false;
	    }
	    else{
		message = "Deleted Successfully";
	    }
	}
	else if (action.startsWith("New")){
	    shift = new VolShift(debug);
	    shift.setPid(pid);
	    id = "";
	}		
	else if (!id.equals("")){
	    String back = shift.doSelect();
	    if(!back.equals("")){
		message += " Could not retreive data "+shift.getMessage();
		success = false;
	    }
	    else{
		pid = shift.getPid();
		days = shift.getDays();
		if(!days.equals("")){
		    days = days.toUpperCase(); // backward comptability
		    if(days.indexOf("SU") > -1 && // MON. - SUN.
		       days.indexOf("-") > -1) // 
			d_all = "checked";
		    else if(days.indexOf("F") > -1 && // MON. - FRI.
			    days.indexOf("-") > -1 ) // MON - FRI
			d_mon_fri = "checked";
		    else{
			if(days.indexOf("SU") > -1) d_sun="checked";
			if(days.indexOf("M") > -1) d_mon="checked";
			if(days.indexOf("TU") > -1) d_tue="checked";
			if(days.indexOf("W") > -1) d_wed="checked";
			if(days.indexOf("TH") > -1) d_thu="checked";
			if(days.indexOf("F") > -1) d_fri="checked";
			if(days.indexOf("SA") > -1) d_sat="checked";
		    }
		}
	    }
	}
	Program prog = shift.getProgram();
	if(!d_sun.equals("")) d_sun="checked";
	if(!d_mon.equals("")) d_mon="checked";
	if(!d_tue.equals("")) d_tue="checked";
	if(!d_wed.equals("")) d_wed="checked";
	if(!d_thu.equals("")) d_thu="checked";
	if(!d_fri.equals("")) d_fri="checked";
	if(!d_sat.equals("")) d_sat="checked";
	if(!d_mon_fri.equals("")) d_mon_fri="checked";
	if(!d_all.equals("")) d_all="checked";		
	LeadList leads = null;
	if(true){
	    leads = new LeadList(debug);
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}		
	//
	// This script validate 
	//
	out.println("<html><head><title>Volunteer Shift</title>");
	Helper.writeWebCss(out, url);
	out.println("<script type='text/javascript'>");
	out.println("/*<![CDATA[*/");						
	out.println(" function checkSelection(element){   ");
	out.println("   for(var i = 0; i < element.options.length; i++) ");
        out.println("    if (element.options[i].selected){  "); 
	out.println("      if(i > 0){ ");
	out.println("         return true;  ");
	out.println("         }     ");
	out.println("       }  ");
	out.println("    return false;  ");
	out.println("   }               ");
	out.println("  function validateForm(){         ");    
	out.println("  return true;                     ");
	out.println("  }	                        ");
	out.println("  function validateDeleteForm(){   ");            
	out.println("  var x = false;                   ");
	out.println("   x = window.confirm(\"Are you sure you want to delete\"); ");
	out.println("   return x;                       ");
	out.println(" }	                                ");
	out.println("/*]]>*/\n");					
	out.println(" </script>                         ");   
	out.println("</head><body>");
	out.println("<center>");
	Helper.writeTopMenu(out, url);
	if(id.equals("")){
	    out.println("<h2>New Volunteer Shift</h2>");
	}
	else 
	    out.println("<h2>Edit Volunteer Shift "+id+"</h2>");
	if(!message.equals("")){
		out.println(message);
	}		
	out.println("<br />");
	//
	out.println("<form name=myForm method=\"post\" id=\"form_id\" "+
		    "onSubmit=\"return validateForm()\">");
	if(!pid.equals(""))
	    out.println("<input type=hidden name=\"pid\" value=\"" + pid + "\" />");
	if(!id.equals(""))
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	if(!shift.getLead_id().equals("")){
	    out.println("<input type=\"hidden\" name=\"lead_id\" value=\""+shift.getLead_id()+"\" />");
	}
	out.println("<table border=\"1\">");
	//
	// fields of the train form
	//
	// program, year, season
	if(prog != null){
	    out.println("<tr><td align=\"right\"><b>Program:</b></td>");
	    out.println("<td align=\"left\"><a href=\""+url+"Program.do?action=zoom&id="+prog.getId()+"\">");
	    out.println(prog.getTitle()+" ("+prog.getSeasons()+"/"+prog.getYear()+")");
			
	    out.println("</a></td></tr>");
	    out.println("<tr><td align=\"right\"><b>Lead</b></td><td>"+prog.getLead()+"</td></tr>");
	}
	else if(id.equals("")){
	    out.println("<tr><td align=\"right\"><label for=\"lead_id\">Lead:</label></td>");
	    out.println("<td align=\"left\">");			
	    out.println("<select name=\"lead_id\" id=\"lead_id\">");
	    out.println("<option value=\"\">Pick One</option>");
	    if(leads != null){
		for(Lead one:leads){
		    if(one.isActive())
			out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }
	    out.println("</select></td></tr>");
	}
	else{
	    out.println("<tr><td align=\"right\"><b>Lead:</b></td>");
	    out.println("<td align=\"left\">"+shift.getLead());
	    out.println("</td></tr>");
	}
	if(prog == null){
	    out.println("<tr><td align=\"right\"><label for=\"sh_title\">Title:</label></td>");
	    out.println("<td align=\"left\">");
	    out.println("<input type=\"text\" name=\"title\" maxlength=\"70\" value=\""+shift.getTitle()+"\" size=\"70\" id=\"sh_title\" /></td></tr>");			
	}
	//
	// volCount
	out.println("<tr><td align=\"right\"><label for=\"volCount\"># of Volunteers:");
	out.println("</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"volCount\" maxlength=\"3\" value=\""+shift.getVolCount()+"\" size=\"3\" required=\"required\" id=\"volCount\" />");
	//
	// Date
	out.println(" <label for=\"date\">Date:</label>");
	out.println("<input type=\"text\" name=\"date\" maxlength=\"10\" value=\""+shift.getDate()+"\" size=\"10\" id=\"date\" /></td></tr>");
	out.println("<tr><td align=\"right\" valign=\"top\"><b>Day(s):");
	out.println("</b></td><td><table width=\"100%\"><caption>Days</caption>");
	out.println("<tr><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_sun\" value=\"y\" "+
		    d_sun+" id=\"d_sun\"/><label for=\"d_sun\">Su</label>");
	out.println("</td><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_mon\" value=\"y\" "+
		    d_mon+" id=\"d_mon\"/><label for=\"d_mon\">M</label>");
	out.println("</td><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_tue\" value=\"y\" "+
		    d_tue+" id=\"d_tue\"/><label for=\"d_tue\">Tu</label>");
	out.println("</td><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_wed\" value=\"y\" "+
		    d_wed+" id=\"d_wed\"/><label for=\"d_wed\">W</label>");
	out.println("</td><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_thu\" value=\"y\" "+
		    d_thu+" id=\"d_thu\"/><label for=\"d_thu\">Th</label>");
	out.println("</td><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_fri\" value=\"y\" "+
		    d_fri+" id=\"d_fri\"/><label for=\"d_fri\">F</label>");
	out.println("</td></tr><tr><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_sat\" value=\"y\" "+
		    d_sat+" id=\"d_sat\"/><label for=\"d_sat\">Sa</label>");
	out.println("</td><td> </td><td colspan=2 align=left>");
	out.println("<input type=\"checkbox\" name=\"d_mon_fri\" value=\"y\" "+
		    d_mon_fri+" id=\"d_mon_fri\" /><label for=\"d_mon_fri\">M-F</label>");
	out.println("</td><td colspan=2 align=left>");
	out.println("<input type=\"checkbox\" name=\"d_all\" value=\"y\" "+
		    d_all+" id=\"d_all\" /><label for=\"d_all\">M-Su</label>");
	out.println("</td></tr></table></td></tr>");
	//
	// Start, End time
	out.println("<tr><td align=\"right\"><label for=\"pic_time\">Start Time:");
	out.println("</label></td><td align=\"left\">");
	out.println("<input type=\"button\" id=\"pic_time\" onClick=\""+
		    "window.open('"+url+"PickTime?id="+id+"&wtime=startTime&time="+java.net.URLEncoder.encode(shift.getStartTime())+"','Time',"+
		    "'toolbar=0,location=0,"+
		    "directories=0,status=0,menubar=0,"+
		    "scrollbars=0,top=300,left=300,"+
		    "resizable=1,width=300,height=250');\""+
		    " value=\"Start Shift\">");				
	out.println("<input type=\"text\" name=\"startTime\" maxlength=\"20\" value=\""+shift.getStartTime()+"\" size=\"20\" readonly=\"readonly\" /> ");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"pic_end\">End Time:");
	out.println("</label></td><td align=\"left\">");
	out.println("<input type=\"button\" id=\"pic_end\" onClick=\""+
		    "window.open('"+url+"PickTime?id="+id+"&wtime=endTime&time="+java.net.URLEncoder.encode(shift.getEndTime())+"','Time',"+
		    "'toolbar=0,location=0,"+
		    "directories=0,status=0,menubar=0,"+
		    "scrollbars=0,top=300,left=300,"+
		    "resizable=1,width=300,height=250');\""+
		    " value=\"End Shift\">");				
	out.println("<input type=\"text\" name=\"endTime\" maxlength=\"20\" value=\""+shift.getEndTime()+"\" size=\"20\" readonly=\"readonly\" /> ");
	out.println("</td></tr>");				
	//
	// Duties
	out.println("<tr><td align=\"right\" valign=\"top\"><label for=\"duties\">Duties:</label></td><td align=\"Left\">");
	out.println("<textarea name=\"duties\" cols=\"50\" rows=\"5\" id=\"dutiers\" >"+ shift.getDuties()+"</textarea>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\" valign=\"top\"><label for=\"notes\">Comments:</label>");
	out.println("</td><td align=\"left\">");
	out.print("<textarea name=\"notes\" rows=\"5\" wrap=\"wrap\" id=\"notes\" "+
		  " cols=\"50\" >");
	out.print(shift.getNotes());
	out.println("</textarea></td></tr>");
	//
	onsite = shift.getOnsite().equals("")?"":"checked=\"checked\"";
	out.print("<tr><td><td align=\"left\"><input type=\"radio\" name=\"training\" value=\"onsite\" id=\"onsite\" "+
		  onsite+" /><label for=\"onsite\">On Site Training Provided, or</label></td></tr>");
	pre_train = shift.getOnsite().equals("")?"checked=\"checked\"":"";
	out.print("<tr><td><td align=\"left\"><input type=\"radio\" "+
		  "name=\"training\" "+pre_train+
		  " value=\"pre_train\" id=\"pre_train\" /><label for=\"pre_train\">Requires Advanced Training/Orientation.</label>");
	out.println("<font size=\"-1\">(if yes complete add training "+
		    "details below)</font></td></tr>");
	//
	if(id.equals("")){
	    if(user.canEdit()){
		out.println("<tr><td align=\"right\"><input type=\"submit\" "+
			    "name=\"action\" value=\"Save\"></td></tr>");
	    }
	}
	else{ // add zoom update
	    out.println("<tr><td align=\"right\">");
	    if(user.canEdit()){
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Update\">");
	    }
	    out.println("<td valign=\"top\" align=\"left\">");
	    out.println("<input type=\"submit\" "+
			"name=\"action\" value=\"New Shift\">");
			
	    out.println("<input type=button value=\"New Training\" "+
			" onclick=\"document.location='"+url+
			"VolTrain.do?pid="+pid+"&shift_id="+id+ 
			"';\" />");			
	    if(user.canDelete()){
		out.println("<input type=\"submit\" name=\"action\" "+
			    "onclick=\"return validateDeleteForm()\" "+
			    "value=\"Delete\">");
	    }
	    out.println("</td></tr>");
	}
	out.println("</table>");
	out.println("<br />");
	if(shift.hasTraining()){
	    List<VolTrain> trains = shift.getTrains();
	    Helper.writeVolTrains(out,trains,url, "Training for this shift");
	}
	if(prog != null && prog.hasShifts()){
	    List<VolShift> shifts = prog.getShifts();
	    // shifts.remove(shift); // remove current shift
	    if(shifts.size() > 0)
		Helper.writeVolShifts(out,shifts,url, "Volunteer Shifts", false);
	}
	if(shift.hasHistory()){
	    Helper.writeHistory(out, "Shift Volunteer Logs", shift.getHistory()); 
	}			
	Helper.writeWebFooter(out, url);
	out.println("</center>");
	out.println("</body></html>");		
	out.close();
    }
		
}















































