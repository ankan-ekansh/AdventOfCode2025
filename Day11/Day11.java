import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 {
    public static void main(String[] args) {
        Path path = Path.of("input.txt");
        // Path path = Path.of("sample.txt");

        // Part 2 sample input
        // Path path = Path.of("sample2.txt");
        String content = null;

        try {
            content = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (content != null) {
            String[] input = content.split("\n");
            solve(input);
        }
    }

    private static void solve(String[] input) {
        List<String> devices = new ArrayList<>();
        Map<String, Integer> deviceIndexMap = new HashMap<>();
        for (int i = 0; i < input.length; i++) {
            String device = input[i].split(":")[0];
            devices.add(device);
            deviceIndexMap.put(device, i);
        }
        devices.add("out");
        deviceIndexMap.put("out", devices.size());
        int n = devices.size();
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n+1; i++) {
            adj.add(new ArrayList<>());
        }
        for (int i = 0; i < input.length; i++) {
            String[] toList = input[i].split(":")[1].trim().split(" ");
            for (int j = 0; j < toList.length; j++) {
                int u = i;
                int v = deviceIndexMap.get(toList[j]);
                adj.get(u).add(v);
            }
        }

        // Part 1
        // int src = deviceIndexMap.get("you");
        // int dest = deviceIndexMap.get("out");
        // int[] ans = new int[1];
        // dfs(src, -1, adj, ans, dest);
        // System.out.println(ans[0]);

        // Part 2
        int src = deviceIndexMap.get("svr");
        int dest = deviceIndexMap.get("out");
        int dac = deviceIndexMap.get("dac");
        int fft = deviceIndexMap.get("fft");

        // src -> dac -> fft -> dest
        long a = dfs2(src, -1, adj, dac, new HashMap<>());
        long b = dfs2(dac, -1, adj, fft, new HashMap<>());
        long c = dfs2(fft, -1, adj, dest, new HashMap<>());
        // src -> fft -> dac -> dest
        long d = dfs2(src, -1, adj, fft, new HashMap<>());
        long e = dfs2(fft, -1, adj, dac, new HashMap<>());
        long f = dfs2(dac, -1, adj, dest, new HashMap<>());
        System.out.println((a*b*c)+(d*e*f));
    }

    private static void dfs(int u, int par, List<List<Integer>> adj, int[] ans, int dest) {
        if (u == dest) {
            ans[0]++;
            return;
        }

        for (int v : adj.get(u)) {
            if (v == par) {
                continue;
            }
            dfs(v, u, adj, ans, dest);
        }
    }

    private static long dfs2(int u, int par, List<List<Integer>> adj, int dest, Map<Integer, Long> memo) {
        if (u == dest) {
            return 1L;
        }
        if (memo.containsKey(u)) {
            return memo.get(u);
        }

        long cnt = 0;
        for (int v : adj.get(u)) {
            cnt += dfs2(v, u, adj, dest, memo);
        }

        memo.put(u, cnt);
        return cnt;
    }
}
