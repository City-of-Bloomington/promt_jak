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

public class CalHandle{

    boolean debug = false;
    String id = "", days="", startDate="", endDate="", sid="";
    static Logger logger = LogManager.getLogger(CalHandle.class);
    /**
     * main constructer
     *
     */
    public CalHandle(boolean deb, String id, String sid){
	this.debug = deb;		
	if(id != null)
	    this.id = id;
	if(sid != null)
	    this.sid = sid;
    }
    public String process(){
	String msg = "", date="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB";
	    return msg;
	}
	try{
	    msg = doDelete(con, pstmt);
	    if(!msg.equals("")){
		return msg;
	    }
	    //
	    // get the startDate, endDate and recurrences from the
	    // main record
	    //
	    msg = getInfo(con, pstmt, rs);
	    if(!msg.equals("")){
		return msg;
	    }
	    if(!startDate.equals("")){
		//
		// one time only, the start date only
		//
		if(endDate.equals("") ||
		   endDate.equals(startDate)){
		    msg += doInsert(con, pstmt, startDate);
					
		}
		else{
		    //
		    // multiple recurrences
		    //
		    if(!days.equals("")){
			int mm = Integer.parseInt(startDate.substring(0,2));
			int dd = Integer.parseInt(startDate.substring(3,5));
			int yy = Integer.parseInt(startDate.substring(6,10));
			int mm2 = Integer.parseInt(endDate.substring(0,2));
			int dd2 = Integer.parseInt(endDate.substring(3,5));
			int yy2 = Integer.parseInt(endDate.substring(6,10));
			//
			// Restrict to one year range only
			//
			if(yy2 > yy+1) yy2 = yy + 1;
			if(yy2 > yy){
			    if(mm2 > mm)
				mm2 = mm;
			    if(dd2 > dd)
				dd2 = dd;
			}
			GregorianCalendar cal = new GregorianCalendar();
			GregorianCalendar endCal = new GregorianCalendar();
			cal.set(Calendar.MONTH, mm-1);
			cal.set(Calendar.DATE, dd);
			cal.set(Calendar.YEAR, yy);
			endCal.set(Calendar.MONTH, mm2-1);
			endCal.set(Calendar.DATE, dd2);
			endCal.set(Calendar.YEAR, yy2);
			boolean in = true;
			if(days.indexOf("SU") > -1 && // MON. - SUN.
			   days.indexOf("-") > -1){
			    //
			    // all days case
			    //
			    while(in){
				date = mm+"/"+dd+"/"+yy;
				msg += doInsert(con, pstmt, date);
				cal.add(Calendar.DATE, 1);
				// check if we passed the end date
				if(cal.after(endCal)) in = false;
				mm = cal.get(Calendar.MONTH)+1;
				dd = cal.get(Calendar.DATE);
				yy = cal.get(Calendar.YEAR);
			    }
			}
			else if(days.indexOf("F") > -1 && // MON. - FRI.
				days.indexOf("-") > -1 ){ // MON - FRI
			    //
			    // Monday - Friday
			    //
			    while(in){
				int cday = cal.get(Calendar.DAY_OF_WEEK);
				if(cday != cal.SUNDAY && cday != cal.SATURDAY){
				    date = mm +"/"+dd+"/"+yy;
				    msg += doInsert(con, pstmt, date);
				}
				cal.add(Calendar.DATE, 1);
				//
				// check if we passed the end date
				//
				if(cal.after(endCal)) in = false;
				mm = cal.get(Calendar.MONTH)+1;
				dd = cal.get(Calendar.DATE);
				yy = cal.get(Calendar.YEAR);
			    }	
			}	
			else{
			    // certain days
			    Set<String> set = new HashSet();
			    if(days.indexOf("SU") > -1) set.add("0");
			    if(days.indexOf("M") > -1) set.add("1");
			    if(days.indexOf("TU") > -1) set.add("2");
			    if(days.indexOf("W") > -1) set.add("3");
			    if(days.indexOf("TH") > -1) set.add("4");
			    if(days.indexOf("F") > -1) set.add("5");
			    if(days.indexOf("SA") > -1) set.add("6");
			    while(in){
				String cday = "" + cal.get(Calendar.DAY_OF_WEEK);
				if(set.contains(cday)){
				    date = mm +"/"+dd+"/"+yy;
				    msg += doInsert(con, pstmt, date);
				}
				cal.add(Calendar.DATE, 1);
				//
				// check if we passed the end date
				//
				if(cal.after(endCal)) in = false;
				mm = cal.get(Calendar.MONTH)+1;
				dd = cal.get(Calendar.DATE);
				yy = cal.get(Calendar.YEAR);
			    }
			}
		    }
		}
	    }
	}
	catch(Exception ex){
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
    //
    public String getInfo(Connection con, PreparedStatement pstmt, ResultSet rs){
	// 
	String msg = "", str="", qq="";
	if(sid.equals("")){		
	    qq = " select days,"+
		" date_format(startDate,'%m/%d/%Y'),"+
		" date_format(endDate,'%m/%d/%Y') ";
	    qq += " from programs where id=? ";
	}
	else{
	    qq = " select days,"+
		" date_format(startDate,'%m/%d/%Y'),"+
		" date_format(endDate,'%m/%d/%Y') ";			
	    qq += " from sessions where id=? and sid=?";
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    if(!sid.equals(""))
		pstmt.setString(2, sid);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		//
		str = rs.getString(1);
		if(str != null) days = str.toUpperCase();
		str = rs.getString(2);
		if(str != null) startDate = str;
		str = rs.getString(3);
		if(str != null) endDate = str;
	    }
	}
	catch(Exception ex){
	    msg += " could not retreive data "+ex;
	    logger.error(ex+":"+qq);
	}
	return msg;
    }
    public String doInsert(Connection con, PreparedStatement pstmt, String date){
	String msg = "";
	String qq = "insert into agenda_info values (0,"+
	    "str_to_date('"+date+"','%m/%d/%Y'),?,?)";
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    if(sid.equals(""))
		pstmt.setNull(2, Types.INTEGER);
	    else
		pstmt.setString(2, sid);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg = " Could not insert "+ex;
	    logger.error(ex+" : "+qq);
	}
	return msg;
    }	
    //
    public String doDelete(){
	String back = "", qq = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	try{
	    back = doDelete(con, pstmt);
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
		
    }
    public String doDelete(Connection con, PreparedStatement pstmt){
	// 
	String msg = "";
	String qq = "delete from agenda_info where pid=? and ";
	if(sid.equals(""))
	    qq += " sid is null "; // program
	else
	    qq += " sid=?";    // session
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    if(!sid.equals(""))
		pstmt.setString(2, sid);				
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += " could not delete data "+ex;
	    logger.error(ex+":"+qq);
	}
	return msg;
    }	

}






















































