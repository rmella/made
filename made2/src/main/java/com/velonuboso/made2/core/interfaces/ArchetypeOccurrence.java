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

package com.velonuboso.made2.core.interfaces;

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

