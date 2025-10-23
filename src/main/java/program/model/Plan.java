package program.model;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;

public class Plan extends CommonInc{

    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static Logger logger = LogManager.getLogger(Plan.class);
    String id="", name =""; 
    // other fields will be added later
    final static String life_cycle_options[] ={"Intro","Growth","Maturity","Saturation","Decline"}; 
    String brStatement="", facility="", schedule="", program_date="", program_title="";
    String closings="", other="", message = "", finalMessage="";
    String stat="", stat2="",stat3="",stat4="",stat5="",
	shedule2="",statement="",schedule2="", lead_id="",p_duration="";
    String ideas="",profit_obj="",
	partner="",market="",frequency="",min_max="",event_time="",
	est_time="",year_season="",history="",supply="",timeline="",
	goals="";
    String life_cycle = "";
    String sponsor="", attendCount="";
    //
    String season = "", program_year=""; // for preplan
    //
    Program lastProgram = null;
    PrePlan prePlan = null;
    List<Staff> staffs = null;
    List<Objective> objectives = null;
    List<Contact> instructors = null;
    List<Objective> addObjectives = null;
    List<Objective> deleteObjectives = null;	
    List<Staff> addStaffs = null;
    List<PromtFile> files = null;
    String[] delStaff = null;
    Contact instructor = null;
    String[] addStaffTypes ={"","",""}; // three at a time
    String[] addStaffQuantity ={"","",""};
    String[] deleteInstructors = null;
    HistoryList historys = null;
    Lead lead = null;
    boolean isNew = true, planChecked = false;
    //
    public Plan(){
	super();
    }	
    public Plan(boolean deb, String val, String val2){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setProgram_title(val2);
    }
    public Plan(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
    public Plan(boolean deb){
	//
	// initialize
	//
	super(deb);
    }	
	
    //
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof Type){
	    match = id.equals(((Plan)gg).id);
	}
	return match;
    }
    public int hashCode(){
	int code = 0;
	try{
	    code = Integer.parseInt(id);
	}catch(Exception ex){};
	return code;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getProgram_title(){
	return program_title; // name
    }
    public String getProgram_date(){
	return program_date; // name
    }    
    public String getProfit_obj(){
	return profit_obj;
    }
    public String getPartner(){
	return partner;
    }
    public String getMarket(){
	return market;
    }
    public String getIdeas(){
	return ideas;
    }	
    public String getFrequency(){
	return frequency;
    }
    public String getMin_max(){
	return min_max;
    }
    public String getEvent_time(){
	return event_time;
    }
    public String getEst_time(){
	return est_time;
    }
    public String getYear_season(){
	String ret = "";
	if(program_year.isEmpty() && season.isEmpty()){
	    if(hasPrePlan()){
		program_year = prePlan.getYear();
		season = prePlan.getSeason();
	    }
	}
	ret =  program_year+"/"+season;
	return ret;
    }
    public String getProgram_year(){
	if(program_year.equals("")){
	    if(hasPrePlan()){
		program_year =  prePlan.getYear();
	    }
	}
	return program_year;
    }
    public String getSeason(){
	if(season.equals("")){
	    if(hasPrePlan()){
		season =  prePlan.getSeason();
	    }
	}
	return season;
    }				
    public String getHistory(){
	return history;
    }
    public String getSupply(){
	return supply;
    }
    public String getTimeline(){
	return timeline;
    }
    public String getLead_id(){
	return lead_id;
    }
    public String getSponsor(){
	return sponsor;
    }
    
    public String getP_duration(){
	return p_duration;
    }
    public String getGoals(){
	return goals;
    }
    public String getAttendCount(){
	return attendCount;
    }
    public String getLife_cycle(){
	return life_cycle;
    }	
    public String toString(){
	String ret = program_title;
	if(!program_year.isEmpty() || !season.isEmpty()){
	    if(!program_year.isEmpty()){
		if(!ret.isEmpty()) ret += " ";	    
		ret += program_year;
	    }
	    if(!season.isEmpty()){
		if(!ret.isEmpty()) ret += "/";
		ret += season;
	    }
	}
	else{
	    if(hasPrePlan()){
		ret += "/"+prePlan.getYear()+"/"+prePlan.getSeason();
	    }
	}
	return ret;
    }
	
    public PrePlan getPrePlan(){
	if(prePlan == null && !id.equals("")){
	    PrePlan one = new PrePlan(debug, id);
	    String back = one.doSelect();
	    if(back.equals("")){
		prePlan = one;
	    }
	}
	return prePlan;
    }
    public boolean hasPrePlan(){
	if(!id.equals("") && prePlan == null){
	    getPrePlan();
	}
	return prePlan != null;
    }
    public List<Objective> getObjectives(){
	if(objectives == null && !id.equals("")){
	    ObjectiveList ones = new ObjectiveList(debug, id);
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		objectives = ones;
	    }
	}
	return objectives;
    }
    public List<Staff> getStaffs(){
	if(staffs == null && !id.equals("")){
	    StaffList ones = new StaffList(debug, id);
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		staffs = ones;
	    }
	}
	return staffs;
    }
    public List<Contact> getInstructors(){
	if(instructors == null && !id.equals("")){
	    ContactList cl = new ContactList(debug, id);
	    String back = cl.find();
	    List<Contact> ones = cl.getContacts();
	    if(ones != null && ones.size() > 0){
		instructors = ones;
	    }
	}
	return instructors;
    }

    public void addStaffType(String name, String val){
	if(val.equals("")) return;
	String str = name.substring(name.indexOf("_")+1);
	int ind = -1;
	try{
	    ind = Integer.parseInt(str);
	    addStaffTypes[ind] = val;
	}catch(Exception ex){
	    System.err.println(ex);
	}
    }
    public void addStaffQuantity(String name, String val){
	if(val.trim().equals("")) return;
	String str = name.substring(name.indexOf("_")+1);
	int ind = -1;
	try{
	    ind = Integer.parseInt(str);
	    addStaffQuantity[ind] = val;
	}catch(Exception ex){
	    System.err.println(ex);
	}
    }
    public boolean hasInstructor(){
	getInstructors();
	if(instructors != null){
	    return instructors.contains(instructor);
	}
	return false;
    }
    public boolean hasInstructors(){
	getInstructors();
	return instructors != null;
    }
    public Lead getLead(){
	if(lead == null && !lead_id.equals("")){
	    Lead one = new Lead(debug, lead_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		lead = one;
	    }
	}
	return lead;
    }		
    //
    // all plans will have one instructor
    // but this could change
    //
    public Contact getInstructor(){
	if(instructors == null){
	    getInstructors();
	}
	if(instructors != null){
	    instructor = instructors.get(0); // the first
	}
	else{
	    instructor = new Contact(debug);
	}
	return instructor;
    }
    public void setInstructor(Contact one){
	if(one != null)
	    instructor = one;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val.trim();
    }
    public void setSeason(String val){
	if(val != null)
	    season = val;
    }
    public void setProgram_year(String val){
	if(val != null)
	    program_year = val;
    }
    public void setLife_cycle(String val){
	if(val != null)
	    life_cycle = val;
    }	
    public void setProgram_title(String val){
	if(val != null)
	    program_title = val.trim();
    }
    public void setProgram_date(String val){
	if(val != null)
	    program_date = val.trim();
    }    
    public void setIdeas(String val){
	if(val != null)
	    ideas = val.trim();
    }
    public void setProfit_obj(String val){
	if(val != null)
	    profit_obj = val.trim();
    }
    public void setPartner(String val){
	if(val != null)
	    partner = val.trim();
    }
    public void setMarket(String val){
	if(val != null)
	    market = val.trim();
    }
    public void setFrequency(String val){
	if(val != null)
	    frequency = val.trim();
    }
    public void setMin_max(String val){
	if(val != null)
	    min_max = val.trim();
    }
    public void setEvent_time(String val){
	if(val != null)
	    event_time = val.trim();
    }
    public void setEst_time(String val){
	if(val != null)
	    est_time = val.trim();
    }
    public void setYear_season(String val){
	if(val != null)
	    year_season = val.trim();
    }
    public void setHistory(String val){
	if(val != null)
	    history = val.trim();
    }
    public void setSupply(String val){
	if(val != null)
	    supply = val.trim();
    }
    public void setTimeline(String val){
	if(val != null)
	    timeline = val.trim();
    }
    public void setLead_id(String val){
	if(val != null)
	    lead_id = val;
    }
    public void setSponsor(String val){
	if(val != null)
	    sponsor = val.trim();
    }
    public void setP_duration(String val){
	if(val != null)
	    p_duration = val.trim();
    }
    public void setGoals(String val){
	if(val != null)
	    goals = val.trim();
    }
    public void setAttendCount(String val){
	if(val != null)
	    attendCount = val.trim();
    }
    public void setPrePlan(PrePlan val){
	if(val != null)
	    prePlan = val;
    }
    public void addObjectives(String[] vals){
	if(vals != null){
	    if(addObjectives == null){
		addObjectives = new ArrayList<Objective>();
	    }
	    for(String str: vals){
		if(str != null && !str.trim().equals("")){
		    Objective one = new Objective(debug, null, id, str);
		    addObjectives.add(one);
		}
	    }
	}
    }
    public void deleteObjectives(String[] vals){
	if(vals != null){
	    if(deleteObjectives == null){
		deleteObjectives = new ArrayList<Objective>();
	    }
	    for(String str: vals){
		if(str != null && !str.trim().equals("")){
		    Objective one = new Objective(debug, str);
		    deleteObjectives.add(one);
		}
	    }
	}
    }
    public void setDeleteInstructors(String[] vals){
	if(vals != null)
	    deleteInstructors = vals;
    }
    public void setDelStaff(String[] vals){
	if(vals != null){
	    delStaff = vals;
	}
    }	
    /**
     * if this plan has a program linked to it, no more programs are
     * allowed, need to create a new plan and add new program to it
     */
    public boolean hasProgram(){
	if(lastProgram == null){
	    lastProgram = getLastProgram();
	}
	return lastProgram != null;
    }
    public Program getLastProgram(){
	if(lastProgram == null && !id.equals("")){
	    ProgramList pl = new ProgramList(debug);
	    pl.setPlan_id(id);
	    pl.setSortby("id Desc");
	    pl.find();
	    if(pl.size() > 0){
		lastProgram = pl.get(0);
	    }
	}
	return lastProgram;
    }
    /**
     * TODO
     */
    /**
    public String doDuplicate(){
	String back = "";
	String old_id = id;
	prePlan = new PrePlan(debug); 
	prePlan.setPrev_id(old_id); 
	prePlan.setSeason(season);
	prePlan.setYear(year);
	prePlan.setName(name);
	prePlan.setLead_id(lead_id);				
	back = prePlan.doSave();
	if(back.equals("")){
	    id = prePlan.getId();
	    back = doSave();
	}
	return back;
    }
    */
    public boolean hasHistory(){
	getHistorys();
	return historys != null;
    }
    public List<History> getHistorys(){
	if(historys == null && !id.equals("")){
	    HistoryList ones = new HistoryList(debug, id, "Plan");
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		historys = ones;
	    }
	}
	return historys;
    }
    public boolean hasFiles(){
	getFiles();
	return files != null && files.size() > 0;
    }		
    public List<PromtFile> getFiles(){
	if(files == null && !id.equals("")){
	    PromtFileList tsl = new PromtFileList(debug, id, "Plan");
	    String back = tsl.find();
	    if(back.equals("")){
		List<PromtFile> ones = tsl.getFiles();
		if(ones != null && ones.size() > 0){
		    files = ones;
		}
	    }
	}
	return files;
    }				
    //
    // check if this is a brand new record
    //
    public boolean isNew(){
	//
	if(planChecked){
	    return isNew;
	}
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;				
	String qq = " select id from plans where id=? ";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return isNew;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		isNew = false;
	    }
	    planChecked = true;
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(qq);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return isNew;
    }
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	if(program_title.equals("")){
	    back = "Program title not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	if(program_year.equals("")){
	    back = "Program year not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	if(season.equals("")){
	    back = "Program season not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}	
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "insert into plans "+
	    " values(0,"+
	    "?,?,?,?,?, ?,?,?,?,?, ?,"+
	    "?,?,?,?,?, ?,?,?,?,?)";
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, program_year); // 1
	    pstmt.setString(2, season);
	    pstmt.setString(3, program_title); // 3
	    if(ideas.equals(""))
		pstmt.setNull(4,Types.VARCHAR);
	    else
		pstmt.setString(4,ideas); // 6
	    if(profit_obj.equals(""))
		pstmt.setNull(5,Types.VARCHAR);
	    else
		pstmt.setString(5,profit_obj); //7
	    if(partner.equals(""))
		pstmt.setNull(6,Types.VARCHAR);
	    else
		pstmt.setString(6,partner); //8
	    if(market.equals(""))
		pstmt.setNull(7,Types.VARCHAR);
	    else
		pstmt.setString(7,market); //12
	    if(frequency.equals(""))
		pstmt.setNull(8,Types.VARCHAR);
	    else
		pstmt.setString(8,frequency); //13
	    if(min_max.equals(""))
		pstmt.setNull(9,Types.VARCHAR);
	    else
		pstmt.setString(9,min_max); //14
	    if(event_time.equals(""))
		pstmt.setNull(10,Types.VARCHAR);
	    else
		pstmt.setString(10,event_time); // 15
	    if(est_time.equals(""))
		pstmt.setNull(11,Types.VARCHAR);
	    else
		pstmt.setString(11,est_time); // 16
	    if(history.equals(""))
		pstmt.setNull(12,Types.VARCHAR);
	    else
		pstmt.setString(12,history); //17
	    if(supply.equals(""))
		pstmt.setNull(13,Types.VARCHAR);
	    else
		pstmt.setString(13,supply); //18
	    if(timeline.equals(""))
		pstmt.setNull(14,Types.VARCHAR);
	    else
		pstmt.setString(14,timeline); //19
	    if(lead_id.equals(""))
		pstmt.setNull(15,Types.INTEGER);
	    else
		pstmt.setString(15,lead_id); //10
	    if(sponsor.equals(""))
		pstmt.setNull(16,Types.VARCHAR);
	    else
		pstmt.setString(16,sponsor); //9
	    if(p_duration.equals(""))
		pstmt.setNull(17,Types.VARCHAR);
	    else
		pstmt.setString(17,p_duration); //11
	    if(goals.equals(""))
		pstmt.setNull(18, Types.VARCHAR);
	    else
		pstmt.setString(18, goals); // 4
	    if(attendCount.equals(""))
		pstmt.setNull(19,Types.VARCHAR);
	    else
		pstmt.setString(19,attendCount); //5
	    if(life_cycle.equals(""))
		pstmt.setNull(20,Types.VARCHAR);
	    else
		pstmt.setString(20,life_cycle); //20
	    if(program_date.equals(""))
		pstmt.setString(21,null);
	    else
		pstmt.setDate(21,new java.sql.Date(dateFormat.parse(program_date).getTime())); // 21	    
	    pstmt.executeUpdate();
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }	    
	    doAddsAndUpdates();
	    message="Saved Successfully";
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(qq);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}		
	return back;
    }
    public String doUpdate(){
		
	String back = "";
	if(program_title.equals("")){
	    back = "program title not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	if(program_year.equals("")){
	    back = "Program year not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	if(season.equals("")){
	    back = "Program season not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}	
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "update plans set program_year=?,season=?,"+
	    "program_title=?,"+
	    "ideas=?,"+
	    "profit_obj=?,"+
	    "partner=?,"+
	    "market=?,"+
	    "frequency=?,"+
	    "min_max=?,"+
	    "event_time=?,"+
	    
	    "est_time=?,"+
	    "history=?,"+
	    "supply=?,"+
	    "timeline=?,"+
	    "lead_id=?,"+ 
	    "sponsor=?,"+
	    "p_duration=?,"+
	    "goals=?,"+
	    "attendCount=?,"+ 
	    "life_cycle=?,"+
	    
	    "program_date=? "+
	    "where id = ? ";
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    //
	    pstmt.setString(1, program_year); // 1
	    pstmt.setString(2, season);
	    pstmt.setString(3, program_title); // 3
	    if(ideas.equals(""))
		pstmt.setNull(4,Types.VARCHAR);
	    else
		pstmt.setString(4,ideas); // 6
	    if(profit_obj.equals(""))
		pstmt.setNull(5,Types.VARCHAR);
	    else
		pstmt.setString(5,profit_obj); //7
	    if(partner.equals(""))
		pstmt.setNull(6,Types.VARCHAR);
	    else
		pstmt.setString(6,partner); //8
	    if(market.equals(""))
		pstmt.setNull(7,Types.VARCHAR);
	    else
		pstmt.setString(7,market); //12
	    if(frequency.equals(""))
		pstmt.setNull(8,Types.VARCHAR);
	    else
		pstmt.setString(8,frequency); //13
	    if(min_max.equals(""))
		pstmt.setNull(9,Types.VARCHAR);
	    else
		pstmt.setString(9,min_max); //14
	    if(event_time.equals(""))
		pstmt.setNull(10,Types.VARCHAR);
	    else
		pstmt.setString(10,event_time); // 15
	    if(est_time.equals(""))
		pstmt.setNull(11,Types.VARCHAR);
	    else
		pstmt.setString(11,est_time); // 16
	    if(history.equals(""))
		pstmt.setNull(12,Types.VARCHAR);
	    else
		pstmt.setString(12,history); //17
	    if(supply.equals(""))
		pstmt.setNull(13,Types.VARCHAR);
	    else
		pstmt.setString(13,supply); //18
	    if(timeline.equals(""))
		pstmt.setNull(14,Types.VARCHAR);
	    else
		pstmt.setString(14,timeline); //19
	    if(lead_id.equals(""))
		pstmt.setNull(15,Types.VARCHAR);
	    else
		pstmt.setString(15,lead_id); //10
	    if(sponsor.equals(""))
		pstmt.setNull(16,Types.VARCHAR);
	    else
		pstmt.setString(16,sponsor); //9
	    if(p_duration.equals(""))
		pstmt.setNull(17,Types.VARCHAR);
	    else
		pstmt.setString(17,p_duration); //11
	    if(goals.equals(""))
		pstmt.setNull(18, Types.VARCHAR);
	    else
		pstmt.setString(18, goals); // 4
	    if(attendCount.equals(""))
		pstmt.setNull(19,Types.VARCHAR);
	    else
		pstmt.setString(19,attendCount); //5
	    if(life_cycle.equals(""))
		pstmt.setNull(20,Types.VARCHAR);
	    else
		pstmt.setString(20,life_cycle); //20
	    if(program_date.equals(""))
		pstmt.setString(21,null);
	    else
		pstmt.setDate(21,new java.sql.Date(dateFormat.parse(program_date).getTime())); // 21	    
	    pstmt.setString(22, id);
	    pstmt.executeUpdate();
	    doAddsAndUpdates();
	    message="Updated Successfully";
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(qq);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;

    }
    String setParams(PreparedStatement pstmt, int jj){
	String back = "";
	try{
	    pstmt.setString(1, program_year); // 1
	    pstmt.setString(2, season);
	    pstmt.setString(3, program_title); // 3
	    if(ideas.equals(""))
		pstmt.setNull(4,Types.VARCHAR);
	    else
		pstmt.setString(4,ideas); // 6
	    if(profit_obj.equals(""))
		pstmt.setNull(5,Types.VARCHAR);
	    else
		pstmt.setString(5,profit_obj); //7
	    if(partner.equals(""))
		pstmt.setNull(6,Types.VARCHAR);
	    else
		pstmt.setString(6,partner); //8
	    if(market.equals(""))
		pstmt.setNull(7,Types.VARCHAR);
	    else
		pstmt.setString(7,market); //12
	    if(frequency.equals(""))
		pstmt.setNull(8,Types.VARCHAR);
	    else
		pstmt.setString(8,frequency); //13
	    if(min_max.equals(""))
		pstmt.setNull(9,Types.VARCHAR);
	    else
		pstmt.setString(9,min_max); //14
	    if(event_time.equals(""))
		pstmt.setNull(10,Types.VARCHAR);
	    else
		pstmt.setString(10,event_time); // 15
	    if(est_time.equals(""))
		pstmt.setNull(11,Types.VARCHAR);
	    else
		pstmt.setString(11,est_time); // 16
	    if(history.equals(""))
		pstmt.setNull(12,Types.VARCHAR);
	    else
		pstmt.setString(12,history); //17
	    if(supply.equals(""))
		pstmt.setNull(13,Types.VARCHAR);
	    else
		pstmt.setString(13,supply); //18
	    if(timeline.equals(""))
		pstmt.setNull(14,Types.VARCHAR);
	    else
		pstmt.setString(14,timeline); //19
	    if(lead_id.equals(""))
		pstmt.setNull(15,Types.VARCHAR);
	    else
		pstmt.setString(15,lead_id); //10
	    if(sponsor.equals(""))
		pstmt.setNull(16,Types.VARCHAR);
	    else
		pstmt.setString(16,sponsor); //9
	    if(p_duration.equals(""))
		pstmt.setNull(17,Types.VARCHAR);
	    else
		pstmt.setString(17,p_duration); //11
	    if(goals.equals(""))
		pstmt.setNull(18, Types.VARCHAR);
	    else
		pstmt.setString(18, goals); // 4
	    if(attendCount.equals(""))
		pstmt.setNull(19,Types.VARCHAR);
	    else
		pstmt.setString(19,attendCount); //5
	    if(life_cycle.equals(""))
		pstmt.setNull(20,Types.VARCHAR);
	    else
		pstmt.setString(20,life_cycle); //20
	    if(program_date.equals(""))
		pstmt.setString(21,null);
	    else
		pstmt.setDate(21,new java.sql.Date(dateFormat.parse(program_date).getTime())); // 21
	    pstmt.setString(22,null);
	}
	catch(Exception ex){
	    back += ex;
	    addError(back);
	}
	return back;
    }
    public String doDelete(){
	String back = "", qq = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    qq = "delete from plans where id=?";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    message="Deleted Successfully";
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
	
    public String doSelect(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = "select program_year,season,program_title,"+
	    "ideas,profit_obj,"+ // 10
	    "partner,market,frequency,min_max,event_time,est_time,"+
	    "history,"+                                        
	    "supply,"+                    
	    "timeline,"+
	    "sponsor,lead_id,p_duration, "+                     
	    "goals,"+                  
	    " attendCount,life_cycle, "+
	    " date_format(program_date,'%m/%d/%Y') "+ 
	    " from plans where id=? ";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id); // pid
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		String str = rs.getString(1);
		if(str != null) program_year = str; // program
		str = rs.getString(2);
		if(str != null) season = str; // program		
		str = rs.getString(3);
		if(str != null) program_title = str; // program
		str = rs.getString(4);
		if(str != null) ideas = str;
		str = rs.getString(5);
		if(str != null) profit_obj = str;
		str = rs.getString(6);
		if(str != null) partner = str;
		str = rs.getString(7);
		if(str != null) market = str;
		str = rs.getString(8);
		if(str != null) frequency = str;
		str = rs.getString(9);
		if(str != null) min_max = str;
		str = rs.getString(10);
		if(str != null) event_time = str;
		str = rs.getString(11);
		if(str != null) est_time = str;
		str = rs.getString(12);
		if(str != null) history += str;
		str = rs.getString(13);
		if(str != null) supply += str;
		str = rs.getString(14);
		if(str != null) timeline += str;
		str = rs.getString(15);
		if(str != null) sponsor = str;
		str = rs.getString(16);
		if(str != null) lead_id = str;
		str = rs.getString(17);
		if(str != null) p_duration = str;
		str = rs.getString(18);
		if(str != null) goals = str;
		str = rs.getString(19);
		if(str != null && !str.equals("0")) 
		    attendCount = str;
		str = rs.getString(20);
		if(str != null) life_cycle = str;
		str = rs.getString(21);
		if(str != null) program_date = str;		
		message = "";
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);			
	}
	return back;
    }
    public String duplicate(){
	String back = "", season="";
	if(id.equals("")){
	    back = "Plan id not set ";
	}
	String prev_id = id;
	getLastProgram();
	if(lastProgram != null){
	    season = lastProgram.getSeason();
	}				
	PrePlan one = new PrePlan(debug);
	one.setPrev_id(prev_id);
	if(!season.equals(""))
	    one.setSeason(season);
	back = one.doSave();
	id = one.getId();
	back = this.doSave();
	return back;
    }
    //
    public String doSelectPart(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select program_title "+
	    "from plans where id=?";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }				
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setProgram_title(rs.getString(1));
	    }
	    else{
		message= "Record "+id+" not found";
		return message;
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);			
	}
	return back;
    }	
    public String doAddsAndUpdates(){
	String back = "";
	if(addObjectives != null){
	    for(Objective one:addObjectives){
		one.setPlan_id(id);
		back += one.doSave();
	    }
	}
	if(deleteObjectives != null){
	    for(Objective one:deleteObjectives){
		back += one.doDelete();
	    }
	}		
	for(int i=0;i<addStaffTypes.length;i++){
	    if(addStaffTypes[i].equals("") || addStaffQuantity.equals("")) continue;
	    Staff one = new Staff(debug, null, id, addStaffTypes[i],addStaffQuantity[i]);
	    back += one.doSave();
			
	}
	if(instructor != null){
	    if(instructor.hasInfo()){
		back += addInstructorToPlan();
	    }
	}
	if(deleteInstructors != null){
	    back += doDeleteInstructors();
	}
	if(delStaff != null){
	    for(String str:delStaff){
		Staff one = new Staff(debug, str);
		one.doDelete();
	    }
	}
	return back;
    }
    String doDeleteInstructors(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "delete from plan_contacts where plan_id=? and con_id=? ";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }				
	    pstmt = con.prepareStatement(qq);
	    for(String str:deleteInstructors){
		pstmt.setString(1, id);
		pstmt.setString(2, str);
		pstmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);			
	}
	return back;
    }
    String addInstructorToPlan(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into plan_contacts values(?,?) ";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }				
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.setString(2,instructor.getId());
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);			
	}
	return back;
    }
}
/**
   alter table plans add program_year int after id;
   alter table plans add season enum('Fall/Winter','Winter/Spring','Summer','Ongoing') after program_year;
   alter table plans add program_date date;
   ALTER TABLE plans RENAME COLUMN program TO program_title;
   update plans p,pre_plans pre set p.program_year=pre.year,p.season=pre.season where pre.id=p.id;
   update plans p,pre_plans pre set p.program_date=pre.date where pre.id=p.id;
   alter table programs drop constraint programs_ibfk_5;
   alter table plans modify id int auto_increment;
   update programs set regDeadLine=null where regDeadLine like '0000%';
   update programs set regDeadLine=null where regDeadLine like '%-00';   
   alter table programs add foreign key(plan_id) references plans(id);




 */
