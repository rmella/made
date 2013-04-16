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
package com.velonuboso.made.core;

import com.velonuboso.made.core.interfaces.Environment;
import com.velonuboso.made.core.interfaces.Feature;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Ruben Hector Garcia Ortega <raiben@gmail.com>
 */
public class MadeAgent extends Thread {
    
    private Random random = null;
    private Integer identifier = null;
    
    
    private String name = null;
    private String surname1 = null;
    private String surname2 = null;
    private Gender gender = null;
    
    private Date birthday = null;
    private Date deadday = null;
    
    private List<Feature> features = null;
    private List<MadeState> states = null;
    private List<Message> messages = null;
    
    private MadeAgent parent1 = null;
    private MadeAgent parent2 = null;
    private List<MadeAgent> children = null;
    
    private Environment environment;
    private Diary diary;

    
    public MadeAgent(Environment env, Random r) {
        super();
        
        environment = env;
        random = r;
        
        identifier = env.getNextInteger();
        
        gender = environment.getRandomGender(random);
        name = environment.getRandomName(random, gender);
        surname1 = environment.getRandomSurname(random);
        surname2 = environment.getRandomSurname(random);
        
        features = environment.getRandomFeatures(random);
        states = environment.getRandomStates(random);
        
        children = new ArrayList<MadeAgent>();
        
        env.setRandomPosition(random, this);
    }
    
    public MadeAgent(Environment env, Random r, MadeAgent p1, MadeAgent p2){
        environment = env;
        random = r;
        parent1 = p1;
        parent2 = p2;
        
        identifier = env.getNextInteger();
        
        gender = environment.getRandomGender(random);
        String proposedName = environment.getRandomName(random, gender);
        name = environment.getPreferredName(parent1, parent2, proposedName);
        surname1 = parent1.getAgentSurname1();
        surname2 = parent2.getAgentSurname2();
        
        features = environment.getMixedFeatures(parent1, parent2);
        states = environment.getInitialStates(random);
        
        children = new ArrayList<MadeAgent>();
    }
   
    @Override
    public void run (){
        try{
            while (deadday == null){
                ArrayList<MadeState> oldStates = new ArrayList<MadeState>();
                Collections.copy(oldStates, states);

                environment.calculateNewStates(this, random);
                ArrayList<MadeState> newStates = new ArrayList<MadeState>();
                Collections.copy(newStates, states);

                newStates.removeAll(oldStates);
                for (MadeState n:newStates){
                    diary.AddLine(n.getDateFrom(), n, n.getEvent(), n.getTarget());
                }
                environment.waitFictional(this);
            }
        }catch (InterruptedException ex){
            // interrupted
        }finally{
            environment.printSheet(this);
        }
    }

    public String getAgentName() {
        return name;
    }

    public String getAgentSurname1() {
        return surname1;
    }

    public String getAgentSurname2() {
        return surname2;
    }
    
    public String getSimpleFullName(){
        return getAgentName().substring(0,1).toUpperCase()+". "+getAgentSurname1();
    }

    public String getIdentifier() {
        return Integer.toString(identifier);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Date getDeadday() {
        return deadday;
    }

    public List<MadeAgent> getChildren() {
        return children;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public Diary getDiary() {
        return diary;
    }

    public Gender getGender() {
        return gender;
    }

    public MadeAgent getParent1() {
        return parent1;
    }

    public MadeAgent getParent2() {
        return parent2;
    }

    public Random getRandom() {
        return random;
    }

    public List<MadeState> getStates() {
        return states;
    }
}
