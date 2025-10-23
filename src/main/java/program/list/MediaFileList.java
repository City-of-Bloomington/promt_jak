/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package program.list;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class MediaFileList{

    String id="", user_id="", date="", date_from="", date_to="";
    String obj_type = "", obj_id="";
    boolean debug = false;
    String limit = " ";
    static final long serialVersionUID = 135L;		
    static Logger logger = LogManager.getLogger(MediaFileList.class);
    List<MediaFile> mediaFiles = null;
    public MediaFileList(){
		
    }
    public MediaFileList(boolean deb){
	debug = deb;
    }
    public MediaFileList(boolean deb, String val, String val2){
	debug = deb;
	setObj_id(val);
	setObj_type(val2);
    }		
    //		
    //
    // getters
    //
    public String getId(){
	return id;
    }	
    public String getUser_id(){
	return user_id;
    }
    public String getDate_from(){
	return date_from;
    }
    public String getDate_to(){
	return date_to;
    }
    public String getObj_type(){
	if(obj_type.equals("")){
	    return "-1";
	}
	return obj_type;
    }
    public String getObj_id(){
	return obj_id;
    }
    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }
    public void setObj_type(String val){
	if(val != null && !val.equals("-1"))
	    obj_type = val;
    }
    public void setObj_id(String val){
	if(val != null)
	    obj_id = val;
    }
    public void setNoLimit(){
	limit = "";
    }
    public MediaFile getLastUpload(){
	if(mediaFiles == null){
	    String back = find();
	}
	if(mediaFiles != null){
	    return mediaFiles.get(0);
	}
	return null;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }	
    public void setUser_id (String val){
	if(val != null)
	    user_id = val;
    }
    public List<MediaFile> getMediaFiles(){
	return mediaFiles;
    }
    public String find(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "select id,file_name,old_file_name,obj_id,obj_type,date_format(date,'%m/%d/%Y'),notes,user_id,year(date) from media ";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    String qw = "";
	    if(!id.equals("")){
		qw += " id = ? ";
	    }
	    else{
		if(!obj_type.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " obj_type = ? ";
		}
		if(!obj_id.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " obj_id = ? ";
		}
		if(!date_from.equals("")){
		    if(!qw.equals("")) qw += " and ";								
		    qw += " date >= str_to_date('"+date_from+"','%m/%d/%Y')";
		}
		if(!date_to.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " date <= str_to_date('"+date_to+"','%m/%d/%Y')";								
		}
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    qq += " order by id desc "+limit;
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.equals("")){
		pstmt.setString(jj++, id);
	    }
	    else{
		if(!obj_type.equals("")){
		    pstmt.setString(jj++, obj_type);
		}
		if(!obj_id.equals("")){
		    pstmt.setString(jj++, obj_id);
		}
	    }
	    rs = pstmt.executeQuery();
	    mediaFiles = new ArrayList<MediaFile>();
	    while(rs.next()){
		MediaFile one = new MediaFile(rs.getString(1),
					      rs.getString(2),
					      rs.getString(3),
					      rs.getString(4),
					      rs.getString(5),
					      rs.getString(6),
					      rs.getString(7),
					      rs.getString(8),
					      rs.getString(9));
		mediaFiles.add(one);
	    }
	}
	catch(Exception ex){
	    msg += " "+ex;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
	
}
