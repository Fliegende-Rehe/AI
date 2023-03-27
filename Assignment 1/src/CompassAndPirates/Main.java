package CompassAndPirates;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
//        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
//        System.out.print("Choose input method (1 or 2):\n1. read from input.txt\n2. use generated maps\n> ");
//        if (Integer.parseInt(stdin.readLine()) == 1) readFile();
//        else readGeneratedMaps(stdin);
        readFile();
    }

    private static void readFile() {
        IO.Input input = IO.Read();
        Search search = new Search(input);
        IO.Output[] outputs = search.viaBoth();
        IO.Write(outputs);
    }

    private static void readGeneratedMaps(BufferedReader stdin) throws IOException {
        System.out.print("\nChoose algorithm (1 or 2):\n1. Backtracking\n2. AStar\n>  ");
        int algorithm = Integer.parseInt(stdin.readLine());
        System.out.print("\nChoose scenario (1 or 2):\n> ");
        int scenario = Integer.parseInt(stdin.readLine());
        int mapCount = 1000;
        IO.Input[] inputs = IO.GenerateMap(mapCount, scenario);
        ArrayList<IO.Output> outputs = new ArrayList<>();
        for (IO.Input input : inputs) {
            Search search = new Search(input);
            IO.Output result = algorithm == 1 ? search.viaBacktracking() : search.viaAStar();
            outputs.add(result);
        }
        System.out.println("\n" + (algorithm == 1 ? "Backtracking" : "AStar") + "(variant " + scenario + ")" + ":");
        Analysis.getData(outputs);
    }
}
