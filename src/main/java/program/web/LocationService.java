package program.web;

import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/LocationService"})
public class LocationService extends TopServlet{

    static Logger logger = LogManager.getLogger(LocationService.class);
    /**
     * Generates the Group form and processes view, add, update and delete
     * operations.
     * @param req
     * @param res
     */
    
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req,res);
    }
    /**
     * @link #doGetost
     */

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
    
	String id = "";

	//
	String message="", action="";
	res.setContentType("application/json");
	PrintWriter out = res.getWriter();
	String name, value;
	String fullName="", term ="", type="";
	boolean success = true;
	HttpSession session = null;
	Enumeration values = req.getParameterNames();
	String [] vals = null;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("term")) { // this is what jquery sends
		term = value;
	    }
	    else if (name.equals("action")){ 
		action = value;  
	    }
	    else{
		// System.err.println(name+" "+value);
	    }
	}
	LocationList clist = new LocationList(debug);
	if(term.length() > 1){
	    List<Location> locations = null;
	    clist.setName(term);
	    String back = clist.find();
	    if(back.isEmpty()){
		locations = clist.getLocations();
	    }
	    if(locations != null && locations.size() > 0){
		String json = writeJson(locations);
		out.println(json);
	    }
	}
	out.flush();
	out.close();
    }
		
    String writeJson(List<Location> ones){
	String json="";
	if(ones.size() > 0){
	    for(Location one:ones){
		if(!json.equals("")) json += ",";
		json += "{\"id\":\""+one.getId()+"\",\"value\":\""+one.getName()+"\"}";
	    }
	}
	json = "["+json+"]";
	return json;
    }

}






















































