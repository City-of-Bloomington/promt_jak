package program.web;

import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/TaxonomyService"})
public class TaxonomyService extends TopServlet{

    static Logger logger = LogManager.getLogger(TaxonomyService.class);
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
	String term ="", tax_id=""; // taxonomy_id
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
	    else if (name.equals("tax_id")){ 
		tax_id = value;  
	    }
	    else if (name.equals("action")){ 
		action = value;  
	    }
	    else{
		// System.err.println(name+" "+value);
	    }
	}
	TaxonomySubList tlist = new TaxonomySubList(debug, tax_id);
	if(!tax_id.equals("")){
	    tlist.find();
	    if(tlist.size() > 0){
		JSONArray json = prepJson(tlist);
		out.println(json);
		// System.err.println(json);
	    }
	}
	out.flush();
	out.close();
    }    
    /**
     * *************************
     *
     * json format as an array
     [
     {"value":"W Sibo",
     "id":"sibow",
     "dept":"ITS"
     },
     {"value":"schertza",
     "id":"A Schertz",
     "dept":"ITS"
     }
     ]
     ***************************
     */
    JSONArray prepJson(List<TaxonomySub> ones){
	JSONArray taxArr = new JSONArray();
	if(ones.size() > 0){
	    for(TaxonomySub one:ones){
		JSONObject taxObj = new JSONObject();
		taxObj.put("id",one.getId());
		String str = one.getName();
		str = one.getTax_id();
		if(!str.equals("")){
		    taxObj.put("tax_id",str);
		}
		if(!str.equals("")){
		    taxObj.put("name",str);
		}
		taxArr.add(taxObj);
	    }
	}
	return taxArr;
    }
}























































