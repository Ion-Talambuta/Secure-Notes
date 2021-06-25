package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.NoticeController.DataUsersController;
import sample.appConfigurations.DataBaseHandler;
import sample.appConfigurations.EncryptionAES;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ApplicationController implements Initializable {

    //Table Data Users Rows And Cell -->
    @FXML
    private TableView<DataUsersController> dataUsersTable;
    @FXML
    private TableColumn<DataUsersController, String> idNotice;

    @FXML
    private TableColumn<DataUsersController, String> idusers_notes;

    @FXML
    private TableColumn<DataUsersController, java.util.Date> data;
    @FXML
    private TableColumn<DataUsersController, String> notes;

    @FXML
    private String id;

    @FXML
    private TextArea NoticeTextInput;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    private String iDPass;
    private String forDelete;

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    ObservableList<DataUsersController> dataUsersControllerList = FXCollections.observableArrayList();

    //Get ID of the User
    public void getLabelText(String text){
        id = text;
    }

    //Get the password of user
    public void getLabelPass(String pass){
        iDPass = pass;
    }

    public void initialize(){
        //Empty only for initialize #FXML method
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadDataUsers();
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    void saveNotice(MouseEvent saveNoticeEvent) throws Exception {
        //variable @notice, @date & @timestamp
        String noticeTextInput = NoticeTextInput.getText();
        var dateOfNotes = new java.util.Date();
        var timeOfNotedNotice = new Timestamp(dateOfNotes.getTime());

        //Initialize for Encryption
        String password = iDPass;
        String encryptedTextBase64 = EncryptionAES.encryptText(noticeTextInput.getBytes(UTF_8), password);

        //SQL Statement for insert in database values:
        var queryINSERT = "INSERT INTO data (`data`, `notes`, `iduser`) VALUES (?,?,?)";
        var dbDataBaseHandler = new DataBaseHandler();
        connection = dbDataBaseHandler.getDbConnection();

        if (saveNoticeEvent.getSource() == saveButton) {
            if (noticeTextInput.isEmpty()) {
                var a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText(null);
                a.setContentText("Please fill all data in fields");
                a.showAndWait();
            } else {
                preparedStatement = connection.prepareStatement(queryINSERT);
                preparedStatement.setString(1, String.valueOf(timeOfNotedNotice));
                preparedStatement.setString(2, encryptedTextBase64);
                preparedStatement.setString(3, id);
                preparedStatement.executeUpdate();

                var b = new Alert(Alert.AlertType.INFORMATION);
                b.setHeaderText(null);
                b.setContentText("NOTICES ARE SAVED!");
                refreshTable();
                b.showAndWait();
            }
        }
    }

    @FXML
    void deleteNotice(MouseEvent deleteNoticeEvent) {

        if(deleteNoticeEvent.getSource() == deleteButton) {
            //Implementation to delete rows on Button Delete clicked
            try{
                var query = "DELETE FROM data WHERE idusers_notes = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, forDelete);
                preparedStatement.executeUpdate();
                refreshTable();
            } catch (SQLException ex) {
               ex.printStackTrace();
            }

            var c = new Alert(Alert.AlertType.INFORMATION);
            c.setHeaderText(null);
            c.setContentText("NOTICES ARE DELETED!");
            c.showAndWait();
        }
    }

    private void loadDataUsers() throws SQLException, ClassNotFoundException {

        var dbDataBaseHandler = new DataBaseHandler();
        connection = dbDataBaseHandler.getDbConnection();
        refreshTable();

        idNotice.setCellValueFactory(new PropertyValueFactory<>("iduser"));
        idusers_notes.setCellValueFactory(new PropertyValueFactory<>("idusers_notes"));
        data.setCellValueFactory(new PropertyValueFactory<>("data"));
        notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }

    @FXML
    void select() {
        int index = dataUsersTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) return;
        forDelete = idusers_notes.getCellData(index);
        NoticeTextInput.setText(notes.getCellData(index));
    }

    @FXML
    void refreshTable() {
        dataUsersControllerList.clear();
        var selectQuery = "SELECT * FROM data WHERE iduser =? ORDER BY data DESC";

        try {
            String idUserReceived = id;
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, idUserReceived);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String idusers_notes = resultSet.getString(1);
                Date date = resultSet.getDate(2);
                String notes = resultSet.getString(3);
                String IDUSER = resultSet.getString(4);

                //Initialize for Encryption
                String password = iDPass;
                String decryptedText = EncryptionAES.decryptText(notes, password);

                dataUsersControllerList.add(new DataUsersController(
                        IDUSER, //ID users notes
                        idusers_notes, //ID of the table entry
                        date, //local date of the entry
                        decryptedText)); //notes of users.
                dataUsersTable.setItems(dataUsersControllerList);
            }
        }catch (Exception refreshTableEx) {
            refreshTableEx.printStackTrace();
        }
    }

    public void logout(MouseEvent logoutEvent) throws IOException {
        var loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/GraphicSourceConfigurations/sample.fxml"));
        loader.load();
        Parent root = loader.getRoot();

        var stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        var node = (Node) logoutEvent.getSource();
        var stage2 = (Stage) node.getScene().getWindow();
        stage2.close();
    }
}

