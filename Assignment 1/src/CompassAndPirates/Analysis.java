package CompassAndPirates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Analysis {
    private static final ArrayList<Double> seq = new ArrayList<>();
    private static double total = 0;
    private static int amt;
    private static double mean;

    public static void getData(ArrayList<IO.Output> outputs) {
        amt = outputs.size();
        int wins = 0, loses = 0;
        for (IO.Output output : outputs) {
            total += output.time;
            seq.add(output.time);
            if (output.path == null) loses++;
            else wins++;
        }

        System.out.printf("Wins: %d (%.1f%%)\n", wins, getRate(wins));
        System.out.printf("Loses: %d (%.1f%%)\n", loses, getRate(loses));

        printMean();
        printMode();
        printMedian();
        printDeviation();
    }

    private static double getRate(int count) {
        return ((double) count * 100.0 / (double) amt);
    }

    private static void printMean() {
        mean = total / (double) amt;
        System.out.printf("Mean: %.4f ms\n", mean);
    }

    private static void printMode() {
        Map<Double, Integer> counter = new HashMap<>();
        for (double t : seq) {
            if (counter.containsKey(t)) counter.put(t, counter.get(t) + 1);
            else counter.put(t, 0);
        }
        System.out.printf("Mode: %.4f ms\n", getLastEntry(sortMap(counter)).getKey());
    }

    private static Stream<Map.Entry<Double, Integer>> sortMap(Map<Double, Integer> map) {
        return map.entrySet().stream().sorted(Map.Entry.comparingByValue());
    }

    private static Map.Entry<Double, Integer> getLastEntry(Stream<Map.Entry<Double, Integer>> stream) {
        return stream.reduce((first, second) -> second).orElse(null);
    }

    private static void printMedian() {
        Collections.sort(seq);
        int index1 = seq.size() / 2, index2;
        double median;
        if (seq.size() % 2 != 0) {
            index2 = index1 + 1;
            median = (seq.get(index1) + seq.get(index2)) / 2.0;
        } else median = seq.get(index1);
        System.out.printf("Median: %.4f ms\n", median);
    }

    private static void printDeviation() {
        System.out.printf("Standard deviation: %.4f ms\n", Math.sqrt(getVariance()));
    }

    private static double getVariance() {
        ArrayList<Double> squaredDifference = new ArrayList<>();
        for (double t : seq) squaredDifference.add(Math.pow(t - mean, 2));
        return listSum(squaredDifference) / amt;
    }

    private static double listSum(ArrayList<Double> list) {
        return list.stream().mapToDouble(a -> a).sum();
    }
}
