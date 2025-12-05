import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.IntStream;

public class Day6 {
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
            // fun(input);
            fun2(input);
        }
    }

    private static void fun(String[] input) {
        // System.out.println(input.length);
        // List<List<Integer>> numsList = new ArrayList<>();
        List<List<BigInteger>> numsList = new ArrayList<>();
        for (int i = 0; i < input.length - 1; i++) {
            // System.out.println(input[i]);
            String[] row = input[i].trim().split("\\s+");
            // List<Integer> rowlist = new ArrayList<>();
            List<BigInteger> rowlist = new ArrayList<>();
            for (int j = 0; j < row.length; j++) {
                // System.out.print(row[j] + ",");
                // rowlist.add(Integer.parseInt(row[j]));
                rowlist.add(new BigInteger(row[j]));
            }
            numsList.add(rowlist);
            // System.out.println(rowlist.toString());
            // System.out.println();
            // System.out.println("-------");
        }

        List<String> opList = new ArrayList<>();
        String[] row = input[input.length - 1].trim().split("\\s+");
        for (int i = 0; i < row.length; i++) {
            opList.add(row[i]);
        }
        // System.out.println(opList.toString());

        // ArrayList<Integer> resList = new ArrayList<>();
        ArrayList<BigInteger> resList = new ArrayList<>();
        for (int i = 0; i < opList.size(); i++) {
            resList.add(numsList.get(0).get(i));
        }

        for (int i = 1; i < numsList.size(); i++) {
            for (int j = 0; j < opList.size(); j++) {
                resList.set(j, calculate(resList.get(j), numsList.get(i).get(j), opList.get(j)));
            }
        }
        System.out.println(resList.toString());
        // System.out.println(resList.stream().mapToInt(Integer::intValue).sum());
        System.out.println(resList.stream().reduce(BigInteger.ZERO, BigInteger::add));
    }

    private static void fun2(String[] input) {
        // List<String> grid = new ArrayList<>();
        char[][] grid = new char[input.length][input[0].length()];
        for (int i = 0; i < input.length - 1; i++) {
            for (int j = 0; j < input[i].length(); j++) {
                if (input[i].charAt(j) == ' ') {
                    grid[i][j] = '0';
                } else {
                    grid[i][j] = input[i].charAt(j);
                }
            }
        }
        for (int j = 0; j < input[input.length - 1].length(); j++) {
            if (input[input.length - 1].charAt(j) == ' ') {
                grid[input.length - 1][j] = grid[input.length - 1][j - 1];
            } else {
                grid[input.length - 1][j] = input[input.length - 1].charAt(j);
            }
        }

        // for (int i = 0; i < input.length; i++) {
        //     for (int j = 0; j < input[i].length(); j++) {
        //         System.out.print(grid[i][j]);
        //     }
        //     System.out.println();
        // }

        BigInteger sum = BigInteger.valueOf(0);
        List<BigInteger> resList = new ArrayList<>();
        // List<BigInteger> numList = new ArrayList<>();
        Stack<BigInteger> opStack = new Stack<>();
        for (int j = input[0].length() - 1; j >= 0; j--) {
            BigInteger num = BigInteger.valueOf(0);
            BigInteger res = BigInteger.valueOf(1);
            for (int i = 0; i < input.length - 1; i++) {
                if (input[i].charAt(j) == ' ') {
                    continue;
                }
                num = num.multiply(BigInteger.valueOf(10)).add(BigInteger.valueOf(grid[i][j] - '0'));
            }
            // res = calculate(res, num, grid[input.length - 1][j]);
            // if (input[input.length - 1].charAt(j) != ' ' && !Character.isDigit(input[input.length - 1].charAt(j))) {
            if (input[input.length - 1].charAt(j) != ' ') {
                opStack.add(num);
                // System.out.println(num);
                if (input[input.length - 1].charAt(j) == '+' || input[input.length - 1].charAt(j) == '-') {
                    res = BigInteger.valueOf(0);
                }
                // System.out.println("stack = " + opStack.toString());
                while (!opStack.isEmpty()) {
                    res = calculate(res, opStack.pop(), input[input.length - 1].charAt(j));
                }
                resList.add(res);
                j--;
                continue;
            }
            opStack.add(num);
            // System.out.println(num);
            // numList.add(num);
            // System.out.println(res);
            // resList.add(res);
            // sum = sum.add(res);
        }

        // System.out.println(sum);
        // System.out.println(resList.toString());
        // System.out.println(numList.toString());
        // System.out.println(resList.toString());
        System.out.println(resList.stream().reduce(BigInteger.ZERO, BigInteger::add));
    }

    private static BigInteger calculate(BigInteger a, BigInteger b, String op) {
        switch (op) {
            case "+":
                return a.add(b);
            case "-":
                return a.subtract(b);
            case "*":
                return a.multiply(b);
            case "/":
                return a.divide(b);
            default:
                return BigInteger.valueOf(0);
        }
    }

    private static BigInteger calculate(BigInteger a, BigInteger b, char op) {
        switch (op) {
            case '+':
                return a.add(b);
            case '-':
                return a.subtract(b);
            case '*':
                return a.multiply(b);
            case '/':
                return a.divide(b);
            default:
                return BigInteger.valueOf(0);
        }
    }
}