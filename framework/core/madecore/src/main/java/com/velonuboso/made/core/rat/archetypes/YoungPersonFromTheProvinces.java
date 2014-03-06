/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.core.rat.archetypes;

import com.sun.tracing.dtrace.DependencyClass;
import com.velonuboso.made.core.common.ArchetypeType;
import com.velonuboso.made.core.interfaces.Archetype;
import com.velonuboso.made.core.interfaces.ArchetypeOccurrence;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;

/**
 *
 * @author rhgarcia
 */
public class YoungPersonFromTheProvinces extends Archetype{

    /**
     * default constructor. Adds a class to the dependencies
     */
    public YoungPersonFromTheProvinces() {
        dependencies.add(Hero.class);
    }

    
    @Override
    public String getArchetypeName() {
        return "Young person from the provinces";
    }

    @Override
    public String getDescription() {
        return "This hero is taken away as an infant or youth and raised by "
                + "strangers. He or she later returns home as a stranger and "
                + "able to recognize new problems and new solutions.";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<MadeAgentInterface> agents, ArchetypeOccurrence o) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArchetypeType getType() {
        return ArchetypeType.LITERARY;
    }
    
}
