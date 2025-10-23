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
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/Program.do","/Program"})
public class ProgramServ extends TopServlet{

    static Logger logger = LogManager.getLogger(ProgramServ.class);

    final static String staffSelectArr[] = { "",  // 0 
	"Leader",          // 1
	"Supervisor",      // 2
	"Instructor",      // 3
	"Staff Assistent", // 4 
	"Volunteer"};      // 5
    /**
     * The main class method.
     *
     * Create an html page for the program form.
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
     * Create an html page for the program form.
     * It also handles all the operations related to adding/updaing/delete
     * or just viewing a record.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	LeadList leads = null;
	TypeList areas = null;
	TypeList locations = null;
	TypeList categories = null;
	List<Type> taxonomies = null;
	String id = "";
	Enumeration values = req.getParameterNames();
	String name, value;
	String ptitle="", season="", year=""; 
	String statement="", subcat="";
	String othercat="",otherCategory="",otherArea="";
	String stat2="",stat3="",stat4="",stat5="",oginfo2="";
	String nraccount="", fee="", oginfo="", scount="0";
	String action = "", otherLead="", codeNeed="";
	String plan_id="";
	boolean setAction = false, marketFound=false; 
	String lead2_id="", title2="", received=""; // date
	String startDate="", endDate="", message="";
	String marketTask="", volTask="", sponTask="",codeTask="",
	    budgetTask="", evalTask="",planTitle="", sessionSort="";
	String ageFrom_c="",ageTo_c="",dateStart_c="",dateEnd_c="",
	    wParent_c="", endDate_c="",
	    otherAge_c="", noPublish="";
	//
	// fields common in program and session
	// if used in program, should not be used in session
	//
	String inCityFee="", nonCityFee="",otherFee="",
	    memberFee="", nonMemberFee="";
	String location_id="", instructor="",regDeadLine="",otherLocation="";
	String ageFrom="", ageTo="",partGrade="", description="";
	String minMaxEnroll="",description2="",
	    allAge="", 
	    otherAge=""; // will be used for 18 month toddlers and so on
	String days="",startTime="", endTime="";
	String classCount="",code="",wParent="",waitList="0";
	//
	// Newly added vars
	String d_sun="",d_mon="",d_tue="",d_wed="",d_thu="",d_fri="",
	    d_sat="",d_all="",d_mon_fri="";
	String startDate_c="", memberFee_c="", nonMemberFee_c="";
	//
	// Since there have been many changes since the first writing
	// of this code, a need backward compatible was needed, 
	// therefore the version variable was introduced to distinguish
	// between different code release 
	// 
	String version ="";
	boolean show[] = { true, 
	    true, false, false, false, false,
	    false, false, false, false, false,
	    false, false, false, false, false};
	//
	// session related vars
	//
	String days_c="", startTime_c="", 
	    inCityFee_c="";
	String otherFee_c=""; 
	String regDeadLine_c="", nonCityFee_c="", 
	    location_c="";
	String classCount_c="", instructor_c="", 
	    partGrade_c="";
	String allAge_c="", 
	    minMaxEnroll_c="", 
	    description_c="";
	String 
	    action2="", prevAction="";
	boolean success = true;
	HttpSession session = null;
	PlanList allPlans = null;
	Plan plan = null;
	//
	session = req.getSession(false);
	User user = null;
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	control = new Control(debug, user);
	Program pr = new Program(debug);
	SessionOpt sopt = new SessionOpt(debug);
	SessionList sessions = null;
	String [] vals;
	while (values.hasMoreElements()){

	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if(name.equals("id")){
		id = value;
		pr.setId(value);
		sopt.setId(value);
		if(control != null)
		    control.setId(id);
	    }
	    else if(name.equals("plan_id")){
		plan_id = value;
		pr.setPlan_id(value);
	    }
	    else if(name.equals("startDate")){
		startDate = value;
		pr.setStartDate(value);
	    }
						
	    else if(name.equals("taxonomy_ids")){
		pr.setTaxonomy_ids(value);
	    }
	    else if(name.equals("summary")){
		pr.setSummary(value);
	    }
	    else if(name.equals("startDate_c")){
		sopt.setStartDate_c(value);
	    }
	    else if(name.equals("endDate")){
		endDate = value;
		pr.setEndDate(value);
	    }
	    else if(name.equals("endDate_c")){
		sopt.setEndDate_c(value);
	    }
	    else if(name.equals("marketTask")){
		marketTask = value;
		pr.setMarketTask(value);
	    }
	    else if(name.equals("codeTask")){
		codeTask = value;
		pr.setCodeTask(value);
	    }
	    else if(name.equals("sponTask")){
		sponTask = value;
		pr.setSponTask(value);
	    }
	    else if(name.equals("volTask")){
		volTask = value;
		pr.setVolTask(value);
	    }
	    else if(name.equals("budgetTask")){
		budgetTask = value;
		pr.setBudgetTask(value);
	    }
	    else if(name.equals("evalTask")){
		evalTask = value;
		pr.setEvalTask(value);
	    }
	    else if(name.equals("lead_id")){
		pr.setLead_id(value);
	    }
	    else if(name.equals("title")){
		pr.setTitle(value);
	    }
	    else if(name.equals("season")){
		pr.setSeason(value);
	    }
	    else if(name.equals("season2")){
		pr.setSeason2(value);
	    }			
	    else if(name.equals("year")){
		pr.setYear(value);
	    }
	    else if(name.equals("category_id")){
		pr.setCategory_id(value);
	    }
	    else if(name.equals("category2_id")){
		pr.setCategory2_id(value);
	    }
	    else if(name.equals("statement")){
		pr.setStatement(value);
	    }
	    else if(name.equals("nraccount")){
		pr.setNraccount(value);
	    }
	    else if(name.equals("fee")){
		pr.setFee(value);
	    }
	    else if(name.equals("oginfo")){
		pr.setOginfo(value);
	    }
	    else if(name.equals("subcat")){
		pr.setSubcat(value);
	    }
	    else if(name.equals("location_details")){ 
		pr.setLocation_details(value);
	    }
	    else if(name.equals("otherLead")){
		otherLead = value;
	    }
	    else if(name.equals("otherCategory")){
		otherCategory = value;
	    }
	    else if(name.equals("otherArea")){
		otherArea = value;
	    }			
	    else if(name.equals("code")){
		pr.setCode(value);
	    }
	    else if(name.equals("ageFrom_c")){
		sopt.setAgeFrom_c(value);
	    }
	    else if(name.equals("memberFee_c")){
		sopt.setMemberFee_c(value);
	    }
	    else if(name.equals("nonMemberFee_c")){
		sopt.setNonMemberFee_c(value);
	    }						
	    else if(name.equals("codeNeed")){
		pr.setCodeNeed(value);
	    }
	    else if(name.equals("inCityFee")){
		pr.setInCityFee(value);
	    }
	    else if(name.equals("nonCityFee")){
		pr.setNonCityFee(value);
	    }
	    else if(name.equals("otherFee")){
		pr.setOtherFee(value);
	    }
	    else if(name.equals("memberFee")){
		pr.setMemberFee(value);
	    }
	    else if(name.equals("nonMemberFee")){
		pr.setNonMemberFee(value);
	    }			
	    else if(name.equals("allAge")){ // from pPartAge
		pr.setAllAge(value);
	    }
	    else if(name.equals("wParent")){ // with parent flag
		pr.setWParent(value);
	    }
	    else if(name.equals("otherAge")){
		pr.setOtherAge(value);
	    }
	    else if(name.equals("partGrade")){
		pr.setPartGrade(value);
	    }
	    else if(name.equals("minMaxEnroll")){
		pr.setMinMaxEnroll(value);
	    }
	    else if(name.equals("waitList")){
		if(!value.equals(""))
		    pr.setWaitList(value);
	    }
	    else if(name.equals("description")){
		pr.setDescription(value);
	    }
	    else if(name.equals("regDeadLine")){
		pr.setRegDeadLine(value);
	    }
	    else if(name.equals("location_id")){
		pr.setLocation_id(value);
	    }
	    else if(name.equals("instructor")){
		pr.setInstructor(value);
	    }
	    else if(name.equals("noPublish")){
		pr.setNoPublish(value);
	    }						
	    else if(name.equals("days")){
		days = value;
		pr.setDays(value);
	    }
	    else if(name.equals("version")){
		version = value;
		pr.setVersion(value);
	    }
	    else if(name.equals("received")){
		pr.setReceived(value);
	    }
	    else if(name.equals("startTime")){
		pr.setStartTime(value);
	    }
	    else if(name.equals("endTime")){
		pr.setEndTime(value);
	    }						
	    else if(name.equals("classCount")){
		pr.setClassCount(value);
	    }
	    else if(name.equals("area_id")){
		pr.setArea_id(value);
	    }
	    else if(name.equals("days_c")){
		sopt.setDays_c(value);
	    }
	    else if(name.equals("d_sun")){
		d_sun = value;
	    }
	    else if(name.equals("d_mon")){
		d_mon = value;
	    }
	    else if(name.equals("d_tue")){
		d_tue = value;
	    }
	    else if(name.equals("d_wed")){
		d_wed = value;
	    }
	    else if(name.equals("d_thu")){
		d_thu = value;
	    }
	    else if(name.equals("d_fri")){
		d_fri = value;
	    }
	    else if(name.equals("d_sat")){
		d_sat = value;
	    }
	    else if(name.equals("d_mon_fri")){
		d_mon_fri = value;
	    }
	    else if(name.equals("d_all")){
		d_all = value;
	    }
	    else if(name.equals("otherArea")){
		otherArea = value;
	    }
	    else if(name.equals("inCityFee_c")){
		sopt.setInCityFee_c(value);
	    }
	    else if(name.equals("nonCityFee_c")){
		sopt.setNonCityFee_c(value);
	    }
	    else if(name.equals("otherFee_c")){
		sopt.setOtherFee_c(value);
	    }
	    else if(name.equals("allAge_c")){ 
		sopt.setAllAge_c(value);
	    }
	    else if(name.equals("partGrade_c")){
		sopt.setPartGrade_c(value);
	    }
	    else if(name.equals("minMaxEnroll_c")){
		sopt.setMinMaxEnroll_c(value);
	    }
	    else if(name.equals("description_c")){
		sopt.setDescription_c(value);
	    }
	    else if(name.equals("startTime_c")){
		sopt.setStartTime_c(value);
	    }
	    else if(name.equals("regDeadLine_c")){
		sopt.setRegDeadLine_c(value);
	    }
	    else if(name.equals("location_c")){
		sopt.setLocation_c(value);
	    }
	    else if(name.equals("classCount_c")){
		sopt.setClassCount_c(value);
	    }
	    else if(name.equals("instructor_c")){
		sopt.setInstructor_c(value);
	    }
	    else if(name.equals("ageFrom")){
		pr.setAgeFrom(value);
	    }
	    else if(name.equals("sessionSort")){
		sopt.setSessionSort(value);
		sessionSort = value;
	    }
	    else if(name.equals("ageTo")){
		pr.setAgeTo(value);
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	    else if(name.equals("prevAction")){
		prevAction = value;
	    }
	    else if(name.equals("action2")){
		action2 = value;
	    }
	}
	if(user == null){
	    String str = url+"Login?source=Program.do&action=zoom&id="+id;
	    res.sendRedirect(str);
	    return;
	}
	if(action.equals(prevAction) && action2.equals("Refresh")){
	    action = action2;
	}
	if(action.equals(prevAction) && action2.equals("Delete")){
	    action = action2;
	}
	else if(action.equals("") && action2.equals("Delete")){
	    action = action2;
	}
	// new record
	if(!otherLead.equals("")){
	    Lead leadd = new Lead(debug, null, otherLead);
	    String back = leadd.doSave();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		pr.setLead_id(leadd.getId());
	    }
	}
	if(!otherCategory.equals("")){
	    Type cat = new Type(debug, null, otherCategory,"categories");
	    String back = cat.doSave();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		pr.setCategory_id(cat.getId());
	    }
	}
	if(!otherArea.equals("")){
	    Type ar = new Type(debug, null, otherArea,"areas");
	    String back = ar.doSave();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		pr.setArea_id(ar.getId());
	    }
	}		
	// 
	if(!startDate.equals("")){
	    if(startDate.length() < 10){
		startDate += "/"+pr.getYear();
		pr.setStartDate(startDate);
	    }
	}
	if(!endDate.equals("")){
	    if(endDate.length() < 10){
		endDate += "/"+pr.getYear();
		pr.setEndDate(endDate);
	    }
	}
	if(!regDeadLine.equals("")){
	    if(regDeadLine.length() < 10){
		regDeadLine += "/"+pr.getYear();
		pr.setRegDeadLine(regDeadLine);
	    }
	}
	//
	// Look the list of days for this program, this feature was
	// added to simplify the selection of days for the user 
	// by replacing the text box with checkboxes without doing
	// any change to the database
	//
	if(!d_all.equals("")){
	    days="M - Su";
	    pr.setDays(days);
	}
	else if(!d_mon_fri.equals("")){
	    days="Mon. - Fri.";
	    days="M - F";
	    pr.setDays(days);
	}
	else{
	    boolean in = false;
	    if(!d_sun.equals("")){
		days = "Su";
		in = true;
	    }
	    if(!d_mon.equals("")){
		if(in) days += " ";
		days += "M";				
		in = true;
	    }
	    if(!d_tue.equals("")){
		if(in) days += " ";
		days += "Tu";				
		in = true;
	    }
	    if(!d_wed.equals("")){
		if(in) days += " ";
		days += "W";				
		in = true;
	    }
	    if(!d_thu.equals("")){
		if(in) days += " ";
		days += "Th";				
		in = true;
	    }
	    if(!d_fri.equals("")){
		if(in) days += " ";
		days += "F";				
		in = true;
	    }
	    if(!d_sat.equals("")){
		if(in) days += " ";
		days += "Sa";				
	    }
	    pr.setDays(days);
	}
	if(action.equals("Save")){
	    String qq = "";
	    received = Helper.getToday();
	    try{
		//
		// all new record will get the current version
		//
		version = cur_ver;
		pr.setVersion(version);
		//
		String back = pr.doSave();
		if(!back.equals("")){
		    message += back;
		    logger.error(back);
		    success = false;
		}
		else{					
		    id = pr.getId();
		    sopt.setId(id);
		    back = sopt.doSave();
		    if(!back.equals("")){
			message += back;
			logger.error(back);
			success = false;
		    }
		    else{									
			message = "Saved Successfully";
		    }
		    CalHandle ch = new CalHandle(debug, id, null);
		    back = ch.process();
		    if(!back.equals("")){
			message += back;
			success = false;
			logger.error(back);
		    }
		    History one = new History(debug, id, "Created","Program",user.getId());
		    one.doSave();
		    //
		    // adding ownership info
		    //
		    if(control != null){
			control.setId(id);
			back = control.doSaveAsOwner();
			if(!back.equals("")){
			    message += back;
			    logger.error(back);
			    success = false;
			}
		    }
		}
	    }
	    catch(Exception ex){
		logger.error(ex+":"+qq);
		message = "Error in saving the recored "+ex;
		success = false;
	    }
	}
	else if(action.equals("Duplicate")){ // for testing pupose only
	    String qq = "";
	    received = Helper.getToday();
	    try{
		//
		// all new record will get the current version
		//
		version = cur_ver;
		pr.setVersion(version);
		//
		String back = pr.doDuplicate();
		if(!back.equals("")){
		    message += back;
		    logger.error(back);
		    success = false;
		}
		else{					
		    id = pr.getId();
		    CalHandle ch = new CalHandle(debug, id, null);
		    back = ch.process();
		    if(!back.equals("")){
			message += back;
			success = false;
			logger.error(back);
		    }
		    History one = new History(debug, id, "Created","Program",user.getId());
		    one.doSave();
		    //
		    // adding ownership info
		    //
		    if(control != null){
			control.setId(id);
			back = control.doSaveAsOwner();
			if(!back.equals("")){
			    message += back;
			    logger.error(back);
			    success = false;
			}
		    }
		}
		sopt = pr.getSessionOpt();
	    }
	    catch(Exception ex){
		logger.error(ex+":"+qq);
		message = "Error in duplicating "+ex;
		success = false;
	    }
	}				
	else if (action.equals("Update")){
	    //
	    String back = "";
	    if(user.canEdit()){
		back = pr.doUpdate();
		if(!back.equals("")){
		    message += back;
		    logger.error(back);
		    success = false;
		}
		else{
		    if(sopt.recordExists()){
			back = sopt.doUpdate();
		    }
		    else{
			back = sopt.doSave();
		    }
		    if(!back.equals("")){
			message += back;
			logger.error(back);
			success = false;
		    }
		    else{
			message += " Updated successfully";
		    }
		    History one = new History(debug, id, "Updated","Program",user.getId());
		    one.doSave();					
		}
	    }
	    else{
		message += " You can not update";
		success = false;
	    }
	    try {
		if(user.canEdit()){
		    CalHandle ch = new CalHandle(debug, id, null);
		    back = ch.process();
		    if(!back.equals("")){
			message += back;
			logger.error(back);
			success = false;
		    }
		}
	    }
	    catch (Exception ex){
		logger.error(ex);
		message += ex;
		success = false;
	    }
	}
	else if (action.equals("Delete")){
	    //
	    if(user.canDelete()){
		String back = pr.doDelete();
		back += sopt.doDelete();
		if(!back.equals("")){
		    message += back;
		    logger.error(back);
		    success = false;
		}
		else{
		    pr = new Program(debug);
		    message += " Deleted successfully";
		    id="";
		    String str = url+"Browse?msg=Deleted+Successfully";
		    res.sendRedirect(str);
		    return;					
		}
	    }
	    else{
		message += " You can not delete";
		success = false;
	    }
	}
	else if (!id.equals("")){
	    //
	    // case from browsing
	    //
	    String back = pr.doSelect();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    try{
		back = sopt.doSelect();
		if(!back.equals("")){
		    message += back;
		    logger.error(back);
		    success = false;
		}				
		days = pr.getDays();
		if(!days.equals("")){
		    days = days.toUpperCase(); // backward comptability
		    if(days.indexOf("SU") > -1 && // MON. - SUN.
		       days.indexOf("-") > -1) // 
			d_all = "checked";
		    else if(days.indexOf("F") > -1 && // MON. - FRI.
			    days.indexOf("-") > -1 ) // MON - FRI
			d_mon_fri = "checked";
		    else{
			if(days.indexOf("SU") > -1) d_sun="checked";
			if(days.indexOf("M") > -1) d_mon="checked";
			if(days.indexOf("TU") > -1) d_tue="checked";
			if(days.indexOf("W") > -1) d_wed="checked";
			if(days.indexOf("TH") > -1) d_thu="checked";
			if(days.indexOf("F") > -1) d_fri="checked";
			if(days.indexOf("SA") > -1) d_sat="checked";
		    }
		}
	    }
	    catch(Exception ex){
		logger.error(ex);
		message += " Error retreiving the recored "+ex;
		success = false;
	    }
	}

        //
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
	    allPlans = new PlanList(debug);
	    back = allPlans.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    if(id.equals("") && ptitle.equals("")){
		plan = pr.getPlan();
		if(plan != null){
		    ptitle = plan.getProgram_title();
		    if(pr.getSeason().equals("")){
			pr.setSeason(plan.getSeason());
		    }
		    if(pr.getYear().equals("")){
			pr.setYear(plan.getProgram_year());
		    }										
		}
	    }
	}
        if(id.equals("") && !plan_id.equals("")){
	    String back = "";
	    Program oldProg = null;
	    plan = pr.getPlan();
	    PrePlan prePlan = null;
	    if(plan != null){
		prePlan = plan.getPrePlan();
		oldProg = plan.getLastProgram();
		if(oldProg == null){ // we get it from the previous plan
		    // that we copied from
		    if(prePlan != null){ 
			String old_plan_id = prePlan.getPrev_id();
			if(old_plan_id != null){
			    Plan oldPlan = new Plan(debug, old_plan_id);
			    back = oldPlan.doSelect();
			    if(back.equals("")){
				oldProg = oldPlan.getLastProgram();
			    }
			    else{
				message += back;
				success = false;
			    }
			}
		    }
		}
		if(oldProg != null){
		    String old_id = oldProg.getId();
		    oldProg.setRegDeadLine("");
		    oldProg.setCode("");
		    oldProg.setLead_id(plan.getLead_id());
		    oldProg.setTitle(plan.getProgram_title());
		    oldProg.setPlan_id(plan.getId());
		    oldProg.setInstructor(plan.getInstructor().getName());
		    if(prePlan != null){
			oldProg.setSeason(prePlan.getSeason());
			oldProg.setYear(prePlan.getYear());
		    }
		    back = oldProg.doDuplicate();
		    if(back.equals("")){
			id = oldProg.getId();
			pr = oldProg; // swap programs
			message ="Program was create using info from Pre Pan, Plan and Program "+old_id;
		    }
		    else{
			message += back;
			success = false;
		    }
		}
		else{ // no old program to copy from
		    pr.setLead_id(plan.getLead_id());
		    pr.setTitle(plan.getProgram_title());
		    pr.setPlan_id(plan.getId());
		    pr.setInstructor(plan.getInstructor().getName());
		    if(prePlan != null){
			pr.setSeason(prePlan.getSeason());
			pr.setYear(prePlan.getYear());
		    }					
		}
	    }
	}
        //
	if(action.equals("Refresh")){
	    action = prevAction;
	}
	sessions = pr.getSessions();
	marketFound = pr.hasMarket();
	String star="*";
	//
	// checkbox flags
	//
	if(!pr.getMarketTask().equals("")) marketTask="checked=\"checked\"";
	if(!pr.getVolTask().equals("")) volTask="checked";
	if(!pr.getSponTask().equals("")) sponTask="checked";
	if(!pr.getBudgetTask().equals("")) budgetTask="checked";
	if(!pr.getEvalTask().equals("")) evalTask="checked";
	if(!pr.getNoPublish().equals("")) noPublish="checked";				
	if(!pr.getCodeNeed().equals("")) codeNeed="checked";
	if(!pr.getAllAge().equals("")) allAge="checked";
	if(!pr.getWParent().equals("")) wParent="checked";
	if(!d_sun.equals("")) d_sun="checked";
	if(!d_mon.equals("")) d_mon="checked";
	if(!d_tue.equals("")) d_tue="checked";
	if(!d_wed.equals("")) d_wed="checked";
	if(!d_thu.equals("")) d_thu="checked";
	if(!d_fri.equals("")) d_fri="checked";
	if(!d_sat.equals("")) d_sat="checked";
	if(!d_mon_fri.equals("")) d_mon_fri="checked";
	if(!d_all.equals("")) d_all="checked";
	if(!sopt.getDays_c().equals("")) days_c="checked";
	if(!sopt.getStartTime_c().equals("")) startTime_c="checked";
	if(!sopt.getInCityFee_c().equals("")) inCityFee_c="checked";
	if(!sopt.getNonCityFee_c().equals("")) nonCityFee_c="checked";
	if(!sopt.getOtherFee_c().equals("")) otherFee_c="checked";
	if(!sopt.getLocation_c().equals("")) location_c="checked";
	if(!sopt.getDescription_c().equals("")) description_c="checked";
	if(!sopt.getAgeFrom_c().equals("")) ageFrom_c="checked";
	if(!sopt.getPartGrade_c().equals("")) partGrade_c="checked";
	if(!sopt.getMinMaxEnroll_c().equals("")) minMaxEnroll_c="checked";
	if(!sopt.getRegDeadLine_c().equals("")) regDeadLine_c="checked";
	if(!sopt.getInstructor_c().equals("")) instructor_c="checked";
	if(!sopt.getStartDate_c().equals("")) startDate_c="checked";
	if(!sopt.getClassCount_c().equals("")) classCount_c="checked";
	if(!sopt.getMemberFee_c().equals("")) memberFee_c="checked";
	if(!sopt.getNonMemberFee_c().equals("")) nonMemberFee_c="checked";		
	sessionSort = sopt.getSessionSort();
	out.println("<html><head><title>Promt</title>");
	Helper.writeWebCss(out, url);
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

        out.println("  function clearTaxo() {           ");
	out.println("   document.getElementById('current_tax').innerHTML='';");
	out.println("   document.getElementById('taxonomy_ids').value='';");
	out.println("   return false; ");
	out.println("   } ");
	out.println("  function createSubList(obj, sub_id) {           ");
	out.println("   var kk = obj.options.selectedIndex;         ");
	out.println("   var key = obj.options[kk].value;    ");
	out.println("   var text = obj.options[kk].text;    ");
	out.println("   var old_text = document.getElementById('current_tax').innerHTML;");
	out.println("   if(old_text != '') old_text += ','; ");

	out.println("  document.getElementById('current_tax').innerHTML=old_text+text;");
	out.println("   var old_taxo = document.getElementById('taxonomy_ids').value;");
	out.println("   if(old_taxo != '') old_taxo += ','; ");
	out.println("  document.getElementById('taxonomy_ids').value=old_taxo+key;");								
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
	out.println("  function addToTaxo(obj) {           ");
	out.println("   var kk = obj.options.selectedIndex;         ");
	out.println("   var key = obj.options[kk].value;    ");
	out.println("   var text = obj.options[kk].text;    ");
	out.println("   var old_text = document.getElementById('current_tax').innerHTML;");
	out.println("   if(old_text != '') old_text += '->'; ");

	out.println("  document.getElementById('current_tax').innerHTML=old_text+text;");
	out.println("   var old_taxo = document.getElementById('taxonomy_ids').value;");
	out.println("   if(old_taxo != '') old_taxo += ':'; ");

	out.println("  document.getElementById('taxonomy_ids').value=old_taxo+key;");				
	out.println("  }                                    ");				
	out.println("  function validateNumber(x){             ");            
	out.println("   if(x.length > 0 ){                     ");
	out.println("   if(isNaN(x)){                          ");
	out.println("      return false;                       ");
	out.println("     }}                                   ");
	out.println("      return true;                        ");
	out.println("     }                                    ");
	out.println(" function validateTextarea(ss, len){ ");
        out.println("    if (ss.value.length > len ){     "); 
	out.println("       alert(\"Text Area should not be more than \"+len+"+
		    " \" characters\"); ");
	out.println("       ss.value = ss.value.substring(0,len); ");
	out.println("       }                             ");
	out.println("   }                                 ");
	out.println("  function validateForm() {          ");
	out.println("  var x=\"\" ;                       ");				
	if(pr.getId().equals("")){
	    out.println("      x = document.myForm.season.options[document.myForm.season.options.selectedIndex].value;  ");
	    out.println("  if(x == \"\"){                               ");
	    out.println("   alert(\"Season is required\"); ");
	    out.println("   return false;                               ");
	    out.println("  }");
	    out.println("   x = \"\";              ");
	    out.println("   x = document.myForm.year.options[document.myForm.year.options.selectedIndex].value;  ");
	    out.println("  if(x == \"\"){                             ");
	    out.println("   alert(\"Year field should be selected\"); ");
	    out.println("   return false;      ");
	    out.println("   }                  ");						
	}
	out.println("   x = document.myForm.lead_id.options.selectedIndex ");;
	out.println("  if(x < 1){                 ");
	out.println("   alert(\"Lead programmer is required\"); ");
	out.println("   return false;          ");
	out.println("  }                       ");
	out.println("  if(document.myForm.title.value.length == 0){     ");
	out.println("   alert(\"The program title is required\"); ");
	out.println("   return false;          ");
	out.println("  }                       ");
	out.println("   x = \"\";          ");
	out.println("    x = document.myForm.category_id.options[document.myForm.category_id.selectedIndex].text;  ");
	out.println("  if(x == \"\"){                      ");
	out.println("   alert(\"Guide heading field should be selected\"); ");
	out.println("   return false;                  ");
	out.println("   }                              ");				
        out.println(" var sum_len = document.myForm.summary.value.trim().length ");
        out.println(" if(sum_len == 0){ ");
	out.println("  alert(\"You need to add summary to your program\") ");
	out.println("  document.myForm.summary.focus(); ");
	out.println("     return false;                ");
	out.println("   }                             ");
        out.println(" if(sum_len > 160){ ");
	out.println("  alert(\"Summary should be less than 160 char\") ");
	out.println("  document.myForm.summary.focus(); ");				
	out.println("     return false;                ");
	out.println("   }                             ");				

	out.println("  if(document.myForm.location_id.value.length == 0){   ");
	out.println("   alert(\"Location is required\"); ");
	out.println("     return false;                 ");
	out.println("    }                              ");				
        out.println(" if(!validateNumber(document.myForm.ageFrom.value)){ ");
	out.println("  alert(\"Participation Age From not a valid number\") ");
	out.println("       return false;                ");
	out.println("      }                            ");
        out.println(" if(!validateNumber(document.myForm.ageTo.value)){ ");
	out.println("  alert(\"Participation Age To not a valid number\") ");
	out.println("       return false;                ");
	out.println("      }                             ");
        out.println(" if(document.myForm.taxonomy_ids.value==\"\"){ ");
	out.println("  alert(\"You need to select a category\") ");
	out.println("       return false;                ");
	out.println("  }                             ");
        out.println(" if(document.myForm.codeNeed.checked){ ");
	out.println("  if(document.myForm.nraccount.value.length == 0){   ");
	out.println("   alert(\"Non-reverting account should be filled\"); ");
	out.println("     return false;                 ");
	out.println("    }                             ");				
	out.println("  if(document.myForm.fee.value.length == 0){   ");
	out.println("   alert(\"Transaction fee should be filled\"); ");
	out.println("     return false;                 ");
	out.println("    }                              ");
	out.println("  }                             ");				
        out.println(" if(!checkQuote()) return false;    "); // check the title
	out.println("   return true;                     ");
	out.println("  }	                         ");
	out.println("  function validateDelete(){    ");            
	out.println("  var x = false ;                   ");
	out.println("  x = window.confirm(\"Are you Sure you want to delete "+
		    "this program and all other related records\");");
	out.println("  if(x){                           ");
	out.println("  document.forms[0].action2.value=\"Delete\"; ");
	out.println("  document.forms[0].submit();      ");
	out.println("   return x;                       ");
	out.println(" }}	                                ");
	out.println("  function checkQuote(){           ");     
	out.println("  var title = document.myForm.title.value;   "); // e.which e.keyCode
	out.println("  if(title.length > 0){        ");
        out.println("  var quote = false;           ");
	out.println("  for(i=0; i<title.length; ++i){      ");	
	out.println("   var key= title.charAt(i);   ");
	out.println("    if(key == \"\\\"\"){       ");
	out.println("       quote = true;           "); 
	out.println("     }}                        "); 
	out.println("    if(quote){                 ");
	out.println("    alert(\"Please do not use double quote in program title \"); ");
	out.println("  document.myForm.title.select();  ");
	out.println("  document.myForm.title.focus();   ");
	out.println("  return false;                     ");
	out.println("  }} 	                             ");
	out.println("  return true;                      ");
	out.println(" }	                                 ");
	out.println("  function checkCatSub(item){       ");   
	out.println(" document.forms[0].action2.value='Refresh';");
	out.println(" document.forms[0].submit();               ");
	out.println(" }                                         ");
	out.println("/*]]>*/\n");			
	out.println(" </script>                                 ");   
	out.println("</head><body>");
        out.println("<center>");
	Helper.writeTopMenu(out, url);
	String tdWidth = "width=\"20%\"";
	if(id.equals("")){
	    out.println("<h2>New Program</h2>");
	}
	else{
	    out.println("<h2>Edit Program </h2>");
	}
	if(!message.equals("")){
		out.println(message);
	    out.println("<br />");
	}
	out.println("</center>");
						
	out.println(" * required field <br />** You can add more than one category/sub-category, if no category match choose 'Other' <br />If you make a wrong selection click on 'Clear all Categories' and start all over<br /> *** Check this box if this program is not intended to be published in the guide or online. <br />");
						
	out.println("<br />");
	//
	//the real table
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\" "+
		    "onSubmit=\"return validateForm()\">");
	out.println("<input type=\"hidden\" name=\"action2\" value=\"\" />");
	out.println("<input type=\"hidden\" name=\"prevAction\" value=\""+action+"\" />");
	if(!pr.getId().equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\"" + pr.getId()+"\" />");
	}
	// plan id
	out.println("<input type=\"hidden\" name=\"plan_id\" value=\""+pr.getPlan_id()+"\" />");
	out.println("<input type=\"hidden\" id=\"taxonomy_ids\" name=\"taxonomy_ids\" value=\""+pr.getTaxonomy_ids()+"\" />");						
	out.println("<center><table border=\"1\" width=\"90%\">");
	out.println("<caption>Program Info</caption>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"center\">");
	out.println("<table width=\"100%\" border=\"1\">");
	out.println("<caption>Completed Tasks </caption> ");
	out.println("<tr><td>");						
	out.println("<input type=\"checkbox\" name=\"marketTask\" value=\"y\" "+
		    marketTask+ " /><b>Marketing </b>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"volTask\" value=\"y\" "+
		    volTask+ " /><b>Volunteer </b>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"sponTask\" value=\"y\" "+
		    sponTask+ " /><b>Sponsorship </b>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"evalTask\" value=\"y\" "+
		    evalTask+ " /><b>Evaluation </b>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"noPublish\" value=\"y\" "+
		    noPublish+ " /> *** <b>Does Not Go in Guide </b>");
	out.println("</td>");						
	out.println("</tr></table></td></tr>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td>");
	out.println("<table width=\"100%\">");
	out.println("<caption>Program Details</caption>");
	//
	// Title season year
	out.println("<tr><td align=\"right\" "+tdWidth+"><label for=\"ptitle\">Program Title:</label>");
	out.println("</td><td align=\"left\">");
	out.println("<input type=\"text\" id=\"ptitle\" name=\"title\" maxlength=\"128\" value=\""+
		    pr.getTitle() + "\" size=\"80\" onchange=\"checkQuote();\">"+star);
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\">");
	out.println("<label for=\"season_id\">Season:</label></td><td>");
	if(pr.getId().equals("")){
	    out.println("<select name=\"season\" id=\"season_id\">");
	    out.println("<option value=\""+pr.getSeason()+"\" selected>"+
			pr.getSeason()+"\n");
	    out.println(Helper.allSeasons);
	    out.println("</select> ");
	    out.println("<label for=\"season2_id\"> Season 2:</label>");
	    out.println("<select name=\"season2\" id=\"season2_id\">");
	    out.println("<option value=\""+pr.getSeason2()+"\" selected>"+pr.getSeason2()+"\n");
	    out.println(Helper.allSeasons);
	    out.println("</select>");
								
	}
	else{
	    out.println(pr.getSeason());
	    if(!pr.getSeason2().equals("")){
		out.println(" <b> Season 2:</b>" +pr.getSeason2());
	    }								
	}

	if(pr.getId().equals("")){
	    out.println(" <label for=\"pyear\">Year:</label>");	    
	    out.println("<select name=\"year\" id=\"pyear\">");
	    out.println("<option value=\"\">\n");
	    int years[] = Helper.getPrevYears();
	    for(int yy:years){
		String selected="";
		if(pr.getYear().equals(""+yy))
		    selected="selected=\"selected\"";
		out.println("<option "+selected+">"+yy+"</option>");
	    }														
	    out.println("</select>&nbsp;&nbsp;");
	}
	else{
	    out.println(" <b>Year:</b>");	    	    
	    out.println(pr.getYear()+" <b> Program ID: </b>"+pr.getId());
	}
	out.println("</td></tr>");
	// 
	// Related plan
	if(pr.hasPlan()){
	    out.println("<tr><td align=\"right\" valign=\"top\"><b>Related Plan:</b>"+
			"</td><td>");			
	    out.println("<a href=\""+url+"ProgPlan?id="+pr.getPlan_id()+"&action=zoom\">Plan</a>");
	    out.println("</td></tr>");			
	}
	else{
	    out.println("<tr><td align=\"right\" valign=\"top\"><b>Related Plan:</b>"+
			"</td><td>");			
	    out.println("No plan is available for this program");
	    out.println("</td></tr>");		
	}
	//
	// Area
	out.println("<tr><td align=\"right\"><label for=\"area_id\">Area/Division:</label>");
	out.println("</td><td align=\"left\">");
	out.println("<select name=\"area_id\" id=\"area_id\">");
	out.println("<option value=\"\"></option>");
	if(areas != null){
	    for(Type one:areas){
		String selected = "";
		if(one.getId().equals(pr.getArea_id()))
		    selected = "selected=\"selected\"";
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}		
	out.println("</select>");
	if(user.isAdmin()){
	    out.println("<label for=\"otherArea\">Other Area</label>");
	    out.println("<input type=\"text\" name=\"otherArea\" size=\"35\" value=\"\" id=\"otherArea\" maxlength=\"70\" />");
	}
	out.println("</td></tr>");
	//
	// Guide Heading
	out.println("<tr><td align=\"right\"><label for=\"cat_id\">Guide Heading:</label>");
	out.println("</td><td>");
	out.println("<select name=\"category_id\" id=\"cat_id\">");
	out.println("<option value=\"\"></option>");
	if(categories != null){
	    for(Type one:categories){
		String selected = "";
		if(one.getId().equals(pr.getCategory_id())){
		    selected = "selected=\"selected\"";
		}
		else if(!one.isActive()) continue;					
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select>"+star+" ");
	out.println("</td></tr>");
	if(user.isAdmin()){
	    out.println("<tr><td align=\"right\"><label for=\"otherCat\">New Guide Heading:</label>");
	    out.println("</td><td>");
	    out.println("<input type=\"text\" name=\"otherCategory\" size=\"30\" value=\"\" id=\"otherCat\" maxlength=\"70\"/>This will add new heading to the heading list.");
	    out.println("</td></tr>");
	}
	
	out.println("<tr><td  align=\"right\">Categories</td>");
	out.println("<td><div id=\"current_tax\">"+pr.getTaxonomyInfo()+"</div>");
	out.println("</td></tr>");
	out.println("<tr><td>&nbsp;</td><td>");
	out.println("<button onclick=\"return clearTaxo();\">Clear All Categories</button></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"tax_id\">Category: **</label>");
	out.println("</td><td>");
	out.println("<select name=\"tax_id\" id=\"tax_id\" onchange=\"createSubList(this,'sub_tax_id');\">");
	out.println("<option value=\"\"></option>");
	if(taxonomies != null){
	    for(Type one:taxonomies){
		out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select>");
	out.println("<label for=\"sub_tax_id\">Sub Categories:</label>");
	out.println("<select name=\"sub_tax_id\" id=\"sub_tax_id\" onchange=\"addToTaxo(this);\" >");
	out.println("<option value=\"\"></option>");
	out.println("</select>");						
	out.println("</td></tr>");						
	//
	// Fix this
	Type category = pr.getCategory();
	Type category2 = pr.getCategory2();
	// Parks as an example
	if(category.getName().startsWith("People") ||
	   category2.getName().startsWith("People")){
	    //
	    // Guide Sub Heading
	    out.println("<tr><td align=\"right\"><label for=\"subcat\">Guide Sub Heading:</label>");
	    out.println("</td><td>");
	    out.println("<select name=\"subcat\" id=\"subcat\">");
	    out.println("<option selected>"+pr.getSubcat()+"\n"); 
	    for(int i=0;i<Helper.peopleUnivSub.length; i++){
		out.println("<option>"+Helper.peopleUnivSub[i]+"\n");
	    }
	    out.println("</select></td></tr>");
	}
	else if(category.getName().startsWith("Adult") ||
		category.getName().startsWith("Youth") ||
		category.getName().startsWith("Teen") ||
		category.getName().startsWith("Preschool") ||
		category2.getName().startsWith("Adult") ||
		category2.getName().startsWith("Youth") ||
		category2.getName().startsWith("Teen") ||
		category2.getName().startsWith("Preschool")){
	    //
	    // Guide Sub Heading
	    out.println("<tr><td align=\"right\"><label for=\"subcat\">Guide Sub Heading:</label>");
	    out.println("</td><td>");
	    out.println("<select name=\"subcat\" id=\"subcat\">");
	    out.println("<option selected>"+subcat+"\n"); 
	    for(int i=0;i<Helper.preschoolSub.length; i++){
		out.println("<option>"+Helper.preschoolSub[i]+"\n");
	    }
	    out.println("</select></td></tr>");
	}
	//
	// Lead programs
	out.println("<tr><td align=\"right\"><label for=\"lead_id\">Lead:");
	out.println("</label></td><td align=\"left\">");
	out.println("<select name=\"lead_id\" id=\"lead_id\">");
	out.println("<option value=\"\">Pick a Lead</option>");
	if(leads != null){
	    for(Lead one:leads){
		String selected = "";
		if(one.getId().equals(pr.getLead_id())){
		    selected = "selected=\"selected\"";
		}
		else if(!one.isActive()) continue;
		out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
	    }
	}
	out.println("</select>"+star);
	out.println("</td></tr>");
	out.println("<tr><td>&nbsp;</td></tr>"); // separator
	//
	out.println("<tr><td colspan=\"2\" align=\"center\" bgcolor=\"navy\" "+
		    "><label for\"summary\"><font color=\"white\">Program Summary</font></label></h3>");
	out.println("</td></tr>");
	out.println("<tr><td "+tdWidth+"></td><td align=\"left\">");
	out.println("Up to 160 characters (3 lines max)</td></tr>");
	out.println("<tr><td><td align=\"left\">");
	out.println("<textarea rows=\"3\" cols=\"70\" wrap=\"soft\" name=\"summary\" "+
		    "id=\"summary\" onkeyup=\"validateTextarea(this,160)\">"); 
	out.println(pr.getSummary());
	out.println("</textarea></td></tr>");
	out.println("<tr><td>&nbsp;</td></tr>"); // separator
	// statement
	out.println("<tr><td colspan=\"2\" align=\"center\" bgcolor=\"navy\" "+
		    "><label for=\"broch\"><font color=\"white\">Brochure Statement</font></label>");
	out.println("</td></tr>");
	out.println("<tr><td></td><td align=\"left\">");
	out.println("Up to 10,000 characters </td></tr>");
	out.println("<tr><td></td><td align=\"left\">");
	out.println("<textarea rows=\"15\" cols=\"70\" id=\"broch\" wrap=\"soft\" name=\"statement\" "+
		    "onkeyup=\"validateTextarea(this,10000)\">"); 
	out.println(pr.getStatement());
	out.println("</textarea></td></tr>");
	out.println("<tr><td>&nbsp;</td></tr>"); // separator
	//
	// other guide info
	out.println("<tr><td colspan=\"2\" align=\"center\" bgcolor=\"navy\" "+
		    "><label for=\"oginfo\"><font color=\"white\">Other Guide Info</font></label>");		
	out.println("<tr><td></td><td align=\"left\">");
	out.println("Up to 4000 characters </td></tr>");
		    
	out.println("<tr><td></td><td align=\"left\">");
	out.println("<textarea rows=\"15\" cols=\"70\" wrap=\"soft\" name=\"oginfo\" "+
		    "id=\"oginfo\" onchange=\"validateTextarea(this, 4000)\">"); 
	out.println(pr.getOginfo());
	out.println("</textarea></td></tr>");
	out.println("</table></td></tr>");
	//
	out.println("<tr bgcolor=\"#CDC9A3\">");		
	out.println("<td align=\"center\">");
	out.println("<table width=\"100%\">");
	out.println("<caption>Code & Fees</caption>");
	//
	///////////////////////////////////////////////////////////
	// common fields with session, added p as prefix 
	// the names to distinguish them
	//
	out.println("<tr><td align=\"right\" "+tdWidth+">&nbsp;</td><td align=\"left\">Fill only the fields that are the same for all the "+
		    "sessions in this program</td></tr>");
	// code
	out.println("<tr><td align=\"right\"><label for=\"code\">Code:");
	out.println("</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"code\" maxlength=\"10\" value=\""+
		    pr.getCode() + "\" size=\"10\" id=\"code\" />");
	out.println(" <input type=\"checkbox\" name=\"codeNeed\" "+
		    "value=\"y\" "+codeNeed+" id=\"codeNeed\" />");
	out.println("<label for=\"codeNeed\"> Code needed </label>");
	out.println("</td></tr>");
	//
	// non-reverting account 
	// fee
	out.println("<tr><td align=\"right\"><label for=\"nrac\">Non-reverting Account:");
	out.println("</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"nraccount\" maxlength=\"15\" value=\""+
		    pr.getNraccount() + "\" size=\"15\" id=\"nrac\" />");
	out.println("<label for=\"fee\">Transaction Fee $</label>");
	out.println("<input type=\"text\" name=\"fee\" maxlength=\"5\" value=\""+
		    pr.getFee()+"\" size=\"5\" id=\"fee\" />");
	out.println("</td></tr>");
	//
	// 3 fees
	out.println("<tr><td align=\"right\"><b>Fees</b>");
	out.println("</td><td align=\"left\"><label for=\"in_city\">In-City: $</label>");
	out.println("<input type=\"text\" name=\"inCityFee\" maxlength=\"20\" value=\""+
		    pr.getInCityFee() + "\" size=\"6\" id=\"in_city\"/>");
	out.println("<label for=\"non_city\">Non-City: ");
	out.println("</label>$");
	out.println("<input type=\"text\" name=\"nonCityFee\" maxlength=\"20\" value=\""+
		    pr.getNonCityFee() + "\" size=\"6\" id=\"non_city\"/>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\">&nbsp;");
	out.println("</td><td align=\"left\"><label for=\"memb\">Member: </label>$");
	out.println("<input type=\"text\" name=\"memberFee\" maxlength=\"20\" value=\""+
		    pr.getMemberFee() + "\" size=\"6\" id=\"memb\"/>");
	out.println("<label for=\"non_memb\">Non-Member: ");
	out.println("</label>$");
	out.println("<input type=\"text\" name=\"nonMemberFee\" maxlength=\"20\" value=\""+
		    pr.getNonMemberFee() + "\" size=\"6\" id=\"non_memb\"/>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\">&nbsp;");
	out.println("</td><td align=\"left\">");	
	out.println("<label for=\"otherFee\">Other: </label>$");
	out.println("<input type=\"text\" name=\"otherFee\" maxlength=\"30\" value=\""+
		    pr.getOtherFee() + "\" size=\"25\" id=\"otherFee\"/>");
	out.println("</td></tr>");	
	// 
	out.println("<tr><td align=\"right\">");
	out.println("</td><td align=\"left\"><font color=\"green\" size=\"-1\">"+
		    "*Includetransaction fee</td></tr>");

	out.println("</table></td></tr>");
	//
	out.println("<tr bgcolor=\"#CDC9A3\">");		
	out.println("<td align=\"center\">");
	out.println("<table width=\"100%\">");
	out.println("<caption>Age, Dates & Time</caption>");
	//
	// age, grade
	out.println("<tr><td align=\"right\" "+tdWidth+"><label for=\"ageFrom\">Staring Participant Age:</label><br>");
	out.println("</td><td align=\"left\">");

	out.println("<input type=\"text\" name=\"ageFrom\" maxlength=\"3\" value=\""+
		    pr.getAgeFrom() + "\" size=\"3\" id=\"ageFrom\" />");
	out.println("<label for=\"ageTo\">To:</label>");
	out.println("<input type=\"text\" name=\"ageTo\" maxlength=\"3\" value=\""+
		    pr.getAgeTo() + "\" size=\"3\" id=\"ageTo\" />");
	out.println(" or ");
	out.println("<input type=\"checkbox\" name=\"allAge\" value=\"y\" "+
		    allAge + " id=\"allAge\" /><label for=\"allAge\">All Ages Welcome");
	out.println("&nbsp;&nbsp;");
	out.println("<input type=\"checkbox\" name=\"wParent\" value=\"y\" "+
		    wParent + " id=\"wparent\" /><label for=\"wparent\" />w/Parent</td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"otherAge\">Other Age:</label>");
	out.println("</td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"otherAge\" maxlength=\"30\" value=\""+
		    pr.getOtherAge()+ "\" size=\"30\" id=\"otherAge\"/>");
	out.println("<label for=\"grade\">Grade:</label>");
	out.println("<input type=\"text\" name=\"partGrade\" maxlength=\"15\" value=\""+
		    pr.getPartGrade() +"\" size=\"15\" id=\"grade\" />");
	out.println("</td></tr>");
	//
	// date time
	out.println("<tr><td align=\"right\">");
	out.println("<label for=\"startDate\">Start Date:</label>");
	out.println("</td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"startDate\" value=\""+
		    pr.getStartDate() + "\" maxlength=\"10\" size=\"10\" "+
		    " class=\"date\" id=\"startDate\" /> ");
	//
	//
	out.println("<label for=\"endDate\">End Date:</label> ");
	out.println("<input type=\"text\" name=\"endDate\" value=\""+
		    pr.getEndDate() + "\" maxlength=\"10\" size=\"10\" "+
		    " class=\"date\" id=\"endDate\" />(mm/dd/yyyy)");
	//
	out.println("</td></tr>");
	//
	// days, # classes
	//
	out.println("<tr><td align=\"right\" valign=\"top\">");
	out.println("</td><td align=\"left\">");
	out.println("<table width=\"50%\">");
	out.println("<caption>Day(s)</caption>");
	out.println("<tr><td align=\"left\">");
	out.println("<input type=\"checkbox\" name=\"d_sun\" value=\"y\" "+
		    d_sun+" id=\"d_sun\"/><label for=\"d_sun\">Su</label>");
	out.println("</td><td align=\"left\">");
	out.println("<input type=\"checkbox\" name=\"d_mon\" value=\"y\" "+
		    d_mon+" id=\"d_mon\"/><label for=\"d_mon\">M</label>");
	out.println("</td><td align=\"left\">");
	out.println("<input type=\"checkbox\" name=\"d_tue\" value=\"y\" "+
		    d_tue+" id=\"d_tue\"/><label for=\"d_tue\">Tu</label>");
	out.println("</td><td align=\"left\">");
	out.println("<input type=\"checkbox\" name=\"d_wed\" value=\"y\" "+
		    d_wed+" id=\"d_wed\"/><label for=\"d_wed\">W</label>");
	out.println("</td><td align=\"left\">");
	out.println("<input type=\"checkbox\" name=\"d_thu\" value=\"y\" "+
		    d_thu+" id=\"d_thu\"/><label for=\"d_thu\">Th</label>");
	out.println("</td><td align=\"left\">");
	out.println("<input type=\"checkbox\" name=\"d_fri\" value=\"y\" "+
		    d_fri+" id=\"d_fri\"/><label for=\"d_fri\">F</label>");
	out.println("</td></tr><tr><td align=\"left\">");
	out.println("<input type=\"checkbox\" name=\"d_sat\" value=\"y\" "+
		    d_sat+" id=\"d_sat\"/><label for=\"d_sat\">Sa</label>");
	out.println("</td><td> </td><td colspan=\"2\" align=\"left\">");
	out.println("<input type=\"checkbox\" name=\"d_mon_fri\" value=\"y\" "+
		    d_mon_fri+" id=\"mon_fri\"/><label for=\"mon_fri\">M-F</label>");
	out.println("</td><td colspan=\"2\" align=\"left\">");
	out.println("<input type=\"checkbox\" name=\"d_all\" value=\"y\" "+
		    d_all+" id=\"d_all\"/><label for=\"d_all\">M-Su</label>");
	out.println("</td></tr></table></td></tr>");
	//
	out.println("<tr><td align=\"right\">");
	out.println("<b>Start Time:</b>");
	out.println("</td><td>");
	out.println("<input type=\"text\" name=\"startTime\" maxlength=\"10\" "+
		    "value=\""+ pr.getStartTime() + "\" size=\"10\" readonly=\"readonly\" /> ");
	out.println("<input type=\"button\" onclick='"+
		    "window.open(\""+url+"PickTime?id="+id+"&wtime=startTime&time="+java.net.URLEncoder.encode(pr.getStartTime())+"\",\"Time\","+
		    "\"toolbar=0,location=0,"+
		    "directories=0,status=0,menubar=0,"+
		    "scrollbars=0,top=300,left=300,"+
		    "resizable=1,width=400,height=250\");'"+
		    " value=\"Pick Time\" />");		
						
	out.println("<b>End Time: </b><input type=\"text\" name=\"endTime\" maxlength=\"10\" "+
		    "value=\""+ pr.getEndTime() + "\" size=\"10\" readonly=\"readonly\" />");
	out.println("<input type=\"button\" onclick='"+
		    "window.open(\""+url+"PickTime?id="+id+"&wtime=endTime&time="+java.net.URLEncoder.encode(pr.getEndTime())+"\",\"Time\","+
		    "\"toolbar=0,location=0,"+
		    "directories=0,status=0,menubar=0,"+
		    "scrollbars=0,top=300,left=300,"+
		    "resizable=1,width=400,height=250\");'"+
		    " value=\"Pick Time\" />");		
						
	out.println(" <label for=\"class_cnt\"># of Classes: </label>");
	out.println("<input type=\"text\" name=\"classCount\" maxlength=\"10\" size=10 id=\"class_cnt\" "+
		    " value=\""+pr.getClassCount()+"\" />");
	out.println("</td></tr>");
	//
	// Min-Max Enroll 
	out.println("<tr><td align=\"right\"><label for=\"min_max\">Min-Max Enrollment:");
	out.println("</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"minMaxEnroll\" maxlength=\"10\" id=\"min_max\" "+
		    "value=\""+pr.getMinMaxEnroll() + "\" size=\"10\" />");
	out.println("<label for=\"wait_list\">Wait List:</label>");
	out.println("<input type=\"text\" name=\"waitList\" maxlength=\"5\" "+
		    "value=\""+pr.getWaitList() + "\" size=\"5\" id=\"wait_list\"/>");
	out.println("</td></tr>");
	//
	// reg deadline
	out.println("<tr><td align=\"right\"><label for=\"regDeadLine\">Reg. Deadline:");
	out.println("</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"regDeadLine\" maxlength=\"10\" "+
		    "value=\""+pr.getRegDeadLine() + "\" size=\"10\" "+
		    " class=\"date\" id=\"regDeadLine\" />(mm/dd/yyyy)");
	out.println("</td></tr>");
	//
	out.println("</table></td></tr>");
	//
	out.println("<tr bgcolor=\"#CDC9A3\">");		
	out.println("<td align=\"center\">");		
	out.println("<table width=\"100%\">");
	out.println("<caption>Location, Instructor and Description</caption>");
	//
	// location
	out.println("<tr><td align=\"right\" "+tdWidth+"><label for=\"location_id\">Location:");
	out.println("</label></td><td align=\"left\">");
	out.println("<select name=\"location_id\" id=\"location_id\">");
	out.println("<option value=\"\"></option>");
	String loc_id = pr.getLocation_id();
	if(locations != null){
	    for(Type one:locations){
		if(one.getId().equals(loc_id)){
		    out.println("<option selected=\"selected\" value=\""+loc_id+"\">"+one.getName()+"</option>");
		}
		else{
		    out.println("<option value=\""+one.getId()+"\">"+one.getName()+"</option>");
		}
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td align=\"right\">");
	out.println("<label for=\"loc_det\">Location Details:</label>");
	out.println("</td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"location_details\" maxlength=\"120\" value=\""+pr.getLocation_details()+"\" size=\"60\" id=\"loc_det\"/>(address, parking, etc)");
	out.println("</td></tr>");
	//
	// instructor
	out.println("<tr><td align=\"right\"><label for=\"instr\">Instructor:");
	out.println("</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"instructor\" maxlength=\"120\" "+
		    "value=\""+pr.getInstructor() + "\" id=\"instr\" size=\"50\" />");
	out.println("</td></tr>");
	//
	// description
	out.println("<tr><td></td><td align=\"left\">Up to 160 characters</td></tr>");						
	out.println("<tr><td align=\"right\" valign=\"top\"><label for=\"descr\">Description:");
	out.println("</label></td><td align=\"left\">");
	out.println("<textarea name=\"description\" rows=\"3\" cols=\"60\" "+
		    "onchange=\"validateTextarea(this,160)\" id=\"descr\" "+
		    "wrap=\"soft\">");
	out.println(pr.getDescription());
	out.println("</textarea>");
	out.println("</td></tr>");
	//
	////////////////////////////////////////////////////////////
	// Sessions flags
	//
	if(sessions != null && sessions.size() > 0){
	    out.println("<tr><td colspan=\"2\"><b>Current Number of Sessions:");
	    out.println("</b> ");
	    out.println(sessions.size());
	    out.println("&nbsp;&nbsp;<label>Date:</label> ");
	    out.println(pr.getReceived());
	    out.println("</td></tr>");
	}
	out.println("</table></td></tr>");
	//
	// session table
	out.println("<tr bgcolor=\"#CDC9A3\">");		
	out.println("<td><table width=\"100%\">");
	out.println("<caption>Session Fields Selection </caption>");		
	out.println("<tr><td align=\"center\" colspan=\"4\">"+
		    "Check all the fields needed for this program sessions");
	out.println("</td></tr>");
	//
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"days_c\" value=\"y\" "+days_c+
		    " id=\"days_c\" /><label for=\"days_c\">Day(s)</label>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"startTime_c\" value=\"y\" "+
		    startTime_c+" id=\"starttime_c\"/><label for=\"start_c\">Start, End Time</label>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"startDate_c\" value=\"y\" "+
		    startDate_c+" id=\"startDate_c\"/><label for=\"startDate_c\">Start, End Date</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"inCityFee_c\" value=\"y\" "+
		    inCityFee_c + " id=\"incity_c\"/><label for=\"Incity_c\">In-City Fee</label>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"nonCityFee_c\" value=\"y\" "+
		    nonCityFee_c + " id=\"noncity_c\"/><label for=\"noncity_c\">Non-City Fee</label>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"otherFee_c\" value=\"y\" "+
		    otherFee_c + " id=\"otherfee_c\"/><label for=\"otherfee_c\">Other Fee</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"location_c\" value=\"y\" "+
		    location_c + " id=\"loc_c\"/><label for=\"loc_c\">Location</label>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"ageFrom_c\" value=\"y\" "+
		    ageFrom_c + " id=\"agefrom_c\"/><label for=\"agefrom_c\">Participant Age Range</label>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"partGrade_c\" value=\"y\" "+
		    partGrade_c + " id=\"grade_c\" /><label for=\"grade_c\">Participant Grade</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"classCount_c\" value=\"y\" "+
		    classCount_c + " id=\"classcnt_c\"/><label for=\"classcnt_c\"># of classes</label>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"minMaxEnroll_c\" value=\"y\" "+
		    minMaxEnroll_c + " id=\"minmax_c\"/><label for=\"minmax_c\">Min-Max Enrollment</label>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"regDeadLine_c\" value=\"y\" "+
		    regDeadLine_c + " id=\"regdead_c\"/><label for=\"regdead_c\">Registration Deadline</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"description_c\" value=\"y\" "+
		    description_c + " id=\"desc_c\"/><label for=\"desc_c\">Description</label>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"instructor_c\" value=\"y\" "+
		    instructor_c+" id=\"instr_c\"/><label for=\"instr_c\">Instructor</label>");
	out.println("</td><td>");
	out.println("<input type=\"checkbox\" name=\"memberFee_c\" value=\"y\" "+
		    memberFee_c+" id=\"memfee_c\"/><label for=\"memfee_c\">Member Fee</label>");		
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"nonMemberFee_c\" value=\"y\" "+
		    nonMemberFee_c+" id=\"nonmem_c\"/><label for=\"nonmem_c\">Non Member Fee</label>");		
	out.println("</td><td>&nbsp;");
	out.println("</td><td>&nbsp;");
	out.println("</td></tr>");		
	out.println("<tr><td align=\"right\"><label for=\"sortby\">Sort Sessions by:</label></td><td colspan=2>");
	out.println("<select name=\"sessionSort\" id=\"sortby\">");
	for(int i=0;i<Helper.sessionSortOpt.length;i++){
	    if(sessionSort.equals(Helper.sessionSortOpt[i]))
		out.println("<option selected value=\""+
			    Helper.sessionSortOpt[i]+"\">"+
			    Helper.sessionSortArr[i]);
	    else
		out.println("<option value=\""+
			    Helper.sessionSortOpt[i]+"\">"+
			    Helper.sessionSortArr[i]);
	}
	out.println("</select></td></tr>");
	out.println("</table>");
	out.println("</td></tr></table></td></tr>");
	//
	//}
	out.println("<tr bgcolor=#CDC9A3><td align=\"center\">");
	if(id.equals("")){
	    out.println("<table width=\"50%\">");
	    out.println("<caption>Actions</caption>");
	    if(user.canEdit()){
		out.println("<tr><td align=\"right\"><input type=\"submit\" "+
			    "name=\"action\" value=\"Save\">"+
			    "</td><td align=\"right\">" +
			    "<input type=\"reset\" value=\"Clear\">"+
			    "</td></tr>");
	    }
	    out.println("</table>");
	}
	else{ // Update add zoom
	    // 
	    // Buttons table
	    out.println("<table width=\"90%\">");
	    out.println("<caption>Actions</caption>");
	    //
	    // first row buttons
	    int jj=1;
	    out.println("<tr>");
	    if(user.canEdit()){
		out.println("<td align=\"center\" valign=\"top\">");
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Update\" /> "+
			    "</td>");
		jj++;
	    }
	    //
	    if(user.canDelete()){
		out.println("<td align=\"center\" valign=\"top\">");
		out.println("<input type=\"button\" value=\"Delete\""+
			    " onclick=\"validateDelete();\" />");
		out.println("</td>");
		jj++;
	    }
	    if(jj > 3){
		jj=1;
		out.println("</tr>");
		out.println("<tr>");
	    }
	    out.println("<td align=\"center\" valign=\"top\">");
	    out.println("<input type=\"button\" value=\"Sessions\""+
			" onclick=\"document.location='"+url+
			"Sessions?action=Sessions&id="+pr.getId()+
			"';\" /></td>");			
	    jj++;
	    if(jj > 4){
		jj=1;
		out.println("</tr>");
		out.println("<tr>");
	    }
	    //
	    if(pr.hasOldMarket()){
		out.println("<td valign=\"top\" align=\"center\">");
		out.println("<input type=\"button\" value=\"Old Marketing\""+
			    " onclick=\"document.location='"+url+
			    "MarketOld.do?id="+pr.getId()+
			    "';\" /></td>");
		jj++;
            }
	    if(jj > 4){
		jj=1;
		out.println("</tr>");
		out.println("<tr>");
	    }
	    out.println("<td valign=\"top\" align=\"center\">");
	    out.println("<input type=\"button\" value=\"Marketing\""+
			" onclick=\"document.location='"+url+
			"Market.do?prog_id="+pr.getId()+
			"';\" /></td>");
	    jj++;			
	    if(jj > 4){
		jj=1;
		out.println("</tr>");
		out.println("<tr>");
	    }
	    if(pr.hasPlan()){
		out.println("<td align=\"center\" valign=\"top\">");				
		out.println("<input type=\"button\" value=\"Evaluation\""+
			    " onclick=\"document.location='"+url+
			    "Evaluation.do?id="+pr.getId()+
			    "';\" /></td>");
		jj++;
                if(jj > 4){
		    jj=1;
		    out.println("</tr>");
		    out.println("<tr>");
		}
	    }
	    out.println("<td align=\"center\" valign=\"top\">");
	    out.println("<input type=\"button\" value=\"Inclusive Recreation\" "+
			" onclick=\"document.location='"+url+
			"Inclusive?id="+pr.getId()+ "';\" /></td>");
	    jj++;
	    if(jj > 4){
		jj=1;
		out.println("</tr>");
		out.println("<tr>");
	    }
	    out.println("<td align=\"center\" valign=\"top\">");
	    out.println("<input type=\"button\" value=\"Volunteer\" "+
			" onclick=\"document.location='"+url+
			"VolShift.do?pid="+pr.getId()+
			"';\" /></td>");
	    jj++;
	    if(jj > 4){
		jj=1;
		out.println("</tr>");
		out.println("<tr>");
	    }
	    out.println("<td align=\"center\" valign=\"top\">");
	    out.println("<input type=\"button\" value=\"New Media Request\" "+
			" onclick=\"document.location='"+url+
			"MediaRequest?program_id="+pr.getId()+
			"';\" /></td>");
	    jj++;
	    if(jj > 4){
		jj=1;
		out.println("</tr>");
		out.println("<tr>");
	    }
	    out.println("<td align=\"center\" valign=\"top\">");
	    out.println("<input type=\"button\" value=\"Add Notes\" "+
			" onclick=\"document.location='"+url+
			"ProgramNote?program_id="+pr.getId()+
			"';\" /></td>");	    	    
	    if(marketFound || pr.hasOldMarket()){
		jj++;
		if(jj > 4){
		    jj=1;
		    out.println("</tr>");
		    out.println("<tr>");
		}								
		out.println("<td valign=\"top\" align=\"center\">");
		out.println("<input type=\"button\" value=\"Sponsorship\" "+
			    " onclick=\"document.location='"+url+
			    "Sponsor.do?pid="+pr.getId()+ 
			    "';\" /></td>");
	    }
	    else{
		jj++;
		if(jj > 4){
		    jj=1;
		    out.println("</tr>");
		    out.println("<tr>");
		}
		out.println("<td valign=\"top\" align=\"center\">");		
		out.println("<input type=\"button\" value=\"Sponsorship\" "+
			    " onclick=\"window.alert('You need to finish marketing first')\"></input></td>");
	    }
	    
	    if(user.isAdmin()){
		jj++;
		if(jj > 4){
		    jj=1;
		    out.println("</tr>");
		    out.println("<tr>");
		}										 
		out.println("<td align=\"center\" valign=\"top\">");
		out.println("<input type=\"button\" value=\"Add Attachments\""+
			    " onclick=\"document.location='"+url+
			    "PromtFile.do?type=Program&related_id="+pr.getId()+
			    "';\" /></td>");
		if(pr.canChangeYear()){
		    jj++;
		    if(jj > 4){
			jj=1;
			out.println("</tr>");
			out.println("<tr>");
		    }
		    out.println("<td align=\"center\" valign=\"top\">");
		    out.println("<input type=\"button\" value=\"Change Year Season\""+
				" onclick=\"document.location='"+url+"UpdateProgram.do?id="+pr.getId()+"';\" /></td>");																						 
		}
	    }
	    if(sessions != null && sessions.size() > 1){
		jj++;
		if(jj > 4){
		    jj=1;
		    out.println("</tr>");
		    out.println("<tr>");
		}
		out.println("<td align=\"center\" valign=\"top\">");
		out.println("<input type=\"button\" value=\"Reorder Sessions\" onclick=\"document.location='"+url+"SessionReorder.do?prog_id="+pr.getId()+
			    "';\" /></td>");

	    }
	    out.println("</tr></table>");
        }
	out.println("</td></tr>");
	if(!star.equals("")){
	    out.println("<tr><td>"+star+" if you need to add new lead, location, guide heading or area, please ask one of your promt admins to assist you.</td></tr>");
	}

	out.println("</table>");
	out.println("</form>");
	// 
	// Listing sessions (if any)
	//
	if(sessions != null && sessions.size() > 0){
	    Helper.writeSessions(out, sessions, sopt, 
				 id, url, debug);
	}
	if(pr.hasMediaRequests()){
	    Helper.writeMediaRequests(out,"Media Requests", pr.getMediaRequests(), url);
	}
        if(pr.hasFiles()){
	    Helper.printFiles(out, url, pr.getFiles());
        }
	if(pr.hasProgramNotes()){
	    Helper.writeProgramNotes(out, "Program Notes", pr.getProgramNotes(), url); 
	}	
	if(pr.hasHistory()){
	    Helper.writeHistory(out, "Program Logs", pr.getHistory()); 
	}
	//
	Helper.writeWebFooter(out, url);
	//
	out.println("<script>");
	out.println("let hasUnsavedChanges = false;");
	out.println("let hasSubmitClicked = false;");	
	// Example for a form:
	out.println("const myForm = document.getElementById('form_id');");
	out.println("if (myForm) { ");
        out.println(" myForm.addEventListener('input', () => { ");
	out.println(" hasUnsavedChanges = true; ");
	out.println("});");
        out.println(" myForm.addEventListener('submit', () => { ");
	out.println(" hasSubmitClicked = true; ");
	out.println("});");	
	out.println("} ");
	out.println("window.addEventListener('beforeunload', (event) => { ");
        out.println("if (hasUnsavedChanges) { ");
	out.println("if (!hasSubmitClicked){ ");
	// Standard way to prompt the user
        out.println("    event.preventDefault();"); // For modern browsers
	out.println("event.returnValue = 'You have unsaved changes. Are you sure you want to leave?';"); // For older browsers
	out.println("   return 'You have unsaved changes. Are you sure you want to leave?';"); // For some browsers
	out.println("}");
	out.println("}");	
	out.println("});");
	out.println("</script>");	
	out.print("</body></html>");
	out.flush();
	out.close();
    }

}























































