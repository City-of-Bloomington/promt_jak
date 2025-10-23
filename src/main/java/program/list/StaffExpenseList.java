package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class StaffExpenseList extends ArrayList<StaffExpense>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(StaffExpenseList.class);
    String budget_id = "";
    double total_direct = 0, total_indirect = 0;
    List<String> errors = null;
    String message = "";
	
    public StaffExpenseList(boolean val){
	debug = val;
    }
    public StaffExpenseList(boolean val, String val2){
	debug = val;
	setBudget_id(val2);
    }
    public void setBudget_id(String val){
	if(val != null)
	    budget_id = val;
    }
    public double getTotalDirect(){
	return total_direct;
    }
    public double getTotalIndirect(){
	return total_indirect;
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
	String qq = "select s.id,s.budget_id,s.staff_id,s.rate,s.hours,s.type,s.quantity, t.name "+
	    "from staff_expenses s,staff_types t where t.id=s.staff_id and s.budget_id=?";
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
	    pstmt.setString(1,budget_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		StaffExpense one =
		    new StaffExpense(debug,
				     rs.getString(1),
				     rs.getString(2),
				     rs.getString(3),
				     rs.getString(4),
				     rs.getString(5),
				     rs.getString(6),
				     rs.getString(7),
				     rs.getString(8)
				     );
		if(one.isDirect()){
		    total_direct += one.getTotal();
		}
		else{
		    total_indirect += one.getTotal();
		}
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






















































