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

import com.velonuboso.made.core.abm.api.IAction;
import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.condition.IConditionAnticipation;
import com.velonuboso.made.core.abm.api.condition.IConditionCanImproveFriendSimilarity;
import com.velonuboso.made.core.abm.api.condition.IConditionFear;
import com.velonuboso.made.core.abm.api.condition.IConditionSadness;
import com.velonuboso.made.core.abm.api.condition.IConditionSurprise;
import com.velonuboso.made.core.abm.api.strategy.IStrategyMoveOrDisplace;
import com.velonuboso.made.core.abm.api.strategy.IStrategyMoveAway;
import com.velonuboso.made.core.abm.api.strategy.IStrategySkipTurn;
import com.velonuboso.made.core.abm.api.strategy.IStrategyStain;
import com.velonuboso.made.core.abm.api.strategy.IStrategyTransferColor;
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.implementation.BehaviourTreeNode;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiPredicate;
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
    private CharacterShape shape;

    private IProbabilityHelper probabilityHelper;
    private AbmConfigurationEntity abmConfigurationEntity;
    private AbmConfigurationHelperPiece abmConfigurationHelper;
    private IBlackBoard pieceCurrentBlackBoard;

    public static final String BLACKBOARD_AFFINITY_MATRIX = "BLACKBOARD_AFFINITY_MATRIX";
    public static final String BLACKBOARD_JOY = "BLACKBOARD_JOY";
    public static final String BLACKBOARD_TARGET_CELL = "BLACKBOARD_CHARACTER_CELL";

    public Piece() {
        probabilityHelper = ObjectFactory.createObject(IProbabilityHelper.class);
        id = null;
        eventsWriter = null;
        TryInitializeBehaviourTree();

        foregroundColor = probabilityHelper.getRandomColor();
        backgroundColor = probabilityHelper.getRandomColor();

        allCharacters = null;
        pieceCurrentBlackBoard = null;
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
    public IEventsWriter getEventsWriter() {
        return eventsWriter;
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
        return PieceUtilities.calculateColorDifference(foregroundColor, backgroundColor);
    }

    @Override
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void setAbmConfiguration(AbmConfigurationEntity abmConfiguration) {
        this.abmConfigurationEntity = abmConfiguration;
        this.abmConfigurationHelper = new AbmConfigurationHelperPiece(abmConfiguration);
        TryInitializeBehaviourTree();
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
    public boolean run() {
        IBlackBoard oldBlackBoard = pieceCurrentBlackBoard;
        pieceCurrentBlackBoard = newEmptyBlackBoard();
        return rootNode.run(pieceCurrentBlackBoard, oldBlackBoard);
    }

    public void setBehaviourTree(IBehaviourTreeNode node) {
        rootNode = buildSimpleNodeForCharacter();
        addBlackBoardInitializerToNode(rootNode);
        rootNode.addChildNode(node);
    }

    @Override
    public AbmConfigurationEntity getAbmConfiguration() {
        return abmConfigurationEntity;
    }

    // <editor-fold defaultstate="collapsed" desc="Private methods">
    private void TryInitializeBehaviourTree() {
        
        if (abmConfigurationEntity== null){
            return;
        }
        
        rootNode = buildSimpleNodeForCharacter();
        addBlackBoardInitializerToNode(rootNode);

        addChildForSurpriseBehaviour(rootNode);
        addChildForFearBehaviour(rootNode);
        addChildForAnticipationBehaviour(rootNode);
        addChildForSadnessBehaviour(rootNode);
        addChildForFallbackBehaviour(rootNode);
    }

    private void addBlackBoardInitializerToNode(IBehaviourTreeNode node) {
        node.setAction((IBlackBoard currentBlackBoard, IBlackBoard oldBlackBoard) -> {
            initializeBlackboard(currentBlackBoard);
            return true;
        });
    }

    private void addChildForSurpriseBehaviour(IBehaviourTreeNode parentNode) {
        IBehaviourTreeNode surpriseCondition = createActionNode(
                ObjectFactory.createObject(IConditionSurprise.class),
                abmConfigurationHelper.getSurpriseProbability());
        IBehaviourTreeNode moveAwayStrategy = createActionNode(
                ObjectFactory.createObject(IStrategyMoveAway.class), 1);

        parentNode.addChildNode(surpriseCondition);
        surpriseCondition.addChildNode(moveAwayStrategy);
    }

    private void addChildForFearBehaviour(IBehaviourTreeNode parentNode) {
        IBehaviourTreeNode fearCondition = createActionNode(
                ObjectFactory.createObject(IConditionFear.class),
                abmConfigurationHelper.getFearProbability());

        IBehaviourTreeNode skipTurnStrategy = createActionNode(
                ObjectFactory.createObject(IStrategySkipTurn.class), 1);

        parentNode.addChildNode(fearCondition);
        fearCondition.addChildNode(skipTurnStrategy);
    }

    private void addChildForAnticipationBehaviour(IBehaviourTreeNode parentNode) {
        IBehaviourTreeNode anticipationCondition = createActionNode(
                ObjectFactory.createObject(IConditionAnticipation.class),
                abmConfigurationHelper.getAnticipationProbability());

        IBehaviourTreeNode moveOrDisplaceStrategy = createActionNode(ObjectFactory.createObject(IStrategyMoveOrDisplace.class), 1);

        IBehaviourTreeNode stainStrategy = createActionNode(
                ObjectFactory.createObject(IStrategyStain.class), 1);

        parentNode.addChildNode(anticipationCondition);
        anticipationCondition.addChildNode(moveOrDisplaceStrategy);
        anticipationCondition.addChildNode(stainStrategy);
    }

    private void addChildForSadnessBehaviour(IBehaviourTreeNode parentNode) {
        IBehaviourTreeNode sadnessCondition = createActionNode(
                ObjectFactory.createObject(IConditionSadness.class),
                abmConfigurationHelper.getSadnessProbability());

        parentNode.addChildNode(sadnessCondition);
        addChildForImprovingFriendSimilarityBehaviour(sadnessCondition);
        addChildForReducingEnemySimilarityBehaviour(sadnessCondition);
        addChildForImprovingSelfSimilarityBehaviour(sadnessCondition);
    }

    private void addChildForImprovingFriendSimilarityBehaviour(IBehaviourTreeNode parentNode) {
        IBehaviourTreeNode canImproveFriendSimilarityCondition = createActionNode(
                ObjectFactory.createObject(IConditionCanImproveFriendSimilarity.class),
                abmConfigurationHelper.getImprovingFriendSimilarityProbability());

        IBehaviourTreeNode moveOrDisplaceStrategy = createActionNode(ObjectFactory.createObject(IStrategyMoveOrDisplace.class), 1);

        IBehaviourTreeNode transferColorStrategy = createActionNode(
                ObjectFactory.createObject(IStrategyTransferColor.class), 1);

        parentNode.addChildNode(canImproveFriendSimilarityCondition);
        canImproveFriendSimilarityCondition.addChildNode(moveOrDisplaceStrategy);
        canImproveFriendSimilarityCondition.addChildNode(transferColorStrategy);
    }

    private void addChildForReducingEnemySimilarityBehaviour(IBehaviourTreeNode parentNode) {
        IBehaviourTreeNode canReduceEnemySimilarityCondition = createActionNode(
                ObjectFactory.createObject(IConditionCanImproveFriendSimilarity.class),
                abmConfigurationHelper.getReducingEnemySimilarityProbability());

        IBehaviourTreeNode moveOrDisplaceStrategy = 
                createActionNode(ObjectFactory.createObject(IStrategyMoveOrDisplace.class), 1);

        parentNode.addChildNode(canReduceEnemySimilarityCondition);
        canReduceEnemySimilarityCondition.addChildNode(moveOrDisplaceStrategy);
    }

    private void addChildForImprovingSelfSimilarityBehaviour(IBehaviourTreeNode parentNode) {
        IBehaviourTreeNode canImproveSelfSimilarityCondition = createActionNode(
                ObjectFactory.createObject(IConditionCanImproveFriendSimilarity.class),
                abmConfigurationHelper.getImprovingSelfSimilarityProbability());

        IBehaviourTreeNode moveOrDisplaceStrategy = createActionNode(ObjectFactory.createObject(IStrategyMoveOrDisplace.class), 1);

        IBehaviourTreeNode stainStrategy = createActionNode(
                ObjectFactory.createObject(IStrategyStain.class), 1);

        parentNode.addChildNode(canImproveSelfSimilarityCondition);
        canImproveSelfSimilarityCondition.addChildNode(moveOrDisplaceStrategy);
        canImproveSelfSimilarityCondition.addChildNode(stainStrategy);
    }

    private void addChildForFallbackBehaviour(IBehaviourTreeNode parentNode) {
        IBehaviourTreeNode skipTurnStrategy = createActionNode(
                ObjectFactory.createObject(IStrategySkipTurn.class), 1);
        parentNode.addChildNode(skipTurnStrategy);
    }

    private void initializeBlackboard(IBlackBoard blackBoard) {
        resetAffinityMatrix(blackBoard);
        resetJoy(blackBoard);
    }

    private IBehaviourTreeNode buildSimpleNodeForCharacter() {
        IBehaviourTreeNode node = ObjectFactory.createObject(IBehaviourTreeNode.class);
        node.setCharacter(this);
        return node;
    }

    private void resetAffinityMatrix(IBlackBoard blackBoard) {
        HashMap<ICharacter, Float> affinityMatrix = new HashMap<>();

        getAllCharacters().stream().filter(character -> character != this).forEach(character
                -> affinityMatrix.put(character, calculateAffinityWithCharacter(character)));

        blackBoard.setObject(BLACKBOARD_AFFINITY_MATRIX, affinityMatrix);
    }

    private void resetJoy(IBlackBoard blackBoard) {
        float joy = calculateJoy(blackBoard);
        blackBoard.setFloat(BLACKBOARD_JOY, joy);
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

    private Float calculateAffinityWithCharacter(ICharacter target) {
        float sumComponentes = getWeightedShapeSimilarityWithCharacter(target)
                + getWeightedForegroundColorSimilarityWithCharacter(target)
                + getWeightedBackgroundColorSimilarityWithCharacter(target);
        return PieceUtilities.normalize(sumComponentes, 0, getMaximumWeightSum(), -1, 1);
    }

    private float getWeightedShapeSimilarityWithCharacter(ICharacter target) {
        float shapeSimilarity = this.getShape() == target.getShape() ? 1 : 0;
        float shapeSimilarityWeight = abmConfigurationHelper.getShapeSimilarityWeight();
        return shapeSimilarity * shapeSimilarityWeight;
    }

    private float getWeightedForegroundColorSimilarityWithCharacter(ICharacter target) {
        float foregroundColorSimilarity = 1f - PieceUtilities.calculateColorDifference(this.getForegroundColor(), target.getForegroundColor());
        float foregroundColorSimilarityWeight = abmConfigurationHelper.getForegroundColorSimilarityWeight();
        return foregroundColorSimilarity * foregroundColorSimilarityWeight;
    }

    private float getWeightedBackgroundColorSimilarityWithCharacter(ICharacter target) {
        float backgroundColorSimilarity = 1f - PieceUtilities.calculateColorDifference(this.getBackgroundColor(), target.getBackgroundColor());
        float backgroundColorSimilarityWeight = abmConfigurationHelper.getBackgroundColorSimilarityWeight();
        return backgroundColorSimilarity * backgroundColorSimilarityWeight;
    }

    private float getMaximumWeightSum() {
        return abmConfigurationHelper.getShapeSimilarityWeight()
                + abmConfigurationHelper.getForegroundColorSimilarityWeight()
                + abmConfigurationHelper.getBackgroundColorSimilarityWeight();
    }

    private float calculateJoy(IBlackBoard blackBoard) {
        float selfSimilarityForJoyWeight = abmConfigurationHelper.getSelfSimilarityForJoyWeight();
        float selfSimilarity = selfSimilarityForJoyWeight == 0 ? 0f : 1f - getColorDifference();

        float neighbourSimilarityForJoyWeight = abmConfigurationHelper.getNeighbourSimilarityForJoyWeight();
        float neighbourSimilarity = neighbourSimilarityForJoyWeight == 0 ? 0f : getNeighbourSimilarityPonderatedByAffinity(blackBoard);

        return (selfSimilarity * selfSimilarityForJoyWeight) + (neighbourSimilarity * neighbourSimilarityForJoyWeight);
    }

    private float getNeighbourSimilarityPonderatedByAffinity(IBlackBoard blackBoard) {
        HashMap<ICharacter, Float> affinityMatrix = (HashMap<ICharacter, Float>) blackBoard.getObject(BLACKBOARD_AFFINITY_MATRIX);
        return (float) affinityMatrix.keySet().stream()
                .mapToDouble(character -> (1f - character.getColorDifference()) * affinityMatrix.get(character))
                .average()
                .orElse(0f);
    }

    private IBlackBoard newEmptyBlackBoard() {
        return ObjectFactory.createObject(IBlackBoard.class);
    }
    // </editor-fold>

    private IBehaviourTreeNode createActionNode(IAction action, float probability) {
        IBehaviourTreeNode node = ObjectFactory.createObject(IBehaviourTreeNode.class);
        node.setAction(action);
        node.setCharacter(this);
        node.setProbability(probability);
        action.setCharacter(this);

        return node;
    }
}
