package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class GeneralList extends ArrayList<General>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(GeneralList.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    List<String> errors = null;
    String message = "";
    String id = "", season="", year="",date_from="", date_to="",
	sortby="", lead_id="", code="";
    boolean hasMarket = false, needCode=false, hasCode=false;
    public GeneralList(boolean val){
	debug = val;
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
    public void addError(String val){
	if(errors == null)
	    errors = new ArrayList<String>();
	errors.add(val);
    }
    public void setId(String val){
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
	    date_from = val;
    }
    public void setDateTo(String val){
	if(val != null)
	    date_to = val;
    }	
    public void setSortby(String val){
	if(val != null)
	    sortby = val;
    }
    public void setCode(String val){
	if(val != null)
	    code = val;
    }
    public void setNeedCode(){
	needCode = true;
    }
    public void setHasCode(){
	hasCode = true;
    }		
    public void setHasMarket(){
	hasMarket = true;
    }
    public String getId(){
	return id;
    }
    public String getSeason(){
	return season;
    }
    public String getYear(){
	return year;
    }
    public String getDateFrom(){
	return date_from;
    }
    public String getDateTo(){
	return date_to;
    }
    public String getLead_id(){
	return lead_id;
    }
    public String getCode(){
	return code;
    }		
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select g.id,g.title,g.season,g.year,g.lead_id,date_format(g.date,'%m/%d/%Y'),g.ltime,g.days,g.cost,g.description,l.name,g.code,g.codeNeed "+
	    "from general_listings g "+			
	    "left join leads l on l.id=g.lead_id ";
	if(hasMarket){
	    qq += " join marketing_generals mg on g.id=mg.general_id ";
	}
	String qw = "";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(!id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " g.id = ? ";
	    }
	    else{
		if(!season.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " g.season = ? ";
		}
		if(!year.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " g.year = ? ";
		}
		if(!date_from.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " g.date >= ? ";
		}
		if(!date_to.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " g.date <= ? ";
		}
		if(!lead_id.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " g.lead_id = ? ";
		}
		if(needCode){
		    if(!qw.equals("")) qw += " and ";
		    qw += " (g.codeNeed is not null and g.code is null) ";
		}
		if(hasCode){
		    if(!qw.equals("")) qw += " and ";
		    qw += " g.code is not null ";
		}
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    qq += " order by 2 ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!id.equals("")){
		pstmt.setString(j++, id);
	    }
	    else{
		if(!season.equals("")){
		    pstmt.setString(j++, season);
		}
		if(!year.equals("")){
		    pstmt.setString(j++, year);
		}
		if(!date_from.equals("")){
		    pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(date_from).getTime()));					
		}
		if(!date_to.equals("")){
		    pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(date_to).getTime()));					
		}
		if(!lead_id.equals("")){
		    pstmt.setString(j++, lead_id);
		}				
	    }
				
	    rs = pstmt.executeQuery();
			
	    while(rs.next()){
		General one =
		    new General(debug,
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
				rs.getString(13)
				);
		if(!this.contains(one))
		    this.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    message += back;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
}






















































