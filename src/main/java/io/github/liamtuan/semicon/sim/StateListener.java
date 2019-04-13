package io.github.liamtuan.semicon.sim;

public interface StateListener {
    void onStateChanged(Cell cell, boolean state);
}
