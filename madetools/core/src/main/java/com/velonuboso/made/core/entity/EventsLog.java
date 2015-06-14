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
package com.velonuboso.made.core.entity;

import java.io.Serializable;
import java.util.Arrays;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class EventsLog implements Serializable {

    private BoardEntity board;
    private CharacterEntity[] characters;
    private DayLog[] dayLogs;

    public EventsLog() {
    }

    public EventsLog(BoardEntity board, CharacterEntity[] characters, DayLog[] dayLogs) {
        this.board = board;
        this.characters = characters;
        this.dayLogs = dayLogs;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    public void setCharacters(CharacterEntity[] characters) {
        this.characters = characters;
    }

    public void setDayLogs(DayLog[] dayLogs) {
        this.dayLogs = dayLogs;
    }

    public BoardEntity getBoard() {
        return board;
    }

    public CharacterEntity[] getCharacters() {
        return characters;
    }

    public DayLog[] getDayLogs() {
        return dayLogs;
    }

    public static class BoardEntity implements Serializable {

        private int gridSize;
        private PositionEntity[] obstacles;

        public BoardEntity() {
        }

        public BoardEntity(int gridSize, PositionEntity[] obstacles) {
            this.gridSize = gridSize;
            this.obstacles = obstacles;
        }

        public void setGridSize(final int gridSize) {
            this.gridSize = gridSize;
        }

        public void setObstacles(PositionEntity[] obstacles) {
            this.obstacles = obstacles;
        }

        public int getGridSize() {
            return gridSize;
        }

        public PositionEntity[] getObstacles() {
            return obstacles;
        }
    }

    public static class PositionEntity implements Serializable {

        private int x;
        private int y;

        public PositionEntity() {
        }

        public PositionEntity(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public static class CharacterEntity implements Serializable {

        private int characterId;
        private String name;
        private Color color;

        public CharacterEntity() {
        }

        public CharacterEntity(int characterId, String name, Color color) {
            this.characterId = characterId;
            this.name = name;
            this.color = color;
        }

        public void setCharacterId(int characterId) {
            this.characterId = characterId;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Color getColor() {
            return color;
        }

        public int getCharacterId() {
            return characterId;
        }

        public String getName() {
            return name;
        }
    }

    public static class DayLog implements Serializable {

        private int day;
        private EventEntity[] events;

        public DayLog() {
        }

        public DayLog(int day, EventEntity[] events) {
            this.day = day;
            this.events = events;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public void setEvents(EventEntity[] events) {
            this.events = events;
        }

        public int getDay() {
            return day;
        }

        public EventEntity[] getEvents() {
            return events;
        }
    }

    public static class EventEntity {

        private int eventId;
        private Integer[] actors;
        private String predicate;

        public EventEntity() {
        }

        public EventEntity(int eventId, Integer[] actors, String predicate) {
            this.eventId = eventId;
            this.actors = actors;
            this.predicate = predicate;
        }

        public void setActors(Integer[] actors) {
            this.actors = actors;
        }

        public void setEventId(final int eventId) {
            this.eventId = eventId;
        }

        public void setPredicate(final String predicate) {
            this.predicate = predicate;
        }

        public Integer[] getActors() {
            return actors;
        }

        public int getEventId() {
            return eventId;
        }

        public String getPredicate() {
            return predicate;
        }
    }
}
