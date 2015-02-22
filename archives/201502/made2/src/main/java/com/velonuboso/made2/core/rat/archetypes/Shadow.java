/*
 * Copyright 2014 Rubén Héctor García <raiben@gmail.com>.
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

package com.velonuboso.made2.core.rat.archetypes;

import com.velonuboso.made2.core.common.ArchetypeType;
import com.velonuboso.made2.core.interfaces.Archetype;
import com.velonuboso.made2.core.interfaces.ArchetypeOccurrence;
import com.velonuboso.made2.core.rat.RatAgent;
import com.velonuboso.made2.core.setup.GlobalSetup;
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
    public double evaluate(GlobalSetup setup, ArrayList<RatAgent> agents, ArchetypeOccurrence o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
