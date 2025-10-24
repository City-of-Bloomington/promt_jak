package program.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem; // For Commons FileUpload 2
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import org.apache.commons.fileupload2.jakarta.servlet6.*;
import org.apache.commons.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/PromtFile.do","/PromtFile"})
public class PromtFileServ extends TopServlet{

    static final long serialVersionUID = 24L;
    static Logger logger = LogManager.getLogger(PromtFileServ.class);
    int maxImageSize = 10000000, maxDocSize=10000000;
    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
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
	String saveDirectory ="",file_path="";
	String newFile = "";
	String action="", date="", load_file="";
	String id="", related_id = "", type="", notes="", action2="";
	
	String message = "";
	int maxMemorySize = 10000000; // 5 MB , int of bytes
	int maxRequestSize = 10000000; // 5 MB
	String [] vals;
	User user = null;
	HttpSession session = null;
	long sizeInBytes = 0;
	// 
	// class to handle multipart request (for example text, image, pdf)
	// the image file or any upload file will be saved to the 
	// specified directory
	// 
	// we need this path for save purpose
	session = req.getSession(false);

	if(session != null){
	    user = (User)session.getAttribute("user");
	    if(user == null){
		String str = url+"Login";
		res.sendRedirect(str);
		return; 
	    }
	}
	else{
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return; 
	}
	// 
	// we have to make sure that this directory exits
	// if not we create it
	//
	
	PromtFile promtFile = new PromtFile(debug);
	String new_path = promtFile.getPath(server_path);
	Path path = Paths.get(new_path);
	File myDir = new File(new_path);
	if(!myDir.isDirectory()){
	    myDir.mkdirs();
	}

	
	// newFile = "spon"+month+day+seq; // no extension 
	// boolean isMultipart = ServletFileUpload.isMultipartContent(req);
	// System.err.println(" Multi "+isMultipart);
	//
	// Create a factory for disk-based file items
	FileCleaningTracker tracker = JakartaFileCleaner.getFileCleaningTracker(context);
	DiskFileItemFactory factory = DiskFileItemFactory.builder()
	    .setPath(path)
	    //.setSizeThreshold(maxDocSize)
	    .setBufferSizeMax(maxMemorySize)
	    .get();
	
	//
	// Set factory constraints
	//
	// if not set will use system temp directory
	// factory.setRepository(fileDirectory); 
	//
	// Create a new file upload handler
	JakartaServletDiskFileUpload upload = new JakartaServletDiskFileUpload(factory);

	// Set overall request size constraint
	upload.setFileSizeMax(maxRequestSize);
	// ServletFileUpload upload = new ServletFileUpload();
	//
	// Set overall request size constraint
	upload.setSizeMax(maxRequestSize);
	//
	String ext = "", old_name="";

	List<DiskFileItem> items = null;
	String content_type = req.getContentType();
	try{
	    if(JakartaServletDiskFileUpload.isMultipartContent(req)){	    
		items = upload.parseRequest(req);
		Iterator<DiskFileItem> iter = items.iterator();
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
			    promtFile.setId(value);
			}
			else if (name.equals("notes")) {
			    notes=value;
			    promtFile.setNotes(value);
			}
			else if (name.equals("type")) {
			    type=value;
			    promtFile.setType(value);
			}												
			else if (name.equals("related_id")){ 
			    related_id = value;
			    promtFile.setRelated_id(value);
			}
			else if (name.equals("load_file")) {
			    load_file =value.replace('+',' ');
			}
			else if(name.equals("action")){
			    action = value;
			}
		    }
		    else {
			// String mimType = Magic.getMagicMatch(item.get(), false).getMimeType();
			// System.err.println(" type "+mimType);
			//
			// process uploaded item/items
			String contentType = item.getContentType();
			if(Helper.mimeTypes.containsKey(contentType)){
			    ext = Helper.mimeTypes.get(contentType);
			}
			sizeInBytes = item.getSize();
			String oldName = item.getName();
			String filename = "";
			// 
			logger.debug("file "+oldName);
			if (oldName != null && !oldName.equals("")) {
			    filename = FilenameUtils.getName(oldName);
			    old_name = filename;
			    if(ext.equals("")){
				ext = Helper.getFileExtensionFromName(filename);
			    }
			    //
			    // create the file on the hard drive and save it
			    //
			    if(sizeInBytes > maxDocSize) 
				sizeLimitExceeded = true;
			    if(sizeLimitExceeded){
				message = " File Uploaded exceeds size limits "+
				    sizeInBytes;
				success = false;
			    }
			    else if(success){
				//
				// get a new name
				//
				promtFile.setOldName(old_name);
				promtFile.composeName(ext);
				newFile = promtFile.getName();
				if(!newFile.equals("")){
				    new_path = promtFile.getFullPath(server_path, ext);
				    Path uploadedFile = Paths.get(new_path+newFile);
				    item.write(uploadedFile);
				}
				else{
				    message = "Error: no file name assigned ";
				    success = false;
				}
			    }
			}
		    }
		}
	    }
	    else{
		Enumeration<String> values = req.getParameterNames();
		while (values.hasMoreElements()){
		    name = values.nextElement().trim();
		    vals = req.getParameterValues(name);
		    if(vals == null) continue;
		    value = vals[vals.length-1].trim();	
		    if (name.equals("id")){
			id = value;
			promtFile.setId(value);
		    }
		    else if(name.equals("related_id")){
			related_id = value;
			promtFile.setRelated_id(value);
		    }
		    else if(name.equals("type")){
			type = value;
			promtFile.setType(value);
		    }										
		    else if(name.equals("notes")){
			promtFile.setNotes(value);
		    }
		    else if(name.equals("action")){
			action = value;
		    }	
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex);
	    success = false;
	    message += ex;
	}
	//
	if(action.equals("Save") && !sizeLimitExceeded){
	    date = Helper.getToday2(); // mm/dd/yyyy format
	    promtFile.setAddedBy(user);
	    String back = promtFile.doSave();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		id = promtFile.getId();
	    }
	}
	if(action.equals("Update")){
	    String back = promtFile.doUpdate();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}
	else if(action.equals("Delete")){
	    String back = promtFile.doDelete();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		id="";
	    }
	}	
	else if(action.equals("download")){
	    String back = promtFile.doSelect();
	    String filename = promtFile.getName();
	    String filePath = promtFile.getPath(server_path);
	    filePath += filename;
	    doDownload(req, res, filePath, promtFile);
	    return;
	}
	else if(action.equals("") && !id.equals("")){
	    String back = promtFile.doSelect();
	    related_id = promtFile.getRelated_id();
	    type = promtFile.getType();						
	}
	RelatedUtil related = null;
	List<PromtFile> files = null;
	if(!related_id.equals("")){
	    related = new RelatedUtil(debug, related_id, type, url);
	    PromtFileList rfl = new PromtFileList(debug, related_id, type);
	    String back = rfl.find();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		List<PromtFile> ones = rfl.getFiles();
		if(ones != null && ones.size() > 0){
		    files = ones;
		}
	    }
	}
	Helper.writeWebCss(out, url);
	out.println("<html><head><title>Promt Files</title>");
	out.println("<div id=\"mainContent\">");
	out.println("<script type=\"text/javascript\">");
	out.println("  function validateForm(){		         ");
       	out.println("     return true;				         ");
	out.println("	}	         			             ");
	out.println(" </script>		                         ");
	out.println(" </head><body><center>                          ");
	Helper.writeTopMenu(out, url);
	out.println(" <h3>File Attachments</h3>");
	if(!message.equals(""))
	    out.println("<h3>"+message+"</h3>");
	out.println("<form name=\"myForm\" method=\"post\" "+
		    "ENCTYPE=\"multipart/form-data\" >");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	}
	if(!related_id.equals("")){
	    out.println("<input type=\"hidden\" name=\"related_id\" value=\""+related_id+"\" />");
	    out.println("<input type=\"hidden\" name=\"type\" value=\""+type+"\" />");						
	}	
	//

	out.println("<table border=\"1\" width=\"75%\">");
	out.println("<caption>Upload Files</caption>");	
	out.println("<tr><td>");
	//
	out.println("<table width=\"100%\">");

	if(related != null && related.foundType()){
	    out.println("<tr><td><label>Related "+related.getType()+" :</label>"+related.getLink()+"</td></tr>");						
	}
	else{
	    out.println("<tr><td><label>Related "+type+" ID: </label>");						
	    out.println(related_id+"</td></tr>");
	}
	if(id.equals("")){
	    out.println("<tr><td>"+
			"To upload a new document "+
			"<ul>"+
			"<li>Download it to your computer </li>"+
			"<li>Click on the Browse button to locate this file"+
			" on your computer.</li> "+
			"<li> Click on Save.</li>"+
			"<li> A new link to the uploaded "+
			" file will be shown below.</li>"+
			"<li> Supported documents are images, MS Documents, PDF;s, web pages, spread sheets, etc </li>"+
			"</ul>");
	    out.println("</td></tr>");
	    out.println("<tr><td><label for=\"file_up\">File </label> "); 
	    out.println("<input type=\"file\" name=\"load_file\" id=\"file_up\" "+
			" size=\"30\"></td></tr>");
	    out.println("<tr><td class=\"left\"><label for=\"notes\">Notes </label></td></tr>");
	    out.println("<tr><td class=\"left\">"); 
	    out.println("<textarea id=\"notes\" name=\"notes\" cols=\"70\" rows=\"5\" wrap=\"wrap\">");
	    out.println("</textarea></td></tr>");
	    out.println("</table></td></tr>");												
	    out.println("<tr><td align=\"right\">  "+
			"<input type=\"submit\" name=\"action\" "+
			"value=\"Save\">"+
			"</td></tr>");

	}
	else{
	    out.println("<tr><td><label>Date: </label>"+promtFile.getDate()+"</td></tr>");
	    out.println("<tr><td><label>Added By: </label>"+promtFile.getAddedBy()+"</td></tr>");						
	    out.println("<tr><td><label>File Download: </label> <a href=\""+url+"PromtFile.do?id="+id+"&action=download\"> "+promtFile.getOldName()+"</a> </td></tr>");
	    if(promtFile.hasNotes()){
		out.println("<tr><td><label>Notes: </label>"+promtFile.getNotes()+"</td></tr>");
	    }
	    out.println("<tr>");
	    out.println("<td align=\"right\">  "+
			"<input type=\"submit\" name=\"action\" "+
			"onclick=\"validateDelete();\" "+
			"value=\"Delete\">"+
			"</td>");
	    out.println("</tr>");				
	}

	out.println("</table>");
	out.println("</form>");
	if(files != null && files.size() > 0){
	    Helper.printFiles(out, url, files);
	}
	//
	// send what we have so far
	//
	out.print("</body></html>");
	out.close();

    }

    void doDownload(HttpServletRequest request,
		    HttpServletResponse response,
		    String inFile,
		    PromtFile promtFile){
		
	BufferedInputStream input = null;
	BufferedOutputStream output = null;
	try{
	    //
	    // Decode the file name (might contain spaces and so on) and prepare file object.
	    // File file = new File(filePath, URLDecoder.decode(inspFile, "UTF-8"));
	    File file = new File(inFile);
	    //
	    // Check if file actually exists in filesystem.
	    //
	    if (!file.exists()) {
		// Do your thing if the file appears to be non-existing.
		// Throw an exception, or send 404, or show default/warning page, or just ignore it.
		response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
		return;
	    }
	    //
	    // Get content type by filename.
	    String contentType = context.getMimeType(file.getName());
	    //
	    // To add new content types, add new mime-mapping entry in web.xml.
	    if (contentType == null) {
		contentType = "application/octet-stream";
	    }
	    //			
	    // Init servlet response.
	    response.reset();
	    response.setBufferSize(DEFAULT_BUFFER_SIZE);
	    response.setContentType(contentType);
	    response.setHeader("Content-Length", String.valueOf(file.length()));
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + (promtFile.getOldName().equals("")?promtFile.getName():promtFile.getOldName()) + "\"");
	    //
	    // Prepare streams.
	    //
            // Open streams.
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
	}
	catch(Exception ex){
	    logger.error(ex);
        } finally {
	    close(output);
            close(input);
        }
    }	
    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }
}






















































