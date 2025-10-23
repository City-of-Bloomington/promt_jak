/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package program.model;
import java.io.*;
import java.sql.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;

public class MediaFile implements java.io.Serializable{

    String id="", file_name="", old_file_name="", user_id="", date="";
    String obj_id="", obj_type="", notes="", year="", ext="";
    static final long serialVersionUID = 133L;
		
    static Logger logger = LogManager.getLogger(MediaFile.class);
    boolean debug = false;
    User user = null;
    public MediaFile(){
    }
    public MediaFile(boolean deb){
	debug = deb;
    }		
    public MediaFile(String val){
	setId(val);
    }	
    public MediaFile(
		     String val,
		     String val2,
		     String val3,
		     String val4,
		     String val5,
		     String val6,
		     String val7,
		     String val8,
		     String val9){
	setValues(val,
		  val2,
		  val3,
		  val4,
		  val5,
		  val6,
		  val7,
		  val8,
		  val9);

    }
    void setValues(String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5,
		   String val6,
		   String val7,
		   String val8,
		   String val9){
	setId(val);
	setFile_name(val2);
	setOld_file_name(val3);
	setObj_id(val4);
	setObj_type(val5);
	setDate(val6);
	setNotes(val7);
	setUser_id(val8);
	setYear(val9);
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
    public String getObj_id(){
	return obj_id;
    }
    public String getObj_type(){
	return obj_type;
    }		
    public String getFile_name(){
	return file_name;
    }
    public String getOld_file_name(){
	return old_file_name;
    }
    public String getYear(){
	if(year.equals("") && !date.equals("")){
	    try{
		year = date.substring(date.lastIndexOf("/")+1);
	    }
	    catch(Exception ex){
		System.err.println(ex);
	    }
	}
	return year;
    }
    public String getDate(){
	return date;
    }
    public String getNotes(){
	return notes;
    }
    public String getObjectLink(){
	String link = "";
	if(obj_type.equals("Program")){
	    link = "Program.do?action=zoom&id="+obj_id;
	}
	return link;
    }
		
    public User getUser(){
	if(user == null && !user_id.equals("")){
	    User one = new User(debug, user_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		user = one;
	    }
	}
	return user;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }	
    public void setFile_name (String val){
	if(val != null)
	    file_name = val;
    }
    public void setOldFileNameAndClean(String val){
	// get rid of special characters but we keep dot for ext 
	if(val != null){
	    old_file_name = val.replaceAll("[^\\dA-Za-z. ]", "_").replaceAll("\\s+", "_");
	}
    }
    public void setOld_file_name (String val){
	if(val != null){
	    old_file_name = val;
	}
    }	
    public void setDate (String val){
	if(val != null)
	    date = val;
    }
    public void setYear (String val){
	if(val != null)
	    year = val;
    }		
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }		
	
    public void setUser_id (String val){
	if(val != null)
	    user_id = val;
    }
    public void setObj_id (String val){
	if(val != null)
	    obj_id = val;
    }
    public void setObj_type(String val){
	if(val != null)
	    obj_type = val;
    }		
	
    public String toString(){
	return old_file_name;
    }
    public boolean hasFileName(){
	return !file_name.equals("");
						
    }
    public String getFullPath(String dir, String ext){
	String path = dir;
	String yy="", separator="/"; // linux
	separator = File.separator;
	if(file_name.equals("")){
	    if(id.equals("")){
		composeName(ext);
	    }
	    else{
		doSelect();
	    }
	}
	if(date.equals("")){
	    yy = ""+Helper.getCurrentYear();
	}
	else{
	    yy = date.substring(6);
	}
	path += yy;
	path += separator;
	File myDir = new File(path);
	if(!myDir.isDirectory()){
	    myDir.mkdirs();
	}
	return path;
    }
    public String composeName(String ext){
	String name = "promt_media_"+obj_id+"."+ext;
	date = Helper.getToday2();
	return name;
    }    
    public String getImageThunbName(){
	String str = "";
	if(!file_name.equals("")){
	    str = file_name.substring(0,file_name.indexOf("."))+"_thumb."+getExt();
	}
	return str;
    }
    public String getExt(){
	if(ext.equals("") && !file_name.equals("")){
	    if(file_name.indexOf(".") > -1){
		ext = file_name.substring(file_name.lastIndexOf(".")+1);
	    }
	}
	return ext;
    }
    public String createImageThumb(File file){
	String back = "";
	if(file != null){
						
	    try{
		String fullPath = file.getParent();
		File thumb_file = new File(fullPath, getImageThunbName());
								
		byte [] imageByteArray =  FileUtils.readFileToByteArray(file);
		InputStream inStream = new ByteArrayInputStream(imageByteArray);
		BufferedImage bufferedImage = ImageIO.read(inStream);
		//
		// after resize save
		//
		BufferedImage bufferedImage2 = resizeImage(bufferedImage);
		//
		ImageIO.write(bufferedImage2, getExt(), thumb_file);
								
	    }catch(Exception ex){
		back += ex;
	    }
	}
	return back;
    }
    BufferedImage resizeImage(BufferedImage image){
	// int scaledWidth,
	// int scaledHeight,
	boolean preserveAlpha = true;
        // System.out.println("resizing...");
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
	int new_w = 0, new_h=0, max_d=72;
	float scale_all = 1f, scale_w=1f, scale_h=1f;
	int org_w = image.getWidth();
	int org_h = image.getHeight();
	new_w = org_w;
	new_h = org_h;
	if(org_w > max_d){
	    scale_w = max_d/(org_w+0f);
	}
	if(org_h > max_d){
	    scale_h = max_d/(org_h+0f);
	}
	// we take the smaller
	if(scale_w < scale_all){
	    scale_all = scale_w;
	}
	if(scale_h < scale_all){
	    scale_all = scale_h;
	}
	if(scale_all < 1f){
	    new_w = (int)(org_w*scale_all);
	    new_h = (int)(org_h*scale_all);						
	}
				
	int currentH = image.getHeight();
	BufferedImage scaledBI = new BufferedImage(new_w, new_h, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(image, 0, 0, new_w, new_h, null); 
        g.dispose();
        return scaledBI;
    }				
    public boolean isImage(){
	getExt();
	return "gif_jpg_png".indexOf(ext) > -1;
    }
    public boolean isPdf(){
	getExt();
	return "pdf".indexOf(ext) > -1;
    }
    public boolean isSheet(){
	getExt();
	return "xls".indexOf(ext) > -1 || "csv".indexOf(ext) > -1;
    }		
    public String updateFileName(String ext){
	if(file_name.indexOf(".") == -1){
	    file_name += ".";
	}
	file_name += ext;
	return file_name;
    }
    public String genNewFileName(String file_ext){
	findNewId(); // first get the next id
	if(file_ext == null)
	    file_ext="";
	if(obj_type.equals("Default")){
	    file_name = "default_"; 
	}
	else{
	    file_name = "prog_";
	}
	if(!id.equals("")){
	    file_name += id; 
	}
	file_name += "."+file_ext;
	return file_name;
    }
    @Override
    public int hashCode() {
	int hash = 7, id_int = 0;
	if(!id.equals("")){
	    try{
		id_int = Integer.parseInt(id);
	    }catch(Exception ex){}
	}
	hash = 67 * hash + id_int;
	return hash;
    }
    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final MediaFile other = (MediaFile) obj;
	return this.id.equals(other.id);
    }
    public String findNewId(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;
	String qq = "insert into media_seq values(0)";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.executeUpdate();
	    qq = "select LAST_INSERT_ID() ";
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
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
    public String doSelect(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;		
	String qq = "select id,file_name,old_file_name,obj_id,obj_type,date_format(date,'%m/%d/%Y'),notes,user_id,year(date) from media where id=?";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);				
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setValues(rs.getString(1),
			  rs.getString(2),
			  rs.getString(3),
			  rs.getString(4),
			  rs.getString(5),
			  rs.getString(6),
			  rs.getString(7),
			  rs.getString(8),
			  rs.getString(9));
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
    public String doSave(){
	String msg="";
	PreparedStatement pstmt = null;
	Connection con = null;
	ResultSet rs = null;
	if(file_name.equals("")){
	    msg = "File name is not set ";
	    return msg;
	}
	String qq = "insert into media values(?,?,?,?,?,now(),?,?)";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg += " could not connect to database";
	    return msg;
	}		
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.setString(2, file_name);
	    pstmt.setString(3, old_file_name);
	    pstmt.setString(4, obj_id);
	    pstmt.setString(5, obj_type);
	    if(notes.equals(""))
		pstmt.setNull(6, Types.VARCHAR);
	    else
		pstmt.setString(6, notes);
	    pstmt.setString(7, user_id);
	    pstmt.executeUpdate();
	    year = ""+Helper.getCurrentYear();
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
