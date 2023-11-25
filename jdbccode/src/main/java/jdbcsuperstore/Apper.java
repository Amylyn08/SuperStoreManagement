package jdbcsuperstore;

import java.io.Console;
import java.util.Arrays;

public class Apper {
    public static void main(String[] args) {
        Console console = System.console() ;

		char [] password = console.readPassword("Enter password: ");
        String pass = "";
        for (char letter : password)
        {
            pass+=letter;
        }
        System.out.println(pass);
    }
}
