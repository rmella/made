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
package com.velonuboso.made.core;

import java.util.Random;

/**
 *
 * @author Ruben Hector Garcia Ortega <raiben@gmail.com>
 */
public enum Gender {

    /**
     * Male.
     */
    MALE,
    /**
     * Female.
     */
    FEMALE;

    /**
     * gets a random gender.
     *
     * @param r the random number generator
     * @return the random gender
     */
    public static Gender getRandomGender(final Random r) {
        Random random;
        if (r == null) {
            random = new Random();
        } else {
            random = r;
        }
        return Gender.values()[random.nextInt(1)];
    }
}
