import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.math.BigInteger;

public class ShamirSecret {

    public static void main(String[] args) {
        try {
            // Read and process the test cases
            JSONObject testCase1 = readJsonFile("testcase1.json");
            JSONObject testCase2 = readJsonFile("testcase2.json");

            System.out.println("Processing TestCase 1:");
            processTestCase(testCase1);

            System.out.println("Processing TestCase 2:");
            processTestCase(testCase2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to read and parse the JSON file
    private static JSONObject readJsonFile(String fileName) throws Exception {
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(fileName);
        return (JSONObject) parser.parse(reader);
    }

    // Method to process each test case
    private static void processTestCase(JSONObject testCase) {
        // Extract 'keys' and its values
        JSONObject keys = (JSONObject) testCase.get("keys");
        int n = ((Long) keys.get("n")).intValue(); // Extracts 'n' value
        int k = ((Long) keys.get("k")).intValue(); // Extracts 'k' value
        System.out.println("n: " + n + ", k: " + k);

        // Iterate through each key-value pair in the test case (excluding 'keys')
        for (Object keyObj : testCase.keySet()) {
            String key = (String) keyObj;
            if (key.equals("keys")) continue; // Skip the "keys" entry

            // Get base and value for each key
            JSONObject entry = (JSONObject) testCase.get(key);
            int base = Integer.parseInt((String) entry.get("base"));
            String value = (String) entry.get("value");

            // Validate and process the value based on the base
            if (!isValidForBase(value, base)) {
                System.err.println("Invalid value for base " + base + ": " + value);
                continue; // Skip this invalid value
            }

            // Parse the value for the given base and print it
            try {
                // Use BigInteger to handle large numbers
                BigInteger parsedValue = new BigInteger(value, base);
                System.out.println("Parsed value for key " + key + ": " + parsedValue);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing value for key " + key + ": " + value);
            }
        }
    }

    // Validate if a string is compatible with a given base
    private static boolean isValidForBase(String value, int base) {
        String allowedChars = "0123456789abcdefghijklmnopqrstuvwxyz";
        String validChars = allowedChars.substring(0, base);
        return value.toLowerCase().matches("[" + validChars + "]+");
    }
}
