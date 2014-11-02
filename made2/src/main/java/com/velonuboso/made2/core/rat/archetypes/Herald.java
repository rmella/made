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
    public double evaluate(GlobalSetup setup, ArrayList<RatAgent> agents, ArchetypeOccurrence o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
