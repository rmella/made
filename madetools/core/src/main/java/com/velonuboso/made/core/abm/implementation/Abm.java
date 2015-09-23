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
package com.velonuboso.made.core.abm.implementation;

import com.velonuboso.made.core.abm.api.IAbm;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IColorSpot;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationHelper;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.entity.EventsLogEntity;
import com.velonuboso.made.core.common.entity.InferencesEntity;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.customization.api.ICustomization;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Abm implements IAbm {

    private ICustomization customization;
    private InferencesEntity inferencesEntity;
    private IEventsWriter eventsWriter;
    private List<String> log;
    private IMap map;
    private AbmConfigurationHelper helper;
    private int counterForId;

    public Abm() {
        reset();
    }

    @Override
    public void setCustomization(ICustomization customization) {
        this.customization = customization;
    }

    @Override
    public void setInferences(InferencesEntity defaultInferences) {
        this.inferencesEntity = defaultInferences;
    }

    @Override
    public void reset() {
        if (inferencesEntity == null) {
            inferencesEntity = new InferencesEntity();
        }
        eventsWriter = ObjectFactory.createObject(IEventsWriter.class);
        log = new ArrayList<>();
        counterForId = 0;
    }

    @Override
    public void run(AbmConfigurationEntity abmConfiguration) {

        try {
            initializeHelper(abmConfiguration);
            initializeMap();
            placeCharactersInMap();
            mainLoop();
        } catch (Exception exception) {
            writeExceptionToLog(abmConfiguration, exception);
        }
    }

    @Override
    public EventsLogEntity getEventsLog() {
        EventsLogEntity entity = new EventsLogEntity();
        entity.setLog((String[]) log.toArray());
        return entity;
    }

    private void mainLoop() {
        int numberOfDays = helper.getWorldAbmConfigurationHelper().getNumberOfDays();
        for (int day = 0; day < numberOfDays; day++) {
            placeSpotsInMap(map);
            runCharactersInMap(map);
            removeSpotsFromMap();
        }
    }

    private void writeExceptionToLog(AbmConfigurationEntity abmConfiguration, Exception exception) {
        String configuration = Arrays.toString(abmConfiguration.getChromosome());
        String message = "Could not run using configuration :" + configuration;
        writeException(message, exception);
    }

    private void writeException(String message, Exception exeption) {
        IEventFactory factory = ObjectFactory.createObject(IEventFactory.class);
        IEvent exceptionEvent = factory.exception(message);
        eventsWriter.add(exceptionEvent);
        Logger.getLogger(Abm.class.getName()).log(Level.SEVERE, message + "." + exeption.getMessage(), exeption);
    }

    private void initializeHelper(AbmConfigurationEntity abmConfiguration) throws Exception {
        helper = new AbmConfigurationHelper(abmConfiguration);
        helper.prepare();
    }

    private void initializeMap() {
        map = ObjectFactory.createObject(IMap.class);
        map.setEventsWriter(eventsWriter);

        int size = helper.getWorldAbmConfigurationHelper().getWorldSize();
        map.initialize(size, size);
    }

    private void placeCharactersInMap() {
        int numberOfSquares = helper.getWorldAbmConfigurationHelper().getNumberOfSquares();
        int numberOfTriangles = helper.getWorldAbmConfigurationHelper().getNumberOfSquares();
        int numberOfCircles = helper.getWorldAbmConfigurationHelper().getNumberOfSquares();

        List<Integer> cells = map.getCells();
        Collections.shuffle(cells);

        AddCharacters(numberOfSquares, cells, CharacterShape.SQUARE);
        AddCharacters(numberOfCircles, cells, CharacterShape.CIRCLE);
        AddCharacters(numberOfTriangles, cells, CharacterShape.TRIANGLE);
    }

    private void placeSpotsInMap(IMap map) {
        float probabilityToAddSpot = helper.getWorldAbmConfigurationHelper().getProbabilityToAddSpot();

        IProbabilityHelper probabilityHelper = ObjectFactory.createObject(IProbabilityHelper.class);
        if (probabilityHelper.getNextProbability(Abm.class) >= probabilityToAddSpot) {
            return;
        }

        List<Integer> freeUnorderedCells = getAndShuffleCellsWithoutSpot();
        if (freeUnorderedCells.isEmpty()) {
            return;
        }

        int cell = freeUnorderedCells.get(0);
        addNewColorSpotToMap(cell, cell);
    }

    private void AddCharacters(int numberOfSquares, List<Integer> cells, CharacterShape shape) {
        for (int characterIndex = 0; characterIndex < numberOfSquares; characterIndex++) {
            int cell = cells.remove(0);
            addNewCharacterToMap(counterForId++, shape, cell);
        }
    }

    private void addNewCharacterToMap(int id, CharacterShape shape, int cell) {
        AbmConfigurationEntity abmConfiguration
                = helper.getPieceAbmConfigurationHelper(CharacterShape.CIRCLE).getAbmConfiguration();

        ICharacter character = ObjectFactory.createObject(ICharacter.class);
        character.setAbmConfiguration(abmConfiguration);
        character.setBackgroundColor(getRandomColor());
        character.setForegroundColor(getRandomColor());
        character.setEventsWriter(eventsWriter);
        character.setId(id);
        character.setMap(map);
        character.setShape(shape);
        map.putCharacter(character, cell);
    }

    private void addNewColorSpotToMap(int id, int cell) {
        IColorSpot spot = ObjectFactory.createObject(IColorSpot.class);
        spot.setColor(getRandomColor());
        spot.setId(id);
        map.putColorSpot(spot, cell);
    }

    private List<Integer> getAndShuffleCellsWithoutSpot() {
        List<Integer> freeCells = map.getCells().stream()
                .filter(cell -> map.getColorSpot(cell) == null)
                .collect(Collectors.toList());
        Collections.shuffle(freeCells);
        return freeCells;
    }

    private void runCharactersInMap(IMap map) {
    }

    private void removeSpotsFromMap() {
        helper.getWorldAbmConfigurationHelper().getProbabilityToRemoveSpot();
        map.getCells().stream()
                .filter(cell -> map.getColorSpot(cell) != null)
                .forEach(cell -> deleteIfNecessary(cell));

    }

    private Color getRandomColor() {
        IProbabilityHelper probabilityHelper = ObjectFactory.createObject(IProbabilityHelper.class);
        return new Color(probabilityHelper.getNextProbability(Abm.class),
                probabilityHelper.getNextProbability(Abm.class),
                probabilityHelper.getNextProbability(Abm.class),
                1f);
    }

    private void deleteIfNecessary(int cell) {
        float probabilityToRemoveSpot = helper.getWorldAbmConfigurationHelper().getProbabilityToRemoveSpot();

        IProbabilityHelper probabilityHelper = ObjectFactory.createObject(IProbabilityHelper.class);
        if (probabilityHelper.getNextProbability(Abm.class) >= probabilityToRemoveSpot) {
            map.removeSpot(cell);
        }
    }
}
