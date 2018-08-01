package Main;

import Controller.File_chooser_controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/File-chooser.fxml"));
            Parent root = fxmlLoader.load();

            File_chooser_controller controller = fxmlLoader.getController();


            controller.setParent(root);
            controller.setScene(new Scene(root));
            controller.setStage(primaryStage);
            controller.launchController();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { launch(args); }
}
