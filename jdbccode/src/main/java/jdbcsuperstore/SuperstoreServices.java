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
                String logLogin = "{call logUserLogin(?)}";
                CallableStatement stmt = this.conn.prepareCall(logLogin);
                stmt.setString(1, user);
                stmt.execute();
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

    /**
     * this function receives a productID as input and checks if that productID is in the db
     * @param productID - represents the productID
     * @throws SQLException
     */
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

    /**
     * this function receives a customerID as input and checks if that customerID is in the db
     * @param customerID - represents the customerID
     * @throws SQLException
     */
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

    /**
     * this function receives a warehouseID as input and checks if that warehouseID is in the db
     * @param warehouseID - represents the warehouseID
     * @throws SQLException
     */
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

    /**
     * this function receives a storeID as input and checks if that storeID is in the db
     * @param storeID - represents the storeID
     * @throws SQLException
     */
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

    /**
     * this function takes the parameters necessary in order to add an order item using the addOrderItem procedure
     * @param newOrderId - represents the orderID of the order that has just been created
     * @param productID - represents the product to add to that order
     * @param quantity - represents the quantity of the product they would like to add
     * @throws SQLException
     */
    public void addOrderItem(int newOrderId, int productID, int quantity) throws SQLException {
        isProductIDValid(productID);
        String addItem = "{call addOrderItem(?, ?, ?)}";
        CallableStatement stmt = conn.prepareCall(addItem);
        stmt.setInt(1, newOrderId);
        stmt.setInt(2, productID);
        stmt.setInt(3, quantity);
        stmt.execute();
    }

    /**
     * this function returns the total inventory of a product with a particular ID
     * @param productID - represents the product whose inventory we are checking
     * @return - returns the total inventory
     * @throws SQLException
     */
    public int totalInventory(int productID) throws SQLException
    {
        isProductIDValid(productID);
        String inventoryProc = "{? = call totalInventory(?)}";
        CallableStatement stmt = conn.prepareCall(inventoryProc);
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.setInt(2, Types.INTEGER);
        stmt.execute();
        int total = stmt.getInt(1);
        return(total);
    }

    /**
     * this function will retrieve a list of all the customers whose comments have been flagged at least once
     * @return - returns a list of customer names
     * @throws SQLException
     */
    public List<String> flaggedCustomers() throws SQLException
    {
        List<String> customers = new ArrayList<String>();
        String flaggedCusProc = "{ ? = call calculations.flaggedCustomers()}";
        CallableStatement stmt = conn.prepareCall(flaggedCusProc);
        stmt.registerOutParameter(1, Types.ARRAY, "CALCULATIONS.CUS_NAMES");
        stmt.execute();
        Array cusNames = stmt.getArray(1);
        String[] resultArray = (String[]) cusNames.getArray();
        List<Integer> results = new ArrayList<Integer>();
        for (String name : resultArray) {
            customers.add(name);
        }
        return customers;
    }

    /**
     * this function takes a productID and and removes that product from the table 
     * @param productID
     * @throws SQLException
     */
    public void removeProduct(int productID) throws SQLException {
        isProductIDValid(productID);
        String removeProd = "{call removeProduct(?)}";
        CallableStatement stmt = conn.prepareCall(removeProd);
        stmt.setInt(1, productID);
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
    public void createReview(Review review)throws SQLException{
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

    /**
     * This function gets the average review score for a product
     * @param productID - the victim.
     * @return - returns back the average review score.
     * @throws SQLException
     */
    public double calculateAvgReviewScore(int productID) throws SQLException{
        String gettingAvg = "{? = call calculateAvgReviewScore(?)}";
        CallableStatement stmt = conn.prepareCall(gettingAvg);
        stmt.setInt(2, productID);
        stmt.execute();
        double avgScore = stmt.getDouble(1);
        return avgScore;
    }
    /**
     * This function 
     * @param productID
     * @return
     * @throws SQLException
     */
    public int numOrders(int productID) throws SQLException{
        String sql = "{? = call numOrders(?)}";
        CallableStatement stmt = conn.prepareCall(sql);
        stmt.setInt(2, productID);
        stmt.execute();
        int num = stmt.getInt(1);
        return num;
    }
    /**
     * This function 
     * @param warehouseID
     * @throws SQLException
     */
    public void removeWarehouse(int warehouseID) throws SQLException{
        String sql = "{call removeWarehouse(?)}";
        CallableStatement stmt = conn.prepareCall(sql);
        stmt.setInt(1, warehouseID);
        stmt.execute();
    }
    /*******************/

}
