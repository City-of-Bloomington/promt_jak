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


public class PlanList extends ArrayList<Plan>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(PlanList.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    List<String> errors = null;
    String message = "";
    String id = "", lead_id="", program_title="",objective="",
	profit_obj = "", market="", 
	instructor="", sortby="pl.id DESC ", con_id="";// instructor id
    // pre plan related
    String date_from = "", date_to="", season="", year="";
	
    public PlanList(boolean val){
	debug = val;
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
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setProgram_title(String val){
	if(val != null)
	    program_title = val;
    }	
    public void setCon_id(String val){
	if(val != null)
	    con_id = val;
    }	
    public void setLead_id(String val){
	if(val != null)
	    lead_id = val;
    }
	
    public void setSeason(String val){
	if(val != null)
	    season = val;
    }
    public void setYear(String val){
	if(val != null)
	    year = val;
    }
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }	
    public void setObjective(String val){
	if(val != null)
	    objective = val;
    }
    public void setProfit_obj(String val){
	if(val != null)
	    profit_obj = val;
    }
    public void setInstructor(String val){
	if(val != null)
	    instructor = val;
    }
    public void setMarket(String val){
	if(val != null)
	    market = val;
    }

    public void setSortby(String val){
	if(val != null)
	    sortby = val;
    }	
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select pl.id,pl.program_title from plans pl ";
	String qw = "";
	boolean instrTbl = false, objTbl = false, preTbl = false;
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(!id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " pl.id = ? ";
	    }
	    else{
		if(!instructor.equals("")){
		    instrTbl = true;
		    if(!qw.equals("")) qw += " and ";
		    qw += " c.name like ? ";
		}
		if(!lead_id.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " pl.lead_id = ? ";
		}
		if(!program_title.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " pl.program_title like ? ";
		}				
		if(!profit_obj.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " pl.profit_obj like ? ";
		}				
		if(!objective.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    objTbl = true;
		    qw += " po.objective like ? ";
		}
		if(!market.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " pl.market like ? ";
		}
		if(!season.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " pl.season = ? ";
		}
		if(!year.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " pl.program_year = ? ";
		}
		if(!date_from.equals("")){
		    preTbl = true;
		    if(!qw.equals("")) qw += " and ";
		    qw += " pl.program_date >= ? ";
		}
		if(!date_to.equals("")){
		    preTbl = true;
		    if(!qw.equals("")) qw += " and ";
		    qw += " pl.program_date <= ? ";
		}
		if(!con_id.equals("")){
		    instrTbl = true;
		    if(!qw.equals("")) qw += " and ";
		    qw += " pc.con_id = ? ";
		}				
	    }
	    if(instrTbl){
		qq += ", contacts c,plan_contacts pc ";
		qw += " and c.id = pc.con_id and pl.id=pc.plan_id ";
	    }
	    if(objTbl){
		qq += ", plan_objectives po ";
		qw += " and pl.id = po.plan_id ";
	    }
	    if(preTbl){
		qq += ", pre_plans prl ";
		qw += " and pl.id = prl.id ";
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    if(sortby.equals("")){
		qq += " order by 2 ";
	    }
	    else{
		qq += " order by "+sortby;
	    }
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!id.equals("")){
		pstmt.setString(j++, id);
	    }
	    else{
		if(!instructor.equals("")){
		    pstmt.setString(j++, "%"+instructor+"%");
		}
		if(!lead_id.equals("")){
		    pstmt.setString(j++, lead_id);
		}
		if(!program_title.equals("")){
		    pstmt.setString(j++, "%"+program_title+"%");
		}								
		if(!profit_obj.equals("")){
		    pstmt.setString(j++, "%"+profit_obj+"%");
		}				
		if(!objective.equals("")){
		    pstmt.setString(j++, "%"+objective+"%");
		}
		if(!market.equals("")){
		    pstmt.setString(j++,"%"+market+"%");
		}
		if(!season.equals("")){
		    pstmt.setString(j++, season);
		}
		if(!year.equals("")){
		    pstmt.setString(j++, year);
		}
				
		if(!date_from.equals("")){
		    pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(date_from).getTime()));					
		}
		if(!date_to.equals("")){
		    pstmt.setDate(j++,new java.sql.Date(dateFormat.parse(date_to).getTime()));					
		}
		if(!con_id.equals("")){
		    pstmt.setString(j++, con_id);
		}				
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Plan one =
		    new Plan(debug,
			     rs.getString(1),
			     rs.getString(2));
		this.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    message += back;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
}






















































