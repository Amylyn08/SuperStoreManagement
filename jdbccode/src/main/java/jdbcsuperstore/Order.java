package jdbcsuperstore;
import java.sql.*;
import java.util.*;

public class Order implements SQLData {
    private int customerID;
    private int storeID;
    private String sql_type = "ORDER_TYPE";

    public Order(int customerID, int storeID, Connection conn) throws SQLException, ClassNotFoundException
    {
        this.customerID = customerID;
        this.storeID = storeID;
        Map map = conn.getTypeMap(); 
        conn.setTypeMap(map); 
        map.put(this.sql_type, Class.forName("jdbcsuperstore.Order"));
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException
    {
        stream.writeInt(this.customerID);
        stream.writeInt(this.storeID);
    }

    @Override
    public void readSQL(SQLInput stream, String type) throws SQLException
    {
        this.customerID = stream.readInt();
        this.storeID = stream.readInt();
    }

    @Override
    public String getSQLTypeName()
    {
        return sql_type; // this corresponds to the type name in SQL!!! (DB side)
    }

}
