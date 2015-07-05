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
package com.velonuboso.made.core.narration.unittest;

import com.velonuboso.made.core.common.entity.EventsLogEntity;
import com.velonuboso.made.core.customization.api.ICustomization;
import com.velonuboso.made.core.customization.entity.NarrationRuleEntity;
import com.velonuboso.made.core.narration.implementation.Narrator;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class NarratorTest {

    private Narrator narrator = new Narrator();

    public NarratorTest() {

    }

    @Test
    public void UT_Sample_predicate_with_no_argument_is_transformed_to_valid_narration() {
        final String predicate = "SpiritedAway ()";
        final String expectedNarration = "Listen, Haku. I don't remember it, "
                + "but my mom told me... Once, when I was little, I fell into "
                + "a river.";
        final NarrationRuleEntity chihiroSpeechRule = new NarrationRuleEntity(
                "SpiritedAway", 0, "Listen, Haku. I don't remember it, "
                + "but my mom told me... Once, when I was little, I fell into "
                + "a river"
        );

        String currentNarration = getNarrationFromPredicateAndRule(predicate, chihiroSpeechRule);

        assertEquals("Should've found the predicate in the world's backstories "
                + "and generate the proper narration",
                expectedNarration, currentNarration);
    }

    @Test
    public void UT_Sample_predicate_with_no_argument_and_rule_mismatch_argument_number_is_transformed_to_empty_narration() {
        final String predicate = "SpiritedAway ()";
        final String expectedNarration = "Listen, Haku. I don't remember it";
        final NarrationRuleEntity chihiroSpeechRule = new NarrationRuleEntity(
                "SpiritedAway", 1, expectedNarration
        );

        String currentNarration = getNarrationFromPredicateAndRule(predicate, chihiroSpeechRule);

        assertEquals("Should've found the predicate in the world's backstories "
                + "and generate the proper narration",
                "", currentNarration);
    }

    @Test
    public void UT_Sample_predicate_with_one_argument_is_transformed_to_valid_narration() {
        final String predicate = "SpiritedAway (Haku)";
        final String expectedNarration = "Listen, Haku. I don't remember it.";
        final NarrationRuleEntity chihiroSpeechRule = new NarrationRuleEntity(
                "SpiritedAway", 1, "Listen, %s. I don't remember it"
        );

        String currentNarration = getNarrationFromPredicateAndRule(predicate, chihiroSpeechRule);

        assertEquals("Should've found the predicate in the world's backstories "
                + "and generate the proper narration",
                expectedNarration, currentNarration);
    }

    @Test
    public void UT_Sample_predicate_with_two_arguments_is_transformed_to_valid_narration() {
        final String predicate = "SpiritedAway (Haku, my mom)";
        final String expectedNarration = "Listen, Haku. I don't remember it, "
                + "but my mom told me.";
        final NarrationRuleEntity chihiroSpeechRule = new NarrationRuleEntity(
                "SpiritedAway", 2, "Listen, %s. I don't remember it, "
                + "but %s told me"
        );

        String currentNarration = getNarrationFromPredicateAndRule(predicate, chihiroSpeechRule);

        assertEquals("Should've found the predicate in the world's backstories "
                + "and generate the proper narration",
                expectedNarration, currentNarration);
    }
    
    @Test
    public void UT_Sample_predicate_ending_with_dot_is_transformed_to_valid_narration_with_no_extra_dot() {
        final String predicate = "SpiritedAway (Haku, my mom)";
        final String expectedNarration = "Listen, Haku. I don't remember it, "
                + "but my mom told me...";
        final NarrationRuleEntity chihiroSpeechRule = new NarrationRuleEntity(
                "SpiritedAway", 2, "Listen, %s. I don't remember it, "
                + "but %s told me..."
        );

        String currentNarration = getNarrationFromPredicateAndRule(predicate, chihiroSpeechRule);

        assertEquals("Should've found the predicate in the world's backstories "
                + "and generate the proper narration",
                expectedNarration, currentNarration);
    }
    
    @Test
    public void UT_Sample_predicate_with_one_argument_with_extra_blanks_is_transformed_to_valid_narration() {
        final String predicate = "SpiritedAway (Haku    )";
        final String expectedNarration = "Listen, Haku. I don't remember it.";
        final NarrationRuleEntity chihiroSpeechRule = new NarrationRuleEntity(
                "SpiritedAway", 1, "Listen, %s. I don't remember it"
        );

        String currentNarration = getNarrationFromPredicateAndRule(predicate, chihiroSpeechRule);

        assertEquals("Should've found the predicate in the world's backstories "
                + "and generate the proper narration",
                expectedNarration, currentNarration);
    }
    
    @Test
    public void UT_Sample_predicate_with_one_argument_with_quotes_and_extra_blanks_is_transformed_to_valid_narration() {
        final String predicate = "SpiritedAway ( \"Haku\"    )";
        final String expectedNarration = "Listen, Haku. I don't remember it.";
        final NarrationRuleEntity chihiroSpeechRule = new NarrationRuleEntity(
                "SpiritedAway", 1, "Listen, %s. I don't remember it"
        );

        String currentNarration = getNarrationFromPredicateAndRule(predicate, chihiroSpeechRule);

        assertEquals("Should've found the predicate in the world's backstories "
                + "and generate the proper narration",
                expectedNarration, currentNarration);
    }
    
    @Test
    public void UT_Sample_predicate_with_one_argument_with_quotes_and_extra_blanks_inside_is_transformed_to_valid_narration() {
        final String predicate = "SpiritedAway ( \"   Haku\"    )";
        final String expectedNarration = "Listen,    Haku. I don't remember it.";
        final NarrationRuleEntity chihiroSpeechRule = new NarrationRuleEntity(
                "SpiritedAway", 1, "Listen, %s. I don't remember it"
        );

        String currentNarration = getNarrationFromPredicateAndRule(predicate, chihiroSpeechRule);

        assertEquals("Should've found the predicate in the world's backstories "
                + "and generate the proper narration",
                expectedNarration, currentNarration);
    }

    @Test
    public void UT_Sample_predicate_with_multiple_arguments_is_transformed_to_narration() {
        final String predicate = "SpiritedAway (Haku, \"my mom\", river)";
        final NarrationRuleEntity chihiroSpeechRule = new NarrationRuleEntity(
                "SpiritedAway", 3,
                "Listen, %s. I don't remember it, "
                + "but %s told me... Once, when I was little, I fell into "
                + "a %s"
        );
        final String expectedNarration = "Listen, Haku. I don't remember it, "
                + "but my mom told me... Once, when I was little, I fell into "
                + "a river.";

        String currentNarration = getNarrationFromPredicateAndRule(predicate, chihiroSpeechRule);

        assertEquals("Should've found the predicate in the world's backstories "
                + "and generate the proper narration",
                expectedNarration, currentNarration);
    }

    @Test
    public void UT__predicates_are_transformed_to_multi_sentence_narration() {
        final String predicates[] = new String[]{
            "SpiritedAway (Haku, \"my mom\", river)",
            "SpiritedAway2 ()"
        };
        final NarrationRuleEntity chihiroSpeechRules[] = new NarrationRuleEntity[]{
            new NarrationRuleEntity("SpiritedAway", 3,
                "Listen, %s. I don't remember it, "
                + "but %s told me... Once, when I was little, I fell into "
                + "a %s"),
            new NarrationRuleEntity("SpiritedAway2", 0,
                "She said they'd drained it and built things on top")
        };
        
        final String expectedNarration = "Listen, Haku. I don't remember it, "
                + "but my mom told me... Once, when I was little, I fell into "
                + "a river. She said they'd drained it and built things on top.";

        String currentNarration = getNarrationFromPredicatesAndRules(predicates, chihiroSpeechRules);

        assertEquals("Should've found the predicate in the world's backstories "
                + "and generate the proper narration",
                expectedNarration, currentNarration);
    }
    
    private String getNarrationFromPredicateAndRule(final String predicate, final NarrationRuleEntity chihiroSpeechRule) {
        EventsLogEntity worldsStories = buildSimpleWorldStories(predicate);
        ICustomization fakeCustomization = buildFakeCustomization(chihiroSpeechRule);
        return getNarrationFromCustomizationAndWorldStories(fakeCustomization, worldsStories);
    }
    
    private String getNarrationFromPredicatesAndRules(final String predicates[], final NarrationRuleEntity chihiroSpeechRules[]) {
        EventsLogEntity worldsStories = buildSimpleWorldStories(predicates);
        ICustomization fakeCustomization = buildFakeCustomization(chihiroSpeechRules);
        return getNarrationFromCustomizationAndWorldStories(fakeCustomization, worldsStories);
    }

    private String getNarrationFromCustomizationAndWorldStories(ICustomization fakeCustomization, EventsLogEntity worldsStories) {
        narrator = new Narrator();
        narrator.setCustomization(fakeCustomization);
        narrator.setEventsLog(worldsStories);
        narrator.narrate();
        String currentNarration = narrator.getNarration();
        return currentNarration;
    }

    private EventsLogEntity buildSimpleWorldStories(final String ... predicates) {
        
        EventsLogEntity.EventEntity entities[] = new EventsLogEntity.EventEntity[predicates.length];
        for(int iterator = 0; iterator<entities.length; iterator++){
            entities[iterator] = new EventsLogEntity.EventEntity(0, null, predicates[iterator]);
        }
        
        EventsLogEntity worldsStories = new EventsLogEntity(
                null,
                null,
                new EventsLogEntity.DayLog[]{
                    new EventsLogEntity.DayLog(0, entities)
                });
        
        
        return worldsStories;
    }

    private ICustomization buildFakeCustomization(final NarrationRuleEntity ... rules) {
        ArrayList<NarrationRuleEntity> narrationRules = new ArrayList<>();
        narrationRules.addAll(Arrays.asList(rules));
        ICustomization fakeCustomization = mock(ICustomization.class);
        stub(fakeCustomization.getNarrationRules()).toReturn(narrationRules);
        return fakeCustomization;
    }

}
