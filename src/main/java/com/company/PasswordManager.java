package com.company;

import java.sql.*;
import java.util.*;


public class PasswordManager {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";        //Configure the driver needed
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/users";     //Connection string – where's the database?
    static final String USER = "clara";   //TODO replace with your username
    static final String PASSWORD = "password";   //TODO replace with your password


    public static void main(String[] args) {

        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check you have drives and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);  //No driver? Need to fix before anything else will work. So quit the program
        }

        try( Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
             Statement statement = conn.createStatement()) {

            String dropTableIfExists = "DROP TABLE IF EXISTS users";
            statement.executeUpdate(dropTableIfExists);

            String createTableSQL = "CREATE TABLE Users (Name varchar(30), Passwd varchar(30) )";
            statement.executeUpdate(createTableSQL);

            //Add some test data
            String prepStatInsert = "INSERT INTO Users VALUES ( ? , ? )";
            PreparedStatement psInsert = conn.prepareStatement(prepStatInsert);

            psInsert.setString(1, "Clara");
            psInsert.setString(2, "i <3 programming");
            psInsert.executeUpdate();

            psInsert.setString(1, "Andy");
            psInsert.setString(2, "i <3 databases");
            psInsert.executeUpdate();

            psInsert.setString(1, "Duncan");
            psInsert.setString(2, "i <3 networks");
            psInsert.executeUpdate();

            //Fetch all the data and display it, just to validate
            String fetchAllDataSQL = "SELECT * FROM Users";
            ResultSet rs = statement.executeQuery(fetchAllDataSQL);
            while (rs.next()) {
                String user = rs.getString("name");
                String pwd = rs.getString("Passwd");
                System.out.println("User name = " + user + " pwd = " + pwd);
            }

            System.out.println("test data added to passwords table\n");

            Scanner scanner = new Scanner(System.in);

            System.out.println("Test the password fetching code\n");

            while (true) {
                System.out.println("Enter username to fetch password or enter to quit");
                String username = scanner.nextLine();
                if (username.equals("")) {
                    break;
                }

                String fetchpw = "select passwd from users where name='" + username + "'";
                rs = statement.executeQuery(fetchpw);

                boolean userfound = false;

                while (rs.next()) {
                    userfound = true;
                    String password = rs.getString("passwd");
                    System.out.println("Your password is: \n" + password);
                }

                if (userfound == false) {
                    System.out.println("Sorry name not found in database");
                }

            }

            scanner.close();
            rs.close();
            statement.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        }


    }
}
