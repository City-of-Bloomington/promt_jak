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

@WebServlet(urlPatterns = {"/GeneralSearch"})
public class GeneralSearch extends TopServlet{

    static Logger logger = LogManager.getLogger(GeneralSearch.class);
    /**
     * The main class method.
     *
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
	String id="";
	String action="", message="";
	//
	// shifts
	String [] vals;
	TypeList categories =new TypeList(debug, "categories");
	message = categories.find();
	GeneralList gens = new GeneralList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	

	    if(name.equals("id")){
		id = value;
		gens.setId(id);
	    }
	    else if(name.equals("season")){
		gens.setSeason(value);
	    }
	    else if(name.equals("year")){
		gens.setYear(value);
	    }			
	    else if(name.equals("dateFrom")){
		gens.setDateFrom(value);
	    }
	    else if(name.equals("dateTo")){
		gens.setDateTo(value);
	    }
	    else if(name.equals("lead_id")){
		gens.setLead_id(value);
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
	LeadList leads = null;
	if(true){
	    leads = new LeadList(debug);
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}				
	if (!action.equals("")){
	    String back = gens.find();
	    if(!back.equals("")){
		message += gens.getMessage();
		success = false;
	    }
	}
	//
	// This script validate 
	//
	out.println("<html><head><title>General Listings Search</title>");
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
	out.println("<h2>General Listings Search</h2>");
	if(!message.equals("")){
	    out.println(message+"<br />");
	}		
	//
	out.println("<form name=\"myForm\" method=\"post\" "+
		    "onSubmit=\"return validateForm()\">");
	out.println("<table width=\"80%\" border=\"1\">");
	out.println("<caption>Search Options</caption>");
	//
	out.println("<tr><td align=\"right\"><label for=\"id\">Listing ID:</label></td>");
	out.println("<td align=\"left\"><input type=\"text\" name=\"id\" size=\"6\" value=\""+gens.getId()+"\" id=\"id\" /></td></tr>"); 		
	out.println("<tr><td align=\"right\"><label for=\"season\">Season:</label></td>");
	out.print("<td align=\"left\">");		
	out.println("<select name=\"season\" id=\"season\">");
	out.println("<option value=\"\">All</option>");
	if(!gens.getSeason().equals("")){
	    out.println("<option value=\""+gens.getSeason()+"\" selected>"+
			gens.getSeason()+"</option>");
	}
	out.println(Helper.allSeasons);
	out.println("</select> ");
	out.println("*<label for=\"year\">Year:</label>");
	out.println("<select name=\"year\" id=\"year\">");
	out.println("<option value=\"\">All</option>");
	if(!gens.getYear().equals("")){
	    out.println("<option value=\""+gens.getYear()+"\" selected=\"selected\">"+
			gens.getYear()+"</option>");
	}
	int[] years = Helper.getPrevYears();
	for(int yy:years){
	    out.println("<option value=\""+yy+"\">"+yy+"</option>");
	}
       	out.println("</select></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"lead\">Lead:</label></td>");
	out.print("<td align=\"left\">");		
	out.println("<select name=\"lead_id\" id=\"lead\" >");
	out.println("<option value=\"\">All</option>");
	if(leads != null){
	    for(Lead one:leads){
		String selected = one.getId().equals(gens.getLead_id())?"selected=\"selected\"":""; 
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select></td></tr>");		
	//
	// Date
	out.println("<tr><td align=\"right\"><label for=\"dateFrom\">Date From:</label></td>");
	out.println("<td align=\"left\">");
	out.println("<input type=\"text\" name=\"dateFrom\" maxlength=\"10\" value=\""+gens.getDateFrom()+"\" size=\"10\" id=\"dateFrom\" />");
	out.println("<label for=\"dateTo\"> to: </label>");
	out.println("<input type=\"text\" name=\"dateTo\" maxlength=\"10\" value=\""+gens.getDateTo()+"\" size=\"10\" id=\"dateTo\" />");
	out.println("</td></tr>");
	//
	out.println("<tr>");
	out.println("<td align=\"right\"><input type=\"submit\" "+
		    "name=\"action\" value=\"Submit\"></td>");
	out.println("<td>");
	out.println("<input type=button value=\"New General Listing\""+
		    " onclick=\"document.location='"+url+
		    "General.do?';\"></input></td>");		
	out.println("</tr>");
	out.println("</table>");
	out.println("<br />");
	if(!action.equals("")){
	    if(gens.size() > 0){
		Helper.writeGenerals(out, gens, url, "Found "+gens.size()+" record", false); // brochure flag
	    }
	    else{
		out.println("No record found <br />");
	    }
	}
	Helper.writeWebFooter(out, url);
	out.println("</center>");
	out.println("</body></html>");		
	out.close();
    }
		
}















































