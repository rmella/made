/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.core.interfaces;

import java.util.ArrayList;

/**
 *
 * @author rhgarcia
 */
public interface ExecutionListenerInterface {
    
    public void start();
    
    public void end();
    
    public void progress (float value);
    
    public void generation (int id, float fitnessMax, float fitnessAVG, ArrayList individual);
    
    public void failure (Exception e);

    public void log (String text);

    public float getProgress();
    
    public void environmentExecuted (ArrayList<MadeAgentInterface> agents);
    
}
