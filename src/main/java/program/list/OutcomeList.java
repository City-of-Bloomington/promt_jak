package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class OutcomeList extends ArrayList<Outcome>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(OutcomeList.class);
    String eval_id = "";
    List<String> errors = null;
    String message = "";
	
    public OutcomeList(boolean val){
	debug = val;
    }
    public OutcomeList(boolean val, String val2){
	debug = val;
	setEval_id(val2);
    }
    public void setEval_id(String val){
	if(val != null)
	    eval_id = val;
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
	String qq = "select g.id,g.eval_id,g.obj_id,g.outcome, "+
	    " o.plan_id,o.objective "+
	    " from eval_outcomes g, plan_objectives o where g.obj_id=o.id and g.eval_id=? ";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    qq += " order by g.id ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,eval_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Objective one =
		    new Objective(debug,
				  rs.getString(2),
				  rs.getString(5),
				  rs.getString(6));
		Outcome two = new Outcome(debug,
					  rs.getString(1),
					  rs.getString(2),
					  rs.getString(3),
					  rs.getString(4),
					  one);
										  
		this.add(two);
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






















































