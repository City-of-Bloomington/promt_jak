package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class FeeItemList extends ArrayList<FeeItem>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(FeeItemList.class);
    String budget_id = "";
    double total = 0.;
    List<String> errors = null;
    String message = "";
	
    public FeeItemList(boolean val){
	debug = val;
    }
    public FeeItemList(boolean val, String val2){
	debug = val;
	setBudget_id(val2);
    }
    public void setBudget_id(String val){
	if(val != null)
	    budget_id = val;
    }
    public double getTotal(){
	return total;
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
	String qq = "select s.id,s.budget_id,s.fee_id,s.rate,s.quantity,t.name "+
	    "from fee_items s,fee_types t where t.id=s.fee_id and s.budget_id=?";
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
		FeeItem one =
		    new FeeItem(debug,
				rs.getString(1),
				rs.getString(2),
				rs.getString(3),
				rs.getString(4),
				rs.getString(5),
				rs.getString(6)
				);
		int quantity = rs.getInt(4);
		double rate = rs.getDouble(5);
		if(quantity > 0 && rate > 0.)
		    total += quantity * rate;
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






















































