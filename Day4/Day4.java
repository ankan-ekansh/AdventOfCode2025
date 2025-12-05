import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Day4 {
    public static void main(String[] args) {
        Path path = Path.of("input.txt");
        // Path path = Path.of("sample.txt");
        String content = null;
        try {
            content = Files.readString(path);
            // System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (content != null) {
            String[] input = content.split("\n");
            fun(input);
        }
    }

    private static void fun(String[] input) {
        int m = input.length;
        int n = input[0].length();
        int[][] neicount = new int[m][n];
        Queue<int[]> q = new LinkedList<>();
        boolean[][] vis = new boolean[m][n];

        int cnt = 0;
        int[] di = { -1, 0, 1, 0, -1, 1, 1, -1 };
        int[] dj = { 0, -1, 0, 1, -1, 1, -1, 1 };
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (input[i].charAt(j) == '.') {
                    continue;
                }
                int nei = 0;
                for (int k = 0; k < 8; k++) {
                    int ni = i + di[k];
                    int nj = j + dj[k];
                    // System.out.println("Nei of i=" + i + ", j=" + j + " at ni=" + ni + ", nj=" + nj);
                    if (check(m, n, ni, nj) && input[ni].charAt(nj) == '@') {
                        // System.out.println("Nei of i=" + i + ", j=" + j + " at ni=" + ni + ", nj=" + nj);
                        nei++;
                    }
                }
                neicount[i][j] = nei;
                if (nei < 4) {
                    // System.out.println("i = " + i + ", j = " + j + ", nei = " + nei + ", cnt = " + cnt);
                    q.add(new int[] { i, j });
                    vis[i][j] = true;
                    cnt++;
                }
            }
        }
        while (!q.isEmpty()) {
            int[] front = q.poll();
            int i = front[0];
            int j = front[1];

            for (int k = 0; k < 8; k++) {
                int ni = i + di[k];
                int nj = j + dj[k];

                if (check(m, n, ni, nj) && input[ni].charAt(nj) == '@' && vis[ni][nj] == false) {
                    neicount[ni][nj]--;
                    if (neicount[ni][nj] < 4) {
                        vis[ni][nj] = true;
                        q.add(new int[] { ni, nj });
                        cnt++;
                    }
                }
            }
        }

        // for (int i = 0; i < m; i++) {
        //     for (int j = 0; j < n; j++) {
        //         if (input[i].charAt(j) == '@' && neicount[i][j] < 4) {

        //         }
        //     }
        // }

        System.out.println(cnt);
    }

    private static boolean check(int m, int n, int i, int j) {
        return i >= 0 && i < m && j >= 0 && j < n;
    }
}