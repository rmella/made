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

package com.velonuboso.made2.ga;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author rhgarcia
 */
public class EvolutionListener {

    private static final String LOG_DIR = "log";
    private String name;

    public EvolutionListener(String name) {
        this.name = name;
    }

    public void logIteration(GaIndividual g, double avg, int iteration) {
        double f = g.getFitness().getTotal();
        
        /*
        String chr = "[";
        for (int i = 0; i < g.getChromosomes().length; i++) {
            chr += g.getChromosomes()[i];
            if (i != g.getChromosomes().length - 1) {
                chr += ",";
            }
        }
        chr += "]";
        */
        
        String[] comN = g.getFitness().getComponentsNamesInOrder();
        
        if (iteration == 0){
            String header = "#     best fitness         average fitness      ";  
            for (String arch:comN){
                String str = new String(arch);
                while(str.length() < 20) {
                    str += " ";
                }
                if (str.length() > 20){
                    str = str.substring(0, 20);
                }
                header+=str+" ";
            }
            writeToLog(header);
        }
        
        String line = String.format("%05d", iteration) + " " + 
                String.format("%.18f", f) + " " +
                String.format("%.18f", avg) + " ";
        for (String arch:comN){
            double d = g.getFitness().getPartialFitnessValue(arch);
            line+=String.format("%.18f", d) + " ";
        }
        writeToLog(line);
    }

    public void logTime(long t0, long t1) {
        long ms = t1 - t0;
        System.out.println("Time: " + ms);
        writeToLog("Time: " + ms);
    }

    private void writeToLog(String text) {
        System.out.println(text);
        File logDir = new File(LOG_DIR);
        if (!logDir.exists()){
            logDir.mkdir();
        }
        BufferedWriter out = null;
        try {
            FileWriter fstream = new FileWriter(LOG_DIR+File.separator+name, true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write("\n"+text);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
            }
        }
    }

}
