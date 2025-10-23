package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;

public class Sponsor extends CommonInc{

    static Logger logger = LogManager.getLogger(Sponsor.class);
    String pid="", // program id
	id="", // sponsor id
	attendCount="",market="",
	monetary="",tangible="",services="",
	signage="",exhibitSpace="",tshirt="",
	comments="";
    Program prog = null;
    HistoryList history = null;			
    //
    public Sponsor(boolean deb){
	//
	// initialize
	//
	super(deb);
    }	
    public Sponsor(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
    public Sponsor(boolean deb,
		   String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5,
		   String val6,
		   String val7,
		   String val8,
		   String val9,
		   String val10,
		   String val11				   
		   ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setPid(val2);
	setAttendCount(val3);
	setMarket(val4);
	setMonetary(val5);
	setTangible(val6);
	setServices(val7);
	setSignage(val8);
	setExhibitSpace(val9);
	setTshirt(val10);
	setComments(val11);
    }
	
    //
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof Sponsor){
	    match = id.equals(((Sponsor)gg).id);
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
    public String getPid(){
	return pid;
    }	
    public String getAttendCount(){
	return attendCount;
    }	
    public String getMarket(){
	return market;
    }
    public String getMonetary(){
	return monetary;
    }
    public String getTangible(){
	return tangible;
    }
    public String getServices(){
	return services;
    }
    public String getSignage(){
	return signage;
    }
    public String getExhibitSpace(){
	return exhibitSpace;
    }
    public String getTshirt(){
	return tshirt;
    }
    public String getComments(){
	return comments;
    }
    public String getCategories(){ // for reports
	String ret = "";
	if(!monetary.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "Monetary";
	}
	if(!tangible.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "Tangible Goods";
	}
	if(!services.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "Services";
	}
	return ret;
    }
    public String getBenefits(){
	String ret = "";
	if(!monetary.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "Signage at Event";
	}
	if(!exhibitSpace.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "Exhibitor Space";
	}
	if(!tshirt.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "T-shirt";
	}
	return ret;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null){
	    id = val.trim();
	}
    }
    public void setPid(String val){
	if(val != null){
	    pid = val.trim();
	}
    }	
    public void setAttendCount(String val){
	if(val != null)
	    attendCount = val.trim();
    }
    // needed for new plan
    public void setMarket(String val){
	if(val != null)
	    market = val.trim();
    }
    // needed for new plan
    public void setMonetary(String val){
	if(val != null)
	    monetary = val;
    }
    public void setTangible(String val){
	if(val != null)
	    tangible = val;
    }
    public void setServices(String val){
	if(val != null)
	    services = val;
    }
    public void setSignage(String val){
	if(val != null){
	    signage = val.trim();
	}
    }
    public void setExhibitSpace(String val){
	if(val != null){
	    exhibitSpace = val.trim();
	}
    }
    public void setTshirt(String val){
	if(val != null){
	    tshirt = val.trim();
	}
    }
    public void setComments(String val){
	if(val != null){
	    comments = val.trim();
	}
    }
	
    public Program getProgram(){
	if(prog == null && !pid.equals("")){
	    Program one = new Program(debug, pid);
	    String back = one.doSelect();
	    if(back.equals("")){
		prog = one;
	    }
	}
	return prog;
    }
    public boolean hasHistory(){
	getHistory();
	return history != null;
    }
    public List<History> getHistory(){
	if(history == null && !id.equals("")){
	    HistoryList ones = new HistoryList(debug, id, "Sponsor");
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		history = ones;
	    }
	}
	return history;
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

	}		
	String qq = "insert into sponsors "+
	    "values(0,?,"+
	    "?,?,?,?,?,"+
	    "?,?,?,?)";						
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, pid);
	    setParams(pstmt, 2); // starting index
	    pstmt.executeUpdate();
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
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
    public String doUpdate(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "update sponsors set "+
	    "attendCount=?,market=?,monetary=?,tangible=?,"+
	    "services=?,signage=?,exhibitSpace=?,tshirt=?,comments=? "+
	    "where id = ? ";
	if(debug)
	    logger.debug(qq);
		
	try{
	    pstmt = con.prepareStatement(qq);
	    //
	    back += setParams(pstmt, 1);
	    pstmt.setString(10, id);
	    pstmt.executeUpdate();
	    message="Updated Successfully";
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
    String setParams(PreparedStatement pstmt, int jj){
	String back = "";
	try{
	    if(attendCount.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++, attendCount);
	    if(market.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,market);
	    if(monetary.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++, "y");
	    if(tangible.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(services.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(signage.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(exhibitSpace.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(tshirt.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(comments.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,comments);			
			
	}
	catch(Exception ex){
	    back += ex;
	    addError(back);
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
	    qq = "delete from sponsors where id=?";
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
		
	String qq = "select pid,attendCount,market,monetary,tangible,"+
	    " services,signage,exhibitSpace,tshirt,comments "+
	    " from sponsors where id=? ";
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
	    pstmt.setString(1, id); 
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setPid(rs.getString(1));
		setAttendCount(rs.getString(2));
		setMarket(rs.getString(3));
		setMonetary(rs.getString(4));
		setTangible(rs.getString(5));
		setServices(rs.getString(6));
		setSignage(rs.getString(7));
		setExhibitSpace(rs.getString(8));
		setTshirt(rs.getString(9));
		setComments(rs.getString(10));				
	    }
	    else{
		message = "No record found";
		back = message;
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
