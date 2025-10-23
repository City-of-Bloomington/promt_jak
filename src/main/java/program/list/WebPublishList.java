package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class WebPublishList extends CommonInc{

    static Logger logger = LogManager.getLogger(WebPublishList.class);
    String season = "", year="", prog_id="", date_from="", date_to="",
	lead_id="";
    List<WebPublish> publishes = null;
    public WebPublishList(boolean val){
	super(val);
    }
    public void setProg_id(String val){
	if(val != null)
	    prog_id = val;
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
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }		
    public List<WebPublish> getPublishes(){
	return publishes;
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select w.id,date_format(w.date,'%m/%d/%Y'),w.user_id,u.full_name from web_publishes w left join users u on u.id=w.user_id left join web_publish_programs wp on wp.publish_id = w.id left join programs p on p.id=wp.prog_id ";
	String qw = "";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(!prog_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.id = ? ";
	    }
	    else {
		if(!year.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " p.year = ? ";
		}								
		if(!season.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " (p.season = ? or p.season2 = ?)";
		}
		if(!lead_id.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " p.lead_id = ? ";
		}												
		if(!date_from.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " w.date >= str_to_date('"+date_from+"','%m/%d/%Y') ";
		}
		if(!date_to.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " w.date <= str_to_date('"+date_to+"','%m/%d/%Y') ";
		}								
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    qq += " order by id desc limit 10";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!prog_id.equals("")){
		pstmt.setString(j++,prog_id);
	    }
	    else{
		if(!year.equals("")){
		    pstmt.setString(j++,year);
		}								
		if(!season.equals("")){
		    pstmt.setString(j++,season);
		}
		if(!lead_id.equals("")){
		    pstmt.setString(j++,lead_id);
		}								
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		WebPublish one =
		    new WebPublish(debug,
				   rs.getString(1),
				   rs.getString(2),
				   rs.getString(3),
				   rs.getString(4));
		if(publishes == null){
		    publishes = new ArrayList<>();
		}
		if(!publishes.contains(one))
		    publishes.add(one);
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






















































