package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;


public class MarketAdList extends ArrayList<MarketAd>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(MarketAdList.class);
    //	
    String market_id = "";
		
    List<String> errors = null;
    String message = "";
	
    public MarketAdList(boolean val){
	debug = val;
    }
    public MarketAdList(boolean val, String val2){
	debug = val;
	setMarket_id(val2);
    }
    public void setMarket_id(String val){
	if(val != null)
	    market_id = val;
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
	String qq = "select a.id,a.name,d.id,d.market_id,d.direct,d.expenses,date_format(d.due_date,'%m/%d/%Y'),d.details from market_ads a, market_ad_details d  where a.id=d.type_id";
	qq += " and d.market_id=? order by d.id ";  		
	String qw = "";
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
	    int j=1;
	    pstmt.setString(1, market_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		MarketAd one =
		    new MarketAd(debug,
				 rs.getString(1),
				 rs.getString(2),
				 rs.getString(3),
				 rs.getString(4),
				 rs.getString(5),
				 rs.getString(6),
				 rs.getString(7),
				 rs.getString(8)
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






















































