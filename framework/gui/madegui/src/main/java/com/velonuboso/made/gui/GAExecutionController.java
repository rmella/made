/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.velonuboso.made.gui;

import com.velonuboso.made.core.Launcher;
import com.velonuboso.made.core.interfaces.ExecutionListenerInterface;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.setup.BaseAgentSetup;
import com.velonuboso.made.core.setup.FitnessSetup;
import com.velonuboso.made.core.setup.GASetup;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javax.imageio.ImageIO;

/**
 *
 * @author raiben@gmail.com
 */
public class GAExecutionController implements Initializable, Runnable, ExecutionListenerInterface {

    @FXML
    private TableView GATable;

    @FXML
    private LineChart GAGraph;

    @FXML
    private TextArea GAShell;

    @FXML
    private TableColumn GATableC1;
    @FXML
    private TableColumn GATableC2;
    @FXML
    private TableColumn GATableC3;
    @FXML
    private TableColumn GATableC4;
    @FXML
    private ProgressBar progress;

    private final Random r = new Random();

    private XYChart.Series seriesMax;
    private XYChart.Series seriesAvg;
    private ObservableList o;

    private BaseAgentSetup baseAgentSetup;
    private GlobalSetup globalSetup;
    private GASetup gASetup;
    private FitnessSetup fitnessSetup;

    private float theProgress = 0;

    private TabPane tabPane;
    private static char counter = 'A';

    private Launcher l = null;
    
    private String filename = ""; 
    
    public GAExecutionController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 
    }

    public void init(
            BaseAgentSetup baseAgentSetup,
            GlobalSetup globalSetup,
            GASetup gASetup,
            FitnessSetup fitnessSetup,
            TabPane tabPane,
            int gaId,
            String filename
    ) {
        this.baseAgentSetup = baseAgentSetup;
        this.gASetup = gASetup;
        this.globalSetup = globalSetup;
        this.fitnessSetup = fitnessSetup;
        this.tabPane = tabPane;
        this.filename = filename;
        
        o = FXCollections.observableArrayList();

        seriesMax = new XYChart.Series();
        seriesMax.setName("Best Fitness");

        seriesAvg = new XYChart.Series();
        seriesAvg.setName("Average Fitness");
        GATableC1.setCellValueFactory(
                new PropertyValueFactory<GAExecutionLine, String>("id"));
        GATableC2.setCellValueFactory(
                new PropertyValueFactory<GAExecutionLine, String>("maxFitness"));
        GATableC3.setCellValueFactory(
                new PropertyValueFactory<GAExecutionLine, String>("avgFitness"));

        GATableC4.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<GAExecutionLine, GAExecutionLine>, ObservableValue<GAExecutionLine>>() {

                    @Override
                    public ObservableValue<GAExecutionLine> call(TableColumn.CellDataFeatures<GAExecutionLine, GAExecutionLine> features) {
                        return new ReadOnlyObjectWrapper(features.getValue());
                    }
                });
        GATableC4.setCellFactory(
                new Callback<TableColumn<GAExecutionLine, GAExecutionLine>, TableCell<GAExecutionLine, GAExecutionLine>>() {

                    @Override
                    public TableCell<GAExecutionLine, GAExecutionLine> call(TableColumn<GAExecutionLine, GAExecutionLine> btnCol) {
                        return new TableCell<GAExecutionLine, GAExecutionLine>() {
                            final Button button = new Button();

                            @Override
                            public void updateItem(final GAExecutionLine ch, boolean empty) {
                                super.updateItem(ch, empty);
                                if (ch != null) {
                                    button.setText("Run");
                                    button.setTooltip(new Tooltip("Press to run an environment with this given solution"));
                                    setGraphic(button);
                                    button.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {

                                            Tab t = new Tab("Env exec (" + gaId + "." + Character.toString(++counter) + ")");
                                            tabPane.getTabs().add(t);
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Execution.fxml"));
                                            Parent root;
                                            try {
                                                ExecutionController controller = new ExecutionController();

                                                loader.setController(controller);
                                                root = (Parent) loader.load();
                                                t.setContent(root);
                                                t.setClosable(true);
                                                tabPane.getSelectionModel().select(t);
                                                controller.init(ch.getChromosome(), baseAgentSetup, globalSetup, fitnessSetup, tabPane);
                                                Thread thread = new Thread(controller);
                                                thread.start();
                                            } catch (IOException ex) {
                                                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }

                                    });
                                } else {
                                    setGraphic(null);
                                }
                            }
                        };
                    }
                });

        GATable.setItems(o);
        GAGraph.getData().add(seriesMax);
        GAGraph.getData().add(seriesAvg);
        GAGraph.setCreateSymbols(false);
        GAGraph.getXAxis().setLabel("Generation");
        GAGraph.getYAxis().setLabel("Fitness");
    }

    @Override
    public void run() {

        l = new Launcher(this, globalSetup, baseAgentSetup, gASetup, fitnessSetup);

        try {
            l.start();
        } catch (Exception e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    MadeLogger.getInstance().error("Couldn't launch the experiment", e);
                }
            });
        }

        /*
         float fitness = 4.67f;

         for (int i = 0; i < 50; i++) {
         fitness += r.nextFloat();
         final Float finalF = fitness;
         final Integer finalI = i;
         Platform.runLater(new Runnable() {
         @Override
         public void run() {
         GAExecutionLine l = new GAExecutionLine(finalI, finalF, finalF - 1);
         o.add(l);
         seriesMax.getData().add(new XYChart.Data(finalI, finalF));
         seriesAvg.getData().add(new XYChart.Data(finalI, finalF - 1));
         }
         });
         try {
         synchronized (this) {
         this.wait(2000);
         }
         } catch (InterruptedException ex) {
         Logger.getLogger(GAExecutionController.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         */
    }

    @Override
    public void start() {
        // TODO
    }

    @Override
    public void end() {
        // TODO
    }

    @Override
    public void progress(float value) {
        theProgress = value;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progress.setProgress(value);
            }
        });
    }

    @Override
    public void generation(int id, float fitnessMax, float fitnessAVG, ArrayList individual) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GAExecutionLine l = new GAExecutionLine(id, fitnessMax, fitnessAVG, individual);
                o.add(l);
                seriesMax.getData().add(new XYChart.Data(id, fitnessMax));
                seriesAvg.getData().add(new XYChart.Data(id, fitnessAVG));
                
                saveLogToFile(l);
            }

            private void saveLogToFile(GAExecutionLine l) {
                FileWriter out = null;
                try {
                    File f = new File (filename);
                    File parent = f.getParentFile();
                    if(!parent.exists() && !parent.mkdirs()){
                        throw new IllegalStateException("Couldn't create dir: " + parent);
                    }
                    String text = l.getId() + ";" + l.getMaxFitness() + ";" + l.getAvgFitness() + "\n";
                    out = new FileWriter(f,true);
                    out.append(text);
                    out.close();
                } catch (Exception ex) {
                    Logger.getLogger(GAExecutionController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex) {
                            Logger.getLogger(GAExecutionController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void failure(Exception e) {
        // TOOD
    }

    @Override
    public void log(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String content = GAShell.getText();
                content += text + "\n";
                GAShell.setText(content);
            }
        });
    }

    @Override
    public float getProgress() {
        return theProgress;
    }

    @FXML
    private void handleExportTableAction(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file (*.csv)", "*.csv"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All", "*"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

            fileChooser.setInitialFileName("made_execution_"
                    + globalSetup.getNumberOfProfiles() + "_"
                    + sdf.format(new Date()) + ".csv");

            File f = fileChooser.showSaveDialog(null);
            if (f != null) {
                String text = "Gen.;Fitness(Best);Fitness(AVG)\n";
                for (Object obj : o.toArray()) {
                    GAExecutionLine l = (GAExecutionLine) obj;
                    text += l.getId() + ";" + l.getMaxFitness() + ";" + l.getAvgFitness() + "\n";
                }
                PrintWriter out = new PrintWriter(f);
                out.println(text);
                out.close();
            }
        } catch (IOException ex) {
            MadeLogger.getInstance().error("Couldn't store csv in the target file", ex);
        }
    }

    @FXML
    private void handleExportConsoleAction(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Log file (*.log)", "*.log"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All", "*"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

            fileChooser.setInitialFileName("made_execution_"
                    + globalSetup.getNumberOfProfiles() + "_"
                    + sdf.format(new Date()) + ".log");

            File f = fileChooser.showSaveDialog(null);
            if (f != null) {
                String text = GAShell.getText();
                PrintWriter out = new PrintWriter(f);
                out.println(text);
                out.close();
            }
        } catch (IOException ex) {
            MadeLogger.getInstance().error("Couldn't store csv in the target file", ex);
        }
    }

    @FXML
    private void handleExportGraphAction(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG file (*.png)", "*.png"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All", "*"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

            fileChooser.setInitialFileName("made_execution_"
                    + globalSetup.getNumberOfProfiles() + "_"
                    + sdf.format(new Date()) + ".png");

            File f = fileChooser.showSaveDialog(null);
            if (f != null) {
                SnapshotParameters params = new SnapshotParameters();

                WritableImage image = GAGraph.snapshot(new SnapshotParameters(), null);
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", f);
            }
        } catch (IOException ex) {
            MadeLogger.getInstance().error("Couldn't store csv in the target file", ex);
        }
    }

    @FXML
    private void handleCloseTabAction(ActionEvent event) {
    }

    @Override
    public void environmentExecuted(ArrayList<MadeAgentInterface> agents) {
        // nothing to do
    }

    @FXML
    private void handleStop(ActionEvent event) {
        l.friendlyStop();
        MadeLogger.getInstance().info("Current execution will eventually stop :-)");
        
    }
    
}
