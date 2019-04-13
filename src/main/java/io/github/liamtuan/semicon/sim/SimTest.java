package io.github.liamtuan.semicon.sim;

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
    }

    private boolean ioTest(){
        Circuit.init();
        //Circuit.setDebugLevel(5);
        UnitPin pin = new UnitPin(new Cell(0,0, 0), Dir.NEGZ);
        Circuit.add(new UnitPin(new Cell(0,0, 0), Dir.NEGZ));
        UnitLed led = new UnitLed(new Cell(0, 0, -1));
        Circuit.add(led);

        Circuit.setInpuState(pin.getPos(), true);
        if(!Circuit.getOutputState(led.getPos()))
            return false;

        Circuit.remove(pin.getPos());
        if(Circuit.getOutputState(led.getPos()))
            return false;

        return true;
    }

    private boolean wireTest(){
        Circuit.init();
        //Circuit.setVerbose(true);
        UnitPin pin = new UnitPin(new Cell(0,0,0), Dir.NEGZ);
        Circuit.add(pin);
        UnitWire wire = new UnitWire(new Cell(0, 0, -1), new Dir[]{NEGZ, POSZ, POSX});
        Circuit.add(wire);
        UnitLed led = new UnitLed(new Cell(0, 0, -2));
        Circuit.add(led);
        UnitLed led2 = new UnitLed(new Cell(1, 0, -1));
        Circuit.add(led2);

        Circuit.setInpuState(pin.getPos(), true);
        if(!Circuit.getOutputState(led.getPos()))
            return false;
        if(!Circuit.getOutputState(led2.getPos()))
            return false;

        Circuit.remove(wire.getPos());

        if(Circuit.getOutputState(led.getPos()))
            return false;
        if(Circuit.getOutputState(led2.getPos()))
            return false;

        return true;
    }

    private static boolean wireLoopTest(){
        Circuit.init();
        //Circuit.setDebugLevel(5);
        UnitPin pin = new UnitPin(new Cell(0, 0, 0), POSZ);
        Circuit.add(pin);
        UnitLed led = new UnitLed(new Cell(-2, 0, 1));
        Circuit.add(led);
        UnitWire wire1 = new UnitWire(new Cell(-1, 0, 1), Dir.horizValues());
        UnitWire wire2 = new UnitWire(new Cell(-1, 0, 2), Dir.horizValues());
        UnitWire wire3 = new UnitWire(new Cell(0, 0, 2), Dir.horizValues());
        UnitWire wire4 = new UnitWire(new Cell(0, 0, 1), Dir.horizValues());
        Circuit.add(wire1);
        Circuit.add(wire2);
        Circuit.add(wire3);
        Circuit.add(wire4);

        Circuit.setInpuState(pin.getPos(), true);
        if(!Circuit.getOutputState(led.getPos()))
            return false;
        Circuit.remove(wire4.getPos());

        if(Circuit.getOutputState(led.getPos()))
            return false;
        Circuit.remove(wire3.getPos());
        if(Circuit.getOutputState(led.getPos()))
            return false;
        Circuit.add(new UnitWire(new Cell(0, 0, 2), Dir.horizValues()));
        if(Circuit.getOutputState(led.getPos()))
            return false;
        Circuit.add(new UnitWire(new Cell(0, 0, 1), Dir.horizValues()));
        if(!Circuit.getOutputState(led.getPos()))
            return false;
        Circuit.remove(wire3.getPos());
        Circuit.remove(wire4.getPos());
        if(Circuit.getOutputState(led.getPos()))
            return false;

        return true;
    }

    private boolean gateTest(){
        Circuit.init();
        UnitPin pin = new UnitPin(new Cell(0, 0, 0), NEGZ);
        Circuit.add(pin);
        UnitPin pin2 = new UnitPin(new Cell(1, 0, -1), NEGX);
        Circuit.add(pin2);

        UnitGate gate = new UnitGate(new Cell(0, 0, -1),
                new Dir[]{POSZ, POSX}, new Dir[]{NEGZ},"and");
        Circuit.add(gate);

        UnitLed led = new UnitLed(new Cell(0, 0, -2));
        Circuit.add(led);

        Circuit.setInpuState(pin.getPos(), true);
        Circuit.setInpuState(pin2.getPos(), true);

        if(!Circuit.getOutputState(led.getPos()))
            return false;

        Circuit.remove(gate.getPos());
        if(Circuit.getOutputState(led.getPos()))
            return false;

        return true;
    }

    private boolean gateLoopTest(){
        Circuit.init();
        //Circuit.setDebugLevel(5);
        UnitLed led = new UnitLed(new Cell(0, 0, 4));
        Circuit.add(led);

        UnitGate notgate = new UnitGate(new Cell(0, 0, 2),
                    new Dir[]{NEGZ}, new Dir[]{POSZ},"not");
        Circuit.add(notgate);

        Circuit.add(new UnitWire(new Cell(0, 0, 1), Dir.horizValues()));
        Circuit.add(new UnitWire(new Cell(1, 0, 1), Dir.horizValues()));
        Circuit.add(new UnitWire(new Cell(1, 0, 2), Dir.horizValues()));
        Circuit.add(new UnitWire(new Cell(1, 0, 3), Dir.horizValues()));
        Circuit.add(new UnitWire(new Cell(0, 0, 3), Dir.horizValues()));

        //Circuit.setVerbose(true);
        Circuit.remove(new Cell(1, 0, 2));

        UnitPin pin = new UnitPin(new Cell(0,0,0), POSZ);
        Circuit.add(pin);
        //Circuit.printCircuit();

        if(!Circuit.getOutputState(led.getPos()))
            return false;

        Circuit.setInpuState(pin.getPos(), true);
        if(Circuit.getOutputState(led.getPos()))
            return false;

        return true;
    }
}
