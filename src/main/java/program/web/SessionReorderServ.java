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

@WebServlet(urlPatterns = {"/SessionReorder.do","/SessionReorder"})
public class SessionReorderServ extends TopServlet {

    static Logger logger = LogManager.getLogger(SessionReorderServ.class);	

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
	res.setContentType("text/html");
	Enumeration values = req.getParameterNames();

	String name= "";
	String value = "", prog_id="", order_id="", sid="";
	String action = "", message="";
	boolean success = true;
	String id = "";
	List<SessionReorder> sessionReorders = null;
	List<Session> sessions = null;
	SessionOpt sopt = null;

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
	Program pr = null;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("prog_id")){ 
		prog_id = value;
	    }
	    else if (name.equals("order_id")){ 
		order_id = value;
	    }
	    else if (name.equals("sid")){
		if(value != null && !value.equals(""))
		    sid = value;
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}

	if(action.startsWith("Move")){
	    SessionReorder sorder = new SessionReorder(debug, prog_id, sid);
	    String back = sorder.doSave();
	    if(!back.equals(""))
		message += back;
	}
	else if(action.startsWith("Change")){
	    SessionReorder sorder = new SessionReorder(debug, prog_id);
	    String back = sorder.changeOrder();
	    if(!back.equals(""))
		message += back;						
	}
	else if(action.startsWith("Cancel")){
	    SessionReorder sorder = new SessionReorder(debug, prog_id);
	    String back = sorder.doClean();
	    if(!back.equals(""))
		message += back;		
	}
	if(true){
	    pr = new Program(debug, prog_id);
	    String back = pr.doSelect();
	    if(!back.equals("")){
		message += back;
	    }
	    String old_sid_set = null;
	    SessionReorderList orders = new SessionReorderList(debug, prog_id);
	    back = orders.find();
						
	    if(back.equals("")){
		sessionReorders = orders.getSessionReorders();
	    }
	    else{
		message += back;
	    }
	    back = orders.findOldSidSet();
	    if(!back.equals("")){
		message += back;
	    }
	    if(orders.hasOldSidSet()){
		old_sid_set = orders.getOldSidSet();
	    }
	    SessionList seslist = new SessionList(debug, prog_id);
	    if(old_sid_set != null){
		seslist.setExcludeSids(old_sid_set);
	    }
	    back = seslist.lookFor();
	    if(back.equals("")){
		sessions = seslist;
	    }
	    else{
		message += back;
	    }						
	    sopt = new SessionOpt(debug, prog_id);
	    back = sopt.doSelect();
	    if(!back.equals("")){
		message += back;
	    }						
	}
	out.println("<html><head><title>Sessions Reorder</title>");
	Helper.writeWebCss(out, url);
	    
	out.println("<script type='text/javascript'>");
	out.println("/*<![CDATA[*/");
	out.println(" function selectAll(source) { ");
	out.println("  checkboxes = document.getElementsByName('prog_ids');");
	out.println("   for(var i=0, n=checkboxes.length;i<n;i++) { ");
	out.println("   checkboxes[i].checked = source.checked;");
	out.println(" } ");
	out.println("}	");			
	out.println("/*]]>*/\n");					
	out.println("</script> ");
	out.println("</head><body>");
	Helper.writeTopMenu(out, url);
	out.println("<h3>Sessions Reorder</h3>");
	if(!message.equals("")){
	    out.println("<h4>"+message+"</h4>");
	}
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\">");
	out.println("<input type=\"hidden\" name=\"prog_id\" value=\""+prog_id+"\" />");
	out.println("Please note the following before you start: <br />");
	out.println("<ul>");
	out.println("<li> Select the session that you want to be reordered starting with the one that need to go in top and then hit 'Move' button.</li>");
	out.println("<li>Then do the same for the second session and so on</li>");
	out.println("<li>When all all sessions are moved from the left table to the right table, you click on 'Change Order button.</li>");
	out.println("<li>If you moved a session by mistake or you changed your mind you can Cancel the reordering by click on 'Cancel Reorder' button</li>");
	out.println("</ul>");
	out.println("<table width=\"100%\" border=\"1\">");
	out.println("<tr><td colspan=\"3\"> Program: <a href=\""+url+"Program.do?id="+pr.getId()+"\">"+pr.getTitle()+"</a></td></tr>");
	out.println("<tr><td width=\"40%\" valign=\"top\">");
	String str ="";
	if(sessions != null && sessions.size() > 0){
	    out.println("<table border=\"1\">");// left table starts here
	    out.println("<caption>Current Sessions Order</caption>");
	    out.println("<tr><th>Select</th>");
	    for(int jj=1;jj<sopt.getShowSize();jj++){
		if(sopt.show(jj)){
		    out.println("<th><b>"+sopt.getTitle(jj)+"</b></th>");
		}
	    }
	    out.println("</tr>");
	    for(Session one:sessions){
		out.println("<tr>");
		out.println("<td><b>` <input type=\"radio\" name=\"sid\" value=\""+one.getSid()+"\" /> "+one.getSid()+"</`td>");
		if(sopt.show(1)){
		    str = one.getCode();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(2)){
		    str = one.getDays();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(3)){
		    str = one.getStartEndDate();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(4)){
		    str = one.getStartEndTime();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(5)){
		    str = one.getRegDeadLine();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(6)){
		    str = one.getInCityFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;
		    out.println("<td align=\"right\">"+str+"</td>");
		}
		if(sopt.show(7)){
		    str = one.getNonCityFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;					
		    out.println("<td align=\"right\">"+str+"</td>");
		}
		if(sopt.show(8)){
		    str = one.getOtherFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;					
		    out.println("<td align=\"right\">"+str+"</td>");
		}
		if(sopt.show(9)){
		    str = one.getMemberFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;
		    out.println("<td align=\"right\">"+str+"</td>");
		}				
		if(sopt.show(10)){
		    str = one.getNonMemberFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;					
		    out.println("<td align=\"right\">"+str+"</td>");
		}					
		if(sopt.show(11)){
		    Location loc = one.getLocation();
		    if(loc != null)
			str = loc.getName();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(12)){
		    //
		    str = one.getAgeInfo();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(13)){
		    str = one.getPartGrade();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(14)){
		    str = one.getMinMaxEnroll();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(15)){
		    str = one.getClassCount();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(16)){
		    str = one.getDescription();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(17)){
		    str = one.getInstructor();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		out.println("</tr>");
	    }
	    out.println("</table>");
	}
	out.println("<td width=\"10%\" valign=\"middle\">");
	if(sessions != null && sessions.size() > 0){
	    out.println("<input type=\"submit\" name=\"action\" value=\"Move -->\"></td>");
	}
	out.println("</td><td valign=\"top\">");
	if(sessionReorders != null && sessionReorders.size() > 0){
	    out.println("<table border=\"1\">");// right table starts here
	    out.println("<caption>New Sessions Order</caption>");
	    out.println("<tr><th>New order </th>");
	    for(int jj=1;jj<sopt.getShowSize();jj++){
		if(sopt.show(jj)){
		    out.println("<th><b>"+sopt.getTitle(jj)+"</b></th>");
		}
	    }
	    out.println("</tr>");
	    for(SessionReorder sesOrd:sessionReorders){
		Session one = sesOrd.getSeason();
		if(one == null) continue;
		out.println("<tr>");
		out.println("<td>"+sesOrd.getOrder_id()+"</td>");
		if(sopt.show(1)){
		    str = one.getCode();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(2)){
		    str = one.getDays();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(3)){
		    str = one.getStartEndDate();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(4)){
		    str = one.getStartEndTime();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(5)){
		    str = one.getRegDeadLine();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(6)){
		    str = one.getInCityFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;
		    out.println("<td align=\"right\">"+str+"</td>");
		}
		if(sopt.show(7)){
		    str = one.getNonCityFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;					
		    out.println("<td align=\"right\">"+str+"</td>");
		}
		if(sopt.show(8)){
		    str = one.getOtherFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;					
		    out.println("<td align=\"right\">"+str+"</td>");
		}
		if(sopt.show(9)){
		    str = one.getMemberFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;
		    out.println("<td align=\"right\">"+str+"</td>");
		}				
		if(sopt.show(10)){
		    str = one.getNonMemberFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;					
		    out.println("<td align=\"right\">"+str+"</td>");
		}					
		if(sopt.show(11)){
		    Location loc = one.getLocation();
		    if(loc != null)
			str = loc.getName();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(12)){
		    //
		    str = one.getAgeInfo();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(13)){
		    str = one.getPartGrade();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(14)){
		    str = one.getMinMaxEnroll();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(15)){
		    str = one.getClassCount();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(16)){
		    str = one.getDescription();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(17)){
		    str = one.getInstructor();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		out.println("</tr>");
	    }
	    out.println("</table>");				
	}
	out.println("</td></tr>");
	out.println("<tr><td colspan=\"3\" align=\"center\">");
	if(message.equals("") &&
	   (sessions == null || sessions.size() == 0)){
	    out.println("<input type=\"submit\" name=\"action\" value=\"Change Order\">");
	}
	if(sessionReorders != null && sessionReorders.size() > 0){
	    out.println("<input type=\"submit\" name=\"action\" value=\"Cancel Reorder\">&nbsp;&nbsp;");
	}
	out.println("</table>");
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






















































