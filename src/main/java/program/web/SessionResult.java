package program.web;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/SessionsTable","/SessionResult"})
public class SessionResult extends TopServlet{

    static Logger logger = LogManager.getLogger(SessionResult.class);
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
     * Create a table of session in html format.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	PrintWriter os;
	boolean connectDbOk = false;
	String[] titles = {"Session id ", // session id (sid)
	    "Year ",
	    "Season ",
	    "Area",
	    "Category ",
	    "Program Title",
	    "Program id",  // not shown but used
	    "Code ", 
	    "Days ",
	    "Star, End Date ", 
	    "Star, End Time ", 
	    "Reg. Deadline ", 
	    "In City Fee $", 
	    "Non City Fee $",
	    "Other Fee $",  
	    "Location ",
	    "Participant Age ",  
	    "Participant Grade ",
	    "Min-Max Enrollment ",
	    "# of Classes ",   
	    "Description ", 
	    "Instructor"};

	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	os = res.getWriter();
	int maxRecords = 100, minRecords = 0, row;
	String browsestr = ""; 
	Enumeration values = req.getParameterNames();
	//obtainProperties(os);
	String name, value;
	boolean rangeFlag = true, success = true;
	String that="", id= ""; 
	String sortby = "", message="";
	String username = "";

	String year = "", season = "", title = "", area_id="",
	    category_id = "", code = "", sid = "", days = "",
	    startDate = "", startEndTime = "", regDeadLine = "",
	    inCityFee = "", nonCityFee = "",
	    otherFee = "", location_id = "", ageFrom="", partGrade="",
	    minMaxEnroll = "", classCount = "", description = "",
	    instructor = "", sessionSort = "";

	os.println("<html>");

	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();

	    if (name.equals("maxRecords")){
		browsestr += "&maxRecords="+value;
		try{
		    maxRecords = Integer.parseInt(value);
		}catch(Exception ex){};
	    }
	    else if (name.equals("minRecords")){
		browsestr += "&minRecords="+value;
		try{
		    minRecords = Integer.parseInt(value);
		}catch(Exception ex){};
	    }
	    else if (name.equals("id")){
		id = value;
	    }
	    else if (name.equals("sid")){
		sid = value;
	    }
	    else if (name.equals("category_id")){
		browsestr += "&category_id="+value;
		category_id = value;
	    }
	    else if (name.equals("title")){
		browsestr += "&title="+value.replace(' ','+');
		title = value ;
	    }
	    else if (name.equals("year")){
		browsestr += "&year="+value;
		year = value;
	    }
	    else if (name.equals("season")){
		browsestr += "&season="+value.replace(' ','+');
		season = value;
	    }
	    else if (name.equals("days")){
		browsestr += "&days="+value.replace(' ','+');
		days = value;
	    }
	    else if (name.equals("startDate")){
		browsestr += "&startDate="+value.replace(' ','+');
		startDate = value;
	    }
	    else if (name.equals("startEndTime")){
		browsestr += "&startEndTime="+value.replace(' ','+');
		startEndTime = value;
	    }
	    else if (name.equals("regDeadLine")){
		browsestr += "&regDeadLine="+value;
		regDeadLine = value;
	    }
	    else if (name.equals("inCityFee")){
		browsestr += "&inCityFee="+value;
		inCityFee = value;
	    }
	    else if (name.equals("nonCityFee")){
		browsestr += "&nonCityFee="+value;
		nonCityFee = value;
	    }
	    else if (name.equals("otherFee")){
		browsestr += "&inCityFee="+value;
		otherFee = value;
	    }
	    else if (name.equals("location_id")){
		browsestr += "&location_id="+value;
		location_id = Helper.escapeIt(value);
	    }
	    else if (name.equals("ageFrom")){
		browsestr += "&ageFrom="+value;
		ageFrom = value;
	    }
	    else if (name.equals("partGrade")){
		browsestr += "&partGrade="+value.replace(' ','+');
		partGrade = value;
	    }
	    else if (name.equals("minMaxEnroll")){
		browsestr += "&minMaxEnroll="+value.replace(' ','+');
		minMaxEnroll = value;
	    }
	    else if (name.equals("classCount")){
		browsestr += "&classCount="+value;
		classCount = value;
	    }
	    else if (name.equals("instructor")){
		browsestr += "&instructor="+value.replace(' ','+');
		instructor = value;
	    }
	    else if (name.equals("description")){
		browsestr += "&description="+value.replace(' ','+');
		description = value;
	    }
	    else if (name.equals("area_id")){
		browsestr += "&area_id="+value;
		area_id = value;
	    }
	    else if (name.equals("sortby")){
		browsestr += "&sortby="+value.replace(' ','+');
		sortby = value;
	    }
	    else {
		// System.err.println("Unknown choice "+name+" "+value);
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
	String qq = "";
	try{
	    con = Helper.getConnection();
	    if(con != null){
		stmt = con.createStatement();
	    }
	    else{
		success = false;
		message += " Could not connect to Database";
		logger.error(message);
	    }
	    if(minRecords > maxRecords){ //swap
		int temp = minRecords;
		minRecords = maxRecords;
		maxRecords = temp;
	    }
	    os.println("<head><title>Browsing Sessions" + 
		       "</title>");
	    Helper.writeWebCss(os, url);
	    os.println("</head><body>");
	    Helper.writeTopMenu(os, url);
	    os.println("<form name=anyform>");
	    String back_to_browse = 
		"<a href="+url+"SessionsBrowse?"+
		"Back to Query Session</a>";

	    os.println("<h2>Sessions Records </h2>");
	    //
	    // check where clause 
	    //
	    Vector<String> wherecases = new Vector<String>();
	    boolean andFlag = false;
	    wherecases.addElement("ps.id=pp.id and not ps.sid=-1"); // program id 
	    andFlag = true;
	    if(!id.equals("")){
		wherecases.add(" and pp.id="+id);
		andFlag = true;
	    }	    
	    if(!year.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "pp.year='"+year+"'";
		wherecases.add(str);
		andFlag = true;
	    }	   
	    if(!season.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "pp.season='"+season+"'";
		wherecases.add(str);
		andFlag = true;
	    }	   
	    if(!category_id.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += " (pp.category_id="+category_id+" or pp.category2_id='"+
		    category_id+"')";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!area_id.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "pp.area_id="+area_id+"";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!code.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.code like '%"+code+"%'";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!title.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "pp.title like '%"+title+"%'";

		wherecases.add(str);
		andFlag = true;
	    }	
	    if(!days.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.days like '%"+days+"%'";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!startDate.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.startDate= str_to_date('"+startDate+"','%m/%d/%Y')";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!startEndTime.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.startEndTime like '%"+startEndTime+"%'";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!regDeadLine.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.regDeadLine=str_to_date('"+regDeadLine+"','%m/%d/%Y')";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!inCityFee.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.inCityFee='"+inCityFee+"'";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!nonCityFee.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.nonCityFee='"+nonCityFee+"'";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!otherFee.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.otherFee='"+otherFee+"'";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!location_id.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.location = "+location_id+"";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!ageFrom.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.ageFrom="+ageFrom;
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!partGrade.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.partGrade like '%"+partGrade+"%'";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!classCount.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.classCount='"+classCount+"'";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!description.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.description like '%"+description+"%'";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    if(!instructor.equals("")){
		String str="";
		if(andFlag) str = " and ";
		str += "ps.instructor like '%"+instructor+"%'";
		wherecases.add(str);
		andFlag = true;
	    }	  
	    String added = "pp.year,pp.season,pp.area_id,pp.category_id,"+
		"pp.title,ps.sid";
	    String query2 = "select count(*) from programs_sessions ps,"+
		"programs pp";
	    if(sortby.equals("catProgCode")) sortby = added;
	    else sortby += ","+added;
	    //
	    // Report representation
	    //
	    String query = 
		"select sid,pp.year,pp.season,a.name,g.name,"+
		"pp.title,pp.id,"+
		"ps.code,ps.days,"+
		"concat_ws('-',date_format(ps.startDate,'%m/%d'),"+
		" date_format(ps.endDate,'%m/%d')),"+
		"ps.startEndTime,"+
		"date_format(ps.regDeadLine,'%m/%d'),"+
		"ps.inCityFee,ps.nonCityFee,ps.otherFee,"+
		"ps.location,ps.allAge,ps.partGrade,ps.minMaxEnroll,"+
		"ps.classCount,ps.description,ps.instructor "+
		"from programs pp left join areas a on a.id=pp.area_id left join categories g on g.id=pp.category_id,"+
		"programs_sessions ps ";

	    if(wherecases.size() > 0){
		query += " where ";
		query2 += " where ";
		for (String str: wherecases){
		    query  += str;
		    query2 += str;
		}
	    }
	    query += " order by "+sortby;
	    qq = query2;
	    if(debug)
		logger.debug(qq);
	    rs = stmt.executeQuery(query2);
	    int total = 0;
	    if(rs.next()){
		total = rs.getInt(1);
	    }
	    os.println("Matching total records :"+ total + 
		       "<br>");
	    if(total < maxRecords && minRecords == 0) 
		os.println("Showing all the: "+ total + 
			   " records <br>");
	    else if(total <= maxRecords && total > minRecords)
		os.println("Showing the records from:"+ minRecords +
			   " to " + total+ "<br>");
	    else if(total > maxRecords && total > minRecords)
		os.println("Showing the records from:"+ minRecords +
			   " to " + maxRecords+ "<br>");
	    else if(total < minRecords){
		os.println("Error in setting the \"From\" field in "+
			   "\"Show Records\", go back and "+
			   "reset this field. <br>");
		rangeFlag = false;
	    }
	    if(rangeFlag){
		qq = query;
		if(debug)
		    logger.debug(qq);				
		rs = stmt.executeQuery(query);
		int colcnt = rs.getMetaData().getColumnCount();
		os.println("<center><Font size=+2>Park and Recreation"+
			   "</Font><br>");
		os.println("<Font size=+1>Sessions' Report</Font><br>"+
			   "<br><br>");
		os.println("<hr></center>");
		//
		//  System.out.println("After rs.next");
		row = 0;
		while (rs.next()){
		    String code2="", year2="", season2="",title2="";
		    String category2="", id2="", sid2="",str=""; 
		    String cat2="", pt2="", area2=""; 
		    if(row >= minRecords && row <= maxRecords){
			os.println("<table border=0 cellspacing=1 "+
				   "cellpadding=1>");
			str = rs.getString(1);
			if(str != null) sid2 = str.trim();
			str = rs.getString(2);
			if(str != null) year2 = str.trim();
			str = rs.getString(3);
			if(str != null) season2 = str.trim();
			str = rs.getString(4);
			if(str != null){ 
			    area2 = str;
			}
			str = rs.getString(5);
			if(str != null){ 
			    category2 = str;
			}

			str = rs.getString(6);
			if(str != null){ 
			    pt2 = str.trim();
			    title2 = pt2.replace(' ','+');
			}
			str = rs.getString(7);
			if(str != null) id2 = str.trim();
			str = rs.getString(8);
			if(str != null) code2 = str.trim();

			String progZoom = "id="+id2+
			    "&action=zoom";

			// sid
			os.println("<tr><td align=right><strong>");
			os.println(titles[0]+" :&nbsp;</strong>"+
				   "</td><td align=left>");
			os.println("<a href="+url+
				   "Sessions?"+
				   "id="+id2+
				   "&action=zoom"+
				   "&year="+year2+
				   "&season="+season2+
				   "&sid="+sid2+
				   ">"+sid2+"</a>");
			os.println("</td></tr>");
			//year
			os.println("<tr><td align=right><strong>");
			os.println(titles[1]+" :&nbsp;</strong>"+
				   "</td><td align=left>");
			os.println(year2+"</td></tr>");
			//season
			os.println("<tr><td align=right><strong>");
			os.println(titles[2]+" :&nbsp;</strong>"+
				   "</td><td align=left>");
			os.println(season2+"</td></tr>");

			//area
			if(!area2.equals("")){
			    os.println("<tr><td align=right><strong>");
			    os.println(titles[3]+" :&nbsp;</strong>"+
				       "</td><td align=left>");
			    os.println(area2+"</td></tr>");
			}
			//cat
			if(!category2.equals("")){
			    os.println("<tr><td align=right><strong>");
			    os.println(titles[4]+" :&nbsp;</strong>"+
				       "</td><td align=left>");
			    os.println(category2+"</td></tr>");
			}
			//title
			os.println("<tr><td align=right><strong>");
			os.println(titles[5]+" :&nbsp;</strong>"+
				   "</td><td align=left>");
			os.println("<a href="+url+
				   "Program.do?"+
				   "id="+id2+
				   "&action=zoom"+
				   ">"+pt2+"</a>");
			os.println("</td></tr>");
			// skip 6 id
			//
			// code
			if(!code2.equals("")){
			    os.println("<tr><td align=right><strong>");
			    os.println(titles[7]+" :&nbsp;</strong>"+
				       "</td><td align=left>");
			    os.println(code2+"</td></tr>");
			}
			//
			for(int i=8; i<titles.length;i++){
			    that = rs.getString(i+1);
			    if(that != null){
				writeItem(os, that, titles[i]);
			    }
			}
			os.println("</table>");
			os.println("<br><br><hr>");
			//
			os.flush();
		    }
		    row++;
		    if(row > maxRecords) break;
		}
	    }
	    os.println("</form>");
	    os.println(back_to_browse +"<br/>"+"<br/>");
	    os.println("</body>");
	    os.println("</html>");
	}
	catch (Exception ex){
	    logger.error(ex+":"+qq);
	    os.println(ex);
	}
	os.flush();
	os.close();
	Helper.databaseDisconnect(con, stmt, rs);

    }
    /**
     * Writes pairs of item title in  table cells.
     * @param out 
     * @param that the item
     * @title the item title
     */
    void writeItem(PrintWriter os, String that, String title){
	os.println("<tr><td align=\"right\"><strong>");
	os.println(title+" :&nbsp;</strong></td><td align=\"left\">");
	os.println(that+"</td></tr>");
    }

}






















































