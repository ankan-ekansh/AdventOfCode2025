import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Stack;

public class Day3 {
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
            // int ans = solve(input);
            fun(input);
            // System.out.println(ans);
            // System.out.println(List.of(input).toString());
        }
    }

    private static void fun(String[] input) {
        // System.out.println(input.length);
        long ans = 0;
        // BigInteger ans = BigInteger.valueOf(0);
        for (int i = 0; i < input.length; i++) {
            // System.out.println(input[0].length());
            // int mxm = 0;
            // for (int j = 0; j < input[i].length(); j++) {
            //     for (int k = j + 1; k < input[i].length(); k++) {
            //         int a = input[i].charAt(j) - '0';
            //         int b = input[i].charAt(k) - '0';
            //         mxm = Math.max(mxm, a * 10 + b);
            //     }
            // }
            // long[] max = new long[1];
            // getmax(input[i], 0, 0, 0, max);
            // ans += mxm;
            // ans += max[0];
            // ans += getmax(input[i]);
            // long mxm = getmax(input[i]);
            // System.out.println(mxm);
            // ans += mxm;
            // break;
            // ans = ans.add(getmax(input[i]));
            // ans += solve(input[i]);
            long mxm = solve(input[i]);
            // System.out.println(mxm);
            ans += mxm;
        }
        System.out.println(ans);
    }

    private static long solve(String s) {
        Stack<Long> st = new Stack<>();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            long x = s.charAt(i) - '0';
            // System.out.println(x + ", " + (!st.isEmpty() && st.peek() < x && (n - i + 1) > 12 - st.size()) + " , "
            //         + (st.isEmpty() || st.peek() >= x || st.size() < 12));
            while (!st.isEmpty() && st.peek() < x && (n - i) > 12 - st.size()) {
                // System.out.print(" pop " + st.peek());
                st.pop();
            }
            // System.out.println(st.toString());
            // System.out.println(x + ", " + (!st.isEmpty() && st.peek() < x && (n - i + 1) > 12 - st.size()) + " , "
            //         + (st.isEmpty() || st.peek() >= x || st.size() < 12));
            if (st.isEmpty() || st.peek() >= x || st.size() < 12) {
                // System.out.print(" push " + x);
                st.push(x);
            }
        }
        while (st.size() > 12) {
            st.pop();
        }
        // System.out.println();
        long p = 1;
        long cnt = 0;
        // System.out.println(st.toString());
        // System.out.println(st.size());
        while (!st.isEmpty()) {
            // cnt = cnt * p + st.peek();
            cnt = st.peek() * p + cnt;
            // System.out.print(st.peek() + " " + cnt);
            st.pop();
            p *= 10;
        }
        // System.out.println();
        return cnt;
    }

    private static BigInteger getmax(String s) {
        int n = s.length();
        boolean[] taken = new boolean[n];
        Arrays.fill(taken, true);
        for (int i = 0; i < n - 12; i++) {
            taken[i] = false;
        }
        // for (int i = 0; i < n; i++) {
        //     System.out.print(taken[i] + " ");
        // }
        // System.out.println();
        int leftmost = -1;
        for (int i = n - 12; i < n; i++) {
            int a = s.charAt(i) - '0';
            // if (taken[i] == false) {
            //     continue;
            // }
            int mxm = a;
            int idx = i;
            for (int j = i; j >= 0 && j > leftmost; j--) {
                int b = s.charAt(j) - '0';
                if (b >= mxm && taken[j] == false) {
                    // if (leftmost == -1) {
                    //     leftmost = j;
                    //     taken[j] = true;
                    //     taken[i] = false;
                    // }
                    mxm = Math.max(mxm, b);
                    idx = j;
                }
            }
            if (mxm > a && idx != i) {
                // System.out.print("i = " + i + ", idx = " + idx + ", leftmost = " + leftmost + ", mxm = " + mxm);
                // System.out.println();
                if (leftmost == -1) {
                    leftmost = idx;
                }
                taken[idx] = true;
                taken[i] = false;
            }
        }

        // long sum = 0;
        BigInteger sum = BigInteger.valueOf(0);
        for (int i = 0; i < n; i++) {
            long x = s.charAt(i) - '0';
            BigInteger xx = BigInteger.valueOf(x);
            if (taken[i]) {
                // sum = sum * 10 + x;
                sum = (sum.multiply(BigInteger.valueOf(10))).add(xx);
            }
        }

        return sum;
    }

    // private static long getmax(String s) {
    //     int n = s.length();
    //     boolean[] taken = new boolean[n];
    //     Arrays.fill(taken, true);
    //     for (int i = 0; i < n - 12; i++) {
    //         taken[i] = false;
    //     }
    //     // for (int i = 0; i < n; i++) {
    //     //     System.out.print(taken[i] + " ");
    //     // }
    //     // System.out.println();
    //     int leftmost = -1;
    //     for (int i = n - 12; i < n; i++) {
    //         int a = s.charAt(i) - '0';
    //         // if (taken[i] == false) {
    //         //     continue;
    //         // }
    //         int mxm = a;
    //         int idx = i;
    //         for (int j = i; j >= 0 && j > leftmost; j--) {
    //             int b = s.charAt(j) - '0';
    //             if (b >= mxm && taken[j] == false) {
    //                 // if (leftmost == -1) {
    //                 //     leftmost = j;
    //                 //     taken[j] = true;
    //                 //     taken[i] = false;
    //                 // }
    //                 mxm = Math.max(mxm, b);
    //                 idx = j;
    //             }
    //         }
    //         if (mxm > a && idx != i) {
    //             // System.out.print("i = " + i + ", idx = " + idx + ", leftmost = " + leftmost + ", mxm = " + mxm);
    //             // System.out.println();
    //             if (leftmost == -1) {
    //                 leftmost = idx;
    //             }
    //             taken[idx] = true;
    //             taken[i] = false;
    //         }
    //     }

    //     long sum = 0;
    //     for (int i = 0; i < n; i++) {
    //         long x = s.charAt(i) - '0';
    //         if (taken[i]) {
    //             sum = sum * 10 + x;
    //         }
    //     }

    //     return sum;
    // }

    private static void getmax(String s, int i, int cnt, long sum, long[] max) {
        if (i == s.length()) {
            if (cnt == 12) {
                max[0] = Math.max(max[0], sum);
                return;
            }
            return;
        }
        if (cnt == 12) {
            max[0] = Math.max(max[0], sum);
            return;
        }
        if (cnt > 12) {
            return;
        }

        long x = s.charAt(i) - '0';
        getmax(s, i + 1, cnt + 1, sum * 10 + x, max);
        getmax(s, i + 1, cnt, sum, max);
    }
}