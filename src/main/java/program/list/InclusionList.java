package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class InclusionList extends ArrayList<Inclusion>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(InclusionList.class);
    //
    String id = "", year="", lead_id="",category_id="",nraccount="", area_id="",
	season="", sortBy="i.id ";
		
    List<String> errors = null;
    String message = "";
	
    public InclusionList(boolean val){
	debug = val;
    }
    public void setSortBy(String val){
	if(val != null)
	    sortBy = val;
    }	
    public void setSeason(String val){
	if(val != null)
	    season = val;
    }
    public void setYear(String val){
	if(val != null)
	    year = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setLead_id(String val){
	if(val != null)
	    lead_id = val;
    }
    public void setArea_id(String val){
	if(val != null)
	    area_id = val;
    }
    public void setCategory_id(String val){
	if(val != null)
	    category_id = val;
    }
    public void setNraccount(String val){
	if(val != null)
	    nraccount = val;
    }
    public void setSortby(String val){
	if(val != null)
	    sortBy = val;
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
	String qq = "select i.id,i.consult_ada,i.training,i.train_aware,"+
	    "i.train_basics,i.train_consider,i.train_behave,i.train_trip,"+
	    "i.train_other,i.comments,i.market,i.site_visit,i.consult,i.sign,"+
	    "i.prov_sign,i.consult_pro from programs_inclusion i,programs p "+
	    " where i.id=p.id ";
	if(!id.equals("")){
	    qq += " and i.id = ? ";
	}
	else {
	    if(!season.equals("")){
		qq += " and p.season = ? ";
	    }
	    if(!year.equals("")){
		qq += " and p.year = ? ";
	    }
	    if(!lead_id.equals("")){
		qq += " and p.lead_id = ? ";
	    }
	    if(!area_id.equals("")){
		qq += " and p.area_id = ? ";
	    }
	    if(!category_id.equals("")){
		qq += " and p.category_id = ? ";
	    }
	    if(!nraccount.equals("")){
		qq += " and p.nraccount = ? ";
	    }
	}
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
	    int jj=1;
	    if(!id.equals("")){
		pstmt.setString(jj++, id);
	    }
	    else{
		if(!season.equals("")){
		    pstmt.setString(jj++, season);
		}
		if(!year.equals("")){
		    pstmt.setString(jj++, year);
		}
		if(!lead_id.equals("")){
		    pstmt.setString(jj++, lead_id);
		}
		if(!area_id.equals("")){
		    pstmt.setString(jj++, area_id);
		}
		if(!category_id.equals("")){
		    pstmt.setString(jj++, category_id);
		}
		if(!nraccount.equals("")){
		    pstmt.setString(jj++, nraccount);
		}				
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Inclusion one =
		    new Inclusion(debug,
				  rs.getString(1),
				  rs.getString(2),
				  rs.getString(3),
				  rs.getString(4),
				  rs.getString(5),
				  rs.getString(6),
				  rs.getString(7),
				  rs.getString(8),
				  rs.getString(9),
				  rs.getString(10),
				  rs.getString(11),
				  rs.getString(12),
				  rs.getString(13),
				  rs.getString(14),
				  rs.getString(15),
				  rs.getString(16)
				  );
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






















































