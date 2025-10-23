package program.list;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class VolTrainList extends ArrayList<VolTrain>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(VolTrainList.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String message = "";
    List<String> errors = null;
    String pid="", // program id
	id="", shift_id="",
	dateFrom="", dateTo="";

	
    Program prog = null;
    //
    public VolTrainList(boolean deb){
	//
	// initialize
	//
	debug = deb;
    }	
    public VolTrainList(boolean deb, String val, String val2){
	//
	// initialize
	//
	debug = deb;
	setPid(val);
	setShift_id(val2);
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
    public void setShift_id(String val){
	if(val != null){
	    shift_id = val;
	}
    }	
    public void setDateFrom(String val){
	if(val != null)
	    dateFrom = val.trim();
    }
    public void setDateTo(String val){
	if(val != null)
	    dateTo = val;
    }
	
    public String find(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = "select v.id,v.pid,v.shift_id,date_format(v.date,'%m/%d/%Y'),"+
	    " v.startTime,v.endTime,v.location_id,v.other,v.tdays,v.notes,l.name  "+
	    " from vol_trainings v left join locations l on v.location_id=l.id ", qw = "";			
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
		qw += " v.pid=? ";
	    }
	    if(!shift_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " v.shift_id=? ";
	    }			
	    if(!id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " v.id=? ";
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!pid.equals("")){			
		pstmt.setString(jj++, pid);
	    }
	    if(!shift_id.equals("")){			
		pstmt.setString(jj++, shift_id);
	    }					
	    if(!id.equals("")){			
		pstmt.setString(jj++, id);
	    }			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		VolTrain one = new VolTrain(debug,
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
