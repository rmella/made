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
package com.velonuboso.made.core.ec.implementation;

import com.velonuboso.made.core.abm.api.IAbm;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.entity.EventsLogEntity;
import com.velonuboso.made.core.common.entity.InferencesEntity;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.customization.api.ICustomization;
import com.velonuboso.made.core.ec.api.IFitnessFunction;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.inference.api.IReasoner;
import com.velonuboso.made.core.inference.entity.WorldDeductions;
import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class FitnessFunction implements IFitnessFunction{

    @Override
    public float evaluateIndividual(IIndividual individual) {
        ICustomization customization = ObjectFactory.createObject(ICustomization.class);
        
        IAbm abm = ObjectFactory.createObject(IAbm.class);
        abm.setCustomization(customization);
        abm.setInferences(new InferencesEntity());
        
        Float chromosome[] = Arrays.stream(individual.getGenes()).map(gene -> gene.getValue()).toArray(Float[]::new);
        abm.run(new AbmConfigurationEntity(ArrayUtils.toPrimitive(chromosome)));
        EventsLogEntity events = abm.getEventsLog();
        
        IReasoner reasoner = ObjectFactory.createObject(IReasoner.class);
        WorldDeductions deductions = reasoner.getWorldDeductions(events.getLogicalTerms());
        return deductions.values().size();
    }
    
}
