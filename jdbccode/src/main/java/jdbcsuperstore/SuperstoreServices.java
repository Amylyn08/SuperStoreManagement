package jdbcsuperstore;
import java.sql.Connection;
import java.sql.DriverManager;

public class SuperstoreServices {
    private Connection conn;

    public SuperstoreServices(String driver, String host, String port, String user, String password)
    {
        this.createConnection(String driver, String host, String port, String user, String password);
    }

    /* method to close connection */
    public void Close() throws SQLException
    {
        if (this.conn != null)
            this.conn.close();
    }

    public void createConnection(String driver, String host, String port, String user, String password)
    {
        try 
        {
            if(this.connectionActive())
            {
                this.conn = DriverManager.getConnection(
                    driver + "@" + host + ":" + port + "/pdbora19c.dawsoncollege.qc.ca",
                    user, password
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public boolean connectionActive() throws SQLException
    {
        return(this.conn != null && !this.connection.isClosed());
    }

    /********** BIANCA *********/


    /*******************/
    
    /********** AMY *********/

    /*******************/

}
