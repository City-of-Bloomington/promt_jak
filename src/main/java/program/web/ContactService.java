package program.web;

import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.ArrayList;
import jakarta.servlet.annotation.WebServlet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/ContactService"})
public class ContactService extends TopServlet{

    boolean production = false;
    static Logger logger = LogManager.getLogger(ContactService.class);
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
	ContactList clist = new ContactList(debug);
	List<Contact> contacts = null;
	if(term.length() > 1){
	    clist.setName(term);
	    String back = clist.find();
	    List<Contact> ones = clist.getContacts();
	    if(ones != null && ones.size() > 0){
		contacts = ones;
		String json = writeJson(contacts);
		out.println(json);
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
     {"value":"Walid Sibo",
     "id":"sibow",
     "dept":"ITS"
     },
     {"value":"schertza",
     "id":"Alan Schertz",
     "dept":"ITS"
     }
     ]
     ***************************
     */
    String writeJson(List<Contact> ones){
	String json="";
	if(ones.size() > 0){
	    for(Contact one:ones){
		if(!json.equals("")) json += ",";
		json += "{\"id\":\""+one.getId()+"\",\"value\":\""+one.getName()+"\",\"phone_h\":\""+one.getPhone_h()+"\",\"phone_w\":\""+one.getPhone_w()+"\",\"phone_c\":\""+one.getPhone_c()+"\",\"address\":\""+one.getAddressCleansed()+"\",\"email\":\""+one.getEmail()+"\"}";
	    }
	}
	json = "["+json+"]";
	return json;
    }

}






















































