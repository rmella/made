/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

            Pattern patt = Pattern.compile("([^\\.]*).r");
            Set<Object> keys = prop.keySet();
            for (Object o : keys) {
                Matcher m = patt.matcher((String) o);
                if (m.find()) {
                    classNames.add(m.group(1));
                }
            }

            for (String c : classNames) {
                MadePattern mp = new MadePattern(c, prop.getProperty(c + ".r"), prop.getProperty(c + ".w"), prop.getProperty(c + ".c"));
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
}
