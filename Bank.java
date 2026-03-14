import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class Bank {

    // Database Connection Method
    static Connection getConnection() {
        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/orcl",
                    "scott",
                    "sql"
            );

            return con;

        } catch (Exception e) {
            System.out.println("Connection Error: " + e);
            return null;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        welcome: for (;;) {

            System.out.println("\n********* WELCOME *********");
            System.out.println("      LAXMI CHIT FUND\n");
            System.out.println("1. CREATE ACCOUNT");
            System.out.println("2. LOGIN");
            System.out.print("Enter your response : ");

            int opt = sc.nextInt();

            switch (opt) {

                // CREATE ACCOUNT
                case 1: {

                    try {

                        Connection con = getConnection();

                        System.out.println("\n**** ACCOUNT CREATION PAGE ****\n");

                        System.out.print("Name : ");
                        sc.nextLine();
                        String name = sc.nextLine();

                        System.out.print("Contact : ");
                        long contact = sc.nextLong();

                        System.out.print("Pin : ");
                        int pin = sc.nextInt();

                        System.out.print("Amount : ");
                        double balance = sc.nextDouble();

                        String query = "INSERT INTO bank_account VALUES(?,?,?,?)";

                        PreparedStatement ps = con.prepareStatement(query);

                        ps.setString(1, name);
                        ps.setLong(2, contact);
                        ps.setInt(3, pin);
                        ps.setDouble(4, balance);

                        ps.executeUpdate();

                        String tquery = "INSERT INTO transactions(contact,type,amount) VALUES(?,?,?)";

                        PreparedStatement ps2 = con.prepareStatement(tquery);

                        ps2.setLong(1, contact);
                        ps2.setString(2, "Deposit");
                        ps2.setDouble(3, balance);

                        ps2.executeUpdate();

                        System.out.println("\nACCOUNT CREATED SUCCESSFULLY\n");

                    } catch (Exception e) {
                        System.out.println(e);
                    }

                    break;
                }

                // LOGIN
                case 2: {

                    try {

                        Connection con = getConnection();

                        System.out.println("\n**** LOGIN MODULE ****\n");

                        System.out.print("Contact : ");
                        long userContact = sc.nextLong();

                        System.out.print("Pin : ");
                        int userPin = sc.nextInt();

                        String query = "SELECT * FROM bank_account WHERE contact=? AND pin=?";

                        PreparedStatement ps = con.prepareStatement(query);

                        ps.setLong(1, userContact);
                        ps.setInt(2, userPin);

                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {

                            double balance = rs.getDouble("balance");

                            System.out.println("\nLOGIN SUCCESSFUL\n");

                            loginLoop: for (;;) {

                                System.out.println("\n**** FEATURES ****\n");
                                System.out.println("1. Deposit");
                                System.out.println("2. Withdraw");
                                System.out.println("3. Check Balance");
                                System.out.println("4. Transactions");
                                System.out.println("5. Logout");

                                System.out.print("Enter your option : ");

                                int opt1 = sc.nextInt();

                                switch (opt1) {

                                    // DEPOSIT
                                    case 1: {

                                        System.out.print("Enter amount : ");
                                        double depositAmt = sc.nextDouble();

                                        balance += depositAmt;

                                        String update = "UPDATE bank_account SET balance=? WHERE contact=?";

                                        PreparedStatement ps1 = con.prepareStatement(update);

                                        ps1.setDouble(1, balance);
                                        ps1.setLong(2, userContact);

                                        ps1.executeUpdate();

                                        String tquery = "INSERT INTO transactions(contact,type,amount) VALUES(?,?,?)";

                                        PreparedStatement ps2 = con.prepareStatement(tquery);

                                        ps2.setLong(1, userContact);
                                        ps2.setString(2, "Deposit");
                                        ps2.setDouble(3, depositAmt);

                                        ps2.executeUpdate();

                                        System.out.println("AMOUNT DEPOSITED SUCCESSFULLY");

                                        break;
                                    }

                                    // WITHDRAW
                                    case 2: {

                                        System.out.print("Enter amount : ");
                                        double wtdAmt = sc.nextDouble();

                                        if (wtdAmt <= balance) {

                                            balance -= wtdAmt;

                                            String update = "UPDATE bank_account SET balance=? WHERE contact=?";

                                            PreparedStatement ps1 = con.prepareStatement(update);

                                            ps1.setDouble(1, balance);
                                            ps1.setLong(2, userContact);

                                            ps1.executeUpdate();

                                            String tquery = "INSERT INTO transactions(contact,type,amount) VALUES(?,?,?)";

                                            PreparedStatement ps2 = con.prepareStatement(tquery);

                                            ps2.setLong(1, userContact);
                                            ps2.setString(2, "Withdraw");
                                            ps2.setDouble(3, wtdAmt);

                                            ps2.executeUpdate();

                                            System.out.println("AMOUNT WITHDRAWN SUCCESSFULLY");

                                        } else {

                                            System.out.println("INSUFFICIENT BALANCE");

                                        }

                                        break;
                                    }

                                    // CHECK BALANCE
                                    case 3: {

                                        System.out.println("\nYour balance is : " + balance + " Rs\n");

                                        break;
                                    }

                                    // TRANSACTIONS
                                    case 4: {

                                        String tq = "SELECT * FROM transactions WHERE contact=?";

                                        PreparedStatement ps3 = con.prepareStatement(tq);

                                        ps3.setLong(1, userContact);

                                        ResultSet rs1 = ps3.executeQuery();

                                        System.out.println("\nTransaction History\n");

                                        while (rs1.next()) {

                                            System.out.println(
                                                    rs1.getString("type") + " : "
                                                            + rs1.getDouble("amount") + " : "
                                                            + rs1.getDate("tdate"));
                                        }

                                        break;
                                    }

                                    // LOGOUT
                                    case 5: {

                                        System.out.println("\nTHANK YOU VISIT AGAIN\n");

                                        continue welcome;
                                    }

                                    default:
                                        System.out.println("INVALID OPTION");
                                }
                            }

                        } else {

                            System.out.println("\nINVALID LOGIN\n");

                        }

                    } catch (Exception e) {

                        System.out.println(e);

                    }

                    break;
                }

                default:
                    System.out.println("INVALID RESPONSE");
            }
        }
    }
}