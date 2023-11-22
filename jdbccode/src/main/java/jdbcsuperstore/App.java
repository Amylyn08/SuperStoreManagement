package jdbcsuperstore;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.ResultSet;
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
            // viewCustomerInfo();
            addCustomer();
            conn.close();

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

    public static void viewCustomerInfo() {
        List<Customer> customers = null;
        try {
            customers = services.viewCustomers();
            for (Customer c : customers) {
                System.out.println(c);
            }
        } catch (SQLException e) {
            e.getMessage();
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

    public static void viewNumOrders() {
        boolean isSuccessful = false;
        while (!isSuccessful) {
            try {
                System.out.println("Enter productid to view it's total orders:");
                int productID = Integer.parseInt(scan.nextLine());
                int total = services.numOrders(productID);
                System.out.println("For productID of " + productID + ", there are " + total + " orders placed");
                isSuccessful = true;
            } catch (SQLException e) {
                e.getMessage();
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer please");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void getAvgReviewScoreforProduct() {
        boolean isSuccessful = false;
        while (!isSuccessful) {
            try {
                System.out.println("Enter a productID to view it's average review score:");
                int productID = Integer.parseInt(scan.nextLine());
                double result = services.calculateAvgReviewScore(productID);
                System.out.println("The average score for this product is: " + result + " stars");
                isSuccessful = true;
            } catch (SQLException e) {
                e.getMessage();
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer please");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void createReview() throws ClassNotFoundException {
        boolean isSuccessful = false;
        while (!isSuccessful) {
            try {
                System.out
                        .println("Enter the params in the respective order: productID, customerID, star, description");
                int productID = Integer.parseInt(scan.nextLine());
                int customerID = Integer.parseInt(scan.nextLine());
                int star = Integer.parseInt(scan.nextLine());
                String description = scan.nextLine();

                Review newReview = new Review(productID, customerID, star, description, conn);

                services.createReview(newReview);
                System.out.println("New order review created!");
                isSuccessful = true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer please");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void flagReview() {
        boolean isSuccessful = false;
        while (!isSuccessful) {
            try {
                System.out.println("Please enter the review ID that you would like to flag.");
                int reviewID = Integer.parseInt(scan.nextLine());
                services.flagReview(reviewID);
                System.out.println("Review was successfully flagged. Thank you");
                isSuccessful = true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer please");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void viewProductsByCategory() {
        PreparedStatement stmt = null;
        boolean loadedProperList = false;
        while (!loadedProperList) {
            try {
                System.out.println(
                        "Enter the category in which products you would like to view (CASE SENSITIVE):");
                String category = scan.nextLine();
                String sqlQuery = "SELECT * FROM Products WHERE category = ?";

                stmt = conn.prepareStatement(sqlQuery);
                stmt.setString(1, category);
                ResultSet rs = stmt.executeQuery();
                List<Product> listOfProducts = new ArrayList<Product>();
                while (rs.next()) {
                    listOfProducts
                            .add(new Product(rs.getInt("productID"), rs.getString("name"), rs.getString("category")));
                }
                for (Product p : listOfProducts) {
                    System.out.println(p);
                }
                if (listOfProducts.size() > 0) {
                    loadedProperList = true;
                } else {
                    System.out.println("There was no products with this specific category. Try again!:");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void removeWarehouse() {
        boolean isSuccessful = false;
        while (!isSuccessful) {
            try {
                System.out.println("Enter the warehouseID you would like to delete:");
                int warehouseID = Integer.parseInt(scan.nextLine());
                services.removeWarehouse(warehouseID);
                System.out.println("Warehouse deleted successfully.");
                isSuccessful = true;
            } catch (SQLException e) {
                e.getMessage();
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer please");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public static void showProducts() throws SQLException {
        List<Product> listProducts = services.getListofProducts();
        for (Product prod : listProducts) {
            System.out.println(prod);
        }
    }

    public static void addCustomer() throws SQLException {
        boolean isSuccessful = false;
        while (!isSuccessful) {
            try {
                System.out.println("Please enter the following values for the new customer: ");
                System.out.println("firstname");
                String firstname = scan.nextLine();
                System.out.println("lastname:");
                String lastname = scan.nextLine();
                System.out.println("email");
                String email = scan.nextLine();
                System.out.println("streetAddress:");
                String address = scan.nextLine();
                System.out.println("city:");
                String city = scan.nextLine();
                System.out.println("province:");
                String province = scan.nextLine();
                System.out.println("country");
                String country = scan.nextLine();

                Customer newCus = new Customer(firstname, lastname, email, address, city, province, country);
                services.addCustomer(newCus);
                System.out.println("Customer added succesffuly!");
                isSuccessful = true;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
