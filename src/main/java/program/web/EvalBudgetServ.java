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

@WebServlet(urlPatterns = {"/EvalBudget.do","/EvalBudget"})
public class EvalBudgetServ extends TopServlet{

    static Logger logger = LogManager.getLogger(EvalBudgetServ.class);	
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
     * Generates and processes the evaluation budget form.
     * Handls view, add, update, delete operations on this form.
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
	
	String id = "";
	boolean success = true, hasRecord = false;
	Enumeration values = req.getParameterNames();
	String name, value;
	String action="";
	String message="";
	boolean	actionSet = false;
	//
	String [] vals;
	HttpSession session = null;
	session = req.getSession(false);
	User user = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}
	Program prog = null;
	Budget budget = new Budget(debug);
	Plan plan = null;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();		    

	    if(name.equals("id")){
		id = value;
		budget.setId(value);
	    }
	    else if(name.equals("wby")){
		budget.setWby(value);
	    }
	    else if(name.equals("date")){
		budget.setDate(value);
	    }
	    else if(name.equals("notes")){
		budget.setNotes(value);
	    }
	    else if(name.equals("del_staff")){
		budget.setDel_staffs(vals); // multiples, direct,indirect
	    }
	    else if(name.equals("del_fee")){
		budget.setDel_fees(vals); // multiples, direct,indirect
	    }
	    else if(name.equals("del_otherDir")){
		budget.setDel_otherDir(vals); // multiples, direct,indirect
	    }			
	    else if(name.equals("admin_percent")){
		budget.setAdmin_percent(value);
	    }
	    else if(name.equals("sponsorship")){
		budget.setSponsorship(value);
	    }
	    else if(name.equals("sponsor_revenue")){
		budget.setSponsor_revenue(value);
	    }
	    else if(name.equals("donation")){
		budget.setDonation(value);
	    }
	    else if(name.startsWith("fee-")){
		budget.addFee(name, value);
	    }
	    else if(name.startsWith("dir-")){
		budget.addDir(name, value);
	    }
	    else if(name.startsWith("indir-")){
		budget.addIndir(name, value);
	    }
	    else if(name.startsWith("addDirDesc")){			
		budget.addOtherDirect(name, value);
	    }
	    else if(name.startsWith("addDirExpense")){			
		budget.addOtherDirect(name, value);
	    }			
	    else if(name.equals("action")){
		action = value;
	    }
	    else{
		System.err.println(" unknown "+name+" "+value);
	    }
	}
	if (action.equals("Save")){
	    String back = budget.doSave();
	    if(!back.equals("")){
		success = false;
		message += back;
	    }
	    else{
		message = budget.getMessage();
	    }
	}
	else if (action.equals("Update")){
	    String back = budget.doUpdate();
	    if(!back.equals("")){
		success = false;
		message += back;
	    }
	    else{
		message = budget.getMessage();
	    }
	}
	else if (action.equals("Delete")){
	    /*
	      String back = budget.doDelete();
	      if(!back.equals("")){
	      success = false;
	      message += back;
	      }
	      else{
	      message = budget.getMessage();
	      }
	    */
	}
	else if (action.equals("zoom") || action.equals("")){
	    budget.doSelect();
	}
	if(true){
	    prog = budget.getProgram();
	    plan = prog.getPlan();
	}
	String twoCells	= "<td>&nbsp;</td><td>&nbsp;</td></tr>";	
	String threeCells = "<td>&nbsp;</td>"+twoCells;
	String fourCells = "<td>&nbsp;</td>"+threeCells;
	String row = "<tr><td colspan=\"4\">&nbsp;</td>"+threeCells;
	//
	out.println("<html>");
	out.println("<head><title>City of Bloomington Parks and "+
		    "Recreation</title>");
	//
	Helper.writeWebCss(out, url);
       	out.println(" <script type=\"text/javascript\">");
	out.println("  function validateString(x){      ");            
	out.println("     if((x.value.length > 0)){     "); 
	out.println("       var eq = 0;	                ");
	out.println("    for(t = 0; t < x.value.length; t++){  ");
	out.println("    if (x.value.substring(t,t+1) != \" \") eq = 1;	");
	out.println("    	       }                ");
	out.println("     if (eq == 0) {	        ");
	out.println("	      return false;		");
	out.println("            } ");
	out.println("	     return true;		");
	out.println("         }  ");  
	out.println("	     return false;		");
	out.println("      }  ");  
	out.println(" function checkSelection(element){   ");
	out.println("   for(var i = 0; i < element.options.length; i++) ");
        out.println("    if (element.options[i].selected){  "); 
	out.println("      if(i > 0){ ");
	out.println("         return true;  ");
	out.println("         }     ");
	out.println("       }  ");
	out.println("    return false;  ");
	out.println("   }               ");
	out.println(" function checkTextArea(ss, len){   ");
        out.println("    if (ss.value.length > len ){  "); 
	out.println("       alert(\"Text Area should not be more than"+
		    "\"+len+\" chars\"); ");
	out.println("       ss.value = ss.value.substring(0,len); ");
	out.println("       }                           ");
	out.println("   }                               ");
	out.println("  function validateDeleteForm(){    ");            
	out.println("  var x = false;                    ");
	out.println("   x = window.confirm(\"Are you sure you want to delete\"); ");
	out.println("   return x;                       ");
	out.println(" }	                                ");
	out.println(" </script>                          ");   
	out.println("</head><body>");
	out.println("<center>");
	Helper.writeTopMenu(out, url);	
	//
	if(!budget.hasRecord()){
	    out.println("<h3>New Evaluation Budget </h3>");
	}
	else // add update
	    out.println("<h3>Edit Evaluation Budget </h3>");
	if(!message.equals("")){
	    out.println(message+"<br />");
	}
	//
	// the real table
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\" "+
		    "onSubmit=\"return validateForm()\">");
	out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	//
	out.println("<table border=\"1\" align=\"center\">");
	out.println("<caption>Evaluation Budget</caption>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td>");
	out.println("<table>");
	out.println("<tr><td align=\"right\"><b>Program: ");
	out.println("</b></td><td align=\"left\">");
	out.println(prog.getTitle()+" &nbsp;");
	out.println(" ("+prog.getSeasons()+"/"+prog.getYear()+")");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\"><b>Attendance: ");		
	out.println("</b></td><td align=\"left\">");
	out.println("<input type=\"text\" name=\"attendance\" value=\""+budget.getAttendance()+"\" size=\"4\" disabled=\"disabled\" />(carried from evaluation page)");
	out.println("</td></tr>");
	if(plan != null){
	    String goals = plan.getGoals();
	    if(goals != null && !goals.equals("")){
		out.println("<tr><td align=\"right\"><b>Program Goals: ");		
		out.println("</b></td><td align=\"left\">");
		out.println(goals);
		out.println("</td></tr>");								
	    }
	}
	out.println("</table>");
	out.println("</td></tr>");
	//
	// Budget Table
	//
	out.println("<tr><td>");
	out.println("<table width=\"100%\" border=\"1\">");
	out.println("<caption>Budget Table</caption>");
	//
	// actual Revenue
	out.println("<tr><td colspan=\"7\" bgcolor=\"navy\" align=\"center\">"+
		    "<h3><font color=\"white\">Budget</font></h3></td></tr>");
	//
	out.println("<tr><td colspan=\"4\">&nbsp;</td><td>Revenue $</td>"+
		    "<td>Expenses $</td><td>&nbsp;</td></tr>");
	out.println("<tr><td colspan=\"4\"><label for=\"spon\">Sponsorship </label></td><td>");
	out.println("<input type=\"text\" name=\"sponsorship\" value=\""+Helper.formatNumber(budget.getSponsorship())+"\" size=\"10\" id=\"spon\" /></td><td> &nbsp;</td><td>");
	String checked = budget.isSponsorshipRevenue()?"checked=\"checked\"":"\"\"";
	out.println("<input type=\"checkbox\" name=\"sponsor_revenue\" value=\"y\" "+checked+" id=\"rev\" /><label for=\"rev\">Mark as Revenue</label>");		
	out.println("</td></tr>");
	out.println("<tr><td colspan=\"4\"><label for=\"don\">Donations </label></td><td>");
	out.println("<input type=\"text\" name=\"donation\" value=\""+Helper.formatNumber(budget.getDonation())+"\" size=\"10\" id=\"don\" /></td>"+twoCells);
	//
	out.println("<tr><td colspan=\"4\">Add Itemized fees</td>"+threeCells);
	out.println(row);
	out.println("<tr><td>Fees </td><td>Rate</td><td>Number</td>"+fourCells);
	FeeItemList fees = budget.getFees();
	//
	if(fees != null){
	    for(FeeItem one:fees){
		out.println("<tr><td><input type=\"checkbox\" name=\"del_fee\" value=\""+one.getId()+"\" />");				
		out.println("<b>"+one.getFee_type()+"</b></td><td>"+one.getRate()+"</td><td>"+one.getQuantity()+"</td><td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(one.getTotal())+"</td>");
		out.println(twoCells);
	    }
	}
	//
	int jj=1;
	TypeList feeTypes = new TypeList(debug, "fee_types");
	feeTypes.find();
	for(int i=0;i<3;i++){
	    out.println("<tr><td><select name=\"fee-type_"+i+"\">");
	    out.println("<option value=\"\">Pick one</option>");
	    if(feeTypes != null && feeTypes.size() > 0){
		for(Type one:feeTypes){
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
	    }
	    out.println("</select></td>");
	    out.println("<td><input type=\"text\" name=\"fee-rate_"+i+"\" size=\"8\" /></td>");
	    out.println("<td><input type=\"text\" name=\"fee-quantity_"+i+"\" size=\"4\" /></td>");
	    out.println(fourCells);
	}
	// Total Revenue
	out.println(row);
	out.println("<tr><td colspan=\"4\"><b>Total Revenue: </b></td>");
	out.println("<td align=\"right\">"+Helper.formatNumber(budget.getTotalRevenue())+"</td>"+twoCells);
	out.println(row);		
	//
	// Direct Expenses
	//
	// Note: Full time staff includes the manager, coordinator, specialist
	// the rest are considered part time
	// The full time costs go into the indirect costs
	//		
	out.println("<tr><td colspan=\"4\"><b>Direct Staff Expenses</b></td>");
	out.println(threeCells);
	out.println(row);		
	out.println("<tr><td>Staff </td><td>Number</td><td>Rate</td><td>Hours</td>"+threeCells);
	StaffExpenseList staffExps = budget.getStaffExpenses();
	if(staffExps != null && staffExps.size() > 0){
	    for(StaffExpense one:staffExps){
		if(one.isDirect()){
		    out.println("<tr><td><input type=\"checkbox\" name=\"del_staff\" value=\""+one.getId()+"\" />");	
		    out.println(one.getStaff_type()+"</td><td>"+one.getQuantity()+"</td><td>"+one.getRate()+"</td><td>"+one.getHours()+"</td><td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(one.getTotal())+"</td><td>&nbsp;</td></tr>");
		}
	    }
	}
	TypeList staffTypes = new TypeList(debug, "staff_types");
	String back = staffTypes.find();
	if(staffTypes.size() > 0){
	    for(int i=0;i<3;i++){
		out.println("<tr><td><select name=\"dir-staff-type_"+i+"\">");
		out.println("<option value=\"\"></option>");
		for(Type one:staffTypes){
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
		out.println("</select></td>");
		out.println("<td><input type=\"text\" name=\"dir-quantity_"+i+"\" size=\"4\" /></td>");
		out.println("<td><input type=\"text\" name=\"dir-rate_"+i+"\" size=\"8\" /></td>");
		out.println("<td><input type=\"text\" name=\"dir-hours_"+i+"\" size=\"8\" /></td>");
		out.println(threeCells);
	    }
	}
	out.println(row);
	out.println("<tr><td colspan=\"4\"><b>Staff Direct Expenses:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getStaffDirectExpenses())+"</td><td>&nbsp;</td></tr>");				
	out.println("<tr><td colspan=\"4\"><b>Marketing Direct Expenses:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getMarketDirectExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println("<tr><td colspan=\"4\" align=\"left\"><b>Other Direct Expenses </b>(you can add 3 at a time)</td>"+threeCells);
	DirectExpenseList otherDirList = budget.getOtherDirectExpenses();
	jj = 1;
	if(otherDirList != null){
	    for(DirectExpense one:otherDirList){
		out.println("<tr><td>&nbsp;</td><td colspan=\"3\" align=\"left\">"+(jj++)+" - <input type=\"checkbox\" name=\"del_otherDir\" value=\""+one.getId()+"\" />"+one+"</td><td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(one.getExpenses())+"</td><td>&nbsp;</td></tr>");
	    }
	}
	for(int i=0;i<3;i++){
	    out.println("<tr><td>&nbsp;</td><td colspan=\"3\" align=\"left\">"+(jj++)+" - <input type=\"text\" name=\"addDirDesc_"+i+"\" value=\"\" size=\"30\" maxlength=\"30\" /><td>&nbsp;</td><td><input type=\"text\" name=\"addDirExpense_"+i+"\" size=\"10\" /></td><td>&nbsp;</td></tr>");
	}
	out.println("<tr><td colspan=\"4\"><b>Total Direct Expenses:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getTotalDirectExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println(row);		
	//
	// Indirect Expenses
	//
	out.println("<tr><td colspan=\"4\"><b>Indirect Expenses </b></td>");
	out.println(threeCells);
	out.println(row);		
	//
	out.println("<tr><td colspan=\"4\"><b>Indirect Staff Expenses</b></td>");
	out.println(threeCells);
	out.println("<tr><td>Staff </td><td>Number</td><td>Rate</td><td>Hours</td>"+threeCells);
	if(staffExps != null && staffExps.size() > 0){
	    for(StaffExpense one:staffExps){
		if(!one.isDirect()){
		    out.println("<tr><td><input type=\"checkbox\" name=\"del_staff\" value=\""+one.getId()+"\" />");						
		    out.println(one.getStaff_type()+"</td><td>"+one.getQuantity()+"</td><td>"+one.getRate()+"</td><td>"+one.getHours()+"</td><td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(one.getTotal())+"</td><td>&nbsp;</td></tr>");
		}
	    }
	}
	if(staffTypes.size() > 0){
	    for(int i=0;i<3;i++){
		out.println("<tr><td><select name=\"indir-staff-type_"+i+"\">");
		out.println("<option value=\"\"></option>");
		for(Type one:staffTypes){
		    out.println("<option value=\""+one.getId()+"\">"+one+"</option>");
		}
		out.println("</select></td>");
		out.println("<td><input type=\"text\" name=\"indir-quantity_"+i+"\" size=\"4\" /></td>");
		out.println("<td><input type=\"text\" name=\"indir-rate_"+i+"\" size=\"8\" /></td>");
		out.println("<td><input type=\"text\" name=\"indir-hours_"+i+"\" size=\"8\" /></td>");
		out.println(threeCells);
	    }
	}
	out.println(row);
	out.println("<tr><td colspan=\"4\"><b>Staff Indirect Expenses:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getStaffIndirectExpenses())+"</td><td>&nbsp;</td></tr>");				
	out.println("<tr><td colspan=\"4\"><b>Marketing Indirect Expenses:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getMarketIndirectExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println("<tr><td colspan=\"4\"><b>Indirect Expenses Subtotal:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getSubTotalIndirectExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println(row);				
	out.println("<tr><td colspan=\"4\"><b>Administration Fees *</b></td>");
	out.println("<td>&nbsp;</td>");
	out.println("<td align=\"right\">"+Helper.formatNumber(budget.getAdmin_fees())+"</td>");
	out.println("<td><select name=\"admin_percent\">");
	String ss5 = budget.getAdmin_percent() == 5?"selected=\"selected\"":"";
	out.println("<option "+ss5+">5</option>");
	out.println("</select>%</td></tr>");
	out.println(row);
	out.println("<tr><td colspan=\"4\"><b>TOTAL INDIRECT EXPENSES:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getTotalIndirectExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println(row);
	//
	out.println("<tr><td>&nbsp;</td><td colspan=\"3\"><b>TOTAL EXPENSES:</b></td>");
	out.println("<td>&nbsp;</td><td align=\"right\">"+Helper.formatNumber(budget.getTotalExpenses())+"</td><td>&nbsp;</td></tr>");
	out.println("<tr><td>&nbsp;</td><td colspan=\"2\"><b>Direct:</b></td>");
	out.println("<td align=\"right\">"+Helper.formatNumber(budget.getTotalDirectExpenses())+"</td>"+threeCells);
	out.println("<tr><td>&nbsp;</td><td colspan=\"2\"><b>Indirect:</b></td>");
	out.println("<td align=\"right\">"+Helper.formatNumber(budget.getTotalIndirectExpenses())+"</td>"+threeCells);
	out.println(row);
	out.println("<tr><td>&nbsp;</td><td colspan=\"3\"><b>COST RECOVERY:</b></td>"+threeCells);
	out.println(row);
	out.println("<tr><td colspan=\"3\"><b>COST RECOVERY LEVEL (100%-((TE-TR)/TE)100%):</b></td><td>"+Helper.formatNumber(budget.getRecoveryLevel())+"%</td>"+threeCells);
	out.println(row);
	out.println("<tr><td colspan=\"3\" align=\"right\">Total Expenses (TE):</td><td align=\"right\">"+Helper.formatNumber(budget.getTotalExpenses())+"</td>"+threeCells);
	out.println("<tr><td colspan=\"3\" align=\"right\">Total Revenue (TR):</td><td align=\"right\">"+Helper.formatNumber(budget.getTotalRevenue())+"</td>"+threeCells);
	out.println("<tr><td colspan=\"3\" align=\"right\">Unrecovered Expenses (UE):</td><td align=\"right\">"+Helper.formatNumber(budget.getUnrecoveredExpenses())+"</td>"+threeCells);
	out.println(row);
	out.println("<tr><td colspan=\"3\" align=\"right\">Cost per participant (UE/attendance):</td><td align=\"right\">$"+Helper.formatNumber(budget.getCostPerParticipant())+"</td>"+threeCells);
	out.println(row);
	//
	out.println("<tr><td colspan=\"3\"><b>DIRECT COST RECOVERY LEVEL (100%-((TDE-TR)/TDE)100%):</b></td><td>"+Helper.formatNumber(budget.getDirectRecoveryLevel())+"%</td>"+threeCells);
	out.println(row);
	out.println("<tr><td colspan=\"3\" align=\"right\">Total Direct Expenses (TDE):</td><td align=\"right\">"+Helper.formatNumber(budget.getTotalDirectExpenses())+"</td>"+threeCells);
	out.println("<tr><td colspan=\"3\" align=\"right\">Total Revenue (TR):</td><td align=\"right\">"+Helper.formatNumber(budget.getTotalRevenue())+"</td>"+threeCells);
	out.println("<tr><td colspan=\"3\" align=\"right\">Unrecovered Direct Expenses (UDE):</td><td align=\"right\">"+Helper.formatNumber(budget.getUnrecoveredDirectExpenses())+"</td>"+threeCells);
	out.println(row);		
	out.println("<tr><td colspan=\"3\" align=\"right\">Direct Cost per participant (UDE/attendance):</td><td align=\"right\">$"+Helper.formatNumber(budget.getDirectCostPerParticipant())+"</td>"+threeCells);
	out.println(row);		
	//
	out.println("<tr><td colspan=\"7\"><label for=\"notes\">Notes</label><font color=\"green\">"+
		    "Up to 500 characters</font></td></tr>");
	out.println("<tr><td colspan=\"7\">");		
	out.print("<textarea name=\"notes\" wrap rows=\"5\" cols=\"80\" "+
		  " onChange=\"checkTextArea(this, 500)\" id=\"notes\" >");
	out.print(budget.getNotes());
	out.println("</textarea></td></tr>");
	//
	out.println("<tr><td colspan=\"4\" align=\"left\"><label for=\"wby\">Prepared By</label>"+
		    "</td><td colspan=\"3\"><label for=\"date\">Date</label></td></tr>");
	out.println("<tr><td colspan=\"4\" align=\"left\">");
	out.println("<input type=\"text\" name=\"wby\" size=\"30\" value=\""+budget.getWby()+"\" id=\"wby\" /></td>");
	out.println("<td colspan=\"3\" align=\"left\">");
	out.println("<input type=\"text\" name=\"date\" size=\"10\" value=\""+budget.getDate()+"\" id=\"date\" /></td></tr>");		
	//
	if(!budget.hasRecord()){
	    out.println("<tr><td colspan=\"7\"><input type=\"submit\" "+
			"name=\"action\" value=\"Save\" /></td></tr>");
	    out.println("</td></tr>");
	}
	else{ 
	    out.println("<tr>");
	    out.println("<td colspan=\"7\">");	    
	    if(user.canEdit()){
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Update\" /> ");
	    }
	    out.println("</form>");
	    //
	    if(user.canDelete()){
		out.println("<form name=\"myForm2\" method=\"post\" "+
			    "onsubmit=\"return validateDeleteForm()\" >");
		out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\">");
		out.println("<input type=\"submit\" name=\"action\" "+
			    "value=\"Delete\">");
		out.println("</form>");	
	    }
	    out.println("</td></tr>");

	}	    
	out.println("</table></td></tr></table>");
	//
	out.println("<li><a href=\""+url+
		    "Evaluation.do?"+
		    "&id="+id+
		    "&action=zoom\">Go Back to Evaluation Page</a></li>");

	out.println("<li><a href=\""+url+
		    "Program.do?"+
		    "&id="+id+
		    "&action=zoom\">Go to the Related Program </a></li>");

	Helper.writeWebFooter(out, url);
	out.print("</body></html>");
	out.flush();
	out.close();

    }

}















































