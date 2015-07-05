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
package com.velonuboso.made.core.narration.implementation;

import com.velonuboso.made.core.common.entity.EventsLogEntity;
import com.velonuboso.made.core.common.util.StringUtil;
import com.velonuboso.made.core.customization.api.ICustomization;
import com.velonuboso.made.core.narration.api.INarrator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Narrator implements INarrator {

    private static final String PATTERN_SPLIT_ARGUMENTS = "(?<=\\\"?),(?!\\\")|(?<!\\\"),(?=\\\")";
    private static final String PATTERN_SPLIT_PARTS = "[\\(\\)]";
    private static final String SENTENCE_SEPARATOR = " ";
    private static final String SENTENCE_END = ".";

    private ICustomization customization;
    private StringBuilder narration;
    private EventsLogEntity eventsLog;

    public Narrator() {
        this.customization = null;
        this.eventsLog = null;
        this.narration = new StringBuilder();
    }

    @Override
    public void setCustomization(ICustomization customization) {
        this.customization = customization;
    }

    @Override
    public void setEventsLog(EventsLogEntity eventsLog) {
        this.eventsLog = eventsLog;
    }

    @Override
    public void narrate() {
        if (canProcessNarration()) {
            processNarration();
        }
    }
    
    @Override
    public String getNarration() {
        return narration.toString();
    }

    private boolean canProcessNarration() {
        return eventsLog != null && customization != null;
    }

    private void processNarration() {
        narration = new StringBuilder();
        for (EventsLogEntity.DayLog dayLog : eventsLog.getDayLogs()) {
            processDayLog(dayLog);
        }
    }

    private void processDayLog(EventsLogEntity.DayLog dayLog) {
        for (EventsLogEntity.EventEntity eventEntity : dayLog.getEvents()) {
            processEventEntity(eventEntity);
        }
    }

    private void processEventEntity(EventsLogEntity.EventEntity eventEntity) {
        String predicate = eventEntity.getPredicate();

        String predicateName = extractPredicateName(predicate);
        List<String> arguments = extractArguments(predicate);

        customization.getNarrationRules().stream().filter(rule
                -> rule.getPredicateName().equals(predicateName) && rule.getNumberOfArguments() == arguments.size()
        ).forEach(rule -> appendNarration(rule.getNaturalLanguageTemplate(), arguments));
    }

    private void appendNarration(String naturalLanguageTemplate, List<String> arguments) {
        String sentence = StringUtil.replaceArguments(naturalLanguageTemplate, arguments);
        if (narration.length() > 0) {
            narration.append(SENTENCE_SEPARATOR);
        }
        narration.append(sentence);
        narration.append(sentence.endsWith(SENTENCE_END) ? "" : SENTENCE_END);
    }

    private String extractPredicateName(String predicate) {
        String name = predicate.split(PATTERN_SPLIT_PARTS)[0];
        return name.trim();
    }

    private List<String> extractArguments(String predicate) {
        String[] predicateParts = predicate.split(PATTERN_SPLIT_PARTS);
        if (predicateParts.length > 1) {
            String arguments = predicateParts[1];
            String arrayOfArguments[] = arguments.split(PATTERN_SPLIT_ARGUMENTS);

            return Arrays.stream(arrayOfArguments).map(
                    argument -> StringUtil.cleanArgument(argument)
            ).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

}
