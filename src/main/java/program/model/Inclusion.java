package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class Inclusion extends CommonInc{

    static Logger logger = LogManager.getLogger(Inclusion.class);
    String id=""; // program id and inclusion id
    String consult_ada="",training="",train_aware="",train_basics="",
	train_consider="",train_behave="",train_trip="",
	train_other="", comments="", consult_pro="",
	market="",site_visit="", consult="",sign="",prov_sign="";
    boolean hasRecord = false;
    Program prog = null;
    //
    public Inclusion(boolean deb){
	//
	// initialize
	//
	super(deb);
    }	
    public Inclusion(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
    public Inclusion(boolean deb,
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
	setConsult_ada(val2);
	setTraining(val3);
	setTrain_aware(val4);
	setTrain_basics(val5);
	setTrain_consider(val6);
	setTrain_behave(val7);
	setTrain_trip(val8);
	setTrain_other(val9);
	setComments(val10);
	setMarket(val11);
	setSite_visit(val12);
	setConsult(val13);
	setSign(val14);
	setProv_sign(val15);
	setConsult_pro(val16);
    }
	
    //
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof Inclusion){
	    match = id.equals(((Inclusion)gg).id);
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
    public String getConsult_ada(){
	return consult_ada;
    }
    public String getConsult_pro(){
	return consult_pro;
    }	
    public String getTraining(){
	return training;
    }
    public String getTrain_aware(){
	return train_aware;
    }
    public String getTrain_basics(){
	return train_basics;
    }
    public String getTrain_consider(){
	return train_consider;
    }
    public String getTrain_behave(){
	return train_behave;
    }
    public String getTrain_trip(){
	return train_trip;
    }
    public String getTrain_other(){
	return train_other;
    }	
    public String getComments(){
	return comments;
    }
    public String getMarket(){
	return market;
    }
    public String getSite_visit(){
	return site_visit;
    }
    public String getConsult(){
	return consult;
    }
    public String getSign(){
	return sign;
    }
    public String getProv_sign(){
	return prov_sign;
    }
    public boolean hasRecord(){
	return hasRecord;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null){
	    id = val.trim();
	}
    }
    public void setConsult_ada(String val){
	if(val != null)
	    consult_ada = val;
    }
    public void setConsult_pro(String val){
	if(val != null)
	    consult_pro = val;
    }	
    public void setTraining(String val){
	if(val != null){
	    training = val;
	}
    }
    public void setTrain_aware(String val){
	if(val != null){
	    train_aware = val.trim();
	}
    }	
    public void setTrain_basics(String val){
	if(val != null){
	    train_basics = val;
	}
    }
    public void setTrain_consider(String val){
	if(val != null){
	    train_consider = val;
	}
    }
    public void setTrain_behave(String val){
	if(val != null){
	    train_behave = val;
	}
    }
    public void setTrain_trip(String val){
	if(val != null){
	    train_trip = val;
	}
    }
    public void setTrain_other(String val){
	if(val != null){
	    train_other = val.trim();
	}
    }
    public void setComments(String val){
	if(val != null){
	    comments = val.trim();
	}
    }
    public void setMarket(String val){
	if(val != null)
	    market = val;
    }

    public void setSite_visit(String val){
	if(val != null)
	    site_visit = val;
    }
    public void setConsult(String val){
	if(val != null)
	    consult = val;
    }
    public void setSign(String val){
	if(val != null)
	    sign = val;
    }
    public void setProv_sign(String val){
	if(val != null){
	    prov_sign = val;
	}
    }
	
    public Program getProgram(){
	if(prog == null && !id.equals("")){
	    Program one = new Program(debug, id);
	    String back = one.doSelect();
	    if(back.equals("")){
		prog = one;
	    }
	}
	return prog;
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
	String qq = "insert into programs_inclusion "+
	    "values(?,?,?,?,?,"+
	    "?,?,?,?,?,"+
	    "?,?,?,?,?,?)";						
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
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
	    hasRecord = true;
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
	String qq = "update programs_inclusion set "+
	    "consult_ada=?,training=?,train_aware=?,train_basics=?,"+
	    "train_consider=?,train_behave=?,train_trip=?,train_other=?,comments=?,market=?,site_visit=?,consult=?,sign=?,prov_sign=?,consult_pro=? "+
	    "where id = ? ";
	if(debug)
	    logger.debug(qq);
		
	try{
	    pstmt = con.prepareStatement(qq);
	    //
	    back += setParams(pstmt, 1);
	    pstmt.setString(16, id);
	    pstmt.executeUpdate();
	    message="Updated Successfully";
	    hasRecord = true;
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
	    if(consult_ada.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++, "y");
	    if(training.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(train_aware.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(train_basics.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(train_consider.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(train_behave.equals("")) // 6
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(train_trip.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(train_other.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,train_other);			
	    if(comments.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,comments);
	    if(market.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(site_visit.equals(""))
		pstmt.setNull(jj++,Types.CHAR); // 11
	    else
		pstmt.setString(jj++,"y");
	    if(consult.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(sign.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(prov_sign.equals(""))
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(consult_pro.equals(""))
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
	    qq = "delete from programs_inclusion where id=?";
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
		
	String qq = "select id,consult_ada,training,train_aware,train_basics,train_consider,train_behave,train_trip,train_other,comments,market,site_visit,consult,sign,prov_sign,consult_pro from programs_inclusion where id=? ";
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
		setConsult_ada(rs.getString(2));
		setTraining(rs.getString(3));
		setTrain_aware(rs.getString(4));
		setTrain_basics(rs.getString(5));
		setTrain_consider(rs.getString(6));
		setTrain_behave(rs.getString(7));
		setTrain_trip(rs.getString(8));
		setTrain_other(rs.getString(9));
		setComments(rs.getString(10));
		setMarket(rs.getString(11));
		setSite_visit(rs.getString(12));
		setConsult(rs.getString(13));
		setSign(rs.getString(14));
		setProv_sign(rs.getString(15));
		setConsult_pro(rs.getString(16));
		hasRecord = true;
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
