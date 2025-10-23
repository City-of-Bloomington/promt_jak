package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;


public class General extends CommonInc{

    static Logger logger = LogManager.getLogger(General.class);
    String id="";
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
    String season="", year="", date="", time="", days="", cost="",
	title="", description="", lead_id="", code="", codeNeed="";
    Type lead = null;
    List<Market> markets = null;
    HistoryList history = null;	
    //
    //
    public General(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
    public General(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public General(boolean deb,
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
		   String val11,
		   String val12,
		   String val13
		   ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setTitle(val2);
	setSeason(val3);
	setYear(val4);
	setLead_id(val5);
	setDate(val6);
	setTime(val7);
	setDays(val8);
	setCost(val9);
	setDescription(val10);
	if(val11 != null){
	    lead = new Type(debug, lead_id, val11);
	}
	setCode(val12);
	setCodeNeed(val13);
    }
	
    //
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof General){
	    match = id.equals(((General)gg).id);
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
    public String getTitle(){
	return title;
    }	
    public String getId(){
	return id;
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
    public String getDays(){
	return days;
    }
    public String getTime(){
	return time;
    }
    public String getCost(){
	return cost;
    }
    public String getLead_id(){
	return lead_id;
    }	
    public String getDescription(){
	return description;
    }
    public String getCode(){
	return code;
    }
    public String getCodeNeed(){
	return codeNeed;
    }		
    //
    // setters
    //
    public void setId(String val){
	if(val != null){
	    id = val;
	}
    }
    public void setSeason(String val){
	if(val != null && !val.equals("-1"))
	    season = val.trim();
    }
    public void setYear(String val){
	if(val != null && !val.equals("-1"))
	    year = val.trim();
    }
    public void setDate(String val){
	if(val != null)
	    date = val.trim();
    }
    public void setTime(String val){
	if(val != null)
	    time = val.trim();
    }
    public void setDays(String val){
	if(val != null)
	    days = val.trim();
    }	
    public void setTitle(String val){
	if(val != null)
	    title = val.trim();
    }
    public void setDescription(String val){
	if(val != null)
	    description = val.trim();
    }
    public void setCost(String val){
	if(val != null)
	    cost = val;
    }
    public void setLead_id(String val){
	if(val != null)
	    lead_id = val;
    }
    public void setCode(String val){
	if(val != null)
	    code = val;
    }
    public void setCodeNeed(String val){
	if(val != null){
	    codeNeed = val;
	}
    }		
    public Type getLead(){
	if(lead == null && !lead_id.equals("")){
	    Type lld = new Type(debug, lead_id,"","leads");
	    String back = lld.doSelect();
	    if(back.equals("")){
		lead = lld;
	    }
	}
	return lead;
    }
    public boolean hasHistory(){
	getHistory();
	return history != null;
    }
    public List<History> getHistory(){
	if(history == null && !id.equals("")){
	    HistoryList ones = new HistoryList(debug, id, "General");
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		history = ones;
	    }
	}
	return history;
    }
    public List<Market> getMarkets(){
	if(markets == null && !id.equals("")){
	    MarketList ml = new MarketList(debug, null, null, id);
	    String back = ml.findMarketForGenerals();
	    if(back.equals("") && ml.size() > 0){
		markets = ml;
	    }
	}
	return markets;
    }
    public boolean hasMarkets(){
	getMarkets();
	return markets != null && markets.size() > 0;
    }		
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	if(title.equals("")){
	    back = "Title is required";
	}
	if(year.equals("")){
	    if(!back.equals("")) back +=", ";
	    back += " year is required";
	}
	if(season.equals("")){
	    if(!back.equals("")) back +=", ";				
	    back += " season is required";
	}
	if(!back.equals("")){
	    addError(back);
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "insert into general_listings "+
	    "values(0,?,?,?,?,"+
	    "?,?,?,?,?,?,?)";						
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
	if(id.equals("")){
	    back = "id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "update general_listings set "+
	    "title=?,"+
	    "lead_id=?,date=?,ltime=?,days=?,cost=?,description=?,code=?, "+
	    "codeNeed=? where id = ? ";
	if(debug)
	    logger.debug(qq);
		
	try{
	    pstmt = con.prepareStatement(qq);
	    //
	    back += setParams(pstmt);
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
	doSelect();
	return back;

    }
    String setParams(PreparedStatement pstmt){
	String back = "";
	try{
	    int jj=1;
	    pstmt.setString(jj++,title);
	    if(id.equals("")){
		pstmt.setString(jj++,season);
		pstmt.setString(jj++,year);
	    }
	    if(lead_id.equals(""))
		pstmt.setNull(jj++,Types.INTEGER);
	    else
		pstmt.setString(jj++,lead_id);
	    if(date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else
		pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date).getTime()));   			
	    if(time.equals(""))
		pstmt.setNull(jj++,Types.INTEGER);
	    else
		pstmt.setString(jj++,time);
	    if(days.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,days);
	    if(cost.equals(""))
		pstmt.setNull(jj++,Types.DOUBLE);
	    else
		pstmt.setString(jj++,cost);
	    if(description.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,description);
	    if(code.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,code);
	    if(codeNeed.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");								
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
	    qq = "delete from general_listings where id=?";
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
		
	String qq = "select g.title,g.season,g.year,g.lead_id,"+
	    " date_format(g.date,'%m/%d/%Y'),g.ltime,g.days, "+			
	    " g.cost,g.description,l.name,g.code,g.codeNeed "+
	    " from general_listings g "+
	    "left join leads l on g.lead_id=l.id where g.id=? ";
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
		setTitle(rs.getString(1));
		setSeason(rs.getString(2));
		setYear(rs.getString(3));
		setLead_id(rs.getString(4));
		setDate(rs.getString(5));				
		setTime(rs.getString(6));
		setDays(rs.getString(7));
		setCost(rs.getString(8));
		setDescription(rs.getString(9));
		String str = rs.getString(10);
		if(str != null){
		    lead = new Type(debug, lead_id, str);
		}
		setCode(rs.getString(11));
		setCodeNeed(rs.getString(12));								
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
