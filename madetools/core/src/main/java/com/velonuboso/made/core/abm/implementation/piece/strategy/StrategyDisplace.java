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
import com.velonuboso.made.core.abm.api.strategy.IStrategyDisplace;
import com.velonuboso.made.core.abm.implementation.piece.BaseAction;
import com.velonuboso.made.core.abm.implementation.piece.Piece;
import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class StrategyDisplace extends BaseAction implements IStrategyDisplace {

    private int characterCell;
    private int targetCell;
    private Integer cellToDisplace;
    
    @Override
    public boolean test(IBlackBoard currentBlackboard, IBlackBoard oldBlackBoard) {
        
        retrieveTargetCellFromBlackboard(currentBlackboard);
        if (!targetCellIsAround()){
            return false;
        }
        
        calculateFreeCellToDisplaceTarget();
        if (cellToDisplace==null){
            return false;
        }

        displaceTarget();
        moveCharacterToTarget();
        return true;
    }

    private void retrieveTargetCellFromBlackboard(IBlackBoard currentBlackboard) {
        targetCell = currentBlackboard.getInt(Piece.BLACKBOARD_CHARACTER_CELL);
    }

    private boolean targetCellIsAround() {
        IMap map = getCharacter().getMap();
        characterCell = map.getCell(getCharacter());
        List<Integer> cellsAround = map.getCellsAround(characterCell, 1);
        return cellsAround.contains(targetCell);
    }

    private void calculateFreeCellToDisplaceTarget() {
        ICharacter targetCharacter = getMap().getCharacter(targetCell);
        List<Integer> cellsAroundTargetCharacter = targetCharacter.getMap().getCellsAround(targetCell, 1);
        cellToDisplace = cellsAroundTargetCharacter.stream()
                .filter(cell->getMap().getCharacter(cell)==null)
                .findFirst()
                .orElse(null);
    }
    
    private void displaceTarget() {
        getMap().moveCharacter(targetCell, cellToDisplace);
    }

    private void moveCharacterToTarget() {
        getMap().moveCharacter(characterCell, targetCell);
    }
}

