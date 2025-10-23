package program.model;

import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;

public class RelatedUtil{

    String id="", url="", type="", link="";
    static Logger logger = LogManager.getLogger(RelatedUtil.class);	
    boolean debug = false;
    public RelatedUtil(
		       boolean deb,
		       String val, 
		       String val2,
		       String val3
		       ){

	setId(val);
	setType(val2);
	setUrl(val3);
	debug = deb;
	createLink();
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getUrl(){
	return url;
    }
    public String getType(){
	return type;
    }
    public String getLink(){
	return link;
    }
    public String toString(){
	return id;
    }
    public boolean foundType(){
	return !type.equals("");
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setUrl (String val){
	if(val != null)
	    url = val;
    }
    public void setType (String val){
	if(val != null)
	    type = val;
    }
		
    //
    // we need this to make sure that the user picked the right type
    //
    public String createLink(){
	String back = "";
	if(type.equals("Plan")){
	    link = "<a href=\""+url+"ProgPlan?id="+id+"\">"+id+"</a>";
	}
	else if(type.equals("Program")){
	    link = "<a href=\""+url+"Program.do?id="+id+"\">"+id+"</a>";
	}
	else if(type.equals("Facility")){
	    link = "<a href=\""+url+"Facility?id="+id+"\">"+id+"</a>";
	}
	else if(type.equals("Marketing")){
	    link = "<a href=\""+url+"Market.do?id="+id+"\">"+id+"</a>";
	}
	else if(type.equals("Evaluation")){
	    link = "<a href=\""+url+"Evaluation.do?id="+id+"\">"+id+"</a>";
	}	
	else{
	    back = " Related Util: Unknown type "+type;
	    System.err.println(back);
	}
	return back;
    }
	
}
