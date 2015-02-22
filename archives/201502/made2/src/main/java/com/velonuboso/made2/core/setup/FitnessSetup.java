/*
 * Copyright 2014 Rubén Héctor García <raiben@gmail.com>.
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


package com.velonuboso.made2.core.setup;

import com.velonuboso.made2.core.interfaces.ArchetypeOccurrence;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author raiben@gmail.com
 */
public class FitnessSetup {
    private ArrayList<Class> archetypes;
    private ArrayList<ArchetypeOccurrence> occurrence;

    public FitnessSetup() {
        archetypes = new ArrayList<Class>();
        occurrence = new ArrayList<ArchetypeOccurrence>();
    }

    public FitnessSetup(Properties p) throws ClassNotFoundException {
        this();
        for (Object o: p.keySet()){
            String key = (String) o;
            
            if (key.startsWith("archetype.")){
                String[] ts = key.split("\\.");
                if (ts[2].compareTo("check")==0){
                    Boolean b = new Boolean(p.getProperty(key));
                    if (b){
                        Integer par = Integer.parseInt(p.getProperty("archetype."+ts[1]+".param"));
                        ArchetypeOccurrence occ = ArchetypeOccurrence.getArchetype(par);
                        this.add(Class.forName("com.velonuboso.made2.core.rat.archetypes."+ts[1]), occ);
                    }
                }
            }
        }
    }
    
    
    
    public void add(Class c, ArchetypeOccurrence o){
        archetypes.add(c);
        occurrence.add(o);
        
    }
    
    public Class getClass(int i){
        return archetypes.get(i);
    }

    public ArchetypeOccurrence getOccurrence(int i) {
        return occurrence.get(i);
    }
    
    public int getSize(){
        return archetypes.size();
    }
}
