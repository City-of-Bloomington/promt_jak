package program.list;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class ProgramNoteList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(ProgramNoteList.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    List<String> errors = null;
    String message = "";
    String id = "", program_id="", added_by="", sortby="";
    List<ProgramNote> programNotes = null;
    public ProgramNoteList(boolean deb, String val){
	debug = deb;
	setProgram_id(val);
    }
	
    public String getMessage(){
	return message;
    }
	
    public boolean hasMessage(){
	return !message.equals("");
    }
    public boolean hasErrors(){
	return (errors != null && errors.size() > 0);
    }
    public List<String> getErrors(){
	return errors;
    }
    void addError(String val){
	if(errors == null)
	    errors = new ArrayList<String>();
	errors.add(val);
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setProgram_id(String val){
	if(val != null)
	    program_id = val;
    }

    public void setSortby(String val){
	if(val != null)
	    sortby = val;
    }
    public String getId(){
	return id;
    }
    public String getProgram_id(){
	return program_id;
    }
    public List<ProgramNote> getProgramNotes(){
	return programNotes;
    }
    public boolean hasProgramNotes(){
	return programNotes != null && programNotes.size() > 0;
	
    }
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select h.id,h.program_id,h.notes,h.added_by,date_format(h.date_time,'%m/%d/%Y %h:%i') from program_notes h ";			
	String qw = "";
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	try{
	    if(!program_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " h.program_id = ? ";
	    }
	    if(!qw.equals("")){
		qq += " where "+qw;
	    }
	    qq += " order by h.date_time DESC ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    int j=1;
	    if(!program_id.equals("")){
		pstmt.setString(j++, program_id);
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		if(programNotes == null)
		    programNotes = new ArrayList<>();
		ProgramNote one =
		    new ProgramNote(debug,
				rs.getString(1),
				rs.getString(2),
				rs.getString(3),
				rs.getString(4),
				rs.getString(5)
				);
		programNotes.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    message += back;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
}






















































