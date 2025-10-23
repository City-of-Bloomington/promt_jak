package program.list;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class VolShiftList extends ArrayList<VolShift>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(VolShiftList.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String message = "";
    List<String> errors = null;
    String pid="", // program id
	id="",  // shift id
	dateFrom="", dateTo="", category_id="", sortby="",
	onsite="",pre_train="", lead_id="", season="", year="";
	
    boolean noPid = false;
    Program prog = null;
    //
    public VolShiftList(boolean deb){
	//
	// initialize
	//
	debug = deb;
    }	
    public VolShiftList(boolean deb, String val){
	//
	// initialize
	//
	debug = deb;
	setPid(val);
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
    public String getId(){
	return id;
    }
    public String getPid(){
	return pid;
    }
    public String getCategory_id(){
	return category_id;
    }
    public String getLead_id(){
	return lead_id;
    }	
    public String getDateFrom(){
	return dateFrom;
    }
    public String getDateTo(){
	return dateTo;
    }
    public String getSeason(){
	return season;
    }
    public String getSortby(){
	return sortby;
    }		
    public String getYear(){
	return year;
    }	
    public void setNoPid(){
	noPid = true;
    }
	
    //
    // setters
    //
    public void setId(String val){
	if(val != null){
	    id = val;
	}
    }
    public void setPid(String val){
	if(val != null){
	    pid = val;
	    noPid = false;
	}
    }
    public void setLead_id(String val){
	if(val != null){
	    lead_id = val;
	}
    }
    public void setSortby(String val){
	if(val != null){
	    sortby = val;
	}
    }
				
    public void setCategory_id(String val){
	if(val != null){
	    category_id = val;
	}
    }	
    public void setDateFrom(String val){
	if(val != null)
	    dateFrom = val.trim();
    }
    public void setDateTo(String val){
	if(val != null)
	    dateTo = val;
    }
    public void setOnsite(String val){
	if(val != null)
	    onsite = val;
    }
    public void setSeason(String val){
	if(val != null)
	    season = val;
    }
    public void setYear(String val){
	if(val != null)
	    year = val;
    }	
    public void setPre_train(String val){
	if(val != null)
	    pre_train = val;
    }
	
    public String find(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String dateFrom2 = "";
	String dateTo2 = "";
	if(dateFrom.equals("") && dateTo.equals("") &&
	   !season.equals("") && !year.equals("")){
	    String ret[] = Helper.getStartEndDateFromSeasonYear(season, year);
	    if(ret != null){
		dateFrom2 = ret[0];
		dateTo2 = ret[1];
	    }
	}
	//
	// volunteer for other than programs first
	//
	String qq = "select v.id,v.pid,v.volCount,v.days,date_format(v.date,'%m/%d/%Y'),"+
	    " v.startTime,v.endTime,v.category_id,v.notes,v.duties,v.onsite,v.pre_train,c.name,v.lead_id,l.name,v.title "+
	    " from vol_shifts v left join categories c on c.id=v.category_id "+
	    " left join leads l on v.lead_id=l.id ";
	String qw = " where v.pid is null ";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    int jj=1;
	    if(pid.equals("")){
		if(!id.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " v.id=? ";
		}
		if(!category_id.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " v.category_id=? ";
		}			
		if(!dateFrom2.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " v.date >= ? ";
		}
		if(!dateTo2.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " v.date <= ? ";
		}
		if(!lead_id.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " v.lead_id = ? ";
		}
		if(!year.equals("") &&
		   dateFrom2.equals("") && dateTo2.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " year(v.date) = ? ";
		}
		qq += qw;
		qq += " order by v.date ";
		if(debug){
		    logger.debug(qq);
		}
				
		pstmt = con.prepareStatement(qq);
		jj=1;
		if(!id.equals("")){			
		    pstmt.setString(jj++, id);
		}
		if(!category_id.equals("")){			
		    pstmt.setString(jj++, category_id);
		}			
		if(!dateFrom2.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateFrom2).getTime()));	
		}
		if(!dateTo2.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateTo2).getTime()));	
		}
		if(!lead_id.equals("")){			
		    pstmt.setString(jj++, lead_id);
		}
		if(!year.equals("") &&
		   dateFrom2.equals("") && dateTo2.equals("")){
		    int yy = Integer.parseInt(year);
		    pstmt.setInt(jj++, yy);
		}
		rs = pstmt.executeQuery();
		while(rs.next()){
		    VolShift one = new VolShift(debug,
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
						rs.getString(16));
		    add(one);
		}
	    }
	    if(!noPid){
		qq = "select v.id,v.pid,v.volCount,v.days,date_format(v.date,'%m/%d/%Y'),"+
		    " v.startTime,v.endTime,v.category_id,v.notes,v.duties,v.onsite,v.pre_train,c.name,p.lead_id,l.name,v.title "+
		    " from vol_shifts v left join categories c on c.id=v.category_id "+
		    " inner join programs p on p.id=v.pid "+					
		    " left join leads l on p.lead_id=l.id ";
		qw = "";
		if(!pid.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " v.pid=? ";
		}
		if(!category_id.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " v.category_id=? ";
		}			
		if(!dateFrom.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " v.date >= ? ";
		}
		if(!dateTo.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " v.date <= ? ";
		}
		if(!lead_id.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "(p.lead_id=? or v.lead_id = ?) ";
		}
		if(!season.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " p.season = ? ";
		}
		if(!year.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " p.year = ? ";
		}
		if(!qw.equals("")){
		    qq += " where "+qw;
		}
		if(sortby.equals(""))
		    qq += " order by v.date,p.title ";
		else {
		    if(sortby.indexOf("title") > -1)
			qq += " order by p.title ";
		    else if(sortby.indexOf("Date") > -1)
			qq += " order by v.date ";
		    else
			qq += " order by "+sortby;
		}
		if(debug){
		    logger.debug(qq);
		}
		pstmt = con.prepareStatement(qq);
		jj=1;
		if(!pid.equals("")){			
		    pstmt.setString(jj++, pid);
		}
		if(!category_id.equals("")){			
		    pstmt.setString(jj++, category_id);
		}			
		if(!dateFrom.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateFrom).getTime()));	
		}
		if(!dateTo.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateTo).getTime()));	
		}
		if(!lead_id.equals("")){			
		    pstmt.setString(jj++, lead_id);
		    pstmt.setString(jj++, lead_id);					
		}
		if(!season.equals("")){			
		    pstmt.setString(jj++, season);
		}
		if(!year.equals("")){			
		    pstmt.setString(jj++, year);
		}
		rs = pstmt.executeQuery();
		while(rs.next()){
		    VolShift one = new VolShift(debug,
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
						rs.getString(16));
		    if(!this.contains(one))
			this.add(one);
		}
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
