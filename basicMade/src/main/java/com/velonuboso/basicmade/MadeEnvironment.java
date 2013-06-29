/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.basicmade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import org.jgap.IChromosome;

/**
 *
 * @author Ruben
 */
public class MadeEnvironment {

    public static int NUMBER_OF_PROFILES = 30;
    public static int MAP_DIMENSION = 20;
    public static int FOOD = 5;
    public static int DAYS = 300;
    
    
    private int[][] mapAgents = null;
    private int[][] mapFood = null;
    IChromosome iChromosome = null;
    int counter;
    ArrayList<MadeAgent> agents = new ArrayList<MadeAgent>();
    ArrayList<MadeAgent> aliveAgents = new ArrayList<MadeAgent>();
    Random r;

    public MadeEnvironment(IChromosome c) {
        this.iChromosome = c;
        mapAgents = new int[MAP_DIMENSION][MAP_DIMENSION];
        for (int x = 0; x < MAP_DIMENSION; x++) {
            for (int y = 0; y < MAP_DIMENSION; y++) {
                mapAgents[x][y] = -1;
            }
        }
        mapFood = new int[MAP_DIMENSION][MAP_DIMENSION];
        for (int x = 0; x < MAP_DIMENSION; x++) {
            for (int y = 0; y < MAP_DIMENSION; y++) {
                mapFood[x][y] = 0;
            }
        }
        counter = 0;
        r = new Random();
    }

    public double getVal(int profile, int position) {
        if (profile > NUMBER_OF_PROFILES || position > MadeAgent.NUMBER_OF_FEATURES) {
            throw new IndexOutOfBoundsException();
        }
        int p = (profile * MadeAgent.NUMBER_OF_FEATURES) + position;
        return (Double) iChromosome.getGene(p).getAllele();
    }

    public double runEnvironment(boolean log) {

        for (int i = 0; i < NUMBER_OF_PROFILES * 2; i++) {
            Gender g = i % 2 == 0 ? Gender.MALE : Gender.FEMALE;
            String name = RatNameHelper.getInstance().getRandomName(r, g);
            String surname = RatNameHelper.getInstance().getRandomSurname(r);
            String nickname = RatNameHelper.getInstance().getRandomNickname(r);
            MadeAgent a = new MadeAgent(counter, g, i / 2, name, surname, nickname, this, r, log);
            agents.add(a);
            aliveAgents.add(a);
            int x, y;
            do {
                x = r.nextInt(MAP_DIMENSION);
                y = r.nextInt(MAP_DIMENSION);
            } while (mapAgents[x][y] != -1);
            mapAgents[x][y] = counter;
            a.setX(x);
            a.setY(y);
            counter++;
        }

        for (int i = 0; i < DAYS; i++) {

            // place some food
            for (int j = 0; j < FOOD; j++) {
                int x = r.nextInt(MAP_DIMENSION);
                int y = r.nextInt(MAP_DIMENSION);
                mapFood[x][y]++;
            }

            // shuffle the alive agents and play
            Collections.shuffle(aliveAgents, r);
            for (int j = 0; j < aliveAgents.size(); j++) {
                MadeAgent a = aliveAgents.get(j);
                if (a.isAlive()) {
                    a.justLive();
                } else {
                    aliveAgents.remove(j);
                }
            }

            // remove dead agents
            for (int x = 0; x < MAP_DIMENSION; x++) {
                for (int y = 0; y < MAP_DIMENSION; y++) {
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
            if (px1 > MAP_DIMENSION - 1) {
                px1 = MAP_DIMENSION;
            }
            int py0 = pos.y - smell;
            if (py0 < 0) {
                py0 = 0;
            }
            int py1 = pos.y + smell;
            if (py1 > MAP_DIMENSION - 1) {
                py1 = MAP_DIMENSION;
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
        if (px1 > MAP_DIMENSION - 1) {
            px1 = MAP_DIMENSION;
        }
        int py0 = pos.y - smell;
        if (py0 < 0) {
            py0 = 0;
        }
        int py1 = pos.y + smell;
        if (py1 > MAP_DIMENSION - 1) {
            py1 = MAP_DIMENSION;
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
        Position pos = source.getPosition();

        ArrayList<Position> ret = new ArrayList<Position>();

        int px0 = pos.x - smell;
        if (px0 < 0) {
            px0 = 0;
        }
        int px1 = pos.x + smell;
        if (px1 > MAP_DIMENSION - 1) {
            px1 = MAP_DIMENSION;
        }
        int py0 = pos.y - smell;
        if (py0 < 0) {
            py0 = 0;
        }
        int py1 = pos.y + smell;
        if (py1 > MAP_DIMENSION - 1) {
            py1 = MAP_DIMENSION;
        }


        for (int x = px0; x < px1; x++) {
            for (int y = py0; y < py1; y++) {
                if (mapAgents[x][y] == -1) {
                    Position p = new Position(x, y);
                    ret.add(p);
                }
            }
        }
        if (ret.size() == 0){
            return null;
        }else{
            if (ret.size()==1){
                return ret.get(0);
            }else{
                return ret.get(r.nextInt(ret.size()));
            }
        }
    }
}
