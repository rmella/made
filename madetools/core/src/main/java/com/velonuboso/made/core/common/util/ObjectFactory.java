/*
 * Copyright (C) 2015 rhgarcia
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

import com.velonuboso.made.core.abm.api.IAction;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.abm.api.IFiniteStateAutomaton;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.api.IPosition;
import com.velonuboso.made.core.abm.implementation.FiniteStateAutomaton;
import com.velonuboso.made.core.abm.implementation.Map;
import com.velonuboso.made.core.abm.implementation.Position;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.implementation.EventFactory;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ObjectFactory {

    private static HashMap<Class, Class> mappings = null;
    private static HashMap<Class, Class> mocks = new HashMap<>();

    private static void insertMappings() {
        // NEW MAPPINGS SHOULD BE DEFINED HERE
        mappings.put(IMap.class, Map.class);
        mappings.put(IPosition.class, Position.class);
        mappings.put(IFiniteStateAutomaton.class, FiniteStateAutomaton.class);
        mappings.put(IEventFactory.class, EventFactory.class);
    }

    public static <T> T createObject(Class<T> targetInterface) {
        if (mappings == null) {
            createMappingsSingleton();
        }
        T newObject = null;
        try {
            if (mocks.containsKey(targetInterface)) {
                newObject = (T) mocks.get(targetInterface).newInstance();
            }
            if (mappings.containsKey(targetInterface)) {
                newObject = (T) mappings.get(targetInterface).newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not instantiate " + targetInterface.getCanonicalName(), e);
        }
        return newObject;
    }

    private static void createMappingsSingleton() {
        mappings = new HashMap<>();
        insertMappings ();
        checkMappingCoherence();
    }

    private static  void checkMappingCoherence() throws RuntimeException {
        Set<Class> classes = mappings.keySet();
        for (Class targetClass : classes) {
            if (!targetClass.isAssignableFrom(mappings.get(targetClass))) {
                throw new RuntimeException("Objectfactory: " + mappings.get(targetClass)
                        + " does not implement " + targetClass);
            }
        }
    }

    public void installMock(Class targetInterface, Class fakeImplementation) {
        mocks.put(targetInterface, fakeImplementation);
    }

    public void removeMock(Class targetInterface) {
        mocks.remove(targetInterface);
    }
}
