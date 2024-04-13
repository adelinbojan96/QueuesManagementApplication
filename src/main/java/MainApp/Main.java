package MainApp;

import BusinessLogic.SimulationManager;
import GUI.SimulationFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        SimulationFrame simulationFrame = new SimulationFrame();
        simulationFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                //when the SimulationFrame is disposed, exit the application
                System.exit(0);
            }
        });
    }
}
