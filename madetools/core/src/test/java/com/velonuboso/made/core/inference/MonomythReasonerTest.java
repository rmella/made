/*
 * Copyright (C) 2015 Ruben
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
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.inference.api.IReasoner;
import com.velonuboso.made.core.inference.entity.Trope;
import com.velonuboso.made.core.inference.entity.WorldDeductions;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author Ruben
 */
public class MonomythReasonerTest {

    private Prolog engine;
    private ArrayList<SolveInfo> solutions;
    private ICharacter characterPeter;
    private ICharacter characterArthur;
    private ICharacter characterMaggie;
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
    public void UT_ConflictNotFound_whenThereIsNoConflictBetweenElements() {
        Term[] terms = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.colorSpotAppears(spotEmerald, 30).toLogicalTerm()
        };
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, new Trope[]{Trope.CONFLICT});
        assertNumberOfTropes(worldDeductions, Trope.CONFLICT, 0);
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
        };
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, Trope.getBaseElements());
        assertNumberOfTropes(worldDeductions, Trope.CONFLICT, 1);
    }
    
    @Test
    public void UT_WhenACharacterDisplacesOther_ConflictAppears() {
        Term[] terms = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm(),
            eventFactory.displaces(characterArthur, characterPeter, 22).toLogicalTerm()
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
        };
        
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1, termsDay2);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, Trope.getBaseElements());
        assertNumberOfTropes(worldDeductions, Trope.CONFLICT, 1);
    }
    
    @Test
    public void UT_WhenTwoCharactersAreFriendsAndOtherPieceDisplacesOne_BothHaveAConflictWithIt() {
        Term[] terms = new Term[]{
            eventFactory.characterAppears(characterPeter, 20).toLogicalTerm(),
            eventFactory.characterAppears(characterMaggie, 25).toLogicalTerm(),
            eventFactory.isFriendOf(characterPeter, characterMaggie).toLogicalTerm(),
            eventFactory.isFriendOf(characterMaggie, characterPeter).toLogicalTerm(),
            eventFactory.characterAppears(characterArthur, 21).toLogicalTerm(),
            eventFactory.displaces(characterArthur, characterPeter, 22).toLogicalTerm()
        };
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, Trope.getBaseElements());
        assertNumberOfTropes(worldDeductions, Trope.CONFLICT, 2);
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
            eventFactory.transfersColor(characterPeter, characterMaggie).toLogicalTerm()
        };
        eventFactory.setDay(2);
        Term[] termsDay2 = new Term[]{
            eventFactory.newDay().toLogicalTerm(),
            eventFactory.hasFear(characterArthur, characterPeter).toLogicalTerm(),
            eventFactory.movesAway(characterArthur, 46).toLogicalTerm()
        };
        Term allTerms[] = addArraysToguether(termsDay0, termsDay1, termsDay2);
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(allTerms, Trope.getTropesInFromMonomyth());
        assertNumberOfTropes(worldDeductions, Trope.JOURNEY, 1);
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
