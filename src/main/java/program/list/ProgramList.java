package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;


public class ProgramList extends ArrayList<Program>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(ProgramList.class);	
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String id="", plan_id="";
    String lead_id="", season="", year=""; 
    String category_id="", statement="", days=""; // 2:old
    String nraccount="", fee="", oginfo="",area_id="", title="";
    String inCityFee="", nonCityFee="",otherFee="";
    String location_id="", instructor="",startTime="";
    String age="", partGrade="", description="";
    String ageFrom="", ageTo="",code="",wParent="", allAge="";
    String minMaxEnroll="", codeNeed="",classCount="";
    String dateFrom="", dateTo="", dateAt="", whichDate="";
    String codeTask="", marketTask="", volTask="", sponTask="",
	budgetTask="", evalTask="",showType="";
    String unCode="", unMarket="", unVol="", unSpon="", subcat="",
	unBudget="",unEval="", sortby="", noPublish = "",
	taxonomy_term="", facility_term="", // for promt service
	taxonomy_id="", taxonomy_ids="", effective_date="",
	publish_id = "";
		
    String limit = " ";
    boolean hasMarket = false, hasSponsor = false, hasCode = false;
    boolean hasShifts = false, forPublishOnly=false;
    boolean forServicePublishOnly = false;
    boolean readyToPublish = false, approvedPublish = false;
    public ProgramList(boolean val){
	debug = val;
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setFacility_term (String val){
	if(val != null)
	    facility_term = val;
    }
    // sub_cat always overide
    public void setTaxonomy_ids(String val){
	if(val != null && !val.equals("")){
	    taxonomy_ids = val;
	}
    }
    // cat 
    public void setTaxonomy_id(String val){
	if(val != null && !val.equals("")){
	    taxonomy_id = val; 
	}
    }
    public void setReadyToPublish(){
	readyToPublish = true;
    }
    public void setApprovedPublish(){
	approvedPublish = true;
    }
    public void forServicePublishOnly(){
	forServicePublishOnly = true;
    }		
    public void setPublish_id(String val){
	if(val != null){
	    publish_id = val;
	}
    }
		
    public void setLocation_id(String val){
	if(val != null)
	    location_id = val; 
    }		
    public void setCurrentOnly(){
	effective_date = Helper.getToday2();
    }
    public void setTitle(String val){
	if(val != null)
	    title = "%"+val+"%";
    }
    public void setLead_id(String val){
	if(val != null)
	    lead_id = val;
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
    public void setStatement(String val){
	if(val != null)
	    statement = val+"%";
    }
    public void setYear(String val){
	if(val != null)
	    year = val;
    }	
    public void setSeason(String val){
	if(val != null)
	    season = val;
    }
    public void setAllAge(){
	allAge = "y";
    }
    public void setCategory_id(String val){
	if(val != null)
	    category_id = val;
    }
    public void setNraccount(String val){
	if(val != null)
	    nraccount = val;
    }
    public void setFee(String val){
	if(val != null)
	    fee = val;
    }
    public void setDeadLineFrom(String val){
	if(val != null){
	    whichDate = "regDeadLine";
	    dateFrom = val;
	}
    }	
    public void setOginfo(String val){
	if(val != null)
	    oginfo = val+"%";
    }
    public void setInCityFee(String val){
	if(val != null)
	    inCityFee= val;		  
    }
    public void setNonCityFee(String val){
	if(val != null)
	    nonCityFee = val;
    }
    public void setPOtherFee(String val){
	if(val != null)
	    otherFee = val;
    }
    public void setPPartGrade(String val){
	if(val != null)
	    partGrade = val;
    }
    public void setPMinMaxEnroll(String val){
	if(val != null)
	    minMaxEnroll = val;
    }
    public void setInstructor(String val){
	if(val != null)
	    instructor = val;
    }
    public void setPDescription(String val){
	if(val != null)
	    description = "%"+val+"%";
    }
    public void setPStartTime(String val){
	if(val != null)
	    startTime = val;
    }
    public void setPClassCount(String val){
	if(val != null)
	    classCount = val;
    }
    public void setCode(String val){
	if(val != null)
	    code = val;
    }
    public void setArea_id(String val){
	if(val != null)
	    area_id = val;
    }
    public void setDays(String val){
	if(val != null)
	    days = val;
    }	
    public void setCodeNeed(String val){
	if(val != null)
	    codeNeed = val;
    }
    public void setCodeTask(String val){
	if(val != null)
	    codeTask = val;
    }
    public void setMarketTask(String val){
	if(val != null)
	    marketTask = val;
    }
    public void setVolTask(String val){
	if(val != null)
	    volTask = val;
    }
    public void setSponTask(String val){
	if(val != null)
	    sponTask = val;
    }
    public void setAgeFrom(String val){
	if(val != null)
	    ageFrom = val;
    }
    public void setAgeTo(String val){
	if(val != null)
	    ageTo = val;
    }
    public void setBudgetTask(String val){
	if(val != null)
	    budgetTask = val;
    }
    public void setEvalTask(String val){
	if(val != null)
	    evalTask = val;
    }
    public void setWParent(){
	wParent = "y";
    }
    public void forPublishOnly(){
	forPublishOnly = true;
    }		
    public void setAge(String val){
	if(val != null)
	    age = val;
    }		
    public void setPlan_id(String val){
	if(val != null)
	    plan_id = val;
    }
    public void setSubcat(String val){
	if(val != null)
	    subcat = val;
    }
    public void setWhichDate(String val){
	if(val != null)
	    whichDate = val;
    }
    public void setDateFrom(String val){
	if(val != null)
	    dateFrom = val;
    }
    public void setDateTo(String val){
	if(val != null)
	    dateTo = val;
    }
    public void setDateAt(String val){
	if(val != null)
	    dateAt = val;
    }
    public void setUnCode(String val){
	if(val != null)
	    unCode = val;
    }
    public void setUnMarket(String val){
	if(val != null)
	    unMarket = val;
    }
    public void setUnVol(String val){
	if(val != null)
	    unVol = val;
    }
    public void setUnBudget(String val){
	if(val != null)
	    unBudget = val;
    }
    public void setUnSpon(String val){
	if(val != null)
	    unSpon = val;
    }
    public void setUnEval(String val){
	if(val != null)
	    unEval = val;
    }
    public void setSortby(String val){
	if(val != null)
	    sortby = val;
    }
    public void setHasMarket(){
	hasMarket = true;
    }
    public void setHasSponsor(){
	hasSponsor = true;
    }
    public void setHasCode(){
	hasCode = true;
    }
    public void setHasShifts(){
	hasShifts = true;
    }
    public void setLimit(String val){
	if(val != null && !val.equals(""))
	    limit = " limit "+val;
    }		
    public void setNoLimit(){
	limit = "";
    }
    //
    public List<Program> getPrograms(){
	if(sortby.indexOf("title") > -1){
	    Collections.sort(this);
	}
	return this;
    }
    /**
       select id,0 from taxonomies where name like '%basketball%' union select 0,id from taxonomy_subs where name like '%basketball%' ;
       --> 0,301
       select id,0 from taxonomies where name like '%camp%' union select 0,id from taxonomy_subs where name like '%camp%' ;
       -> 3,0

       select id,taxonomy_ids from programs_programs where taxonomy_ids like '4%';
				
    */
    public String findTaxoIds(){
	String back="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = " select id,0 from taxonomies where name like ? "+
	    " union "+
	    " select 0,id from taxonomy_subs where name like ? ";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);			
	    pstmt.setString(1, "%"+taxonomy_term+"%");
	    pstmt.setString(2, "%"+taxonomy_term+"%");								
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		int id1 = rs.getInt(1);
		int id2 = rs.getInt(2);
		if(id1 > 0){
		    taxonomy_id = ""+id1;
		}
		if(id2 > 0){
		    taxonomy_ids = ""+id2;
		}								
	    }
	}catch(Exception ex){
	    back += ex;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
    public String find(){
	String message = "", back="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String related_season="", next_season="";
	int related_year = Helper.getCurrentYear(),
	    next_season_year=Helper.getCurrentYear();
	if(!effective_date.equals("")){
	    related_season = Helper.getRelatedSeason(effective_date);
	    next_season =  Helper.getNextSeason(effective_date);
	    related_year = Helper.getDatePart(effective_date,"year");
	    next_season_year = Helper.getNextSeasonYear(effective_date);
	}
	String qq = "select p.id,p.lead_id,l.name,p.statement,"+ 
	    " p.title,p.year,p.season,"+ // 7
	    " p.category_id,c.name,p.nraccount,p.fee,p.oginfo, "+ 
	    " p.inCityFee,p.nonCityFee,p.otherFee,"+  
	    " p.allAge,p.partGrade,p.minMaxEnroll,"+   
	    " date_format(p.regDeadLine,'%m/%d/%Y'),"+ 
	    " p.location_id,lc.name, p.instructor,p.description, "+ 
	    " p.days,p.startTime,p.endTime,p.classCount, "+
	    " p.code,p.area_id,a.name,p.codeNeed,"+
	    " date_format(p.startDate,'%m/%d/%Y'),"+
	    " date_format(p.endDate,'%m/%d/%Y'),p.codeTask,"+ 
	    " p.marketTask,p.volTask,p.sponTask,p.ageFrom,p.ageTo, "+ 
	    " p.budgetTask,p.evalTask, "+ 
	    " p.wParent,p.waitList,p.plan_id,p.oplocation,p.category2_id,"+
	    " p.version, "+ 
	    " p.otherAge, "+ 
	    " p.subcat, "+  
	    " date_format(p.received,'%m/%d/%Y'), "+ 
	    " p.memberFee,p.nonMemberFee,p.season2, "+
	    " p.summary,p.taxonomy_ids,p.location_details,p.noPublish "+
	    " from programs p "+
	    " left join leads l on p.lead_id = l.id "+
	    " left join categories c on p.category_id = c.id "+
	    " left join areas a on p.area_id = a.id "+
	    " left join locations lc on p.location_id = lc.id "+
	    " left join sessions s on p.id = s.id ";
	if(hasMarket){
	    qq += " join marketing_programs m on m.prog_id=p.id ";
	}
	if(hasSponsor){
	    qq += " join sponsors sp on sp.pid=p.id ";
	}
	if(hasShifts){
	    qq += " join vol_shifts vs on vs.pid=p.id ";
	}
	if(!effective_date.equals("")){
	    qq += " join web_publish_programs wp on wp.prog_id=p.id ";
	}
	if(!facility_term.equals("")){
	    qq += " , facilities f ";
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}
	String qw = "";
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.id=?";
	    // prevent call by ID unless it is authorized for publish
	    if(forServicePublishOnly){ // 
		if(!qw.equals("")) qw += " and ";
		qw += "p.noPublish is not null ";
	    }
	}
	if(!plan_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.plan_id=?";
	}
	if(!facility_term.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "lc.facility_id=f.id and f.name like ?";

	}
	if(!taxonomy_ids.equals("")){  // p.taxonomy_ids like ? 
	    if(!qw.equals("")) qw += " and ";
	    qw = "p.taxonomy_ids like ? ";
	    // }
	}
	else if(!taxonomy_id.equals("")){  // p.taxonomy_ids like ? 
	    if(!qw.equals("")) qw += " and ";
	    qw = "p.taxonomy_ids like ? ";
	}				
				
	if(!title.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.title like ?";
	}
	if(!lead_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.lead_id = ?";
	}
	if(!statement.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.statement like ?";
	}
	if(!year.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.year = ?";
	}
	if(!days.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.days like ? or s.days like ?)";
	}
	if(!season.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.season = ? or p.season2=?)";
	}
	if(!category_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.category_id = ?";
	}
	if(!nraccount.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.nraccount = ?";
	}
	if(!fee.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.fee = ?";
	}
	if(!inCityFee.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.inCityFee = ?";
	}
	if(!nonCityFee.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.nonCityFee = ?";
	    qw += " or s.nonCityFee = ?)";			
	}
	if(!partGrade.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.partGrade = ?";
	    qw += " or s.partGrade = ?)";
	}
	if(!classCount.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.classCount = ?";
	    qw += " or ";
	    qw += "s.classCount = ?)";
	}
	if(!code.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.code like ?";
	    qw += " or ";
	    qw += "s.code like ?)";
	}
	if(!instructor.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.instructor like ?";
	    qw += " or ";
	    qw += "s.instructor like ?)";
	}
	if(!location_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.location_id = ?";
	    qw += " or ";	
	    qw += "s.location = ?)";
	}
	if(!area_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.area_id like ?";
	}
	if(!marketTask.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.marketTask is not null ";
	}
	if(!volTask.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.volTask is not null ";
	}
	if(!sponTask.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.sponTask is not null ";
	}
	if(!evalTask.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.evalTask is not null ";
	}
	if(!ageFrom.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.ageFrom >= ?";
	    qw += " or ";	
	    qw += "s.ageFrom >= ?)";
	}
	if(!ageTo.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.ageTo <= ?";
	    qw += " or ";	
	    qw += "s.ageTo <= ?)";
	}
	if(!age.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "((p.ageFrom <= ? and ";
	    qw += " p.ageTo >= ?) or ";						
	    qw += "(s.ageFrom <= ? and ";
	    qw += "s.ageTo >= ?))";
	}				
	if(!wParent.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.wParent is not null ";
	    qw += " or ";	
	    qw += "s.wParent is not null) ";
	}
	if(!subcat.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.subcat like ?";
	    qw += " or ";	
	    qw += "pp.subcat like ?)";
	}
	if(!effective_date.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " ((p.season = '"+related_season+"' and p.year = "+related_year+") or (p.season='Ongoing' and p.year="+related_year+") or "+
		" (p.season = '"+next_season+"' and p.year = "+next_season_year+") or (p.season='Ongoing' and p.year="+next_season_year+")) ";						
						
	    qw += " and (p.regDeadLine >= str_to_date('"+effective_date+"','%m/%d/%Y') "+
		" or (p.regDeadLine is null and p.endDate >= str_to_date('"+effective_date+"','%m/%d/%Y')) "+
		" or (p.regDeadLine is null and p.endDate is null))";
	}
	if(!whichDate.equals("")){
	    if(!dateAt.equals("")){
		if(!qw.equals("")) qw += " and ";				
		qw += "(p."+whichDate+" = ?";
		qw += " or ";	
		qw += "s."+whichDate+" = ?)";
	    }
	    else{
		if(!dateFrom.equals("")){
		    if(!qw.equals("")) qw += " and ";				
		    qw += "(p."+whichDate+" > ?";
		    qw += " or ";	
		    qw += "s."+whichDate+" > ?)";
		}
		if(!dateTo.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "(p."+whichDate+" < ?";
		    qw += " or ";	
		    qw += "s."+whichDate+" < ?)";
		}
	    }
	}
	if(!unCode.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.codeTask is null ";
	}
	if(!unMarket.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.marketTask is null ";
	}
	if(!unVol.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.volTask is null ";
	}
	if(!unSpon.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.sponTask is null ";
	}
	if(!unBudget.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.budgetTask is null ";
	}
	if(!unEval.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.evalTask is null ";
	}
	if(hasCode){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.codeNeed is not null and (p.code is not null or s.code is not null))";
	}
	else if(!codeNeed.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.codeNeed is not null and (p.code is null or s.code is null))";
	}
	if(forPublishOnly){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.noPublish is null ";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	String added = "a.name,c.name"; // area, category
	if(sortby.indexOf("cat") > -1) sortby = added+",p.title";

	if(!sortby.equals("")){
	    qq += " order by "+sortby;
	}
	qq += limit;
	// System.err.println("qq: "+qq);
	if(debug){
	    logger.debug(qq);
	}
	// System.err.println(qq);
	try{
	    pstmt = con.prepareStatement(qq);			
	    int jj = 1;
	    if(!id.equals("")){
		pstmt.setString(jj++, id);
	    }
	    if(!plan_id.equals("")){
		pstmt.setString(jj++, plan_id);
	    }
	    if(!facility_term.equals("")){
		pstmt.setString(jj++, "%"+facility_term+"%");
	    }
	    if(!taxonomy_ids.equals("")){
		pstmt.setString(jj++, taxonomy_ids+"%");
	    }						
	    else if(!taxonomy_id.equals("")){
		pstmt.setString(jj++, taxonomy_id+"%");
	    }						
	    if(!title.equals("")){
		pstmt.setString(jj++, title);
	    }
	    if(!lead_id.equals("")){
		pstmt.setString(jj++, lead_id);
	    }
	    if(!statement.equals("")){
		pstmt.setString(jj++, statement);
	    }
	    if(!year.equals("")){
		pstmt.setString(jj++, year);
	    }
	    if(!days.equals("")){
		pstmt.setString(jj++,days);
		pstmt.setString(jj++,days);				
	    }			
	    if(!season.equals("")){
		pstmt.setString(jj++,season);
		pstmt.setString(jj++,season);				
	    }
	    if(!category_id.equals("")){
		pstmt.setString(jj++,category_id);
	    }
	    if(!nraccount.equals("")){
		pstmt.setString(jj++,nraccount);
	    }
	    if(!fee.equals("")){
		pstmt.setString(jj++,fee);
	    }
	    if(!inCityFee.equals("")){
		pstmt.setString(jj++,inCityFee);
	    }
	    if(!nonCityFee.equals("")){
		pstmt.setString(jj++,nonCityFee);
		pstmt.setString(jj++,nonCityFee);								
	    }
	    if(!partGrade.equals("")){
		pstmt.setString(jj++,partGrade);
		pstmt.setString(jj++,partGrade);				
	    }
	    if(!classCount.equals("")){
		pstmt.setString(jj++,classCount);
		pstmt.setString(jj++,classCount);
	    }
	    if(!code.equals("")){
		pstmt.setString(jj++,code);
		pstmt.setString(jj++,code);
	    }
	    if(!instructor.equals("")){
		pstmt.setString(jj++,"%"+instructor+"%");
		pstmt.setString(jj++,"%"+instructor+"%");
	    }
	    if(!location_id.equals("")){
		pstmt.setString(jj++,location_id);
		pstmt.setString(jj++,location_id);
	    }
	    if(!area_id.equals("")){
		pstmt.setString(jj++,area_id);
	    }

	    if(!ageFrom.equals("")){
		pstmt.setString(jj++,ageFrom);
		pstmt.setString(jj++,ageFrom);
	    }
	    if(!ageTo.equals("")){
		pstmt.setString(jj++,ageTo);
		pstmt.setString(jj++,ageTo);
	    }
	    if(!age.equals("")){
		pstmt.setString(jj++,age);
		pstmt.setString(jj++,age);
		pstmt.setString(jj++,age);
		pstmt.setString(jj++,age);								
	    }						
	    if(!subcat.equals("")){
		pstmt.setString(jj++,subcat);
		pstmt.setString(jj++,subcat);
	    }
	    if(!whichDate.equals("")){
		if(!dateAt.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateAt).getTime()));
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateAt).getTime()));		
		}
		else{
		    if(!dateFrom.equals("")){
			pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateFrom).getTime()));
			pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateFrom).getTime()));
		    }
		    if(!dateTo.equals("")){
			pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateTo).getTime()));
			pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateTo).getTime()));
		    }
		}
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Program one = new Program(debug,
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
					  rs.getString(16),
					  rs.getString(17),
					  rs.getString(18),
					  rs.getString(19),
					  rs.getString(20),
					  rs.getString(21),
					  rs.getString(22),
					  rs.getString(23),
					  rs.getString(24),
					  rs.getString(25),
					  rs.getString(26),
					  rs.getString(27),
					  rs.getString(28),
					  rs.getString(29),
					  rs.getString(30),
					  rs.getString(31),
					  rs.getString(32),
					  rs.getString(33),
					  rs.getString(34),
					  rs.getString(35),
					  rs.getString(36),
					  rs.getString(37),
					  rs.getString(38),
					  rs.getString(39),
					  rs.getString(40),
					  rs.getString(41),
					  rs.getString(42),
					  rs.getString(43),
					  rs.getString(44),
					  rs.getString(45),
					  rs.getString(46),
					  rs.getString(47),
					  rs.getString(48),
					  rs.getString(49),
					  rs.getString(50),
					  rs.getString(51),
					  rs.getString(52),
					  rs.getString(53),
					  rs.getString(54),
					  rs.getString(55),
					  rs.getString(56),
					  rs.getString(57)
					  );
		if(!this.contains(one))
		    this.add(one);
	    }
	    //
	    // we need this without sessions
	    //
	    qq = "select p.id,p.lead_id,l.name,p.statement,"+ 
		" p.title,p.year,p.season,"+ // 7
		" p.category_id,c.name,p.nraccount,p.fee,p.oginfo, "+ 
		" p.inCityFee,p.nonCityFee,p.otherFee,"+  
		" p.allAge,p.partGrade,p.minMaxEnroll,"+   
		" date_format(p.regDeadLine,'%m/%d/%Y'),"+ 
		" p.location_id,lc.name, p.instructor,p.description, "+ 
		" p.days,p.startTime,p.endTime,p.classCount, "+
		" p.code,p.area_id,a.name,p.codeNeed,"+
		" date_format(p.startDate,'%m/%d/%Y'),"+
		" date_format(p.endDate,'%m/%d/%Y'),p.codeTask,"+ 
		" p.marketTask,p.volTask,p.sponTask,p.ageFrom,p.ageTo, "+ 
		" p.budgetTask,p.evalTask, "+ 
		" p.wParent,p.waitList,p.plan_id,p.oplocation,p.category2_id,"+
		" p.version, "+ 
		" p.otherAge, "+ 
		" p.subcat, "+  
		" date_format(p.received,'%m/%d/%Y'), "+ 
		" p.memberFee,p.nonMemberFee,p.season2, "+
		" p.summary,p.taxonomy_ids,p.location_details,p.noPublish "+
		" from programs p "+
		" left join leads l on p.lead_id = l.id "+
		" left join categories c on p.category_id = c.id "+
		" left join areas a on p.area_id = a.id "+
		" left join locations lc on p.location_id = lc.id ";
	    if(hasMarket){
		qq += " join marketing m on m.id=p.id ";
	    }
	    if(hasSponsor){
		qq += " join sponsors sp on sp.pid=p.id ";
	    }
	    if(hasShifts){
		qq += " join vol_shifts vs on vs.pid=p.id ";
	    }
	    if(!effective_date.equals("")){
		qq += " join web_publish_programs wp on wp.prog_id=p.id ";
	    }						
	    if(!facility_term.equals("")){
		qq += " , facilities f ";
	    }						
	    qw = "";
	    if(!id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.id=?";
		if(forServicePublishOnly){
		    if(!qw.equals("")) qw += " and ";
		    qw += "p.noPublish is null ";
		}
	    }
	    if(!plan_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.plan_id=?";
	    }
	    if(!facility_term.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "lc.facility_id=f.id and f.name like ?";
								
	    }
	    if(!taxonomy_ids.equals("")){  // p.taxonomy_ids like ? 
		if(!qw.equals("")) qw += " and ";

		qw = "p.taxonomy_ids like ? ";
	    }
	    else if(!taxonomy_id.equals("")){  // p.taxonomy_ids like ? 
		if(!qw.equals("")) qw += " and ";
		qw = "p.taxonomy_ids like ? ";
	    }												
	    if(!title.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.title like ?";
	    }
	    if(!lead_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.lead_id = ?";
	    }
	    if(!statement.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.statement like ?";
	    }
	    if(!year.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.year = ?";
	    }
	    if(!days.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.days like ?";
	    }
	    if(!season.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.season = ? or p.season2=?)";
	    }
	    if(!category_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.category_id=? ";
	    }

	    if(!nraccount.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.nraccount = ?";
	    }
	    if(!fee.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.fee = ?";
	    }
	    if(!inCityFee.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.inCityFee = ?";
	    }
	    if(!nonCityFee.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.nonCityFee = ?";
	    }
	    if(!partGrade.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.partGrade = ?";
	    }
	    if(!classCount.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.classCount = ?";
	    }
	    if(!code.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.code like ?";
	    }
	    if(!instructor.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.instructor like ?";
	    }
	    if(!location_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.location_id = ?";
	    }
	    if(!area_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.area_id like ?";
	    }
	    if(!marketTask.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.marketTask is not null ";
	    }
	    if(!volTask.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.volTask is not null ";
	    }
	    if(!sponTask.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.sponTask is not null ";
	    }
	    if(!evalTask.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.evalTask is not null ";
	    }
	    if(!ageFrom.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.ageFrom >= ?";
	    }
	    if(!ageTo.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.ageTo <= ?";
	    }
	    if(!age.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.ageFrom <= ? and ";
		qw += "p.ageTo >= ?";
	    }										
	    if(!wParent.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.wParent is not null ";
	    }
	    if(!subcat.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.subcat like ?";
		qw += " or ";	
		qw += "pp.subcat like ?)";
	    }
	    if(!whichDate.equals("")){
		if(!dateAt.equals("")){
		    if(!qw.equals("")) qw += " and ";				
		    qw += "p."+whichDate+" = ?";
		}
		else{
		    if(!dateFrom.equals("")){
			if(!qw.equals("")) qw += " and ";				
			qw += "p."+whichDate+" > ?";
		    }
		    if(!dateTo.equals("")){
			if(!qw.equals("")) qw += " and ";
			qw += "p."+whichDate+" < ?";
		    }
		}
	    }
	    if(!unCode.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.codeTask is null ";
	    }
	    if(!unMarket.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.marketTask is null ";
	    }
	    if(!unVol.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.volTask is null ";
	    }
	    if(!unSpon.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.sponTask is null ";
	    }
	    if(!unBudget.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.budgetTask is null ";
	    }
	    if(!unEval.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.evalTask is null ";
	    }
	    if(hasCode){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.codeNeed is not null and p.code is not null)";
	    }
	    else if(!codeNeed.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.codeNeed is not null and p.code is null)";
	    }
	    if(!effective_date.equals("")){
		if(!qw.equals("")) qw += " and ";
								
		qw += " ((p.season = '"+related_season+"' and p.year = "+related_year+") or (p.season='Ongoing' and p.year="+related_year+") or ";
		qw += " (p.season = '"+next_season+"' and p.year = "+next_season_year+") or (p.season='Ongoing' and p.year="+next_season_year+")) ";						
		qw += " and (p.regDeadLine >= str_to_date('"+effective_date+"','%m/%d/%Y') "+
		    " or (p.regDeadLine is null and p.endDate >= str_to_date('"+effective_date+"','%m/%d/%Y')) "+
		    " or (p.regDeadLine is null and p.endDate is null))";
	    }
	    if(forPublishOnly){
		if(!qw.equals("")) qw += " and ";
		qw += " p.noPublish is null ";
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    added = "a.name,c.name";
	    if(sortby.indexOf("cat") > -1) sortby = added+",p.title";
			
	    if(!sortby.equals("")){
		qq += " order by "+sortby;
	    }
	    // System.err.println(qq);
	    qq += limit;
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);			
	    jj = 1;
	    if(!id.equals("")){
		pstmt.setString(jj++,id);
	    }
	    if(!plan_id.equals("")){
		pstmt.setString(jj++,plan_id);
	    }
	    if(!facility_term.equals("")){
		pstmt.setString(jj++, "%"+facility_term+"%");
	    }
	    if(!taxonomy_ids.equals("")){
		pstmt.setString(jj++, taxonomy_ids+"%");
		// }
	    }
	    else if(!taxonomy_id.equals("")){
		pstmt.setString(jj++, taxonomy_id+"%");
	    }										
	    if(!title.equals("")){
		pstmt.setString(jj++,title);
	    }
	    if(!lead_id.equals("")){
		pstmt.setString(jj++,lead_id);
	    }
	    if(!statement.equals("")){
		pstmt.setString(jj++,statement);
	    }
	    if(!year.equals("")){
		pstmt.setString(jj++,year);
	    }
	    if(!days.equals("")){
		pstmt.setString(jj++,days);
	    }
	    if(!season.equals("")){
		pstmt.setString(jj++,season);
		pstmt.setString(jj++,season);				
	    }
	    if(!category_id.equals("")){
		pstmt.setString(jj++,category_id);
	    }
	    if(!nraccount.equals("")){
		pstmt.setString(jj++,nraccount);
	    }
	    if(!fee.equals("")){
		pstmt.setString(jj++,fee);
	    }
	    if(!inCityFee.equals("")){
		pstmt.setString(jj++,inCityFee);
	    }
	    if(!nonCityFee.equals("")){
		pstmt.setString(jj++,nonCityFee);
	    }
	    if(!partGrade.equals("")){
		pstmt.setString(jj++,partGrade);
	    }
	    if(!classCount.equals("")){
		pstmt.setString(jj++,classCount);
	    }
	    if(!code.equals("")){
		pstmt.setString(jj++,code);
	    }
	    if(!instructor.equals("")){
		pstmt.setString(jj++,"%"+instructor+"%");
	    }
	    if(!location_id.equals("")){
		pstmt.setString(jj++,location_id);
	    }
	    if(!area_id.equals("")){
		pstmt.setString(jj++,area_id);
	    }
	    //
	    if(!ageFrom.equals("")){
		pstmt.setString(jj++,ageFrom);
	    }
	    if(!ageTo.equals("")){
		pstmt.setString(jj++,ageTo);
	    }
	    if(!age.equals("")){
		pstmt.setString(jj++,age);
		pstmt.setString(jj++,age);
	    }						
	    if(!subcat.equals("")){
		pstmt.setString(jj++,subcat);
		pstmt.setString(jj++,subcat);								
	    }
	    if(!whichDate.equals("")){
		if(!dateAt.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateAt).getTime()));		
		}
		else{
		    if(!dateFrom.equals("")){
			pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateFrom).getTime()));
		    }
		    if(!dateTo.equals("")){
			pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(dateTo).getTime()));
		    }
		}
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Program one = new Program(debug,
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
					  rs.getString(16),
					  rs.getString(17),
					  rs.getString(18),
					  rs.getString(19),
					  rs.getString(20),
					  rs.getString(21),
					  rs.getString(22),
					  rs.getString(23),
					  rs.getString(24),
					  rs.getString(25),
					  rs.getString(26),
					  rs.getString(27),
					  rs.getString(28),
					  rs.getString(29),
					  rs.getString(30),
					  rs.getString(31),
					  rs.getString(32),
					  rs.getString(33),
					  rs.getString(34),
					  rs.getString(35),
					  rs.getString(36),
					  rs.getString(37),
					  rs.getString(38),
					  rs.getString(39),
					  rs.getString(40),
					  rs.getString(41),
					  rs.getString(42),
					  rs.getString(43),
					  rs.getString(44),
					  rs.getString(45),
					  rs.getString(46),
					  rs.getString(47),
					  rs.getString(48),
					  rs.getString(49),
					  rs.getString(50),
					  rs.getString(51),
					  rs.getString(52),
					  rs.getString(53),
					  rs.getString(54),
					  rs.getString(55),
					  rs.getString(56),
					  rs.getString(57)
					  );
		if(!this.contains(one))
		    this.add(one);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    message += " Error retrieving data " +ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;		
    }
    /**
     * we need this to get the list of program names and their id using
     * season and year
     */
    public String findAbraviatedList(){
	String message = "", qq="", back="",qw="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	con = Helper.getConnection();
		
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}		
	//
	// we select the field for sort purpose only here
	//
	qq = " select p.id,p.title,p.code,p.season,p.year from programs p ";
	qq += " join sessions s on s.id = p.id ";
	if(hasMarket){
	    qq += " join marketing m on m.id=p.id ";
	}
	if(hasSponsor){
	    qq += " join sponsors sp on sp.pid=p.id ";
	}
	if(hasShifts){
	    qq += " join vol_shifts vs on vs.pid=p.id ";
	}
	if(!codeNeed.equals("") || hasCode){

	}
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.id = ?";
	}		
	if(!year.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.year = ?";
	}
	if(!season.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.season = ? or p.season2=?)";
	}
	if(!lead_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.lead_id = ?";
	}						
	if(!category_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.category_id = ? or category2_id=?) ";
	}
	if(!area_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.area_id = ?";
	}
	if(!nraccount.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "p.nraccount like ?";
	}
	if(!location_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.location_id = ? or s.location = ?)";
	}		
	if(hasCode){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.codeNeed is not null and (p.code is not null or s.code is not null))";
	}
	else if(!codeNeed.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += "(p.codeNeed is not null and (p.code is null or s.code is null))";
	}
	if(!code.equals("")){
	    qw += "p.code is not null";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	if(!sortby.equals("")){
	    qq += " order by "+sortby;
	}
	else{
	    if(hasCode || !codeNeed.equals(""))
		qq += " order by s.code,p.code ";
	    else
		qq += " order by p.title ";
	}
	if(!limit.equals("")){
	    qq += limit;
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);			
	    int jj = 1;
	    if(!id.equals("")){
		pstmt.setString(jj++,id);
	    }			
	    if(!year.equals("")){
		pstmt.setString(jj++,year);
	    }
	    if(!season.equals("")){
		pstmt.setString(jj++,season);
		pstmt.setString(jj++,season);				
	    }
	    if(!lead_id.equals("")){
		pstmt.setString(jj++,lead_id);
	    }
	    if(!category_id.equals("")){
		pstmt.setString(jj++,category_id);
		pstmt.setString(jj++,category_id);
	    }			
	    if(!area_id.equals("")){
		pstmt.setString(jj++,area_id);
	    }
	    if(!nraccount.equals("")){
		pstmt.setString(jj++,"%"+nraccount+"%");
	    }
	    if(!location_id.equals("")){
		pstmt.setString(jj++,location_id);
		pstmt.setString(jj++,location_id);				
	    }			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		String str2 = rs.getString(2);
		String str3 = rs.getString(3);
		String str4 = rs.getString(4);
		String str5 = rs.getString(5);
		if(str != null){
		    Program pr = new Program(debug, str, str2, str3, str4, str5);
		    if(!this.contains(pr)) // no dups
			add(pr);
		}
	    }
	    //
	    // we do union without sessions
	    //
	    qq = " select p.id,p.title,p.code,p.season,p.year from programs p ";
	    if(hasMarket){
		qq += " join marketing m on m.id=p.id ";
	    }
	    if(hasSponsor){
		qq += " join sponsors sp on sp.pid=p.id ";
	    }
	    if(hasShifts){
		qq += " join vol_shifts vs on vs.pid=p.id ";
	    }
	    qw = "";
	    if(!id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.id = ?";
	    }		
	    if(!year.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.year = ?";
	    }
	    if(!season.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.season = ? or p.season2=?)";
	    }
	    if(!lead_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.lead_id = ?";
	    }						
	    if(!category_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.category_id = ? or category2_id=?) ";
	    }
	    if(!area_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.area_id = ?";
	    }
	    if(!nraccount.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.nraccount like ?";
	    }
	    if(!location_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.location_id = ?";
	    }			
	    if(hasCode){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.codeNeed is not null and p.code is not null)";
	    }
	    else if(!codeNeed.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.codeNeed is not null and p.code is null)";
	    }
			
	    if(!code.equals("")){
		qw += "p.code is not null";
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    if(sortby.equals("")){
		if(hasCode || !codeNeed.equals("")){
		    qq += " order by p.code ";
		}
		else{
		    qq += " order by p.title ";
		}
	    }
	    else{
		if(sortby.indexOf("s.code") > -1){
		    qq += " order by p.code ";
		}
		else{
		    qq += " order by "+sortby;
		}
	    }
	    //}
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);			
	    jj = 1;
	    if(!id.equals("")){
		pstmt.setString(jj++,id);
	    }			
	    if(!year.equals("")){
		pstmt.setString(jj++,year);
	    }
	    if(!season.equals("")){
		pstmt.setString(jj++,season);
		pstmt.setString(jj++,season);				
	    }
	    if(!lead_id.equals("")){
		pstmt.setString(jj++,lead_id);
	    }
	    if(!category_id.equals("")){
		pstmt.setString(jj++,category_id);
		pstmt.setString(jj++,category_id);
	    }			
	    if(!area_id.equals("")){
		pstmt.setString(jj++,area_id);
	    }
	    if(!nraccount.equals("")){
		pstmt.setString(jj++,"%"+nraccount+"%");
	    }
	    if(!location_id.equals("")){
		pstmt.setString(jj++,location_id);
	    }			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		String str2 = rs.getString(2);
		String str3 = rs.getString(3);
		String str4 = rs.getString(4);
		String str5 = rs.getString(5);				
		if(str != null){
		    Program pr = new Program(debug, str, str2, str3, str4, str5);
		    if(!this.contains(pr)) // no dups
			add(pr);
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    message += " Error retrieving data " +ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
    /**
     * we need this to get the list of program names and their id using
     * season and year
     */
    public String findListForPublish(){
	String message = "", qq="", back="",qw="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	con = Helper.getConnection();
		
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}		
	try{
	    qq = " select p.id,p.title,p.category_id,c.name,p.area_id,a.name,p.lead_id,l.name from programs p left join categories c on c.id=p.category_id left join areas a on a.id=p.area_id left join leads l on l.id=p.lead_id ";
	    if(approvedPublish || !publish_id.equals("")){
		qq += ", web_publish_programs wp ";
		if(!qw.equals("")) qw += " and ";
		qw += " p.id = wp.prog_id ";
		if(!publish_id.equals("")){
		    qw += " and wp.publish_id = ? ";
		}
	    }
	    else if(readyToPublish){
		if(!qw.equals("")) qw += " and ";
		qw += " p.id not in (select prog_id from web_publish_programs) ";
	    }
	    if(!id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.id = ?";
	    }		
	    if(!year.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.year = ?";
	    }
	    if(!season.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.season = ? or p.season2=?)";
	    }
	    if(!lead_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.lead_id = ?";
	    }						
	    if(!category_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.category_id = ? or category2_id=?) ";
	    }
	    if(!area_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.area_id = ?";
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    if(sortby.equals("")){
		qq += " order by p.title ";
	    }
	    else{
		qq += " order by "+sortby;
	    }
						
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);			
	    int jj = 1;
	    if(!publish_id.equals("")){
		pstmt.setString(jj++, publish_id);
	    }
	    if(!id.equals("")){
		pstmt.setString(jj++,id);
	    }			
	    if(!year.equals("")){
		pstmt.setString(jj++,year);
	    }
	    if(!season.equals("")){
		pstmt.setString(jj++,season);
		pstmt.setString(jj++,season);				
	    }
	    if(!lead_id.equals("")){
		pstmt.setString(jj++,lead_id);
	    }
	    if(!category_id.equals("")){
		pstmt.setString(jj++,category_id);
		pstmt.setString(jj++,category_id);
	    }			
	    if(!area_id.equals("")){
		pstmt.setString(jj++,area_id);
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Program pr = new Program(debug,
					 rs.getString(1),
					 rs.getString(2),
					 rs.getString(3),
					 rs.getString(4),
					 rs.getString(5),
					 rs.getString(6),
					 rs.getString(7),
					 rs.getString(8));
		if(!this.contains(pr)) // no dups
		    add(pr);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    message += " Error retrieving data " +ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return message;
    }
				

}





































