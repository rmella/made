/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.velonuboso.environment.rats;

import com.velonuboso.made.core.BasicRunner;
import com.velonuboso.made.core.Gender;
import com.velonuboso.made.core.MadeAgent;
import com.velonuboso.made.core.MadeState;
import com.velonuboso.made.core.Position;
import com.velonuboso.made.core.interfaces.Environment;
import com.velonuboso.made.core.interfaces.Feature;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * What do magic rats usually do? - Born - Eat, when possible - Have children,
 * when possible - Gossip about other rats - Wear bowler, but never use a suit -
 * eventually die - and maybe, even more things
 * 
 * @author Ruben Hector Garcia Ortega <raiben@gmail.com>
 */
public class InvisibleUniversityRatsEnvironment implements Environment {

    // constants
    private static final int PARAM_SIZE = 100;
    private static final int mapWidth = 50;
    private static final int mapHeight = 50;
    private static final String FEATURE_STRENGTH = "strength";
    private static final String FEATURE_DEXTRESITY = "dextresity";
    private static final String FEATURE_INTELLIGENCE = "intelligence";
    private static final String FEATURE_HEALTH = "health";
    // variables
    private float[] params = null;
    private HashSet<MadeAgent>[][] map;
    private HashMap<MadeAgent, Position> agents;
    EnvironmentVariables vars = null;

    public static void main(String[] args) {
        float[] params = new float[PARAM_SIZE];

        InvisibleUniversityRatsEnvironment env = new InvisibleUniversityRatsEnvironment(params);

        // TESTS
        Random r = new Random();
        BasicRunner runner = new BasicRunner(env, true, new Date("1800/01/01"), new Date(), 0.0001f);
        runner.start();
    }

    public InvisibleUniversityRatsEnvironment(float[] params) {
        this.params = params;
        map = new HashSet[mapWidth][mapHeight];
        
        vars = new EnvironmentVariables(params);
        
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapWidth; j++) {
                map[i][j] = new HashSet<MadeAgent>();
            }
        }
        agents = new HashMap<MadeAgent, Position>();

    }

    public List<MadeAgent> getNeighborhood(Position pos) {
        
        int neighborhoodSize = (int)(10*vars.getVal(EnvironmentVariables.IND_NEIGHBORHOOD_CELLS));
        
        int x0 = (int) (pos.getX() - neighborhoodSize < 0
                ? 0
                : pos.getX() - neighborhoodSize);
        int x1 = (int) (pos.getX() + neighborhoodSize > neighborhoodSize - 1
                ? neighborhoodSize - 1
                : pos.getX() + neighborhoodSize);
        int y0 = (int) (pos.getY() - neighborhoodSize < 0
                ? 0
                : pos.getY() - neighborhoodSize);
        int y1 = (int) (pos.getY() + neighborhoodSize > neighborhoodSize - 1
                ? neighborhoodSize - 1
                : pos.getY() + neighborhoodSize);

        ArrayList<MadeAgent> ret = new ArrayList<MadeAgent>();
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                ret.addAll(map[x][y]);
            }
        }
        return ret;
    }

    public Position getPosition(MadeAgent agent) {
        return agents.get(agent);
    }

    public void setPosition(MadeAgent agent, Position pos) {
        Position pos2 = agents.get(agent);
        if (pos2 != null) {
            map[(int) pos2.getX()][(int) pos2.getY()].remove(agent);
        }
        map[(int) pos.getX()][(int) pos.getY()].add(agent);
        agents.put(agent, pos);
    }

    public String getRandomName(Random r, Gender g) {
        return RatNameHelper.getInstance().getRandomName(r, g);
    }

    public String getRandomSurname(Random r) {
        return RatNameHelper.getInstance().getRandomSurname(r);
    }

    public String getRandomNickname(Random r) {
        return RatNameHelper.getInstance().getRandomNickname(r);
    }

    public Gender getRandomGender(Random random) {
        return Gender.values()[random.nextInt(2)];
    }

    public List<Feature> getRandomFeatures(Random random) {
        ArrayList<Feature> ret = new ArrayList<Feature>();
        
        ret.add(new BasicFeature(random, FEATURE_STRENGTH));
        ret.add(new BasicFeature(random, FEATURE_DEXTRESITY));
        ret.add(new BasicFeature(random, FEATURE_INTELLIGENCE));
        ret.add(new BasicFeature(random, FEATURE_HEALTH));
        
        return ret;
    }

    public List<MadeState> getRandomStates(Random random) {
        // TODO
        //MadeState st = new MadeState(env, FEATURE_HEALTH, true, true, null, FEATURE_HEALTH, null)
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DateFormat getDateFormat() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getString(String str) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getNextInteger() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getPreferredName(MadeAgent parent1, MadeAgent parent2, String proposedName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Feature> getMixedFeatures(MadeAgent parent1, MadeAgent parent2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<MadeState> getInitialStates(Random random) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setRandomPosition(Random random, MadeAgent aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void initFictionalDate(Date fictional, Date real, float speedfactor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Date getFictionalDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void waitFictional(MadeAgent ma) throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String printSheet(MadeAgent aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void calculateNewStates(MadeAgent aThis, Random random) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public MadeState getNextState(MadeState aThis, MadeAgent source, MadeAgent related) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getNumberOfInitialAgents() {
        return (int)(50f*vars.getVal(EnvironmentVariables.IND_NUMBER_OF_INITIAL_AGENTS));
    }
}
