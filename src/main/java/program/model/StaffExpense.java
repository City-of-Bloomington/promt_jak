package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;

public class StaffExpense extends CommonInc{

    static Logger logger = LogManager.getLogger(StaffExpense.class);
    String id="", staff_id="", budget_id="", type="Direct"; // direct/indirect
    double rate = 0, hours=0;
    int quantity = 0;
    Type staff_type = null;
    //
    public StaffExpense(boolean deb,
			String val,
			String val2,
			String val3,
			String val4,
			String val5,
			String val6,
			String val7,
			String val8){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setBudget_id(val2);
	setStaff_id(val3);
	setRate(val4);
	setHours(val5);
	setType(val6);
	setQuantity(val7);		
	staff_type = new Type(debug, val3, val8);
    }
    public StaffExpense(boolean deb,
			String val,
			String val2,
			String val3,
			String val4,
			String val5,
			String val6,
			String val7){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setBudget_id(val2);
	setStaff_id(val3);
	setRate(val4);
	setHours(val5);
	setType(val6);
	setQuantity(val7);		
    }	
    public StaffExpense(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
	
	
    //
    public StaffExpense(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public boolean equals(Object gg){
	boolean match = false;
	if (gg != null && gg instanceof StaffExpense){
	    match = id.equals(((StaffExpense)gg).id);
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
    public String getBudget_id(){
	return budget_id;
    }
    public String getStaff_id(){
	return staff_id;
    }
    public String getType(){
	return type;
    }	
    public double getRate(){
	return rate;
    }
    public double getHours(){
	return hours;
    }
    public double getTotal(){
	return rate*hours*quantity;
    }
    public int getQuantity(){
	return quantity;
    }		
	
    public Type getStaff_type(){
	return staff_type;
    }
    public boolean isDirect(){
	return type.equals("Direct");
    }
    public String toString(){
	String ret = "";
	return ret;
    }	
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val.trim();
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }	
    public void setStaff_id(String val){
	if(val != null)
	    staff_id = val.trim();
    }	
    public void setBudget_id(String val){
	if(val != null)
	    budget_id = val;
    }
    public void setRate(String val){
	if(val != null){
	    try{
		rate = Double.parseDouble(val);
	    }catch(Exception ex){}
	}
    }
    public void setHours(String val){
	if(val != null){
	    try{
		hours = Double.parseDouble(val);
	    }catch(Exception ex){}
	}
    }
    public void setQuantity(String val){
	if(val != null){
	    try{
		quantity = Integer.parseInt(val);
	    }catch(Exception ex){}
	}
    }	
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into staff_expenses values(0,?,?,?,?,?,?)";
	if(staff_id.equals("") ||
	   budget_id.equals("") ||
	   rate == 0 ||
	   hours == 0){
	    back = "supply type or budget id not set";
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
	    pstmt.setString(1,budget_id);
	    pstmt.setString(2,staff_id);			
	    pstmt.setString(3,""+rate);
	    pstmt.setString(4,""+hours);
	    pstmt.setString(5,type);
	    pstmt.setInt(6,quantity);
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
	if(id.equals("") || rate == 0 || hours == 0){
	    back = "rate not set ";
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
	    qq = "update staff_expenses set staff_id=?, rate=?, hours =?, type=?  , quantity=? "+
		"where id=?";
			
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,staff_id);
	    pstmt.setString(2,""+rate);
	    pstmt.setString(3,""+hours);
	    pstmt.setString(4,type);						
	    pstmt.setString(5,id);
	    pstmt.setString(6,""+quantity);
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
	    qq = "delete from staff_expenses where id=?";
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
	String qq = "select s.budget_id,s.staff_id,s.rate,s.hours,s.type,s.quantity,t.name "+
	    "from staff_expenses s,staff_types t where t.id=s.staff_id and id=?";
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
		setBudget_id(rs.getString(1));
		String str = rs.getString(2);
		setStaff_id(str);
		rate = rs.getDouble(3);
		hours = rs.getDouble(4);
		setType(rs.getString(5));
		String str2 = rs.getString(6);
		quantity = rs.getInt(7);
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
