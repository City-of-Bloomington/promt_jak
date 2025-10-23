package program.list;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class SponsorList extends ArrayList<Sponsor>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(SponsorList.class);
    String message = "";
    List<String> errors = null;
    String pid="", // program id
	id="", // sponsor id
	attendCount="",market="",
	monetary="",tangible="",services="",
	signage="",exhibitSpace="",tshirt="",
	comments="";

	
    Program prog = null;
    //
    public SponsorList(boolean deb){
	//
	// initialize
	//
	debug = deb;
    }	
    public SponsorList(boolean deb, String val){
	//
	// initialize
	//
	debug = deb;
	setPid(val);
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
    public String getId(){
	return id;
    }
    public String getPid(){
	return pid;
    }	
    //
    // setters
    //
    public void setId(String val){
	if(val != null){
	    id = val;
	}
    }
    public void setPid(String val){
	if(val != null){
	    pid = val;
	}
    }	
    public void setAttendCount(String val){
	if(val != null)
	    attendCount = val.trim();
    }
    // needed for new plan
    public void setMarket(String val){
	if(val != null)
	    market = val.trim();
    }
    // needed for new plan
    public void setMonetary(String val){
	if(val != null)
	    monetary = val;
    }
    public void setTangible(String val){
	if(val != null)
	    tangible = val;
    }
    public void setServices(String val){
	if(val != null)
	    services = val;
    }
    public void setSignage(String val){
	if(val != null){
	    signage = val.trim();
	}
    }
    public void setExhibitSpace(String val){
	if(val != null){
	    exhibitSpace = val.trim();
	}
    }
    public void setTshirt(String val){
	if(val != null){
	    tshirt = val.trim();
	}
    }
    public void setComments(String val){
	if(val != null){
	    comments = val.trim();
	}
    }	
	
    public String find(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = "select id,pid,attendCount,market,monetary,tangible,"+
	    " services,signage,exhibitSpace,tshirt,comments "+
	    " from sponsors ", qw = "";
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
	    if(!pid.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " pid=? ";
	    }
	    if(!id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " id=? ";
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!pid.equals("")){			
		pstmt.setString(jj++, pid);
	    }
	    if(!id.equals("")){			
		pstmt.setString(jj++, id);
	    }			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Sponsor one = new Sponsor(debug,
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
					  rs.getString(11));
		add(one);
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
