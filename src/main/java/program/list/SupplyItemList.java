package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class SupplyItemList extends ArrayList<SupplyItem>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(SupplyItemList.class);
    String budget_id = "";
    double total = 0.;
    List<String> errors = null;
    String message = "";
	
    public SupplyItemList(boolean val){
	debug = val;
    }
    public SupplyItemList(boolean val, String val2){
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
	String qq = "select s.id,s.budget_id,s.supply_id,s.rate,s.other,t.name "+
	    "from supply_items s,supply_types t where t.id=s.supply_id and s.budget_id=?";
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
		SupplyItem one =
		    new SupplyItem(debug,
				   rs.getString(1),
				   rs.getString(2),
				   rs.getString(3),
				   rs.getString(4),
				   rs.getString(5),
				   rs.getString(6)
				   );
		double rate = rs.getDouble(4);
		if(rate > 0.)
		    total += rate;
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






















































