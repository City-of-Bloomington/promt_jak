package program.web;

import java.util.*;
import java.text.*;
import java.util.Date;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/UnPublish.do","/UnPublish"})
public class UnPublishServ extends TopServlet {

    static Logger logger = LogManager.getLogger(MarketAdServ.class);	
    /**
     * Generates time string
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
	res.setContentType("text/html");
	Enumeration values = req.getParameterNames();

	String name= "";
	String value = "", year="", season="", lead_id="";
	String action = "", message="";
	boolean success = true;
	String id = "";
	PrintWriter out = res.getWriter();
	User user = null;
	HttpSession	session = req.getSession(false);
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}				
        String [] vals;
	//
	ProgramList pl = new ProgramList(debug);
	ProgramList pl2 = new ProgramList(debug);				
	pl.setApprovedPublish();
	WebPublish wp = new WebPublish(debug);
	wp.setUser_id(user.getId());
	LeadList leads = new LeadList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("season")){ 
		pl.setSeason(value);
		pl2.setSeason(value);								
		season = value;
		leads.setSeason(value);
	    }
	    else if (name.equals("year")){ 
		pl.setYear(value);
		pl2.setYear(value);
		year = value;
		leads.setYear(year);
	    }
	    else if (name.equals("lead_id")){ 
		pl.setLead_id(value);
		pl2.setLead_id(value);								
		lead_id = value;
	    }
	    else if(name.equals("prog_ids")){
		wp.setProg_ids(vals);
	    }
	    else if(name.equals("prog_web_ids")){
		wp.setProg_web_ids(vals);
	    }						
	    else if(name.equals("id")){
		pl.setPublish_id(value);
		action = "Search";
	    }						
	    else if(name.equals("action")){
		action = value;
	    }
	}
	if(true){
	    String back = leads.find();
	    if(!back.equals("")){
		message = "Error "+back;
		success = false;
	    }
	}
	if(action.equals("Search")){
	    String back = pl.findListForPublish();
	    if(!back.equals("")){
		message = "Error "+back;
		success = false;
	    }
	    pl2.forPublishOnly();
	    back = pl2.find();
	    if(!back.equals("")){
		message = "Error "+back;
		success = false;
	    }						
	}
	else if(action.startsWith("Unpublish")){
	    String back = wp.unPublish();
	    if(!back.equals("")){
		message = "Error "+back;
		success = false;
	    }
	    else{
		message = "Unpublished successfully";
		back = pl.findListForPublish();
		if(!back.equals("")){
		    message = "Error "+back;
		    success = false;
		}
		pl2.forPublishOnly();
		back = pl2.find();
		if(!back.equals("")){
		    message = "Error "+back;
		    success = false;
		}									
	    }
	}
	out.println("<html><head><title>Program Selection for Unpublishing</title>");
	Helper.writeWebCss(out, url);
	out.println("<script type='text/javascript'>");
	out.println("/*<![CDATA[*/");
	out.println(" function selectAll(source) { ");
	out.println("  checkboxes = document.getElementsByName('prog_ids');");
	out.println("   for(var i=0, n=checkboxes.length;i<n;i++) { ");
	out.println("   checkboxes[i].checked = source.checked;");
	out.println(" } ");
	out.println("}	");
	out.println(" function selectAll2(source) { ");
	out.println("  checkboxes = document.getElementsByName('prog_web_ids');");
	out.println("   for(var i=0, n=checkboxes.length;i<n;i++) { ");
	out.println("   checkboxes[i].checked = source.checked;");
	out.println(" } ");
	out.println("}	");				
	out.println("/*]]>*/\n");					
	out.println("</script> ");
	out.println("</head><body>");
	out.println("<center>");
	Helper.writeTopMenu(out, url);
	out.println("<h3>Programs Selection for Web Unpublishing</h3>");
	if(!message.equals("")){
	    out.println("<h4>"+message+"</h4>");
	}
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\">");
	out.println("<input type=\"hidden\" name=\"season\" value=\""+season+"\" />");
	out.println("<input type=\"hidden\" name=\"year\" value=\""+year+"\" />");				
	out.println("<table border=\"1\" width=\"90%\">");
	out.println("<caption>Programs Selection</caption>");
	if(!season.isEmpty()){
	    out.println("<tr><td align=\"right\"><b>Year/Season:</b></td><td>"+year+"/"+season+"</td></tr>");
	}
	else{
	    out.println("<tr><td align=\"right\"><b>Year:</b></td><td>"+year+"</td></tr>");
	}
	out.println("<tr><td align=\"right\"><label for=\"lead_id\">Lead:</label></td>");
	out.println("<td align=\"left\"><select name=\"lead_id\" id=\"lead_id\">");
	out.println("<option value=\"\">All</option>");
	String selected = "";
	if(leads != null && leads.size() > 0){
	    for(Lead one:leads){
		selected = one.getId().equals(lead_id)?"selected=\"selected\"":"";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td align=\"center\"><input type=\"submit\" name=\"action\" value=\"Search\">");
	out.println("</td></tr>");
	out.println("</table>");
	if(!action.equals("")){
	    if(pl.size() > 0  || pl2.size() > 0){
		if(pl.size() > 0){
		    out.println("<table width=\"100%\"><caption>Found "+pl.size()+" Programs</caption>");		    
		    out.println("<tr><td colspan=\"6\">Note: Check the programs that need to be removed from publishing on the City website and then hit 'Unpublish' button.<br /><br /></td></tr>");
		    out.println("<tr><td><b> <input type=\"checkbox\" onclick=\"selectAll(this)\" /> Select All </b></td><td><b>ID</b></td></td><td><b>Program</b></td><td><b>Guide Heading</b></td><td><b>Area</b></td><td><b>Lead</b></td></tr>");
		    for(Program one:pl){
			out.println("<tr><td><input type=\"checkbox\" name=\"prog_ids\" value=\""+one.getId()+"\" /></td><td>"+one.getId()+"</td>");
			out.println("<td>"+one.getTitle()+"</td>");
			out.println("<td>"+one.getCategory()+"</td>");
			out.println("<td>"+one.getArea()+"</td>");
			out.println("<td>"+one.getLead()+"</td>");
			out.println("</tr>");
		    }
		    out.println("</table>");
		}
		if(pl2.size() > 0){
		    out.println("<table width=\"100%\"><caption>Found "+pl2.size()+" Programs</caption>");		    
		    out.println("<tr><td colspan=\"6\">Note: Check the programs that need to be removed from publishing on the City website and then hit 'Unpublish' button.<br /><br /></td></tr>");
		    out.println("<tr><td><b> <input type=\"checkbox\" onclick=\"selectAll2(this)\" /> Select All </b></td><td><b>ID</b></td></td><td><b>Program</b></td><td><b>Guide Heading</b></td><td><b>Area</b></td><td><b>Lead</b></td></tr>");
		    for(Program one:pl2){
			out.println("<tr><td><input type=\"checkbox\" name=\"prog_web_ids\" value=\""+one.getId()+"\" /></td><td>"+one.getId()+"</td>");
			out.println("<td>"+one.getTitle()+"</td>");
			out.println("<td>"+one.getCategory()+"</td>");
			out.println("<td>"+one.getArea()+"</td>");
			out.println("<td>"+one.getLead()+"</td>");
			out.println("</tr>");
		    }
		    out.println("</table>");
		}
		out.println("<input type=\"submit\" name=\"action\" value=\"Unpublish\">");
	    }
	    else{
		out.println("No match found <br />");
	    }
	}
	out.println("<br />");
				
	out.println("</form>");		
	out.println("</center>");
	out.println("</body></html>");
	out.flush();
	out.close();

    }
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req, res);
    }

}






















































