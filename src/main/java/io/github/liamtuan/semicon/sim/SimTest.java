package io.github.liamtuan.semicon.sim;

import io.github.liamtuan.semicon.App;
import io.github.liamtuan.semicon.AppData;
import io.github.liamtuan.semicon.sim.core.Analyser;
import io.github.liamtuan.semicon.sim.core.Node;
import org.json.JSONObject;
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
        if(!clockTest())
            System.out.println("clock test failed");
        if(!performanceTest())
            System.out.println("performance test failed");
        if(!latchTest())
            System.out.println("circuit latch test failed");
    }

    private boolean ioTest(){
        Circuit circuit = new Circuit();
        //circuit.setDebugLevel(5);
        UnitPin pin = new UnitPin(new Cell(0,0, 0), Dir.NEGZ, true);
        circuit.add(pin);
        UnitLed led = new UnitLed(new Cell(0, 0, -1));
        circuit.add(led);

        //circuit.setInpuState(pin.getPos(), true);
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
        String jsonstr = circuit.toJson().toString();
        circuit.fromJson(new JSONObject(jsonstr));


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

    private boolean wireBridgeTest(){
        Circuit circuit = new Circuit();

        UnitPin pin = new UnitPin(new Cell(0, 0, -1), POSZ);
        circuit.add(pin);
        UnitWire wirebridge = new UnitWire(new Cell(0, 0, 0), new Dir[][]{{POSX, NEGX},{POSZ, NEGZ}});
        circuit.add(wirebridge);
        UnitLed led = new UnitLed(new Cell(0, 0, 1));
        circuit.add(led);

        if(!circuit.getOutputState(led.getPos()))
            return false;
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


    boolean performanceTest(){
        Circuit circuit = new Circuit();
        for(int x = 0; x < 10; x++){
            for(int y = 0; y < 10; y++) {
                for (int z = 0; z < 10; z++) {
                    Unit gate = new UnitGate(new Cell(x, y, z), new Dir[]{NEGZ}, new Dir[]{POSZ}, "not");
                    circuit.add(gate);
                }
            }
        }
        return true;
    }


    private boolean clockTest(){
        Circuit circuit = new Circuit();
        //circuit.setDebugLevel(5);
        UnitClock clock = new UnitClock(new Cell(0,0, 0), Dir.NEGZ, 1.f);
        circuit.add(clock);
        UnitLed led = new UnitLed(new Cell(0, 0, -1));
        circuit.add(led);

        circuit.update(0.5f);
        //circuit.setInpuState(pin.getPos(), true);
        if(!circuit.getOutputState(led.getPos()))
            return false;

        circuit.update(0.5f);
        if(circuit.getOutputState(led.getPos()))
            return false;

        return true;
    }

    private boolean latchTest(){
        Circuit circuit = new Circuit();
        UnitGate srlatch = new UnitGate(new Cell(0, 0, 0), new Dir[]{POSZ, POSX}, new Dir[]{NEGZ, NEGX}, "srlatch");
        circuit.add(srlatch);
        UnitLed q = new UnitLed(new Cell(0, 0, -1));
        UnitLed qbar = new UnitLed(new Cell(-1, 0, 0));
        UnitPin s = new UnitPin(new Cell(0, 0, 1), NEGZ);
        UnitPin r = new UnitPin(new Cell(1, 0, 0), NEGX);
        circuit.add(q);
        circuit.add(qbar);
        circuit.add(s);
        circuit.add(r);

        circuit.setInpuState(s.getPos(), true);
        if(!circuit.getOutputState(q.getPos()) || circuit.getOutputState(qbar.getPos()))
            return false;

        circuit.setInpuState(s.getPos(), false);
        if(!circuit.getOutputState(q.getPos()) || circuit.getOutputState(qbar.getPos()))
            return false;

        circuit.setInpuState(r.getPos(), true);
        if(circuit.getOutputState(q.getPos()) || !circuit.getOutputState(qbar.getPos()))
            return false;

        return true;
    }
}
