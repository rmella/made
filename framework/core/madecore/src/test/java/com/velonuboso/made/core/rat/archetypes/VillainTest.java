/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.core.rat.archetypes;

import com.velonuboso.made.core.common.LabelArchetype;
import com.velonuboso.made.core.common.Position;
import com.velonuboso.made.core.interfaces.ArchetypeOccurrence;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.rat.RatAgent;
import com.velonuboso.made.core.rat.RatState;
import es.velonuboso.madecore.DummyAgent;
import java.util.ArrayList;
import java.util.HashSet;
import junit.framework.TestCase;

/**
 *
 * @author rhgarcia
 */
public class VillainTest extends TestCase {
    
    public VillainTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    
    /**
     * Test of evaluate method, of class Villain.
     */
    public void testEvaluate0() {
        Villain v = new Villain();
        ArrayList<MadeAgentInterface> agents = new ArrayList<MadeAgentInterface>();
        DummyAgent d = new DummyAgent(0, "");
        agents.add(d);
        double ret = v.evaluate(null, agents, ArchetypeOccurrence.THE_MORE_THE_BETTER);
        assertTrue(ret == 0);
    }
    
    /**
     * Test of evaluate method, of class Villain.
     */
    public void testEvaluate1() {
        Villain v = new Villain();
        ArrayList<MadeAgentInterface> agents = new ArrayList<MadeAgentInterface>();
        DummyAgent d = new DummyAgent(0, "");
        d.addline(0, RatState.KILL + " 0");
        agents.add(d);
        double ret = v.evaluate(null, agents, ArchetypeOccurrence.THE_MORE_THE_BETTER);
        assertTrue(ret == 0);
    }
    
     public void testEvaluate10() {
        Villain v = new Villain();
        ArrayList<MadeAgentInterface> agents = new ArrayList<MadeAgentInterface>();
        DummyAgent d = new DummyAgent(0, "");
        for (int i=0; i<10; i++){
            d.addline(0, RatState.KILL + " 0");
            d.addline(0, RatState.FREE_TIME + " 0");
        }
        agents.add(d);
        double ret = v.evaluate(null, agents, ArchetypeOccurrence.THE_MORE_THE_BETTER);
        assertTrue(ret == 1);
    }
    
    
}
