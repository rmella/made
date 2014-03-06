/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
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
package com.velonuboso.made.core.interfaces;

import com.velonuboso.made.core.common.ArchetypeType;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.math3.analysis.function.Gaussian;

/**
 * The abstract class for an archetype
 *
 * @author raiben@gmail.com
 */
public abstract class Archetype {

    protected ArrayList<Class> dependencies = new ArrayList<Class>();
    protected String processedName = null;

    /**
     * gets the name of the archetype.
     *
     * @return the name of the archetype
     */
    public String getArchetypeName() {
        if (processedName == null) {
            StringBuilder stb = new StringBuilder();

            char[] arr = this.getClass().getSimpleName().toCharArray();
            for (int i=0; i<arr.length; i++){
                char aux = arr[i];
                if (Character.isUpperCase(aux)){
                    if (i==0){
                        stb.append(aux);
                    }else{
                        stb.append(" ");
                        stb.append(Character.toLowerCase(aux));
                    }
                }else{
                    stb.append(aux);
                }
            }
            processedName = stb.toString();
        }
        return processedName;
    }

    /**
     * gets the description of the archetype.
     *
     * @return the description
     */
    public abstract String getDescription();

    /**
     * evaluates the fitness of a list of agents, given a global setup and the
     * desired occurence.
     *
     * @param setup the global setup
     * @param agents the full set of agents
     * @param o the target occurrence
     * @return the fitness as a double
     */
    public abstract double evaluate(
            GlobalSetup setup,
            ArrayList<MadeAgentInterface> agents,
            ArchetypeOccurrence o);

    /**
     * gets the type of the archetype.
     *
     * @return the type of the archetype
     */
    public ArchetypeType getType() {
        return ArchetypeType.LITERARY;
    }

    /**
     * gets the dependencies of other archetypes. i.e.: if an Archetype A relies
     * in the existence of the archetype B, this method will return an array
     * with the B class.
     *
     * @return the classes that this one dependes on. By default an empty array.
     */
    public ArrayList<Class> getDependencies() {
        return dependencies;
    }
}
