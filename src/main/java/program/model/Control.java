package program.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;

public class Control{

    boolean debug = false;
    String id = "";
    Set editors = new HashSet();
    static Logger logger = LogManager.getLogger(Control.class);
    String owner = "";
    String userid = "";
    public User user = null;
    /**
     * Constructer
     *
     * @param debug debugging flag
     * @param id 
     */
    public Control(boolean debug, String id){
	this.debug = debug;
	setId(id);
    }
    /**
     * another constructer
     *
     * @param debug debugging flag
     * @param id 
     * @param userid 
     */
    public Control(boolean debug, String id, User usr){
	this.debug = debug;
	setId(id);
	setUser(usr);
    }
    /**
     * another constructer
     *
     * @param debug debugging flag
     * @param id 
     * @param userid 
     */
    public Control(boolean debug, User usr){
	this.debug = debug;
	setUser(usr);
    }
    /**
     * seters
     */ 
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setUserid(String val){
	if(val != null)
	    userid = val;
    }
    public void setUser(User val){
	if(val != null){
	    user = val;
	    setUserid(user.getUsername());
	}
    }
    /**
     * saving functions
     */
    public String doSaveAsOwner(){
	return doSave("owner");
    }
    public String doSaveAsEditor(){
	return doSave("editor");
    }
    /**
     * saving data
     */
    public String doSave(String type){
		
	String qq = "", msg = "";
	if(id.equals("") || userid.equals("")){
	    return "program id or userid not set";
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	qq = " insert into programs_access values(?,?,?)";
	if(debug)
	    logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.setString(2,userid);
	    if(type.equals("owner")){
		pstmt.setString(3,"owner");
		owner = userid;
	    }
	    else{
		pstmt.setString(3,"editor");
		editors.add(userid);
	    }
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg = " could not save "+ex;
	    logger.error(ex+" : "+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;		
    }
    /**
     * delete all (owners and editors)
     *
     */
    public String doDeleteAll(){
	return doDelete("");
    }
    /**
     * remove one of the editors from the list
     */
    public String doDeleteEditor(String usrid){
	return doDelete(usrid);
    }
    /**
     * remove certain or all record for certain program
     */
    public String doDelete(String usrid){
	//
		
	String qq = "", msg = "";
	if(id.equals("") || userid.equals("")){
	    return "program id or userid not set";
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;			
	qq = " delete from programs_access where id=?";
	if(!usrid.equals(""))
	    qq += " and userid=?";
	if(debug)
	    logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    if(!usrid.equals(""))
		pstmt.setString(2,usrid);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg = " could not delete "+ex;
	    logger.error(ex+": "+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;		
    }
    //
    public String doSelect(){
	String qq = "", msg = "";
	if(id.equals("")){
	    return "program id not set";
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;			
	qq = " select * from programs_access where id=?";
	if(debug)
	    logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(2);
		String str2 = rs.getString(3);
		if(str2 != null && str != null){
		    if(str2.equals("owner")) owner = str;
		    else editors.add(str);
		}
	    }
	}
	catch(Exception ex){
	    msg = " could not retreive data "+ex;
	    logger.error(ex+" : "+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;		

    }
    /**
     * check if this user is in the set
     *
     */
    public boolean hasAccess(){
	return isEditor() || isOwner();
    }
    /**
     * check if the current user is in the editors list
     * if no editor list is available (backward compatible)
     * then everybody is editor
     */
    public boolean isEditor(){
	return isAdmin() || editors.size() == 0 || editors.contains(userid);
    }
    /**
     * check if the current user is the owner of this program
     * for old programs where no owner was assigned, everybody acts
     * as owner
     */
    public boolean isOwner(){
	return isAdmin() || owner.equals("") || owner.equals(userid);
    }
    /**
     * check if the user has admin role
     */
    public boolean isAdmin(){
	return user != null && user.isAdmin();
    }
		
}






















































