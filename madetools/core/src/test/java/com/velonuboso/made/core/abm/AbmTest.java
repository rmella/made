/*
 * Copyright (C) 2015 rhgarcia
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
package com.velonuboso.made.core.abm;

import alice.tuprolog.Term;
import com.velonuboso.made.core.abm.api.IAbm;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.implementation.EventsWriter;
import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationHelperWorld;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.entity.CommonAbmConfiguration;
import com.velonuboso.made.core.common.entity.InferencesEntity;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.customization.api.ICustomization;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author rhgarcia
 */
public class AbmTest {

    public boolean SHOW_EVENTS = false;

    private IAbm abm;
    private ICustomization fakeCustomization;
    private InferencesEntity inferencesEntity;
    
    public AbmTest() {
    }
    
    @Before
    public void setUp() {
        fakeCustomization = Mockito.mock(ICustomization.class);
        inferencesEntity = new InferencesEntity();
        
        abm = ObjectFactory.createObject(IAbm.class);
        abm.setCustomization(fakeCustomization);
        abm.setInferences(inferencesEntity);
        
        ObjectFactory.cleanAllMocks();
        IMap map = ObjectFactory.createObject(IMap.class);
        ObjectFactory.installMock(IMap.class, map);
    }

    @After
    public void cleanUp() {
        ObjectFactory.cleanAllMocks();
    }
    
   
    @Test
    public void testRun_many_characters() {
        int size = 52;
        float[] chromosome = new float[size];
        
        IGlobalConfigurationFactory globalConfigurationFactory
                = ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonAbmConfiguration config = globalConfigurationFactory.getCommonAbmConfiguration();
        
        chromosome[0] = config.MIN_WORLD_SIZE;
        chromosome[1] = config.MAX_NUMBER_OF_CIRCLES;
        chromosome[2] = config.MAX_NUMBER_OF_TRIANGLES;
        chromosome[3] = config.MAX_NUMBER_OF_SQUARES;
        chromosome[4] = config.MAX_NUMBER_OF_DAYS/10;
        chromosome[5] = 1;
        chromosome[6] = 0.2f;
        Arrays.fill(chromosome, 5, size, 0.5f);
        AbmConfigurationEntity entity = new AbmConfigurationEntity(chromosome);
        
        abm.run(entity);
        
        /*
        for(Term term : abm.getEventsLog().getLogicalTerms()){
            System.out.println(term);
        }
        System.out.println(abm.getEventsLog().getLog());
        */
    }
    
    @Test
    public void testRun_one_character() {
        int size = 52;
        
        IGlobalConfigurationFactory globalConfigurationFactory
                = ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonAbmConfiguration config = globalConfigurationFactory.getCommonAbmConfiguration();
        
        float[] chromosome = new float[size];
        chromosome[0] = config.MIN_WORLD_SIZE;
        chromosome[1] = 1;
        chromosome[2] = 0;
        chromosome[3] = 0;
        chromosome[4] = config.MAX_NUMBER_OF_DAYS;
        chromosome[5] = 1;
        chromosome[6] = 0.4f;
        Arrays.fill(chromosome, 5, size, 0.5f);
        AbmConfigurationEntity entity = new AbmConfigurationEntity(chromosome);
        
        abm.run(entity);
        
        if (SHOW_EVENTS) {
            for (Term term : abm.getEventsLog().getLogicalTerms()) {
                System.out.println(term);
            }
            System.out.println(abm.getEventsLog().getLog());
        }
    }

    @Ignore
    @Test
    public void testRun_multiple_characters_big_log() throws FileNotFoundException {
        int size = 52;
        
        IGlobalConfigurationFactory globalConfigurationFactory
                = ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonAbmConfiguration config = globalConfigurationFactory.getCommonAbmConfiguration();
        config.MAX_NUMBER_OF_CIRCLES = 20;
        config.MAX_NUMBER_OF_TRIANGLES = 20;
        config.MAX_NUMBER_OF_SQUARES = 20;
        config.MAX_NUMBER_OF_DAYS = 200;
        config.MAX_WORLD_SIZE = 11;
        
        
        float[] chromosome = new float[size];
        chromosome[0] = config.MAX_WORLD_SIZE;
        chromosome[1] = config.MAX_NUMBER_OF_CIRCLES;
        chromosome[2] = config.MAX_NUMBER_OF_TRIANGLES;
        chromosome[3] = config.MAX_NUMBER_OF_SQUARES;
        chromosome[4] = config.MAX_NUMBER_OF_DAYS;
        chromosome[5] = 1;
        chromosome[6] = 0.2f;
        Arrays.fill(chromosome, 5, size, 0.5f);
        AbmConfigurationEntity entity = new AbmConfigurationEntity(chromosome);
        
        abm.run(entity);
        
        PrintWriter writer = new PrintWriter("sample_virtual_world.pl");
        for(Term term : abm.getEventsLog().getLogicalTerms()){
            writer.println(term.toString()+".");
        }
        writer.close();
    }
    
}
