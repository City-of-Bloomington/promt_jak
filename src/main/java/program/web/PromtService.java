package program.web;

import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import org.json.simple.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/PromtService"})
public class PromtService extends TopServlet{

    boolean production = false;
    static Logger logger = LogManager.getLogger(PromtService.class);
    /**
       age groups that may be requested
       grp:toddler, preschool, youth,  teen, adult,adult 50+, all ages, w/parent
       range: < 3      3-4       5-12   13-17 18-49  50- 
       avrge   2        3         8       15    34   55
    */
    private static final String[] ageArr = {"toddler","preschool","youth","teen","adult","adult 50+","all","with parent"}; 
    private static final Map<String, String> ageGroup = new HashMap<String, String>();
    static{
	ageGroup.put("toddler","2");
	ageGroup.put("preschool","3");
	ageGroup.put("youth","8");
	ageGroup.put("teen","15");
	ageGroup.put("adult","34");
	ageGroup.put("adult 50+","55");				
	ageGroup.put("all","all");
	ageGroup.put("with parent","wParent");
    };
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
    
	//
	String message="", action="";
	res.setContentType("application/json");
	res.setCharacterEncoding("UTF-8");
	PrintWriter out = res.getWriter();
	String name, value;
	String term ="", category_id="", sub_category_id="", program_id="",
	    facility_term="", location_id="", info="",
	    effective_date="", age="", list_type="";
	boolean success = true;
	HttpSession session = null;
	Enumeration values = req.getParameterNames();
	String [] vals = null;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim().toLowerCase();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("term")) { // this is what jquery sends
		term = value;
	    }
	    else if (name.startsWith("info")){ 
		info = value;  
	    }												
	    else if (name.startsWith("category")){ 
		category_id = value;  
	    }
	    else if (name.startsWith("sub_category")){ 
		sub_category_id = value;  
	    }						
	    else if (name.startsWith("id")){ 
		program_id = value;  
	    }										
	    else if (name.startsWith("program_id")){ 
		program_id = value;  
	    }						
	    else if (name.startsWith("facility")){ 
		facility_term = value;  
	    }
	    else if (name.startsWith("location")){ 
		location_id = value;  // assuming location id
	    }						
	    else if (name.equals("effective_date")){ 
		effective_date = value;  
	    }
	    else if (name.equals("age")){ 
		age = value;  
	    }
	    else if (name.startsWith("list")){ 
		list_type = value;  // age_list, location_list
	    }						
	    else if (name.equals("action")){ 
		action = value;  
	    }
	    else{
		System.err.println(" promt service: "+name+" "+value);
	    }
	}
	JSONObject jObj = null;
	JSONArray jArr = new JSONArray();
	if(!info.equals("")){
	    jArr = prepInfoList();					
	}
	else if(!list_type.equals("")){
	    //
	    // request for list
	    //
	    if(list_type.startsWith("age")){
		jArr = prepAgeList();
		out.println(jArr);
	    }
	    else if(list_type.startsWith("location")){
		LocationList llist = new LocationList(debug);
		llist.setActiveOnly();
		if(effective_date.equals("")){
		    llist.setCurrentOnly();
		}
		else{
		    llist.setEffective_date(effective_date);										
		}
		String back = llist.find();
		if(back.isEmpty()){
		    List<Location> locations = llist.getLocations();
		    if(locations != null && locations.size() > 0){
			jArr = prepLocationList(locations);
			out.println(jArr);			
		    }
		}
	    }
	    else if(list_type.startsWith("categories")){
		TaxonomyList tlist = new TaxonomyList(debug);
		tlist.find();
		if(tlist.size() > 0){
		    jArr = prepTaxonomyList(tlist);
		}
		out.println(jArr);
	    }
	    else{
		out.println(jArr);
	    }
	}
	else{
	    ProgramList plist = new ProgramList(debug);
	    if(!program_id.equals("")){
		plist.setId(program_id);
		// plist.forPublishOnly();
		plist.forServicePublishOnly();
	    }
	    else{
		plist.forPublishOnly();
		if(!category_id.equals("")){
		    String str = category_id;
		    if(!sub_category_id.equals("")){
			str += ":"+sub_category_id; // category:sub_category
		    }
		    plist.setTaxonomy_ids(str);
		}
		else if(!sub_category_id.equals("")){
		    plist.setTaxonomy_ids(sub_category_id);
		}
		if(!facility_term.equals("")){
		    plist.setFacility_term(facility_term);
		}				
		if(effective_date.equals(""))
		    plist.setCurrentOnly(); // what is available now
		else
		    plist.setEffective_date(effective_date);
		if(!location_id.equals("")){
		    plist.setLocation_id(location_id);
		}
		if(!age.equals("")){
		    if(ageGroup.containsKey(age)){
			String val = ageGroup.get(age);
			if(val.equals("all")){
			    plist.setAllAge();
			}
			else if(val.equals("wParent")){
			    plist.setWParent();
			}
			else{
			    plist.setAge(val);
			}
		    }
		    else{
			// assuming a number
			try{
			    int val = Integer.parseInt(age);
			    plist.setAge(age);
			}catch(Exception ex){
			    logger.error(ex+": invalid age "+age);
			} 
		    }
		}
	    }
	    // plist.setLimit("30");
	    plist.find();
	    if(!program_id.equals("")){
		if(plist.size() == 1){
		    Program pp = plist.get(0);
		    if(pp.canPublish())
			jObj = prepOneProgram(pp, effective_date);
		}
		else{
		    jObj = new JSONObject();			
		}
		out.println(jObj);																		
	    }
	    else if(plist.size() > 0){
		jArr = prepJson(plist, effective_date);
		// System.err.println(jArr);
		out.println(jArr);
	    }
	    else{
		out.println(jArr);
	    }
	}
	out.flush();
	out.close();
    }
    JSONArray prepInfoList(){
	JSONArray jArr = new JSONArray();
	JSONObject jObj = new JSONObject();											
	jObj.put("parameter","list_type");
	jObj.put("value","age_groups");
	jObj.put("description","List of age groups");
	jArr.add(jObj);
	jObj = new JSONObject();											
	jObj.put("parameter","list_type");
	jObj.put("value","locations");
	jObj.put("description","List of locations");
	jArr.add(jObj);
	jObj = new JSONObject();											
	jObj.put("parameter","list_type");
	jObj.put("value","categories");
	jObj.put("description","List of categories");
	jArr.add(jObj);
	jObj = new JSONObject();											
	jObj.put("parameter","program_id");
	jObj.put("value","number");
	jObj.put("description","program with that have the program_id number");
	jArr.add(jObj);
	jObj = new JSONObject();											
	jObj.put("parameter","location_id");
	jObj.put("value","number");
	jObj.put("description","all available programs in the specified location");
	jArr.add(jObj);
	jObj = new JSONObject();											
	jObj.put("parameter","age");
	jObj.put("value","string");
	jObj.put("description","all available programs that match that age group");
	jArr.add(jObj);
	jObj = new JSONObject();											
	jObj.put("parameter","category_id");
	jObj.put("value","number");
	jObj.put("description","all available programs that match the category");
	jArr.add(jObj);
	jObj = new JSONObject();											
	jObj.put("parameter","sub_category_id");
	jObj.put("value","number");
	jObj.put("description","all available programs that match the sub category");
	jArr.add(jObj);
	jObj = new JSONObject();											
	jObj.put("parameter","effective_date");
	jObj.put("value","date string");
	jObj.put("description","effective program date in mm/dd/yyyy or yyyy-mm-dd format, default is current date");
	jArr.add(jObj);				
	return jArr;

    }
    JSONArray prepAgeList(){
	String str="", str2="";
	// JSONObject jTop = new JSONObject();			
	JSONArray jArr = new JSONArray();
	for(String key:ageArr){
	    jArr.add(key);
	}
	// jTop.put("age groups",jArr);
	return jArr;
    }
    JSONArray prepLocationList(List<Location> ll){
	String str="", str2="";
	// JSONObject jTop = new JSONObject();			
	JSONArray jArr = new JSONArray();
	for(Location one:ll){
	    JSONObject jloc = new JSONObject();											
	    jloc.put("id",one.getId());
	    jloc.put("name",one.getName());
	    jArr.add(jloc);
	}
	// jTop.put("locations", jArr);
	return jArr;
    }
    JSONArray prepTaxonomyList(List<Taxonomy> tl){
	String str="", str2="";
	// JSONObject jTop = new JSONObject();			
	JSONArray jArr = new JSONArray();
	for(Taxonomy one:tl){
	    JSONObject jTax = new JSONObject();											
	    jTax.put("id",one.getId());
	    jTax.put("name",one.getName());
	    if(one.hasSubs()){
		JSONArray jSubs = new JSONArray();								
		List<TaxonomySub> subs = one.getSubs();
		if(subs.size() > 0){
		    for(TaxonomySub two:subs){
			JSONObject jSub = new JSONObject();
			jSub.put("id",two.getId());
			jSub.put("name",two.getName());
			jSubs.add(jSub);
		    }
		    jTax.put("sub_categories",jSubs);
		}
	    }
	    jArr.add(jTax);
	}
	// jTop.put("categories", jArr);
	return jArr;
    }
		
    JSONArray prepJson(List<Program> ones, String effective_date){
	JSONArray prgArr = new JSONArray();				
	if(ones.size() > 0){
	    for(Program one:ones){
		if(one.canPublish()){
		    JSONObject jprog = prepOneProgram(one, effective_date);
		    if(jprog != null)
			prgArr.add(jprog);
		}
	    }
	}
	return prgArr;
    }
    JSONObject prepOneProgram(Program one, String date){
	String str = "";
	String effective_date = date;
	JSONObject jprog = new JSONObject();				
	Type cat = one.getCategory();
	Location location = one.getLocation();
	Type area = one.getArea();
	Type lead = one.getLead();
	String taxonomyTerm = one.getTaxonomyInfo();
	List<TaxonomySub> taxSubs = one.getTaxonomySubList();
	SessionOpt sessionOpt = one.getSessionOpt();
	SessionList sessions = null;
	if(date.equals("")){
	    effective_date = Helper.getToday2();
	}
	sessions  = one.getSessionsWithDateLimit(effective_date);
						
	jprog.put("id",one.getId());
	jprog.put("year",one.getYear());
	jprog.put("season",one.getSeason());								
	jprog.put("title",one.getTitle());
	jprog.put("can_publish",one.canPublish());
	if(!one.getCode().equals("")){
	    jprog.put("code", one.getCode());		
	}
	str = one.getSummary();
	if(!str.equals("")){
	    str = Helper.cleanText(str);
	    jprog.put("summary", str);										
	}
	if(cat != null && !cat.getName().equals("")){
	    jprog.put("web_heading", cat.getName());			
	}
	if(area != null && !area.getName().equals("")){
	    jprog.put("area", area.getName());			
	}
	if(lead != null && !lead.getName().equals("")){
	    jprog.put("lead", lead.getName());			
	}				
	if(location != null){
	    str = location.getName();
	    if(!str.equals("")){
		jprog.put("location_id", location.getId());
		jprog.put("location", location.getName());
		str = location.getLocation_url();
		if(!str.equals("")){
		    jprog.put("location_url", str);
		}
	    }
	}
	//
	// comment out this when the next one is pushed to production
	//
	if(taxSubs != null && taxSubs.size() > 0){
	    JSONArray catArr = new JSONArray();
	    for(TaxonomySub ts:taxSubs){
		JSONObject catObj = new JSONObject();								
		Type tax = ts.getTaxonomy();
		if(tax != null){
		    catObj.put("category_id", tax.getId());
		    catObj.put("category_name", tax.getName());
		    if(!ts.getId().equals("")){
			catObj.put("sub_category_id", ts.getId());
			catObj.put("sub_category_name", ts.getName());
		    }
		}
		catArr.add(catObj);
	    }
	    jprog.put("categories",catArr);
	}
	str = one.getStatement();				
	if(!str.equals("")){
	    str = Helper.cleanText(str);
	    jprog.put("statement", str);
	}
	if(!one.getInCityFee().equals("")){
	    str = one.getInCityFee();
	    if(Helper.isNumberic(str))
		jprog.put("in_city_fees","$"+ str);
	    else
		jprog.put("in_city_fees",str);								
	}
	if(!one.getNonCityFee().equals("")){
	    str = one.getNonCityFee();
	    if(Helper.isNumberic(str))
		jprog.put("non_city_fees","$"+str);
	    else
		jprog.put("non_city_fees",str);								
	}
	if(!one.getMemberFee().equals("")){
	    str = one.getMemberFee();
	    if(Helper.isNumberic(str))
		jprog.put("member_fees","$"+ str);
	    else
		jprog.put("member_fees",str);								
	}
	if(!one.getNonMemberFee().equals("")){
	    str = one.getNonMemberFee();
	    if(Helper.isNumberic(str))
		jprog.put("non_member_fees","$"+str);
	    else
		jprog.put("non_member_fees",str);								
	}				
	if(!one.getRegDeadLine().equals("")){
	    jprog.put("registration_deadline", one.getRegDeadLine());
	}
	str = one.getStartDate();
	if(!str.equals("")){
	    jprog.put("start_date",str);
	}
	str = one.getEndDate();
	if(!str.equals("")){
	    jprog.put("end_date",str);
	}
	str = one.getStartTime();
	if(!str.equals("")){
	    jprog.put("start_time",str);
	}
	str = one.getEndTime();
	if(!str.equals("")){
	    jprog.put("end_time",str);
	}
	str = one.getStartEndTime();
	if(!str.equals("")){
	    jprog.put("start_end_time",str);
	}				
	if(!one.getDays().equals("")){
	    jprog.put("days", one.getDays());
	}
	str = one.getAgeInfo();
	if(!str.equals("")){
	    jprog.put("participant_age", str);
	}
	str = one.getPartGrade();
	if(!str.equals("")){
	    jprog.put("participant_grade", str);
	}
	str = one.getMinMaxEnroll();
	if(!str.equals("")){
	    jprog.put("min_max_enroll", str);
	}								
	str = one.getWaitList();
	if(!(str.equals("") || str.equals("0"))){
	    jprog.put("waiting_list", str);										
	}
	str = one.getInstructor();
	if(!str.equals("")){
	    jprog.put("instructor", str);
	}
	str = one.getDescription();
	if(!str.equals("")){
	    str = Helper.cleanText(str);
	    jprog.put("description", str);
	}
	str = one.getOginfo();
	if(!str.equals("")){
	    str = Helper.cleanText(str);
	    jprog.put("other_info", str);
	}
	if(sessions != null && sessions.size() > 0){
	    JSONArray sessArr = new JSONArray();
	    for(Session one2:sessions){
		JSONObject sessObj = new JSONObject();
		sessObj.put("session_id",one2.getSid());
		str = one2.getCode();
		if(!str.equals("")){
		    sessObj.put("code",str);
		}
		str = one2.getDays();
		if(!str.equals("")){
		    sessObj.put("days",str);
		}
		str = one2.getStartDate();
		if(!str.equals("")){
		    sessObj.put("start_date",str);
		}
		str = one2.getEndDate();
		if(!str.equals("")){
		    sessObj.put("end_date",str);
		}												
		str = one2.getStartTime();
		if(!str.equals("")){
		    sessObj.put("start_time",str);
		}
		str = one2.getEndTime();
		if(!str.equals("")){
		    sessObj.put("end_time",str);
		}
		str = one2.getStartEndTime();
		if(!str.equals("")){
		    sessObj.put("start_end_time",str);
		}								
		str = one2.getRegDeadLine();												
		if(!str.equals("")){
		    sessObj.put("registration_deadline",str);
		}
		str = one2.getInCityFee();
		if(!str.equals("")){
		    if(Helper.isNumberic(str)){
			sessObj.put("in_city_fees","$"+str);
		    }
		    else{
			sessObj.put("in_city_fees",str);
		    }
		}
		str = one2.getNonCityFee();
		if(!str.equals("")){
		    if(Helper.isNumberic(str)){										
			sessObj.put("non_city_fees","$"+str);
		    }
		    else{
			sessObj.put("non_city_fees",str);
		    }
		}
		str = one2.getMemberFee();
		if(!str.equals("")){
		    if(Helper.isNumberic(str)){													
			sessObj.put("member_fees","$"+str);
		    }
		    else{
			sessObj.put("member_fees",str);
		    }
		}
		str = one2.getNonMemberFee();
		if(!str.equals("")){
		    if(Helper.isNumberic(str)){														
			sessObj.put("non_member_fees","$"+str);
		    }
		    else{
			sessObj.put("non_member_fees",str);
		    }
		}
		Location loc = one2.getLocation();
		if(loc != null){
		    str = loc.getName();
		    if(!str.equals("")){
			sessObj.put("location_id",loc.getId());
			sessObj.put("location",str);
			str = loc.getLocation_url();
			if(!str.equals("")){
			    sessObj.put("location_url", str);
			}												
		    }
		}
		str = one2.getAgeInfo();
		if(!str.equals("")){
		    sessObj.put("participant_age",str);
		}
		str = one2.getPartGrade();
		if(!str.equals("")){
		    sessObj.put("participant_grade",str);
		}
		str = one2.getDescription();
		if(!str.equals("")){
		    sessObj.put("description",str);
		}
		str = one2.getInstructor();
		if(!str.equals("")){
		    sessObj.put("instructor",str);
		}												
		sessArr.add(sessObj);
	    }
	    jprog.put("sessions",sessArr);
	}
	return jprog;
    }

		
}






















































