package program.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;

public class MediaRequest extends CommonInc{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(MediaRequest.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id="", season="", request_year="",
	program_id="", lead_id="", location_id="",
	facility_id="",  
	location_description="", content_specific="", notes="";
    String orientation = ""; // Vertical, Horizental, Both
    // request_tyes: Photography, Videography, Other
    String request_type[] = null;
    String other_type="", request_date="";
    Lead lead = null;
    Location location = null;
    Program program = null;
    Facility facility = null;
    public MediaRequest(boolean val){
	debug = val;
    }
    public MediaRequest(boolean val, String val2){
	debug = val;
	setId(val2);
    }
    public MediaRequest(boolean val,
			String val2,
			String val3,
			String val4,
			String val5,
			String val6,
			String val7,
			String val8,
			String val9,
			String val10,
			String[] val11,
			String val12,
			String val13,
			String val14,
			String val15
	    ){
	setVals(val,val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15);

    }
    private void
	setVals(
		boolean val,
		String val2,
		String val3,
		String val4,
		String val5,
		String val6,
		String val7,
		String val8,
		String val9,
		String val10,
		String[] val11,
		String val12,
		String val13,
		String val14,
		String val15
		){
	debug = val;
	setId(val2);
	setSeason(val3);
	setRequestYear(val4);
	setProgram_id(val5);
	setFacility_id(val6);
	setLead_id(val7);
	setLocation_id(val8);
	setLocationDescription(val9);
	setContentSepecific(val10);		
	setRequestType(val11);
	setOtherType(val12);
	setRequestDate(val13);
	setOrientation(val14);	
	setNotes(val15);
    }	

    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setSeason(String val){
	if(val != null)
	    season = val;
    }
    public void setRequestYear(String val){
	if(val != null)
	    request_year = val;
    }    
    public void setProgram_id (String val){
	if(val != null && !val.isEmpty())
	    program_id = val;
    }
    public void setFacility_id (String val){
	if(val != null && !val.isEmpty())
	    facility_id = val;
    }    
    public void setLead_id (String val){
	if(val != null)
	    lead_id = val;
    }        
    public void setLocation_id(String val){
	if(val != null)
	   location_id = val;
    }
    public void setLocationDescription(String val){
	if(val != null)
	    location_description = val;
    }
    public void setContentSepecific(String val){
	if(val != null)
	    content_specific = val;
    }
    public void setRequestTypeStr(String val){
	if(val != null){
	    String [] arr = null;
	    if(val.indexOf(",") > -1){
		arr = val.split(",");
	    }
	    else{
		arr = new String[1];
	        arr[0] = val;
	    }
	    setRequestType(arr);
	}
    }
    public void setRequestType(String[] val){
	if(val != null)
	    request_type = val;
    }
    public void setOtherType(String val){
	if(val != null)
	    other_type = val;
    }
    public void setRequestDate(String val){
	if(val != null)
	    request_date = val;
    }
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }
    public void setOrientation(String val){
	if(val != null)
	    orientation = val;
    }    
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getSeason(){
	return season;
    }
    public String getRequestYear(){
	return request_year;
    }    
    public String getProgram_id(){
	return program_id;
    }
    public String getFacility_id(){
	return facility_id;
    }    
    public String getLead_id(){
	return lead_id;
    }
    public String getLocation_id(){
	return location_id;
    }    
    public String getLocationDescription(){
	return location_description;
    }	
    public String getContentSpecific(){
	return content_specific;
    }
    public String[] getRequestType(){
	return request_type;
    }
    public String getRequestTypeStr(){
	String str = "";
	if(request_type != null){
	    for(String one:request_type){
		if(!str.isEmpty()) str += ", ";
		str += one;
	    }
	}
	return str;
    }
    public String getOtherType(){
	return other_type;
    }
    public String getRequestDate(){
	return request_date;
    }
    public String getNotes(){
	return notes;
    }
    public String getOrientation(){
	return orientation;
    }    
    public Lead getLead(){
	if(lead == null && !lead_id.isEmpty()){
	    Lead one = new Lead(debug, lead_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		lead = one;
	    }
	}
	return lead;
    }
    public Location getLocation(){
	if(location == null && !location_id.isEmpty()){
	    Location one = new Location(debug, location_id);
	    String back = one.doSelect();
	    if(back.isEmpty()){
		location = one;
	    }
	}
	return location;
    }
    public Program getProgram(){
	if(program == null){
	    Program pp = new Program(debug, program_id);
	    String back = pp.doSelect();
	    if(back.isEmpty()){
		program = pp;
	    }
	}
	return program;
    }
    public Facility getFacility(){
	if(facility == null){
	    Facility ff = new Facility(debug, facility_id);
	    String back = ff.doSelect();
	    if(back.isEmpty()){
		facility = ff;
	    }
	}
	return facility;
    }
    public String getLocationName(){
	getLocation();
	if(location != null)
	    return location.getName();
	return "";
    }
    public boolean hasLocation(){
	return !location_id.isEmpty();
    }
    public boolean hasProgram(){
	return !program_id.isEmpty();
    }
    public boolean hasFacility(){
	return !facility_id.isEmpty();
    }
    public boolean hasLead(){
	return !lead_id.isEmpty();
    }
    public boolean hasLocationDescription(){
	return !location_description.isEmpty();
    }
    public boolean hasContentSpecific(){
	return !content_specific.isEmpty();
    }
    public boolean hasOtherType(){
	return !other_type.isEmpty();
    }
    public boolean hasNotes(){
	return !notes.isEmpty();
    }
    public boolean hasOrientation(){
	return !orientation.isEmpty();
    }        
    public boolean equals(Object gg){
	boolean match = false;
	if (gg != null && gg instanceof MediaRequest){
	    match = id.equals(((MediaRequest)gg).id);
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
    public String toString(){
	return id;
    }
    //
    public String doDelete(){

	String back = "";
	String qq = "delete from media_requests where id=?";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;		
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}			
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    message = " Error deleting record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doSelect(){
	//
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String qq = "select id,season,request_year,program_id,facility_id,lead_id,location_id,location_description,content_specific,request_type,other_type,date_format(request_date,'%m/%d/%Y'),orientation,notes "+
	    " from media_requests where id=?";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		String str  = rs.getString(10);
		String [] arr = null;
		if(str != null){
		    if(str.indexOf(",") > -1){
			arr = str.split(",");
		    }
		    else{
			arr = new String[1];
			arr[0] = str;
		    }
		}
		setVals(debug,
			rs.getString(1),
			rs.getString(2),
			rs.getString(3),
			rs.getString(4),
			rs.getString(5),
			rs.getString(6),
			rs.getString(7),
			rs.getString(8),
			rs.getString(9),
			arr,			
			rs.getString(11),
			rs.getString(12),
			rs.getString(13),
			rs.getString(14)
			);
	    }
	    else{
		message = "No match found";
	    }
	}
	catch(Exception ex){
	    message += " Error retreiving the record " +ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doUpdate(){

	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String qq = "update media_requests set season=?,request_year=?,program_id=?,facility_id=?,lead_id=?,location_id=?,location_description=?,content_specific=?,request_type=?,other_type=?,request_date=?,orientation=?,notes=? "+
	    " where id=?";
	if(debug){
	    logger.debug(qq);
	}
	if(lead_id.isEmpty()){
	    back = "Lead is required";
	    addError(back);
	    return back;
	}
	if(season.isEmpty()){
	    back = "Season is required";
	    addError(back);
	    return back;
	}
	if(request_year.isEmpty()){
	    back = "Request year is required";
	    addError(back);
	    return back;
	}
	if(request_date.isEmpty())
	    request_date = Helper.getToday2();	
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}		
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, season);
	    pstmt.setString(2, request_year);
	    if(program_id.equals(""))
		pstmt.setString(3,null);
	    else
		pstmt.setString(3,program_id);
	    if(facility_id.equals(""))
		pstmt.setString(4,null);
	    else
		pstmt.setString(4,facility_id);
	    if(lead_id.equals(""))
		pstmt.setString(5,null);
	    else
		pstmt.setString(5,lead_id);	    
	    if(location_id.equals(""))
		pstmt.setString(6,null);
	    else
		pstmt.setString(6,location_id);
	    if(location_description.equals(""))
		pstmt.setString(7,null);
	    else
		pstmt.setString(7,location_description);			
	    if(content_specific.equals(""))
		pstmt.setString(8,null);
	    else
		pstmt.setString(8,content_specific);
	    if(request_type != null){
		String r_types = "";
		for(String str:request_type){
		    if(!r_types.isEmpty())  r_types +=",";
		    r_types += str;
		}
		pstmt.setString(9,r_types);
	    }
	    else
		pstmt.setString(9,null);
	    if(other_type.equals(""))
		pstmt.setString(10,null);
	    else
		pstmt.setString(10,other_type);
	    pstmt.setDate(11,new java.sql.Date(dateFormat.parse(request_date).getTime()));	    
	    if(orientation.equals(""))
		pstmt.setString(12,null);
	    else
		pstmt.setString(12,orientation);
	    if(notes.equals(""))
		pstmt.setString(13,null);
	    else
		pstmt.setString(13,notes);	    
	    pstmt.setString(14,id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    message += " Error updating the record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doSave(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String back = "";
	String qq = "insert into media_requests values (0,?,?,?,?, ?,?,?,?,?, ?,?,?,?)";
	if(request_date.isEmpty())
	    request_date = Helper.getToday2();
	if(request_year.isEmpty() || season.isEmpty()){ 
	    if(!program_id.isEmpty()){
		Program pp = new Program(debug, program_id);
		back = pp.doSelect();
		if(back.isEmpty()){
		    season = pp.getSeason();
		    request_year = pp.getYear();
		}
		else{
		    return back;
		}
	    }
	}
	if(lead_id.isEmpty()){
	    back = "Lead is required";
	    addError(back);
	    return back;
	}
	if(season.isEmpty()){
	    back = "Season is required";
	    addError(back);
	    return back;
	}
	if(request_year.isEmpty()){
	    back = "Request year is required";
	    addError(back);
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, season);
	    pstmt.setString(2, request_year);	    
	    if(program_id.equals(""))
		pstmt.setString(3,null);
	    else
		pstmt.setString(3,program_id);
	    if(facility_id.equals(""))
		pstmt.setString(4,null);
	    else
		pstmt.setString(4,facility_id);
	    if(lead_id.equals(""))
		pstmt.setString(5,null);
	    else
		pstmt.setString(5,lead_id);	    
	    if(location_id.equals(""))
		pstmt.setString(6,null);
	    else
		pstmt.setString(6,location_id);
	    if(location_description.equals(""))
		pstmt.setString(7,null);
	    else
		pstmt.setString(7,location_description);			
	    if(content_specific.equals(""))
		pstmt.setString(8,null);
	    else
		pstmt.setString(8,content_specific);
	    if(request_type == null)
		pstmt.setString(9,null);
	    else{
		String r_types = "";
		for(String str:request_type){
		    if(!r_types.isEmpty())  r_types +=",";
		    r_types += str;
		}
		pstmt.setString(9,r_types);
	    }
	    if(other_type.equals(""))
		pstmt.setString(10,null);
	    else
		pstmt.setString(10,other_type);
	    pstmt.setDate(11,new java.sql.Date(dateFormat.parse(request_date).getTime()));
	    if(orientation.equals(""))
		pstmt.setString(12,null);
	    else
		pstmt.setString(12,orientation);	    
	    if(notes.equals(""))
		pstmt.setString(13,null);
	    else
		pstmt.setString(13,notes);	    
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
	    message = " Error adding the record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }

    /**
       create table media_requests (
       id int auto_increment primary key,
       season enum('Fall/Winter','Winter/Spring','Summer','Ongoing'),
       request_year int,
       program_id int,
       facility_id int,
       lead_id int,
       location_id int,
       location_description varchar(1024),
       content_specific varchar(1024),
       request_type varchar(1024),
       other_type varchar(512),
       request_date date,
       orientation enum('Vertical','Horizental','Both'),
       notes varchar(1024),
       foreign key(program_id) references programs(id),
       foreign key(lead_id) references leads(id),
       foreign key(location_id) references locations(id),
       foreign key(facility_id) references facilities(id)
       )engine=InnoDB;

       alter table media_requests add season enum('Fall/Winter','Winter/Spring','Summer','Ongoing') after id;
       alter table media_requests add request_year int after season;
       alter table media_requests add orientation enum('Vertical','Horizental','Both') after request_date;

       // marketing
       alter table marketing add sign_board char(1);
       alter table marketing add sign_board_date date;
       
     */
}





































