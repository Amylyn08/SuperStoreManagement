package jdbcsuperstore;

import java.sql.*;
import java.util.*;

public class Review implements SQLData {
    private int productID;
    private int customerID;
    private int star;
    private int flagnums;
    private String description;
    private String sql_type = "REVIEW_TYPE";

    public Review(int productID, int customerID, int star, String description, Connection conn)
            throws SQLException, ClassNotFoundException {
        this.productID = productID;
        this.customerID = customerID;
        this.star = star;
        this.flagnums = 0;
        this.description = description;

        Map map = conn.getTypeMap();
        conn.setTypeMap(map);
        map.put(this.sql_type, Class.forName("jdbcsuperstore.Review"));
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeInt(this.productID);
        stream.writeInt(this.customerID);
        stream.writeInt(this.star);
        stream.writeInt(this.flagnums);
        stream.writeString(this.description);
    }

    @Override
    public void readSQL(SQLInput stream, String type) throws SQLException {
        this.productID = stream.readInt();
        this.customerID = stream.readInt();
        this.star = stream.readInt();
        this.flagnums = stream.readInt();
        this.description = stream.readString();
    }

    @Override
    public String getSQLTypeName() {
        return this.sql_type;
    }
}