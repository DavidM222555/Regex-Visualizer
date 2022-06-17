package com.example.regexvisualizer.regexutils;

import java.util.ArrayList;

public class NFA {
    ArrayList<State> listOfStates;
    ArrayList<State> acceptStates;
    State startState;
    ArrayList<State> currentStates;

    public NFA(ArrayList<State> listOfStates, ArrayList<State> acceptStates, State startState) {
        this.listOfStates = listOfStates;
        this.acceptStates = acceptStates;
        this.startState = startState;
        this.currentStates = new ArrayList<>();

        currentStates.add(startState); // We begin in the start state
    }

    public void addState(State stateToAdd) {
        listOfStates.add(stateToAdd);
    }

    public boolean inAcceptState() {
        for(var state : currentStates) {
            if(state.isAccept) {
                return true;
            }
        }

        return false;
    }

    // Place in currentStates all states reachable from the current set of states on charRead
    public void transition(char charRead) {
        ArrayList<State> newCurrentStates = new ArrayList<>();

        for(State state : currentStates) {
            var transitionStates = state.getTransitionStates(charRead);

            newCurrentStates.addAll(transitionStates);
        }

        currentStates = newCurrentStates;
    }
}
