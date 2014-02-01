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

package com.velonuboso.made.core.rat;

import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import com.velonuboso.made.core.interfaces.MadeEvaluatorInterface;
import com.velonuboso.made.core.interfaces.MadeEvaluatorInterface;
import com.velonuboso.made.core.common.MadePattern;
import com.velonuboso.made.core.common.MadePattern;
import com.velonuboso.made.core.interfaces.Archetype;
import com.velonuboso.made.core.rat.archetypes.Survival;
import com.velonuboso.made.core.setup.FitnessSetup;
import com.velonuboso.made.core.setup.GlobalSetup;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javassist.CannotCompileException;

/**
 *
 * @author raiben@gmail.com
 */
public class RatEvaluator implements MadeEvaluatorInterface {

    FitnessSetup fsetup;
    GlobalSetup gsetup;
    
    
    public RatEvaluator(FitnessSetup s, GlobalSetup gp){
        fsetup = s;
        gsetup = gp;
        
    }

    @Override
    public double getFitness(ArrayList<MadeAgentInterface> agents) {
        
        double result = 0;
        
        for (int i=0; i<fsetup.getSize(); i++){
            try {
                Class c = fsetup.getClass(i);
                Archetype arch = (Archetype) c.getConstructors()[0].newInstance();
                double d = arch.evaluate(gsetup, agents, fsetup.getParam(i)-0.5f, fsetup.getParam(i)+0.5f);
                result += (d*fsetup.getFitness(i));
            } catch (Exception ex) {
                Logger.getLogger(RatEvaluator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
}
