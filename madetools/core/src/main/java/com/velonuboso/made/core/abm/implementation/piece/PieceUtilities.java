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

import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class PieceUtilities {
    
    public static float calculateColorDifference(Color source, Color target) {
        double diffRed = Math.abs(source.getRed() - target.getRed());
        double diffBlue = Math.abs(source.getBlue() - target.getBlue());
        double diffGreen = Math.abs(source.getGreen() - target.getGreen());

        return (float) ((diffBlue + diffGreen + diffRed) / 3f);
    }
    
    public static float normalize(float value, float sourceMinimum,
            float sourceMaximum, float targetMinimum, float targetMaximum) {

        if (sourceMaximum - sourceMinimum == 0) {
            return 0;
        }
        return ((targetMaximum - targetMinimum) * (value - sourceMinimum))
                / (sourceMaximum - sourceMinimum)
                + targetMinimum;
    }
}
