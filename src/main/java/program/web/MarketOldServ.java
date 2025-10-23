package program.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.list.*;
import program.util.*;

/**
 * The old marketing form.
 * handles old market data up to 06/30/2014
 * modified for all new data after this date
 * look at MarketServ class instead
 *
 */
public class MarketOldServ extends TopServlet{

    static Logger logger = LogManager.getLogger(MarketOldServ.class);
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
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	String unicID ="";
	String allCategory = "";
	String finalMessage = "";

	PrintWriter out = res.getWriter();
	String id = "";
	String username = "", role = "";
	Enumeration values = req.getParameterNames();
	String name, value;
	String pyear="", season=""; 
	String action="";
	String ptitle="", dateNeed="", released="";
	String ids="", ht="", ryder="", otherAd="", otherAd2="";
	String season2="", ptitle2="", psa="";
	String qpage="", hpage="", fpage="",brochure="", newsLetter="";
	String a2page="",tpage="", ppage="", brochuremail="";
	String bookMark="", postCard="", otherLayout="", poster="";
	String otherLayout2="", kidsKraze="", outnAbout="", 
	    garden="", q_garden="",
	    kk_sep="",kk_oct="",kk_nov="",kk_dec="",kk_jan_feb="",
	    kk_mar="",kk_apr="",kk_may="",kk_sum="";
	String facebook = "", twitter ="";
	String spInstruction="", spInstruction2="";
	String reccal="", outcal="", sportcol="";
	String goutdoor="", message="";
	//
	//quantity vars
	String q_qpage="",q_hpage="",q_fpage="",
	    q_a2page="",q_tpage="",q_ppage="",q_opage="",
	    q_brochure="", q_mail="",q_postcard="", q_goutdoor="";
	String web="", web2="", web3="";
	//
	// added 7/2008
	String htRunDate="",htWeather="",radio="",active8="",tickets="",
	    q_tickets="",radio_tickets="",q_radio_tickets="",signage="",
	    q_signage="",wallet="",q_wallet="",
	    tshirt="",q_tshirt="",
	    yard_sign="",q_yard_sign="",class_list="",
	    other_market="";

	int afterFact=0;  // after the fact flag
	boolean	actionSet = false, success = true;
	String [] vals;
	HttpSession session = null;
	session = req.getSession(false);
	User user = null;
	Program pr = new Program(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();		    
	    if(name.equals("id")){
		id = value;
		pr.setId(value);
	    }
	    else if(name.equals("dateNeed")){
		dateNeed = value;
	    }
	    else if(name.equals("ht")){
		ht = value;
	    }
	    else if(name.equals("ids")){
		ids = value;
	    }
	    else if(name.equals("ryder")){
		ryder = value;
	    }
	    else if(name.equals("qpage")){
		qpage = value;
	    }
	    else if(name.equals("hpage")){
		hpage = value;
	    }
	    else if(name.equals("fpage")){
		fpage = value;
	    }
	    else if(name.equals("a2page")){
		a2page = value;
	    }
	    else if(name.equals("tpage")){
		tpage = value;
	    }
	    else if(name.equals("garden")){
		garden = value;
	    }
	    else if(name.equals("q_garden")){
		q_garden = value;
	    }
	    else if(name.equals("kk_sep")){
		kk_sep = value;
	    }
	    else if(name.equals("kk_oct")){
		kk_oct = value;
	    }
	    else if(name.equals("kk_nov")){
		kk_nov = value;
	    }
	    else if(name.equals("kk_dec")){
		kk_dec = value;
	    }
	    else if(name.equals("kk_jan_feb")){
		kk_jan_feb = value;
	    }
	    else if(name.equals("kk_mar")){
		kk_mar = value;
	    }
	    else if(name.equals("kk_apr")){
		kk_apr = value;
	    }
	    else if(name.equals("kk_may")){
		kk_may = value;
	    }
	    else if(name.equals("kk_sum")){
		kk_sum = value;
	    }
	    else if(name.equals("ppage")){
		ppage = value;
	    }
	    else if(name.equals("goutdoor")){
		goutdoor = value;
	    }
	    else if(name.equals("q_qpage")){
		q_qpage = value;
	    }
	    else if(name.equals("q_hpage")){
		q_hpage = value;
	    }
	    else if(name.equals("q_fpage")){
		q_fpage = value;
	    }
	    else if(name.equals("q_a2page")){
		q_a2page = value;
	    }
	    else if(name.equals("q_tpage")){
		q_tpage = value;
	    }
	    else if(name.equals("q_ppage")){
		q_ppage = value;
	    }
	    else if(name.equals("q_opage")){
		q_opage = value;
	    }
	    else if(name.equals("q_brochure")){
		q_brochure = value;
	    }
	    else if(name.equals("q_mail")){
		q_mail = value;
	    }
	    else if(name.equals("q_postcard")){
		q_postcard = value;
	    }
	    else if(name.equals("brochure")){
		brochure = value;
	    }
	    else if(name.equals("brochuremail")){
		brochuremail = value;
	    }
	    else if(name.equals("newsLetter")){
		newsLetter = value;
	    }
	    else if(name.equals("bookMark")){
		bookMark = value;
	    }
	    else if(name.equals("postCard")){ // postMark
		postCard = value;
	    }
	    else if(name.equals("poster")){ // 
		poster = value;
	    }
	    else if(name.equals("kidsKraze")){
		kidsKraze = value;
	    }
	    else if(name.equals("outnAbout")){
		outnAbout = value;
	    }
	    else if(name.equals("psa")){
		psa = value;
	    }
	    else if(name.equals("reccal")){
		reccal = value;
	    }
	    else if(name.equals("outcal")){
		outcal = value;
	    }
	    else if(name.equals("released")){
		released = value;
	    }
	    else if(name.equals("sportcol")){
		sportcol = value;
	    }
	    else if(name.equals("afterFact")){
		if(!value.equals("")){
		    try{
			afterFact = Integer.parseInt(value);
		    }catch(Exception ex){}
		}
	    }
	    else if(name.equals("otherLayout")){
		otherLayout = value;
	    }
	    else if(name.equals("otherAd")){
		otherAd = value;
	    }
	    else if(name.equals("spInstruction")){
		spInstruction = value;
	    }
	    else if(name.equals("htRunDate")){
		htRunDate = value;
	    }
	    else if(name.equals("htWeather")){
		htWeather = value;
	    }
	    else if(name.equals("radio")){
		radio = value;
	    }
	    else if(name.equals("facebook")){
		facebook = value;
	    }
	    else if(name.equals("twitter")){
		twitter = value;
	    }			
	    else if(name.equals("active8")){
		active8 = value;
	    }
	    else if(name.equals("tickets")){
		tickets = value;
	    }
	    else if(name.equals("q_tickets")){
		q_tickets = value;
	    }
	    else if(name.equals("radio_tickets")){
		radio_tickets = value;
	    }
	    else if(name.equals("q_radio_tickets")){
		q_radio_tickets = value;
	    }
	    else if(name.equals("signage")){
		signage = value;
	    }
	    else if(name.equals("q_signage")){
		q_signage = value;
	    }
	    else if(name.equals("wallet")){
		wallet = value;
	    }
	    else if(name.equals("q_wallet")){
		q_wallet = value;
	    }
	    else if(name.equals("tshirt")){
		tshirt = value;
	    }
	    else if(name.equals("q_tshirt")){
		q_tshirt = value;
	    }
	    else if(name.equals("yard_sign")){
		yard_sign = value;
	    }
	    else if(name.equals("q_yard_sign")){
		q_yard_sign = value;
	    }
	    else if(name.equals("class_list")){
		class_list = value;
	    }
	    else if(name.equals("other_market")){
		other_market = value;
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	try{
	    con = Helper.getConnection();
	    //
	    if(con != null){
		stmt = con.createStatement();
	    }
	    else{
		success = false;
		message += " Could not connect to Database";
		logger.error(message);
	    }
	}catch(Exception ex){
	    success = false;
	    message += " Could not connect to Database "+ex;
	    logger.error(ex);
	}
	if(true){
	    String back = pr.doSelect();
	    if(!back.equals("")){
		message += back;
	    }
	}
	// check if the program has market record
	// in case of startNew
	//
	if(action.startsWith("Start") || action.equals("")){
	    String qq = "select count(*) " ;
	    qq += " from programs_market where id= "+id +
		" and afterfact="+afterFact;
	    if(debug)
		logger.debug(qq);
	    try{
		rs = stmt.executeQuery(qq);
		if(rs.next()){
		    int nc = rs.getInt(1); 
		    if(nc > 0) action = "zoom";
		}
	    }
	    catch(Exception ex){
		success = false;
		message += " Could not query Database "+ex;
		logger.error(ex+":"+qq);
	    }
	}
	//
	// Adding new record
	//
	if(action.equals("Save")){
	    // new record
	    String qq = "";			
	    try{
		qq += "insert into programs_market values (";
		qq += id+",";
		if(dateNeed.equals(""))
		    qq += "null,";
		else
		    qq += "str_to_date('"+dateNeed+"','%m/%d/%Y'),";
		if(released.equals("")) // 3
		    qq += "null,";
		else
		    qq += "'y',";
		if(ht.equals(""))
		    qq += "null,";
		else
		    qq += "'y',";
		if(ids.equals("")) // 5
		    qq += " null,";
		else
		    qq += "'y',";
		if(ryder.equals(""))
		    qq += "null,";
		else
		    qq += "'y',";
		if(otherAd.equals("")) // 7
		    qq += "null,";
		else
		    qq += "'"+Helper.escapeIt(otherAd)+"',";
		if(qpage.equals(""))
		    qq += " null,";
		else
		    qq += "'y',";
		if(hpage.equals("")) // 9
		    qq += " null,";
		else
		    qq += "'y',";
		if(fpage.equals(""))
		    qq += " null,";
		else
		    qq += "'y',";	       		
		if(brochure.equals("")) // 11
		    qq += " null,";
		else
		    qq += "'y',";
		if(newsLetter.equals("")) // 12
		    qq += " null,";
		else
		    qq += "'y',";
		qq += "null,"; // tshirt  // 13
		if(bookMark.equals(""))   // 14
		    qq += " null,";
		else
		    qq += "'y',";
		if(postCard.equals(""))   // 15
		    qq += " null,";
		else
		    qq += "'y',"; 
		if(kidsKraze.equals(""))
		    qq += " null,";
		else
		    qq += "'y',";
		if(outnAbout.equals(""))
		    qq += " null,";
		else
		    qq += "'y',"; // 17
		if(otherLayout.equals("")) // 18
		    qq += "null,";
		else
		    qq += "'"+Helper.escapeIt(otherLayout)+"',";
		if(spInstruction.equals(""))  // 19
		    qq += " null,";
		else
		    qq += "'"+Helper.escapeIt(spInstruction)+"',";
		// added later
		if(tpage.equals("")) // 20
		    qq += " null,";
		else
		    qq += "'y',";
		if(ppage.equals("")) // 21
		    qq += " null,";
		else
		    qq += "'y',";	
		if(brochuremail.equals("")) // 22)
		    qq += " null,";
		else
		    qq += "'y',";	
		if(poster.equals("")) //
		    qq += " null,";
		else
		    qq += "'y',";
		if(psa.equals("")) // 24
		    qq += " null,";
		else
		    qq += "'y',";
		if(reccal.equals("")) // 25
		    qq += " null,";
		else
		    qq += "'y',";
		if(outcal.equals("")) // 26
		    qq += " null,";
		else
		    qq += "'y',";
		if(sportcol.equals("")) // 27
		    qq += " null,";
		else
		    qq += "'y',";
		qq +=  afterFact+",";	  // 28
		if(a2page.equals("")) // 29
		    qq += " null,";
		else
		    qq += "'y',";
		if(goutdoor.equals("")) // 29
		    qq += " null,";
		else
		    qq += "'y',";
		if(q_qpage.equals("")) //  30
		    qq += " null,";
		else
		    qq += ""+q_qpage+",";
		if(q_hpage.equals("")) // 31
		    qq += " null,";
		else
		    qq += ""+q_hpage+",";
		if(q_fpage.equals("")) // 32
		    qq += " null,";
		else
		    qq += ""+q_fpage+",";
		if(q_a2page.equals("")) // 33
		    qq += " null,";
		else
		    qq += ""+q_a2page+",";
		if(q_tpage.equals("")) // 34
		    qq += " null,";
		else
		    qq += ""+q_tpage+",";
		if(q_ppage.equals("")) // 35
		    qq += " null,";
		else
		    qq += ""+q_ppage+",";
		if(q_opage.equals("")) // 36
		    qq += " null,";
		else
		    qq += ""+q_opage+",";
		if(q_brochure.equals("")) //  37
		    qq += " null,";
		else
		    qq += ""+q_brochure+",";
		if(q_mail.equals(""))     // 38
		    qq += " null,";
		else
		    qq += ""+q_mail+",";
		if(q_postcard.equals("")) // 39
		    qq += " null,";
		else
		    qq += ""+q_postcard+",";
		if(q_goutdoor.equals("")) // 40
		    qq += " null,";
		else
		    qq += ""+q_goutdoor+",";
		if(garden.equals("")) // 29
		    qq += " null,";
		else
		    qq += "'y',";
		if(q_garden.equals("")) // 29
		    qq += " null,";
		else
		    qq += ""+q_garden+",";
		if(kk_sep.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(kk_oct.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(kk_nov.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(kk_dec.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(kk_jan_feb.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(kk_mar.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(kk_apr.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(kk_may.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(kk_sum.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(htRunDate.equals(""))
		    qq += "null,";
		else
		    qq += "str_to_date('"+htRunDate+"','%m/%d/%Y'),";
		if(htWeather.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(radio.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(active8.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(tickets.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(q_tickets.equals("")) 
		    qq += " null,";
		else
		    qq += q_tickets+",";
		if(radio_tickets.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(q_radio_tickets.equals("")) 
		    qq += " null,";
		else
		    qq += q_radio_tickets+",";
		if(signage.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(q_signage.equals("")) 
		    qq += " null,";
		else
		    qq += q_signage+",";
		if(wallet.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(q_wallet.equals("")) 
		    qq += " null,";
		else
		    qq += q_wallet+",";
		if(q_tshirt.equals("")) 
		    qq += " null,";
		else
		    qq += q_tshirt+",";
		if(yard_sign.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(q_yard_sign.equals("")) 
		    qq += " null,";
		else
		    qq += q_yard_sign+",";
		if(class_list.equals("")) // 7
		    qq += "null,";
		else
		    qq += "'"+Helper.escapeIt(class_list)+"',";
		if(other_market.equals("")) // 7
		    qq += "null,";
		else
		    qq += "'"+Helper.escapeIt(other_market)+"',";
		if(facebook.equals("")) 
		    qq += " null,";
		else
		    qq += "'y',";
		if(twitter.equals("")) 
		    qq += " null";
		else
		    qq += "'y'";		
		qq += ")";
		if(user.canEdit()){
		    if(debug){
			logger.debug(qq);
		    }
		    stmt.executeUpdate(qq);
		    message = "Record saved";
		}
		else{
		    message += "You could not save ";
		    success = false;
		}
	    }
	    catch(Exception ex){
		logger.error(ex+":"+qq);
		message += " Could not save "+ex;
		success = false;
	    }
	}
	else if (action.equals("Update")){

	    String qq = "";
	    qq += " update programs_market set ";
	    if(dateNeed.equals(""))
		qq += "dateNeed=null,";
	    else
		qq += "dateNeed=str_to_date('"+dateNeed+"','%m/%d/%Y'),";
	    if(released.equals(""))
		qq += "released=null,";
	    else
		qq += "released='y',";
	    if(ht.equals(""))
		qq += "ht=null,";
	    else
		qq += "ht='y',";
	    if(ids.equals(""))
		qq += "ids=null,";
	    else
		qq += "ids='y',";
	    if(ryder.equals(""))
		qq += "ryder=null,";
	    else
		qq += "ryder='y',";
	    if(garden.equals(""))
		qq += "garden=null,";
	    else
		qq += "garden='y',";
	    if(q_garden.equals(""))
		qq += "q_garden=null,";
	    else
		qq += "q_garden="+q_garden+",";
	    if(kk_sep.equals(""))
		qq += "kk_sep=null,";
	    else
		qq += "kk_sep='y',";
	    if(kk_oct.equals(""))
		qq += "kk_oct=null,";
	    else
		qq += "kk_oct='y',";
	    if(kk_nov.equals(""))
		qq += "kk_nov=null,";
	    else
		qq += "kk_nov='y',";
	    if(kk_dec.equals(""))
		qq += "kk_dec=null,";
	    else
		qq += "kk_dec='y',";
	    if(kk_jan_feb.equals(""))
		qq += "kk_jan_feb=null,";
	    else
		qq += "kk_jan_feb='y',";
	    if(kk_mar.equals(""))
		qq += "kk_mar=null,";
	    else
		qq += "kk_mar='y',";
	    if(kk_apr.equals(""))
		qq += "kk_apr=null,";
	    else
		qq += "kk_apr='y',";
	    if(kk_may.equals(""))
		qq += "kk_may=null,";
	    else
		qq += "kk_may='y',";
	    if(kk_sum.equals(""))
		qq += "kk_sum=null,";
	    else
		qq += "kk_sum='y',";
	    if(otherAd.equals(""))
		qq += "otherAd=null,";
	    else
		qq += "otherAd='"+Helper.escapeIt(otherAd)+"',";
	    if(qpage.equals(""))
		qq += "qpage=null,";
	    else
		qq += "qpage='y',";
	    if(hpage.equals(""))
		qq += "hpage=null,";
	    else
		qq += "hpage='y',";
	    if(fpage.equals(""))
		qq += "fpage=null,";
	    else
		qq += "fpage='y',";
	    if(a2page.equals(""))
		qq += "a2page=null,";
	    else
		qq += "a2page='y',";
	    if(tpage.equals(""))
		qq += "tpage=null,";
	    else
		qq += "tpage='y',";
	    if(ppage.equals(""))
		qq += "ppage=null,";
	    else
		qq += "ppage='y',";
	    if(facebook.equals(""))
		qq += "facebook=null,";
	    else
		qq += "facebook='y',";
	    if(twitter.equals(""))
		qq += "twitter=null,";
	    else
		qq += "twitter='y',";			
	    if(q_qpage.equals(""))
		qq += "q_qpage=null,";
	    else
		qq += "q_qpage="+q_qpage+",";
	    if(q_hpage.equals(""))
		qq += "q_hpage=null,";
	    else
		qq += "q_hpage="+q_hpage+",";
	    if(q_fpage.equals(""))
		qq += "q_fpage=null,";
	    else
		qq += "q_fpage="+q_fpage+",";
	    if(q_a2page.equals(""))
		qq += "q_a2page=null,";
	    else
		qq += "q_a2page="+q_a2page+",";
	    if(q_tpage.equals(""))
		qq += "q_tpage=null,";
	    else
		qq += "q_tpage="+q_tpage+",";
	    if(q_ppage.equals(""))
		qq += "q_ppage=null,";
	    else
		qq += "q_ppage="+q_ppage+",";
	    if(q_opage.equals(""))
		qq += "q_opage=null,";
	    else
		qq += "q_opage="+q_opage+",";
	    if(q_brochure.equals(""))
		qq += "q_brochure=null,";
	    else
		qq += "q_brochure="+q_brochure+",";
	    if(q_mail.equals(""))
		qq += "q_mail=null,";
	    else
		qq += "q_mail="+q_mail+",";
	    if(q_postcard.equals(""))
		qq += "q_postcard=null,";
	    else
		qq += "q_postcard="+q_postcard+",";
	    if(goutdoor.equals(""))
		qq += "goutdoor=null,";
	    else
		qq += "goutdoor='y',";
	    if(q_goutdoor.equals(""))
		qq += "q_goutdoor=null,";
	    else
		qq += "q_goutdoor='"+q_goutdoor+"',";
	    if(brochure.equals(""))
		qq += "brochure=null,";
	    else
		qq += "brochure='y',";
	    if(brochuremail.equals(""))
		qq += "brochuremail=null,";
	    else
		qq += "brochuremail='y',";
	    if(newsLetter.equals(""))
		qq += "newsLetter=null,";
	    else
		qq += "newsLetter='y',";
	    if(bookMark.equals(""))
		qq += "bookMark=null,";
	    else
		qq += "bookMark='y',";
	    if(postCard.equals(""))
		qq += "postCard=null,";
	    else
		qq += "postCard='y',";
	    if(poster.equals(""))
		qq += "poster=null,";
	    else
		qq += "poster='y',";
	    if(psa.equals(""))
		qq += "psa=null,";
	    else
		qq += "psa='y',";
	    if(reccal.equals(""))
		qq += "reccal=null,";
	    else
		qq += "reccal='y',";
	    if(outcal.equals(""))
		qq += "outcal=null,";
	    else
		qq += "outcal='y',";
	    if(sportcol.equals(""))
		qq += "sportcol=null,";
	    else
		qq += "sportcol='y',";
	    if(kidsKraze.equals(""))
		qq += "kidsKraze=null,";
	    else
		qq += "kidsKraze='y',";
	    if(outnAbout.equals(""))
		qq += "outnAbout=null,";
	    else
		qq += "outnAbout='y',";
	    if(otherLayout.equals(""))
		qq += "otherLayout=null,";
	    else
		qq += "otherLayout='"+Helper.escapeIt(otherLayout)+"',";
	    if(spInstruction.equals(""))
		qq += "spInstruction=null,";
	    else
		qq += "spInstruction='"+Helper.escapeIt(spInstruction)+"',";
	    if(htRunDate.equals(""))
		qq += "htRunDate=null,";
	    else
		qq += "htRunDate=str_to_date('"+htRunDate+"','%m/%d/%Y'),";
	    if(htWeather.equals("")) 
		qq += " htWeather=null,";
	    else
		qq += "htWeather='y',";
	    if(radio.equals("")) 
		qq += " radio=null,";
	    else
		qq += "radio='y',";
	    if(active8.equals("")) 
		qq += " active8=null,";
	    else
		qq += "active8='y',";
	    if(tickets.equals("")) 
		qq += " tickets=null,";
	    else
		qq += "tickets='y',";
	    if(q_tickets.equals("")) 
		qq += " q_tickets=null,";
	    else
		qq += " q_tickets="+q_tickets+",";
	    if(radio_tickets.equals("")) 
		qq += " radio_tickets=null,";
	    else
		qq += "radio_tickets='y',";
	    if(q_radio_tickets.equals("")) 
		qq += " q_radio_tickets=null,";
	    else
		qq += "q_radio_tickets="+q_radio_tickets+",";
	    if(signage.equals("")) 
		qq += " signage=null,";
	    else
		qq += "signage='y',";
	    
	    if(q_signage.equals("")) 
		qq += " q_signage=null,";
	    else
		qq += " q_signage="+q_signage+",";
	    if(wallet.equals("")) 
		qq += " wallet=null,";
	    else
		qq += "wallet='y',";
	    if(q_wallet.equals("")) 
		qq += " q_wallet=null,";
	    else
		qq += "q_wallet="+q_wallet+",";
	    if(tshirt.equals("")) 
		qq += " tshirt=null,";
	    else
		qq += "tshirt='y',";
	    if(q_tshirt.equals("")) 
		qq += " q_tshirt=null,";
	    else
		qq += "q_tshirt="+q_tshirt+",";
	    if(yard_sign.equals("")) 
		qq += " yard_sign=null,";
	    else
		qq += "yard_sign='y',";
	    if(q_yard_sign.equals("")) 
		qq += " q_yard_sign=null,";
	    else
		qq += "q_yard_sign="+q_yard_sign+",";
	    if(class_list.equals("")) // 7
		qq += "class_list=null,";
	    else
		qq += "class_list='"+Helper.escapeIt(class_list)+"',";
	    if(other_market.equals("")) // 7
		qq += "other_market=null";
	    else
		qq += "other_market='"+Helper.escapeIt(other_market)+"'";
	    qq += " where id = "+id +" and afterfact="+afterFact;

	    try {
		if(user.canEdit()){
		    if(debug){
			logger.debug(qq);
		    }				
		    stmt.executeUpdate(qq);
		    message = "Record updated";
		}
		else{
		    message += " You could not update ";
		    success = false;
		}
	    }
	    catch (Exception ex){
		logger.error(ex+":"+qq);
		message += " Could not update "+ex;
		success = false;
	    }
	}
	else if (action.equals("Delete")){
	    //
	    String query ="";
	    query += "delete from programs_market where id= "+id;
	    query += " and afterfact="+afterFact;
	    //
	    try{
		if(user.canDelete()){
		    if(debug){
			logger.debug(query);
		    }
		    stmt.executeUpdate(query);
		    message = "Record deleted";
		}
		else{
		    message += " Could not delete ";
		    success = false;
		}
	    }
	    catch(Exception ex){
		logger.error(ex+":"+query);
		message += " Could not delete "+ex;
		success = false;
	    }
	}
	else if (action.equals("zoom")){
	    //
	    // case from browsing
	    //
	    String query = "select " +
		" date_format(dateNeed, '%m/%d/%Y'),released,"+
		" ht,ids,ryder,otherAd,qpage,hpage,fpage,a2page,tpage,ppage,"+
		" brochure,brochuremail, "+ // 14
		" newsLetter,bookMark,postCard,kidsKraze,outnAbout,"+
		" otherLayout,spInstruction,poster,psa, "+
		" reccal,outcal,sportcol, "+
		" goutdoor,q_qpage,q_hpage,q_fpage,q_a2page,q_tpage,"+
		" q_ppage,q_opage,q_brochure,q_mail,q_postcard,q_goutdoor,"+
		" garden,q_garden,"+
		" kk_sep,kk_oct,kk_nov,kk_dec,kk_jan_feb,kk_mar,"+
		" kk_apr,kk_may,kk_sum, "+

		" date_format(htRunDate, '%m/%d/%Y'),"+
		" htWeather,radio,active8,tickets,q_tickets,"+
		" radio_tickets,q_radio_tickets,signage,q_signage,wallet,"+
		" q_wallet,q_tshirt,yard_sign,q_yard_sign,class_list,"+
		" other_market,tshirt, "+
		" facebook,twitter ";
	    query += " from programs_market where id= "+id+
		" and afterFact="+afterFact;
	    if(debug){
		logger.debug(query);
	    }
	    try{
		rs = stmt.executeQuery(query);
		//
		if(rs.next()){
		    String str = rs.getString(1);
		    if(str != null) dateNeed = str;
		    str = rs.getString(2);
		    if(str != null) released = str;
		    str = rs.getString(3);
		    if(str != null) ht = str;
		    str = rs.getString(4);
		    if(str != null) ids = str;
		    str = rs.getString(5);
		    if(str != null) ryder = str;
		    str = rs.getString(6);
		    if(str != null) otherAd = str;
		    str = rs.getString(7);
		    if(str != null) qpage = str;
		    str = rs.getString(8);
		    if(str != null) hpage = str;
		    str = rs.getString(9);
		    if(str != null) fpage = str;
		    str = rs.getString(10);
		    if(str != null) a2page = str;
		    str = rs.getString(11);
		    if(str != null) tpage = str;
		    str = rs.getString(12);
		    if(str != null) ppage = str;
		    str = rs.getString(13);
		    if(str != null) brochure = str;
		    str = rs.getString(14);
		    if(str != null) brochuremail = str;
		    str = rs.getString(15);
		    if(str != null) newsLetter = str;
		    str = rs.getString(16);
		    if(str != null) bookMark = str;
		    str = rs.getString(17);
		    if(str != null) postCard = str;
		    str = rs.getString(18);
		    if(str != null) kidsKraze = str;
		    str = rs.getString(19);
		    if(str != null) outnAbout = str;
		    str = rs.getString(20);
		    if(str != null) otherLayout = str;
		    str = rs.getString(21);
		    if(str != null) spInstruction = str;
		    str = rs.getString(22);
		    if(str != null) poster = str;
		    str = rs.getString(23);
		    if(str != null) psa = str;
		    str = rs.getString(24);
		    if(str != null) reccal = str;
		    str = rs.getString(25);
		    if(str != null) outcal = str;
		    str = rs.getString(26);
		    if(str != null) sportcol = str;
		    str = rs.getString(27);
		    if(str != null) goutdoor = str;
		    str = rs.getString(28);
		    if(str != null) q_qpage = str;
		    str = rs.getString(29);
		    if(str != null) q_hpage = str;
		    str = rs.getString(30);
		    if(str != null) q_fpage = str;
		    str = rs.getString(31);
		    if(str != null) q_a2page = str;
		    str = rs.getString(32);
		    if(str != null) q_tpage = str;
		    str = rs.getString(33);
		    if(str != null) q_ppage = str;
		    str = rs.getString(34);
		    if(str != null) q_opage = str;
		    str = rs.getString(35);
		    if(str != null) q_brochure = str;
		    str = rs.getString(36);
		    if(str != null) q_mail = str;
		    str = rs.getString(37);
		    if(str != null) q_postcard = str;
		    str = rs.getString(38);
		    if(str != null) q_goutdoor = str;
		    str = rs.getString(39);
		    if(str != null) garden = str;
		    str = rs.getString(40);
		    if(str != null) q_garden = str;
		    str = rs.getString(41);
		    if(str != null) kk_sep = str;
		    str = rs.getString(42);
		    if(str != null) kk_oct = str;
		    str = rs.getString(43);
		    if(str != null) kk_nov = str;
		    str = rs.getString(44);
		    if(str != null) kk_dec = str;
		    str = rs.getString(45);
		    if(str != null) kk_jan_feb = str;
		    str = rs.getString(46);
		    if(str != null) kk_mar = str;
		    str = rs.getString(47);
		    if(str != null) kk_apr = str;
		    str = rs.getString(48);
		    if(str != null) kk_may = str;
		    str = rs.getString(49);
		    if(str != null) kk_sum = str;
		    str = rs.getString(50);
		    if(str != null)  htRunDate= str;
		    str = rs.getString(51);
		    if(str != null) htWeather = str;
		    str = rs.getString(52);
		    if(str != null) radio = str;
		    str = rs.getString(53);
		    if(str != null) active8 = str;
		    str = rs.getString(54);
		    if(str != null) tickets = str;
		    str = rs.getString(55);
		    if(str != null) q_tickets = str;
		    str = rs.getString(56);
		    if(str != null) radio_tickets = str;
		    str = rs.getString(57);
		    if(str != null) q_radio_tickets = str;
		    str = rs.getString(58);
		    if(str != null) signage = str;
		    str = rs.getString(59);
		    if(str != null) q_signage = str;
		    str = rs.getString(60);
		    if(str != null) wallet = str;
		    str = rs.getString(61);
		    if(str != null) q_wallet = str;
		    str = rs.getString(62);
		    if(str != null) q_tshirt = str;
		    str = rs.getString(63);
		    if(str != null) yard_sign = str;
		    str = rs.getString(64);
		    if(str != null) q_yard_sign = str;
		    str = rs.getString(65);
		    if(str != null) class_list = str;
		    str = rs.getString(66);
		    if(str != null) other_market = str;
		    str = rs.getString(67);
		    if(str != null) tshirt = str;
		    str = rs.getString(68);
		    if(str != null) facebook = str;
		    str = rs.getString(69);
		    if(str != null) twitter = str;
		}
	    }
	    catch(Exception ex){
		logger.error(ex+":"+query);
		message += " Could not retreive data "+ex;
	    }
	}
	if(!released.equals("")) released = "checked";
	if(!ht.equals("")) ht = "checked";
	if(!ids.equals("")) ids = "checked";
	if(!ryder.equals("")) ryder = "checked";
	if(!qpage.equals("")) qpage = "checked";
	if(!hpage.equals("")) hpage = "checked";
	if(!fpage.equals("")) fpage = "checked";
	if(!a2page.equals("")) a2page = "checked";
	if(!tpage.equals("")) tpage = "checked";
	if(!ppage.equals("")) ppage = "checked";
	if(!brochure.equals("")) brochure = "checked";
	if(!brochuremail.equals("")) brochuremail = "checked";
	if(!newsLetter.equals("")) newsLetter = "checked";
	if(!bookMark.equals("")) bookMark = "checked";
	if(!postCard.equals("")) postCard = "checked";
	if(!kidsKraze.equals("")) kidsKraze = "checked";
	if(!outnAbout.equals("")) outnAbout = "checked";
	if(!poster.equals("")) poster = "checked";
	if(!psa.equals("")) psa = "checked";
	if(!reccal.equals("")) reccal = "checked";
	if(!outcal.equals("")) outcal = "checked";
	if(!sportcol.equals("")) sportcol = "checked";
	if(!goutdoor.equals("")) goutdoor = "checked";

	if(!garden.equals("")) garden = "checked";
	if(!kk_sep.equals("")) kk_sep = "checked";
	if(!kk_oct.equals("")) kk_oct = "checked";
	if(!kk_nov.equals("")) kk_nov = "checked";
	if(!kk_dec.equals("")) kk_dec = "checked";
	if(!kk_jan_feb.equals("")) kk_jan_feb = "checked";
	if(!kk_mar.equals("")) kk_mar = "checked";
	if(!kk_apr.equals("")) kk_apr = "checked";
	if(!kk_may.equals("")) kk_may = "checked";
	if(!kk_sum.equals("")) kk_sum = "checked";

	if(!htWeather.equals("")) htWeather = "checked";
	if(!radio.equals("")) radio = "checked";
	if(!active8.equals("")) active8 = "checked";
	if(!tickets.equals("")) tickets = "checked";
	if(!radio_tickets.equals("")) radio_tickets = "checked";
	if(!signage.equals("")) signage = "checked";
	if(!wallet.equals("")) wallet = "checked";
	if(!yard_sign.equals("")) yard_sign = "checked";
	if(!tshirt.equals("")) tshirt = "checked";
	if(!facebook.equals("")) facebook = "checked";
	if(!twitter.equals("")) twitter = "checked";
	//
	// else start empty form, startNew
	//
	out.println("<html>");
	out.println("<head><title>City of Bloomington Parks and "+
		    "Recreation</title>");
	//
	// This script validate 
	//
      	out.println(" <script type=\"text/javascript\">");
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
        out.println("    if (ss.value.length > 500 ){  "); 
	out.println("       alert(\"Text Area should not be more than"+
		    " 500 chars\"); ");
	out.println("       ss.value = ss.value.substring(0,500); ");
	out.println("      ss.focus();                            ");
	out.println("      ss.select();                           ");
	out.println("       }                                     ");
	out.println("   }                                         ");
	out.println("  function validateForm(){                   ");    
	out.println("  var x=\"\";                                ");
	out.println("  with(document.myForm){               ");
	out.println(" if(dateNeed.value.length > 0){        ");  
	out.println("   if(!(dateNeed.value.length == 10)){ ");
	out.println("       alert(\"Date should be in mm/dd/yyyy format\"); ");
	out.println("	     return false;		    ");
	out.println(" 	   }                                ");
	out.println("   if(!validateInteger(dateNeed.value.substring(0,2)) || ");
	out.println("   !validateInteger(dateNeed.value.substring(3,5)) || "); 
	out.println("   !validateInteger(dateNeed.value.substring(6,10))){ "); 
	out.println("  alert(\"Date should be in mm/dd/yyyy format\"); ");
	out.println("	     return false;		");
	out.println(" 	       }                        ");
	out.println(" 	    }                           ");
	out.println(" if(!checkNumber(q_qpage)||        ");  
	out.println(" !checkNumber(q_hpage)  ||         ");  
	out.println(" !checkNumber(q_fpage)  ||         ");  
	out.println(" !checkNumber(q_tpage)  ||         ");  
	out.println(" !checkNumber(q_opage)  ||         ");  
	out.println(" !checkNumber(q_a2page) ||         ");  
	out.println(" !checkNumber(q_ppage)){           ");  
	out.println("	     return false;	        ");
	out.println("  	   }                            ");
	out.println("    }	                        ");
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
	out.println(" </script>                         ");   
	out.println("</head><body>");
	out.println("<center>");
	if(action.equals("") || 
	   action.equals("delete")){
	    out.println("<h2>Add Marketing </h2>");
	}
	else // add update
	    out.println("<h2>Edit Marketing </h2>");
	if(afterFact == 1){
	    out.println("<h3><font color=blue><center>Achieved Marketing Operations</font></h3>");
	}
	out.println("<br>");
	if(!message.equals("")){
	    if(success)
		out.println("<font color=green>"+message+"</font><br>");
	    else
		out.println("<font color=red>"+message+"</font><br>");
	}
	//
	out.println("<table border align=center>");
	out.println("<tr bgcolor=#CDC9A3><td>");
	out.println("<table>");
	out.println("<form name=myForm method=post "+
		    "onSubmit=\"return validateForm()\">");
	out.println("<input type=hidden name=id value=" + id + ">");
	out.println("<input type=hidden name=afterFact value="+afterFact+">");
	//
	// program, year, season
	out.println("<tr><td align=right><b>Program");
	out.println("</b></td><td>");
	out.println(pr.getTitle()+",&nbsp;");
	out.print("(");
	out.print(pr.getSeason()+"/");
	out.print(pr.getYear());
	out.println(")</td></tr>");
	//
	out.println("<tr><td align=right><b>Program ID:");//same as program
	out.println("</b></td><td><left>"+id);
	//
	out.println("</left></td></tr>");
	// beside record id
	//
	// Due date
	out.println("<tr><td align=right><b>Due date:</b></td><td>");
	out.println("<input name=dateNeed maxlength=10 size=10 value=\""+
		    dateNeed+"\">");
	out.println("<input type=button onClick=\""+
		    "window.open('"+url+"PickDate?wdate=dateNeed"+
		    "','Date',"+
		    "'toolbar=0,location=0,"+
		    "directories=0,status=0,menubar=0,"+
		    "scrollbars=0,top=300,left=300,"+
		    "resizable=1,width=300,height=250');\""+
		    " value=\"^\"></td></tr>");
	//
	// Program Ad
	out.println("<tr><td align=right><b>Program Ad: ");
	out.println("</b></td><td><left>");
	out.println("<input type=checkbox name=ht value=y "+
		    ht+"></input><b>HT</b> Run date:");
	out.println("<input type=text name=htRunDate size='10' value='"+
		    htRunDate+"'></input>");
	out.println("<input type=button onClick=\""+
		    "window.open('"+url+"PickDate?wdate=htRunDate"+
		    "','Date',"+
		    "'toolbar=0,location=0,"+
		    "directories=0,status=0,menubar=0,"+
		    "scrollbars=0,top=300,left=300,"+
		    "resizable=1,width=300,height=250');\""+
		    " value=\"^\">");

	out.println("<input type=checkbox name=htWeather value=y "+
		    htWeather+"></input><b>HT Weather Page</b>");
	out.println("<input type=checkbox name=radio value=y "+
		    radio+"></input><b>Radio</b>");
	out.println("</td></tr>");
	out.println("<tr><td></td><td><left>");
	//
	out.println("<input type=checkbox name=ids value=y "+
		    ids+"></input><b>IDS</b>");
	out.println("<input type=checkbox name=ryder value=y "+
		    ryder+"></input><b>Ryder,</b>&nbsp;&nbsp;");
	out.println("<b>Other</b> specify");
	out.println("<input type=text name=otherAd maxlength=20 value=\""+ 
		    otherAd+"\" size=20>");
	out.println("</left></td></tr>");
	//
	// marketing table
	out.println("<tr><td align=right valign=top><b>Marketing :<b>");
	out.println("</td><td>");
	//
	// table with 3 columns
	out.println("<table><tr><td>Type</td><td>Quantity</td>"+
		    "<td>Type</td></tr>");

	//
	// Fliers
	out.println("<tr><td>");
	out.println("<input type=checkbox name=qpage value=y "+
		    qpage+"></input><b>1/4-page flier</b></td><td>");
	out.println("<input name=q_qpage value=\""+q_qpage+
		    "\" maxlength=4 size=4></td><td> ");
	/*;
	  out.println("<input type=checkbox name=active8 value=y "+
	  active8+"></input><b>Active8! flier</b>");
	*/
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=checkbox name=hpage value=y "+
		    hpage+"></input><b>1/2-page flier</b></td><td>");
	out.println("<input name=q_hpage value=\""+q_hpage+
		    "\" maxlength=4 size=4></td><td>"); 
	out.println("<input type=checkbox name=goutdoor value=y "+
		    goutdoor+"></input><b>The Great Outdoor flier</b>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=checkbox name=fpage value=y "+
		    fpage+"></input><b>full-page flier</b></td><td>");
	out.println("<input name=q_fpage value=\""+q_fpage+
		    "\" maxlength=4 size=4></td><td>"); 
	out.println("<input type=checkbox name=ppage value=y "+
		    ppage+"></input><b>Preschool Possibilities flier</b>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=checkbox name=brochuremail value=y "+
		    brochuremail +"><b>full-page mailer </b>");
	out.println("</td><td>");
	out.println("<input name=q_mail value=\""+q_mail+"\" size=4"+
		    " maxlength=4></td><td>");
	out.println("<input type=checkbox name=garden value=y "+
		    garden+"></input><b>Gardening Classes flier</b>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=checkbox name=brochure value=y "+
		    brochure +"></input><b>trifold handout</b>");
	out.println("</td><td>");
	out.println("<input name=q_brochure value=\""+q_brochure+"\" size=4"+
		    " maxlength=4></td><td rowspan=4>");
	out.println("<input type=checkbox name=kidsKraze value=y "+
		    kidsKraze+"></input>"+
		    "<b>Kid's Kraze, </b>issue of: <br>");
	out.println("<table><tr><td>&nbsp;</td><td>");
	out.println("<input type=checkbox name=kk_sep value=y "+
		    kk_sep+"/>Sept.</td><td>");
	out.println("<input type=checkbox name=kk_oct value=y "+
		    kk_oct+"/>Oct.</td><td>");
	out.println("<input type=checkbox name=kk_nov value=y "+
		    kk_nov+"/>Nov.</td></tr>");
	out.println("<tr><td>&nbsp;</td><td>");
	out.println("<input type=checkbox name=kk_dec value=y "+
		    kk_dec+"/>Dec.</td><td>");
	out.println("<input type=checkbox name=kk_jan_feb value=y "+
		    kk_jan_feb+"/>Jan./Feb.</td><td>");
	out.println("<input type=checkbox name=kk_mar value=y "+
		    kk_mar+"/>March</td></tr>");
	out.println("<tr><td>&nbsp;</td><td>");
	out.println("<input type=checkbox name=kk_apr value=y "+
		    kk_apr+"/>April</td><td>");
	out.println("<input type=checkbox name=kk_may value=y "+
		    kk_may+"/>May</td><td>");
	out.println("<input type=checkbox name=kk_sum value=y "+
		    kk_sum+"/>Summer</td></tr>");
	out.println("</table></td></tr>");
	//
	out.println("<tr><td>");
	out.println("<input type=checkbox name=a2page value=y "+
		    a2page+"></input><b>11x17 poster</b></td><td>");
	out.println("<input name=q_a2page value=\""+q_a2page+
		    "\" maxlength=4 size=4></td></tr>"); 
	//
	out.println("<tr><td>");
	out.println("<input type=checkbox name=postCard value=y "+
		    postCard+"><b>postcard</b>");
	out.println("</td><td>");
	out.println("<input name=q_postcard value=\""+q_postcard+"\" size=4"+
		    " maxlength=4></td></tr>");
	//
	out.println("<tr><td>");
	out.println("<input type=checkbox name=tickets value=y "+
		    tickets+"><b>tickets/passes</b>");
	out.println("</td><td>");
	out.println("<input name=q_tickets value=\""+q_tickets+"\" size=4"+
		    " maxlength=4></td></tr>");
	//
	out.println("<tr><td>");
	out.println("<input type=checkbox name=radio_tickets value=y "+
		    radio_tickets+"><b>radio giveaway tickets/passes</b>");
	out.println("</td><td>");
	out.println("<input name=q_radio_tickets value=\""+
		    q_radio_tickets+"\" size=4"+
		    " maxlength=4></td></tr>");
	//
	out.println("<tr><td>");
	out.println("<input type=checkbox name=signage value=y "+
		    signage+"><b>signage</b>");
	out.println("</td><td>");
	out.println("<input name=q_signage value=\""+q_signage+"\" size=4"+
		    " maxlength=4></td></tr>");
	out.println("<tr><td>");
	out.println("<input type=checkbox name=wallet value=y "+
		    wallet+"><b>wallet card</b>");
	out.println("</td><td>");
	out.println("<input name=q_wallet value=\""+q_wallet+"\" size=4"+
		    " maxlength=4></td></tr>");
	//
	out.println("<tr><td>");
	out.println("<input type=checkbox name=tshirt value=y "+
		    tshirt+"><b>T-shirt</b>");
	out.println("</td><td>");
	out.println("<input name=q_tshirt value=\""+q_tshirt+"\" size=4"+
		    " maxlength=4></td></tr>");
	//
	out.println("<tr><td>");
	out.println("<input type=checkbox name=yard_sign value=y "+
		    yard_sign+"><b>yard sign</b>");
	out.println("</td><td>");
	out.println("<input name=q_yard_sign value=\""+q_yard_sign+"\" size=4"+
		    " maxlength=4></td></tr>");
	//
	out.println("<tr><td>");
	out.println("<b>Does the marketing piece <br />combine classes or programs?<br />Please list which ones should <br />appear </b>(example: two classes sharing <br />one flier):</td><td colspan='2'>");
	out.println("<textarea name='class_list' rows='5' cols='35' wrap>");
	out.println(class_list);
	out.println("</textarea></td></tr>");
	out.println("<tr><td colspan='3'>");
	out.println("<table><tr><td valign='top'><b>Other </b></td><td>");
	out.println("<textarea name='other_market' rows='5' cols='62' wrap>");
	out.println(other_market);
	out.println("</textarea></td></tr></table></td></tr>");
	//
	out.println("</table></td></tr>"); // end of marketing table
	//
	// Announcement
	out.println("<tr><td align=right valign=top><b>Announcements:</b>");
	out.println("</td><td><left>");
	//
	// announcement table
	out.println("<table><tr><td>");
	out.println("<input type=checkbox name=outnAbout value=y "+
		    outnAbout+"></input><b>Out and About</b>");
	out.println("<input type=checkbox name=outcal value=y "+
		    outcal+"></input><b>Outdoor Calendar</b> ");
	out.println("<input type=checkbox name=sportcol value=y "+
		    sportcol+"></input><b>Youth Sports Column</b><br> ");
	out.println("</td></tr>");
	//
	out.println("<tr><td>");
	out.println("<input type=checkbox name=released value=y "+
		    released+"></input>");
	out.println("<b>News Release</b>");
	out.println("<input type=checkbox name=facebook value=y "+
		    facebook+"></input>");
	out.println("<b>Facebook</b>");				
	out.println("<input type=checkbox name=twitter value=y "+
		    twitter+"></input>");
	out.println("<b>Twitter</b>");	
	out.println("</td></tr>");

	//
	// Special Instruction
	out.println("<tr><td><table><td valign='top'>");
	out.println("<b>Special <br />Instructions</b></td><td>");
	out.println("<textarea rows=5 cols=57 name=spInstruction wrap "+
		    "onChange=\"validateTextarea(this)\">"); 
	out.println(spInstruction);
	out.println("</textarea>");
	out.println("</td></tr></table></td></tr>");
	out.println("</table></td></tr>");
	//
	// check action type
	//
	if(action.equals("") || 
	   action.equals("delete")){
	    if(user.canEdit()){
		out.println("<tr><td colspan=2 align=right><input type=submit "+
			    "name=action value=Save>&nbsp;&nbsp;"+
			    "&nbsp;&nbsp;&nbsp;" +
			    "&nbsp;&nbsp;<input type=reset value=Clear>"+
			    "</td></tr>");
	    }
	    out.println("</form>");
	}
	else{ // add zoom update
	    //
	    out.println("<tr>");
	    if(user.canEdit()){
		out.println("<td align=right><input type=submit "+
			    "name=action value=Update> "+
			    "&nbsp;&nbsp;&nbsp;&nbsp;" +
			    "</td>");
	    }
	    out.println("</form>");
	    //
	    if(user.canDelete()){
		out.println("<form name=myForm2 method=post "+
			    "onSubmit=\"return validateDeleteForm()\">");
		out.println("<input type=hidden name=id value=" + id + ">");
		out.println("<input type=hidden name=afterFact value="+afterFact+">");
		out.println("<td align=right>");
		out.println("&nbsp;&nbsp;<input type=submit name=action "+
			    "value=Delete>");
		out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>");
		out.println("</form>");	
	    }
	    out.println("</tr>");
	}	    
	out.println("</table>");
	out.println("</td></tr>");
	out.println("</table>");
	//
	// put all the old marketing if any, in one table
	//
	out.println("<HR>");
	out.println("<HR>");
	out.println("<LI><A href="+url+
		    "Program.do?"+
		    "id="+id+
		    "&action=zoom>Go to the Related Program </A>");
	out.println("</center>");
	out.print("</body></html>");
	out.flush();
	out.close();
	Helper.databaseDisconnect(con, stmt, rs);
    }

}















































