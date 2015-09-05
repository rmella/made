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
package com.velonuboso.made.core.abm.implementation.piece.condition;

import com.velonuboso.made.core.abm.api.IBlackBoard;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.condition.IConditionFear;
import static com.velonuboso.made.core.abm.implementation.piece.Piece.BLACKBOARD_AFFINITY_MATRIX;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ConditionFear extends BaseCondition implements IConditionFear{

    @Override
    public boolean test(IBlackBoard blackBoard) {
        int currentCharacterPosition = getMap().getCell(character);
        List<Integer> cellsToLookAt = getMap().getCellsAround(currentCharacterPosition, 1);
        
        HashMap<ICharacter, Float> affinityMatrix = 
                (HashMap<ICharacter, Float>) blackBoard.getObject(BLACKBOARD_AFFINITY_MATRIX);
        
        ICharacter enemy;
        enemy = cellsToLookAt.stream()
                .map(cell->getMap().getCharacter(cell))
                .filter(piece->piece!=null && piece != character && piece.getShape().wins(character.getShape()))
                .min((ICharacter firstCharacter, ICharacter secondCharacter) -> {
                    return Float.compare(affinityMatrix.get(firstCharacter), 
                            affinityMatrix.get(secondCharacter));
                })
                .orElse(null);
        
        // TODO store enemy cell in the blackboard
        // TODO predicate 
    
        return enemy != null;
    }
}
