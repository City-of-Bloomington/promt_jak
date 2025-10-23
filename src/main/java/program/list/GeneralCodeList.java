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

public class GeneralCodeList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(GeneralCodeList.class);
    final static int  max_rows = 20; // for code entry
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id="", pid="";
    String lead_id="", season="", year="", date_from="", date_to="";
    String sortBy="", limit="20";
    boolean hasCode=false, hasNoCodeYet=false;
		
    public GeneralCodeList(boolean val){
	debug = val;
    }
    List<GeneralCode> list = null;
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
    public void setSortBy(String val){
	if(val != null)
	    sortBy = val;
    }
    public void setDateFrom(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDateTo(String val){
	if(val != null)
	    date_to = val;
    }	
    public void setHasNoCodeYet(){
	hasNoCodeYet = true;
    }		
    public void setHasCode(){
	hasCode = true;
    }
    public void setLimit(){
	limit = " limit "+max_rows;
    }
    public List<GeneralCode> getList(){
	Collections.sort(list);
	return list;
    }
    public String doUpdate(String ids[], String codes[]){
	String back="";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	String qq = "update general_listings set code=? where id=? ";
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
	    for(int jj=0;jj<ids.length;jj++){
		if(!ids[jj].equals("") && !codes[jj].equals("")){
		    System.err.println(" id, code "+ids[jj]+" "+codes[jj]);
		    pstmt.setString(1, codes[jj]);
		    pstmt.setString(2, ids[jj]);
		    pstmt.executeUpdate();
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
    //
    /**
     * we need this to update codes in general listings
     * here we do not have code yet, we are looking for the list
     * that do not have code
     */
    public String lookFor(){
	String back="";
	String qq = "select g.id,g.title,l.name from general_listings g left join leads l on l.id=g.lead_id ";

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
	if(hasNoCodeYet){
	    if(!qw.equals("")) qw += " and ";
	    qw += "g.code is null and g.codeNeed is not null ";
	}
	if(!year.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "g.year = ?";
	}
	if(!season.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "g.season = ?";
	}
	if(!lead_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "g.lead_id = ?";
	}
	if(!date_from.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " g.date >= ? ";
	}
	if(!date_to.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " g.date <= ? ";
	}				
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	if(!limit.equals("")){
	    qq += limit;
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);			
	    int jj = 1;
	    if(!year.equals("")){
		pstmt.setString(jj++,year);
	    }
	    if(!season.equals("")){
		pstmt.setString(jj++,season);
	    }
	    if(!lead_id.equals("")){
		pstmt.setString(jj++,lead_id);
	    }
	    if(!date_from.equals("")){
		pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_from).getTime()));					
	    }
	    if(!date_to.equals("")){
		pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_to).getTime()));					
	    }						
	    rs = pstmt.executeQuery();
	    if(list == null)
		list = new ArrayList<>();
	    while(rs.next()){
		GeneralCode one = new GeneralCode(debug,
						  rs.getString(1),
						  rs.getString(2),
						  rs.getString(3),
						  null);
		if(list.size() >= max_rows) break;
		if(!list.contains(one)){
		    list.add(one);
		}
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
     * this is used for code reports
     * we need this to get the list of general listings title and their id using
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
	qq = " select g.id,g.title,l.name,g.code from general_listings g left join leads l on g.lead_id=l.id ";
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "g.id = ?";
	}		
	if(!year.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "g.year = ?";
	}
	if(!season.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "g.season = ?";
	}
	if(!lead_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "g.lead_id = ?";
	}						
	if(hasCode){
	    if(!qw.equals("")) qw += " and ";
	    qw += "g.code is not null";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by g.code ";
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
	    }
	    if(!lead_id.equals("")){
		pstmt.setString(jj++,lead_id);
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		String str2 = rs.getString(2);
		String str3 = rs.getString(3);
		String str4 = rs.getString(4);
		if(str != null){
		    GeneralCode one = new GeneralCode(debug, str, str2, str3, str4);
		    if(list == null){
			list = new ArrayList<>();
		    }
		    if(!list.contains(one)) // no dups
			list.add(one);
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





































