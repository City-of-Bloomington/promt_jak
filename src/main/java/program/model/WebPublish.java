package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;

public class WebPublish extends CommonInc{

    static Logger logger = LogManager.getLogger(WebPublish.class);
    String id="", date="", user_id="";
    String[] prog_ids = null;
    String[] prog_web_ids = null;
    User user = null;
    //
    public WebPublish(){
	super();
    }
    public WebPublish(boolean deb, String val, String val2, String val3, String val4){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setDate(val2);
	setUser_id(val3);
	if(val4 != null)
	    user = new User(debug, val3, null, val4);
    }		
    public WebPublish(boolean deb, String val, String val2, String val3){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setDate(val2);
	setUser_id(val3);
    }
		
    public WebPublish(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
    public WebPublish(boolean deb){
	//
	// initialize
	//
	super(deb);
    }		
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof WebPublish){
	    match = id.equals(((WebPublish)gg).id);
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
    public String getDate(){
	return date;
    }
    public String getUser_id(){
	return user_id;
    }	
    public String toString(){
	return id;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setUser_id(String val){
	if(val != null)
	    user_id = val;
    }	
    public void setProg_ids(String[] vals){
	if(vals != null)
	    prog_ids = vals;
    }
    public void setProg_web_ids(String[] vals){
	if(vals != null)
	    prog_web_ids = vals;
    }		
    public User getUser(){
	return user;
    }
    public String doSave(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "", qq2="";
	qq =  "insert into web_publishes values(0,now(),?)";
	qq2 = "insert into web_publish_programs values(?,?)";
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
	    pstmt.setString(1,user_id);
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
	    if(prog_ids != null){
		if(debug){
		    logger.debug(qq2);
		}
		pstmt = con.prepareStatement(qq2);		
		for(String str:prog_ids){
		    pstmt.setString(1, id);
		    pstmt.setString(2, str);
		    pstmt.executeUpdate();
		}
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

    public String doDelete(){
	String back = "", qq = "", qq2="", qq3="";
		
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3 = null;
	ResultSet rs = null;
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    qq = "update programs set noPublish='y' where id in (select prog_id from web_publish_programs where publish_id=?"; 
	    qq2 = "delete from web_publish_programs where publish_id=? ";
	    qq3 = "delete from web_publishes where id=?";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    if(debug){
		logger.debug(qq2);
	    }
	    pstmt2.setString(1,id);
	    pstmt2.executeUpdate();						
	    if(debug){
		logger.debug(qq3);
	    }
	    pstmt3.setString(1,id);
	    pstmt3.executeUpdate();						
	    message="Deleted Successfully";
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);
	}
	return back;
    }
    public String unPublish(){
				
	String back = "", qq = "", qq2="";
		
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2 = null, pstmt3=null;
	ResultSet rs = null;
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(prog_ids != null){
		qq = "delete from web_publish_programs where prog_id=? ";
		qq2 = "update programs set noPublish='y' where id=?";
		pstmt = con.prepareStatement(qq);
		pstmt2 = con.prepareStatement(qq2);						
		for(String str:prog_ids){
		    pstmt.setString(1, str);
		    pstmt.executeUpdate();
		    pstmt2.setString(1, str);
		    pstmt2.executeUpdate();								
		}
	    }
	    if(prog_web_ids != null){
		qq = "update programs set noPublish='y' where id=?";
		pstmt3 = con.prepareStatement(qq);
		for(String str:prog_web_ids){
		    pstmt3.setString(1, str);
		    pstmt3.executeUpdate();
		}								
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);			
	}
	return back;
						
    }

    public String doSelect(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select  date_format(date,'%m/%d/%Y'),user_id "+
	    "from web_publishes where id=?";
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
		setDate(rs.getString(1));
		setUser_id(rs.getString(2));
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
