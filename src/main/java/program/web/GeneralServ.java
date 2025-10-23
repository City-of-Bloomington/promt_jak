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

@WebServlet(urlPatterns = {"/General.do","/General"})
public class GeneralServ extends TopServlet{

    static Logger logger = LogManager.getLogger(GeneralServ.class);

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
	String id ="", message="";
	String d_sun="",d_mon="",d_tue="",d_wed="",d_thu="",d_fri="",
	    d_sat="",d_all="",d_mon_fri="", days="";
	String codeNeed="";
	//
	// reinitialize to blank
	//
	out.println("<html><head><title>General Listing</title>"); 
	boolean actionSet = false, success = true;
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
	General pp = new General(debug);
        String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("id")){ 
		id = value;
		pp.setId(value);
	    }
	    if(name.equals("title")){ 
		pp.setTitle(value);
	    }
	    if(name.equals("msg")){ 
		message = value; // from duplication
	    }			
	    else if(name.equals("season")){
		pp.setSeason(value);
	    }
	    else if(name.equals("time")){
		pp.setTime(value);
	    }
	    else if(name.equals("days")){
		pp.setDays(value);
	    }
	    else if(name.equals("cost")){
		pp.setCost(value);
	    }			
	    else if(name.equals("lead_id")){
		pp.setLead_id(value);
	    }			
	    else if(name.equals("year")){
		pp.setYear(value);
	    }
	    else if(name.equals("date")){
		pp.setDate(value);
	    }
	    else if(name.equals("code")){
		pp.setCode(value);
	    }
	    else if(name.equals("codeNeed")){
		pp.setCodeNeed(value);
		codeNeed = value;
	    }						
	    else if(name.equals("description")){
		pp.setDescription(value);
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
	    pp.setDays(days);
	}
	else if(!d_mon_fri.equals("")){
	    days="Mon. - Fri.";
	    days="M - F";
	    pp.setDays(days);
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
	    pp.setDays(days);
	}		
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
	else if(action.equals("Update")){
	    if(user.canEdit()){
		String back = pp.doUpdate();
		if(back.equals("")){
		    History one = new History(debug, id, "Updated","General",user.getId());
		    one.doSave();
		    message = "Updated successfully";
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
		String back = pp.doSave();
		if(back.equals("")){
		    id = pp.getId();
		    History one = new History(debug, id, "Created","General",user.getId());					
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
	else if(!id.isEmpty()){
	    String back = pp.doSelect();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    codeNeed = pp.getCodeNeed();
	    days = pp.getDays();
	    if(!days.equals("")){
		if(days.indexOf("Su") > -1 && // MON. - SUN.
		   days.indexOf("-") > -1) // 
		    d_all = "checked";
		else if(days.indexOf("F") > -1 && // MON. - FRI.
			days.indexOf("-") > -1 ) // MON - FRI
		    d_mon_fri = "checked";
		else{
		    if(days.indexOf("Su") > -1) d_sun="checked";
		    if(days.indexOf("M") > -1) d_mon="checked";
		    if(days.indexOf("Tu") > -1) d_tue="checked";
		    if(days.indexOf("W") > -1) d_wed="checked";
		    if(days.indexOf("Th") > -1) d_thu="checked";
		    if(days.indexOf("F") > -1) d_fri="checked";
		    if(days.indexOf("Sa") > -1) d_sat="checked";
		}
	    }			
	}
	if(!d_sun.equals("")) d_sun="checked=\"checked\"";
	if(!d_mon.equals("")) d_mon="checked=\"checked\"";
	if(!d_tue.equals("")) d_tue="checked=\"checked\"";
	if(!d_wed.equals("")) d_wed="checked=\"checked\"";
	if(!d_thu.equals("")) d_thu="checked=\"checked\"";
	if(!d_fri.equals("")) d_fri="checked=\"checked\"";
	if(!d_sat.equals("")) d_sat="checked=\"checked\"";
	if(!d_mon_fri.equals("")) d_mon_fri="checked=\"checked\"";
	if(!d_all.equals("")) d_all="checked=\"checked\"";
	if(!codeNeed.isEmpty()) codeNeed="checked=\"checked\"";
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
	Helper.writeWebCss(out, url);
	out.println("<script type=\"text/javascript\">");
	out.println("/*<![CDATA[*/");			
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
	out.println("	with(document.myForm){ ");  		
	out.println("	  if(title.value.length == 0){ ");  
	out.println("        alert(\"Title field is required\");     ");
	out.println("	     return false;	           ");
	out.println(" 	  }                              ");
	out.println("	  if(lead_id.tagName == 'SELECT' && lead_id.options.selectedIndex < 1){ ");
	out.println("        alert(\"Lead programmer is required\");     ");
	out.println("	     return false;	           ");
	out.println(" 	  }                              ");
	out.println("	  if(season.tagName == 'SELECT' && season.options.selectedIndex < 1){ ");
	out.println("        alert(\"season is required\");     ");
	out.println("	     return false;	           ");
	out.println(" 	  }                              ");
	out.println("	  if(year.tagName == 'SELECT' && year.options.selectedIndex < 1){ ");
	out.println("        alert(\"year is required\");     ");
	out.println("	     return false;	           ");
	out.println(" 	  }                              ");				
	out.println("	     return true;	           ");
	out.println(" 	 }                                 ");
	out.println("  }                                 ");		
	out.println("/*]]>*/\n");			
	out.println("	</script>                          ");   
	out.println("</head><body>");
	//
	out.println("<center>");
	Helper.writeTopMenu(out, url);		
	if(id.equals("")){
	    out.println("<h2>General Listing</h2>");
	    out.println("* Required field <br />");
	}
	else{
	    out.println("<h2>Edit General Listing "+id+"</h2>");
	}
	if(!message.equals("")){
	    out.println(message);
	    out.println("<br />");
	}		
	//
	out.println("<form name=\"myForm\" method=\"post\" onsubmit=\"return "+
		    "validateForm()\" id=\"form_id\" >");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	    if(!pp.getLead_id().isEmpty()){
		out.println("<input type=\"hidden\" name=\"lead_id\" value=\""+pp.getLead_id()+"\" />");
	    }
	}
	out.println("<table width=\"90%\" border=\"1\">");
	out.println("<caption>General Listing Info</caption>");
	if(id.equals("")){
	    out.println("<tr><td align=\"left\" colspan=\"2\"><b>Note: </b>Use this form for any listing that is not a 'Program' and you want it to appear in the 'Parks & Recs Brochure'</td></tr>");
	}
	//		
	out.println("<tr><td align=\"right\"><label for=\"gtitle\">Listing Title: </label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"title\" id=\"gtitle\" "+
		    "value=\""+pp.getTitle()+"\" maxlength=\"100\" size=\"70\" />*</td></tr>");
	out.println("</td></tr>");
	if(id.equals("")){
	    out.println("<tr><td align=\"right\"><label for=\"season\">Listing Season: </label></td><td>");						
	    out.println("<select name=\"season\" id=\"season\">");
	    out.println("<option value=\"-1\">Pick Season</option>");
	    out.println(Helper.allSeasons);
	    out.println("</select>* ");
	}
	else{
	    out.println("<tr><td align=\"right\"><b>Listing Season: </b></td><td>");		
	    out.println(pp.getSeason());
	}

	if(id.equals("")){
	    out.println("<label for=\"year\">Year:</label>");						
	    out.println("<select name=\"year\" id=\"year\">");
	    out.println("<option value=\"-1\">Pick Year</option>");
	    int[] years = Helper.getFutureYears();
	    for(int yy:years){
		out.println("<option value=\""+yy+"\">"+yy+"</option>");
	    }
	    out.println("</select>*");
	}
	else{
	    out.println("<b>Year:</b>");
	    out.println(pp.getYear());	
	}
	out.println("</td></tr>");
	out.println("<tr><td align=\"left\"><label for=\"lead\">Lead Programmer:</label></td>");
	out.println("<td align=\"left\">");
	if(id.equals("") || pp.getLead_id().equals("")){
	    out.println("<select name=\"lead_id\" id=\"lead\">");
	    out.println("<option value=\"\"></option>");
	    if(leads != null && leads.size() > 0){
		for(Lead one:leads){
		    if(one.isActive()){
			out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		    }
		}
	    }
	    out.println("</select>");
	}
	else{
	    Type lead = pp.getLead();
	    if(lead != null)
		out.println(lead);
	}
	out.println("</td></tr>");		
	out.println("<tr><td align=\"right\"><label for=\"code\">Code: </label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"code\" id=\"code\" "+
		    "value=\""+pp.getCode()+"\" maxlength=\"10\" size=\"10\" />*&nbsp;&nbsp;");
	out.println("<input type=\"checkbox\" id=\"coden\" name=\"codeNeed\" value=\"y\" "+codeNeed+"/><label for=\"coden\"> Code Needed</label>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"desc\">Description: </label></td><td align=\"left\">");		
	out.println("<textarea name=\"description\" rows=\"8\" cols=\"70\" id=\"desc\" wrap>"+pp.getDescription()+"</textarea></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"date\">Date: </label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"date\" id=\"date\" "+
		    "value=\""+pp.getDate()+"\" maxlength=\"10\" size=\"10\" /></td></tr>");
	out.println("<td align=\"left\"><table width=\"100%\">");
	out.println("<caption>Days</caption>");
	out.println("<tr><td align=\"left\">");
	out.println("<input type=\"checkbox\" name=\"d_sun\" value=\"y\" "+
		    d_sun+" id=\"dsu\"><label for=\"dsu\">Su</label>");
	out.println("</td><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_mon\" value=\"y\" "+
		    d_mon+" id=\"dmon\"><label for=\"dmon\">M</label>");
	out.println("</td><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_tue\" value=\"y\" "+
		    d_tue+" id=\"dtu\"><label for=\"dtu\">Tu</label>");
	out.println("</td><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_wed\" value=\"y\" "+
		    d_wed+" id=\"dwed\"><label for=\"dwed\">W</label>");
	out.println("</td><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_thu\" value=\"y\" "+
		    d_thu+" id=\"dth\"><label for=\"dth\">Th</label>");
	out.println("</td><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_fri\" value=\"y\" "+
		    d_fri+" id=\"dfr\"><label for=\"dfr\">F</label>");
	out.println("</td></tr><tr><td align=left>");
	out.println("<input type=\"checkbox\" name=\"d_sat\" value=\"y\" "+
		    d_sat+" id=\"dsa\"><label for=\"dsa\">Sa</label>");
	out.println("</td><td> </td><td colspan=2 align=left>");
	out.println("<input type=\"checkbox\" name=\"d_mon_fri\" value=\"y\" "+
		    d_mon_fri+" id=\"dmf\"><label for=\"dmf\">M-F</label>");
	out.println("</td><td colspan=2 align=left>");
	out.println("<input type=\"checkbox\" name=d_all value=\"y\" "+
		    d_all+" id=\"dall\"><label for=\"dall\">M-Su</label>");
	out.println("</td></tr></table></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"pickt\">Start, End Time: </label></td><td align=\"left\">");
	out.println("<input type=\"button\" id=\"pickt\" onclick=\""+
		    "window.open('"+url+"PickTime?id="+id+"&wtime=time&time="+java.net.URLEncoder.encode(pp.getTime())+"','Time',"+
		    "'toolbar=0,location=0,"+
		    "directories=0,status=0,menubar=0,"+
		    "scrollbars=0,top=300,left=300,"+
		    "resizable=1,width=300,height=250');\""+
		    " value=\"Time\">");		
	out.println("<input type=\"text\" name=\"time\" maxlength=\"20\" "+
		    "value=\""+ pp.getTime() + "\" size=\"16\" readonly=\"readonly\" />");		
	out.println("</td></tr>");		
	out.println("<tr><td align=\"right\"><label for=\"cost\">Cost: </label></td><td align=\"left\">");
	out.println("$<input type=\"text\" name=\"cost\" id=\"cost\" id=\"cost\" "+
		    "value=\""+pp.getCost()+"\" maxlength=\"8\" size=\"8\" /></td></tr>");		
	//
	out.println("</table></td></tr>");
		
	//
	out.println("<tr>");
	if(!id.equals("")){
	    if(user.canEdit()){
		out.println("<td valign=top align=\"right\">");					out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Update\">");
		out.println("</form></td>");
	    }
	    // delete
	    if(user.canDelete()){
		out.println("<td valign=\"top\" align=\"right\">");				
		out.println("<form name=\"myForm2\" method=\"post\" "+
			    "onSubmit=\"return validateForm2()\">");
		out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\">");
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Delete\" />&nbsp;");
		out.println("</form></td>");
	    }
	    out.println("</tr>");
	    List<Market> markets = null;						
	    if(pp.hasMarkets()){
		markets = pp.getMarkets();														
	    }
	    out.println("<tr><td valign=\"top\" align=\"right\">");
	    out.println("<button  "+
			"onclick=\"javascript:window.document.location.href='"+url+"Market.do?general_id="+id+"';return false\">Marketing</button>");
	    out.println("</td></tr>");
	}
	else { 
	    out.println("<tr><td valign=top align=right>");
	    if(user.canEdit()){
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Save\" /></td>");
	    }
	    out.println("</form></td></tr>");
	}
	out.println("</table>");
	if(pp.hasMarkets()){
	    List<Market> markets = pp.getMarkets();
	    for(Market market:markets){
		Helper.writeMarket(out, market, url);
	    }
	}
	if(pp.hasHistory()){
	    Helper.writeHistory(out, "Listing Logs", pp.getHistory()); 
	}		
	Helper.writeWebFooter(out, url);
	out.print("</body></html>");
	out.close();
    }


}





































