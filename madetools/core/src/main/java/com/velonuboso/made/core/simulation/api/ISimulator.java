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

package com.velonuboso.made.core.simulation.api;

import com.velonuboso.made.core.abm.api.IAbm;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.util.ImplementedBy;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.narration.api.INarrator;
import com.velonuboso.made.core.simulation.implementation.Simulator;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
@ImplementedBy(targetClass = Simulator.class)
public interface ISimulator {
    String simulate (AbmConfigurationEntity configuration);
}
