package jdbcsuperstore;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class SuperstoreServices {
    private Connection conn;

    public SuperstoreServices(String driver, String host, String port, String user, String password)
    {
        createConnection(driver, host, port, user, password);
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
                    driver + "//" + host + ":" + port + "/pdbora19c.dawsoncollege.qc.ca",
                    user, password
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Connection getConnection()
    {
        return this.conn;
    }

    public boolean connectionActive() throws SQLException
    {
        return(this.conn != null && !this.conn.isClosed());
    }

    /**
     * this function takes an Order object as input and creates a new order in the DB
     * @param orderObj - represents the order object
     * @return - int representing the new OrderID
     * @throws SQLException
     */
    public int createOrderID(Order orderObj, List<Product> products) throws SQLException
    {
        String orderProc = "{call createOrder(?, ?)}";
        CallableStatement stmt = conn.prepareCall(orderProc);
        stmt.setObject(1, orderObj);
        stmt.registerOutParameter(2, Types.INTEGER);
        stmt.execute();
        int newOrderId = stmt.getInt(2);
        return newOrderId;
    }

    public void addOrderItem(int newOrderId, Product product) throws SQLException
    {
        String addItem = "{call addOrderItem(?, ?, ?)}";
        CallableStatement stmt = conn.prepareCall(addItem);
        stmt.setInt(1, newOrderId);
        stmt.setInt(2, product.getQuantity());
        stmt.setInt(3, product.getID());
        stmt.execute();
    }


    // functions and procedures here:

    /********** BIANCA *********/


    /*******************/
    
    /********** AMY *********/

    /*******************/

}
