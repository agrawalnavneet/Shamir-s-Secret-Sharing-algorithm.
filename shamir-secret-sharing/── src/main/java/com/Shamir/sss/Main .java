package com.shamir.sss;

import com.shamir.sss.model.Share;
import com.shamir.sss.util.Combinatorics;
import com.shamir.sss.util.JsonReader;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Main {

  
    private static final String TEST_CASE_1_PATH = "testcase1.json";
    private static final String TEST_CASE_2_PATH = "testcase2.json";

    public static void main(String[] args) {
        System.out.println("--- Shamir's Secret Sharing Assignment ---");

        System.out.println("\n--- Processing Test Case 1 ---");
        processTestCase(TEST_CASE_1_PATH);

   
        System.out.println("\n--- Processing Test Case 2 ---");
        processTestCase(TEST_CASE_2_PATH);

        System.out.println("\n--- All Test Cases Processed ---");
    }

   
    private static void processTestCase(String filePath) {
        System.out.println("Reading input from: " + filePath);
        try {
        
            JsonReader.ParsedInput parsedInput = JsonReader.readInput(filePath);

            int n = parsedInput.n;
            int k = parsedInput.k;
            List<Share> allShares = parsedInput.shares;

            System.out.println("Parameters:");
            System.out.println("  Total shares (n): " + n);
            System.out.println("  Threshold (k): " + k);
            System.out.println("  All shares read (decoded): " + allShares);

   
            if (allShares.size() < k) {
                System.err.println("Warning: Not enough shares (" + allShares.size() + ") provided to meet threshold (k=" + k + "). This test case might not yield a correct secret.");
            
            }
            if (k < 2) {
                throw new IllegalArgumentException("Threshold (k) must be at least 2 for secret sharing to work (degree m=k-1).");
            }
            if (allShares.size() < n) {
                System.err.println("Warning: Actual number of shares (" + allShares.size() + ") is less than 'n' specified in JSON (" + n + "). Proceeding with available shares.");
            } else if (allShares.size() > n) {
                 System.err.println("Warning: Actual number of shares (" + allShares.size() + ") is greater than 'n' specified in JSON (" + n + "). Using all " + allShares.size() + " shares.");
            }
        
            n = allShares.size();



            List<List<Share>> allCombinations = Combinatorics.getCombinations(allShares, k);
            System.out.println("\nGenerated " + allCombinations.size() + " combinations of " + k + " shares from " + n + " total shares.");


            Map<BigInteger, Integer> secretFrequency = new HashMap<>();
            Map<BigInteger, List<List<Share>>> secretToProducingCombinations = new HashMap<>();

            System.out.println("\nReconstructing secrets (constant term 'c') from combinations:");
            if (allCombinations.isEmpty()) {
                System.out.println("No valid combinations of " + k + " shares could be formed. Cannot reconstruct secret.");
            }
            for (List<Share> combo : allCombinations) {
                try {
                    BigInteger reconstructedSecretC = ShamirSecretSharing.reconstructSecret(combo);
                    // Step 6: Count how many times each secret appears
                    secretFrequency.put(reconstructedSecretC, secretFrequency.getOrDefault(reconstructedSecretC, 0) + 1);
                    secretToProducingCombinations.computeIfAbsent(reconstructedSecretC, key -> new ArrayList<>()).add(combo);
                    // System.out.println("  Combo " + combo + " -> Secret 'c': " + reconstructedSecretC); // Uncomment for detailed combo output
                } catch (IllegalArgumentException e) {
                    System.err.println("  Error reconstructing secret from combination " + combo + ": " + e.getMessage());
                }
            }

         
            BigInteger mostFrequentSecret = null;
            int maxFrequency = 0;

            System.out.println("\n--- Secret Reconstruction Summary ---");
            if (secretFrequency.isEmpty()) {
                System.out.println("No secrets could be reconstructed from any combination for this test case.");
            } else {
                for (Map.Entry<BigInteger, Integer> entry : secretFrequency.entrySet()) {
                    System.out.println("  Secret 'c': " + entry.getKey() + ", Reconstructed " + entry.getValue() + " times.");
                    if (entry.getValue() > maxFrequency) {
                        maxFrequency = entry.getValue();
                        mostFrequentSecret = entry.getKey();
                    }
                }
         
                System.out.println("\nFor " + filePath + ", the most likely true constant term 'c' is: " + mostFrequentSecret + " (appeared " + maxFrequency + " times)");
            }
            System.out.println("-----------------------------------");

         
            System.out.println("\n--- Share Validation for " + filePath + " ---");
            if (mostFrequentSecret != null) {
                List<List<Share>> correctCombinations = secretToProducingCombinations.getOrDefault(mostFrequentSecret, Collections.emptyList());
                Set<Share> sharesInCorrectCombinations = correctCombinations.stream()
                                                            .flatMap(List::stream)
                                                            .collect(Collectors.toSet());

                List<Share> validShares = new ArrayList<>();
                List<Share> invalidShares = new ArrayList<>();

                for (Share share : allShares) {
                    if (sharesInCorrectCombinations.contains(share)) {
                        validShares.add(share);
                    } else {
                        invalidShares.add(share);
                    }
                }

                System.out.println("Valid Shares (contributed to the most frequent 'c'): " + validShares);
                System.out.println("Invalid Shares (did NOT contribute to the most frequent 'c'): " + invalidShares);
            } else {
                System.out.println("Cannot identify invalid shares as no most frequent 'c' was found.");
            }
            System.out.println("-----------------------------------\n");


        } catch (IOException e) {
            System.err.println("Error reading JSON file at " + filePath + ": " + e.getMessage());
            System.err.println("Please ensure the JSON file exists in the project root or specified path.");
        } catch (IllegalArgumentException | NumberFormatException e) {
            System.err.println("Processing Error for " + filePath + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred for " + filePath + ": " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for debugging unexpected errors
        }
    }
}
