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
    public double evaluate(GlobalSetup setup, ArrayList<RatAgent> agents, ArchetypeOccurrence o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
