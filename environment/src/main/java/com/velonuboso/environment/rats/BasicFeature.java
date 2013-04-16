/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.velonuboso.environment.rats;

import com.velonuboso.made.core.interfaces.Feature;
import java.util.Random;

/**
 *
 * @author Ruben Hector Garcia Ortega <raiben@gmail.com>
 */
public class BasicFeature implements Feature{

    private String name;
    private float value;
    private Random random;
    
    public BasicFeature(Random r, String name, float value) {
        this.name = name;
        this.value = value;
        this.random = r;
    }

    public BasicFeature(Random r, String name) {
        this.name = name;
        this.setRandomValue();
        this.random = r;
    }
    
    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
   }

    public void setValue(float v) {
        this.value = v;
    }

    public void setRandomValue() {
        value = (float) random.nextGaussian();
    }

    public void addValue(float v) {
        value += v;
    }
    
}
