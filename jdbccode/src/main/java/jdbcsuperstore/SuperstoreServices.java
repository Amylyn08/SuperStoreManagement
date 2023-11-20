package jdbcsuperstore;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Array;

public class SuperstoreServices {
    private Connection conn;

    public SuperstoreServices(String driver, String host, String port, String user, String password) {
        createConnection(driver, host, port, user, password);
    }

    /* method to close connection */
    public void Close() throws SQLException {
        if (this.conn != null)
            this.conn.close();
    }

    public void createConnection(String driver, String host, String port, String user, String password) {
        try {
            if (!this.connectionActive()) {
                this.conn = DriverManager.getConnection(
                        "jdbc:oracle:thin:@198.168.52.211:1521/pdbora19c.dawsoncollege.qc.ca",
                        user, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.conn;
    }

    public boolean connectionActive() throws SQLException {
        return (this.conn != null && !this.conn.isClosed());
    }

    /* CHECK VALIDITY OF IDs */
    public void isProductIDValid(int productID) throws SQLException {
        String orderProc = "{ ? = call getProductIDs()}";
        CallableStatement stmt = conn.prepareCall(orderProc);
        stmt.registerOutParameter(1, Types.ARRAY, "ARRAY_IDS");
        stmt.execute();
        Array productIDs = stmt.getArray(1);
        BigDecimal[] resultArray = (BigDecimal[]) productIDs.getArray();
        List<Integer> results = new ArrayList<Integer>();
        for (BigDecimal id : resultArray) {
            results.add(id.intValue());
        }
        if (!results.contains(productID)) {
            throw new IllegalArgumentException("invalid productID");
        }

    }

    public void isCustomerIDValid(int customerID) throws SQLException {
        String orderProc = "{ ? = call getCustomerIDs()}";
        CallableStatement stmt = conn.prepareCall(orderProc);
        stmt.registerOutParameter(1, Types.ARRAY, "ARRAY_IDS");
        stmt.execute();
        Array customerIDs = stmt.getArray(1);
        BigDecimal[] resultArray = (BigDecimal[]) customerIDs.getArray();
        List<Integer> results = new ArrayList<Integer>();
        for (BigDecimal id : resultArray) {
            results.add(id.intValue());
        }
        if (!results.contains(customerID)) {
            throw new IllegalArgumentException("invalid customerID");
        }
    }

    public void isWarehouseIDValid(int warehouseID) throws SQLException {
        String orderProc = "{ ? = call getWarehouseIDs()}";
        CallableStatement stmt = conn.prepareCall(orderProc);
        stmt.registerOutParameter(1, Types.ARRAY, "ARRAY_IDS");
        stmt.execute();
        Array warehouseIDs = stmt.getArray(1);
        BigDecimal[] resultArray = (BigDecimal[]) warehouseIDs.getArray();
        List<Integer> results = new ArrayList<Integer>();
        for (BigDecimal id : resultArray) {
            results.add(id.intValue());
        }
        if (!results.contains(warehouseID)) {
            throw new IllegalArgumentException("invalid warehouseID");
        }
    }

    public void isStoreIDValid(int storeID) throws SQLException {
        String orderProc = "{ ? = call getStoreIDs()}";
        CallableStatement stmt = conn.prepareCall(orderProc);
        stmt.registerOutParameter(1, Types.ARRAY, "ARRAY_IDS");
        stmt.execute();
        Array storeIDs = stmt.getArray(1);
        BigDecimal[] resultArray = (BigDecimal[]) storeIDs.getArray();
        List<Integer> results = new ArrayList<Integer>();
        for (BigDecimal id : resultArray) {
            results.add(id.intValue());
        }

        if (!results.contains(storeID)) {
            throw new IllegalArgumentException("invalid storeID");
        }
    }

    // functions and procedures here:

    /********** BIANCA *********/

    /**
     * this function takes an Order object as input and creates a new order in the
     * DB
     * 
     * @param orderObj - represents the order object
     * @return - int representing the new OrderID
     * @throws SQLException
     */
    public int createOrder(Order orderObj) throws SQLException {
        String orderProc = "{call createOrder(?, ?)}";
        CallableStatement stmt = conn.prepareCall(orderProc);
        stmt.setObject(1, orderObj);
        stmt.registerOutParameter(2, Types.INTEGER);
        stmt.execute();
        int newOrderId = stmt.getInt(2);
        return newOrderId;
    }

    public void addOrderItem(int newOrderId, int productID, int quantity) throws SQLException {
        String addItem = "{call addOrderItem(?, ?, ?)}";
        CallableStatement stmt = conn.prepareCall(addItem);
        stmt.setInt(1, newOrderId);
        stmt.setInt(2, productID);
        stmt.setInt(3, quantity);
        stmt.execute();
    }

    /*******************/

    /********** AMY *********/
    /**
     * This  method creates and inserts the review into the DB
     * @param productID - represents the productID that holds the review
     * @param customerID - represents the customer that is making the review
     * @param star  -represents the star rating.
     * @param description - represents the description of the rating.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void createReview(int productID, int customerID, int star, String description)throws SQLException, ClassNotFoundException{
        Review review = new Review(productID, customerID, star, description, this.conn);
        String createReview = "{call createReview(?)}";
        CallableStatement stmt = conn.prepareCall(createReview);
        stmt.setObject(1, review);
        stmt.execute();
    }
    /**
     * This method flags a review in the databse
     * @param reviewID
     * @throws SQLException
     */
    public void flagReview(int reviewID) throws SQLException{
        String flagReview = "{call flagReview(?)}";
        CallableStatement stmt = conn.prepareCall(flagReview);
        stmt.setInt(1, reviewID);
        stmt.execute();
    }
    /*******************/

}
