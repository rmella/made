/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.velonuboso.made.core.common;

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
