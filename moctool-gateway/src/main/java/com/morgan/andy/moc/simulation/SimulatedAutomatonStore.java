package com.morgan.andy.moc.simulation;

import java.util.ArrayList;
import java.util.HashMap;

public class SimulatedAutomatonStore {

    private HashMap<Integer, Simulation> simulations = new HashMap<>();

    public HashMap<Integer, Simulation> getSimulations() {
        return simulations;
    }

    public void setSimulations(HashMap<Integer, Simulation> simulations) {
        this.simulations = simulations;
    }

    public void addSimulation(Integer simulationId, Simulation simulation) {
        simulations.put(simulationId, simulation);
    }

    public Simulation getSimulation(Integer simulationId) {
        return simulations.get(simulationId);
    }
}
