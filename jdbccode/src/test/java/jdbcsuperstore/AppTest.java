package jdbcsuperstore;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.util.*;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test

    public void testValidIDProduct() {
        SuperstoreServices services = new SuperstoreServices("jdbc:oracle:thin:", "198.168.52.211", "1521", "A2232160",
                "Iliketurtles0132");
        services.isProductIDValid(2);

    }

}
