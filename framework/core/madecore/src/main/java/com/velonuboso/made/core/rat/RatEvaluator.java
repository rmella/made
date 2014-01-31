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
 * @author Ruben
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
