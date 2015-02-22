/*
 * Copyright (C) 2015 Rubén Héctor García (raiben@gmail.com)
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

package com.velonuboso.made.core.facts;

import com.velonuboso.made.interfaces.ICharacter;
import com.velonuboso.made.interfaces.IFact;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class InhabitantExists implements IFact{

    private ICharacter character;

    public InhabitantExists(ICharacter character) {
        this.character = character;
    }
    
    @Override
    public String toLogicalPredicate() {
        return "Exists("+character.getId()+","+character.getName()+")";
    }
}
