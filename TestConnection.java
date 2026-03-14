import java.sql.Connection;
import java.sql.DriverManager;

class TestConnection {

    public static void main(String[] args) {

        try {

            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/orcl",
                    "scott",
                    "sql"  //password oracle
            );

            System.out.println("Database Connected Successfully");

        } catch (Exception e) {
            System.out.println(e);
            
        }

    }
}