package CompassAndPirates;

import java.util.*;

public class AStar extends Search {
    private PriorityQueue<Node> nodePriorityQueue;
    private Set<Node> nodeSet;

    public AStar(IO.Input data) {
        super(data);
    }

    public IO.Output findPath() {
        double t = System.nanoTime();

        List<Node> toChest = getPath(jack, chest),
                toTortuga = getPath(jack, tortuga), fromTortuga = getPath(tortuga, chest);

        List<Node> path = toChest;
        if (toTortuga != null && fromTortuga != null) {
            fromTortuga.remove(0);
            toTortuga.addAll(fromTortuga);
            if (toChest == null || toChest.size() > toTortuga.size())
                path = toTortuga;
        }

        return new IO.Output("AStar", path, getTime(t));
    }

    private List<Node> getPath(Node initialNode, Node targetNode) {
        double t = System.nanoTime();
        setRoute(initialNode, targetNode);

        nodePriorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        nodeSet = new HashSet<>();
        searchTable = createTable();

        nodePriorityQueue.add(init);
        for (Node n; nodePriorityQueue.size() != 0; ) {
            if (getTime(t) > maxTime) break;
            n = nodePriorityQueue.poll();
            lookAround(n);
            nodeSet.add(n);
            if (!target.equals(n)) {
                addUpper(n);
                addMiddle(n);
                addLower(n);
            } else return pavePaths(n);
        }
        return null;
    }

    private List<Node> pavePaths(Node n) {
        List<Node> path = new ArrayList<>(List.of(n));
        for (Node parent; (parent = n.parent) != null; n = parent)
            path.add(0, parent);
        return path;
    }

    private void addLower(Node n) {
        int lowerRow = n.row + 1;
        if (lowerRow >= searchTable.length) return;
        if (n.col - 1 >= 0) checkNode(n, n.col - 1, lowerRow);
        if (n.col + 1 < searchTable[0].length) checkNode(n, n.col + 1, lowerRow);
        checkNode(n, n.col, lowerRow);
    }

    private void addMiddle(Node n) {
        if (n.col - 1 >= 0) checkNode(n, n.col - 1, n.row);
        if (n.col + 1 < searchTable[0].length) checkNode(n, n.col + 1, n.row);
    }

    private void addUpper(Node n) {
        int upperRow = n.row - 1;
        if (upperRow < 0) return;
        if (n.col - 1 >= 0) checkNode(n, n.col - 1, upperRow);
        if (n.col + 1 < searchTable[0].length) checkNode(n, n.col + 1, upperRow);
        checkNode(n, n.col, upperRow);
    }

    private void checkNode(Node n, int col, int row) {
        int cost = 10;
        Node adj = searchTable[row][col];
        if (adj.isLocked || nodeSet.contains(adj)) return;
        if (!nodePriorityQueue.contains(adj)) {
            adj.setupNode(n, cost);
            nodePriorityQueue.add(adj);
        } else {
            if (adj.checkBetterPath(n, cost)) return;
            nodePriorityQueue.remove(adj);
            nodePriorityQueue.add(adj);
        }
    }
}
