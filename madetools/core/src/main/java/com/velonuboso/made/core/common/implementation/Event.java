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
package com.velonuboso.made.core.common.implementation;

import alice.tuprolog.Int;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.entity.EventMood;
import com.velonuboso.made.core.common.entity.EventType;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;

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
    private NLGElement phrase = null;
    private EventMood mood = EventMood.NEUTRAL;
    private EventType type = EventType.DESCRIPTION;
    private float currentDay;

    public Event(float day, final EventMood mood, final EventType type, final NLGElement phrase, final String name,
                 final Object... arguments){
        this.currentDay = day;
        this.mood = mood;
        this.type = type;
        this.phrase = phrase;
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

    @Override
    public Term toLogicalTerm() {
        Term[] argumentsAsTerms = Arrays.stream(arguments).map(argument -> objectToTerm(argument)).toArray(Term[]::new);
        return new Struct(name, argumentsAsTerms);
    }

    @Override
    public NLGElement toPhrase() {
        return this.phrase;
    }

    @Override
    public EventMood getMood() {
        return mood;
    }

    @Override
    public EventType getType() { return type; }

    @Override
    public float getDay() {
        return currentDay;
    }

    private Term objectToTerm(Object object) {
        if (object instanceof Integer) {
            return new alice.tuprolog.Int((int) object);
        } else if (object instanceof String) {
            return new Struct((String) object);
        } else if (object instanceof Float) {
            return new alice.tuprolog.Float((float) object);
        } else if (object instanceof int[]) {
            Int[] terms = Arrays.stream((int[])object)
                    .mapToObj(element -> new Int(element))
                    .toArray(Int[]::new);
            return new Struct(terms);
        } else {
            throw new RuntimeException("Could not convert object " + object + " to term");
        }
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
        return argument instanceof Integer || argument instanceof Float;
    }

    private String argumentAsString(Object argument) {
        if (argument == null) {
            argument = "NULL";
        }
        return isANumber(argument) ? argument.toString() : decorateStringWithQuotes(argument);
    }
}
