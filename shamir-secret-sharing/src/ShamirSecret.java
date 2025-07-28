import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.*;

public class ShamirSecret {

    static class Point {
        int x;
        BigInteger y;
        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }

    static List<Point> readPoints(String filename) throws Exception {
        JSONObject obj = new JSONObject(new JSONTokener(new FileInputStream(filename)));
        List<Point> points = new ArrayList<>();
        for (String key : obj.keySet()) {
            if (key.equals("keys")) continue;
            int x = Integer.parseInt(key);
            JSONObject root = obj.getJSONObject(key);
            int base = Integer.parseInt(root.getString("base"));
            String value = root.getString("value");
            BigInteger y = decodeValue(value, base);
            points.add(new Point(x, y));
        }
        points.sort(Comparator.comparingInt(p -> p.x));
        return points;
    }

    static BigInteger lagrangeAtZero(List<Point> points) {
        BigInteger result = BigInteger.ZERO;
        int k = points.size();
        for (int i = 0; i < k; i++) {
            BigInteger numerator = points.get(i).y;
            BigInteger denom = BigInteger.ONE;
            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                numerator = numerator.multiply(BigInteger.valueOf(-points.get(j).x));
                denom = denom.multiply(BigInteger.valueOf(points.get(i).x - points.get(j).x));
            }
            result = result.add(numerator.divide(denom));
        }
        return result;
    }

    static <T> List<List<T>> combinations(List<T> arr, int k) {
        List<List<T>> result = new ArrayList<>();
        combine(arr, k, 0, new ArrayList<>(), result);
        return result;
    }
    static <T> void combine(List<T> arr, int k, int idx, List<T> curr, List<List<T>> result) {
        if (curr.size() == k) {
            result.add(new ArrayList<>(curr));
            return;
        }
        for (int i = idx; i < arr.size(); i++) {
            curr.add(arr.get(i));
            combine(arr, k, i + 1, curr, result);
            curr.remove(curr.size() - 1);
        }
    }

    public static void main(String[] args) throws Exception {
        String[] files = {"testcase1.json", "testcase2.json"};
        for (String file : files) {
            JSONObject obj = new JSONObject(new JSONTokener(new FileInputStream(file)));
            int n = obj.getJSONObject("keys").getInt("n");
            int k = obj.getJSONObject("keys").getInt("k");
            List<Point> points = readPoints(file);
            Map<BigInteger, Integer> freq = new HashMap<>();
            Map<BigInteger, List<List<Point>>> secretToCombs = new HashMap<>();
            List<List<Point>> combs = combinations(points, k);
            for (List<Point> comb : combs) {
                BigInteger secret = lagrangeAtZero(comb);
                freq.put(secret, freq.getOrDefault(secret, 0) + 1);
                secretToCombs.computeIfAbsent(secret, x -> new ArrayList<>()).add(comb);
            }
            BigInteger best = null;
            int max = 0;
            for (Map.Entry<BigInteger, Integer> e : freq.entrySet()) {
                if (e.getValue() > max) {
                    max = e.getValue();
                    best = e.getKey();
                }
            }
            System.out.println(best);
            Set<Integer> correctXs = new HashSet<>();
            for (List<Point> comb : secretToCombs.get(best)) {
                for (Point p : comb) {
                    correctXs.add(p.x);
                }
            }
            List<Integer> wrongShares = new ArrayList<>();
            for (Point p : points) {
                if (!correctXs.contains(p.x)) {
                    wrongShares.add(p.x);
                }
            }
            if (wrongShares.isEmpty()) {
                System.out.println("No wrong shares");
            } else {
                System.out.print("Wrong shares: ");
                for (int i = 0; i < wrongShares.size(); i++) {
                    System.out.print(wrongShares.get(i));
                    if (i < wrongShares.size() - 1) System.out.print(", ");
                }
                System.out.println();
            }
        }
    }
} 