package jdbcsuperstore;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static final Scanner scan = new Scanner(System.in);
    public static Connection conn = null;
    public static SuperstoreServices services = null;
    public static void main( String[] args ) throws SQLException
    {
        String user = scan.nextLine();
        String password = scan.nextLine();
        try {
            services = new SuperstoreServices("jdbc:oracle:thin:", "198.168.52.211", "1521", user, password);
            conn = services.getConnection();
            mainMenu();
            conn.close(); // keeps running even with exception?????
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            services.Close();
        }
    }

    /**
     * this function represents the main menu with MAIN FUNCTIONS
     */
    public static void mainMenu()
    {
        System.out.println("\033c");
        int input = 0;
        do {
        System.out.println("1. View information about customers");
        System.out.println("2. View information about reviews and products");
        System.out.println("3. View information about orders");
        System.out.println("4. Add or modify");
        System.out.println("5. EXIT");
        System.out.println("Enter the number of the option you would like to select: ");
        input = Integer.parseInt(scan.nextLine());
        if (input == 1)
            viewCustomerMenu();
        else if (input == 2)
            viewReviewProdMenu();
        else if (input == 3)
            viewOrderMenu();
        else if (input == 4)
            manipulationMenu();
        else if (input == 5)
            return;
        } while (input < 1 || input > 4);

    }

    public static void viewCustomerMenu()
    {
        System.out.println("\033c");
        int input = 0;
        do {
        System.out.println("1. View all customer information");
        System.out.println("2. View names of flagged customers");
        System.out.println("3. BACK");
        System.out.println("Enter the number of the option you would like to select: ");
        input = Integer.parseInt(scan.nextLine());
        if (input == 1)
            viewCustomerInfo();
        else if (input == 2)
            viewFlaggedCustomers();
        else if (input == 3)
            mainMenu();
        } while (input < 1 || input > 3);
    }

    public static void viewReviewProdMenu()
    {
        System.out.println("\033c");
        int input = 0;
        do {
        System.out.println("1. View all reviews");
        System.out.println("2. Find average review score of a product");
        System.out.println("3. BACK");
        System.out.println("Enter the number of the option you would like to select: ");
        input = Integer.parseInt(scan.nextLine());
        if (input == 1)
            viewReviewInfo();
        else if (input == 2)
            return; // calculateAvgReviewScore
        else if (input == 3)
            mainMenu();
        } while (input < 1 || input > 3);
    }

    public static void viewOrderMenu()
    {
        System.out.println("\033c");
        int input = 0;
        do {
        System.out.println("1. View total inventory of a specific product");
        System.out.println("2. Calculate the number of orders placed on a product");
        System.out.println("3. BACK");
        System.out.println("Enter the number of the option you would like to select: ");
        input = Integer.parseInt(scan.nextLine());
        if (input == 1)
            viewReviewInfo();
        else if (input == 2)
            return; // calculateAvgReviewScore
        else if (input == 3)
            mainMenu();
        } while (input < 1 || input > 3);
    }

    public static void manipulationMenu()
    {
        System.out.println("\033c");
        int input = 0;
        do {
        System.out.println("1. Place an order an order");
        System.out.println("2. Remove a product");
        System.out.println("3. Log a new delivery into the system");
        System.out.println("4. Flag a review");
        System.out.println("5. Create a review");
        System.out.println("6. Remove  warehouse");
        System.out.println("7. EXIT");
        System.out.println("Enter the number of the option you would like to select: ");
        input = Integer.parseInt(scan.nextLine());
        if (input == 1)
            placeOrder();
        else if (input == 2)
            removeProduct();
        else if (input == 3)
            logDelivery();
        else if (input == 4)
            return; // flag review
        else if (input == 5)
            return; // create review
        else if (input == 6)
            return; // remove warehouse
        else if (input == 7)
            mainMenu();
        } while (input < 1 || input > 7);
    }
    
    /**
     * this function creates an order in the Orders table using user input
     */
    public static void placeOrder()
    {
        boolean isSuccessful = false;
        Order newOrder = null;
        int orderID = 0;
        while (!isSuccessful)
        {
            try {
                System.out.println("Please enter the customerID associated with the order:");
                int customerID = Integer.parseInt(scan.nextLine());
                services.isCustomerIDValid(customerID);
                System.out.println("Please enter the storeID associated with the order:");
                int storeID = Integer.parseInt(scan.nextLine());
                services.isStoreIDValid(storeID);
                newOrder = new Order(customerID, storeID, conn);
                isSuccessful = true;
                orderID = services.createOrder(newOrder);
                addOrderItems(orderID);
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
            catch (ClassNotFoundException e)
            {
                System.out.println(e.getMessage());
            }
            catch(NumberFormatException e)
            {
                System.out.println("enter an integer please");
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * this function takes the orderID of the order recently added and adds the items 
     * the user selects into the Orders_Products table
     * @param orderID - represents the ID of the order created
     * @throws SQLException - may throw an SQL exception
     */
    public static void addOrderItems(int orderID)
    {
        String addMore = "yes";
        while (addMore.equals("yes") || addMore.equals("y"))
        {
            try {
                System.out.println("Please enter the productID of the product you would like to add:");
                int productID = Integer.parseInt(scan.nextLine());
                System.out.println("Please enter the quantity of the product:");
                int quantity = Integer.parseInt(scan.nextLine());
                services.addOrderItem(orderID, productID, quantity);
                System.out.println("your order item has been added successfully");
                System.out.println("add another item?");
                addMore = scan.nextLine().toLowerCase();
            }
            catch(NumberFormatException e)
            {
                System.out.println("enter an integer please");
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * this function takes the orderID of the order recently added and adds the items 
     * the user selects into the Orders_Products table
     * @param orderID - represents the ID of the order created
     * @throws SQLException - may throw an SQL exception
     */
    public static void logDelivery()
    {
        boolean isSuccessful = false;
        while (!isSuccessful)
        {
            try {
                System.out.println("Please enter the productID of the product in the delivery:");
                int productID = Integer.parseInt(scan.nextLine());
                System.out.println("Please enter the warehouseID of the warehouse where the delivery will be sent:");
                int warehouseID = Integer.parseInt(scan.nextLine());
                System.out.println("Please enter the quantity of the product:");
                int quantity = Integer.parseInt(scan.nextLine());
                services.newDeliveryIncome(productID, warehouseID, quantity);
                System.out.println("your delivery has been added successfully!");
                isSuccessful = true;
            }
            catch(NumberFormatException e)
            {
                System.out.println("enter an integer please");
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void checkTotalProdInventory()
    {
        boolean isSuccessful = false;
        while (!isSuccessful)
        {
            try {
                System.out.println("Please enter the productID of the product whose inventory you would like to view:");
                int productID = Integer.parseInt(scan.nextLine());
                int inventory = services.totalInventory(productID);
                System.out.println("total inventory of product with id " + productID + " is " + inventory);
                isSuccessful = true;
            }
            catch (SQLException e)
            {
                e.getMessage();
            }
            catch(NumberFormatException e)
            {
                System.out.println("enter an integer please");
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void viewCustomerInfo()
    {
        List<Customer> customers = null;
        try 
        {
            customers = services.viewCustomers();
            for (Customer c : customers)
            {
                System.out.println(c);
            }
        }
        catch (SQLException e)
        {
            e.getMessage();
        }
    }

    public static void viewReviewInfo()
    {
        List<Review> reviews = null;
        try 
        {
            reviews = services.viewReviews();
            for (Review c : reviews)
            {
                System.out.println(c);
            }
        }
        catch (SQLException e)
        {
            e.getMessage();
        }
    }

    public static void viewFlaggedCustomers()
    {
        List<String> flaggedCus = null;
        try 
        {
            flaggedCus = services.flaggedCustomers();
        }
        catch (SQLException e)
        {
            e.getMessage();
        }

        System.out.println("the flagged customers are: ");
        for (String cus : flaggedCus)
        {
            System.out.println(cus);
        }
    }

    public static void removeProduct()
    {
        boolean isSuccessful = false;
        while (!isSuccessful)
        {
            try {
                System.out.println("Please enter the productID of the product you would like to remove:");
                int productID = Integer.parseInt(scan.nextLine());
                services.removeProduct(productID);
                System.out.println("product has been successfully removed!");
                isSuccessful = true;
            }
            catch (SQLException e)
            {
                e.getMessage();
            }
            catch(NumberFormatException e)
            {
                System.out.println("enter an integer please");
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
