package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class SessionReorderList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(SessionReorderList.class);
    String prog_id = "", old_sid_set="";
    List<String> errors = null;
    String message = "";
    List<SessionReorder> sessionReorders = null;
    public SessionReorderList(boolean val, String val2){
	debug = val;
	setProg_id(val2);
    }
    public void setProg_id(String val){
	if(val != null)
	    prog_id = val;
    }
    public String getMessage(){
	return message;
    }
    public boolean hasMessage(){
	return !message.equals("");
    }
    public boolean hasErrors(){
	return (errors != null && errors.size() > 0);
    }
    public List<String> getErrors(){
	return errors;
    }
    void addError(String val){
	if(errors == null)
	    errors = new ArrayList<String>();
	errors.add(val);
    }
    public List<SessionReorder> getSessionReorders(){
	return sessionReorders;
    }
    public boolean hasSessionReorders(){
	return sessionReorders != null && sessionReorders.size() > 0;
    }
    public String getOldSidSet(){
	return old_sid_set;
    }
    public boolean hasOldSidSet(){
	return !old_sid_set.equals("");
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select order_id,old_sid "+
	    "from session_reorders where prog_id=? order by order_id ";
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
	    pstmt.setString(1,prog_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(sessionReorders == null)
		    sessionReorders = new ArrayList<>();
		SessionReorder one =
		    new SessionReorder(debug,
				       rs.getString(1),
				       prog_id,
				       rs.getString(2)
				       );
		sessionReorders.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }

    public String findOldSidSet(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select old_sid "+
	    "from session_reorders where prog_id=? ";
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
	    pstmt.setString(1,prog_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		if(str != null){
		    if(!old_sid_set.equals("")) old_sid_set +=",";
		    old_sid_set += str;
		}
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }		
}






















































