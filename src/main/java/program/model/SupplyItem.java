package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;

public class SupplyItem extends CommonInc{

    static Logger logger = LogManager.getLogger(FeeItem.class);
    String id="", supply_id="", budget_id="", other="";
    double rate = 0;
    Type supply_type = null;
    //
    public SupplyItem(boolean deb,
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
	setSupply_id(val3);
	setRate(val4);
	setOther(val5);
	supply_type = new Type(debug, val3, val6);
    }
    public SupplyItem(boolean deb,
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
	setSupply_id(val3);
	setRate(val4);
	setOther(val5);
    }	
    public SupplyItem(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
	
	
    //
    public SupplyItem(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public boolean equals(Object gg){
	boolean match = false;
	if (gg != null && gg instanceof SupplyItem){
	    match = id.equals(((SupplyItem)gg).id);
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
    public String getSupply_id(){
	return supply_id;
    }	
    public String getOther(){
	return other;
    }
    public double getRate(){
	return rate;
    }	
    public Type getSupply_type(){
	return supply_type;
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
    public void setSupply_id(String val){
	if(val != null)
	    supply_id = val.trim();
    }	
    public void setBudget_id(String val){
	if(val != null)
	    budget_id = val;
    }
    public void setOther(String val){
	if(val != null){
	    other = val;
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
	String qq = "insert into supply_items values(0,?,?,?,?)";
	if(supply_id.equals("") || budget_id.equals("")
	   || rate == 0){
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
	    pstmt.setString(2,supply_id);			
	    pstmt.setString(3,""+rate);
	    if(other.equals(""))
		pstmt.setNull(4, Types.VARCHAR);
	    else
		pstmt.setString(4,other);
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
	if(id.equals("") || rate == 0){
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
	    qq = "update supply_items set supply_id=?, rate=?, other =?  "+
		"where id=?";
			
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,supply_id);
	    pstmt.setString(2,""+rate);
	    if(other.equals(""))
		pstmt.setNull(3, Types.VARCHAR);
	    else
		pstmt.setString(3,other);			
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
	    qq = "delete from supply_items where id=?";
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
	String qq = "select s.budget_id,s.supply_id,s.rate,s.other,t.name "+
	    "from supply_items s,supply_types t where t.id=s.supply_id and id=?";
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
		setSupply_id(str);
		rate = rs.getDouble(3);
		setOther(rs.getString(4));
		String str2 = rs.getString(5);
		if(str != null && str2 != null){
		    supply_type = new Type(debug, str, str2);
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
