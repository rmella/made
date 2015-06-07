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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */

public class TimerSequentialPlayer {

    public int FRAMES_PER_SECOND = 60;
    
    private static TimerSequentialPlayer instance = null;
    private boolean animating = false;
    private Queue<Timeline> timelines = new ConcurrentLinkedQueue<>();
    
    public static TimerSequentialPlayer getInstance() {
        if (instance == null) {
            instance = new TimerSequentialPlayer();
        }
        return instance;
    }
    
    private TimerSequentialPlayer(){
        animating = false;
        final Duration frameDuration = Duration.millis(1000 / (float) FRAMES_PER_SECOND);
        final KeyFrame sequentialKeyFrame = new KeyFrame(
                frameDuration,new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!animating){
                    animateNextTimeLine();
                }
            }
        });
        Timeline timeline = new Timeline(sequentialKeyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    private void animateNextTimeLine() {
        Timeline nextTimeline = timelines.poll();
        if (nextTimeline != null){
            animating = true;
            nextTimeline.setOnFinished((ActionEvent event) -> {
                animating = false;
            });
            nextTimeline.play();
        }
    }
    
    public void addTimeLine(Timeline timeline){
        timelines.add(timeline);
    }
}
