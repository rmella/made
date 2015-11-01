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
package com.velonuboso.made.core.inference;

import alice.tuprolog.Int;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.UnknownVarException;
import com.velonuboso.made.core.abm.api.IBehaviourTreeNode;
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IColorSpot;
import com.velonuboso.made.core.abm.api.IEventsWriter;
import com.velonuboso.made.core.abm.api.IMap;
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.common.api.IEventFactory;
import com.velonuboso.made.core.common.entity.AbmConfigurationEntity;
import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.inference.api.IReasoner;
import com.velonuboso.made.core.inference.entity.Trope;
import com.velonuboso.made.core.inference.entity.WorldDeductions;
import com.velonuboso.made.core.inference.implementation.MonomythReasoner;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.mockito.Mockito;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class MonomythReasonerTest {

    private Prolog engine;
    private ArrayList<SolveInfo> solutions;
    private ICharacter characterPeter;
    private ICharacter characterArthur;
    private ICharacter characterMaggie;
    private ICharacter characterSomeone;
    private IColorSpot spotEmerald;
    private IColorSpot spotRuby;
    private IReasoner reasoner;
    IEventFactory eventFactory;
    
    public MonomythReasonerTest() {
    }

    @Before
    public void setUp() {
        engine = new Prolog();
        solutions = new ArrayList<>();
        characterPeter = buildPuppetCharacter(20, Color.ALICEBLUE, Color.YELLOW, CharacterShape.CIRCLE);
        characterArthur = buildPuppetCharacter(21, Color.BLACK, Color.WHITE, CharacterShape.TRIANGLE);
        characterMaggie = buildPuppetCharacter(23, Color.AZURE, Color.DARKSALMON, CharacterShape.CIRCLE);
        characterSomeone = buildPuppetCharacter(24, Color.ALICEBLUE, Color.ALICEBLUE, CharacterShape.CIRCLE);
        spotEmerald = buildPuppetSpot(100, Color.GREEN);
        spotRuby = buildPuppetSpot(100, Color.RED);
        reasoner = ObjectFactory.createObject(IReasoner.class);
        eventFactory = ObjectFactory.createObject(IEventFactory.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void UT_Tuprolog_works() {

        String predicateArray[] = new String[]{
            "associatedWith(\"a\",\"b\")",
            "associatedWith(\"b\",\"c\")",
            "associatedWith(\"c\",\"d\")",
            "associated(X,Y):-associatedWith(X,Y)",
            "associated(X,Z):-associatedWith(X,Y),associatedWith(Y,Z)"
        };
        IntStream.range(0, predicateArray.length).forEach(index -> predicateArray[index] = predicateArray[index] + ".");
        String theoryAsString = String.join("\n", predicateArray);

        String predicateToSolve = "associated(Z, T).";

        solveWithReasoner(theoryAsString, predicateToSolve);

        assertTrue("Should've found 5 solutions", solutions.size() == 5);
    }
    
    @Test
    public void UT_ShowRulesFromTheMonomyth() {
        MonomythReasoner monomythReasoner = new MonomythReasoner();
        String rules = monomythReasoner.getMonomythRulesAsString();
        System.out.println("Rules:\n"+rules);
        assertFalse("The monomyth reasoner should use a non-empty set of rules",
                StringUtils.isBlank(rules));
    }
    
    @Test
    public void UT_ConflictNotFound_whenThereIsNoConflictBetweenElements() {
        Term[] terms = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.colorSpotAppears(spotEmerald, 30).toLogicalTerm()
        };
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, new Trope[]{Trope.CONFLICT});
        assertNumberOfTropes(worldDeductions, Trope.CONFLICT, 0);
    }

    @Test
    public void UT_WhenTwoCharactersHaveAffinityWithEachOther_TheyAreRealFriends() {
        Term[] terms = new Term[]{
            eventFactory.isFriendOf(characterPeter, characterArthur, characterSomeone).toLogicalTerm(),
            eventFactory.isFriendOf(characterArthur, characterPeter, characterSomeone).toLogicalTerm()
        };
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, new Trope[]{Trope.REAL_FRIENDS});
        assertNumberOfTropes(worldDeductions, Trope.REAL_FRIENDS, 2);
    }
    
    @Test
    public void UT_WhenTwoCharactersDoNotHaveAffinityWithEachOther_TheyAreRealEnemies() {
        Term[] terms = new Term[]{
            eventFactory.isEnemyOf(characterPeter, characterArthur, characterSomeone).toLogicalTerm(),
            eventFactory.isEnemyOf(characterArthur, characterPeter, characterSomeone).toLogicalTerm()
        };
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, new Trope[]{Trope.REAL_ENEMIES});
        assertNumberOfTropes(worldDeductions, Trope.REAL_ENEMIES, 2);
    }
    
    @Test
    public void UT_WhenACharacterAndASpotappear_TwoElementsAreFound() {
        Term[] terms = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.colorSpotAppears(spotEmerald, 30).toLogicalTerm()
        };
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, Trope.getBaseElements());
        assertNumberOfTropes(worldDeductions, Trope.ELEMENT, 2);
    }
    
    @Test
    public void UT_WhenACharacterMovesAway_ConflictAppears() {
        Term[] terms = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm(),
            eventFactory.hasFear(characterPeter, characterArthur).toLogicalTerm(),
            eventFactory.movesAway(characterPeter, 22).toLogicalTerm(),
            eventFactory.isEnemyOf(characterPeter, characterArthur).toLogicalTerm(),
            eventFactory.isEnemyOf(characterArthur, characterPeter).toLogicalTerm()
        };
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, Trope.getBaseElements());
        assertNumberOfTropes(worldDeductions, Trope.CONFLICT, 1);
    }
    
    @Test
    public void UT_WhenACharacterDisplacesOther_ConflictAppears() {
        Term[] terms = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm(),
            eventFactory.displaces(characterArthur, characterPeter, 22).toLogicalTerm(),
            eventFactory.isEnemyOf(characterPeter, characterArthur).toLogicalTerm(),
            eventFactory.isEnemyOf(characterArthur, characterPeter).toLogicalTerm()
        };
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, Trope.getBaseElements());
        assertNumberOfTropes(worldDeductions, Trope.CONFLICT, 1);
    }
    
    @Test
    public void UT_WhenACharacterCannotStainWithSpotButOtherCan_ConflictAppears() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm(),
            eventFactory.colorSpotAppears(spotEmerald, 30).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.canImproveSelfSimilarity(characterPeter, spotEmerald).toLogicalTerm()
        };
        eventFactory.setDay(2);
        Term[] termsDay2 = new Term[]{
            eventFactory.stains(characterArthur, spotEmerald).toLogicalTerm(),
            eventFactory.isEnemyOf(characterPeter, characterArthur).toLogicalTerm(),
            eventFactory.isEnemyOf(characterArthur, characterPeter).toLogicalTerm()
        };
        
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1, termsDay2);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, Trope.getBaseElements());
        assertNumberOfTropes(worldDeductions, Trope.CONFLICT, 1);
    }
    
    @Test
    public void UT_WhenTwoCharactersAreFriendsAndOtherPiecethatIsAnEnemyDisplacesOneOfThem_ThereIsAConflictWithIt() {
        Term[] terms = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 25).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm(),
            
            eventFactory.isFriendOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.isFriendOf(characterMaggie, characterPeter).toLogicalTerm(),
            eventFactory.isEnemyOf(characterArthur, characterMaggie).toLogicalTerm(),
            eventFactory.isEnemyOf(characterMaggie, characterArthur).toLogicalTerm(),
            
            eventFactory.displaces(characterArthur, characterPeter, 22).toLogicalTerm()
        };
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, Trope.getBaseElements());
        assertNumberOfTropes(worldDeductions, Trope.CONFLICT, 1);
    }
    
    @Test
    public void UT_WhenACharacterHelpsLessFriendsThanOther_IsItMoreEvil() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.transfersColor(characterPeter, characterMaggie).toLogicalTerm()
        };
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, Trope.getBaseElements());
        assertNumberOfTropes(worldDeductions, Trope.MORE_EVIL, 2);
    }
    
    @Test
    public void UT_WhenACharacterLosesAConflictWithAMoreEvilCharacterAndHeWinsLater_IsHasLivedAJourney() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.displaces(characterArthur, characterPeter, 45).toLogicalTerm(),
            eventFactory.transfersColor(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.isEnemyOf(characterArthur, characterPeter).toLogicalTerm(),
            eventFactory.isEnemyOf(characterPeter, characterArthur).toLogicalTerm()
        };
        eventFactory.setDay(2);
        Term[] termsDay2 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.hasFear(characterArthur, characterPeter).toLogicalTerm(),
            eventFactory.movesAway(characterArthur, 46).toLogicalTerm(),
            eventFactory.isEnemyOf(characterArthur, characterPeter).toLogicalTerm(),
            eventFactory.isEnemyOf(characterPeter, characterArthur).toLogicalTerm()
        };
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1, termsDay2);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, Trope.values());
        assertNumberOfTropes(worldDeductions, Trope.JOURNEY, 1);
    }
    
    @Test
    public void UT_WhenTheJourneyIsFound_ThereIsAHero () {
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, 
                    new Int(0), new Int(2), 
                    new Int(characterPeter.getId()), new Int(characterArthur.getId()))
        };
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(extraTerms, Trope.getTropesInFromMonomyth());
        assertNumberOfTropes(worldDeductions, Trope.HERO, 1);
    }
    
    @Test
    public void UT_WhenTheJourneyIsFound_ThereIsAShadow () {
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, 
                    new Int(0), new Int(2), 
                    new Int(characterPeter.getId()), new Int(characterArthur.getId()))
        };
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(extraTerms, Trope.getTropesInFromMonomyth());
        assertNumberOfTropes(worldDeductions, Trope.SHADOW, 1);
    }
    
    @Test
    public void UT_WhenSomeoneGivesATurnToTheHeroAlongAJourney_HeIsAnAllied() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.givesTurn(characterMaggie, characterPeter).toLogicalTerm()
        };
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, 
                    new Int(0), new Int(2), 
                    new Int(characterPeter.getId()), new Int(characterArthur.getId()))
        };
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1, extraTerms);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, Trope.getTropesInFromMonomyth());
        assertNumberOfTropes(worldDeductions, Trope.ALLIED, 1);
    }
    
    @Test
    public void UT_WhenSomeoneDisplacesTheHeroAlongAJourney_HeIsAGuardian() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.displaces(characterMaggie, characterPeter, 38).toLogicalTerm(),
            eventFactory.isEnemyOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.isEnemyOf(characterMaggie, characterPeter).toLogicalTerm()
        };
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, 
                    new Int(0), new Int(2), 
                    new Int(characterPeter.getId()), new Int(characterArthur.getId()))
        };
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1, extraTerms);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, Trope.getTropesInFromMonomyth());
        assertNumberOfTropes(worldDeductions, Trope.GUARDIAN, 1);
    }
    
    @Test
    public void UT_WhenSomeoneScaresTheHeroAlongAJourney_HeIsAGuardian() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.hasFear(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.movesAway(characterPeter, 38).toLogicalTerm(),
            eventFactory.isEnemyOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.isEnemyOf(characterMaggie, characterPeter).toLogicalTerm()
        };
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, 
                    new Int(0), new Int(2), 
                    new Int(characterPeter.getId()), new Int(characterArthur.getId()))
        };
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1, extraTerms);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, Trope.getTropesInFromMonomyth());
        assertNumberOfTropes(worldDeductions, Trope.GUARDIAN, 1);
    }
    
    @Test
    public void UT_WhenSomeoneIsAnAlliedButAlsoAGuardian_HeIsAShapeShifter() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.givesTurn(characterMaggie, characterPeter).toLogicalTerm(),
            eventFactory.displaces(characterMaggie, characterPeter, 38).toLogicalTerm(),
            eventFactory.isEnemyOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.isEnemyOf(characterMaggie, characterPeter).toLogicalTerm()
        };
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, 
                    new Int(0), new Int(2), 
                    new Int(characterPeter.getId()), new Int(characterArthur.getId()))
        };
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1, extraTerms);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, Trope.getTropesInFromMonomyth());
        assertNumberOfTropes(worldDeductions, Trope.SHAPESHIFTER, 1);
    }
    
    @Test
    public void UT_TheReasonForTheJourney_IsTheHerald() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            
            eventFactory.isFriendOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.isFriendOf(characterMaggie, characterPeter).toLogicalTerm(),
            eventFactory.isEnemyOf(characterArthur, characterPeter).toLogicalTerm(),
            eventFactory.isEnemyOf(characterPeter, characterArthur).toLogicalTerm(),
            eventFactory.displaces(characterArthur, characterMaggie, 22).toLogicalTerm(),
            
            eventFactory.transfersColor(characterPeter, characterMaggie).toLogicalTerm(),
            
            eventFactory.isEnemyOf(characterArthur, characterPeter).toLogicalTerm(),
            eventFactory.isEnemyOf(characterPeter, characterArthur).toLogicalTerm()
        };
        eventFactory.setDay(2);
        Term[] termsDay2 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.hasFear(characterArthur, characterPeter).toLogicalTerm(),
            eventFactory.movesAway(characterArthur, 46).toLogicalTerm(),
            eventFactory.isEnemyOf(characterArthur, characterPeter).toLogicalTerm(),
            eventFactory.isEnemyOf(characterPeter, characterArthur).toLogicalTerm()
        };
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1, termsDay2);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, new Trope[]{Trope.HERALD});
        assertNumberOfTropes(worldDeductions, Trope.HERALD, 1);
    }
    
    @Test
    public void UT_WhenSomeoneIsAFriendOfTheHeroAndAcoompaniesHimAlongThejourney_HeIsAnAllied() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.isFriendOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(2);
        Term[] termsDay2 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.isFriendOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.isEnemyOf(characterArthur, characterPeter).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(3);
        Term[] termsDay3 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.isFriendOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, 
                    new alice.tuprolog.Float(0), new alice.tuprolog.Float(3), 
                    new Int(characterPeter.getId()), new Int(characterArthur.getId()))
        };
        
        
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1,termsDay2, termsDay3, extraTerms);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, new Trope[]{Trope.ALLIED});
        assertNumberOfTropes(worldDeductions, Trope.ALLIED, 1);
    }
    
    @Test
    public void UT_WhenSomeoneIsAnEnemyOfTheHeroAndAcoompaniesHimAlongThejourney_HeIsNotAnAllied() {
         eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.isFriendOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(2);
        Term[] termsDay2 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.isFriendOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.isEnemyOf(characterArthur, characterPeter).toLogicalTerm(),
            eventFactory.isEnemyOf(characterMaggie, characterPeter).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(3);
        Term[] termsDay3 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.isFriendOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, 
                    new alice.tuprolog.Float(0), new alice.tuprolog.Float(3), 
                    new Int(characterPeter.getId()), new Int(characterArthur.getId()))
        };
        
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1,termsDay2, termsDay3, extraTerms);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, new Trope[]{Trope.ALLIED});
        assertNumberOfTropes(worldDeductions, Trope.ALLIED, 0);
    }
    
    @Test
    public void UT_WhenSomeoneAccompaniesTheHeroAndHasAlwaysBeenHappierThanTheHeroAlongTheJourney_HeIsATrickster() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm(),
            
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.1f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.2f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0f).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.5f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.6f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0f).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(2);
        Term[] termsDay2 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.2f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.3f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0f).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(3);
        Term[] termsDay3 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.1f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.2f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0f).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, 
                    new alice.tuprolog.Float(0), new alice.tuprolog.Float(3), 
                    new Int(characterPeter.getId()), new Int(characterArthur.getId()))
        };
        
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1,termsDay2, termsDay3, extraTerms);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, new Trope[]{Trope.TRICKSTER});
        assertNumberOfTropes(worldDeductions, Trope.TRICKSTER, 1);
    }
    
    @Test
    public void UT_WhenTwoCharactersAccompaniesTheHeroAndHasAlwaysBeenHappierThanTheHeroAlongTheJourney_HeIsATrickster() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm(),
            
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.1f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.2f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0.3f).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.5f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.6f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0.7f).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(2);
        Term[] termsDay2 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.2f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.3f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0.4f).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(3);
        Term[] termsDay3 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.1f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.2f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0.3f).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, 
                    new alice.tuprolog.Float(0), new alice.tuprolog.Float(3), 
                    new Int(characterPeter.getId()), new Int(characterArthur.getId()))
        };
        
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1,termsDay2, termsDay3, extraTerms);
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, new Trope[]{Trope.TRICKSTER});
        assertNumberOfTropes(worldDeductions, Trope.TRICKSTER, 2);
    }
    
    @Test
    public void UT_TrickstersWorksWellWithMultipleCompanionsAlongTheJourney() {
        eventFactory.setDay(0);
        Term[] termsDay0 = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 23).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm(),
                
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.1f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.2f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0.3f).toLogicalTerm()
        };
        eventFactory.setDay(1);
        Term[] termsDay1 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.5f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.6f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0.7f).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(2);
        Term[] termsDay2 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.2f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.3f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0.4f).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(3);
        Term[] termsDay3 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.1f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.2f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0.3f).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(4);
        Term[] termsDay4 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.1f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.2f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0.3f).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(5);
        Term[] termsDay5 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.joy(characterPeter, 0.1f).toLogicalTerm(),
            eventFactory.joy(characterMaggie, 0.2f).toLogicalTerm(),
            eventFactory.joy(characterArthur, 0.3f).toLogicalTerm(),
            eventFactory.areNear(characterPeter, characterArthur, characterMaggie).toLogicalTerm()
        };
        
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, 
                    new alice.tuprolog.Float(0), new alice.tuprolog.Float(5), 
                    new Int(characterPeter.getId()), new Int(characterArthur.getId()))
        };
        
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1,termsDay2, termsDay3, termsDay4, termsDay5, extraTerms);
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, new Trope[]{Trope.TRICKSTER});
        assertNumberOfTropes(worldDeductions, Trope.TRICKSTER, 2);
    }
    
    @Test
    public void UT_WhenThereIsAJourneyWithHeraldMentorAlliedGuardianShapeshifterTrickster_ThereIsAMonomyth() {
        Int dayBegin = new Int(0);
        Int  DayEnd = new Int(5);
        Int  characterHero = new Int(1);
        Int  characterShadow = new Int(2);
        Int  characterHerald = new Int(3);
        Int  characterMentor = new Int(4);
        Int  characterAllied = new Int(5);
        Int  characterGuardian = new Int(6);
        Int  characterTrickster = new Int(7);
        Int  characterShapeShifter = new Int(8);
        
        Term[] extraTerms = new Term[]{
            new Struct(MonomythReasoner.PREDICATE_JOURNEY, dayBegin, DayEnd, characterHero,characterShadow),
            new Struct(MonomythReasoner.PREDICATE_HERALD, dayBegin, DayEnd, characterHero,characterShadow, characterHerald),
            new Struct(MonomythReasoner.PREDICATE_MENTOR, dayBegin, DayEnd, characterHero,characterShadow, characterMentor),
            new Struct(MonomythReasoner.PREDICATE_ALLIED, dayBegin, DayEnd, characterHero,characterShadow, characterAllied),
            new Struct(MonomythReasoner.PREDICATE_GUARDIAN, dayBegin, DayEnd, characterHero,characterShadow, characterGuardian),
            new Struct(MonomythReasoner.PREDICATE_TRICKSTER, dayBegin, DayEnd, characterHero,characterShadow, characterTrickster),
            new Struct(MonomythReasoner.PREDICATE_SHAPESHIFTER, dayBegin, DayEnd, characterHero,characterShadow, characterShapeShifter)
        };
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(extraTerms, new Trope[]{Trope.MONOMYTH});
        assertNumberOfTropes(worldDeductions, Trope.MONOMYTH, 1);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Private methods">
    
    private void solveWithReasoner(String theoryAsString, String predicateToSolve) {
        try {
            Theory theory = new Theory(theoryAsString);
            engine.setTheory(theory);
            SolveInfo solveInfo = engine.solve(predicateToSolve);
            InternalSolve(solveInfo);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    private ICharacter buildPuppetCharacter(int id, Color foreground, Color background, CharacterShape shape) {
        ICharacter character = new PuppetCharacter();
        character.setId(id);
        character.setForegroundColor(foreground);
        character.setBackgroundColor(background);
        character.setShape(shape);
        return character;
    }
    
    private void InternalSolve(SolveInfo solveInfo) throws NoMoreSolutionException, NoSolutionException {
        if (solveInfo.isSuccess()) {
            Term solution = solveInfo.getSolution();
            
            System.out.println("Bindings: " + solveInfo.getSolution());
            solutions.add(solveInfo);
            
            while (engine.hasOpenAlternatives()) {
                solveInfo = engine.solveNext();
                if (solveInfo.isSuccess()) {
                    System.out.println("Bindings: " + solveInfo.getSolution());
                    solutions.add(solveInfo);
                }
            }
        }
    }
    
    private void assertNumberOfTropes(WorldDeductions deductions, Trope trope, int expectedNumber) {
        System.out.println("Deductions for trope "+trope.name()+":");
        if (deductions.get(trope)!=null){
            Arrays.stream(deductions.get(trope)).forEach(deduction -> System.out.println(deduction.toString()));
        }
        
        int numberOfOccurrences = deductions.get(trope)==null? 0: deductions.get(trope).length;
        assertEquals("Should've found "+expectedNumber+" occurrences of the trope "+trope.name(),
                expectedNumber, numberOfOccurrences);
    }
    
    private IColorSpot buildPuppetSpot(int id, Color color) {
        IColorSpot spot = new PuppetColorSpot();
        spot.setId(id);
        spot.setColor(color);
        return spot;
    }
    
    private Term[] addArraysToguether(Term[] ... arrays) {
        ArrayList<Term> allTerms = new ArrayList<>();
        Arrays.stream(arrays).forEach((terms) -> allTerms.addAll(Arrays.asList(terms)));
        return allTerms.toArray(new Term[0]);
    }
    
    //</editor-fold>

}
