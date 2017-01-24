package com.morgan.andy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FiniteAutomaton {

    private Long id;

    private List<State> states;

    private String[] alphabet;

    private State startState;

    private State currentAcceptState;

    public void setStates(List<State> states) {
        this.states = states;
    }

    public State getCurrentAcceptState() {
        return currentAcceptState;
    }

    public void setCurrentAcceptState(State currentAcceptState) {
        this.currentAcceptState = currentAcceptState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getStartState() {
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }

    public void addState(State state) {
        if(states == null) {
            states = new ArrayList<>();
        }
        states.add(state);
    }

    public String[] getAlphabet() { return alphabet; }

    public void setAlphabet(String[] alphabet) { this.alphabet = alphabet; }
}
