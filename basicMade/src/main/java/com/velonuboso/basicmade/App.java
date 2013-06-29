package com.velonuboso.basicmade;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;
import org.jgap.impl.IntegerGene;


public class App {

    private static int MAX_ALLOWED_EVOLUTIONS = 20;//5000; // TODO Change this
    private static int POPULATION_SIZE = 100; //500;

    public static void main(String[] args) throws Exception {
        
        Configuration conf = new DefaultConfiguration();
        FitnessFunction myFunc = new MadeEnvironmentFitnessFunction();

        conf.setFitnessFunction(myFunc);

        Gene[] sampleGenes = new Gene[MadeEnvironmentFitnessFunction.getGeneNumber()];
        for (int i=0; i<MadeEnvironmentFitnessFunction.getGeneNumber(); i++){
            sampleGenes[i] = new DoubleGene(conf, 0, 1);
        }
        
        Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);

        conf.setPopulationSize(POPULATION_SIZE);
        Genotype population = Genotype.randomInitialGenotype(conf);
        
        // show a sample of a random solution
        MadeEnvironment environment = new MadeEnvironment(population.getPopulation().getChromosome(0));
        environment.runEnvironment(true);
        
        // start iterating
        IChromosome bestSolutionSoFar = population.getFittestChromosome();
        
        System.out.println("Best solution: "+bestSolutionSoFar);
        System.out.println("fitness: "+bestSolutionSoFar.getFitnessValue());
        
        double fitness = bestSolutionSoFar.getFitnessValue();
        
        for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
            System.out.println("Iteration: "+i);
            population.evolve();
            bestSolutionSoFar = population.getFittestChromosome();
            if (bestSolutionSoFar.getFitnessValue()>fitness){
                System.out.println("Best solution: "+bestSolutionSoFar);
                System.out.println("fitness: "+bestSolutionSoFar.getFitnessValue());
                fitness = bestSolutionSoFar.getFitnessValue();
            }
        }
        
        // show a sample
        environment = new MadeEnvironment(bestSolutionSoFar);
        environment.runEnvironment(true);
    }
}
