import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day2 {
    public static void main(String[] args) {
        Path path = Path.of("input.txt");
        // Path path = Path.of("sample.txt");
        String content = null;
        try {
            content = Files.readString(path);
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (content != null) {
            String[] input = content.split(",");
            // int ans = solve(input);
            fun(input);
            // System.out.println(ans);
            // System.out.println(List.of(input).toString());
        }
    }

    private static void fun(String[] input) {
        // return solve(38593856,38593862);
        // int ans = 0;
        BigInteger ans = BigInteger.valueOf(0);
        for (int i = 0; i < input.length; i++) {
            String[] nums = input[i].split("-");
            long x = Long.parseLong(nums[0]);
            long y = Long.parseLong(nums[1]);
            // ans += solve(x, y);
            // ans = ans.add(solve(x, y));
            ans = ans.add(solve2(x, y));
        }
        // return ans;
        System.out.println(ans.toString());
    }

    private static BigInteger solve(long x, long y) {
        // long cnt = 0;
        // long sum = 0;
        BigInteger sum = BigInteger.valueOf(0);
        for (long i = x; i <= y; i++) {
            String s = Long.toString(i);
            int l = s.length();
            if (l % 2 != 0) {
                continue;
            }
            String a = s.substring(0, l / 2);
            String b = s.substring(l / 2);
            if (!a.equals(b)) {
                continue;
            }
            // cnt++;
            // sum += i;
            sum = sum.add(BigInteger.valueOf(i));
        }
        // return cnt;
        return sum;
    }

    private static BigInteger solve2(long x, long y) {
        BigInteger sum = BigInteger.valueOf(0);
        for (long i = x; i <= y; i++) {
            String s = Long.toString(i);
            if (!check(s)) {
                continue;
            }
            sum = sum.add(BigInteger.valueOf(i));
        }
        return sum;
    }

    private static boolean check(String s) {
        int L = s.length();
        int curstart = 0;
        boolean any = false;
        for (int l = 1; l <= L / 2; l++) {
            String a = s.substring(curstart, curstart + l);
            boolean match = true;
            for (int i = curstart + l; i < L; i += l) {
                String b = s.substring(i, Math.min(L, i + l));
                if (!a.equals(b)) {
                    match = false;
                    break;
                }
            }
            if (match && !any) {
                any = true;
            }
        }
        return any;
    }
}