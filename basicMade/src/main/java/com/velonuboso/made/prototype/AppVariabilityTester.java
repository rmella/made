/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.velonuboso.made.prototype;

import java.text.DecimalFormat;
import java.util.ArrayList;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.audit.Evaluator;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;

/**
 * Main class that can run the full experiment.
 *
 * @author Rubén Héctor García <raiben@gmail.com>
 */
public class AppVariabilityTester {

    /**
     * main method.
     *
     * @param args shell arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {


        long t0 = System.currentTimeMillis();

        RatEvaluator e = RatEvaluator.getInstance();

        Configuration conf = new DefaultConfiguration();
        FitnessFunction myFunc = new RatFitnessFunction();

        conf.setFitnessFunction(myFunc);

        Gene[] sampleGenes =
                new Gene[RatFitnessFunction.getGeneNumber()];
        for (int i = 0; i < RatFitnessFunction.getGeneNumber(); i++) {
            sampleGenes[i] = new DoubleGene(conf, 0, 1);
        }

        Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);

        conf.setPopulationSize(Parameters.getInstance().getPopulation());
        Genotype population = Genotype.randomInitialGenotype(conf);

        // show a sample of a random solution
        for (int i=0; i<10; i++){
            Integer inds[] = new Integer[]{5,10,15,20,30,40,50,60,70, 80, 90, 100};
            for (Integer ind : inds) {
                Parameters.getInstance().setNumberOfExecutions(ind);
                Gene genes[] = population.getPopulation().getChromosome(i).getGenes();
                IChromosome cr = new Chromosome(conf, genes);
                double av = cr.getFitnessValue();
                System.out.println(ind+";"+av+";");
            }
        }
        
    }

    static DecimalFormat df = new DecimalFormat("#.###");
    static DecimalFormat df2 = new DecimalFormat("###");
    static DecimalFormat df3 = new DecimalFormat("###.#####");


    public static String printIChromosome(IChromosome ic){
        StringBuilder str = new StringBuilder();
        Gene[] genes = ic.getGenes();
        boolean first = true;
        for (Gene gene : genes) {
            str.append(df.format((Double)gene.getAllele()));
            if (!first){
                str.append(";");
            }else{
                first = false;
            }
        }

        return str.toString();
    }
}
