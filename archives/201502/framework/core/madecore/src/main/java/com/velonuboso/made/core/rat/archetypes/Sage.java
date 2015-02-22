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
public class Sage extends Archetype{


    @Override
    public String getDescription() {
        return "The \"sage\" archetype is a character who is wise, very old "
                + "and generally helps the hero in his or her quest. The sage "
                + "can be a man or woman, and he or she often lives in "
                + "relative seclusion from others. The sage's hermit-like "
                + "lifestyle and eccentric behavior sets him or her apart "
                + "from other characters. An example of a sage character would "
                + "be the wise old wizard who provides thoughtful advice to "
                + "the young adventurer.";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<MadeAgentInterface> agents, ArchetypeOccurrence o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
