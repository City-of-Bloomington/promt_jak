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

@WebServlet(urlPatterns = {"/Contact.do","/Contact"})
public class ContactServ extends TopServlet{

    static Logger logger = LogManager.getLogger(ContactServ.class);

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
	String name, value, action="", fromBrowse="";
	String id ="";
		
	//
	// reinitialize to blank
	//
	String message = "", finalMessage="";

	out.println("<html><head><title>City of Bloomington Parks and "+
		    "Recreation</title>"); 
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
	Contact cont = new Contact(debug);
        String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("id")){
		cont.setId(value);
		id = value;
	    }
	    if(name.equals("plan_id")){
		cont.setPlan_id(value);
	    }			
	    else if(name.equals("instructor")){
		cont.setName(value);
	    }
	    else if(name.equals("phone_h")){
		cont.setPhone_h(value);
	    }
	    else if(name.equals("fromBrowse")){
		fromBrowse = value;
	    }			
	    else if(name.equals("phone_c")){
		cont.setPhone_c(value);
	    }			
	    else if(name.equals("phone_w")){
		cont.setPhone_w(value);
	    }
	    else if(name.equals("email")){
		cont.setEmail (value);
	    }
	    else if(name.equals("address")){
		cont.setAddress (value);
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}
	if(id.equals("")){ // for new records
	    fromBrowse="y";
	}
	//
       	if(action.equals("Delete")){
	    if(user.canDelete()){
		String back = cont.doDelete();
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
		String back = cont.doUpdate();
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
		String back = cont.doSave();
		if(back.equals("")){
		    id = cont.getId();
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
	    String back = cont.doSelect();
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
	    out.println("<h2>New Instructor</h2>");
	}
	else{
	    out.println("<h2>Edit Instructor "+id+"</h2>");
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
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	}
	// Plan
	out.println("<table width=\"90%\" border=\"1\" >");
	out.println("<caption>instructor Info</caption>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"center\">");
	out.println("<table>");
	out.println("<tr><td colspan=\"2\" align=\"center\" bgcolor=\"navy\" "+
		    "><h3><font color=\"white\">"+
		    "Instructor Contact Info </font></h3></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"inst_id\">Instructor: </label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"instructor\" id=\"inst_id\" "+
		    "value=\""+cont.getName()+"\" maxlength=\"60\" size=\"60\"></td></tr>"); 
	out.println("<tr><td align=\"right\"><label for=\"w_phone\">Work Phone:</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"phone_w\" "+
		    "value=\""+cont.getPhone_w()+"\" maxlength=\"12\" size=\"12\" id=\"w_phone\" /><label for=\"c_phone\">Cell:</label>");
	out.println("<input type=\"text\" name=\"phone_c\" "+
		    "value=\""+cont.getPhone_c()+"\" maxlength=\"12\" size=\"12\" id=\"c_phone\" />");
	out.println(" <label for=\"h_phone\">Home:</label>");
	out.println("<input type=\"text\" name=\"phone_h\" "+
		    "value=\""+cont.getPhone_h()+"\" maxlength=\"12\" size=\"12\" id=\"h_phone\" /></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"email\">Email:</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"email\" id=\"email\" "+
		    "value=\""+cont.getEmail()+"\" maxlength=\"50\" size=\"50\" /></td></tr>");
	out.println("<tr><td valign=\"top\" align=\"right\"><label for=\"addr\">Address:</label></td><td align=\"left\">");
	out.print("<textarea name=\"address\" cols=\"50\" rows=\"4\" id=\"addr\" "+
		  " onchange='checkTextLen(this,200)' wrap>");
	out.print(cont.getAddress());
	out.println("</textarea></td></tr>");
	if(!id.equals("")){
	    //
	    // if no program yet (it is new plan)
	    // can be duplicated only when a program is linked to it
	    //
	    out.println("<tr><td valign=\"top\" align=\"right\">");				
	    out.println("<input type=\"submit\" "+
			"name=\"action\" value=\"Update\" /></td>");
	    if(user.canDelete() && !cont.hasPlans()){
		out.println("<td>");				
		out.println("<form name=\"myForm2\" method=\"post\" "+
			    "onSubmit=\"return validateForm2()\">");
		out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Delete\">&nbsp;");
		out.println("</form></td>");
	    }
	    out.println("</tr>");
	}
	else { 
	    out.println("<tr><td valign=\"top\" align=\"right\">");
	    if(user.canEdit()){
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Save\" /></td>"+
			    "<td align=\"right\" valign=\"top\">" );
	    }
	    out.println("</form></td>");
	    out.println("</tr>");	    
	}
	out.println("</table>");
	out.println("</td></tr></table>");
	out.println("<br />");
	if(fromBrowse.equals("")){
	    out.println("<a href=\"javascript:window.close();\">Close</a>");
	}
	else if(cont.hasPlans()){
	    List<Plan> plans = cont.getPlans();
	    out.println("<table border=\"1\" width=\"70%\"><caption>Plans associated with this Instructor</caption>");
	    for(Plan one:plans){
		out.println("<tr><td><a href=\""+url+"ProgPlan?action=zoom&id="+one.getId()+"\">"+one.getId()+"</a></td>");
		out.println("<td>"+one+"</td></tr>");
	    }
	    out.println("</table>");
	}
	Helper.writeWebFooter(out, url);
	//
	out.print("</center></body></html>");
	out.close();
    }

}




































