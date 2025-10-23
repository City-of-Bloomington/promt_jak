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

@WebServlet(urlPatterns = {"/PlanBrowse","/PlanSearch"})
public class PlanSearch extends TopServlet{

    static Logger logger = LogManager.getLogger(PlanSearch.class);
    String staffSelectArr[] = Helper.staffSelectArr;
    /**
     * Create an html page for the search engine form.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	PrintWriter out;
	out = res.getWriter();

	String id="",program="",objective="",profit_obj="",instructor="",
	    phone="",address="",email="",market="",username="";
	String season="", year="";
	String sortby = "", message="";
	String allLead="";
	String allPrograms="";
	String name, value;

	Enumeration values = req.getParameterNames();
	boolean success = true;
	out.println("<html>");
	PlanList allPlans = null;
	String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	

	    if (name.equals("id")){
		id = value;
	    }
	    else if (name.equals("program")){
		program = value;
	    }
	    else if (name.equals("season")){
		season = value;
	    }
	    else if (name.equals("year")){
		year = value;
	    }
	    else if (name.equals("name")){
		name = value;
	    }						
	    else if (name.equals("instructor")){
		instructor = value;
	    }
	    else if (name.equals("phone")){
		phone = value;
	    }
	    else if (name.equals("email")){
		email = value;
	    }
	    else if (name.equals("objective")){
		objective = value;
	    }
	    else if (name.equals("profit_obj")){
		profit_obj = value;
	    }
	    else if (name.equals("market")){
		market = value;
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
	TypeList leads = null;
	if(true){
	    leads = new TypeList(debug, "leads");
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    allPlans = new PlanList(debug);
	    back = allPlans.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }			
	}
	//
	// Browsing the records
	//
	out.println("<html>");
	out.println("<head><title>Search Plans</title>");

	//
	// This script validate 
	//
	Helper.writeWebCss(out, url);
	out.println("</head><body><center>");
	Helper.writeTopMenu(out, url);
	out.println("<h2>Plan/Pre Plan Search</h2>");
	out.println("<form name=\"myForm\" method=\"post\">");
	out.println("<input type=\"hidden\" name=\"minRecords\" value=\"0\" />");
	out.println("<table align=\"center\" border=\"1\" width=\"90%\">");
	out.println("<caption>Search Options</caption>");
	// sortby
	out.println("<tr><td align=\"right\">");
	out.println("<label for=\"sortby\">Sort by: </label></td><td align=\"left\">");
	out.println("<select name=\"sortby\" id=\"sortby\">");
	out.println("<option value=\"pl.id\" selected>ID</option>");
	out.println("<option value=\"program\">Program Title</option>");
	out.println("</select></td></tr>");
	//
	out.println("<tr><td align=\"right\">");

	out.println("<input type=\"text\" name=\"maxRecords\" value=\"100\" "+
		    "size=\"6\" id=\"maxr\"/> <label for=\"maxr\">Show records/page </label></td></tr>");
	out.println("<tr><td align=\"right\">");	
	out.println("<label for=\"id\">Plan/Pre Plan ID: </label>"+
		    "</td><td>");
	out.println("<input type=\"text\" name=\"id\" value=\""+id+
		    "\" size=\"8\" id=\"id\" /></td></tr>");
	//
	out.println("<tr><td align=\"right\">");				
	out.println("<label for=\"season\">Season: </label></td><td align=\"left\">");
	out.println("<select name=\"season\" id=\"season\"> ");
	out.println("<option value=\"\">All</otion>");
	if(!season.equals(""))
	    out.println("<option value=\""+season+"\" selected=\"selected\">"+season+"</option>\n");
	out.println(Helper.allSeasons);
	out.println("</select>");
	out.println("<label for=\"year\">Year: </label>");
	out.println("<select name=\"year\" id=\"year\"> ");
	int years[] = Helper.getPrevYears();
	out.println("<option value=\"\">All\n");		
	for(int yy:years){
	    out.println("<option>"+yy+"</option>");
	}
	out.println("</select>");
	out.println("</td></tr>");				
	out.println("<tr><td align=right>"+
		    "<label for=\"lead_id\">Lead: </label></td>");
	out.println("<td align=\"left\">");
	out.println("<select name=\"lead_id\" id=\"lead_id\">");
	out.println("<option value=\"\">All</option>");
	if(leads != null){
	    for(Type one:leads){
		String selected = "";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select></td></tr>");
				
	out.println("<tr><td align=\"right\" valign=\"bottom\">");
	out.println("<label for=\"ptitle\">Plan Program Title: </label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"program\" maxlength=\"70\" size=\"40\" id=\"ptitle\" />");
	out.println("</td></tr>");
	//
	// instructor
	out.println("<tr><td align=\"right\" valign=\"bottom\">");				
	out.println("<label for=\"instr\">Instructor Name: </label>");
	out.println("</td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"instructor\" maxlength=\"50\" size=\"30\" id=\"instr\" />");
	out.println("</td></tr>");
	//
	// 
	// objective
	out.println("<tr><td align=\"right\" valign=\"bottom\">");
	out.println("<label for=\"obj\">Program Objective: </label>");
	out.println("</td><td align=\"left\">");

	out.println("<input type=\"text\" name=\"objective\" "+
		    " maxlength=\"50\" size=\"30\" id=\"obj\" />");
	out.println("</td></tr>");
	// 
	// target market
	out.println("<tr><td align=\"right\" valign=\"bottom\">");
	out.println("<label for=\"target\">Target Market: </label>");
	out.println("</td><td><font color=\"green\" size=\"-1\">"+
		    "Key word or a phrase<br></font>");
	out.println("<input type=\"text\" name=\"market\" maxlength=\"50\" "+
		    " size=\"30\" id=\"target\"/>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"date_from\">Pre Plan Date, from </label>");
	out.println("</td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"date_from\" maxlength=\"10\" "+
		    "value=\"\" id=\"date_from\" size=\"10\" class=\"date\" />");
	out.println("<label for=\"date_to\"> to </label>");
	out.println("<input type=\"text\" name=\"date_to\" maxlength=\"10\" "+
		    "value=\"\" size=\"10\" class=\"date\" id=\"date_to\" />");
	out.println("(mm/dd/yyyy)</td></tr>");
	// 		
	out.println("<tr><td align=right><input type=\"submit\" value=\"Search\" "+
		    "></tr></table>");
	out.println("<br />");
	out.println("</form>");
	out.println("</center>");
	Helper.writeWebFooter(out, url);
	String dateStr = "{ nextText: \"Next\",prevText:\"Prev\", buttonText: \"Pick Date\", showOn: \"both\", navigationAsDateFormat: true, buttonImage: \""+url+"js/calendar.gif\"}";
	out.println("<script>");
	out.println("  $( \".dateTo\" ).datepicker("+dateStr+"); ");
	out.println("</script>");			
	out.println("</body>\n</html>");
	out.close();
    }
    /**
     * Generate the html page for the search form.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	boolean success = true;

	String[] titles = { "ID ",
	    "Program",
	    "Season & Year",
	    "Lead",
	    "Instructor",
														
	    "Ideas to Program",
	    "Goals",
	    "Profit Objective",
	    "Potential Partnership",
	    "Target Market",
														
	    "Intended Frequency", // 10
	    "Event Time", // 11
	    "Total Estimated Time", 
	    "Event History",  
	    "Supplies",
														
	    "Timeline",       // 15
	    "Staff"
	};

	int minRecords=0, maxRecords = 100;

	String message = "";
	String sortby = "", id="", id2="";
	//
	// staff type and count

	Enumeration values = req.getParameterNames();
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	PrintWriter os = res.getWriter();

	String name, value;
	boolean rangeFlag = true;
	os.println("<html>");
	PlanList pl = new PlanList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = Helper.escapeIt((req.getParameter(name)).trim());
	    if (name.equals("maxRecords")){
		try{
		    maxRecords = Integer.parseInt(value);
		}catch(Exception ex){};
	    }
	    else if (name.equals("minRecords")){
		try{
		    minRecords = Integer.parseInt(value);
		}catch(Exception ex){};
	    }
	    else if (name.equals("id")){
		if(!value.equals("")){
		    pl.setId(value);
		    id = value;
		}
	    }
	    else if (name.equals("id2")){
		if(!value.equals("")){
		    pl.setId(value);
		    id = value;
		}
	    }
	    else if (name.equals("lead_id")){
		pl.setLead_id(value);
	    }
	    else if (name.equals("season")){
		pl.setSeason(value);
	    }
	    else if (name.equals("year")){
		pl.setYear(value);
	    }
	    else if (name.equals("program")){
		pl.setProgram_title(value);
	    }						
	    else if (name.equals("date_from")){
		pl.setDate_from(value);
	    }
	    else if (name.equals("date_to")){
		pl.setDate_to(value);
	    }			
	    else if (name.equals("objective")){
		pl.setObjective(value);
	    }
	    else if (name.equals("market")){
		pl.setMarket(value);
	    }
	    else if (name.equals("profit_obj")){
		pl.setProfit_obj(value);
	    }
	    else if (name.equals("instructor")){
		pl.setInstructor(value);
	    }
	    else if (name.equals("sortby")){
		pl.setSortby(value);
		sortby = value;
	    }
	}
	message = pl.find();		
	if(pl.size() == 1){
	    res.sendRedirect(url+"ProgPlan?action=zoom&id="+pl.get(0).getId());
	    return;
	}
	if(minRecords > maxRecords){ //swap
	    int temp = minRecords;
	    minRecords = maxRecords;
	    maxRecords = temp;
	}
	os.println("<head><title>Browsing Records" + 
		   "</title>");
	Helper.writeWebCss(os, url);
	os.println("</head><body><center>");
	Helper.writeTopMenu(os, url);
	//
	String str="";
	//

	if(!message.equals("")){
	    os.println("<h3>"+message+"</h3>");
	}
	try{
	    os.println("<Font size=\"+2\">Parks and Recreation"+
		       "</Font><br />");
	    os.println("<Font size=\"+1\">Program Plans </Font><br><br><br>");
	    os.println("<hr width=\"75%\" size=\"4\">");
	    if(pl.size() == 0){
		os.println("No records found <br />");
	    }
	    else{
		os.println("Total records :"+ pl.size() + "<br />");
	    }
	    for(Plan plan:pl){
		os.println("<table width=\"90%\">");
		os.println("<caption> Search Results</caption>");
		os.println("<tr><td align=\"right\" width=\"20%\"><b>ID:");
		os.println("</b></td><left><td>");
		plan.doSelect();
		String that = plan.getId();
		//
		// reference for zooming
		//
		os.println("<a href=\""+url+"ProgPlan?id="+
			   that+"&action=zoom\">"+that+"</a>");
		os.println("</td></tr>");
		that = plan.getProgram_title();
		if(that != null)
		    writeItem(that, titles[1], os);
		that = plan.getYear_season();
		if(that != null)
		    writeItem(that, titles[2], os);
		//
		Lead one = plan.getLead();
		if(one != null)
		    writeItem(one.getName(), titles[3], os);
		Contact one2 = plan.getInstructor();
		if(one2 != null)
		    writeItem(one2.getName(), titles[4], os);								
		that = plan.getIdeas();
		if(that != null)
		    writeItem(that, titles[5], os);
		that = plan.getGoals();
		if(that != null)
		    writeItem(that, titles[6], os);								
		that = plan.getProfit_obj();
		if(that != null)
		    writeItem(that, titles[7], os);
		that = plan.getPartner();
		if(that != null)
		    writeItem(that, titles[8], os);						
		that = plan.getMarket();
		if(that != null)
		    writeItem(that, titles[9], os);
		that = plan.getFrequency();
		if(that != null)
		    writeItem(that, titles[10], os);
		that = plan.getEvent_time();
		if(that != null)
		    writeItem(that, titles[11], os);
		that = plan.getEst_time();
		if(that != null)
		    writeItem(that, titles[12], os);
		that = plan.getHistory();
		if(that != null)
		    writeItem(that, titles[13], os);
		that = plan.getSupply();
		if(that != null)
		    writeItem(that, titles[14], os);
		that = plan.getTimeline();
		if(that != null)
		    writeItem(that, titles[15], os);
		os.println("</table>");
		os.println("<br /><br />");
	    }
	    os.println("</body>");
	    os.println("</html>");
	}
	catch (Exception ex){
	    logger.error(ex);
	    os.println(ex);
	}
	os.flush();
	os.close();
    }
    /**
     * Aranges the output as a table.
     * @param that the details of an item.
     * @param title the item title
     *
     */
    void writeItem(String that, String title, PrintWriter os){
	if(!that.equals("")){
	    os.println("<tr><td align=\"right\" valign=\"top\"><b>");
	    os.println(title+"</b></td><td align=\"left\">");
	    os.println(that+"</td></tr>");
	}
    }
    //

}






















































