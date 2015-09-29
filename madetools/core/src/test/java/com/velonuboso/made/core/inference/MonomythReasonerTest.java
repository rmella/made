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
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.UnknownVarException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ruben
 */
public class MonomythReasonerTest {

    Prolog engine;
    ArrayList<SolveInfo> solutions;

    public MonomythReasonerTest() {
    }

    @Before
    public void setUp() {
        engine = new Prolog();
        solutions = new ArrayList<>();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void UT_Tuprolog_works() throws IOException, InvalidTheoryException, MalformedGoalException, NoSolutionException, UnknownVarException, NoMoreSolutionException {

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

    private void solveWithReasoner(String theoryAsString, String predicateToSolve) {
        try {
            Theory theory = new Theory(theoryAsString);
            engine.setTheory(theory);

            SolveInfo solveInfo = engine.solve(predicateToSolve);

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
        } catch (InvalidTheoryException | MalformedGoalException | NoSolutionException | NoMoreSolutionException ex) {
            fail(ex.getMessage());
        }
    }
}
