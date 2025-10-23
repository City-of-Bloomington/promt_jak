package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;
/**
 *
 */

public class Market extends CommonInc{

    static Logger logger = LogManager.getLogger(Market.class);
    String id="",
    // program,  facility or genearal
	prog_id="",facility_id="",general_id="",
	year = "", season="", // for facility
	other_ad="",class_list="",other_market="",
	sign_board="", sign_board_date="",
	spInstructions="";
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	
    List<MarketAd> ads = null;
    List<Type> announces = null;
    List<MarketItem> items = null;
    MarketAd[] addAds = null;
    String[] addAnnounces = null;
    String[] delAds = null;
    String[] delAnnounces = null;
    MarketItem[] addItems = null;
    String[] delItems = null;
    double totalIndirect = 0,totalDirect = 0;
    Program prog = null;
    HistoryList history = null;
    Facility facility = null;
    General general = null;
    boolean selectionDone = false;
    List<PromtFile> files = null;
		
    //
    public Market(boolean deb){
	//
	// initialize
	//
	super(deb);
    }	
    public Market(boolean deb, String val){
	//
	// initialize
	//
	super(deb);
	setId(val);
    }
    public Market(boolean deb,
		  String val,
		  String val2,
		  String val3,
		  String val4,
		  String val5,
		  String val6,
		  String val7
		  ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setOther_ad(val2);
	setClass_list(val3);
	setOther_market(val4);
	setSpInstructions(val5);
	setSignBoard(val6);
	setSignBoardDate(val7);
    }
    public Market(boolean deb,
		  String val,
		  String val2,
		  String val3,
		  String val4,
		  String val5,
		  String val6,
		  String val7,
		  String val8,
		  String val9,
		  String val10,
		  String val11,
		  String val12
		  ){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setOther_ad(val2);
	setClass_list(val3);
	setOther_market(val4);
	setSpInstructions(val5);
	setSignBoard(val6);
	setSignBoardDate(val7);	
	setProg_id(val8);
	setFacility_id(val9);
	setGeneral_id(val10);
	setYear(val11);
	setSeason(val12);
	selectionDone = true;
    }	
    //
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof Market){
	    match = id.equals(((Market)gg).id);
	}
	return match;
    }
    public int hashCode(){
	int code = 0;
	try{
	    code = Integer.parseInt(id);
	}catch(Exception ex){};
	return code;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getOther_ad(){
	return other_ad;
    }
    public String getClass_list(){
	return class_list;
    }
    public String getOther_market(){
	return other_market;
    }
    public String getSpInstructions(){
	return spInstructions;
    }
    public String getYear(){
	return year;
    }
    public String getSeason(){
	return season;
    }
    public String getSignBoard(){
	return sign_board;
    }
    public String getSignBoardDate(){
	return sign_board_date;
    }    
    //
    // setters
    //
    public void setId(String val){
	if(val != null){
	    id = val.trim();
	}
    }
    public void setProg_id(String val){
	if(val != null){
	    prog_id = val;
	}
    }
    public void setFacility_id(String val){
	if(val != null){
	    facility_id = val;
	}
    }
    public void setYear(String val){
	if(val != null && !val.equals("-1")){
	    year = val;
	}
    }
    public void setGeneral_id(String val){
	if(val != null){
	    general_id = val;
	}
    }
    public void setSeason(String val){
	if(val != null && !val.equals("-1")){
	    season = val;
	}
    }			
    // needed for new plan
    public void setOther_ad(String val){
	if(val != null)
	    other_ad = val.trim();
    }
    // needed for new plan
    public void setClass_list(String val){
	if(val != null)
	    class_list = val;
    }
    public void setOther_market(String val){
	if(val != null)
	    other_market = val.trim();
    }
    public void setSpInstructions(String val){
	if(val != null)
	    spInstructions = val.trim();
    }
    public void setSignBoard(String val){
	if(val != null && !val.equals("")){
	    sign_board = val;
	}
    }
    public void setSignBoardDate(String val){
	if(val != null && !val.equals("")){
	    sign_board_date = val;
	}
    }    
    public void addAds(MarketAd[] vals){
	addAds = vals;
    }
    public void addAnnounces(String[] vals){
	addAnnounces = vals;
    }
    public void delAds(String[] vals){
	delAds = vals;
    }
	
    public void delAnnounces(String[] vals){
	delAnnounces = vals;
    }
    public void addItems(MarketItem[] vals){
	addItems = vals;
    }
    public void delItems(String[] vals){
	delItems = vals;
    }
    public double getTotalIndirectExpenses(){
	if(totalIndirect > 0) return totalIndirect;
	getItems();
	computeExpenses();
	return totalIndirect;
    }
    public double getTotalDirectExpenses(){
	if(totalDirect > 0) return totalDirect;
	computeExpenses();
	return totalDirect;
    }
    public Program getProgram(){
	if(prog == null && !prog_id.equals("")){
	    Program one = new Program(debug, prog_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		prog = one;
	    }
	}
	return prog;
    }
    public Facility getFacility(){
	if(facility == null && !facility_id.equals("")){
	    Facility one = new Facility(debug, facility_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		facility = one;
	    }
	}
	return facility;
    }
    public General getGeneral(){
	if(general == null && !general_id.equals("")){
	    General one = new General(debug, general_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		general = one;
	    }
	}
	return general;
    }		
    public boolean hasProgram(){
	getProgram();
	return prog != null;
    }
    public boolean hasFacility(){
	getFacility();
	return facility != null;
    }
    public boolean hasHistory(){
	getHistory();
	return history != null;
    }
    public boolean hasGeneral(){
	getGeneral();
	return general != null;
    }		
    public List<History> getHistory(){
	if(history == null && !id.equals("")){
	    HistoryList ones = new HistoryList(debug, id, "Market");
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		history = ones;
	    }
	}
	return history;
    }		
    public void computeExpenses(){
	totalDirect = 0;
	totalIndirect = 0;
	if(items == null)
	    getItems();
	if(ads == null)
	    getAds();
	if(items != null && items.size() > 0){
	    for(MarketItem one:items){
		double value = one.getExpenses();
		if(one.isDirect()) totalDirect += value;
		else totalIndirect += value;
	    }
	}
	if(ads != null && ads.size() > 0){
	    for(MarketAd one:ads){
		double value = one.getExpenses();
		if(one.isDirect()) totalDirect += value;
		else totalIndirect += value;
	    }
	}
    }
    public boolean hasFiles(){
	getFiles();
	return files != null && files.size() > 0;
    }		
    public List<PromtFile> getFiles(){
	if(files == null && !id.equals("")){
	    PromtFileList tsl = new PromtFileList(debug, id, "Marketing");
	    String back = tsl.find();
	    if(back.equals("")){
		List<PromtFile> ones = tsl.getFiles();
		if(ones != null && ones.size() > 0){
		    files = ones;
		}
	    }
	}
	return files;
    }		
    public boolean recordExists(){
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int count = 0;
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return false;
	}		
	String qq = "select count(*) from marketing where id=?";
	try{
	    pstmt = con.prepareStatement(qq);			
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		count = rs.getInt(1);
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}		
	return count > 0;
    }
    public String doSaveOrUpdate(){
	if(recordExists()){
	    return doUpdate();
	}
	return doSave();
    }
    public String doSave(){
				
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);

	}
	if(!facility_id.equals("") || !general_id.equals("")){
	    if(year.equals("")){
		back = "year is required";
		addError(back);
		return back;
	    }
	    if(season.equals("")){
		back = "season is required";
		addError(back);
		return back;
	    }
	}
	String qq = "insert into marketing "+
	    "values(0,?,?,?,?,?,?)";						
	try{
	    pstmt = con.prepareStatement(qq);			
	    setParams(pstmt);
	    pstmt.executeUpdate();
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	    qq = "insert into marketing_programs values(?,?)";
	    if(!facility_id.equals("")){
		qq = "insert into marketing_facilities values(?,?,?,?)";
	    }
	    else if(!general_id.equals("")){
		qq = "insert into marketing_generals values(?,?,?,?)";
	    }
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    if(!prog_id.equals("")){
		pstmt.setString(2, prog_id);
	    }
	    else if(!general_id.equals("")){
		pstmt.setString(2, general_id);
		pstmt.setString(3, year);
		pstmt.setString(4, season);
	    }
	    else{ // facility
		pstmt.setString(2, facility_id);								
		pstmt.setString(3, year);
		pstmt.setString(4, season);
	    }
	    pstmt.executeUpdate();
	    message="Saved Successfully";
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	back += doAdding();
	return back;
    }
    public String doDuplicate(String program_id){
	String back = "";
	if(!selectionDone){
	    back = doSelect();
	    if(!back.equals("")){
		return back;
	    }
	}
	getAnnounces();
	if(announces != null && announces.size() > 0){
	    addAnnounces = new String[announces.size()];
	    int j=0;
	    for(Type one:announces){
		addAnnounces[j++] = one.getId();
	    }
	}
	getItems();
	if(items != null){
	    if(items != null && items.size() > 0){
		addItems = new MarketItem[items.size()];
		int j=0;
		for(MarketItem one:items){
		    addItems[j++] =new MarketItem(debug, one);
		}
	    }
	}
	getAds();
	if(ads != null){
	    addAds = new MarketAd[ads.size()];
	    int j=0;								
	    for(MarketAd one:ads){
		addAds[j++] =new MarketAd(debug, one);
	    }
	}
	id="";
	prog_id = program_id;
	back = doSave();
	return back;
    }
    public String doUpdate(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String str="";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qq = "update marketing set "+
	    "other_ad=?,class_list=?,other_market=?,"+
	    "spInstructions=?,sign_board=?,sign_board_date=? "+
	    "where id = ? ";
	if(debug)
	    logger.debug(qq);
		
	try{
	    pstmt = con.prepareStatement(qq);
	    //
	    int jj=1;
	    if(other_ad.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,other_ad);
	    if(class_list.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++, class_list);
	    if(other_market.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,other_market);
	    if(spInstructions.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,spInstructions);
	    if(sign_board.isEmpty())
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(sign_board_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else
		pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(sign_board_date).getTime()));
	    pstmt.setString(jj++, id);
	    pstmt.executeUpdate();
	    message="Updated Successfully";
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	back += doAdding();
	back += doDeleting();
	return back;

    }
    String setParams(PreparedStatement pstmt){
	String back = "";
	try{
	    int jj=1;
	    if(other_ad.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,other_ad);
	    if(class_list.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++, class_list);
	    if(other_market.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,other_market);
	    if(spInstructions.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,spInstructions);
	    if(sign_board.isEmpty())
		pstmt.setNull(jj++,Types.CHAR);
	    else
		pstmt.setString(jj++,"y");
	    if(sign_board_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else
		pstmt.setDate(jj++,new java.sql.Date(dateFormat.parse(sign_board_date).getTime()));
	}
	catch(Exception ex){
	    back += ex;
	    addError(back);
	}
	return back;
    }
    public String doDelete(){
	String back = "", qq = "", qq2="", qq3="", qq4="";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    qq = "delete from marketing_programs where market_id=?";
	    qq2 = "delete from marketing_facilities where market_id=?";
	    qq3 = "delete from marketing_generals where marke_id=?";
	    qq4 = "delete from marketing where id=?";
			
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq2;
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq3;
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    qq = qq4;
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();						
	    message="Deleted Successfully";
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
	
    public String doSelect(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	String qq = "select "+
	    " m.other_ad,m.class_list,m.other_market,m.spInstructions, "+
	    " m.sign_board, "+
	    " date_format(m.sign_board_date,'%m/%d/%Y'), "+ // 44
	    " mp.prog_id,mf.facility_id,mf.year,mf.season,"+
	    " g.general_id,g.year,g.season "+
	    " from marketing m "+
	    " left join marketing_programs mp on m.id=mp.market_id "+
	    " left join marketing_facilities mf on m.id=mf.market_id "+
	    " left join marketing_generals g on m.id=g.market_id "+						
	    " where m.id=? ";
	con = Helper.getConnection();
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
	    pstmt.setString(1, id); 
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		String str = rs.getString(1);
		if(str != null) other_ad = str;
		str = rs.getString(2);
		if(str != null) class_list = str;
		str = rs.getString(3);				
		if(str != null) other_market = str;
		str = rs.getString(4);
		if(str != null) spInstructions = str;
		str = rs.getString(5);
		if(str != null) sign_board = str;
		str = rs.getString(6);
		if(str != null) sign_board_date = str;
		setProg_id(rs.getString(7));
		setFacility_id(rs.getString(8));
		setYear(rs.getString(9));
		setSeason(rs.getString(10));
		setGeneral_id(rs.getString(11));
		setYear(rs.getString(12));
		setSeason(rs.getString(13));
		message = "";
		selectionDone = true;
	    }
	    else{
		message = "No Marketing found";
		back = message;
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
	
    public String doAdding(){
		
	String back = "", qq="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	if(addAnnounces != null){
	    con = Helper.getConnection();
	    if(con == null){
		back = "Could not connect to DB";
		addError(back);
		return back;
	    }
	    try{
		qq = "insert into market_announce_details values(?,?)";
		if(debug){
		    logger.debug(qq);
		}
		pstmt = con.prepareStatement(qq);
		for(String str:addAnnounces){
		    if(!str.equals("")){
			pstmt.setString(1, id);
			pstmt.setString(2, str);
			pstmt.executeUpdate();
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
	}
	if(addItems != null){
	    for(MarketItem one:addItems){
		one.setMarket_id(id);
		if(one.isValid()){
		    back += one.doSave();
		}
	    }
	}
	if(addAds != null){
	    for(MarketAd one:addAds){
		one.setMarket_id(id);
		if(one.isValid()){
		    back += one.doSave();
		}
	    }
	}
	return back;		
    }
    public String doDeleting(){
		
	String back = "", qq="";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(delAds != null){
		qq = "delete from market_ad_details where id=?";
		if(debug){
		    logger.debug(qq);
		}
		pstmt = con.prepareStatement(qq);
		for(String str:delAds){
		    if(!str.equals("")){
			pstmt.setString(1, str);
			pstmt.executeUpdate();
		    }
		}
	    }
	    if(delAnnounces != null){
		qq = "delete from market_announce_details where market_id=? and an_id=?";
		if(debug){
		    logger.debug(qq);
		}
		pstmt = con.prepareStatement(qq);
		for(String str:delAnnounces){
		    if(!str.equals("")){					
			pstmt.setString(1, id);
			pstmt.setString(2, str);
			pstmt.executeUpdate();
		    }
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
	if(delItems != null){
	    for(String str:delItems){
		MarketItem tt = new MarketItem(debug, str);
		back += tt.doDelete();
	    }
	}
	return back;		
    }	
    public List<MarketAd> getAds(){
	if(ads == null && !id.equals("")){
	    MarketAdList ads2 = new MarketAdList(debug, id);
	    String back = ads2.find();
	    if(back.equals("") && ads2.size() > 0){
		ads = ads2;
	    }
	}
	return ads;
    }
    public List<Type> getAnnounces(){
	if(announces == null && !id.equals("")){
	    AnnounceList ones = new AnnounceList(debug, id);
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		announces = ones;
	    }			
	}
	return announces;
    }
    public List<MarketItem> getItems(){
	if(items == null && !id.equals("")){
	    MarketItemList ones = new MarketItemList(debug, id);
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		items = ones;
	    }					
	}
	return items;
    }	

}
