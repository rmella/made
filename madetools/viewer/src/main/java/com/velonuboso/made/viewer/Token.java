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
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;


/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */

public class Token {

    private final String name;
    private final Pane pane;
    private final Random random;
    
    private Group group;
    private Circle circle;
    private Text textName;
    private Text textSay;
    
    final static int DURATION_MOVE = 500;
    final static int TEXT_WRAPPING = 100;

    public Token(String name, Pane pane, Random random) {
        this.name = name;
        this.pane = pane;
        this.random = random;

        createStructure();
    }

    private void createStructure() {
        circle = new Circle();
        textName = new Text(name);
        textSay = new Text();
        group = new Group(circle, textName, textSay);
    }

    private DropShadow createDropShadow() {
        final int SHADOW_OFFSET = 2;
        final Color SHADOW_COLOR = Color.GRAY;

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetY(SHADOW_OFFSET);
        dropShadow.setOffsetX(SHADOW_OFFSET);
        dropShadow.setColor(SHADOW_COLOR);
        return dropShadow;
    }

    public void draw(Cell cell) {
        final int radius = calculateBestRadius(cell);
        createCircle(cell, radius);
        configureTextName(cell, radius);
        configureTextToSay(cell, radius);

        pane.getChildren().add(group);
    }

    private void configureTextToSay(Cell cell, int radius) {
        int textX = calculateTextCenterX(cell);
        int textSayY = calculateTextToSayCenterY(cell, radius);

        configureText(textSay, textX, textSayY);
    }

    private void configureTextName(Cell cell, int radius) {
        int textX = calculateTextCenterX(cell);
        int textNameY = calculateTextNameCenterY(cell, radius);
        configureText(textName, textX, textNameY);
    }

    private void configureText(Text targetText, int textX, int textY) {
        final int TEXT_FONT_SIZE = 10;
        final Color TEXT_COLOR = Color.BLACK;

        targetText.setTextAlignment(TextAlignment.CENTER);
        targetText.setWrappingWidth(100);
        targetText.setX(textX);
        targetText.setY(textY);
        targetText.setFont(new Font(TEXT_FONT_SIZE));
        targetText.setWrappingWidth(TEXT_WRAPPING);
        targetText.setFill(TEXT_COLOR);
        targetText.setOpacity(0);
    }

    private int calculateTextToSayCenterY(Cell cell, int radius) {
        final int TEXT_TO_SAY_VERTICAL_OFFSET = -5;

        int textSayY = cell.getCenterPositionY() - radius + TEXT_TO_SAY_VERTICAL_OFFSET;
        return textSayY;
    }

    private int calculateTextNameCenterY(Cell cell, int radius) {
        final int TEXT_NAME_VERTICAL_OFFSET = 15;

        int textNameY = cell.getCenterPositionY() + radius + TEXT_NAME_VERTICAL_OFFSET;
        return textNameY;
    }

    private int calculateTextCenterX(Cell cell) {
        final int TEXT_HORIZONTAL_OFFSET = -TEXT_WRAPPING / 2;

        int textX = cell.getCenterPositionX() + TEXT_HORIZONTAL_OFFSET;
        return textX;
    }

    private int calculateBestRadius(Cell cell) {
        final float CELL_OCCUPATION = 0.85f;

        int radius = (int) ((int) cell.getSize() * CELL_OCCUPATION / 2);
        return radius;
    }

    private void createCircle(Cell cell, int radius) {
        DropShadow dropShadow = createDropShadow();
        circle.setCenterX(cell.getCenterPositionX());
        circle.setCenterY(cell.getCenterPositionY());
        circle.setRadius(radius);
        circle.setEffect(dropShadow);
        circle.setFill(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1));
    }

    public void move(final Cell cell) {
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(
                        new Duration(DURATION_MOVE / 8),
                        new KeyValue(textName.opacityProperty(), 1)
                ),
                new KeyFrame(
                        new Duration(DURATION_MOVE * 7 / 8),
                        new KeyValue(textName.opacityProperty(), 1)
                ),
                new KeyFrame(
                        new Duration(DURATION_MOVE),
                        new KeyValue(group.translateXProperty(), cell.getCenterPositionX() - circle.getCenterX(), Interpolator.EASE_BOTH),
                        new KeyValue(group.translateYProperty(), cell.getCenterPositionY() - circle.getCenterY(), Interpolator.EASE_BOTH),
                        new KeyValue(textName.opacityProperty(), 0)
                )
        );
        TimerSequentialPlayer.getInstance().addTimeLine(timeline);
    }

    public void say(final String something) {
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(
                        Duration.ZERO, (ActionEvent event) -> {
                            textSay.setText(something);
                        }),
                new KeyFrame(
                        new Duration(DURATION_MOVE / 8),
                        new KeyValue(textSay.opacityProperty(), 1)
                ),
                new KeyFrame(
                        new Duration(DURATION_MOVE * 7 / 8),
                        new KeyValue(textSay.opacityProperty(), 1)
                ),
                new KeyFrame(
                        new Duration(DURATION_MOVE),
                        new KeyValue(textSay.opacityProperty(), 0)
                )
        );
        TimerSequentialPlayer.getInstance().addTimeLine(timeline);
    }
}
