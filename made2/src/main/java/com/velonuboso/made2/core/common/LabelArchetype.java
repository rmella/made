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


package com.velonuboso.made2.core.common;

/**
 *
 * @author rhgarcia
 */
public class LabelArchetype {
    
    private String archetypeName;
    private Integer fromDate;
    private Integer toDate;
    private String explanation;

    public LabelArchetype() {
        this(null, null, null, null);
    }

    public LabelArchetype(String aName) {
        this(aName, null, null, null);
    }

    
    public LabelArchetype(String archetypeName, Integer fromDate, Integer toDate, String explanation) {
        this.archetypeName = archetypeName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.explanation = explanation;
    }

    public String getArchetypeName() {
        return archetypeName;
    }

    public String getExplanation() {
        return explanation;
    }

    public Integer getFromDate() {
        return fromDate;
    }

    public Integer getToDate() {
        return toDate;
    }

}
