package CompassAndPirates;

public class Node {
    public final int row, col;
    public int g, f, h;
    public boolean isLocked;
    public Node parent;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void setH(Node targetNode) {
        h = Math.abs(targetNode.row - row) + Math.abs(targetNode.col - col);
    }

    public void setupNode(Node n, int cost) {
        parent = n;
        g = n.g + cost;
        f = g + h;
    }

    public boolean checkBetterPath(Node n, int cost) {
        if (n.g + cost >= g) return false;
        setupNode(n, cost);
        return true;
    }

    @Override
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof Node other)) return false;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public String toString() {
        return "[" + row + "," + col + "]";
    }

    public int getF() {
        return f;
    }
}
