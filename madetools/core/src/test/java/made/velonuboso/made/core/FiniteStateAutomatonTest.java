/*
 * Copyright (C) 2015 rhgarcia
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
package made.velonuboso.made.core;

import com.velonuboso.made.interfaces.ICondition;
import com.velonuboso.made.core.FiniteStateAutomaton;
import com.velonuboso.made.core.RandomNumberFactory;
import com.velonuboso.made.interfaces.IAction;
import com.velonuboso.made.interfaces.ICharacter;
import com.velonuboso.made.interfaces.IFiniteStateAutomaton;
import com.velonuboso.made.interfaces.IWorld;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author rhgarcia
 */
public class FiniteStateAutomatonTest {

    public FiniteStateAutomatonTest() {
    }

    @Test
    public void FiniteStateAutomaton_is_not_correct_when_no_states_are_provided() {
        IFiniteStateAutomaton automaton = new FiniteStateAutomaton();
        assertFalse("shouldn't have set the ok flag when no states are provided", automaton.isOk());
    }

    @Test
    public void FiniteStateAutomaton_is_not_correct_when_no_initial_state_is_provided() {
        IFiniteStateAutomaton automaton = new FiniteStateAutomaton();
        automaton.addState("Initial", mock(IAction.class));
        assertFalse("shouldn't have set the ok flag when no initial state is provided", automaton.isOk());
    }

    @Test
    public void FiniteStateAutomaton_is_not_correct_when_initial_state_does_not_exist() {
        IFiniteStateAutomaton automaton = new FiniteStateAutomaton();
        automaton.addState("Initial", mock(IAction.class));
        automaton.setInitialState("Init");
        assertFalse("shouldn't have set the ok flag when no state  \"iInit\" exist", automaton.isOk());
    }

    @Test
    public void FiniteStateAutomaton_is_not_correct_when_world_is_not_set() {
        IFiniteStateAutomaton automaton = new FiniteStateAutomaton();
        automaton.addState("Initial", mock(IAction.class));
        automaton.setInitialState("Initial");
        assertFalse("shouldn't have set the ok flag when no world is provided exist", automaton.isOk());
    }

    @Test
    public void FiniteStateAutomaton_can_use_transitions() {
        IFiniteStateAutomaton automaton = new FiniteStateAutomaton();
        automaton.addState("Initial", mock(IAction.class));
        automaton.addState("Final", mock(IAction.class));
        automaton.addTransition("Initial", "Final", mock(ICondition.class), 1f, mock(IAction.class));
        automaton.setInitialState("Initial");

        assertTrue("shoul've set final as next state of initial",
                automaton.getTargetStatesAsStrings("Initial").contains("Final"));
    }

    @Test
    public void getTargetStates_must_return_states_in_order() {
        IFiniteStateAutomaton automaton = new FiniteStateAutomaton();
        automaton.addState("Initial", mock(IAction.class));
        automaton.addState("STATE 1", mock(IAction.class));
        automaton.addState("STATE 2", mock(IAction.class));
        automaton.addTransition("Initial", "STATE 1", mock(ICondition.class), 0.1f, mock(IAction.class));
        automaton.addTransition("Initial", "STATE 2", mock(ICondition.class), 0.2f, mock(IAction.class));
        automaton.addTransition("Initial", "STATE 3", mock(ICondition.class), 0.15f, mock(IAction.class));
        automaton.setInitialState("Initial");

        assertTrue("shoul've set the STATE 2 (with bigger probability) the first",
                automaton.getTargetStatesAsStrings("Initial").get(0).equals("STATE 2"));
    }

    @Test
    public void FiniteStateAutomaton_launches_action_of_initial_state() {
        IFiniteStateAutomaton automaton = new FiniteStateAutomaton();
        IAction fakeAction = mock(IAction.class);
        ICharacter fakeCharacter = mock(ICharacter.class);
        IWorld fakeWorld = mock(IWorld.class);

        automaton.addState("Initial", fakeAction);
        automaton.addState("Final", mock(IAction.class));
        automaton.addTransition("Initial", "Final", mock(ICondition.class), 1f, mock(IAction.class));
        automaton.setInitialState("Initial");
        automaton.setCharacter(fakeCharacter);
        automaton.setWorld(fakeWorld);

        automaton.run();

        verify(fakeAction).run(any(), any(), any());
    }

    @Test
    public void run_directs_to_first_state_when_condition_accomplished_and_random_lower_than_probability() {
        IFiniteStateAutomaton automaton = new FiniteStateAutomaton();
        RandomNumberFactory.setNextValues(0.05f, 0.05f);
        
        IAction fakeInitialStateAction = mock(IAction.class);
        IAction fakeFirstStateAction = mock(IAction.class);
        IAction fakeSecondStateAction = mock(IAction.class);
        IAction fakeTransationActionFirstStateAction = mock(IAction.class);
        IAction fakeTransationActionSecondStateAction = mock(IAction.class);

        ICondition fakeConditionFirstState = mock(ICondition.class);
        when(fakeConditionFirstState.check(any(), any(), any())).thenReturn(Boolean.TRUE);
        ICondition fakeConditionSecondState = mock(ICondition.class);
        when(fakeConditionSecondState.check(any(), any(), any())).thenReturn(Boolean.TRUE);
        
        
        automaton.addState("INITIAL", fakeInitialStateAction);
        automaton.addState("FIRST", fakeFirstStateAction);
        automaton.addState("SECOND", fakeSecondStateAction);
        automaton.addTransition("INITIAL", "SECOND", fakeConditionSecondState , 0.1f, fakeTransationActionSecondStateAction);
        automaton.addTransition("INITIAL", "FIRST", fakeConditionFirstState, 0.2f, fakeTransationActionFirstStateAction);

        automaton.setInitialState("INITIAL");

        ICharacter fakeCharacter = mock(ICharacter.class);
        IWorld fakeWorld = mock(IWorld.class);

        automaton.setCharacter(fakeCharacter);
        automaton.setWorld(fakeWorld);

        automaton.run();

        verify(fakeTransationActionFirstStateAction, times(1)).run(any(), any(), any());
        verify(fakeTransationActionSecondStateAction, times(0)).run(any(), any(), any());
        assertEquals("Current state should be FIRST", "FIRST", automaton.getCurrentState());
    }
    
    @Test
    public void run_directs_to_second_state_when_first_condition_did_not_accomplish() {
        IFiniteStateAutomaton automaton = new FiniteStateAutomaton();
        RandomNumberFactory.setNextValues(0.05f, 0.05f);
        
        IAction fakeInitialStateAction = mock(IAction.class);
        IAction fakeFirstStateAction = mock(IAction.class);
        IAction fakeSecondStateAction = mock(IAction.class);
        IAction fakeTransationActionFirstStateAction = mock(IAction.class);
        IAction fakeTransationActionSecondStateAction = mock(IAction.class);

        ICondition fakeConditionFirstState = mock(ICondition.class);
        when(fakeConditionFirstState.check(any(), any(), any())).thenReturn(Boolean.FALSE);
        ICondition fakeConditionSecondState = mock(ICondition.class);
        when(fakeConditionSecondState.check(any(), any(), any())).thenReturn(Boolean.TRUE);
        
        
        automaton.addState("INITIAL", fakeInitialStateAction);
        automaton.addState("FIRST", fakeFirstStateAction);
        automaton.addState("SECOND", fakeSecondStateAction);
        automaton.addTransition("INITIAL", "SECOND", fakeConditionSecondState , 0.1f, fakeTransationActionSecondStateAction);
        automaton.addTransition("INITIAL", "FIRST", fakeConditionFirstState, 0.2f, fakeTransationActionFirstStateAction);

        automaton.setInitialState("INITIAL");

        ICharacter fakeCharacter = mock(ICharacter.class);
        IWorld fakeWorld = mock(IWorld.class);

        automaton.setCharacter(fakeCharacter);
        automaton.setWorld(fakeWorld);

        automaton.run();

        verify(fakeTransationActionFirstStateAction, times(0)).run(any(), any(), any());
        verify(fakeTransationActionSecondStateAction, times(1)).run(any(), any(), any());
        assertEquals("Current state should be SECOND", "SECOND", automaton.getCurrentState());
    }
    
    @Test
    public void run_directs_to_the_same_state_when_no_condition_accomplish() {
        IFiniteStateAutomaton automaton = new FiniteStateAutomaton();
        RandomNumberFactory.setNextValues(0.9f, 0.9f);
        
        IAction fakeInitialStateAction = mock(IAction.class);
        IAction fakeFirstStateAction = mock(IAction.class);
        IAction fakeSecondStateAction = mock(IAction.class);
        IAction fakeTransationActionFirstStateAction = mock(IAction.class);
        IAction fakeTransationActionSecondStateAction = mock(IAction.class);

        ICondition fakeConditionFirstState = mock(ICondition.class);
        when(fakeConditionFirstState.check(any(), any(), any())).thenReturn(Boolean.TRUE);
        ICondition fakeConditionSecondState = mock(ICondition.class);
        when(fakeConditionSecondState.check(any(), any(), any())).thenReturn(Boolean.TRUE);
        
        
        automaton.addState("INITIAL", fakeInitialStateAction);
        automaton.addState("FIRST", fakeFirstStateAction);
        automaton.addState("SECOND", fakeSecondStateAction);
        automaton.addTransition("INITIAL", "SECOND", fakeConditionSecondState , 0.1f, fakeTransationActionSecondStateAction);
        automaton.addTransition("INITIAL", "FIRST", fakeConditionFirstState, 0.2f, fakeTransationActionFirstStateAction);

        automaton.setInitialState("INITIAL");

        ICharacter fakeCharacter = mock(ICharacter.class);
        IWorld fakeWorld = mock(IWorld.class);

        automaton.setCharacter(fakeCharacter);
        automaton.setWorld(fakeWorld);

        automaton.run();

        verify(fakeTransationActionFirstStateAction, times(0)).run(any(), any(), any());
        verify(fakeTransationActionSecondStateAction, times(0)).run(any(), any(), any());
        assertEquals("Current state should be INITIAL", "INITIAL", automaton.getCurrentState());
    }
    
    @Test
    public void run_directs_to_the_same_state_when_no_random_under_probabilities() {
        IFiniteStateAutomaton automaton = new FiniteStateAutomaton();
        RandomNumberFactory.setNextValues(0.05f, 0.05f);
        
        IAction fakeInitialStateAction = mock(IAction.class);
        IAction fakeFirstStateAction = mock(IAction.class);
        IAction fakeSecondStateAction = mock(IAction.class);
        IAction fakeTransationActionFirstStateAction = mock(IAction.class);
        IAction fakeTransationActionSecondStateAction = mock(IAction.class);

        ICondition fakeConditionFirstState = mock(ICondition.class);
        when(fakeConditionFirstState.check(any(), any(), any())).thenReturn(Boolean.FALSE);
        ICondition fakeConditionSecondState = mock(ICondition.class);
        when(fakeConditionSecondState.check(any(), any(), any())).thenReturn(Boolean.FALSE);
        
        
        automaton.addState("INITIAL", fakeInitialStateAction);
        automaton.addState("FIRST", fakeFirstStateAction);
        automaton.addState("SECOND", fakeSecondStateAction);
        automaton.addTransition("INITIAL", "SECOND", fakeConditionSecondState , 0.1f, fakeTransationActionSecondStateAction);
        automaton.addTransition("INITIAL", "FIRST", fakeConditionFirstState, 0.2f, fakeTransationActionFirstStateAction);

        automaton.setInitialState("INITIAL");

        ICharacter fakeCharacter = mock(ICharacter.class);
        IWorld fakeWorld = mock(IWorld.class);

        automaton.setCharacter(fakeCharacter);
        automaton.setWorld(fakeWorld);

        automaton.run();

        verify(fakeTransationActionFirstStateAction, times(0)).run(any(), any(), any());
        verify(fakeTransationActionSecondStateAction, times(0)).run(any(), any(), any());
        assertEquals("Current state should be INITIAL", "INITIAL", automaton.getCurrentState());
    }
    
}
