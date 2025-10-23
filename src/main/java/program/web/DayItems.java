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

@WebServlet(urlPatterns = {"/DayItems"})
public class DayItems extends TopServlet{

    static Logger logger = LogManager.getLogger(DayItems.class);
    /**
     * The main class method.
     *
     * Create an html page for the item form.
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
     * Create an html page for the item form.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	final int baseNumber = 1;
	
	String finalMessage = ""; // to be shown in the status bar
	PrintWriter out = null;
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	res.setContentType("text/html");
	out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name, value;
	String action = "startNew", id="", infoDate="",
	    message = "";

	String month="", year="", day = "", info="", reqDate="";
	String titles[] = {"ID &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
	    "Events/Programs &nbsp;&nbsp;","Session No."};
	String category_id="",lead_id="",area_id="",location_id="";
	//
	// this flag is used to check if the user set the session
	// fields in the program form
	// if not he/she should be warned
	//
	boolean	 success = true; //to check if the user set 
	String query="", qq="";
	boolean connectDbOk = false;
	String [] vals;
	while (values.hasMoreElements()){
	    //
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	

	    if(name.equals("id")){ // item id
		id = value;
	    }
	    else if(name.equals("year")){ 
		year = value;
	    }
	    else if(name.equals("month")){
		month = value;
	    }
	    else if(name.equals("day")){
		day = value;
	    }
	    else if(name.equals("reqDate")){
		reqDate = value;
	    }
	    else if(name.equals("category_id")){
		category_id = value;
	    }
	    else if(name.equals("area_id")){
		area_id = value;
	    }
	    else if(name.equals("lead_id")){
		lead_id = value;
	    }
	    else if(name.equals("location_id")){
		location_id = value;
	    }
	    else if(name.equals("info")){
		info = value;
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
	try{
	    con = Helper.getConnection();
	    //
	    if(con != null){
		stmt = con.createStatement();
	    }
	    else{
		success = false;
		message += " Could not connect to Database";
		logger.error(message);
	    }
	}catch(Exception ex){
	    logger.error(ex);
	    message += " Could not connect to Database "+ex;
	    success = false;
	}
	if(reqDate.equals("")|| action.startsWith("Start")){
	    // check if we have month, year, day set
	    if(month.equals("")){
		// if not set, use today's date
		reqDate = Helper.getToday2();
	    }
	    else{
		reqDate = month+"/"+day+"/"+year;
	    }
	}
	if(action.equals("Delete")){
	    // 
	    query = "delete from agenda_info where " +
		" id="+id;

	    try{
		if(user.canDelete()){
		    if(debug){
			logger.debug(query);
		    }				
		    stmt.executeUpdate(query);
		    message = "Record Deleted";
		}
		else{
		    message += " You can not delete ";
		    success = false;
		}
	    }
	    catch(Exception ex){
		message = "Could not Delete "+ex;
		success = false;
		logger.error(ex+" : "+query);
	    }
	}
	else if(action.equals("zoom")){
	    query = "select info,date_format(infoDate,'%m/%d/%Y') from "+
		"agenda_info where "+
		" id="+id;
			
	    if(debug){
		logger.debug(query);
	    }
	    try{
		rs = stmt.executeQuery(query);

		if(rs.next()){
		    String str = rs.getString(1);
		    if(str != null) info = str;
		    str = rs.getString(2);  
		    if(str != null){
			infoDate = str;
			month = str.substring(0,2);
			day = str.substring(3,5);
			year = str.substring(6,10);
		    }
		}
		finalMessage ="Zooming";
	    }
	    catch(Exception ex){
		success = false;
		logger.debug(ex+":"+query);
		message += " Error retreiving data "+ex;
	    }
	}
	else if(action.equals("Update")){
	    qq = "update agenda_info set ";
	    if(info.equals(""))
		qq += "info = null,";
	    else
		qq += "info='"+ Helper.escapeIt(info) + "',";
	    if(!infoDate.equals(""))
		qq += "infoDate=str_to_date('"+reqDate+"','%m/%d/%Y')";
	    else
		qq += "infoDate=str_to_date('"+month+"/"+day+"/"+year+
		    "','%m/%d/%Y')";
	    qq += " where id="+id;


	    try{
		if(user.canEdit()){
		    if(debug){
			logger.debug(qq);
		    }				
		    stmt.executeUpdate(qq);
		    message = "Record updated";
		}
		else{
		    message += " You can not update ";
		    success = false;
					
		}
	    }
	    catch(Exception ex){
		message += " Could not update "+ex;
		logger.error(ex+":"+qq);
		success = false;
	    }
	}
	else if(action.equals("Save")){
	    //
	    query = "insert into agenda_info values ("; 
	    qq = "'"+Helper.escapeIt(info)+ "',";
	    if(!reqDate.equals(""))
		qq += "str_to_date('"+ reqDate+ "','%m/%d/%Y'),0,0)";
	    else if(!month.equals("") && !day.equals("") && !year.equals("")){
		qq += "str_to_date('"+ month+"/"+day+"/"+year+"','%m/%d/%Y'),"+
		    "0,0)"; //  for pid,sid
	    } 
	    else {
		message = "Date not set properly";
		success = false;
	    }
	    if(success){
		query = "insert into agenda_info values (0,"; //auto_increment 
		query += ","+qq;
		try{
		    if(user.canEdit()){
			if(debug){
			    logger.debug(query);
			}
			stmt.executeUpdate(query);
			message = "Record updated";
		    }
		    else{
			message += " You can not save ";
			success = false;
		    }
		}
		catch(Exception ex){
		    message = "Could not update";
		    logger.error(ex+":"+query);
		    success = false;
		}
	    }
	}
	else if(action.startsWith("Start")){
	    //
	    // we blank some of the fields to avoid
	    // mistake using the same again, such as 
	    // the code
	    // 
	    info = "";
	    finalMessage = "New Record";
	}
	//
	out.println("<html><head><title>City of Bloomington Parks and "+
		    "Recreation</title>");
	//
	// This script validate
	//
	Helper.writeWebCss(out, url);	
	out.println(" <script language=Javascript>");
	out.println(" function showStatus(){      ");
	out.println(" defaultStatus =\" "+finalMessage+"\";"); 
	out.println("    }                          ");
	out.println("  function validateInteger(x){     ");            
	out.println("	if((x == \"0\")|| (x==\"1\") || ");  
	out.println("	   (x == \"2\")|| (x==\"3\") || ");  
	out.println("	   (x == \"4\")|| (x==\"5\") || "); 
	out.println("	   (x == \"6\")|| (x==\"7\") || ");
	out.println("	   (x == \"8\")|| (x==\"9\")){  ");
	out.println("	            return true;        ");
	out.println(" 	        }                       ");
	out.println("	       return false;	        ");
	out.println(" 	   }                            ");
	out.println("  function checkValue(x){ ");            
	out.println("	if((x == \"0\")|| (x==\"1\") || ");  
	out.println("	  (x == \"2\")|| (x==\"3\") || ");  
	out.println("	  (x == \"4\")|| (x==\"5\") || "); 
	out.println("	  (x == \"6\")|| (x==\"7\") || ");
	out.println("	  (x == \"8\")|| (x==\"9\") || ");
	out.println("	  (x == \".\")){               ");
	out.println("	     return 0;	           ");
	out.println(" 	       }                   ");
	out.println(" return 1;                    ");
	out.println(" 	  }                        ");
	out.println(" function checkSelection(element){   ");
	out.println("   for(var i = 0; i < element.options.length; i++) ");
        out.println("    if (element.options[i].selected){  "); 
	out.println("      if(i > 0){ ");
	out.println("         return true;  ");
	out.println("         }     ");
	out.println("       }  ");
	out.println("    return false;  ");
	out.println("   }               ");
	out.println(" function checkSelectionValue(element){   ");
	out.println("  var x = \"\"; ");
	out.println("   for(var i = 0; i < element.options.length; i++) ");
        out.println("    if (element.options[i].selected){  "); 
        out.println("     x = element.options[i].value;    ");
        out.println("     }                          ");
	out.println("    if(x.length == 0 ){           ");
	out.println("       return false;             ");
	out.println("         }                      ");
	out.println("    return true;               ");
	out.println("      }                         ");
	out.println("  function validateForm(){      ");            
	out.println("	if((document.myForm.info.value.length == 0)){ ");  
	out.println("        alert(\"Info field must be entered\"); ");
	out.println("	     return false;	        ");
	out.println(" 	    }                           ");
       	out.println("    return true;                   ");
	out.println("	}	                        ");
	out.println("  function validateDeleteForm(){   ");            
	out.println("  var x = false ;                  ");
	out.println("   x = window.confirm(\"Are you sure you want to delete\"); ");
	out.println("   return x;                       ");
	out.println(" }	                                ");
	out.println(" </script>                         ");   
	out.println("</head><body  onLoad=\"showStatus();\">");
	Helper.writeTopMenu(out, url);		
	out.println("<h2><center>Add/Edit Day Event</center></h2>");

	out.println("<br />");
	if(!message.equals(""))
	    out.println(message+"<br />");

	//box it in 
	out.println("<center><table border=\"1\" align=\"center\">");
	out.println("<caption>Day Items</caption>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td>");

	//the real table
	out.println("<form name=\"myForm\" method=\"post\" "+
		    "onsubmit=\"return validateForm()\">");
	if(!category_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"category_id\" value=\"" + 
			category_id + 
			"\"></input>");
	}
	if(!area_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + 
			area_id + 
			"\"></input>");
	}
	if(!lead_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + 
			lead_id + 
			"\"></input>");
	}
	if(!location_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"location_id\" value=\"" + 
			location_id + 
			"\"></input>");
	}
	if(action.equals("zoom")){
	    out.println("<input type=\"hidden\" name=\"year\" value=\"" + year + 
			"\"></input>");
	    out.println("<input type=\"hidden\" name=\"day\" value=\"" + day + 
			"\"></input>");
	    out.println("<input type=\"hidden\" name=\"month\" value=\"" + month + 
			"\"></input>");
	}
	out.println("<table>");
       
	out.println("<tr><td align=\"right\"><b>Date: ");
	out.println("</b></td><td><left>(mm/dd/yyyy)");
    	out.println("<input type=\"text\" name=\"reqDate\" maxlength=\"10\" size=\"10\" "+
		    "value=\""+reqDate+"\" />");

	out.println("</left></td></tr>");

	out.println("<tr><td align=\"right\"><b>Item Info: ");
	out.println("</b></td><td><left>");
    	out.println("<input type=\"text\" name=\"info\" maxlength=\"80\" size=\"50\" "+
		    "value=\""+info+"\" />");
	out.println("</left></td></tr>");

	//
	if(action.equals("") || action.equals("startNew") || 
	   action.equals("delete")){
	    out.println("<tr><td colspan=\"2\" align=\"right\"><input type=\"submit\" "+
			"name=\"action\" value=\"Save\" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
			"&nbsp;&nbsp;&nbsp;<input type=\"reset\" "+
			" value=\"Clear\" /></td></tr>"); 
	    out.println("</form>");
	}
	else{
	    out.println("<tr><td align=\"right\"><input type=\"submit\" "+
			"name=\"action\" value=\"Update\" />"+
			"&nbsp;&nbsp;" +
			"<input type=\"submit\" "+
			"name=\"action\" value=\"Start New\" />"+
			"</td>");

	    out.println("</form>");
	    //
	    out.println("<form name=\"myForm2\" method=\"post\" "+
			"onSubmit=\"return validateDeleteForm()\">");
	    out.println("<input type=\"hidden\" name=\"reqDate\" value=\""+reqDate+ "\" />");
	    out.println("<input type=\"hidden\" name=\"year\" value=\""+ year + "\" />");
	    out.println("<input type=\"hidden\" name=\"month\" value=\""+month +"\" />");
	    out.println("<input type=\"hidden\" name=\"day\" value=\""+day +"\" />");
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+ id + "\" />");
	    out.println("<td align=\"right\">");
	    out.println("&nbsp;&nbsp;<input type=\"submit\" name=\"action\" "+
			"value=\"Delete\" />");
	    out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>");
	    out.println("</form>");	

	}
	out.println("</table></center>");
	out.println("</td></tr></table>");
	//
	// end of overall table
	//
	// put all the old items for this day in one table
	//

	int nrcnt = 0; // record count
	String qq2 = "select count(*) from agenda_info agi ";
	String qqw = "";
	boolean inFlag = false;
	if(!category_id.equals("") || !lead_id.equals("") || 
	   !area_id.equals("") || !location_id.equals("")){
	    qq2 +=", programs pp ";
	    inFlag = true;
	}
	
	qqw += " where agi.infoDate="+
	    "str_to_date('"+reqDate+"','%m/%d/%Y') ";

	if(!category_id.equals("")){
	    qqw += " and pp.category_id = "+category_id+
		"'";
	}
	if(!area_id.equals("")){
	    qqw += " and pp.area_id = "+area_id;
	}
	if(!lead_id.equals("")){
	    qqw += " and pp.lead_id = "+lead_id;
	}
	if(!location_id.equals("")){
	    qqw += " and pp.location_id = "+location_id;
	}
	if(inFlag){
	    qqw += " and agi.pid=pp.id ";
	}
	qq2 += qqw;
	if(debug){
	    logger.debug(qq2);
	}
	try{
	    rs = stmt.executeQuery(qq2);
	    if(rs.next()){
		// out.println("Record zooming");
		String str = rs.getString(1);
		if(str != null) nrcnt = Integer.parseInt(str);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+" : "+qq2);
	    out.println(ex);
	}
	out.println("<br># of events:"+nrcnt);
	out.println("<br>");


	if(nrcnt > 0){
	    int pid=0;
	    out.println("<table border=\"1\">");
	    out.println("<caption>Events on this Day</caption>");
	    qq = "select agi.id,agi.info,agi.pid,agi.sid "+
		"from agenda_info agi ";
	    if(inFlag){
		qq += ",programs pp";
	    }
	    qq += qqw;

	    if(debug){
		logger.debug(qq);
	    }
	    out.println("<tr>");
	    for(int i=0; i<titles.length; ++i){
		out.println("<th><strong>"+titles[i]+"</strong></th>");
	    }
	    out.println("</tr>");
	    try{
		rs = stmt.executeQuery(qq);
		while(rs.next()){
		    out.println("<tr>");
		    String str = rs.getString(1); 
		    pid = rs.getInt(3);
		    // 
		    // if this is a program we do not allow users
		    // to change the program title here 
		    // They can change other events only. 
		    //
		    if(pid > 0){
			out.println("<td>"+ str +
				    "</td>");
		    }
		    else {
			out.println("<td><a href=\""+url+
				    "DayItems?"+
				    "&zoom=yes&id="+id+
				    "&year="+year+
				    "&month="+month+
				    "&day="+day+
				    "&reqDate="+reqDate+
				    "&id="+str+"\">"+ str +
				    "</a></td>");
		    }
		    str = rs.getString(2); 
		    if(str != null){ 
			if(pid > 0){
			    out.println("<td><a href=\""+url+
					"Programs?"+
					"&zoom=yes&id="+pid+
					"&pyear="+year+
					"\">"+ str +
					"</a></td>");
			    int sid = rs.getInt(4);
			    if(sid > 0){
				out.println("<td><a href=\""+url+
					    "Sessions?"+
					    "&zoom=yes&id="+pid+
					    "&sid="+sid+
					    "&pyear="+year+
					    "\"><center>"+ sid +
					    "</center></a></td>");
			    }
			    else{
				out.println("<td>&nbsp;</td>");
			    }
			}  // event but not program
			else{
			    out.println("<td>"+str+"</td><td>&nbsp;</td>");
			}
		    }
		    else // case of id only
			out.println("<td>&nbsp;</td><td>&nbsp;</td>");
		    out.println("</tr>");
		}		
	    }
	    catch(Exception ex){
		out.println(ex);
		logger.error(ex+":"+qq);
	    }
	    out.println("</table>");
	}
	//
	out.println("<HR>");
	out.println("<ul><li><A href=\""+url+
		    "agenda?"+
		    "&year="+ year +
		    "&month="+ month +
		    "\"> Go to the Related Agenda </a>");

	out.print("</ul></body></html>");
	out.close();
	Helper.databaseDisconnect(con, stmt, rs);
    }

}






















































