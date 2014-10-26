/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made2.ga;

/**
 *
 * @author rhgarcia
 */
class GaPartialFitness {
    private String name;
    private double value;

    public GaPartialFitness(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

}
