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

@WebServlet(urlPatterns = {"/PickTime"})
public class PickTime extends TopServlet {

    final static String[] hours = {"","1","2","3","4","5","6","7","8","9","10","11","12"};
    final static String[] mins = {"",
	"05",
	"10",
	"15",
	"20",
	"25",
	"30",
	"35",
	"40",
	"45",
	"50",
	"55"}; // 12
	
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
	String value = "";
	String action = "";
	String wtime = "", time="", whichForm="";
	String id = "", sid="", type=""; // program or session
	PrintWriter out = res.getWriter();
		
        String [] vals;
	//
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("time")){ 
		time = value;
	    }
	    else if (name.equals("id")){ 
		id = value;
	    }
	    else if (name.equals("sid")){ 
		sid = value;
	    }
	    else if (name.equals("type")){ 
		type = value;
	    }			
	    else if (name.equals("wtime")){ // which time field
		wtime = value;
	    }
	    else if (name.equals("whichForm")){ // which form
		whichForm = value;
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}
	// time = "11:15 p.m.";
	String in_hh="", in_mm="", in_ampm="";
	if(time.length() > 0){
	    if(time.indexOf(" ") > 0){
		String hhampm[] = time.split(" ");
		in_hh = hhampm[0];
		in_ampm = hhampm[1];
		if(in_hh.indexOf(":") > 0){
		    String hhmm[] = in_hh.split(":");
		    in_hh = hhmm[0];
		    in_mm = hhmm[1];
		}
		else{ // no minutes, just am/pm
		    in_mm = in_ampm.trim();
		}
	    }
	}
	System.err.println("time "+time);		
	System.err.println("in_hh "+in_hh+" in_mm "+in_mm);
	System.err.println("in_ampm "+in_ampm);		
	if(!action.equals("")){
	    if(type.equals("") && !id.equals("")){
		Program pp  = new Program(debug, id);
		// 	pp.setStartEndTime(time);
		String back = pp.updateTime(wtime, time);
	    }
	    else{
		Session se = new Session(debug);
		se.setId(id);
		se.setSid(sid);
		String back = se.updateTime(wtime, time);
	    }
	}

	out.println("<html><head><title>Pick Time</title>");
	out.println("<script type=\"text/javaScript\"> ");
	out.println(" function composeTime(){   ");
	out.println(" var all = \"\";           ");
	out.println("  with(document.myForm){              ");
	out.println("   all = in_hh.options[in_hh.selectedIndex].value; ");
	out.println("   var in_m = in_mm.options[in_mm.selectedIndex].value;");
	// out.println("   alert(' id '+in_mm_id);            ");
	out.println("   if(in_m != ''){");
	out.println("     all += ':'+in_m; ");
	out.println("    } ");
	out.println(" if(document.getElementById('radio_am').checked) { ");
	out.println("     all += ' a.m.'; ");  
	out.println(" }else if(document.getElementById('radio_pm').checked) { ");
	out.println("     all += ' p.m.'; ");  
	out.println("  } ");		
	out.println(" } ");
	out.println(" myForm.time.value=all; ");
	out.println("  opener.document.myForm."+wtime+".value=all;");		
	if(id.equals("")){
	    out.println("  window.close(); ");
	}
	else{
	    out.println("  forms[0].submit(); ");
	}
	out.println(" } ");
	out.println("</script> ");
	if(!id.equals("") && !action.equals("")){
	    out.println("</head><body onload=\"javascript:window.close()\">");
	}
	else{
	    out.println("</head><body>");
	}
	out.println("<center>");
	out.println("<form name=\"myForm\" method=\"post\" onsubmit=\"composeTime();\">");
	if(!whichForm.equals(""))
	    out.println("<input type=hidden name=\"whichForm\" value=\""+whichForm+"\" />");
	if(!id.equals(""))
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	out.println("<input type=\"hidden\" name=\"time\" value=\"\" />");
	out.println("<table border=\"1\" width=\"90%\">");
	out.println("<caption>Pick Time</caption>");
	out.println("<tr><th></th><th><label for=\"in_hh\">Hour</label></th>"+
		    "<th><label for=\"in_mm\">Minute</label></th>"+
		    "<th><b>am/pm</b></th></tr>");
	out.println("<tr><th>Time</th>");
				
	out.println("<td><select name=\"in_hh\" id=\"in_hh\">");
	for(String hh:hours){
	    String selected = (in_hh.equals(hh))?"selected=\"selected\'":"";
	    out.println("<option "+selected+" value=\""+hh+"\">"+hh+"</option>");
	}
	out.println("</select>:");
	out.println("</td><td>");
	out.println("<select name=\"in_mm\" id=\"in_mm\">");
	for(String mm:mins){
	    String selected = (in_mm.equals(mm))?"selected=\"selected\'":"";
	    out.println("<option "+selected+" value=\""+mm+"\">"+mm+"</option>");
	}
	out.println("</select>:</td><td> ");
	String checked = in_ampm.equals("a.m.")?"checked=\"checked\"":"";
	out.println("<input type=\"radio\" id=\"radio_am\" name=\"in_ampm\" value=\"a.m.\" "+checked+"/><label for=\"radio_am\">A.M.</label>");
	checked = "";
	checked = in_ampm.equals("p.m.")?"checked=\"checked\"":"";		
	out.println("<input type=\"radio\" id=\"radio_pm\" name=\"in_ampm\" value=\"p.m.\" "+checked+" /><label for=\"radio_pm\">P.M.</label>");		
	out.println("</td></tr>");	
	out.println("<tr><td align=\"right\">");
	if(id.equals("")){
	    out.println("<input type=\"submit\" name=\"action\" value=\"Copy to Form\">");
	}
	else{
	    out.println("<input type=\"submit\" name=\"action\" value=\"Save\">");
	}
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");		
	out.println("<br />");
	out.println("<a href=javascript:window.close();>Close</a>");
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






















































