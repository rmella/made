/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
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

import java.util.Random;

/**
 *
 * @author raiben@gmail.com
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
