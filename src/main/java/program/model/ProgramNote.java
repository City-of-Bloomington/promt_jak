package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;

public class ProgramNote extends CommonInc{

    static Logger logger = LogManager.getLogger(ProgramNote.class);
    String id="";
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
    String notes="", added_by="", date_time="", program_id="";
    User user = null;
    //
    public ProgramNote(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public ProgramNote(boolean deb, // for saving
		       String val,
		       String val2,
		       String val3,
		       String val4
		   ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setProgram_id(val2);
	setNotes(val3);
	setAdded_by(val4);
    }	
    public ProgramNote(boolean deb, // from list
		       String val,
		       String val2,
		       String val3,
		       String val4,
		       String val5
		   ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setProgram_id(val2);
	setNotes(val3);
	setAdded_by(val4);
	setDate_time(val5);
	if(val4 != null){
	    user = new User(debug, added_by);
	    String back = user.doSelect();
	}
    }
	
	
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getProgram_id(){
	return program_id;
    }    
    public String getNotes(){
	return notes;
    }
    public String getAdded_by(){
	return added_by;
    }
    public String getDate_time(){
	return date_time;
    }

    //
    // setters
    //
    public void setId(String val){
	if(val != null){
	    id = val;
	}
    }
    public void setProgram_id(String val){
	if(val != null){
	    program_id = val;
	}
    }    
    public void setNotes(String val){
	if(val != null)
	    notes = val.trim();
    }
    public void setAdded_by(String val){
	if(val != null)
	    added_by = val;
    }
    public void setDate_time(String val){
	if(val != null)
	    date_time = val.trim();
    }
    public User getUser(){
	if(user == null && !added_by.equals("")){
	    User one = new User(debug, added_by);
	    String back = one.doSelect();
	    if(back.equals("")){
		user = one;
	    }
	}
	return user;
    }	
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	if(notes.isEmpty()){
	    back = "No notes to be saved ";
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "insert into program_notes "+
	    "values(0,?,?,?,now())";
	try{
	    pstmt = con.prepareStatement(qq);			
	    pstmt.setString(1, program_id);
	    pstmt.setString(2, notes);
	    pstmt.setString(3, added_by);
	    pstmt.executeUpdate();
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt2 = con.prepareStatement(qq);
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	    
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}
	if(!id.isEmpty()){
	    doSelect();
	}
	return back;
    }
    public String doUpdate(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	if(notes.isEmpty()){
	    back = "No notes to be saved ";
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "update program_notes set notes=?,added_by=?, date_time=now() where id=? ";
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, notes);
	    pstmt.setString(2, added_by);
	    pstmt.setString(3, id);
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
    public String doDelete(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	if(notes.isEmpty()){
	    back = "No notes to be saved ";
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "delete from program_notes where id=? ";
	try{
	    pstmt = con.prepareStatement(qq);			
	    pstmt.setString(1, id);
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
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select h.id,h.program_id,h.notes,h.added_by,date_format(h.date_time,'%m/%d/%Y %h:%i') from program_notes h where h.id=? ";			
	String qw = "";
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
	    int j=1;
	    if(!id.equals("")){
		pstmt.setString(j++, id);
	    }
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setProgram_id(rs.getString(2));
		setNotes(rs.getString(3));
		setAdded_by(rs.getString(4));
		setDate_time(rs.getString(5));
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    message += back;
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
       create table program_notes (
       id int auto_increment primary key,
       program_id int,
       notes text,
       added_by int,
       date_time datetime,
       foreign key(program_id) references programs(id),
       foreign key(added_by) references users(id)
       )engine=InnoDB;   

 */
