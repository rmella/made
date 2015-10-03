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
package com.velonuboso.made.core.inference;

import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class PuppetCharacter implements ICharacter{
        int id;
        Color background;
        Color foreground;
        CharacterShape shape;
        
        @Override
        public void setId(int id) {
            this.id = id;
        }

        @Override
        public void setEventsWriter(IEventsWriter eventsWriter) {
        }

        @Override
        public void setMap(IMap map) {
        }

        @Override
        public void setAbmConfiguration(AbmConfigurationEntity abmConfiguration) {
        }

        @Override
        public void setShape(CharacterShape shape) {
            this.shape = shape;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public IBehaviourTreeNode getBehaviourTree() {
            return null;
        }

        @Override
        public IMap getMap() {
            return null;
        }

        @Override
        public CharacterShape getShape() {
            return shape;
        }

        @Override
        public IEventsWriter getEventsWriter() {
            return null;
        }

        @Override
        public Color getBackgroundColor() {
            return background;
        }

        @Override
        public Color getForegroundColor() {
            return foreground;
        }

        @Override
        public float getColorDifference() {
            return 0;
        }

        @Override
        public AbmConfigurationEntity getAbmConfiguration() {
            return null;
        }

        @Override
        public void setForegroundColor(Color foregroundColor) {
            this.foreground = foregroundColor;
        }

        @Override
        public void setBackgroundColor(Color backgroundColor) {
            this.background = backgroundColor;
        }

        @Override
        public void applyColorChange() {
        }

        @Override
        public boolean run() {
            return true;
        }
    }
