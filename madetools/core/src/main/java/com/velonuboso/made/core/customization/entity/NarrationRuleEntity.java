/*
 * Copyright (C) 2015 ubén Héctor García (raiben@gmail.com)
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
package com.velonuboso.made.core.customization.entity;

import java.util.Objects;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class NarrationRuleEntity {

    private String predicateName;
    private Integer numberOfArguments;
    private String naturalLanguageTemplate;

    public NarrationRuleEntity() {
    }

    public NarrationRuleEntity(String predicateName, Integer numberOfArguments, String naturalLanguageTemplate) {
        this.predicateName = predicateName;
        this.numberOfArguments = numberOfArguments;
        this.naturalLanguageTemplate = naturalLanguageTemplate;
    }

    public String getPredicateName() {
        return predicateName;
    }

    public void setPredicateName(final String predicateName) {
        this.predicateName = predicateName;
    }

    public Integer getNumberOfArguments() {
        return numberOfArguments;
    }

    public void setNumberOfArguments(Integer numberOfArguments) {
        this.numberOfArguments = numberOfArguments;
    }

    public String getNaturalLanguageTemplate() {
        return naturalLanguageTemplate;
    }

    public void setNaturalLanguageTemplate(String naturalLanguageTemplate) {
        this.naturalLanguageTemplate = naturalLanguageTemplate;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (!(obj instanceof NarrationRuleEntity)) {
            return false;
        }
        
        NarrationRuleEntity target = (NarrationRuleEntity) obj;
        return Objects.equals(this.naturalLanguageTemplate, target.naturalLanguageTemplate)
                && this.numberOfArguments == target.numberOfArguments
                && Objects.equals(this.predicateName, target.predicateName);
    }
}
