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

@WebServlet(urlPatterns = {"/Inclusive"})
public class Inclusive extends TopServlet{

    static Logger logger = LogManager.getLogger(Inclusive.class);

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
	String id = "";
	String message = "";
	boolean success = true;
	Enumeration values = req.getParameterNames();
	String name, value;
	String pyear="", season="", ptitle="", action=""; 
	String consult_ada="",training="",train_aware="",train_basics="",
	    train_consider="",train_behave="",train_trip="",
	    train_other="", comments="", consult_pro="",
	    market="",site_visit="", consult="",sign="",prov_sign="";
	Inclusion inc = new Inclusion(debug);
	String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if(name.equals("id")){
		id = value;
		inc.setId(value);
	    }
	    else if(name.equals("consult_ada")){
		inc.setConsult_ada(value);
	    }
	    else if(name.equals("consult_pro")){
		inc.setConsult_pro(value);
	    }			
	    else if(name.equals("training")){
		inc.setTraining(value);
	    }
	    else if(name.equals("train_aware")){
		inc.setTrain_aware(value);
	    }
	    else if(name.equals("train_basics")){
		inc.setTrain_basics(value);
	    }
	    else if(name.equals("train_consider")){
		inc.setTrain_consider(value);
	    }
	    else if(name.equals("train_behave")){
		inc.setTrain_behave(value);
	    }
	    else if(name.equals("train_trip")){
		inc.setTrain_trip(value);
	    }
	    else if(name.equals("train_other")){
		inc.setTrain_other(value);
	    }
	    else if(name.equals("comments")){
		inc.setComments(value);
	    }
	    else if(name.equals("market")){
		inc.setMarket(value);
	    }
	    else if(name.equals("site_visit")){
		inc.setSite_visit(value);
	    }
	    else if(name.equals("consult")){
		inc.setConsult(value);
	    }
	    else if(name.equals("sign")){
		inc.setSign(value);
	    }
	    else if(name.equals("prov_sign")){
		inc.setProv_sign(value);
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
	if(action.equals("Add")){
	    //
	    String back = inc.doSave();
	    if(!back.equals("")){
		message += " Could not save "+inc.getMessage();
		success = false;
	    }
	    else{
		message += inc.getMessage();
	    }			
	}
	else if (action.equals("Update")){
	    String back = inc.doUpdate();
	    if(!back.equals("")){
		message += " Could not update "+inc.getMessage();
		success = false;
	    }
	    else{
		message += inc.getMessage();
	    }			

	}
	else if (action.equals("Delete")){
	    String back = inc.doDelete();
	    if(!back.equals("")){
		message += " Could not delete "+inc.getMessage();
		success = false;
	    }
	    else{
		message += inc.getMessage();
	    }			

	}
	else if (action.equals("zoom")){
	    String back = inc.doSelect();
	    if(!back.equals("")){
		message += " Could not retreive data "+inc.getMessage();
		success = false;
	    }
	    else{
		message += inc.getMessage();
	    }			
	}
	else if(action.equals("")){
	    String back = inc.doSelect();
	    if(back.equals("") && inc.hasRecord()){
		action = "zoom";
	    }
	}
	Program prog = inc.getProgram();
	if(!inc.getConsult_ada().equals("")) consult_ada = "checked=\"checked\"";
	if(!inc.getConsult_pro().equals("")) consult_pro = "checked=\"checked\"";
	if(!inc.getTraining().equals("")) training = "checked=\"checked\"";
	if(!inc.getTrain_aware().equals("")) train_aware = "checked=\"checked\"";
	if(!inc.getTrain_basics().equals("")) train_basics = "checked=\"checked\"";
	if(!inc.getTrain_consider().equals("")) train_consider = "checked=\"checked\"";
	if(!inc.getTrain_behave().equals("")) train_behave = "checked=\"checked\"";
	if(!inc.getTrain_trip().equals("")) train_trip = "checked=\"checked\"";
	if(!inc.getMarket().equals("")) market = "checked=\"checked\"";
	if(!inc.getSite_visit().equals("")) site_visit = "checked=\"checked\"";
	if(!inc.getConsult().equals("")) consult = "checked=\"checked\"";
	if(!inc.getSign().equals("")) sign = "checked=\"checked\"";
	if(!inc.getProv_sign().equals("")) prov_sign = "checked=\"checked\"";
	//
	out.println("<html>");
	out.println("<head><title>City of Bloomington Parks and "+
		    "Recreation</title>");
	Helper.writeWebCss(out, url);
	//
	// This script validate 
	//
       	out.println(" <script>");
	out.println("  function validateInteger(x) {     ");            
	out.println("	if((x == \"0\")|| (x==\"1\") || (x == \"2\")|| (x==\"3\") || (x == \"4\")|| (x==\"5\") || (x == \"6\")|| (x==\"7\") || (x == \"8\")|| (x==\"9\")){  ");
	out.println("	            return true;        ");
	out.println(" 	        }                       ");
	out.println("	       return false;	        ");
	out.println(" 	   }                            ");
	out.println("  function validateString(x){      ");            
	out.println("     if((x.value.length > 0)){     "); 
	out.println("       var eq = 0;	                ");
	out.println("    for(t = 0; t < x.value.length; t++){  ");
	out.println("    if (x.value.substring(t,t+1) != \" \") eq = 1;	");
	out.println("    	       }                ");
	out.println("     if (eq == 0) {	        ");
	out.println("	      return false;		");
	out.println("            } ");
	out.println("	     return true;		");
	out.println("         }  ");  
	out.println("	     return false;		");
	out.println("      }  ");  
	out.println(" function checkSelection(element){   ");
	out.println("   for(var i = 0; i < element.options.length; i++) ");
        out.println("    if (element.options[i].selected){  "); 
	out.println("      if(i > 0){ ");
	out.println("         return true;  ");
	out.println("         }     ");
	out.println("       }  ");
	out.println("    return false;  ");
	out.println("   }               ");
	out.println(" function validateTextarea(ss){   ");
        out.println("    if (ss.value.length > 500 ){  "); 
	out.println("       alert(\"Text Area should not be more than"+
		    " 500 chars\"); ");
	out.println("       ss.value = ss.value.substring(0,149); ");
	out.println("       }                  ");
	out.println("   }                      ");
	out.println("  function validateForm() {                   ");    
	out.println("   return true;                    ");
	out.println("  }	                        ");
	out.println("  function validateDeleteForm(){   ");            
	out.println("  var x = false ;                  ");
	out.println("   x = window.confirm(\"Are you sure you want to delete\"); ");
	out.println("   return x;                       ");
	out.println(" }	                                ");
	out.println(" </script>                         ");   
	out.println("</head><body><center>");
	Helper.writeTopMenu(out, url);		
	out.println("<h2>Accessibility Services in PROMT</h2>");

	if(!inc.hasRecord()){
	    out.println("<h2>New Inclusive Recreation Request</h2>");
	}
	else 
	    out.println("<h2>Edit Inclusive Recreation Request</h2>");
	if(!message.equals("")){
		out.println(message+"<br />");
	}
	//
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\" "+
		    "onSubmit=\"return validateForm()\">");
	out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	//
	// program, year, season
       	out.println("<table border=\"1\">");
	out.println("<caption>Inclusive Items</caption>");
	out.println("<tr><td><b>Program:</b>");
	out.println("<a href=\""+url+"Program.do?action=zoom&id="+id+"\">");
	out.println(prog.getTitle()+" ("+prog.getSeasons()+"/"+prog.getYear()+")");
	out.println("</a></td></tr>");
	if(action.equals("")){
	    out.println("<tr><td>");
	    out.println("<b>Definition, Examples and Guidelines for "+
			"Requesting Inclusive Services</b>");
	    out.println("</td></tr>");
	    out.println("<tr><td>");
	    out.println("<p>Definition: Inclusion is individuals with and without disabilities participating in recreational activities together.  In order for individuals with disabilities to participate as fully as possible, reasonable accommodations are provided.</p>");
	    out.println("<p>Examples of reasonable accommodations: ");
	    out.println("<ul>");
	    out.println("<i>Alternative Formats- Some examples include but are not limited to: Braille, audio versions of text, and large print documents</li>");
	    out.println("<li>Leisure companion-is a service in which participants with disabilities are partnered with a volunteer to provide additional support during an activity.</li>");
	    out.println("<li>Sign Language Interpreter</li>");
	    out.println("<li>Equivalent Facilitation- When a program or activity is not accessible for all an alternative experience can be provided.</li>");
	    out.println("</ul></p>");
	    out.println("</td></tr>");
	}
	out.println("<tr><td>");

	out.println("<p>Please check the boxes that you need for your program. In addition, contact Inclusion Coordinator for additional discussion on any items.</p>");
	out.println("<ul>");
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"consult_pro\" value=\"y\" "+consult_pro+
		    " id=\"pro\" />");
	out.println("<label for=\"pro\">I need a Programming Consultation to focus on accessibility/usability of programs and activities to community members with disabilities</label> </li>");		
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"consult_ada\" value=\"y\" "+consult_ada+
		    " id=\"ada\"/>");
	out.println("<label for=\"ada\">I want/need an Accessibility/ADA Consultation to focus on physical access to facility, park or other site</label></li>");
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"training\" value=\"y\" "+
		    training+" id=\"train\" />");
	out.println("<label for=\"train\">I need Staff Training on: - Can be either tailored to a program area or one of the pre set topics. Topics include, but not limited to: </label>");
	out.println("<ul><li>");
	out.println("<input type=\"checkbox\" name=\"train_aware\" value=\"y\" "+
		    train_aware+" id=\"aware\" />");
	out.println("<label for=\"aware\">Disability Awareness and customer service<label></li>");
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"train_basics\" value=\"y\" "+
		    train_basics+" id=\"basic\"/>");
	out.println("<label for=\"basic\">Inclusion the Basics</label></li>");
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"train_consider\" value=\"y\" "+
		    train_consider+" id=\"con\"/>");
	out.println("<label for=\"con\">Program Considerations for Inclusion</label></li>");
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"train_behave\" value=\"y\" "+
		    train_behave+" id=\"behave\"/>");
	out.println("<label for=\"behave\">Behavior Management</lable></li>");
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"train_trip\" value=\"y\" "+
		    train_trip+" id=\"trip\" />");
	out.println("<label for=\trip\">Accessibility considerations when planning field trips</label></li>");
	out.println("<li><label for=\"other\">Other Specify below:</label><br />");

	out.println("<textarea name=\"train_other\" rows=\"5\" cols=\"70\" wrap=\"wrap\" id=\"other\">");
	out.println(inc.getTrain_other());
	out.println("</textarea></li>");
	out.println("</ul>");
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"market\" value=\"y\" "+
		    market+" id=\"market\" />");
	out.println("<label for=\"market\">I would like this Marketed to Disability Groups/Populations</label></li>");
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"site_visit\" value=\"y\" "+
		    site_visit+" id=\"visit\"/>");
	out.println("<label for=\"visit\">I would like a site visit/walk through - If the program site has already been determined, this would provide information on any simple maintenance necessary to increase access to individuals with disabilities and identify any removable barriers.</label></li>");
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"consult\" value=\"y\" "+
		    consult+" id=\"cons\"/>");
	out.println("<label for=\"cons\">I need a Programming Consultation to focus on accessibility/usability of programs and activities to community members with disabilities </label></li>");
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"sign\" value=\"y\" "+
		    sign+" id=\"sign\" />");
	out.println("<label for=\"sign\">I want a sign Language Interpreter</label></li>");
	out.println("<li>");
	out.println("<input type=\"checkbox\" name=\"prov_sign\" value=\"y\" "+
		    prov_sign+" id=\"psign\"/>");
	out.println("<label for=\"psign\" I need to know if I should provide a sign language interpreter</label></li>");
	out.println("</ul></td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"comm\">Comments</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.print("<textarea name=\"comments\" rows=\"5\" cols=\"70\" wrap id=\"comm\">");
	out.print(inc.getComments());
	out.println("</textarea>");
	out.println("</td></tr>");
	//
	if(action.equals("") || 
	   action.equals("Delete")){
	    out.println("<tr><td align=right>");
	    if(user.canEdit()){
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Add\">");
	    }
	    out.println("</td></tr>"); 
	    out.println("</form>");
	}
	else{ // add zoom update
	    out.println("<tr><td>");
	    if(user.canEdit()){
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Update\">");
	    }
	    out.println("</form>");
	    //
	    if(user.canDelete()){
		out.println("<form name=myForm2 method=post "+
			    "onsubmit=\"return validateDeleteForm()\">");
		out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
		out.println("<input type=\"submit\" name=\"action\" "+
			    "value=\"Delete\">");
		out.println("</form>");					
	    }
	    out.println("</td></tr>");
	}	    
	out.println("</table>");
	Helper.writeWebFooter(out, url);
	out.println("</center>");
	out.print("</body></html>");
	out.flush();
	out.close();
    }

}























































