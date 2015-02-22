/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.core.rat.archetypes;

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
public class Shadow extends Archetype{

    public Shadow() {
        dependencies.add(Hero.class);
    }

    
    
    @Override
    public String getDescription() {
        return "The Shadow is the opposite of light and provides the tension "
                + "of anxiety and fear in the story. The Shadow is often "
                + "opposes the hero and is typically the main antagonist. "
                + "They may also be people who provide obstacles along the "
                + "way, although not as a guardian.\n" 
                + "The hero must struggle with the Shadow, somehow "
                + "overcoming the opposition they provide.\n" 
                + "The shadow also represents the darker side of our own "
                + "nature, as in "
                + "Jung's Archetypes, and it is disquieting to recognize "
                + "them as somehow related to ourselves.\n"
                + "(Vogler)";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<MadeAgentInterface> agents, ArchetypeOccurrence o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
