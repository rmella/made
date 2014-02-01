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

package com.velonuboso.made.core.setup;

/**
 *
 * @author raiben@gmail.com
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
