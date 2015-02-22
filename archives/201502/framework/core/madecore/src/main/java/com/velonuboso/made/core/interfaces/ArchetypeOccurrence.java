/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.made.core.interfaces;

/**
 *
 * @author rhgarcia
 */
public enum ArchetypeOccurrence {

    DONT_MIND,
    THE_MORE_THE_BETTER,
    THE_LESS_THE_BETTER;

    public static final double C = Math.log10(11);//1.04139268515823;

    public static ArchetypeOccurrence getArchetype(int value) {
        ArchetypeOccurrence ret;
        switch (value) {
            case 0:
                ret = THE_LESS_THE_BETTER;
                break;
            case 1:
                ret = DONT_MIND;
                break;
            case 2:
                ret = THE_MORE_THE_BETTER;
                break;
            default:
                ret = null;
                break;
        }
        return ret;
    }

    public double getValue(int total, int matchTotal) {
        double ret = 0;
        double proportion = (double)matchTotal / (double)total;
        switch (this) {
            case THE_MORE_THE_BETTER:
                ret = Math.log10((proportion * 10.0d) + 1d) / C;
                break;
            case THE_LESS_THE_BETTER:
                ret = 1 - (Math.log10((proportion * 10.0d) + 1d) / C);
                break;
            default:
                ret = 0;
                break;
        }
        return ret;
    }

    public String toShortString() {
        String ret;
        switch (this) {
            case THE_MORE_THE_BETTER:
                ret = "Yes";
                break;
            case THE_LESS_THE_BETTER:
                ret = "No";
                break;
            default:
                ret = "";
                break;
        }
        return ret;
    }
}

