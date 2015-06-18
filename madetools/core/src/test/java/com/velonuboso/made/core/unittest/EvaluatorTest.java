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
package com.velonuboso.made.core.unittest;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import com.velonuboso.made.core.implementation.Evaluator;
import com.velonuboso.made.core.api.ITrope;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author rhgarcia
 */
public class EvaluatorTest {
    
    public EvaluatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        Evaluator evaluator = new Evaluator();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void Prolog_engine_can_be_used(){
        try {
            Prolog engine = new Prolog();
            SolveInfo info = engine.solve("append([1],[2,3],X).");
            //System.out.println(info.getSolution());
        } catch (Exception ex) {
            Logger.getLogger(EvaluatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void Evaluator_uses_a_list_of_events_as_input(){
        //TODO
    }
    
    @Test
    public void Evaluator_must_be_configured_with_tropes(){
        ITrope trope = mock(ITrope.class);
    }
}
