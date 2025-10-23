package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;
/**
 *
 */

public class ContactList {

    boolean debug = false;
    static Logger logger = LogManager.getLogger(ContactList.class);
    //	
    String plan_id = "", name="", address="", phone="", id="", email="";
		
    List<String> errors = null;
    String message = "";
    List<Contact> contacts = null;
    public ContactList(boolean val){
	debug = val;
    }
    public ContactList(boolean val, String val2){
	debug = val;
	setPlan_id(val2);
    }
    public void setPlan_id(String val){
	if(val != null)
	    plan_id = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setPhone(String val){
	if(val != null)
	    phone = val;
    }
    public void setEmail(String val){
	if(val != null)
	    email = val;
    }
    public void setAddress(String val){
	if(val != null)
	    address = val;
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
    public List<Contact> getContacts(){
	return contacts;
    }
	
	
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select c.id,c.name,c.address,c.email,c.phone_c,c.phone_w,c.phone_h from contacts c ";
	String qw = "", qf="";
			
	if(!plan_id.equals("")){
	    qf = ", plan_contacts p ";
	    qw = " p.con_id=c.id and p.plan_id=? ";
	}
	if(!name.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " c.name like ? ";
	}
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " c.id = ? ";
	}
	if(!address.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " c.address like ? ";
	}
	if(!email.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " c.email like ? ";
	}		
	if(!phone.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " (c.phone_c like ? or c.phone_h like ? or phone_w like ?) ";
	}		
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(!qw.equals("")){
		qq += qf+" where "+qw;
	    }
	    qq += " order by c.name ";
	    if(debug){
		logger.debug(qq);
	    }
			
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!plan_id.equals("")){
		pstmt.setString(j++, plan_id);
	    }
	    if(!name.equals("")){
		pstmt.setString(j++, name+"%");
	    }
	    if(!id.equals("")){
		pstmt.setString(j++, id);
	    }
	    if(!address.equals("")){
		pstmt.setString(j++, "%"+address+"%");
	    }
	    if(!email.equals("")){
		pstmt.setString(j++, "%"+email+"%");
	    }
	    if(!phone.equals("")){
		pstmt.setString(j++, "%"+phone+"%");
		pstmt.setString(j++, "%"+phone+"%");
		pstmt.setString(j++, "%"+phone+"%");
	    }			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Contact one =
		    new Contact(debug,
				rs.getString(1),
				rs.getString(2),
				rs.getString(3),
				rs.getString(4),
				rs.getString(5),
				rs.getString(6),
				rs.getString(7)
				);
		if(contacts == null)
		    contacts = new ArrayList<>();
		if(!contacts.contains(one))
		    contacts.add(one);
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






















































