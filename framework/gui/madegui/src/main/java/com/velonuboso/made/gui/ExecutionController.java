/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made.gui;

import com.velonuboso.made.core.Launcher;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;

/**
 *
 * @author rhgarcia
 */
public class ExecutionController implements Initializable, Runnable, ExecutionListenerInterface {
    @FXML
    private TextArea individual;
    @FXML
    private BarChart<?, ?> individualGraph;

    @FXML
    private TextArea console;
    private IChromosome chromosome;
    
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
            Configuration conf = new DefaultConfiguration();
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
            
            LauncherThread thread = new LauncherThread(this, globalSetup, null, fitnessSetup, baseAgentSetup, chromosome);
            thread.start();
            
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(ExecutionController.class.getName()).log(Level.SEVERE, null, ex);
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
    
        StringBuffer strb = new StringBuffer();
        for (MadeAgentInterface m : agents){
            RatAgent agent = (RatAgent) m;
            strb.append(agent.getSheet()+"\n");
            strb.append(agent.getStringLog()+"\n");
        }
        
        final String content = strb.toString();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                console.setText(content);
            }
        });
        
    }
    
    
    private class LauncherThread extends Thread{

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

}
