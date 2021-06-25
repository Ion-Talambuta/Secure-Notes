package sample;

import de.mkammerer.argon2.Argon2Factory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.appConfigurations.DataBaseHandler;
import sample.appConfigurations.UsersParameters;

import java.sql.SQLException;

public class SignUpController {
    @FXML
    private TextField SignUpSurname;
    @FXML
    private TextField login_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private PasswordField password_fieldMatched;
    @FXML
    private TextField SignUpName;
    @FXML
    private TextField SignUpLocation;

    @FXML
    void initialize() {
        //Empty only for initialize #FXML method
    }

     /**
     * Sign Up New Users Method.
     */
     public void SignUpNewUsers() throws SQLException, ClassNotFoundException {

        var dbDataBaseHandler = new DataBaseHandler();
        String firstName = SignUpSurname.getText();
        String lastName = SignUpName.getText();
        String userName = login_field.getText();
        String password = password_field.getText();
        String passwordConfirmation = password_fieldMatched.getText();
        String location = SignUpLocation.getText();

        //Verify the complexity of the password with the specials characters
        var complexityCharsForPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,32}$";

        if (firstName.equals("") && lastName.equals("") && userName.equals("")
                && password.equals("") && location.equals("")) {
                    // set alert in case of empty username or password
                    var a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("Error, One or more fields are empty!");
                    a.show();
        } else {
            if (password.length() < 9 || !password.equals(passwordConfirmation)) {
                // set alert in case of empty Password is shorter than 9 chars
                var b = new Alert(Alert.AlertType.NONE);
                b.setAlertType(Alert.AlertType.INFORMATION);
                b.setContentText("Password cannot be shorter than 9 chars");
                b.show();
            } else {
                if (!password.matches(complexityCharsForPassword)) {
                    //Passwords complexity Use 8 or more characters with a mix of letters, numbers & symbol
                    var c = new Alert(Alert.AlertType.NONE);
                    c.setAlertType(Alert.AlertType.INFORMATION);
                    c.setContentText("Please use in your Password letters, numbers and symbols");
                    c.show();
                } else {
                    /* Argon2 Implementation  */
                    var argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2d);
                    String encodedPSWD = argon2.hash(4, 1024 * 1024, 8, password);

                    /* Argon2 Implementation  */
                    var usersParameters = new UsersParameters(firstName, lastName, userName, encodedPSWD, location);
                    dbDataBaseHandler.signUpUsers(usersParameters);

                    var d = new Alert(Alert.AlertType.INFORMATION);
                    d.setAlertType(Alert.AlertType.INFORMATION);
                    d.setContentText("You Registered Successfully! " +
                            "Please close the registration window and Log In with your credentials");
                    d.show();

                    deleteEnteredDataInField();
                }
            }
        }
    }

    /**
     * Delete Entered Data in Each Field to prevent Copy.
     */
    private void deleteEnteredDataInField() {
        login_field.clear();
        password_field.clear();
        password_fieldMatched.clear();
        SignUpSurname.clear();
        SignUpName.clear();
        SignUpLocation.clear();

    }

}

