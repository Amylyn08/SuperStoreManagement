package jdbcsuperstore;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.sql.Struct;

import javax.naming.spi.DirStateFactory.Result;

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
     * this function receives a productID as input and checks if that productID is
     * in the db
     * 
     * @param productID - represents the productID
     * @throws SQLException
     */
    public void isProductIDValid(int productID) throws SQLException {
        String sql = "{ ? = call getProductIDs(?)}";
        CallableStatement stmt = conn.prepareCall(sql);
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.setInt(2, productID);
        stmt.execute();

        int result = stmt.getInt(1);

        if (result == 0) {
            throw new IllegalArgumentException("invalid productID");
        }
    }

    /**
     * this function receives a customerID as input and checks if that customerID is
     * in the db
     * 
     * @param customerID - represents the customerID
     * @throws SQLException
     */
    public void isCustomerIDValid(int customerID) throws SQLException {
        String sql = "{ ? = call getCustomerIDs(?)}";
        CallableStatement stmt = conn.prepareCall(sql);
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.setInt(2, customerID);
        stmt.execute();

        int result = stmt.getInt(1);

        if (result == 0) {
            throw new IllegalArgumentException("invalid customer ID");
        }
    }

    /**
     * this function receives a warehouseID as input and checks if that warehouseID
     * is in the db
     * 
     * @param warehouseID - represents the warehouseID
     * @throws SQLException
     */
    public void isWarehouseIDValid(int warehouseID) throws SQLException {
        String sql = "{ ? = call getWarehouseIDs(?)}";
        CallableStatement stmt = conn.prepareCall(sql);
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.setInt(2, warehouseID);
        stmt.execute();

        int result = stmt.getInt(1);

        if (result == 0) {
            throw new IllegalArgumentException("invalid warehouse ID");
        }
    }

    /**
     * this function receives a storeID as input and checks if that storeID is in
     * the db
     * 
     * @param storeID - represents the storeID
     * @throws SQLException
     */
    public void isStoreIDValid(int storeID) throws SQLException {
        String sql = "{ ? = call getStoreIDs(?)}";
        CallableStatement stmt = conn.prepareCall(sql);
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.setInt(2, storeID);
        stmt.execute();

        int result = stmt.getInt(1);

        if (result == 0) {
            throw new IllegalArgumentException("invalid store ID");
        }
    }

    public void isReviewIDValid(int reviewID) throws SQLException {
        String sql = "{ ? = call getReviewIDs(?)}";
        CallableStatement stmt = conn.prepareCall(sql);
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.setInt(2, reviewID);
        stmt.execute();

        int result = stmt.getInt(1);

        if (result == 0) {
            throw new IllegalArgumentException("invalid review ID");
        }
    }

    // functions and procedures here:

    /********** BIANCA *********/

    /**
     * this function will call the viewCustomer procedure and loop through the cursor to print out info
     * for all current customers in our table.
     * @return - represents the list of customers
     * @throws SQLException
     */

     public List<Customer> viewCustomers() throws SQLException
     {
        CallableStatement cs = this.conn.prepareCall("{call viewPackage.viewCustomers(?)}");
        cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        cs.execute();
        ResultSet rs = (ResultSet)cs.getObject(1);
        List<Customer> customers = new ArrayList<Customer>();
        while(rs.next())
        {
            customers.add(new Customer(
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("email"),
                rs.getString("streetAddress"),
                rs.getString("city"),
                rs.getString("province"),
                rs.getString("country")
                 ));
        }
        return customers;

     }

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

    /**
     * this function takes the parameters necessary in order to add an order item
     * using the addOrderItem procedure
     * 
     * @param newOrderId - represents the orderID of the order that has just been
     *                   created
     * @param productID  - represents the product to add to that order
     * @param quantity   - represents the quantity of the product they would like to
     *                   add
     * @throws SQLException
     */
    public void addOrderItem(int newOrderId, int productID, int quantity) throws SQLException {
        isProductIDValid(productID);
        if (totalInventory(productID) < quantity) {
            throw new IllegalArgumentException("product does not have enough stock");
        }
        String addItem = "{call addOrderItem(?, ?, ?)}";
        CallableStatement stmt = conn.prepareCall(addItem);
        stmt.setInt(1, newOrderId);
        stmt.setInt(2, productID);
        stmt.setInt(3, quantity);
        stmt.execute();
    }

    /**
     * this function returns the total inventory of a product with a particular ID
     * 
     * @param productID - represents the product whose inventory we are checking
     * @return - returns the total inventory
     * @throws SQLException
     */
    public int totalInventory(int productID) throws SQLException {
        isProductIDValid(productID);
        String inventoryProc = "{? = call totalInventory(?)}";
        CallableStatement stmt = conn.prepareCall(inventoryProc);
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.setInt(2, productID);
        stmt.execute();
        int total = stmt.getInt(1);
        return (total);
    }

    /**
     * this function will retrieve a list of all the customers whose comments have
     * been flagged at least once
     * 
     * @return - returns a list of customer names
     * @throws SQLException
     */
    public List<String> flaggedCustomers() throws SQLException {
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
     * 
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
     * This method creates and inserts the review into the DB
     * 
     * @param productID   - represents the productID that holds the review
     * @param customerID  - represents the customer that is making the review
     * @param star        -represents the star rating.
     * @param description - represents the description of the rating.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void createReview(Review review) throws SQLException {
        String createReview = "{call createReview(?)}";
        CallableStatement stmt = conn.prepareCall(createReview);
        stmt.setObject(1, review);
        stmt.execute();
    }

    /**
     * This method flags a review in the databse
     * 
     * @param reviewID
     * @throws SQLException
     */
    public void flagReview(int reviewID) throws SQLException {
        isReviewIDValid(reviewID);
        String flagReview = "{call flagReview(?)}";
        CallableStatement stmt = conn.prepareCall(flagReview);
        stmt.setInt(1, reviewID);
        stmt.execute();
    }

    /**
     * This function gets the average review score for a product
     * 
     * @param productID - the victim.
     * @return - returns back the average review score.
     * @throws SQLException
     */
    public double calculateAvgReviewScore(int productID) throws SQLException {
        isProductIDValid(productID);
        String gettingAvg = "{? = call calculateAvgReviewScore(?)}";
        CallableStatement stmt = conn.prepareCall(gettingAvg);
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.setInt(2, productID);
        stmt.execute();
        double avgScore = stmt.getDouble(1);
        return avgScore;
    }

    /**
     * This function
     * 
     * @param productID
     * @return
     * @throws SQLException
     */
    public int numOrders(int productID) throws SQLException {
        isProductIDValid(productID);
        String sql = "{? = call numOrders(?)}";
        CallableStatement stmt = conn.prepareCall(sql);
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.setInt(2, productID);
        stmt.execute();
        int num = stmt.getInt(1);
        return num;
    }

    /**
     * This function
     * 
     * @param warehouseID
     * @throws SQLException
     */
    public void removeWarehouse(int warehouseID) throws SQLException {
        isWarehouseIDValid(warehouseID);
        String sql = "{call removeWarehouse(?)}";
        CallableStatement stmt = conn.prepareCall(sql);
        stmt.setInt(1, warehouseID);
        stmt.execute();
    }

    public List<Product> getListofProducts() throws SQLException {
        List<Product> productList = new ArrayList<Product>();
        CallableStatement stmt = conn.prepareCall("{? = call getproducts}");
        stmt.registerOutParameter(1, Types.ARRAY, "PRODUCTS_COLLECTION");
        stmt.execute();

        Array array = stmt.getArray(1);
        Object[] structArray = (Object[]) array.getArray();

        // Convert the array of STRUCT objects to a List of Product objects
        for (Object structObject : structArray) {
            Struct struct = (Struct) structObject;

            Object[] attributes = struct.getAttributes();
            int productID = ((BigDecimal) attributes[0]).intValue();
            String name = (String) attributes[1];
            String category = (String) attributes[2];

            Product product = new Product(productID, name, category);
            productList.add(product);
        }
        return productList;
    }

    public void addCustomer(Review review) throws SQLException {
        String createReview = "{call createReview(?)}";
        CallableStatement stmt = conn.prepareCall(createReview);
        stmt.setObject(1, review);
        stmt.execute();
    }

    /*******************/

}

// Array arr = stmt.getArray(1);
// ResultSet rs = arr.getResultSet();
// while (rs.next()) {
// int productID = rs.getInt(1);
// String name = rs.getString(2);
// String category = rs.getString(3);
// Product prod = new Product(productID, name, category);
// productList.add(prod);