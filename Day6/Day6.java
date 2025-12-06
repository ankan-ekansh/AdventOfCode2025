import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
        List<List<BigInteger>> numsList = new ArrayList<>();
        for (int i = 0; i < input.length - 1; i++) {
            String[] row = input[i].trim().split("\\s+");
            List<BigInteger> rowlist = new ArrayList<>();
            for (int j = 0; j < row.length; j++) {
                rowlist.add(new BigInteger(row[j]));
            }
            numsList.add(rowlist);
        }

        List<String> opList = new ArrayList<>();
        String[] row = input[input.length - 1].trim().split("\\s+");
        for (int i = 0; i < row.length; i++) {
            opList.add(row[i]);
        }

        ArrayList<BigInteger> resList = new ArrayList<>();
        for (int i = 0; i < opList.size(); i++) {
            resList.add(numsList.get(0).get(i));
        }

        for (int i = 1; i < numsList.size(); i++) {
            for (int j = 0; j < opList.size(); j++) {
                resList.set(j, calculate(resList.get(j), numsList.get(i).get(j), opList.get(j)));
            }
        }

        System.out.println(resList.stream().reduce(BigInteger.ZERO, BigInteger::add));
    }

    private static void fun2(String[] input) {
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

        List<BigInteger> resList = new ArrayList<>();
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

            if (input[input.length - 1].charAt(j) != ' ') {
                opStack.add(num);

                if (input[input.length - 1].charAt(j) == '+' || input[input.length - 1].charAt(j) == '-') {
                    res = BigInteger.valueOf(0);
                }

                while (!opStack.isEmpty()) {
                    res = calculate(res, opStack.pop(), input[input.length - 1].charAt(j));
                }
                resList.add(res);
                j--;
                continue;
            }
            opStack.add(num);
        }

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