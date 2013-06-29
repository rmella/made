/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.basicmade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author Ruben
 */
public class MadeAgent {

    public static int NUMBER_OF_FEATURES = 10;
    private static final int FEATURE_BITE = 0; // attack
    private static final int FEATURE_FUR = 1; // defense
    private static final int FEATURE_PROFILE_VARIANCE = 2;
    private static final int FEATURE_HEALTH = 3;
    private static final int FEATURE_LIFE = 4;
    private static final int FEATURE_SMELL = 5;
    private static final int FEATURE_METHABOLISM = 6;
    private static final int FEATURE_HUNGRY_LEVEL = 7;
    private static final int FEATURE_LOVE = 8;
    private static final int FEATURE_CHARACTER = 9;
    
    
    
    private int id;
    private Gender gender;
    private int profile;
    private String name;
    private String surname;
    private String nickname;
    private MadeEnvironment env;
    
    private Random r;
    private int days = -1;
    private boolean alive = false;
    private int maxDays = -1;
    
    private double profileVariance;
    private int energy;
    private int maxEnergy;
    private int smell;
    private int nutrition;
    private int bite;
    private int fur;
    private double hungryLevel;
    private double love;
    private double character;
    
    private MadeAgent inLoveWith;
    
    private int x = -1;
    private int y = -1;
    private boolean log;
    private StringBuffer strb;

    private HashSet<String> labels;
    
    public MadeAgent(int id, Gender gender, int profile, String name,
            String surname, String nickname, MadeEnvironment env,
            Random r, boolean log) {

        profileVariance = env.getVal(profile, FEATURE_PROFILE_VARIANCE);
        this.log = log;

        this.id = id;
        this.gender = gender;
        this.profile = profile;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.env = env;
        this.r = r;
        alive = true;


        days = 0;
        // A Rattus rattus Norvegicus can live 12 months (360 days)
        maxDays = (int) 
                (
                200 + 
                (200*env.getVal(profile, FEATURE_LIFE))+
                ((200*env.getVal(profile, FEATURE_LIFE))*(((r.nextDouble()*2)-1)*profileVariance))   
                );

        // About one week without eating
        maxEnergy = (int) 
                (
                3 + 
                (3*env.getVal(profile, FEATURE_HEALTH))+
                ((3*env.getVal(profile, FEATURE_HEALTH))*(((r.nextDouble()*2)-1)*profileVariance))   
                );
        energy = maxEnergy;

        // between 1 and 15 cells to search food
        smell = (int)
                (
                3 + 
                (3*env.getVal(profile, FEATURE_SMELL))+
                ((3*env.getVal(profile, FEATURE_SMELL))*(((r.nextDouble()*2)-1)*profileVariance))   
                );


        nutrition = (int)
                (
                1 + 
                (1*env.getVal(profile, FEATURE_METHABOLISM))+
                ((1*env.getVal(profile, FEATURE_METHABOLISM))*(((r.nextDouble()*2)-1)*profileVariance))   
                );
        
        hungryLevel = env.getVal(profile, FEATURE_HUNGRY_LEVEL);
            
        bite = (int)
                (
                5 + 
                (5*env.getVal(profile, FEATURE_BITE))+
                ((5*env.getVal(profile, FEATURE_BITE))*(((r.nextDouble()*2)-1)*profileVariance))   
                );
        fur = (int)
                (
                5 + 
                (5*env.getVal(profile, FEATURE_FUR))+
                ((5*env.getVal(profile, FEATURE_FUR))*(((r.nextDouble()*2)-1)*profileVariance))   
                );
        
        love = env.getVal(profile, FEATURE_LOVE);
        
        character = env.getVal(profile, FEATURE_CHARACTER);
        
        if (log) {
            strb = new StringBuffer();
            addline(days, "BORN");
        }
        
        labels = new HashSet<String>();
    }

    boolean isAlive() {
        return alive;
    }

    void justLive() {
        days++;
        energy--;
        if (days > maxDays || energy <= 0) {
            alive = false;
            addline(days, "DIE");
        } else {
            if (energy < maxEnergy * hungryLevel + (1 - 2 * r.nextDouble())) {
                addline(days, "HUNGRY " + energy);
                Position p = env.findFreeFood(this, smell);
                if (p != null) {
                    addline(days, "EAT " + nutrition);
                    env.moveAgent(this, p);
                    env.eatFood(p);
                    energy += nutrition;
                } else {
                    p = env.findFoodWithAgent(this, smell);
                    if (p != null) {
                        MadeAgent target = env.getAgent(p);
                        if (this.getBite() > target.getFur()){
                            target.nudge(this);
                            addline(days, "NUDGE_OK "+target.id);
                            addline(days, "EAT " + nutrition);
                            env.moveAgent(this, p);
                            env.eatFood(p);
                            energy += nutrition;
                        }else{
                            target.defended(this);
                            addline(days, "NUDGE_FAILED "+target.id);
                        }
                    }
                }
            } else {
                Position p = env.getFreePosition(this, smell);
                if (p!=null){
                    env.moveAgent(this, p);
                    addline(days, "MOVE "+p.x+" "+p.y);
                }
            }

        }
    }

    public String getStringLog() {
        if (strb == null) {
            return "";
        } else {
            return strb.toString();
        }
    }

    public String getSheet() {
        String ret = "";
        ret += "------------------------------------" + "\n";
        ret += "Name: " + name + " (" + nickname + ") " + surname + "\n";
        ret += "Id: " + id + "\n";
        ret += "Gender: " + gender + "\n";
        ret += "Variance: " + profileVariance + "\n";
        ret += "Profile: " + profile + "\n";
        ret += "Alive: " + alive + "\n";
        ret += "Days: " + days + "\n";
        ret += "MaxDays: " + maxDays + "\n";
        ret += "Energy: " + energy + "\n";
        ret += "Maximum energy: " + maxEnergy + "\n";
        ret += "Smell: " + smell + "\n";
        ret += "Nutrition: " + nutrition + "\n";
        ret += "Hungry level: " + hungryLevel + "\n";
        ret += "Bite: " + bite + "\n";
        ret += "Fur: " + fur + "\n";
        ret += "Love: " + love + "\n";
        ret += "Character: " + character + "\n";
        
        ret += "Labels: "+"{";
        for (String key:labels){
            ret+="key ";
        }
        ret += "}\n";
        
        return ret;
    }

    private void addline(int days, String action) {
        if (strb == null) {
            strb = new StringBuffer();
        }
        strb.append(days + ":" + action + "\n");
    }

    private void nudge(MadeAgent source) {
        ArrayList<Position> arr = new ArrayList<Position>();
        for (int a = x - 1; a < x + 2; a += 2) {
            if (a > 0 && a < MadeEnvironment.MAP_DIMENSION) {
                for (int b = y - 1; b < y + 2; b += 2) {
                    if (b > 0 && b < MadeEnvironment.MAP_DIMENSION) {
                        Position p = new Position();
                        p.x = a;
                        p.y = b;
                        arr.add(p);
                    }
                }
            }
        }
        if (arr.size()>0){
            Position p = null;
            if (arr.size() == 1){
                p = arr.get(0);
            }else{
                p = arr.get(r.nextInt(arr.size()-1));
            }
            env.moveAgent(this, p);
            this.energy --;
            addline(days, "NUDGED "+source.id);
        }else{
            alive = false;
            addline(days, "DIE");
        }
    }
    
    private void defended(MadeAgent source) {
        addline(days, "DEFENDED "+source.id);
    }
    
    // getters and setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(Position p) {
        x = p.x;
        y = p.y;
    }

    public Position getPosition() {
        Position p = new Position();
        p.x = x;
        p.y = y;
        return p;
    }

    public int getDays() {
        return days;
    }

    int getId() {
        return id;
    }

    private double getBite() {
        return bite;
    }

    private double getFur() {
        return fur;
    }

    void addLabel(String label) {
        labels.add(label);
    }
}
