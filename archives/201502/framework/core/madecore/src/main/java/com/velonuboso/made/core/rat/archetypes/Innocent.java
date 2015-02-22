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
public class Innocent extends Archetype{


    @Override
    public String getDescription() {
        return "The archetypal \"innocent\" character is a naive individual "
                + "who knows little of the evils in the world. The "
                + "\"innocent\" is generally young and lacks \"real-world\" "
                + "experience, and as such, he or she is more likely to "
                + "suffer at the hands of others. For example, a female "
                + "\"innocent\" may be a young girl who has lived a privileged "
                + "and uneventful life and is forced to experience the painful "
                + "realities of the outside world after leaving home.";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<MadeAgentInterface> agents, ArchetypeOccurrence o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
