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

@WebServlet(urlPatterns = {"/ProgramsTable","/ProgramResult"})
public class ProgramResult extends TopServlet{

    static Logger logger = LogManager.getLogger(ProgramResult.class);
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
    //
    // Mr Post
    /**
     * Create an html page for the program search engine form.
     * processes the request from the search engine page and presents 
     * the matching results as html in a table
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	String[] titles = {"ID", 
	    "Lead Programmer",
	    "Title", 
	    "Season, Year",            
	    "Year", 
	    "Guide Heading",      // category
	    "Area",                //7
	    "Brochure Statement", //8
	    "Non-reverting Account", 
	    "Transaction Fee $",
	    "Other Guide Info",
	    "Code",               //12
	    "Code Needed",
	    "Days",
	    "# of Classes",
	    "Start End Date",     // 16
	    // 
	    "Start End Time",
	    "In City Fee $", // common fields
	    "Non City Fee $",
	    "Other Fee $",
	    "Member Fee $",       // 21
	    "Non-Member Fee $",   // 22
	    "Participant Age",     // for all ages check
	    "Participant Age",     // 24
	    "Participant Grade",   // 25 
	    "Min-Max Enrollment",  
	    "Reg. Deadline",
	    "Location",
	    "Instructor",
	    "Description",         // 30
	    "Completed Task",      // 31
	    "Completed Task",
	    "Completed Task",
	    "Completed Task",
	    "Completed Task",
	    "Completed Task"};      // 36

	boolean show[] = { true, false, false, false, false,
	    false, false, false, false, false,
	    false, false, false, false, false,
	    false, false, false, false, false,
	    false, false, false, false, false,
	    false, false, false, false, false,
	    false, false, false, false, false,
	    false
	};

	PrintWriter os, out;
	String sortby = "";
	boolean showAll = true;  // for the programs
	String unicID = "", username="";
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	os = res.getWriter();
	int maxRecords = 100, minRecords = 0, incr=100;
	Enumeration values = req.getParameterNames();
	//obtainProperties(os);
	String name, value, wt="table";
	boolean rangeFlag = true, connectDbOk = false;
	String that=""; 
        String browsestr = "";
	String lead_id="", title="", season="", year=""; 
	String category_id="", statement="", days="", category2_id=""; // 2:old
	String nraccount="", fee="", oginfo="",area_id="";
	String inCityFee="", nonCityFee="";
	String location_id="", instructor="",regDeadLine="";
	String partAge="", partGrade="", description="";
	String ageFrom="", ageTo="",code="",wParent="";
	String minMaxEnroll="", description2="", id="", codeNeed="";
	String dateFrom="", dateTo="", dateAt="", whichDate=""; 
	String codeTask="", marketTask="", volTask="", sponTask="",
	    budgetTask="", evalTask="",showType="", dup_plan_id="";
	String unCode="", unMarket="", unVol="", unSpon="", taxanomy_term_id="",
	    unBudget="",unEval="", sessionSort="", message="";
	//
	// we use these for program duplication
	//
	String needDuplication = "", plan_id="";
	boolean showNext=false, success=true;
	os.println("<html>");

	ProgramList pl = new ProgramList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = Helper.escapeIt((req.getParameter(name)).trim());
	    if(value.equals("")) continue;
	    if (name.equals("id")){
		id = value;
		pl.setId(value);
	    }
	    else if (name.equals("plan_id")){
		plan_id = value;
		pl.setPlan_id(value);
	    }
	    else if (name.equals("dup_plan_id")){
		dup_plan_id = value;
	    }						
	    else if (name.equals("needDuplication")){
		needDuplication = value;
	    }						
	    else if (name.equals("codeTask")){
		codeTask = value;
		pl.setCodeTask(value);
	    }
	    else if (name.equals("marketTask")){
		marketTask = value;
		pl.setMarketTask(value);
	    }
	    else if (name.equals("volTask")){
		volTask = value;
		pl.setVolTask(value);
	    }
	    else if (name.equals("taxonomy_id")){
		pl.setTaxonomy_id(value);
	    }
	    else if (name.equals("taxonomy_ids")){
		pl.setTaxonomy_ids(value);
	    }						
	    else if (name.equals("budgetTask")){
		budgetTask = value;
		pl.setBudgetTask(value);
	    }
	    else if (name.equals("evalTask")){
		evalTask = value;
		pl.setEvalTask(value);
	    }
	    else if (name.equals("code")){
		code = value;
		pl.setCode(value);
	    }
	    else if (name.equals("pDays")){
		days = value;
		pl.setDays(value);
	    }
	    else if (name.equals("wParent")){
		wParent = value;
		pl.setWParent();
	    }
	    else if (name.equals("sponTask")){
		sponTask = value;
		pl.setSponTask(value);
	    }
	    else if (name.equals("showType")){
		showType = value;
	    }
	    else if (name.equals("dateFrom")){
		dateFrom = value;
		pl.setDateFrom(value);
	    }
	    else if (name.equals("dateTo")){
		dateTo = value;
		pl.setDateTo(value);
	    }
	    else if (name.equals("dateAt")){
		dateAt = value;
		pl.setDateAt(value);
	    }
	    else if (name.equals("whichDate")){
		whichDate = value;
		pl.setWhichDate(value);
	    }
	    else if (name.equals("maxRecords")){
		try{
		    maxRecords = Integer.parseInt(value);
		}catch(Exception ex){};
	    }
	    else if (name.equals("minRecords")){
		try{
		    minRecords = Integer.parseInt(value);
		}catch(Exception ex){};
	    }
	    else if (name.equals("lead_id")){
		lead_id = value;
		pl.setLead_id(value);
	    }
	    else if (name.equals("ageFrom")){
		ageFrom = value;
		pl.setAgeFrom(value);
	    }
	    else if (name.equals("ageTo")){
		ageTo = value;
		pl.setAgeTo(value);
	    }
	    else if (name.equals("title")){
		title = value;
		pl.setTitle(value);
	    }
	    else if (name.equals("year")){
		year = value ;
		pl.setYear(value);
	    }
	    else if (name.equals("season")){
		season = value;
		pl.setSeason(value);
	    }
	    else if (name.equals("category_id")){
		category_id = value;
		pl.setCategory_id(value);
	    }
	    else if (name.equals("category2_id")){
		category2_id = value;
	    }
	    else if (name.equals("statement")){
		statement = value;
		pl.setStatement(value);
	    }
	    else if (name.equals("oginfo")){
		oginfo = value;
		pl.setOginfo(value);
	    }
	    else if (name.equals("fee")){
		fee = value;
		pl.setFee(value);
	    }
	    else if (name.equals("sortby")){
		sortby = value;
		pl.setSortby(value);
	    }
	    else if(name.equals("inCityFee")){
		inCityFee = value;
		pl.setInCityFee(value);	
	    }
	    else if(name.equals("nonCityFee")){
		nonCityFee = value;
		pl.setNonCityFee(value);
	    }
	    else if(name.equals("location_id")){
		location_id = value;
		pl.setLocation_id(value);
	    }
	    else if(name.equals("instructor")){
		instructor = value;
		pl.setInstructor(value);
	    }
	    else if(name.equals("codeNeed")){
		if(value != null){
		    codeNeed =  "y";
		    pl.setCodeNeed("y");
		}
	    }
	    else if(name.equals("area_id")){
		area_id =  value;
		pl.setArea_id(value);
	    }
	    else if(name.equals("unCode")){
		unCode = value;
		pl.setUnCode(value);
	    }
	    else if(name.equals("unMarket")){
		unMarket = value;
		pl.setUnMarket(value);
	    }
	    else if(name.equals("unVol")){
		unVol = value;
		pl.setUnVol(value);
	    }
	    else if(name.equals("unSpon")){
		unSpon = value;
		pl.setUnSpon(value);
	    }
	    else if(name.equals("nraccount")){
		nraccount = value;
		pl.setNraccount(value);
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
	incr = maxRecords - minRecords;
	if(!showType.equals("")){
	    showAll = false;
	}
	if(minRecords > maxRecords){ //swap
	    int temp = minRecords;
	    minRecords = maxRecords;
	    maxRecords = temp;
	}
	String back = pl.find();
	int total = 0, total2=1;
	total = pl.size();
	if(total == 1){
	    String str = url+"Program.do?action=zoom&id="+pl.get(0).getId();
	    if(!needDuplication.equals("")){
		str = url+"Duplicate.do?id="+pl.get(0).getId();								
		str += "&plan_id="+dup_plan_id;
	    }
	    res.sendRedirect(str);
	    return;
	}
	if(!back.equals("")){
	    message += back;
	}

	os.println("<head><title>Programs " + 
		   "</title>");
	Helper.writeWebCss(os, url);
	os.println("</head><body>");
	Helper.writeTopMenu(os, url);
	if(!message.equals("")){
	    os.println("<h4>Error: "+message+"</h4>");
	}
	os.println("<center>");
	os.println("<form name=\"anyform\">");
	os.println("<h2>Programs </h2>");
	//
	os.println("Matching total records :"+total+"<br>");
	if(total < maxRecords && minRecords == 0){ 
	    os.println("Showing all the: "+ total + 
		       " records <br />");
	}
	else if(total <= maxRecords && total > minRecords)
	    os.println("Showing the records from:"+ minRecords +
		       " to " + total+ "<br />");
	else if(total > maxRecords && total > minRecords){
	    os.println("Showing the records from:"+ minRecords +
		       " to " + maxRecords+ "<br />");
	    showNext = true;
	    incr = maxRecords - minRecords;
	}
	else if(total < minRecords){
	    os.println("Error in setting the \"From\" field in "+
		       "\"Show Records\", go back and "+
		       "reset this field. <br />");
	    rangeFlag = false;
	}
	if(rangeFlag){
	    os.println("<hr /></center>");
	    String id2 = "";
	    for(int row =0; row < total; row++){
		if(row >= minRecords && row <= maxRecords){
		    Program pr = pl.get(row);
		    os.println("<table width=\"90%\">");
		    os.println("<caption>"+pr.getTitle()+"</caption>");
		    os.println("</td></tr>");
		    os.println("<tr><td align=\"right\" width=\"20%\"><strong>ID:");
		    os.println("</strong></right></td><td align=\"left\">");
		    if(needDuplication.equals("")){
			os.println("<a href=\""+url+"Program.do?id="+pr.getId()+
				   "&action=zoom\">"+
				   pr.getId()+"</a>");
		    }
		    else{
			os.println("<a href=\""+url+"Duplicate.do?id="+pr.getId()+"&plan_id="+dup_plan_id+"\">Duplicate This Program </a>");
		    }
		    os.println("</td></tr>");
		    if(showAll || show[1]){
			that = "";
			Type tlead = pr.getLead();
			if(tlead != null)
			    that = tlead.getName();
			if(that != null){
			    that = that.trim();
			    writeItem(that, titles[1], os);
			}
		    }
		    if(showAll || show[3]){
			that = pr.getSeason();
			if(that != null){
			    that += " "+pr.getYear();
			    that = that.trim();
			    writeItem(that, titles[3], os);
			}
		    }
		    if(showAll || show[5]){
			that = "";
			Type cat = pr.getCategory();
			if(cat != null)
			    that = cat.getName();
			if(that != null){
			    that = that.trim();
			    writeItem(that, titles[5], os);
			}
			that = pr.getTaxonomyInfo();
			if(that != null && !that.equals("")) {  // 2
			    writeItem(that, "Category", os);
			}
		    }
		    if(showAll || show[6]){
			that = "";						
			Type tarea = pr.getArea();
			if(tarea != null)
			    that = tarea.getName();
			if(that != null){
			    that = that.trim();
			    writeItem(that, titles[6], os);
			}
		    }
		    if(showAll || show[7]){
			that = pr.getSummary();
			if(that != null){
			    that = that.trim();
			    writeItem(that, "Summary", os);
			}																								
			that = pr.getStatement();
			if(that != null){
			    that = that.trim();
			    writeItem(that, titles[7], os);
			}
		    }
		    if(showAll || show[8]){
			that = pr.getNraccount();
			if(that != null){
			    that = that.trim();
			    writeItem(that, titles[8], os);
			}
		    }
		    if(showAll || show[9]){
			that = pr.getFee();
			if(that != null){
			    that = that.trim();
			    writeItem(that, titles[9], os);
			}
		    }
		    if(showAll || show[10]){
			that = pr.getOginfo();
			if(that != null){
			    that = that.trim();
			    writeItem(that, titles[10], os);
			}
		    }
		    if(showAll || show[11]){
			that = pr.getCode();// show code even when empty
			if(that == null) that = "&nbsp;"; 
			writeItem(that, titles[11], os);
		    }
		    if(showAll || show[12]){
			that = pr.getCodeNeed();
			if(that != null && !that.equals("")){
			    that = "Yes";
			    writeItem(that, titles[12], os);
			}
		    }
		    if(showAll || show[13]){
			that = pr.getDays();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[13], os);
			}
		    }
		    if(showAll || show[14]){
			that = pr.getClassCount();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[14], os);
			}
		    }
		    if(showAll || show[15]){
			that = pr.getStartEndDate();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[15], os);
			}
		    }
		    if(showAll || show[16]){
			that = pr.getStartEndTime();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[16], os);
			}
		    }
		    if(showAll || show[17]){
			that = pr.getInCityFee();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[17], os);
			}
		    }
		    if(showAll || show[18]){
			that = pr.getNonCityFee();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[18], os);
			}
		    }
		    if(showAll || show[19]){
			that = pr.getOtherFee();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[19], os);
			}
		    }
		    if(showAll || show[20]){
			that = pr.getMemberFee();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[20], os);
			}
		    }
		    if(showAll || show[21]){
			that = pr.getNonMemberFee();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[21], os);
			}
		    }	
		    if(showAll || show[22]){ // 21 age also
			that = pr.getAgeInfo();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[22], os);
			}
		    }
		    if(showAll || show[23]){
			that = pr.getPartGrade();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[23], os);
			}
		    }
		    if(showAll || show[25]){
			that = pr.getMinMaxEnroll();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[25], os);
			}
		    }
		    if(showAll || show[26]){
			that = pr.getRegDeadLine();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[26], os);
			}
		    }
		    if(showAll || show[27]){
			that = "";
			Location loc = pr.getLocation();
			if(loc != null)
			    that =  loc.getName();
			String str = pr.getLocation_details().trim();
			if(!str.equals("")){
			    that += " ("+str+")";
			}
			writeItem(that, titles[27], os);
		    }
		    if(showAll || show[28]){
			that = pr.getInstructor();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[28], os);
			}
		    }
		    if(showAll || show[29]){
			that = pr.getDescription();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[29], os);
			}
		    }
		    if(showAll || show[30]){
			that = pr.getTaskInfo();
			if(that != null && !that.equals("")){
			    writeItem(that, titles[30], os);
			}
		    }
		    os.println("</table>");
		    if(showAll){
			SessionList list = pr.getSessions();
			if(list != null && list.size() > 0){
			    SessionOpt sopt = pr.getSessionOpt();

			    Helper.writeSessions(os, list, sopt, pr.getId(), url, debug);
			}
		    }
		    if(row > maxRecords) break;
		    os.println("<br /><br /><hr />");
		}
	    }
	}    
	//
	os.println("</form>");
	if(showNext){
	    os.println("<br />");
	    os.println("<form method=\"post\">");
	    os.println("<input type=\"hidden\" name=\"sortby\" "+
		       "value=\"" + sortby + "\" />");
	    if(!showType.equals("")){
		os.println("<input type=\"hidden\" name=\"showType\" "+
			   "value=\"" + showType + "\" />");
	    }
	    os.println("<input type=\"hidden\" name=\"minRecords\" "+
		       "value=" + (minRecords+incr) + "></input>");
	    os.println("<input type=\"hidden\" name=\"maxRecords\" "+
		       "value=" + (maxRecords+incr) + "></input>");
	    if(!id.equals("")){
		os.println("<input type=\"hidden\" name=\"id\" "+
			   "value=" + id +"></input>");
	    }
	    if(!codeTask.equals("")){
		os.println("<input type=\"hidden\" name=\"codeTask\" "+
			   "value=\""+ codeTask +"\"></input>");
	    }
	    if(!marketTask.equals("")){
		os.println("<input type=\"hidden\" name=\"marketTask\" "+
			   "value=\""+ marketTask +"\"></input>");
	    }
	    if(!volTask.equals("")){
		os.println("<input type=\"hidden\" name=\"volTask\" "+
			   "value=\""+ volTask +"\"></input>");
	    }
	    if(!budgetTask.equals("")){
		os.println("<input type=\"hidden\" name=\"budgetTask\" "+
			   "value=\""+ budgetTask +"\"></input>");
	    }
	    if(!evalTask.equals("")){
		os.println("<input type=\"hidden\" name=\"evalTask\" "+
			   "value=\""+ evalTask +"\"></input>");
	    }
	    if(!code.equals("")){
		os.println("<input type=\"hidden\" name=\"pCode\" "+
			   "value=\""+ code +"\"></input>");
	    }
	    if(!sponTask.equals("")){
		os.println("<input type=\"hidden\" name=\"sponTask\" "+
			   "value=\""+ sponTask +"\"></input>");
	    }
	    if(!dateFrom.equals("")){
		os.println("<input type=\"hidden\" name=\"dateFrom\" "+
			   "value=\""+ dateFrom +"\"></input>");
	    }
	    if(!dateTo.equals("")){
		os.println("<input type=\"hidden\" name=\"dateTo\" "+
			   "value=\""+ dateTo +"\"></input>");
	    }
	    if(!dateAt.equals("")){
		os.println("<input type=\"hidden\" name=\"dateAt\" "+
			   "value=\""+ dateAt +"\"></input>");
	    }
	    if(!whichDate.equals("")){
		os.println("<input type=\"hidden\" name=\"whichDate\" "+
			   "value=\""+ whichDate +"\"></input>");
	    }
	    if(!days.equals("")){
		os.println("<input type=\"hidden\" name=\"days\" "+
			   "value=\"" + days +"\"></input>");
	    }
	    if(!lead_id.equals("")){
		os.println("<input type=\"hidden\" name=\"lead_id\" "+
			   "value=\"" + lead_id +"\"></input>");
	    }
	    if(!ageFrom.equals("")){
		os.println("<input type=\"hidden\" name=\"ageFrom\" "+
			   "value=\"" + ageFrom +"\"></input>");
	    }
	    if(!ageTo.equals("")){
		os.println("<input type=\"hidden\" name=\"pAgeTo\" "+
			   "value=\"" + ageTo +"\"></input>");
	    }
	    if(!title.equals("")){
		os.println("<input type=\"hidden\" name=\"ptitle\" "+
			   "value=\"" + title +"\"></input>");
	    }
	    if(!year.equals("")){
		os.println("<input type=\"hidden\" name=\"pyear\" "+
			   "value=\"" + year +"\"></input>");
	    }
	    if(!season.equals("")){
		os.println("<input type=\"hidden\" name=\"season\" "+
			   "value=\"" + season +"\"></input>");
	    }
	    if(!category_id.equals("")){
		os.println("<input type=\"hidden\" name=\"category_id\" "+
			   "value=\"" + category_id +"\"></input>");
	    }
	    if(!statement.equals("")){
		os.println("<input type=\"hidden\" name=\"statement\" "+
			   "value=\"" + statement +"\"></input>");
	    }
	    if(!oginfo.equals("")){
		os.println("<input type=\"hidden\" name=\"oginfo\" "+
			   "value=\"" + oginfo +"\"></input>");
	    }
	    if(!fee.equals("")){
		os.println("<input type=\"hidden\" name=\"fee\" "+
			   "value=\"" + fee +"\"></input>");
	    }
	    if(!inCityFee.equals("")){
		os.println("<input type=\"hidden\" name=\"inCityFee\" "+
			   "value=\"" + inCityFee +"\"></input>");
	    }
	    if(!nonCityFee.equals("")){
		os.println("<input type=\"hidden\" name=\"nonCityFee\" "+
			   "value=\"" + nonCityFee +"\"></input>");
	    }

	    if(!regDeadLine.equals("")){
		os.println("<input type=\"hidden\" name=\"regDeadLine\" "+
			   "value=\"" + regDeadLine +"\"></input>");
	    }
	    if(!location_id.equals("")){
		os.println("<input type=\"hidden\" name=\"location_id\" "+
			   "value=\"" + location_id +"\"></input>");
	    }
	    if(!instructor.equals("")){
		os.println("<input type=\"hidden\" name=\"instructor\" "+
			   "value=\"" + instructor +"\"></input>");
	    }
	    if(!codeNeed.equals("")){
		os.println("<input type=\"hidden\" name=\"codeNeed\" "+
			   "value=\"" + codeNeed +"\"></input>");
	    }
	    if(!area_id.equals("")){
		os.println("<input type=\"hidden\" name=\"area_id\" "+
			   "value=\"" + area_id +"\"></input>");
	    }
	    if(!unCode.equals("")){
		os.println("<input type=\"hidden\" name=\"unCode\" "+
			   "value=\"" + unCode +"\"></input>");
	    }
	    if(!unMarket.equals("")){
		os.println("<input type=\"hidden\" name=\"unMarket\" "+
			   "value=\"" + unMarket +"\"></input>");
	    }
	    if(!unVol.equals("")){
		os.println("<input type=\"hidden\" name=\"unVol\" "+
			   "value=\"" + unVol +"\"></input>");
	    }
	    if(!unSpon.equals("")){
		os.println("<input type=\"hidden\" name=\"unSpon\" "+
			   "value=\"" + unSpon +"\"></input>");
	    }
	    if(!nraccount.equals("")){
		os.println("<input type=\"hidden\" name=\"nraccount\" "+
			   "value=\"" + nraccount +"\"></input>");
	    }
	    os.println("<center><table ><tr><td>"); 
	    os.println("<input type=submit value=\"Next Page\"" + 
		       "></input>");
	    os.println("</td></tr></table></center>"); 
	    os.println("</form>");
	}
	os.println("</body>");
	os.println("</html>");
	os.flush();
	os.close();

    }
    /**
     * Writes a pair of title item in table cells.
     *
     *@param that the item details
     *@param title the item title
     */
    void writeItem(String that, String title, PrintWriter os){
	if(!that.equals("")){
	    os.println("<tr><td align=\"right\" valign=\"top\"><b>");
	    os.println(title+":&nbsp;</b></td><td valign=\"top\">");
	    os.println(that+"</td></tr>");
	}
    }

}






















































