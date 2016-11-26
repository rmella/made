/*
 * Copyright (C) 2016 Rubén Héctor García (raiben@gmail.com)
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
package com.velonuboso.made.core.conceptual;

import com.velonuboso.made.core.abm.api.IAbm;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.implementation.EventsWriter;
import com.velonuboso.made.core.common.api.IEvent;
import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.api.IProbabilityHelper;
import com.velonuboso.made.core.common.entity.*;
import com.velonuboso.made.core.common.implementation.Event;
import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.customization.api.ICustomization;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class SimpleNlgConceptualTest {

    private Lexicon lexicon;
    private NLGFactory factory;
    private Realiser realiser;

    private IAbm abm;
    private ICustomization fakeCustomization;
    private InferencesEntity inferencesEntity;

    @Before
    public void setUp() {
        lexicon = Lexicon.getDefaultLexicon();
        factory = new NLGFactory(lexicon);
        realiser = new Realiser(lexicon);
    }

    @After
    public void cleanUp() {
        ObjectFactory.cleanAllMocks();
    }

    @Test
    public void test_simple_nlg_when_sentence_has_no_ending_dot_then_adds_dot_at_the_end() {
        NLGElement s1 = factory.createSentence("My name is Iñigo Montoya");
        String rewrittenSentence = realiser.realiseSentence(s1);
        assertEquals("Should've added dot at the end of the sentence", "My name is Iñigo Montoya.", rewrittenSentence);
    }

    @Test
    public void test_simple_nlg_when_parts_of_the_sentence_provided_then_sentence_is_generated() {
        SPhraseSpec phrase = factory.createClause();
        phrase.setSubject("My name");
        phrase.setVerb("be");
        phrase.setObject("Iñigo Montoya");
        String rewrittenSentence = realiser.realiseSentence(phrase);

        assertEquals("Should've generated the wholse sentence", "My name is Iñigo Montoya.", rewrittenSentence);
    }

    @Test
    public void test_simple_nlg_when_event_provided_then_sentence_is_generated() {
        int CURRENT_DAY = -1;
        EventMood CURRENT_MOOD = EventMood.NEUTRAL;
        EventType CURRENT_TYPE = EventType.ACTION;
        int EXPECTED_MINIMUM_LENGTH = 250;

        setProbabilityHelperSeed();

        NaturalLanguageEventsWriter naturalLanguageEventsWriter = new NaturalLanguageEventsWriter();
        ObjectFactory.installMock(IEventsWriter.class, naturalLanguageEventsWriter);
        runSample();
        ArrayList<IEvent> events = naturalLanguageEventsWriter.getEvents();

        String document = buildDocumentFromEvents(CURRENT_DAY, CURRENT_MOOD, CURRENT_TYPE, events);
        System.out.println(document);

        assertTrue("Message should've been more than 250 characters long", document.length() > EXPECTED_MINIMUM_LENGTH);
    }

    // private methods

    private void setProbabilityHelperSeed() {
        int SEED = 123;
        IProbabilityHelper probabilityHelper = ObjectFactory.createObject(IProbabilityHelper.class);
        probabilityHelper.setSeed(SEED);
    }

    private String buildDocumentFromEvents(int CURRENT_DAY, EventMood CURRENT_MOOD, EventType CURRENT_TYPE,
                                           ArrayList<IEvent> events) {
        StringBuilder stringBuilder = new StringBuilder();
        CoordinatedPhraseElement complexPhrase = buildPhraseFromEvents(CURRENT_DAY, CURRENT_MOOD, CURRENT_TYPE,
                stringBuilder, events);
        String text = realiser.realiseSentence(complexPhrase);
        stringBuilder.append(text);

        String document = stringBuilder.toString().replaceAll("  *", " ");
        document = document.replaceAll("^ *", "");
        return document;
    }

    private CoordinatedPhraseElement buildPhraseFromEvents(float currentDay, EventMood currentMood,
                                                           EventType currentType, StringBuilder stringBuilder,
                                                           ArrayList<IEvent> events) {

        CoordinatedPhraseElement complexPhrase = factory.createCoordinatedPhrase();
        for (IEvent iEvent : events) {
            if (iEvent.toPhrase() != EventFactory.NULL_PHRASE) {
                Event event = (Event) iEvent;
                float newDay = event.getDay();
                EventMood newMood = event.getMood();
                EventType newType = event.getType();

                if ((newDay != currentDay) || (newMood == EventMood.BAD && currentMood == EventMood.GOOD)
                        || (newMood == EventMood.GOOD && currentMood == EventMood.BAD)
                        || (newType != currentType)) {

                    String text = realiser.realiseSentence(complexPhrase);
                    stringBuilder.append(text);
                    if (newDay != currentDay) {
                        stringBuilder.append("\n\n");
                    } else {
                        stringBuilder.append(" ");
                    }
                    complexPhrase = factory.createCoordinatedPhrase();
                }
                currentDay = newDay;
                currentMood = newMood;
                currentType = newType;
                String text = realiser.realiseSentence(complexPhrase);
                complexPhrase.addCoordinate(event.toPhrase());
            }
        }
        return complexPhrase;
    }

    public void runSample() {
        fakeCustomization = Mockito.mock(ICustomization.class);
        inferencesEntity = new InferencesEntity();

        abm = ObjectFactory.createObject(IAbm.class);
        abm.setCustomization(fakeCustomization);
        abm.setInferences(inferencesEntity);

        ObjectFactory.cleanAllMocks();
        IMap map = ObjectFactory.createObject(IMap.class);
        ObjectFactory.installMock(IMap.class, map);
        AbmConfigurationEntity entity = buildSampleConfigurationEntity();

        abm.run(entity);
    }

    private AbmConfigurationEntity buildSampleConfigurationEntity() {
        int size = 52;

        IGlobalConfigurationFactory globalConfigurationFactory
                = ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        CommonAbmConfiguration config = globalConfigurationFactory.getCommonAbmConfiguration();

        float[] chromosome = new float[size];
        chromosome[0] = config.MIN_WORLD_SIZE;
        chromosome[1] = 1;
        chromosome[2] = 1;
        chromosome[3] = 1;
        chromosome[4] = config.MAX_NUMBER_OF_DAYS;
        chromosome[5] = 1;
        chromosome[6] = 0.4f;
        Arrays.fill(chromosome, 5, size, 0.5f);
        return new AbmConfigurationEntity(chromosome);
    }

    class NaturalLanguageEventsWriter extends EventsWriter {
        ArrayList<IEvent> events = new ArrayList<>();

        @Override
        public void add(IEvent event) {
            super.add(event);
            events.add(event);
        }

        public ArrayList<IEvent> getEvents() {
            return events;
        }
    }
}
