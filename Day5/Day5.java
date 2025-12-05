import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class Day5 {
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
            fun3(input);
        }
    }

    private static void fun(String[] input) {
        int i = 0;
        for (i = 0; i < input.length; i++) {
            if (input[i].equals("")) {
                break;
            }
            // System.out.println(input[i]);
        }
        // System.out.println(i);
        // i++;
        int j = i + 1;
        for (; j < input.length; j++) {
            // System.out.println(input[j]);
        }
        // System.out.println(j - i);

        System.out.println("i = " + i + ", j = " + j);
        int cnt = 0;
        for (int J = i + 1; J < input.length; J++) {
            for (int I = 0; I < i; I++) {
                String intervalString = input[I];
                String[] interval = intervalString.split("-");
                long L = Long.parseLong(interval[0]);
                long R = Long.parseLong(interval[1]);

                long id = Long.parseLong(input[J]);

                // System.out.println("L = " + L + ", R = " + R + ", id = " + id);
                if (id < L) {
                    continue;
                }
                if (id >= L && id <= R) {
                    cnt++;
                    break;
                }
                // if (id > R) {
                //     break;
                // }
            }
        }
        System.out.println(cnt);
    }

    private static void fun2(String[] input) {
        int i = 0;
        for (i = 0; i < input.length; i++) {
            if (input[i].equals("")) {
                break;
            }
        }

        System.out.println("i = " + i);
        // int cnt = 0;
        BigInteger cnt = BigInteger.valueOf(0);

        Set<Long> set = new TreeSet<>();
        Map<Long, Long> map = new HashMap<>();
        for (int j = 0; j < i; j++) {
            String intervalString = input[j];
            String[] interval = intervalString.split("-");
            long L = Long.parseLong(interval[0]);
            long R = Long.parseLong(interval[1]);
            set.add(L);
            set.add(R);
            set.add(R + 1);
            map.put(L, 1L);
            map.put(R, 0L);
            map.put(R + 1, -1L);
        }

        long prev = -1;
        long cumsum = 0;
        for (long e : set) {
            cumsum += map.get(e);
            if (cumsum > 0) {
                if (prev == -1L) {
                    // cnt += 1L;
                    cnt = cnt.add(BigInteger.valueOf(1));
                } else {
                    // cnt += e - prev;
                    if (e - prev <= 0) {
                        System.out.println("!!!");
                    }
                    cnt = cnt.add(BigInteger.valueOf(e).subtract(BigInteger.valueOf(prev)));
                }
                prev = e;
            } else {
                // System.out.println(e-1 - prev);
                BigInteger tmp = BigInteger.valueOf(e).subtract(BigInteger.valueOf(1));
                tmp = tmp.subtract(BigInteger.valueOf(prev));
                if (prev != -1)
                    cnt = cnt.add(tmp);
                prev = -1;
            }
            // System.out.println("e = " + e + ", cumsum = " + cumsum + ", cnt = " + cnt);
        }
        System.out.println(cnt);
    }

    private static void fun3(String[] input) {
        int i = 0;
        for (i = 0; i < input.length; i++) {
            if (input[i].equals("")) {
                break;
            }
        }

        System.out.println("i = " + i);

        BigInteger cnt = BigInteger.valueOf(0);
        List<long[]> intervals = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            String intervalString = input[j];
            String[] interval = intervalString.split("-");
            long L = Long.parseLong(interval[0]);
            long R = Long.parseLong(interval[1]);
            intervals.add(new long[] { L, R });
        }

        Collections.sort(intervals, (a, b) -> {
            return a[0] != b[0] ? Long.compare(a[0], b[0]) : Long.compare(a[1], b[1]);
        });
        List<long[]> mergedintervals = new ArrayList<>();
        for (long[] interval : intervals) {
            if (mergedintervals.isEmpty()) {
                mergedintervals.add(interval);
            } else {
                long[] prevInterval = mergedintervals.get(mergedintervals.size() - 1);
                long prevL = prevInterval[0];
                long prevR = prevInterval[1];
                long L = interval[0];
                long R = interval[1];
                if (prevR >= L) {
                    prevInterval[1] = Math.max(prevR, R);
                } else {
                    mergedintervals.add(new long[] { L, R });
                }
            }
        }
        for (long[] interval : mergedintervals) {
            System.out.println(interval[0] + "," + interval[1]);
            BigInteger tmp = BigInteger.valueOf(interval[1]).subtract(BigInteger.valueOf(interval[0]))
                    .add(BigInteger.valueOf(1));
            cnt = cnt.add(tmp);
        }
        System.out.println(cnt);
    }
}