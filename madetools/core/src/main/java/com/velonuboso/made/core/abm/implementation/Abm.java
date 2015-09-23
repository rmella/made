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
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.implementation.piece.AbmConfigurationHelper;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.entity.EventsLogEntity;
import com.velonuboso.made.core.common.entity.InferencesEntity;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.customization.api.ICustomization;
import com.velonuboso.made.core.customization.implementation.Customization;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private void initializeMap() {
        map = ObjectFactory.createObject(IMap.class);
        map.setEventsWriter(eventsWriter);

    }

    private void initializeHelper(AbmConfigurationEntity abmConfiguration) throws Exception {
        helper = new AbmConfigurationHelper(abmConfiguration);
        helper.prepare();
    }

    private void placeCharactersInMap() {
    }

    private void placeSpotsInMap(IMap map) {
    }

    private void runCharactersInMap(IMap map) {
    }

    private void removeSpotsFromMap() {
    }

}
