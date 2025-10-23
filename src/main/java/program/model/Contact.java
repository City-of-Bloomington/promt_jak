package program.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;

public class Contact extends CommonInc{

    boolean debug = false;
    String name="", address="", email="";
    String phone_c="", phone_h="", phone_w="", id="", plan_id="";
    static Logger logger = LogManager.getLogger(Contact.class);
    List<Plan> plans = null;
    public Contact(boolean val){
	debug = val;
    }
    public Contact(boolean val, String val2){
	debug = val;
	setId(val2);
    }
    public Contact(boolean val,
	    String val2,
	    String val3,
	    String val4,
	    String val5,
	    String val6,
	    String val7,
	    String val8
	    ){
	debug = val;
	setId(val2);
	setName(val3);
	setAddress(val4);
	setEmail(val5);
	setPhone_c(val6);
	setPhone_w(val7);		
	setPhone_h(val8);
    }	

    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setAddress(String val){
	if(val != null)
	    address = val;
    }
    public void setEmail (String val){
	if(val != null)
	    email = val;
    }
    public void setPhone_h (String val){
	if(val != null)
	    phone_h = val;
    }
    public void setPhone_c (String val){
	if(val != null)
	    phone_c = val;
    }
    public void setPhone_w (String val){
	if(val != null)
	    phone_w = val;
    }	
    public void setPlan_id (String val){
	if(val != null)
	    plan_id = val;
    }	
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getName(){
	return name;
    }
    public String getAddressCleansed(){
	String ret = "";
	if(!address.equals("")){
	    ret = Helper.replaceSpecialChars(address);
	}
	return ret;
    }
    public String getAddress(){
	return address;
    }	
    public String getEmail(){
	return email;
    }
    public String getPhone_c(){
	return phone_c;
    }
    public String getPhone_h(){
	return phone_h;
    }
    public String getPhone_w(){
	return phone_w;
    }
    public String getPhones(){
	String str = "";
	if(!phone_w.equals("")){
	    str += "w:"+phone_w;
	}
	if(!phone_c.equals("")){
	    if(!str.equals("")) str += ", ";
	    str += "c:"+phone_c;
	}
	if(!phone_h.equals("")){
	    if(!str.equals("")) str += ", ";
	    str += "h:"+phone_h;
	}
	return str;
    }
    public boolean equals(Object gg){
	boolean match = false;
	if (gg != null && gg instanceof Contact){
	    match = id.equals(((Contact)gg).id);
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
    public String toString(){
	return name;
    }
    public boolean hasInfo(){
	return (!id.equals(""));
    }
    public boolean isNew(){
	return id.equals("");
    }
    public boolean isEmpty(){
	return name.isEmpty() && id.isEmpty();
    }
    public String getInfo(){
	return name;
    }
    public boolean hasPlans(){
	if(plans == null){
	    getPlans();
	}
	return plans != null && plans.size() > 0;
    }
    public List<Plan> getPlans(){
	if(plans == null && !id.equals("")){
	    PlanList pl = new PlanList(debug);
	    pl.setCon_id(id);
	    String back = pl.find();
	    if(back.equals("")){
		plans = pl;
	    }
	}
	return plans;
    }
    public String doSaveOrUpdate(){
	if(id.isEmpty()){
	    return doSave();
	}
	else{
	    return doUpdate();
	}
    }
    //
    public String doDelete(){

	String back = "";
	String qq = "delete from contacts where id=?";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;		
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}			
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    message = " Error deleting record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doSelect(){
	//
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String qq = "select name,address,email,phone_c,phone_w,phone_h "+
	    " from contacts where id=?";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		String str = rs.getString(1);
		if(str != null) name = str;
		//
		str = rs.getString(2);
		if(str != null) address = str;
		str = rs.getString(3);
		if(str != null)
		    email = str;
		str = rs.getString(4);
		if(str != null) 
		    phone_c = str;
		str = rs.getString(5);
		if(str != null) 
		    phone_w = str;
		str = rs.getString(6);
		if(str != null) 
		    phone_h = str;				
	    }
	    else{
		message = "No match found";
	    }
	}
	catch(Exception ex){
	    message += " Error retreiving the record " +ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doUpdate(){

	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String qq = "update contacts set name=?,address=?,email=?,phone_c=?,phone_w=?,phone_h=? "+
	    " where id=?";
	if(debug){
	    logger.debug(qq);
	}
	if(name.equals("")){
	    message = "Contact name is required";
	    return message;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}		
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,name);
	    if(address.equals(""))
		pstmt.setString(2,null);
	    else
		pstmt.setString(2,address);
	    if(email.equals(""))
		pstmt.setString(3,null);
	    else
		pstmt.setString(3,email);
	    if(phone_c.equals(""))
		pstmt.setString(4,null);
	    else
		pstmt.setString(4,phone_c);
	    if(phone_w.equals(""))
		pstmt.setString(5,null);
	    else
		pstmt.setString(5,phone_w);			
	    if(phone_h.equals(""))
		pstmt.setString(6,null);
	    else
		pstmt.setString(6,phone_h);
	    pstmt.setString(7,id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    message += " Error updating the record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    //
    public String doSave(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;	
	String back = "";
	String qq = "insert into contacts values (0,?,?,?,?,?,?)";
	if(name.equals("")){
	    message = "Contact name is required";
	    return message;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}				
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,name);
	    if(address.equals(""))
		pstmt.setString(2,null);
	    else
		pstmt.setString(2,address);
	    if(email.equals(""))
		pstmt.setString(3,null);
	    else
		pstmt.setString(3,email);
	    if(phone_c.equals(""))
		pstmt.setString(4,null);
	    else
		pstmt.setString(4,phone_c);
	    if(phone_h.equals(""))
		pstmt.setString(5,null);
	    else
		pstmt.setString(5,phone_h);			
	    if(phone_w.equals(""))
		pstmt.setString(6,null);
	    else
		pstmt.setString(6,phone_w);
	    pstmt.executeUpdate();
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	}
	catch(Exception ex){
	    message = " Error adding the record "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }

}





































