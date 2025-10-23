package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class Outcome extends CommonInc{

    static Logger logger = LogManager.getLogger(Outcome.class);
    String id="", eval_id="", obj_id="", outcome="";
    Objective objective = null;
    //
    public Outcome(boolean deb,
		   String val,
		   String val2,
		   String val3,
		   String val4,
		   Objective val5){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setEval_id(val2);
	setObj_id(val3);		
	setOutcome(val4);
	setObjective(val5);
    }	
    public Outcome(boolean deb,
		   String val,
		   String val2,
		   String val3,
		   String val4){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setEval_id(val2);
	setObj_id(val3);		
	setOutcome(val4);
    }
    public Outcome(boolean deb,
		   String val,
		   String val2,
		   String val3){
	//
	// initialize
	//
	super(deb);
	setEval_id(val);
	setObj_id(val2);
	setOutcome(val3);
    }		
    public Outcome(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
	
	
    //
    public Outcome(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof Outcome){
	    match = id.equals(((Outcome)gg).id);
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
    public String getEval_id(){
	return eval_id;
    }
    public String getObj_id(){
	return obj_id;
    }
    public Objective getObjective(){
	if(objective == null && !obj_id.equals("")){
	    Objective one = new Objective(debug, obj_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		objective = one;
	    }
	}
	return objective;
    }	
    public String getOutcome(){
	return outcome;
    }
    public String toString(){
	return outcome;
    }	
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val.trim();
    }
    public void setEval_id(String val){
	if(val != null)
	    eval_id = val.trim();
    }
    public void setObj_id(String val){
	if(val != null)
	    obj_id = val.trim();
    }
    public void setObjective(Objective val){
	if(val != null)
	    objective = val;
    }	
    public void setOutcome(String val){
	if(val != null)
	    outcome = val.trim();
    }


    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into eval_outcomes values(0,?,?,?)";
	if(eval_id.equals("") || obj_id.equals("")){
	    back = "objective id or evaluation id not set";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	if(outcome.equals("")) return "";
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
	    pstmt.setString(1,eval_id);
	    pstmt.setString(2,obj_id);			
	    pstmt.setString(3,outcome);
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
	if(outcome.equals("")){
	    back = "outcome not set ";
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
	    qq = "update eval_outcomes set outcome=? "+
		"where id=?";
			
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,outcome);
	    pstmt.setString(2,id);			
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
	    qq = "delete from eval_outcomes where id=?";
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
	String qq = "select eval_id,obj_id,outcome "+
	    "from eval_outcomes where id=?";
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
		setEval_id(rs.getString(1));
		setObj_id(rs.getString(2));				
		setOutcome(rs.getString(3));
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
