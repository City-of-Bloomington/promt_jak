package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;

public class Staff extends CommonInc{

    static Logger logger = LogManager.getLogger(Staff.class);
    String id="", plan_id="", staff_id="";
    Type staff_type = null;
    int quantity = 0;
    //
    public Staff(boolean deb,
		 String val,
		 String val2,
		 String val3,
		 String val4,
		 String val5){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setPlan_id(val2);
	setStaff_id(val3);
	setQuantity(val4);
	staff_type = new Type(debug, val3, val5);
    }
    public Staff(boolean deb,
		 String val,
		 String val2,
		 String val3,
		 String val4){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setPlan_id(val2);
	setStaff_id(val3);
	setQuantity(val4);
    }	
    public Staff(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
	
	
    //
    public Staff(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public boolean equals(Object gg){
	boolean match = false;
	if (gg != null && gg instanceof Staff){
	    match = id.equals(((Staff)gg).id);
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
    public String getPlan_id(){
	return plan_id;
    }
    public String getStaff_id(){
	return staff_id;
    }	
    public int getQuantity(){
	return quantity;
    }	
    public Type getStaff_type(){
	return staff_type;
    }
    public String toString(){
	String ret = "";
	if(staff_type != null){
	    ret = staff_type.getName();
	}
	else{
	    ret = staff_id;
	}
	return ret;
    }	
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val.trim();
    }
    public void setPlan_id(String val){
	if(val != null)
	    plan_id = val.trim();
    }	
    public void setStaff_id(String val){
	if(val != null)
	    staff_id = val;
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
	String qq = "insert into plan_staffs values(0,?,?,?)";
	if(staff_id.equals("") || plan_id.equals("") || quantity == 0){
	    back = "staff id or plan id not set";
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
	    pstmt.setString(1,plan_id);
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
	if(id.equals("") || quantity == 0){
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
	    qq = "update plan_staffs set staff_id=?, quantity=? "+
		"where id=?";
			
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,staff_id);
	    pstmt.setString(2,""+quantity);						
	    pstmt.setString(3,id);			
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
	    qq = "delete from plan_staffs where id=?";
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
	String qq = "select s.plan_id,s.staff_id,s.quantity,t.name "+
	    "from plan_staffs s,staff_types t where t.id=s.staff_id and id=?";
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
		setPlan_id(rs.getString(1));
		String str = rs.getString(2);
		setStaff_id(str);
		quantity = rs.getInt(3);
		String str2 = rs.getString(4);
		if(str != null && str2 != null){
		    staff_type = new Type(debug, str, str2);
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
