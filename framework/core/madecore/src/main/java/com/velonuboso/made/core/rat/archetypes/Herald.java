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
public class Herald extends Archetype{

    public Herald() {
        dependencies.add(Hero.class);
    }

    
    
    @Override
    public String getDescription() {
        return "The Herald announces important events verbally, telling us "
                + "what we do not realize or emphasizing the importance of "
                + "an event. In particular, the herald provides the "
                + "information that triggers the hero into original action.\n" 
                + "The herald need not be a professional announcer nor even "
                + "a person - a message on a scrap of paper or a radio "
                + "broadcast can serve equally to trigger change.\n"
                + "(Vogler)";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<MadeAgentInterface> agents, ArchetypeOccurrence o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
