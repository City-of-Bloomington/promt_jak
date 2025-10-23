package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class TaxonomyList extends ArrayList<Taxonomy>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(TaxonomyList.class);
    String name = "";
    List<String> errors = null;
    String message = "";
	
    public TaxonomyList(boolean val){
	debug = val;
    }
    public TaxonomyList(boolean val, String val2){
	debug = val;
	setName(val2);
    }
    public void setName(String val){
	if(val != null)
	    name = val;
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
	String qq = "select * from taxonomies ";
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
		Taxonomy one =
		    new Taxonomy(debug,
				 rs.getString(1),
				 rs.getString(2));
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






















































