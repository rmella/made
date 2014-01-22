/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.core.interfaces;

import com.velonuboso.made.core.setup.GlobalSetup;
import java.util.ArrayList;
import org.apache.commons.math3.analysis.function.Gaussian;

/**
 *
 * @author rhgarcia
 */
public abstract class Archetype {
    
    public abstract String getName();
    
    public abstract double evaluate(
            GlobalSetup setup, 
            ArrayList<MadeAgentInterface> agents, 
            Float from, 
            Float to);

    public static double getGaussian(float ratio, Float from, Float to) {
        Gaussian gaussian = new Gaussian();
        if (from == null && to == null){
            return 1;
        }else if (from==null){
            from = 1-to;
        }else if (to==null){
            to = 2-from;
        }
        float target = (from + to) /2f;
        float amplitude = to - from;
        return gaussian.value((ratio-target)/amplitude)/0.40;
    }
    
}
