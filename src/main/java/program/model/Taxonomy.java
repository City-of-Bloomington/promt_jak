package program.model;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.list.*;
/**
 *
 */

public class Taxonomy extends Type{

    static Logger logger = LogManager.getLogger(Taxonomy.class);
    List<TaxonomySub> subs = null;
    //
    public Taxonomy(){
	super();
	setTableName("taxonomies");
    }
    public Taxonomy(boolean deb, String val, String val2){
	//
	// initialize
	//
	super(deb);
	setId(val);
	setName(val2);
	setTableName("taxonomies");
    }	
    public Taxonomy(boolean deb, String val){
	super(deb);
	setId(val);
	setTableName("taxonomies");
    }		
    //
    public Taxonomy(boolean deb){
	super(deb);
    }
    public boolean hasSubs(){
	findSubs();
	return subs != null && subs.size() > 0;
    }
    public List<TaxonomySub> getSubs(){
	return subs;
    }
    public String findSubs(){
	String back = "";
	if(!id.equals("")){
	    TaxonomySubList tsl = new TaxonomySubList(debug, id);
	    back = tsl.find();
	    if(back.equals("")){
		if(tsl.size() > 0){
		    subs = tsl;
		}
	    }
	}
	return back;
    }


}
