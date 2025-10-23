package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class Type extends CommonInc{

    static Logger logger = LogManager.getLogger(Type.class);
    String id="", name="",
	table_name="categories";//  areas, leads, locations
    String active = ""; // for certain types
    int column_count = 2; // the default for most tables;
    //
    public Type(){
	super();
    }
    public Type(boolean deb, String val, String val2, String val3, String val4){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
	setActive(val3);
	setTableName(val4);
    }	
    public Type(boolean deb, String val, String val2, String val3){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
	setTableName(val3);
    }
    public Type(boolean deb, String val, String val2){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
    }		
    public Type(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
	
	
    //
    public Type(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof Type){
	    match = id.equals(((Type)gg).id);
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
    public String getName(){
	return name;
    }
    public String getActive(){
	return active;
    }	
    public String toString(){
	return name;
    }
    public boolean isActive(){
	return !active.equals("");
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val.trim();
    }
    public void setName(String val){
	if(val != null)
	    name = val.trim();
    }
    public void setActive(String val){
	if(val != null)
	    active = val.trim();
    }	
    public void setTableName(String val){
	if(val != null)
	    table_name = val.trim();
    }	
    private String findFieldsCount(){
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select * "+
	    "from "+table_name+" ";
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
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		column_count = rs.getMetaData().getColumnCount();
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

    public String doSave(){
		
	String back = "";
	back = findFieldsCount();
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "";
	if(column_count == 2)
	    qq =  "insert into "+table_name+" values(0,?)";
	else
	    qq =  "insert into "+table_name+" values(0,?,'y')";			
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
	back = findFieldsCount();
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(column_count == 2)
		qq = "update "+table_name+" set name=? "+
		    "where id=?";
	    else
		qq = "update "+table_name+" set name=?,active=? "+
		    "where id=?";				
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setString(jj++,name);
	    if(column_count > 2){
		if(active.equals(""))
		    pstmt.setNull(jj++,Types.CHAR);
		else
		    pstmt.setString(jj++,"y");
	    }
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
	    qq = "delete from "+table_name+" where id=?";
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
	back = findFieldsCount();
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select * "+
	    "from "+table_name+" where id=?";
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
		setName(rs.getString(2));
		if(column_count > 2){
		    setActive(rs.getString(3));
		}
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
