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

@WebServlet(urlPatterns = {"/MediaRequestReport"})
public class MediaRequestReport extends TopServlet{

    static Logger logger = LogManager.getLogger(MediaRequestReport.class);

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
	MediaRequestList mlist = new MediaRequestList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if(name.equals("season")){
		season = value;
		mlist.setSeason(value);
	    }
	    else if(name.equals("year")){
		year = value;
		mlist.setYear(value);
	    }
	    else if(name.equals("csvOutput")){
		if(value != null)
		    csvOutput=value;
	    }
	    else if(name.equals("sortby")){
		// mlist.setSortby(value);
		mlist.setSortby(" id "); 
	    }
	    else if(name.equals("id")){  
		if(value != null)
		mlist.setProgram_id(value);
	    }
	    
	    else if(name.equals("program_id")){  
		if(value != null)
		mlist.setProgram_id(value);
	    }
	    else if(name.equals("facility_id")){  
		if(value != null)
		mlist.setFacility_id(value);
	    }	    
	    else if(name.equals("lead_id")){   
		if(value != null)
		mlist.setLead_id(value);
	    }
	    else if(name.equals("category_id")){   
		// not set
	    }
	    else if(name.equals("area_id")){   
		// not set
	    }
	    else if(name.equals("nraccount")){   
		// not set
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
	List<MediaRequest> requests = null;	
	String back = mlist.find();
	if(!back.equals("")){
	    message = "Error "+back;
	}
	else{
	    List<MediaRequest> ones = mlist.getRequests();
	    if(ones != null && ones.size() > 0){
		requests = ones;
	    }
	    else{
		message ="No match found ";
	    }
	}
	if(!message.isEmpty()){
	    res.setStatus(HttpServletResponse.SC_OK);
	    res.setContentType("text/html");
	    out = res.getWriter();
	    out.println("<html>");
	    out.println("<head><title>Media Requests' Report" + 
			"</title>");
	    Helper.writeWebCss(out, url);
	    out.println("</head>");
	    out.println("<body>");
	    Helper.writeTopMenu(out, url);	    
	    out.println("<h3> "+ message +"</b3>");
	    out.println("</body></html>");
	    out.close();
	    return;
	}
	if(csvOutput.isEmpty()){
	    res.setStatus(HttpServletResponse.SC_OK);
	    res.setContentType("text/html");
	    out = res.getWriter();
	    out.println("<head><title>Media Requests' Report" + 
			"</title>");
	    Helper.writeWebCss(out, url);
	    out.println("</head>");
	    out.println("<body>");
	    Helper.writeTopMenu(out, url);
	    //
	    // check where clause 
	    //
	    Helper.writeFirstPage(out,"Media Requests' Report", year, season);
	    out.println("<br />");
	    String str = " Total: "+ requests.size();
	    out.println("<hr width=\"75%\"/>");
	    Helper.writeMediaRequests(out, "Media Requests ("+str+")", requests, url);
	    out.flush();
	    out.println("</form>");
	    out.println("</center></body>");
	    out.println("</html>");
	    out.flush();
	    out.close();
	}
	else{ // csv format
	    String all = "";
	    String line = "Id, Year, Season, Date, Program ID, Facility ID, Lead, Location, Location Specific, Content Specific, Media Type, Orientation, Other Media Type, Notes\n";
	    all += line;
	    for(MediaRequest one:requests){
		all += Helper.writeMediaRequestCsv(one);
	    }
	    res.setHeader("Expires", "0");
	    res.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
	    res.setHeader("Pragma", "public");
	    res.setHeader("Content-Disposition","inline; filename=\"media_requests.csv\"");
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






















































