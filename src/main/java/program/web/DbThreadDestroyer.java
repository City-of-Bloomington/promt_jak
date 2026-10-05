package program.web;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.annotation.WebListener;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Set;
import java.util.Enumeration;
import java.sql.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class DbThreadDestroyer implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(DbThreadDestroyer.class);

   @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Nothing needed on startup
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Shut down the MySQL AbandonedConnectionCleanupThread
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            sce.getServletContext().log("Failed to shut down MySQL cleanup thread", e);
        }
	// Deregister JDBC drivers to prevent further memory leaks
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (Exception e) {
                sce.getServletContext().log("Failed to deregister JDBC driver: " + driver, e);
            }
        }
    }
}

