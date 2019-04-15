package io.github.liamtuan.semicon.sim.core;

import java.util.ArrayList;
import java.util.List;

public class CoreTest {

    public void testAll(){
        if(!gateTest())
            System.out.println("gate test failed");
        if(!simpleTest())
            System.out.println("simple test failed");
        oscillationTest();
        if(!srTest())
            System.out.println("sr latch test failed");
        if(!MergeTest())
            System.out.println("node merge test failed");
        if(!stackedTest())
            System.out.println("stacked test failed");
        if(!latchTest())
            System.out.println("latch test failed");
    }

    boolean gateTest(){
        Processor p = new Processor();
        Node x1 = new Node();
        Node x2 = new Node();
        Node y = new Node();

        // and gate
        Gate and = new AndGate();
        and.setInputNodes(new Node[]{x1, x2});
        and.setOutputNodes(new Node[]{y});
        and.attach();

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
        Gate or = new OrGate();
        or.setInputNodes(new Node[]{x1, x2});
        or.setOutputNodes(new Node[]{y});
        or.attach();

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


        // xor gate
        Gate xor = new XorGate();
        xor.setInputNodes(new Node[]{x1, x2});
        xor.setOutputNodes(new Node[]{y});
        xor.attach();

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
        expect = new int[]{1, 1, 0};
        if(!compareStates(nodes, expect))
            return false;

        x1.setState(false);
        events = new Node[]{x1};
        p.eval(events);
        expect = new int[]{0, 1, 1};
        if(!compareStates(nodes, expect))
            return false;

        xor.dettach();

        // not gate
        Gate not = new NotGate();
        not.setInputNodes(new Node[]{x1});
        not.setOutputNodes(new Node[]{y});
        not.attach();

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

    boolean latchTest(){
        Processor p = new Processor();
        Node s = new Node();
        Node r = new Node();
        Node q = new Node();
        Node qbar = new Node();
        SrLatchGate srlatch = new SrLatchGate();
        srlatch.setInputNodes(new Node[]{s, r});
        srlatch.setOutputNodes(new Node[]{q, qbar});
        srlatch.attach();

        s.setState(true);
        Node[] events = new Node[]{s, r, q, qbar};
        p.eval(events);
        if(!compareStates(events, new int[]{1, 0, 1, 0}))
            return false;

        s.setState(false);
        p.eval(events);
        if(!compareStates(events, new int[]{0, 0, 1, 0}))
            return false;

        r.setState(true);
        p.eval(events);
        if(!compareStates(events, new int[]{0, 1, 0, 1}))
            return false;

        return true;
    }

    boolean simpleTest(){

        Processor p = new Processor();
        Node A = new Node();
        Node B = new Node();
        Node C = new Node();
        Node Q = new Node();
        Gate G1 = new NotGate();
        G1.setInputNodes(new Node[]{B});
        G1.setOutputNodes(new Node[]{C});
        G1.attach();

        Gate G2 = new AndGate();
        G2.setInputNodes(new Node[]{A, C});
        G2.setOutputNodes(new Node[]{Q});
        G2.attach();

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
        Gate not = new NotGate();
        not.setInputNodes(new Node[]{x});
        not.setOutputNodes(new Node[]{x});
        not.attach();

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
        Gate G1 = new AndGate();
        G1.setInputNodes(new Node[]{R, QB});
        G1.setOutputNodes(new Node[]{t1});
        G1.attach();

        Gate G2 = new AndGate();
        G2.setInputNodes(new Node[]{S, Q});
        G2.setOutputNodes(new Node[]{t2});
        G2.attach();

        Gate N1 = new NotGate();
        N1.setInputNodes(new Node[]{t1});
        N1.setOutputNodes(new Node[]{Q});
        N1.attach();

        Gate N2 = new NotGate();
        N2.setInputNodes(new Node[]{t2});
        N2.setOutputNodes(new Node[]{QB});
        N2.attach();

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

    boolean MergeTest(){
        Node A = new Node();
        Node B = new Node();
        Node C = new Node();
        Node D = new Node();
        Gate not = new NotGate();
        not.setInputNodes(new Node[]{A});
        not.setOutputNodes(new Node[]{B});
        not.attach();

        Gate and = new AndGate();
        and.setInputNodes(new Node[]{B, C});
        and.setOutputNodes(new Node[]{D});
        and.attach();

        Node[] nodes = new Node[]{A, B, C, D};
        Node[] events = nodes;
        C.setState(true);
        Processor p = new Processor();
        p.eval(events);
        int[] expect = new int[]{0, 1, 1, 1};
        if(!compareStates(nodes, expect))
            return false;

        Node E = new Node();
        Node F = new Node();
        Node G = new Node();
        Gate or = new OrGate();
        or.setInputNodes(new Node[]{E, F});
        or.setOutputNodes(new Node[]{G});
        or.attach();

        nodes = new Node[]{E, F, G};
        events = nodes;
        p.eval(events);
        expect = new int[]{0, 0, 0};
        if(!compareStates(nodes, expect))
            return false;

        B.merge(E);
        events = new Node[]{B};
        p.eval(events);
        nodes = new Node[]{A, B, C, D, F, G};
        expect = new int[]{0, 1, 1, 1, 0, 1};
        if(!compareStates(nodes, expect))
            return false;

        return true;
    }

    boolean stackedTest(){
        Processor p = new Processor();

        Node in = new Node();
        Node y = in;
        List<Node> nodes = new ArrayList<>();
        nodes.add(in);
        boolean expect = false;
        for(int i = 0; i < 100; i++){
            Node x = y;
            y = new Node();
            nodes.add(y);
            Gate notgate = new NotGate();
            notgate.setInputNodes(new Node[]{x});
            notgate.setOutputNodes(new Node[]{y});
            notgate.attach();
            expect = !expect;
        }
        p.eval(nodes.toArray(new Node[0]));
        if(y.getState() != expect)
            return false;
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
