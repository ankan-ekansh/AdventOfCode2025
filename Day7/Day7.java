import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Day7 {
    public static void main(String[] args) {
        Path path = Path.of("input.txt");
        // Path path = Path.of("sample.txt");
        String content = null;
        try {
            content = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (content != null) {
            String[] input = content.split("\n");
            // fun(input);
            fun2(input);
        }
    }

    private static void fun(String[] input) {
        int m = input.length;
        int n = input[0].length();

        Queue<int[]> q = new LinkedList<>();
        for (int j = 0; j < n; j++) {
            if (input[0].charAt(j) == 'S') {
                q.add(new int[] { 1, j });
                break;
            }
        }
        int cnt = 0;
        boolean[][] vis = new boolean[m][n];
        while (!q.isEmpty()) {
            int[] front = q.poll();
            int i = front[0];
            int j = front[1];
            if (input[i].charAt(j) == '.') {
                if (check(m, n, i + 1, j)) {
                    q.add(new int[] { i + 1, j });
                }
            }
            else if (input[i].charAt(j) == '^') {
                if (!vis[i][j]) {
                    cnt++;
                    vis[i][j] = true;
                }
                else {
                    continue;
                }
                if (check(m, n, i, j - 1)) {
                    q.add(new int[] { i, j - 1 });
                }
                if (check(m, n, i, j + 1)) {
                    q.add(new int[] { i, j + 1 });
                }
            }
        }
        System.out.println(cnt);
    }

    private static void fun2(String[] input) {
        int m = input.length;
        int n = input[0].length();
        // long[][] count = new long[m][n];
        long[] prevcount = new long[n];
        long[] curcount = new long[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (input[i].charAt(j) == 'S') {
                    // count[i][j] = 1L;
                    prevcount[j] = 1L;
                }
            }
        }

        for (int i = 1; i < m; i++) {
            Arrays.fill(curcount, 0L);
            for (int j = 0; j < n; j++) {
                if (input[i].charAt(j) == '^') {
                    if (check(m, n, i, j - 1)) {
                        // count[i][j - 1] += count[i - 1][j];
                        curcount[j - 1] += prevcount[j];
                    }
                    if (check(m, n, i, j + 1)) {
                        // count[i][j + 1] += count[i - 1][j];
                        curcount[j + 1] += prevcount[j];
                    }
                } else {
                    // count[i][j] += count[i - 1][j];
                    curcount[j] += prevcount[j];
                }
            }
            long[] tmp = curcount;
            curcount = prevcount;
            prevcount = tmp;
        }

        long cnt = 0;
        for (int j = 0; j < n; j++) {
            // cnt += count[m - 1][j];
            cnt += prevcount[j];
        }
        System.out.println(cnt);
    }

    private static boolean check(int m, int n, int i, int j) {
        return i >= 0 && i < m && j >= 0 && j < n;
    }
}
