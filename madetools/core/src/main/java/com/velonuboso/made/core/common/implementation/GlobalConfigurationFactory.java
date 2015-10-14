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
package com.velonuboso.made.core.common.implementation;

import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.entity.CommonAbmConfiguration;
import com.velonuboso.made.core.common.entity.CommonEcConfiguration;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class GlobalConfigurationFactory implements IGlobalConfigurationFactory{

    private CommonAbmConfiguration commonAbmConfiguration;
    private CommonEcConfiguration commonEcConfiguration;

    public GlobalConfigurationFactory() {
        commonAbmConfiguration = new CommonAbmConfiguration();
        commonEcConfiguration = new CommonEcConfiguration();
    }
    
    @Override
    public CommonAbmConfiguration getCommonAbmConfiguration() {
        return commonAbmConfiguration;
    }

    @Override
    public CommonEcConfiguration getCommonEcConfiguration() {
        return commonEcConfiguration;
    }

    @Override
    public void setCommonAbmConfiguration(CommonAbmConfiguration commonAbmConfiguration) {
        this.commonAbmConfiguration = commonAbmConfiguration;
    }

    @Override
    public void setCommonEcConfiguration(CommonEcConfiguration commonEcConfiguration) {
        this.commonEcConfiguration = commonEcConfiguration;
    }
}
