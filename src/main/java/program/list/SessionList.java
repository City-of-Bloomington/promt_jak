package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class SessionList extends ArrayList<Session>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(SessionList.class);
    String sid="", id="", sortby="", effective_date="",
	exclude_sids =""; // needed for reordering
	 
    public SessionList(boolean val){
	debug = val;
    }
    public SessionList(boolean val, String val2){
	debug = val;
	setId(val2);
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setSid (String val){
	if(val != null)
	    sid = val;
    }
    public void setSortby (String val){
	if(val != null)
	    sortby = val;
    }
    public void setEffective_date(String date){
	effective_date = date;
    }
    public void setCurrentOnly(){
	if(effective_date.equals("")){
	    effective_date = Helper.getToday2();
	}
    }
    public void setExcludeSids(String vals){
	if(vals != null)
	    exclude_sids = vals;
    }
    public List<Session> getSessions(){
	return this;
    }
    //
    public String lookFor(){
	//
	String message = "";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2 = null;
	ResultSet rs = null;	
	String qq = "select id,sid,"+ 
	    " inCityFee,nonCityFee,otherFee,"+ 
	    " allAge,partGrade,minMaxEnroll,"+  
	    " date_format(regDeadLine,'%m/%d/%Y'),"+ 
	    " location,instructor,description, "+ 
	    " days,startTime,endTime,classCount, "+
	    " code,"+ 
	    " date_format(startDate,'%m/%d/%Y'),"+
	    " date_format(endDate,'%m/%d/%Y'),"+ 
	    " ageFrom,ageTo, "+ 
	    " wParent,"+
	    " otherAge, "+ 
	    " memberFee,nonMemberFee "+
	    " from sessions "; 
		
	String qw = "";
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "id=?";
	}
	if(!effective_date.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(regDeadLine >= str_to_date('"+effective_date+"','%m/%d/%Y') "+
		"or (regDeadLine is null and endDate >= str_to_date('"+effective_date+"','%m/%d/%Y'))) ";
	}
	if(!exclude_sids.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " sid not in ("+exclude_sids+") ";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	if(!sortby.equals(""))
	    qq += " order by "+sortby;
	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();
	    pstmt = con.prepareStatement(qq);
	    int jj = 1;
	    if(!id.equals("")){
		pstmt.setString(jj,id);
		jj++;
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Session one = new Session(debug,
					  rs.getString(1),
					  rs.getString(2),
					  rs.getString(3),
					  rs.getString(4),
					  rs.getString(5),
					  rs.getString(6),
					  rs.getString(7),
					  rs.getString(8),
					  rs.getString(9),
					  rs.getString(10),
					  rs.getString(11),
					  rs.getString(12),
					  rs.getString(13),
					  rs.getString(14),
					  rs.getString(15),
					  rs.getString(16),
					  rs.getString(17),
					  rs.getString(18),
					  rs.getString(19),
					  rs.getString(20),
					  rs.getString(21),
					  rs.getString(22),
					  rs.getString(23),
					  rs.getString(24),
					  rs.getString(25));
		if(!this.contains(one)){
		    add(one);
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

}





































