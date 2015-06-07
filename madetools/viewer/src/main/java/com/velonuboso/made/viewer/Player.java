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

import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static com.velonuboso.made.viewer.Player.BOARD_MARGIN;
import com.velonuboso.made.entity.EventsLog;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.application.Platform;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */

public class Player extends Application {

    public static final int BOARD_MARGIN = 5;
    public static final String STAGE_TITLE = "MADE Viewer";
    public static int SCENE_WIDTH = 700;
    public static int SCENE_HEIGHT = 700;
    
    private static String[] arguments;

    public static void main(String[] args) {
        if (args== null || args.length ==0){
            throw new RuntimeException("Please, provide a file name");
        }
        launch(args);
    }
    
    private Pane boardPane;
    private Pane outerPane;
    private Scene scene;
    private Stage primaryStage;
    private Canvas canvas;
    private EventsLog eventsLog;
    private Cell cellMatrix[][];
    private Random random = new Random();
    
    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        configureEvents(arguments[0]);

        createBoardPane();
        createCanvas();
        createScene();
        createCellMatrix();
        
        printGrid();

        primaryStage.setTitle(STAGE_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        executeEvents();
    }
    
    private void configureEvents(String fileName){
        try {
            final Gson gson = new Gson();
            final String json = new String(Files.readAllBytes(Paths.get(fileName)));
            eventsLog = gson.fromJson(json, EventsLog.class);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            Platform.exit();
        }
    }

    private void printGrid() {
        for (int iterX = 0; iterX < eventsLog.getBoard().getGridSize(); iterX++) {
            for (int iterY = 0; iterY < eventsLog.getBoard().getGridSize(); iterY++) {
                cellMatrix[iterX][iterY].draw();
            }
        }
    }

    private int getCellSize() {
        final int boardPaneSize = (int) Math.min(boardPane.getWidth(), boardPane.getHeight());
        final int boardPaneSizeWithoutBorder = boardPaneSize - (BOARD_MARGIN * 2);
        
        return boardPaneSizeWithoutBorder / eventsLog.getBoard().getGridSize();
    }

    private void createBoardPane() {
        boardPane = new Pane();
    }

    private void createCanvas() {
        canvas = new Canvas();
        boardPane.getChildren().add(canvas);
    }

    private void createScene() {
        scene = new Scene(boardPane, SCENE_WIDTH, SCENE_HEIGHT);
    }

    private void createCellMatrix() {
        final int cellSizeInPixels = getCellSize();

        final int gridSize = eventsLog.getBoard().getGridSize();
        
        cellMatrix = new Cell[gridSize][gridSize];
        for (int iterX = 0; iterX < gridSize; iterX++) {
            cellMatrix[iterX] = new Cell[gridSize];
            for (int iterY = 0; iterY < gridSize; iterY++) {
                cellMatrix[iterX][iterY] = new Cell(boardPane, iterX, iterY,
                        cellSizeInPixels);
            }
        }
    }

    /*
    private void sampleRun() {
    Token tokenA = new Token("Tremmie Yesoig", boardPane, random);
    Token tokenB = new Token("Zhen Suwah", boardPane, random);
    tokenA.draw(cellMatrix[0][0]);
    tokenB.draw(cellMatrix[1][5]);
    tokenA.move(cellMatrix[5][8]);
    tokenB.move(cellMatrix[9][3]);
    tokenA.move(cellMatrix[2][9]);
    tokenB.move(cellMatrix[9][4]);
    tokenA.move(cellMatrix[4][4]);
    tokenB.move(cellMatrix[3][4]);
    tokenA.say("Hello!!");
    tokenB.say("How are you?");
    tokenA.say("Fine!");
    }*/
    private void executeEvents() {
        
    }

}
