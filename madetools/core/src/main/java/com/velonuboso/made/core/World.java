/*
 * Copyright (C) 2015 Rubén Héctor García (raiben@gmail.com)
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

package com.velonuboso.made.core;

import com.velonuboso.made.interfaces.IWorld;
import com.velonuboso.made.core.facts.WorldConfigurationFact;
import com.velonuboso.made.interfaces.ICharacter;
import com.velonuboso.made.interfaces.IFact;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * world class
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class World implements IWorld {

    private final List<ICharacter> inhabitants;
    private int identifiersSequence;
    private final List<IFact> facts;
    private TimeUnit timeUnit;
    private int runTime;
    
    public World() {
        inhabitants = new ArrayList<>();
        identifiersSequence = 0;
        facts = new ArrayList<>();
        timeUnit = TimeUnit.DAYS;
        runTime = 0;
    }
    
    @Override
    public List<ICharacter> getInhabitants() {
        return inhabitants;
    }

    @Override
    public void addInhabitant(ICharacter character) {
        if (character.getId()==null){
            character.setId(getNextIdentifier());
        }
        character.setFactsWriter(new FactsWriter(facts));
        inhabitants.add(character);
        character.isAdded();
    }

    private synchronized int getNextIdentifier(){
        return identifiersSequence++;
    }

    @Override
    public List<IFact> getFacts() {
        return facts;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    @Override
    public void run(int expectedExecutionDays) {
        facts.add(getConfigurationfact());
        
        for (runTime=0; runTime<expectedExecutionDays; runTime++){
            
        }
        //TODO What should happen if run is called twice?
    }

    @Override
    public int getRunTime() {
        return runTime;
    }

    private IFact getConfigurationfact() {
        WorldConfigurationFact fact = new WorldConfigurationFact();
        return fact;
    }

    @Override
    public Object getWorldStory() {
        return "";
    }
}
