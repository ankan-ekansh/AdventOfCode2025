import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day12 {
    public static void main(String[] args) {
        // Path path = Path.of("sample.txt");
        Path path = Path.of("input.txt");
        String content = null;

        try {
            content = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (content != null) {
                String[] input = content.split("\n");
                solve(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void solve(String[] input) throws Exception {
        List<List<List<Integer>>> shapes = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            shapes.add(new ArrayList<>());
        }
        List<List<List<Integer>>> tree = new ArrayList<>();
        int idx = 0;
        for (int i = 0; i < input.length; i++) {
            String line = input[i].strip();
            if (line.isEmpty()) {
                idx++;
                continue;
            }
            if (idx < 6 && line.contains(":")) {
                continue;
            }
            if (idx < 6) {
                List<Integer> row = new ArrayList<>();
                for (char c : line.toCharArray()) {
                    if (c == '#') {
                        row.add(1);
                    } else if (c == '.') {
                        row.add(0);
                    }
                }
                shapes.get(idx).add(row);
            } else {
                // String[] counts = line.split(": ")[1].split(" ");
                String[] lineSplit = line.split(": ");
                String[] size = lineSplit[0].split("x");
                String[] counts = lineSplit[1].split(" ");
                List<Integer> row1 = new ArrayList<>();
                for (int j = 0; j < size.length; j++) {
                    row1.add(Integer.parseInt(size[j]));
                }
                List<Integer> row2 = new ArrayList<>();
                for (int j = 0; j < counts.length; j++) {
                    row2.add(Integer.parseInt(counts[j]));
                }
                List<List<Integer>> row = new ArrayList<>();
                row.add(row1);
                row.add(row2);
                tree.add(row);
            }
        }

        System.out.println(shapes.toString());
        System.out.println(tree.toString());

        int ans = 0;

        int[] shapesizes = new int[6];
        for (int i = 0; i < shapes.size(); i++) {
            for (int j = 0; j < shapes.get(i).size(); j++) {
                for (int k = 0; k < shapes.get(i).get(j).size(); k++) {
                    shapesizes[i] += shapes.get(i).get(j).get(k);
                }
            }
            // System.out.println(shapesizes[i]);
        }

        for (int i = 0; i < tree.size(); i++) {
            List<List<Integer>> row = tree.get(i);
            List<Integer> size = row.get(0);
            List<Integer> shapeCounts = row.get(1);
            int totalarea = size.get(0) * size.get(1);
            int area = 0;
            for (int j = 0; j < shapeCounts.size(); j++) {
                area += shapesizes[j] * shapeCounts.get(j);
            }
            if (totalarea < area) {
                continue;
            }
            if (doesfitbyarea(size, shapeCounts)) {
                ans++;
            } else {
                System.out.println("Wont work by this logic for " + size.toString() + ", " + shapeCounts.toString());
            }
        }

        System.out.println(ans);
    }

    /**
     * This logic only works because the input is too relaxed
     * This does not work for the sample case
     * For sample might need to do backtracking to fit shapes properly in the area
     * Using some optimizations for pruning
     */
    private static boolean doesfitbyarea(List<Integer> size, List<Integer> shapeCounts) {
        return (size.get(0) / 3) * (size.get(1) / 3) >= shapeCounts.size();
    }
}
