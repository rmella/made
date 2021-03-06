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
package com.velonuboso.made.core.common.util;

import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.IPosition;
import com.velonuboso.made.core.abm.implementation.Position;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.implementation.EventFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ObjectFactory {

    private static HashMap<Class, Object> mappingSingleton;
    private static HashMap<Class, Class> mappingClasses;
    private static final HashMap<Class, Class> mappingMockingClasses = new HashMap<>();
    private static final HashMap<Class, Object> mappingMockingInstances = new HashMap<>();

    public static <T> T createObject(Class<T> targetInterface) {
        if (mappingClasses == null) {
            createMappings();
        }
        try {
            if (mappingMockingInstances.containsKey(targetInterface)) {
                return (T) mappingMockingInstances.get(targetInterface);
            }
            if (mappingMockingClasses.containsKey(targetInterface)) {
                return (T) mappingMockingClasses.get(targetInterface).newInstance();
            }
            if (mappingClasses.containsKey(targetInterface)) {
                return (T) mappingClasses.get(targetInterface).newInstance();
            }
            if (mappingSingleton.containsKey(targetInterface)) {
                return (T) mappingSingleton.get(targetInterface);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not instantiate " + targetInterface.getCanonicalName(), e);
        }
        throw new RuntimeException("No target implementation for " + targetInterface.getSimpleName());
    }

    private static void createMappings() {
        mappingClasses = new HashMap<>();
        mappingSingleton = new HashMap<>();
        insertInterfacesIntoMappingsFromPackage();
        checkMappingCoherence();
    }

    private static void insertInterfacesIntoMappingsFromPackage() {
        Set<Class<?>> allImplementedBy = findAllImplementedBy();
        allImplementedBy.stream().filter(theClass -> theClass.isInterface()).
                forEach((theInterface) -> insertInterfaceIntoMapping(theInterface));
    }

    private static void insertInterfaceIntoMapping(Class<?> theInterface) {
        ImplementedBy annotation = (ImplementedBy) theInterface.getDeclaredAnnotation(ImplementedBy.class);
        switch (annotation.targetMode()) {
            case NORMAL:
                mappingClasses.put(theInterface, annotation.targetClass());
                break;
            default: {
                try {
                    Object instance = annotation.targetClass().newInstance();
                    mappingSingleton.put(theInterface, instance);
                } catch (Exception ex) {
                    throw new RuntimeException("Could not instantiate " + theInterface.getCanonicalName(), ex);
                }
                break;
            }
        }
    }

    private static void checkMappingCoherence() throws RuntimeException {
        Set<Class> classes = mappingClasses.keySet();
        for (Class targetClass : classes) {
            if (!targetClass.isAssignableFrom(mappingClasses.get(targetClass))) {
                throw new RuntimeException("Objectfactory: " + mappingClasses.get(targetClass)
                        + " does not implement " + targetClass);
            }
        }
    }

    public static void installMock(Class targetInterface, Class fakeImplementation) {
        removeMock(targetInterface);
        mappingMockingClasses.put(targetInterface, fakeImplementation);
    }

    public static void installMock(Class targetInterface, Object fakeInstance) {
        removeMock(targetInterface);
        mappingMockingInstances.put(targetInterface, fakeInstance);
    }

    public static void removeMock(Class targetInterface) {
        mappingMockingInstances.remove(targetInterface);
        mappingMockingClasses.remove(targetInterface);
    }

    private static Set<Class<?>> findAllImplementedBy() {
        final Reflections reflections = new Reflections("com.velonuboso");
        return reflections.getTypesAnnotatedWith(ImplementedBy.class);
    }

    public static void cleanAllMocks() {
        mappingMockingClasses.clear();
        mappingMockingInstances.clear();
    }
}
