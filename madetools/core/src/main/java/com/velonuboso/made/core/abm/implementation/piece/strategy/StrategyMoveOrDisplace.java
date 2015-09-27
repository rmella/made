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
package com.velonuboso.made.core.abm.implementation.piece.strategy;

import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.strategy.IStrategyMoveOrDisplace;
import com.velonuboso.made.core.abm.entity.ActionReturnException;
import com.velonuboso.made.core.abm.implementation.piece.BaseAction;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class StrategyMoveOrDisplace extends BaseStrategy implements IStrategyMoveOrDisplace {

    int characterCell;
    int targetCell;
    ICharacter targetCharacter;
    boolean targetCellIsAround;
    Integer cellToDisplace;

    @Override
    public boolean testAction(IBlackBoard currentBlackBoard, IBlackBoard oldBlackBoard)
            throws ActionReturnException {

        retrieveCurrentCharacterCell();
        retrieveTargetCellFromBlackboard(currentBlackBoard);
        validatePieceIsInAlreadyTargetCell();

        HashMap<ICharacter, Float> affinityMatrix = 
                (HashMap<ICharacter, Float>) currentBlackBoard.getObject(Piece.BLACKBOARD_AFFINITY_MATRIX);
        replaceTargetCellByTheCloser(affinityMatrix);

        validatePieceIsInAlreadyTargetCell();
        
        if (getMap().getCharacter(targetCell) == null) {
            moveCharacterToTarget();
            writeMoveEvent();
            return true;
        }

        calculateFreeCellToDisplaceTarget();
        if (cellToDisplace == null) {
            return false;
        }

        displaceTarget();
        writeDisplaceEvent();
        moveCharacterToTarget();
        writeMoveEvent();
        return true;
    }

    private void replaceTargetCellByTheCloser(HashMap<ICharacter, Float> affinityMatrix) {
        if (!isTargetCellAround()) {
            targetCell = getMap().getCloserCell(getCharacter(), targetCell, affinityMatrix);
        }
    }

    private void validatePieceIsInAlreadyTargetCell() throws ActionReturnException {
        if (pieceIsInTargetCell()) {
            throw new ActionReturnException(false);
        }
    }

    private void retrieveTargetCellFromBlackboard(IBlackBoard currentBlackboard) {
        targetCell = currentBlackboard.getInt(Piece.BLACKBOARD_TARGET_CELL);
    }

    private boolean isTargetCellAround() {
        IMap map = getCharacter().getMap();
        characterCell = map.getCell(getCharacter());
        List<Integer> cellsAround = map.getCellsAround(characterCell, 1);
        return cellsAround.contains(targetCell);
    }

    protected boolean targetCellHasCharacter() {
        return getMap().getCharacter(targetCell) != null;
    }

    private boolean pieceIsInTargetCell() {
        return targetCell == characterCell;
    }

    private void calculateFreeCellToDisplaceTarget() {
        targetCharacter = getMap().getCharacter(targetCell);
        List<Integer> cellsAroundTargetCharacter = targetCharacter.getMap().getCellsAround(targetCell, 1);
        cellToDisplace = cellsAroundTargetCharacter.stream()
                .filter(cell -> getMap().getCharacter(cell) == null)
                .findFirst()
                .orElse(null);
    }

    private void displaceTarget() {
        getMap().moveCharacter(targetCell, cellToDisplace);
    }

    private void moveCharacterToTarget() {
        getMap().moveCharacter(characterCell, targetCell);
    }

    private void writeMoveEvent() {
        IEventFactory factory = ObjectFactory.createObject(IEventFactory.class);
        IEvent event = factory.moves(getCharacter(), targetCell);
        getCharacter().getEventsWriter().add(event);
    }

    private void writeDisplaceEvent() {
        IEventFactory factory = ObjectFactory.createObject(IEventFactory.class);
        IEvent event = factory.displaces(getCharacter(), targetCharacter, cellToDisplace);
        getCharacter().getEventsWriter().add(event);
    }

    private void retrieveCurrentCharacterCell() {
        characterCell = getMap().getCell(getCharacter());
    }
}
