package Controller;

import com.opencsv.CSVReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class File_chooser_controller {

    @FXML
    private TextField file_path_text;

    private File file;

    private Parent parent;
    private Stage stage;
    private Scene scene;

    public void setParent(Parent parent){ this.parent = parent; }
    public void setStage(Stage stage) { this.stage = stage; }
    public void setScene(Scene scene){ this.scene = scene; }

    private Desktop desktop = Desktop.getDesktop();

    public void launchController(){
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(true);
        stage.show();
    }

    public void select_file(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        this.file = fileChooser.showOpenDialog(stage);
        if(this.file != null){
            this.file_path_text.setText(this.file.getAbsolutePath());
        }

    }

    public void generate_sql(ActionEvent actionEvent) {
        try{
            String csvFile = this.file.getAbsolutePath();

            CSVReader reader;
            reader = new CSVReader(new FileReader(csvFile), ';');
            String [] line;

            while((line = reader.readNext()) != null){

            }
        }catch (IOException| NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("File not found");

            alert.showAndWait();
        }
    }
}
