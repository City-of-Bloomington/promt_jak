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

@WebServlet(urlPatterns = {"/MarketReport"})
public class MarketReport extends TopServlet{

    static Logger logger = LogManager.getLogger(MarketReport.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	doPost(req, res);
    }
    /**
     * Processes the marketing search request. 
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	PrintWriter out = null;
	ServletOutputStream out2 = null;
	String sortby = "";
	String message="";
	boolean success = true;

	Enumeration values = req.getParameterNames();
	//obtainProperties(os);
	String name, value;
	String year = "", season = "", csvOutput="";
	ProgramList plist = new ProgramList(debug);
	plist.setHasMarket(); // join marketing table
	GeneralList glist = new GeneralList(debug);
	glist.setHasMarket();
	FacilityList flist = new FacilityList(debug);
	flist.setHasMarket();
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if(name.equals("season")){
		season = value;
		plist.setSeason(value);
		glist.setSeason(value);
		flist.setSeason(value);
	    }
	    else if(name.equals("year")){
		year = value;
		plist.setYear(value);
		glist.setYear(value);
		flist.setYear(value);
	    }
	    else if(name.equals("csvOutput")){
		if(value != null)
		    csvOutput=value;
	    }
	    else if(name.equals("sortby")){
		plist.setSortby(value);
	    }
	    else if(name.equals("id")){  // progam id
		plist.setId(value);
	    }
	    else if(name.equals("category_id")){
		plist.setCategory_id(value);
	    }
	    else if(name.equals("lead_id")){   // by zoom
		plist.setLead_id(value);
		glist.setLead_id(value);
	    }
	    else if(name.equals("area_id")){   // by zoom
		plist.setArea_id(value);
	    }
	    else if(name.equals("nraccount")){   // by zoom
		plist.setNraccount(value);
	    }
	}
	User user = null;
	HttpSession	session = req.getSession(false);
	Control control = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	String back = plist.find();
	back += glist.find();
	back += flist.lookFor();
	if(!back.equals("")){

	    res.setStatus(HttpServletResponse.SC_OK);
	    res.setContentType("text/html");
	    out = res.getWriter();
	    out.println("<html>");
	    out.println("<head><title>Marketing Report" + 
			"</title>");
	    Helper.writeWebCss(out, url);
	    out.println("</head>");
	    out.println("<body>");
	    Helper.writeTopMenu(out, url);	    
	    out.println("<h3> Error "+back+"</b3>");
	    out.println("</body></html>");
	    out.close();
	    return;
	}
	if(csvOutput.isEmpty()){
	    res.setStatus(HttpServletResponse.SC_OK);
	    res.setContentType("text/html");
	    out = res.getWriter();
	    out.println("<head><title>Marketing Report" + 
			"</title>");
	    Helper.writeWebCss(out, url);
	    out.println("</head>");
	    out.println("<body>");
	    Helper.writeTopMenu(out, url);
	    //
	    // check where clause 
	    //
	    if(plist.size() == 0 && glist.size() == 0 && flist.size() == 0){
		//
		Helper.sendMessage(out, "No Programs","No records found "+
				   "make sure you select the right year and season");
	    }
	    else{
		Helper.writeFirstPage(out,"Marketing Report",year, season);
		out.println("</font><br />");
		if(glist.size() > 0){
		    out.println(" Total General Listings :"+ glist.size() + "<br />");
		    out.println("<hr width=\"75%\"/>");
		    Helper.writeGenerals(out, glist, url,"General Listings", false);
		}
		if(flist.size() > 0){
		    out.println(" Total Facilities :"+ flist.size() + "<br />");
		    out.println("<hr width=\"75%\"/>");
		    Helper.writeFacilities(out, flist, url);
		}
		if(plist.size() > 0){
		    out.println("Total Programs :"+ plist.size() + "<br />");
		    out.println("<hr width=\"75%\"/>");
		    //
		    for(Program prog:plist){
			String cat="", id="";
			Market market = null;				
			String str2="";
			id = prog.getId(); 
			Helper.writeProgram(out, prog, false, false, false, url);
			//
			// write sessions
			if(prog.hasSessions()){
			    Helper.writeSessions(out, prog.getSessions(), prog.getSessionOpt(), prog.getId(), url, debug);
			}
			if(prog.hasFiles()){
			    Helper.printFiles(out, url, prog.getFiles());
			}
			if(prog.hasMarket()){
			    market = prog.getMarket();
			    Helper.writeMarket(out, market, url);
			    if(market.hasFiles()){
				Helper.printFiles(out, url, market.getFiles());
			    }
			}
			//
			if(prog.hasShifts()){
			    Helper.writeVolShifts(out, prog.getShifts(), url,"Volunteer Shifts", false);
			}
			//
			// sponsors
			if(prog.hasSponsors()){
			    List<Sponsor> slist = prog.getSponsors();
			    Helper.writeSponsors(out, slist, url, debug);
			}
			//
			out.println("<br /><hr width=\"75%\"><br />");
										
		    }
		}
	    }
	    out.flush();
	    out.println("</form>");
	    out.println("</center></body>");
	    out.println("</html>");
	    out.flush();
	    out.close();
	}
	else{ // csv format
	    String all = "";
	    String line = "Type, Name, Lead, Request #,Request Type, Quantity, Due Date, Details\n";
	    all += line;
	    if(glist.size() > 0){
		// out.println(" Total General Listings :"+ glist.size() + "<br />");
		for(General gg:glist){
		    if(gg.hasMarkets()){
			List<Market> markets = gg.getMarkets();
			for(Market one:markets){
			    all += Helper.writeMarketCsv(one, "General List", gg.getTitle(), "");
			}
		    }
		}
	    }
	    if(flist.size() > 0){
		for(Facility one:flist){
		    String lead_name = "";
		    Type lead = one.getLead();
		    if(lead != null)
			lead_name = lead.getName();
		    if(one.hasMarket()){
			Market market = one.getMarket();
			all += Helper.writeMarketCsv(market, "Facility",one.getName(),lead_name);
		    }						    
		}
	    }
	    if(plist.size() > 0){
		// out.println("Total Programs :"+ plist.size() + "<br />");
		//
		for(Program prog:plist){
		    // write sessions
		    if(prog.hasMarket()){
			Market market = prog.getMarket();
			Type lead = prog.getLead();
			String lead_name = "";
			if(lead != null)
			    lead_name = lead.getName();			
			all += Helper.writeMarketCsv(market, "Program",prog.getTitle(), lead_name);
		    }
		}
	    }
	    res.setHeader("Expires", "0");
	    res.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
	    res.setHeader("Pragma", "public");
	    res.setHeader("Content-Disposition","inline; filename=\"marketing.csv\"");
	    out2 = res.getOutputStream();
	    byte [] buf = all.getBytes();
	    // setting the content type
	    res.setContentType("application/csv");
	    // the contentlength is needed for MSIE!!!
	    res.setContentLength(buf.length);
	    // write ByteArrayOutputStream to the ServletOutputStream
	    out2 = res.getOutputStream();
	    out2.write(buf);
	    out2.close();
	}
    }
				
}






















































