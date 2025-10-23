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

@WebServlet(urlPatterns = {"/CodeTable","/CodeResult"})
public class CodeResult extends TopServlet{

    final static int recCount = 20;
    static Logger logger = LogManager.getLogger(CodeResult.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 

	throws ServletException, IOException{
	doPost(req, res);

    }
    /**
     * processes the code search request.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{

	
	String browsestr = "";
	
	int minRecords=0, maxRecords = 100;
	boolean errorFlag = false, success = true;
	String message = ""; 
	String sortby = "", action="",season="",year="",id2="";
	String [] code = new String[recCount];
	String [] id = new String[recCount];
	String [] sid = new String[recCount];
	String [] gid = new String[recCount];
	String [] g_code = new String[recCount];
	//
	// initialize
	for(int i=0; i<recCount; i++){
	    code[i] = "";
	    id[i] = "";
	    sid[i] = "";
	    // general listing
	    gid[i] = "";
	    g_code[i] = "";
	}
		
	String area_id="", category_id="",lead_id="",nraccount="", date_from="", date_to="";
	browsestr = "";
	Enumeration values = req.getParameterNames();
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	boolean rangeFlag = true;
	out.println("<html>");
	ProgCodeList pl = new ProgCodeList(debug);
	GeneralCodeList gl = new GeneralCodeList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if (name.equals("year")){
		pl.setYear(value);
		gl.setYear(value);
		year = value;
	    }
	    else if (name.equals("season")){
		pl.setSeason(value);
		gl.setSeason(value);
		season = value;
	    }
	    else if (name.equals("lead_id")){
		pl.setLead_id(value);
		gl.setLead_id(value);
		lead_id = value;
	    }
	    else if (name.equals("date_from")){
		date_from = value;
		pl.setDateFrom(value);
	    }
	    else if (name.equals("date_to")){
		date_to = value;
		pl.setDateTo(value);
	    }			
	    else if (name.equals("area_id")){
		pl.setArea_id(value);
		area_id = value;
	    }
	    else if (name.equals("category_id")){
		pl.setCategory_id(value);
		category_id = value;
	    }
	    else if (name.equals("nraccount")){
		nraccount = value;
		pl.setNraccount(value);
	    }
	    else if (name.equals("action")){
		action = value;
	    }
	    else if (name.equals("id")){
		id2 = value;
		pl.setId(value);
	    }
	    else if (name.startsWith("id")){
		for(int i=0; i<recCount; i++)
		    if(name.equals("id"+i)){
			id[i] = value;
			break;
		    }
	    }
	    else if (name.startsWith("sid")){
		for(int i=0; i<recCount; i++)
		    if(name.equals("sid"+i)){
			sid[i] = value;
			break;
		    }
	    }
	    else if (name.startsWith("code")){
		for(int i=0; i<recCount; i++)
		    if(name.equals("code"+i)){
			code[i] = value;
			break;
		    }
	    }
	    else if (name.startsWith("gid")){
		for(int i=0; i<recCount; i++)
		    if(name.equals("gid"+i)){
			gid[i] = value;
			break;
		    }
	    }
	    else if (name.startsWith("g_code")){
		for(int i=0; i<recCount; i++)
		    if(name.equals("g_code"+i)){
			g_code[i] = value;
			break;
		    }
	    }						
	    else if (name.equals("sortBy")){
		sortby = value;
		pl.setSortBy(value);
		if(!value.equals(""))
		    browsestr +="&sortby="+value;
	    }
	}
	pl.setCodeNeed("y");
	pl.setHasNoCodeYet();
	pl.includeSessions();
	User user = null;
	HttpSession	hsession = req.getSession(false);
	Control control = null;
	if(hsession != null){
	    user = (User)hsession.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	out.println("<head><title>Browsing Records</title>");
	out.println("<link rel=\"stylesheet\" href=\""+url+"css/menu_style.css\" />");		
	out.println("</head>");
	out.println("<body>");
	Helper.writeTopMenu(out, url);	
	out.println("<form name=\"anyform\">");
	out.println("<input type=\"hidden\"  name=\"year\" value=\"" + year + 
		    "\"></input>");
	out.println("<input type=\"hidden\"  name=\"season\" value=\"" + season + 
		    "\" />");
	if(!lead_id.equals("")){
	    out.println("<input type=\"hidden\"  name=\"lead_id\" value=\"" + lead_id + 
			"\" />");
	}
	if(!area_id.equals("")){
	    out.println("<input type=\"hidden\"  name=\"area_id\" value=\"" + area_id + 
			"\" />");
	}
	if(!sortby.equals("")){
	    out.println("<input type=\"hidden\"  name=\"sortby\" value=\"" + sortby + 
			"\" />");
	}
	if(!id2.equals("")){
	    out.println("<input type=\"hidden\"  name=\"id\" value=\"" +id2+ 
			"\" />");
	}
	if(!date_from.equals("")){
	    out.println("<input type=\"hidden\"  name=\"date_from\" value=\"" +date_from+ 
			"\" />");
	}
	if(!date_to.equals("")){
	    out.println("<input type=\"hidden\"  name=\"date_to\" value=\"" +date_to+ 
			"\" />");
	}		
	if(!category_id.equals("")){
	    out.println("<input type=\"hidden\"  name=\"category_id\" value=\"" + 
			category_id + "\" />");
	}
	if(!nraccount.equals("")){
	    out.println("<input type=\"hidden\"  name=\"nraccount\" value=\"" + 
			nraccount + "\" />");
	}
	out.println("<br>");
	//
	if(action.startsWith("Update")){
	    String back = pl.doUpdate(id, sid, code);
	    back = gl.doUpdate(gid, g_code);
	    //
	    // reinitailize to empty
	    //
	    for(int i=0; i<recCount; i++){
		code[i] = "";
		id[i] = "";
		sid[i] = "";
		gid[i] = "";
		g_code[i] = "";
	    }
	}
	List<ProgCode> list =  null;
	pl.setLimit();
	message = pl.lookFor();
	if(message.isEmpty()){
	    list = pl.getList();
	}
	else{
	    success = false;
	}
	List<GeneralCode> glist =  null;
	gl.setLimit();
	gl.setHasNoCodeYet();
	String back = gl.lookFor();
	if(back.isEmpty()){
	    glist = gl.getList();
	}
	else{
	    if(!message.isEmpty()) message += ", ";
	    message += back;
	    success = false;
	}				
	//
	// Search for programs and sessions that need codes
	//
	out.println("<center>");
	out.println("<Font size=\"+3\">Code Entry Page</Font><br><br>");
	if(!success){
	    if(!message.equals(""))
		out.println("<font color=\"red\">"+message+"</font><br />");
	}
	out.println("<font color=\"green\">Note: Maximum of "+recCount+" records will "+
		    "be shown at a time.<br>");
	out.println("Enter the codes for these events and click on Update "+
		    " to get to the next "+recCount+" records (if any)<br /><br /></font>");

	out.println("<b>Year:</b> "+year+"-"+season+"<br />");
	boolean hasList = false;
	if((list == null || list.size() == 0) && (glist == null || glist.size() == 0)){
	    out.println("<font color=\"green\" size=\"+2\">"+
			"No records to show</font><br />");
	}
	else{
	    hasList = true;
	}
	if(list != null && list.size() > 0){
	    //
	    out.println("<p>Note: The general listing are listed in a separate tble (if any)</p><br />");
	    out.println("<table border=\"1\" width=\"100%\">");
	    out.println("<caption>Programs and Sessions</caption>");
	    out.println("<tr><th>No.</th><th>Program</th><th>Session</th>"+
			"<th>Category</th>"+
			"<th>Lead</th><th>Code</th></tr>");
	    //
	    int jj=0;
	    for(ProgCode one:list){
		out.println("<tr><td valign=\"top\">"+(jj+1)+"</td>"+
			    "<td valign=\"top\">"+
			    one.getTitle()+" </td>");
		out.println("<td valign=\"top\">"+one.getSid()+"&nbsp;</td>");
		out.println("<td valign=\"top\">"+one.getCategory_name()+" </td>");
		out.println("<td valign=\"top\">"+one.getLead_name()+"</td>");
		out.println("<td valign=\"top\">");
		out.println("<input type=\"text\" name=\"code"+jj+"\""+ 
			    " size=\"10\" maxlength=\"10\" value=\"\" />");
		out.println("</td></tr>");
		out.println("<input type=\"hidden\"  name=\"id"+jj+"\" value=\"" + 
			    one.getId() + "\" />");
		if(one.hasSid()){
		    out.println("<input type=\"hidden\"  name=\"sid"+jj+
				"\" value=\"" + one.getSid() + "\" />");
		}
		jj++;
	    }
	    out.println("</table>");
	}
	if(glist.size() > 0){
	    //
	    out.println("<table border=\"1\" width=\"100%\">");
	    out.println("<caption>General Listings</caption>");
	    out.println("<tr><th>No.</th><th>Title</th><th>ID</th>"+
			"<th>Lead</th><th>Code</th></tr>");
	    //
	    int jj=0;
	    for(GeneralCode one:glist){
		out.println("<tr><td valign=\"top\">"+(jj+1)+"</td>"+
			    "<td valign=\"top\">"+
			    one.getTitle()+" </td>");
		out.println("<td valign=\"top\">"+one.getId()+"&nbsp;</td>");
		out.println("<td valign=\"top\">"+one.getLead_name()+"</td>");
		out.println("<td valign=\"top\">");
		out.println("<input type=\"text\" name=\"g_code"+jj+"\""+ 
			    " size=\"10\" maxlength=\"10\" value=\"\" />");
		out.println("<input type=\"hidden\"  name=\"gid"+jj+"\" value=\"" + 
			    one.getId() + "\" />");
		out.println("</td></tr>");

		jj++;
	    }
	    out.println("</table>");

	}
	if(hasList){
	    out.println("<input type=\"submit\" name=\"action\" "+
			"value=\"Update & Next\"><br />");
	}
	out.println("</form>");
	out.println("</body>");
	out.println("</html>");

	out.flush();
	out.close();
    }

}






















































