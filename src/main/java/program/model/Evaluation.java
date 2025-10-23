package program.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;


public class Evaluation extends CommonInc{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(Evaluation.class);
    public final static String life_cycle_options[] ={"Intro","Growth","Maturity","Saturation","Decline"}; 
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
    String id="", sponsorship="", attendance="";
    String assignment = "", recommend="", other="";
    String partnership="", flier_points="", life_cycle="",
	date="", wby="", life_cycle_info="";
    boolean record_found = false;
    List<EvalStaff> evalStaffs = null;
    List<Outcome> outcomes = null;
    List<Outcome> addOutcomes = null;
    List<EvalStaff> addEvalStaffs = null;
    List<Outcome> updateOutcomes = null;
    List<EvalStaff> updateEvalStaffs = null;
    List<PromtFile> files = null;		
    Program program = null;
    public Evaluation(boolean deb){
	//
	// initialize
	//
	super(deb);
    }	
    public Evaluation(boolean deb,
		      String val
		      ){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
    public Evaluation(boolean deb,
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
		      String val12
		      ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setAttendance(val2);
	setAssignment(val3);
	setRecommend(val4);
	setPartnership(val5);
	setSponsorship(val6);
	setFlier_points(val7);
	setLife_cycle(val8);
	setOther(val9);
	setWby(val10);
	setDate(val11);
	setLife_cycle_info(val12);
	record_found = true;
    }	
    public String getId(){
	return id;
    }
    public String getAttendance(){
	return attendance;
    }
    public String getAssignment(){
	return assignment;
    }	
    public String getRecommend(){
	return recommend;
    }
    public String getPartnership(){
	return partnership;
    }
    public String getSponsorship(){
	return sponsorship;
    }
    public String getFlier_points(){
	return flier_points;
    }
    public String getLife_cycle(){
	return life_cycle;
    }
    public String getOther(){
	return other;
    }
    public String getWby(){
	return wby;
    }
    public String getDate(){
	return date;
    }
    public String getLife_cycle_info(){
	return life_cycle_info;
    }		
    public boolean hasRecord(){
	return record_found;
    }
    public Program getProgram(){
	if(program == null && !id.equals("")){
	    Program one = new Program(debug, id);
	    String back = one.doSelect();
	    program = one;
	}
	if(program == null){
	    program = new Program(debug, id);
	}
	return program;
    }
    public boolean hasFiles(){
	getFiles();
	return files != null && files.size() > 0;
    }
    public List<PromtFile> getFiles(){
	if(files == null && !id.equals("")){
	    PromtFileList tsl = new PromtFileList(debug, id, "Evaluation");
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
    //
    public void setId(String val){
	if(val != null)
	    id= val;
    }
    public void setAttendance(String val){
	if(val != null)
	    attendance= val;
    }
    public void setAssignment(String val){
	if(val != null)
	    assignment= val;
    }	
    public void setRecommend(String val){
	if(val != null)
	    recommend= val;
    }
    public void setPartnership(String val){
	if(val != null)
	    partnership= val;
    }
    public void setSponsorship(String val){
	if(val != null)
	    sponsorship= val;
    }
    public void setFlier_points(String val){
	if(val != null)
	    flier_points= val;
    }
    public void setLife_cycle(String val){
	if(val != null)
	    life_cycle= val;
    }
    public void setOther(String val){
	if(val != null)
	    other= val;
    }
    public void setWby(String val){
	if(val != null)
	    wby= val;
    }
    public void setDate(String val){
	if(val != null)
	    date= val;
    }
    public void setLife_cycle_info(String val){
	if(val != null)
	    life_cycle_info = val;
    }		
    public void addOutcome(String name, String val){
	if(addOutcomes == null){
	    addOutcomes = new ArrayList<Outcome>();
	}
	String obj_id = name.substring(name.indexOf("_")+1);
	Outcome one = new Outcome(debug, null, id, obj_id, val);
	addOutcomes.add(one); // id may not be set, so need to set it again before saving
    }
    public void addEvalStaff(String name, String val){
	if(addEvalStaffs == null){
	    addEvalStaffs = new ArrayList<EvalStaff>();
	}
	String staff_id = name.substring(name.indexOf("_")+1);
	EvalStaff one = new EvalStaff(debug, null, id, staff_id, val);
	addEvalStaffs.add(one);
    }
    public void updateOutcome(String name, String val){
	if(updateOutcomes == null){
	    updateOutcomes = new ArrayList<Outcome>();
	}
	String out_id = name.substring(name.indexOf("_")+1);
	Outcome one = new Outcome(debug, out_id, id, null, val);
	updateOutcomes.add(one); 
    }
    public void updateEvalStaff(String name, String val){
	if(updateEvalStaffs == null){
	    updateEvalStaffs = new ArrayList<EvalStaff>();
	}
	String evalStaff_id = name.substring(name.indexOf("_")+1);
	EvalStaff one = new EvalStaff(debug, evalStaff_id, id, null, val);
	updateEvalStaffs.add(one);
    }	
    public List<Outcome> getOutcomes(){
	if(outcomes == null && record_found){
	    OutcomeList ones = new OutcomeList(debug, id);
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		outcomes = ones;
	    }
	}
	return outcomes;
    }
    public List<EvalStaff> getEvalStaffs(){
	if(evalStaffs == null && record_found){
	    EvalStaffList ones = new EvalStaffList(debug, id);
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		evalStaffs = ones;
	    }
	}
	return evalStaffs;
    }
    //
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into evaluations values("+
	    id+","+ // same as program id
	    "?,?,?,?,?,"+
	    "?,?,?,?,?,?)";
	if(id.equals("")){
	    back = "evaluation id not set";
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
	    back = setParams(pstmt);
	    if(back.equals("")){
		pstmt.executeUpdate();
		message="Saved Successfully";
		record_found = true;
		back += doAddsAndUpdates();
	    }
	    else{
		message += back;
	    }
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
	    back = "evaluation id not set ";
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
	    qq = "update evaluations set attendance=?,assignment=?, "+
		"recommend=?,partnership=?,sponsorship=?,flier_points=?,"+
		"life_cycle=?,other=?,wby=?,date=?,life_cycle_info=? "+
		"where id=?";
			
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    back = setParams(pstmt);
	    if(back.equals("")){
		pstmt.setString(12,id);			
		pstmt.executeUpdate();
		message="Updated Successfully";
		record_found = true;
		back += doAddsAndUpdates();
	    }
	    else{
		message += back;
	    }
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
    private String setParams(PreparedStatement pstmt){
	String back = "";
	int j=1;
	try{
	    if(attendance.equals(""))
		pstmt.setNull(j++,Types.INTEGER);
	    else
		pstmt.setString(j++, attendance);
	    if(assignment.equals(""))
		pstmt.setNull(j++,Types.VARCHAR);
	    else
		pstmt.setString(j++, assignment);
	    if(recommend.equals(""))
		pstmt.setNull(j++,Types.VARCHAR);
	    else
		pstmt.setString(j++, recommend);
	    if(partnership.equals(""))
		pstmt.setNull(j++,Types.VARCHAR);
	    else
		pstmt.setString(j++, partnership);
	    if(sponsorship.equals(""))
		pstmt.setNull(j++,Types.VARCHAR);
	    else
		pstmt.setString(j++, sponsorship);
	    if(flier_points.equals(""))
		pstmt.setNull(j++,Types.VARCHAR);
	    else
		pstmt.setString(j++, flier_points);
	    if(life_cycle.equals(""))
		pstmt.setNull(j++,Types.VARCHAR);
	    else
		pstmt.setString(j++, life_cycle);
	    if(other.equals(""))
		pstmt.setNull(j++,Types.VARCHAR);
	    else
		pstmt.setString(j++, other);
	    if(wby.equals(""))
		pstmt.setNull(j++,Types.VARCHAR);
	    else
		pstmt.setString(j++, wby);
	    if(date.equals("")){
		date = Helper.getToday2();
	    }
	    pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(date).getTime()));
	    if(life_cycle_info.equals(""))
		pstmt.setNull(j++,Types.VARCHAR);
	    else
		pstmt.setString(j++, life_cycle_info);						
	}
	catch(Exception ex){
	    back += ex;
	}
	return back;
		
    }
    String doAddsAndUpdates(){
	String back = "";
	if(addOutcomes != null){
	    for(Outcome one: addOutcomes){
		one.setEval_id(id);
		back += one.doSave();
	    }
	}
	if(addEvalStaffs != null){
	    for(EvalStaff one: addEvalStaffs){
		one.setEval_id(id);
		back += one.doSave();
	    }
	}
	if(updateOutcomes != null){
	    for(Outcome one: updateOutcomes){
		one.setEval_id(id);
		back += one.doUpdate();
	    }
	}
	if(updateEvalStaffs != null){
	    for(EvalStaff one: updateEvalStaffs){
		one.setEval_id(id);
		back += one.doUpdate();
	    }
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
	    qq = "delete from evaluations where id=?";
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
	String qq = "select id,attendance,assignment,recommend,partnership,"+
	    "sponsorship,flier_points,life_cycle,other,wby,date_format(date,'%m/%d/%Y'),life_cycle_info from evaluations where id=? ";
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
		setAttendance(rs.getString(2));
		setAssignment(rs.getString(3));
		setRecommend(rs.getString(4));
		setPartnership(rs.getString(5));
		setSponsorship(rs.getString(6));
		setFlier_points(rs.getString(7));
		setLife_cycle(rs.getString(8));
		setOther(rs.getString(9));
		setWby(rs.getString(10));
		setDate(rs.getString(11));
		setLife_cycle_info(rs.getString(12));
		record_found = true;
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















































