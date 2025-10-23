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


public class MarketList extends ArrayList<Market>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(MarketList.class);
    //
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
    String id = "", date_from="", date_to="",
	year="", category_id="", prog_id="", facility_id="", general_id="",
	season="", market_ad="", market_announce="",
	market_type="", sortBy="m.id ";
    boolean excludeProgram = false, excludeFacility = false, excludeGeneral = false;
    String which_date="";
		
    List<String> errors = null;
    String message = "";
	
    public MarketList(boolean val){
	debug = val;
    }
    public MarketList(boolean deb, String val, String val2, String val3){
	debug = deb;
	setProg_id(val);
	setFacility_id(val2);
	setGeneral_id(val3);
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
    public void setCategory_id(String val){
	if(val != null)
	    category_id = val;
    }	
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setProg_id(String val){
	if(val != null){
	    prog_id = val;
	    excludeFacility = true;
	    excludeGeneral = true;
	}
    }
    public void setGeneral_id(String val){
	if(val != null){
	    general_id = val;
	    excludeFacility = true;
	    excludeProgram = true;
	}
    }	
    public void setFacility_id(String val){
	if(val != null){
	    facility_id = val;
	    excludeGeneral = true;
	    excludeProgram = true;
	}
    }
    public void setWhich_date(String val){
	if(val != null)
	    which_date = val;
    }	
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }
    public void setMarket_ad(String val){
	if(val != null)
	    market_ad = val;
    }
    public void setMarket_announce(String val){
	if(val != null)
	    market_announce = val;
    }
    public void setMarket_type(String val){
	if(val != null)
	    market_type = val;
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
	sortBy = "m.id desc "; // last first
	if(!excludeFacility)
	    back = findMarketForFacilities();
	if(!excludeGeneral)
	    back += findMarketForGenerals();
	if(!excludeProgram)
	    back += findMarketForPrograms();
	return back;
    }
    public String findLatestForFacility(){
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "select m.id, "+
	    " m.other_ad,m.class_list,m.other_market,m.spInstructions,"+
	    " m.sign_board, "+
	    " date_format(m.sign_board_date,'%m/%d/%Y'), "+ // 44
	    " null,mf.facility_id,null,mf.year,mf.season ";
	String qw = "", qf = "from marketing m ";		
	qf += " join marketing_facilities mf on m.id=mf.market_id ";
	qf += " left join market_ad_details ad on m.id = ad.market_id ";
	qf += " left join market_type_details t on m.id = t.market_id ";
	qw += " mf.facility_id=?";
	sortBy = " m.id DESC limit 1"; // last first
	if(!qw.equals("")){
	    qw = " where "+qw;
	}
	qq += qf+qw;
	if(!sortBy.equals("")){
	    qq += " order by "+sortBy;
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, facility_id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		Market one =
		    new Market(debug,
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
			       rs.getString(12)
			       );
		if(!this.contains(one)){
		    this.add(one);
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
	return back;
    }
    //
    public String findMarketForPrograms(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "select m.id, "+
	    " m.other_ad,m.class_list,m.other_market,m.spInstructions,"+
	    " m.sign_board,"+
	    " date_format(m.sign_board_date,'%m/%d/%Y'), "+
	    " mp.prog_id,null,null,null,null ";
	String qw = "", qf = "from marketing m ";		
	qf += " join marketing_programs mp on m.id=mp.market_id ";
	qf += " join programs p on p.id=mp.prog_id ";
	qf += " left join market_ad_details ad on m.id = ad.market_id ";
	qf += " left join market_type_details t on m.id = t.market_id ";		
	boolean progTbl = false, facilTbl = false;
	if(!id.equals("")){
	    qw += " m.id = ? ";
	}
	else {
	    // any date type
	    if(!date_from.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(ad.due_date >= ? or t.due_date >= ?)";
	    }
	    if(!date_to.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(ad.due_date <= ? or t.due_date <= ?)";					
	    }
	    if(!season.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.season = ? ";
	    }
	    if(!year.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.year = ? ";
	    }
	    if(!category_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.category_id = ? ";
	    }		
	    if(!market_ad.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " ad.type_id = ? "; // ad_id
	    }
	    if(!market_announce.equals("")){
		if(!qw.equals("")) qw += " and ";
		qf += ", market_announce_details an ";
		qw += " an.an_id = ? and m.id = an.market_id ";
	    }
	    if(!market_type.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " t.type_id=? ";
	    }
	    if(!prog_id.equals("")){
		qw += " p.id=?";
	    }
	}
	if(!qw.equals("")){
	    qw = " where "+qw;
	}
	qq += qf+qw;
	if(!sortBy.equals("")){
	    qq += " order by "+sortBy;
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
		if(!date_from.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_from).getTime()));					
		}
		if(!date_to.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_to).getTime()));
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_to).getTime()));					
		}
		if(!season.equals("")){
		    pstmt.setString(jj++, season);
		}
		if(!year.equals("")){
		    pstmt.setString(jj++, year);
		}
		if(!category_id.equals("")){
		    pstmt.setString(jj++, category_id);
		}			
		if(!market_ad.equals("")){
		    pstmt.setString(jj++, market_ad);
		}
		if(!market_announce.equals("")){
		    pstmt.setString(jj++, market_announce);
		}
		if(!market_type.equals("")){
		    pstmt.setString(jj++, market_type);
		}
		if(!prog_id.equals("")){
		    pstmt.setString(jj++, prog_id);
		}
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Market one =
		    new Market(debug,
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
			       rs.getString(12)
			       );
		if(!this.contains(one)){
		    this.add(one);
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
	return back;
    }
    public String findMarketForGenerals(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "select m.id, "+
	    " m.other_ad,m.class_list,m.other_market,m.spInstructions,"+
	    " m.sign_board, "+
	    " date_format(m.sign_board_date,'%m/%d/%Y'), "+ // 44
	    " null,null,mp.general_id,gl.year,gl.season ";
	String qw = "", qf = "from marketing m ";		
	qf += " join marketing_generals mp on m.id=mp.market_id ";
	qf += " join general_listings gl on gl.id=mp.general_id ";		
	qf += " left join market_ad_details ad on m.id = ad.market_id ";
	qf += " left join market_type_details t on m.id = t.market_id ";		
	boolean progTbl = false, facilTbl = false;
	if(!id.equals("")){
	    qw += " m.id = ? ";
	}
	else {
	    // any date type
	    if(!date_from.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(ad.due_date >= ? or t.due_date >= ?)";
	    }
	    if(!date_to.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(ad.due_date <= ? or t.due_date <= ?)";					
	    }
	    if(!season.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "( gl.season = ? or mp.season=?) ";
	    }
	    if(!year.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(gl.year = ? or mp.year=?)";
	    }
	    if(!market_ad.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " ad.type_id = ? "; // ad_id
	    }
	    if(!market_announce.equals("")){
		if(!qw.equals("")) qw += " and ";
		qf += ", market_announce_details an ";
		qw += " an.an_id = ? and m.id = an.market_id ";
	    }
	    if(!market_type.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " t.type_id=? ";
	    }
	    if(!general_id.equals("")){
		qw += " gl.id=?";
	    }
	}
	if(!qw.equals("")){
	    qw = " where "+qw;
	}
	qq += qf+qw;
	if(!sortBy.equals("")){
	    qq += " order by "+sortBy;
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
		if(!date_from.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_from).getTime()));
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_from).getTime()));					
		}
		if(!date_to.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_to).getTime()));
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_to).getTime()));					
		}
		if(!season.equals("")){
		    pstmt.setString(jj++, season);
		    pstmt.setString(jj++, season);
		}
		if(!year.equals("")){
		    pstmt.setString(jj++, year);
		    pstmt.setString(jj++, year);

		}
		if(!market_ad.equals("")){
		    pstmt.setString(jj++, market_ad);
		}
		if(!market_announce.equals("")){
		    pstmt.setString(jj++, market_announce);
		}
		if(!market_type.equals("")){
		    pstmt.setString(jj++, market_type);
		}
		if(!general_id.equals("")){
		    pstmt.setString(jj++, general_id);
		}
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Market one =
		    new Market(debug,
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
			       rs.getString(12)
			       );
		if(!this.contains(one)){
		    this.add(one);
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
	return back;
    }	
	
    public String findMarketForFacilities(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String date_from2 = date_from;
	String date_to2 = date_to;
	if(date_from.equals("") && date_to.equals("") &&
	   !season.equals("") && !year.equals("")){
	    String ret[] = Helper.getStartEndDateFromSeasonYear(season, year);
	    if(ret != null){
		date_from2 = ret[0];
		date_to2 = ret[1];
	    }
	}		
	String qq = "select m.id, "+
	    " m.other_ad,m.class_list,m.other_market,m.spInstructions,"+
	    " m.sign_board, "+
	    " date_format(m.sign_board_date,'%m/%d/%Y'), "+ // 44
	    " null,mf.facility_id,null,mf.year,mf.season ";
	String qw = "", qf = "from marketing m ";		
	qf += " join marketing_facilities mf on m.id=mf.market_id ";
	qf += " left join market_ad_details ad on m.id = ad.market_id ";
	qf += " left join market_type_details t on m.id = t.market_id ";		
	boolean progTbl = false, facilTbl = false;
	if(!id.equals("")){
	    qw += " m.id = ? ";
	}
	else {
	    // any date type
	    if(!date_from2.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(ad.due_date >= ? or t.due_date >= ?)";
	    }
	    if(!date_to2.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(ad.due_date <= ? or t.due_date <= ?)";					
	    }
	    if(!season.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "mf.season = ? ";					
	    }
	    if(!year.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "mf.year = ? ";					
	    }			
	    if(!market_ad.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " ad.type_id = ? ";
	    }
	    if(!market_announce.equals("")){
		if(!qw.equals("")) qw += " and ";
		qf += ", market_announce_details an ";
		qw += " an.an_id = ? and m.id = an.market_id ";
	    }
	    if(!market_type.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " t.type_id=? ";
	    }
	    if(!facility_id.equals("")){
		qw += " mf.facility_id=?";
		sortBy = " m.id DESC "; // last first
	    }
	}
	if(!qw.equals("")){
	    qw = " where "+qw;
	}
	qq += qf+qw;
	if(!sortBy.equals("")){
	    qq += " order by "+sortBy;
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.equals("")){
		pstmt.setString(jj++, id);
	    }
	    else{
		if(!date_from2.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_from2).getTime()));
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_from2).getTime()));					
		}
		if(!date_to2.equals("")){
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_to2).getTime()));
		    pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(date_to2).getTime()));					
		}
		if(!season.equals("")){
		    pstmt.setString(jj++, season);
		}
		if(!year.equals("")){
		    pstmt.setString(jj++, year);
		}
		if(!market_ad.equals("")){
		    pstmt.setString(jj++, market_ad);
		}
		if(!market_announce.equals("")){
		    pstmt.setString(jj++, market_announce);
		}
		if(!market_type.equals("")){
		    pstmt.setString(jj++, market_type);
		}
		if(!facility_id.equals("")){
		    pstmt.setString(jj++, facility_id);
		}				
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		Market one =
		    new Market(debug,
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
			       rs.getString(12)
			       );
		if(!this.contains(one)){
		    this.add(one);
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
	return back;
    }	
}






















































