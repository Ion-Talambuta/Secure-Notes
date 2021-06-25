package sample;

import de.mkammerer.argon2.Argon2Factory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.appConfigurations.DataBaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class SampleController implements Initializable {

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private Button SignUpButton;

    String idField = null;
    String passwordToSendInController = null;


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Action event on enter Sign Up button
        SignUpButton.setOnAction(signUpEvent -> {
            SignUpButton.getScene().getWindow().isShowing();
            var loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/GraphicSourceConfigurations/signUp.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            var stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        });
    }

    public void LogInNewUsers(MouseEvent logInEvent) {
        String username = login_field.getText();
        String password = password_field.getText();

        if (!username.isEmpty() && !password.isEmpty()) {

            var argon2PasswordHash = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2d) ;
            var sql = "SELECT * FROM users WHERE username = ? ";

            try {
                var dbDataBaseHandler = new DataBaseHandler();
                var conn = dbDataBaseHandler.getDbConnection();

                try (var preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setString(1, username);
                    var resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {

                        //Implementation to receive hash from Data Base
                        idField = resultSet.getString(1);
                        var hashFromBD = resultSet.getString(5);
                        boolean success = argon2PasswordHash.verify(hashFromBD, password);

                        if (success) {

                            var loader = new FXMLLoader(getClass()
                                    .getResource("/sample/GraphicSourceConfigurations/application.fxml"));
                            Parent root = loader.load();

                            passwordToSendInController = password_field.getText();
                            ApplicationController applicationController;
                            applicationController = loader.getController();
                            applicationController.getLabelText(idField);
                            applicationController.getLabelPass(passwordToSendInController);

                            var stage = new Stage();
                            stage.initStyle(StageStyle.UNDECORATED);
                            var scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();

                            //Close previous scene with set stage2
                            var node = (Node) logInEvent.getSource();
                            var stage2 = (Stage) node.getScene().getWindow();
                            stage2.close();

                        } else {
                            // set alert in case of error username or password
                            var c = new Alert(Alert.AlertType.INFORMATION);
                            c.setAlertType(Alert.AlertType.INFORMATION);
                            c.setContentText("Credentials is incorrect!");
                            c.show();
                        }
                    } else {
                        // set alert in case of error username or password
                        var b = new Alert(Alert.AlertType.INFORMATION);
                        b.setAlertType(Alert.AlertType.INFORMATION);
                        b.setContentText("Credentials is incorrect!");
                        b.show();
                    }
                    //Close connection in reverse order to opening sequence
                    resultSet.close();
                }
                conn.close();
                }catch(SQLException | IOException | ClassNotFoundException sqlException) {
                    sqlException.printStackTrace();
                }
            } else{
                var a = new Alert(Alert.AlertType.INFORMATION);
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Login or password is Empty");
                a.show();

                deleteEnteredDataInField();
            }
        }

    private void deleteEnteredDataInField(){
        login_field.clear();
        password_field.clear();
    }
}
