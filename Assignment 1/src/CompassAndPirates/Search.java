package CompassAndPirates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Search {
    public final int tableSize = 9;
    public final double maxTime = 1000;
    private final IO.Input input;
    public ArrayList<Node> kraken, davy;
    public Node jack, chest, rock, tortuga, init, target;
    public boolean passTortuga;
    public Node[][] searchTable;
    public int scenario;
    private boolean krakenIsDead = false;

    public Search(IO.Input data) {
        input = data;
        setCharacters();
    }

    private static boolean nodesColliding(ArrayList<Node> table, Node n) {
        for (Node t : table) if (t.equals(n)) return true;
        return false;
    }

    private static boolean nodesColliding(Node n1, Node n2) {
        return n1.equals(n2);
    }

    private void setCharacters() {
        Map<String, ArrayList<Node>> pos = input.positions;
        jack = pos.get("Jack").get(0);
        davy = pos.get("Davy");
        kraken = pos.get("Kraken");
        rock = pos.get("Rock").get(0);
        chest = pos.get("Chest").get(0);
        tortuga = pos.get("Tortuga").get(0);
        scenario = input.scenario;
    }

    public IO.Output[] viaBoth() {
        return new IO.Output[]{viaAStar(), viaBacktracking()};
    }

    public IO.Output viaAStar() {
        return new AStar(input).findPath();
    }

    public IO.Output viaBacktracking() {
        return new Backtracking(input).findPath();
    }

    public double getTime(double t) {
        return (System.nanoTime() - t) / 1000000;
    }

    public void setRoute(Node initialNode, Node targetNode) {
        init = initialNode;
        target = targetNode;
        passTortuga = initialNode.equals(tortuga) || targetNode.equals(tortuga);
    }

    public Node[][] createTable() {
        return createTable(false);
    }

    public Node[][] createTable(boolean lock) {
        Node[][] table = new Node[tableSize][tableSize];
        for (int i = 0; i < tableSize; i++)
            for (int j = 0; j < tableSize; j++) {
                Node n = new Node(i, j);
                n.setH(target);
                table[i][j] = n;
                table[i][j].isLocked = lock;
            }
        return table;
    }

    public void lockNode(ArrayList<Node> cells, boolean lock) {
        for (Node n : cells)
            if (isSafe(n.row, n.col))
                searchTable[n.row][n.col].isLocked = lock;
    }

    public void lookAround(Node n) {
        int row = n.row, col = n.col;
        if (passTortuga) killKraken(n);
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                if (i != 0 || j != 0)
                    setBarriers(row + i, col + j);
        if (scenario == 2) {
            for (int i = -2; i < 3; i += 2)
                for (int j = -2; j < 3; j += 2)
                    if (Math.abs(i) != Math.abs(j))
                        setBarriers(row + i, col + j);
        }
    }

    public void setBarriers(int row, int col) {
        Node n = new Node(row, col);
        if (!davy.get(0).isLocked && nodesColliding(davy, n))
            lockNode(davy, true);
        if (!krakenIsDead && !kraken.get(4).isLocked && nodesColliding(new ArrayList<>(kraken.subList(0, 5)), n))
            lockNode(kraken, true);
        if (!rock.isLocked && nodesColliding(rock, n))
            lockNode(rock, true);
    }

    public void lockNode(Node n, boolean lock) {
        int row = n.row, col = n.col;
        if (!isSafe(row, col)) return;
        searchTable[row][col].isLocked = lock;
    }

    public boolean isSafe(int row, int col) {
        return 0 <= row && row < tableSize && 0 <= col && col < tableSize;
    }

    public void killKraken(Node n) {
        for (int i = 5; i < kraken.size(); i++) {
            if (!n.equals(kraken.get(i))) continue;
            lockNode(kraken, false);
            lockNode(davy, true);
            lockNode(new ArrayList<>(List.of(rock)), true);
            krakenIsDead = true;
        }
    }
}
