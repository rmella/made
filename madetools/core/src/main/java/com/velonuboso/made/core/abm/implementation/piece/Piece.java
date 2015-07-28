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
package com.velonuboso.made.core.abm.implementation.piece;

import com.velonuboso.made.core.abm.api.IBehaviourTree;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Piece implements ICharacter {

    private String name;
    private Integer id;
    private IEventsWriter eventsWriter;
    private IBehaviourTree behaviourTree;
    
    private Color foregroundColor;
    private Color backgroundColor;
    
    public Piece() {
        name = "Kroo";
        id = null;
        eventsWriter = null;
        InitializeBehaviourTree();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String newName) {
        name = newName;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final int newId) {
        this.id = newId;
    }

    @Override
    public void setEventsWriter(final IEventsWriter newEventsWriter) {
        this.eventsWriter = newEventsWriter;
    }

    private void InitializeBehaviourTree() {
        behaviourTree = ObjectFactory.createObject(IBehaviourTree.class);
        behaviourTree.setEventsWriter(eventsWriter);
    }

    @Override
    public IBehaviourTree getBehaviourTree() {
        return behaviourTree;
    }
}
