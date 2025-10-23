package program.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;


public class Facility extends CommonInc{

    boolean debug = false;
    public final static String[] facility_types = {"Facility","Park","Trail","Other"};
    String statement="", name="", schedule="";
    String closings="", other="", id="", lead_id="",
	type="";// Facility, Park, Trail, Other
    static Logger logger = LogManager.getLogger(Facility.class);
    Type lead = null;
    HistoryList history = null;
    Market market = null;
    MarketList markets = null;
    List<PromtFile> files = null;
    List<MediaRequest> mediaRequests = null;
    
    public Facility(boolean val){
	debug = val;
    }
    public Facility(boolean val, String val2){
	debug = val;
	setId(val2);
    }	
    public Facility(boolean deb,
	     String val,
	     String val2,
	     String val3,
	     String val4,
	     String val5,
	     String val6,
	     String val7,
	     String val8,
	     String val9
	     ){
	debug = deb;
	setId(val);
	setName(val2);
	setStatement(val3);
	setSchedule(val4);
	setClosings(val5);
	setOther(val6);
	setLead_id(val7);
	if(val8 != null){
	    lead = new Type(debug, lead_id, val8);
	}
	setType(val9);
    }
    //
    // setters
    //
    public void setStatement(String val){
	if(val != null)
	    statement = val;
    }
    public void setName (String val){
	if(val != null)
	    name = val;
    }	
    public void setSchedule (String val){
	if(val != null)
	    schedule = val;
    }
    public void setClosings(String val){
	if(val != null)
	    closings = val;
    }
    public void setOther (String val){
	if(val != null)
	    other = val;
    }
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }		
    public void setLead_id (String val){
	if(val != null)
	    lead_id = val;
    }	
	
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getType(){
	return type;
    }		
    public String getLead_id(){
	return lead_id;
    }	
    public String getStatement(){
	return statement;
    }
    public String getName(){
	return name;
    }
    public String getSchedule(){
	return schedule;
    }
    public String getClosings(){
	return closings;
    }
    public String getOther(){
	return other;
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
    public String toString(){
	return name;
    }
    public boolean hasHistory(){
	getHistory();
	return history != null;
    }
    public List<History> getHistory(){
	if(history == null && !id.equals("")){
	    HistoryList ones = new HistoryList(debug, id, "Facility");
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		history = ones;
	    }
	}
	return history;
    }
    public boolean hasFiles(){
	getFiles();
	return files != null && files.size() > 0;
    }		
    public List<PromtFile> getFiles(){
	if(files == null && !id.equals("")){
	    PromtFileList tsl = new PromtFileList(debug, id, "Facility");
	    String back = tsl.find();
	    if(back.equals("")){
		List<PromtFile> ones = tsl.getFiles();
		if(ones != null && ones.size() > 0){
		    files = ones;
		}
	    }
	}
	return files;
    }		
    public boolean hasMediaRequests(){
	findMediaRequests();
	return mediaRequests != null && mediaRequests.size() > 0;
    }
    public List<MediaRequest> getMediaRequests(){
	return mediaRequests;
    }
    private String findMediaRequests(){
	MediaRequestList mdl = new MediaRequestList(debug);
	mdl.setFacility_id(id);
	String back = mdl.find();
	if(back.isEmpty()){
	    mediaRequests = mdl.getRequests(); 
	}
	return back;
    }		
    public boolean hasMarket(){
	getMarket();
	return market != null;
    }
    public Market getMarket(){
	if(market == null && !id.equals("")){
	    getMarkets();
	}
	return market;
    }
    public List<Market> getMarkets(){
	MarketList ml = new MarketList(debug, null, id, null);
	String back = ml.find();
	if(back.equals("") && ml.size() > 0){
	    markets = ml;
	    market = markets.get(0);
	}
	return markets;
    }
    public Market findLatestMarket(){
	MarketList ml = new MarketList(debug, null, id, null);
	String back = ml.findLatestForFacility();
	if(back.isEmpty()){
	    if(ml.size() > 0){
		market = ml.get(0);
	    }
	}
	return market;
    }
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof Location){
	    match = id.equals(((Facility)gg).id);
	}
	return match;
    }
    public int hashCode(){
	int code = 31;
	try{
	    code = Integer.parseInt(id);
	}catch(Exception ex){};
	return code;
    }		
    //
    public String doDelete(){

	String message = "", back="";
	String qq = "delete from facilities where id=?";		
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();			
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    message = " Error deleting record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doSelect(){
	//
	String message = "", back="";
	String qq = "select f.name,"+
	    "f.statement,"+
	    "f.schedule,f.closings,"+
	    "f.other, f.lead_id,l.name,f.type "+
	    " from facilities f left join leads l on f.lead_id=l.id "+
	    " where f.id=?";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();			
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}		
	if(debug){
	    logger.debug(qq);
	}
		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		String str = rs.getString(1);
		if(str != null) name = str;
		//
		str = rs.getString(2);
		if(str != null) statement += str;
		str = rs.getString(3);
		if(str != null)
		    schedule = str;
		str = rs.getString(4);
		if(str != null) 
		    closings = str;
		str = rs.getString(5);
		if(str != null) 
		    other = str;
		setLead_id(rs.getString(6));
		str = rs.getString(7);
		if(str != null){ 
		    lead = new Type(debug, lead_id, str);
		}
		str = rs.getString(8);
		if(str != null){
		    type = str;
		}
				
	    }
	}
	catch(Exception ex){
	    message += " Error retreiving the record " +ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doUpdate(){
		
	String message = "", back="";
	String qq = "update facilities set "+
	    " name=?,statement=?,schedule=?,closings=?,other=?,"+
	    " lead_id=?,type=? "+
	    " where id=?";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();			
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}				
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    if(name.equals(""))
		pstmt.setString(1,null);
	    else
		pstmt.setString(1,name);
	    if(statement.equals(""))
		pstmt.setString(2,null);
	    else
		pstmt.setString(2,statement);
	    if(schedule.equals(""))
		pstmt.setString(3,null);
	    else
		pstmt.setString(3,schedule);
	    if(closings.equals(""))
		pstmt.setString(4,null);
	    else
		pstmt.setString(4,closings);
	    if(other.equals(""))
		pstmt.setString(5,null);
	    else
		pstmt.setString(5,other);
	    if(lead_id.equals(""))
		pstmt.setString(6,null);
	    else
		pstmt.setString(6,lead_id);
	    if(type.equals(""))
		pstmt.setString(7,null);
	    else
		pstmt.setString(7,type);						
	    pstmt.setString(8,id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    message += " Error updating the record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doSave(){

	String message = "", back="";
	String qq = "insert into facilities values (0,?,?,?,?,?,?,?)";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();			
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}		
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    if(name.equals(""))
		pstmt.setString(1,null);
	    else
		pstmt.setString(1,name);
	    if(statement.equals(""))
		pstmt.setString(2,null);
	    else
		pstmt.setString(2,statement);
	    if(schedule.equals(""))
		pstmt.setString(3,null);
	    else
		pstmt.setString(3,schedule);
	    if(closings.equals(""))
		pstmt.setString(4,null);
	    else
		pstmt.setString(4,closings);
	    if(other.equals(""))
		pstmt.setString(5,null);
	    else
		pstmt.setString(5,other);
	    if(lead_id.equals(""))
		pstmt.setString(6,null);
	    else
		pstmt.setString(6,lead_id);
	    if(type.equals(""))
		pstmt.setString(7,null);
	    else
		pstmt.setString(7,type);						
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
	}
	catch(Exception ex){
	    message = " Error adding the record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }

}





































