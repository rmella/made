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
package com.velonuboso.made.core.inference.implementation;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;
import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.inference.api.IReasoner;
import com.velonuboso.made.core.inference.entity.Trope;
import com.velonuboso.made.core.inference.entity.WorldDeductions;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class MonomythReasoner implements IReasoner {

    private Prolog engine;

    private static final String PREDICATE_CHARACTER = "character";
    private static final String PREDICATE_COLOR_SPOT = "colorSpot";
    private static final String PREDICATE_ELEMENT = "element";
    private static final String PREDICATE_CONFLICT = "conflict";
    
    @Override
    public WorldDeductions getWorldDeductions(Term[] events) {
        return getWorldDeductionsWithTropesInWhiteList(events, Trope.values());
    }

    @Override
    public WorldDeductions getWorldDeductionsWithTropesInWhiteList(Term[] events, Trope[] tropesWhiteList) {
        WorldDeductions deductions = new WorldDeductions();
        Arrays.stream(tropesWhiteList, 0, tropesWhiteList.length)
                .forEach(trope -> searchTrope(events, trope, deductions));
        return deductions;
    }

    // <editor-fold defaultstate="collapsed" desc="Private methods">
    private void searchTrope(Term[] events, Trope trope, WorldDeductions deductions) {

        engine = new Prolog();
        Term predicateToSolve = getpredicateToSolve(trope);
        ArrayList<Term> solutions = new ArrayList<>();

        try {
            createTheoryAndSolveTrope(events, predicateToSolve, solutions);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        deductions.put(trope, solutions.toArray(new Term[0]));
    }

    private void createTheoryAndSolveTrope(Term[] events, Term predicateToSolve, ArrayList<Term> solutions)
            throws InvalidTheoryException, NoSolutionException, NoMoreSolutionException {

        engine.setTheory(new Theory(getMonomythRules()));
        engine.addTheory(new Theory(new Struct(events)));

        SolveInfo solveInfo = engine.solve(predicateToSolve);

        if (solveInfo.isSuccess()) {
            System.out.println("Bindings: " + solveInfo.getSolution());
            solutions.add(solveInfo.getSolution());

            while (engine.hasOpenAlternatives()) {
                solveInfo = engine.solveNext();
                if (solveInfo.isSuccess()) {
                    System.out.println("Bindings: " + solveInfo.getSolution());
                    solutions.add(solveInfo.getSolution());
                }
            }
        }
    }

    private Term getpredicateToSolve(Trope trope) {
        switch (trope) {
            case ELEMENT:
                return new Struct(PREDICATE_ELEMENT, new Var());
            case CONFLICT:
                return new Struct(PREDICATE_CONFLICT,  new Var(), new Var(), new Var());
            default:
                return new Struct();
        }
    }

    private String getMonomythRules() {
        
        TermRule rules[] = new TermRule[]{
            new TermRule(
                    new Struct(PREDICATE_CHARACTER, new Var("X")),
                    new Struct(EventFactory.CHARACTER_APPEARS, new Var(), new Var("X"), new Var(), new Var(), new Var())
            ),
            new TermRule(
                    new Struct(PREDICATE_COLOR_SPOT, new Var("X")),
                    new Struct(EventFactory.COLOR_SPOT_APPEARS, new Var(), new Var("X"), new Var())
            ),
            new TermRule(
                    new Struct(PREDICATE_ELEMENT, new Var("X")),
                    new Struct(PREDICATE_CHARACTER, new Var("X"))
            ),
            new TermRule(
                    new Struct(PREDICATE_ELEMENT, new Var("X")),
                    new Struct(PREDICATE_COLOR_SPOT, new Var("X"))
            ),
            new TermRule(
                    new Struct(PREDICATE_CONFLICT, new Var("Day"), new Var("Winner"), new Var("Loser")),
                    new Struct(EventFactory.HAS_FEAR, new Var("Day"), new Var("Loser"), new Var("Winner")),
                    new Struct(EventFactory.MOVES_AWAY,  new Var("Day"),  new Var("Loser"),  new Var())
            ),
            new TermRule(
                    new Struct(PREDICATE_CONFLICT, new Var("Day"), new Var("Winner"), new Var("Loser")),
                    new Struct(EventFactory.DISPLACES, new Var("Day"), new Var("Winner"), new Var("Loser"), new Var())
            ),
            new TermRule(
                    new Struct(PREDICATE_CONFLICT, new Var("Day"), new Var("Winner"), new Var("Loser")),
                    new Struct(EventFactory.CAN_IMPROVE_SELF_SIMILARITY, new Var("DayLoserWantedSpot"), new Var("Loser"), new Var("Spot")),
                    new Struct(EventFactory.STAINS, new Var("DayWinnerStains"), new Var("Winner"), new Var("Spot")),
                    new Struct("not", new Struct(EventFactory.STAINS, new Var(), new Var("Loser"), new Var("Spot"))),
                    new Struct(">=", new Var("DayWinnerStains"), new Var("DayLoserWantedSpot"))
            )
        };
        
        String[] rulesAsStringArray = Arrays
                .stream(rules)
                .map(rule -> rule.toString()+".")
                .toArray(String[]::new);
        return String.join("\n", rulesAsStringArray);
    }
    //</editor-fold>
}

