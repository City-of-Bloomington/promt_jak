package program.model;
import java.sql.*;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;
import program.model.*;

public class SessionReorder extends CommonInc{

    static Logger logger = LogManager.getLogger(SessionReorder.class);
    String order_id="", prog_id="", old_sid="";
    int next_order_id = 1;
    Session session = null;
    //
    public SessionReorder(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public SessionReorder(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setProg_id(val);
    }
    public SessionReorder(boolean deb,
			  String val,
			  String val2){
	//
	// initialize
	//
	super(deb);
	setProg_id(val);
	setOld_sid(val2);
    }		
    public SessionReorder(boolean deb,
			  String val,
			  String val2,
			  String val3){
	//
	// initialize
	//
	super(deb);
	setOrder_id(val);
	setProg_id(val2);
	setOld_sid(val3);
    }
    public boolean equals(Object gg){
	boolean match = false;
	if (gg != null && gg instanceof SessionReorder){
	    match = old_sid.equals(((SessionReorder)gg).old_sid);
	}
	return match;
    }
    public int hashCode(){
	int code = 37;
	try{
	    code += Integer.parseInt(old_sid);
	}catch(Exception ex){};
	return code;
    }
    //
    // getters
    //
    public String getNextOrder_id(){
	return ""+next_order_id;
    }		
    public String getOrder_id(){
	return order_id;
    }
    public String getProg_id(){
	return prog_id;
    }
    public String getOld_sid(){
	return old_sid;
    }	
    public String toString(){
	String ret = "";
	ret = order_id;
	return ret;
    }
    public Session getSeason(){
	if(!old_sid.equals("") && !prog_id.equals("")){
	    Session one = new Session(debug, prog_id, old_sid);
	    String back = one.doSelect();
	    if(back.equals("")){
		session = one;
	    }
	}
	return session;
    }
    //
    // setters
    //
    public void setOrder_id(String val){
	if(val != null)
	    order_id = val.trim();
    }
    public void setProg_id(String val){
	if(val != null)
	    prog_id = val.trim();
    }	
    public void setOld_sid(String val){
	if(val != null)
	    old_sid = val;
    }
    /**
       order  old_sid sid change
       ============== ==========
       1       3      3->0  1->3 0->1 (old_sid -> 0) 
       (order_id -> old_sid)
       (0->order_id)
       2       1
       3       2

       change old_sid reocrd to 0
       swap sid=order_id with old_sid
       change 0 to order_id
       suppose we want to change sid=4 to sid=1(order_id) for prog_id 10429
			 
       update sessions set sid=0 where id=10429 and sid=4;
       update sessions set sid=4 where id=10429 and sid=1;
       update sessions set sid=1 where id=10429 and sid=0;
			 
    */
    public String changeOrder(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	String qs = "select order_id,old_sid from session_reorders where prog_id=?";
	String qq = "update sessions set sid=? where id=? and sid=? ";
	String qq2 = "delete from session_reorders ";
	Set<String> set =new HashSet<>();
						
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qs);
	    }						
	    pstmt = con.prepareStatement(qs);
	    pstmt2 = con.prepareStatement(qq);						
	    pstmt.setString(1, prog_id);
	    rs = pstmt.executeQuery();
	    if(debug){
		logger.debug(qq);
	    }
	    while(rs.next()){
								
		order_id = rs.getString(1);
		old_sid = rs.getString(2);
		if(set.contains(order_id)) continue;
		set.add(order_id);
		set.add(old_sid);
		if(order_id.equals(old_sid)) continue; //skip
		pstmt2.setString(1,"0");
		pstmt2.setString(2,prog_id);			
		pstmt2.setString(3,old_sid); // old_sid
		pstmt2.executeUpdate();
		//
		pstmt2.setString(1, old_sid);
		pstmt2.setString(2, prog_id);			
		pstmt2.setString(3, order_id);
		pstmt2.executeUpdate();
		//
		pstmt2.setString(1, order_id);
		pstmt2.setString(2, prog_id);			
		pstmt2.setString(3, "0");				
		pstmt2.executeUpdate();
	    }
	    // clean up
	    pstmt3 = con.prepareStatement(qq2);
	    pstmt3.executeUpdate();
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);
	    // Helper.databaseDisconnect(con, pstmt2, rs);						
	}
	return back;						
    }
		
    public String doSave(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	findNextOrder();
	order_id = ""+next_order_id;
	String qq = "insert into session_reorders values(?,?,?)";
	if(old_sid.equals("") || prog_id.equals("")){
	    back = "prog id or session id not set";
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
	    pstmt.setString(1,order_id);
	    pstmt.setString(2,prog_id);			
	    pstmt.setString(3,old_sid);
	    pstmt.executeUpdate();
	    //
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

    public String doClean(){
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
	    qq = "delete from session_reorders where prog_id=?"; // all
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, prog_id);
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
	String qq = "select s.prog_id,s.old_sid "+
	    "from session_reorders s where s.order_id=?";
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
	    pstmt.setString(1,order_id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		prog_id = rs.getString(1);
		old_sid = rs.getString(2);
	    }
	    else{
		message= "Record "+order_id+" not found";
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

    public String findNextOrder(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select max(order_id) from session_reorders where prog_id=? ";
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
	    pstmt.setString(1, prog_id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		next_order_id = rs.getInt(1)+1;
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
