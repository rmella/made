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
import com.velonuboso.made.core.common.Gender;
import com.velonuboso.made.core.common.Helper;
import com.velonuboso.made.core.interfaces.ExecutionListenerInterface;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.rat.RatAgent;
import com.velonuboso.made.core.setup.BaseAgentSetup;
import com.velonuboso.made.core.setup.FitnessSetup;
import com.velonuboso.made.core.setup.GASetup;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;

/**
 *
 * @author raiben@gmail.com
 */
public class ExecutionController implements Initializable, Runnable, ExecutionListenerInterface {

    @FXML
    private TextArea individual;
    @FXML
    private BarChart<?, ?> individualGraph;
    @FXML
    private Label console;

    @FXML
    private TableColumn columnId;
    @FXML
    private TableColumn columnName;
    @FXML
    private TableColumn columnAge;
    @FXML
    private TableColumn columnGender;
    @FXML
    private TableColumn columnArchetype;
    @FXML
    private TableColumn columnAlive;

    @FXML
    private TableView tableAgents;
    @FXML
    private Label labelTableSummary;

    
    @FXML
    private Label sheetName;
    
    @FXML
    private Label sheetProfile;
    
    @FXML
    private Label sheetLog;
    
    @FXML
    private ImageView sheetImage;
    
    private IChromosome chromosome;

    private String log = null;
    private ObservableList<MadeAgentInterface> olist = FXCollections.observableArrayList();
    private ArrayList<MadeAgentInterface> agents;

    
    public ExecutionController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 
    }

    public void init(ArrayList<Double> chr, BaseAgentSetup baseAgentSetup,
            GlobalSetup globalSetup, FitnessSetup fitnessSetup,
            TabPane tabPane) {
        try {
            Configuration conf = new DefaultConfiguration(Long.toString(System.currentTimeMillis()),"");
            Gene[] genes = new Gene[chr.size()];

            for (int k = 0; k < chr.size(); k++) {
                genes[k] = new DoubleGene(conf, 0, 1);
                genes[k].setAllele(chr.get(k));

            }
            chromosome = new Chromosome(conf, genes);

            String txt = Helper.chromosomeToString(chromosome);
            individual.setText(txt);

            XYChart.Series series1 = new XYChart.Series();

            /*
             private static final int FEATURE_BITE = 0; // attack
             private static final int FEATURE_FUR = 1; // defense
             //private static final int FEATURE_PROFILE_VARIANCE = 2;
             private static final int FEATURE_HEALTH = 2;
             private static final int FEATURE_LIFE = 3;
             private static final int FEATURE_SMELL = 4;
             private static final int FEATURE_METHABOLISM = 5;
             private static final int FEATURE_HUNGRY_LEVEL = 6;
             private static final int FEATURE_PROCREATION = 7;
             private static final int FEATURE_ENJOYABLE = 8;
             private static final int FEATURE_AGE_TO_BE_ADULT = 9;
             private static final int FEATURE_PREGNANCY_TIME = 10;
             private static final int FEATURE_KINDNESS = 11;
             */
            // TODO allow many profiles
            series1.getData().add(new XYChart.Data("BITE", chr.get(0)));
            series1.getData().add(new XYChart.Data("FUR", chr.get(1)));
            series1.getData().add(new XYChart.Data("HEALTH", chr.get(2)));
            series1.getData().add(new XYChart.Data("LIFE", chr.get(3)));
            series1.getData().add(new XYChart.Data("SMELL", chr.get(4)));
            series1.getData().add(new XYChart.Data("METHAB.", chr.get(5)));
            series1.getData().add(new XYChart.Data("HUNGRY", chr.get(6)));
            series1.getData().add(new XYChart.Data("PROCR.", chr.get(7)));
            series1.getData().add(new XYChart.Data("ENJOYABLE", chr.get(8)));
            series1.getData().add(new XYChart.Data("ADULT", chr.get(9)));
            series1.getData().add(new XYChart.Data("PREGNANCY", chr.get(10)));
            series1.getData().add(new XYChart.Data("KINDNESS", chr.get(11)));

            individualGraph.getData().addAll(series1);
            individualGraph.getXAxis().setLabel("Features");
            individualGraph.getYAxis().setLabel("Profile level");

            columnId.setCellValueFactory(new PropertyValueFactory<MadeAgentInterface, Integer>("id"));
            columnAge.setCellValueFactory(new PropertyValueFactory<MadeAgentInterface, Integer>("days"));
            columnArchetype.setCellValueFactory(new PropertyValueFactory<MadeAgentInterface, Integer>("labelsAsString"));
            columnGender.setCellValueFactory(new PropertyValueFactory<MadeAgentInterface, Gender>("gender"));
            columnName.setCellValueFactory(new PropertyValueFactory<MadeAgentInterface, Integer>("fullName"));
            columnAlive.setCellValueFactory(new PropertyValueFactory<MadeAgentInterface, Boolean>("alive"));
            
            sheetImage.setBlendMode(BlendMode.MULTIPLY);
            sheetLog.autosize();
            
            LauncherThread thread = new LauncherThread(this, globalSetup, null, fitnessSetup, baseAgentSetup, chromosome);
            thread.start();

        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(ExecutionController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
    }

    @Override
    public void start() {
    }

    @Override
    public void end() {
    }

    @Override
    public void progress(float value) {
    }

    @Override
    public void generation(int id, float fitnessMax, float fitnessAVG, ArrayList individual) {
    }

    @Override
    public void failure(Exception e) {
    }

    @Override
    public void log(String text) {
    }

    @Override
    public float getProgress() {
        return 0;
    }

    @Override
    public void environmentExecuted(ArrayList<MadeAgentInterface> agents) {
        this.agents = agents;

        StringBuffer strb = new StringBuffer();
        for (MadeAgentInterface m : agents) {
            RatAgent agent = (RatAgent) m;
            strb.append(agent.getSheet() + "\n");
            strb.append(agent.getStringLog() + "\n");
        }

        log = strb.toString();
        olist.clear();
        olist.addAll(agents);
        tableAgents.setItems(olist);
        tableAgents.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                RatAgent a = (RatAgent) tableAgents.getSelectionModel().getSelectedItem();
                if (a!=null){
                    System.out.println(a);
                    System.out.println(sheetName);
                    System.out.println(sheetProfile);
                    System.out.println(sheetLog);
                    
                    sheetName.setText(a.getFullName());
                    sheetProfile.setText(a.getSheet());
                    sheetLog.setText(a.getStringLog());
                }
            }
        });
    }

    private class LauncherThread extends Thread {

        private ExecutionListenerInterface i;
        private GlobalSetup gs;
        private GASetup ga;
        private FitnessSetup fs;
        private BaseAgentSetup bs;
        private IChromosome ic;

        public LauncherThread(ExecutionListenerInterface i, GlobalSetup gs, GASetup ga, FitnessSetup fs, BaseAgentSetup bs, IChromosome ic) {
            this.i = i;
            this.gs = gs;
            this.ga = ga;
            this.fs = fs;
            this.bs = bs;
            this.ic = ic;
        }

        @Override
        public void run() {
            super.run(); //To change body of generated methods, choose Tools | Templates.
            Launcher l = new Launcher(i, gs, bs, ga, fs);
            l.launch(chromosome);
        }

    }

    @FXML
    private void export(ActionEvent event) {
        // TODO

    }
}
