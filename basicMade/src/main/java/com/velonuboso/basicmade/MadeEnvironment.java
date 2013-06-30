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
package com.velonuboso.basicmade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import org.jgap.IChromosome;

/**
 *
 * @author Ruben
 */
public class MadeEnvironment {

    
    private int numberOfProfiles;
    private int mapDimension;
    private int food;
    private int days;

    private int[][] mapAgents = null;
    private int[][] mapFood = null;
    IChromosome iChromosome = null;
    int counter;
    ArrayList<MadeAgent> agents = new ArrayList<MadeAgent>();
    ArrayList<MadeAgent> aliveAgents = new ArrayList<MadeAgent>();
    Random r;
    int currDate;
    boolean log = false;

    public MadeEnvironment(IChromosome c) {

        MadeEvaluator e = MadeEvaluator.getInstance();
        numberOfProfiles = e.getProperty(e.NUMBER_OF_PROFILES);
        mapDimension = e.getProperty(e.MAP_DIMENSION);
        food = e.getProperty(e.FOOD);
        days = e.getProperty(e.DAYS);


        this.iChromosome = c;
        mapAgents = new int[mapDimension][mapDimension];
        for (int x = 0; x < mapDimension; x++) {
            for (int y = 0; y < mapDimension; y++) {
                mapAgents[x][y] = -1;
            }
        }
        mapFood = new int[mapDimension][mapDimension];
        for (int x = 0; x < mapDimension; x++) {
            for (int y = 0; y < mapDimension; y++) {
                mapFood[x][y] = 0;
            }
        }
        counter = 0;
        r = new Random();
    }

    public double getVal(int profile, int position) {
        if (profile > numberOfProfiles || position > MadeAgent.NUMBER_OF_FEATURES) {
            throw new IndexOutOfBoundsException();
        }
        int p = (profile * MadeAgent.NUMBER_OF_FEATURES) + position;
        return (Double) iChromosome.getGene(p).getAllele();
    }

    public double runEnvironment(boolean log) {

        currDate = 0;
        this.log = log;

        for (int i = 0; i < numberOfProfiles * 2; i++) {
            Gender g = i % 2 == 0 ? Gender.MALE : Gender.FEMALE;
            String name = RatNameHelper.getInstance().getRandomName(r, g);
            String surname = RatNameHelper.getInstance().getRandomSurname(r);
            String nickname = RatNameHelper.getInstance().getRandomNickname(r);
            MadeAgent a = new MadeAgent(counter, currDate, g, i / 2, name, surname, nickname, this, r, log);
            agents.add(a);
            aliveAgents.add(a);
            int x, y;
            do {
                x = r.nextInt(mapDimension);
                y = r.nextInt(mapDimension);
            } while (mapAgents[x][y] != -1);
            mapAgents[x][y] = counter;
            a.setX(x);
            a.setY(y);
            counter++;
        }

        for (currDate = 0; currDate < days; currDate++) {

            // place some food
            for (int j = 0; j < food; j++) {
                int x = r.nextInt(mapDimension);
                int y = r.nextInt(mapDimension);
                mapFood[x][y]++;
            }

            // shuffle the alive agents and play
            ArrayList<MadeAgent> tempAliveAgents = new ArrayList<MadeAgent>();
            for (int i = 0; i < aliveAgents.size(); i++) {
                tempAliveAgents.add(aliveAgents.get(i));
            }

            Collections.shuffle(tempAliveAgents, r);
            for (int j = 0; j < tempAliveAgents.size(); j++) {
                MadeAgent a = tempAliveAgents.get(j);
                if (a.isAlive()) {
                    a.justLive();
                }
            }

            // remove dead agents
            for (int x = 0; x < mapDimension; x++) {
                for (int y = 0; y < mapDimension; y++) {
                    if (mapAgents[x][y] != -1 && !agents.get(mapAgents[x][y]).isAlive()) {
                        agents.get(mapAgents[x][y]).setPosition(Position.NULL_POSITION);
                        aliveAgents.remove(agents.get(mapAgents[x][y]));
                        mapAgents[x][y] = -1;
                    }
                }
            }
        }

        if (log) {
            for (int i = 0; i < agents.size(); i++) {
                System.out.println(agents.get(i).getSheet());
                System.out.println(agents.get(i).getStringLog());
            }
        }

        // evaluate
        return MadeEvaluator.getInstance().getFitness(agents);
    }

    Position findFreeFood(MadeAgent source, int smell) {
        Position pos = source.getPosition();

        Position ret = new Position();

        if (mapFood[pos.x][pos.y] > 0) {
            ret.x = pos.x;
            ret.y = pos.y;
            return pos;
        } else {
            int px0 = pos.x - smell;
            if (px0 < 0) {
                px0 = 0;
            }
            int px1 = pos.x + smell;
            if (px1 > mapDimension - 1) {
                px1 = mapDimension;
            }
            int py0 = pos.y - smell;
            if (py0 < 0) {
                py0 = 0;
            }
            int py1 = pos.y + smell;
            if (py1 > mapDimension - 1) {
                py1 = mapDimension;
            }


            for (int x = px0; x < px1; x++) {
                for (int y = py0; y < py1; y++) {
                    if (mapAgents[x][y] == -1 && mapFood[x][y] > 0) {
                        ret.x = x;
                        ret.y = y;
                        return ret;
                    }
                }
            }

            return null;
        }
    }

    Position getPosition(MadeAgent source) {
        return source.getPosition();
    }

    void moveAgent(MadeAgent source, Position t) {
        Position p = source.getPosition();
        mapAgents[p.x][p.y] = -1;
        mapAgents[t.x][t.y] = source.getId();

        source.setX(t.x);
        source.setY(t.y);
    }

    void eatFood(Position p) {
        mapFood[p.x][p.y]--;
    }

    Position findFoodWithAgent(MadeAgent source, int smell) {
        Position pos = source.getPosition();

        Position ret = new Position();

        int px0 = pos.x - smell;
        if (px0 < 0) {
            px0 = 0;
        }
        int px1 = pos.x + smell;
        if (px1 > mapDimension - 1) {
            px1 = mapDimension;
        }
        int py0 = pos.y - smell;
        if (py0 < 0) {
            py0 = 0;
        }
        int py1 = pos.y + smell;
        if (py1 > mapDimension - 1) {
            py1 = mapDimension;
        }


        for (int x = px0; x < px1; x++) {
            for (int y = py0; y < py1; y++) {
                if (mapAgents[x][y] != -1 && mapFood[x][y] > 0) {
                    ret.x = x;
                    ret.y = y;
                    return ret;
                }
            }
        }

        return null;
    }

    MadeAgent getAgent(Position p) {
        return agents.get(mapAgents[p.x][p.y]);
    }

    Position getFreePosition(MadeAgent source, int smell) {
        ArrayList<Position> positions = getFreePositions(source, smell);
        if (positions.size() == 0) {
            return null;
        } else {
            if (positions.size() == 1) {
                return positions.get(0);
            } else {
                return positions.get(r.nextInt(positions.size()));
            }
        }
    }

    ArrayList<Position> getFreePositions(MadeAgent source, int smell) {

        Position pos = source.getPosition();

        ArrayList<Position> ret = new ArrayList<Position>();

        int px0 = pos.x - smell;
        if (px0 < 0) {
            px0 = 0;
        }
        int px1 = pos.x + smell;
        if (px1 > mapDimension - 1) {
            px1 = mapDimension;
        }
        int py0 = pos.y - smell;
        if (py0 < 0) {
            py0 = 0;
        }
        int py1 = pos.y + smell;
        if (py1 > mapDimension - 1) {
            py1 = mapDimension;
        }


        for (int x = px0; x < px1; x++) {
            for (int y = py0; y < py1; y++) {
                if (mapAgents[x][y] == -1) {
                    Position p = new Position(x, y);
                    ret.add(p);
                }
            }
        }
        return ret;

    }

    ArrayList<MadeAgent> getAgentsAround(MadeAgent source, int smell) {

        Position pos = source.getPosition();

        ArrayList<MadeAgent> ret = new ArrayList<MadeAgent>();

        int px0 = pos.x - smell;
        if (px0 < 0) {
            px0 = 0;
        }
        int px1 = pos.x + smell;
        if (px1 > mapDimension - 1) {
            px1 = mapDimension;
        }
        int py0 = pos.y - smell;
        if (py0 < 0) {
            py0 = 0;
        }
        int py1 = pos.y + smell;
        if (py1 > mapDimension - 1) {
            py1 = mapDimension;
        }


        for (int x = px0; x < px1; x++) {
            for (int y = py0; y < py1; y++) {
                if (mapAgents[x][y] != -1 && x != pos.x && y != pos.y) {
                    ret.add(agents.get(mapAgents[x][y]));
                }
            }
        }

        return ret;
    }

    void newAgents(MadeAgent aThis, MadeAgent inLoveWith, int numberOfChildren) {
        ArrayList<Position> p = getFreePositions(aThis, 3);
        for (int i = 0; i < p.size(); i++) {
            Position pos = p.get(i);
            Gender g = i % 2 == 0 ? Gender.MALE : Gender.FEMALE;
            String name = RatNameHelper.getInstance().getRandomName(r, g);
            String surname = aThis.getSurname();
            String nickname = inLoveWith.getNickname();

            int profile = -1;
            int val = r.nextInt(3);
            switch (val) {
                case 0:
                    profile = aThis.getProfile();
                    break;
                case 1:
                    profile = inLoveWith.getProfile();
                    break;
                default:
                    profile = r.nextInt(numberOfProfiles);
            }

            MadeAgent a = new MadeAgent(counter, currDate, g, profile, name, surname, nickname, this, r, log);
            agents.add(a);
            aliveAgents.add(a);

            mapAgents[pos.x][pos.y] = counter;
            a.setX(pos.x);
            a.setY(pos.y);
            counter++;
        }
    }

    public String getSummary() {
        StringBuffer str = new StringBuffer();

        HashMap<String, ArrayList<MadeAgent>> labels = new HashMap<String, ArrayList<MadeAgent>>();

        int total = 0;
        int alive = 0;
        int ageMax = Integer.MIN_VALUE;
        int ageMin = Integer.MAX_VALUE;
        int ageAverage = 0;

        for (int i = 0; i < agents.size(); i++) {
            MadeAgent ma = agents.get(i);
            HashSet<String> agentLabels = ma.getLabels();
            for (String label : agentLabels) {
                if (labels.get(label) == null) {
                    labels.put(label, new ArrayList<MadeAgent>());
                }
                labels.get(label).add(ma);
            }
            total++;
            if (ma.isAlive()) {
                alive++;
            }
            if (ma.getDays() > ageMax) {
                ageMax = ma.getDays();
            }
            if (ma.getDays() < ageMin) {
                ageMin = ma.getDays();
            }
            ageAverage = ((ageAverage * i) + ma.getDays()) / (i + 1);
        }

        str.append("World\n");
        str.append("\tTotal agents: " + total + "\n");
        str.append("\tAlive agents: " + alive + "/" + total + "\n");
        str.append("\tWorld age: " + days + "\n");
        str.append("\tDimension: " + mapDimension + "\n");
        str.append("\tFood per day: " + food + "\n");
        str.append("\tProfiles: " + numberOfProfiles + "\n");
        str.append("\tMax Age: " + ageMax + "\n");
        str.append("\tMin Age: " + ageMin + "\n");
        str.append("\tAverage Age: " + ageAverage + "\n");

        str.append("Labels\n");

        for (String label : labels.keySet()) {
            str.append("\t" + label + ": " + labels.get(label).size() + "\n");
        }


        return str.toString();
    }
}
