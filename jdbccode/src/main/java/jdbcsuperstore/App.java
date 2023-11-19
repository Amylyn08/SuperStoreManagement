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
            placeOrder();
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

    public static void placeOrder() throws SQLException, ClassNotFoundException
    {
        boolean isSuccessful = false;
        Order newOrder = null;
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

        int orderID = services.createOrder(newOrder);
        addOrderItems(orderID);
    }

    public static void addOrderItems(int orderID) throws SQLException
    {
        String addMore = "yes";
        while (addMore.equals("yes") || addMore.equals("y"))
        {
            try {
                System.out.println("Please enter the customerID associated with the order:");
                int productID = Integer.parseInt(scan.nextLine());
                services.isProductIDValid(productID);
                System.out.println("Please enter the storeID associated with the order:");
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
                if (e.getErrorCode() == 20001)
                {
                    System.out.println("Product could not be added because there is not enough stock in the warehouses.");
                }
                else 
                {
                    e.getMessage();
                }
            }
        }
    }
}
