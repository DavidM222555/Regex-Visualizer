package com.example.regexvisualizer.regexutils;

import java.util.ArrayList;
import java.util.HashMap;

public class State {

    // Store regular transitions and epsilon transitions separately.
    // Initially my idea was to create a class for transitions and have an optional character value but I believe
    // this way of having a dictionary for regular transitions while noting epsilon transitions separately is a bit
    // easier to work with and probably more efficient.
    HashMap<Character, ArrayList<State>> transitions;
    ArrayList<State> epsilonTransitions; // Stores all the outgoing states from here along epsilon transitions
    public int id;
    public boolean isAccept;

    public State(int id, boolean isAccept) {
        this.transitions = new HashMap<>();
        this.id = id;
        this.isAccept = isAccept;
        this.epsilonTransitions = new ArrayList<>();
    }

    public void addTransition(char transChar, State transState) {
        // If the key is already in the map then we just append to the list
        if(transitions.containsKey(transChar)) {
            transitions.get(transChar).add(transState);
        } else {
            ArrayList<State> newList = new ArrayList<>();
            newList.add(transState);

            transitions.put(transChar, newList);
        }
    }

    public void addEpsilonTransition(State transState) {
        epsilonTransitions.add(transState);
    }

    public ArrayList<State> getTransitionStates(char transChar) {
        if(transitions.containsKey(transChar)) {
            return transitions.get(transChar);
        }

        return new ArrayList<>();
    }

}
