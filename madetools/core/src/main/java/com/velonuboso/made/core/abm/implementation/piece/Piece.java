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

import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.function.Consumer;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Piece implements ICharacter {

    private Integer id;
    private IEventsWriter eventsWriter;
    private IBehaviourTreeNode rootNode;
    private IMap map;

    private Color foregroundColor;
    private Color backgroundColor;

    IProbabilityHelper probabilityHelper;

    public Piece() {
        probabilityHelper = ObjectFactory.createObject(IProbabilityHelper.class);
        id = null;
        eventsWriter = null;
        InitializeBehaviourTree();

        foregroundColor = probabilityHelper.getRandomColor();
        backgroundColor = probabilityHelper.getRandomColor();
    }

    @Override
    public void setMap(IMap map) {
        this.map = map;
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

    @Override
    public IBehaviourTreeNode getBehaviourTree() {
        return rootNode;
    }

    @Override
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public Color getForegroundColor() {
        return foregroundColor;
    }

    @Override
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    @Override
    public float getColorDifference() {
        double diffRed = Math.abs(foregroundColor.getRed() - backgroundColor.getRed());
        double diffBlue = Math.abs(foregroundColor.getBlue() - backgroundColor.getBlue());
        double diffGreen = Math.abs(foregroundColor.getGreen() - backgroundColor.getGreen());

        return (float) (diffBlue + diffGreen + diffRed) / 3f;
    }

    @Override
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    private void InitializeBehaviourTree() {
        rootNode = buildSimpleNodeForCharacter();
        addBlackBoardInitializerToNode(rootNode);
        addChildForSurpriseBehaviour(rootNode);
        addChildForFearBehaviour(rootNode);
        addChildForAnticipationBehaviour(rootNode);
        addChildForSadnessBehaviour(rootNode);
        addChildForFallbackBehaviour(rootNode);
    }

    private void addBlackBoardInitializerToNode(IBehaviourTreeNode node) {
        node.setActionWhenRun(new Consumer<IBehaviourTreeNode>() {
            @Override
            public void accept(IBehaviourTreeNode node) {
                initializeBlackboard();
            }
        });
    }

    private void addChildForSurpriseBehaviour(IBehaviourTreeNode parentNode) {
    }

    private void addChildForFearBehaviour(IBehaviourTreeNode parentNode) {
    }

    private void addChildForAnticipationBehaviour(IBehaviourTreeNode parentNode) {
    }

    private void addChildForSadnessBehaviour(IBehaviourTreeNode parentNode) {
    }

    private void addChildForFallbackBehaviour(IBehaviourTreeNode parentNode) {
    }
    
    private void initializeBlackboard() {
        resetAffinityMatrix();
        resetJoy();
    }
    
    private IBehaviourTreeNode buildSimpleNodeForCharacter() {
        IBehaviourTreeNode node = ObjectFactory.createObject(IBehaviourTreeNode.class);
        node.setCharacter(this);
        return node;
    }

    private void resetAffinityMatrix() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void resetJoy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
