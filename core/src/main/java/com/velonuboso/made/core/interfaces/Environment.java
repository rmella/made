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

package com.velonuboso.made.core.interfaces;

import com.velonuboso.made.core.Gender;
import com.velonuboso.made.core.MadeAgent;
import com.velonuboso.made.core.MadeState;
import com.velonuboso.made.core.Position;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Ruben Hector Garcia Ortega <raiben@gmail.com>
 */
public interface Environment {
    
    public List<MadeAgent> getNeighborhood (Position pos);
    
    public Position getPosition (MadeAgent agent);
    
    public void setPosition (MadeAgent agent, Position pos);
    
    public String getRandomName(Random r, Gender g);
    
    public String getRandomSurname(Random r);
    
    public String getRandomNickname(Random r);

    public Gender getRandomGender(Random random);

    public List<Feature> getRandomFeatures(Random random);

    public List<MadeState> getRandomStates(Random random);

    public DateFormat getDateFormat();

    public String getString(String str);

    public Integer getNextInteger();

    public String getPreferredName(MadeAgent parent1, MadeAgent parent2, String proposedName);

    public List<Feature> getMixedFeatures(MadeAgent parent1, MadeAgent parent2);

    public List<MadeState> getInitialStates(Random random);

    public void setRandomPosition(Random random, MadeAgent aThis);
    
    public void initFictionalDate (Date fictional, Date real, float speedfactor);
    
    public Date getFictionalDate();
    
    public void waitFictional(MadeAgent ma) throws InterruptedException;
    
    public String printSheet(MadeAgent aThis);
    
    public void calculateNewStates(MadeAgent aThis, Random random);

    public MadeState getNextState(MadeState aThis, MadeAgent source, MadeAgent related);

    public int getNumberOfInitialAgents();
    
    
}
