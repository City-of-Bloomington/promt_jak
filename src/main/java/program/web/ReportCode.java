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

@WebServlet(urlPatterns = {"/ReportCode"})
public class ReportCode extends TopServlet{

    static Logger logger = LogManager.getLogger(ReportCode.class);
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
     * Create a session report in html format.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String allCategory = "";
	String idList[] = null;
	
	boolean success = true;
	Enumeration values = req.getParameterNames();
	String name, value;
	String id="",year="", season="", title="", category_id="", lead_id=""; 
	String addWhere="", area_id="", nraccount="", sortby="", unsetCode="",
	    sessionSort="", message="";
	ProgramList plist = new ProgramList(debug);
	plist.setHasCode();
	plist.setCodeNeed("y");
	ProgCodeList pcList = new ProgCodeList(debug);
	pcList.setCodeNeed("y");
	pcList.setHasCode();
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = Helper.escapeIt(req.getParameter(name).trim());
	    if(name.equals("season")){
		season = value;
		plist.setSeason(value);
		pcList.setSeason(value);
	    }
	    else if(name.equals("year")){
		plist.setYear(value);
		pcList.setYear(value);				
		year = value;
	    }
	    else if(name.equals("id")){  // progam id
		pcList.setId(value);
		plist.setId(value);
	    }
	    else if(name.equals("sortby")){  // progam id
		if(!value.equals("")){
		    plist.setSortby(value);
		    sortby = value;
		}
	    }
	    else if(name.equals("category_id")){
		pcList.setCategory_id(value);				
		plist.setCategory_id(value);
	    }
	    else if(name.equals("lead_id")){
		pcList.setLead_id(value);				
		plist.setLead_id(value);
	    }
	    else if(name.equals("area_id")){
		pcList.setArea_id(value);
		plist.setArea_id(value);
	    }
	    else if(name.equals("nraccount")){
		pcList.setNraccount(value);
		plist.setNraccount(value);
	    }
	}
	if(sortby.equals("")){
	    plist.setSortby("s.code, p.code");
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
	out.println("<html>");
	out.println("<head><title>City of Bloomington Parks and "+
		    "Recs</title>");
	Helper.writeWebCss(out, url);
	out.println("</head><body><center>");
	Helper.writeTopMenu(out, url);	
	message = pcList.find();
	if(!message.isEmpty()){
	    out.println("<h3>Error "+message+"</h3>");
	    out.println("<br /></body></html>");
	    out.close();
	    return;
	}
	List<ProgCode> list = pcList.getList();
	if(list == null || list.size() == 0){
	    //
	    out.println("<p>No match found </p>");
	    out.println("</body></html>");
	    out.close();
	    return;	    
	}
	else{
	    //
	    writeFirstPage(out,"Code Programs Report", year, season);
	    out.println("<h3>Total Matching "+list.size()+"</h3>");
	    out.println("<h4>Note: sorting by program code will bring all programs with sessions first followed by programs without sessions</h4>");
	    out.println("<table border=\"1\">");
	    out.println("<caption>Program Codes</caption>");
	    out.println("<tr><th>Code</th><th>Program</th><th>ID</th></tr>");
	    //
	    for(ProgCode one:list){
		String str = "<a href=\""+url+"Program.do?action=zoom&id="+one.getId()+"\">"+one.getTitle()+"</a>";				
		if(one.isProgram()){
		    out.println("<tr><td>"+one.getCode()+"</td><td>"+str+"</td><td>"+one.getId()+"</td></tr>");
		}
		else{
		    out.println("<tr><td>"+one.getCode()+"</td><td>"+str+"</td><td>Session "+one.getSid()+"</td></tr>");
		}
	    }
	    out.println("</table>");
	}
	out.println("</body></html>");
	out.close();
		
    }
    /** 
     * Writes the first page.
     * @param out the output stream
     * @param reportTitle the title
     * @param season the season
     * @param pyear the year
     */
    void writeFirstPage(PrintWriter out, String reportTitle,
			String year,String season){
	out.println("<center><bParks and Recreation</b><br />");
	out.println("<b>"+reportTitle+"</b><br />");
	out.println("<b> "+season+" "+year+ "</b><br /><br />");
	out.println("<hr width=\"75%\" size=\"4\" /><br />");
	out.println("<b>Note: \"Total In City Fee\" and other fees do include transaction fee <br /></b>"); 
	out.println("</center>");
    }

}













































