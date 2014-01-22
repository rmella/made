package com.velonuboso.made.gui;

import com.velonuboso.made.core.setup.BaseAgentSetup;
import com.velonuboso.made.core.setup.FitnessSetup;
import com.velonuboso.made.core.setup.GASetup;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialog;

public class MainController implements Initializable {

    private Stage stage;
    @FXML
    private Button btnRunExperiment;
    @FXML
    private Button btnLoadSetup;
    @FXML
    private Button btnLoadExecution;
    @FXML
    private Button btnSaveExecution;
    @FXML
    private Button btnExecuteSeed;

    @FXML
    private Accordion accordion1;
    @FXML
    private Hyperlink infoLabel;

    @FXML
    private Tooltip tooltipGenerations;
    @FXML
    private TitledPane globalSetupPane;
    @FXML
    private TextField txtMapGridDimension;
    @FXML
    private TextField txtFoodPerDay;
    @FXML
    private TextField txtNumberOfInitialAgents;
    @FXML
    private TextField txtNumberOfVirtualDays;
    @FXML
    private TextField txtNumberOfProfiles;
    @FXML
    private TitledPane x2;
    @FXML
    private TextField txtBaseLifeInDays;
    @FXML
    private TextField txtBaseEnergy;
    @FXML
    private TextField txtBaseSmell;
    @FXML
    private TextField txtBaseNutrition;
    @FXML
    private TextField txtBaseBite;
    @FXML
    private TextField txtBaseFur;
    @FXML
    private TextField txtBaseAgeToBeAdultMale;
    @FXML
    private TextField txtBaseAgeToBeAdultFemale;
    @FXML
    private TextField txtbasePregnancyDays;
    @FXML
    private TextField txtGenerations;
    @FXML
    private TextField txtPopulation;
    @FXML
    private TextField txtExecutionsAVG;
    @FXML
    private Button btnSaveSetup;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private void handleLoadSetupAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Properties file (*.properties)", "*.properties"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All", "*"));

        File f = fileChooser.showOpenDialog(stage);
        if (f != null) {
            try {
                Configurator.getInstance().loadProperties(f);
                settextValues();
                MadeLogger.getInstance().info("Your configuration has succesfully been loaded");
            } catch (Exception ex) {
                MadeLogger.getInstance().error("Couldn't store setup in the target file", ex);
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleSaveSetupAction(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Properties file (*.properties)", "*.properties"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All", "*"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

            fileChooser.setInitialFileName("made_setup_" + sdf.format(new Date()) + ".properties");
            File f = fileChooser.showSaveDialog(stage);
            if (f != null) {
                Configurator.getInstance().storeProperties(f);
            }
        } catch (IOException ex) {
            MadeLogger.getInstance().error("Couldn't store setup in the target file", ex);
        }
    }

    @FXML
    private void handleRunExperimentAction(ActionEvent event) {
        Tab t = new Tab();
        t.setText("Execution");
        mainTabPane.getTabs().add(t);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GAExecution.fxml"));
        Parent root;
        try {
            GAExecutionController controller = new GAExecutionController();

            loader.setController(controller);
            root = (Parent) loader.load();
            t.setContent(root);
            t.setClosable(true);
            mainTabPane.getSelectionModel().select(t);
            BaseAgentSetup baseAgentSetup = BaseAgentSetupFromForm();
            GlobalSetup globalSetup = GlobalSetupFromForm();
            GASetup gaSetup = GASetupFromForm();
            FitnessSetup fitnessSetup = FitnessSetupFromForm();
            controller.init(baseAgentSetup, globalSetup, gaSetup, fitnessSetup);
            Thread thread = new Thread(controller);
            thread.start();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleExecuteSeedAction(ActionEvent event) {
        Dialog d = new Dialog(stage, "Executing...");
        d.show();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    void setup() {
        accordion1.getPanes().get(0).setExpanded(true);
        /*Text t1 = new Text("www.velonuboso.com/made/");
        
         infoPanel.getChildren().add(t1);
         */
        infoLabel.setTextFill(Color.WHITE);
        infoLabel.setText("www.velonuboso.com/made");
        tooltipGenerations.setText("Number of generations of the experiment");

        Configurator c = Configurator.getInstance();

        settextValues();

        txtBaseAgeToBeAdultFemale.focusedProperty().addListener(new FocusPropertyChangeListener(txtBaseAgeToBeAdultFemale));
        txtBaseAgeToBeAdultMale.focusedProperty().addListener(new FocusPropertyChangeListener(txtBaseAgeToBeAdultMale));
        txtBaseBite.focusedProperty().addListener(new FocusPropertyChangeListener(txtBaseBite));
        txtBaseEnergy.focusedProperty().addListener(new FocusPropertyChangeListener(txtBaseEnergy));
        txtBaseFur.focusedProperty().addListener(new FocusPropertyChangeListener(txtBaseFur));
        txtBaseLifeInDays.focusedProperty().addListener(new FocusPropertyChangeListener(txtBaseLifeInDays));
        txtBaseNutrition.focusedProperty().addListener(new FocusPropertyChangeListener(txtBaseNutrition));
        txtBaseSmell.focusedProperty().addListener(new FocusPropertyChangeListener(txtBaseSmell));
        txtExecutionsAVG.focusedProperty().addListener(new FocusPropertyChangeListener(txtExecutionsAVG));
        txtFoodPerDay.focusedProperty().addListener(new FocusPropertyChangeListener(txtFoodPerDay));
        txtGenerations.focusedProperty().addListener(new FocusPropertyChangeListener(txtGenerations));
        txtMapGridDimension.focusedProperty().addListener(new FocusPropertyChangeListener(txtMapGridDimension));
        txtNumberOfInitialAgents.focusedProperty().addListener(new FocusPropertyChangeListener(txtNumberOfInitialAgents));
        txtNumberOfProfiles.focusedProperty().addListener(new FocusPropertyChangeListener(txtNumberOfProfiles));
        txtNumberOfVirtualDays.focusedProperty().addListener(new FocusPropertyChangeListener(txtNumberOfVirtualDays));
        txtPopulation.focusedProperty().addListener(new FocusPropertyChangeListener(txtPopulation));
        txtbasePregnancyDays.focusedProperty().addListener(new FocusPropertyChangeListener(txtbasePregnancyDays));
    }

    private void settextValues() {
        Configurator c = Configurator.getInstance();
        txtBaseAgeToBeAdultFemale.setText(c.get("txtBaseAgeToBeAdultFemale"));
        txtBaseAgeToBeAdultMale.setText(c.get("txtBaseAgeToBeAdultMale"));
        txtBaseBite.setText(c.get("txtBaseBite"));
        txtBaseEnergy.setText(c.get("txtBaseEnergy"));
        txtBaseFur.setText(c.get("txtBaseFur"));
        txtBaseLifeInDays.setText(c.get("txtBaseLifeInDays"));
        txtBaseNutrition.setText(c.get("txtBaseNutrition"));
        txtBaseSmell.setText(c.get("txtBaseSmell"));
        txtExecutionsAVG.setText(c.get("txtExecutionsAVG"));
        txtFoodPerDay.setText(c.get("txtFoodPerDay"));
        txtGenerations.setText(c.get("txtGenerations"));
        txtMapGridDimension.setText(c.get("txtMapGridDimension"));
        txtNumberOfInitialAgents.setText(c.get("txtNumberOfInitialAgents"));
        txtNumberOfProfiles.setText(c.get("txtNumberOfProfiles"));
        txtNumberOfVirtualDays.setText(c.get("txtNumberOfVirtualDays"));
        txtPopulation.setText(c.get("txtPopulation"));
        txtbasePregnancyDays.setText(c.get("txtbasePregnancyDays"));

    }

    @FXML
    private void handleLoadExecutionAction(ActionEvent event) {
    }

    @FXML
    private void handleSaveExecutionAction(ActionEvent event) {
    }

    private BaseAgentSetup BaseAgentSetupFromForm() {
        int baseAgeToBeAdultFemale = Integer.parseInt(txtBaseAgeToBeAdultFemale.getText());
        int baseAgeToBeAdultMale = Integer.parseInt(txtBaseAgeToBeAdultMale.getText());
        int baseBite = Integer.parseInt(txtBaseBite.getText());
        int baseEnergy = Integer.parseInt(txtBaseEnergy.getText());
        int baseFur = Integer.parseInt(txtBaseFur.getText());
        int baseLifeInDays = Integer.parseInt(txtBaseLifeInDays.getText());
        int baseNutrition = Integer.parseInt(txtBaseNutrition.getText());
        int baseSmell = Integer.parseInt(txtBaseSmell.getText());
        int basePregnancyDays = Integer.parseInt(txtbasePregnancyDays.getText());
        BaseAgentSetup baseAgentSetup = new BaseAgentSetup(baseAgeToBeAdultFemale, baseAgeToBeAdultMale, baseBite, baseEnergy, baseFur, baseLifeInDays, baseNutrition, baseSmell, basePregnancyDays);
        return baseAgentSetup;
    }

    private GlobalSetup GlobalSetupFromForm() {
        int foodPerDay = Integer.parseInt(txtFoodPerDay.getText());
        int mapGridDimension  = Integer.parseInt(txtMapGridDimension.getText());
        int numberOfInitialAgents  = Integer.parseInt(txtNumberOfInitialAgents.getText());
        int numberOfProfiles = Integer.parseInt(txtNumberOfProfiles.getText());
        int numberOfVirtualDays = Integer.parseInt(txtNumberOfVirtualDays.getText());
        GlobalSetup globalSetup = new GlobalSetup(foodPerDay, mapGridDimension, numberOfInitialAgents, numberOfProfiles, numberOfVirtualDays);
        return globalSetup;
    }

    private GASetup GASetupFromForm() {
        int executionsAVG = Integer.parseInt(txtExecutionsAVG.getText());
        int generations = Integer.parseInt(txtGenerations.getText());
        int population = Integer.parseInt(txtPopulation.getText());
        GASetup gASetup = new GASetup(executionsAVG, generations, population);
        return gASetup;
    }

    private FitnessSetup FitnessSetupFromForm() {
        FitnessSetup fitnessSetup = new FitnessSetup();
        return fitnessSetup;
    }

    private class FocusPropertyChangeListener implements ChangeListener<Boolean> {

        private TextField t;

        FocusPropertyChangeListener(TextField f) {
            t = f;
            System.out.println("New FPCL instance");
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> ov,
                Boolean oldb, Boolean newb) {

            if (!newb) {
                String key = t.getId();
                if (Configurator.getInstance().get(key).compareTo(t.getText()) != 0) {
                    try {
                        Configurator.getInstance().set(key, t.getText());
                    } catch (Exception ex) {
                        MadeLogger.getInstance().error("Could not store value " + t.getText() + " for property " + key, ex);
                    }
                }
            }
        }
    }
}
