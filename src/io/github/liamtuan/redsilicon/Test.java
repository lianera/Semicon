package io.github.liamtuan.redsilicon;

import io.github.liamtuan.redsilicon.core.*;

import java.util.Arrays;

public class Test {
    public void testAll(){
        if(!gateTest())
            System.out.println("gate test failed");
        if(!simpleTest())
            System.out.println("simple test failed");
        oscillationTest();
        if(!srTest())
            System.out.println("sr latch test failed");
    }

    boolean gateTest(){
        Processor p = new Processor();
        Node x1 = new Node();
        Node x2 = new Node();
        Node y = new Node();

        // and gate
        Gate and = new AndGate(x1, x2, y);
        Node[] nodes = new Node[]{x1, x2, y};
        Node[] events = new Node[]{x1, x2, y};
        int[] expect = new int[]{0, 0, 0};
        p.eval(events);
        if(!compareStates(nodes, expect))
            return false;

        x1.setState(true);
        events = new Node[]{x1};
        p.eval(events);
        expect = new int[]{1, 0, 0};
        if(!compareStates(nodes, expect))
            return false;

        x2.setState(true);
        events = new Node[]{x2};
        p.eval(events);
        expect = new int[]{1, 1, 1};
        if(!compareStates(nodes, expect))
            return false;

        x1.setState(false);
        events = new Node[]{x1};
        p.eval(events);
        expect = new int[]{0, 1, 0};
        if(!compareStates(nodes, expect))
            return false;

        and.dettach();

        // or gate
        Gate or = new OrGate(x1, x2, y);
        x1.setState(false);
        x2.setState(false);
        y.setState(false);
        events = new Node[]{x1, x2, y};
        expect = new int[]{0, 0, 0};
        p.eval(events);
        if(!compareStates(nodes, expect))
            return false;

        x1.setState(true);
        events = new Node[]{x1};
        p.eval(events);
        expect = new int[]{1, 0, 1};
        if(!compareStates(nodes, expect))
            return false;

        x2.setState(true);
        events = new Node[]{x2};
        p.eval(events);
        expect = new int[]{1, 1, 1};
        if(!compareStates(nodes, expect))
            return false;

        x1.setState(false);
        events = new Node[]{x1};
        p.eval(events);
        expect = new int[]{0, 1, 1};
        if(!compareStates(nodes, expect))
            return false;

        or.dettach();

        // not gate
        Gate not = new NotGate(x1, y);
        x1.setState(false);
        y.setState(false);
        nodes = new Node[]{x1, y};
        events = new Node[]{x1, y};
        expect = new int[]{0, 1};
        p.eval(events);
        if(!compareStates(nodes, expect))
            return false;

        x1.setState(true);
        events = new Node[]{x1};
        expect = new int[]{1, 0};
        p.eval(events);
        if(!compareStates(nodes, expect))
            return false;

        return true;
    }

    boolean simpleTest(){

        Processor p = new Processor();
        Node A = new Node();
        Node B = new Node();
        Node C = new Node();
        Node Q = new Node();
        Gate G1 = new NotGate(B, C);
        Gate G2 = new AndGate(A, C, Q);
        Node[] nodes = new Node[]{A, B, C, Q};
        p.eval(nodes);
        int[] expect = new int[]{0, 0, 1, 0};
        if(!compareStates(nodes, expect))
            return false;

        A.setState(true);
        B.setState(true);
        Node[] events = new Node[]{A, B};
        p.eval(events);
        expect = new int[]{1,1,0,0};
        if(!compareStates(nodes, expect))
            return false;
        return true;
    }

    boolean oscillationTest()
    {
        Processor p = new Processor();
        Node x = new Node();
        Gate not = new NotGate(x, x);
        Node[] nodes = new Node[]{x};
        p.eval(nodes);
        return true;
    }

    boolean srTest(){
        Processor p = new Processor();
        Node R = new Node();
        Node S = new Node();
        Node Q = new Node();
        Node QB = new Node();
        Node t1 = new Node();
        Node t2 = new Node();
        Gate G1 = new AndGate(R, QB, t1);
        Gate G2 = new AndGate(S, Q, t2);
        Gate N1 = new NotGate(t1, Q);
        Gate N2 = new NotGate(t2, QB);
        Node[] nodes = new Node[]{R, S, Q, QB, t1, t2};
        Node[] events = nodes;
        R.setState(true);
        p.eval(events);
        int[] expect = new int[]{1, 0, 0, 1, 1, 0};
        if(!compareStates(nodes, expect))
            return false;

        S.setState(true);
        events = new Node[]{S};
        p.eval(events);
        expect = new int[]{1, 1, 0, 1, 1, 0};
        if(!compareStates(nodes, expect))
            return false;

        R.setState(false);
        events = new Node[]{R};
        p.eval(events);
        expect = new int[]{0, 1, 1, 0, 0, 1};
        if(!compareStates(nodes, expect))
            return false;

        S.setState(false);
        events = new Node[]{S};
        p.eval(events);

        return true;
    }

    boolean compareStates(Node[] nodes, int[] states){
        for(int i = 0; i < nodes.length; i++){
            boolean b = states[i] != 0;
            if(nodes[i].getState() != b)
                return false;
        }
        return true;
    }

}
