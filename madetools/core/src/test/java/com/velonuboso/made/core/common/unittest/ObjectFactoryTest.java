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

import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.implementation.Map;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class ObjectFactoryTest {
    
    public ObjectFactoryTest() {
    }

    @Test
    public void UT_ObjectFactory_must_return_Map_instance_for_IMap_interface() {
        Class expectedClass = Map.class;
        Object object = ObjectFactory.createObject(IMap.class);
        assertTrue("Should've returned a "+expectedClass.getSimpleName()+" instance",
                object.getClass().equals(expectedClass));
    }
    
    @Test
    public void UT_ObjectFactory_must_return_a_mock_for_IMap_interface_when_mock_class_is_installed() {
        Class expectedClass = NewMapImplementation.class;
        ObjectFactory.installMock(IMap.class, expectedClass);
        Object object = ObjectFactory.createObject(IMap.class);
        ObjectFactory.removeMock(IMap.class);
        
        assertTrue("Should've returned the mock when mock is installed",
                object.getClass().equals(expectedClass));
    }
    
    @Test
    public void UT_ObjectFactory_must_return_Map_instance_for_IMap_interface_when_mock_class_is_uninstalled() {
        
        Class expectedClass = NewMapImplementation.class;
        ObjectFactory.installMock(IMap.class, expectedClass);
        ObjectFactory.removeMock(IMap.class);
        Object object = ObjectFactory.createObject(IMap.class);
        
        assertFalse("Should've returned the original mapping",
                object.getClass().equals(expectedClass));
    }

    @Test
    public void UT_ObjectFactory_must_return_a_mock_for_IMap_interface_when_mock_intance_is_installed() {
        IMap fakeMap = Mockito.mock(IMap.class);
        ObjectFactory.installMock(IMap.class, fakeMap);
        Object object = ObjectFactory.createObject(IMap.class);
        ObjectFactory.removeMock(IMap.class);
        
        assertTrue("Should've returned the original mapping when the mock is uninstalled",
                object == fakeMap);
    }
    
    @Test
    public void UT_ObjectFactory_must_return_Map_instance_for_IMap_interface_when_mock_instance_is_uninstalled() {
        IMap fakeMap = Mockito.mock(IMap.class);
        ObjectFactory.installMock(IMap.class, fakeMap);
        ObjectFactory.removeMock(IMap.class);
        
        Object object = ObjectFactory.createObject(IMap.class);
        assertFalse("Should've returned the original mapping when the mock is uninstalled",
                object == fakeMap);
    }
    
    @Test(expected = RuntimeException.class)
    public void UT_ObjectFactory_must_return_Exception_when_interface_is_not_registered(){
        Object object = ObjectFactory.createObject(List.class);
        fail("Should've thrown an exception since the List interface does not have a default"
                + "implementation in MADE's ObjectFactory");
    }
    
    @Test
    public void UT_ObjectFactory_createObject_must_return_same_instance_when_called_twice_on_singleton(){
        Object firstOccurrence = ObjectFactory.createObject(IProbabilityHelper.class);
        Object secondOccurrence = ObjectFactory.createObject(IProbabilityHelper.class);
        assertSame("Should've created the same instance when Interface is defined as singleton",
                firstOccurrence, secondOccurrence);
    }
    
    @Test
    public void UT_ObjectFactory_createObject_must_return_different_instances_when_called_twice_on_non_singleton(){
        Object firstOccurrence = ObjectFactory.createObject(IMap.class);
        Object secondOccurrence = ObjectFactory.createObject(IMap.class);
        assertNotSame("Should've created the same instance when Interface is defined as singleton",
                firstOccurrence, secondOccurrence);
    }
}
