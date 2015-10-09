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
package com.velonuboso.made.core.ec;

import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.ec.api.IFitnessFunction;
import com.velonuboso.made.core.ec.api.IGene;
import com.velonuboso.made.core.ec.api.IGeneticAlgorithm;
import com.velonuboso.made.core.ec.api.IGeneticAlgorithmListener;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.entity.Fitness;
import com.velonuboso.made.core.ec.entity.GeneType;
import com.velonuboso.made.core.ec.entity.GeneDefinition;
import com.velonuboso.made.core.ec.entity.IndividualDefinition;
import com.velonuboso.made.core.ec.entity.TrialInformation;
import com.velonuboso.made.core.ec.implementation.GeneticAlgorithm;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author rhgarcia
 */
public class GeneticAlgorithmImplementationsTest {
    private IGeneticAlgorithm algorithm;
    private IGeneticAlgorithmListener fakeListener;
    private IndividualDefinition definition;
    private SampleFitnessFunction sampleFitnessFunction;
    private int SEED = 314;
    
    @Before
    public void before(){
        ObjectFactory.createObject(IProbabilityHelper.class).setSeed(SEED);
        
        definition = new IndividualDefinition(new GeneDefinition[]{
            new GeneDefinition(GeneType.FLOAT, 0, 1),
            new GeneDefinition(GeneType.FLOAT, -1, 1),
            new GeneDefinition(GeneType.INTEGER, 0, 100),
            new GeneDefinition(GeneType.INTEGER, 10, 20)
        });
        fakeListener =  Mockito.mock(IGeneticAlgorithmListener.class);
        
        algorithm = ObjectFactory.createObject(IGeneticAlgorithm.class);
        algorithm.addListener(fakeListener);
        
        sampleFitnessFunction = new SampleFitnessFunction();
        ObjectFactory.installMock(IFitnessFunction.class, sampleFitnessFunction);
    }
    
    @After
    public void after(){
        ObjectFactory.removeMock(IFitnessFunction.class);
    }
    
    @Test
    public void GeneticAlgorithm_finds_better_solution_when_population_is_bigger(){
        
        float phenotypeWithShortPopulation = getPhenotypeOfBestIndividualWhenGeneticAlgorithmIsRun(2, 0);
        float phenotypeWithMiddlePopulation = getPhenotypeOfBestIndividualWhenGeneticAlgorithmIsRun(10, 0);
        float phenotypeWithBigPopulation = getPhenotypeOfBestIndividualWhenGeneticAlgorithmIsRun(1000, 0);
        
        assertTrue("the best phenotype whith medium population should've been better than with short population", 
                Math.abs(Math.PI - phenotypeWithShortPopulation) > Math.abs(Math.PI - phenotypeWithMiddlePopulation));
        
        assertTrue("the best phenotype whith big population should've been better than with medium population", 
                Math.abs(Math.PI - phenotypeWithMiddlePopulation) > Math.abs(Math.PI - phenotypeWithBigPopulation));
    }
    
    @Test
    public void GeneticAlgorithm_finds_better_solution_when_number_of_iterations_are_bigger(){
        
        float phenotypeWithFewIterations = getPhenotypeOfBestIndividualWhenGeneticAlgorithmIsRun(2, 0);
        float phenotypeWithMediumIterations = getPhenotypeOfBestIndividualWhenGeneticAlgorithmIsRun(2, 5);
        float phenotypeWithManyIterations = getPhenotypeOfBestIndividualWhenGeneticAlgorithmIsRun(2, 20);
        
        assertTrue("the best phenotype whith medium number of iterations should've been better than with short number", 
                Math.abs(Math.PI - phenotypeWithFewIterations) > Math.abs(Math.PI - phenotypeWithMediumIterations));
        
        assertTrue("the best phenotype whith hight number of iterations should've been better than with medium number", 
                Math.abs(Math.PI - phenotypeWithMediumIterations) > Math.abs(Math.PI - phenotypeWithManyIterations));
    }

    private float getPhenotypeOfBestIndividualWhenGeneticAlgorithmIsRun(int population, int iterations) {
        ObjectFactory.createObject(IProbabilityHelper.class).setSeed(SEED);
        algorithm.configure(definition, population, iterations, 0.5f, 10);
        IIndividual bestInShortPopulation = algorithm.run();
        return sampleFitnessFunction.calculatePhenotypeForIndividual(bestInShortPopulation);
    }
    
    public class SampleFitnessFunction implements IFitnessFunction{

        @Override
        public Fitness evaluateIndividual(IIndividual individual) {
            float targetValue = (float)Math.PI;
            
            IGene[] genes = individual.getGenes();
            try{
                float calculation = calculatePhenotypeForIndividual(individual);
                float fitnessValue = 1/Math.abs(targetValue - calculation);
                return new Fitness(new TrialInformation(1, fitnessValue, 0));
            }catch(Exception e){
                return new Fitness();
            }
        }
        
        public float calculatePhenotypeForIndividual (IIndividual individual){
            IGene[] genes = individual.getGenes();
            return calculate(genes[0].getValue(), genes[1].getValue(),genes[2].getValue(), genes[3].getValue());
        }
        
        public float calculate(float firstValue, float secondValue, float thirdValue, float fourthValue){
            return (firstValue-secondValue)*(thirdValue-fourthValue);
        }
    }
}
