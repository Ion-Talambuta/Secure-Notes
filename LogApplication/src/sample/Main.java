package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStageSet) throws Exception{

        Parent rootStage = FXMLLoader.load(Objects.requireNonNull(getClass()
                .getResource("graphicSourceConfigurations/sample.fxml")));
        primaryStageSet.initStyle(StageStyle.UNDECORATED);

        primaryStageSet.setTitle("Secure IT");
        primaryStageSet.setScene(new Scene(rootStage, 1366, 768));
        primaryStageSet.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
