package jdbcsuperstore;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App {
    public static final Scanner scan = new Scanner(System.in);
    public static Connection conn = null;
    public static SuperstoreServices services = null;

    public static void main(String[] args) throws SQLException {
        String user = scan.nextLine();
        String password = scan.nextLine();
        try {
            services = new SuperstoreServices("jdbc:oracle:thin:", "198.168.52.211", "1521", user, password);
            conn = services.getConnection();
            // placeOrder();
            // System.out.println(services.totalInventory(20));
            checkTotalProdInventory();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            services.Close();
        }
    }

    /**
     * this function creates an order in the Orders table using user input
     */
    public static void placeOrder() throws SQLException, ClassNotFoundException {
        boolean isSuccessful = false;
        Order newOrder = null;
        while (!isSuccessful) {
            try {
                System.out.println("Please enter the customerID associated with the order:");
                int customerID = Integer.parseInt(scan.nextLine());
                services.isCustomerIDValid(customerID);
                System.out.println("Please enter the storeID associated with the order:");
                int storeID = Integer.parseInt(scan.nextLine());
                services.isStoreIDValid(storeID);
                newOrder = new Order(customerID, storeID, conn);
                isSuccessful = true;
            } catch (NumberFormatException e) {
                System.out.println("enter an integer please");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        int orderID = services.createOrder(newOrder);
        addOrderItems(orderID);
    }

    /**
     * this function takes the orderID of the order recently added and adds the
     * items
     * the user selects into the Orders_Products table
     * 
     * @param orderID - represents the ID of the order created
     * @throws SQLException - may throw an SQL exception
     */
    public static void addOrderItems(int orderID) {
        String addMore = "yes";
        while (addMore.equals("yes") || addMore.equals("y")) {
            try {
                System.out.println("Please enter the productID of the product you would like to add:");
                int productID = Integer.parseInt(scan.nextLine());
                System.out.println("Please enter the quantity of the product:");
                int quantity = Integer.parseInt(scan.nextLine());
                services.addOrderItem(orderID, productID, quantity);
                System.out.println("your order item has been added successfully");
                System.out.println("add another item?");
                addMore = scan.nextLine().toLowerCase();
            } catch (NumberFormatException e) {
                System.out.println("enter an integer please");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (SQLException e) {
                if (e.getErrorCode() == 20001) {
                    System.out
                            .println("Product could not be added because there is not enough stock in the warehouses.");
                } else {
                    e.getMessage();
                }
            }
        }
    }

    public static void checkTotalProdInventory() {
        boolean isSuccessful = false;
        while (!isSuccessful) {
            try {
                System.out.println("Please enter the productID of the product whose inventory you would like to view:");
                int productID = Integer.parseInt(scan.nextLine());
                int inventory = services.totalInventory(productID);
                System.out.println("total inventory of product with id " + productID + " is " + inventory);
                isSuccessful = true;
            } catch (SQLException e) {
                e.getMessage();
            } catch (NumberFormatException e) {
                System.out.println("enter an integer please");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void viewFlaggedCustomers() {
        List<String> flaggedCus = null;
        try {
            flaggedCus = services.flaggedCustomers();
        } catch (SQLException e) {
            e.getMessage();
        }

        System.out.println("the flagged customers are: ");
        for (String cus : flaggedCus) {
            System.out.println(cus);
        }
    }

    public static void removeProduct() {
        boolean isSuccessful = false;
        while (!isSuccessful) {
            try {
                System.out.println("Please enter the productID of the product you would like to remove:");
                int productID = Integer.parseInt(scan.nextLine());
                services.removeProduct(productID);
                System.out.println("product has been successfully removed!");
                isSuccessful = true;
            } catch (SQLException e) {
                e.getMessage();
            } catch (NumberFormatException e) {
                System.out.println("enter an integer please");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
