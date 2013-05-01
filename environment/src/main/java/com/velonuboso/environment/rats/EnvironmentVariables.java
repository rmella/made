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

/**
 *
 * @author Rubén Héctor García <raiben@gmail.com>
 */
public class EnvironmentVariables {
    
    public static int IND_NUMBER_OF_INITIAL_AGENTS = 0;
    public static int IND_NEIGHBORHOOD_CELLS = 1;
    
    float[] individual;

    
    public EnvironmentVariables(float[] individual) {
        this.individual = individual;
    }
    
    public float getVal (int index){
        return individual[index];
    }
    
}
