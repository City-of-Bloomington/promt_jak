package program.util;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonInc{

    public boolean debug = false;
    static Logger logger = LogManager.getLogger(CommonInc.class);
    public String message = "";
    List<String> errors = null;
    //
    public CommonInc(){
    }
    public CommonInc(boolean deb){
	debug = deb;
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
    public void addError(String val){
	if(errors == null)
	    errors = new ArrayList<String>();
	errors.add(val);
    }

}
