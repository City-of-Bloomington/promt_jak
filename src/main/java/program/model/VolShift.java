package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;


public class VolShift extends CommonInc{

    static Logger logger = LogManager.getLogger(VolShift.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String pid="", // program id
	id="", // shift id
	volCount="",days="", date="",
	startTime="", endTime="", category_id="",
	duties="",onsite="",pre_train="",
	notes="", lead_id="", title="";
    boolean selectionDone = false;
    Program prog = null;
    List<VolTrain> trains = null;
    Type category = null;
    Type lead = null;
    HistoryList history = null;			
    //
    public VolShift(boolean deb){
	//
	// initialize
	//
	super(deb);
    }	
    public VolShift(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
    public VolShift(boolean deb,
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
		    String val13,
		    String val14,
		    String val15,
		    String val16
		    ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setPid(val2);
	setVolCount(val3);
	setDays(val4);
	setDate(val5);
	setStartTime(val6);
	setEndTime(val7);
	setCategory_id(val8);
	setNotes(val9);
	setDuties(val10);
	setOnsite(val11);
	setPre_train(val12);
	if(val13 != null){
	    category = new Type(debug, val8, val13);
	}
	setLead_id(val14);
	if(val15 != null){
	    lead = new Type(debug, val14, val15);
	}
	setTitle(val16);
	selectionDone = true;
    }
	
    //
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof VolShift){
	    match = id.equals(((VolShift)gg).id);
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
    public String getVolCount(){
	return volCount;
    }	
    public String getDays(){
	return days;
    }
    public String getDate(){
	return date;
    }
    public String getStartTime(){
	return startTime;
    }
    public String getStartEndTime(){
	String ret = startTime;
	if(!endTime.equals("")){
	    if(!ret.equals("")) ret += " - ";
	    ret += endTime;
	}
	return ret;
    }		
    public String getEndTime(){
	return endTime;
    }
    public String getCategory_id(){
	return category_id;
    }
    public String getNotes(){
	return notes;
    }
    public String getDuties(){
	return duties;
    }
    public String getOnsite(){
	return onsite;
    }
    public String getPre_train(){
	return pre_train;
    }
    public String getLead_id(){
	return lead_id;
    }
    public String getTitle(){
	return title;
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
    public void setTitle(String val){
	if(val != null){
	    title = val.trim();
	}
    }
		
    public void setLead_id(String val){
	if(val != null){
	    lead_id = val;
	}
    }	
    public void setVolCount(String val){
	if(val != null)
	    volCount = val.trim();
    }
    public void setDays(String val){
	if(val != null){
	    days = val.trim();
	}
    }
    public void setDate(String val){
	if(val != null){
	    date = val.trim();
	}
    }	

    public void setStartTime(String val){
	if(val != null)
	    startTime = val.trim();
    }

    public void setEndTime(String val){
	if(val != null)
	    endTime = val;
    }
    public void setCategory_id(String val){
	if(val != null)
	    category_id = val;
    }
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }
    public void setDuties(String val){
	if(val != null){
	    duties = val.trim();
	}
    }
    public void setOnsite(String val){
	if(val != null){
	    onsite = val;
	}
    }
    public void setPre_train(String val){
	if(val != null){
	    pre_train = val;
	}
    }
    public Type getCategory(){
	if(category == null && !category_id.equals("")){
	    Type cat = new Type(debug, category_id,"","categories");
	    String back = cat.doSelect();
	    if(back.equals("")){
		category = cat;
	    }
	}
	return category;
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
    public boolean hasTraining(){
	getTrains();
	return trains != null && trains.size() > 0;
    }
    public boolean hasProgram(){
	getProgram();
	return prog != null;
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
    public List<VolTrain> getTrains(){
	if(trains == null && !id.equals("")){
	    VolTrainList ones = new VolTrainList(debug, pid, id);
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		trains = ones;
	    }
	}
	return trains;
    }
    public boolean hasHistory(){
	getHistory();
	return history != null;
    }
    public List<History> getHistory(){
	if(history == null && !id.equals("")){
	    HistoryList ones = new HistoryList(debug, id, "Volunteer");
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
	if(volCount.equals("")){
	    back = "volunteer count is required";
	    addError(back);
	    return back;
	}		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}

	String qq = "insert into vol_shifts "+
	    "values(0,?,"+
	    "?,?,?,?,?,"+
	    "?,?,?,?,?,?,?)";						
	try{
	    pstmt = con.prepareStatement(qq);
	    if(pid.equals(""))
		pstmt.setNull(1, Types.INTEGER);
	    else
		pstmt.setString(1,pid);				
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
    public String doDuplicate(String program_id){
	String back = "";
	if(program_id == null || program_id.equals("")){
	    back = "No program id set ";
	    return back;
	}
	if(selectionDone){
	    back = doSelect();
	    if(!back.equals("")){
		return back;
	    }
	}
	getTrains();// save these for later
	id="";
	date=""; // we do not copy dates
	pid = program_id;
	back = doSave();
	if(trains != null){
	    for(VolTrain one:trains){
		back += one.doDuplicate(program_id, id);
	    }
	}
	return back;
    }
    public String doUpdate(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="";
	if(volCount.equals("")){
	    back = "volunteer count is required";
	    addError(back);
	    return back;						
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "update vol_shifts set "+
	    "volCount=?,days=?,date=?,startTime=?,"+
	    "endTime=?,category_id=?,notes=?,duties=?,onsite=?,pre_train=?, "+
	    "lead_id=?,title=? "+
	    "where id = ? ";
	if(debug)
	    logger.debug(qq);
		
	try{
	    pstmt = con.prepareStatement(qq);
	    //
	    back += setParams(pstmt, 1);
	    pstmt.setString(13, id);
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
    public String setParams(PreparedStatement pstmt, int jj){
	String back = "";
	try{
	    if(volCount.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++, volCount);
	    if(days.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,days);
	    if(date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else
		pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date).getTime()));			
	    if(startTime.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,startTime);
	    if(endTime.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,endTime);
	    if(category_id.equals(""))
		pstmt.setNull(jj++,Types.INTEGER);
	    else
		pstmt.setString(jj++,category_id);
	    if(notes.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++, notes);
	    if(duties.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,duties);
	    if(onsite.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");			
	    if(pre_train.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(lead_id.equals(""))
		pstmt.setNull(jj++,Types.INTEGER);
	    else
		pstmt.setString(jj++,lead_id);
	    if(title.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++, title);
			
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
	    qq = "delete from vol_shifts where id=?";
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
		
	String qq = "select v.pid,v.volCount,v.days,date_format(v.date,'%m/%d/%Y'),"+
	    " v.startTime,v.endTime,v.category_id,v.notes,v.duties,v.onsite,v.pre_train,c.name,v.lead_id,l.name,v.title "+
	    " from vol_shifts v left join categories c on c.id=v.category_id "+
	    " left join leads l on l.id = v.lead_id "+
	    " where v.id=? ";
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
		setVolCount(rs.getString(2));
		setDays(rs.getString(3));
		setDate(rs.getString(4));
		setStartTime(rs.getString(5));
		setEndTime(rs.getString(6));
		setCategory_id(rs.getString(7));
		setNotes(rs.getString(8));
		setDuties(rs.getString(9));
		setOnsite(rs.getString(10));
		setPre_train(rs.getString(11));
		String str = rs.getString(12);
		if(str != null)
		    category =new Type(debug, category_id, str);
		setLead_id(rs.getString(13));
		str = rs.getString(14);
		if(str != null)
		    lead =new Type(debug, lead_id, str);
		setTitle(rs.getString(15));
		selectionDone = true;
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
