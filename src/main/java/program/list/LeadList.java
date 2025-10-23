package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class LeadList extends ArrayList<Lead>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(LeadList.class);
    String inactive = "";
    String season = "", year = "";
    boolean active = false;
    List<String> errors = null;
    String message = "";
	
    public LeadList(boolean val){
	debug = val;
    }
    public LeadList(boolean val, boolean val2){
	debug = val;
	if(val2)
	    setActive();
    }
    public void setActive(){
	active = true;
    }
    public void setSeason(String val){
	if(val != null)
	    season = val;
    }
    public void setYear(String val){
	if(val != null)
	    year = val;
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
	
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select g.id,g.name,g.inactive from leads g ";
	String qw = "", qp="";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(active){
		qw = " g.inactive is null ";
	    }
	    if(!season.equals("") || !year.equals("")){
		qp = "select distinct(p.lead_id) from programs p where ";
		if(!season.equals("")){
		    qp += " p.season='"+season+"' ";
		}
		if(!year.equals("")){
		    if(!season.equals(""))qp += " and ";
		    qp += " p.year="+year;
		}
		if(!qw.equals("")) qw += " and ";
		qw =" g.id in ("+qp+") ";
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    qq += " order by g.name ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Lead one =
		    new Lead(debug,
			     rs.getString(1),
			     rs.getString(2),
			     rs.getString(3));
		this.add(one);
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






















































