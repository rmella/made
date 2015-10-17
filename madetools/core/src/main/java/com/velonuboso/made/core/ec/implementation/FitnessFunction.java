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

import com.velonuboso.made.core.ec.api.IFitnessMetric;
import com.velonuboso.made.core.abm.api.IAbm;
import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.entity.CommonEcConfiguration;
import com.velonuboso.made.core.common.entity.EventsLogEntity;
import com.velonuboso.made.core.common.entity.InferencesEntity;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.customization.api.ICustomization;
import com.velonuboso.made.core.ec.api.IFitnessFunction;
import com.velonuboso.made.core.ec.api.IGeneticAlgorithmListener;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.entity.Fitness;
import com.velonuboso.made.core.ec.entity.TrialInformation;
import com.velonuboso.made.core.inference.api.IReasoner;
import com.velonuboso.made.core.inference.entity.Trope;
import com.velonuboso.made.core.inference.entity.WorldDeductions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class FitnessFunction implements IFitnessFunction{
    
    @Override
    public Fitness evaluateIndividual(IIndividual individual) {
        
        IGlobalConfigurationFactory globalConfigurationFactory = 
            ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonEcConfiguration config = globalConfigurationFactory.getCommonEcConfiguration();
        
        ArrayList<WorldDeductions> deductionsForAllTrials = new ArrayList<>();
        
        for (int trialIndex = 0; trialIndex < config.NUMBER_OF_TRIALS; trialIndex++){
            ICustomization customization = ObjectFactory.createObject(ICustomization.class);

            EventsLogEntity events = runVirtualWorld(customization, individual);
            WorldDeductions deductions = InferTropesFromEvents(events);
            deductionsForAllTrials.add(deductions);
            
            ObjectFactory.createObject(IGeneticAlgorithmListener.class).notifyTrialExecuted(deductions);
        }
        
        IFitnessMetric metric = ObjectFactory.createObject(IFitnessMetric.class);
        TrialInformation trialInformation = metric.getTrialInformation(deductionsForAllTrials);
        
        HashMap<String, TrialInformation> informationByTrope = new HashMap<>();
        for (Trope trope : Trope.getTropesInFromMonomyth()){
           TrialInformation trialInformationForTrope = metric.getTrialInformationForSpecificTrope(deductionsForAllTrials, trope);
           informationByTrope.put(trope.toString(), trialInformationForTrope);
        }
       
        Fitness fitness = new Fitness();
        fitness.setValue(trialInformation);
        fitness.setExtraMeasures(informationByTrope);
        
        ObjectFactory.createObject(IGeneticAlgorithmListener.class).notifyIndividualEvaluation(fitness);
        
        return fitness;
    }

    private WorldDeductions InferTropesFromEvents(EventsLogEntity events) {
        IReasoner reasoner = ObjectFactory.createObject(IReasoner.class);
        WorldDeductions deductions = reasoner.getWorldDeductions(events.getLogicalTerms());
        return deductions;
    }

    private EventsLogEntity runVirtualWorld(ICustomization customization, IIndividual individual) {
        IAbm abm = ObjectFactory.createObject(IAbm.class);
        abm.setCustomization(customization);
        abm.setInferences(new InferencesEntity());
        Float chromosome[] = Arrays.stream(individual.getGenes()).map(gene -> gene.getValue()).toArray(Float[]::new);
        abm.run(new AbmConfigurationEntity(ArrayUtils.toPrimitive(chromosome)));
        EventsLogEntity events = abm.getEventsLog();
        return events;
    }
    
}
