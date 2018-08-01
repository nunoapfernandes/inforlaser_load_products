package Controller;

import com.opencsv.CSVReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

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

    public void launchController(){
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
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
            String csvFile = this.file_path_text.getText();
            String path_file = new File(this.file_path_text.getText()).getParent();

            Pattern drum_generico = Pattern.compile("(?=.*DRUM)(?=.*GENERICO)");
            Pattern drum_remanufacturado = Pattern.compile("(?=.*DRUM)(?=.*REMANUFACTURADO)");
            Pattern toner_generico = Pattern.compile("(?=.*TONER)(?=.*GENERICO)");
            Pattern toner_remanufacturado = Pattern.compile("(?=.*TONER)(?=.*REMANUFACTURADO)");
            Pattern tinta_generico = Pattern.compile("(?=.*TINTA)(?=.*GENERICO)");
            Pattern tinta_remanufacturado = Pattern.compile("(?=.*TINTA)(?=.*REMANUFACTURADO)");


            PrintWriter prod = new PrintWriter(path_file + "\\prod_sql.sql", "UTF-8");
            PrintWriter prod_dim = new PrintWriter(path_file+ "\\prod_dim.sql", "UTF-8");
            PrintWriter error_file = new PrintWriter(path_file+"\\errors.txt", "UTF-8");

            CSVReader reader;
            reader = new CSVReader(new FileReader(csvFile), ';');
            String [] line;
            reader.skip(1);

            while((line = reader.readNext()) != null){
                StringBuilder sp = new StringBuilder();
                String referencia = "\'" + line[0] + "\'";
                String marca = "\'" + line[1] + "\'";
                String dimensao = "\'" + line[4] + "\'";
                String descricao = "\'" + line[3] + "\'";
                String pvp = line[7].replaceAll(",",".").trim();
                int switcher = 0;
                int categoria = 0;

                switch (Integer.valueOf(line[5])){
                    case 181:
                        switcher = 1;
                        if (tinta_generico.matcher(line[3]).find()){
                            categoria = 273;
                        }
                        else if(tinta_remanufacturado.matcher(line[3]).find()) {
                            categoria = 274;
                        }
                        else {
                            switcher = 0;
                            }
                        break;
                    case 193:
                        categoria = 281;
                        switcher = 1;
                        break;
                    case 237:
                        categoria = 280;
                        switcher = 1;
                        break;
                    case 239:
                        categoria = 284;
                        switcher = 2;
                        break;
                    case 185:
                        switcher=1;
                        if (toner_generico.matcher(line[3]).find()){
                            categoria = 270;
                        }else if(toner_remanufacturado.matcher(line[3]).find()){
                            categoria = 271;
                        }else if(drum_generico.matcher(line[3]).find()){
                            categoria = 276;
                        }else if(drum_remanufacturado.matcher(line[3]).find()){
                            categoria = 277;
                        }else {
                            switcher = 0;
                        }
                        break;
                    case 198:
                        switcher = 2;
                        categoria = 283;
                        break;
                    default:
                        System.out.println("Error figuring out where to put");
                        break;
                }

                switch (switcher){
                    case 0:
                        sp.append(referencia + ";" + marca + ";" + descricao + ";" + dimensao + ";" + pvp + "\n");
                        error_file.write(sp.toString());
                        break;
                    case 1:
                        sp.append("CALL insert_prod(");
                        sp.append(referencia + ",");
                        sp.append(marca + ",");
                        sp.append(categoria + ",");
                        sp.append(descricao + ",");
                        sp.append(pvp + ");\n");
                        prod.write(sp.toString());
                        break;
                    case 2:
                        sp.append("CALL insert_prod_dim(");
                        sp.append(referencia + ",");
                        sp.append(marca + ",");
                        sp.append(categoria + ",");
                        sp.append(descricao + ",");
                        sp.append(pvp + ",");
                        sp.append(dimensao + ");\n");
                        prod_dim.write(sp.toString());
                        break;
                    default:
                        System.out.println("Error");
                }
            }

            prod.close();
            prod_dim.close();
            error_file.close();

        }catch (IOException| NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("File not found");

            alert.showAndWait();
        }
    }
}
