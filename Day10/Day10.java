import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day10 {
    public static void main(String[] args) {
        Path path = Path.of("input.txt");
        // Path path = Path.of("sample.txt");
        String content = null;

        try {
            content = Files.readString(path);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (content != null) {
            String[] input = content.split("\n");
            part1(input);
        }
    }

    private static void part1(String[] input) {
        List<String> machineStrings = new ArrayList<>();
        List<String[]> availableButtonStrings = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            String[] splitStrings = input[i].split(" ");
            String machineString = splitStrings[0];
            machineString = machineString.substring(1, machineString.length() - 1);
            String[] availableButtonString = Arrays.copyOfRange(splitStrings, 1, splitStrings.length - 1);
            machineStrings.add(machineString);
            availableButtonStrings.add(availableButtonString);
        }

        int ans = 0;
        for (int i = 0; i < machineStrings.size(); i++) {
            ans += bfs(machineStrings.get(i), availableButtonStrings.get(i));
        }
        System.out.println(ans);
    }

    private static int bfs(String finishPoint, String[] moves) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < finishPoint.length(); i++) {
            sb.append('.');
        }
        String startPoint = sb.toString();
        char[] start = startPoint.toCharArray();
        char[] finish = finishPoint.toCharArray();

        Queue<Object[]> q = new LinkedList<>();
        q.add(new Object[] { startPoint, 0 });
        int cnt = 0;
        HashSet<String> seen = new HashSet<>();
        while (!q.isEmpty()) {
            Object[] top = q.poll();
            String cur = (String) top[0];
            int curCnt = (int) top[1];

            for (int i = 0; i < moves.length; i++) {
                char[] curArray = cur.toCharArray();
                int[] movesIndexes = getIndexesFromButtonString(moves[i]);
                for (int j = 0; j < movesIndexes.length; j++) {
                    int idx = movesIndexes[j];
                    curArray[idx] = curArray[idx] == '#' ? '.' : '#';
                }
                if (isDone(finish, curArray)) {
                    return curCnt + 1;
                }
                String next = new String(curArray);
                if (seen.contains(next)) {
                    continue;
                }
                q.add(new Object[] { next, curCnt + 1 });
            }
        }

        return cnt;
    }

    private static boolean isDone(char[] start, char[] end) {
        for (int i = 0; i < start.length; i++) {
            if (start[i] != end[i]) {
                return false;
            }
        }
        return true;
    }

    private static int[] getIndexesFromButtonString(String button) {
        String trimmedButton = button.substring(1, button.length() - 1);
        String[] splitStrings = trimmedButton.split(",");
        return Arrays.stream(splitStrings).mapToInt(Integer::parseInt).toArray();
    }
}