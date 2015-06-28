/*
 * Copyright (C) 2015 rhgarcia
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
package com.velonuboso.made.core.common.implementation;

import com.velonuboso.made.core.common.api.IEvent;
import java.util.Arrays;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Event implements IEvent {

    private static final String ARGUMENT_BEGIN = " (";
    private static final String ARGUMENT_SEPARATOR = ", ";
    private static final String ARGUMENT_END = ")";
    private static final String ARGUMENT_QUOTE = "\"";

    private final String name;
    private final Object[] arguments;

    public Event(final String name, final Object... arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public String toLogicalPredicate() {
        final StringBuilder logicalPredicate = new StringBuilder();
        
        logicalPredicate.append(name);
        logicalPredicate.append(ARGUMENT_BEGIN);
        logicalPredicate.append(getCommaSeparatedArguments());
        logicalPredicate.append(ARGUMENT_END);
        
        return logicalPredicate.toString();
    }

    private String getCommaSeparatedArguments() {
        String[] argumentsArray = Arrays.stream(arguments).map(argument -> argumentAsString(argument)).toArray(String[]::new);
        String commaSeparatedArguments = String.join(ARGUMENT_SEPARATOR, argumentsArray);
        return commaSeparatedArguments;
    }

    private String decorateStringWithQuotes(Object argument) {
        return ARGUMENT_QUOTE + argument.toString() + ARGUMENT_QUOTE;
    }
    
    private boolean isANumber(Object argument) {
        return argument instanceof Integer;
    }

    private String argumentAsString(Object argument) {
        return isANumber(argument)? argument.toString(): decorateStringWithQuotes(argument);
    }
}
