package program.web;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import javax.naming.*;
import javax.naming.directory.*;
import javax.sql.*;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem; // For Commons FileUpload 2
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import org.apache.commons.fileupload2.jakarta.servlet6.*;
import org.apache.commons.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;
import program.model.*;
import program.list.*;
/**
 * Probably not needed anymore
 *
 * Generates the interface to handle media uploading.
 * Uses third party package to handle multi part forms.
 * A new version uses Apache commons multipart package
 */

@SuppressWarnings("unchecked")
@WebServlet(urlPatterns = {"/MediaUpload.do","/MediaUpload"})
public class MediaUploadServ extends TopServlet{

    final static long serialVersionUID = 390L;
    static Logger logger = LogManager.getLogger(MediaUploadServ.class);


    /**
     * Generates the main upload or view image form.
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req,res);
    }
    /**
     * @link #doGet
     * @see #doGet
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
    
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	boolean success = true,
	    sizeLimitExceeded = false;
	String saveDirectory ="",image_path="";
	String action="", old_file_name="", file_name="";
	String id="", obj_id="", obj_type="", notes="", action2="";
	int maxMemorySize = 10000000; // 10 MB , int of bytes
	int maxRequestSize = 10000000; // 10 MB	
	String message = "";
	String [] vals;
	User user = null;
	HttpSession session = null;
	long sizeInBytes = 0;
	// 
	// 
	// we have to make sure that this directory exits
	// if not we create it
	//
	saveDirectory = server_path+Helper.getCurrentYear()+"/";

	Path path = Paths.get(saveDirectory);
	File myDir = new File(saveDirectory);
	if(!myDir.isDirectory()){
	    myDir.mkdirs();
	}
	session = req.getSession(false);
	if(session != null){
	    user = (User)session.getAttribute("user");
	    if(user == null){
		String str = url+"Login?source=MediaUploadServ&obj_id="+obj_id+"&obj_type="+obj_type;
		if(!id.equals("")){
		    str += "&id="+id;
		}
		res.sendRedirect(str);
		return; 
	    }
	}
	else{
	    String str = url+"Login?source=MediaUploadServ&obj_id="+obj_id+"&obj_type="+obj_type;
	    if(!id.equals("")){
		str += "&id="+id;
	    }			
	    res.sendRedirect(str);
	    return; 
	}
	//
	String content_type = req.getContentType();						
	String ext = "";
	boolean actionSet = false;
	MediaFile media = new MediaFile(debug);
	FileCleaningTracker tracker = JakartaFileCleaner.getFileCleaningTracker(context);
	DiskFileItemFactory factory = DiskFileItemFactory.builder()
	    .setPath(path)
	    //.setSizeThreshold(maxDocSize)
	    .setBufferSizeMax(maxMemorySize)
	    .get();
	JakartaServletDiskFileUpload upload = new JakartaServletDiskFileUpload(factory);

	// Set overall request size constraint
	upload.setFileSizeMax(maxRequestSize);
	// ServletFileUpload upload = new ServletFileUpload();
	//
	// Set overall request size constraint
	upload.setSizeMax(maxRequestSize);
	//
	String old_name="";

	List<DiskFileItem> items = null;
	try{
	    if(JakartaServletDiskFileUpload.isMultipartContent(req)){	    
		//if(content_type != null && content_type.startsWith("multipart")){
		items = upload.parseRequest(req);
		Iterator<DiskFileItem> iter = items.iterator();	    
		//
		while (iter.hasNext()) {
		    FileItem item = iter.next();
		    if (item.isFormField()) {
			//
			// process form fields
			//
			name = item.getFieldName();
			value = item.getString();
			if (name.equals("id")){  
			    id = value;
			    media.setId(value);
			}
			else if (name.equals("notes")) {
			    media.setNotes(value);
			}
			else if (name.equals("obj_id")){ 
			    obj_id =value;
			    media.setObj_id(value);
			}
			else if (name.equals("obj_type")){ 
			    obj_type =value;
			    media.setObj_type(value);
			}												
			else if (name.equals("file_name")) {
			    old_file_name = value.replace('+',' ');
			    media.setOld_file_name(old_file_name);
			}
			else if (name.equals("date")) {
			    media.setDate(value);
			}
			else if(name.equals("action")){
			    // we want the first (which is the last in the array);
			    if(!actionSet){
				actionSet = true;
				if(value.equals("New")) action = "";
				else action = value;
			    }
			}
		    }
		    else {
			//
			// process uploaded item/items
			//
			String fieldName = item.getFieldName();
			String contentType = item.getContentType();
			String temp_file_name = item.getName();
			old_file_name = FilenameUtils.getName(temp_file_name);
			String extent = "";
			// ext = "pdf";
			if(old_file_name.indexOf(".") > -1){
			    extent = old_file_name.substring(old_file_name.lastIndexOf(".")).toLowerCase();
			    if(extent.startsWith(".jp"))
				ext = "jpg";
			    else if(extent.startsWith(".gif"))
				ext = "gif";
			    else if(extent.startsWith(".png"))
				ext = "png";
			    else if(extent.startsWith(".pdf"))
				ext = "pdf";
			    else if(extent.startsWith(".txt"))
				ext = "txt";
			    else if(extent.startsWith(".xlsx"))
				ext = "xlsx";
			    else if(extent.startsWith(".xls"))
				ext = "xls";
			    else if(extent.startsWith(".csv"))
				ext = "csv";														
			    else if(extent.startsWith(".docx"))
				ext = "docx";														
			    else if(extent.startsWith(".doc"))
				ext = "doc";
			    else if(extent.startsWith(".htm"))
				ext = "html";
			}
			file_name = media.genNewFileName(ext);
			media.setUser_id(user.getId());
			media.setOldFileNameAndClean(old_file_name);
			//
			if(!file_name.isEmpty()){
			    String new_path = media.getFullPath(server_path, ext);			
			    Path uploadedFile = Paths.get(new_path+file_name);
			    item.write(uploadedFile);
			    File file = new File(new_path+file_name);
			    if(media.isImage()){
				String back = media.createImageThumb(file);
				if(!back.equals("")) message += "Error create thumb "+ back;
			    }
			}
		    }
		} // end while
	    }
	    else{ // regular url
		Enumeration<String> values = req.getParameterNames();
		while (values.hasMoreElements()){
		    name = values.nextElement().trim();
		    vals = req.getParameterValues(name);
		    value = vals[vals.length-1].trim();	
		    if (name.equals("id")){  
			id = value;
			media.setId(value);
		    }
		    if (name.equals("obj_id")){  
			obj_id = value;
			media.setObj_id(value);
		    }
		    if (name.equals("obj_type")){  
			obj_type = value;
			media.setObj_type(value);
		    }										
		    if (name.equals("action")){  
			action = value;
		    }					
		    else if (name.equals("notes")) {
			notes = value.trim();
			media.setNotes(value);
		    }
		}
	    }
	}
	catch(Exception ex){
	    success = false;
	    message = "Error "+ex;
	    logger.error(message);
	}
		
	//
	if(action.equals("Save")){
	    //
	    if(!media.hasFileName()){
		media.genNewFileName(ext);
	    }
	    String back = media.doSave();
	    if(!back.equals("")){
		success = false;
		message = "Could not save ";
	    }
	}
	else{ 
	    media = new MediaFile(debug);
	    media.setObj_id(obj_id);
	    media.setObj_type(obj_type);
	    id="";
	}
	//
	out.println("<html><head><title>Promt Media</title>");
	out.println("<script type=\"text/javascript\">");
	out.println("  function validateForm(){		                 ");
	out.println(" if (document.myForm.notes){                        ");
	out.println(" if ((document.myForm.notes.value.length>500)){     ");
	out.println("  alert(\"Notes field should be 500 char or less\");");
	out.println("  document.myForm.notes.value = "+
		    "document.myForm.notes.value.substr(0,500);          "); 
	out.println("  	  document.myForm.notes.focus();                 ");
	out.println("     return false;				       	 ");
	out.println("	}}						 ");
       	out.println("     return true;				         ");
	out.println("	}	         			         ");
	//
	out.println("  function validateDelete(){	                 ");
	out.println("   var x = false;                                   ");
	out.println("   x = confirm(\"Are you sure you want to delete "+
		    "this record\");");
	out.println("     return x;                                      ");
	out.println("	}						 ");
	out.println(" </script>		                     ");
	out.println(" </head><body>                          ");
	out.println(" <center><h3>Media Attachments</h3>");
	//
	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3><font color=\"red\">"+message+"</font></h3>");
	}
	out.println("<form method=\"post\" "+
		    "ENCTYPE=\"multipart/form-data\">");

	if(!obj_id.equals(""))
	    out.println("<input type=\"hidden\" name=\"obj_id\" value=\""+obj_id+"\" />");
	if(!obj_type.equals(""))
	    out.println("<input type=\"hidden\" name=\"obj_type\" value=\""+obj_type+"\" />");				
				
	//
	out.println("<table border width=\"80%\">");
	out.println("<caption>Upload New Media</caption>");
	//
	//
	//
	// 1st block
	//
	if(media.getObj_type().equals("Program")){
	    out.println("<tr><td align=\"left\"><b>Related "+media.getObj_type()+":</b></td><td><a href=\""+url+media.getObjectLink()+"\">"+media.getObj_id()+"</a></td></tr>");
	}
	out.println("<tr><td colspan=\"2\">To upload a new file, please consider the following:<uL>"+
		    " <li>You can upload media files</li>"+
		    " <li>You may get an error if the file is too big to upload or unsuppored type</li>"+
		    " <li>If the upload is successful, a link to the uploaded file will show in the list belw</li>"+
		    " <li>To make sure it is successful, click on the new link to see the file</li>");
	out.println("</ul></td></tr>");
	out.println("<tr><td width=\"30%\"><label for=\"file\">Pick File to Upload: </label></td>"); 
	out.println("<td><input type=\"file\" name=\"old_file_name\" "+
		    " size=\"30\" id=\"file\"/></td></tr>");
	out.println("<tr><td colspan=\"2\"><label for=\"notes\">Notes </label>"+
		    "(optional) up to 500 characters</td></tr>");
	out.println("<tr><td colspan=\"2\">");				
	out.println("<textarea rows=\"5\" cols=\"80\" wrap name=\"notes\"></textarea id=\"notes\">");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\">"+
		    "<input type=\"submit\" "+
		    "name=\"action\" "+
		    "value=\"Save\" />"+
		    "</td></tr>");
	out.println("</table><br />");
	out.println("</form>");
	//
	// show what we have so far
	//
	/**
	if(!obj_id.equals("") && !obj_type.equals("")){
	    MediaFileList mfl = new MediaFileList(debug, obj_id, obj_type);
	    String back = mfl.find();
	    if(back.equals("")){
		List<MediaFile> ones = mfl.getMediaFiles();
	    }
	}
	*/
	out.print("</body></html>");
	out.close();
    }
	
}






















































