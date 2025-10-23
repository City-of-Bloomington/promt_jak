package program.model;
import java.sql.*;
import java.text.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class MarketAd extends CommonInc{

    static Logger logger = LogManager.getLogger(MarketAd.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
    String id="", market_id="", type_id="",direct="", due_date="", details="";
    double expenses = 0;
    Type adType = null;
    //	
    public MarketAd(){
	super();
    }	
    public MarketAd(boolean deb,
		    String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6,
		    String val7,
		    String val8
		    ){
	//
	// initialize
	//
	super(deb);
	setType_id(val);
	adType = new Type(debug, val, val2);
	setId(val3);
	setMarket_id(val4);
	setDirect(val5);
	setExpenses(val6);
	setDue_date(val7);
	setDetails(val8);
    }
    public MarketAd(boolean deb, MarketAd one){
	//
	// for duplication
	//
	super(deb);
	setType_id(one.getType_id());
	setDirect(one.getDirect());
	setExpenses(""+one.getExpenses());
	setDetails(one.getDetails());
    }				
    public MarketAd(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }		
	
	
    //
    public MarketAd(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof MarketAd){
	    match = id.equals(((MarketAd)gg).id);
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
    public String getDirect(){
	return direct;
    }
    public String getMarket_id(){
	return market_id;
    }	
    public String getType_id(){
	return type_id;
    }
    public String getDue_date(){
	return due_date;
    }	
    public double getExpenses(){
	return expenses;
    }
    public String getDetails(){
	return details;
    }		
    public boolean isDirect(){
	return !direct.equals("");
    }
    public String toString(){
	if(adType != null)
	    return adType.toString();
	else
	    return market_id;
    }
    public Type getType(){
	return adType;
    }
    public boolean isValid(){
	if(type_id.equals("") ||
	   market_id.equals("")
	   ) return false;
	return true;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val.trim();
    }
    public void setType_id(String val){
	if(val != null)
	    type_id = val.trim();
    }
    public void setMarket_id(String val){
	if(val != null)
	    market_id = val.trim();
    }	
    public void setDirect(String val){
	if(val != null)
	    direct = val;
    }
    public void setDetails(String val){
	if(val != null)
	    details = val;
    }	
    public void setDue_date(String val){
	if(val != null)
	    due_date = val;
    }	
    public void setExpenses(String val){
	if(val != null){
	    try{
		expenses = Double.parseDouble(val);
	    }catch(Exception ex){}
	}
    }	

    public String doSave(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into market_ad_details values(0,?,?,?,?,?,?)";
	if(market_id.equals("")){
	    back = "market id not set ";
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
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,market_id);
	    pstmt.setString(2, type_id);
	    if(direct.equals(""))
		pstmt.setNull(3,Types.CHAR);
	    else
		pstmt.setString(3,"y");
	    pstmt.setString(4,""+expenses);
	    if(due_date.equals(""))
		pstmt.setNull(5,Types.DATE);
	    else
		pstmt.setDate(5,new java.sql.Date(dateFormat.parse(due_date).getTime()));
	    if(details.equals(""))
		pstmt.setNull(6,Types.VARCHAR);
	    else
		pstmt.setString(6,details);			
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
	    back = "id not set ";
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
	    qq = "update market_ad_details set type_id=?, "+
		"direct = ?,expenses=?,due_date=?,details=? "+
		"where id=?";
			
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,type_id);
	    if(direct.equals(""))
		pstmt.setNull(2,Types.CHAR);
	    else
		pstmt.setString(2,"y");
	    pstmt.setString(3,""+expenses);
	    if(due_date.equals(""))
		pstmt.setNull(4, Types.DATE);
	    else
		pstmt.setDate(4, new java.sql.Date(dateFormat.parse(due_date).getTime()));
	    if(details.equals(""))
		pstmt.setNull(5,Types.VARCHAR);
	    else
		pstmt.setString(5,details);				
	    pstmt.setString(6,id);			
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
	    qq = "delete from market_ad_details where id=?";
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
	String qq = "select a.id,a.name,d.market_id,d.direct,"+
	    "d.expenses,date_format(d.due_date,'%m/%d/%Y'),d.details "+
	    "from market_ads a, market_ad_details d where a.id=type_id and d.id=?";
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
		setType_id(rs.getString(1));
		adType = new Type(debug, rs.getString(1),rs.getString(2));
		setMarket_id(rs.getString(3));
		setDirect(rs.getString(4));
		setExpenses(rs.getString(5));
		setDue_date(rs.getString(6));
		setDetails(rs.getString(7));
	    }
	    else{
		back= "Record "+id+" not found";
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
