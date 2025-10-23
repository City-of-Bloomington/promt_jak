package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class DirectExpenseList extends ArrayList<DirectExpense>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(DirectExpenseList.class);
    //	
    String budget_id = "";
    double total = 0;
    List<String> errors = null;
    String message = "";
	
    public DirectExpenseList(boolean val){
	debug = val;
    }
    public DirectExpenseList(boolean val, String val2){
	debug = val;
	setBudget_id(val2);
    }
    public void setBudget_id(String val){
	if(val != null)
	    budget_id = val;
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
    public double getTotal(){
	return total;
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select d.id,d.budget_id,d.description,d.expenses from budget_direct_expenses d  where d.budget_id= ? ";
	String qw = "";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    pstmt.setString(1, budget_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		DirectExpense one =
		    new DirectExpense(debug,
				      rs.getString(1),
				      rs.getString(2),
				      rs.getString(3),
				      rs.getString(4)
				      );
		total += rs.getDouble(4);
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






















































