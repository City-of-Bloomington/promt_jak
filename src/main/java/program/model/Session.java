package program.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class Session{

    boolean debug = false;
    boolean selectionDone = false;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id="", sid="";
    String startDate="", endDate="";
    static Logger logger = LogManager.getLogger(Session.class);
    //
    // fields common in program and session
    // if used in program, should not be used in session
    //
    String inCityFee="", nonCityFee="",otherFee="";
    String location_id="", instructor="",regDeadLine="";
    String ageFrom="", ageTo="",partGrade="", description="";
    String minMaxEnroll="",
	allAge = "", // pPartAge
	otherAge = ""; // will be used for 18 month toddlers and so on
    String days="", startEndTime="", startTime="", endTime="",
	memberFee="",nonMemberFee="";
    String classCount="",code="",wParent="";
    Location location = null;
    //
    public Session(boolean val){
	debug = val;
    }
    public Session(boolean deb, String val, String val2){
	debug = deb;
	setId(val);
	setSid(val2);
    }
    public Session(boolean deb,
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
	    String val25
	    ){
	debug = deb;
	setId(val);
	setSid(val2);		
	setInCityFee(val3);
	setNonCityFee(val4);
	setOtherFee(val5);
	setAllAge(val6);
	setPartGrade(val7);
	setMinMaxEnroll(val8);
	setRegDeadLine(val9);
	setLocation_id(val10);
	setInstructor(val11);
	setDescription(val12);
	setDays(val13);
	setStartTime(val14);
	setEndTime(val15);
	setClassCount(val16);
	setCode(val17);
	setStartDate(val18);
	setEndDate(val19);
	setAgeFrom(val20);
	setAgeTo(val21);
	setWParent(val22);
	setOtherAge(val23);
	setMemberFee(val24);
	setNonMemberFee(val25);		
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setSid(String val){
	if(val != null)
	    sid = val;
    }
    public void setAllAge(String val){
	if(val != null)
	    allAge = "y";
    }
    public void setInCityFee(String val){
	if(val != null)
	    inCityFee= val;		  
    }
    public void setNonCityFee(String val){
	if(val != null)
	    nonCityFee = val;
    }
    public void setOtherFee(String val){
	if(val != null)
	    otherFee = val;
    }
    public void setMemberFee(String val){
	if(val != null)
	    memberFee = val;
    }
    public void setNonMemberFee(String val){
	if(val != null)
	    nonMemberFee = val;
    }	
    public void setPartGrade(String val){
	if(val != null)
	    partGrade = val;
    }
    public void setMinMaxEnroll(String val){
	if(val != null)
	    minMaxEnroll = val;
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
    public void setRegDeadLine(String val){
	if(val != null)
	    regDeadLine = val;
    }
    public void setStartDate(String val){
	if(val != null)
	    startDate = val;
    }
    public void setEndDate(String val){
	if(val != null)
	    endDate = val;
    }
    public void setAgeFrom(String val){
	if(val != null && !val.equals("0"))
	    ageFrom = val;
    }
    public void setAgeTo(String val){
	if(val != null && !val.equals("0") && !val.equals("100"))
	    ageTo = val;
    }
    public void setWParent(String val){
	if(val != null)
	    wParent = val;
    }
    public void setLocation_id(String val){
	if(val != null)
	    location_id = val;
    }
    public void setOtherAge(String val){
	if(val != null)
	    otherAge = val;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getSid(){
	return sid;
    }
    public String getAllAge(){
	return allAge;
    }
    public String getInCityFee(){
	return inCityFee;
    }
    public String getNonCityFee(){
	return nonCityFee;
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
    public String getStartDate(){
	return startDate;
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
    public String getWParent(){
	return wParent;
    }
    public String getOtherAge(){
	return otherAge;
    }
    public String getMemberFee(){
	return memberFee;
    }
    public String getNonMemberFee(){
	return nonMemberFee;
    }
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
    public String getLocationName(){
	if(location == null && !location_id.equals("")){
	    getLocation();
	}
	if(location != null){
	    return location.getName();
	}
	return "";
    }
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof Session){
	    match = !id.equals("") && id.equals(((Session)gg).id)&&
		!sid.equals("") && sid.equals(((Session)gg).sid);
	}
	return match;
    }

    public int hashCode(){
	int code = 0;
	try{
	    code = Integer.parseInt(id)*37+Integer.parseInt(sid)*43;
	}catch(Exception ex){};
	return code;
    }		
    public String getStartEndDate(){
	String ret = "";
	if(!startDate.equals("")){
	    ret = startDate.substring(0,5); // short format mm/dd
	}
	if(!endDate.equals("")){
	    if(!ret.equals("")) ret += " - ";
	    ret += endDate.substring(0,5);
	}
	return ret;
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
    public String getAgeInfo(){
	String ret = "";
	int from =0, to=0;
	if(!allAge.equals("")){
	    return "All";
	}
	try{
	    if(!ageFrom.equals("")){
		from = Integer.parseInt(ageFrom);
	    }
	    if(!ageTo.equals("")){
		to = Integer.parseInt(ageTo);
	    }
	}catch(Exception ex){}
	if(from > 0 && to < 100) 
	    ret = " "+ 
		from +
		"-"+to;
	else if(from > 0) 
	    ret =" "+from + 
		" yrs. and up";
	else if (to > 0)
	    ret =to+" yrs. and under";
	if(!wParent.equals("")){
	    ret += " w/Parent";
	}
	if(!otherAge.equals("")){
	    if(!ret.equals("")) ret += " ";
	    ret += otherAge;
	}
	return ret;
    }
    public Location getLocation(){
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
    public String getNextId(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String qq = "", back="";
	int nextid = 0;
	qq = "select max(sid) "+
	    " from sessions where id=? " ;
	if(debug)
	    logger.debug(qq);
		
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);			
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		nextid = rs.getInt(1);
	    }
	}
	catch(Exception ex){
	    back = ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	sid = ""+(nextid + 1);
	return sid;
    }
    public String doSave(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String qq = "", message="";
	//
	// all new record will get the current version
	//
	qq = "insert into sessions values (?,?,?,?,?,"+
	    "?,?,?,?,?,"+
	    "?,?,?,?,?,"+
	    "?,?,?,?,?,"+
	    "?,?,?,?,?)"; // 24
	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    pstmt.setString(j++,sid);
	    if(code.equals("")) 
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,code);
	    pstmt.setString(j++,id);
	    if(days.equals(""))  // 5
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
	    if(regDeadLine.equals("")) // should be avoided
		pstmt.setString(j++,null);
	    else
		pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(regDeadLine).getTime()));
	    if(inCityFee.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,inCityFee);
	    if(nonCityFee.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,nonCityFee);
	    if(otherFee.equals("")) // 10
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,otherFee);

	    if(location_id.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,location_id);
	    if(allAge.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,allAge);
	    if(partGrade.equals("")) // 15
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,partGrade);
	    if(minMaxEnroll.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,minMaxEnroll);
	    if(classCount.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,classCount);
	    if(description.equals("")) // 25
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,description);
	    if(instructor.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,instructor);
	    if(ageFrom.equals(""))  
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,ageFrom);
	    if(ageTo.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,ageTo);
	    if(startDate.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(startDate).getTime()));
	    if(endDate.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(endDate).getTime()));

	    if(wParent.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,"y");
	    if(otherAge.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,otherAge);
	    if(memberFee.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,memberFee);
	    if(nonMemberFee.equals(""))
		pstmt.setString(j++,null);
	    else
		pstmt.setString(j++,nonMemberFee);			
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    message = "Error in saving the recored "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}			
	return message;
    }
    public String doDuplicate(String program_id, String session_id){
	String back = "";
	if(!selectionDone){
	    back = doSelect();
	    if(!back.equals("")){
		return back;
	    }
	}
	id = program_id;
	sid = session_id;
	startDate = "";
	endDate = "";
	code = "";
	regDeadLine = "";
	back = doSave();
	return back;
    }		
    public String doUpdate(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;			
	String message = "";
	String qq = "update sessions set "+
	    "inCityFee=?,nonCityFee=?,"+
	    "otherFee=?,allAge=?,partGrade=?,minMaxEnroll=?,regDeadLine=?,"+
	    "location=?,instructor=?,description=?,days=?,startTime=?,endTime=?,"+
	    "classCount=?,code=?,startDate=?,endDate=?,"+
	    "ageFrom=?,"+
	    "ageTo=?,wParent=?,"+
	    "otherAge=?,memberFee=?,nonMemberFee=? "+
	    " where id=? and sid=?";

	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    if(inCityFee.equals(""))
		pstmt.setString(1,null);
	    else
		pstmt.setString(1,inCityFee);
	    if(nonCityFee.equals("")) 
		pstmt.setString(2,null);
	    else
		pstmt.setString(2,nonCityFee);
	    if(otherFee.equals(""))
		pstmt.setString(3,null);
	    else
		pstmt.setString(3,otherFee);
	    if(allAge.equals(""))  // replaced pPartAge
		pstmt.setString(4,null);
	    else
		pstmt.setString(4,"y");
	    if(partGrade.equals("")) // 15
		pstmt.setString(5,null);
	    else
		pstmt.setString(5,partGrade);
	    if(minMaxEnroll.equals(""))
		pstmt.setString(6,null);
	    else
		pstmt.setString(6,minMaxEnroll);
	    if(regDeadLine.equals(""))
		pstmt.setString(7,null);
	    else
		pstmt.setDate(7,new java.sql.Date(dateFormat.parse(regDeadLine).getTime()));

	    if(location_id.equals(""))
		pstmt.setString(8,null);
	    else
		pstmt.setString(8,location_id);
	    if(instructor.equals(""))
		pstmt.setString(9,null);
	    else
		pstmt.setString(9,instructor);
	    if(description.equals("")) // 20
		pstmt.setString(10,null);
	    else
		pstmt.setString(10,description);
	    if(days.equals(""))
		pstmt.setString(11,null);
	    else
		pstmt.setString(11,days);
	    if(startTime.equals(""))
		pstmt.setString(12,null);
	    else
		pstmt.setString(12,startTime);
	    if(endTime.equals(""))
		pstmt.setString(13,null);
	    else
		pstmt.setString(13,endTime);						
	    if(classCount.equals(""))
		pstmt.setString(14,null);
	    else
		pstmt.setString(14,classCount);
	    if(code.equals("")) // 25
		pstmt.setString(15,null);
	    else
		pstmt.setString(15,code);
	    if(startDate.equals(""))
		pstmt.setString(16,null);
	    else
		pstmt.setDate(16,new java.sql.Date(dateFormat.parse(startDate).getTime()));
	    if(endDate.equals(""))
		pstmt.setString(17,null);
	    else
		pstmt.setDate(17,new java.sql.Date(dateFormat.parse(endDate).getTime()));
	    if(ageFrom.equals(""))  
		pstmt.setString(18,null);
	    else
		pstmt.setString(18,ageFrom);
	    if(ageTo.equals(""))
		pstmt.setString(19,null);
	    else
		pstmt.setString(19,ageTo);
	    if(wParent.equals(""))
		pstmt.setString(20,null);
	    else
		pstmt.setString(20,"y");
	    if(otherAge.equals(""))
		pstmt.setString(21,null);
	    else
		pstmt.setString(21,otherAge);
	    if(memberFee.equals(""))
		pstmt.setString(22,null);
	    else
		pstmt.setString(22,memberFee);
	    if(nonMemberFee.equals(""))
		pstmt.setString(23,null);
	    else
		pstmt.setString(23,nonMemberFee);				
	    pstmt.setString(24,id);
	    pstmt.setString(25,sid);
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
    public String doDelete(){
	//
	// we have to delete all related session, volunteer, 
	// shifts, training and sponsors records
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;			
	String message = "";
	String qq = "delete from sessions where id=? and sid=?";
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
	    pstmt.setString(2,sid);
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
	String qq = "select "+ 
	    " inCityFee,nonCityFee,otherFee,"+ 
	    " allAge,partGrade,minMaxEnroll,"+  
	    " date_format(regDeadLine,'%m/%d/%Y'),"+ 
	    " location,instructor,description, "+ // 10 
	    " days,"+
	    " startTime,"+ // 12
	    " endTime,"+  //13
	    " classCount, "+
	    " code,"+ 
	    " date_format(startDate,'%m/%d/%Y'),"+ // 16
	    " date_format(endDate,'%m/%d/%Y'),"+  // 17
	    " ageFrom,ageTo, "+ // 18,19
	    " wParent,"+ // 20
	    " otherAge, "+ // 21
	    " memberFee,nonMemberFee "+// 22,23
	    " from sessions where id=? and sid=? "; // 24
	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.setString(2,sid);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		//
		setInCityFee(rs.getString(1));
		setNonCityFee(rs.getString(2));
		setOtherFee(rs.getString(3));
		setAllAge(rs.getString(4));
		setPartGrade(rs.getString(5));
		setMinMaxEnroll(rs.getString(6));
		setRegDeadLine(rs.getString(7));
		setLocation_id(rs.getString(8));
		setInstructor(rs.getString(9));
		setDescription(rs.getString(10));
		setDays(rs.getString(11));
		setStartTime(rs.getString(12));
		setEndTime(rs.getString(13));
		setClassCount(rs.getString(14));
		setCode(rs.getString(15));
		setStartDate(rs.getString(16));
		setEndDate(rs.getString(17));
		setAgeFrom(rs.getString(18));
		setAgeTo(rs.getString(19));
		setWParent(rs.getString(20));
		setOtherAge(rs.getString(21));
		setMemberFee(rs.getString(22));
		setNonMemberFee(rs.getString(23));
	    }
	    selectionDone = true;
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
	String qq = "update sessions set "+timeName +" = ?  where id=? and sid=?";

	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();	
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, val);
	    pstmt.setString(2, id);
	    pstmt.setString(3, sid);						
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
}























































