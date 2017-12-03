//CREATE FILE AND TEST IN LINUX1
/*
//files needed in same folder
•jdbc.jar
//compile
javac  Jdbcconnector.java
//run
java -cp .:jdbc.jar Jdbcconnector
*/
import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Jdbcconnector {
    public static void main(String[] args) {
      Connection conn = null;
      Statement stmt = null;
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      try {
          //reg server
          Class.forName("com.mysql.jdbc.Driver").newInstance();
          //get connection
          conn = DriverManager.getConnection("jdbc:mysql://appsrvdb.cse.cuhk.edu.hk/CSCI3170S39", "CSCI3170S39", "GPA4.0^_^");
          //basic statement loop
          /*
           Statement stmt  = conn.createStatement();
           stmt.execute("DROP TABLE IF EXISTS category");
           stmt.close();
           */
          while (true) {
              System.out.println("********MENU********");
              System.out.println("1.Operation for administrator");
              System.out.println("2.Operation for library user");
              System.out.println("3.Operation for librarian");
              System.out.println("4.Exit");
              System.out.print("Your command(1~4): ");
              Scanner in = new Scanner(System.in);
              int menuCmd = in.nextInt();
              switch (menuCmd) {
                  case 1:
                      while (true) {
                          System.out.println("********ADMINISTRATOR********");
                          System.out.println("1.Create all tables");
                          System.out.println("2.Delete all tables");
                          System.out.println("3.Load data");
                          System.out.println("4.Show number of records in each table");
                          System.out.println("5.Return to MENU");
                          System.out.print("Your command(1~5): ");
                          Scanner adminIn = new Scanner(System.in);
                          int adminCmd = adminIn.nextInt();
                          int returnIndicator = 0;
                          String createStatement;
                          stmt = conn.createStatement();
                          switch (adminCmd) {
                              case 1:  // create all tables
                                  //operation
                                  System.out.println("......CREATE TABLE......");
                                  //create book
                                  System.out.println("......creating book......");
                                  createStatement = "CREATE TABLE book "
                                          + "(call_number VARCHAR(8), "
                                          + " title VARCHAR(30) NOT NULL, "
                                          + " publish_date DATE, "
                                          + " PRIMARY KEY ( call_number ))";
                                  stmt.execute(createStatement);
                                  //create category
                                  System.out.println("......creating category......");
                                  createStatement = "CREATE TABLE category"
                                          + "(id INT(1),"
                                          + "loan_period INT(2),"
                                          + "max_books INT(2),"
                                          + "PRIMARY KEY (id))";
                                  stmt.execute(createStatement);
                                  //create user
                                  System.out.println("......creating user......");
                                  createStatement = "CREATE TABLE user"
                                          + "(id VARCHAR(10),"
                                          + "name VARCHAR(25),"
                                          + "address VARCHAR(100),"
                                          + "category_id INT(1) NOT NULL,"
                                          + "PRIMARY KEY(id),"
                                          + "FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE)";
                                  stmt.execute(createStatement);
                                  //create copy
                                  System.out.println("......creating copy......");
                                  createStatement = "CREATE TABLE copy"
                                          + "(call_number VARCHAR(8),"
                                          + "copy_number INT(1),"
                                          + "PRIMARY KEY(call_number,copy_number),"
                                          + "FOREIGN KEY (call_number) REFERENCES book ON DELETE CASCADE)";
                                  stmt.execute(createStatement);
                                  //create checkout_record
                                  System.out.println("......creating checkout_record......");
                                  createStatement = "CREATE TABLE checkout_record"
                                          + "(user_id VARCHAR(10),"
                                          + "call_number VARCHAR(8),"
                                          + "copy_number INT(1),"
                                          + "checkout_date DATE,"
                                          + "return_date DATE,"
                                          + "PRIMARY KEY (user_id,call_number,copy_number,checkout_date),"
                                          + "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,"
                                          + "FOREIGN KEY (call_number) REFERENCES book ON DELETE CASCADE,"
                                          + "FOREIGN KEY (copy_number) REFERENCES copy)";
                                  stmt.execute(createStatement);
                                  //create author
                                  System.out.println("......creating author......");
                                  createStatement = "CREATE TABLE author"
                                          + "(name VARCHAR(100),"
                                          + "call_number VARCHAR(8),"
                                          + "PRIMARY KEY (name,call_number),"
                                          + "FOREIGN KEY (call_number) REFERENCES book ON DELETE CASCADE)";
                                  stmt.execute(createStatement);
                                  //done
                                  stmt.close();
                                  System.out.println("......Done......");
                                  break;
                              case 2:  // delete all tables
                                  System.out.println("......Start drop tables......");
                                  createStatement = "DROP TABLE IF EXISTS user";
                                  stmt.execute(createStatement);
                                  System.out.println("user dropped!");
                                  createStatement = "DROP TABLE IF EXISTS category";
                                  stmt.execute(createStatement);
                                  System.out.println("category dropped!");
                                  createStatement = "DROP TABLE IF EXISTS book";
                                  stmt.execute(createStatement);
                                  System.out.println("book dropped!");
                                  createStatement = "DROP TABLE IF EXISTS copy";
                                  stmt.execute(createStatement);
                                  System.out.println("copy dropped!");
                                  createStatement = "DROP TABLE IF EXISTS checkout_record";
                                  stmt.execute(createStatement);
                                  System.out.println("checkout_record dropped!");
                                  createStatement = "DROP TABLE IF EXISTS author";
                                  stmt.execute(createStatement);
                                  System.out.println("author dropped!");
                                  stmt.close();
                                  System.out.println("......Tables dropped!......");
                                  break;
                              case 3:  // insert data
                                  //get folder name                                   
                                  System.out.print("......Input path name :");
                                  Scanner fol = new Scanner(System.in);
                                  String folName = fol.nextLine();
                                  System.out.printf("path name : %s \n", folName);
                                  //assign data
                                  File actual = new File(folName);
                                  for (File f : actual.listFiles()) {//for every folder
                                      String fileName = f.getName();
                                      System.out.println(fileName);
                                      /* access file */
                                      BufferedReader dataReader = new BufferedReader(new FileReader(folName + "/" + fileName));
                                      String line;
                                      PreparedStatement pStatement;
                                      if (fileName.contains("book.txt")) {
                                          System.out.printf("loading %s\n", fileName);
                                          while ((line = dataReader.readLine()) != null) {//for each line
                                              String[] token = line.split(Pattern.quote("\t"));
                                              /**
                                               * *book**
                                               */
                                              pStatement = conn.prepareStatement("INSERT INTO book VALUES (?,?,?)");
                                              //call_number
                                              pStatement.setString(1, token[0]);
                                              //title
                                              pStatement.setString(2, token[2]);
                                              //date     
                                         java.util.Date dateStr = sdf.parse(token[4]);
                                            java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
                                         pStatement.setDate(3,dateDB);
                                              //pStatement.setString(3, token[4]);
                                              pStatement.execute();
                                              /**
                                               * *copy**
                                               */
                                              pStatement = conn.prepareStatement("INSERT INTO copy VALUES (?,?)");
                                              //call_number
                                              pStatement.setString(1, token[0]);
                                              //number of copy
                                              pStatement.setInt(2, Integer.parseInt(token[1]));
                                              pStatement.execute();
                                              /**
                                               * *author**
                                               */
                                              pStatement = conn.prepareStatement("INSERT INTO author VALUES (?,?)");
                                              //name
                                              pStatement.setString(1, token[3]);
                                              //call_number
                                              pStatement.setString(2, token[0]);
                                              pStatement.execute();
                                              /* print each token in line */
/*
                                                  for (int i = 0; i < token.length; i++) {                                                       
                                                      System.out.println(token[i]);
                                                  }                                                   
                                               */
                                          }
                                      }
                                      if (fileName.contains("category.txt")) {
                                          System.out.printf("loading %s\n", fileName);
                                          while ((line = dataReader.readLine()) != null) {//for each line
                                              String[] token = line.split(Pattern.quote("\t"));
                                              pStatement = conn.prepareStatement("INSERT INTO category VALUES (?,?,?)");
                                              //id
                                              pStatement.setInt(1, Integer.parseInt(token[0]));
                                            //period
                                              pStatement.setInt(2, Integer.parseInt(token[2]));
                                              //max book
                                              pStatement.setInt(3, Integer.parseInt(token[1]));
                                             
                                              pStatement.execute();
                                          }
                                      }
                                      if (fileName.contains("checkout.txt")) {
                                          System.out.printf("loading %s\n", fileName);
                                          while ((line = dataReader.readLine()) != null) {//for each line
                                              String[] token = line.split(Pattern.quote("\t"));
                                              pStatement = conn.prepareStatement("INSERT INTO checkout_record VALUES (?,?,?,?,?)");
                                              //userid
                                              pStatement.setString(1, token[2]);
                                              //call_number
                                              pStatement.setString(2, token[0]);
                                              //copy_number
                                              pStatement.setInt(3, Integer.parseInt(token[1]));
                                              //checkout date
                                              if (token[3].equals("null")) {//do nothing
                                              } else {
                                                  java.util.Date dateStr;
                                                  dateStr = sdf.parse(token[3]);
                                                  java.sql.Date dateDB1 = new java.sql.Date(dateStr.getTime());
                                                  pStatement.setDate(4, dateDB1);
                                              }                                               
                                              //return date
                                              java.util.Date dateStr2;
                                              java.sql.Date dateDB2;
                                              if (token[4].equals("null")) {
                                                 
                                                  pStatement.setDate(5, null);
                                              } else {
                                                  dateStr2 = sdf.parse(token[4]);
                                                  dateDB2 = new java.sql.Date(dateStr2.getTime());
                                                  pStatement.setDate(5, dateDB2);
                                              }
                                              pStatement.execute();
                                          }
                                      }
                                      if (fileName.contains("user.txt")) {
                                          System.out.printf("loading %s\n", fileName);
                                          while ((line = dataReader.readLine()) != null) {//for each line
                                              String[] token = line.split(Pattern.quote("\t"));
                                              pStatement = conn.prepareStatement("INSERT INTO user VALUES (?,?,?,?)");
                                              //id
                                              pStatement.setString(1, token[0]);
                                              //name
                                              pStatement.setString(2, token[1]);
                                              //address
                                              pStatement.setString(3, token[2]);
                                              //cat id
                                              pStatement.setInt(4, Integer.parseInt(token[3]));
                                              pStatement.execute();
                                          }
                                      }
                                      dataReader.close();
                                      System.out.println("LOADED!!!!!!!!!!!!");
                                  }
                                  break;
                              case 4://get entry number for every table                                   
                                  int entryCount=0;
                                  String query1 = "SELECT COUNT(*) FROM category";
                                  String query2 = "SELECT COUNT(*) FROM user";
                                  String query3 = "SELECT COUNT(*) FROM book";
                                  String query4 = "SELECT COUNT(*) FROM copy";
                                  String query5 = "SELECT COUNT(*) FROM checkout_record";
                                  String query6 = "SELECT COUNT(*) FROM author";
                                  System.out.printf("Number of entries for each table:\n");
                                  stmt = conn.createStatement();
                                  ResultSet rs = stmt.executeQuery(query1);
                                  while (rs.next()) {
                                      entryCount = rs.getInt("COUNT(*)");                                                                            
                                  }
                                  System.out.printf("category : %d\n", entryCount);
                                 
                                 
                                  stmt = conn.createStatement();
                                  rs = stmt.executeQuery(query2);
                                  while (rs.next()) {
                                      entryCount = rs.getInt("COUNT(*)");                                        
                                  }
                                  System.out.printf("user : %d\n", entryCount);
                                                                    
                                 
                                  stmt = conn.createStatement();
                                  rs = stmt.executeQuery(query3);
                                  while (rs.next()) {
                                      entryCount = rs.getInt("COUNT(*)");                                        
                                  }
                                  System.out.printf("book : %d\n", entryCount);
                                 
                                  stmt = conn.createStatement();
                                  rs = stmt.executeQuery(query4);
                                  while (rs.next()) {
                                      entryCount = rs.getInt("COUNT(*)");                                        
                                  }
                                  System.out.printf("copy : %d\n", entryCount);
                                 
                                  stmt = conn.createStatement();
                                  rs = stmt.executeQuery(query5);
                                  while (rs.next()) {
                                      entryCount = rs.getInt("COUNT(*)");                                        
                                  }
                                  System.out.printf("checkout_record : %d\n", entryCount);
                                 
                                  stmt = conn.createStatement();
                                  rs = stmt.executeQuery(query6);
                                  while (rs.next()) {
                                      entryCount = rs.getInt("COUNT(*)");                                        
                                  }
                                  System.out.printf("author : %d\n", entryCount);
                                 
                                  break;
                              case 5:
                                  returnIndicator = 1;
                                  break;
                              default:
                                  break;
                          }
                          if (returnIndicator == 1) {
                              break;
                          }
                      }
               
               break;
                 
                  /* LIBRARY USER */
                  case 2:  // library user
                      //fill code here
                      while (true) {
                          System.out.println("********LIBRARY USER********");
                          System.out.println("1.Search for books");
                          System.out.println("2.Show checkout records of a user");
                          System.out.println("3.Return to main menu");
                          System.out.print("Your command(1~3): ");
                          Scanner libusIn = new Scanner(System.in);
                          int libusCmd = libusIn.nextInt();
                          int returnIndicator = 0;
                          switch (libusCmd) {
                              case 1:  // search for books
                                  System.out.println("Choose the search criterion:");
                                  System.out.println("1.Call number");
                                  System.out.println("2.Title");
                                  System.out.println("3.Author");
                                  System.out.print("Your command(1~3): ");
                                  Scanner libusIn1 = new Scanner(System.in);
                                  int libusCmd1 = libusIn1.nextInt();
                                  switch (libusCmd1) {
                                      case 1:  // calL_number
                                          System.out.print("Type in the search keyword: ");
                                          Scanner read1 = new Scanner(System.in);
                                          String keyword1 = read1.nextLine();
                                          // System.out.println("You've inputted: " + keyword1);
                                          Statement s1 = conn.createStatement();
                                          String q1 = "SELECT b.call_number, b.title, a.name, c.copy_number "
                                              + "FROM book b, author a, copy c "
                                              + "WHERE b.call_number = '" + keyword1 + "' AND "
                                              + "b.call_number = a.call_number AND "
                                              + "c.call_number = b.call_number "
                                              + "ORDER BY b.call_number";
                                          ResultSet rs1 = s1.executeQuery(q1);
                                          System.out.println("|Call Number|Title|Author|Available No. of Copies|");
                                          while(rs1.next()) {
                                              String x1 = rs1.getString("b.call_number");
                                              String x2 = rs1.getString("b.title");
                                              String x3 = rs1.getString("a.name");
                                              int x4 = rs1.getInt("c.copy_number");
                                              System.out.println("|" + x1 + "|" + x2 + "|" + x3 + "|" + x4 + "|");
                                          }
                                          System.out.println("End of query.");
                                          break;
                                      case 2:  // title
                                          System.out.print("Type in the search keyword: ");
                                          Scanner read2 = new Scanner(System.in);
                                          String keyword2 = read2.nextLine();
                                          // System.out.println("You've inputted: " + keyword2);
                                          Statement s2 = conn.createStatement();
                                          String q2 = "SELECT b.call_number, b.title, a.name, c.copy_number "
                                              + "FROM book b, author a, copy c "
                                              + "WHERE b.title LIKE '%" + keyword2 + "%' AND "
                                              + "b.call_number = a.call_number AND "
                                              + "c.call_number = b.call_number "
                                              + "ORDER BY b.call_number";
                                          ResultSet rs2 = s2.executeQuery(q2);
                                          System.out.println("|Call Number|Title|Author|Available No. of Copies|");
                                          while(rs2.next()) {
                                              String y1 = rs2.getString("b.call_number");
                                              String y2 = rs2.getString("b.title");
                                              String y3 = rs2.getString("a.name");
                                              int y4 = rs2.getInt("c.copy_number");
                                              System.out.println("|" + y1 + "|" + y2 + "|" + y3 + "|" + y4 + "|");
                                          }
                                          System.out.println("End of query.");
                                          break;
                                      case 3:  // author
                                          System.out.print("Type in the search keyword: ");
                                          Scanner read3 = new Scanner(System.in);
                                          String keyword3 = read3.nextLine();
                                          // System.out.println("You've inputted: " + keyword3);
                                          Statement s3 = conn.createStatement();
                                          String q3 = "SELECT b.call_number, b.title, a.name, c.copy_number "
                                              + "FROM book b, author a, copy c "
                                              + "WHERE a.name LIKE '%" + keyword3 + "%' AND "
                                              + "b.call_number = a.call_number AND "
                                              + "c.call_number = b.call_number "
                                              + "ORDER BY b.call_number";
                                          ResultSet rs3 = s3.executeQuery(q3);
                                          System.out.println("|Call Number|Title|Author|Available No. of Copies|");
                                          while(rs3.next()) {
                                              String z1 = rs3.getString("b.call_number");
                                              String z2 = rs3.getString("b.title");
                                              String z3 = rs3.getString("a.name");
                                              int z4 = rs3.getInt("c.copy_number");
                                              System.out.println("|" + z1 + "|" + z2 + "|" + z3 + "|" + z4 + "|");
                                          }
                                          System.out.println("End of query.");
                                          break;
                                  }
                                  break;
                              case 2:  // show checkout recoeds of a user
                                  System.out.print("Enter the user ID: ");
                                  Scanner read4 = new Scanner(System.in);
                                  String keyword4 = read4.nextLine();
                                  // System.out.println("You've inputted: " + keyword4);
                                  Statement s4 = conn.createStatement();
                                  String q4 = "SELECT ch.call_number, ch.copy_number, b.title, a.name, ch.checkout_date, ch.return_date "
                                      + "FROM checkout_record ch, book b, author a "
                                      + "WHERE ch.user_id='" + keyword4 + "' AND "
                                      + "b.call_number = ch.call_number AND "
                                      + "a.call_number = ch.call_number "
                                      + "ORDER BY ch.checkout_date DESC";
                                  ResultSet rs4 = s4.executeQuery(q4);
                                  System.out.println("|CallNum|CopyNum|Title|Author|Check-out|Returned?|");
                                  while(rs4.next()) {
                                      String w1 = rs4.getString("ch.call_number");
                                      String w2 = rs4.getString("ch.copy_number");
                                      String w3 = rs4.getString("b.title");
                                      String w4 = rs4.getString("a.name");
                                      String w5 = rs4.getString("ch.checkout_date");
                                      String w6 = rs4.getString("ch.return_date");
                                      if(w6 == null)
                                       w6 = "No";
                                      else
                                       w6 = "Yes";
                                      System.out.println("|" + w1 + "|" + w2 + "|" + w3 + "|" + w4 + "|" + w5 + "|" + w6 + "|");
                                  }
                                  System.out.println("End of query.");
                                  break;
                              case 3:  // return
                                  returnIndicator = 1;
                                  break;
                                  default:
                                     break;
                          }
                          if (returnIndicator == 1) {
                              break;
                          }
                      }
                 break;
                  /*  LIBRARIAN */
                  case 3:  // librarian
                      while (true) {
                    System.out.println("********Liberian********");
                      System.out.println("1.Borrow book");
                      System.out.println("2.Return book");
                      System.out.println("3.List all unreturned book copies which are checked out within a period");
                      System.out.println("4.Return to menu");
                      System.out.print("Your command(1~4): ");
                      Scanner librarianIn = new Scanner(System.in);
                      int librarianCmd = librarianIn.nextInt();
                      int returnIndicator = 0;
                      mainLoop:
                      switch (librarianCmd) {
                          case 1:
                              System.out.print("Borrower user ID: ");
                              Scanner idIn = new Scanner(System.in);
                              String uID = idIn.nextLine();
                              System.out.print("Call number: ");
                              Scanner callNumIn = new Scanner(System.in);
                              String callNum = callNumIn.nextLine();
                              System.out.print("Copy number: ");
                              Scanner copyNumIn = new Scanner(System.in);
                              int copyNum = copyNumIn.nextInt();
                              Statement c1 = conn.createStatement();
                             
                              String checkAvailable = "SELECT count(*) "
                                      + "FROM checkout_record "
                                      + "WHERE call_number='" + callNum + "' AND "
                                      + "copy_number=" + copyNum + " AND "
                                      + "return_date IS NULL "
                                      + "GROUP BY call_number";
                              ResultSet rsx = c1.executeQuery(checkAvailable);
                              int borrowed = 0;
                              while (rsx.next()) {
                                  //return 0 -> not borrowed yet
                                  int notAvailable = rsx.getInt("count(*)");
                                  // System.out.println("No. of records: " + notAvailable);
                                  // If it is inside that means there are borrowed records
                                  // --> directly end
                                  System.out.println("This book copy is already borrowed by someone!");
                                  borrowed = 1;
                                  break;
                              }
                              if (borrowed == 0) {
                               // System.out.println("This book can be borrowed!");
                               Date date = new Date();
        String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                               // System.out.println(modifiedDate);
                               Statement insertion = conn.createStatement();
                               String insertCheckout = "INSERT INTO checkout_record "
                                + "VALUES ('" + uID + "', '" + callNum + "', "
                                + copyNum + ", '" + modifiedDate + "', NULL)";
                               insertion.executeUpdate(insertCheckout);
                               System.out.println("Book borrowed successfully!!!");
                              }
                              break;
                          case 2:
                    System.out.print("Borrower user ID: ");
                               Scanner idIn2 = new Scanner(System.in);
                               String uID2 = idIn2.nextLine();
                               System.out.print("Call number: ");
                               Scanner callNumIn2 = new Scanner(System.in);
                               String callNum2 = callNumIn2.nextLine();
                               System.out.print("Copy number: ");
                               Scanner copyNumIn2 = new Scanner(System.in);
                               int copyNum2 = copyNumIn2.nextInt();
                               Statement c2 = conn.createStatement();
                               String q2="SELECT user_id,call_number,copy_number "
                                       +"from checkout_record "
                                       +"where user_id = '"+ uID2 + "' and call_number = '"+callNum2+ "' and copy_number = "+copyNum2;
                               ResultSet rs2 = c2.executeQuery(q2);
                               String tmpUID="",tmpCallNum="";
                               int tmpCopyNum=0;
                              
                               while (rs2.next()){
                                   tmpUID=rs2.getString("user_id");
                                   tmpCallNum=rs2.getString("call_number");
                                   tmpCopyNum=rs2.getInt("copy_number");                                   
                               }
                              
                               if(tmpUID.equals(uID2) && tmpCallNum.equals(callNum2) && tmpCopyNum==copyNum2){    
                                   // System.out.println("Found record!!!!!!!!!!!!");
                                   Date jdate = new Date();
                                   String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(jdate);
                                   // System.out.println(modifiedDate);
                                   java.sql.Date sdate;                                   
                                   String q3="UPDATE checkout_record SET return_date = '"+modifiedDate
                                           +"' where user_id = '"+ uID2 + "' and call_number = '"+callNum2+ "' and copy_number = "+copyNum2;
                                   c2.execute(q3);
                                   System.out.println("Book returned!!!!!!!!!!!!");
                               }else{
                                   System.out.println("[ERROR]: no matching record , book not borrowed yet dumb dumb");                               
                               }
                              break;
                          case 3:
                                System.out.print("Type in the starting date [dd/mm/YYYY]: ");
                                Scanner d1 = new Scanner(System.in);
                                  String date1 = d1.nextLine();
                                  System.out.print("Type in the ending date [dd/mm/YYYY]: ");
                                Scanner d2 = new Scanner(System.in);
                                  String date2 = d2.nextLine();
                                  // System.out.println("Starting date: " + date1);
                                  // System.out.println("Ending date: " + date2);
                                  Statement s9 = conn.createStatement();
                                String q9 = "SELECT user_id, call_number, copy_number, checkout_date "
                                      + "FROM checkout_record "
                                      + "WHERE checkout_date>='" + date1 + "' AND "
                                      + "checkout_date<='" + date2 + "' AND "
                                      + "return_date IS NULL "
                                      + "ORDER BY checkout_date DESC";
                                  ResultSet rs9 = s9.executeQuery(q9);
                                  System.out.println("|User ID|Call number|Copy number|Checkout date|");
                                  while(rs9.next()) {
                                      String m1 = rs9.getString("user_id");
                                      String m2 = rs9.getString("call_number");
                                      String m3 = rs9.getString("copy_number");
                                      String m4 = rs9.getString("checkout_date");
                                      System.out.println("|" + m1 + "|" + m2 + "|" + m3 + "|" + m4 + "|");
                                  }
                                  System.out.println("End of query.");
                                  break;
                          case 4:
                              returnIndicator = 1;
                                 break;
                         default:
                                 break;
                      }
                      if (returnIndicator == 1) {
                          break;
                      }
            }
                 break;
                  case 4:  // exit
                      System.exit(0);
                  default:
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

