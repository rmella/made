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
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;
import junit.framework.Assert;
import static org.mockito.Mockito.mock;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class PojoTester {
    
    public static void testPojo(Class targetClass) throws Exception {
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
                    }else if (returnType.equals(Integer.class)){
                        fakeObject = new Integer(1);
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
