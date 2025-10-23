package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class TypeList extends ArrayList<Type>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(TypeList.class);
    String table_name = "categories";
    int column_count = 2;
    String name = "", id="";
    List<String> errors = null;
    String message = "";
	
    public TypeList(boolean val){
	debug = val;
    }
    public TypeList(boolean val, String val2){
	debug = val;
	setTableName(val2);
    }
    public void setTableName(String val){
	if(val != null)
	    table_name = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
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
	back = findFieldsCount();
	String qq = "select * from "+table_name+" ";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(!name.equals("")){
		qq += " where name like ? ";
	    }
	    qq += " order by name ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!name.equals("")){
		pstmt.setString(1,"%"+name+"%");
	    }			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(column_count == 2){
		    Type one =
			new Type(debug,
				 rs.getString(1),
				 rs.getString(2));
		    this.add(one);
		}
		else{
		    Type one =
			new Type(debug,
				 rs.getString(1),
				 rs.getString(2),
				 rs.getString(3),
				 table_name);
		    this.add(one);
		}
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
    private String findFieldsCount(){
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select * "+
	    "from "+table_name+" ";
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
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		column_count = rs.getMetaData().getColumnCount();
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






















































