package program.util;

import java.util.*;
import java.util.Date;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.list.*;

/**
 * 
 */
public class Helper{
	
    static Logger logger = LogManager.getLogger(Helper.class);
    final static Locale local = new Locale("Latin","US");
    final static TimeZone tzone = TimeZone.getTimeZone("America/Indiana/Indianapolis");
    public final static String summerStartDate = "05/01/"; // real 6/22
    public final static String summerEndDate = "08/31/";   // real 09/22
    public final static String fallWinterStartDate = "09/01/"; // real 9/23
    public final static String fallWinterEndDate = "12/31/";  
    public final static String winterSpringStartDate = "01/01/"; // 
    public final static String winterSpringEndDate = "04/30/"; //  real 6/21
    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");    
    final static Map<String, String> cutUpSeasons = createMap();
    final static Map<String, String> cutUpDates = createMap2();		
    static int c_con = 0;
    public final static Map<String, String>       mimeTypes = new HashMap<>();
    static {
        mimeTypes.put("image/gif",       "gif");
        mimeTypes.put("image/jpeg",      "jpg");
        mimeTypes.put("image/png",       "png");
        mimeTypes.put("image/tiff",      "tiff");
        mimeTypes.put("image/bmp",       "bmp");
        mimeTypes.put("text/plain",      "txt");
        mimeTypes.put("audio/x-wav",     "wav");
        mimeTypes.put("application/pdf", "pdf");
        mimeTypes.put("audio/midi",      "mid");
        mimeTypes.put("video/mpeg",      "mpeg");
        mimeTypes.put("video/mp4",       "mp4");
        mimeTypes.put("video/x-ms-asf",  "asf");
        mimeTypes.put("video/x-ms-wmv",  "wmv");
        mimeTypes.put("video/x-msvideo", "avi");
        mimeTypes.put("text/html",       "html");

        mimeTypes.put("application/mp4",               "mp4");
        mimeTypes.put("application/x-shockwave-flash", "swf");
        mimeTypes.put("application/msword",            "doc");
        mimeTypes.put("application/xml",               "xml");
        mimeTypes.put("application/vnd.ms-excel",      "xls");
        mimeTypes.put("application/vnd.ms-powerpoint", "ppt");
        mimeTypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
    }		
    /**
     *
     */
		
    Helper(){

    }
    public final static Map<String, String> createMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("Ongoing","");
        result.put("Fall/Winter", "09/01/");
	result.put("Winter/Spring","01/01/");
	result.put("Summer","05/01/");
        return Collections.unmodifiableMap(result);
    }
    public final static Map<String, String> createMap2() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("09/01/","Fall/Winter");
	result.put("01/01/","Winter/Spring");
	result.put("05/01/","Summer");
        return Collections.unmodifiableMap(result);
    }		
    public final static String staffSelectArr[] = { 
	"&nbsp;",           // 0 
	"Leader SPT",       // 1
	"Supervisor SPT",   // 2
	"Instructor SPT",   // 3
	"Full Time Staff",  // 4             
	"Staff Assistant",  // 5 
	// "Volunteer",     // 6
	"Other",            // 7
	"Intern",           // 8
	"Contractor",        // 9
	"RFTL",                
	"RFTS"
    };	

    public final static String staffSelectArr2[] = { 
	"&nbsp;",           // 0 
	"Leader SPT",       // 1
	"Supervisor SPT",   // 2
	"Instructor SPT",   // 3
	"&nbsp;",           // 4  "Full Time Staff",   // deleted 4
	"Staff Assistant",  // 5 
	"&nbsp;",           // 6 " Volunteer deleted 6
	"Other",            // 7
	"Intern",           // 8
	"Contractor",       // 9
	"RFTL",                
	"RFTS"
    };

    public final static String allFreqArr[] = { 
	"",                 // 0 
	"Seasonal",         // 1
	"Yearly",           // 2
	"Ongoing"           // 3
    };

    public final static String allPartArr[] = {
	"",
	"Paid Participant",
	"Unpaid Participant",
	"Group",
	"Spectator",
	"Animal",
	"Vendor/Exhibitor",
	"Partnership",
	"Single",
	"Double",
	"Triple"
    };

    //
    public final static String sessionSortOpt[]={"sid",
	"startDate,startEndTime",
	"description,startDate",
	"ageFrom"
    };
    //
    public final static String sessionSortArr[]={"Session ID",
	"Start Date, Start Time",
	"Description, Start Date",
	"Age"
    };
    //
    public final static String allSeasons = 
	"<option value=\"Ongoing\">Ongoing</option>"+
	"<option value=\"Fall/Winter\">Fall/Winter</option>"+
	"<option value=\"Winter/Spring\">Winter/Spring</option>"+
	"<option value=\"Summer\">Summer</option>";
    public final static String[] seasonsArr ={"Ongoing",
	"Fall/Winter",
	"Winter/Spring",
	"Summer"};
    //
    //
    // Preschool/Youth/Adult
    // Preschool:Youth:Adult:Teen
    //

    public final static String[] preschoolSub = {
	"",
	"Music & Dance",
	"Sports & Fitness",
	"Living & Learning",
	"Arts & Crafts",
	"The Great Outdoors",
	"Travel",
	"Technology"
    };
    public final static void databaseDisconnect(Connection con,
						ResultSet rs,
						Statement... stmt) {
	try {
	    if(rs != null) rs.close();
	    rs = null;
	    if(stmt != null){
		for(Statement one:stmt){
		    if(one != null)
			one.close();
		    one = null;
		}
	    }
	    if(con != null) con.close();
	    con = null;
	    logger.debug("Closed Connection "+c_con);
	    c_con--;
	    if(c_con < 0) c_con = 0;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
	    if (rs != null) {
		try { rs.close(); } catch (SQLException e) { }
		rs = null;
	    }
	    if (stmt != null) {
		try {
		    for(Statement one:stmt){										
			if(one != null)
			    one.close(); 
			one = null;
		    }
		} catch (SQLException e) { }
	    }
	    if (con != null) {
		try { con.close(); } catch (SQLException e) { }
		con = null;
	    }
	}
    }				
    public final static String getFileExtension(File file) {
	String ext = "";
	if(file == null) return ext;
	try {
	    String name = file.getName();
	    String pp = file.getAbsolutePath();
	    Path path = Paths.get(pp);
	    String fileType = Files.probeContentType(path);
	    if(fileType != null){
		// application/pdf
		if(fileType.endsWith("pdf")){
		    ext="pdf";
		}
		//image/jpeg
		else if(fileType.endsWith("jpeg")){
		    ext="jpg";
		}
		//image/gif
		else if(fileType.endsWith("gif")){
		    ext="gif";
		}
		//image/bmp
		else if(fileType.endsWith("bmp")){
		    ext="bmp";
		}
		// application/msword
		else if(fileType.endsWith("msword")){
		    ext="doc";
		}
		//application/vnd.ms-excel
		else if(fileType.endsWith("excel")){
		    ext="csv";
		}
		//application/vnd.openxmlformats-officedocument.wordprocessingml.document
		else if(fileType.endsWith(".document")){
		    ext="docx";
		}
		// text/plain
		else if(fileType.endsWith("plain")){
		    ext="txt";
		}
		//application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
		else if(fileType.endsWith(".sheet")){
		    ext="xlsx";
		}
		// audio/wav
		else if(fileType.endsWith("wav")){
		    ext="wav";
		}
		// text/xml
		else if(fileType.endsWith("xml")){
		    ext="xml";
		}										
		else if(fileType.endsWith("html")){
		    ext="html";
		}
		// video/mng
		else if(fileType.endsWith("mng")){
		    ext="mng";
		}
		else if(fileType.endsWith("mpeg")){
		    ext="mpg";
		}
		// video/mp4
		else if(fileType.endsWith("mp4")){
		    ext="mp4";
		}										
		else if(fileType.endsWith("avi")){
		    ext="avi";
		}
		else if(fileType.endsWith("mov")){
		    ext="mov";
		}
		// quick time video
		else if(fileType.endsWith("quicktime")){
		    ext="qt";
		}
		else if(fileType.endsWith("wmv")){
		    ext="wmv"; 
		}
		else if(fileType.endsWith("asf")){
		    ext="asf";
		}
		// flash video
		else if(fileType.endsWith("flash")){
		    ext="swf";
		}										
		else if(fileType.startsWith("image")){
		    ext="jpg";
		}
	    }
	} catch (Exception e) {
	    System.err.println(e);
	}
	return ext;
    }
    public final static void printFiles(PrintWriter out,
				 String url,
				 List<PromtFile> files){
	if(files == null || files.size() == 0) return;
	out.println("<table border=\"1\" width=\"80%\"><caption> Related Files</caption>");
	out.println("<tr><th>Date</th><th>Added by</th><th>File Name</th><th>Notes<th></tr>");
	for(PromtFile one:files){
	    out.println("<tr><td>"+one.getDate()+"</td>"+
			"<td>"+one.getAddedBy()+"</td>"+
			"<td><a href=\""+url+"PromtFile.do?action=download&id="+one.getId()+"\">"+one.getOldName()+"</a></td>"+
			"<td>"+one.getNotes()+"</td></tr>");
	}
	out.println("</table>");
    }
    public final static String getFileExtensionFromName(String name)
    {
        String ext = "";
        try {
            if (name.indexOf(".") > -1) {
                ext = name.substring(name.lastIndexOf(".") + 1);
            }
        }
        catch (Exception ex) {

        }
        return ext;
    }
    //
    // People's University
    //
    public final static String[] peopleUnivSub = {
	"",
	"Writing, Language & Literature",
	"Home, Garden & Beyond",
	"Cooking",
	"Health & Wellness",
	"Music & Dance",
	"Arts & Crafts",
	"Finance",
	"Lectures & Workshops"
    };
    public final static boolean isNumberic(String str){
	return str.matches("[+-]?\\d*(\\.\\d+)?");
    }

    public final static Connection getConnection(){
	return getConnectionProd();
    }
    //
    // get rid single quote, double quote new line, return, tab, & > and <
    //
    public final static String cleanText(final String str){
	if(str != null){
	    return str.replaceAll("[\'\"\n\r\b\t&<>]+","");
	    // return  str.replaceAll("[^a-zA-Z0-9,_ ]+", "");
	}
	else
	    return str;
    }
    public final static Connection getConnectionProd(){

	Connection con = null;
	int trials = 0;
	boolean pass = false;
	while(trials < 3 && !pass){
	    try{
		trials++;
		// logger.debug("Connection try "+trials);
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/MySQL_promt");
		con = ds.getConnection();
		if(con == null){
		    String str = " Could not connect to DB ";
		    logger.error(str);
		}
		else{
		    pass = testCon(con);
		    if(pass){
			c_con++;
			// logger.debug("Got connection: "+c_con);
			// logger.debug("Got connection at try "+trials);
		    }
		}
	    }
	    catch(Exception ex){
		logger.error(ex);
	    }
	}
	return con;
		
    }
    final static boolean testCon(Connection con){
	boolean pass = false;
	Statement stmt  = null;
	ResultSet rs = null;
	String qq = "select 1+1";		
	try{
	    if(con != null){
		stmt = con.createStatement();
		logger.debug(qq);
		rs = stmt.executeQuery(qq);
		if(rs.next()){
		    pass = true;
		}
	    }
	    rs.close();
	    stmt.close();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	}
	return pass;
    }
    /**
     * given season we want the date
     */ 
    public final static String getCutUpDate(String season, String year){
	String ret = "";
	if(season.equals("Ongoing")) return ret;
	if(cutUpDates.containsKey(season)){
	    ret = cutUpDates.get(season)+year;
	}
	return ret;
    }
    /**
     * given date we want season
     */
    public final static String getCurrentSeason(){
	String ret = "";
	int mm = getCurrentMonth(); 
	if(mm < 5) ret = "Winter/Spring";
	else if(mm < 9) ret = "Summer";
	else ret = "Fall/Winter";
	return ret;
    }
    public final static String getNextSeason(){
	String ret = "";
	int mm = getCurrentMonth(); 
	if(mm < 5) ret = "Summer";
	else if(mm < 9) ret = "Fall/Winter";
	else ret = "Winter/Spring";
	return ret;
    }
    public final static String getRelatedSeason(String date){
	String ret = "";
	int mm = getDatePart(date, "month"); 
	if(mm < 5) ret = "Winter/Spring";
	else if(mm < 9) ret = "Summer";
	else ret = "Fall/Winter";
	return ret;
    }
    public final static String getNextSeason(String date){
	String ret = "";
	int mm = getDatePart(date, "month"); 
	if(mm < 5) ret = "Summer";
	else if(mm < 9) ret = "Fall/Winter";
	else ret = "Winter/Spring";
	return ret;
    }
    public final static int getNextSeasonYear(String date){
	int ret = 0;
	int yy = getDatePart(date, "year");
	ret = yy;
	int mm = getDatePart(date, "month"); 
	if(mm >= 9) ret = yy+1; 
	return ret;
    }				
    /**
     *
     */
    public final static int getNextSeasonYear(){
	int ret = 0;
	int yy = getCurrentYear();
	ret = yy;
	int mm = getCurrentMonth(); 
	if(mm >= 9) ret = yy+1; // next year
	return ret;
    }		
    /**
     * Disconnect the database.
     */
    public final static void databaseDisconnect(Connection con, Statement stmt, 
					 ResultSet rs){

	try {
	    if(rs != null) rs.close();
	    rs = null;
	    if(stmt != null) stmt.close();
	    stmt = null;
	    if(con != null) con.close();
	    con = null;
	    logger.debug("closed connection "+c_con);
	    c_con--;
	}
	catch (Exception e) {
	    //  e.printStackTrace();
	    // logger.error(e); // ignore 
	}
	finally{
	    if (rs != null) {
		try { rs.close(); } 
		catch (
		       SQLException e) { ; }
		rs = null;
	    }
	    if (stmt != null) {
		try { stmt.close(); } 
		catch (SQLException e) { ; }
		stmt = null;
	    }
	    if (con != null) {
		try { con.close(); } 
		catch (SQLException e) { ; }
		con = null;
	    }
	}
    }
    public final static void databaseDisconnect(Connection con,
					 PreparedStatement stmt, 
					 ResultSet rs){

	try {
	    if(rs != null) rs.close();
	    rs = null;
	    if(stmt != null) stmt.close();
	    stmt = null;
	    if(con != null) con.close();
	    con = null;
	    logger.debug("closed connection "+c_con);
	    c_con--;
	}
	catch (Exception e) {
	    //  e.printStackTrace();
	    // logger.error(e); // ignore 
	}
	finally{
	    if (rs != null) {
		try { rs.close(); } 
		catch (
		       SQLException e) { ; }
		rs = null;
	    }
	    if (stmt != null) {
		try { stmt.close(); } 
		catch (SQLException e) { ; }
		stmt = null;
	    }
	    if (con != null) {
		try { con.close(); } 
		catch (SQLException e) { ; }
		con = null;
	    }
	}
    }
	
    public final static String escapeIt(String s) {

	StringBuffer safe = new StringBuffer(s);
	int len = s.length();
	int c = 0;
	boolean noSlashBefore = true;
	while (c < len) {                           
	    if ((safe.charAt(c) == '\'' ||
		 safe.charAt(c) == '"' 
		 ) && noSlashBefore){
		safe.insert(c, '\\');
		c += 2;
		len = safe.length();
	    }
	    else if(safe.charAt(c) == '\\'){
		noSlashBefore = false;
		c++;
	    }
	    else {
		noSlashBefore = true;	
		c++;
	    }
	}
	return safe.toString();
    }
    /**
     * Translate season and year to start date and end date
     *
     * in certain searches we need to translate season and year to a
     * date so that it can be used in objects that have date but not
     * season and year such Marketing
     * The seasons used in this code are not standard seasons, but
     * they are assumed as they are considered by parks & recs in their
     * publications
     */
    public final static String[] getStartEndDateFromSeasonYear(String season,
							String year){
	String dateFrom = "", dateTo="";		
	if(season == null ||
	   season.equals("") ||
	   year == null || year.equals("")) return null;
	if(season.equals("Summer")){
	    dateFrom = summerStartDate+year;
	    dateTo = summerEndDate+year;
	}
	else if(season.startsWith("Fall")){ // fall/winter
	    dateFrom = fallWinterStartDate+year;
	    dateTo = fallWinterEndDate+year;
	}
	else if(season.startsWith("Winter")){ // winter/spring
	    dateFrom = winterSpringStartDate+year;
	    dateTo = winterSpringEndDate+year;				
	}
	else if(season.equals("Ongoing")){
	    dateFrom = "01/01/"+year;
	    dateTo = "12/31/"+year;	
	}
	return new String[]{dateFrom,dateTo};
    }

    public final static int[] getPrevYears(){
				
	GregorianCalendar current_cal = new GregorianCalendar();
	int year = current_cal.get(Calendar.YEAR);
	int[] years = new int[8];
	// to include next year as well
	for(int i=-1;i< years.length-1;i++){
	    years[i+1] = year-i;
	}
	return years;
    }	

    public final static int[] getFutureYears(){
				
	GregorianCalendar current_cal = new GregorianCalendar();
	int year = current_cal.get(Calendar.YEAR);
				
	int[] years = new int[2];
	for(int i=0;i<2;i++){
	    years[i] = year+i;
	}
	return years;
    }
    public final static void writeHistory(PrintWriter out,
				   String title,
				   List<History> history){
	out.println("<table width=\"50%\" border=\"1\">");
	out.println("<caption>"+title+"</caption>");
	out.println("<th>Action</th><th>Action By</th><th>Date & Time</th></tr>");
	for(History one:history){
	    out.println("<tr><td>"+one.getAction()+"</td>");
	    out.println("<td>"+one.getUser()+"</td>");
	    out.println("<td>"+one.getDate_time()+"</td>");
	    out.println("</tr>");
	}
	out.println("</table>");
    }
    public final static void writeProgramNotes(PrintWriter out,
					       String title,
					       List<ProgramNote> programNotes,
					       String url){
	out.println("<table width=\"50%\" border=\"1\">");
	out.println("<caption>"+title+"</caption>");
	out.println("<tr><th>ID</th><th>Added By</th><th>Date & Time</th><th>Notes</th></tr>");
	for(ProgramNote one:programNotes){
	    out.println("<tr><td><a href=\""+url+"ProgramNote?id="+one.getId()+"\">"+one.getId()+"</td>");
	    out.println("<td>"+one.getUser()+"</td>");
	    out.println("<td>"+one.getDate_time()+"</td>");
	    out.println("<td>"+one.getNotes()+"</td>");	    
	    out.println("</tr>");
	}
	out.println("</table>");
    }    
    public final static void writeMediaRequests(PrintWriter out,
						String title,
						List<MediaRequest> requests,
						String url){
	out.println("<table width=\"75%\" border=\"1\">");
	out.println("<caption>"+title+"</caption>");
	for(MediaRequest one:requests){
	    out.println("<tr><th>Media Request:</th><td><a href=\""+url+"MediaRequest?id="+one.getId()+"\">"+one.getId()+"</td></tr>");
	    out.println("<tr><th>Request Date:</th><td>"+one.getRequestDate()+"</td></tr>");
	    if(one.hasLead()){
		out.println("<tr><th>lead:</th><td>"+one.getLead()+"</td></tr>");
	    }
	    out.println("<tr><th>Year - Season</th><td>"+one.getRequestYear()+" - "+one.getSeason()+"</td></tr>");	    
	    if(one.hasProgram()){
		out.println("<tr><th>Program:</th><td><a href=\""+url+"Program?id="+one.getProgram_id()+"\">"+one.getProgram().getTitle()+"</a></td></tr>");
	    }
	    else if(one.hasFacility()){
		out.println("<tr><th>Facility:</th><td>"+"<a href=\""+url+"Facility?id="+one.getFacility_id()+"\">"+one.getFacility().getName()+"</a></td></tr>");
	    }
	    if(one.hasLocation()){
		out.println("<tr><th>Location:</th><td>"+one.getLocation()+"</td></tr>");
	    }
	    if(one.hasLocationDescription()){
		out.println("<tr><th>Other Location Info:</th><td>"+one.getLocationDescription()+"</td></tr>");
	    }
	    if(one.hasContentSpecific()){
		out.println("<tr><th>Content Specific:</th><td>"+one.getContentSpecific()+"</td></tr>");
	    }
	    out.println("<tr><th>Meda Requested:</th><td>"+one.getRequestTypeStr()+"</td></tr>");
	    if(one.hasOrientation()){
		out.println("<tr><th>Orientation:</th><td>"+one.getOrientation()+"</td></tr>");
	    }
	    if(one.hasOtherType()){
		out.println("<tr><th>Other Meda Type:</th><td>"+one.getOtherType()+"</td></tr>");
	    }
	    if(one.hasNotes()){
		out.println("<tr><th>Notes:</th><td>"+one.getNotes()+"</td></tr>");
	    }
	    out.println("<tr><td colspan=\"2\" align=\"center\"> ------------- </td></tr>");	    
	}
	out.println("</table>");
    }								   
    public final static void writeProgram(PrintWriter out, 
				   Program prog,
				   boolean showTasks, 
				   boolean includePlan,
				   boolean showTrans, //no need for marketing
				   String url){
	out.println("<table width=\"100%\">");
	out.println("<caption>"+prog.getTitle()+"</caption>");
	String str = "", str2="";
	str = prog.getId();
	str2 = "<a href=\""+url+"Program.do?id="+str+"&action=zoom\">"+str+"</a>";
	str = prog.getNoPublish();
	if(str != null && !str.equals("")){
	    str2 += " (Does Not Go in Guide)";
	}				
	writeItem(out, str2, "ID");

	Type area = prog.getArea();
	if(area != null){  // 1
	    writeItem(out, area.getName(), "Area");
	}
	Type cat = prog.getCategory();
	if(cat != null) {  // 2
	    str = cat.getName();
	    str2 = prog.getSubcat();
	    if(!str2.equals("")){
		str = str+"/"+str2;
	    }
	    writeItem(out, str, "Guide Heading");			
	}
	str = prog.getTaxonomyInfo();
	if(str != null && !str.equals("")) {  // 2
	    writeItem(out, str, "Categories");
	}
	if(showTrans){
	    writeItem(out, prog.getNraccount(), "Non-reverting Account");
	    writeItem(out, prog.getFee(), "Transaction Fees $");
	}
	str = prog.getInCityFee();
	writeItem(out, str, "In City Fees $");
	str = prog.getNonCityFee();
	writeItem(out, str, "Non City Fees $");		
	str = prog.getOtherFee();
	writeItem(out, str, "Other Fees $");		
	str = prog.getMemberFee();
	writeItem(out, str, "Member Fees $");		
	str = prog.getNonMemberFee();
	writeItem(out, str, "Non-Member Fees $");		
	Type lead = prog.getLead();
	if(lead != null){
	    writeItem(out, lead.getName(), "Lead Programmer");
	}
	str = prog.getSummary();
	if(!str.equals("")){
	    writeItem(out, str, "Program Summary");		
	}
	writeItem(out, prog.getStatement(), "Brochure Statement");

	if(!prog.getCodeNeed().equals("")){
	    writeItem(out, "yes", "Code Needed");
	    writeItem(out, prog.getCode(), "Code");			
	}
	writeItem(out, prog.getDays(), "Days");
	writeItem(out, prog.getClassCount(), "# of Classes");
	str = prog.getStartEndDate();
	writeItem(out, str, "Start End Dates");	 // Mon dd format
	str = prog.getStartEndTime();
	str2 = prog.getEndTime();
	if(!str2.equals("")){ // we have both
	    writeItem(out, str, "Start End Times");	 
	}
	else{ // just start time or none
	    str = prog.getStartTime();
	    if(!str.equals(""))
		writeItem(out, str, "Start Time");	 
	}
	str = prog.getAgeInfo();
	if(!str.equals("")){
	    writeItem(out, str, "Participant Age");
	}
	str = prog.getPartGrade();
	writeItem(out, str, "Participant Grade");
	str = prog.getMinMaxEnroll();
	str2 = prog.getWaitList();
	if(!(str2.equals("") || str2.equals("0"))){
	    str += " Wait list "+str2;
	}
	writeItem(out, str, "Min-Max Enroll");
	writeItem(out, prog.getRegDeadLine(), "Reg. Deadline");
	Location location = prog.getLocation();
	str = "";
	if(location != null){
	    str = location.getName();
	}
	str2 = prog.getLocation_details().trim();
	if(!str2.equals("")){
	    if(!str.equals(""))
		str += " ("+str2+")";
	    else
		str = str2;
	}
	if(!str.equals(""))								
	    writeItem(out, str, "Location");			
	//
	writeItem(out, prog.getInstructor(), "Instructor");
	writeItem(out, prog.getDescription(), "Description");
	writeItem(out, prog.getOginfo(), "Other Guide Info");
	if(showTasks){
	    str = prog.getTaskInfo();
	    writeItem(out, str, "Completed Tasks");
	}
	out.println("</table>");

    }
    //
    public final static String getToday2(){
	GregorianCalendar cal = new GregorianCalendar(tzone, local);
	String month = ""+(cal.get(Calendar.MONTH) + 1);
	String day = ""+cal.get(Calendar.DATE);
	String year = ""+cal.get(Calendar.YEAR); 
	if(month.length() == 1) month = "0"+month;
	if(day.length() == 1) day = "0"+day;
	String today = month+"/"+day+"/"+year;
	return today;
    }
    public final static String getToday(){
	GregorianCalendar cal = new GregorianCalendar(tzone, local);
	String month = ""+(cal.get(Calendar.MONTH) + 1);
	String day = ""+cal.get(Calendar.DATE);
	String year = ""+cal.get(Calendar.YEAR); 
	String today = 
	    month+" - "+ day + " - " + year;
	return today;
    }    
    public final static int getCurrentMonth(){
	GregorianCalendar current_cal = new GregorianCalendar(tzone, local);
	int  mm = current_cal.get(Calendar.MONTH)+1;
	return mm;
    }
    public final static int getCurrentYear(){
	GregorianCalendar current_cal = new GregorianCalendar(tzone, local);
	int  yy = current_cal.get(Calendar.YEAR);
	return yy;
    }
    //
    
    public final static int getDatePart(String date, String part){
	int ret = 0;
	if(date == null || part == null) return ret;
	try{		
	    if(part.equals("month")){
		ret = Integer.parseInt(date.substring(0,date.indexOf("/")));
	    }
	    else if(part.equals("day")){
		ret = Integer.parseInt(date.substring(date.indexOf("/")+1,date.lastIndexOf("/")));
	    }
	    else{ // year
		ret = Integer.parseInt(date.substring(date.lastIndexOf("/")+1));
	    }
	}catch(Exception ex){
	}		
	return ret;
    }
    //
    // sessions
    //
    public final static void writeSessions(PrintWriter out,
				    SessionList list,
				    SessionOpt sopt,
				    String id,									
				    String url,
				    boolean debug){
	if(list.size() > 0){
	    out.println("<center><table border=\"1\">");
	    out.println("<caption>Sessions ("+list.size()+")</caption>");
	    out.println("<tr>");
	    for(int jj=0;jj<sopt.getShowSize();jj++){
		if(sopt.show(jj)){
		    out.println("<th><b>"+sopt.getTitle(jj)+"</b></th>");
		}
	    }
	    out.println("</tr>");
	    for(Session se:list){
		out.println("<tr>");				
		String str = se.getSid();
		out.println("<td><a href="+url+
			    "Sessions?"+
			    "action=zoom&id="+id+
			    "&sid="+str+">"+ str +
			    "</a></td>");
		if(sopt.show(1)){
		    str = se.getCode();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(2)){
		    str = se.getDays();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(3)){
		    str = se.getStartEndDate();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(4)){
		    str = se.getStartEndTime();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(5)){
		    str = se.getRegDeadLine();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(6)){
		    str = se.getInCityFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;
		    out.println("<td align=\"right\">"+str+"</td>");
		}
		if(sopt.show(7)){
		    str = se.getNonCityFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;					
		    out.println("<td align=\"right\">"+str+"</td>");
		}
		if(sopt.show(8)){
		    str = se.getOtherFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;					
		    out.println("<td align=\"right\">"+str+"</td>");
		}
		if(sopt.show(9)){
		    str = se.getMemberFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;
		    out.println("<td align=\"right\">"+str+"</td>");
		}				
		if(sopt.show(10)){
		    str = se.getNonMemberFee();
		    if(str.equals(""))
			str = "&nbsp;";
		    else
			str = "$"+str;					
		    out.println("<td align=\"right\">"+str+"</td>");
		}					
		if(sopt.show(11)){
		    Location loc = se.getLocation();
		    if(loc != null)
			str = loc.getName();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(12)){
		    //
		    str = se.getAgeInfo();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(13)){
		    str = se.getPartGrade();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(14)){
		    str = se.getMinMaxEnroll();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(15)){
		    str = se.getClassCount();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(16)){
		    str = se.getDescription();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(sopt.show(17)){
		    str = se.getInstructor();
		    if(str.equals(""))
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		out.println("</tr>");
	    }
	    out.println("</table></center>");
	}
		

    }
	
    public final static void writeSponsors(PrintWriter out, 
				    List<Sponsor> slist, 
				    String url,
				    boolean debug){
	out.println("<center><h3>Sponsorships</h3></center>");
	for(Sponsor spon:slist){
	    out.println("<table width=\"100%\">");
	    out.println("<caption>Sponsorship: "+
			"<a href=\""+url+"Sponsor.do?id="+spon.getId()+"&action=zoom\">"+spon.getId()+"</a></caption>");
	    out.println("<tr><td align=\"right\"><b>Program ID:</b> "+
			"</td><td align=\"left\"><a href=\""+url+"Program.do?action=zoom&id="+spon.getPid()+"\">"+spon.getPid()+"</a></td></tr>");			
	    out.println("<tr><td align=\"right\"><b>Attendance:</b> "+
			"</td><td align=\"left\">"+spon.getAttendCount()+"</td></tr>");
	    if(!spon.getMarket().equals("")){			
		out.println("<tr><td align=\"right\"><b>Target Market:</b> "+
			    "</td><td align=\"left\">"+spon.getMarket()+"</td></tr>");
	    }
	    out.println("<tr><td align=\"right\"><b>Request Category:</b> "+
			"</td><td align=\"left\">"+spon.getCategories()+"</td></tr>");
	    out.println("<tr><td align=\"right\"><b>Sponsor Benefits:</b> "+
			"</td><td align=\"left\">"+spon.getBenefits()+"</td></tr>");
	    if(!spon.getComments().equals("")){
		out.println("<tr><td align=\"right\" valign=\"top\"><b>Ideas:</b> "+
			    "</td><td align=\"left\">"+spon.getComments()+"</td></tr>");
	    }
	    out.println("</table>");
	}
    }
    //
    public final static void writeInclusive(PrintWriter out,
				     Inclusion inc,
				     String url){
	String str = "";
	String consult_pro = "<li>I need a Programming Consultation to focus on accessibility/usability of programs and activities to community members with disabilities  </li>";
	String consult_ada="<li>I want/need an Accessibility/ADA Consultation to focus on physical access to facility, park or other site</li>";
	String training = "<li>I need Staff Training on: </li>";
	String train[] = 
	    {"<li>Disability Awareness and customer service</li>",
	     "<li>Inclusion the Basics</li>",
	     "<li>Program Considerations for Inclusion</li>",
	     "<li>Behavior Management</li>",
	     "<li>Accessibility considerations when planning field trips</li>"
	    };
	String train_other="Other Training";
	String market = "<li>I would like this Marketed to Disability Groups/Populations.</li>";
	String site_visit = "<li>I would like a site visit/walk through - If the program site has already been determined, this would provide information on any simple maintenance necessary to increase access to individuals with disabilities and identify any removable barriers.</li>";
	String consult = "<li>I need a Programming Consultation to focus on accessibility/usability of programs and activities to community members with disabilities.</li>";
	String sign= "<li>I want a sign Language Interpreter.</li>";
	String prov_sign = "<li>I need to know if I should provide a sign language interpreter.</li>";
	//
	out.println("<table>"); 
	out.println("<caption>Inclusive Recreation </caption>");
	out.println("<tr><td>Inclusive Page:");
	out.println("<a href="+url+
		    "Inclusive?&action=zoom&id="+inc.getId()+
		    "> "+inc.getId()+"</a></td></tr>");
	out.println("<tr><td><ul>");
	str = inc.getConsult_pro();
	if(!str.equals("")){
	    out.println(consult_pro);
	}		
	str = inc.getConsult_ada();
	if(!str.equals("")){
	    out.println(consult_ada);
	}
	String train_info = "";		
	if(!inc.getTraining().equals("")){
	    if(!inc.getTrain_aware().equals("")){
		train_info += train[0];
	    }
	    if(!inc.getTrain_basics().equals("")){
		train_info += train[1];
	    }
	    if(!inc.getTrain_consider().equals("")){
		train_info += train[2];
	    }
	    if(!inc.getTrain_behave().equals("")){
		train_info += train[3];
	    }
	    if(!inc.getTrain_trip().equals("")){
		train_info += train[4];
	    }			
	}
	if(!train_info.equals("")){
	    out.println(training);
	    out.println("<ul>");
	    out.println(train_info);
	    str = inc.getTrain_other();
	    if(!str.equals("")){
		out.println("<li>"+str+"</li>");
	    }
	    out.println("</ul>");
	}
	else{
	    if(!inc.getTrain_other().equals("")){
		out.println(training);
		out.println("<ul>");				
		out.println("<li>"+inc.getTrain_other()+"</li>");
		out.println("</ul>");
	    }
	}
	if(!inc.getMarket().equals("")){
	    out.println(market); 
	}
	if(!inc.getSite_visit().equals("")){
	    out.println(site_visit); 
	}
	if(!inc.getConsult().equals("")){
	    out.println(consult); 
	}
	if(!inc.getSign().equals("")){
	    out.println(sign); 
	}
	if(!inc.getProv_sign().equals("")){
	    out.println(prov_sign); 
	}
	if(!inc.getComments().equals("")){
	    out.println("<li>"+inc.getComments()+"</li>"); 
	}
	out.println("</ul></td></tr>");
	out.println("<br>");
	out.flush(); 
    }

    public final static String formatNumber(double dd){
	return formatNumber(""+dd);
    }
    /**
     * Put a number in 2 decimal places format.
     * @param str the number to be formated
     */
    public final static String replaceSpecialChars(String s) {
	char ch[] ={'\'','\"','>','<'};
	String entity[] = {"&#39;","&#34;","&gt;","&lt;"};
	//
	// &#34; = &quot;

	String ret ="";
	int len = s.length();
	int c = 0;
	boolean in = false;
	while (c < len) {             
	    for(int i=0;i< entity.length;i++){
		if (s.charAt(c) == ch[i]) {
		    ret+= entity[i];
		    in = true;
		}
	    }
	    if(!in){
		if(s.charAt(c) == '\n') ret+=" "; // with space
		else if(s.charAt(c) == '\r') ret+=" "; // with space
		else ret += s.charAt(c);
	    }
	    in = false;
	    c ++;
	}
	return ret;
    }	
    public final static String formatNumber(String str){

	String ret="";
	if(str.equals("")) return ret;

	int l = str.length();
	int i = str.indexOf('.');
	int r = i+3;  // required length to keep only two decimal
	if(i > -1 && r<l){
	    ret = str.substring(0,r);
	}
	else if(i > -1 && r>l){
	    ret = str+"0";
	}
	else if(i > -1 && r==l){
	    ret = str;
	}
	else{
	    ret = str+".00";
	}
	return ret;
    }
	
    public final static void writeItem(PrintWriter out, String that, String title){
	if(that == null || that.equals("")) return;
	String title2 = title;
	String that2 = that;
	if(title.indexOf("$") > 0){
	    title2 = title.substring(0,title.indexOf("$")-1);
	    that2 = "$"+that;
	}
	if(that.length() > 80){
	    out.println("<tr><td align=\"right\" valign=\"top\" width=\"30%\"><b>");
	}
	else{
	    out.println("<tr><td align=\"right\" width=\"30%\"><b>");
	}
	out.println(title2+":&nbsp;</b></td><td align=\"left\">");
	out.println(that2+"</td></tr>");

    }

    /**
     * Extracts the budget info and write it to the output stream in a table
     * form.
     * @param out the PrintWriter
     */

    // transform on non alpha/numeric to asscii code 
    // 
    // ASCII code for some of the symbols
    // space=32, &=38, 
    // we do not change the '&' and '+' since they have certain meaning 
    // in html
    public final static String encodeString(String str){
	char sym[]= {'!','\'',  // single quote
	    '#','>',
	    '_','=','?','"','@',
	    '*','^','(',')',',',
	    '.',';',':','-','<','/','&'}; // apace = 32
	String[] code={"33","39",
	    "35","62",
	    "95","61","63","34","64",
	    "42","94","123","124","44",
	    "46","59","58","45","60","47","38"};
	String[] codeh={"21","27",
	    "23","3E",
	    "5F","3D","3F","22","40",
	    "2A","5E","7B","7C","2C",
	    "2E","3A","3A","2D","3C","2F","26"};
	String str2=str;
	int i=0,l,v,jj=-1;
	if(str == null) return str2;
	str2="";
	l = str.length();
	Character b = new Character('b'); // temporary
	if(l > 0){
	    while(i < l){

		char c = str.charAt(i);
		if(b.isLetterOrDigit(c)) str2 += c;
		else{
		    jj=-1;
		    for(int j=0;j<sym.length; j++) 
			if(c == sym[j]){
			    jj = j;
			    break;
			}
		    if(jj>-1){
			str2 += "%"+codeh[jj];
		    }
		    else str2 += c; // in case not in the list
		}
		i++;
	    }
	}
	return str2;
    }
    /** 
     * Writes the first page.
     * @param out the output stream
     * @param reportTitle the title
     * @param season the season
     * @param pyear the year
     */
    public final static void writeFirstPage(PrintWriter out,
				     String reportTitle,
				     String pyear,
				     String season){
	out.println("<br /><center><font size=\"+1\">"+reportTitle+"</font> ");
	if(season != null && !season.isEmpty()){
	    out.println("<b> ("+season+"/"+pyear+")</b><br />");
	}
	out.println("<hr width=\"75%\"><br /></center>");
    }
    public final static void writeFirstPage(PrintWriter out,
				     String reportTitle,
				     String lead,
				     String pyear,
				     String season){
	out.println("<center><font size=+2>Parks and Recreation</font><br>");
	out.println("<font size=\"+1\">"+reportTitle+"</font> ");
	out.println("<b> ("+season+"/"+pyear+")<br />");
	out.println(" (By "+lead+")</b><br />");
	out.println("<hr width=\"75%\"><br /></center>");
    }	
    /**
     * Issue an error message.
     *
     * @param out the output stream
     * @param titl the message title
     * @param message
     */
    public final static void sendMessage(PrintWriter out, String titl, String message){
	out.println("<br /><center><b>"+titl+"</b>");
	out.println("<br /><br/>"+message);
	out.println("</center>");	
    }
    /**
     *
     */
    public final static void writeEvaluation(PrintWriter out, Evaluation eval, boolean debug){
	Program prog = eval.getProgram();
	Plan plan = null;
	Market market = null;
	String goals = "";
	List<Objective> objectives = null;
	List<Staff> staffs = null;
	if(prog != null){
	    plan = prog.getPlan();
	    if(prog.hasMarket()){
		market = prog.getMarket();
	    }
	}
	if(plan != null){
	    objectives = plan.getObjectives();
	    staffs = plan.getStaffs();
	    goals = plan.getGoals();
	}
	out.println("<center>");
	out.println("<table width=90% border>");	
	out.println("<caption>");
	if(prog != null){
	    out.println(prog.getTitle()+" ("+prog.getSeasons()+" /"+prog.getYear());
	}
	out.println("</caption>");
	//
	out.println("<tr><td colspan=2 align=center "+
		    "><h3>"+
		    "Program Objectives </h3></td></tr>");
	out.println("<tr><td><b>Program Goals</b></td><td>"+goals+"</td></tr>");
	//
	// Program Objective (purpose)		

	out.println("<tr><td><b>Objectives</b></td><td><b>Outcomes</b></td></tr>");
	if(eval.hasRecord()){
	    List<Outcome> outcomes = eval.getOutcomes();
	    if(outcomes != null && outcomes.size() > 0){
		int jj=1;
		for(Outcome one:outcomes){				
		    out.println("<tr><td valign=top>"+jj+" - "+one.getObjective().getObjective()+"</td><td>");
		    out.print(one.getOutcome());
		    out.println("</td></tr>");
		}
	    }
	}
	out.println("<tr><td colspan=\"2\">&nbsp;</td></tr>");
	//
	// Profit Objective
	if(!plan.getProfit_obj().equals("")){
	    out.println("<tr><td align=right valign=top><b>Profit "+
			"Objective: ");
	    out.println("</b></td><td valign=top>");
	    out.println(plan.getProfit_obj());
	    out.println("</td></tr>");
	}
	//
	// Target Market
	if(!plan.getMarket().equals("")){
	    out.println("<tr><td align=right valign=top><b>Target Market: ");
	    out.println("</b></td><td valign=top>");
	    out.println(plan.getMarket());
	    out.println("</td></tr>");
	}
	out.println("<tr><td colspan=\"2\">&nbsp;</td></tr>");
	//
	out.println("<tr><td></td><td>"+
		    "Planned Min/Max</td><td>Actual #</td></tr>");
	out.println("<tr><td>Attendance</td><td>&nbsp; ");
	out.println(plan.getMin_max());
	out.println("</td><td>");
	out.println(eval.getAttendance());
	out.println("</td></tr></table></td></tr>");
	String str = eval.getLife_cycle();
	if(!str.equals("")){
	    out.println("<tr><td align=\"right\"><b>Life Cycle:</b></td><td>");
	    out.println(str);
	    out.println("</td></tr>");
	}
	str = eval.getLife_cycle_info();
	if(!str.equals("")){
	    out.println("<tr><td align=\"right\" valign=\"top\"><b>Life Cycle Comments:</b></td><td>"+str);
	    out.println("</td></tr>");
	}
	out.println("</table>");
	//
	// staff consideration
	out.println("<table><caption>Staff Consideration </caption>");
	out.println("<tr><th>Staff</th><th>Planned </th><th>"+
		    "Actual</th></tr>");
	if(eval.hasRecord()){
	    int jj=1;
	    List<EvalStaff> estaffs = eval.getEvalStaffs();
	    if(estaffs != null && estaffs.size() >  0){
		for(EvalStaff one:estaffs){
		    Staff staff = one.getStaff();
		    out.println("<tr><td>"+(jj++)+" - "+staff.getStaff_type().getName()+"</td>");
		    out.println("<td>"+staff.getQuantity()+"</td>");
		    out.println("<td>"+one.getQuantity()+"</td></tr>");
		}
	    }
	}
	out.println("</table>");

	//
	// staff assignments
	if(!eval.getAssignment().equals("")){
	    out.println("<b>Staff Assignments:</b>");
	    out.print(eval.getAssignment());
	    out.println("<br />");
	}
	//
	// partnerships
	if(!eval.getPartnership().equals("")){
	    out.println("<b>Partnerships:<b>");
	    out.print(eval.getPartnership());
	    out.println("<br />");
	}
	//
	// sponsors
	if(!eval.getSponsorship().equals("")){
	    out.println("<b>Sponsorships:<b>");
	    out.print(eval.getSponsorship());
	    out.println("<br />");
	}
	if(!eval.getRecommend().equals("")){
	    out.println("<b>General&nbsp"+
			"Recommendations:</b>");
	    out.print(eval.getRecommend());
	    out.println("<br />");
	}
	if(!eval.getFlier_points().equals("")){
	    out.println("<b>Flyer "+
			"Distribution Points:</b> ");
	    out.print(eval.getFlier_points());
	    out.println("<br />");
	}
	//
	// Other
	if(!eval.getOther().equals("")){
	    out.println("<b>Other:</b>");
	    out.print(eval.getOther());
	    out.println("<br />");
	}
	//
	if(market != null){
	    writeMarket(out, market, null);
	}
	else{
	    out.println("<b>No Marketing Info Available<b>");
	}
	Budget budget = new Budget(debug, prog.getId());
	budget.doSelect();
	if(budget.hasRecord()){
	    writeEvalBudget(out, budget);
	}
	out.println("<b>Evaluation Prepared by:</b>");
	out.println(eval.getWby());
	out.println(" on <b> Date:</b>");
	out.println(eval.getDate());
	out.println("<br /><br />"); 

    }
    public final static void writeEvalBudget(PrintWriter out, Budget budget){

	String twoCells	= "<td>&nbsp;</td><td>&nbsp;</td></tr>";	
	String threeCells = "<td>&nbsp;</td>"+twoCells;
	String fourCells = "<td>&nbsp;</td>"+threeCells;
	String row = "<tr><td colspan=\"4\">&nbsp;</td>"+threeCells;
	Program prog = budget.getProgram();
	//Market market = prog.getMarket();
	Plan plan = prog.getPlan();
	//
	out.println("<table width=\"100%\" border=\"1\">");
	out.println("<caption>Budget Evaluation</caption>");
	//
	// actual Revenue
	out.println("<tr><td colspan=\"7\" align=\"center\">"+
		    "<h3>Budget</h3></td></tr>");
	if(plan != null){
	    String goals = plan.getGoals();
	    if(goals != null && !goals.equals("")){
		out.println("<tr><td colspan=\"7\" align=\"left\">");
		out.println("<b>Program Goals:<b> ");		
		out.println(goals);
		out.println("</td></tr>");								
	    }
	}		
	//
	out.println("<tr><td colspan=\"4\">&nbsp;</td><td>Revenue $</td>"+
		    "<td>Expenses $</td><td>&nbsp;</td></tr>");
	out.println("<tr><td><b>Attendance</b></td><td colspan=\"3\" align=\"left\">"+budget.getAttendance()+"</td>"+threeCells);
		
	out.println("<tr><td colspan=\"4\"><b>Sponsorship </b></td><td align=\"right\">");
	out.println(Helper.formatNumber(budget.getSponsorship())+"</td><td> &nbsp;</td><td>&nbsp;");
	if(budget.isSponsorshipRevenue()){
	    out.println("Considered Revenue");
	}
	out.println("</td></tr>");
	out.println("<tr><td colspan=\"4\"><b>Donations </b></td><td align=\"right\">");
	out.println(Helper.formatNumber(budget.getDonation())+"</td>"+twoCells);
	//
	out.println("<tr><td colspan=\"4\">Itemized fees</td>"+threeCells);
	out.println(row);
	out.println("<tr><td>Fees </td><td>Rate</td><td>Number</td>"+fourCells);
	FeeItemList fees = budget.getFees();
	//
	if(fees != null){
	    for(FeeItem one:fees){
		out.println("<tr><td>");				
		out.println("<b>"+one.getFee_type()+"</b></td><td>"+one.getRate()+"</td><td>"+one.getQuantity()+"</td><td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(one.getTotal())+"</td>");
		out.println(twoCells);
	    }
	}
	//
	// Total Revenue
	out.println(row);
	out.println("<tr><td colspan=\"4\"><b>Total Revenue: </b></td>");
	out.println("<td align=\"right\">"+Helper.formatNumber(budget.getTotalRevenue())+"</td>"+twoCells);
	out.println(row);		
	//
	// Direct Expenses
	//
	// Note: Full time staff includes the manager, coordinator, specialist
	// the rest are considered part time
	// The full time costs go into the indirect costs
	//		
	out.println("<tr><td colspan=\"4\"><b>Direct Staff Expenses</b></td>");
	out.println(threeCells);
	out.println(row);		
	out.println("<tr><td>Staff </td><td>Number</td><td>Rate</td><td>Hours</td>"+threeCells);
	StaffExpenseList staffExps = budget.getStaffExpenses();
	if(staffExps != null && staffExps.size() > 0){
	    for(StaffExpense one:staffExps){
		if(one.isDirect()){
		    out.println("<tr><td>");	
		    out.println(one.getStaff_type()+"</td><td>"+one.getQuantity()+"</td><td>"+one.getRate()+"</td><td>"+one.getHours()+"</td><td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(one.getTotal())+"</td><td>&nbsp;</td></tr>");
		}
	    }
	}
	out.println(row);
	DirectExpenseList otherDirList = budget.getOtherDirectExpenses();
	int jj = 1;
	if(otherDirList != null){
	    out.println("<tr><td colspan=\"4\"><b>Other Direct Expenses:</b></td>"+threeCells);			
	    for(DirectExpense one:otherDirList){
		out.println("<tr><td>&nbsp;</td><td colspan=\"3\" align=\"left\">"+(jj++)+" - "+one+"</td><td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(one.getExpenses())+"</td><td>&nbsp;</td></tr>");
	    }
	}		
	out.println("<tr><td colspan=\"4\"><b>Marketing Direct Expenses:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getMarketDirectExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println("<tr><td colspan=\"4\"><b>Total Direct Expenses:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getTotalDirectExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println(row);		
	//
	// Indirect Expenses
	//
	out.println("<tr><td colspan=\"4\"><b>Indirect Expenses </b></td>");
	out.println(threeCells);
	out.println(row);		
	//
	out.println("<tr><td colspan=\"4\"><b>Indirect Staff Expenses</b></td>");
	out.println(threeCells);
	out.println("<tr><td>Staff </td><td>Number</td><td>Rate</td><td>Hours</td>"+threeCells);
	if(staffExps != null && staffExps.size() > 0){
	    for(StaffExpense one:staffExps){
		if(!one.isDirect()){
		    out.println("<tr><td>");						
		    out.println(one.getStaff_type()+"</td><td>"+one.getQuantity()+"</td><td>"+one.getRate()+"</td><td>"+one.getHours()+"</td><td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(one.getTotal())+"</td><td>&nbsp;</td></tr>");
		}
	    }
	}
	out.println(row);		
	out.println("<tr><td colspan=\"4\"><b>Marketing Indirect Expenses:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getMarketIndirectExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println("<tr><td colspan=\"4\"><b>Indirect Expenses Subtotal:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getSubTotalIndirectExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println(row);				
	out.println("<tr><td colspan=\"4\"><b>Administration Fees </b></td>");
	out.println("<td>&nbsp;</td>");
	out.println("<td align=\"right\">"+Helper.formatNumber(budget.getAdmin_fees())+"</td>");
	out.println("<td>"+budget.getAdmin_percent()+"%</td></tr>");
	out.println(row);
	out.println("<tr><td colspan=\"4\"><b>TOTAL INDIRECT EXPENSES:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getTotalIndirectExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println(row);
	//
	out.println("<tr><td>&nbsp;</td><td colspan=\"3\"><b>TOTAL EXPENSES:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getTotalExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println("<tr><td>&nbsp;</td><td colspan=\"2\"><b>Direct:</b></td>");
	out.println("<td align=\"right\">$"+Helper.formatNumber(budget.getTotalDirectExpenses())+"</td>"+threeCells);
	out.println("<tr><td>&nbsp;</td><td colspan=\"2\"><b>Indirect:</b></td>");
	out.println("<td align=\"right\">$"+Helper.formatNumber(budget.getTotalIndirectExpenses())+"</td>"+threeCells);
	out.println(row);
	out.println("<tr><td>&nbsp;</td><td colspan=\"3\"><b>COST RECOVERY:</b></td>"+threeCells);
	out.println(row);
	out.println("<tr><td colspan=\"3\"><b>COST RECOVERY LEVEL (100%-((TE-TR)/TE)100%):</b></td><td>"+Helper.formatNumber(budget.getRecoveryLevel())+"%</td>"+threeCells);
	out.println(row);
	out.println("<tr><td colspan=\"3\" align=\"right\">Total Expenses (TE):</td><td align=\"right\">$"+Helper.formatNumber(budget.getTotalExpenses())+"</td>"+threeCells);
	out.println("<tr><td colspan=\"3\" align=\"right\">Total Revenue (TR):</td><td align=\"right\">$"+Helper.formatNumber(budget.getTotalRevenue())+"</td>"+threeCells);
	out.println("<tr><td colspan=\"3\" align=\"right\">Unrecovered Expenses (UE):</td><td align=\"right\">$"+Helper.formatNumber(budget.getUnrecoveredExpenses())+"</td>"+threeCells);
	out.println(row);
	out.println("<tr><td colspan=\"3\" align=\"right\">Cost per participant (UE/attendance):</td><td align=\"right\">$"+Helper.formatNumber(budget.getCostPerParticipant())+"</td>"+threeCells);
	out.println(row);
	//
	out.println("<tr><td colspan=\"3\"><b>DIRECT COST RECOVERY LEVEL (100%-((TDE-TR)/TDE)100%):</b></td><td>"+Helper.formatNumber(budget.getDirectRecoveryLevel())+"%</td>"+threeCells);
	out.println(row);
	out.println("<tr><td colspan=\"3\" align=\"right\">Total Direct Expenses (TDE):</td><td align=\"right\">$"+Helper.formatNumber(budget.getTotalDirectExpenses())+"</td>"+threeCells);
	out.println("<tr><td colspan=\"3\" align=\"right\">Total Revenue (TR):</td><td align=\"right\">$"+Helper.formatNumber(budget.getTotalRevenue())+"</td>"+threeCells);
	out.println("<tr><td colspan=\"3\" align=\"right\">Unrecovered Direct Expenses (UDE):</td><td align=\"right\">$"+Helper.formatNumber(budget.getUnrecoveredDirectExpenses())+"</td>"+threeCells);
	out.println(row);		
	out.println("<tr><td colspan=\"3\" align=\"right\">Direct Cost per participant (UDE/attendance):</td><td align=\"right\">$"+Helper.formatNumber(budget.getDirectCostPerParticipant())+"</td>"+threeCells);
	out.println(row);		
	//
	if(!budget.getNotes().equals("")){
	    out.println("<tr><td colspan=\"7\"><b>Notes</b>"+
			"</td></tr>");
	    out.println("<tr><td colspan=\"7\">");		
	    out.print(budget.getNotes());
	    out.println("</td></tr>");
	}
	//
	out.println("<tr><td colspan=\"4\" align=\"left\"><b>Budget Prepared By</b>"+
		    "</td><td colspan=\"3\"><b>Date</b></td></tr>");
	out.println("<tr><td colspan=\"4\" align=\"left\">");
	out.println(budget.getWby()+"</td>");
	out.println("<td colspan=\"3\" align=\"left\">");
	out.println(budget.getDate()+"</td></tr>");		
	out.println("</table>");

    }
    public final static void writeMarket(PrintWriter out, Market market, String url){
	//
	String str = market.getId(); 
	if(url != null)
	    str = "<a href=\""+url+"Market.do?id="+market.getId()+"&action=zoom\">"+market.getId()+"</a>";
	String season = market.getSeason();
	String year = market.getYear();
	if(!season.equals("")){
	    str += " ("+season+"/"+year+")";
	}
	out.println("<center><h3>Marketing "+str+"</h3></center>");
	//
	// Program Ad
	//
	float totalDirect = 0f, totalIndirect = 0f;		
	int j=1;
	List<MarketAd> ads = market.getAds();
	Set<String> adSet = new HashSet<String>();
	if(ads != null && ads.size() > 0){
	    out.println("<table border=\"1\"><caption>Advertisements</caption>");
	    out.println("<tr><th>&nbsp;</th><th>Ad type</th><th>Expenses $</th><th>Account Type</th><th>Due Date</th><th>Details</th></tr>");
	    for(MarketAd one:ads){
		out.println("<tr><td>"+(j++)+"</td>");
		out.println("<td>"+one.getType().getName()+"</td>");
		out.println("<td align=\"right\">$"+Helper.formatNumber(one.getExpenses())+"</td>");
		if(one.isDirect()){
		    totalDirect += one.getExpenses();
		    out.println("<td>Direct</td></td>");
		}
		else{
		    totalIndirect += one.getExpenses();
		    out.println("<td>Indirect</td>");
		}
		out.println("<td>&nbsp;"+one.getDue_date()+"</td>");
		out.println("<td>&nbsp;"+one.getDetails()+"</td>");				
		out.println("</tr>");
	    }
	    out.println("</table>");
	}
	if(!market.getOther_ad().isEmpty()){
	    out.println("<b>Other Ad:</b>");
	    out.println(market.getOther_ad()+"<br />");
	}
	//
	//
	// table with 3 columns
	List<MarketItem> items = market.getItems();
	j=1;
	Set<String> hashSet = new HashSet<String>();
	if(items != null && items.size() > 0){
	    // marketing items
	    out.println("<table border=\"1\"><caption>Marketing Pieces:</caption>");
	    out.println("<tr><th>&nbsp;</th><th>Type</th><th>Quantity</th>"+
			"<th>Expenses</th><th>Direct</td><th>Indirect</th>"+
			"<th>Due Date</th><th>Details</th></tr>");
			
	    for(MarketItem one:items){
		out.println("<tr><td>"+(j++)+"</td>");
		hashSet.add(one.getType_id());
		out.println("<td>"+one.getType().getName()+"</td>");
		out.println("<td>"+one.getQuantity()+"</td>");
		out.println("<td>$"+one.getExpenses()+"</td>");
		if(one.isDirect()){
		    out.println("<td>Direct</td><td>&nbsp;</td>");
		    totalDirect += one.getExpenses();
		}
		else{
		    out.println("<td>&nbsp;</td><td>Indirect</td>");
		    totalIndirect += one.getExpenses();
		}
		out.println("<td>&nbsp;"+one.getDue_date()+"</td>");
		out.println("<td>&nbsp;"+one.getDetails()+"</td>");				
		out.println("</tr>");
	    }
	    if(totalDirect > 0 || totalIndirect > 0){
		out.println("<tr><td colspan=\"4\" align=\"right\"><b>Total Expenses:</b>(<font color=\"green\" size=\"-1\">(These will be carried on to evaluation page)</font></td><td align=\"right\">$"+totalDirect+"</td><td align=\"right\">$"+totalIndirect+"</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
	    }
	    out.println("</table>");				
	}
	//
	if(!market.getClass_list().isEmpty()){
	    out.println("<b>Does the marketing piece combine classes or programs?</b>");
	    out.print(market.getClass_list());
	    out.println("<br />");
	}
	if(!market.getOther_market().isEmpty()){
	    out.println("<b>Other Marketing:</b>");
	    out.print(market.getOther_market());
	    out.println("<br />");
	}
	//
	// Announcement
	//
	List<Type> announces = market.getAnnounces();
	j=1;
	if(announces != null && announces.size() > 0){
	    out.println("<b>Announcements:</b>");			
	    boolean inn = false;
	    for(Type one:announces){
		if(inn) out.print(", ");
		out.print((j++)+" - "+one);
		inn = true;
	    }
	    out.println("<br />");
	}
	//		
	// Special Instruction
	if(!market.getSpInstructions().isEmpty()){
	    out.println("<b>Special Instructions:</b>");
	    out.println(market.getSpInstructions());
	    out.println("<br />");
	}
	if(!market.getSignBoard().isEmpty()){
	    out.println("<b>Digital Sign Board is Needed: Yes, </b>");
	    out.println("<b>Needed Date </b>"+market.getSignBoardDate()+"<br />");	    
	}
	out.println("<br />");

    }
    public static String cleanText2(String str){
	String str3 = "";
	if(str != null && !str.isEmpty()){
	    String str2 = removeTags(str);	    
	    str3 = cleanText(str2);
	}
	return str3;
    }
    public static String removeTags(String string) {

	if (string == null || string.length() == 0) {
	    return string;
	}

	Matcher m = REMOVE_TAGS.matcher(string);
	return m.replaceAll("");
    }
    public final static String writeMediaRequestCsv(MediaRequest one){
	String line="";
	String pr_title = "", fc_name = "";
	if(one.getProgram() != null){
	    pr_title = one.getProgram().getTitle();
	}
	if(one.getFacility() != null){
	    fc_name = one.getFacility().getName();
	}
	
	line = "\""+one.getId()+"\",\""+one.getRequestYear()+"\",\""+one.getSeason()+"\",\""+one.getRequestDate()+"\",\""+pr_title+"\",\""+fc_name+"\",\""+one.getLead()+"\",\""+one.getLocationName()+"\",\""+one.getLocationDescription()+"\",\""+one.getContentSpecific()+"\",\""+one.getRequestTypeStr()+"\",\""+one.getOrientation()+"\",\""+one.getOtherType()+"\",\""+one.getNotes()+"\"\n";
	return line;
    }
    public final static String writeMarketCsv(Market market, String type, String name, String lead){
	//
	
	String all = "",line="";
	if(name == null) name = "";
	if(lead == null) lead = "";
	//
	// Program Ad
	//
	List<MarketAd> ads = market.getAds();
	Set<String> adSet = new HashSet<String>();
	if(ads != null && ads.size() > 0){
	    int jj=1;
	    for(MarketAd one:ads){
		line = type+",\""+name+"\",\""+lead+"\",\""+jj+"\",\"";
		line += one.getType().getName()+"\",\"";
		line += "\",\""; // quantity
		line += one.getDue_date()+"\",\"";
		line += cleanText2(one.getDetails())+"\"\n";
		all += line;
		jj++;
	    }
	    if(!market.getOther_ad().isEmpty()){
		line = type+",\""+name+"\",\""+lead+"\",\""+jj+"\",\"";
		line += "Other Ad\",\"\",\"\",\"";
		line += cleanText2(market.getOther_ad())+"\"\n";
		all += line;
	    }
	}
	//
	//
	// table with 3 columns
	List<MarketItem> items = market.getItems();
	Set<String> hashSet = new HashSet<String>();
	int jj=1;	
	if(items != null && items.size() > 0){
	    for(MarketItem one:items){
		hashSet.add(one.getType_id());
		line = type+",\""+name+"\",\""+lead+"\",\""+jj+"\",\"";
		line += one.getType().getName()+"\",\"";
		line += one.getQuantity()+"\",\"";
		line += one.getDue_date()+"\",\"";
		line += cleanText2(one.getDetails())+"\"\n";
		all += line;
		jj++;		
	    }
	}
	//
	// Announcement
	//
	return all;
    }    
    public final static void writeFacilities(PrintWriter out, List<Facility> ones, String url){
	if(ones == null || ones.size() < 1) return;
	String[] titles = { "ID ","Name","Type",
	    "Brochure Statement",
	    "Operation Schedule",  
	    "Closings",
	    "Other"};		
	out.println("<table width=\"100%\">");
	out.println("<caption>Facilities</caption>");
	for(Facility one:ones){
	    out.println("<tr><td align=\"right\" width=\"30%\"><strong>ID:");
	    out.println("</strong></td><td align=\"left\">");
	    out.print("<a href="+url+"Facility?id="+
		      one.getId()+"&action=zoom>"+
		      one.getId()+"</a>");
	    out.println("</td></tr>");
	    String that = one.getName();
	    if(that != null && !that.equals(""))
		writeItem(out, that, titles[1]);
	    that = one.getType();
	    if(that != null && !that.equals(""))
		writeItem(out, that, titles[2]);
												
	    Type lead = one.getLead();
	    if(lead != null)
		writeItem(out, lead.getName(), "Lead");
	    that = one.getStatement();
	    if(that != null && !that.equals("")) 
		writeItem(out, that, titles[3]);
	    that = one.getSchedule();
	    if(that != null && !that.equals("")) 
		writeItem(out, that, titles[4]);
	    that = one.getClosings();
	    if(that != null && !that.equals("")) 
		writeItem(out, that, titles[5]);
	    that = one.getOther();
	    if(that != null && !that.equals("")) 
		writeItem(out, that, titles[6]);
	    if(one.hasMarket()){
		out.println("<tr><td colspan=\"2\">");
		Helper.writeMarket(out, one.getMarket(), url);
		out.println("</td></tr>");
	    }
	    out.println("<tr><td colspan=\"2\"><hr width=\"75%\"></td></tr>");
	}
	out.println("</table>");
    }
    public final static void writePlan(PrintWriter out, Plan pp){
	out.println("<table>");
	out.println("<caption>Program Plan</caption>");
	out.println("<tr><td><b>Program: </b></td><td>");
	out.println(pp.getProgram_title()+"</td></tr>");
	
	out.println("<tr><td><b>Program Season/Year: </b></td><td>");
	out.println(pp.getSeason()+"/"+pp.getProgram_year());
	out.println("</td></tr>");
	Contact instructor = pp.getInstructor();
	//
	if(instructor != null && instructor.hasInfo()){
	    out.println("<tr><td colspan=\"2\" align=\"center\"><table border=\"1\" width=\"50%\"><caption>Instructor Info</caption>");
	    out.println("<tr><td><b>Name: </b></td><td>");
	    out.println(instructor.getName()+"</td></tr>");
	    if(!instructor.getPhones().equals("")){
		out.println("<tr><td><b>Phones:</b></td><td>");
		out.println(instructor.getPhones()+"</td></tr>");
	    }
	    if(!instructor.getEmail().equals("")){			
		out.println("<tr><td><b>Email:</b></td><td>");
		out.println(instructor.getEmail()+"</td></tr>");
	    }
	    if(!instructor.getAddress().equals("")){			
		out.println("<tr><td valign=top><b>Address:</b></td><td>");
		out.print(instructor.getAddress());
		out.println("</td></tr>");
	    }
	    out.println("</table>");
	}
	//
	// Plan Objectives
	if(!pp.getIdeas().equals("")){
	    out.println("<b>Ideas to Program: </b>");
	    out.println(pp.getIdeas());
	    out.println("<br />");
	}
	//
	if(!pp.getGoals().equals("")){
	    out.println("<b>Program Goals: </b>");
	    out.println(pp.getGoals());
	    out.println("<br />");
	}
	List<Objective> objectives = pp.getObjectives();
	int jj=1;
	if(objectives != null && objectives.size() > 0){
	    out.println("<b> Objectives:</b>");
	    for(Objective one:objectives){
		if(jj > 1) out.print(", ");
		out.print((jj++)+"-"+ one);
	    }
	    out.println("<br />");
	}
	//
	if(!pp.getProfit_obj().equals("")){
	    out.println("<b>Profit Objective: </b>");
	    out.println(pp.getProfit_obj());
	    out.println("<br />");
	}
	//
	// partner
	if(!pp.getPartner().equals("")){
	    out.println("<b>Potential Partnership:</b>");
	    out.println(pp.getPartner()+"<br />");
	}
	//
	// sponsor
	if(!pp.getSponsor().equals("")){		
	    out.println("<b>Potential Sponsorship:</b>");
	    out.println(pp.getSponsor()+"<br />");
	}
	//
	// market
	if(!pp.getMarket().equals("")){
	    out.println("<b>Target Market:</b>");
	    out.println(pp.getMarket()+"<br />");
	}
	//
	// frequency
	if(!pp.getFrequency().equals("")){
	    out.println("<b>Intended Frequency:</b>");
	    out.print(pp.getFrequency());
	    out.println("<br />");
	}
	//
	// # attendance
	out.println("<b>Estimated # Attendance:</b>");
	out.println(pp.getAttendCount());
	out.println("&nbsp;&nbsp;or <b> Min/Max: </b>");
	out.println(pp.getMin_max()+"<br />");
	//
	// Event time
	if(!pp.getEvent_time().equals("")){
	    out.println("<b>Program Time:</b>");
	    out.print(pp.getEvent_time()+"<br />"); 
	}
	//
	// duration
	if(!pp.getP_duration().equals("")){		
	    out.println("<b>Program Duration:</b>");
	    out.println(pp.getP_duration()+"<br />");
	}
	if(!pp.getEst_time().equals("")){						
	    out.println("<b>Total Estimated Time:</b>");
	    out.println(pp.getEst_time()+"<br />");
	}
	//

	List<Staff> staffs = pp.getStaffs();
	jj=1;
	if(staffs != null && staffs.size() > 0){
	    out.println("<table width=\"40%\" border=\"1\">");
	    out.println("<caption>Staff Consideration</caption>");
	    out.println("<tr><td>Staff </td><td> Number </td></tr>");			
	    for(Staff one:staffs){
		out.println("<tr><td align=\"left\">"+(jj++)+" - "+one.getStaff_type()+"</td><td align=\"left\">"+one.getQuantity()+"</td></tr>");
	    }
	    out.println("</table>");			
	}
	//
	// Event history
	if(!pp.getHistory().equals("")){
	    out.println("<b>Event History:</b>");
	    out.println(pp.getHistory());
	    out.println("<br />");
	}
	//
	// Supplies
	if(!pp.getHistory().equals("")){
	    out.println("<b>Supplies:</b>");
	    out.println(pp.getSupply());
	    out.println("<br />");
	}
	//
	// timeline
	if(!pp.getHistory().equals("")){
	    out.println("<b>Timeline:</b>");
	    out.println(pp.getTimeline());
	    out.println("<br />");
	}
	out.println("<br /><br />");
    }
    public final static void writeGenerals(PrintWriter out,
				    List<General> gens,
				    String url,
				    String title,
				    boolean forBrochure){
	if(gens == null || gens.size() == 0) return;		
	out.println("<table width=\"100%\"><caption>"+title+"</caption>>");
	for(General one:gens){
	    String str = "<a href=\""+url+"General.do?action=zoom&id="+one.getId()+"\">"+one.getId()+"</a>";
	    writeItem(out, str, "Listing ID");
	    if(!one.getTitle().equals("")){
		writeItem(out, one.getTitle(), "Title");
	    }
	    if(!forBrochure){
		str = one.getSeason()+"/"+one.getYear();
		writeItem(out, str, "Season/Year");
		Type lead = one.getLead();
		if(lead != null)
		    writeItem(out, lead.getName(), "Lead");			
	    }
	    if(!one.getDate().isEmpty()){
		writeItem(out,one.getDate(),"Date");
	    }
	    if(!one.getDays().isEmpty()){
		writeItem(out,one.getDays(),"Days");
	    }
	    if(!one.getTime().isEmpty()){
		writeItem(out,one.getTime(),"Start, End Time");
	    }
	    if(!one.getCost().isEmpty()){
		writeItem(out,"$"+one.getCost(),"Cost");
	    }
	    if(!one.getCode().isEmpty()){
		writeItem(out,one.getCode(),"Code");
	    }
	    if(!one.getCodeNeed().isEmpty()){
		writeItem(out,"yes","Code Needed");
	    }						
	    if(!one.getDescription().isEmpty()){
		writeItem(out,one.getDescription(),"Description");
	    }
	    if(!forBrochure){
		if(one.hasMarkets()){
		    for(Market market:one.getMarkets()){
			out.println("<tr><td colspan=\"2\">");
			writeMarket(out, market, url);
			out.println("</td></tr>");
		    }
		}
	    }
	    out.println("<tr><td colspan=\"2\" align=\"center\"><hr width=\"70%\"></td></tr>");
	}
	out.println("</table>");			
		
    }
    public final static void writeVolTrains(PrintWriter out,
				     List<VolTrain> trains,
				     String url,
				     String title){
	if(trains == null || trains.size() == 0) return;
	out.println("<table border=\"1\"><caption>"+title+"</caption>");
	for(VolTrain one:trains){
	    String str = "<a href=\""+url+"VolTrain.do?action=zoom&id="+one.getId()+"&pid="+one.getPid()+"&shift_id="+one.getShift_id()+"\">"+one.getId()+"</a>";
	    writeItem(out, str, "Training ID");
	    if(!one.getDate().isEmpty()){
		writeItem(out,one.getDate(),"Date");
	    }
	    if(!one.getStartTime().isEmpty()){
		writeItem(out,one.getStartTime(),"Start, End Time");
	    }
	    Type location = one.getLocation();
	    if(location != null){
		writeItem(out,location.toString(),"Location");
	    }
	    if(!one.getOther().isEmpty()){
		writeItem(out,one.getOther(),"Other");
	    }			
	    if(!one.getTdays().isEmpty()){
		writeItem(out,one.getTdays(),"Days");
	    }
	    if(!one.getNotes().isEmpty()){
		writeItem(out,one.getNotes(),"Comments");
	    }

	}
	out.println("</table>");
    }
   public final static void writeVolShifts(PrintWriter out,
				     List<VolShift> shifts,
				     String url,
				     String title,
				     boolean includeProgramTitle){
	if(shifts == null || shifts.size() == 0) return;
	out.println("<table border=\"1\" width=\"70%\">");
	out.println("<caption>"+title+"</caption>");	
	for(VolShift one:shifts){
	    String str = "<a href=\""+url+"VolShift.do?action=zoom&id="+one.getId()+"&pid="+one.getPid()+"\">"+one.getId()+"</a>";
	    writeItem(out, str, "Shift ID");
	    if(!one.getTitle().isEmpty()){
		writeItem(out, one.getTitle(),"Title");
	    }
	    else if(includeProgramTitle && one.hasProgram()){
		Program prog = one.getProgram();
		str = "<a href=\""+url+"Program.do?action=zoom&id="+prog.getId()+"\">"+prog.getTitle()+"</a>";
		writeItem(out, str,"Program");				
	    }
	    if(one.getLead() != null){
		writeItem(out, one.getLead().getName(),"Lead");
	    }					
	    if(one.getCategory() != null){
		writeItem(out, one.getCategory().getName(),"Category");
	    }			
	    if(!one.getVolCount().isEmpty()){
		writeItem(out, one.getVolCount(),"# of Volunteers");
	    }
	    if(!one.getDate().isEmpty()){
		writeItem(out, one.getDate(),"Date");
	    }
	    if(!one.getDays().isEmpty()){
		writeItem(out, one.getDays(),"Days");
	    }
	    if(!one.getStartTime().isEmpty()){
		writeItem(out,one.getStartEndTime(),"Start, End Time");
	    }
	    if(!one.getDuties().isEmpty()){
		writeItem(out, one.getDuties(),"Duties");
	    }
	    if(!one.getNotes().isEmpty()){
		writeItem(out, one.getNotes(),"Comments");
	    }
	    if(!one.getOnsite().isEmpty()){
		writeItem(out,"On site training provided","Training");
	    }
	    else if(!one.getPre_train().isEmpty()){
		writeItem(out,"Advanced training/orientation required","Training");
	    }
	    if(one.hasTraining()){
		out.println("<tr><td colspan=\"2\">");
		writeVolTrains(out, one.getTrains(), url,"This Shift Training");
		out.println("</td></tr>");
				
	    }
	    out.println("<tr><td colspan=\"2\" align=\"center\"><hr width=\"75%\" /></td></tr>");				
	}
	out.println("</table>");
    }
    /**
     * Finds out the starting day of a given month.
     * @param MM month
     * @param YYYY the year
     * @return a number representing the first day of month or the day 
     * of the weeek
     */
    public static int get_first_day_of_month(int MM, int YYYY){

	GregorianCalendar cal = new GregorianCalendar();
	cal.set(Calendar.YEAR, YYYY);
	cal.set(Calendar.MONTH, MM - 1);
	cal.set(Calendar.DAY_OF_MONTH, 1);
      	return cal.get(Calendar.DAY_OF_WEEK);

    }
    /**
     * Finds the number of days in a given month.
     * @param mm the month
     * @param yy the year
     * @return the number of days in the month
     */
    public static int get_days_in_month(int mm, int yy) {

	GregorianCalendar cal = new GregorianCalendar();
	cal.set(Calendar.YEAR, yy);
	cal.set(Calendar.MONTH, mm - 1);
	//
	int days = cal.getActualMaximum(Calendar.DATE);
	//
	return days;  
    }
    /**
     * Updates the current date by increase of one day.
     * @param month 
     * @param day
     * @param year
     * @retuns an array of month/day/year
     */    
    public static int[] updateCurrentDate(int month, int day, int year){
		
	int daysInMonth[] = { 31, 28, 31, 30, 31, 30, 31,
	    31, 30, 31, 30, 31, 30, 31};
	int [] newDate = new int[3];
	if(day + 1 <= daysInMonth[month-1]) day++;
	else if(month == 2) { //February
	    if(day+1 < get_days_in_month(month, year)){
		day++;
	    }
	}
	else if(month+1 < 13){
	    day = 1;
	    month++;
	}
	else { // the last day of the year
	    day = 1;
	    month = 1;
	    year++;
	}
	newDate[0] = month;
	newDate[1] = day;
	newDate[2] = year;
	return newDate;
    }
    public final static void writeWebCss(PrintWriter out,
					    String url
					    ){
	if(out != null && !url.isEmpty()){
	    /**
	    out.println("<link rel=\"stylesheet\" href=\""+url+"css/jquery-ui.min-1.13.2.css\" type=\"text/css\" media=\"all\" />\n");
	    out.println("<link rel=\"stylesheet\" href=\""+url+"css/jquery.ui.theme.min-1.13.2.css\" type=\"text/css\" media=\"all\" />\n");


	    out.println(".ui-datepicker-prev .ui-icon, .ui-datepicker-next .ui-icon { ");
	    out.println(" background-image: url(\"js/images/ui-icons_228ef1_256x240.png\");");
	    out.println("} ");
	    */
	    out.println("<style>");	    
	    out.println("<link rel=\"stylesheet\" href=\""+url+"css/menu_style.css\" />");	    
	    out.println("</style>");

	}
    }
    public final static void writeTopMenu(PrintWriter out,
					  String url
					  ){
	if(out != null && !url.isEmpty()){
	    out.println("<center>");
	    out.println("<h3>Parks & Recreation Dept - Promt</h3>");	    	    
	    out.println("<div id=\"div_top\">");
	    out.println("<ul id=\"ul_top\">");
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"ProgPlan\">New Plan</a></li>");

	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"PlanSearch\">Plans</a></li>");
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"Search\">Programs</a></li>");
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"MarketSearch\">Marketings</a></li>");
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"VolunteerSearch\">Volunteers</a></li>");
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"GeneralSearch\">Gen. Listings</a></li>");
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"SubMenu?choice=code\">Code Entry</a></li>");
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"SubMenu?choice=toPublish\">Publishing</a></li>");
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"SubMenu?choice=unPublish\">Unpublishing</a></li>");

	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"SubMenu?choice=report\">Reports</a></li>");
	    
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"SubMenu?choice=calendar\">Calendar</a></li>");
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"ContactBrowse\">Instructors</a></li>");
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"LocationSearch\">Locations</a></li>");
	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"FacilitySearch\">Facilities</a></li>");

	    out.println("<li style=\"float:left;list-style-type:none;padding:4px;\"><a href=\""+url+"logout\">Logout</a></li>");	    
	    out.println("</ul>");
	    out.println("</div><br />");
	    out.println("</center>");
	    out.println("<br /><br />");
	}
    }
    public final static void writeWebFooter(PrintWriter out,
					    String url
					    ){
	if(out != null && !url.isEmpty()){
	    /**
	    out.println("<script type=\"text/javascript\" src=\""+url+"js/jquery-3.6.1.min.js\"></script>");
	    out.println("<script type=\"text/javascript\" src=\""+url+"js/jquery-ui.min-1.13.2.js\"></script>");
	    out.println("<script type=\"text/javascript\" src=\""+url+"js/jquery.easing.1.3.js\"></script>");
	    out.println("<script type=\"text/javascript\" src=\""+url+"js/jquery.jBreadCrumb.1.1.js\"></script>");	    
	    out.println("<script type=\"text/javascript\" src=\""+url+"js/jqAreYouSure.js\"></script>");
	    String dateStr = "{ nextText: \"Next\",prevText:\"Prev\", buttonText: \"Pick Date\", showOn: \"both\", navigationAsDateFormat: true, buttonImage: \""+url+"js/calendar.gif\"}";
	    out.println("<script>");
	    out.println("  $( \".date\" ).datepicker("+dateStr+"); ");
	    out.println("  $( \".date\" ).datepicker("+dateStr+"); ");
	    out.println("$(function() { ");
	    out.println("$('#form_id').areYouSure(); ");
	    out.println("}); ");			
	    out.println("</script>");
	    */
	}
    }
}






















































