package program.web;
import java.net.URI;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.util.*;

public class TopServlet extends HttpServlet {
    static String url = "", cur_ver="";
    static String cookieName="", cookieValue="";
    static String server_path="";
    static boolean debug = false;
    static Configuration config = null;
    static Logger logger = LogManager.getLogger(TopServlet.class);
    static ServletContext context = null;
    public void init(ServletConfig conf){
	try{
	    context = conf.getServletContext();
	    url = context.getInitParameter("url");
	    String str = context.getInitParameter("debug");
	    if(str != null && str.equals("true")) debug = true;
	    str = context.getInitParameter("cur_ver");
	    if(str != null) cur_ver = str;
	    str = context.getInitParameter("server_path");
	    if(str != null) server_path = str;	    
	    str = context.getInitParameter("cookieName");
	    if(str != null)
		cookieName = str;
	    str = context.getInitParameter("cookieValue");
	    if(str != null)
		cookieValue = str;
	    String username = context.getInitParameter("adfs_username");
	    String auth_end_point = context.getInitParameter("auth_end_point");
	    String token_end_point = context.getInitParameter("token_end_point");
	    String callback_uri = context.getInitParameter("callback_uri");
	    String client_id = context.getInitParameter("client_id");
	    String client_secret = context.getInitParameter("client_secret");
	    String scope = context.getInitParameter("scope");
	    String discovery_uri = context.getInitParameter("discovery_uri");
	    config = new
		Configuration(auth_end_point, token_end_point, callback_uri, client_id, client_secret, scope, discovery_uri, username);
	    // System.err.println(config.toString());
	}catch(Exception ex){
	    System.err.println(" top init "+ex);
	    logger.error(" "+ex);
	}
    }

}
