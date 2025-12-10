import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day9 {
    public static void main(String[] args) {
        // Path path = Path.of("sample.txt");
        Path path = Path.of("input.txt");
        String content = null;

        try {
            content = Files.readString(path);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (content != null) {
            String[] input = content.split("\n");
            part1(input);
            part2(input);
        }
    }

    private static void part1(String[] input) {
        List<long[]> points = new ArrayList<>();
        int n = input.length;
        for (int i = 0; i < n; i++) {
            String[] coordsString = input[i].split(",");
            long x = Long.parseLong(coordsString[0]);
            long y = Long.parseLong(coordsString[1]);
            points.add(new long[] { x, y });
        }

        long largestArea = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long curArea = getArea(points.get(i), points.get(j));
                if (curArea > largestArea) {
                    largestArea = curArea;
                }
            }
        }

        System.out.println(largestArea);
    }

    private static void part2(String[] input) {
        List<Point> points = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        int n = input.length;
        for (int i = 0; i < n; i++) {
            String[] coordsString = input[i].split(",");
            long x = Long.parseLong(coordsString[0]);
            long y = Long.parseLong(coordsString[1]);
            points.add(new Point(x, y));
        }
        for (int i = 0; i < n - 1; i++) {
            Point from = points.get(i);
            Point to = points.get(i + 1);
            edges.add(new Edge(from, to));
        }
        // Closing the polygon
        Point lastfrom = points.get(n - 1);
        Point firstto = points.get(0);
        edges.add(new Edge(lastfrom, firstto));

        long ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Point from = points.get(i);
                Point to = points.get(j);
                long minX = Math.min(from.x, to.x);
                long minY = Math.min(from.y, to.y);
                long maxX = Math.max(from.x, to.x);
                long maxY = Math.max(from.y, to.y);

                if (!intersects(minX, minY, maxX, maxY, edges)) {
                    long area = getArea(from, to);
                    ans = Math.max(ans, area);
                }
            }
        }

        System.out.println(ans);
    }

    private static boolean intersects(long minX, long minY, long maxX, long maxY, List<Edge> edges) {
        for (Edge edge : edges) {
            // Check if edge intersects with the rectangle defined by (minX, minY) and (maxX, maxY)
            // Axis aligned bounding box check
            // When using AABB collision detection, there are four conditions that must be true in order to say a collision has occurred. Given Bounding Box [A] and Bounding Box [B], these conditions are
            // - The left edge x-position of [A] must be less than the right edge x-position of [B].
            // - The right edge x-position of [A] must be greater than the left edge x-position of [B].
            // - The top edge y-position of [A] must be less than the bottom edge y-position of [B].
            // - The bottom edge y-position of [A] must be greater than the top edge y-position of [B].
            // If any of these four conditions are false, even just one, then no collision has occurred.
            long iMinX = Math.min(edge.start.x, edge.end.x);
            long iMaxX = Math.max(edge.start.x, edge.end.x);
            long iMinY = Math.min(edge.start.y, edge.end.y);
            long iMaxY = Math.max(edge.start.y, edge.end.y);
            if (minX < iMaxX && maxX > iMinX && minY < iMaxY && maxY > iMinY) {
                return true;
			}
        }
        return false;
    }

    private static long getArea(long[] a, long[] b) {
        long len = (Math.abs(a[0] - b[0]) + 1);
        long bre = (Math.abs(a[1] - b[1]) + 1);
        long area = len * bre;
        return area;
    }

    private static long getArea(Point a, Point b) {
        long len = (Math.abs(a.x - b.x) + 1);
        long bre = (Math.abs(a.y - b.y) + 1);
        long area = len * bre;
        return area;
    }
}

class Point {
    long x;
    long y;

    Point(long x, long y) {
        this.x = x;
        this.y = y;
    }
}

class Edge {
    Point start;
    Point end;

    Edge(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
}