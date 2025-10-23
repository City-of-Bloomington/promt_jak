package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;


public class GeneralCode extends CommonInc implements Comparable{

    static Logger logger = LogManager.getLogger(GeneralCode.class);
    String id="", title="", code="";
    String category_name="", lead_name="";
    public GeneralCode(boolean val){
	super(val);
    }
    public GeneralCode(boolean deb,
		       String val,
		       String val2,
		       String val3,
		       String val4
		       ){
	//
	super(deb);
	setId(val);
	setTitle(val2);
	setLead_name(val3);
	setCode(val4);
    }
    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof GeneralCode){
	    match = id.equals(((GeneralCode)gg).id);
	}
	return match;
    }
    public int hashCode(){
	int hashCode = 1;
	hashCode = hashCode * 37 + this.id.hashCode()+
	    + this.title.hashCode();
	return hashCode;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getCode(){
	return code;
    }
	
    public String getTitle(){
	return title;
    }	
    public String toString(){
	String ret = title+" "+id;
	return ret;
    }
    public String getLead_name(){
	return lead_name;
    }			
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val.trim();
    }
    public void setCode(String val){
	if(val != null)
	    code = val.trim();
    }
    public void setTitle(String val){
	if(val != null)
	    title = val;
    }
    public void setLead_name(String val){
	if(val != null)
	    lead_name = val;
    }		
    // for sorting
    public int compareTo(Object obj){
	int i=0;
	if(obj instanceof GeneralCode){
	    if(!code.equals("")){
		try{
		    GeneralCode p2 = (GeneralCode)obj;
		    i = code.compareTo(p2.code);				
		}catch(Exception ex){

		}
	    }
	    else{
		try{
		    GeneralCode p2 = (GeneralCode)obj;
		    i = title.compareTo(p2.title);				
		}catch(Exception ex){

		}								
	    }
	}
	return i;
    }

}
