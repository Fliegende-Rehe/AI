package CompassAndPirates;

import java.util.ArrayList;
import java.util.List;

public class Backtracking extends Search {

    private Node[][] solutionsTable;
    private List<Node> path;
    private double t;

    public Backtracking(IO.Input data) {
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
        t = System.nanoTime();
        setRoute(initialNode, targetNode);
        searchTable = createTable();
        path = new ArrayList<>();
        solutionsTable = createTable(true);
        if (visit(init.row, init.col)) return path;
        return null;
    }

    private boolean visit(int row, int col) {
        if (getTime(t) > maxTime) return false;
        if (!canVisit(row, col)) return false;
        Node n = solutionsTable[row][col];
        lookAround(n);
        n.isLocked = false;
        path.add(n);
        if (n.equals(target)) return true;
        int x = IO.getRandom(-1, 1), y = IO.getRandom(-1, 1);
        for (int i = x; i < 2; i++)
            for (int j = y; j < 2; j++)
                if (visit(row + i, col + j))
                    return true;
        n.isLocked = true;
        path.remove(path.size() - 1);
        return false;
    }

    private boolean canVisit(int x, int y) {
        return isSafe(x, y) && !searchTable[x][y].isLocked && solutionsTable[x][y].isLocked;
    }
}
