package jdbcsuperstore;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
    
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException
    {
        Scanner scan = new Scanner(System.in);
        String user = scan.nextLine();
        String password = scan.nextLine();
        Connection conn = null;
        SuperstoreServices services = null;
        try {
            services = new SuperstoreServices("jdbc:oracle:thin", "localhost", "1521", user, password);
            conn = services.getConnection();
        
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        /*catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("course could not be added");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("class could not be found");
        }*/
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            services.Close();
        }
    }
}
