package program.web;
import java.util.*;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/PUserMenu"})
public class PUserMenu extends TopServlet{

    static Logger logger = LogManager.getLogger(PUserMenu.class);
    String allCategory = "";
    String allArea = "";
    String allLocation="";
    String allLead = "";
    String Months[] = {"","Jan","Feb","March","April","May","June","July",
	"Aug","Sept","Oct","Nov","Dec"};
    /**
     * Create an html page for the main menu form.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");

	PrintWriter out = res.getWriter();
	String pyear="", season="";
	PrintWriter os;
	boolean success = true;
	Enumeration values = req.getParameterNames();
	String id="", ptitle="", message="";
	String allPrograms="";
	String name, value, year="", choice="";
	String allMonths = "", category="";
	for(int i=0; i<Months.length;++i){
	    allMonths += "<option value="+i+">"+Months[i]+"\n";
	}
	int month=0;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if(name.equals("pyear")){
		pyear = value;
	    }
	    else if(name.equals("season")){
		season = value;
	    }
	    else if(name.equals("choice")){
		choice = value; // report, code, calendar
	    }
	    else if(name.equals("ptitle")){
		ptitle = value;
	    }
	    else if(name.equals("category")){
		category = value;
	    }
	    else if(name.equals("id")){  // progam id
		id = value;
	    }
	    else if(name.equals("year")){  // for calendar 
		year = value;
	    }
	    else if(name.equals("month")){  // for calendar
		if(!value.equals("")){
		    month = Integer.parseInt(value);
		}
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
	TypeList leads = null;
	TypeList areas = null;
	TypeList locations = null;
	TypeList categories = null;
	if(true){
	    leads = new TypeList(debug, "leads");
	    String back = leads.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    areas = new TypeList(debug, "areas");
	    back = areas.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    categories = new TypeList(debug, "categories");
	    back = categories.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    locations = new TypeList(debug, "locations");
	    back = locations.find();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }			
	}		
	//
	// in case year and season are set but not id
	if(!pyear.equals("") && !season.equals("") && id.equals("")){
	    allPrograms = "";
	    ProgramList pl = new ProgramList(debug);
	    pl.setYear(pyear);
	    pl.setSeason(season);
	    String back = pl.findAbraviatedList();
	    if(back.equals("")){
		for(Program pp:pl){
		    allPrograms += "<option value=\""+pp.getId()+"\">" + 
			pp.getTitle();
		}
	    }
	}
	String title ="";
	if(choice.equals("report")) title = "Reports";
	else if(choice.equals("code"))title = "Code Entry";
	else if(choice.equals("calendar")) title="Calendar";
	int years[] = Helper.getPrevYears();
	//
	// check for the user
	//
	out.println("<HTML><HEAD><TITLE>Programs Management Menu </TITLE>");
	Helper.writeWebCss(out,url);
	out.println(" <script language=\"javascript\">");
	out.println("  function validateForm(){            ");    
	out.println("  var x=\"\" ;                        ");
	out.println("   x = document.myForm.pyear.options[document.myForm.pyear.options.selectedIndex].text; ");
	out.println("  if(x == \"\"){                      ");
	out.println("   alert(\"No year selected yet\");   ");
	out.println("   return false;                      ");
	out.println("  }                                   ");
	out.println("  x=\"\";                             ");
	out.println("      x = document.myForm.season.options[document.myForm.season.options.selectedIndex].text;  ");
	out.println("  if(x == \"\"){                      ");
	out.println("   alert(\"No season selected yet\"); ");
	out.println("   return false;                      ");
	out.println("  }                                   ");
	out.println("   return true;                       ");
	out.println("  }                                   ");
	out.println(" </script>                            ");
	out.println("</head><body><center>");
	Helper.writeTopMenu(out, url);
	out.println("<center><h1> "+title+" </h1>");

	if(choice.equals("report")){
	    out.println("<form name=\"myForm\" action="+
			url + "ReportMenu "+
			"onSubmit=\"return validateForm()\">");
	    out.println("<input type=\"hidden\" name=\"choice\" value=\"" + 
			choice + "\" />");
	    //
	    // vol table
	    out.println("<table border=\"1\"><tr><td>");
	    out.println("<caption>Options</caption>");	    
	    out.println("<tr><td align=center>");
	    out.println("</td></tr><tr><td align=center>");
	    out.println("Select the year and season then click on Next");


	    if(pyear.equals("") && season.equals("")){
		out.println("<tr><td align=\"center\"><label for=\"year\">Year</label>");
		out.println("<select name=\"pyear\" id=\"year\">");
		out.println("<option></option>");
		for(int yy:years){
		    out.println("<option>"+yy+"</option>");
		}				
		out.println("</select>");
		out.println("&nbsp;&nbsp;<strong><label for=\"season\">Season</label>");
		out.println("<select name=\"season\" id=\"season\">");
		out.println("<option selected>"+season+"\n");
		out.println(Helper.allSeasons);
		out.println("</select></td></tr>");
	    }
	    out.println("<tr><td align=\"right\"> ");
	    out.println("<input type=\"submit\" value=\"Next\" />");
	    out.println("</td></tr>");
	    out.println("</form>");
	    out.println("</td></tr></table>");
	    out.println("</td></tr>"); // end of middle
	    //
	}
	else if(choice.equals("code")){
	    // Code
	    //
	    GregorianCalendar cal = new GregorianCalendar();
	    String year2 =  "" + cal.get(Calendar.YEAR);  
	    //
	    out.println("<form name=\"myForm\" method=\"post\" action=\""+url+
			"CodeBrowse?\""+
			" onsubmit=\"return validateForm()\">");			
	    out.println("<table>");
	    out.println("<caption>Code Entry</caption>");
	    out.println("<tr><td><b> Use this option to add "+
			"codes to programs and sessions that need codes.</b><br />");
	    out.println("Select the year and season then click on the "+
			"Submit button");

	    out.println("</td></tr>");
	    out.println("<tr><td><label for=\"year\">Year: </label>");
	    out.println("<select name=\"pyear\" id=\"year\">");
	    if(pyear.equals("")) pyear = year2;
	    out.println("<option selected>"+pyear+"\n");
	    for(int yy:years){
		out.println("<option>"+yy+"</option>");
	    }					
	    out.println("</select>");
	    out.println("&nbsp;&nbsp;&nbsp;<label for=\"season\">Season: </label>");
	    out.println("<select name=\"season\" id=\"season\">");
	    out.println("<option selected>"+season+"\n");
	    out.println(Helper.allSeasons);
	    out.println("</select></td></tr>");
	    out.println("<tr><td align=\"right\">");
	    out.println("<input type=\"submit\" name=\"action\" value=\"Submit\" "+
			" ></input>");
	    out.println("</td></tr>");
	    out.println("</table>");
	    out.println("</form>"); // end of code
	    //
	}
	else if(choice.equals("calendar")){
	    //
	    GregorianCalendar cal = new GregorianCalendar();
	    String year2 =  "" + cal.get(Calendar.YEAR);  
	    int month2 = cal.get(Calendar.MONTH) + 1;
	    
	    out.println("<table border=0><tr><td align=center colspan=2>"); // 
	    out.println("<FONT color=#000099 size=+2>Calendar "+
			"</FONT>");
	    out.println("<form name=calForm method=post action="+
			url + "Agenda?>");
	    out.println("</td></tr><tr><td align=center colspan=2>");
	    out.println("<b>Year: </b>");
	    out.println("<select name=year>");
	    if(year.equals("")) year = year2;
	    out.println("<option selected>"+year+"\n");
	    for(int yy:years){
		out.println("<option>"+yy+"</option>");
	    }					
	    out.println("</select>");
	    out.println("&nbsp;&nbsp;&nbsp;<b>Month: </b>");
	    out.println("<select name=month>");
	    if(month == 0) month = month2;
	    out.println("<option selected value="+month+">"+
			Months[month]+"\n");
	    out.println(allMonths);
	    out.println("</select></td></tr>");
	    // Area
	    out.println("<tr><td><b>Area: </b></td><td>");
	    out.println("<select name=area>");
	    out.println("<option value=\"\"></option>");
	    if(areas != null){
		for(Type one:areas){
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }		
	    out.println("</select></td></tr>");
	    //
	    // Lead
	    out.println("<tr><td><b>Lead: </b></td><td>");
	    out.println("<select name=lead>");
	    out.println("<option value=\"\"></option>");
	    if(leads != null){
		for(Type one:leads){
		    String selected = "";
		    out.println("<option "+selected+" value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }
	    out.println("</select></td></tr>");
	    //
	    // category
	    out.println("<tr><td><b>Heading: </b></td><td>");
	    out.println("<select name=category>");
	    out.println("<option value=\"\"></option>");
	    if(categories != null){
		for(Type one:categories){
		    String selected = "";
		    if(!one.isActive()) continue;
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }		
	    out.println("</select></td></tr>");
	    //
	    // Location
	    out.println("<tr><td><b>Location: </b></td><td>");
	    out.println("<select name=pLocation>");
	    out.println("<option value=\"\"></option>");			
	    if(locations != null){
		for(Type one:locations){
		    String selected = "";
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }		
	    out.println("</select></td></tr>");
	    out.println("<tr><td align=right colspan=2>");
	    out.println("<INPUT type=submit value=Submit"+
			"></input>");
	    out.println("</td></tr></table>");// end of calendar table
	}
	out.println("</td></tr></table>"); // end of overall
	//
	// log out section
	//
	out.println("</form>");

	out.println("</center>");

	out.println("</body></html>");
	out.close();

    }				   
    /**
     * The main class method.
     *
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */		  
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	doGet(req, res);
    }

}

