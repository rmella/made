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
package com.velonuboso.made.core.simulation.integrationtest;

import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.simulation.api.ISimulator;
import com.velonuboso.made.core.simulation.implementation.Simulator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author rhgarcia
 */
public class SimulatorIntegrationTest {

    public SimulatorIntegrationTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void IT_ISimulator_can_be_constructed_using_objectFactory() {
        try {
            ISimulator simulator = ObjectFactory.createObject(ISimulator.class);
        } catch (Exception exception) {
            fail("Should've constructed Simulator and all the elements within with the ObjectFactory:"
                    + exception.getMessage());
            exception.printStackTrace();
        }
    }

}
