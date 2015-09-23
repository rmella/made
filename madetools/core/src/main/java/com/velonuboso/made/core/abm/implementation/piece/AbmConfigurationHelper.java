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

import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class AbmConfigurationHelper {

    private AbmConfigurationEntity abmConfiguration;
    private int numberOfGenesForPiece;
    private int numberOfGenesForWorld;
    private int numberOfPieceType;

    private AbmConfigurationHelperWorld worldAbmConfigurationHelper;
    private HashMap<CharacterShape, AbmConfigurationHelperPiece> pieceAbmConfigurationHelpers;

    public AbmConfigurationHelper(AbmConfigurationEntity abmConfiguration) {
        this.abmConfiguration = abmConfiguration;
        numberOfGenesForWorld = AbmConfigurationHelperWorld.Gene.values().length;
        numberOfGenesForPiece = AbmConfigurationHelperPiece.Gene.values().length;
        numberOfPieceType = CharacterShape.values().length;
    }

    public void prepare() throws Exception {
        validateNumberOfGenes();
        setAndValidateTypeOfGenes();
    }

    private void validateNumberOfGenes() throws Exception {
        int expectedNumberOfGenes = numberOfGenesForWorld + (numberOfPieceType * numberOfGenesForPiece);
        int currentNumberOfGenes = abmConfiguration.getChromosome().length;

        if (expectedNumberOfGenes != currentNumberOfGenes) {
            throw new Exception(
                    "Currently found " + currentNumberOfGenes + " genes. "
                    + "Should've received " + expectedNumberOfGenes + " genes:\n"
                    + " - " + numberOfGenesForWorld + " for the world\n"
                    + " - " + numberOfGenesForPiece + " for every type of piece (" + numberOfPieceType + ")"
            );
        }
    }

    private void setAndValidateTypeOfGenes() throws Exception {
        float[] worldConfiguration = Arrays.copyOfRange(abmConfiguration.getChromosome(), 0, numberOfGenesForWorld);
        worldAbmConfigurationHelper = new AbmConfigurationHelperWorld(new AbmConfigurationEntity(worldConfiguration));
        worldAbmConfigurationHelper.validateTypes();

        pieceAbmConfigurationHelpers = new HashMap<>();
        for (CharacterShape shape : CharacterShape.values()) {
            processShape(shape);
        }
    }

    private void processShape(CharacterShape shape) throws Exception {
        int initialPosition = shape.ordinal() * numberOfGenesForPiece + numberOfGenesForWorld;
        int endPositionExclusive = (shape.ordinal() + 1) * numberOfGenesForPiece + numberOfGenesForWorld;

        float[] pieceConfiguration = Arrays.copyOfRange(abmConfiguration.getChromosome(),
                initialPosition, endPositionExclusive);
        AbmConfigurationHelperPiece pieceAbmConfigurationHelper = new AbmConfigurationHelperPiece(
                new AbmConfigurationEntity(pieceConfiguration)
        );
        try {
            pieceAbmConfigurationHelper.validateTypes();
        } catch (Exception ex) {
            throw new Exception("Found error in chromosome part (from " + initialPosition
                    + " to " + endPositionExclusive + ". " + ex.getMessage());
        }
        pieceAbmConfigurationHelpers.put(shape, pieceAbmConfigurationHelper);
    }
}
