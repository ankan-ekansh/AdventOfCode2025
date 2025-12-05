import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day1 {
    public static void main(String[] args) {
        Path path = Path.of("input.txt");
        // Path path = Path.of("sampleinput.txt");
        // Path path = Path.of("sampleinput2.txt");
        String content = null;
        try {
            content = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (content != null) {
            String[] input = content.split("\n");
            // System.out.println(content);
            // System.out.println(input.length);
            // System.out.println(input[0]);
            // String[] input = { "L33" };
            int ans = solve2(input);
            System.out.println(ans);
            // input[0] = "L66";
            // ans = solve(input);
            // System.out.println(ans);
            // input[0] = "R33";
            // System.out.println(ans);
            // input[0] = "R66";
            // System.out.println(ans);
        }
    }

    private static int solve(String[] input) {
        int n = input.length;
        int pos = 50;
        int size = 100;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            char directionChar = input[i].charAt(0);
            int direction = directionChar == 'R' ? 1 : -1;
            int steps = Integer.parseInt(input[i].substring(1));
            pos = (pos + direction * steps + size) % size;
            if (pos == 0)
                ans++;
        }
        return ans;
    }

    private static int solve2(String[] input) {
        int n = input.length;
        int pos = 50;
        int size = 100;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            char directionChar = input[i].charAt(0);
            int direction = directionChar == 'R' ? 1 : -1;
            int steps = Integer.parseInt(input[i].substring(1));
            int oldpos = pos;
            int reps = steps / size;
            pos = pos + direction * (steps % 100);
            if (pos >= size || pos <= 0) {
                // ans++;
                if (oldpos != 0) {
                    ans++;
                }
                pos = (pos + size) % size;
            }
            ans += reps;
        }
        return ans;
    }
}