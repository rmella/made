package com.velonuboso.made.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialog;

public class MainController implements Initializable {

    private Stage stage;

        private Label label;
    @FXML
    private TitledPane x1;
    @FXML
    private Button btnSaveExperiment;
    @FXML
    private Button btnRunExperiment;
    @FXML
    private Button btnViewResults;
    @FXML
    private Button btnLoadSetup;
    @FXML
    private Button btnLoadExperiment;
    @FXML
    private Button btnLoadExecution;
    @FXML
    private Button btnSaveExecution;
    @FXML
    private Button btnExecuteSeed;

        private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    @FXML
    private void handleLoadSetupAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(stage);
    }

    
    @FXML
    private void handleLoadExperimentAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(stage);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleSaveSetupAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(stage);
    }

    @FXML
    private void handleRunExperimentAction(ActionEvent event) {
    }

    @FXML
    private void handleViewResultsAction(ActionEvent event) {
    }

    @FXML
    private void handleSaveExperimentAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(stage);
    }

    @FXML
    private void handleLoadExecutionAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(stage);
    }

    @FXML
    private void handleSaveExecutionAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(stage);
    }

    @FXML
    private void handleExecuteSeedAction(ActionEvent event) {
        Dialog d = new Dialog(stage, "Executing...");
        d.show();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    
}
