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

@WebServlet(urlPatterns = {"/VolTrain.do","/VolTrain"})
public class VolTrainServ extends TopServlet{

    static Logger logger = LogManager.getLogger(VolTrainServ.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	doPost(req, res);
    }
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	boolean success = true;

	Enumeration values = req.getParameterNames();
	String name, value;
	String id="", pid="", shift_id="";
	String action="", message="";
	String d_sun="",d_mon="",d_tue="",d_wed="",d_thu="",d_fri="",
	    d_sat="",d_all="",d_mon_fri="", days="";		
	//
	// shifts
	String [] vals;
	TypeList locations = new TypeList(debug, "locations");
	message = locations.find();
	VolTrain train = new VolTrain(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	

	    if(name.equals("id")){
		id = value;
		train.setId(value);
	    }
	    else if(name.equals("pid")){
		pid = value;
		train.setPid(value);
	    }
	    else if(name.equals("shift_id")){
		shift_id= value;
		train.setShift_id(value);
	    }			
	    else if(name.equals("date")){
		train.setDate(value);
	    }
	    else if(name.equals("notes")){
		train.setNotes(value);
	    }
	    else if(name.equals("other")){
		train.setOther(value);
	    }			
	    else if(name.equals("location_id")){
		train.setLocation_id(value);
	    }
	    else if(name.equals("tdays")){
		train.setTdays(value);
	    }
	    else if(name.equals("startTime")){
		train.setStartTime(value);
	    }
	    else if(name.equals("endTime")){
		train.setEndTime(value);
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
	if(!d_all.equals("")){
	    days="M - Su";
	    train.setDays(days);
	}
	else if(!d_mon_fri.equals("")){
	    days="Mon. - Fri.";
	    days="M - F";
	    train.setDays(days);
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
	    train.setDays(days);
	}		
	if(action.equals("Save")){
	    String back = train.doSave();
	    if(!back.equals("")){
		message += " Could not save "+train.getMessage();
		success = false;
	    }
	    else{
		message += train.getMessage();
		id = train.getId();
	    }
	}
	else if (action.equals("Update")){
	    String back = train.doUpdate();
	    if(!back.equals("")){
		message += " Could not update "+train.getMessage();
		success = false;
	    }
	    else{
		message += train.getMessage();
	    }
	}
	else if (action.equals("Delete")){
	    String back = train.doDelete();
	    if(!back.equals("")){
		message += " Could not delete "+train.getMessage();
		success = false;
	    }
	    else{
		train = new VolTrain(debug);
		train.setPid(pid);
		train.setShift_id(shift_id);
		message = "Deleted Successfully";
		id = "";
	    }
	}
	else if (action.startsWith("New")){
	    train = new VolTrain(debug);
	    train.setPid(pid);
	    train.setShift_id(shift_id);
	    id = "";
	}		
	else if (!id.equals("")){
	    String back = train.doSelect();
	    if(!back.equals("")){
		message += " Could not retreive data "+train.getMessage();
		success = false;
	    }
	    else{
		pid = train.getPid();
		shift_id = train.getShift_id();
		days = train.getDays();
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
	Program prog = train.getProgram();
	VolShift shift = train.getShift();
	if(!d_sun.equals("")) d_sun="checked";
	if(!d_mon.equals("")) d_mon="checked";
	if(!d_tue.equals("")) d_tue="checked";
	if(!d_wed.equals("")) d_wed="checked";
	if(!d_thu.equals("")) d_thu="checked";
	if(!d_fri.equals("")) d_fri="checked";
	if(!d_sat.equals("")) d_sat="checked";
	if(!d_mon_fri.equals("")) d_mon_fri="checked";
	if(!d_all.equals("")) d_all="checked";		
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
	out.println(" </script>                         ");
	out.println("/*]]>*/\n");							
	out.println("</head><body>");
	out.println("<center>");
	Helper.writeTopMenu(out, url);
	if(id.equals("")){
	    out.println("<h2>New Volunteer Training</h2>");
	}
	else 
	    out.println("<h2>Edit Volunteer Training</h2>");
	if(!message.equals("")){
	    out.println(message+"<br />");
	}
	//
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\" "+
		    "onSubmit=\"return validateForm()\">");
	out.println("<input type=\"hidden\" name=\"pid\" value=\"" + pid + "\" />");
	out.println("<input type=\"hidden\" name=\"shift_id\" value=\""+shift_id+"\" />");
	if(!id.equals(""))
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");				
	out.println("<table border=\"1\">");
	//
	// fields of the train form
	//
	if(prog != null){
	    // program, year, season
	    out.println("<tr><td align=\"right\"><b>Program:</b></td>");
	    out.println("<td align=\"left\"><a href=\""+url+"Program.do?action=zoom&id="+prog.getId()+"\">");
	    out.println(prog.getTitle()+" ("+prog.getSeasons()+"/"+prog.getYear()+")");
	    out.println("</a></td></tr>");
	}
	//
	out.println("<tr><td align=\"right\"><b>Related Shift:</b></td>");
	out.println("<td align=\"left\"><a href=\""+url+"VolShift.do?action=zoom&id="+train.getShift_id()+"\">");
	out.println(train.getShift_id());
	out.println("</a></td></tr>");		
	//
	// Date
	out.println("<tr><td align=\"right\"><b>Date:</b></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"date\" maxlength=\"10\" value=\""+train.getDate()+"\" size=\"10\" id=\"date\" />");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\" valign=\"top\">");
	out.println("</td><td><table width=\"100%\"><caption>Days</caption>");
	out.println("<tr><td align=\"left\">");
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
	out.println("<tr><td align=\"right\"><label for=\"start_pic\">Start, End Time:");
	out.println("</label></td><td align=\"left\">");
	out.println("<input type=\"button\" id=\"start_pic\" onClick=\""+
		    "window.open('"+url+"PickTime?id="+id+"&wtime=startTime&time="+java.net.URLEncoder.encode(train.getStartTime())+"','Time',"+
		    "'toolbar=0,location=0,"+
		    "directories=0,status=0,menubar=0,"+
		    "scrollbars=0,top=300,left=300,"+
		    "resizable=1,width=300,height=250');\""+
		    " value=\"Select Time\">");			
	out.println("<input type=\"text\" name=\"startTime\" maxlength=\"10\" value=\""+train.getStartTime()+"\" size=\"20\" readonly=\"readonly\" /> ");
	out.println("</td></tr>");
	//
	// Duties
	out.println("<tr><td align=\"right\"><label for=\"loc_id\">Location:</label></td><td align=\"Left\">");
	out.println("<select name=\"location_id\" id=\"loc_id\">");
	out.println("<option value=\"\"></option>");
	if(locations != null){
	    for(Type one:locations){
		String selected = one.getId().equals(train.getLocation_id())?"selected=\"selected\"":"";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");				
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"other\">Other:</label></td><td align=\"Left\">");
	out.println("<input type=\"text\" name=\"other\" maxlength=\"50\" value=\""+ train.getOther()+"\" size=\"50\" id=\"other\" />");
	out.println("</td></tr>");
		
	out.println("<tr><td align=\"right\" valign=\"top\"><label for=\"notes\">Comments:</label>");
	out.println("</td><td align=\"left\">");
	out.print("<textarea name=\"notes\" rows=\"5\" wrap "+
		  " cols=\"50\" id=\"notes\" >");
	out.print(train.getNotes());
	out.println("</textarea></td></tr>");
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
	    out.println("</td><td valign=\"top\">");
	    if(user.canDelete()){
		out.println("<input type=\"submit\" name=\"action\" "+
			    "onclick=\"return validateDeleteForm()\" "+
			    "value=\"Delete\">");
	    }
	    out.println("<input type=\"submit\" "+
			"name=\"action\" value=\"New Training\">");
			
	    out.println("</td></tr>");
	}
	out.println("</table>");
	out.println("<br />");		
	if(prog != null && prog.hasShifts()){
	    List<VolShift> shifts = prog.getShifts();
	    Helper.writeVolShifts(out,shifts,url, "Volunteer Shifts", false);
	}
	Helper.writeWebFooter(out, url);
	out.println("</center>");
	out.println("</body></html>");		
	out.close();
    }
		
}















































