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
package com.velonuboso.made.core.common.entity;

import com.velonuboso.made.core.inference.entity.Trope;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class CommonEcConfiguration {
    public int NUMBER_OF_TRIALS = 30;
    public int POPULATION_SIZE = 50; 
    public int MAXIMUM_ITERATIONS = 1000;
    public float BLX_ALPHA = 0.5f;
    public int ETA_DISTANCE_MUTATION_DISTRIBUTION = 20;
    public int TERMINATE_IF_NOT_IMPROVES_IN_ITERATIONS = 30;
    public Trope FITNESS_TROPE = null;
}
