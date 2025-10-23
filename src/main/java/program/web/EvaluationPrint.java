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

@WebServlet(urlPatterns = {"/EvaluationPrint"})
public class EvaluationPrint extends TopServlet{

    String staffSelectArr[] = Helper.staffSelectArr;
    static Logger logger = LogManager.getLogger(EvaluationPrint.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	doPost(req, res);
    }

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String id = "";
	Enumeration values = req.getParameterNames();
	String name, value;
	String action="", message="";
	boolean	success = true;
	String [] vals;
	HttpSession session = null;
	session = req.getSession(false);
	User user = null;
	Evaluation eval = new Evaluation(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();		    

	    if(name.equals("id")){
		id = value;
		eval.setId(id);
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	}
	if (true){
	    String back = eval.doSelect();
	    if(!back.equals("")){
		success = false;
		message = eval.getMessage();
	    }
	}
	//
	// else start empty form, startNew
	//
	out.println("<html>");
	out.println("<head><title>City of Bloomington Parks and "+
		    "Recreation</title>");
	//
	out.println("</head><body>");
	if(!message.equals("")){
	    if(!success)
		out.println("<font color=red>"+message+"</font><br>");
	}
	if(success){
	    Helper.writeEvaluation(out, eval, debug);
	}
	out.println("</center>");		
	out.print("</body></html>");
	out.flush();
	out.close();

    }

}















































