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
import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.CharacterShape;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.implementation.BehaviourTreeNode;
import com.velonuboso.made.core.abm.implementation.BlackBoard;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
    private ArrayList<ICharacter> allCharacters;
    private IBlackBoard blackBoard;
    private CharacterShape shape;

    private IProbabilityHelper probabilityHelper;
    private PieceAbmConfigurationHelper abmConfigurationHelper;

    public static final String BLACKBOARD_AFFINITY_MATRIX = "BLACKBOARD_AFFINITY_MATRIX";

    public Piece() {
        probabilityHelper = ObjectFactory.createObject(IProbabilityHelper.class);
        id = null;
        eventsWriter = null;
        InitializeBehaviourTree();

        foregroundColor = probabilityHelper.getRandomColor();
        backgroundColor = probabilityHelper.getRandomColor();

        allCharacters = null;
        blackBoard = ObjectFactory.createObject(IBlackBoard.class);
    }

    @Override
    public void setMap(IMap map) {
        this.map = map;
    }

    public IMap getMap() {
        return map;
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
        return getColorDifference(foregroundColor, backgroundColor);
    }

    @Override
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void setAbmConfiguration(AbmConfigurationEntity abmConfiguration) {
        this.abmConfigurationHelper = new PieceAbmConfigurationHelper(abmConfiguration);
    }

    @Override
    public CharacterShape getShape() {
        return shape;
    }

    @Override
    public void setShape(CharacterShape shape) {
        this.shape = shape;
    }

    @Override
    public void run() {
        rootNode.run(blackBoard);
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
        HashMap<ICharacter, Float> affinityMatrix = new HashMap<>();

        getAllCharacters().stream().filter(character -> character != this).forEach(character
                -> affinityMatrix.put(character, getAffinityWithCharacter(character)));
        blackBoard.setObject(BLACKBOARD_AFFINITY_MATRIX, affinityMatrix);
    }

    private void resetJoy() {
    }

    private ArrayList<ICharacter> getAllCharacters() {
        if (allCharacters == null) {
            retrieveCharactersFromMap();
        }
        return allCharacters;
    }

    private void retrieveCharactersFromMap() {
        int numberOfCells = map.getWidth() * map.getHeight();

        allCharacters = new ArrayList<>();
        for (int positionIterator = 0; positionIterator < numberOfCells; positionIterator++) {
            retrieveCharacterFromCell(positionIterator);
        }
        orderAllCharactersById();
    }

    private void retrieveCharacterFromCell(int positionIterator) {
        ICharacter character = map.getCharacter(positionIterator);
        if (character != null) {
            allCharacters.add(character);
        }
    }

    private void orderAllCharactersById() {
        allCharacters.sort((firstElement, secondElement) -> firstElement.getId().compareTo(secondElement.getId()));
    }

    private Float getAffinityWithCharacter(ICharacter target) {
        float shapeSimilarity = this.getShape() == target.getShape() ? 1 : -1;
        float shapeSimilarityWeight = abmConfigurationHelper.getShapeSimilarityWeight();

        float foregroundColorSimilarity = 1f - getColorDifference(this.getForegroundColor(), target.getForegroundColor());
        foregroundColorSimilarity = normalize(foregroundColorSimilarity, 0f, 1f, -1, 1);
        float foregroundColorSimilarityWeight = abmConfigurationHelper.getForegroundColorSimilarityWeight();

        float backgroundColorSimilarity = 1f - getColorDifference(this.getBackgroundColor(), target.getBackgroundColor());
        backgroundColorSimilarity = normalize(backgroundColorSimilarity, 0f, 1f, -1, 1);
        float backgroundColorSimilarityWeight = abmConfigurationHelper.getBackgroundColorSimilarityWeight();

        return ((shapeSimilarity * shapeSimilarityWeight)
                + (foregroundColorSimilarity * foregroundColorSimilarityWeight)
                + (backgroundColorSimilarity * backgroundColorSimilarityWeight)
                )/3f;
    }

    private static float getColorDifference(Color source, Color target) {
        double diffRed = Math.abs(source.getRed() - target.getRed());
        double diffBlue = Math.abs(source.getBlue() - target.getBlue());
        double diffGreen = Math.abs(source.getGreen() - target.getGreen());

        return (float) ((diffBlue + diffGreen + diffRed) / 3f);
    }
    
    private static float normalize (float value, float sourceMinimum,
            float sourceMaximum, float targetMinimum, float targetMaximum){
        return ((targetMaximum - targetMinimum) * (value - sourceMinimum))/(sourceMaximum - sourceMinimum) + targetMinimum;
    }
}
