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

@WebServlet(urlPatterns = {"/Facility.do","/Facility"})
public class FacilityServ extends TopServlet{

    static Logger logger = LogManager.getLogger(FacilityServ.class);
    final static String iniFile =  "";
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
     * Create and html page for the Facility form.
     * @param req request input parameters
     * @param res reponse output parameters
     * 
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	PrintWriter out;
	out = res.getWriter();
	Enumeration values = req.getParameterNames();
	final int baseNumber = 1; // interment 
	String name, value, action="";
	//
	// reinitialize to blank
	//
	String brStatement="", facility="", schedule="";
	String closings="", other="",id="", role="";
	String stat="", stat2="",stat3="",stat4="",stat5="",
	    shedule2="",statement="",schedule2="", message="",
	    other2="", other3="",other4="",other5="";
	//
	LeadList leads = null;
	out.println("<html><head><title>City of Bloomington Parks and "+
		    "Recs</title>"); 
	boolean success = true;
        String [] vals;
	HttpSession session = null;
	session = req.getSession(false);
	User user = null;
	Facility fc = new Facility(debug);
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("name")){
		fc.setName(value);
	    }
	    else if(name.equals("id")){
		id = value;
		fc.setId(value);
	    }
	    else if(name.equals("lead_id")){
		fc.setLead_id(value);
	    }			
	    else if(name.equals("schedule")){
		fc.setSchedule(value);
	    }
	    else if(name.equals("closings")){
		fc.setClosings(value);
	    }
	    else if(name.equals("other")){
		fc.setOther(value);
	    }
	    else if(name.equals("statement")){
		fc.setStatement(value);
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}
	if(action.startsWith("Delete")){
	    if(user.canDelete()){
		String back = fc.doDelete();
		if(!back.equals("")){
		    message += " Error deleting records "+back;
		    success = false;
		    logger.error(back);
		}
		else{
		    message = "Record deleted";
		}
	    }
	    else{
		message += " You can not delete  ";
		success = false;
	    }
	}
	else if(action.startsWith("Update")){
	    //
	    if(user.canEdit()){
		String back = fc.doUpdate();
		if(!back.equals("")){
		    message += " Error updating records "+back;
		    success = false;
		    logger.error(back);
		}
		else{
		    History one = new History(debug, id, "Updated","Facility",user.getId());
		    one.doSave();					
		    message = "Updated successfully";
		}
	    }
	    else{
		message += " You can not update ";
		success = false;
	    }
	}
	else if(action.startsWith("Save")){
	    //
	    if(user.canEdit()){
		String back = fc.doSave();
		if(!back.equals("")){
		    message += " Error saving "+back;
		    success = false;
		    logger.error(back);
		}
		else{
		    id = fc.getId();
		    History one = new History(debug, id, "Created","Facility",user.getId());
		    one.doSave();
		    message = "Saved successfully";
		}
	    }
	    else{
		message += " You can not save ";
		success = false;
	    }
	}
	else if(action.startsWith("Start")){
	    //
	    // we just set the vars to blank
	    // 
	    fc = new Facility(debug);
	}
	else if(!id.isEmpty()){
	    //
	    //
	    String back = fc.doSelect();
	    if(!back.equals("")){
		message += " Error retreiving data "+back;
		success = false;
		logger.error(back);
	    }
	}
	
	if(true){
	    leads = new LeadList(debug);
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}
	//
	// This script validates textareas and facility
	//
	Helper.writeWebCss(out, url);
	out.println("<script>");
	out.println("    function validateTextarea(ss){              ");
	out.println("      if (ss.value.length > 10000){              ");
	out.println("       alert(\"Maximum Character number is 10000\"); ");
	out.println("        ss.value = ss.value.substring(0,9999);     ");
	out.println("                    }                            ");
	out.println("                }                                ");
	//
	out.println("     function validateForm2() {                  ");     
	out.println("				                                  ");
	out.println("    answer = window.confirm (\"Are you sure you want to Delete ?\") ");	
	out.println("	 return answer;		                            ");
	out.println("	 }		                                        ");
	out.println("    function validateTextarea2(ss) {               ");
	out.println("      if (ss.value.length > 1000) {                 ");
	out.println("       alert(\"Maximum number of characters is 250\"); ");
	out.println("        ss.value = ss.value.substring(0,249);      ");
	out.println("                    }                              ");
	out.println("                }                                  ");
	out.println("   function validateTextarea3(ss) {                ");
	out.println("     if (ss.value.length > 4000) {                 ");
	out.println("      alert(\"Maximum number of characters is 4000\"); ");
	out.println("        ss.value = ss.value.substring(0,4000);     ");
	out.println("        }                                          ");
	out.println("    }                                              ");
	out.println("  function validateForm(){         ");            
	out.println("	if((document.myForm.facility.value.length == 0)){ ");  
	out.println("        alert(\"Facility field must be entered\"); ");
	out.println("	     return false;	              ");
	out.println(" 	    }                           ");
	out.println("	     return true;	                ");
	out.println(" 	 }                              ");
	out.println("	</script>                         ");   
	out.println("</head><body>");
	out.println("<center>");
	Helper.writeTopMenu(out, url);		
	//
	if(id.equals("")){
	    out.println("<h2>New Facility</h2>");
	}
	else{
	    out.println("<h2>Edit Facility "+id+"</h2>");
	}
	if(!message.equals("")){
	    out.println(message+"<br />");
	}
	out.println("<b>* indicates required field</b><br />");

	// box it in 
	//
	out.println("<form name=\"myForm\" method=\"post\" onSubmit=\"return "+
		    "validateForm()\" id=\"form_id\" >");
	if(!fc.getId().equals(""))
	    out.println("<input type=\"hidden\" name=\"id\" value=\"" + fc.getId()+"\" />");
	// facility
	out.println("<center><table border=\"1\" align=\"center\" width=\"90%\">");
	out.println("<caption>facility Info</caption>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td>");
	out.println("<label for=\"f_name\">Facility Name* </label>");
	out.println("<input type=\"text\" name=\"name\" "+
		    "value=\""+fc.getName()+"\" maxlength=\"80\" size=\"80\" "+
		    " required=\"required\" id=\"f_name\" />"); 
	out.println("</td></tr>");
	//
	// statement
	out.println("<tr><td>");
	out.println("<label for=\"type\">Facility Type </label>");
	out.println("<select name=\"type\" id=\"type\">");
	for(String str:Facility.facility_types){
	    String selected = "";
	    if(fc.getType().equals(str)){
		selected="selected=\"selected\"";
	    }
	    out.println("<option value=\""+str+"\" "+selected+">"+str+"</option>");
	}
	out.println("</select></td></tr>");				
	out.println("<tr><td><label for=\"lead_id\">Lead Programmer:</label>");
	out.println("<select name=\"lead_id\" id=\"lead_id\">");
	out.println("<option value=\"\"></option>");
	if(leads != null && leads.size() > 0){
	    for(Lead one:leads){
		if(one.getId().equals(fc.getLead_id())){ 
		    out.println("<option selected=\"selected\" value=\""+one.getId()+"\">"+one+"</option>");
		}
		else if(one.isActive()){
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"broch\">Brochure Statement</label>");
	out.println("Limit: No more than 10,000 "+
		    " characters <br />"); 
	out.println("<br />");
	out.print("<textarea name=\"statement\" rows=\"20\" cols=\"80\" wrap"+
		  " onkeyup =\"validateTextarea(this)\" id=\"broch\">");
	out.print(fc.getStatement()); 
	out.println("</textarea>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"sched\">Operations Schedule</label>");
	out.println("Limit:No more than 4,000 "+
		    " characters"); 
	out.println("<br />");
	out.print("<textarea name=\"schedule\" rows=\"10\" cols=\"80\" wrap"+
		  " onkeyup=\"validateTextarea3(this)\" id=\"sched\">");
	out.print(fc.getSchedule()); /* The pre-existing comment, if any */
	out.println("</textarea>");
	out.println("</td></tr>");
	//
	// closings
	out.println("<tr><td>");
	out.println("<label for=\"close\">Closings</label>");
	out.println("<font color=\"green\" size=\"-1\">Limit: No more than 4000 "+
		    " characters </font>"); 
	out.println("<br />");
	out.print("<textarea name=\"closings\" rows=\"10\" cols=\"80\" wrap"+
		  " onkeyup=\"validateTextarea3(this)\" id=\"close\">");
	out.print(fc.getClosings()); /* The pre-existing comment, if any */
	out.println("</textarea>");
	out.println("</td></tr>");
	//
	// other
	out.println("<tr><td>");
	out.println("<label for=\"other\">Other</label>");
	out.println("Limit:No more than 10,000 "+
		    " characters"); 
	out.println("<br />");
	out.print("<textarea name=\"other\" rows=\"10\" cols=\"80\" wrap"+
		  " onkeyup=\"validateTextarea(this)\" id=\"other\">");
	out.print(fc.getOther()); 
	out.println("</textarea>");
	out.println("</td></tr>");
								
	out.println("<tr><td>");
	//
	if(!id.equals("")){
	    if(user.canEdit()){
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Update\">");
	    }
	    out.println("<input type=\"button\" value=\"New Media Request\" "+
			" onclick=\"document.location='"+url+
			"MediaRequest?facility_id="+fc.getId()+
			"';\" />");
	    out.println("<input type=button value=\"New Marketing\""+
			" onclick=\"document.location='"+url+
			"Market.do?facility_id="+fc.getId()+
			"';\"></input>");			
	    out.println("<input type=\"submit\" "+
			"name=\"action\" value=\"Start New\">" +
			"&nbsp;&nbsp;");
	    out.println("<input type=\"button\" value=\"Add Attachments\""+
			" onclick=\"document.location='"+url+
			"PromtFile.do?type=Facility&related_id="+fc.getId()+
			"';\" />");
	    out.println("</form>");
	    //
	    // delete
	    if(user.canDelete()){
		out.println("<form name=\"myForm2\" method=\"post\" "+
			    "onSubmit=\"return validateForm2()\">");
		out.println("<input type=\"hidden\" name=\"id\" value=\""+fc.getId()+"\" />");
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Delete\" />"+
			    "&nbsp;");
		out.println("</form>");
	    }
	}
	else { // Save
	    out.println("<input type=\"submit\" "+
			"name=\"action\" value=\"Save\"></td>" +
			"<td valign=\"top\" align=\"right\">"+
			"<input type=\"reset\" "+
			"name=\"reset\" value=\"Clear\" />" +
			"&nbsp;</td>"); 
	    out.println("</form>");
	}
	out.println("</td></tr>");
	out.println("</table>");
	if(!id.equals("")){
	    List<Market> markets = fc.getMarkets();
	    Market lastMarket = null;
	    if(markets != null){
		if(markets.size() > 1){
		    out.println("<ul>");
		}
		for(Market one:markets){
		    if(lastMarket == null){
			lastMarket = one;
		    }
		    else{
			String str = ""; 
			if(url != null)
			    str = "Marketing <a href=\""+url+"Market.do?id="+one.getId()+"&action=zoom\">"+one.getId()+"</a>";
			String season = one.getSeason();
			String year = one.getYear();
			if(!season.equals("")){
			    str += " ("+season+"/"+year+")";
			}
			out.println("<li>"+str+"</li>");
		    }
		}
		if(markets.size() > 1){
		    out.println("</ul>");
		}								
	    }
	    if(lastMarket != null)
		Helper.writeMarket(out, lastMarket, url);
	}
	out.println("<div id=\"accordion\">");
	
	if(fc.hasFiles()){
	    out.println("<h3>Uploaded Files </h3>");
	    out.println("<div>");
	    Helper.printFiles(out, url, fc.getFiles());
	    out.println("</div>");
        }
	if(fc.hasMediaRequests()){
	    out.println("<h3>Media Requests </h3>");
	    out.println("<div>");	    
	    Helper.writeMediaRequests(out,"Media Requests", fc.getMediaRequests(), url);
	    out.println("</div>");
	}	
	//
	if(fc.hasHistory()){
	    out.println("<h3>Update Logs </h3>");
	    out.println("<div>");
	    Helper.writeHistory(out, " ", fc.getHistory());
	    out.println("</div>");
	}
	out.println("</div>");
	Helper.writeWebFooter(out, url);
	out.println("<br />");
	out.print("</body></html>");
	out.close();

    }


}





































