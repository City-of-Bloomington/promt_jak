package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;

public class History extends CommonInc{

    static Logger logger = LogManager.getLogger(History.class);
    String id="";
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
    String action="Created", type="Program", action_by="", date_time="";
    User user = null;
    //
    public History(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public History(boolean deb, // for saving
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
	setAction(val2);
	setType(val3);
	setAction_by(val4);
    }	
    public History(boolean deb, // from list
		   String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5,
		   String val6
		   ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setAction(val2);
	setType(val3);
	setAction_by(val4);
	setDate_time(val5);
	if(val6 != null){
	    user = new User(debug, action_by, null, null, val6, null);
	}
    }
	
	
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getAction(){
	return action;
    }
    public String getType(){
	return type;
    }
    public String getAction_by(){
	return action_by;
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
    public void setType(String val){
	if(val != null){
	    type = val;
	}
    }	
    public void setAction(String val){
	if(val != null)
	    action = val.trim();
    }
    public void setAction_by(String val){
	if(val != null)
	    action_by = val;
    }
    public void setDate_time(String val){
	if(val != null)
	    date_time = val.trim();
    }
    public User getUser(){
	if(user == null && !action_by.equals("")){
	    User one = new User(debug, action_by);
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
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "insert into history (id,action,type,action_by, date_time) "+
	    "values(?,?,?,?,now())";
	try{
	    pstmt = con.prepareStatement(qq);			
	    pstmt.setString(1, id);
	    pstmt.setString(2, action);
	    pstmt.setString(3, type);
	    pstmt.setString(4, action_by);
	    pstmt.executeUpdate();
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

}
