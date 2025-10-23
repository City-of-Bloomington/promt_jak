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

@WebServlet(urlPatterns = {"/Duplicate.do","/Duplicate"})
public class Duplicate extends TopServlet{

    String allCategory = "";
    String allLead = "";
    String allArea = "";
    static Logger logger = LogManager.getLogger(Duplicate.class);
    /**
     * The main class method.
     *
     * Create an html page for the program form.
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
     * The main class method.
     *
     * Create an html page for the program form.
     * It also handles all the operations related to adding/updaing/delete
     * or just viewing a record.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	final int baseNumber = 1;
	String message = "";
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String id = "", plan_id=""; // plan id
	Enumeration values = req.getParameterNames();
	String name, value;
	String lead_id="", season="", season2="", year="",id_dup="", action=""; 
	boolean success = true;
	HttpSession session = null;
	session = req.getSession(false);
	User user = null;
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	if(user != null){
	    control = new Control(debug, user);
	}
	Program pp = new Program(debug);		
	String [] vals;
	while (values.hasMoreElements()){

	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	

	    if(name.equals("id")){
		pp.setId(value);
		id = value;
	    }
	    else if(name.equals("lead_id")){
		lead_id = value;
	    }
	    else if(name.equals("plan_id")){
		plan_id = value;
		// we add it later as this will be overidden when we do select
	    }						
	    else if(name.equals("season")){
		season = value;
	    }
	    else if(name.equals("season2")){
		season2 = value;
	    }						
	    else if(name.equals("year")){
		year = value;
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}
	String back = pp.doSelect();
	if(!back.equals("")){
	    message = back;
	    success = false;
	}
	LeadList leads = new LeadList(debug);
	back = leads.find();
	if(!back.equals("")){
	    message += back;
	    success = false;
	}		
	if(action.startsWith("Dup") && user.canEdit()){
	    pp.setRegDeadLine("");
	    pp.setPlan_id(plan_id); // as a new program we add the related plan id
	    pp.setCode("");
	    pp.setSeason(season);
	    pp.setSeason2(season2);
	    pp.setYear(year);
	    pp.setLead_id(lead_id);
	    back = pp.doDuplicate();
	    if(back.equals("")){
		id_dup = pp.getId();
	    }
	    else{
		message += back;
		success = false;
	    }
	    if(!id_dup.equals("")){
		if(control != null){
		    control.setId(id_dup);
		    back = control.doSaveAsOwner();
		    if(!back.equals("")){
			message += back;
			success = false;
		    }
		}
	    }
	}
	if(season.equals("") && !plan_id.equals("")){
	    Plan pn = new Plan(debug, plan_id);
	    back = pn.doSelect();
	    if(back.equals("")){
		if(pn.hasPrePlan()){
		    PrePlan pe = pn.getPrePlan();
		    season = pe.getSeason();
		}
	    }
	}
	out.println("<html>");
	out.println("<head><title>City of Bloomington Parks and "+
		    "Recreation</title>");
	Helper.writeWebCss(out, url);	
	//
	// This script validate 
	//
       	out.println(" <script>");
	out.println("  function validateNumber(x){             ");            
	out.println("   if(x.length > 0 ){                     ");
	out.println("     if(isNaN(x)){   ");
	out.println("      return false;                       ");
	out.println("     }                                  ");
	out.println("      return true;                        ");
	out.println("    }                                    ");
	out.println("  function validateString(x){             ");            
	out.println("     if((x.value.length > 0)){            "); 
	out.println("       var eq = 0;	                       ");
	out.println("    for(t = 0; t < x.value.length; t++){  ");
	out.println("    if (x.value.substring(t,t+1) != \" \") eq = 1;	");
	out.println("    	       }                       ");
	out.println("     if (eq == 0) {	               ");
	out.println("	      return false;		       ");
	out.println("            }                             ");
	out.println("	     return true;		       ");
	out.println("         }                                ");  
	out.println("	     return false;		       ");
	out.println("      }                                   ");  
	out.println(" function checkSelection(element){        ");
	out.println("   for(var i = 0; i < element.options.length; i++) ");
        out.println("    if (element.options[i].selected){     "); 
	out.println("      if(i > 0){                          ");
	out.println("         return true;                     ");
	out.println("         }                                ");
	out.println("       }                                  ");
	out.println("    return false;                         ");
	out.println("   }                                      ");
	out.println("  function validateForm() {               ");    
	out.println(" if(!checkSelection(document.myForm.year)){ ");
	out.println("  alert(\"Need to select a year for the new program\")");;
	out.println("   document.myForm.year.focus();         ");
	out.println("  return false;                           ");
	out.println("    }	                               ");
      	out.println(" if(!checkSelection(document.myForm.season)){ ");
	out.println("  alert(\"Need to select a season for the new "+
		    "program\")");
	out.println("   document.myForm.season.focus();        ");
	out.println("   return false;                          ");            
	out.println("    }	                                   ");
	out.println("   return true;                           ");
	out.println("  }	                                     ");
	out.println(" </script>                                ");   
	out.println("</head><body><center>    ");
	Helper.writeTopMenu(out, url);		
	if(!message.isEmpty()){
	    out.println(message+"<br />");
	}
	if(action.equals("")){
	    out.println("<h2>Duplicate Program</h2>");
	    out.println("To make a similar copy of this "+
			"program and all its related session, volunteer,"+
			" sponsor, budget, .. records <br />"+
			"<ul><li>Select the new year and season for the new program </li>"+
			"<li>Then click on Duplicate </li>"+
			"<li>You will get a message about the duplication process either success or failure.</li>"+
			"<li>If success a link to the "+
			"newly duplicated program will be provided </li>"+
			"<li>Now you can make "+
			" additional changes to the new program.</li></ul><br />");
	    out.println("<form name=\"myForm\" method=\"post\" "+
			"onsubmit=\" return validateForm()\">");
	    //
	    if(!id.equals("")) 
		out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	    if(!plan_id.equals("")) 
		out.println("<input type=\"hidden\" name=\"plan_id\" value=\""+plan_id+"\" />");						
	    //
	    // Title season year
	    out.println("<table width=\"100%\">");
	    out.println("<caption>Duplicate a program</caption>");
	    if(!plan_id.equals("")){
		out.println("<tr><td align=\"right\">Plan:");
		out.println("</td><td align=\"left\">");
		out.println("<a href=\""+url+"ProgPlan?id="+plan_id+
			    "&action=zoom"+
			    "\'>Related Plan "+plan_id+"</a>");
		out.println("</td></tr>");
	    }						
	    out.println("<tr><td align=\"right\"><b>Program:</b>");
	    out.println("</td><td align=\"left\">");
	    out.println("<a href=\""+url+"Program.do?id="+id+
			"&action=zoom"+
			"\">Related Program "+id+"</a>");						
	    out.println("</td></tr>");
	    out.println("<tr><td align=\"right\">");
	    out.println("<label for=\"season_id\">Season</label></td><td>");
	    out.println("<select name=\"season\">");
	    out.println("<option value=\""+season+"\" id=\"season_id\" selected=\"selected\">"+season+"\n");
	    out.println(Helper.allSeasons);
	    out.println("</select> ");
	    out.println(" <label for=\"season2_id\">Season 2</label>");
	    out.println("<select name=\"season2\">");
	    out.println("<option value=\""+season2+"\" id=\"season2_id\" selected=\"selected\" >"+season2+"\n");
	    out.println(Helper.allSeasons);
	    out.println("</select> ");						
	    out.println("<label for=\"year\">Year</label>");
	    out.println("<select name=\"year\" id=\"year\">");
	    int[] years = Helper.getFutureYears();
	    for(int yy:years){
		String selected = "";
		if(year.equals(""+yy)){
		    selected="selected=\"selected\"";
		}
		out.println("<option value=\""+yy+"\" "+selected+">"+yy+"\n");
	    }
	    out.println("</select></td></tr>");
	    out.println("<tr><td align=\"right\"><label for=\"lead_id\">Lead:");
	    out.println("</label></td><td align=\"left\">");
	    out.println("<select name=\"lead_id\" id=\"lead_id\">");
	    out.println("<option value=\"\"></option>");
	    if(leads != null){
		for(Lead one:leads){
		    String selected = "";
		    if(one.getId().equals(pp.getLead_id())){
			selected = "selected=\"selected\"";
		    }
		    else if(!one.isActive()) continue;
		    out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }
	    out.println("</select>");
	    out.println("</td></tr>");				
	    out.println("<tr><td colspan=\"2\" align=\"right\">"+
			"<input type=\"submit\" "+
			"name=\"action\" value=\"Duplicate\">&nbsp;&nbsp;"+
			"</td></tr>"); 
	    out.println("</form>");
	    out.println("</table>");
	}
	else if(!id_dup.equals("")){
	    out.println("<h2> Duplication completed </h2>");
	    out.println("<h3>Program duplicated successfully </h3>");
	    out.println("<h3>The new program ID: "+id_dup+" </h3>");
	    out.println("<hr />");
	    out.println("<li><a href=\""+url+"Program.do?id="+id_dup+
			"&action=zoom"+
			"\">Go to newly duplicated program </a></li>");
	}
	out.println("<hr />");
	out.println("</center>");
	out.print("</body></html>");
	out.flush();
	out.close();

    }

}























































