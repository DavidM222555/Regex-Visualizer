package com.example.regexvisualizer.regexutils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NFA {
    ArrayList<State> listOfStates;
    ArrayList<State> acceptStates;
    State startState;
    ArrayList<State> currentStates;
    static int idsForStates = 100;


    public NFA() {
        this.listOfStates = new ArrayList<>();
        this.acceptStates = new ArrayList<>();
        this.currentStates = new ArrayList<>();
    }


    public static NFA symbolNFA(char transChar) {
        NFA symbolNFA = new NFA();

        State startState = new State(idsForStates, false);
        idsForStates++;
        State endState = new State(idsForStates, true);
        idsForStates++;

        startState.addTransition(transChar, endState);
        symbolNFA.addState(startState);
        symbolNFA.addState(endState);
        symbolNFA.addAcceptState(endState);
        symbolNFA.addStartState(startState);

        symbolNFA.addCurrentState(startState);

        return symbolNFA;
    }

    // This unions two NFAs together, giving them
    // a joined start state and a new accepting state
    public static NFA unionNFA(NFA nfa1, NFA nfa2) {
        NFA unionedNFA = new NFA();

        for(var state : nfa1.getAllStates()) {
            unionedNFA.addState(state);
        }

        for(var state : nfa2.getAllStates()) {
            unionedNFA.addState(state);
        }

        State startState = new State(idsForStates, false);
        idsForStates++;
        State endState = new State(idsForStates, true);
        idsForStates++;

        startState.addEpsilonTransition(nfa1.getStartState());
        startState.addEpsilonTransition(nfa2.getStartState());

        unionedNFA.addStartState(startState);
        unionedNFA.addAcceptState(endState);
        unionedNFA.addState(startState);
        unionedNFA.addState(endState);

        for(var state : nfa1.getAcceptStates()) {
            state.addEpsilonTransition(endState);
        }

        for(var state : nfa2.getAcceptStates()) {
            state.addEpsilonTransition(endState);
        }

        unionedNFA.addCurrentState(startState);
        unionedNFA.addStartState(startState);

        return unionedNFA;
    }

    public static NFA concatenatedNFA(NFA nfa1, NFA nfa2) {
        NFA concatNFA = new NFA();

        for(var state : nfa1.getAllStates()) {
            concatNFA.addState(state);
        }

        for(var state : nfa2.getAllStates()) {
            concatNFA.addState(state);
        }

        concatNFA.addStartState(nfa1.getStartState());
        concatNFA.addCurrentState(nfa1.getStartState());

        // Add all accept states from nfa2 to the combined NFA
        for(var state : nfa2.getAcceptStates()) {
            concatNFA.addAcceptState(state);
        }

        // Add epsilon transitions from the accept states of
        // nfa1 to the start states of nfa2
        for(var state : nfa1.getAcceptStates()) {
            state.addEpsilonTransition(nfa2.getStartState());
        }

        return concatNFA;
    }

    public static NFA kleeneNFA(NFA nfa) {
        NFA klNFA = new NFA();

        State startState = new State(idsForStates, false);
        idsForStates++;
        State endState = new State(idsForStates, true);
        idsForStates++;

        klNFA.addState(startState);
        klNFA.addState(endState);

        for(var state : nfa.getAllStates()) {
            klNFA.addState(state);
        }

        // Add an epsilon transition from the end state to the start state
        for(var state : nfa.getAcceptStates()) {
            state.addEpsilonTransition(nfa.getStartState());
        }

        for(var state : nfa.getAcceptStates()) {
            state.addEpsilonTransition(endState);
        }

        startState.addEpsilonTransition(nfa.getStartState());
        startState.addEpsilonTransition(endState);
        klNFA.addStartState(startState);
        klNFA.addCurrentState(startState);
        klNFA.addAcceptState(endState);

        return klNFA;
    }

    public State getStartState() {
       return this.startState;
    }

    public void addState(State stateToAdd) {
        listOfStates.add(stateToAdd);
    }

    public boolean inAcceptState() {
        for(var state : currentStates) {
            if(acceptStates.contains(state)) {
                return true;
            }
        }

        return false;
    }

    public void addCurrentState(State stateToAdd) {
        this.currentStates.add(stateToAdd);
    }

    public ArrayList<State> getAcceptStates() {
        return this.acceptStates;
    }

    public ArrayList<State> getAllStates() {
        return this.listOfStates;
    }

    public void addAcceptState(State acceptState) {
        this.acceptStates.add(acceptState);
    }

    public void addStartState(State startState) {
        this.startState = startState;
    }

    // Gets all states reachable along epsilon transitions from a given state
    public ArrayList<State> getStatesReachableAlongEpsilonTransitions(State state) {
        ArrayList<State> returnList = state.epsilonTransitions;

        while(true) {
            int prevLength = returnList.size();

            for(int i = 0; i < returnList.size(); i++) {
                ArrayList<State> epsilonStatesFromHere = returnList.get(i).epsilonTransitions;

                for (State epsilonState : epsilonStatesFromHere) {
                    if (!returnList.contains(epsilonState)) {
                        returnList.add(epsilonState);
                    }
                }
            }

            if(returnList.size() == prevLength) {
                break;
            }
        }

        return returnList;
    }

    public ArrayList<State> allStatesFromEpsilonTransitions(ArrayList<State> listOfStatesToGetTransFor) {
        ArrayList<State> returnList = new ArrayList<>(listOfStatesToGetTransFor);

        for(State state : listOfStatesToGetTransFor) {
            ArrayList<State> epsilonStatesFromHere = this.getStatesReachableAlongEpsilonTransitions(state);

            returnList.addAll(epsilonStatesFromHere);
        }

        Set<State> set = new HashSet<>(returnList);
        returnList.clear();
        returnList.addAll(set);

        return returnList;
    }

    // Resets the current states of the NFA
    public void reset() {
        this.currentStates.clear();

        this.currentStates.add(this.startState);
    }

    public void transition(char charRead) {
        ArrayList<State> newCurrentStates = new ArrayList<>();

        // Begin by going to all states reachable along epsilon transitions
        ArrayList<State> statesReachableAlongEpsilonTransitions = allStatesFromEpsilonTransitions(this.currentStates);

        currentStates.addAll(statesReachableAlongEpsilonTransitions);

        Set<State> setOfStates = new HashSet<>(currentStates);
        currentStates.clear();
        currentStates.addAll(setOfStates);


        // Now read a single character and go to all states reachable from that transition character
        for(State state : currentStates) {
            var transitionStates = state.getTransitionStates(charRead);

            newCurrentStates.addAll(transitionStates);
        }

        currentStates = newCurrentStates;
        var statesReachableAlongEpsilonAfterTransition = allStatesFromEpsilonTransitions(currentStates);
        currentStates.addAll(statesReachableAlongEpsilonAfterTransition);

        Set<State> setOfStatesAfter = new HashSet<>(currentStates);
        currentStates.clear();
        currentStates.addAll(setOfStatesAfter);

    }

    public boolean readString(String input) {
        CharacterIterator it = new StringCharacterIterator(input);

        while(it.current() != CharacterIterator.DONE) {
            transition(it.current());
            it.next();
        }

        boolean result = inAcceptState();
        this.reset(); // Reset current states

        return result;
    }
}
