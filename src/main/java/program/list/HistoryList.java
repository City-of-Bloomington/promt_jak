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

public class HistoryList extends ArrayList<History>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(HistoryList.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    List<String> errors = null;
    String message = "";
    String id = "", type="", action_by="", sortby="";
	
    public HistoryList(boolean deb, String val, String val2){
	debug = deb;
	setId(val);
	setType(val2);
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
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }

    public void setSortby(String val){
	if(val != null)
	    sortby = val;
    }
    public String getId(){
	return id;
    }
    public String getType(){
	return type;
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select h.id,h.action,h.type,h.action_by,date_format(h.date_time,'%m/%d/%Y %h:%i'),u.full_name "+
	    "from history h "+			
	    "left join users u on u.id=h.action_by ";
	String qw = "";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(!id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " h.id = ? ";
	    }
	    if(!type.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " h.type = ? ";
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    qq += " order by h.date_time DESC ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!id.equals("")){
		pstmt.setString(j++, id);
	    }
	    if(!type.equals("")){
		pstmt.setString(j++, type);
	    }
	    rs = pstmt.executeQuery();
			
	    while(rs.next()){
		History one =
		    new History(debug,
				rs.getString(1),
				rs.getString(2),
				rs.getString(3),
				rs.getString(4),
				rs.getString(5),
				rs.getString(6)
				);
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






















































