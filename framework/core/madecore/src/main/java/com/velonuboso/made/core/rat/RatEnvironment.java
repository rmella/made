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


package com.velonuboso.made.core.rat;

import com.velonuboso.made.core.interfaces.MadeEnvironmentInterface;
import com.velonuboso.made.core.common.Position;
import com.velonuboso.made.core.common.Gender;
import com.velonuboso.made.core.common.Gender;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.interfaces.MadeEnvironmentInterface;
import com.velonuboso.made.core.common.Position;
import com.velonuboso.made.core.interfaces.ExecutionListenerInterface;
import com.velonuboso.made.core.interfaces.MadeEvaluatorInterface;
import com.velonuboso.made.core.setup.BaseAgentSetup;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jgap.IChromosome;

/**
 *
 * @author raiben@gmail.com
 */
public class RatEnvironment implements MadeEnvironmentInterface {

    private int numberOfProfiles;
    private int numberOfInitialAgents;
    private int mapDimension;
    private int food;
    private int days;
    private int[][] mapAgents = null;
    private int[][] mapFood = null;
    private IChromosome iChromosome = null;
    private int counter;
    private ArrayList<MadeAgentInterface> agents = new ArrayList<MadeAgentInterface>();
    private ArrayList<RatAgent> aliveAgents = new ArrayList<RatAgent>();
    private Random r;
    private int currDate;
    private boolean log = false;
    private boolean graph = false;
    private BaseAgentSetup baseAgentSetup;
    private GlobalSetup globalSetup;
    private ExecutionListenerInterface logger = null;
    private MadeEvaluatorInterface evaluator;

    public RatEnvironment(IChromosome c, BaseAgentSetup baseAgentSetup,
            GlobalSetup globalSetup, Random rand,
            ExecutionListenerInterface logger,
            MadeEvaluatorInterface eval) {

        this.logger = logger;
        this.r = rand;
        this.evaluator = eval;

        this.globalSetup = globalSetup;
        this.baseAgentSetup = baseAgentSetup;

        numberOfProfiles = globalSetup.getNumberOfProfiles();
        numberOfInitialAgents = globalSetup.getNumberOfInitialAgents();
        mapDimension = globalSetup.getMapGridDimension();
        food = globalSetup.getFoodPerDay();
        days = globalSetup.getNumberOfVirtualDays();

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
    }

    @Override
    public double getVal(int profile, int position) {
        if (profile > numberOfProfiles || position > RatAgent.NUMBER_OF_FEATURES) {
            throw new IndexOutOfBoundsException();
        }
        int p = (profile * RatAgent.NUMBER_OF_FEATURES) + position;
        return (Double) iChromosome.getGene(p).getAllele();
    }

    @Override
    public double runEnvironment(boolean log, boolean graph) {

        currDate = 0;
        this.log = log;
        this.graph = graph;

        int currentProfile = 0;

        for (int i = 0; i < numberOfInitialAgents; i++) {

            Gender g = null;
            if (i % 2 == 0) {
                g = Gender.MALE;
            } else {
                g = Gender.FEMALE;
            }

            RatAgent a = new RatAgent(counter, currDate, g, currentProfile, this, r, log, logger);
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
            currentProfile = (currentProfile + 1) % numberOfProfiles;
        }

        for (currDate = 0; currDate < days; currDate++) {

            // place some food
            for (int j = 0; j < food; j++) {
                int x = r.nextInt(mapDimension);
                int y = r.nextInt(mapDimension);
                mapFood[x][y]++;
            }

            // shuffle the alive agents and play
            ArrayList<RatAgent> tempAliveAgents = new ArrayList<RatAgent>();
            for (int i = 0; i < aliveAgents.size(); i++) {
                tempAliveAgents.add(aliveAgents.get(i));
            }

            Collections.shuffle(tempAliveAgents, r);
            for (int j = 0; j < tempAliveAgents.size(); j++) {
                RatAgent a = tempAliveAgents.get(j);
                if (a.isAlive()) {
                    a.justLive();
                }
            }

            // remove dead agents
            for (int x = 0; x < mapDimension; x++) {
                for (int y = 0; y < mapDimension; y++) {
                    if (mapAgents[x][y] != -1 && !agents.get(mapAgents[x][y]).isAlive()) {
                        ((RatAgent) agents.get(mapAgents[x][y])).setPosition(Position.NULL_POSITION);
                        aliveAgents.remove(agents.get(mapAgents[x][y]));
                        mapAgents[x][y] = -1;
                    }
                }
            }
        }

        // evaluate
        double ret = evaluator.getFitness(agents);

        if (log) {
            for (int i = 0; i < agents.size(); i++) {
                logger.log(agents.get(i).getSheet());
                logger.log(agents.get(i).getStringLog());
            }
        }
        if (graph) {

            HashMap<String, Integer> labels = new HashMap<String, Integer>();

            logger.log("digraph world_dynamics {\n"
                    + "size=\"4,4\";");

            for (int i = 0; i < agents.size(); i++) {
                RatAgent r = (RatAgent) agents.get(i);
                String fillcolor = "";
                switch (r.getLabels().size()) {
                    case 4:
                        fillcolor = "black";
                        break;
                    case 3:
                        fillcolor = "sienna";
                        break;
                    case 2:
                        fillcolor = "red";
                        break;
                    case 1:
                        fillcolor = "orange";
                        break;
                    case 0:
                        fillcolor = "white";
                        break;
                    default:
                        fillcolor = "black";
                        break;
                }
                String sides = r.getGender() == Gender.MALE ? "4" : "4";
                logger.log(r.getId()
                        + " [fontsize=14, style=filled, shape=polygon, "
                        + "sides=" + sides + ", fillcolor=" + fillcolor + ","
                        + "label=\"" + r.getName() + " " + r.getNickname().replace('"', '\'')
                        + " " + r.getSurname() + "\""
                        + "];");
            }
            for (int i = 0; i < agents.size(); i++) {
                String agentLog = agents.get(i).getStringLog();

                Pattern patt = Pattern.compile("@" + RatState.PARTNER_FOUND + " ([0-9]*)");
                Matcher m = patt.matcher(agentLog);
                while (m.find()) {
                    logger.log(agents.get(i).getId() + " -- " + m.group(1) + ";");
                }

                Pattern patt2 = Pattern.compile("@" + RatState.PARENT + " ([0-9]*)");
                Matcher m2 = patt2.matcher(agentLog);
                while (m2.find()) {
                    logger.log(agents.get(i).getId() + " -> " + m2.group(1) + ";");
                }

                for (String s : agents.get(i).getLabels()) {
                    Integer val = labels.get(s);
                    if (val == null) {
                        val = 0;
                    }
                    labels.put(s, val + 1);
                }
            }
            logger.log("}");

            logger.log("SUMMARY: ");
            for (String s : labels.keySet()) {
                logger.log(s + ":" + labels.get(s));
            }

        }

        return ret;
    }

    Position findFreeFood(RatAgent source, int smell) {
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

    Position getPosition(RatAgent source) {
        return source.getPosition();
    }

    void moveAgent(RatAgent source, Position t) {
        Position p = source.getPosition();
        mapAgents[p.x][p.y] = -1;
        mapAgents[t.x][t.y] = source.getId();

        source.setX(t.x);
        source.setY(t.y);
    }

    void eatFood(Position p) {
        mapFood[p.x][p.y]--;
    }

    Position findFoodWithAgent(RatAgent source, int smell) {
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

    RatAgent getAgent(Position p) {
        return ((RatAgent) agents.get(mapAgents[p.x][p.y]));
    }

    Position getFreePosition(RatAgent source, int smell) {
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

    ArrayList<Position> getFreePositions(RatAgent source, int smell) {

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

    ArrayList<RatAgent> getAgentsAround(RatAgent source, int smell) {

        Position pos = source.getPosition();

        ArrayList<RatAgent> ret = new ArrayList<RatAgent>();

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
                    ret.add(((RatAgent) agents.get(mapAgents[x][y])));
                }
            }
        }

        return ret;
    }

    void newAgents(RatAgent aThis, RatAgent inLoveWith, int numberOfChildren) {
        ArrayList<Position> p = getFreePositions(aThis, 3);
        for (int i = 0; i < p.size() && i < numberOfChildren; i++) {
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

            RatAgent a = new RatAgent(counter, currDate, g, profile, name, surname, nickname, this, r, log, logger);
            agents.add(a);
            aliveAgents.add(a);

            aThis.addline(a.getDays(), RatState.PARENT + " " + a.getId());
            inLoveWith.addline(inLoveWith.getDays(), RatState.PARENT + " " + a.getId());

            mapAgents[pos.x][pos.y] = counter;
            a.setX(pos.x);
            a.setY(pos.y);
            counter++;
        }
    }

    @Override
    public String getSummary() {
        StringBuffer str = new StringBuffer();

        HashMap<String, ArrayList<RatAgent>> labels = new HashMap<String, ArrayList<RatAgent>>();

        int total = 0;
        int alive = 0;
        int ageMax = Integer.MIN_VALUE;
        int ageMin = Integer.MAX_VALUE;
        int ageAverage = 0;

        for (int i = 0; i < agents.size(); i++) {
            RatAgent ma = ((RatAgent) agents.get(i));
            HashSet<String> agentLabels = ma.getLabels();
            for (String label : agentLabels) {
                if (labels.get(label) == null) {
                    labels.put(label, new ArrayList<RatAgent>());
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

    public BaseAgentSetup getBaseAgentSetup() {
        return baseAgentSetup;
    }

    public GlobalSetup getGlobalSetup() {
        return globalSetup;
    }

    public ArrayList<MadeAgentInterface> getAgents() {
        return agents;
    }

    
    
}
