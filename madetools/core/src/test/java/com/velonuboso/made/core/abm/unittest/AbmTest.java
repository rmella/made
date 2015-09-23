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
package com.velonuboso.made.core.abm.unittest;

import com.velonuboso.made.core.abm.api.IAbm;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.implementation.Abm;
import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationHelperWorld;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.entity.EventsLogEntity;
import com.velonuboso.made.core.common.entity.InferencesEntity;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.customization.api.ICustomization;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.mockito.Mockito;

/**
 *
 * @author rhgarcia
 */
public class AbmTest {

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
        IMap map = Mockito.spy(ObjectFactory.createObject(IMap.class));
        ObjectFactory.installMock(IMap.class, map);
    }

    @After
    public void cleanUp() {
        ObjectFactory.cleanAllMocks();
    }
    
   
    @Test
    public void testRun() {
        int size = 46;
        float[] chromosome = new float[size];
        chromosome[0] = 8;
        chromosome[1] = 1;
        chromosome[2] = 0;
        chromosome[3] = 0;
        chromosome[4] = 100;
        chromosome[5] = 1;
        chromosome[6] = 0f;
        Arrays.fill(chromosome, 5, size, 1f);
        AbmConfigurationEntity entity = new AbmConfigurationEntity(chromosome);
        
        abm.run(entity);
        
        //EventsLogEntity log = abm.getEventsLog();
        /*for (String line: log.getLog()){
            System.out.println(line);
        }*/
    }

    
}
