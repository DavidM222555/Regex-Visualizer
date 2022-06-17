package com.example.regexvisualizer.regexutils;

import java.util.ArrayList;
import java.util.HashMap;

public class State {
    HashMap<Character, ArrayList<State>> transitions;
    public int id;
    public boolean isAccept;

    public State(int id, boolean isAccept) {
        transitions = new HashMap<>();
        this.id = id;
        this.isAccept = isAccept;
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

    public ArrayList<State> getTransitionStates(char transChar) {
        if(transitions.containsKey(transChar)) {
            return transitions.get(transChar);
        }

        return new ArrayList<>();
    }

    public void printTransitions() {
        System.out.println("State ID: " + id);

        for(char transChar : transitions.keySet()) {
            System.out.println("Transitions to the following states on char " + transChar);
            for(State transStates : transitions.get(transChar)) {
                System.out.println(transStates.id);
            }
        }
    }

}
