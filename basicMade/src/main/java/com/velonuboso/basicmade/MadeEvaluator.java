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

package com.velonuboso.basicmade;

import java.io.IOException;
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
public class MadeEvaluator {

    public static String MAX_ALLOWED_EVOLUTIONS="global.MAX_ALLOWED_EVOLUTIONS";
    public static String POPULATION_SIZE="global.POPULATION_SIZE";

    public static String NUMBER_OF_PROFILES = "global.NUMBER_OF_PROFILES";
    public static String NUMBER_OF_INITIAL_AGENTS = "global.NUMBER_OF_INITIAL_AGENTS";
    public static String MAP_DIMENSION = "global.MAP_DIMENSION";
    public static String FOOD = "global.FOOD";
    public static String DAYS = "global.DAYS";
    public static String AVERAGE = "global.AVERAGE";

    public static String BASE_DAYS = "base.BASE_DAYS";
    public static String BASE_ENERGY = "base.BASE_ENERGY";
    public static String BASE_SMELL = "base.BASE_SMELL";
    public static String BASE_NUTRITION = "base.BASE_NUTRITION";
    public static String BASE_BITE = "base.BASE_BITE";
    public static String BASE_FUR = "base.BASE_FUR";
    public static String BASE_AGE_TO_BE_ADULT_FEMALE = "base.BASE_AGE_TO_BE_ADULT_FEMALE";
    public static String BASE_AGE_TO_BE_ADULT_MALE = "base.BASE_AGE_TO_BE_ADULT_MALE";
    public static String BASE_PREGNANCY_TIME = "base.BASE_PREGNANCY_TIME";




    private static MadeEvaluator instance;
    private Properties prop;
    private ArrayList<MadePattern> patterns;



    private MadeEvaluator() throws CannotCompileException, Exception {
        patterns = new ArrayList<MadePattern>();

        URL url = this.getClass().getClassLoader().getResource("evaluation.properties");
        prop = new Properties();
        try {
            prop.load(url.openStream());

            HashSet<String> classNames = new HashSet<String>();

            Pattern patt = Pattern.compile("(label\\.[^\\.]*)\\.r");
            Set<Object> keys = prop.keySet();
            for (Object o : keys) {
                Matcher m = patt.matcher((String) o);
                if (m.find()) {
                    classNames.add(m.group(1));
                }
            }

            for (String c : classNames) {
                MadePattern mp = new MadePattern(c.substring(6), prop.getProperty(c + ".r"), prop.getProperty(c + ".w"), prop.getProperty(c + ".c"));
                patterns.add(mp);
            }

        } catch (IOException ex) {
            Logger.getLogger(MadeEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static MadeEvaluator getInstance() {
        if (instance == null) {
            try {
                instance = new MadeEvaluator();
            } catch (CannotCompileException ex) {
                Logger.getLogger(MadeEvaluator.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(MadeEvaluator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }

    public double getFitness(ArrayList<MadeAgent> agents) {
        double f = 0;
        int p = 0;
        HashMap<String, Integer> pm = new HashMap<String, Integer>();
        int a = 0;
        HashMap<String, Integer> am = new HashMap<String, Integer>();

        for (MadePattern madePattern : patterns) {
            if (!am.containsKey(madePattern.getLabel())) {
                am.put(madePattern.getLabel(), 0);
            }
            if (!pm.containsKey(madePattern.getLabel())) {
                pm.put(madePattern.getLabel(), 0);
            }
        }

        for (MadeAgent madeAgent : agents) {
            p++;
            if (madeAgent.isAlive()) {
                a++;
            }

            for (MadePattern madePattern : patterns) {
                if (!am.containsKey(madePattern.getLabel())) {
                    am.put(madePattern.getLabel(), 0);
                }
                if (!pm.containsKey(madePattern.getLabel())) {
                    pm.put(madePattern.getLabel(), 0);
                }
                if (madePattern.evaluate(madeAgent.getStringLog())) {
                    madeAgent.addLabel(madePattern.getLabel());
                    if (madeAgent.isAlive()) {
                        Integer currentval = am.get(madePattern.getLabel());
                        am.put(madePattern.getLabel(), currentval + 1);
                    }
                    Integer currentval = pm.get(madePattern.getLabel());
                    pm.put(madePattern.getLabel(), currentval + 1);
                }
            }
        }

        for (MadePattern madePattern : patterns) {
            f += madePattern.getWeight(p, pm.get(madePattern.getLabel()),
                    a, am.get(madePattern.getLabel()));
        }

        return f;
    }

    public int getProperty(String key){
        Object val = prop.get(key);
        return Integer.parseInt((String)val);
    }
}
