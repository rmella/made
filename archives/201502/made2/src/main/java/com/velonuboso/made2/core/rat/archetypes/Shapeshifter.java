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
public class Shapeshifter extends Archetype {

    public Shapeshifter() {
        dependencies.add(Hero.class);
    }

    

    @Override
    public String getDescription() {
        return "The Shapeshifter represents uncertainty and change, "
                + "reminding us that not all is as it seems. They may be a "
                + "character who keeps changing sides or whose allegiance "
                + "is uncertain. They act to keep the hero (and us) on his or "
                + "her toes and may thus catalyze critical action.\n" 
                + "A typical Shapeshifter is a person of the opposite sex "
                + "who provides the "
                + "love interest and whose affections vary across the story. "
                + "Other characters may also be shapeshifters, including "
                + "Mentors, Guardians and Tricksters.\n"
                + "(Vogler)";
    }

    @Override
    public double evaluate(GlobalSetup setup, ArrayList<RatAgent> agents, ArchetypeOccurrence o) {
        //TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArchetypeType getType() {
        return ArchetypeType.LITERARY;
    }


}
