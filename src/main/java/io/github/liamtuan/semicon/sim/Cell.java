package io.github.liamtuan.semicon.sim;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;

public class Cell {
    public int x, y, z;
    public Cell(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        Cell c = (Cell)obj;
        return c.x == x && c.y == y && c.z == z;
    }

    @Override
    public int hashCode() {
        return x*100000000 + y*10000 + z;
    }

    @Override
    public String toString() {
        return "( "+x+" , "+y+" , "+z+" )";
    }
    public static Cell fromString(String s){
        Scanner scanner = new Scanner(s);
        scanner.next();
        int x = scanner.nextInt();
        scanner.next();
        int y = scanner.nextInt();
        scanner.next();
        int z = scanner.nextInt();
        return new Cell(x, y, z);
    }
}
