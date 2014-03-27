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
public class Trickster extends Archetype{

    public Trickster() {
        dependencies.add(Hero.class);
    }

    
    
    @Override
    public String getDescription() {
        return "The Trickster provides entertainment in the story through "
                + "wit, foolishness or other means. They may be wise, as in "
                + "the Shakespearian fool, or may be criminal in their "
                + "deception. They provide further uncertainty and keep us "
                + "(and the hero) on our toes.\n" 
                + "The Trickster may remind us to lighten up and see the "
                + "funny side of things. They also remind us not be be "
                + "naive and to expect the unexpected.\n"
                + "(Vogler)";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<MadeAgentInterface> agents, ArchetypeOccurrence o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
