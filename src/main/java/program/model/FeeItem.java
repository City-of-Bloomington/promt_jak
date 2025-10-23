package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class FeeItem extends CommonInc{

    static Logger logger = LogManager.getLogger(FeeItem.class);
    String id="", fee_id="", budget_id="";
    int quantity = 0;
    double rate = 0;
    Type fee_type = null;
    //
    public FeeItem(boolean deb,
		   String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5,
		   String val6){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setBudget_id(val2);
	setFee_id(val3);
	setRate(val4);
	setQuantity(val5);
	fee_type = new Type(debug, val3, val6);
    }
    public FeeItem(boolean deb,
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
	setBudget_id(val2);
	setFee_id(val3);
	setRate(val4);
	setQuantity(val5);
    }	
    public FeeItem(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
	
	
    //
    public FeeItem(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public boolean equals(Object gg){
	boolean match = false;
	if (gg != null && gg instanceof FeeItem){
	    match = id.equals(((FeeItem)gg).id);
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
    public String getFee_id(){
	return fee_id;
    }	
    public int getQuantity(){
	return quantity;
    }
    public double getRate(){
	return rate;
    }
    public double getTotal(){
	return rate*quantity;
    }	
    public Type getFee_type(){
	return fee_type;
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
    public void setFee_id(String val){
	if(val != null)
	    fee_id = val.trim();
    }	
    public void setBudget_id(String val){
	if(val != null)
	    budget_id = val;
    }
    public void setQuantity(String val){
	if(val != null){
	    try{
		quantity= Integer.parseInt(val);
	    }catch(Exception ex){}
	}
    }
    public void setRate(String val){
	if(val != null){
	    try{
		rate = Double.parseDouble(val);
	    }catch(Exception ex){}
	}
    }
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into fee_items values(0,?,?,?,?)";
	if(fee_id.equals("") || budget_id.equals("")
	   || quantity == 0 || rate == 0){
	    back = "fee type or budget id not set";
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
	    pstmt.setString(2,fee_id);			
	    pstmt.setString(3,""+quantity);
	    pstmt.setString(4,""+rate);
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
	    qq = "update fee_items set fee_id=?, quantity=?, rate=? "+
		"where id=?";
			
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,fee_id);
	    pstmt.setString(2,""+quantity);
	    pstmt.setString(3,""+rate);			
	    pstmt.setString(4,id);			
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
	    qq = "delete from fee_items where id=?";
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
	String qq = "select s.budget_id,s.fee_id,s.quantity,s.rate,t.name "+
	    "from fee_items s,fee_types t where t.id=s.fee_id and id=?";
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
		setFee_id(str);
		quantity = rs.getInt(3);
		rate = rs.getDouble(4);
		String str2 = rs.getString(5);
		if(str != null && str2 != null){
		    fee_type = new Type(debug, str, str2);
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
