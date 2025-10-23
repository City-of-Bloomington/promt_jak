package program.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/VolunteerSearch"})
public class VolunteerSearch extends TopServlet{

    static Logger logger = LogManager.getLogger(VolunteerSearch.class);

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
	String id="", pid="", pre_train="", onsite="";
	String action="", message="";
	//
	// shifts
	String [] vals;

	TypeList categories =new TypeList(debug, "categories");
	message = categories.find();
	VolShiftList shifts = new VolShiftList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	

	    if(name.equals("id")){
		id = value;
		shifts.setId(id);
	    }
	    else if(name.equals("pid")){
		shifts.setPid(value);
	    }
	    else if(name.equals("lead_id")){
		shifts.setLead_id(value);
	    }
	    else if(name.equals("year")){
		shifts.setYear(value);
	    }
	    else if(name.equals("season")){
		shifts.setSeason(value);
	    }			
	    else if(name.equals("category_id")){
		shifts.setCategory_id(value);
	    }			
	    else if(name.equals("dateFrom")){
		shifts.setDateFrom(value);
	    }
	    else if(name.equals("dateTo")){
		shifts.setDateTo(value);
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
	    String str = url+"Login?source=Volunteer.do";
	    res.sendRedirect(str);
	    return;
	}
	if (!action.equals("")){
	    String back = shifts.find();
	    if(!back.equals("")){
		message += shifts.getMessage();
		success = false;
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
	// This script validate 
	//
	out.println("<html><head><title>Volunteering</title>");
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
	out.println("/*]]>*/\n");					
	out.println(" </script>                         ");   
	out.println("</head><body>");
	out.println("<center>");
	Helper.writeTopMenu(out, url);
	out.println("<h2>Volunteer Search  </h2>");
	if(!message.equals("")){
	    out.println(message);
	}		
	out.println("<br />");
	//
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\" "+
		    "onSubmit=\"return validateForm()\">");
	out.println("<table border=\"1\" width=\"70%\">");
	//
	// fields of the train form
	//
	// program, year, season
	out.println("<tr><td align=\"right\"><label for=\"id\">Volunteer Shift ID:</label></td>");
	out.println("<td align=\"left\"><input type=\"text\" name=\"id\" size=\"6\" value=\""+shifts.getId()+"\" id=\"id\" /></td></tr>"); 		
	out.println("<tr><td align=\"right\"><label for=\"prog_id\">Program ID:</label></td>");
	out.println("<td align=\"left\"><input type=\"text\" name=\"pid\" size=\"6\" value=\""+shifts.getPid()+"\" id=\"prog_id\" /></td></tr>"); 
	out.println("<tr><td align=\"right\"><label for=\"cat_id\">Category:</label></td>");
	out.println("<td align=\"left\">");
	out.println("<select name=\"category_id\" id=\"cat_id\">");
	out.println("<option value=\"\"></option>");
	if(categories != null){
	    for(Type one:categories){
		String selected = shifts.getCategory_id().equals(one.getId()) ? "selected=\"selected\"":"";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"lead_id\">Lead:</label></td>");
	out.println("<td align=\"left\">");
	out.println("<select name=\"lead_id\" id=\"lead_id\">");
	out.println("<option value=\"\"></option>");
	if(leads != null){
	    for(Lead one:leads){
		String selected = shifts.getLead_id().equals(one.getId()) ? "selected=\"selected\"":"";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select></td></tr>");		
	//
	out.println("<tr><td align=\"right\"><label for=\"season\">Season:</label></td>");		
	out.println("<td align=\"left\">");
	out.println("<select name=\"season\" id=\"season\"> ");
	out.println("<option value=\"\">All</option>");		
	if(!shifts.getSeason().equals(""))
	    out.println("<option value=\""+shifts.getSeason()+"\" selected>"+shifts.getSeason()+"\n");
	out.println(Helper.allSeasons);
	out.println("</select>");
	out.println("<label for=\"year\">Year</label>");
	out.println("<select name=\"year\" id=\"year\"> ");
	int years[] = Helper.getPrevYears();
	out.println("<option value=\"\">All\n");		
	for(int one:years){
	    String selected = (shifts.getYear().equals(""+one)) ?"selected=\"selected\"":"";
	    out.println("<option "+selected+">"+one+"</option>");
	}
	out.println("</select>");
	out.println("</td></tr>");		
	// Date
	out.println("<tr><td align=\"right\"><label for=\"dateFrom\">Date From:</label></td>");
	out.println("<td align=\"left\">");
	out.println("<input type=\"text\" name=\"dateFrom\" maxlength=\"10\" value=\""+shifts.getDateFrom()+"\" size=\"10\" id=\"dateFrom\" />");
	out.println("<label for=\"dateTo\"> to: </label>");
	out.println("<input type=\"text\" name=\"dateTo\" maxlength=\"10\" value=\""+shifts.getDateTo()+"\" size=\"10\" id=\"dateTo\" />");
	out.println("</td></tr>");
	//
	out.println("</table></td></tr>");
	out.println("<tr><td align=\"right\">"+
		    "<table width=\"90%\"><tr>");
	out.println("<td align=\"right\"><input type=\"submit\" "+
		    "name=\"action\" value=\"Submit\"></td>");
	out.println("<td align=\"right\">");
	out.println("<input type=button value=\"New Volunteer Shift\""+
		    " onclick=\"document.location='"+url+
		    "VolShift.do?';\"></input></td>");		
	out.println("</tr></table></td></tr>");
	out.println("</table>");
	out.println("<br />");
	if(!action.equals("")){
	    if(shifts.size() > 0){
		Helper.writeVolShifts(out, shifts, url, "Found "+shifts.size()+" record", true);
	    }
	    else{
		out.println("No record found <br />");
	    }
	}
	Helper.writeWebFooter(out, url);
	String dateStr = "{ nextText: \"Next\",prevText:\"Prev\", buttonText: \"Pick Date\", showOn: \"both\", navigationAsDateFormat: true, buttonImage: \""+url+"js/calendar.gif\"}";
	out.println("<script>");
	out.println("  $( \"#dateFrom\" ).datepicker("+dateStr+"); ");
	out.println("  $( \"#dateTo\" ).datepicker("+dateStr+"); ");
	out.println("</script>");			
	out.println("</center>");
	out.println("</body></html>");		
	out.close();
    }
		
}















































