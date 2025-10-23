package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;

public class ProgCode extends CommonInc implements Comparable{

    static Logger logger = LogManager.getLogger(ProgCode.class);
    String id="", title="", code="", sid="";
    String category_name="", lead_name="";
    public ProgCode(boolean val){
	super(val);
    }
    public ProgCode(boolean deb,
		    String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5
		    ){
	//
	super(deb);
	setId(val);
	setTitle(val2);
	setCode(val3);
	setSid(val4);
	setCode(val5);
    }
    public ProgCode(boolean deb,
		    String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6,
		    String val7){
	//
	super(deb);
	setId(val);
	setTitle(val2);
	setCode(val3);
	setSid(val4);
	setCode(val5);
	setCategory_name(val6);
	setLead_name(val7);
    }			

    public boolean equals(Object  gg){
	boolean match = false;
	if (gg != null && gg instanceof ProgCode){
	    match = id.equals(((ProgCode)gg).id) && sid.equals(((ProgCode)gg).sid);
	}
	return match;
    }
    public int hashCode(){
	int hashCode = 1;
	hashCode = hashCode * 37 + this.id.hashCode()+
	    + this.sid.hashCode()
	    + this.title.hashCode();
	return hashCode;
    }
    public boolean isSession(){
	return !sid.equals("");
    }
    public boolean isProgram(){
	return sid.equals("");
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
    public String getSid(){
	return sid;
    }
    public String toString(){
	String ret = title+" "+id;
	if(hasSid()){
	    ret += " "+sid;
	}
	return ret;
    }
    public String getCategory_name(){
	return category_name;
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
    public void setSid(String val){
	if(val != null)
	    sid = val;
    }
    public boolean hasSid(){
	return !sid.equals("");
    }
    public void setCategory_name(String val){
	if(val != null)
	    category_name = val;
    }
    public void setLead_name(String val){
	if(val != null)
	    lead_name = val;
    }
    // for sorting
    public int compareTo(Object obj){
	int i=0;
	if(obj instanceof ProgCode){
	    if(!code.equals("")){
		try{
		    ProgCode p2 = (ProgCode)obj;
		    i = code.compareTo(p2.code);				
		}catch(Exception ex){

		}
	    }
	    else{
		try{
		    ProgCode p2 = (ProgCode)obj;
		    i = title.compareTo(p2.title);				
		}catch(Exception ex){

		}								
	    }
	}
	return i;
    }

}
