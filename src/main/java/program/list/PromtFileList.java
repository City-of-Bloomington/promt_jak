package program.list;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class PromtFileList{

    boolean debug;
    static final long serialVersionUID = 23L;
    static Logger logger = LogManager.getLogger(PromtFileList.class);
    String related_id="", type="";
    List<PromtFile> files = null;
    //
    public PromtFileList(boolean deb){

	debug = deb;
    }
    public PromtFileList(boolean deb,
			 String val,
			 String val2){

	debug = deb;
	setRelated_id(val);
	setType(val2);
    }
    //
    // setters
    //
    public void setRelated_id(String val){
	if(val != null){
	    related_id = val;
	}
    }
    public void setType(String val){
	if(val != null){
	    type = val;
	}
    }
				
    public List<PromtFile> getFiles(){
	return files;
    }
    //
    public String find(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq ="", qo = "";
	String qf =	"from promt_files ";
	qq = "select id,related_id,type,added_by_id,"+
	    "date_format(date,'%m/%d/%Y'),name, "+
	    "old_name,notes ";

	String back="", qw = "";
		
	if(!related_id.equals("")){
	    qw = " related_id = ? ";
	}
	if(!type.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " type = ? ";
	}				
	if(!qw.equals("")){
	    qw = " where "+qw;
	}
	qq = qq + qf + qw+" order by id desc ";			
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}		
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!related_id.equals(""))
		pstmt.setString(jj++, related_id);
	    if(!type.equals(""))
		pstmt.setString(jj++, type);						
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		PromtFile one = new PromtFile(debug,
					      rs.getString(1),
					      rs.getString(2),
					      rs.getString(3),
					      rs.getString(4),
					      rs.getString(5),
					      rs.getString(6),
					      rs.getString(7),
					      rs.getString(8));
		if(files == null)
		    files = new ArrayList<>();
		if(!files.contains(one))
		    files.add(one);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+" : "+qq);
	    return ex.toString();
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return "";
    }

	

}






















































