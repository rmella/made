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

package com.velonuboso.made.core.abm.api;

import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.util.ImplementedBy;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
@ImplementedBy(targetClass = Piece.class, targetMode = ImplementedBy.Mode.NORMAL)
public interface ICharacter {

    public void setId(int id);
    public void setEventsWriter(IEventsWriter eventsWriter);
    public void setMap(IMap map);
    public void setAbmConfiguration(AbmConfigurationEntity abmConfiguration);
    public void setShape (CharacterShape shape);
    
    public Integer getId();
    public IBehaviourTreeNode getBehaviourTree();
    public IMap getMap();
    public CharacterShape getShape();
    
    public IEventsWriter getEventsWriter();
    public Color getBackgroundColor();
    public Color getForegroundColor();
    public float getColorDifference ();
    public AbmConfigurationEntity getAbmConfiguration();
    
    public void setForegroundColor(Color foregroundColor);
    public void setBackgroundColor(Color backgroundColor);
    
    public void applyColorChange();
    public boolean run();
}
