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

package com.velonuboso.made.viewer;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import static com.velonuboso.made.viewer.Player.BOARD_MARGIN;


/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */

public class Cell {

    private Pane mainPane;
    private int indexX;
    private int indexY;
    int positionX;
    int positionY;
    private int cellSize;
    private Rectangle shape;

    public Cell(Pane pane, int indexX, int indexY, int size) {
        this.mainPane = pane;
        this.indexX = indexX;
        this.indexY = indexY;
        this.cellSize = size;
        
        positionX = indexX * cellSize + BOARD_MARGIN;
        positionY = indexY * cellSize + BOARD_MARGIN;

        shape = new Rectangle(positionX, positionY, cellSize, cellSize);
    }

    public int getCenterPositionX() {
        return positionX + (cellSize / 2);
    }

    public int getCenterPositionY() {
        return positionY + (cellSize / 2);
    }

    public int getSize() {
        return cellSize;
    }

    public void draw() {
        configureRectangle();
        mainPane.getChildren().add(shape);
    }

    private void configureRectangle() {
        shape.setStroke(Color.WHITE);
        shape.setFill(Color.BEIGE);
        shape.setStrokeWidth(2);
        shape.setArcWidth(0);
        shape.setArcHeight(0);
    }
}