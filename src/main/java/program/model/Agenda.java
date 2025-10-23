package program.model;

import java.util.*;
import java.text.*;
import java.util.Date;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;

public class Agenda{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(Agenda.class);
    public Agenda(boolean deb){
	debug = deb;
    }
    /**
     * Gets the programs and events in this month.
     * get the data info for this month
     * @param mm the month number
     * @param yy the year
     * @return a vector of programs and events.
     */
    public Vector<String>[] getInfo(int mm, int yy,
				    String lead_id, 
				    String area_id, 
				    String location_id,
				    String category_id,
				    String url){
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	int days_in_month = Helper.get_days_in_month(mm, yy); 

	Vector<String>[] vec = new Vector[days_in_month];
	for(int i=0; i<days_in_month; ++i){
	    vec[i] = new Vector<String>();
	}
	boolean inFlag = false;
	String qq = " select pp.title, agi.infoDate,agi.pid,agi.sid "+
	    " from agenda_info agi, programs pp ";
		
	qq += " where agi.pid=pp.id and "+
	    " agi.infoDate >= str_to_date('01/"+mm+"/"+yy+"','%d/%m/%Y') and "+
	    " agi.infoDate <= str_to_date('"+days_in_month+"/"+mm+"/"+yy+
	    "','%d/%m/%Y') ";
	if(!area_id.equals("")){
	    qq += " and pp.area_id = "+area_id;
	}
	if(!lead_id.equals("")){
	    qq += " and pp.lead_id = "+lead_id;
	}
	if(!location_id.equals("")){
	    qq += " and pp.location_id = "+location_id+"";
	}
	if(!category_id.equals("")){
	    qq += " and pp.category_id = "+category_id;
	}
	qq += " order by agi.infoDate,agi.pid,agi.sid "; 

	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();
	    //
	    if(con != null){
		stmt = con.createStatement();
	    }
	    else{
		logger.error("could not create connnection to DB");
	    }
	}catch(Exception ex){
	    logger.error(ex);
	}
	if(con != null){
	    try{
		rs = stmt.executeQuery(qq);
		while(rs.next()){
		    String dateDay = rs.getString(2).substring(8,10);
		    int dayId = Integer.parseInt(dateDay);
		    String str = rs.getString(1);
		    String str3 = rs.getString(3);
		    String str4 = rs.getString(4);
		    if(str4 != null){
			// session
			str += ", Session: "+str4;
			str = "<a href="+url+
			    "Sessions?id="+str3+
			    "&sid="+str4+
			    "&action=zoom>"+str+"</a>";
		    }
		    else{
			str = "<a href="+url+
			    "Program.do?id="+str3+
			    "&action=zoom>"+str+"</a>";
		    }
		    if(str != null && dayId > 0){
			vec[dayId-1].addElement(str);
		    }
		}
	    }
	    catch(Exception ex){
		logger.error(ex+" : "+qq);
	    }
	    finally{
		Helper.databaseDisconnect(con, stmt, rs);
	    }
	}
	return vec;
    }

}






















































