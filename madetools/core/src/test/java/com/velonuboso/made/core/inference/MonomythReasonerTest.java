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
import com.velonuboso.made.core.abm.api.ICharacter;
import com.velonuboso.made.core.abm.api.IColorSpot;
import com.velonuboso.made.core.abm.entity.CharacterShape;
import com.velonuboso.made.core.common.api.IEventFactory;
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
import javafx.scene.paint.Color;
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
    private ICharacter character;
    private IColorSpot spot;
    private IReasoner reasoner;
    IEventFactory eventFactory;
    
    public MonomythReasonerTest() {
    }

    @Before
    public void setUp() {
        engine = new Prolog();
        solutions = new ArrayList<>();
        initializeCharacter();
        initializeSpot();
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
            eventFactory.characterAppears(character, 20).toLogicalTerm(),
            eventFactory.colorSpotAppears(spot, 30).toLogicalTerm()
        };
        
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, new Trope[]{Trope.CONFLICT});
        assertNumberOfTropes(worldDeductions, Trope.CONFLICT, 0);
    }

    @Test
    public void UT_ElementFound() {
        Term[] terms = new Term[]{
            eventFactory.characterAppears(character, 20).toLogicalTerm(),
            eventFactory.colorSpotAppears(spot, 30).toLogicalTerm()
        };
        WorldDeductions worldDeductions = reasoner.getWorldDeductionsWithTropesInWhiteList(terms, Trope.getBaseElements());
        assertNumberOfTropes(worldDeductions, Trope.ELEMENT, 2);
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

    private void initializeCharacter() {
        character = ObjectFactory.createObject(ICharacter.class);
        character.setId(0);
        character.setForegroundColor(Color.BLACK);
        character.setBackgroundColor(Color.AQUA);
        character.setShape(CharacterShape.CIRCLE);
    }

    private void initializeSpot() {
        spot = ObjectFactory.createObject(IColorSpot.class);
        spot.setId(100);
        spot.setColor(Color.AZURE);
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
        int numberOfOccurrences = deductions.get(trope)==null? 0: deductions.get(trope).length;
        assertEquals("Should've found "+expectedNumber+" occurrences of the trope "+trope.name(),
                expectedNumber, numberOfOccurrences);
    }
    //</editor-fold>
}
