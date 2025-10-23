package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;

public class VolTrain extends CommonInc{

    static Logger logger = LogManager.getLogger(VolTrain.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String pid="", // program id
	id="", // train id
	shift_id = "",
	date="",
	startTime="", endTime="", location_id="",
	other="", // other location
	tdays="",
	notes="";
    Program prog = null;
    VolShift shift = null;
    Type location = null;
    //
    public VolTrain(boolean deb){
	//
	// initialize
	//
	super(deb);
    }	
    public VolTrain(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
    public VolTrain(boolean deb,
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
		    String val11
		    ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setPid(val2);
	setShift_id(val3);
	setDate(val4);
	setStartTime(val5);
	setEndTime(val6);
	setLocation_id(val7);
	setOther(val8);
	setTdays(val9);		
	setNotes(val10);
	if(val11 != null){
	    location = new Type(debug, val7, val11);
	}
    }
	
    //
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof VolTrain){
	    match = id.equals(((VolTrain)gg).id);
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
    public String getPid(){
	return pid;
    }
    public String getShift_id(){
	return shift_id;
    }	
    public String getTdays(){
	return tdays;
    }
    public String getDays(){
	return tdays;
    }	
    public String getDate(){
	return date;
    }
    public String getStartTime(){
	return startTime;
    }
    public String getEndTime(){
	return endTime;
    }
    public String getLocation_id(){
	return location_id;
    }
    public Type getLocation(){
	if(location == null && !location_id.equals("")){
	    Type one = new Type(debug, location_id, "","locations");
	    String back = one.doSelect();
	    if(back.equals("")){
		location = one;
	    }
	}
	return location;
    }
    public String getNotes(){
	return notes;
    }
    public String getOther(){
	return other;
    }
    // setters
    //
    public void setId(String val){
	if(val != null){
	    id = val;
	}
    }
    public void setPid(String val){
	if(val != null){
	    pid = val;
	}
    }
    public void setShift_id(String val){
	if(val != null){
	    shift_id = val;
	}
    }	
    public void setTdays(String val){
	if(val != null){
	    tdays = val.trim();
	}
    }
    public void setDays(String val){
	if(val != null){
	    tdays = val.trim();
	}
    }	
    public void setDate(String val){
	if(val != null){
	    date = val.trim();
	}
    }	
    public void setStartTime(String val){
	if(val != null)
	    startTime = val.trim();
    }

    public void setEndTime(String val){
	if(val != null)
	    endTime = val;
    }
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }
    public void setLocation_id(String val){
	if(val != null){
	    location_id = val;
	}
    }
    public void setOther(String val){
	if(val != null){
	    other = val;
	}
    }
	
    public Program getProgram(){
	if(prog == null && !pid.equals("")){
	    Program one = new Program(debug, pid);
	    String back = one.doSelect();
	    if(back.equals("")){
		prog = one;
	    }
	}
	return prog;
    }
    public VolShift getShift(){
	if(shift == null && !shift_id.equals("")){
	    VolShift one = new VolShift(debug, shift_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		shift = one;
	    }
	}
	return shift;
    }	

    public String doSave(){

	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);

	}		
	String qq = "insert into vol_trainings "+
	    "values(0,?,?,"+
	    "?,?,?,?,?,"+
	    "?,?)";						
	try{
	    pstmt = con.prepareStatement(qq);
	    if(!pid.equals(""))
		pstmt.setString(1, pid);
	    else
		pstmt.setNull(1, Types.INTEGER);				
	    pstmt.setString(2, shift_id);
	    setParams(pstmt, 3); // starting index
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
	    message="Saved Successfully";
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
    public String doDuplicate(String program_id, String shift_id2){
	id="";date="";
	pid=program_id;
	shift_id = shift_id2;
	return doSave();
    }
    public String doUpdate(){
		
	String back = "";
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
	String qq = "update vol_trainings set "+
	    "date=?,startTime=?,"+
	    "endTime=?,location_id=?,other=?,tdays=?,notes=? where id = ? ";
	if(debug)
	    logger.debug(qq);
		
	try{
	    pstmt = con.prepareStatement(qq);
	    //
	    back += setParams(pstmt, 1);
	    pstmt.setString(8, id);
	    pstmt.executeUpdate();
	    message="Updated Successfully";
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
    String setParams(PreparedStatement pstmt, int jj){
	String back = "";
	try{
	    if(date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else
		pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date).getTime()));			
	    if(startTime.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,startTime);
	    if(endTime.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,endTime);
			
	    if(location_id.equals(""))
		pstmt.setNull(jj++,Types.INTEGER);
	    else
		pstmt.setString(jj++, location_id);
	    if(other.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,other);			
	    if(tdays.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,tdays);
	    if(notes.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++, notes);
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
	    qq = "delete from vol_trainings where id=?";
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
		
	String qq = "select v.pid,v.shift_id,date_format(v.date,'%m/%d/%Y'),"+
	    " v.startTime,v.endTime,v.location_id,v.other,v.tdays,v.notes, "+
	    " l.name from vol_trainings v "+
	    " left join locations l on l.id=v.location_id "+
	    " where v.id=? ";
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
	    pstmt.setString(1, id); 
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setPid(rs.getString(1));
		setShift_id(rs.getString(2));
		setDate(rs.getString(3));
		setStartTime(rs.getString(4));
		setEndTime(rs.getString(5));				
		setLocation_id(rs.getString(6));
		setOther(rs.getString(7));				
		setTdays(rs.getString(8));
		setNotes(rs.getString(9));
		String str = rs.getString(10);
		if(str != null){
		    location = new Type(debug, rs.getString(6), str);
		}
	    }
	    else{
		message = "No record found";
		back = message;
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
