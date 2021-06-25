package sample.appConfigurations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseHandler extends ConfigurationController {

    /**
     * Establish Connection to Database with Parameters From Configuration Controller:
     */
    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        var connectString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(connectString, dbUser, dbPass);
    }

    //Sign Up New Users with parameters from Constants Parameters and transmit to Database User table:
    public void signUpUsers(UsersParameters usersParameters) throws SQLException, ClassNotFoundException {
        String insertData = "INSERT INTO " + ConstantsParameters.USER_TABLE + "(" + ConstantsParameters.USER_FIRSTNAME
                + "," + ConstantsParameters.USER_LASTNAME + "," + ConstantsParameters.USER_LOGIN + ","
                + ConstantsParameters.USER_PASSWORD + "," + ConstantsParameters.USER_LOCATION + ")" + "VALUES(?,?,?,?,?)";

        //Set User parameters to each, in relations with Class Users:
        try (var prpStm = getDbConnection().prepareStatement(insertData)) {
            prpStm.setString(1, usersParameters.getFirstName());
            prpStm.setString(2, usersParameters.getLastName());
            prpStm.setString(3, usersParameters.getUserName());
            prpStm.setString(4, usersParameters.getPassword());
            prpStm.setString(5, usersParameters.getLocation());

            prpStm.executeUpdate();
        }
    }
}
