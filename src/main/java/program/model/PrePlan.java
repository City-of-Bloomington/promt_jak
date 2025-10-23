package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;

public class PrePlan extends CommonInc{

    static Logger logger = LogManager.getLogger(PrePlan.class);
    String id="", prev_id =""; // id is ahared with plan id
    String season="", year="",determinants="", pre_eval_text="",
	market_considers="",fulfilled="",date="", explained="";
    //
    String name="", lead_id=""; // program_name for new Plan
    Lead lead = null;
    //
    Plan plan = null;
    //
    public PrePlan(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
	plan = new Plan(debug, val);
	plan.doSelect();
    }
    public PrePlan(boolean deb){
	//
	// initialize
	//
	super(deb);
    }	
    //
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof Type){
	    match = id.equals(((PrePlan)gg).id);
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
    public String getPrev_id(){
	return prev_id;
    }
    public String getSeason(){
	return season;
    }
    public String getYear(){
	return year;
    }
    public String getDate(){
	return date;
    }
    public String getLead_id(){
	return lead_id;
    }
    public Lead getLead(){
	if(lead == null && !lead_id.equals("")){
	    Lead one = new Lead(debug, lead_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		lead = one;
	    }
	}
	return lead;
    }		
    public String getDeterminants(){
	return determinants;
    }
    public String getMarket_considers(){
	return market_considers;
    }		
    public String getFulfilled(){
	return fulfilled;
    }
    public boolean isFulfilled(){
	return !fulfilled.equals("");
    }
    public String getPre_eval_text(){
	return pre_eval_text;
    }
    public String getExplained(){
	return explained;
    }	
    public String getName(){
	return name;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null){
	    id = val.trim();
	    if(plan == null)
		plan = new Plan(debug, val);
	}
    }
    public void setSeason(String val){
	if(val != null)
	    season = val.trim();
    }
    public void setYear(String val){
	if(val != null)
	    year = val.trim();
    }
    public void setDate(String val){
	if(val != null)
	    date = val.trim();
    }
    // needed for new plan
    public void setName(String val){
	if(val != null)
	    name = val.trim();
    }
    // needed for new plan
    public void setLead_id(String val){
	if(val != null)
	    lead_id = val;
    }
    public void setExplained(String val){
	if(val != null)
	    explained = val;
    }	
    public void setDeterminants(String[] vals){
	determinants = "";
	if(vals != null){
	    for(String str:vals){
		if(!determinants.equals("")) determinants += ","; 
		determinants += str;
	    }
	}
    }	
    public void setPre_eval_text(String val){
	if(val != null)
	    pre_eval_text = val.trim();
    }
    public void setPrev_id(String val){
	if(val != null)
	    prev_id = val.trim();
    }
    public void setMarket_considers(String[] vals){
	market_considers = "";
	if(vals != null){
	    for(String str:vals){
		if(!market_considers.equals("")) market_considers +=",";
		market_considers += str;
	    }
	}
    }
    public void setFulfilled(String val){
	if(val != null)
	    fulfilled = val.trim();
    }	

    Program getLastProgram(){
	Program lastProgram = null;
	if(lastProgram == null && !id.equals("")){
	    ProgramList pl = new ProgramList(debug);
	    pl.setPlan_id(id);
	    pl.setSortby("id Desc");
	    pl.find();
	    if(pl.size() > 0){
		lastProgram = pl.get(0);
	    }
	}
	return lastProgram;
    }
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//
	// check if all data are entered
	//
	checkForFulfillment();		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}

	String qq = "insert into pre_plans "+
	    "values(0,?,?,?,?,?,?,?,now(),?,?,?)";						
	try{
	    pstmt = con.prepareStatement(qq);			
	    setParams(pstmt);
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
	    logger.error(qq);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}		
	return back;
    }
    void checkForFulfillment(){
	//
	if(!determinants.equals("1,2,3,4,5,6")) return;
	if(!market_considers.equals("1,2,3,4")) return;
	if(pre_eval_text.equals("")) return;
	if(year.equals("")) return;
	if(season.equals("")) return;
	if(explained.equals("")) return;
	// if all these conditions are satisfied we set
	// the fulfilled flag to on
	fulfilled = "y";
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
	checkForFulfillment();
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "update pre_plans set prev_id=?,"+
	    "determinants=?,pre_eval_text=?,market_considers=?,"+
	    "fulfilled=?,season=?,year=?,explained=?,name=?,lead_id=? "+
	    "where id = ? ";
	if(debug)
	    logger.debug(qq);
		
	try{
	    pstmt = con.prepareStatement(qq);
	    //
	    back += setParams(pstmt);
	    pstmt.setString(11, id);
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
    String setParams(PreparedStatement pstmt){
	String back = "";
	try{
	    int jj=1;
	    // pstmt.setString(jj++,id);
	    if(prev_id.equals(""))
		pstmt.setNull(jj++,Types.INTEGER);
	    else
		pstmt.setString(jj++,prev_id);
	    if(determinants.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,determinants);
	    if(pre_eval_text.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,pre_eval_text);
	    if(market_considers.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,market_considers);
	    if(fulfilled.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");			
	    if(season.equals(""))
		pstmt.setNull(jj++,Types.INTEGER);
	    else
		pstmt.setString(jj++,season);
	    if(year.equals(""))
		pstmt.setNull(jj++,Types.INTEGER);
	    else
		pstmt.setString(jj++,year);
	    if(explained.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,explained);
	    if(name.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,name);
	    if(lead_id.equals(""))
		pstmt.setNull(jj++,Types.INTEGER);
	    else
		pstmt.setString(jj++,lead_id);						
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
	    qq = "delete from pre_plans where id=?";
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
    //
    public String doSelect(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = "select prev_id,determinants,pre_eval_text,"+
	    " market_considers,fulfilled,season,year, "+
	    " date_format(date,'%m/%d/%Y'),explained,name,lead_id "+
	    " from pre_plans where id=? ";
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
		String str = rs.getString(1);
		if(str != null) prev_id = str; // prev plan
		str = rs.getString(2);
		if(str != null) determinants = str;
		str = rs.getString(3);
		if(str != null) pre_eval_text = str;
		str = rs.getString(4);
		if(str != null) market_considers = str;
		str = rs.getString(5);				
		if(str != null) fulfilled = str;
		str = rs.getString(6);
		if(str != null) season = str;
		str = rs.getString(7);
		if(str != null) year = str;
		str = rs.getString(8);
		if(str != null) date = str;
		str = rs.getString(9);
		if(str != null) explained = str;
		str = rs.getString(10);
		if(str != null) name = str;
		str = rs.getString(11);
		if(str != null) lead_id = str;
		message = "";
	    }
	    else{
		message = "No Pre Plan found";
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
