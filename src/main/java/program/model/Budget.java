package program.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;


public class Budget extends CommonInc{

    boolean debug = false, hasRecord = false;
    static Logger logger = LogManager.getLogger(Budget.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    int admin_percent = 15; // 5, 10, 15
    double admin_fees = 0, // calculated 
	sponsorship =0, donation=0;
    String id = "", sponsor_revenue="", wby="", date="",
	notes="";
    //
    // reveue stuff
    FeeItemList fees = null;
    //
    // expenses stuff
    StaffExpenseList staffExpenses = null;
    SupplyItemList supplies = null;
    DirectExpenseList otherDirectExpenses = null;	
    //
    Evaluation eval = null;
    Market market = null;
    Program program = null;
    String[] feeTypes = {"","",""};
    String[] feeRates = {"","",""};
    String[] feeQuantity ={"","",""};
	
    String[] dirTypes = {"","",""};
    String[] dirRates = {"","",""};
    String[] dirQuantity ={"1","1","1"};	
    String[] dirHours = {"","",""};

    String[] indirTypes = {"","",""};
    String[] indirRates = {"","",""};
    String[] indirQuantity ={"1","1","1"};	
    String[] indirHours = {"","",""};
    //
    String[] otherDirDesc = {"","",""};
    String[] otherDirExpense = {"","",""};

    String[] del_staff = null;
    String[] del_fee = null;
    String[] del_otherDir = null;
    public Budget(boolean val){
	debug = val;
    }
    public Budget(boolean val, String val2){
	this(val);
	setId(val2);
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setWby(String val){
	if(val != null)
	    wby = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }	
    public void setSponsor_revenue(String val){
	if(val != null)
	    sponsor_revenue = val;
    }	
    public void setAdmin_percent(String val){
	if(val != null){
	    try{
		admin_percent = Integer.parseInt(val);
	    }catch(Exception ex){}
	}
    }
    public void setAdmin_fees(String val){
	if(val != null){
	    try{
		admin_fees = Double.parseDouble(val);
	    }catch(Exception ex){}
	}
    }
    public void setSponsorship(String val){
	if(val != null){
	    try{
		sponsorship = Double.parseDouble(val);
	    }catch(Exception ex){}
	}
    }
    public void setDonation(String val){
	if(val != null){
	    try{
		donation = Double.parseDouble(val);
	    }catch(Exception ex){}
	}
    }
    public void setDel_staffs(String[] vals){
	del_staff = vals;
    }
    public void setDel_fees(String[] vals){
	del_fee = vals;
    }
    public void setDel_otherDir(String[] vals){
	del_otherDir = vals;		
    }	
    public String getId(){
	return id;
    }
    public String getWby(){
	return wby;
    }
    public String getDate(){
	return date;
    }
    public String getNotes(){
	return notes;
    }
    public String getSponsor_revenue(){
	return sponsor_revenue;
    }	
    public int getAdmin_percent(){
	return admin_percent;
    }
    public double getSponsorship(){
	return sponsorship;
    }
    public double getDonation(){
	return donation;
    }	
    public boolean hasRecord(){
	return hasRecord;
    }
    public boolean isSponsorshipRevenue(){
	return !sponsor_revenue.equals("");
    }
    public StaffExpenseList getStaffExpenses(){
	return staffExpenses;
    }
    public SupplyItemList getSupplies(){
	return supplies;
    }
    public FeeItemList getFees(){
	return fees;
    }
    public Evaluation getEvaluation(){
	if(eval == null && !id.equals("")){
	    Evaluation one = new Evaluation(debug, id);
	    String back = one.doSelect();
	    if(back.equals("")){
		eval = one;
	    }
	}
	return eval;
    }
    public Market getMarket(){
	if(market == null){
	    if(!id.equals("")){
		getProgram(); // market included
	    }
	    if(market == null){
		market = new Market(debug);
	    }
	}
	return market;
    }
    public Program getProgram(){
	if(program == null && !id.equals("")){
	    Program one = new Program(debug, id);
	    String back = one.doSelect();
	    if(back.equals("")){
		program = one;
		if(program.hasMarket()){
		    market = program.getMarket();
		}
	    }
	}
	return program;
    }	
    public int getAttendance(){
	int count = 0;
	getEvaluation();
	if(eval != null){
	    try{
		count =  Integer.parseInt(eval.getAttendance());
	    }catch(Exception ex){}
	}
	return count;
    }
    public double getMarketIndirectExpenses(){
	getMarket();
	if(market != null){
	    return market.getTotalIndirectExpenses();
	}
	return 0;
    }
    public double getTotalRevenue(){
	double total = 0;
	getFees();
	if(fees != null){
	    total = fees.getTotal();
	}
	if(isSponsorshipRevenue()){
	    total += sponsorship;
	}
	total += donation;		
	return total;
    }
    public double getTotalDirectExpenses(){
	double total = 0;
	if(staffExpenses != null){
	    total += staffExpenses.getTotalDirect();
	}
	if(market != null){
	    total += market.getTotalDirectExpenses();
	}
	if(otherDirectExpenses != null){
	    total += otherDirectExpenses.getTotal();
	}
	return total;
    }
    public double getStaffDirectExpenses(){
	if(staffExpenses != null){
	    return staffExpenses.getTotalDirect();
	}
	return 0;
    }
    public double getStaffIndirectExpenses(){
	if(staffExpenses != null){
	    return staffExpenses.getTotalIndirect();
	}
	return 0;
    }		
    public double getMarketDirectExpenses(){
	getMarket();
	if(market != null){
	    return market.getTotalDirectExpenses();
	}
	return 0;
    }
    public double getSubTotalIndirectExpenses(){
	double total = 0;
	if(staffExpenses != null){
	    total += staffExpenses.getTotalIndirect();
	}
	if(market != null){
	    total += market.getTotalIndirectExpenses();
	}
	return total;
    }
    public double getAdmin_fees(){
		
	double total = getSubTotalIndirectExpenses();
	admin_fees = total * (admin_percent/100.);
	return admin_fees;
    }
    public double getTotalIndirectExpenses(){
	double total = getSubTotalIndirectExpenses();
	total += getAdmin_fees();
	return total;
    }
    public Double getTotalExpenses(){
	double total = getTotalDirectExpenses();
	total += getTotalIndirectExpenses();
	return total;
    }
    public double getRecoveryLevel(){
	double tr = getTotalRevenue();
	double te = getTotalExpenses();
	double ratio = 0;
	if(te > 0){
	    ratio = 100. - ((te-tr)/te)*100.;
	}
	return ratio;
    }
    public double getUnrecoveredExpenses(){
	return getTotalExpenses()-getTotalRevenue();
    }
    public double getCostPerParticipant(){
	double cost = 0;		
	int count = getAttendance();
	double total = getUnrecoveredExpenses();
	if(count > 0){
	    cost = total/count;
	}
	return cost;
    }
    public double getUnrecoveredDirectExpenses(){
	double total = getTotalDirectExpenses();
	total -= getTotalRevenue();
	return total;
    }
    public double getDirectCostPerParticipant(){
	double cost = 0;		
	int count = getAttendance();
	double total = getUnrecoveredDirectExpenses();
	if(count > 0){
	    cost = total/count;
	}
	return cost;
    }	
    public double getDirectRecoveryLevel(){
	double tr = getTotalRevenue();
	double tde = getTotalDirectExpenses();
	double ratio = 0;
	if(tde > 0){
	    ratio = 100. - ((tde-tr)/tde)*100.;
	}
	return ratio;
    }
    public DirectExpenseList getOtherDirectExpenses(){
	if(otherDirectExpenses == null){
	    DirectExpenseList ones = new DirectExpenseList(debug, id);
	    String back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		otherDirectExpenses = ones;
	    }
	}
	return otherDirectExpenses;
    }	
    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into eval_budgets values(?,?,?,?,?,?,?,?,?)";
	if(id.equals("")){
	    back = " id not set";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt.setString(1,id);
	    pstmt.setString(2,""+admin_fees);
	    pstmt.setString(3, ""+admin_percent);
	    pstmt.setString(4,""+sponsorship);
	    if(sponsor_revenue.equals(""))
		pstmt.setNull(5, Types.CHAR);
	    else
		pstmt.setString(5,sponsor_revenue);
	    pstmt.setString(6,""+donation);
	    if(wby.equals(""))
		pstmt.setNull(7,Types.VARCHAR);
	    else
		pstmt.setString(7,wby);
	    if(date.equals(""))
		date = Helper.getToday2();
			
	    pstmt.setDate(8,new java.sql.Date(dateFormat.parse(date).getTime()));
	    if(notes.equals(""))
		pstmt.setNull(9,Types.VARCHAR);
	    else
		pstmt.setString(9,notes);
	    pstmt.executeUpdate();
	    hasRecord = true;
	    message="Saved Successfully";			
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	back += addNewItems();
	findRelatedLists();
	return back;
    }
    public String doUpdate(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "update eval_budgets set admin_fees=?,admin_percent=?,sponsorship=?,sponsor_revenue=?,donation=?,wby=?,date=?,notes=? where id=?";
	if(id.equals("")){
	    back = " id not set";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt.setString(1,""+admin_fees);
	    pstmt.setString(2, ""+admin_percent);
	    pstmt.setString(3,""+sponsorship);
	    if(sponsor_revenue.equals(""))
		pstmt.setNull(4, Types.CHAR);
	    else
		pstmt.setString(4,sponsor_revenue);
	    pstmt.setString(5,""+donation);
	    if(wby.equals(""))
		pstmt.setNull(6,Types.VARCHAR);
	    else
		pstmt.setString(6,wby);
	    if(date.equals(""))
		date = Helper.getToday2();
			
	    pstmt.setDate(7,new java.sql.Date(dateFormat.parse(date).getTime()));
	    if(notes.equals(""))
		pstmt.setNull(8,Types.VARCHAR);
	    else
		pstmt.setString(8,notes);
	    pstmt.setString(9,id);
	    pstmt.executeUpdate();
	    //
	    hasRecord = true;
	    message="Updated Successfully";			
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	back += addNewItems();
	back += deleteItems();
	findRelatedLists();
	return back;
    }
    // handles fee-type, fee-rate, fee-quantity
    public void addFee(String name, String value){
	if(value.equals("")) return;
	String str = name.substring(name.indexOf("_")+1);
	int ind = -1;
	try{
	    ind = Integer.parseInt(str);
	}catch(Exception ex){
	    System.err.println(ex);
	}
	if(ind < 0) return;
	if(name.contains("type")){
	    feeTypes[ind] = value;
	}
	else if(name.contains("rate")){
	    feeRates[ind] = value;
	}
	else{
	    feeQuantity[ind] = value;
	}
    }
    // handles dir-type, dir-rate, dir-quantity, dir-hours
    public void addDir(String name, String value){
	if(value.equals("")) return;
	String str = name.substring(name.indexOf("_")+1);
	int ind = -1;
	try{
	    ind = Integer.parseInt(str);
	}catch(Exception ex){
	    System.err.println(ex);
	}
	if(ind < 0) return;
	if(name.contains("type")){
	    dirTypes[ind] = value;
	}
	else if(name.contains("rate")){
	    dirRates[ind] = value;
	}
	else if(name.contains("hours")){
	    dirHours[ind] = value;
	}		
	else{
	    dirQuantity[ind] = value;
	}
    }
    public void addIndir(String name, String value){
	if(value.equals("")) return;
	String str = name.substring(name.indexOf("_")+1);
	int ind = -1;
	try{
	    ind = Integer.parseInt(str);
	}catch(Exception ex){
	    System.err.println(ex);
	}
	if(ind < 0) return;
	if(name.contains("type")){
	    indirTypes[ind] = value;
	}
	else if(name.contains("rate")){
	    indirRates[ind] = value;
	}
	else if(name.contains("hours")){
	    indirHours[ind] = value;
	}		
	else{
	    indirQuantity[ind] = value;
	}
    }
    public void addOtherDirect(String name, String val){
	if(val.equals("")) return;
	String str = name.substring(name.indexOf("_")+1);
	int ind = -1;
	try{
	    ind = Integer.parseInt(str);
	}catch(Exception ex){
	    System.err.println(ex);
	}
	if(ind < 0) return;
	if(name.contains("Desc")){
	    otherDirDesc[ind] = val;
	}
	else if(name.contains("Expense")){
	    otherDirExpense[ind] = val;
	}
		
    }
	
    private String addNewItems(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "insert into fee_items values(0,?,?,?,?)";
	String qq2 = "insert into staff_expenses values(0,?,?,?,?,?,?)";
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
	    for(int i=0;i<feeTypes.length;i++){
		if(feeTypes[i].equals("") || feeRates[i].equals("") || feeQuantity[i].equals("")) continue;
		pstmt.setString(1, id);
		pstmt.setString(2,feeTypes[i]);
		pstmt.setString(3,feeQuantity[i]);
		pstmt.setString(4,feeRates[i]);
		pstmt.executeUpdate();
	    }
	    qq = qq2;
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    for(int i=0;i<dirTypes.length;i++){
		if(dirTypes[i].equals("") || dirRates[i].equals("") || dirHours[i].equals("")) continue;
		pstmt.setString(1, id);
		pstmt.setString(2,dirTypes[i]);
		pstmt.setString(3,dirHours[i]);
		pstmt.setString(4,dirRates[i]);
		pstmt.setString(5,"Direct");
		pstmt.setString(6,dirQuantity[i]);

		pstmt.executeUpdate();
	    }
	    for(int i=0;i<indirTypes.length;i++){
		if(indirTypes[i].equals("") || indirRates[i].equals("") || indirHours[i].equals("")) continue;
		pstmt.setString(1, id);
		pstmt.setString(2,indirTypes[i]);
		pstmt.setString(3,indirHours[i]);
		pstmt.setString(4,indirRates[i]);
		pstmt.setString(5,"Indirect");
		pstmt.setString(6,indirQuantity[i]);

		pstmt.executeUpdate();
	    }
	    for(int i=0;i<otherDirDesc.length;i++){
		DirectExpense one = new DirectExpense(debug, null, id, otherDirDesc[i],otherDirExpense[i]);
		if(one.isValid()){
		    one.doSave();
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);	
	}
	return back;
    }
    String deleteItems(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	//
	if(del_staff == null &&
	   del_fee == null &&
	   del_otherDir == null) return back;
	String qq = "delete from staff_expenses where id=? ";
	String qq2 = "delete from fee_items where id=? ";
	String qq3 = "delete from budget_direct_expenses where id=? ";
			
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(del_staff != null){
		if(debug){
		    logger.debug(qq);
		}
			
		pstmt = con.prepareStatement(qq);
		for(String str:del_staff){
		    pstmt.setString(1, str);
		    pstmt.executeUpdate();
		}
	    }
	    if(del_fee != null){
		qq = qq2;
		if(debug){
		    logger.debug(qq);
		}
		pstmt = con.prepareStatement(qq);
		for(String str:del_fee){
		    pstmt.setString(1, str);
		    pstmt.executeUpdate();
		}
	    }
	    if(del_otherDir != null){
		qq = qq3;
		if(debug){
		    logger.debug(qq);
		}
		pstmt = con.prepareStatement(qq);
		for(String str:del_otherDir){
		    pstmt.setString(1, str);
		    pstmt.executeUpdate();
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+qq);
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
		
	String qq = "select admin_fees,admin_percent,sponsorship,"+
	    " sponsor_revenue,donation,wby,"+
	    " date_format(date,'%m/%d/%Y'),"+
	    " notes "+
	    " from eval_budgets where id=? ";
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
		setAdmin_fees(rs.getString(1));
		setAdmin_percent(rs.getString(2));
		setSponsorship(rs.getString(3));
		setSponsor_revenue(rs.getString(4));				
		setDonation(rs.getString(5));
		setWby(rs.getString(6));
		setDate(rs.getString(7));
		setNotes(rs.getString(8));
		hasRecord = true;
		back = findRelatedLists();
	    }
	    else{
		message = "record found";
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
    private String findRelatedLists(){
	String back;
	if(true){
	    StaffExpenseList ones = new StaffExpenseList(debug, id);
	    back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		staffExpenses = ones;
	    }
	}
	if(true){
	    SupplyItemList ones = new SupplyItemList(debug, id);
	    back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		supplies = ones;
	    }			
	}
	if(true){
	    FeeItemList ones = new FeeItemList(debug, id);
	    back = ones.find();
	    if(back.equals("") && ones.size() > 0){
		fees = ones;
	    }					
	}
	return back;
    }
	
	
}















































