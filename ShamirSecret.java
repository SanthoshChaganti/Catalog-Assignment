import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ShamirSecret {

    // Method to read JSON file and extract the points
    private static JSONObject readJsonFile(String fileName) throws Exception {
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(fileName);
        return (JSONObject) parser.parse(reader);
    }

    // Method to decode a value based on the base using BigInteger
    private static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }

    // Method to apply Lagrange Interpolation and compute the secret
    private static BigInteger lagrangeInterpolation(List<Point> points, int xValue) {
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < points.size(); i++) {
            BigInteger term = points.get(i).y;
            for (int j = 0; j < points.size(); j++) {
                if (i != j) {
                    BigInteger numerator = BigInteger.valueOf(xValue).subtract(BigInteger.valueOf(points.get(j).x));
                    BigInteger denominator = BigInteger.valueOf(points.get(i).x).subtract(BigInteger.valueOf(points.get(j).x));
                    term = term.multiply(numerator).divide(denominator);
                }
            }
            result = result.add(term);
        }
        return result;
    }

    // Method to process each test case file and compute the constant term (secret)
    private static void processTestCase(String testCaseFileName) throws Exception {
        // Read and parse the JSON file containing the shares
        JSONObject testCase = readJsonFile(testCaseFileName);

        // Extract the keys n and k by casting the values properly
        JSONObject keys = (JSONObject) testCase.get("keys");
        long n = (Long) keys.get("n");
        long k = (Long) keys.get("k");

        System.out.println("Processing Test Case: " + testCaseFileName);
        System.out.println("n: " + n + ", k: " + k);

        // Extract the polynomial points (shares)
        List<Point> points = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            String base = (String) ((JSONObject) testCase.get(String.valueOf(i))).get("base");
            String value = (String) ((JSONObject) testCase.get(String.valueOf(i))).get("value");

            // Convert the base and value to decimal (real numbers)
            int baseValue = Integer.parseInt(base);
            BigInteger yValue = decodeValue(value, baseValue); // Converting value based on base

            points.add(new Point(i, yValue)); // Adding the point (x, y)
        }

        // Find the constant term by evaluating the polynomial at x = 0
        BigInteger constantTerm = lagrangeInterpolation(points, 0);
        System.out.println("The secret (constant term) is: " + constantTerm);
        System.out.println("-------------------------------------------------");
    }

    public static void main(String[] args) {
        try {
            // Process both test case files
            processTestCase("testcase1.json");
            processTestCase("testcase2.json");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper class to store points (x, y)
    static class Point {
        int x;
        BigInteger y;

        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }
}
