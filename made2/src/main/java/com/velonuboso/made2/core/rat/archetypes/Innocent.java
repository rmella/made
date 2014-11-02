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
    public double evaluate(GlobalSetup setup, ArrayList<RatAgent> agents, ArchetypeOccurrence o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
