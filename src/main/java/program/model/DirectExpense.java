package program.model;
import java.sql.*;
import java.text.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class DirectExpense extends CommonInc{

    static Logger logger = LogManager.getLogger(DirectExpense.class);
    String id="", budget_id="", description="";
    double expenses = 0;
    //	
    public DirectExpense(){
	super();
    }	
    public DirectExpense(boolean deb,
			 String val,
			 String val2,
			 String val3,
			 String val4
			 ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setBudget_id(val2);
	setDescription(val3);
	setExpenses(val4);
    }
    public DirectExpense(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }		
	
	
    //
    public DirectExpense(boolean deb){
	//
	// initialize
	//
	super(deb);
    }
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof DirectExpense){
	    match = id.equals(((DirectExpense)gg).id);
	}
	return match;
    }
    public int hashCode(){
	int code = 0;
	try{
	    code = Integer.parseInt(id);
	}catch(Exception ex){};
	return code;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getBudget_id(){
	return budget_id;
    }
    public String getDescription(){
	return description;
    }	
    public double getExpenses(){
	return expenses;
    }

    public String toString(){
	return description;
    }
    public boolean isValid(){
	if(description.equals("") ||
	   expenses == 0) return false;
	return true;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val.trim();
    }
    public void setBudget_id(String val){
	if(val != null)
	    budget_id = val.trim();
    }
    public void setDescription(String val){
	if(val != null)
	    description = val;
    }
    public void setExpenses(String val){
	if(val != null){
	    try{
		expenses = Double.parseDouble(val);
	    }catch(Exception ex){}
	}
    }	

    public String doSave(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into budget_direct_expenses values(0,?,?,?)";
	if(budget_id.equals("")){
	    back = "budgetuation id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt.setString(1,budget_id);
	    pstmt.setString(2, description);
	    pstmt.setString(3,""+expenses);
	    pstmt.executeUpdate();
	    //
	    // get the id of the new record
	    //
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);				
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }			
	    message="Saved Successfully";			
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
    public String doUpdate(){
		
	String back = "";
	if(id.equals("")){
	    back = "id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="";
	String qq = "";
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    qq = "update budget_direct_expenses set description=?, "+
		"expenses=? where id=?";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,description);
	    pstmt.setString(2,""+expenses);
	    pstmt.setString(3,id);			
	    pstmt.executeUpdate();
	    message="Updated Successfully";
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(qq);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;

    }
    public String doDelete(){
	String back = "", qq = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    qq = "delete from budget_direct_expenses where id=?";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    message="Deleted Successfully";
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
    public String doSelect(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select d.budget_id,d.description,"+
	    "d.expenses from budget_direct_expenses d where d.id=?";
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
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setBudget_id(rs.getString(1));
		setDescription(rs.getString(2));
		setExpenses(rs.getString(3));
	    }
	    else{
		back= "Record "+id+" not found";
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
