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

import alice.tuprolog.Int;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;
import alice.tuprolog.event.ExceptionEvent;
import alice.tuprolog.event.ExceptionListener;
import alice.tuprolog.event.SpyEvent;
import alice.tuprolog.event.SpyListener;
import com.velonuboso.made.core.common.api.IGlobalConfigurationFactory;
import com.velonuboso.made.core.common.implementation.EventFactory;
import com.velonuboso.made.core.common.util.ObjectFactory;
import com.velonuboso.made.core.inference.api.IReasoner;
import com.velonuboso.made.core.inference.entity.Trope;
import com.velonuboso.made.core.inference.entity.WorldDeductions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class MonomythReasoner implements IReasoner {

    public static final String PREDICATE_CHARACTER = "character";
    public static final String PREDICATE_COLOR_SPOT = "colorSpot";
    public static final String PREDICATE_ELEMENT = "element";
    public static final String PREDICATE_REAL_FRIENDS = "realFriends";
    public static final String PREDICATE_REAL_ENEMIES = "realEnemies";
    public static final String PREDICATE_CONFLICT = "conflict";
    public static final String PREDICATE_TRANSFER_COLOR_BEFORE_MAXDAY = "transferColorBeforeMaxday";
    public static final String PREDICATE_ACCOMPANIES_BETWEEN_DAYS = "accompaniesBetweenDays";
    public static final String PREDICATE_HELPED_COUNTER = "helpedCounter";
    public static final String PREDICATE_MORE_EVIL = "moreEvil";
    public static final String PREDICATE_JOURNEY = "journey";
    public static final String PREDICATE_HERO = "hero";
    public static final String PREDICATE_SHADOW = "shadow";
    public static final String PREDICATE_ALLIED = "allied";
    public static final String PREDICATE_BETWEEN = "between";
    public static final String PREDICATE_GUARDIAN = "guardian";
    public static final String PREDICATE_MENTOR = "mentor";
    public static final String PREDICATE_SHAPESHIFTER = "shapeshifter";
    public static final String PREDICATE_ENEMY_BETWEEN = "enemyBetween";
    public static final String PREDICATE_POSSIBLE_HERALD = "possibleHerald";
    public static final String PREDICATE_HERALD = "herald";
    public static final String PREDICATE_IS_ENEMY_OF_CHARACTER = "enemyOfCharacter";
    public static final String PREDICATE_IS_FRIEND_OF_CHARACTER = "friendOfCharacter";
    public static final String PREDICATE_TRICKSTER = "trickster";
    public static final String PREDICATE_MONOMYTH = "monomyth";
    public static final String PREDICATE_NEAR_OF_CHARACTER = "nearOfCharacter";
    public static final String PREDICATE_ACCOMPANY_COUNTER = "accompanieCounter";
    public static final String PREDICATE_ACCOMPANIES = "accompanies";
    public static final String PREDICATE_TRICKIER = "trickier";

    private Prolog engine;
    private List<String> stack;

    @Override
    public WorldDeductions getWorldDeductions(Term[] events) {
        return getWorldDeductionsWithTropesInWhiteList(events, Trope.getTropesInFromMonomyth());
    }

    @Override
    public WorldDeductions getWorldDeductionsWithTropesInWhiteList(Term[] events, Trope[] tropesWhiteList) {

        WorldDeductions deductions = new WorldDeductions();
        try {
            engine = new Prolog();
            initializeExceptionListener();

            //addListenersToEngine();
            engine.setTheory(new Theory(getMonomythRules()));
            //System.out.println("Number of events = "+events.length);

            File temporalFileName = writeEventsToTemporalFile(events);
            FileInputStream fis = new FileInputStream(temporalFileName);

            engine.addTheory(new Theory(fis));

            fis.close();
            temporalFileName.delete();

            Arrays.stream(tropesWhiteList, 0, tropesWhiteList.length)
                    .forEach(trope -> searchTrope(events, trope, deductions));
        } catch (Exception error) {
            Logger.getLogger(MonomythReasoner.class.getName()).log(Level.SEVERE, null, error.getMessage());
        }
        return deductions;
    }

    private void initializeExceptionListener() {
        engine.addExceptionListener(new ExceptionListener() {
            @Override
            public void onException(ExceptionEvent event) {
                System.out.println("Exception found: "+event.getMsg());
            }
        });
    }

    private File writeEventsToTemporalFile(Term[] events) throws FileNotFoundException, IOException {
        File temporalFileName = File.createTempFile("madetemp_", ".tmp");
        PrintWriter writer = new PrintWriter(temporalFileName);
        for (Term term : events) {
            writer.println(term.toString() + ".");
        }
        writer.close();
        return temporalFileName;
    }

    // <editor-fold defaultstate="collapsed" desc="Private methods">
    private void searchTrope(Term[] events, Trope trope, WorldDeductions deductions) {
        Term predicateToSolve = getpredicateToSolve(trope);
        ArrayList<Term> solutions = new ArrayList<>();

        try {
            solveTrope(events, predicateToSolve, solutions);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        deductions.put(trope, solutions.toArray(new Term[0]));
    }

    private void solveTrope(Term[] events, Term predicateToSolve, ArrayList<Term> solutions){
        stack = new ArrayList();

        TimerTask engineHalter = buildEngineHalter();
        Timer timer = buildTimerHalter(engineHalter);
        
        SolveInfo solveInfo = engine.solve(predicateToSolve);

        ArrayList<Term> temporalSolutions = new ArrayList<>();

        try{
            if (solveInfo.isSuccess()) {
                //System.out.println("Bindings: " + solveInfo.getSolution());
                temporalSolutions.add(solveInfo.getSolution());

                while (engine.hasOpenAlternatives()) {
                    solveInfo = engine.solveNext();
                    if (solveInfo.isSuccess()) {
                        //System.out.println("Bindings: " + solveInfo.getSolution());
                        temporalSolutions.add(solveInfo.getSolution());
                    }
                }
            }
        }catch(Exception exception){
            System.out.println("Exception thrown when solving: "+exception.getMessage());
        }
        
        timer.cancel();
        engineHalter.cancel();
        
        temporalSolutions.stream()
                .map(TermWrapper::new)
                .distinct()
                .map(TermWrapper::unwrap)
                .forEachOrdered(element -> solutions.add(element));

    }

    private Timer buildTimerHalter(TimerTask engineHalter) {
        Timer timer = new Timer();
        long maximumTimeToSolve = getMaximumTimeToSolve();
        timer.schedule(engineHalter, maximumTimeToSolve);
        return timer;
    }

    private long getMaximumTimeToSolve() {
        IGlobalConfigurationFactory globalConfigurationFactory
                = ObjectFactory.createObject(IGlobalConfigurationFactory.class);
        long maximumTimeToSolve = 1000*(long)globalConfigurationFactory.getCommonEcConfiguration().MAXIMUM_SECONDS_TO_GET_ALL_OCCURRENCES;
        return maximumTimeToSolve;
    }

    private TimerTask buildEngineHalter() {
        return new TimerTask() {
            @Override
            public void run() {
                
                
                System.out.println("Halting ... ");
                engine.solveHalt();
            }
        };
    }

    private Term getpredicateToSolve(Trope trope) {
        switch (trope) {
            case FRIEND_OF_CHARACTER:
                return new Struct(PREDICATE_IS_FRIEND_OF_CHARACTER, new Var("Day"), new Var("A"), new Var("B"));
            case ENEMY_OF_CHARACTER:
                return new Struct(PREDICATE_IS_ENEMY_OF_CHARACTER, new Var("Day"), new Var("A"), new Var("B"));
            case REAL_FRIENDS:
                return new Struct(PREDICATE_REAL_FRIENDS, new Var("Day"), new Var("A"), new Var("B"));
            case REAL_ENEMIES:
                return new Struct(PREDICATE_REAL_ENEMIES, new Var("Day"), new Var("A"), new Var("B"));
            case ELEMENT:
                return new Struct(PREDICATE_ELEMENT, new Var());
            case CONFLICT:
                return new Struct(PREDICATE_CONFLICT, new Var("Day"), new Var("Winer"), new Var("Loser"));
            case MORE_EVIL:
                return new Struct(PREDICATE_MORE_EVIL, new Var("Day"), new Var("Evil"), new Var("Good"));
            case JOURNEY:
                return new Struct(PREDICATE_JOURNEY, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"));
            case HERO:
                return new Struct(PREDICATE_HERO, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"));
            case SHADOW:
                return new Struct(PREDICATE_SHADOW, new Var("DayBegin"), new Var("DayEnd"), new Var("Shadow"));
            case ALLIED:
                return new Struct(PREDICATE_ALLIED, new Var("DayBegin"), new Var("DayEnd"), new Var("Allied"));
            case GUARDIAN:
                return new Struct(PREDICATE_GUARDIAN, new Var("DayBegin"), new Var("DayEnd"), new Var("Guardian"));
            case MENTOR:
                return new Struct(PREDICATE_MENTOR, new Var("DayBegin"), new Var("DayEnd"), new Var("Mentor"));
            case SHAPESHIFTER:
                return new Struct(PREDICATE_SHAPESHIFTER, new Var("DayBegin"), new Var("DayEnd"), new Var("Shapeshifter"));
            case HERALD:
                return new Struct(PREDICATE_HERALD, new Var("DayBegin"), new Var("DayEnd"), new Var("Herald"));
            case TRICKSTER:
                return new Struct(PREDICATE_TRICKSTER, new Var("DayBegin"), new Var("DayEnd"), new Var("Trickster"));
            case MONOMYTH:
                return new Struct(PREDICATE_MONOMYTH,
                        new Term[]{
                            new Var("DayBengin"), new Var("DayEnd"),
                            new Var("Hero"), new Var("Shadow") /*, new Var("Herald"), new Var("Mentor"),
                         new Var("Allied"), new Var("Guardian"), new Var("Trickster"), new Var("Shapeshifter")*/}
                );
            default:
                return new Struct();
        }
    }

    public String getMonomythRules() {

        TermRule rules[] = new TermRule[]{
            new TermRule(
            new Struct(PREDICATE_BETWEEN, new Var("Start"), new Var("NumberBetween"), new Var("End")),
            new Struct(">=", new Var("NumberBetween"), new Var("Start")),
            new Struct(">=", new Var("End"), new Var("NumberBetween"))
            ),
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
            new Struct(PREDICATE_IS_FRIEND_OF_CHARACTER, new Var("Day"), new Var("A"), new Var("B")),
            new Struct(EventFactory.IS_FRIEND_OF, new Var("Day"), new Var("A"), new Var("ListOfFriends")),
            new Struct("member", new Var("B"), new Var("ListOfFriends"))
            ),
            new TermRule(
            new Struct(PREDICATE_NEAR_OF_CHARACTER, new Var("Day"), new Var("B"), new Var("A")),
            new Struct(EventFactory.ARE_NEAR, new Var("Day"), new Var("A"), new Var("ListOfCharacters")),
            new Struct("member", new Var("B"), new Var("ListOfCharacters"))
            ),
            new TermRule(
            new Struct(PREDICATE_REAL_FRIENDS, new Var("Day"), new Var("A"), new Var("B")),
            new Struct(PREDICATE_IS_FRIEND_OF_CHARACTER, new Var("Day"), new Var("A"), new Var("B")),
            new Struct(PREDICATE_IS_FRIEND_OF_CHARACTER, new Var("Day"), new Var("B"), new Var("A"))
            ),
            new TermRule(
            new Struct(PREDICATE_IS_ENEMY_OF_CHARACTER, new Var("Day"), new Var("A"), new Var("B")),
            new Struct(EventFactory.IS_ENEMY_OF, new Var("Day"), new Var("A"), new Var("ListOfEnemies")),
            new Struct("member", new Var("B"), new Var("ListOfEnemies"))
            ),
            new TermRule(
            new Struct(PREDICATE_REAL_ENEMIES, new Var("Day"), new Var("A"), new Var("B")),
            new Struct(PREDICATE_IS_ENEMY_OF_CHARACTER, new Var("Day"), new Var("A"), new Var("B")),
            new Struct(PREDICATE_IS_ENEMY_OF_CHARACTER, new Var("Day"), new Var("B"), new Var("A"))
            ),
            new TermRule(
            new Struct(PREDICATE_CONFLICT, new Var("Day"), new Var("Winner"), new Var("Loser")),
            new Struct(EventFactory.HAS_FEAR, new Var("Day"), new Var("Loser"), new Var("Winner")),
            new Struct(EventFactory.MOVES_AWAY, new Var("Day"), new Var("Loser"), new Var()),
            new Struct(PREDICATE_REAL_ENEMIES, new Var("Day"), new Var("Winner"), new Var("Loser"))
            ),
            new TermRule(
            new Struct(PREDICATE_CONFLICT, new Var("Day"), new Var("Winner"), new Var("Loser")),
            new Struct(EventFactory.DISPLACES, new Var("Day"), new Var("Winner"), new Var("Loser"), new Var()),
            new Struct(PREDICATE_REAL_ENEMIES, new Var("Day"), new Var("Winner"), new Var("Loser"))
            ),
            new TermRule(
            new Struct(PREDICATE_CONFLICT, new Var("Day"), new Var("Winner"), new Var("Loser")),
            new Struct(PREDICATE_POSSIBLE_HERALD, new Var("Day"), new Var("Winner"), new Var("Loser"), new Var("Herald"))
            ),
            new TermRule(
            new Struct(PREDICATE_POSSIBLE_HERALD, new Var("DayWinnerStains"), new Var("Winner"), new Var("Loser"), new Var("Spot")),
            new Struct(EventFactory.CAN_IMPROVE_SELF_SIMILARITY, new Var("DayLoserWantedSpot"), new Var("Loser"), new Var("Spot")),
            new Struct(EventFactory.STAINS, new Var("DayWinnerStains"), new Var("Winner"), new Var("Spot")),
            new Struct("not", new Struct(EventFactory.STAINS, new Var(), new Var("Loser"), new Var("Spot"))),
            new Struct(">=", new Var("DayWinnerStains"), new Var("DayLoserWantedSpot")),
            new Struct(PREDICATE_REAL_ENEMIES, new Var("DayWinnerStains"), new Var("Winner"), new Var("Loser"))
            ),
            new TermRule(
            new Struct(PREDICATE_POSSIBLE_HERALD, new Var("Day"), new Var("Winner"), new Var("Loser"), new Var("Friend")),
            new Struct(EventFactory.DISPLACES, new Var("Day"), new Var("Winner"), new Var("Friend"), new Var()),
            new Struct(PREDICATE_REAL_FRIENDS, new Var("Day"), new Var("Loser"), new Var("Friend")),
            new Struct(PREDICATE_REAL_ENEMIES, new Var("Day"), new Var("Winner"), new Var("Loser")),
            new Struct("!")
            ),
            new TermRule(
            new Struct(PREDICATE_TRANSFER_COLOR_BEFORE_MAXDAY, new Var("MaxDay"), new Var("TransferDay"), new Var("Subject")),
            new Struct(EventFactory.NEW_DAY, new Var("MaxDay")),
            new Struct(EventFactory.NEW_DAY, new Var("TransferDay")),
            new Struct(PREDICATE_CHARACTER, new Var("Subject")),
            new Struct(EventFactory.TRANSFERS_COLOR, new Var("TransferDay"), new Var("Subject"), new Var()),
            new Struct(">=", new Var("MaxDay"), new Var("TransferDay"))
            ),
            new TermRule(
            new Struct(PREDICATE_ACCOMPANIES_BETWEEN_DAYS, new Var("DayBegin"), new Var("DayEnd"), new Var("AccompaniesDay"), new Var("Subject"), new Var("Target")),
            new Struct(EventFactory.NEW_DAY, new Var("DayBegin")),
            new Struct(EventFactory.NEW_DAY, new Var("DayEnd")),
            new Struct(EventFactory.NEW_DAY, new Var("AccompaniesDay")),
            new Struct(PREDICATE_CHARACTER, new Var("Subject")),
            new Struct(PREDICATE_CHARACTER, new Var("Target")),
            new Struct(PREDICATE_NEAR_OF_CHARACTER, new Var("AccompaniesDay"), new Var("Subject"), new Var("Target")),
            new Struct(">=", new Var("DayEnd"), new Var("AccompaniesDay")),
            new Struct(">=", new Var("AccompaniesDay"), new Var("DayBegin"))
            ),
            new TermRule(
            new Struct(PREDICATE_HELPED_COUNTER, new Var("Day"), new Var("Subject"), new Var("Count")),
            new Struct(EventFactory.NEW_DAY, new Var("Day")),
            new Struct(PREDICATE_CHARACTER, new Var("Subject")),
            new Struct("findall",
            new Var("TransferDay"),
            new Struct(PREDICATE_TRANSFER_COLOR_BEFORE_MAXDAY, new Var("Day"), new Var("TransferDay"), new Var("Subject")),
            new Var("List")
            ),
            new Struct("length", new Var("List"), new Var("Count"))
            ),
            new TermRule(
            new Struct(PREDICATE_MORE_EVIL, new Var("Day"), new Var("Evil"), new Var("Good")),
            new Struct(PREDICATE_HELPED_COUNTER, new Var("Day"), new Var("Evil"), new Var("EvilCounter")),
            new Struct(PREDICATE_HELPED_COUNTER, new Var("Day"), new Var("Good"), new Var("GoodCounter")),
            new Struct("<", new Var("EvilCounter"), new Var("GoodCounter"))
            ),
            new TermRule(
            new Struct(PREDICATE_JOURNEY, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow")),
            new Struct(PREDICATE_CONFLICT, new Var("DayBegin"), new Var("Shadow"), new Var("Hero")),
            new Struct(PREDICATE_CONFLICT, new Var("DayEnd"), new Var("Hero"), new Var("Shadow")),
            new Struct(PREDICATE_MORE_EVIL, new Var("DayBegin"), new Var("Shadow"), new Var("Hero")),
            new Struct(">", new Var("DayEnd"), new Var("DayBegin"))
            ),
            new TermRule(
            new Struct(PREDICATE_HERO, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero")),
            new Struct(PREDICATE_JOURNEY, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"))
            ),
            new TermRule(
            new Struct(PREDICATE_SHADOW, new Var("DayBegin"), new Var("DayEnd"), new Var("Shadow")),
            new Struct(PREDICATE_JOURNEY, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"))
            ),
            new TermRule(
            new Struct(PREDICATE_ALLIED, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"),
            new Var("Allied")),
            new Struct(PREDICATE_JOURNEY, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow")),
            new Struct(EventFactory.GIVES_TURN, new Var("DayHappened"), new Var("Allied"), new Var("Hero")),
            new Struct(PREDICATE_BETWEEN, new Var("DayBegin"), new Var("DayHappened"), new Var("DayEnd"))
            ),
            new TermRule(
            new Struct(PREDICATE_ACCOMPANY_COUNTER, new Var("DayBegin"), new Var("DayEnd"), new Var("Subject"), new Var("Companion"), new Var("Count")),
            new Struct(EventFactory.NEW_DAY, new Var("DayBegin")),
            new Struct(EventFactory.NEW_DAY, new Var("DayEnd")),
            new Struct(PREDICATE_CHARACTER, new Var("Subject")),
            new Struct(PREDICATE_CHARACTER, new Var("Companion")),
            new Struct("findall",
            new Var("AccompaniesDay"),
            new Struct(PREDICATE_ACCOMPANIES_BETWEEN_DAYS, new Var("DayBegin"), new Var("DayEnd"), new Var("AccompaniesDay"), new Var("Companion"), new Var("Subject")),
            new Var("List")
            ),
            new Struct("\\==", new Var("Subject"), new Var("Companion")),
            new Struct("length", new Var("List"), new Var("Count"))
            ),
            
            new TermRule(
            new Struct(PREDICATE_ACCOMPANIES, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Allied")),
            new Struct(EventFactory.NEW_DAY, new Var("DayBegin")),
            new Struct(EventFactory.NEW_DAY, new Var("DayEnd")),
            new Struct(PREDICATE_CHARACTER, new Var("Hero")),
            new Struct(PREDICATE_CHARACTER, new Var("Allied")),
            new Struct(PREDICATE_ACCOMPANY_COUNTER, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Allied"), new Var("Count")),
            //new Struct("is", new Var("MinimumCompanion"), new Struct("ceiling", new Struct ("div", new Struct("-", new Var("DayEnd"), new Var("DayBegin")), new Int(3)))),
            new Struct(">", new Var("Count"), new Struct("ceiling", new Struct ("div", new Struct("-", new Var("DayEnd"), new Var("DayBegin")), new Int(3))))//new Var("MinimumCompanion"))
            ),
            
            new TermRule(
            new Struct(PREDICATE_ALLIED, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"), new Var("Allied")),
            new Struct(PREDICATE_JOURNEY, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow")),
            new Struct(PREDICATE_ACCOMPANIES, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Allied")),
            new Struct("not", new Struct(PREDICATE_ENEMY_BETWEEN, new Var("DayBegin"), new Var("DayEnd"), new Var("Allied"), new Var("Hero")))
            ),
            new TermRule(
            new Struct(PREDICATE_ALLIED, new Var("DayBegin"), new Var("DayEnd"), new Var("Allied")),
            new Struct(PREDICATE_ALLIED, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"),
            new Var("Allied"))
            ),
            new TermRule(
            new Struct(PREDICATE_GUARDIAN, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"),
            new Var("Guardian")),
            new Struct(PREDICATE_JOURNEY, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow")),
            new Struct(PREDICATE_CONFLICT, new Var("DayHappened"), new Var("Guardian"), new Var("Hero")),
            new Struct(PREDICATE_BETWEEN, new Var("DayBegin"), new Var("DayHappened"), new Var("DayEnd"))
            ),
            new TermRule(
            new Struct(PREDICATE_GUARDIAN, new Var("DayBegin"), new Var("DayEnd"), new Var("Guardian")),
            new Struct(PREDICATE_GUARDIAN, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"),
            new Var("Guardian"))
            ),
            new TermRule(
            new Struct(PREDICATE_ENEMY_BETWEEN, new Var("DayBegin"), new Var("DayEnd"), new Var("Subject"), new Var("Enemy")),
            new Struct(PREDICATE_IS_ENEMY_OF_CHARACTER, new Var("Day"), new Var("Subject"), new Var("Enemy")),
            new Struct(PREDICATE_BETWEEN, new Var("DayBegin"), new Var("Day"), new Var("DayEnd"))
            ),
            new TermRule(
            new Struct(PREDICATE_MENTOR, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"),
            new Var("Mentor")),
            new Struct(PREDICATE_JOURNEY, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow")),
            new Struct(EventFactory.TRANSFERS_COLOR, new Var("DayHappened"), new Var("Mentor"), new Var("Hero")),
            new Struct("not",
            new Struct(PREDICATE_ENEMY_BETWEEN, new Var("DayBegin"), new Var("DayEnd"), new Var("Mentor"), new Var("Hero"))
            ),
            new Struct(PREDICATE_BETWEEN, new Var("DayBegin"), new Var("DayHappened"), new Var("DayEnd"))
            ),
            new TermRule(
            new Struct(PREDICATE_MENTOR, new Var("DayBegin"), new Var("DayEnd"), new Var("Mentor")),
            new Struct(PREDICATE_MENTOR, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"),
            new Var("Mentor"))
            ),
            new TermRule(
            new Struct(PREDICATE_SHAPESHIFTER, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"),
            new Var("Shapeshifter")),
            new Struct(PREDICATE_GUARDIAN, new Var("DayBegin"), new Var("DayEnd"), new Var("Shapeshifter")),
            new Struct(PREDICATE_ALLIED, new Var("DayBegin"), new Var("DayEnd"), new Var("Shapeshifter"))
            ),
            new TermRule(
            new Struct(PREDICATE_SHAPESHIFTER, new Var("DayBegin"), new Var("DayEnd"), new Var("Shapeshifter")),
            new Struct(PREDICATE_SHAPESHIFTER, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"),
            new Var("Shapeshifter"))
            ),
            new TermRule(
            new Struct(PREDICATE_HERALD, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"),
            new Var("Herald")),
            new Struct(PREDICATE_JOURNEY, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow")),
            new Struct(PREDICATE_POSSIBLE_HERALD, new Var("DayBegin"), new Var("Shadow"), new Var("Hero"), new Var("Herald"))
            ),
            new TermRule(
            new Struct(PREDICATE_HERALD, new Var("DayBegin"), new Var("DayEnd"), new Var("Herald")),
            new Struct(PREDICATE_HERALD, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"),
            new Var("Herald"))
            ),
            new TermRule(
            new Struct(PREDICATE_TRICKSTER, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"), new Var("Trickster")),
            
            new Struct(PREDICATE_JOURNEY, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("_")),
            new Struct(PREDICATE_ACCOMPANIES, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Trickster")),
            new Struct(PREDICATE_TRICKIER, new Var("DayBegin"), new Var("DayEnd"), new Var("Trickster"), new Var("Hero"))
            ),
            new TermRule(
            new Struct(PREDICATE_TRICKSTER, new Var("DayBegin"), new Var("DayEnd"), new Var("Trickster")),
            new Struct(PREDICATE_TRICKSTER, new Var("DayBegin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"), new Var("Trickster"))
            ),
            new TermRule(
            new Struct(PREDICATE_TRICKIER, new Var("DayBegin"), new Var("DayEnd"), new Var("Subject"), new Var("Target")),
            new Struct(EventFactory.JOY, new Var("OneDay"), new Var("Target"), new Var("JoyTarget")),
            new Struct(EventFactory.JOY, new Var("OneDay"), new Var("Subject"), new Var("JoySubject")),
            new Struct(PREDICATE_BETWEEN, new Var("DayBegin"), new Var("OneDay"), new Var("DayEnd")),
            new Struct("\\+", new Struct(">=", new Var("JoyTarget"), new Var("JoySubject"))),
            new Struct("!")
            ),
            new TermRule(
            new Struct(PREDICATE_MONOMYTH,
            new Term[]{
                new Var("DayBengin"), new Var("DayEnd"),
                new Var("Hero"), new Var("Shadow"), new Var("Herald"), new Var("Mentor"),
                new Var("Allied"), new Var("Guardian"), new Var("Trickster"), new Var("Shapeshifter")
            }
            ),
            new Struct(MonomythReasoner.PREDICATE_JOURNEY,
            new Var("DayBengin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow")),
            new Struct(MonomythReasoner.PREDICATE_HERALD,
            new Var("DayBengin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"), new Var("Herald")),
            new Struct(MonomythReasoner.PREDICATE_MENTOR,
            new Var("DayBengin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"), new Var("Mentor")),
            new Struct(MonomythReasoner.PREDICATE_ALLIED,
            new Var("DayBengin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"), new Var("Allied")),
            new Struct(MonomythReasoner.PREDICATE_GUARDIAN,
            new Var("DayBengin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"), new Var("Guardian")),
            new Struct(MonomythReasoner.PREDICATE_TRICKSTER,
            new Var("DayBengin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"), new Var("Trickster")),
            new Struct(MonomythReasoner.PREDICATE_SHAPESHIFTER,
            new Var("DayBengin"), new Var("DayEnd"), new Var("Hero"), new Var("Shadow"), new Var("Shapeshifter"))
            ),
            new TermRule(
            new Struct(PREDICATE_MONOMYTH,
            new Var("DayBengin"), new Var("DayEnd"),
            new Var("Hero"), new Var("Shadow")
            ),
            new Struct(PREDICATE_MONOMYTH,
            new Term[]{
                new Var("DayBengin"), new Var("DayEnd"),
                new Var("Hero"), new Var("Shadow"), new Var("Herald"), new Var("Mentor"),
                new Var("Allied"), new Var("Guardian"), new Var("Trickster"), new Var("Shapeshifter")
            }
            )
            )
        };

        String[] rulesAsStringArray = Arrays
                .stream(rules)
                .map(rule -> rule.toString() + ".")
                .toArray(String[]::new);
        return String.join("\n", rulesAsStringArray);
    }

    private void addListenersToEngine() {
        engine.setSpy(true);
        engine.addSpyListener(new SpyListener() {
            @Override
            public void onSpy(SpyEvent e) {
                if (stack != null) {
                    Pattern patt = Pattern.compile("^spy:\\s+[0-9]+\\s+Eval\\s+([^_]*)$");
                    Matcher matcher = patt.matcher(e.getMsg());
                    if (matcher.find()) {
                        stack.add(matcher.group(1));
                        //spy: 2  Back  journey(1,DayEnd_e3,20,21)
                    }
                }
            }
        });
    }
    //</editor-fold>

}
