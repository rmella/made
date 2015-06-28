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
package com.velonuboso.made.core.common.unittest;

import com.google.common.base.Defaults;
import com.velonuboso.made.core.common.entity.EventsLog;
import com.velonuboso.made.core.common.entity.EventsLogConverter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class EventsLogTest {

    private EventsLog log;

    @Before
    public void before() {
        log = new EventsLog(
                new EventsLog.BoardEntity(
                        10,
                        new EventsLog.PositionEntity[]{
                            new EventsLog.PositionEntity(1, 4),
                            new EventsLog.PositionEntity(2, 5)
                        }
                ),
                new EventsLog.CharacterEntity[]{
                    new EventsLog.CharacterEntity(0, "Zhen Suwah", Color.AQUA),
                    new EventsLog.CharacterEntity(1, "Quddah Oofi", Color.BROWN)
                },
                new EventsLog.DayLog[]{
                    new EventsLog.DayLog(0, new EventsLog.EventEntity[]{
                        new EventsLog.EventEntity(0, new Integer[]{0}, "IsBorn (0, 0, 3)"),
                        new EventsLog.EventEntity(1, new Integer[]{1}, "IsBorn (0, 1, 50)")
                    }),
                    new EventsLog.DayLog(1, new EventsLog.EventEntity[]{
                        new EventsLog.EventEntity(2, new Integer[]{0}, "Move (1, 0, 19)"),
                        new EventsLog.EventEntity(3, new Integer[]{1}, "Move (1, 1, 65)")
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

        EventsLog sampleAsEventsLog = EventsLogConverter.fromJson(sampleJson);

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
        
        EventsLog sampleAsEventsLog = EventsLogConverter.fromJson(sampleJson);

        Assert.assertNull("Should've returned null when json is incorrect",
                sampleAsEventsLog);
    }

    @Test
    public void UT_EventsLog_getters_must_return_set_values() throws Exception {
        testPojo(EventsLog.class);
    }
    
    @Test
    public void UT_PositionEntity_getters_must_return_set_values() throws Exception {
        testPojo(EventsLog.PositionEntity.class);
    }
    
    @Test
    public void UT_BoardEntity_getters_must_return_set_values() throws Exception {
        testPojo(EventsLog.BoardEntity.class);
    }
    
    @Test
    public void UT_CharacterEntity_getters_must_return_set_values() throws Exception {
        testPojo(EventsLog.CharacterEntity.class);
    }
    
    @Test
    public void UT_DayLog_getters_must_return_set_values() throws Exception {
        testPojo(EventsLog.DayLog.class);
    }
    
    @Test
    public void UT_EventEntity_getters_must_return_set_values() throws Exception {
        testPojo(EventsLog.EventEntity.class);
    }
    public void testPojo(Class targetClass) throws Exception {
        try {
            Method[] methods = targetClass.getDeclaredMethods();
            for (Method getter : methods) {
                if (getter.getName().startsWith("get")) {
                    String parameterName = getter.getName().substring(3);

                    Object fakeObject = null;
                    Class returnType = getter.getReturnType();
                    
                    if (returnType.isArray()){
                        fakeObject = Array.newInstance(returnType.getComponentType(), 0);
                    }else if (returnType.isPrimitive() || returnType.equals(String.class)){
                        fakeObject = Defaults.defaultValue(getter.getReturnType());
                    }else if (returnType.equals(Color.class)){
                        fakeObject = new Color(0.5, 0.8, 0.2, 1);
                    }else{
                        fakeObject = mock(returnType);
                    }
                    
                    List<Method> setters = Arrays.stream(methods).filter(
                            setter -> setter.getName().equals("set"+parameterName)
                    ).collect(Collectors.toList());
                    
                    Assert.assertTrue(
                            "parameter " + parameterName + " should have a setter",
                            setters.size() > 0
                    );
                    Assert.assertTrue(
                            "parameter " + parameterName + " should only have one setter",
                            setters.size() == 1
                    );
                    Method setter = setters.get(0);

                    Constructor constructor = targetClass.getDeclaredConstructor();

                    Object newInstance = constructor.newInstance();
                    setter.invoke(newInstance, fakeObject);
                    Object valueFromGetter = getter.invoke(newInstance);
                    Assert.assertEquals(getter.getName()
                            + " should've retrieved the value used in " + setter.getName(),
                            fakeObject, valueFromGetter
                    );
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        }
    }
}
