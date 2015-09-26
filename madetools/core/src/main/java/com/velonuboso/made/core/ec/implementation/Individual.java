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

import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.common.util.StringUtil;
import com.velonuboso.made.core.ec.api.IFitnessFunction;
import com.velonuboso.made.core.ec.api.IFloatGene;
import com.velonuboso.made.core.ec.api.IGene;
import com.velonuboso.made.core.ec.api.IIndividual;
import com.velonuboso.made.core.ec.api.IIntGene;
import com.velonuboso.made.core.ec.entity.GeneDefinition;
import com.velonuboso.made.core.ec.entity.IndividualDefinition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Individual implements IIndividual {

    private ArrayList<IGene> genes;
    private IndividualDefinition definition;
    IProbabilityHelper probabilityHelper;
    private float currentFitnessValue;
    
    public Individual() {
        genes = new ArrayList<>();
        definition = null;
        probabilityHelper = ObjectFactory.createObject(IProbabilityHelper.class);
        currentFitnessValue = Float.MIN_VALUE;
    }

    @Override
    public void setGenes(IndividualDefinition definition, IGene... gene) {
        genes = new ArrayList<>(Arrays.asList(gene));
        this.definition = definition;
    }

    @Override
    public void setRandomGenes(IndividualDefinition definition) {
        this.definition = definition;
        genes.clear();
        Arrays.stream(definition.getGeneDefinition()).forEach(geneDefinition -> addNewRandomGene(geneDefinition));
    }

    @Override
    public void copyFromIndividual(IIndividual target) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reEvaluate() {
        IFitnessFunction fitnessFunction = ObjectFactory.createObject(IFitnessFunction.class);
        currentFitnessValue = fitnessFunction.evaluateIndividual(this);
    }

    @Override
    public float getCurrentFitness() {
        return currentFitnessValue;
    }

    private void addNewRandomGene(GeneDefinition geneDefinition) {
        IGene gene = buildRandomGene(geneDefinition);
        genes.add(gene);
    }

    private IGene buildRandomGene(GeneDefinition geneDefinition) {
        IGene gene;
        switch (geneDefinition.getType()) {
            case FLOAT:
                gene =  ObjectFactory.createObject(IFloatGene.class);
                gene.setValue(probabilityHelper.getNextFloat(geneDefinition.getMinValue(), geneDefinition.getMaxValue()));
                break;
            default:
                gene = ObjectFactory.createObject(IIntGene.class);
                gene.setValue(probabilityHelper.getNextInt((int)geneDefinition.getMinValue(), (int)geneDefinition.getMaxValue()));
                break;
        }
        return gene;
    }

    @Override
    public IGene[] getGenes() {
        return genes.toArray(new IGene[genes.size()]);
    }

    @Override
    public IndividualDefinition getIndividualDefinition() {
        return definition;
    }
    
    @Override
    public String toString() {
        return Arrays.deepToString(getGenes());
    }

}
