package program.model;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class User implements java.io.Serializable{

    String id="", username="", fullName="", dept="", role="", active="";
    boolean debug = false, userExists = false;
    static Logger logger = LogManager.getLogger(User.class);
    String errors = "";
    public User(String val){
	setId(val);
    }
    public User(boolean deb, String val){
	debug = deb;
	setId(val);
    }		
    public User(boolean deb, String val, String val2){
	debug = deb;
	setId(val);
	setUsername(val2);
    }
    public User(boolean deb, String val, String val2, String val3){
	debug = deb;
	setId(val);
	setUsername(val2);
	setFullName(val3);
    }		
    public User(boolean deb,
		String val, String val2, String val3, String val4, String val5){
	setId(val);
	setUsername(val2);
	setRole(val3);
	setFullName(val4);
	setActive(val5);
	debug = deb;
    }
    //
    public boolean hasRole(String val){
	if(role.indexOf(val) > -1) return true;
	return false;
    }
    //
    // getters
    //
    public String getUsername(){
	return username;
    }
    public String getId(){
	return id;
    }		
    public String getUserid(){
	return username;
    }
    public String getFullName(){
	return fullName;
    }
    public String getDept(){
	return dept;
    }
    public String getRole(){
	return role;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }		
    public void setUsername (String val){
	if(val != null)
	    username = val;
    }
    public void setFullName (String val){
	if(val != null)
	    fullName = val;
    }
    public void setRole (String val){
	if(val != null)
	    role = val;
    }
    public void setDept (String val){
	if(val != null)
	    dept = val;
    }
    public void setActive(String val){
	if(val != null)
	    active = val;
    }	
    public boolean userExists(){
	return userExists;
    }
    public boolean isActive(){
	return !active.equals("");
    }
    public boolean canEdit(){
	return role.indexOf("Edit") > -1;
    }
    public boolean canDelete(){
	return role.indexOf("Delete") > -1;
    }
    public boolean isAdmin(){
	return role.indexOf("Admin") > -1;
    }
    public String toString(){
	if(fullName == null) return username;
	else return fullName;
    }
    /**
     // user table has the following fields
     table users(
     userid varchar(9) not null primary key,
     role enum('Edit','Edit:Delete','Admin:Edit:Delete'),
     full_name varchar(50),
     active char(1) default 'y'
     )
    */
    public String doSelect(){
	String msg="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;					
	String qq = "select * from users where ";
	if(!id.equals("")){
	    qq += " id=? ";
	}
	else if(!username.equals("")){
	    qq += " userid=? ";
	}
	else {
	    msg = "user id or username not set ";
	    return msg;
	}
	con = Helper.getConnection();
	if(con == null){
						
	    msg = " Could not connect to database ";
	    return msg;
	}
	try{
	    if(debug)
		logger.debug(qq);
	    pstmt = con.prepareStatement(qq);
	    if(!id.equals("")){
		pstmt.setString(1, id);
	    }
	    else
		pstmt.setString(1, username);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setId(rs.getString(1));
		setUsername(rs.getString(2));
		setRole(rs.getString(3));
		setFullName(rs.getString(4));	
		setActive(rs.getString(5));	
		userExists = true;
	    }
	    else{
		msg = "User not found";
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += " "+ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
}
