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

@WebServlet(urlPatterns = {"/PrePlan","/PrePlan.do"})
public class PrePlanServ extends TopServlet{

    static Logger logger = LogManager.getLogger(PrePlanServ.class);

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
	//
	// reinitialize to blank
	//
	out.println("<html><head><title>Promt</title>"); 
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
	PrePlan pp = new PrePlan(debug);
        String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("id")){ 
		id = value;
		pp.setId(value);
	    }
	    if(name.equals("prev_id")){ 
		pp.setPrev_id(value);
	    }
	    if(name.equals("msg")){ 
		message = value; // from duplication
	    }			
	    else if(name.equals("season")){
		pp.setSeason(value);
	    }
	    else if(name.equals("name")){
		pp.setName(value);
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
	    else if(name.equals("explained")){
		pp.setExplained(value);
	    }			
	    else if(name.equals("determinants")){
		pp.setDeterminants(vals); // array
	    }
	    else if(name.equals("pre_eval_text")){
		pp.setPre_eval_text(value);
	    }
	    else if(name.equals("market_considers")){
		pp.setMarket_considers(vals); // array
	    }
	    else if(name.equals("fulfilled")){
		pp.setFulfilled(value);
	    }
	    else if(name.equals("action")){
		action = value;
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
	}
	else if(action.equals("Update")){
	    if(user.canEdit()){
		String back = pp.doUpdate();
		if(back.equals("")){
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
	else if(!id.equals("")){
	    String back = pp.doSelect();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}
	//
	Helper.writeWebCss(out, url);
	out.println("<script type=\"text/javascript\">");
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
	out.println("	if((document.myForm.program.value.length == 0)){ ");  
	out.println("        alert(\"Title field must be entered\");     ");
	out.println("	     return false;	           ");
	out.println(" 	    }                              ");
	out.println("	     return true;	           ");
	out.println(" 	 }                                 ");
	out.println(" function showStatus(){               ");
	// display in status line 
	out.println("    defaultStatus ='"+message+"';     "); 
	out.println("    }                                 ");
	out.println("  function checkNavigator(){		         ");
	out.println("  var appl = navigator.appName;                     ");
	out.println("  return true; }			         ");
	out.println("	</script>                          ");   
	out.println("</head><body onLoad=\"showStatus();\">");
	//
	out.println("<center>");
	Helper.writeTopMenu(out, url);
	if(id.equals("")){
	    out.println("<h2>Add New Pre Plan</h2>");
	}
	else{
	    out.println("<h2>Edit Pre Plan "+id+"</h2>");
	}
	if(!message.equals("")){
	    out.println(message);
	    out.println("<br />");
	}		
	if(!pp.isFulfilled()){
	    out.println("<p><b>These are some determinants need to be fulfilled before you move to the plan page</b></p>");
	}
	//
	out.println("<form name=\"myForm\" method=\"post\" onsubmit=\"return "+
		    "validateForm()\" id=\"form_id\" >");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	    out.println("<input type=\"hidden\" name=\"name\" value=\""+pp.getName()+"\" />");
	    out.println("<input type=\"hidden\" name=\"lead_id\" value=\""+pp.getLead_id()+"\" />");					
	}
	out.println("<input type=\"hidden\" name=\"prev_id\" value=\""+pp.getPrev_id()+"\" />");		

	out.println("* indicates a required field<br />");	
	out.println("<table width=\"90%\" border=\"1\">");
	out.println("<caption>PrePlan Info</caption>");

	if(id.equals("")){
	    out.println("<tr><td align=\"right\" width=\"30%\"><label for=\"pid\">Program Title: </label></td><td>");	    
	    out.println("<input type=\"text\" name=\"name\" "+
			"value=\""+pp.getName()+"\" maxlength=\"128\" size=\"70\" id=\"pid\"/></td></tr>");
	    out.println("<tr><td align=\"right\"><label for=\"lead_id\">Program Lead: </label></td><td>");
	    out.println("<select name=\"lead_id\" id=\"lead_id\">");
	    out.println("<option value=\"\">Pick One</option>");
	    if(leads != null){
		for(Lead one:leads){
		    if(one.isActive()){
			out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		    }
		}
	    }
	    out.println("</select></td></tr>");				    
	}
	else{
	    out.println("<tr><td align=\"right\" width=\"30%\"><b>Program Title: </b></td><td>");	    
	    out.println(pp.getName()+"</td></tr>");
	    out.println("<tr><td align=\"right\"><b>Lead: </b></td><td>");
	    out.println(pp.getLead()+"</td></tr>");
	}
	out.println("<tr><td align=\"right\">*<label for=\"season\">Program Season: </label></td><td>");
	out.println("<select name=\"season\" id=\"season\">");
	out.println("<option value=\""+pp.getSeason()+"\" selected>"+
		    pp.getSeason()+"\n");
	out.println(Helper.allSeasons);
	out.println("</select> ");
	out.println("*<label for=\"year\">Year:</label>");
	out.println("<select name=\"year\" id=\"year\">");
	out.println("<option value=\""+pp.getYear()+"\" selected=\"selected\">"+
		    pp.getYear()+"</option>");					
	int[] years = Helper.getFutureYears();
	for(int yy:years){
	    out.println("<option value=\""+yy+"\">"+yy+"</option>");
	}
       	out.println("</select></td></tr>");
	out.println("<tr><td colspan=\"2\">Note: The following list of determinants must be checked off before adding or editing the plan:</td></tr> ");
	out.println("<tr><td valign=\"top\" align=\"right\" width=\"30%\">* <b>Is your program based on:</b></td><td>");
	String chk = (pp.getDeterminants().indexOf("1") > -1)?"checked=\"checked\"":"";
	out.println("<input type=\"checkbox\" name=\"determinants\" "+chk+" value=\"1\"  id=\"c1\"/><label for=\"c1\">Conceptual foundations of play, recreation and leisure.</label><br />");
	chk = (pp.getDeterminants().indexOf("2") > -1)?"checked=\"checked\"":"";		
	out.println("<input type=\"checkbox\" name=\"determinants\" "+chk+" value=\"2\" id=\"c2\" /><label for=\"c2\">Organizational agency philosophy, mission and vision, and goals and objectives.</label><br />");
	chk = (pp.getDeterminants().indexOf("3") > -1)?"checked=\"checked\"":"";		
	out.println("<input type=\"checkbox\" "+chk+" name=\"determinants\" value=\"3\"  id=\"c3\"/><label for=\"c3\">Constituent interests and desired needs.</label><br />");
	chk = (pp.getDeterminants().indexOf("4") > -1)?"checked=\"checked\"":"";		
	out.println("<input type=\"checkbox\" "+chk+" name=\"determinants\" value=\"4\"  id=\"c4\"/><label for=\"c4\">Creation of a constituent-centered culture.</label> <br />");
	chk = (pp.getDeterminants().indexOf("5") > -1)?"checked=\"checked\"":"";		
	out.println("<input type=\"checkbox\" "+chk+" name=\"determinants\" value=\"5\" id=\"c5\" /><label for=\"c5\">Experiences desirable for clientele.</label><br />");
	chk = (pp.getDeterminants().indexOf("6") > -1)?"checked=\"checked\"":"";	 
	out.println("<input type=\"checkbox\" "+chk+" name=\"determinants\" value=\"6\"  id=\"c6\" /><label for=\"c6\">Community opportunity.</label><br /></td></tr>");
	out.println("<tr><td colspan=\"2\">* <label for=\"pre_eval\">Pre Evaluation Notes:</b> Based on previous evaluations, what did you change for this year and how did you use your marketing materials</label></td></tr>");
	out.println("<tr><td colspan=\"2\">");		
	out.println("<textarea name=\"pre_eval_text\" rows=\"8\" cols=\"70\" wrap id=\"pre_eval\">"+pp.getPre_eval_text()+"</textarea></td></tr>");
	out.println("<tr><td valign=\"top\" align=\"right\">* <b>Have you considered?</b></td><td>");
	chk = (pp.getMarket_considers().indexOf("1") > -1)?"checked=\"checked\"":"";		
	out.println("<input type=\"checkbox\" "+chk+" name=\"market_considers\" value=\"1\" id=\"m1\" /><label for=\"m1\">Tracking trends.</label><br />");
	chk = (pp.getMarket_considers().indexOf("2") > -1)?"checked=\"checked\"":"";				
	out.println("<input type=\"checkbox\" "+chk+" name=\"market_considers\" value=\"2\" id=\"m2\"/><label for=\"m2\">Reviewing previous evaluations.</label><br />");
	chk = (pp.getMarket_considers().indexOf("3") > -1)?"checked=\"checked\"":"";				
	out.println("<input type=\"checkbox\" "+chk+" name=\"market_considers\" value=\"3\" id=\"m3\"/><label for=\"m3\">Duplication of service.</label><br />");
	chk = (pp.getMarket_considers().indexOf("4") > -1)?"checked=\"checked\"":"";				
	out.println("<input type=\"checkbox\" "+chk+" name=\"market_considers\" value=\"4\" id=\"m4\"/><label for=\"m4\">Cost recovery.</label></td></tr>");
	out.println("<tr><td valign=\"top\" align=\"right\">* <label for=\"exp\">Explain:</label></td>");
	out.println("<td align=\"left\">");
	out.println("<textarea name=\"explained\" rows=\"8\" id=\"exp\" cols=\"70\" wrap>"+pp.getExplained()+"</textarea></td></tr>");		
	out.println("<tr>");
	if(!id.equals("")){
	    if(user.canEdit()){
		out.println("<td valign=top align=right>");				
		out.println("<input type=submit "+
			    "name=\"action\" value=\"Update\">");
		out.println("</td>");
		if(pp.isFulfilled()){
		    out.println("<td valign=top "+
				"align=right><input type=\"button\" "+
				"onclick=\"document.location='"+url+"ProgPlan?id="+id+"';\" "+
				"value=\"Go to Plan Page\"></input></td>");
		}
	    }
	    // delete
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
	else { 
	    out.println("<td valign=\"top\" align=\"right\">");
	    if(user.canEdit()){
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Save\"></td>"+
			    "<td align=\"right\" valign=\"top\">" +
			    "<input type=\"reset\" name=\"reset\" value=\"Clear\" />");
	    }
	    out.println("</td>");
	}
	out.println("</tr>");
	out.println("</table>");
	out.println("</form>");
	Helper.writeWebFooter(out, url);
	out.print("</body></html>");
	out.close();
    }


}





































