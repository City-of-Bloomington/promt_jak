package program.web;

import java.util.*;
import java.text.*;
import java.util.Date;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/MarketItem.do","/MarketItem"})
public class MarketItemServ extends TopServlet {

    static Logger logger = LogManager.getLogger(MarketItemServ.class);	
    /**
     * 
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
	res.setContentType("text/html");
	Enumeration values = req.getParameterNames();

	String name= "";
	String value = "", message="";
	String action = "";
	boolean success = true;
	String id = "", market_id="";
	PrintWriter out = res.getWriter();
        String [] vals;
	//
	List<Type> marketTypes = null;
	MarketItem item = new MarketItem(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("type_id")){ 
		item.setType_id(value);
	    }
	    else if (name.equals("id")){
		item.setId(value);
		id = value;
	    }			
	    else if (name.equals("direct")){ 
		item.setDirect(value);
	    }
	    else if (name.equals("due_date")){ 
		item.setDue_date(value);
	    }			
	    else if (name.equals("expenses")){ 
		item.setExpenses(value);
	    }
	    else if (name.equals("quantity")){
		item.setQuantity(value);
	    }
	    else if (name.equals("details")){ 
		item.setDetails(value);
	    }					
	    else if(name.equals("action")){
		action = value;
	    }
	}
	if(action.equals("Update")){
			
	    String back = item.doUpdate();
	    if(back.equals("")){
		message = "Updated Successfully";
	    }
	    else{
		message = "Error "+back;
		success = false;
	    }
	}
	if(true){
	    String back = item.doSelect();
	    if(!back.equals("")){
		message = "Error "+item.getMessage();
		success = false;
	    }
	    else{
		market_id= item.getMarket_id();
	    }
	    if(marketTypes == null){
		TypeList ones = new TypeList(debug, "marketing_types");
		back = ones.find();
		if(!back.equals("")){
		    message += " Could not retreive data ";
		    success = false;
		}
		else{
		    marketTypes = ones;
		}					
	    }			
	}
	out.println("<html><head><title>Marketing Piece(s)</title>");
	Helper.writeWebCss(out, url);
	out.println("<script type='text/javascript'>");
	out.println("/*<![CDATA[*/");								
	out.println(" function reloadIt(){       ");
	out.println(" var url=\""+url+"Market.do?action=zoom&id="+market_id+"\";");
	out.println(" opener.window.location.href=url; ");
	out.println("  }                          ");
	out.println("/*]]>*/\n");					
	out.println("</script> ");
	if(!action.equals("") && success){
	    out.println(" </head><body onload=\"reloadIt()\">  ");
	}
	else{
	    out.println("</head><body>");
	}
	out.println("<center>");
	// Helper.writeTopMenu(out, url);
	out.println("<h3>Edit Marketing Piece</h3>");
	if(!message.equals("")){
	    out.println("<h4>"+message+"</h4>");
	}
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\">");
	out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	out.println("<table border=\"1\" width=\"90%\">");
	out.println("<tr><td><label for=\"type\">Piece Type:</label></td>");
	out.println("<td align=\"left\"><select name=\"type_id\" id=\"type\">");
	String selected = "";
	if(marketTypes != null && marketTypes.size() > 0){
	    for(Type one:marketTypes){
		selected = one.getId().equals(item.getType_id())?"selected=\"selected\"":"";
		if(!selected.equals(""))
		    out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
		else if(one.isActive()){
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td><label for=\"quant\">Quantity:</label></td>");
	out.println("<td align=\"left\">");
	out.println("<input type=\"text\" name=\"quantity\" value=\""+item.getQuantity()+"\" size=\"6\" id=\"quant\"/>");
	out.println("</td></tr>");			
	out.println("<tr><td><label for=\"exp\">Expenses:</label></td>");
	out.println("<td align=\"left\">");
	out.println("<input type=\"text\" name=\"expenses\" value=\""+item.getExpenses()+"\" size=\"10\" id=\"exp\" />");
	out.println("</td></tr>");	
	out.println("<tr><td><b>Expenses Type:</b></td>");
	out.println("<td align=\"left\">");
	selected = !item.getDirect().equals("")?"checked=\"checked\"":"";
	out.println("<input type=\"radio\" name=\"direct\" value=\"y\" "+selected+" id=\"dir\"/><label for=\"dir\">Direct</label>");
	selected = item.getDirect().equals("")?"checked=\"checked\"":"";
	out.println("<input type=\"radio\" name=\"direct\" value=\"\" "+selected+" id=\"ind\" /><label for=\"ind\">Indirect</label>");		
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"due_date\">Due Date:</label></td>");
	out.println("<td align=\"left\">");
	out.println("<input type=\"text\" name=\"due_date\" value=\""+item.getDue_date()+"\" size=\"10\" id=\"due_date\" class=\"date\" />");

	out.println("</td></tr>");
	out.println("<tr><td><label for=\"det\">Details:</label></td>");
	out.println("<td align=\"left\">");
	out.println("<textarea name=\"details\" rows=\"5\" cols=\"40\" id=\"det\">");
	out.println(item.getDetails());
	out.println("</textarea></td></tr>");			
	out.println("<tr><td colspan=\"2\" align=\"center\">");
	out.println("<input type=\"submit\" name=\"action\" value=\"Update\">");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");		
	out.println("<br />");
	Helper.writeWebFooter(out, url);
	out.println("<a href=javascript:window.close();>Close</a><br />");
	out.println("</center>");
		
	out.println("</body></html>");
	out.flush();
	out.close();

    }
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req, res);
    }

}






















































