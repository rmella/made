/*
 * Copyright 2014 Rubén Héctor García <raiben@gmail.com>.
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
package com.velonuboso.made2.core;

import com.velonuboso.made2.core.common.Helper;
import com.velonuboso.made2.core.setup.FitnessSetup;
import com.velonuboso.made2.core.setup.GASetup;
import com.velonuboso.made2.core.setup.BaseAgentSetup;
import com.velonuboso.made2.core.setup.GlobalSetup;
import com.velonuboso.made2.core.interfaces.ExecutionListenerInterface;
import com.velonuboso.made2.core.rat.RatAgent;
import com.velonuboso.made2.core.rat.RatEnvironment;
import com.velonuboso.made2.core.rat.RatEvaluator;
import com.velonuboso.made2.ga.EvolutionListener;
import com.velonuboso.made2.ga.GaIndividual;
import com.velonuboso.made2.ga.GaPopulation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raiben@gmail.com
 */
public class Launcher extends Thread {

    private ExecutionListenerInterface listener;
    private GlobalSetup globalSetup;
    private BaseAgentSetup baseAgentSetup;
    private GASetup gaSetup;
    private FitnessSetup fitnessSetup;
    private Random r = new Random();
    private RatEvaluator evaluator = null;
    private String filename = "";

    public Launcher(ExecutionListenerInterface listener,
            GlobalSetup globalSetup,
            BaseAgentSetup baseAgentSetup,
            GASetup gaSetup,
            FitnessSetup fitnessSetup,
            String filename) {
        this.filename = filename;
        this.listener = listener;
        this.globalSetup = globalSetup;
        this.baseAgentSetup = baseAgentSetup;
        this.gaSetup = gaSetup;
        this.fitnessSetup = fitnessSetup;
        this.evaluator = new RatEvaluator(fitnessSetup, globalSetup);
    }

    @Override
    public void run() {
        launch();
    }

    public void launch() {

        long t0 = System.currentTimeMillis();

        Random r = new Random();
        GaPopulation pop = new GaPopulation(
                r, gaSetup.getTxtPopulation(),
                globalSetup.getNumberOfProfiles() * RatAgent.NUMBER_OF_FEATURES,
                gaSetup.getTxtCrossoverProbability(),
                gaSetup.getTxtMutationProbability(),
                evaluator,
                globalSetup,
                gaSetup,
                baseAgentSetup);
        EvolutionListener l = new EvolutionListener(filename);

        pop.executeGA(l, listener, gaSetup.getTxtStopCriteriarelativeDifference(),
                gaSetup.getTxtStopCriteriaPeriod());

        long t1 = System.currentTimeMillis();
        l.logTime(t0, t1);

    }

    public void launch(GaIndividual iChromosome) {
        RatEnvironment env = new RatEnvironment(iChromosome, baseAgentSetup, globalSetup, new Random(), listener, evaluator);
        env.runEnvironment(false, false);
        listener.environmentExecuted(env.getAgents());
    }

    public static void main(String[] args) {

        String run = "";

        try {

            Properties prop = new Properties();

            if (args.length < 1) {
                System.out.println("Error: please use java -jar <executable.jar> <iteration> from<N> to<N>");
                System.exit(1);
            } else {
                InputStream in = ClassLoader.getSystemResourceAsStream("base.properties");
                prop.load(in);
                in.close();
                run = args[0];
                
            }
            try {

                int Es[] = {1, 2, 5}; // environment (1, 2, and 5 archetypes)
                int Ps[] = {1, 2, 4, 8}; // profiles
                int Ds[] = {128, 256, 512}; // virtual days
                int Ws[] = {5, 10, 20}; // World map dimension
                int Fs[] = {2, 4, 8}; // food per day: n_cells/2 , n_cells/4, n_cells/8
                int Ss[] = {256, 128, 64}; // GA's population

                for (int iE = 0; iE < Fs.length; iE++) {
                    int e = Es[iE];
                    if (e == 1) {
                        prop.setProperty("archetype.Villain.check", "true");
                        prop.setProperty("archetype.Villain.param", "2");
                        prop.setProperty("archetype.Hero.check", "false");
                        prop.setProperty("archetype.Hero.param", "1");
                        prop.setProperty("archetype.Mentor.check", "false");
                        prop.setProperty("archetype.Mentor.param", "1");
                        prop.setProperty("archetype.ThresholdGuardian.check", "false");
                        prop.setProperty("archetype.ThresholdGuardian.param", "1");
                        prop.setProperty("archetype.Avenger.check", "false");
                        prop.setProperty("archetype.Avenger.param", "1");
                    } else if (e == 2) {
                        prop.setProperty("archetype.Villain.check", "true");
                        prop.setProperty("archetype.Villain.param", "2");
                        prop.setProperty("archetype.Hero.check", "true");
                        prop.setProperty("archetype.Hero.param", "2");
                        prop.setProperty("archetype.Mentor.check", "false");
                        prop.setProperty("archetype.Mentor.param", "1");
                        prop.setProperty("archetype.ThresholdGuardian.check", "false");
                        prop.setProperty("archetype.ThresholdGuardian.param", "1");
                        prop.setProperty("archetype.Avenger.check", "false");
                        prop.setProperty("archetype.Avenger.param", "1");
                    } else if (e == 5) {
                        prop.setProperty("archetype.Villain.check", "true");
                        prop.setProperty("archetype.Villain.param", "2");
                        prop.setProperty("archetype.Hero.check", "true");
                        prop.setProperty("archetype.Hero.param", "2");
                        prop.setProperty("archetype.Mentor.check", "true");
                        prop.setProperty("archetype.Mentor.param", "2");
                        prop.setProperty("archetype.ThresholdGuardian.check", "true");
                        prop.setProperty("archetype.ThresholdGuardian.param", "2");
                        prop.setProperty("archetype.Avenger.check", "true");
                        prop.setProperty("archetype.Avenger.param", "2");
                    } else {
                        System.err.println("f not found: " + e);
                    }

                    for (int iP = 0; iP < Ps.length; iP++) {
                        int p = Ps[iP];
                        prop.setProperty("txtNumberOfProfiles", Integer.toString(p));

                        for (int iD = 0; iD < Ds.length; iD++) {
                            int d = Ds[iD];
                            prop.setProperty("txtNumberOfVirtualDays", Integer.toString(d));

                            for (int iW = 0; iW < Ws.length; iW++) {

                                int w = Ws[iW];
                                prop.setProperty("txtMapGridDimension", Integer.toString(w));

                                for (int iS = 0; iS < Ss.length; iS++) {
                                    int s = Ss[iS];
                                    prop.setProperty("txtPopulation", Integer.toString(s));

                                    for (int iF = 0; iF < Fs.length; iF++) {
                                        int f = Fs[iF];
                                        int f2 = w * w / f;
                                        prop.setProperty("txtFoodPerDay", Integer.toString(f2));

                                        String name = "run_" + run + "_E" + e + "_P" + p + "_D" + d + "_W" + w + "_F" + f + "_S" + s + ".log";

                                        FitnessSetup fitnessSetup = new FitnessSetup(prop);
                                        BaseAgentSetup baseAgentSetup = new BaseAgentSetup(prop);

                                        GASetup gASetup = new GASetup(prop);
                                        GlobalSetup globalSetup = new GlobalSetup(prop);

                                        Launcher l = new Launcher(new ExecutionListenerInterface() {

                                            public void start() {
                                            }

                                            public void end() {
                                            }

                                            public void progress(float value) {
                                            }

                                            public void generation(int id, float fitnessMax, float fitnessAVG, ArrayList individual) {
                                            }

                                            public void failure(Exception e) {
                                            }

                                            public void log(String text) {
                                                System.out.println(text);
                                            }

                                            public float getProgress() {
                                                return 0;
                                            }

                                            public void environmentExecuted(ArrayList<RatAgent> agents) {
                                            }
                                        }, globalSetup, baseAgentSetup, gASetup, fitnessSetup, name);

                                        l.run();
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            System.err.println("Could noy open the file " + args[0]);
        }

    }

}
