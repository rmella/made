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

import com.velonuboso.made.core.common.entity.EventsLog;
import com.velonuboso.made.core.customization.api.ICustomization;
import com.velonuboso.made.core.customization.entity.NarrationRuleEntity;
import com.velonuboso.made.core.narration.implementation.Narrator;
import java.util.ArrayList;
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
    public void Sample_predicate_is_transformed_to_narration(){
        final String predicate = "SpiritedAway (Haku, \"my mom\", river)";
        final NarrationRuleEntity chihiroSpeechRule = new NarrationRuleEntity(
                "SpiritedAway", 3, 
                "Listen, %s. I don't remember it, "
                + "but %s told me... Once, when I was little, I fell into "
                + "a %s."
        );
        final String expectedNarration = "Listen, Haku. I don't remember it, "
                + "but my mom told me... Once, when I was little, I fell into "
                + "a river.";
        
        EventsLog worldsStories = new EventsLog(
                null, 
                null, 
                new EventsLog.DayLog[]{
                    new EventsLog.DayLog(
                            0, 
                            new EventsLog.EventEntity[]{
                                new EventsLog.EventEntity(0, null, predicate)
                            })
                });
        ArrayList<NarrationRuleEntity> narrationRules = new ArrayList<>();
        narrationRules.add(chihiroSpeechRule);
        
        ICustomization fakeCustomization = mock(ICustomization.class);
        stub(fakeCustomization.getNarrationRules()).toReturn(narrationRules);
        
        narrator = new Narrator();
        narrator.setCustomization(fakeCustomization);
        narrator.setEventsLog(worldsStories);
        String currentNarration = narrator.getNarration();
        
        assertEquals("Should've found the predicate in the world's backstories "
                + "and generate the proper narration",
                expectedNarration, currentNarration);
    }
    
}
