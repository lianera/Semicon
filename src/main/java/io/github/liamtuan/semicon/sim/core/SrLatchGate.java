package io.github.liamtuan.semicon.sim.core;

public class SrLatchGate extends Gate{
    Node s, r, q, qbar;

    SrLatchGate(){
        this.s = new Node();
        this.r = new Node();
        this.q = new Node();
        this.qbar = new Node();

        super.attach();
    }

    @Override
    public Node[] getInputNodes() {
        return new Node[]{s, r};
    }

    @Override
    public Node[] getOutputNodes() {
        return new Node[]{q, qbar};
    }

    @Override
    public void setInputNodes(Node[] nodes) {
        s = nodes[0];
        r = nodes[1];
    }

    @Override
    public void setOutputNodes(Node[] nodes) {
        q = nodes[0];
        qbar = nodes[1];
    }

    @Override
    public void evel() {
        boolean s_state = s.getState();
        boolean r_state = r.getState();
        if(s_state){
            if(r_state){
                // illegal
                q.setState(true);
                qbar.setState(true);
            }else{
                q.setState(true);
                qbar.setState(false);
            }
        }else{
            if(r_state){
                q.setState(false);
                qbar.setState(true);
            }else{
                // latch

            }
        }
    }

    @Override
    public String getName() {
        return "srlatch";
    }
}
