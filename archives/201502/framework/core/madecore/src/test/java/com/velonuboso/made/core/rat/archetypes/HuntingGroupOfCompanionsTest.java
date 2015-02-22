/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made.core.rat.archetypes;

import com.velonuboso.made.core.common.ArchetypeType;
import com.velonuboso.made.core.interfaces.ArchetypeOccurrence;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;
import junit.framework.TestCase;

/**
 *
 * @author rhgarcia
 */
public class HuntingGroupOfCompanionsTest extends TestCase {

    public HuntingGroupOfCompanionsTest(String testName) {
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

    public void testGetName() {
        HuntingGroupOfCompanions instance = new HuntingGroupOfCompanions();
        System.out.println(instance.getArchetypeName());
        assertEquals(instance.getArchetypeName(), "Hunting group of companions");
    }

}
