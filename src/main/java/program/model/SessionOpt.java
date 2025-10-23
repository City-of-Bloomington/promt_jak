package program.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class SessionOpt{

    boolean debug = false;
    String id="";
    static Logger logger = LogManager.getLogger(SessionOpt.class);
    boolean selectionDone = false;
    //
    // fields common in program and session
    // if used in program, should not be used in session
    //
    boolean show[] = { true, true, false, false, false,
	false, false, false, false, false,
	false, false, false, false, false,
	false, false, false, false, false};
    String titles[] = {
	"ID",
	"Code &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
	"Days",  // 3
	"Start, End Date", // 4
	"Start, End Time", // 5
		
	"Reg. Deadline",   // 6
	"In City Fee",
	"Non City Fee",
	"Other Fee",
	"Member Fee",
		
	"Non Member Fee",		
	"Location",         // 12
	"Participant Age",  // 13
	"Participant Grade",
	"Min-Max Enroll",
	"# of Classes",
	"Description",
	"Instructor"
    };   // 16
    String code_c="",days_c="", // 2
	startDate_c="",
	startTime_c="", 
	regDeadLine_c="",
	inCityFee_c="",    
	nonCityFee_c="",  
	otherFee_c="",  
	location_c="",  
	ageFrom_c="", 
	partGrade_c="", 
	minMaxEnroll_c="", 
	classCount_c="", 
	description_c="",
	instructor_c="", 
	sessionSort = "",
	allAge_c="", ageTo_c="", endDate_c="", wParent_c="", otherAge_c="",
	memberFee_c="", nonMemberFee_c="", endTime_c="";
    //
    public SessionOpt(boolean val){
	debug = val;
    }
    public SessionOpt(boolean val, String val2){
	debug = val;
	setId(val2);
    }	
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setAllAge_c(String val){
	if(val != null)
	    allAge_c = val;
    }
    public void setInCityFee_c(String val){
	if(val != null)
	    inCityFee_c= val;		  
    }
    public void setNonCityFee_c(String val){
	if(val != null)
	    nonCityFee_c = val;
    }
    public void setOtherFee_c(String val){
	if(val != null)
	    otherFee_c = val;
    }
    public void setPartGrade_c(String val){
	if(val != null)
	    partGrade_c = val;
    }
    public void setMinMaxEnroll_c(String val){
	if(val != null)
	    minMaxEnroll_c = val;
    }
    public void setInstructor_c(String val){
	if(val != null)
	    instructor_c = val;
    }
    public void setDescription_c(String val){
	if(val != null)
	    description_c = val;
    }
    public void setDays_c(String val){
	if(val != null)
	    days_c = val;
    }
    public void setStartTime_c(String val){
	if(val != null){
	    startTime_c = val;
	    endTime_c = val;
	}
    }
    public void setEndTime_c(String val){
	if(val != null)
	    endTime_c = val;
    }
    public void setClassCount_c(String val){
	if(val != null)
	    classCount_c = val;
    }
    public void setCode_c(String val){
	if(val != null)
	    code_c = val;
    }
    public void setRegDeadLine_c(String val){
	if(val != null)
	    regDeadLine_c = val;
    }
    public void setStartDate_c(String val){
	if(val != null)
	    startDate_c = val;
    }
    public void setEndDate_c(String val){
	if(val != null)
	    endDate_c = val;
    }
    public void setAgeFrom_c(String val){
	if(val != null)
	    ageFrom_c = val;
    }
    public void setAgeTo_c(String val){
	if(val != null)
	    ageTo_c = val;
    }
    public void setWParent_c(String val){
	if(val != null)
	    wParent_c = val;
    }
    public void setLocation_c(String val){
	if(val != null)
	    location_c = val;
    }
    public void setOtherAge_c(String val){
	if(val != null)
	    otherAge_c = val;
    }
    public void setMemberFee_c(String val){
	if(val != null)
	    memberFee_c = val;
    }
    public void setNonMemberFee_c(String val){
	if(val != null)
	    nonMemberFee_c = val;
    }	
    public void setSessionSort(String val){
	if(val != null)
	    sessionSort = val;
    }	
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getAllAge_c(){
	return allAge_c;
    }
    public String getInCityFee_c(){
	return inCityFee_c;
    }
    public String getNonCityFee_c(){
	return nonCityFee_c;
    }
    public String getOtherFee_c(){
	return otherFee_c;
    }
    public String getPartGrade_c(){
	return partGrade_c;
    }
    public String getMinMaxEnroll_c(){
	return minMaxEnroll_c;
    }
    public String getRegDeadLine_c(){
	return regDeadLine_c;
    }
    public String getInstructor_c(){
	return instructor_c;
    }
    public String getDescription_c(){
	return description_c;
    }
    public String getDays_c(){
	return days_c;
    }
    public String getEndTime_c(){
	return endTime_c;
    }
    public String getStartTime_c(){
	return startTime_c;
    }		
    public String getClassCount_c(){
	return classCount_c;
    }
    public String getCode_c(){
	return code_c;
    }
    public String getLocation_c(){
	return location_c;
    }
    public String getStartDate_c(){
	return startDate_c;
    }
    public String getEndDate_c(){
	return endDate_c;
    }
    public String getAgeFrom_c(){
	return ageFrom_c;
    }
    public String getAgeTo_c(){
	return ageTo_c;
    }
    public String getMemberFee_c(){
	return memberFee_c;
    }
    public String getNonMemberFee_c(){
	return nonMemberFee_c;
    }	
    public String getWParent_c(){
	return wParent_c;
    }
    public String getOtherAge_c(){
	return otherAge_c;
    }
    public String getSessionSort(){
	return sessionSort;
    }
    public String getTitle(int jj){
	if(jj < 0 || jj > titles.length) return "";
	return titles[jj];
    }
    public int getShowSize(){
	return titles.length;
    }	
    public boolean show(int jj){
	if(jj < 0 || jj > show.length) return false;
	return show[jj];
    }
    //
    public boolean showAllAge(){
	return !allAge_c.equals("") || !ageFrom_c.equals("") || !ageTo_c.equals("");
    }
    public boolean showInCityFee(){
	return !inCityFee_c.equals("");
    }
    public boolean showNonCityFee(){
	return !nonCityFee_c.equals("");
    }
    public boolean showOtherFee(){
	return !otherFee_c.equals("");
    }
    public boolean showPartGrade(){
	return !partGrade_c.equals("");
    }
    public boolean showMinMaxEnroll(){
	return !minMaxEnroll_c.equals("");
    }
    public boolean showRegDeadLine(){
	return !regDeadLine_c.equals("");
    }
    public boolean showInstructor(){
	return !instructor_c.equals("");
    }
    public boolean showDescription(){
	return !description_c.equals("");
    }
    public boolean showDays(){
	return !days_c.equals("");
    }
    public boolean showStartEndTime(){
	return !startTime_c.equals("");
    }
    public boolean showStartTime(){
	return !startTime_c.equals("");
    }
    public boolean showEndTime(){
	return !endTime_c.equals("");
    }		
    public boolean showClassCount(){
	return !classCount_c.equals("");
    }
    public boolean showCode(){
	return !code_c.equals("");
    }
    public boolean showLocation(){
	return !location_c.equals("");
    }
    public boolean showStartDate(){
	return !startDate_c.equals("");
    }
    public boolean showEndDate(){
	return !endDate_c.equals("");
    }
    public boolean showAgeFrom(){
	return !ageFrom_c.equals("");
    }
    public boolean showAgeTo(){
	return !ageTo_c.equals("");
    }
    public boolean showWParent(){
	return !wParent_c.equals("");
    }
    public boolean showOtherAge(){
	return !otherAge_c.equals("");
    }
    public boolean showMemberFee(){
	return !memberFee_c.equals("");
    }
    public boolean showNonMemberFee(){
	return !nonMemberFee_c.equals("");
    }	
    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String qq = "", message="";
	//
	// all new record will get the current version
	//
	qq = "insert into session_opts values (";
	qq +="-1,"; // sid
	qq +="'y',"; // code
	qq += "?,";	  // id
	qq += "?,?,?,?,?,"+
	    "?,?,?,?,?,"+
	    "?,?,?,?,?,"+
	    "?,?,";
	qq += "?,";  // endDate_c
	qq += "?,"; // wParent
	qq += "?,"; // othrAge
	qq += "?,?,"; // memberfee_c, nonMemeberFee_c
	qq += "?)"; // sessionSort
	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    setParams(pstmt);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    message = "Error in saving the recored "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	message += selectForShow();
	return message;
    }
    public String doDuplicate(String program_id){
	String back = "";
	if(!selectionDone){
	    back = doSelect();
	    if(!back.equals("")){
		return back;
	    }
	}
	id = program_id;
	back = doSave();
	return back;
    }
    void setParams(PreparedStatement pstmt){
	try{
	    pstmt.setString(1,id);
	    if(days_c.equals("")) 
		pstmt.setString(2,null);
	    else
		pstmt.setString(2,"y");
	    if(startTime_c.equals(""))
		pstmt.setString(3,null);
	    else
		pstmt.setString(3,"y");
	    if(endTime_c.equals(""))
		pstmt.setString(4,null);
	    else
		pstmt.setString(4,"y");						
	    if(regDeadLine_c.equals("")) // should be avoided
		pstmt.setString(5,null);
	    else
		pstmt.setString(5,"y");
	    if(inCityFee_c.equals(""))
		pstmt.setString(6,null);
	    else
		pstmt.setString(6,"y");
	    if(nonCityFee_c.equals(""))
		pstmt.setString(7,null);
	    else
		pstmt.setString(7,"y");
	    if(otherFee_c.equals("")) 
		pstmt.setString(8,null);
	    else
		pstmt.setString(8,"y");

	    if(location_c.equals(""))
		pstmt.setString(9,null);
	    else
		pstmt.setString(9,"y");
	    if(allAge_c.equals(""))
		pstmt.setString(10,null);
	    else
		pstmt.setString(10,"y");
	    if(partGrade_c.equals("")) // 15
		pstmt.setString(11,null);
	    else
		pstmt.setString(11,"y");
	    if(minMaxEnroll_c.equals(""))
		pstmt.setString(12,null);
	    else
		pstmt.setString(12,"y");
	    if(classCount_c.equals(""))
		pstmt.setString(13,null);
	    else
		pstmt.setString(13,"y");
	    if(description_c.equals("")) 
		pstmt.setString(14,null);
	    else
		pstmt.setString(14,"y");
	    if(instructor_c.equals(""))
		pstmt.setString(15,null);
	    else
		pstmt.setString(15,"y");
	    if(ageFrom_c.equals(""))  
		pstmt.setString(16,null);
	    else
		pstmt.setString(16,"y");
	    if(ageTo_c.equals(""))
		pstmt.setString(17,null);
	    else
		pstmt.setString(17,"y");
	    if(startDate_c.equals(""))
		pstmt.setString(18,null);
	    else
		pstmt.setString(18,"y");
	    if(endDate_c.equals(""))
		pstmt.setString(19,null);
	    else
		pstmt.setString(19,"y");
	    if(wParent_c.equals(""))
		pstmt.setString(20,null);
	    else
		pstmt.setString(20,"y");
	    if(otherAge_c.equals(""))
		pstmt.setString(21,null);
	    else
		pstmt.setString(21,"y");
	    if(memberFee_c.equals(""))
		pstmt.setString(22,null);
	    else
		pstmt.setString(22,"y");
	    if(nonMemberFee_c.equals(""))
		pstmt.setString(23,null);
	    else
		pstmt.setString(23,"y");
						
	    if(sessionSort.equals(""))
		pstmt.setString(24,null);
	    else
		pstmt.setString(24,sessionSort);
	}
	catch(Exception ex){
	    logger.error(ex);
	}
    }
    public String doUpdate(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;			
	String message = "";
	String qq = "update session_opts set sid=-1,"+
	    "code_c='y',"+
	    "id = ?,"+
	    "days_c=?,"+
	    "startTime_c=?,"+
	    "endTime_c=?,"+						
	    "regDeadLine_c=?,"+
	    "inCityFee_c=?,"+
	    "nonCityFee_c=?,"+
	    "otherFee_c=?,"+
	    "location_c=?,"+
	    "allAge_c=?,"+
	    "partGrade_c=?,"+
	    "minMaxEnroll_c=?,"+
	    "classCount_c=?,"+
	    "description_c=?,"+
	    "instructor_c=?,"+
	    "ageFrom_c=?,"+
	    "ageTo_c=?,"+
	    "startDate_c=?,"+
	    "endDate_c=?,"+
	    "wParent_c=?,"+
	    "otherAge_c=?, "+
	    "memberFee_c=?, "+
	    "nonMemberFee_c=?,"+
	    "sessionSort=? "+
	    " where id=? ";

	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    setParams(pstmt);
	    pstmt.setString(25,id);
	    pstmt.executeUpdate();

	}
	catch (Exception ex){
	    logger.error(ex+":"+qq);
	    message = "Error updating  "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	message += selectForShow();		
	return message;
    }
    public String doDelete(){
	//
	// we have to delete all related session, volunteer, 
	// shifts, training and sponsors records
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;			
	String message = "";
	String qq = "delete from session_opts where id=? ";
	//
	// delete the program and the sessions from agenda
	//
	try{
	    con = Helper.getConnection();	
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
			
	}
	catch (Exception ex){
	    logger.error(ex+":"+qq);
	    message = "Error updating  "+ex;
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
	String message ="", str="";
	String qq = "select * "+ 
	    " from session_opts where id=? "; // 22
	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		str = rs.getString(4); // skip the first 3
		if(str != null) days_c = str;
		str = rs.getString(5) ;
		if(str != null) startTime_c = str;
		str = rs.getString(6) ;
		if(str != null) endTime_c = str;								
		str  = rs.getString(7) ;
		if(str != null) regDeadLine_c = str;
		str   = rs.getString(8);
		if(str != null) inCityFee_c = str;
		str  = rs.getString(9) ;
		if(str != null) nonCityFee_c = str;
		str    = rs.getString(10) ;
		if(str != null) otherFee_c = str;
		str     = rs.getString(11) ;
		if(str != null) location_c = str;
		str      = rs.getString(12);
		if(str != null) allAge_c = str;
		str    = rs.getString(13);
		if(str != null) partGrade_c = str;
		str = rs.getString(14);
		if(str != null) minMaxEnroll_c = str;
		str = rs.getString(15);
		if(str != null) classCount_c = str;
		str = rs.getString(16);
		if(str != null) description_c = str;
		str   = rs.getString(17);
		if(str != null) instructor_c = str;
		str   = rs.getString(18);
		if(str != null) ageFrom_c = str;
		str   = rs.getString(19);
		if(str != null) ageTo_c = str;
		str   = rs.getString(20);
		if(str != null) startDate_c = str;
		str   = rs.getString(21);
		if(str != null) endDate_c = str;
		str   = rs.getString(22);
		if(str != null) wParent_c = str;
		str   = rs.getString(23);
		if(str != null) otherAge_c = str;
		str   = rs.getString(24);
		if(str != null) memberFee_c = str;
		str   = rs.getString(25);
		if(str != null) nonMemberFee_c = str;				
		str   = rs.getString(26);
		if(str != null) sessionSort = str;
		selectionDone = true;
	    }
	    //
	    // for show purpose
	    //
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    message += " Error retreiving the recored "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	message += selectForShow();
	return message;
    }
    public String selectForShow(){
	//
	// case from browsing
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String message ="", str="";
	String qq = "select sid,code_c,days_c,"+ // 3
	    " startDate_c,"+
	    " startTime_c, "+   
	    " regDeadLine_c, "+ // 6
	    " inCityFee_c, "+     
	    " nonCityFee_c, "+ 
	    " otherFee_c, "+
	    " memberFee_c,"+
	    " nonMemberFee_c,"+
	    " location_c, "+ 
	    " ageFrom_c, "+  // 13
	    " partGrade_c, "+ 
	    " minMaxEnroll_c, "+ 
	    " classCount_c, "+
	    " description_c, "+  
	    " instructor_c, "+   // 18
	    " sessionSort "+     // 19
	    " from session_opts where "+
	    " id = ? ";
	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		for(int jj = 2;jj < titles.length;jj++){
		    str = rs.getString(jj+1);
		    if(str != null && !str.equals(""))
			show[jj] = true;
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    message += " Error retreiving the recored "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}		
	return message;
    }	
    /**
     * we need to know if we have a record or not, because when programs are
     * duplicated, session_opt is not, to determine if we need to save or
     * update the record
     */
    public boolean recordExists(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int count = 0;
	String message ="", str="";
	String qq = "select count(*) "+ 
	    " from session_opts where id=? "; 
	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return count > 0;
    }
	
}























































