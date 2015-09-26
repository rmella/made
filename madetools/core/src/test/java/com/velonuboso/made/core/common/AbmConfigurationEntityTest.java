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
package com.velonuboso.made.core.common;

import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import java.util.Objects;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class AbmConfigurationEntityTest {
    
    @Test
    public void UT_Solution_getter_must_return_set_values() throws Exception {
        float expectedValue[] = new float[]{2f, 134f, 2f};
        AbmConfigurationEntity solution = new AbmConfigurationEntity(expectedValue);
        assertTrue("Should've get the set value",
                Objects.deepEquals(expectedValue, solution.getChromosome()));
    }
    
}
