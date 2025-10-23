package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class FacilityList extends ArrayList<Facility>{

    boolean debug = false;
    String statement="", name="", schedule="", season="", year="";
    String closings="", other="", id="", lead_id="", type="";
    static Logger logger = LogManager.getLogger(FacilityList.class);
    boolean hasMarket = false;
    public FacilityList(boolean val){
	debug = val;
    }

    //
    // setters
    //
    public void setStatement(String val){
	if(val != null)
	    statement = val;
    }
    public void setName (String val){
	if(val != null)
	    name = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }		
    public void setSeason(String val){
	if(val != null)
	    season = val;
    }
    public void setYear (String val){
	if(val != null)
	    year = val;
    }		
    public void setSchedule (String val){
	if(val != null)
	    schedule = val;
    }
    public void setClosings(String val){
	if(val != null)
	    closings = val;
    }
    public void setOther (String val){
	if(val != null)
	    other = val;
    }
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setLead_id (String val){
	if(val != null)
	    lead_id = val;
    }
    public void setHasMarket(){
	hasMarket = true;
    }
    //
    public String find(){
	return lookFor();
    }
    public String lookFor(){
	//
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();		
	String message = "";
	String qq = "select f.id,f.name,f.statement,f.schedule,f.closings,f.other,f.lead_id,l.name,f.type from facilities f left join leads l on l.id = f.lead_id ";
	if(hasMarket){
	    qq += " join marketing_facilities mf on f.id=mf.facility_id ";
	}
	String qw = "", qo=" order by f.name ";
	if(!name.equals("")){
	    qw += "f.name like ?";
	}
	if(!schedule.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "f.schedule like ?";
	}
	if(!statement.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "f.statement like ?";
	}
	if(!closings.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "f.closings like ?";
	}
	if(!other.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "f.other like ?";
	}
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "f.id=?";
	}
	if(!lead_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "f.lead_id=?";
	}
	if(!type.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "f.type=?";
	}				
	if(hasMarket){
	    if(!season.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "mf.season=?";
	    }
	    if(!year.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "mf.year=?";
	    }
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " "+qo;
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj = 1;
	    if(!name.equals("")){
		pstmt.setString(jj++,name);

	    }
	    if(!schedule.equals("")){
		pstmt.setString(jj++,schedule);

	    }
	    if(!statement.equals("")){
		pstmt.setString(jj++,statement);

	    }
	    if(!closings.equals("")){
		pstmt.setString(jj++,closings);
	    }
	    if(!other.equals("")){
		pstmt.setString(jj++,other);
	    }
	    if(!id.equals("")){
		pstmt.setString(jj++,id);
	    }
	    if(!lead_id.equals("")){
		pstmt.setString(jj++,lead_id);
	    }
	    if(!type.equals("")){
		pstmt.setString(jj++,type);
	    }						
	    if(hasMarket){
		if(!season.equals("")){
		    pstmt.setString(jj++, season);
		}
		if(!year.equals("")){
		    pstmt.setString(jj++, year);
		}
	    }						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Facility one = new Facility(debug,
					    rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6),
					    rs.getString(7),
					    rs.getString(8),
					    rs.getString(9));
		if(!this.contains(one))
		    add(one);
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

}





































