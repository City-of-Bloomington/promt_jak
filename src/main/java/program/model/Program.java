package program.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Comparator;
import program.util.*;
import program.list.*;



public class Program implements Comparable <Program>{

    boolean debug = false;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static Logger logger = LogManager.getLogger(Program.class);
    String lead_id="", title="", season="", season2="", year=""; 
    String category_id="", statement="", subcat="", version="";
    String othercat="", category2_id="";
    String stat2="",stat3="",stat4="",stat5="",oginfo2="";
    String nraccount="", fee="", oginfo="", scount="0";
    String action = "", otherlead="",otherlead2="", codeNeed="";
    String area_id = "", otherArea="",plan_id="",pDeadLine="", id="";
    String received=""; // date
    String startDate="", endDate="",oplocation="";
    String marketTask="", volTask="", sponTask="",codeTask="",
	budgetTask="", evalTask="", noPublish="";
    String summary="", taxonomy_ids="", location_details="";
    //
    // fields common in program and session
    // if used in program, should not be used in session
    //
    String inCityFee="", nonCityFee="",otherFee="",
	memberFee="",nonMemberFee="";
    String location_id="", instructor="",regDeadLine="";
    String ageFrom="", ageTo="",partGrade="", description="";
    String minMaxEnroll="",description2="",
	allAge="", // pPartAge
	otherAge=""; // will be used for 18 month toddlers and so on
    String days="",startEndTime="", startTime="", endTime="";
    String classCount="",code="",wParent="",waitList="";
    Type category= null, category2 = null,
	area = null,
	lead=null;
    Location location = null;
    SessionOpt sessionOpt = null;
    SessionList sessions = null;
    Plan plan = null;
    Market market = null;
    List<Sponsor> sponsors = null;
    List<VolShift> shifts = null;
    HistoryList history = null;
    TaxonomySubList taxSubList = null;
    List<PromtFile> files = null;
    List<MediaRequest> mediaRequests = null;
    List<ProgramNote> programNotes = null;
    //
    public Program(boolean val){
	debug = val;
    }
    public Program(boolean deb, String val){
	debug = deb;
	setId(val);
    }
    public Program(boolean deb, String val, String val2, String val3,
	    String val4, String val5){// for short list
	debug = deb;
	setId(val);
	setTitle(val2);
	setCode(val3);
	setSeason(val4);
	setYear(val5);
    }
    public Program(boolean deb, String val, String val2,
	    String val3, String val4,
	    String val5, String val6,
	    String val7, String val8){// for short list
	debug = deb;
	setId(val);
	setTitle(val2);
	setCategory_id(val3);
	if(val4 != null){
	    category = new Type(debug, val3, val4);
	}
	setArea_id(val5);
	if(val6 != null){
	    area = new Type(debug, val5, val6);
	}				
	setLead_id(val7);
	if(val8 != null){
	    lead = new Type(debug, val7, val8);
	}								
    }		
    public Program(boolean deb,
	    String val,
	    String val2,
	    String val3,
	    String val4,
	    String val5,
	    String val6,
	    String val7,
	    String val8,
	    String val9,
	    String val10,
	    String val11,
	    String val12,
	    String val13,
	    String val14,
	    String val15,
	    String val16,
	    String val17,
	    String val18,
	    String val19,
	    String val20,
	    String val21,
	    String val22,
	    String val23,
	    String val24,
	    String val25,
	    String val26,
	    String val27,
	    String val28,
	    String val29,
	    String val30,
	    String val31,
	    String val32,
	    String val33,
	    String val34,
	    String val35,
	    String val36,
	    String val37,
	    String val38,
	    String val39,
	    String val40,
	    String val41,
	    String val42,
	    String val43,
	    String val44,
	    String val45,
	    String val46,
	    String val47,
	    String val48,
	    String val49,
	    String val50,
	    String val51,
	    String val52,
	    String val53,
	    String val54,
	    String val55,
	    String val56,
	    String val57
	    ){// for short list
	debug = deb;
	setId(val);
	setLead_id(val2);
	if(val3 != null){
	    lead =new Type(debug, val2, val3);
	}
	setStatement(val4);
	setTitle(val5);
	setYear(val6);
	setSeason(val7);
	setCategory_id(val8);
	if(val9 != null){
	    category = new Type(debug, val8, val9);
	}
	setNraccount(val10);
	setFee(val11);
	setOginfo(val12);
	setInCityFee(val13);
	setNonCityFee(val14);
	setOtherFee(val15);
	setAllAge(val16);
	setPartGrade(val17);
	setMinMaxEnroll(val18);
	setRegDeadLine(val19);
	setLocation_id(val20);
	if(val21 != null){
	    location = new Location(debug, val20, val21);
	}
	setInstructor(val22);
	setDescription(val23);
	setDays(val24);
	setStartTime(val25);
	setEndTime(val26);
	setClassCount(val27);
	setCode(val28);
	setArea_id(val29);
	if(val30 != null){
	    area = new Type(debug, val29, val30);
	}
	setCodeNeed(val31);
	setStartDate(val32);
	setEndDate(val33);
	setCodeTask(val34);
	setMarketTask(val35);
	setVolTask(val36);
	setSponTask(val37);
	setAgeFrom(val38);
	setAgeTo(val39);
	setBudgetTask(val40);
	setEvalTask(val41);
	setWParent(val42);
	setWaitList(val43);
	setPlan_id(val44);
	setOPLocation(val45);
	setCategory2_id(val46);
	setVersion(val47);
	setOtherAge(val48);
	setSubcat(val49);
	setReceived(val50);
	setMemberFee(val51);
	setNonMemberFee(val52);
	setSeason2(val53);
	setSummary(val54);
	setTaxonomy_ids(val55, true); // we are coming from select
	setLocation_details(val56);
	setNoPublish(val57);				
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setLead_id(String val){
	if(val != null)
	    lead_id = val;
    }
    public void setStatement(String val){
	if(val != null)
	    statement = val;
    }
    public void setSummary(String val){
	if(val != null){
	    if(val.length() > 255){
		summary = val.substring(0,255);
	    }
	    else {
		summary = val;
	    }
	}
    }
    public void setNoPublish(String val){
	if(val != null)
	    noPublish = val;
    }
    public boolean canPublish(){
	return noPublish.equals("");
    }
    public void setLocation_details(String val){
	if(val != null)
	    location_details = val;
    }		
    public void setTaxonomy_ids(String val){
	// System.err.println(" taxo ids "+val);
	if(val != null){
	    if(val.indexOf(":") > 0){ 
		if(val.indexOf(",") > 0){ // more than one
		    String[] strArr = val.split(",");
		    for(String str:strArr){
			if(str.indexOf(":") > 0){
			    String[] strArr2 = str.split(":");
			    for(String str2:strArr2){
				// we ignore main category
				if(str2.length()  > 1){ // sub cat only
				    if(!taxonomy_ids.equals("")) taxonomy_ids +=","; 
				    taxonomy_ids += str2;
				}
			    }
			}
			else{
			    if(!taxonomy_ids.equals("")) taxonomy_ids +=","; 
			    taxonomy_ids += str;
			}
		    }
		}
		else{ // only one
		    String[] strArr = val.split(":");
		    for(String str:strArr){
			// we ignore main category
			if(str.length()  > 1){ // sub cat only
			    if(!taxonomy_ids.equals("")) taxonomy_ids +=","; 
			    taxonomy_ids += str;
			}
		    }
		}
	    }
	    else{ // only one category without sub category
		taxonomy_ids = val; 
	    }
	}
    }
    // we need this when reading from db
    public void setTaxonomy_ids(String val, boolean treat){
	if(val != null)
	    taxonomy_ids = val;
    }				
    public void setTitle(String val){
	if(val != null)
	    title = val;
    }
    public void setYear(String val){
	if(val != null)
	    year = val;
    }
    public void setSeason(String val){
	if(val != null)
	    season = val;
    }
    public void setSeason2(String val){
	if(val != null)
	    season2 = val;
    }	
    public void setAllAge(String val){
	if(val != null)
	    allAge = val;
    }
		
    public void setCategory_id(String val){
	if(val != null)
	    category_id = val;
    }
    public void setNraccount(String val){
	if(val != null)
	    nraccount = val;
    }
    public void setFee(String val){
	if(val != null && !val.equals(""))
	    fee = val;
    }
    public void setOginfo(String val){
	if(val != null)
	    oginfo = val;
    }
    public void setInCityFee(String val){
	if(val != null && !val.equals("0"))
	    inCityFee= val;		  
    }
    public void setNonCityFee(String val){
	if(val != null && !val.equals("0"))
	    nonCityFee = val;
    }
    public void setMemberFee(String val){
	if(val != null && !val.equals("0"))
	    memberFee= val;		  
    }
    public void setNonMemberFee(String val){
	if(val != null && !val.equals("0"))
	    nonMemberFee= val;		  
    }	
    public void setOtherFee(String val){
	if(val != null)
	    otherFee = val;
    }
    public void setPartGrade(String val){
	if(val != null)
	    partGrade = val;
    }
    public void setMinMaxEnroll(String val){
	if(val != null)
	    minMaxEnroll = val;
    }
    public void setRegDeadLine(String val){
	if(val != null)
	    regDeadLine = val;
    }
    public void setInstructor(String val){
	if(val != null)
	    instructor = val;
    }
    public void setDescription(String val){
	if(val != null)
	    description = val;
    }
    public void setDays(String val){
	if(val != null)
	    days = val;
    }
    // we need to take care of old programs that
    // have startEndTime in one field
    public void setStartTime(String val){
	if(val != null){
	    if(val.indexOf("-") > 0){
		splitStartEndTime(val);
	    }
	    else{
		startTime = val;
	    }
	}
    }
    /**
     * input could be like
     * 10:30 a.m.-1:30 p.m
     * 10:30-11:30 am
     * 4:30-5:30 pm
     */
    public void splitStartEndTime(String val){
	if(val.indexOf("-") > 0){
	    String am="a.m.", pm="p.m.";
	    String times[] = val.split("-");
	    if(times.length > 0){
		startTime = times[0];
		endTime = times[1];
		if(startTime.indexOf(am) == -1 && startTime.indexOf(pm)== -1){
		    if(endTime.indexOf(am) > 0){
			startTime += " "+am;
		    }
		    else{
			startTime += " "+pm;
		    }
		}									 
	    }
	}
    }
    public void setEndTime(String val){
	if(val != null)
	    endTime = val;
    }
    public void setClassCount(String val){
	if(val != null)
	    classCount = val;
    }
    public void setCode(String val){
	if(val != null)
	    code = val;
    }
    public void setArea_id(String val){
	if(val != null)
	    area_id = val;
    }
    public void setCodeNeed(String val){
	if(val != null)
	    codeNeed = val;
    }
    public void setStartDate(String val){
	if(val != null)
	    startDate = val;
    }
    public void setEndDate(String val){
	if(val != null)
	    endDate = val;
    }
    public void setCodeTask(String val){
	if(val != null)
	    codeTask = val;
    }
    public void setMarketTask(String val){
	if(val != null)
	    marketTask = val;
    }
    public void setVolTask(String val){
	if(val != null)
	    volTask = val;
    }
    public void setSponTask(String val){
	if(val != null)
	    sponTask = val;
    }
    public void setAgeFrom(String val){
	if(val != null)
	    ageFrom = val;
    }
    public void setAgeTo(String val){
	if(val != null)
	    ageTo = val;
    }
    public void setBudgetTask(String val){
	if(val != null)
	    budgetTask = val;
    }
    public void setEvalTask(String val){
	if(val != null)
	    evalTask = val;
    }
    public void setWParent(String val){
	if(val != null)
	    wParent = val;
    }
    public void setWaitList(String val){
	if(val != null)
	    waitList = val;
    }
    public void setPlan_id(String val){
	if(val != null)
	    plan_id = val;
    }
    public void setOPLocation(String val){
	if(val != null)
	    oplocation = val;
    }
    public void setLocation_id(String val){
	if(val != null)
	    location_id = val;
    }
    public void setCategory2_id(String val){
	if(val != null)
	    category2_id = val;
    }
    public void setVersion(String val){
	if(val != null)
	    version = val;
    }
    public void setOtherAge(String val){
	if(val != null)
	    otherAge = val;
    }
    public void setSubcat(String val){
	if(val != null)
	    subcat = val;
    }
    public void setReceived(String val){
	if(val != null)
	    received = val;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getLead_id(){
	return lead_id;
    }
    public String getStatement(){
	return statement;
    }
    public String getSummary(){
	return summary;
    }
    public String getNoPublish(){
	return noPublish;
    }		
    public String getLocation_details(){
	return location_details;
    }
    public String getLocationName(){
	if(location == null && !location_id.equals("")){
	    getLocation();
	}
	if(location != null){
	    return location.getName();
	}
	return "";
    }		
		
    public String getTaxonomy_ids(){
	return taxonomy_ids;
    }		
    public String getTitle(){
	return title;
    }
    public String getYear(){
	return year;
    }	
    public String getSeason(){
	return season;
    }
    public String getSeason2(){
	return season2;
    }
    public String getSeasons(){
	String ret = season;
	if(!season2.equals("")){
	    if(!ret.equals("")) ret += " - ";
	    ret += season2;
	}
	return ret;
    }	
    public String getAllAge(){
	return allAge;
    }
    public String getCategory_id(){
	return category_id;
    }
    public String getNraccount(){
	return nraccount;
    }
    public String getFee(){
	return fee;
    }
    public String getOginfo(){
	return oginfo;
    }
    public String getInCityFee(){
	return inCityFee;
    }
    public String getNonCityFee(){
	return nonCityFee;
    }
    public String getMemberFee(){
	return memberFee;
    }
    public String getNonMemberFee(){
	return nonMemberFee;
    }	
    public String getOtherFee(){
	return otherFee;
    }
    public String getPartGrade(){
	return partGrade;
    }
    public String getMinMaxEnroll(){
	return minMaxEnroll;
    }
    public String getRegDeadLine(){
	return regDeadLine;
    }
    public String getInstructor(){
	return instructor;
    }
    public String getDescription(){
	return description;
    }
    public String getDays(){
	return days;
    }
    public String getSubcat(){
	return subcat;
    }
    /**
     * we want an output of the foramt
     * 10:30-11:30 pm
     * 10:30-11:30 am
     * 10:30 am-11:30 pm
     */
    public String getStartEndTime(){
	String am="a.m.", pm="p.m.";
	if(startEndTime.equals("")){
	    if(!startTime.equals("")){
		if(endTime.equals("")){
		    startEndTime = startTime;
		}
		else if(startTime.indexOf(am) > 0 && endTime.indexOf(am) >0){
		    startEndTime = startTime.substring(0,startTime.indexOf(am)).trim()+"-"+endTime;
		}
		else if(startTime.indexOf(pm) > 0 && endTime.indexOf(pm) >0){
		    startEndTime = startTime.substring(0,startTime.indexOf(pm)).trim()+"-"+endTime;
		}								
		else{
		    startEndTime = startTime+"-"+endTime;
		}
	    }
	}
	return startEndTime;
    }
    public String getStartTime(){
	return startTime;
    }
    public String getEndTime(){
	return endTime;
    }		
    public String getClassCount(){
	return classCount;
    }
    public String getCode(){
	return code;
    }
    public String getLocation_id(){
	return location_id;
    }
    public String getArea_id(){
	return area_id;
    }
    public String getCodeNeed(){
	return codeNeed;
    }
    public String getStartDate(){
	return startDate;
    }
    public boolean hasPlan(){
	return !plan_id.equals("");
    }
    public String getWaitList(){
	return waitList;
    }
    public String getSponTask(){
	return sponTask;
    }
    public String getEvalTask(){
	return evalTask;
    }
    public String getVolTask(){
	return volTask;
    }
    public String getBudgetTask(){
	return budgetTask;
    }	
    public String getMarketTask(){
	return marketTask;
    }
    public String getEndDate(){
	return endDate;
    }
    public String getAgeFrom(){
	return ageFrom;
    }
    public String getAgeTo(){
	return ageTo;
    }
    public String getReceived(){
	return received;
    }
    public String getOtherAge(){
	return otherAge;
    }
    public String getPlan_id(){
	return plan_id;
    }
    public String getCategory2_id(){
	return category2_id;
    }
    public String getWParent(){
	return wParent;
    }
    public boolean canChangeYear(){
	int yy = Helper.getCurrentYear();
	if(!year.equals("")){
	    try{
		int y2 = Integer.parseInt(year);
		return yy <= y2;
	    }catch(Exception ex){
								
	    }
	}
	return false;
    }
    public String getStartEndDateComplete(){
	String ret = "";
	if(!startDate.equals("")){
	    ret = startDate;
	}
	if(!endDate.equals("")){
	    if(!ret.equals("")) ret += " - ";
	    ret += endDate;
	}
	return ret;
    }
    public String getStartEndDate(){
	String ret = "";
	if(!startDate.equals("")){
	    ret = Helper.getDatePart(startDate,"month")+"/"+Helper.getDatePart(startDate,"day");
	}
	if(!endDate.equals("")){
	    if(!ret.equals("")) ret += " - ";
	    ret += Helper.getDatePart(endDate,"month")+"/"+Helper.getDatePart(endDate,"day");
	}		
	return ret;
    }
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof Program){
	    match = !id.equals("") && id.equals(((Program)gg).id);
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
	
    public int compareTo(Program pp) {
	return title.compareTo(pp.title);
    }
    public List<TaxonomySub> getTaxonomySubList(){
	if(taxSubList == null && !taxonomy_ids.equals("")){
	    TaxonomySubList tsl = new TaxonomySubList(debug);
	    String back = tsl.findFromIds(taxonomy_ids);
	    if(back.equals("") && tsl.size() > 0){
		taxSubList = tsl;
	    }
	}
	return taxSubList;
    }
    public List<PromtFile> getFiles(){
	if(files == null && !id.equals("")){
	    PromtFileList tsl = new PromtFileList(debug, id, "Program");
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
    public boolean hasFiles(){
	getFiles();
	return files != null && files.size() > 0;
    }
    public boolean hasMediaRequests(){
	findMediaRequests();
	return mediaRequests != null && mediaRequests.size() > 0;
    }
    public List<MediaRequest> getMediaRequests(){
	return mediaRequests;
    }
    private String findMediaRequests(){
	String back = "";
	if(!id.isEmpty()){
	    MediaRequestList mdl = new MediaRequestList(debug);
	    mdl.setProgram_id(id);
	    back = mdl.find();
	    if(back.isEmpty()){
		mediaRequests = mdl.getRequests(); 
	    }
	}
	return back;
    }    
    public boolean hasProgramNotes(){
	findProgramNotes();
	return programNotes != null && programNotes.size() > 0;
    }
    public List<ProgramNote> getProgramNotes(){
	return programNotes;
    }
    private String findProgramNotes(){
	String back = "";
	if(!id.isEmpty()){
	    ProgramNoteList mdl = new ProgramNoteList(debug, id);
	    back = mdl.find();
	    if(back.isEmpty() && mdl.hasProgramNotes()){
		programNotes = mdl.getProgramNotes(); 
	    }
	}
	return back;
    }    

    //
    // one string containing all taxonomy info
    //
    public String getTaxonomyInfo(){
	String ret = "";
	if(taxSubList == null){
	    getTaxonomySubList();
	}
	if(taxSubList != null && taxSubList.size() > 0){
	    for(TaxonomySub one:taxSubList){
		if(!ret.equals("")) ret +=", ";
		ret += one.getCompistName();
	    }
	}
	return ret;
    }
    public String getTaskInfo(){
	String ret="";
	if(!codeTask.equals(""))
	    ret += "Code";
	if(!marketTask.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "Marketing";
	}
	if(!volTask.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "Volunteer";
	}
	if(!sponTask.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "Sponsorship";
	}
	if(!budgetTask.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "Budget";
	}
	if(!evalTask.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "Evaluation";
	}
	return ret;
				
    }
    public String getAgeInfo(){
	String ret = "";
	int from = 0, to = 0;
	if(!allAge.equals("")){
	    return "For all ages";
	}
	try{
	    if(!ageFrom.equals("")){
		from = Integer.parseInt(ageFrom);
	    }
	    if(!ageTo.equals("")){
		to = Integer.parseInt(ageTo);
	    }
	}catch(Exception ex){}
	if(from > 0 && to < 100 && to > 0){
	    if(to == 99){
		ret = ""+from +" yrs and up";
	    }
	    else{
		ret = ""+from +" - "+to;
	    }
	}
	else if(from > 0) 
	    ret =""+from+" yrs and up";
	else if(to > 0){
	    ret = to+" yrs and under";
	}
	if(!wParent.equals("")){
	    ret += " w/parent";
	}
	if(!otherAge.equals("")){
	    if(!ret.equals("")) ret += " ";
	    ret += otherAge;
	}
		
	return ret;
    }
    public boolean hasSessions(){
	getSessions();
	return (sessions != null && sessions.size() > 0);
    }
    /**
     * check if this program has an old market data
     */
    public boolean hasOldMarket(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String back="";
	int count = 0;
	if(id.equals("")){
	    logger.error("id not set");
	    return false;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return false;
	}
	String qq = " select count(*) from programs_market where id=? ";
	try{
	    pstmt = con.prepareStatement(qq);			
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return count > 0;
    }
    public Type getCategory(){
	if(category == null){
	    if(!category_id.equals("")){
		Type one = new Type(debug, category_id, null, "categories");
		String back = one.doSelect();
		category = one;
	    }
	    else{
		category = new Type(debug);
	    }
	}
	return category;
    }
    public Type getCategory2(){
	if(category2 == null){
	    if(!category2_id.equals("")){
		Type one = new Type(debug, category2_id, null, "categories");
		String back = one.doSelect();
		category2 = one;
	    }
	    else{
		category2 = new Type(debug);
	    }
	}
	return category2;
    }	
    public Type getArea(){
	if(area == null){
	    if(!area_id.equals("")){
		Type one = new Type(debug, area_id, null, "areas");
		String back = one.doSelect();
		area = one;
	    }
	    else{
		area = new Type(debug);
	    }
	}
	return area;
    }
    public Type getLead(){
	if(lead == null){
	    if(!lead_id.equals("")){
		Type one = new Type(debug, lead_id, null, "leads");
		String back = one.doSelect();
		lead = one;
	    }
	    else{
		lead = new Type(debug);
	    }
	}
	return lead;
    }
    public Location getLocation(){
	//if(location == null){
	if(!location_id.equals("")){
	    Location one = new Location(debug, location_id);
	    String back = one.doSelect();
	    location = one;
	}
	else{
	    location = new Location(debug);
	}
	return location;
    }
    public SessionList getSessions(){
	if(sessions == null && !id.equals("")){
	    SessionList ones = new SessionList(debug, id);
	    String back = ones.lookFor();
	    if(!back.equals("")){
		logger.error(back);
	    }
	    if(ones.size() > 0){
		sessions = ones;
	    }						
	}
	return sessions;
    }		
    public SessionList getSessionsWithDateLimit(String date){
	if(sessions == null && !id.equals("")){
	    SessionList ones = new SessionList(debug, id);
	    if(date != null){
		ones.setEffective_date(date);
	    }
	    String back = ones.lookFor();
	    if(!back.equals("")){
		logger.error(back);
	    }
	    if(ones.size() > 0){
		sessions = ones;
	    }						
	}
	return sessions;
    }
    public SessionList getCurrentSessions(){
	if(sessions == null && !id.equals("")){
	    SessionList ones = new SessionList(debug, id);
	    ones.setCurrentOnly();
	    String back = ones.lookFor();
	    if(!back.equals("")){
		logger.error(back);
	    }
	    if(ones.size() > 0){
		sessions = ones;
	    }
	}
	return sessions;
    }
    public SessionOpt getSessionOpt(){
	if(sessionOpt == null && !id.equals("")){
	    sessionOpt = new SessionOpt(debug, id);
	    sessionOpt.doSelect();
	}
	return sessionOpt;
    }
	
    public Plan getPlan(){
	if(plan == null && !plan_id.equals("")){
	    plan = new Plan(debug, plan_id);
	    String back = plan.doSelect();
	}
	if(plan == null){
	    plan = new Plan(debug);
	}
	return plan;
    }
    public Market getMarket(){
	if(market == null && !id.equals("")){
	    MarketList ml = new MarketList(debug, id, null, null);
	    String back = ml.find();
	    if(back.equals("") && ml.size() > 0){
		market = ml.get(0);
	    }
	}
	return market;
    }
    public boolean hasMarket(){
	getMarket();
	return market != null;
    }
    public boolean hasSponsors(){
	getSponsors();
	return sponsors != null && sponsors.size() > 0;
    }
    public List<Sponsor> getSponsors(){
	if(sponsors == null && !id.equals("")){
	    SponsorList slist = new SponsorList(debug, id);
	    String back = slist.find();
	    if(back.equals("") && slist.size() > 0){
		sponsors = slist;
	    }
	}
	return sponsors;
    }
    public boolean hasShifts(){
	getShifts();
	return shifts != null && shifts.size() > 0;
    }
    public List<VolShift> getShifts(){
	if(shifts == null && !id.equals("")){
	    VolShiftList slist = new VolShiftList(debug, id);
	    String back = slist.find();
	    if(back.equals("") && slist.size() > 0){
		shifts = slist;
	    }
	}
	return shifts;
    }
    public boolean hasHistory(){
	getHistory();
	return history != null;
    }
    public List<History> getHistory(){
	if(history == null && !id.equals("")){
	    HistoryList ones = new HistoryList(debug, id, "Program");
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		history = ones;
	    }
	}
	return history;
    }
    //	
    public String doSave(){

	String qq = "", message="";
	received = Helper.getToday2();
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	if(year.equals("")){
	    message = "year is required";
	}
	if(season.equals("")){
	    message += " season is required";
	}
	if(title.equals("")){
	    message += " program title is required";
	}				
	if(!message.equals("")){
	    return message;
	}
	//
	// all new record will get the current version
	//
	qq = "insert into programs values (0,?,?,?,?,?,"+
	    "?,?,?,?,?,"+ // 11
	    "?,?,?,?,?,"+
	    "?,?,?,?,?,"+ // 21
	    "?,?,?,?,?,"+
	    "?,?,?,?,?,"+ // 31
	    "?,?,?,?,?,"+
	    "?,?,?,?,?,"+ // 41
	    "?,?,?,?,?,"+ // 
	    "?,?,?,?,?, ?,?)"; // 

	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();			
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(lead_id.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,lead_id);
	    if(statement.equals("")) 
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,statement);
	    if(title.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,title);
	    pstmt.setString(j++,year);
	    pstmt.setString(j++,season);
	    if(category_id.equals("")) // should be avoided
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,category_id);
	    if(nraccount.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,nraccount);
	    if(fee.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,fee);
	    if(oginfo.equals("")) // 10
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,oginfo);

	    if(inCityFee.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,inCityFee);
	    if(nonCityFee.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,nonCityFee);
	    if(otherFee.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,otherFee);
	    if(allAge.equals(""))  // replaced pPartAge
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,"y");
	    if(partGrade.equals("")) // 15
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,partGrade);
	    if(minMaxEnroll.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,minMaxEnroll);
	    if(regDeadLine.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(regDeadLine).getTime()));
	    if(location_id.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,location_id);
	    if(instructor.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,instructor);
	    if(description.equals("")) // 20
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,description);
	    if(days.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,days);
	    if(startTime.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,startTime);
	    if(endTime.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,endTime);						
	    if(classCount.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,classCount);
	    if(code.equals("")) // 25
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,code);
	    if(area_id.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,area_id);
	    if(codeNeed.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,codeNeed);
	    if(startDate.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(startDate).getTime()));
	    if(endDate.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(endDate).getTime()));
	    if(codeTask.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,codeTask);
	    if(marketTask.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,marketTask);
	    if(volTask.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,volTask);
	    if(sponTask.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,sponTask);
			
	    if(ageFrom.equals(""))  
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,ageFrom);
	    if(ageTo.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,ageTo);
	    if(budgetTask.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,budgetTask);
	    if(evalTask.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,evalTask);
	    if(wParent.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,"y");
	    if(waitList.equals(""))
		pstmt.setString(j++,"0");
	    else
		pstmt.setString(j++,waitList);
	    if(plan_id.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,plan_id);
	    pstmt.setNull(j++,Types.VARCHAR);
	    if(category2_id.equals("")) // should be avoided
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,category2_id);
	    pstmt.setString(j++,version);
	    if(otherAge.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,otherAge);
	    if(subcat.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,subcat);
	    pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(received).getTime()));
	    if(memberFee.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,memberFee);
	    if(nonMemberFee.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,nonMemberFee);
	    if(season2.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,season2);			
	    if(summary.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,summary);
	    if(taxonomy_ids.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,taxonomy_ids);
	    if(location_details.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,location_details);
	    if(noPublish.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,"y");						
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
	}
	catch(Exception ex){
	    logger.error(ex+" : "+qq);
	    message = "Error in saving the record "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doUpdate(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String message = "";
	String qq = "update programs set "+
	    "lead_id=?,statement=?,title=?,category_id=?,nraccount=?,"+
	    "fee=?,oginfo=?,inCityFee=?,nonCityFee=?,otherFee=?,"+
	    "allAge=?,partGrade=?,minMaxEnroll=?,regDeadLine=?,location_id=?,"+
	    "instructor=?,description=?,days=?,startTime=?,endTime=?,classCount=?,"+
	    "code=?,area_id=?,codeNeed=?,startDate=?,endDate=?,"+
	    "codeTask=?,marketTask=?,volTask=?,sponTask=?,ageFrom=?,"+
	    "ageTo=?,budgetTask=?,evalTask=?,wParent=?,waitList=?,"+
	    "category2_id=?,otherAge=?,subcat=?,memberFee=?,"+
	    "nonMemberFee=?,summary=?,"+
	    "taxonomy_ids=?,location_details=?,noPublish=? "+
	    " where id=?";

	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(lead_id.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,lead_id);
	    if(statement.equals("")) 
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,statement);
	    if(title.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,title);
	    if(category_id.equals("")) 
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,category_id);
	    if(nraccount.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,nraccount);
	    if(fee.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,fee);
	    if(oginfo.equals("")) // 10
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,oginfo);

	    if(inCityFee.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,inCityFee);
	    if(nonCityFee.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,nonCityFee);
	    if(otherFee.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,otherFee);
	    if(allAge.equals(""))  
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,"y");
	    if(partGrade.equals("")) 
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,partGrade);
	    if(minMaxEnroll.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,minMaxEnroll);
	    if(regDeadLine.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(regDeadLine).getTime()));
	    if(location_id.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,location_id);
	    if(instructor.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,instructor);
	    if(description.equals("")) // 20
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,description);
	    if(days.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,days);
	    if(startTime.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,startTime);
	    if(endTime.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,endTime);						
	    if(classCount.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,classCount);
	    if(code.equals("")) // 25
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,code);
	    if(area_id.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,area_id);
	    if(codeNeed.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,codeNeed);
	    if(startDate.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(startDate).getTime()));
	    if(endDate.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(endDate).getTime()));
	    if(codeTask.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,codeTask);
	    if(marketTask.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,marketTask);
	    if(volTask.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,volTask);
	    if(sponTask.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,sponTask);
	    if(ageFrom.equals(""))  
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,ageFrom);
	    if(ageTo.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,ageTo);
	    if(budgetTask.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,budgetTask);
	    if(evalTask.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,evalTask);
	    if(wParent.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,"y");
	    if(waitList.equals("")){
		pstmt.setString(jj++,"0");
	    }
	    else
		pstmt.setString(jj++,waitList);
	    if(category2_id.equals("")) 
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,category2_id);
	    if(otherAge.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,otherAge);
	    if(subcat.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,subcat);
	    if(memberFee.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,memberFee);
	    if(nonMemberFee.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,nonMemberFee);
	    if(summary.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,summary);
	    if(taxonomy_ids.equals("")) // set
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,taxonomy_ids);
						
	    if(location_details.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,location_details);
	    if(noPublish.equals(""))
		pstmt.setString(jj++,null);
	    else
		pstmt.setString(jj++,"y");
	    pstmt.setString(jj++,id);
						
	    pstmt.executeUpdate();

	}
	catch (Exception ex){
	    logger.error(ex+" : "+qq);
	    message = "Error updating  "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	doSelect();
	return message;
    }
    /**
     * timeName either startTime or endTime
     */
    public String updateTime(String timeName, String val){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String message = "";
	if(timeName.equals("") || val.equals("")){
	    message = " time name or value not set ";
	    return message;
	}
	String qq = "update programs set "+timeName +" = ?  where id=?";

	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, val);
	    pstmt.setString(2, id);
	    pstmt.executeUpdate();

	}
	catch (Exception ex){
	    logger.error(ex+" : "+qq);
	    message = "Error updating  "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    public String updateSeasonYear(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String message = "";
	if(id.equals("")){
	    message = "program id not set ";
	    return message;
	}
	if(season.equals(season2)){
	    message = "Season and season2 are the same "+season;
	    return message;
	}
	String qq = "update programs set season = ?,season2=?,year=?  where id=?";
	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, season);
	    if(season2.equals("")){
		pstmt.setNull(2, Types.VARCHAR);
	    }
	    else
		pstmt.setString(2, season2);								
	    pstmt.setString(3, year);						
	    pstmt.setString(4, id);
	    pstmt.executeUpdate();

	}
	catch (Exception ex){
	    logger.error(ex+" : "+qq);
	    message = "Error updating  "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }		
    //	
    public String doDelete(){
	//
	// we have to delete all related session, volunteer, 
	// shifts, training and sponsors records
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;		
	String message = "";
	String qq1 = "delete from session_opts where id=? ";
	String qq2 = "delete from sessions where id=? ";
	String qq3 = "delete from vol_trainings where pid=? ";
	String qq4 = "delete from vol_shifts where pid=? ";
	String qq5 = "delete from sponsors where pid=? ";
	String qq6 = "delete from marketing_programs where prog_id=? ";
	//
	// delete the program and the sessions from agenda
	//
	String qq7 = "delete from agenda_info where pid=? ";
	String qq8 = "delete from evaluations where id=? "; // same as pid
	String qq10 = "delete from programs_prebudget where id=? ";
	String qq11 = "delete from prog_eval_budget_rev where id=? ";
	String qq12 = "delete from programs_market where id=? ";
	String qq13 = "delete from web_publish_programs where prog_id=? ";		
	String q = "delete from programs where id=? ";
	String qq = "";
	try{
	    con = Helper.getConnection();	
	    qq = qq1;
	    if(debug){
		logger.debug(qq1);
	    }
	    pstmt = con.prepareStatement(qq1);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq2;
	    if(debug){
		logger.debug(qq2);
	    }
	    pstmt = con.prepareStatement(qq2);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq2;
	    if(debug){
		logger.debug(qq3);
	    }
	    pstmt = con.prepareStatement(qq3);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq4;
	    if(debug){
		logger.debug(qq4);
	    }
	    pstmt = con.prepareStatement(qq4);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq5;
	    if(debug){
		logger.debug(qq5);
	    }
	    pstmt = con.prepareStatement(qq5);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq6;
	    if(debug){
		logger.debug(qq6);
	    }
	    pstmt = con.prepareStatement(qq6);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq7;
	    if(debug){
		logger.debug(qq7);
	    }
	    pstmt = con.prepareStatement(qq7);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq8;
	    if(debug){
		logger.debug(qq8);
	    }
	    pstmt = con.prepareStatement(qq8);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq10;
	    if(debug){
		logger.debug(qq10);
	    }
	    pstmt = con.prepareStatement(qq10);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq11;
	    if(debug){
		logger.debug(qq11);
	    }
	    pstmt = con.prepareStatement(qq11);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
						
	    qq = qq12;
	    if(debug){
		logger.debug(qq12);
	    }
	    pstmt = con.prepareStatement(qq12);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    if(debug){
		logger.debug(qq);
	    }
	    qq = qq13;
	    if(debug){
		logger.debug(qq13);
	    }
	    pstmt = con.prepareStatement(qq13);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    if(debug){
		logger.debug(qq);
	    }						
	    qq = q;
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
						
	}
	catch(Exception ex){
	    logger.error(ex+" : "+qq);
	    message = " Error deleting the recored "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    public String doSelect(){
	//
	// case from browsing
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String message ="", str="", str2="";
	String qq = "select p.lead_id,l.name,p.statement,"+ 
	    " p.title,p.year,p.season,"+ // 6
	    " p.category_id,c.name,p.nraccount,p.fee,p.oginfo, "+ // 11
	    " p.inCityFee,p.nonCityFee,p.otherFee,"+  // 14
	    " p.allAge,p.partGrade,p.minMaxEnroll,"+   //17
	    " date_format(p.regDeadLine,'%m/%d/%Y'),"+ // 18
	    " p.location_id,lc.name, p.instructor,p.description, "+ // 22
	    " p.days,p.startTime,p.endTime,p.classCount, "+// 25
	    " p.code,p.area_id,a.name,p.codeNeed,"+ // 29
	    " date_format(p.startDate,'%m/%d/%Y'),"+//30 
	    " date_format(p.endDate,'%m/%d/%Y'),p.codeTask,"+ // 32
	    " p.marketTask,p.volTask,p.sponTask,p.ageFrom,p.ageTo, "+ // 33
	    " p.budgetTask,p.evalTask, "+ // 35
	    " p.wParent,p.waitList,p.plan_id,p.oplocation,p.category2_id,"+//40
	    " p.version, "+ // 41
	    " p.otherAge, "+ // 42
	    " p.subcat, "+  // 43
	    " date_format(p.received,'%m/%d/%Y'), "+ // 44
	    " p.memberFee,p.nonMemberFee,p.season2, "+
	    " p.summary,p.taxonomy_ids,p.location_details,p.noPublish "+
	    " from programs p "+
	    " left join leads l on p.lead_id = l.id "+
	    " left join categories c on p.category_id = c.id "+
	    " left join areas a on p.area_id = a.id "+
	    " left join locations lc on p.Location_id = lc.id "+
	    " where p.id=? ";		
	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		//
		str = rs.getString(1);
		if(str != null) lead_id = str;
		str2 = rs.getString(2);
		if(str != null && str2 != null){
		    lead = new Type(debug, str, str2);
		}
		str = rs.getString(3); // 2
		if(str != null) statement = str;
		str = rs.getString(4);
		if(str != null) title = str;
		str = rs.getString(5);
		if(str != null) year = str;
		str = rs.getString(6);
		if(str != null) season = str;
		str = rs.getString(7);
		if(str != null) category_id = str;
		str2 = rs.getString(8);
		if(str != null && str2 != null){
		    category = new Type(debug, str, str2);
		}
		str = rs.getString(9);
		if(str != null) nraccount = str;
		str = rs.getString(10);
		if(str != null) fee = str;
		str = rs.getString(11);
		if(str != null) oginfo = str;
		str = rs.getString(12);
		if(str != null) inCityFee = str;
		str = rs.getString(13);
		if(str != null) nonCityFee = str;
		str = rs.getString(14);
		if(str != null) otherFee = str;
		str = rs.getString(15);
		if(str != null) allAge = "y";
		str = rs.getString(16);
		if(str != null) partGrade = str;
		str = rs.getString(17);
		if(str != null && !str.equals("0")) minMaxEnroll = str;
		str = rs.getString(18);
		if(str != null) regDeadLine = str;
		str = rs.getString(19);
		if(str != null) location_id = str;
		str2 = rs.getString(20);
		if(str != null && str2 != null){
		    location = new Location(debug, str, str2);
		}				
		str = rs.getString(21);
		if(str != null) instructor = str;
		str = rs.getString(22);
		if(str != null) description = str;
		str = rs.getString(23);
		if(str != null) days = str;
		str = rs.getString(24);
		if(str != null) setStartTime(str);
		str = rs.getString(25);
		if(str != null) endTime = str;								
		str = rs.getString(26);
		if(str != null) classCount = str;
		str = rs.getString(27);
		if(str != null) code = str;
		str = rs.getString(28);
		if(str != null) area_id = str;
		str2 = rs.getString(29);
		if(str != null && str2 != null){
		    area = new Type(debug, str, str2);
		}				
		str = rs.getString(30);
		if(str != null) codeNeed = str;
		str = rs.getString(31);
		if(str != null) startDate = str;
		str = rs.getString(32);
		if(str != null) endDate = str;
		str = rs.getString(33);
		if(str != null) codeTask = str;
		str = rs.getString(34);
		if(str != null) marketTask = str;
		str = rs.getString(35);
		if(str != null) volTask = str;
		str = rs.getString(36);
		if(str != null) sponTask = str;
		str = rs.getString(37);
		if(str != null && !str.equals("0")) ageFrom = str;
		str = rs.getString(38);
		if(str != null && !str.equals("100") && 
		   !str.equals("0")) ageTo = str;
		str = rs.getString(39);
		if(str != null) budgetTask = str;
		str = rs.getString(40);
		if(str != null) evalTask = str;
		str = rs.getString(41);
		if(str != null) wParent = str;
		str = rs.getString(42);
		if(str != null && !str.trim().equals("0")) waitList = str;
		str = rs.getString(43);
		if(str != null && !str.equals("0")) plan_id = str;
		str = rs.getString(44);
		if(str != null) oplocation = str;
		str = rs.getString(45);
		if(str != null) category2_id = str;
		str = rs.getString(46);
		if(str != null) version = str;
		str = rs.getString(47);
		if(str != null) otherAge = str;
		str = rs.getString(48);
		if(str != null) subcat = str;
		str = rs.getString(49);
		if(str != null) received = str;
		str = rs.getString(50);
		if(str != null) memberFee = str;
		str = rs.getString(51);
		if(str != null) nonMemberFee = str;
		str = rs.getString(52);
		if(str != null) season2 = str;
		str = rs.getString(53);
		if(str != null) summary = str;
		str = rs.getString(54);
		if(str != null) taxonomy_ids = str;
		str = rs.getString(55);
		if(str != null) location_details = str;
		str = rs.getString(56);
		if(str != null) noPublish = str;
								
	    }
	}
	catch(Exception ex){
	    logger.error(ex+" : "+qq);
	    message += " Error retreiving the recored "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}		
	return message;
    }
    /**
     * create a new program from the entries of this program by
     * adding new ID and set some fields to null
     * we are also duplicating, sessions, market, volunteer stuff
     */
    public String doDuplicate(){

	// assuming the doSelect is already ran
	//
	String oldId = id;
	String back = "";
	getShifts();
	getMarket();
	getSessionOpt();
	getSessions();
	//
	// set these fields to empty 
	//
	id = "";
	regDeadLine=""; code="";
	codeNeed=""; startDate="";endDate="";
	codeTask="";marketTask="";volTask="";sponTask=""; budgetTask="";
	evalTask="";noPublish="";
	//
	back = doSave();
	if(!back.equals("")){
	    return back;
	}
	if(shifts != null && shifts.size() > 0){
	    for(VolShift one:shifts){
		back += one.doDuplicate(id);
	    }
	    if(!back.equals("")){
		return back;
	    }
	}
	if(market != null){
	    back = market.doDuplicate(id);
	}
	if(sessionOpt != null){
	    back = sessionOpt.doDuplicate(id);
	}
	if(sessions != null){
	    int j=1;
	    for(Session one:sessions){
		back = one.doDuplicate(id, ""+j);
		j++;
	    }
	}

	return back;
    }
}























































