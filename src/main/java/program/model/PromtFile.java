
package program.model;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.io.File;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
/**
 *
 */

public class PromtFile{
	
    boolean debug;
    static Logger logger = LogManager.getLogger(PromtFile.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id="", related_id ="", errors="",
	type="", // Program, Plan, Marketing, Facility, Other
	notes="", name="", old_name="", added_by_id="",
	date="";
    User addedBy = null;
    //
    //
    // basic constructor
    public PromtFile(boolean deb){

	debug = deb;
	//
	// initialize
	//
    }
    public PromtFile(boolean deb, String val){

	debug = deb;
	//
	// initialize
	//
	setId(val);
    }
    public PromtFile(boolean deb,
		     String val,
		     String val2,
		     String val3,
		     String val4,
		     String val5,
		     String val6,
		     String val7,
		     String val8
		     ){

	debug = deb;
	setId(val);
	setRelated_id(val2);
	setType(val3);				
	setAdded_by_id(val4);
	setDate(val5);		
	setName(val6);
	setOldName(val7);				
	setNotes(val8);
    }	
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setRelated_id(String val){
	if(val != null)		
	    related_id = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }		
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setAdded_by_id(String val){
	if(val != null)
	    added_by_id = val;
    }	
    public void setOldName(String val){
	if(val != null)
	    old_name = val.replace(" ","_");
    }
    public void setAddedBy(User val){
	if(val != null){
	    addedBy = val;
	    added_by_id = addedBy.getId();
	}
    }
    //
    // getters
    //
    public String  getId(){
	return id;
    }
    public String  getRelated_id(){
	return related_id;
    }
    public String  getNotes(){
	return notes;
    }
    public String  getType(){
	return type;
    }		
    public String  getName(){
	return name;
    }
		
    public String  getOldName(){
	return old_name;
    }
    public String  getAdded_by_id(){
	return added_by_id;
    }
    public User getAddedBy(){
	if(addedBy == null && !added_by_id.equals("")){
	    User one = new User(debug, added_by_id);
	    String back = one.doSelect();
	    if(back.equals(""))
		addedBy = one;
	}
	return addedBy;
    }
    public String  getDate(){
	if(id.equals("")){
	    date = Helper.getToday2(); //mm/dd/yyyy format
	}
	return date;
    }
    public boolean hasNotes(){
	return !notes.equals("");
    }
    public String  getErrors(){
	return errors;
    }
    public String getFullPath(String dir, String ext){
	String path = dir;
	String yy="", separator="/"; // linux
	separator = File.separator;
	if(name.equals("")){
	    if(id.equals("")){
		composeName(ext);
	    }
	    else{
		doSelect();
	    }
	}
	if(date.equals("")){
	    yy = ""+Helper.getCurrentYear();
	}
	else{
	    yy = date.substring(6);
	}
	path += yy;
	path += separator;
	File myDir = new File(path);
	if(!myDir.isDirectory()){
	    myDir.mkdirs();
	}
	return path;
    }

    /**
     * for download purpose
     */
    public String getPath(String dir){
	String path = dir;
	String yy="", separator="/"; // linux
	separator = File.separator;
	if(!date.equals("")){
	    yy = date.substring(6); // year 4 digits
	}
	if(!yy.equals("")){
	    path += yy;
	}
	path += separator;
	return path;
    }	
    public String composeName(String ext){
	String back = getNewIndex();
	if(back.equals("")){
	    name = "promt_"+type+"_"+related_id+"_"+id+"."+ext;
	    date = Helper.getToday2();
	}
	return back;
    }
    public String getNewIndex(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	date = Helper.getToday2();  // mm/dd/yyyy format
	String qq = "insert into promt_file_seq values(0)";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.executeUpdate();
	    //
	    // get the id of the new record
	    //
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
	    back += ex;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}
	return back;

    }
    public String doSave(){

	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into promt_files values(?,?,?,?,now(),?,?,?)";
	date = Helper.getToday2();
	if(name.equals("")){
	    back = "File name not set ";
	    logger.error(back);
	    return back;
	}
	if(type.equals("")){
	    back = "Related link type not set ";
	    logger.error(back);
	    return back;
	}				
	if(related_id.equals("")){
	    back = "Related id not set ";
	    logger.error(back);
	    return back;
	}
				
	if(old_name.equals("")){
	    old_name = name;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}
	if(debug){
	    logger.debug(qq);
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);		
	    pstmt.setString(2,related_id);
	    pstmt.setString(3,type);								
	    if(added_by_id.equals(""))
		pstmt.setNull(4,Types.INTEGER);
	    else
		pstmt.setString(4,added_by_id);				
	    pstmt.setString(5,name);
	    pstmt.setString(6,old_name);								
	    if(notes.equals(""))
		pstmt.setNull(7,Types.VARCHAR);
	    else
		pstmt.setString(7,notes);

	    pstmt.executeUpdate();
	    //
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
    public String doUpdate(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	date = Helper.getToday2();
	String qq = "update promt_files set notes=? ";
	if(!name.equals("")){
	    qq += ", name = ? ";
	}
	qq += " where id=? ";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    if(debug){
		logger.debug(qq);
	    }
	    int jj=1;
	    pstmt.setString(jj++,notes);
	    if(!name.equals("")){
		pstmt.setString(jj++,name);
	    }
	    pstmt.setString(jj,id);
	    pstmt.executeUpdate();
	    back += doSelect();
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
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
	    return back;
	}
	try{
	    qq = "delete from promt_files where id=?";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    name =  "";old_name = ""; notes="";
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;

    }
	
    //
    public String doSelect(){
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,"+
	    " related_id,type,added_by_id,"+
	    " date_format(date,'%m/%d/%Y'), "+						
	    " name,old_name,notes "+
	    " from promt_files where id=?";		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
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
		setRelated_id(rs.getString(2));
		setType(rs.getString(3));
		setAdded_by_id(rs.getString(4));
		setDate(rs.getString(5));
		setName(rs.getString(6));								
		setOldName(rs.getString(7));
		setNotes(rs.getString(8));
	    }
	    else{
		return "Record "+id+" Not found";
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);			
	}
	return back;
    }	

}
/**

alter table promt_files modify type  enum('Program','Plan','Marketing','Facility','Other','Evaluation'); 
   


 */





















































