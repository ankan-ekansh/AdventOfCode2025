package com.adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

public class Day10 {
    public static void main(String[] args) {
        // Path path = Path.of("./src/main/java/com/adventofcode/input.txt");
        Path path = Path.of("./src/main/java/com/adventofcode/sample.txt");
        String content = null;

        try {
            content = Files.readString(path);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (content != null) {
            String[] input = content.split("\n");
            part2(input);
        }
    }

    /**
     * Using bfs for part 2 takes lot of time to run since there are a lot more states to explore.
     * Instead, it can be represented as a system of linear equations where the variables are the number of times each button is pressed.
     */
    private static void part2(String[] input) {
        // List<String> machineStrings = new ArrayList<>();
        List<String[]> availableButtonStrings = new ArrayList<>();
        List<String> joltageStrings = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            String[] splitStrings = input[i].split(" ");
            String joltageString = joltageString(splitStrings[splitStrings.length - 1]);
            joltageStrings.add(joltageString);
            String[] availableButtonString = Arrays.copyOfRange(splitStrings, 1, splitStrings.length - 1);
            availableButtonStrings.add(availableButtonString);
        }

        int ans = 0;
        // BFS Takes forever to run, can do smart pruning to reduce time maybe?
        // for (int i = 0; i < joltageStrings.size(); i++) {
        //     ans += bfs2(joltageStrings.get(i), availableButtonStrings.get(i));
        // }
        for (int i = 0; i < joltageStrings.size(); i++) {
            ans += solveIntegerLinearProgramILP(joltageStrings.get(i), availableButtonStrings.get(i));
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

    private static int bfs2(String finishPoint, String[] moves) {
        StringBuilder sb = new StringBuilder();
        int len = finishPoint.split(",").length;
        for (int i = 0; i < len; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append('0');
        }
        String startPoint = sb.toString();
        char[] start = startPoint.toCharArray();
        char[] finish = finishPoint.toCharArray();

        Queue<Object[]> q = new LinkedList<>();
        q.add(new Object[] { startPoint, 0 });
        int cnt = 0;
        HashSet<String> seen = new HashSet<>();
        seen.add(startPoint);
        while (!q.isEmpty()) {
            Object[] top = q.poll();
            String cur = (String) top[0];
            int curCnt = (int) top[1];

            for (int i = 0; i < moves.length; i++) {
                int[] movesIndexes = getIndexesFromButtonString(moves[i]);
                String next = increment(cur, movesIndexes);
                if (next.equals(finishPoint)) {
                    return curCnt + 1;
                }
                if (seen.contains(next)) {
                    continue;
                }
                seen.add(next);
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

    private static String joltageString(String joltage) {
        String trimmedJolString = joltage.substring(1, joltage.length() - 1);
        return trimmedJolString;
    }

    private static String increment(String joltageString, int[] indexes) {
        StringBuilder sb = new StringBuilder();
        String[] splitString = joltageString.split(",");
        int[] nums = Arrays.stream(splitString).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < indexes.length; i++) {
            int j = indexes[i];
            nums[j]++;
        }
        for (int i = 0; i < nums.length; i++) {
            if (i != 0) {
                sb.append(',');
            }
            sb.append(nums[i]);
        }
        return sb.toString();
    }

    /**
     * Using z3 library for solving Integer linear programming
     * Basically it is a set of linear equations and we want to find the minimum value satisfying the set of equations
     * Can be done with gaussian elimination but too tedious so better to use a library
     * Z3 library documentation was very obscure and did not seem intuitive, used Claude to model the behavior as per the requirement
     */
    private static int solveIntegerLinearProgramILP(String targetString, String[] buttons) {
        int[] target = Arrays.stream(targetString.split(",")).mapToInt(Integer::parseInt).toArray();
        int n = target.length;
        int m = buttons.length;
        int[][] instructions = new int[m][];
        for (int i = 0; i < buttons.length; i++) {
            instructions[i] = getIndexesFromButtonString(buttons[i]);
        }

        HashMap<String, String> cfg = new HashMap<>();
        cfg.put("model", "true");
        try (Context ctx = new Context(cfg)) {
            Optimize opt = ctx.mkOptimize();

            // create integer variables x0..x_{m-1}
            IntExpr[] x = new IntExpr[m];
            for (int i = 0; i < m; i++) {
                x[i] = ctx.mkIntConst("x" + i);
                opt.Add(ctx.mkGe(x[i], ctx.mkInt(0)));
            }

            // per-position constraints
            // for each position j, sum_{i: j in instructions[i]} x[i] == target[j]
            for (int j = 0; j < n; j++) {
                ArithExpr sum = ctx.mkInt(0);
                for (int i = 0; i < m; i++) {
                    for (int idx : instructions[i]) {
                        if (idx == j) {
                            sum = (ArithExpr) ctx.mkAdd(sum, x[i]);
                            break;
                        }
                    }
                }
                opt.Add(ctx.mkEq(sum, ctx.mkInt(target[j])));
            }

            // objective: minimize total sum of xi
            ArithExpr total = ctx.mkInt(0);
            for (int i = 0; i < m; i++) {
                total = (ArithExpr) ctx.mkAdd(total, x[i]);
            }
            Optimize.Handle h = opt.MkMinimize(total);

            // solve
            if (opt.Check() == Status.SATISFIABLE) {
                Model model = opt.getModel();
                var minTotal = model.evaluate(total, true);
                int min = Integer.parseInt(minTotal.toString());
                // System.out.println("Min total = " + min);
                // for (int i = 0; i < m; i++) {
                //     System.out.println("x" + i + " = " + model.evaluate(x[i], true));
                // }
                return min;
            }
        }
        catch (Z3Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}