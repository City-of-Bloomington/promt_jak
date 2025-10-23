package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;


public class ProgCodeList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(ProgCodeList.class);
    final static int  max_rows = 20; // for code entry
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id="", pid="";
    String lead_id="", season="", year="", area_id="", category_id="",
	location_id="", whichDate="",dateFrom="", dateTo="", codeNeed="";
    String nraccount="", sortBy="", limit="";
    String hasNoCodeYet="";
    boolean hasMarket = false, hasSponsor= false, hasCode=false;
    boolean hasShifts = false, includeSessions = false;
		
    public ProgCodeList(boolean val){
	debug = val;
    }
    List<ProgCode> list = null;
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setLead_id(String val){
	if(val != null)
	    lead_id = val;
    }		
    public void setYear(String val){
	if(val != null)
	    year = val;
    }	
    public void setSeason(String val){
	if(val != null)
	    season = val;
    }
    public void setDateFrom(String val){
	if(val != null)
	    dateFrom = val;
    }
    public void setDateTo(String val){
	if(val != null)
	    dateTo = val;
    }		
    public void setCategory_id(String val){
	if(val != null)
	    category_id = val;
    }
    public void setNraccount(String val){
	if(val != null)
	    nraccount = val;
    }

    public void setDeadLineFrom(String val){
	if(val != null){
	    whichDate = "regDeadLine";
	    dateFrom = val;
	}
    }
    public void setArea_id(String val){
	if(val != null)
	    area_id = val;
    }
    public void setCodeNeed(String val){
	if(val != null)
	    codeNeed = val;
    }
    public void setSortBy(String val){
	if(val != null)
	    sortBy = val;
    }		
    public void setHasNoCodeYet(){
	hasNoCodeYet = "y";
    }		
    public void setHasMarket(){
	hasMarket = true;
    }
    public void setHasSponsor(){
	hasSponsor = true;
    }
    public void setHasCode(){
	hasCode = true;
    }
    public void setHasShifts(){
	hasShifts = true;
    }
    public void setLimit(){
	limit = " limit "+max_rows;
    }
    public void includeSessions(){
	includeSessions = true;
    }
    public List<ProgCode> getList(){
	if(list != null)
	    Collections.sort(list);
	return list;
    }
    public String doUpdate(String ids[], String sids[], String codes[]){
	String back="";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	String qq = "update sessions set code=? where id=? and sid=? ";
	String qq2 = "update programs set codeTask='y' where id=? ";
	String qq3 = "update programs set code=?, codeTask='y' where id=? ";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);
	    for(int jj=0;jj<ids.length;jj++){
		if(!ids[jj].equals("") && !codes[jj].equals("")){
		    if(!sids[jj].trim().equals("")){
			pstmt.setString(1, codes[jj]);
			pstmt.setString(2, ids[jj]);
			pstmt.setString(3, sids[jj]);
			pstmt.executeUpdate();
			pstmt2.setString(1, ids[jj]);
			pstmt2.executeUpdate();
		    }
		    else{
			pstmt3.setString(1, codes[jj]);
			pstmt3.setString(2, ids[jj]);
			pstmt3.executeUpdate();
		    }
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;				
    }
    public String lookFor(){
	String message = findProgramsAndOrSessions();
	if(message.equals("") && list.size() < max_rows){
	    // if we have more space in the list, then we add sessions
	    includeSessions();
	    message = findProgramsAndOrSessions();
	}
	return message;
    }
    //
    /**
     * we need this to update codes in programs and sessions
     * here we do not have code yet, we are looking for programs
     * and/or sessions that do not have code
     */
    public String findProgramsAndOrSessions(){
	String message = "", back="", qq="";
	// program code
	String q = "select p.id,p.title,null,c.name,l.name,p.code from programs p ";
	// session code
	String q2 = "select p.id,p.title,ps.sid,c.name,l.name,ps.code from programs p ";
	if(includeSessions){
	    qq = q2;
	}
	else{
	    qq = q;
	}
	qq += " left join categories c on p.category_id=c.id "+
	    " left join leads l on l.id=p.lead_id ";
	String qw="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	con = Helper.getConnection();
		
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}
	if(includeSessions){
	    qq += "left join sessions ps on p.id=ps.id ";
	}
	if(!codeNeed.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.codeNeed is not null";
	}
	if(!hasNoCodeYet.equals("")){
	    if(includeSessions){
		if(!qw.equals("")) qw += " and ";		
		qw += "(ps.code is null or p.code is null)";
	    }
	    else{
		if(!qw.equals("")) qw += " and ";		
		qw += "p.code is null";
	    }
	}
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.id = ?";
	}		
	if(!year.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.year = ?";
	}
	if(!season.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.season = ? or p.season2=?)";
	}
	if(!lead_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.lead_id = ?";
	}						
	if(!category_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.category_id = ? or p.category2_id=?) ";
	}
	if(!area_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.area_id = ?";
	}
	if(!nraccount.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.nraccount like ?";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	if(!limit.equals("")){
	    // qq += limit;
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);			
	    int jj = 1;
	    if(!id.equals("")){
		pstmt.setString(jj++,id);
	    }			
	    if(!year.equals("")){
		pstmt.setString(jj++,year);
	    }
	    if(!season.equals("")){
		pstmt.setString(jj++,season);
		pstmt.setString(jj++,season);				
	    }
	    if(!lead_id.equals("")){
		pstmt.setString(jj++,lead_id);
	    }
	    if(!category_id.equals("")){
		pstmt.setString(jj++,category_id);
		pstmt.setString(jj++,category_id);
	    }			
	    if(!area_id.equals("")){
		pstmt.setString(jj++,area_id);
	    }
	    if(!nraccount.equals("")){
		pstmt.setString(jj++,"%"+nraccount+"%");
	    }			
	    rs = pstmt.executeQuery();
	    if(list == null)
		list = new ArrayList<>();
	    while(rs.next()){
		ProgCode one = new ProgCode(debug,
					    rs.getString(1),
					    rs.getString(2),
					    null,
					    rs.getString(3),
					    null,
					    rs.getString(4),
					    rs.getString(5));
		String code = rs.getString(6);
		if(code == null || code.isEmpty()){
		    if(list.size() >= max_rows) break;
		    if(!list.contains(one)){
			list.add(one);
		    }
		}
	    }
	    if(list.size() > 0){
		// Collections.sort(list);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back += " Error retrieving data " +ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;						

    }
    /**
     * this is used for report showing
     * we need this to get the list of program names and their id using
     * season and year
     */
    public String find(){
	String message = "", qq="", back="",qw="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	con = Helper.getConnection();
		
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}		
	//
	// we select the field for sort purpose only here
	//
	qq = " select p.id,p.title,p.code,s.sid,s.code from programs p ";
	qq += " left join sessions s on s.id = p.id ";
	if(hasMarket){
	    qq += " join marketing m on m.id=p.id ";
	}
	if(hasSponsor){
	    qq += " join sponsors sp on sp.pid=p.id ";
	}
	if(hasShifts){
	    qq += " join vol_shifts vs on vs.pid=p.id ";
	}
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.id = ?";
	}		
	if(!year.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.year = ?";
	}
	if(!season.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.season = ? or p.season2=?)";
	}
	if(!lead_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.lead_id = ?";
	}						
	if(!category_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.category_id = ? or category2_id=?) ";
	}
	if(!area_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.area_id = ?";
	}
	if(!nraccount.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.nraccount like ?";
	}
	if(hasCode){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.codeNeed is not null and (p.code is not null or s.code is not null))";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by s.code,p.code ";
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);			
	    int jj = 1;
	    if(!id.equals("")){
		pstmt.setString(jj++,id);
	    }			
	    if(!year.equals("")){
		pstmt.setString(jj++,year);
	    }
	    if(!season.equals("")){
		pstmt.setString(jj++,season);
		pstmt.setString(jj++,season);				
	    }
	    if(!lead_id.equals("")){
		pstmt.setString(jj++,lead_id);
	    }
	    if(!category_id.equals("")){
		pstmt.setString(jj++,category_id);
		pstmt.setString(jj++,category_id);
	    }			
	    if(!area_id.equals("")){
		pstmt.setString(jj++,area_id);
	    }
	    if(!nraccount.equals("")){
		pstmt.setString(jj++,"%"+nraccount+"%");
	    }			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		String str2 = rs.getString(2);
		String str3 = rs.getString(3);
		String str4 = rs.getString(4);
		String str5 = rs.getString(5);
		if(str != null){
		    ProgCode pr = new ProgCode(debug, str, str2, str3, str4, str5);
		    if(list == null){
			list = new ArrayList<>();
		    }
		    if(!list.contains(pr)) // no dups
			list.add(pr);
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    message += " Error retrieving data " +ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }

}





































