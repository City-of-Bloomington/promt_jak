package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class LocationList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(LocationList.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String name = "", id="", facility_id="";
    String effective_date = "";
    List<String> errors = null;
    boolean active_only = false, currentOnly = false;
    List<Location> locations = null;
    String activeStatus = "All";
    String message = "";
	
    public LocationList(boolean val){
	debug = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setFacility_id(String val){
	if(val != null)
	    facility_id = val;
    }
    public void setActiveOnly(){
	active_only = true;
    }
    public void setActiveStatus(String val){
	if(val != null){
	    activeStatus = val;				
	    if(val.equals("All")){
		active_only = false;
	    }
	    else if(val.equals("active")){
		active_only = true;
	    }
	}
    }
    public String getActiveStatus(){
	return activeStatus;
    }
    public void setActive(String val){
	if(val != null && !val.equals(""))
	    active_only = true;
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
    public void setCurrentOnly(){
	currentOnly = true;
	effective_date = Helper.getToday2();				
    }
    public void setEffective_date(String val){
	if(val != null){
	    if(val.indexOf("/") > -1){
		effective_date = val;
	    }
	    else if(val.indexOf("-") > -1){
		try{
		    java.util.Date date = df2.parse(val);
		    effective_date = dateFormat.format(date);
		}catch(Exception ex){}
	    }
	}
    }
    public List<String> getErrors(){
	return errors;
    }
    void addError(String val){
	if(errors == null)
	    errors = new ArrayList<String>();
	errors.add(val);
    }
    public List<Location> getLocations(){
	return locations ;
    }
	
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select l.id,l.name,l.facility_id,l.active,f.name, "+
	    "f.statement,f.schedule,f.closings,f.other,f.lead_id,f.type, "+
	    "l.location_url "+
	    "from locations l left join facilities f on l.facility_id=f.id ";
	String qw = "";
						
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(!effective_date.equals("")){
		String related_season = Helper.getRelatedSeason(effective_date);
		String next_season =  Helper.getRelatedSeason(effective_date);
		int related_year = Helper.getDatePart(effective_date,"year");
		int next_season_year = Helper.getNextSeasonYear(effective_date);
		if(!qw.equals("")) qw += " and ";
		qw += " l.id in (select p.location_id from programs p where ";
		qw += "(p.regDeadLine >= str_to_date('"+effective_date+"','%m/%d/%Y') "+
		    " or p.regDeadLine is null) and ((p.season = '"+related_season+"' and p.year = "+related_year+") or (p.season='Ongoing' and p.year="+related_year+") or (p.season = '"+next_season+"' and "+
		    "p.year="+next_season_year+"))) ";						
	    }						
	    if(!name.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " l.name like ? ";
	    }
	    if(!id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " l.id = ? ";
	    }
	    if(!facility_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " l.facility_id = ? ";
	    }
	    if(active_only){
		if(!qw.equals("")) qw += " and ";
		qw += " l.active is not null ";
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    qq += " order by l.name ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!name.equals("")){
		pstmt.setString(j++,"%"+name+"%");								
	    }
	    if(!id.equals("")){
		pstmt.setString(j++,id);								
	    }
	    if(!facility_id.equals("")){
		pstmt.setString(j++,facility_id);								
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Location one =
		    new Location(debug,
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
				 rs.getString(12));
		if(locations == null){
		    locations = new ArrayList<>();
		}
		if(!locations.contains(one))
		    locations.add(one);
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






















































