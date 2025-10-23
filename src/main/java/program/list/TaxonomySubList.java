package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class TaxonomySubList extends ArrayList<TaxonomySub>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(TaxonomySubList.class);
    String name = "", tax_id="";
    List<String> errors = null;
    String message = "";
	
    public TaxonomySubList(boolean val){
	debug = val;
    }
    public TaxonomySubList(boolean val, String val2){
	debug = val;
	setTax_id(val2);
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setTax_id(String val){
	if(val != null)
	    tax_id = val;
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
    /**
     * we are expecting values like this
     * x,x,xx so on // could be tax term or sub-tax
     */
    public String findFromIds(String val){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select s.id,s.tax_id,s.name,t.name from taxonomy_subs s, taxonomies t where t.id=s.tax_id and s.id=? ";
	String qq2 = "select '',t.id,'',t.name from taxonomies t where t.id=? ";
	List<String> sub_ids = new ArrayList<>();
	List<String> ids = new ArrayList<>();
	if(val != null && val.length() > 0){
	    if(val.indexOf(",") > -1){
		String arr[] = val.split(",");
		for(String str:arr){
		    if(str.length() > 1){ // subs are like 101, 203,310
			sub_ids.add(str);
		    }
		    else{
			ids.add(str);// if no sub
		    }
		}
	    }
	    else{ // only one
		if(val.length() > 1){
		    sub_ids.add(val); // one sub
		}
		else{
		    ids.add(val); // case one main category
		}
	    }
	}
	if(ids.size() > 0 || sub_ids.size() > 0){
	    Connection con = Helper.getConnection();
	    if(con == null){
		back = "Could not connect to DB";
		addError(back);
		return back;
	    }
	    try{
		if(sub_ids.size() > 0){
		    pstmt = con.prepareStatement(qq);
		    for(String str:sub_ids){
			pstmt.setString(1, str);
			rs = pstmt.executeQuery();
			if(rs.next()){
			    TaxonomySub one =
				new TaxonomySub(debug,
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4));
			    if(!contains(one))
				this.add(one);
			}
		    }
		}
		if(ids.size() > 0){
		    pstmt = con.prepareStatement(qq2);
		    for(String str:ids){
			pstmt.setString(1, str);
			rs = pstmt.executeQuery();
			if(rs.next()){
			    TaxonomySub one =
				new TaxonomySub(debug,
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4));
			    if(!contains(one))
				this.add(one);
			}
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
	}
	return back ;
    }
	
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select s.id,s.tax_id,s.name,t.name from taxonomy_subs s left join taxonomies t on t.id=s.tax_id ";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(!tax_id.equals("")){
		qq += " where s.tax_id = ? ";
	    }				
	    else if(!name.equals("")){
		qq += " where s.name like ? ";
	    }
	    qq += " order by s.name ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!tax_id.equals("")){
		pstmt.setString(1, tax_id);					
	    }
	    else if(!name.equals("")){
		pstmt.setString(1,"%"+name+"%");
	    }			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		TaxonomySub one =
		    new TaxonomySub(debug,
				    rs.getString(1),
				    rs.getString(2),
				    rs.getString(3),
				    rs.getString(4));
		if(!this.contains(one))
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






















































