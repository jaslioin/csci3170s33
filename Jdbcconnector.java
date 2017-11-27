//CREATE FILE AND TEST IN LINUX1

/*
	//compile
  	javac Jdbcconnector.java
  //run
		java -cp .:jdbc.jar Jdbcconnector
*/
//test github testasfafasd
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;
import java.sql.SQLException;

public class Jdbcconnector {

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            //reg server
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //get connection
            conn = DriverManager.getConnection("jdbc:mysql://appsrvdb.cse.cuhk.edu.hk/CSCI3170S39", "CSCI3170S39", "GPA4.0^_^");
            /*
                Statement stmt  = conn.createStatement();
                stmt.execute("DROP TABLE IF EXISTS category");
                stmt.close();
             */

            while (true) {
                System.out.println("********MENU********");
                System.out.println("1.Operation for administrator");
                System.out.println("2.Operation for library user");
                System.out.println("3.Operation for liberian");
                System.out.println("4.Exit");
                System.out.print("Your command(1~4): ");
                Scanner in = new Scanner(System.in);
                int menuCmd = in.nextInt();
                switch (menuCmd) {
                    case 1:
                        while (true) {
                            System.out.println("********ADMIN********");
                            System.out.println("1.Create all tables");
                            System.out.println("2.Delete all tables");
                            System.out.println("3.Load data");
                            System.out.println("4.Show number of records in each table");
                            System.out.println("5.Return to MENU");
                            System.out.print("Your command(1~5): ");
                            Scanner adminIn = new Scanner(System.in);
                            int adminCmd = adminIn.nextInt();
                            int returnIndicator=0;
                            switch (menuCmd) {
                                case 1:
                                    //operation
                                    System.out.println("......Doint job......");

                                    stmt = conn.createStatement();

                                    //create book
                                    String createStatement = "CREATE TABLE book "
                                            + "(call_number VARCHAR(8), "
                                            + " title INT(1), "
                                            + " publish_date DATE, "
                                            + " PRIMARY KEY ( call_number ))";
                                    stmt.execute(createStatement);
                                    //create category
                                    createStatement = "CREATE TABLE";
                                    stmt.execute(createStatement);
                                    //create user
                                    createStatement = "CREATE TABLE";
                                    stmt.execute(createStatement);
                                    //create copy
                                    createStatement = "CREATE TABLE";
                                    stmt.execute(createStatement);
                                    //create checkout_record
                                    createStatement = "CREATE TABLE";
                                    stmt.execute(createStatement);
                                    //create author
                                    createStatement = "CREATE TABLE";
                                    stmt.execute(createStatement);
                                    //done
                                    stmt.close();
                                    System.out.println("......Done......");
                                    break;
                                case 2:
                                    System.out.println("Not done yet");
                                    break;
                                case 3:
                                    System.out.println("Not done yet");
                                    break;
                                case 4:
                                    System.out.println("Not done yet");
                                    break;
                                case 5:
                                    returnIndicator=1;
                                    System.out.println("Not done yet");
                                    break;
                                default:
                                    break;
                            }
                            if(returnIndicator==1)
                                break;

                        }
                    case 2:
                        System.out.println("Not done yet");
                        break;
                    case 3:
                        System.out.println("Not done yet");
                        break;
                    case 4:
                        System.out.println("Not done yet");
                        break;
                    default:
                        System.out.println("Not done yet");
                        break;
                }

            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
