package MainApp;

import BusinessLogic.SimulationManager;
import GUI.SimulationFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        //access manager for the simulation which has the GUI inside
        SimulationFrame frame = new SimulationFrame();
        frame.getButton().addActionListener(e -> new SimulationManager(frame));
    }
}
