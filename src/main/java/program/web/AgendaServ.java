package program.web;

import java.util.*;
import java.text.*;
import java.util.Date;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.model.*;

@WebServlet(urlPatterns = {"/Agenda"}, loadOnStartup = 1)
public class AgendaServ extends TopServlet {

    String calColor = "black";
    String headerBG = "white";
    String headerFR = "blue";
    String headerWKFR ="red";
    String headerWKBG = "white";
    String calPPBG = "#DDDDDD";
    String calBG = "#CDC9A3";
    String calTDBG = "#BBBBBB"; 
    static Logger logger = LogManager.getLogger(AgendaServ.class);
	
    /**
     * Generates a monthly calendar page with programs and events.
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
	String category_id = "";
	String area_id="",lead_id="",
	    location_id="";
	boolean success=true;
	Vector dt = new Vector();
	String name= "";
	String value = "";
	String action = "";
	String message = "";
	int month = 0;
	int year = 0;
	int day = 0;
	int nextMonth = 0;
	int nextMonthYear = 0;
	int prevMonth = 0;
	int prevMonthYear = 0;
	PrintWriter out = res.getWriter();

	String[] Days_full = {"Sunday","Monday","Tuesday","Wednesday",
	    "Thursday","Friday","Saturday"};
	String[] Days = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
	String[] Months = {"January","February","March","April","May",
	    "June","July","August","September","October",
	    "November","December"};

        String [] vals;
	//
	category_id="";
	area_id="";
	lead_id="";
	location_id="";

	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("month")){ 
		if(!value.equals(""))
		    month = Integer.parseInt(value);
	    }
	    else if (name.equals("year")){
		if(!value.equals(""))
		    year = Integer.parseInt(value);
	    }
	    else if (name.equals("day")){
		if(!value.equals(""))
		    day = Integer.parseInt(value);
	    }
	    else if (name.equals("prev")){
		action = "prev";
	    }
	    else if (name.equals("next")){
		action = "next";
	    }
	    else if (name.equals("category_id")){
		category_id = value;
	    }
	    else if (name.equals("area_id")){
		area_id = value;
	    }
	    else if (name.equals("lead_id")){
		lead_id = value;
	    }
	    else if (name.equals("pLocation")){
		location_id = value;
	    }
	}
	HttpSession session = req.getSession(false);
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
	GregorianCalendar cal = new GregorianCalendar();

	int current_month = cal.get(Calendar.MONTH) + 1;
	int current_day = cal.get(Calendar.DATE);
	int current_year = cal.get(Calendar.YEAR); 
	int today[] ={ current_month,current_day,current_year};
	if (month == 0) {
	    month = current_month;
	} 
	if (year == 0) {
	    year = current_year;
	} 
	if (day == 0) {
	    day = current_day;
	} 
	Agenda agenda = new Agenda(debug);
	Vector<String>[] info = agenda.getInfo(month, year,
					       lead_id,
					       area_id,
					       location_id,
					       category_id,
					       url);
	//
	// get next month and year
	GregorianCalendar temp_cal = new GregorianCalendar();
	temp_cal.set(Calendar.MONTH, month-1);
	temp_cal.set(Calendar.YEAR, year);
	temp_cal.add(Calendar.MONTH, 1);
	nextMonth = temp_cal.get(Calendar.MONTH)+1;
	nextMonthYear = temp_cal.get(Calendar.YEAR);
	// get prev month and year
	temp_cal.add(Calendar.MONTH, -2);
	prevMonth = temp_cal.get(Calendar.MONTH)+1;
	prevMonthYear = temp_cal.get(Calendar.YEAR);

	int days_in_month = Helper.get_days_in_month(month, year);

	int first_day_of_month = Helper.get_first_day_of_month(month, year);

	out.println("<html><head><title>Agenda</title>");
	out.println("<link rel=\"stylesheet\" href=\""+url+"css/menu_style.css\" />");	
	out.println("<style type=\"text/css\"><!--");
	out.println("a:link     {text-decoration: none; color:"+calColor+";}");
	out.println("a:visited  {text-decoration: none; color:"+calColor+";}"); 
	out.println("a:active   {text-decoration: none; color:"+calColor+";}"); 
	out.println("a:hover    {text-decoration: none; color:"+calColor+";}"); 
	out.println("--></style>");
	out.println("</head><body>");
	//
	Helper.writeTopMenu(out, url);
	if(!message.equals(""))
	    out.println(message+"<br />");
	cal.set(Calendar.YEAR, year);
	cal.set(Calendar.MONTH, month - 1);
	cal.set(Calendar.DATE, day);
	out.println("<center>");
	out.println("<br /><font size=\"+2\" color=\"blue\"> Activities & Events "+
		    "Calendar </font><br>");

	out.println("<table border=\"4\" cellspacing=\"0\" cols=\"7\" width=\"90%\">");
	out.println("<caption> "+Months[month-1]+ " "+year+
		    "</caption>");	
	String dd = "&nbsp;";
	int ddd = 0;
	int nxt = 0;
	boolean pass = false, more_rows = true;
	
	for (int row = 0; row < 7 && more_rows; row++){
	    out.println("<tr>");
	    for (int col = 0; col < 7; col++){
		if (row != 0) {
		    if (col == first_day_of_month - 1){ 
			pass = true;
		    }
		}	
		if (pass){	
		    ddd++;
		    dd = Integer.toString(ddd);
		}
		if (ddd > days_in_month){ 
		    dd = "&nbsp;";     
		}
		if (ddd >= days_in_month){ 
		    more_rows = false;
		}
		//
		// first row
		// write day names
		//
		if (row == 0){ 
		    if (col == 0 || col == 6) // WS weekends  	
			out.println("<td bgcolor=\"" + headerWKBG + 
				    "\' align=\"center\" valign=\"top\"><font color=\"" + 
				    headerWKFR + "\" >" + Days[col] + 
				    "</font><b></td>");
		    else	// other days			
			out.println("<td bgcolor=\"" + headerBG +
				    "\" align=\"center\" valign=\"top\"><font color=\"" +
				    headerFR + "\">" + Days[col] +
				    "</font></td>");
		} 
		else{
		    //
		    // other rows
		    //
		    if (dd.equals("&nbsp;")) {	
			if (col == 0 || col == 6) 
			    out.println("<td bgcolor=\"white\">"+dd+"</td>"); 
			else
			    out.println("<td bgcolor=\""+calBG+"\">"+dd+"</td>"); 
		    }
		    else if(!dd.equals("")) {		
			String infoSymbol = "&nbsp;&nbsp;";
			if(info[ddd-1].size() > 0){
			    infoSymbol = ""+info[ddd-1].size();
			}
			String str ="";
			if (col == 0 || col == 6) 
			    str += "<td bgcolor=\"white\"" ;   
			else 
			    str += "<td bgcolor=\""+calBG+"\""; 
			str += " valign=\"top\" align=\"left\"><b>"+
			    "<font face=\"Courier new\">" +
			    dd + 
			    "&nbsp;&nbsp;&nbsp;&nbsp;"+
			    "</font></b><br />"+
			    "<table border=\"0\"><tr><td><font color=\""+
			    calColor + 
			    "\" face=\"Courier new\"><a href=#"+dd+">"+
			    infoSymbol+"</a>"+
			    "</font></td></tr></table>"+
			    "</td>";
			out.println(str);
		    }
		    nxt++;
		}
	    }
	    out.println("</tr>");    
	}
	out.println("</table>");
	out.println("<br>");
	out.println("<form name=\"myForm\" method=\"post\">");
	out.println("<table border=0 width=90%>");
	out.println("<input type=\"hidden\" name=\"month\" value=\""+prevMonth+ 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"year\" value=\""+prevMonthYear+ 
		    "\" />");
	if(!category_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"category_id\" value=\"" + 
			category_id + "\" />");
	}
	if(!area_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + 
			area_id + "\"></input>");
	}
	if(!lead_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + 
			lead_id + "\"></input>");
	}
	if(!location_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"location_id\" value=\"" + 
			location_id + "\"></input>");
	}
	out.println("<tr><td align=\"left\" valign=\"top\">");
	out.println("<input type=\"submit\" name=\"prev\" value=\"Previous Month\" />");
	out.println("</td><td align=\"right\" valign=\"top\">");
	out.println("</form>");
	out.println("<form name=\"myForm2\" method=\"post\">");
	out.println("<input type=\"hidden\" name=\"month\" value=\""+nextMonth+ 
		    "\" />");
	out.println("<input type=\"hidden\" name=\"year\" value=\""+nextMonthYear+ 
		    "\" />");
	if(!category_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"category_id\" value=\"" + 
			category_id + "\" />");
	}
	if(!area_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"area_id\" value=\"" + 
			area_id + "\"></input>");
	}
	if(!lead_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"lead_id\" value=\"" + 
			lead_id + "\"></input>");
	}
	if(!location_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"location_id\" value=\"" + 
			location_id + "\"></input>");
	}
	out.println("<input type=\"submit\" name=\"next\" value=\"Next Month\" />");
	out.println("</td></tr></table></center>");
	out.println("</form>");
	// 
	// show the events
	//
	out.println("<table border=\"0\" cellspacing=\"1\" width=\"75%\" >");
	out.println("<caption>Events </caption>");
	out.println("<tr><th align=\"right\">Day : </th><th align=\"left\"> Event</th>"+
		    "</tr>");
	for(int i=0; i<days_in_month; ++i){
	    if(info[i].size() > 0){
		out.println("<tr><td valign=\"top\" align=\"right\"><b><a id="+(i+1)+
			    ">"+(i+1)+"</a> :</b></td> ");
		out.println("<left><td align=\"left\">");
		for(int j=0;j<info[i].size(); ++j){
		    String str = (String) info[i].elementAt(j);
		    out.println(str+"<br>");
		}
		out.println("</td></left>");
		out.println("</tr>");
	    }
	}
	out.println("</table>");
	out.println("<hr />");
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






















































