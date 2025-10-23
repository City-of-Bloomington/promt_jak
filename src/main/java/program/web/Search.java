package program.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import jakarta.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;
import program.model.*;

@WebServlet(urlPatterns = {"/Browse","/Search"})
public class Search extends TopServlet{

    static Logger logger = LogManager.getLogger(Search.class);
    String lastUpdate = "";
		
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
	PrintWriter out = res.getWriter();
	LeadList leads = null;
	TypeList areas = null;
	TypeList locations = null;
	TypeList categories = null;
	List<Type> taxonomies = null;
	String sortby = "";
	String lead_id="", title="", season="", year="", plan_id=""; 
	String category_id="", category2_id="",statement="";
	String nraccount="", fee="", oginfo="";
	//
	// fields common in program and session
	// if used in program, should not be used in session
	//
	String inCityFee="", nonCityFee="",otherFee="";
	String location_id="", instructor="",regDeadLine="";
	String partAge="", partGrade="", description="",ageFrom="";
	String ageTo="";
	String minMaxEnroll="", description2="", code="", area_id="";
	String days="", allLocation="";
	String codeNeed="", dateFrom="", dateTo="", dateAt="", whichDate="";

	String codeTask="", marketTask="", volTask="", sponTask="";
	String needDuplication="", taxonomy_id="", taxonomy_ids="";
	Enumeration values = req.getParameterNames();

	String name, value;
	String id="", message="", advanceSearch="";
	String pid=""; // needed to duplicate from search
	boolean success = true;
	out.println("<html>");

	String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	

	    if (name.equals("id")){
		id = value;
	    }
	    else if (name.equals("plan_id")){
		plan_id = value;
	    }
	    else if (name.equals("taxonomy_id")){
		taxonomy_id = value;
	    }
	    else if (name.equals("taxonomy_ids")){
		taxonomy_ids = value;
	    }									
	    else if (name.equals("advanceSearch")){
		advanceSearch = value;
	    }						
	    else if (name.equals("needDuplication")){
		needDuplication = value;
	    }						
	    else if (name.equals("lead_id")){
		lead_id = value;
	    }
	    else if (name.equals("msg")){
		message = value;
	    }			
	    else if (name.equals("title")){
		title = value;
	    }
	    else if (name.equals("year")){
		year = value;
	    }
	    else if (name.equals("season")){
		season = value;
	    }
	    else if (name.equals("category_id")){
		category_id = value;
	    }
	    else if (name.equals("category2_id")){
		category2_id = value;
	    }
	    else if (name.equals("statement")){
		statement = value;
	    }
	    else if (name.equals("nraccount")){
		nraccount = value;
	    }
	    else if (name.equals("fee")){
		fee = value;
	    }
	    else if (name.equals("oginfo")){
		oginfo = value;
	    }
	    //
	    // common fields with sessions
	    //
	    else if(name.equals("inCityFee")){
		inCityFee = value;
	    }
	    else if(name.equals("nonCityFee")){
		nonCityFee = value;
	    }
	    else if(name.equals("otherFee")){
		otherFee = value;
	    }
	    else if(name.equals("partAge")){
		partAge = value;
	    }
	    else if(name.equals("ageFrom")){
		ageFrom = value;
	    }
	    else if(name.equals("ageTo")){
		ageTo = value;
	    }
	    else if(name.equals("partGrade")){
		partGrade = value;
	    }
	    else if(name.equals("minMaxEnroll")){
		minMaxEnroll = value;
	    }
	    else if(name.equals("description")){
		description = value;
		description2 = Helper.escapeIt(value);
	    }
	    else if(name.equals("location_id")){
		location_id = value;
	    }
	    else if(name.equals("instructor")){
		instructor = value;
	    }
	    else if (name.equals("code")){
		code = value;
	    }
	    else if (name.equals("codeNeed")){
		codeNeed = value;
	    }
	    else if (name.equals("area_id")){
		area_id = value;
	    }
	    else if (name.equals("whichDate")){
		whichDate = value;
	    }
	    else if (name.equals("dateAt")){
		dateAt = value;
	    }
	    else if (name.equals("dateFrom")){
		dateFrom = value;
	    }
	    else if (name.equals("dateTo")){
		dateTo = value;
	    }
	}
	HttpSession session = req.getSession(false);
	Control control = null;
	User user = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	String today = Helper.getToday();
		
	if(true){	
	    leads = new LeadList(debug);
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    areas = new TypeList(debug, "areas");
	    back = areas.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    categories = new TypeList(debug, "categories");
	    back = categories.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    locations = new TypeList(debug, "locations");
	    back = locations.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    TypeList taxos = new TypeList(debug, "taxonomies");
	    back = taxos.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		taxonomies = taxos;
	    }						
	}
	//
	// Browsing the records
	//
	out.println("<html>");
	out.println("<head><title>Browsing Donation Records</title>");
	//
	Helper.writeWebCss(out, url);
	//
	// This script validate 
	//
	out.println("<script type='text/javascript'>");
	out.println("/*<![CDATA[*/");				
	//
	out.println(" var allTax = []; ");
	TaxonomySubList tsl = null;
	for(Type one:taxonomies){
	    tsl = new TaxonomySubList(debug, one.getId());
	    String back = tsl.find();
	    if(back.equals("")){
		out.println(" var subTax = []; ");								
		for(TaxonomySub one2:tsl){
		    out.println(" subTax["+one2.getId()+"]=\""+one2+"\"; ");
		}
		out.println(" allTax["+one.getId()+"]=subTax;");
	    }
	}				
	out.println("  function createSubList(obj, sub_id) {           ");
	out.println("   var kk = obj.options.selectedIndex;         ");
	out.println("   var key = obj.options[kk].value;    ");
	out.println("   var text = obj.options[kk].text;    ");
	out.println("   var sct = document.getElementById(sub_id); ");
	// remove old options
	out.println("   sct.options.length = 0; ");
	out.println("   sct.options[0] = new Option ('', ' '); ");
        out.println("   sct.options[0].selected=\"true\"; ");
	// out.println("    alert('obj '+sct);                  ");
	out.println("	   if(allTax.hasOwnProperty(key)){             ");
	out.println("	     var subTax = allTax[key];               ");
	out.println("	     for(var k in subTax){               ");
	// out.println("	      alert(k+' '+subTax[k]); ");
	out.println("	       if(subTax.hasOwnProperty(k)){    ");
	out.println("          opt = document.createElement('option'); ");
	out.println("          opt.value=k; ");
	out.println("          opt.text=subTax[k]; ");
	out.println("          sct.appendChild(opt); ");				
	out.println(" 	    }                              ");
	out.println(" 	  }                              ");				
	out.println("  }}                                   ");
				
	if(needDuplication.equals("") && !advanceSearch.equals("")){
	    // these are for the second form
	    out.println(" function validateForm2(){	           ");
	    // if ok we copy the fields to the sencond form 
	    out.println("  var jj, val; ");
	    out.println("  jj = document.myForm.season.selectedIndex; ");
	    out.println("  val = document.myForm.season.options[jj].value; ");
	    out.println("  document.myForm2.season.value=val; ");
	    out.println("  jj = document.myForm.year.selectedIndex; ");
	    out.println("  val = document.myForm.year.options[jj].value; ");
	    // out.println("  alert(\"Year \"+val);                   ");
	    out.println("  document.myForm2.year.value=val; ");
	    out.println("   jj = document.myForm.sortby.selectedIndex; ");
	    out.println("   val = document.myForm.sortby.options[jj].value; ");
	    out.println("   document.myForm2.sortby.value=val;     ");
	    out.println("  val = document.myForm.maxRecords.value; ");
	    out.println("  document.myForm2.maxRecords.value=val; ");
	    out.println("  val = document.myForm.id.value; ");
	    out.println("  document.myForm2.id.value=val; ");
	    out.println("  jj = document.myForm.lead_id.selectedIndex; ");
	    out.println("  val = document.myForm.lead_id.options[jj].value; ");
	    out.println("  document.myForm2.lead_id.value=val; ");
	    out.println("  val = document.myForm.title.value; ");
	    out.println("  document.myForm2.title.value=val; ");
	    out.println("  jj = document.myForm.category_id.selectedIndex; ");
	    out.println("  val = document.myForm.category_id.options[jj].value; ");
	    out.println("  document.myForm2.category_id.value=val; ");
	    out.println("  jj = document.myForm.area_id.selectedIndex; ");
	    out.println("  val = document.myForm.area_id.options[jj].value; ");
	    out.println("  document.myForm2.area_id.value=val; ");
	    out.println("  val = document.myForm.nraccount.value; ");
	    out.println("  document.myForm2.nraccount.value=val; ");
	    out.println("  val = document.myForm.fee.value; ");
	    out.println("  document.myForm2.fee.value=val; ");
	    out.println("  val = document.myForm.oginfo.value; ");
	    out.println("  document.myForm2.oginfo.value=val; ");
	    out.println("  val = document.myForm.code.value; ");
	    out.println("  document.myForm2.code.value=val; ");
	    out.println("  val = document.myForm.statement.value; ");
	    out.println("  document.myForm2.statement.value=val; ");
	    out.println("  if(document.myForm.codeNeed.checked) ");
	    out.println("   document.myForm2.codeNeed.value=\"y\"; ");
	    out.println("  val = document.myForm.inCityFee.value; ");
	    out.println("  document.myForm2.inCityFee.value=val; ");
	    out.println("  val = document.myForm.nonCityFee.value; ");
	    out.println("  document.myForm2.nonCityFee.value=val; ");
	    out.println("  val = document.myForm.otherFee.value; ");
	    out.println("  document.myForm2.otherFee.value=val; ");
	    out.println("  val = document.myForm.days.value; ");
	    out.println("  document.myForm2.days.value=val; ");
	    out.println("  val = document.myForm.ageFrom.value; ");
	    out.println("  document.myForm2.ageFrom.value=val; ");
	    out.println("  val = document.myForm.ageTo.value; ");
	    out.println("  document.myForm2.ageTo.value=val; ");
	    out.println("  if(document.myForm.wParent.checked) ");
	    out.println("   document.myForm2.wParent.value=\"y\"; ");
	    out.println("  val = document.myForm.dateAt.value; ");
	    out.println("  document.myForm2.dateAt.value=val; ");
	    out.println("  val = document.myForm.dateFrom.value; ");
	    out.println("  document.myForm2.dateFrom.value=val; ");
	    out.println("  val = document.myForm.dateTo.value; ");
	    out.println("  document.myForm2.dateTo.value = val; ");
	    out.println("  jj = document.myForm.location_id.selectedIndex; ");
	    out.println("  val = document.myForm.location_id.options[jj].value; ");
	    out.println("  document.myForm2.location_id.value=val; ");
	    out.println("  val = document.myForm.instructor.value; ");
	    out.println("  document.myForm2.instructor.value=val; ");
	    out.println("  if(document.myForm.codeTask.checked) ");
	    out.println("   document.myForm2.codeTask.value=\"y\"; ");
	    out.println("  if(document.myForm.marketTask.checked) ");
	    out.println("   document.myForm2.marketTask.value=\"y\"; ");
	    out.println("  if(document.myForm.volTask.checked) ");
	    out.println("   document.myForm2.volTask.value=\"y\"; ");
	    out.println("  if(document.myForm.sponTask.checked) ");
	    out.println("   document.myForm2.sponTask.value=\"y\"; ");
	    out.println("  if(document.myForm.budgetTask.checked) ");
	    out.println("   document.myForm2.budgetTask.value=\"y\"; ");
	    out.println("  if(document.myForm.evalTask.checked) ");
	    out.println("   document.myForm2.evalTask.value=\"y\"; ");
	    out.println("  if(document.myForm.unCode.checked) ");
	    out.println("   document.myForm2.unCode.value=\"y\"; ");
	    out.println("  if(document.myForm.unMarket.checked) ");
	    out.println("   document.myForm2.unMarket.value=\"y\"; ");
	    out.println("  if(document.myForm.unVol.checked) ");
	    out.println("   document.myForm2.unVol.value=\"y\"; ");
	    out.println("  if(document.myForm.unSpon.checked) ");
	    out.println("   document.myForm2.unSpon.value=\"y\"; ");
	    out.println("  if(document.myForm.unBudget.checked) ");
	    out.println("   document.myForm2.unBudget.value=\"y\"; ");
	    out.println("  if(document.myForm.unEval.checked) ");
	    out.println("   document.myForm2.unEval.value=\"y\"; ");
	    out.println("  for(var i=0;i<3;i++){             ");
	    out.println("   if(document.myForm.whichDate[i].checked){ ");
	    out.println("     var xx = document.myForm.whichDate[i].value; ");
	    out.println("     document.myForm2.whichDate.value=xx; ");
	    out.println("     }                               ");
	    out.println("    }                                ");
	    out.println("   return true;                      ");
	    out.println("   }                                ");
	}
	out.println("/*]]>*/\n");							
	out.println(" </script>                          ");
	out.println("</head><body>");
	Helper.writeTopMenu(out, url);
	out.println("<center><h2>Programs Search</h2>");

	if(!message.equals("")){
	    out.println("<p>"+message+"</p>");
	}
				
	if(!needDuplication.equals("")){
	    out.println("<p>Search for the program to duplicate</p>");
	}
	else if(advanceSearch.equals("")){
	    out.println("<h4>For <a href=\""+url+"Browse?advanceSearch=y\">Advance Search</a></h4>");				
	}
	out.println("<form name=\"myForm\" method=\"post\" action=\""+url+"ProgramsTable\" >");
	if(!plan_id.equals("")){ // plan id
	    // we need this for duplication not search
	    out.println("<input type=\"hidden\" name=\"dup_plan_id\" value=\""+plan_id+"\" />");	
	}
	if(!needDuplication.equals("")){
	    out.println("<input type=\"hidden\" name=\"needDuplication\" value=\""+needDuplication+"\" />");	
	}
	out.println("<input type=\"hidden\" name=\"minRecords\" value=\"0\" />");
	out.println("<table border=\"1\" width=\"90%\">");
	out.println("<caption>Search Options</caption>");
	//
	// sortby
        out.println("<tr><td align=\"right\">");
	out.println("<label for=\"sort_by\">Sort by: </label></td><td align=\"left\">");
	out.println("<select name=\"sortby\" id=\"sort_by\">");
	out.println("<option value=\"p.id\" selected=\"selected\" >ID</option>");
	out.println("<option value=\"l.name\">Lead</option>");
	out.println("<option value=\"p.title\">Title</option>");
	out.println("<option value=\"a.name\">Area</option>");
	out.println("<option value=\"c.name\">Guide Heading</option>");
	out.println("<option value=\"p.season\">Season</option>");
	out.println("<option value=\"p.year\">Year</option>");
	out.println("<option value=\"p.nraccount\">Non-revert Account</option>");
	out.println("</select></td</tr>");
	out.println("<tr><td align=\"right\"><label for=\"max_rec\">Show: "+
		    "</label></td><td align=\"left\">");

	out.println("<input type=\"text\" name=\"maxRecords\" value=\"100\" "+
		    "size=\"6\" id=\"max_rec\"/>records/page </td></tr>");
	out.println("<tr>");
	out.println("<td align=\"right\"><label for=\"prog_id\">ID: "+
		    "<label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"id\" value=\""+id+"\" size=\"6\" id=\"prog_id\"/> ");
	out.println("<label for=\"season\">Season: </label>");
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
	//
	// lead
	out.println("<tr><td align=\"right\">"+
		    "<label for=\"lead_id\">Lead: </label>");
	out.println("</td><td align=\"left\">");
	out.println("<select name=\"lead_id\" id=\"lead_id\"> ");
	out.println("<option value=\"\">All</option>");
	if(leads != null){
	    for(Lead one:leads){
		String selected = "";
		if(one.getId().equals(lead_id))
		    selected = "selected=\"selected\"";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\">"+
		    "<label for=\"cat_id\">Category: </label>");
	out.println("</td><td align=\"left\">");
	out.println("<select name=\"taxonomy_id\" onchange=\"createSubList(this,'sub_tax_id');\" id=\"cat_id\"\">");
	out.println("<option value=\"\">All</option>");
	if(taxonomies != null){
	    for(Type one:taxonomies){
		out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select>");
	out.println("<label for=\"sub_tax_id\">Sub Category:</label>");
	out.println("<select name=\"taxonomy_ids\" id=\"sub_tax_id\" >");
	out.println("<option value=\"\">All</option>");
	out.println("</select>");						
	out.println("</td></tr>");								
	//
	// Title 
	out.println("<tr><td align=\"right\"><label for=\"ptitle\">Program Title: ");
	out.println("</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"title\" maxlength=\"50\" value=\""+
		    title + "\" size=\"30\" id=\"ptitle\" />");
	out.println("</td></tr>");
	// location
	out.println("<tr><td align=\"right\"><label for=\"loc_id\">Location: </label>");
	out.println("</td><td align=\"left\">");
	out.println("<select name=\"location_id\" id=\"loc_id\">");
	out.println("<option value=\"\">All</option>");
	if(locations != null){
	    for(Type one:locations){
		String selected = "";
		if(one.getId().equals(location_id))
		    selected = "selected=\"selected\"";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}				
	out.println("</select>");
	out.println("</td></tr>");
	//
	// area, category
	if(needDuplication.equals("") && !advanceSearch.equals("")){
	    out.println("<tr><td align=\"right\"><label for=\"g_id\">Guide Heading: </label>");
	    out.println("</td><td><left>");
	    out.println("<select name=\"category_id\" id=\"g_id\">");
	    out.println("<option value=\"\">All</option>");
	    if(categories != null){
		for(Type one:categories){
		    String selected = "";
		    if(one.getId().equals(category_id)){
			selected = "selected=\"selected\"";
		    }
		    else if(!one.isActive()) continue; 
		    out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }
	    out.println("</select></td></tr>");
	    out.println("<tr><td align=\"right\">");
	    out.println("<label for=\"area_id\"> Area: </label></td><td align=\"left\">");
	    out.println("<select name=\"area_id\" id=\"area_id\">");
	    out.println("<option value=\"\">All</option>");
	    if(areas != null){
		for(Type one:areas){
		    String selected = "";
		    if(one.getId().equals(area_id))
			selected = "selected=\"selected\"";
		    out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }		
	    out.println("</select>");
	    out.println("</td></tr>");
	    //
	    // statement
	    out.println("<tr><td align=\"right\" valign=\"bottom\"><label for=\"broch_id\">Brochure Statement: ");
	    out.println("</td></label><td align=\"left\"><font color=\"green\" size=\"-1\">"+
			"Key word or a phrase<br /></font>");
	    out.println("<input type=\"text\" name=\"statement\" "+
			"size=\"50\" value=\""+statement+"\" id=\"broch_id\"/>");
	    out.println("</td></tr>");
	    //
	    // non-reverting account 
	    // 
	    out.println("<tr><td align=\"right\"><label for=\"nr_id\">Non-reverting Account: ");
	    out.println("</label></td><td align=\"left\">");
	    out.println("<input type=\"text\" name=\"nraccount\" maxlength=\"25\" value=\""+
			nraccount + "\" size=\"25\" id=\"nr_id\"/>");
	    out.println("<label>Transaction Fee ($): </label>");
	    out.println("<input type=\"text\" name=\"fee\" maxlength=\"5\" value=\""+
			fee + "\" size=\"5\" />");
	    out.println("</td></tr>");
	    //
	    // other info
	    out.println("<tr><td align=\"right\" valign=\"bottom\"><label for=\"other_id\">Other Guide Info: ");
	    out.println("</td></label><td align=\"left\"><font color=\"green\" size=\"-1\">"+
			"Key word or a phrase<br /></font>");
	    out.println("<input type=\"text\" name=\"oginfo\" "+
			"size=\"50\" value=\""+oginfo+"\" id=\"other_id\"/>");
	    out.println("</td></tr>");
	    // 
	    out.println("<tr><td align=\"right\"><label for=\"code\">Code: ");
	    out.println("</label></td><td align=\"left\">");
	    out.println("<input type=\"text\" name=\"code\" "+
			"size=\"10\"  />&nbsp;&nbsp;");
	    out.println(" <input type=\"checkbox\" name=\"codeNeed\" "+
			"value=\"y\" "+codeNeed+" id=\"code\"/>");
	    out.println("<label>Code Needed</label></td></tr>");
	    //
	    // common fields with session
	    //
	    out.println("<tr><td align=\"right\"><label for=\"inf_id\">In City Fee($): ");
	    out.println("</label></td><td align=\"left\">");
	    out.println("<input type=\"text\" name=\"inCityFee\" maxlength=\"10\" value=\""+
			inCityFee + "\" size=\"10\" id=\"inf_id\" />");
	    out.println("<label for=\"nif_id\">Non-City Fee ($): ");
	    out.println("</label>");
	    out.println("<input type=\"text\" name=\"nonCityFee\" maxlength=\"10\" value=\""+
			nonCityFee + "\" size=\"10\" id=\"nif_id\" />");
	    out.println("</td></tr>");
	    //
	    // Days, ages
	    out.println("<tr><td align=\"right\"><label for=\"part_from\">Participation Age, From: ");
	    out.println("</label></td><td align=\"left\">");
	    out.println("<input type=\"text\" name=\"ageFrom\" maxlength=\"3\" value=\""+
			ageFrom + "\" size=\"3\" id=\"part_from\"/>");
	    out.println("<label for=\"part_to\">To:</label>");
	    out.println("<input type=\"text\" name=\"ageTo\" maxlength=\"3\" value=\""+
			ageTo + "\" size=\"3\" id=\"part_to\"/> &nbsp;");
	    out.println("<input type=\"checkbox\" name=\"wParent\" value=\"y\" id=\"w_parent\"/>");
	    out.println("<label for=\"w_parent\">w/Parent, </label>");
	    out.println("<label for=\"days\"> Days: </label>");
	    out.println("<input type=\"text\" name=\"days\" maxlength=\"10\" value=\""+
			days + "\" size=\"8\" id=\"days\"/>");
	    out.println("</td></tr>");
	    //
	    //
	    // instructor
	    out.println("<tr><td align=\"right\"><label for=\"instruct\">Instructor: ");
	    out.println("</label></td><td align=\"left\">");
	    out.println("<input type=\"text\" name=\"instructor\" maxlength=\"50\" "+
			"value=\""+instructor + "\" size=\"50\" id=\"instruct\"/>");
	    out.println("</td></tr>");
	    //
	    // completed tasks
	    out.println("<tr><td align=\"right\" valign=\"top\"><label for=\"comp_tasks\">Completed Tasks: ");
	    out.println("</label></td><td align=\"left\"><table><tr><td>");
	    out.println("<input type=\"checkbox\" name=\"codeTask\" id=\"code_task\" value=\"y\" />");
	    out.println("<label for=\"code_task\">Code, </label>");
	    out.println("</td><td>");
	    out.println("<input type=\"checkbox\" name=\"marketTask\" value=\"y\" id=\"market_task\"/>");
	    out.println("<label for=\"market_task\">Marketing, </label>");
	    out.println("</td><td>");
	    out.println("<input type=\"checkbox\" name=\"volTask\" value=\"y\" id=\"vod_task\"/>");
	    out.println("<label for=\"vol_task\">Volunteer, </label>");
	    out.println("</td></tr><tr><td>");
	    out.println("<input type=\"checkbox\" name=\"sponTask\" value=\"y\"  id=\"spon_task\"/>");
	    out.println("<label for=\"spon_task\">Sponsorship </label>");
	    out.println("</td><td>");
	    out.println("<input type=\"checkbox\" name=\"budgetTask\" value=\"y\" id=\"budget_task\"/>");
	    out.println("<label for=\"budget_task\'>Budget Estimater </label>");
	    out.println("</td><td>");
	    out.println("<input type=\"checkbox\" name=\"evalTask\" value=\"y\"  id=\"eval_task\"/>");
	    out.println("<label for=\"eval_task\">Evaluation </label>");
	    out.println("</td></tr></table>");
	    out.println("</td></tr>");
	    //
	    out.println("<tr><td align=\"right\" valign=\"top\"><b>Uncompleted Tasks:");
	    out.println("</b></td><td align=\"left\"><table><tr><td>");
	    out.println("<input type=\"checkbox\" name=\"unCode\" value=\"y\" id=\"uncode\" />");
	    out.println("<label for=\"unclode\">Code, </label>");
	    out.println("</td><td>");
	    out.println("<input type=\"checkbox\" name=\"unMarket\" value=\"y\"  id=\"unmarket\" />");
	    out.println("<label for=\"unmarket\">Marketing, </label>");
	    out.println("</td><td>");
	    out.println("<input type=\"checkbox\" name=\"unVol\" value=\"y\" id=\"unvol\"/>");
	    out.println("<label id=\"unvol\">Volunteer, </label>");
	    out.println("</td></tr><tr><td>");
	    out.println("<input type=\"checkbox\" name=\"unSpon\" value=\"y\" id=\"unspon\"/>");
	    out.println("<label for=\"unspon\">Sponsorship </label>");
	    out.println("</td><td>");
	    out.println("<input type=\"checkbox\" name=\"unBudget\" value=\"y\"  id=\"unbudget\"/>");
	    out.println("<label for=\"unbudget\">Budget Estimater </label>");
	    out.println("</td><td>");
	    out.println("<input type=\"checkbox\" name=\"unEval\" value=\"y\" id=\"uneval\"/>");
	    out.println("<label for=\"uneval\">Evaluation </label>");
	    out.println("</td></tr>");
	    out.println("</table></td></tr>");
	    //
	    // Dates
	    // 
	    out.println("<tr><td align=\"right\"><label for=\"whichDate\">Select Date: </label></td><td align=\"left\">");
	    out.println("<input type=\"radio\" name=\"whichDate\" checked value=\"startDate\" />"+
			"Start Date ");
	    out.println("<input type=\"radio\" name=\"whichDate\" value=\"endDate\" />"+
			"End Date ");
	    out.println("<input type=\"radio\" name=\"whichDate\" value=\"regDeadLine\" />"+
			"Reg. Dead Line</td></tr> ");
						
	    out.println("<tr><td align=\"right\"><b>Date Range: </b>");
	    out.println("</td><td align=\"left\"><label for=\"dateAt\">At: </label>");
	    out.println("<input type=\"text\" name=\"dateAt\" maxlength=\"10\" "+
			"value=\""+dateAt + "\" size=\"10\" class=\"date\" id=\"dateAt\"/>");
	    out.println(", Or <label for=\"date_from\">from: </label>");
	    out.println("<input type=\"text\" name=\"dateFrom\" maxlength=\"10\" "+
			"value=\""+dateFrom + "\" size=\"10\" class=\"date\" id=\"date_from\"/>");
	    out.println("<label for=\"date_to\">To: </label> ");
	    out.println("<input type=\"text\" name=\"dateTo\" maxlength=\"10\" "+
			"value=\""+dateTo + "\" size=\"10\" class=\"date\" id=\"date_to\"/>");
	    out.println("<font color=\"green\" size=\"-1\">(mm/dd/yyyy)</font>");
	    out.println("</td></tr>");
	}
	out.println("</table></td></tr>");
	//
	out.println("<tr bgcolor=\"#CDC9A3\">");						
	out.println("<td align=\"right\">");
	out.println("<input type=\"submit\" value=\"Search\" "+
		    "></td></tr>");
	out.println("</table>");
	out.println("</form>");
	//
	// second form to show the output in the left panel as a list of 
	// links for the users to update the records
	// the fields are copy of the first form in hidden fields
	//
	if(needDuplication.equals("") && !advanceSearch.equals("")){
	    out.println("<form name=\"myForm2\" method=\"post\" action=\""+url+
			"ProgramResult\" "+
			"onsubmit=\"return validateForm2()\">");
	    out.println("<input type=\"hidden\" name=\"sortby\" value=\"\">");
	    if(!plan_id.equals(""))
		out.println("<input type=\"hidden\" name=\"plan_id\" value=\""+plan_id+"\" />");
	    out.println("<input type=\"hidden\" name=\"showType\" value=\"breif\" />");
	    out.println("<input type=\"hidden\" name=\"minRecords\" value=\"0\" />");
	    out.println("<input type=\"hidden\" name=\"maxRecords\" value=\"100\" />");
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	    out.println("<input type=\"hidden\" name=\"season\" value=\""+season+"\" />");
	    out.println("<input type=\"hidden\" name=\"year\" value=\""+year+"\" />");
	    out.println("<input type=\"hidden\" name=\"lead_id\" value=\""+lead_id+"\" />");
	    out.println("<input type=\"hidden\" name=\"title\" value=\""+title+"\" />");
	    out.println("<input type=\"hidden\" name=\"category_id\" value=\""+category_id+"\" />");
	    out.println("<input type=\"hidden\" name=\"area_id\" value=\""+area_id+"\" />");
	    out.println("<input type=\"hidden\" name=\"statement\" value=\""+
			statement+"\" />");
	    out.println("<input type=\"hidden\" name=\"nraccount\" value=\""+
			nraccount+"\" />");
	    out.println("<input type=\"hidden\" name=\"oginfo\" value=\""+oginfo+"\" />");
	    out.println("<input type=\"hidden\" name=\"code\" value=\""+code+"\" />");
	    out.println("<input type=\"hidden\" name=\"inCityFee\" value=\""+inCityFee+"\" />");
	    out.println("<input type=\"hidden\" name=\"nonCityFee\" value=\""+nonCityFee+"\" />");
	    out.println("<input type=\"hidden\" name=\"fee\" value=\""+fee+"\" />");
	    out.println("<input type=\"hidden\" name=\"ageFrom\" value=\""+ageFrom+"\" />");
	    out.println("<input type=\"hidden\" name=\"ageTo\" value=\""+ageTo+"\" />");
	    out.println("<input type=\"hidden\" name=\"wParent\" value=\"\" />");
	    out.println("<input type=\"hidden\" name=\"days\" value=\""+days+"\" />");
	    out.println("<input type=\"hidden\" name=\"dateAt\" value=\""+dateAt+"\" />");
	    out.println("<input type=\"hidden\" name=\"dateFrom\" value=\""+dateFrom+"\" />");
	    out.println("<input type=\"hidden\" name=\"dateTo\" value=\""+dateTo+"\" />");
	    out.println("<input type=\"hidden\" name=\"whichDate\" value=\""+whichDate+"\" />");
	    out.println("<input type=\"hidden\" name=\"location_id\" value=\""+
			location_id+"\" />");
	    out.println("<input type=\"hidden\" name=\"instructor\" value=\""+instructor+"\" />");
	    out.println("<input type=\"hidden\" name=\"codeTask\" value=\"\" />");
	    out.println("<input type=\"hidden\" name=\"marktTask\" value=\"\" />");
	    out.println("<input type=\"hidden\" name=\"volTask\" value=\"\" />");
	    out.println("<input type=\"hidden\" name=\"sponTask\" value=\"\" />");
	    out.println("<input type=\"hidden\" name=\"budgetTask\" value=\"\" />");
	    out.println("<input type=\"hidden\" name=\"evalTask\" value=\"\" />");
	    out.println("<input type=\"hidden\" name=\"unCode\" value=\"\" />");
	    out.println("<input type=\"hidden\" name=\"unMarket\" value=\"\" />");
	    out.println("<input type=\"hidden\" name=\"unVol\" value=\"\" />");
	    out.println("<input type=\"hidden\" name=\"unSpon\" value=\"\" />");
	    out.println("<input type=\"hidden\" name=\"unBudget\" value=\"\" />");
	    out.println("<input type=\"hidden\"  name=\"unEval\" value=\"\" />");
	    out.println("</form>");
	    Helper.writeWebFooter(out, url);
	}
	out.println("</center>");
	out.println("</body></html>");
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

	doGet(req, res);

    }

}






















































