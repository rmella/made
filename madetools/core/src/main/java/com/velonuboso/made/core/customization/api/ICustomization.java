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

package com.velonuboso.made.core.customization.api;

import com.velonuboso.made.core.common.util.ImplementedBy;
import com.velonuboso.made.core.common.util.InitializationException;
import com.velonuboso.made.core.customization.entity.NarrationRuleEntity;
import com.velonuboso.made.core.customization.implementation.Customization;
import java.io.File;
import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */

@ImplementedBy(targetClass = Customization.class, targetMode = ImplementedBy.Mode.NORMAL)
public interface ICustomization {
    void loadFromFile(final File file) throws InitializationException;
    List<NarrationRuleEntity> getNarrationRules();
}
