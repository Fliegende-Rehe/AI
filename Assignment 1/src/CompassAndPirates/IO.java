package CompassAndPirates;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class IO {
    private final static String[] characters = {"Jack", "Davy", "Kraken", "Rock", "Chest", "Tortuga"};

    private static Map<String, ArrayList<Node>> positions;

    public static Input[] GenerateMap(int num, int scenario) {
        Input[] inputs = new Input[num];
        positions = new HashMap<>();
        for (int i = 0; i < num; i++, positions = new HashMap<>()) {
            int row = 0, col = 0;
            for (String name : characters) {
                positions.put(name, new ArrayList<>(List.of(new Node(row, col))));
                while (nonColliding(row, true)) row = getRandom(0, 8);
                while (nonColliding(col, false)) col = getRandom(0, 8);
            }
            setPerceptionZones();
            inputs[i] = new Input(positions, scenario);
        }
        return inputs;
    }

    public static int getRandom(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    private static boolean nonColliding(int x, boolean isRow) {
        boolean isRock = getLastEntry(positions).getKey().equals("Kraken");
        for (String name : positions.keySet()) {
            Node n = positions.get(name).get(0);
            if (Math.abs((isRow ? n.row : n.col) - x) < 1) {
                if (isRock && name.equals("Kraken")) continue;
                return true;
            }
        }
        return false;
    }

    private static Map.Entry<String, ArrayList<Node>> getLastEntry(Map<String, ArrayList<Node>> map) {
        return map.entrySet().stream().reduce((first, second) -> second).orElse(null);
    }

    public static Input Read() {
        positions = new HashMap<>();
        int scenario = 0;
        try {
            BufferedReader buffer = new BufferedReader(new FileReader("input.txt"));
            String line = buffer.readLine();
            for (int i = 1, k = 0, row, col; i < line.length(); i += 6, k++) {
                row = Character.getNumericValue(line.charAt(i));
                col = Character.getNumericValue(line.charAt(i + 2));
                positions.put(characters[k], new ArrayList<>(List.of(new Node(row, col))));
            }
            setPerceptionZones();
            scenario = Integer.parseInt(buffer.readLine());
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Input(positions, scenario);
    }

    private static void setPerceptionZones() {
        for (String key : positions.keySet()) {
            ArrayList<Node> cells = positions.get(key);
            int row = cells.get(0).row, col = cells.get(0).col;
            if (key.equals("Davy")) {
                for (int i = -1; i < 2; i++)
                    for (int j = -1; j < 2; j++)
                        if (i != 0 || j != 0)
                            cells.add(new Node(row + i, col + j));
            }
            if (key.equals("Kraken")) {
                for (int i = -1; i < 2; i++)
                    for (int j = -1; j < 2; j++) {
                        if (Math.abs(i) != Math.abs(j)) cells.add(0, new Node(row + i, col + j));
                        else if(i != 0) cells.add(new Node(row + i, col + j));
                    }
            }
        }
    }

    public static void Write(Output[] outputs) {
        for (Output output : outputs)
            Write(output);
    }

    public static void Write(Output output) {
        FileWriter writer;
        try {
            writer = new FileWriter("output" + output.filename + ".txt", false);
            StringBuilder result = new StringBuilder();
            result.append(output.path != null ? "Win" : "Lose").append("\n");
            if (output.path != null) outputBuilder(output, result);
            System.out.print("\n" + result + "\n");
            writer.write(result.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void outputBuilder(Output output, StringBuilder result) {
        result.append(output.path.size() - 1).append("\n");
        for (Node n : output.path)
            result.append(n).append(output.path.indexOf(n) != output.path.size() - 1 ? " " : "\n");
        result.append("-------------------\n");
        for (int i = 0; i < 10; i++) {
            if (i == 0) result.append("  ");
            else result.append(i - 1).append(" ");
            for (int j = 1; j < 10; j++) {
                if (i != 0) {
                    boolean starred = false;
                    for (Node n : output.path) {
                        if (i - 1 != n.row || j - 1 != n.col) continue;
                        result.append("*");
                        starred = true;
                        break;
                    }
                    if (!starred) result.append("-");
                } else result.append(j - 1);
                if (j != 9) result.append(" ");
            }
            result.append("\n");
        }
        result.append("-------------------\n").append(output.time).append(" ms\n");
    }

    public static class Input {
        public Map<String, ArrayList<Node>> positions;
        public int scenario;

        public Input(Map<String, ArrayList<Node>> positions, int scenario) {
            this.positions = positions;
            this.scenario = scenario;
        }
    }

    public static class Output {
        public String filename;
        public List<Node> path;
        public double time;

        public Output(String filename, List<Node> sequence, double time) {
            this.filename = filename;
            this.path = sequence;
            this.time = time;
        }
    }
}
