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
package com.velonuboso.made.core.common;

import com.velonuboso.made.core.common.entity.EventsLogEntity;
import com.velonuboso.made.core.common.util.EventsLogConverter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.paint.Color;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class EventsLogEntityTest {

    private EventsLogEntity log;

    @Before
    public void before() {
        log = new EventsLogEntity(
                new EventsLogEntity.BoardEntity(
                        10,
                        new EventsLogEntity.PositionEntity[]{
                            new EventsLogEntity.PositionEntity(1, 4),
                            new EventsLogEntity.PositionEntity(2, 5)
                        }
                ),
                new EventsLogEntity.CharacterEntity[]{
                    new EventsLogEntity.CharacterEntity(0, "Zhen Suwah", Color.AQUA),
                    new EventsLogEntity.CharacterEntity(1, "Quddah Oofi", Color.BROWN)
                },
                new EventsLogEntity.DayLog[]{
                    new EventsLogEntity.DayLog(0, new EventsLogEntity.EventEntity[]{
                        new EventsLogEntity.EventEntity(0, new Integer[]{0}, "IsBorn (0, 0, 3)"),
                        new EventsLogEntity.EventEntity(1, new Integer[]{1}, "IsBorn (0, 1, 50)")
                    }),
                    new EventsLogEntity.DayLog(1, new EventsLogEntity.EventEntity[]{
                        new EventsLogEntity.EventEntity(2, new Integer[]{0}, "Move (1, 0, 19)"),
                        new EventsLogEntity.EventEntity(3, new Integer[]{1}, "Move (1, 1, 65)")
                    })
                }
        );
    }

    @Test
    public void UT_EventsLog_is_converted_to_json() throws IOException, URISyntaxException {

        String sampleJsonFileName = "samplejson.json";
        String toBeReplaced = "\r";
        String replacement = "";

        URI sampleJsonURI = ClassLoader.getSystemResource(sampleJsonFileName).toURI();
        String sampleJson = new String(Files.readAllBytes(Paths.get(sampleJsonURI)));
        sampleJson = sampleJson.replace(toBeReplaced, replacement);

        String logAsJson = EventsLogConverter.toJson(log);
        Assert.assertNotNull("Should've converted the object to a json correctly", logAsJson);
        Assert.assertEquals("Extracted Json should be equal to sample", sampleJson, logAsJson);
    }

    @Test
    public void UT_fromJson_must_return_EventsLog_when_Json_is_valid() throws IOException, URISyntaxException {

        String sampleJsonFileName = "samplejson.json";
        String toBeReplaced = "\r";
        String replacement = "";

        URI sampleJsonURI = ClassLoader.getSystemResource(sampleJsonFileName).toURI();
        String sampleJson = new String(Files.readAllBytes(Paths.get(sampleJsonURI)));
        sampleJson = sampleJson.replace(toBeReplaced, replacement);

        EventsLogEntity sampleAsEventsLog = EventsLogConverter.fromJson(sampleJson);

        Assert.assertNotNull("Should've converted the json to a EventsLog object correctly", sampleAsEventsLog);
    }
    
    @Test
    @Ignore
    public void UT_fromJson_must_return_null_when_Json_is_invalid() throws IOException, URISyntaxException {

        String sampleJsonFileName = "samplejson.json";
        String toBeReplaced[] = new String[] {"\r", "gridSize"};
        String replacement[] = new String[] { ":", ""};
        
        URI sampleJsonURI = ClassLoader.getSystemResource(sampleJsonFileName).toURI();
        String sampleJson = new String(Files.readAllBytes(Paths.get(sampleJsonURI)));
        
        for (int iterator=0; iterator<toBeReplaced.length; iterator++){
            sampleJson = sampleJson.replace(toBeReplaced[iterator], replacement[iterator]);
        }
        
        EventsLogEntity sampleAsEventsLog = EventsLogConverter.fromJson(sampleJson);

        Assert.assertNull("Should've returned null when json is incorrect",
                sampleAsEventsLog);
    }

    @Test
    public void UT_EventsLog_getters_must_return_set_values() throws Exception {
        PojoTester.testPojo(EventsLogEntity.class);
    }
    
    @Test
    public void UT_PositionEntity_getters_must_return_set_values() throws Exception {
        PojoTester.testPojo(EventsLogEntity.PositionEntity.class);
    }
    
    @Test
    public void UT_BoardEntity_getters_must_return_set_values() throws Exception {
        PojoTester.testPojo(EventsLogEntity.BoardEntity.class);
    }
    
    @Test
    public void UT_CharacterEntity_getters_must_return_set_values() throws Exception {
        PojoTester.testPojo(EventsLogEntity.CharacterEntity.class);
    }
    
    @Test
    public void UT_DayLog_getters_must_return_set_values() throws Exception {
        PojoTester.testPojo(EventsLogEntity.DayLog.class);
    }
    
    @Test
    public void UT_EventEntity_getters_must_return_set_values() throws Exception {
        PojoTester.testPojo(EventsLogEntity.EventEntity.class);
    }
}
