package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class EvalStaff extends CommonInc{

    static Logger logger = LogManager.getLogger(EvalStaff.class);
    String id="", eval_id="", staff_id="";
    Staff staff = null;
    int quantity = 0;
    //
    public EvalStaff(boolean deb,
		     String val,
		     String val2,
		     String val3,
		     String val4,
		     Staff val5){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setEval_id(val2);
	setStaff_id(val3);
	setQuantity(val4);
	setStaff(val5);
    }	
    public EvalStaff(boolean deb,
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
	setStaff_id(val3);
	setQuantity(val4);
    }
    public EvalStaff(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
	
	
    //
    public EvalStaff(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public boolean equals(Object gg){
	boolean match = false;
	if (gg != null && gg instanceof EvalStaff){
	    match = id.equals(((EvalStaff)gg).id);
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
    public String getStaff_id(){
	return staff_id;
    }	
    public int getQuantity(){
	return quantity;
    }	
    public Staff getStaff(){
	if(staff == null && !staff_id.equals("")){
	    Staff one = new Staff(debug, staff_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		staff = one;
	    }
	}
	return staff;
    }
    public String toString(){
	return ""+quantity;
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
    public void setStaff_id(String val){
	if(val != null)
	    staff_id = val;
    }
    public void setStaff(Staff val){
	if(val != null)
	    staff = val;
    }	
    public void setQuantity(String val){
	if(val != null){
	    try{
		quantity= Integer.parseInt(val);
	    }catch(Exception ex){}
	}
    }

    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into eval_staffs values(0,?,?,?)";
	if(staff_id.equals("") || eval_id.equals("")){
	    back = "staff id or evaluation id not set";
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
	    pstmt.setString(1,eval_id);
	    pstmt.setString(2,staff_id);			
	    pstmt.setString(3,""+quantity);
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
	if(id.equals("")){
	    back = "quantity not set ";
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
	    qq = "update eval_staffs set quantity=? "+
		"where id=?";
			
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,""+quantity);						
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
	    qq = "delete from eval_staffs where id=?";
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
	String qq = "select s.eval_id,s.staff_id,s.quantity "+
	    "from eval_staffs s where s.id=?";
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
		String str = rs.getString(2);
		setStaff_id(str);
		quantity = rs.getInt(3);
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
