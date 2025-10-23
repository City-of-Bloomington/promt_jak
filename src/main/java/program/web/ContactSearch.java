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

@WebServlet(urlPatterns = {"/ContactBrowse","/ContactSearh"})
public class ContactSearch extends TopServlet{

    static Logger logger = LogManager.getLogger(ContactSearch.class);

    /**
     * The main class method doGet.
     *
     * Create an html page for the Facility form.
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
     * The main class method doPost.
     *
     * Create an html page for the planning form.
     * @param req request input parameters
     * @param res reponse output parameters
     * 
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	PrintWriter out;

	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name, value, action="";
	String id ="";
		
	//
	// reinitialize to blank
	//
	String message = "", finalMessage="";

	out.println("<html><head><title>City of Bloomington Parks and "+
		    "Recreation</title>"); 
	boolean actionSet = false, success=true;
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
	ContactList clist = new ContactList(debug);
	List<Contact> contacts = null;
        String [] vals;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();

	    if(name.equals("id")){
		clist.setId(value);
		id = value;
	    }
	    else if(name.equals("name")){
		clist.setName(value);
	    }
	    else if(name.equals("phone")){
		clist.setPhone(value);
	    }
	    else if(name.equals("email")){
		clist.setEmail (value);
	    }
	    else if(name.equals("address")){
		clist.setAddress (value);
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}

	//
       	if(!action.equals("")){
	    String back = clist.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		List<Contact> ones = clist.getContacts();
		if(ones != null && ones.size() > 0){
		    contacts = ones;
		    if(contacts.size() == 1){
			Contact one = contacts.get(0);
			String str = url+"Contact.do?fromBrowse=y&action=zoom&id="+one.getId();
			res.sendRedirect(str);
			return;
		    }
		}
	    }
	}
	//
	// This script validates textareas and facility
	//
	Helper.writeWebCss(out, url);	
	out.println("</head><body>");
	//
	Helper.writeTopMenu(out, url);	
	out.println("<center>");
	out.println("<h2>Instructors Search</h2>");
	if(!message.equals("")){
	    out.println(message+"<br />");
	}
	//
	out.println("<form name=\"myForm\" method=\"post\">");
	if(action.equals("")){
	    out.println("<p>Before you decide to add a new instructor, Please make sure he/she is not already in the system by doing search in this form</p>");
	}
	out.println("<table width=\"90%\" border=\"1\">");
	out.println("<caption>Search Options</caption>");
	out.println("<tr><td colspan=\"2\" align=\"center\" bgcolor=\"navy\" "+
		    "><h3><font color=\"white\">"+
		    "Instructor Search Options </font></h3></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"id\">Instructor ID: </label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"id\" id=\"id\" "+
		    "value=\"\" maxlength=\"10\" size=\"10\" /></td></tr>"); 
	out.println("<tr><td align=\"right\"><label for=\"name\">Name: </label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"name\" id=\"name\" "+
		    "value=\"\" maxlength=\"30\" size=\"30\" />*</td></tr>"); 
	out.println("<tr><td align=\"right\"><label for=\"phone\">Phone:</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"phone\" maxlength=\"12\" size=\"12\" id=\"phone\" />*</td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"email\">Email:</label></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"email\" id=\"email\" "+
		    "value=\"\" maxlength=\"50\" size=\"50\" /></td></tr>");
	out.println("<tr><td align=\"right\"><label for=\"addr\">Address:</label></td><td align=\"left\">");
	out.print("<input type=\"text\" name=\"address\" size=\"30\" id=\"addr\"/>");
	out.println("*</td></tr>");
	out.println("</table>");
	//
	out.println("<input type=\"submit\" "+
		    "name=\"action\" value=\"Search\" />");
	out.println("<input type=\"button\" onclick=\"document.location='"+url+"Contact.do';\" value=\"New Instructor\" /></td>");		
	out.println("<p>*<font color=\"green\" size=\"-1\"> Notice:you can enter partial name, word or number. </font></p>");
	if(!action.equals("") && contacts == null){
	    out.println("<h3> No match found </h3>");
	}
	else if(contacts != null && contacts.size() > 0){
	    out.println("<h3> Found "+contacts.size()+" records </h3>");
	    out.println("<table border=\"1\"><caption>Search Results</caption>");
	    out.println("<tr><th>ID</th><th>Name</th><th>Address</th><th>Email</th><th>Phones</th></tr>");
	    for(Contact one:contacts){
		out.println("<tr>");
		out.println("<td><a href=\""+url+"Contact.do?fromBrowse=y&action=zoom&id="+one.getId()+"\">Edit</a></td>");
		out.println("<td>"+one.getName()+"</td>");				
		out.println("<td>&nbsp;"+one.getAddress()+"</td>");
		out.println("<td>&nbsp;"+one.getEmail()+"</td>");
		out.println("<td>&nbsp;"+one.getPhones()+"</td>");
		out.println("</tr>");
	    }
	    out.println("</table>");
	}
	out.println("<br />");
	out.print("</center></body></html>");
	out.close();
    }

}





































