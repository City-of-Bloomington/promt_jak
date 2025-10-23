package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;

public class TaxonomySub extends CommonInc{

    static Logger logger = LogManager.getLogger(TaxonomySub.class);
    String id="", tax_id="", name="";
    Type taxonomy = null;
    //
    public TaxonomySub(){
	super();
    }
    public TaxonomySub(boolean deb, String val){
	//
	super(deb);
	setId(val);
    }
		
    public TaxonomySub(boolean deb, String val, String val2, String val3){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setTax_id(val2);		
	setName(val3);
    }
    public TaxonomySub(boolean deb,
		       String val,
		       String val2,
		       String val3,
		       String val4){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setTax_id(val2);		
	setName(val3);
	if(val4 != null){
	    taxonomy = new Type(debug, val2, val4);
	}
    }
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof TaxonomySub){
	    match = id.equals(((TaxonomySub)gg).id);
	}
	return match;
    }
    public int hashCode(){
	int code = 13;
	try{
	    code = Integer.parseInt(id);
	}catch(Exception ex){};
	return code;
    }
    public Type getTaxonomy(){
	if(taxonomy == null && !tax_id.equals("")){
	    Type one = new Type(debug, tax_id);
	    one.setTableName("taxonomies");
	    String back = one.doSelect();
	    if(back.equals("")){
		taxonomy = one;
	    }
	}
	return taxonomy;
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
    public String getTax_id(){
	return tax_id;
    }	
    public String toString(){
	return name;
    }
    public String getCompistName(){
	getTaxonomy();
	String ret = "";
	if(taxonomy != null){
	    ret = taxonomy.getName();
	}
	if(!name.equals("")){
	    if(!ret.equals("")){
		ret += "->";
	    }
	    ret += name;
	}
	return ret;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setTax_id(String val){
	if(val != null)
	    tax_id = val;
    }	

    public String doSave(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "";
	qq =  "insert into taxonomy_subs values(0,?,?)";

	if(name.equals("") || tax_id.equals("")){
	    back = " name or top taxonomy type not set ";
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
	    pstmt.setString(1,tax_id);
	    pstmt.setString(2,name);
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
	if(name.equals("")){
	    back = "name not set ";
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
	    qq = "update taxonomy_subs set name=?  where id=?";

	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setString(jj++,name);
	    pstmt.setString(jj++,id);			
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
	    qq = "delete from taxonomy_subs where id=?";
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
	String qq = "select tax_id, name where id=?";
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
		setTax_id(rs.getString(1));
		setName(rs.getString(2));
	    }
	    else{
		message= "Record "+id+" not found";
		return message;
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
