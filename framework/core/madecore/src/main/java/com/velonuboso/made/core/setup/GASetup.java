/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made.core.setup;

/**
 *
 * @author rhgarcia
 */
public class GASetup {

    private int txtExecutionsAVG;
    private int txtGenerations;
    private int txtPopulation;

    public GASetup(int txtExecutionsAVG, int txtGenerations, int txtPopulation) {
        this.txtExecutionsAVG = txtExecutionsAVG;
        this.txtGenerations = txtGenerations;
        this.txtPopulation = txtPopulation;
    }

    public int getTxtExecutionsAVG() {
        return txtExecutionsAVG;
    }

    public int getTxtGenerations() {
        return txtGenerations;
    }

    public int getTxtPopulation() {
        return txtPopulation;
    }

}
