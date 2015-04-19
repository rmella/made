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

package com.velonuboso.made.core;

import com.velonuboso.made.interfaces.ICharacter;
import com.velonuboso.made.interfaces.IFactsWriter;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class AntMite implements ICharacter{

    private String name;
    private Integer id;
    private IFactsWriter factsWriter;
    
    public AntMite() {
        name = "Kroo";
        id = null;
        factsWriter = null;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(int newId) {
        this.id = newId;
    }

    @Override
    public void setFactsWriter(IFactsWriter newFactsWriter) {
        this.factsWriter = newFactsWriter;
    }

}
