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

@WebServlet(urlPatterns = {"/FacilityTable","/FacilityResult"})
public class FacilityResult extends TopServlet{

    static Logger logger = LogManager.getLogger(FacilityResult.class);
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
     * processes the facility search request.
     * Handls view, add, update, delete operations on this form.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{

	PrintWriter os;
	
	String username="", message="";
	String sortby = "";
	String id = "";
	Enumeration values = req.getParameterNames();
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	os = res.getWriter();

	String name, value;
	boolean rangeFlag = true, success = true;
	os.println("<html>");
	FacilityList fl = new FacilityList(debug);

	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if (name.equals("id")){
		fl.setId(value);
	    }
	    else if (name.equals("lead_id")){
		fl.setLead_id(value);
	    }
	    else if (name.equals("type")){
		fl.setType(value);
	    }			
	    else if (name.equals("name")){
		fl.setName(value);
	    }
	    else if (name.equals("statement")){
		fl.setStatement(value);
	    }
	    else if (name.equals("schedule")){
		fl.setSchedule(value);
	    }
	    else if (name.equals("closings")){
		fl.setClosings(value);
	    }
	    else if (name.equals("other")){
		fl.setOther(value);
	    }
	    else if (name.equals("sortby")){
		sortby = value;
	    }
	    else {
		// System.err.println("Unknown choice "+name+" "+value);
	    }
	}
		
	os.println("<head><title>Facilities" + 
		   "</title>");
	Helper.writeWebCss(os, url);	
	os.println("</head><body>");
	Helper.writeTopMenu(os, url);	
		
	//
	// check where clause, it is common for table and report
	//
	os.println("<br /><center>");
	String back = fl.lookFor();
	String that = "";
	if(!back.equals("")){
	    os.println("<h4>Error "+back+"</h4>");
	}

	os.println("<Font size=+1>Facilities Report </Font><br />"+
		   "<br /><br />");
	if(fl.size() > 0){
	    os.println("Total records :"+ fl.size() + "<br />");
	    Helper.writeFacilities(os, fl, url);			
	}
	else{
	    os.println("No record found <br />"); 
	}
	os.println("</center></body>");
	os.println("</html>");
	os.flush();
	os.close();
    
    }

}






















































