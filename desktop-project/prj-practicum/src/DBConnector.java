import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConnector{
    public static Connection conn = null;

    public static Connection getConnection(){
        try{
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test","sa", "");
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            return conn;
        }
    }
}
