package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.sim.core.Analyser;
import io.github.liamtuan.semicon.sim.core.Node;
import org.lwjgl.Sys;

import static io.github.liamtuan.semicon.sim.Dir.*;

public class SimTest {
    public SimTest(){
    }

    public void testAll(){
        if(!ioTest())
            System.out.println("io test failed");
        if(!wireTest())
            System.out.println("wire test failed");
        if(!gateTest())
            System.out.println("gate test failed");
        if(!wireLoopTest())
            System.out.println("wire loop test failed");
        if(!gateLoopTest())
            System.out.println("gate loop test failed");
        if(!stackedNotGateTest())
            System.out.println("stacked not gate test failed");
    }

    private boolean ioTest(){
        Circuit circuit = new Circuit();
        //circuit.setDebugLevel(5);
        UnitPin pin = new UnitPin(new Cell(0,0, 0), Dir.NEGZ);
        circuit.add(new UnitPin(new Cell(0,0, 0), Dir.NEGZ));
        UnitLed led = new UnitLed(new Cell(0, 0, -1));
        circuit.add(led);

        circuit.setInpuState(pin.getPos(), true);
        if(!circuit.getOutputState(led.getPos()))
            return false;

        circuit.remove(pin.getPos());
        if(circuit.getOutputState(led.getPos()))
            return false;

        return true;
    }

    private boolean wireTest(){
        Circuit circuit = new Circuit();
        //circuit.setVerbose(true);
        UnitPin pin = new UnitPin(new Cell(0,0,0), Dir.NEGZ);
        circuit.add(pin);
        UnitWire wire = new UnitWire(new Cell(0, 0, -1), new Dir[]{NEGZ, POSZ, POSX});
        circuit.add(wire);
        UnitLed led = new UnitLed(new Cell(0, 0, -2));
        circuit.add(led);
        UnitLed led2 = new UnitLed(new Cell(1, 0, -1));
        circuit.add(led2);

        circuit.setInpuState(pin.getPos(), true);
        if(!circuit.getOutputState(led.getPos()))
            return false;
        if(!circuit.getOutputState(led2.getPos()))
            return false;

        circuit.remove(wire.getPos());

        if(circuit.getOutputState(led.getPos()))
            return false;
        if(circuit.getOutputState(led2.getPos()))
            return false;

        return true;
    }

    private static boolean wireLoopTest(){
        Circuit circuit = new Circuit();
        //circuit.setDebugLevel(5);
        UnitPin pin = new UnitPin(new Cell(0, 0, 0), POSZ);
        circuit.add(pin);
        UnitLed led = new UnitLed(new Cell(-2, 0, 1));
        circuit.add(led);
        UnitWire wire1 = new UnitWire(new Cell(-1, 0, 1), Dir.horizValues());
        UnitWire wire2 = new UnitWire(new Cell(-1, 0, 2), Dir.horizValues());
        UnitWire wire3 = new UnitWire(new Cell(0, 0, 2), Dir.horizValues());
        UnitWire wire4 = new UnitWire(new Cell(0, 0, 1), Dir.horizValues());
        circuit.add(wire1);
        circuit.add(wire2);
        circuit.add(wire3);
        circuit.add(wire4);

        // serialize test
        circuit.fromJson(circuit.toJson());


        circuit.setInpuState(pin.getPos(), true);
        if(!circuit.getOutputState(led.getPos()))
            return false;
        circuit.remove(wire4.getPos());

        if(circuit.getOutputState(led.getPos()))
            return false;
        circuit.remove(wire3.getPos());
        if(circuit.getOutputState(led.getPos()))
            return false;
        circuit.add(new UnitWire(new Cell(0, 0, 2), Dir.horizValues()));
        if(circuit.getOutputState(led.getPos()))
            return false;
        circuit.add(new UnitWire(new Cell(0, 0, 1), Dir.horizValues()));
        if(!circuit.getOutputState(led.getPos()))
            return false;
        circuit.remove(wire3.getPos());
        circuit.remove(wire4.getPos());
        if(circuit.getOutputState(led.getPos()))
            return false;

        return true;
    }

    private boolean gateTest(){
        Circuit circuit = new Circuit();

        UnitPin pin = new UnitPin(new Cell(0, 0, 0), NEGZ);
        circuit.add(pin);
        UnitPin pin2 = new UnitPin(new Cell(1, 0, -1), NEGX);
        circuit.add(pin2);

        UnitGate gate = new UnitGate(new Cell(0, 0, -1),
                new Dir[]{POSZ, POSX}, new Dir[]{NEGZ},"and");
        circuit.add(gate);

        UnitLed led = new UnitLed(new Cell(0, 0, -2));
        circuit.add(led);

        // serialize test
        circuit.fromJson(circuit.toJson());


        circuit.setInpuState(pin.getPos(), true);
        circuit.setInpuState(pin2.getPos(), true);

        if(!circuit.getOutputState(led.getPos()))
            return false;

        circuit.remove(gate.getPos());
        if(circuit.getOutputState(led.getPos()))
            return false;

        return true;
    }

    private boolean gateLoopTest(){
        Circuit circuit = new Circuit();
        //circuit.setDebugLevel(5);
        UnitLed led = new UnitLed(new Cell(0, 0, 4));
        circuit.add(led);

        UnitGate notgate = new UnitGate(new Cell(0, 0, 2),
                    new Dir[]{NEGZ}, new Dir[]{POSZ},"not");
        circuit.add(notgate);

        circuit.add(new UnitWire(new Cell(0, 0, 1), Dir.horizValues()));
        circuit.add(new UnitWire(new Cell(1, 0, 1), Dir.horizValues()));
        circuit.add(new UnitWire(new Cell(1, 0, 2), Dir.horizValues()));
        circuit.add(new UnitWire(new Cell(1, 0, 3), Dir.horizValues()));
        circuit.add(new UnitWire(new Cell(0, 0, 3), Dir.horizValues()));

        // serialize and deserialize test
        circuit.fromJson(circuit.toJson());

        //circuit.setVerbose(true);
        circuit.remove(new Cell(1, 0, 2));

        UnitPin pin = new UnitPin(new Cell(0,0,0), POSZ);
        circuit.add(pin);
        //circuit.printCircuit();

        if(!circuit.getOutputState(led.getPos()))
            return false;

        circuit.setInpuState(pin.getPos(), true);

        if(circuit.getOutputState(led.getPos()))
            return false;

        //System.out.println(circuit.toJson());
        return true;
    }

    private boolean stackedNotGateTest(){
        Circuit circuit = new Circuit();
        //circuit.setDebugLevel(5);
        UnitLed led = new UnitLed(new Cell(0, 0, -1));
        circuit.add(led);
        boolean expect = false;
        UnitGate notgate = null;
        for(int i = 0; i < 4; i++){
            notgate = new UnitGate(new Cell(0, 0, i), new Dir[]{POSZ}, new Dir[]{NEGZ}, "not");
            circuit.add(notgate);
            expect = !expect;
            if(circuit.getOutputState(led.getPos()) != expect) {
                Analyser analyser = new Analyser();
                System.out.println("failed at " + i);
                Node[] inputs = notgate.getInputNodes().toArray(new Node[0]);
                System.out.println(analyser.getGraphViz(inputs));
                circuit.printNodes();
                return false;
            }
        }


        //circuit.printSvg();

        return true;
    }
}
