package program.web;

import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;
/**
 * The marketing form.
 * this an update to the previous market class (look at MarketOld)
 *
 */
@WebServlet(urlPatterns = {"/Market.do","/Market"})
public class MarketServ extends TopServlet{

    static Logger logger = LogManager.getLogger(MarketServ.class);
    /**
     * The main class method.
     *
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	doPost(req, res);
    }
    /**
     * Generates and processes the marketing form.
     * Handls view, add, update, delete operations on this form.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	String allCategory = "";
	String finalMessage = "";

	PrintWriter out = res.getWriter();
	String id = "", prog_id="", facility_id="", general_id="";
	String season="", year=""; // needed for facilities
	Enumeration values = req.getParameterNames();
	String name, value;
	String action="";
	String message="";
	//
	boolean	success = true;
	String [] vals;
	HttpSession session = null;
	session = req.getSession(false);
		
	User user = null;
	Control control = null;
	if(session != null){
			
	    user = (User)session.getAttribute("user");
	}
	List<Type> allAnnounces = null;
	List<Type> marketTypes = null;
	List<Type> allAds = null;
	Market market = new Market(debug);
	Program program = null;
	Facility facility = null;
	General general = null;
	MarketItem[] newItems = new MarketItem[4];
	MarketAd[] newAds = new MarketAd[3];		
	for(int i=0;i<newItems.length;i++){
	    newItems[i] = new MarketItem(debug);
	}
	for(int i=0;i<newAds.length;i++){
	    newAds[i] = new MarketAd(debug);
	}		
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();		    
	    if(name.equals("id")){
		id = value;
		market.setId(value);
		updateItems(newItems, "market_id", value);
	    }
	    else if(name.equals("prog_id")){
		prog_id = value;
		market.setProg_id(value);
	    }
	    else if(name.equals("general_id")){
		general_id = value;
		market.setGeneral_id(value);
	    }						
	    else if(name.equals("facility_id")){
		facility_id = value;
		market.setFacility_id(value);
	    }
	    else if(name.equals("season")){
		season = value;
		market.setSeason(value);
	    }
	    else if(name.equals("year")){
		year = value;
		market.setYear(value);
	    }						
	    else if(name.equals("other_ad")){
		market.setOther_ad(value);
	    }
	    else if(name.equals("other_market")){
		market.setOther_market(value);
	    }
	    else if(name.equals("class_list")){
		market.setClass_list(value);
	    }			
	    else if(name.equals("spInstructions")){
		market.setSpInstructions(value);
	    }
	    else if(name.equals("signBoard")){
		market.setSignBoard(value);
	    }
	    else if(name.equals("signBoardDate")){
		market.setSignBoardDate(value);
	    }	    
	    else if(name.equals("addAnnounces")){
		market.addAnnounces(vals);
	    }
	    else if(name.equals("delAds")){
		market.delAds(vals);
	    }
	    else if(name.equals("delAnnounces")){
		market.delAnnounces(vals);
	    }
	    else if(name.equals("delItem")){
		market.delItems(vals);
	    }
	    else if(name.equals("delAd")){
		market.delAds(vals);
	    }			
	    else if(name.startsWith("quantity")){
		updateItems(newItems, name, value);
	    }
	    else if(name.startsWith("expenses")){
		updateItems(newItems, name, value);
	    }
	    else if(name.startsWith("direct")){
		updateItems(newItems, name, value);
	    }
	    else if(name.startsWith("type_id")){
		updateItems(newItems, name, value);
	    }
	    else if(name.startsWith("due_date")){
		updateItems(newItems, name, value);
	    }
	    else if(name.startsWith("details")){
		updateItems(newItems, name, value);
	    }			
	    else if(name.startsWith("ad_expenses")){
		updateAds(newAds, name, value);
	    }
	    else if(name.startsWith("ad_direct")){
		updateAds(newAds, name, value);
	    }
	    else if(name.startsWith("ad_type_id")){
		updateAds(newAds, name, value);
	    }
	    else if(name.startsWith("ad_due_date")){
		updateAds(newAds, name, value);
	    }
	    else if(name.startsWith("ad_details")){
		updateAds(newAds, name, value);
	    }			
	    else if(name.equals("action")){
		action = value;
	    }
	    else {
		System.err.println(name +" ** "+value);
	    }
	}
	if(user == null){
	    String str = url+"Login?source=Market.do&action=zoom&id="+id;
	    res.sendRedirect(str);
	    return;
	}
	if(true){
	    String back = "";
	    if(allAds == null){
		AdList ones = new AdList(debug);
		back = ones.find();
		if(!back.equals("")){
		    message += " Could not retreive data ";
		    success = false;
		}
		else{
		    allAds = ones;
		}
	    }
	    if(allAnnounces == null){
		AnnounceList ones = new AnnounceList(debug);
		back = ones.find();
		if(!back.equals("")){
		    message += " Could not retreive data ";
		    success = false;
		}
		else{
		    allAnnounces = ones;
		}				
	    }
	    if(marketTypes == null){
		TypeList ones = new TypeList(debug, "marketing_types");
		back = ones.find();
		if(!back.equals("")){
		    message += " Could not retreive data ";
		    success = false;
		}
		else{
		    marketTypes = ones;
		}					
	    }
	    market.addAds(newAds);
	    market.addItems(newItems);
	}
	//
	if(action.equals("Save")){
	    // new record
	    String back = market.doSave();
	    if(!back.equals("")){
		message += " Could not save "+back;
		success = false;
	    }
	    else{
		id = market.getId();
		History one = new History(debug, id, "Created","Market",user.getId());
		one.doSave();	
		message += market.getMessage();
	    }
	}
	else if (action.equals("Update")){
	    String back = market.doUpdate();
	    if(!back.equals("")){
		message += " Could not update "+back;
		success = false;
	    }
	    else{
		History one = new History(debug, id, "Updated","Market",user.getId());
		one.doSave();				
		message += market.getMessage();
	    }
	}
	else if (!id.equals("")){
	    String back = market.doSelect();
	    if(!back.equals("")){
		message = market.getMessage();
		success = false;
	    }
	    else{
		action = "zoom";
	    }
	}
	else if(!prog_id.equals("")){
	    program = new Program(debug, prog_id);
	    String back = program.doSelect();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    if(program.hasMarket()){
		market = program.getMarket();
		id = market.getId();
	    }
	}
	else if(!facility_id.equals("")){
	    facility = new Facility(debug, facility_id);
	    String back = facility.doSelect();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}
	else if(!general_id.equals("")){
	    general = new General(debug, general_id);
	    String back = general.doSelect();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}				
	if(program == null && market.hasProgram()){
	    program = market.getProgram();
	    prog_id = program.getId();
	}
	else if(facility == null && market.hasFacility()){
	    facility = market.getFacility();
	    facility_id = facility.getId();
	    season = market.getSeason();
	    year = market.getYear();
	}
	else if(general == null && market.hasGeneral()){
	    general = market.getGeneral();
	    general_id = general.getId();
	    season = market.getSeason();
	    year = market.getYear();						
	}
	//
	out.println("<html><head><title>Marketing</title>");
	Helper.writeWebCss(out, url);
	out.println("<script type='text/javascript'>");
	out.println("/*<![CDATA[*/");				
	out.println("  function validateInteger(x) {    ");            
	out.println("	if(!isNaN(x)) {                 ");
	out.println("	            return true;        ");
	out.println(" 	        }                       ");
	out.println("	       return false;	        ");
	out.println(" 	   }                            ");
	out.println("  function validateString(x){      ");            
	out.println("     if((x.value.length > 0)){     "); 
	out.println("       var eq = 0;	                ");
	out.println("    for(t = 0; t < x.value.length; t++){  ");
	out.println("    if (x.value.substring(t,t+1) != \" \") eq = 1;	");
	out.println("    	       }                  ");
	out.println("     if (eq == 0) {	          ");
	out.println("	      return false;		  ");
	out.println("            }                        ");
	out.println("	     return true;		  ");
	out.println("         }                           ");  
	out.println("	     return false;		  ");
	out.println("      }                              ");  
	out.println(" function checkSelection(element){   ");
	out.println("   for(var i = 0; i < element.options.length; i++) ");
        out.println("    if (element.options[i].selected){  "); 
	out.println("      if(i > 0){ ");
	out.println("         return true;  ");
	out.println("         }     ");
	out.println("       }  ");
	out.println("    return false;  ");
	out.println("   }               ");
	out.println(" function validateTextarea(ss){   ");
        out.println("    if (ss.value.length > 1000 ){  "); 
	out.println("       alert(\"Text Area should not be more than"+
		    " 1000 chars\"); ");
	out.println("       ss.value = ss.value.substring(0,1000); ");
	out.println("      ss.focus();                            ");
	out.println("      ss.select();                           ");
	out.println("       }                                     ");
	out.println("   }                                         ");
	out.println("  function validateForm(){                   ");    
	out.println("  return true;                     ");
	out.println("  }	                        ");
	out.println("  checkNumber(item){               ");
	out.println(" if(item.value.length > 0){        ");  
	out.println(" if(isNaN(item.value)){            ");
	out.println("  alert(\"Invalid Number\"+item.value); ");
	out.println("  item.focus();                    ");
	out.println("  return false;}}		        ");
	out.println("  return true;                     ");
	out.println("  	 }                              ");
	out.println("  function validateDeleteForm(){   ");            
	out.println("  var x = false;                   ");
	out.println("   x = window.confirm(\"Are you sure you want to delete\"); ");
	out.println("   return x;                       ");
	out.println(" }	                                ");
	out.println("/*]]>*/\n");					
	out.println(" </script>                         ");   
	out.println("</head><body>");
	out.println("<center>");
	Helper.writeTopMenu(out, url);
	if(id.equals("")){
	    out.println("<h2>Add Marketing </h2>");
	}
	else {
	    out.println("<h2>Edit Marketing "+id+"</h2>");
	}
	out.println("<br />");
	if(!message.equals("")){
	    out.println(message+"<br />");
	}
	//
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\" "+
		    "onSubmit=\"return validateForm()\">");
	if(!id.equals(""))
	    out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\" />");
	if(!prog_id.equals(""))
	    out.println("<input type=\"hidden\" name=\"prog_id\" value=\"" + prog_id + "\" />");
	if(!facility_id.equals(""))
	    out.println("<input type=\"hidden\" name=\"facility_id\" value=\"" + facility_id + "\" />");
	if(!general_id.equals(""))
	    out.println("<input type=\"hidden\" name=\"general_id\" value=\"" + general_id + "\" />");
	out.println("<table border=\"1\" width=\"95%\">");
	out.println("<caption>Marketing</caption>");
	//
	// program, year, season
	if(program != null){
	    out.println("<tr><td align=\"right\">");
	    out.println("</td><td><a href=\""+url+"Program.do?action=zoom&id="+program.getId()+"\">Program: ");
	    out.print(program.getTitle()+", (");
	    out.print(program.getSeasons());		
	    out.print("/"+program.getYear()+")");
	    out.println("</a></td></tr>");
	}
	else if(facility != null){
	    out.println("<tr><td align=\"right\">");
	    out.println("</td><td>");
	    String str = "";
	    if(!season.equals("")){
		str += season;
	    }
	    if(!year.equals("")){
		if(!str.equals("")) str +="/"; 
		str += year;
	    }
	    if(!str.equals("")) str =", ("+str+")"; 						
	    out.println("Facility: "+facility.getName() + str);
	    out.println("</td></tr>");
	    //
	    // the user need to pick season and year for facility
	    //
	    if(id.equals("")){
		out.println("<tr><td align=\"right\"><label for=\"season\">Season</label>");
		out.println("</td><td>");
		out.println("<select name=\"season\" id=\"season\">");
		if(season.equals("")){
		    out.println("<option value=\"-1\" selected=\"selected\">Pick Season</option>");
		}
		else{
		    out.println("<option value=\""+season+"\" selected=\"selected\">"+season+"</option>");
		}
		out.println(Helper.allSeasons);
		out.println("</select><label for=\"year\"> Year:</label>");
		out.println("<select name=\"year\" id=\"year\">");
		out.println("<option value=\"-1\">Pick Year</option>");
		int[] yy = Helper.getFutureYears();
		for(int y:yy){
		    if(!year.isEmpty() && year.equals(""+y))
			out.println("<option selected=\"selected\" value=\""+y+"\">"+y+"</option>");
		    else
			out.println("<option value=\""+y+"\">"+y+"</option>");												
		}
		out.println("</select></td></tr>");						
	    }														
	    out.println("</select>&nbsp;&nbsp;");						
	    out.println("</td></tr>");
						
	}
	else if(general != null){
	    out.println("<tr><td align=\"right\">");
	    out.println("</td><td>General Listing: ");
	    out.println(general.getTitle());
	    out.println("</td></tr>");
	    if(id.equals("")){
		season = general.getSeason();
		year = general.getYear();
		out.println("<tr><td align=\"right\"><label for=\"season\">Season</label>");
		out.println("</td><td>");
		out.println("<select name=\"season\" id=\"season\">");
		if(season.equals("")){							
		    out.println("<option value=\"-1\" selected>Pick Season</option>");
		}
		else{
		    out.println("<option value=\""+season+"\" selected=\"selected\">"+season+"</option>");
		}
		out.println(Helper.allSeasons);
		out.println("</select><label for=\"year\"> Year:</label>");
		out.println("<select name=\"year\" id=\"year\">");
		out.println("<option value=\"-1\">Pick Year</option>");
		int[] yy = Helper.getFutureYears();
		for(int y:yy){
		    if(!year.isEmpty() && year.equals(""+y))
			out.println("<option selected=\"selected\" value=\""+y+"\">"+y+"</option>");
		    else
			out.println("<option value=\""+y+"\">"+y+"</option>");
		}
		out.println("</select></td></tr>");						
	    }														
	    out.println("</select>&nbsp;&nbsp;");						
	    out.println("</td></tr>");
	}
	//
	// Program Ad
	out.println("<tr><td colspan=\"2\" align=\"center\">");		
	out.println("<table border=\"1\" width=\"90%\">");
	out.println("<caption>Marketing Ads </caption>");
	out.println("<tr><th>&nbsp;</th><th>Ad Type</th>"+
		    "<th>Expenses</th><th colspan=\"2\">Expense Type</th><th>Due Date</th><th width=\"25%\">Details</th><th>&nbsp;</th></tr>");		
	int j=1;
	List<MarketAd> ads = market.getAds();
	Set<String> adSet = new HashSet<String>();
	if(ads != null && ads.size() > 0){
	    for(MarketAd one:ads){
		out.println("<tr><td>"+(j++)+"<input type=\"checkbox\" name=\"delAd\" value=\""+one.getId()+"\" id=\""+one.getId()+"\" /></td>");
		// adSet.add(one.getType_id());
		out.println("<td><label for=\""+one.getId()+"\">"+one.getType().getName()+"</label></td>");
		out.println("<td align=\"right\">$"+Helper.formatNumber(one.getExpenses())+"</td>");
		if(one.isDirect()){
		    out.println("<td>Direct</td><td>&nbsp;</td>");
		}
		else{
		    out.println("<td>&nbsp;</td><td>Indirect</td>");
		}
		out.println("<td>&nbsp;"+one.getDue_date()+"</td>");
		out.println("<td>&nbsp;"+one.getDetails()+"</td>");				
		out.println("<td>");
		out.println("<input "+
			    "type=\"button\" value=\"Edit\" "+
			    "onclick=\"window.open('"+
			    url+
			    "MarketAd.do?id="+one.getId()+
			    "','MarketAd','toolbar=0,location=0,"+
			    "directories=0,status=0,menubar=0,"+
			    "scrollbars=0,top=200,left=200,"+
			    "resizable=1,width=500,height=500');return false;\" />");
		out.println("</td></tr>");
	    }
	}
	for(int i=0;i<3;i++){
	    out.println("<tr><td><label for=\"ad_type_id"+i+"\">"+(j++)+"</label></td>");
	    out.println("<td><select name=\"ad_type_id"+i+"\" id=\"ad_type_id"+i+"\" >");
	    out.println("<option value=\"\">Pick one</option>");
	    if(allAds != null && allAds.size() > 0){
		for(Type one:allAds){
		    // if(adSet.contains(one.getId())) continue;
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }
	    out.println("</select></td>");
	    out.println("<td><label for=\"exp"+i+"\">Ad Expenses </label><input type=\"text\" name=\"ad_expenses"+i+"\" value=\"\" size=\"6\" maxlength=\"6\" id=\"exp"+i+"\" /></td>");
	    out.println("<td><input type=\"radio\" name=\"ad_direct"+i+"\" value=\"y\" id=\"dir"+i+"\" /><label for=\"dir"+i+"\">Direct</label></td>");
	    out.println("<td><input type=\"radio\" name=\"ad_direct"+i+"\" value=\"\" id=\"ind"+i+"\"><label for=\"ind"+i+"\">Indirect</lable></td>");
	    out.println("<td><input type=\"text\" name=\"ad_due_date"+i+"\" value=\"\" size=\"10\" maxlength=\"10\" id=\"ad_due_date"+i+"\" /></td>");
	    out.println("<td colspan=\"2\"><textarea name=\"ad_details"+i+"\" rows=\"3\" cols=\"40\" id=\"ad_details"+i+"\"></textarea></td>");			
	    out.println("</tr>");
	}
	out.println("</table></td></tr>");
	//
	out.println("<tr><td align=\"right\"><label for=\"other_ad\">Other Ad:</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"other_ad\" value=\""+market.getOther_ad()+"\" size=\"30\" maxlength=\"70\" id=\"other_ad\" /></td></tr>");
	//
	// marketing pieces
	out.println("<tr><td colspan=\"2\" align=\"center\"><b>Marketing Pieces:</b></td></tr>");
	out.println("<tr><td colspan=\"2\" align=\"center\">Note: due date is required for every marketing piece.</td></tr>");				
	out.println("<tr><td align=\"center\" colspan=\"2\">");
	//
	// table with 3 columns
	double totalDirect = 0, totalIndirect = 0;		
	out.println("<table border=\"1\" width=\"90%\"><tr><th>&nbsp;</th><th>Type</th><th>Quantity</th>"+
		    "<th>Expenses</th><th colspan=\"2\">Expense Type</th><th width=\"12%\">Due Date</th><th width=\"30%\">Details</th><th width=\"5%\">&nbsp;</th></tr>");
	List<MarketItem> items = market.getItems();
	j=1;
	Set<String> hashSet = new HashSet<String>();
	if(items != null && items.size() > 0){
	    for(MarketItem one:items){
		out.println("<tr><td>"+(j++)+"<input type=\"checkbox\" name=\"delItem\" value=\""+one.getId()+"\" /></td>");
		hashSet.add(one.getType_id());
		out.println("<td>"+one.getType().getName()+"</td>");
		out.println("<td>"+one.getQuantity()+"</td>");
		out.println("<td align=\"right\">$"+Helper.formatNumber(one.getExpenses())+"</td>");
		if(one.isDirect()){
		    out.println("<td>Direct</td><td>&nbsp;</td>");
		    // totalDirect += one.getExpenses();
		}
		else{
		    out.println("<td>&nbsp;</td><td>Indirect</td>");
		    // totalIndirect += one.getExpenses();
		}
		out.println("<td>&nbsp;"+one.getDue_date()+"</td>");
		out.println("<td>&nbsp;"+one.getDetails()+"</td>");
		out.println("<td><input "+
			    "type=\"button\" value=\"Edit\" "+
			    "onClick=\"window.open('"+
			    url+
			    "MarketItem.do?id="+one.getId()+
			    "','MarketPiece','toolbar=0,location=0,"+
			    "directories=0,status=0,menubar=0,"+
			    "scrollbars=0,top=200,left=200,"+
			    "resizable=1,width=500,height=500');return false;\" />");				
		out.println("</td></tr>");
	    }
	}
	for(int i=0;i<4;i++){
	    out.println("<tr><td>"+(j++)+"</td>");
	    out.println("<td><select name=\"type_id"+i+"\">");
	    out.println("<option value=\"\">Pick one</option>");
	    if(marketTypes != null && marketTypes.size() > 0){
		for(Type one:marketTypes){
		    if(one.isActive())
			out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }
	    out.println("</select></td>");
	    out.println("<td><input type=\"text\" name=\"quantity"+i+"\" value=\"\" size=\"4\" maxlength=\"4\" /></td>");
	    out.println("<td><input type=\"text\" name=\"expenses"+i+"\" value=\"\" size=\"6\" maxlength=\"6\" /></td>");
	    out.println("<td><input type=\"radio\" name=\"direct"+i+"\" value=\"y\" />Direct</td>");
	    out.println("<td><input type=\"radio\" name=\"direct"+i+"\" value=\"\">Indirect</td>");
	    out.println("<td><input type=\"text\" name=\"due_date"+i+"\" value=\"\" size=\"10\" maxlength=\"10\" id=\"due_date"+i+"\" /></td>");
	    out.println("<td colspan=\"2\"><textarea name=\"details"+i+"\" rows=\"3\" cols=\"30\" ></textarea></td>");			
	    out.println("</tr>");
	}
	totalDirect = market.getTotalDirectExpenses();
	totalIndirect = market.getTotalIndirectExpenses();
	if(totalDirect > 0 || totalIndirect > 0){
	    out.println("<tr><td colspan=\"4\" align=\"right\"><b>Total Expenses:</b>(<font color=\"green\" size=\"-1\">(These will be carried on to evaluation page)</font></td><td align=\"right\">$"+Helper.formatNumber(totalDirect)+"</td><td align=\"right\">$"+Helper.formatNumber(totalIndirect)+"</td></tr>");
	}
	out.println("</table></td></tr>");
		
	//
	out.println("<tr><td align=\"right\">");
	out.println("<label for=\"class\">Does the marketing piece <br />combine classes or programs?<br />Please list which ones should appear </label>(example: two classes sharing <br />one flier):</td><td colspan='2'>");
	out.print("<textarea name=\"class_list\" rows=\"5\" cols=\"65\" id=\"class\" wrap>");
	out.print(market.getClass_list());
	out.println("</textarea></td></tr>");
	out.println("<tr><td valign=\"top\" align=\"right\"><label for=\"other\">Other Marketing</label></td><td valign=\"top\">(Up to 1000 characters)<br />");
	out.print("<textarea name=\"other_market\" rows=\"5\" cols=\"65\" id=\"other\" wrap>");
	out.print(market.getOther_market());
	out.println("</textarea></td></tr>");
	//
	// Announcement
	out.println("<tr><td align=\"right\" valign=\"top\"><b>Announcements:</b>");
	out.println("</td><td align=\"left\">");
	//
	List<Type> announces = market.getAnnounces();
	j=1;
	if(announces != null && announces.size() > 0){
	    out.println("<table width=\"100%\"><tr><td><b>Current Announcements</b></td></tr>");
	    out.println("<tr><td>");
	    boolean inn = false;
	    for(Type one:announces){
		if(allAnnounces != null && allAnnounces.contains(one)){
		    allAnnounces.remove(one);
		}
		if(inn) out.print(", ");
		out.print((j++)+" - <input type=\"checkbox\" name=\"delAnnounces\" value=\""+one.getId()+"\">"+one);
		inn = true;
	    }
	    out.println("</td></tr></table>");
	}
	if(allAnnounces != null){
	    out.println("<table width=\"100%\"><tr><td>You can add 3 announcements at a time</td></tr>");
	    out.print("<tr><td>");
	    for(int i=1;i<4;i++){
		if(i > 1) out.print(", ");
		out.print((j++)+" - <select name=\"addAnnounces\">");
		out.print("<option value=\"\">Pick One</option>");
		for(Type one:allAnnounces){
		    if(one.isActive())
			out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
		out.println("</select>");				
	    }
	    out.println("</td></tr>");
	    out.println("</table>");
	}
	out.println("</td></tr>");
	//		
	// Special Instruction
	out.println("<tr><td valign=\"top\" align=\"right\">");
	out.println("<label for=\"spe\">Special Instructions</b></td><td>");
	out.println("<textarea rows=\"5\" cols=\"65\" name=\"spInstructions\" id=\"spe\" wrap "+
		    "onchange=\"validateTextarea(this)\">"); 
	out.println(market.getSpInstructions());
	out.println("</textarea>");
	
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\">");
	String checked = "";
	if(!market.getSignBoard().isEmpty()){
	    checked="checked=\"checked\"";
	}
	out.println("<input type=\"checkbox\" name=\"signBoard\" value=\"y\" "+checked+" id=\"signb\" /><label for=\"signb\">Reserve the digital signboard.</label></td><td align=\"left\"><label for=\"board_date\"> Date digital sign needed </label>");
	out.println("<input type=\"text\" name=\"signBoardDate\" value=\""+market.getSignBoardDate()+"\" size=\"10\" id=\"board_date\" />(mm/dd/yyyy)");
	out.println("</td></tr>");
	out.println("</table></td></tr>");
	//
	// check action type
	//
	if(id.equals("")){
	    if(user.canEdit()){
		out.println("<tr><td colspan=2 align=right><input type=\"submit\" "+
			    "name=\"action\" value=\"Save\" />&nbsp;&nbsp;"+
			    "&nbsp;&nbsp;&nbsp;" +
			    "&nbsp;&nbsp;<input type=\"reset\" value=\"Clear\" />"+
			    "</td></tr>");
	    }
	    out.println("</table></td></tr>");			
	}
	else{ // add zoom update
	    //
	    out.println("<tr><td><table width=\"80%\"><tr>");
	    if(user.canEdit()){
		out.println("<td align=\"right\" valign=\"top\"><input type=submit "+
			    "name=\"action\" value=\"Update\" /> "+
			    "&nbsp;&nbsp;&nbsp;&nbsp;" +
			    "</td>");
		out.println("<td align=\"center\" valign=\"top\">");
		out.println("<input type=\"button\" value=\"Add Attachments\""+
			    " onclick=\"document.location='"+url+
			    "PromtFile.do?type=Marketing&related_id="+market.getId()+
			    "';\" /></td>");
								
	    }
	    //
	    out.println("</tr></table></td></tr>");
	    out.println("</table>");			
	}
	out.println("</form>");
        if(market.hasFiles()){
	    Helper.printFiles(out, url, market.getFiles());
        }				
	if(market.hasHistory()){
	    Helper.writeHistory(out, "Marketing Logs", market.getHistory()); 
	}
				
	//
	// put all the old marketing if any, in one table
	//
	if(facility != null)
	    out.println("<li><a href="+url+"Facility?id="+facility.getId()+
			"&action=zoom>Go to the Related Facility </a>");
	else if(general != null)
	    out.println("<li><a href="+url+"General.do?id="+general.getId()+
			"&action=zoom>Go to the Related General Listing </a>");
	Helper.writeWebFooter(out, url);
	String dateStr = "{ nextText: \"Next\",prevText:\"Prev\", buttonText: \"Pick Date\", showOn: \"both\", navigationAsDateFormat: true, buttonImage: \""+url+"js/calendar.gif\"}";
	out.println("<script>");
	out.println("  $( \"#ad_due_date0\" ).datepicker("+dateStr+"); ");
	out.println("  $( \"#ad_due_date1\" ).datepicker("+dateStr+"); ");
	out.println("  $( \"#ad_due_date2\" ).datepicker("+dateStr+"); ");
	out.println("  $( \"#due_date0\" ).datepicker("+dateStr+"); ");
	out.println("  $( \"#due_date1\" ).datepicker("+dateStr+"); ");
	out.println("  $( \"#due_date2\" ).datepicker("+dateStr+"); ");
	out.println("  $( \"#due_date3\" ).datepicker("+dateStr+"); ");
	out.println("  $( \"#board_date\" ).datepicker("+dateStr+"); ");
	out.println("</script>");
	out.println("</center>");
	out.print("</body></html>");
	out.flush();
	out.close();
    }
    /**
     * this function handles extracting the index from a word such as
     * quantity1, quantity2,  and saving the info to the corresponding
     * item
     */
    void updateItems(MarketItem[] items, String name, String value){
	//
	if(value.equals("")) return;
	int jj=-1;
	if(name.startsWith("quantity")){
	    jj = 8;
	    try{
		int id = Integer.parseInt(name.substring(jj));
		items[id].setQuantity(value);
	    }catch(Exception ex){
		System.err.println(ex+" "+name);
	    }
	}
	else if(name.startsWith("expenses")){
	    jj = 8;
	    try{
		int id = Integer.parseInt(name.substring(jj));
		items[id].setExpenses(value);
	    }catch(Exception ex){
		System.err.println(ex+" "+name);
	    }
	}
	else if(name.startsWith("direct")){
	    jj = 6;
	    try{
		int id = Integer.parseInt(name.substring(jj));
		items[id].setDirect(value);
	    }catch(Exception ex){
		System.err.println(ex+" "+name);
	    }
	}
	else if(name.startsWith("type_id")){
	    jj = 7;
	    try{
		int id = Integer.parseInt(name.substring(jj));
		items[id].setType_id(value);
	    }catch(Exception ex){
		System.err.println(ex+" "+name);
	    }
	}
	else if(name.startsWith("due_date")){
	    jj = 8;
	    try{
		int id = Integer.parseInt(name.substring(jj));
		items[id].setDue_date(value);
	    }catch(Exception ex){
		System.err.println(ex+" "+name);
	    }
	}
	else if(name.startsWith("details")){
	    jj = 7;
	    try{
		int id = Integer.parseInt(name.substring(jj));
		items[id].setDetails(value);
	    }catch(Exception ex){
		System.err.println(ex+" "+name);
	    }
	}			
    }
    /**
     * this function handles extracting the index from a word such as
     * quantity1, quantity2,  and saving the info to the corresponding
     * item
     */
    void updateAds(MarketAd[] items, String name, String value){
	//
	if(value.equals("")) return;
	int jj=-1;
	if(name.startsWith("ad_type_id")){
	    jj=10;
	    try{
		int id = Integer.parseInt(name.substring(jj));
		items[id].setType_id(value);
	    }catch(Exception ex){
		System.err.println(ex+" "+name);
	    }	
	}
	else if(name.startsWith("ad_expenses")){
	    jj = 11;
	    try{
		int id = Integer.parseInt(name.substring(jj));
		items[id].setExpenses(value);
	    }catch(Exception ex){
		System.err.println(ex+" "+name);
	    }
	}
	else if(name.startsWith("ad_direct")){
	    jj = 9;
	    try{
		int id = Integer.parseInt(name.substring(jj));
		items[id].setDirect(value);
	    }catch(Exception ex){
		System.err.println(ex+" "+name);
	    }
	}
	else if(name.startsWith("ad_details")){
	    jj = 10;
	    try{
		int id = Integer.parseInt(name.substring(jj));
		items[id].setDetails(value);
	    }catch(Exception ex){
		System.err.println(ex+" "+name);
	    }
	}		
	else if(name.startsWith("ad_due_date")){
	    jj = 11;
	    try{
		int id = Integer.parseInt(name.substring(jj));
		items[id].setDue_date(value);
	    }catch(Exception ex){
		System.err.println(ex+" "+name);
	    }
	}		
    }
}















































