package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class StaffList extends ArrayList<Staff>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(StaffList.class);
    String plan_id = "";
    List<String> errors = null;
    String message = "";
	
    public StaffList(boolean val){
	debug = val;
    }
    public StaffList(boolean val, String val2){
	debug = val;
	setPlan_id(val2);
    }
    public void setPlan_id(String val){
	if(val != null)
	    plan_id = val;
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
	String qq = "select s.id,s.plan_id,s.staff_id,s.quantity,t.name "+
	    "from plan_staffs s,staff_types t where t.id=s.staff_id and s.plan_id=?";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    qq += " order by s.id ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    if(!plan_id.equals("")){
		pstmt.setString(1,plan_id);
	    }			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Staff one =
		    new Staff(debug,
			      rs.getString(1),
			      rs.getString(2),
			      rs.getString(3),
			      rs.getString(4),
			      rs.getString(5)
			      );
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






















































