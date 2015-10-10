/*
 * Copyright (C) 2015 Rubén Héctor García (raiben@gmail.com)
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

package com.velonuboso.made.core.inference.implementation;

import alice.tuprolog.Term;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class TermWrapper {
    Term term;

    public TermWrapper(Term term) {
        this.term = term;
    }
    public Term unwrap(){
        return term;
    }

    @Override
    public boolean equals(Object target) {
        if (target instanceof Term){
            return this.toString().compareTo(((Term)target).toString())==0;
        }
        return false;
    }
    
}
