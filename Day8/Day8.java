import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Day8 {
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
            solve(input, 1, path.getFileName().toString());
            solve(input, 2, path.getFileName().toString());
        }
    }

    private static void solve(String[] input, int part, String inputFileName) {
        int n = input.length;
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String pointString = input[i];
            String[] pointStringArray = pointString.split(",");
            int[] pointCoordinates = Arrays.stream(pointStringArray).mapToInt(Integer::parseInt).toArray();
            Point p = new Point(pointCoordinates[0], pointCoordinates[1], pointCoordinates[2]);
            points.add(p);
        }

        List<LineSegment> lineSegments = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                Point p = points.get(i);
                Point q = points.get(j);
                LineSegment ls = new LineSegment(p, q);
                lineSegments.add(ls);
            }
        }

        Collections.sort(lineSegments);;
        DisjointSet dsu = new DisjointSet(points);
        long cost = 0;
        long count = 0;
        for (LineSegment lineSegment : lineSegments) {
            Point p = lineSegment.p;
            Point q = lineSegment.q;

            if (!dsu.findParent(p).equals(dsu.findParent(q))) {
                dsu.union(p, q);
                cost = p.x * q.x;
                if (part == 2 && ++count == n-1) {
                    break;
                }
            }
            // 10 for sample, 1000 for input
            if (part == 1 && ++count == (inputFileName.equals("sample.txt") ? 10 : 1000)) {
                break;
            }
        }
        List<Long> sizeCounts = new ArrayList<>(dsu.size.values());
        Collections.sort(sizeCounts, (a, b) -> Long.compare(b, a));
        long result = Math.multiplyExact(sizeCounts.get(0), Math.multiplyExact(sizeCounts.get(1), sizeCounts.get(2)));
        System.out.println("For input file name " + inputFileName + ", Part " + part + " ans = " + (part == 1 ? result : cost));
    }
}

class Point {
    long x;
    long y;
    long z;

    Point(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long distance(Point p) {
        return square(this.x - p.x) + square(this.y - p.y) + square(this.z - p.z);
    }

    public static long distance(Point p, Point q) {
        return square(p.x - q.x) + square(p.y - q.y) + square(p.z - q.z);
    }

    private static long square(long x) {
        return x * x;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(').append(x).append(',').append(y).append(',').append(z).append(')');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Point p = (Point) o;
        return this.x == p.x && this.y == p.y && this.z == p.z;
    }
}

class LineSegment implements Comparable<LineSegment> {
    Point p;
    Point q;
    long length;

    LineSegment(Point p, Point q) {
        this.p = p;
        this.q = q;
        this.length = Point.distance(p, q);
    }

    @Override
    public int compareTo(LineSegment o) {
        return Long.compare(this.length, o.length);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(p.toString()).append("<->").append(q.toString());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(p, q);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        LineSegment lineSegment = (LineSegment) o;
        return ((this.p.equals(lineSegment.p) && this.q.equals(lineSegment.q))
                || (this.p.equals(lineSegment.q) && this.q.equals(lineSegment.p))) && (this.length == lineSegment.length);
    }
}

class DisjointSet {
    HashMap<Point, Point> parent;
    HashMap<Point, Long> size;

    DisjointSet(List<Point> points) {
        parent = new HashMap<>();
        size = new HashMap<>();

        for (int i = 0; i < points.size(); i++) {
            parent.put(points.get(i), points.get(i));
            size.put(points.get(i), 1L);
        }
    }

    public Point findParent(Point p) {
        if (parent.get(p).equals(p)) {
            return p;
        }
        parent.put(p, findParent(parent.get(p)));
        return parent.get(p);
    }

    public void union(Point p, Point q) {
        p = findParent(p);
        q = findParent(q);

        if (!p.equals(q)) {
            if (size.get(p) < size.get(q)) {
                Point tmp = p;
                p = q;
                q = tmp;
            }
            parent.put(q, p);
            size.put(p, size.get(p) + size.get(q));
        }
    }
}