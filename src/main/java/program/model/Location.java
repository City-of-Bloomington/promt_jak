package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class Location extends CommonInc{

    static Logger logger = LogManager.getLogger(Location.class);
    String id="", name="", active="",
	facility_id="", facility_name="", // facility related info
	statement="", schedule="", closings="", other="", lead_id="",
	location_url = "",
	type="";
    //
    public Location(){
	super();
    }
    public Location(boolean deb,
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
		    String val12
		    ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
	setFacility_id(val3);
	setActive(val4);
	setFacility_name(val5);
	setStatement(val6);
	setSchedule(val7);
	setClosings(val8);
	setOther(val9);
	setLead_id(val10);
	setType(val11);
	setLocation_url(val12);
    }					
    public Location(boolean deb, String val, String val2, String val3, String val4, String val5, String val6){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
	setFacility_id(val3);
	setActive(val4);
	setFacility_name(val5);
	setLocation_url(val6);
    }			
    public Location(boolean deb, String val, String val2, String val3, String val4, String val5){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
	setFacility_id(val3);
	setActive(val4);
	setLocation_url(val5);
    }	
    public Location(boolean deb, String val, String val2, String val3, String val4){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
	setFacility_id(val3);
	setLocation_url(val4);
    }
    public Location(boolean deb, String val, String val2,String val3){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
	setLocation_url(val3);
    }
    public Location(boolean deb, String val, String val2){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
    }				
    public Location(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
	
	
    //
    public Location(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof Location){
	    match = id.equals(((Location)gg).id);
	}
	return match;
    }
    public int hashCode(){
	int code = 37;
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
    public String getName(){
	return name;
    }
    public String getLocation_url(){
	return location_url;
    }		
    public String getActive(){
	return active;
    }
    public String getStatement(){
	return statement;
    }
    public String getSchedule(){
	return schedule;
    }
    public String getClosings(){
	return closings;
    }
    public String getOther(){
	return other;
    }
    public String getLead_id(){
	return lead_id;
    }
    public String getType(){
	return type;
    }		
    public String getFacility_id(){
	return facility_id;
    }		
    public String toString(){
	return name;
    }
    public String getFacility_name(){
	return facility_name;
    }				
    public boolean isActive(){
	return !active.equals("");
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val.trim();
    }
    public void setLocation_url(String val){
	if(val != null)
	    location_url = val.trim();
    }		
    public void setActive(String val){
	if(val != null)
	    active = val;
    }
    public void setSchedule(String val){
	if(val != null)
	    schedule = val;
    }
    public void setStatement(String val){
	if(val != null)
	    statement = val;
    }
    public void setClosings(String val){
	if(val != null)
	    closings = val;
    }
    public void setOther(String val){
	if(val != null)
	    other = val;
    }
    public void setLead_id(String val){
	if(val != null)
	    lead_id = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }		
    public void setFacility_id(String val){
	if(val != null)
	    facility_id = val;
    }
    public void setFacility_name(String val){
	if(val != null)
	    facility_name = val;
    }		

    public String doSave(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "";
	qq =  "insert into locations values(0,?,?,?,'y')";
	if(name.equals("")){
	    back = "name not set ";
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
	try{
	    pstmt = con.prepareStatement(qq);
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt.setString(1,name);
	    if(facility_id.equals(""))
		pstmt.setNull(2,Types.INTEGER);
	    else
		pstmt.setString(2, facility_id);
	    if(location_url.equals(""))
		pstmt.setNull(3,Types.VARCHAR);
	    else
		pstmt.setString(3, location_url);						
	    pstmt.executeUpdate();
	    //
	    // get the id of the new record
	    //
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);				
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }			
	    message="Saved Successfully";			
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(back.equals("")){
	    doSelect();
	}
	return back;
    }
    public String doUpdate(){
		
	String back = "";
	if(name.equals("")){
	    back = "name not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="";
	String qq = "";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    qq = "update locations set name=?,facility_id=?,location_url=?, active=? "+
		"where id=?";				
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setString(jj++,name);
	    if(facility_id.equals(""))
		pstmt.setNull(jj++,Types.INTEGER);
	    else
		pstmt.setString(jj++,facility_id);
	    if(location_url.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++, location_url);								
	    if(active.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    pstmt.setString(jj++,id);			
	    pstmt.executeUpdate();
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
	if(back.equals("")){
	    doSelect();
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
	    qq = "delete from locations where id=?";
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
	String qq = "select l.name,l.facility_id,l.active,f.name, "+
	    "f.statement,f.schedule,f.closings,f.other,f.lead_id,f.type, "+
	    " l.location_url "+
	    " from locations l left join facilities f on l.facility_id=f.id "+
	    "where l.id=?";
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
		setName(rs.getString(1));
		setFacility_id(rs.getString(2));
		setActive(rs.getString(3));
		setFacility_name(rs.getString(4));
		setStatement(rs.getString(5));
		setSchedule(rs.getString(6));
		setClosings(rs.getString(7));
		setOther(rs.getString(8));
		setLead_id(rs.getString(9));
		setType(rs.getString(10));
		setLocation_url(rs.getString(11));
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

}
