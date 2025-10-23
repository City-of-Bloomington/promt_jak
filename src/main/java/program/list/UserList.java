package program.list;
import java.util.*;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.model.*;
import program.util.*;

public class UserList extends ArrayList<User>{

    String userid="", id="", full_name="", dept="", role="", active="";
    boolean debug = false;
    String errors = "";
    static Logger logger = LogManager.getLogger(UserList.class);
    public UserList(boolean deb){
	debug = deb;
    }

    //
    // getters
    //
    public List<User> getUsers(){
	return this;
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }		
    public void setUserid (String val){
	if(val != null)
	    userid = val;
    }
    public void setActive (String val){
	if(val != null)
	    active = val;
    }
    public void setRole (String val){
	if(val != null)
	    role = val;
    }
    public void setDept (String val){
	dept = val;
    }
    public String lookFor(){
	String msg="";
	String qq = "select * from users ";
	String qw = "";		
	PreparedStatement pstmt = null;
	ResultSet rs = null;		
	Connection con = Helper.getConnection();
	if(con == null){
	    msg = " Could not connect to database ";
	    return msg;
	}
	if(!id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " id = ? ";
	}		
	if(!userid.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " userid =? ";
	}
	if(!active.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " active=? ";
	}
	if(!role.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " role=? ";
	}
	if(qw.equals("")){
	    qq += " where "+qw;
	}
	if(debug)
	    logger.debug(qq);

	try{
	    pstmt = con.prepareStatement(qq);
	    int jj = 1;
	    if(!id.equals("")){
		pstmt.setString(jj,id);
		jj++;
	    }			
	    if(!userid.equals("")){
		pstmt.setString(jj,userid);
		jj++;
	    }
	    if(!active.equals("")){
		pstmt.setString(jj,"y");
		jj++;
	    }
	    if(!role.equals("")){
		pstmt.setString(jj,role);
		jj++;
	    }
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		User user = new User(debug,
				     rs.getString(1),
				     rs.getString(2),
				     rs.getString(3),
				     rs.getString(4),
				     rs.getString(5));
		add(user);
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
