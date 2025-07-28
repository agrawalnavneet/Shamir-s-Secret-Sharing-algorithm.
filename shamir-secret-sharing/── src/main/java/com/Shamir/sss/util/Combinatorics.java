package com.shamir.sss.util;

import com.shamir.sss.model.Share;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for generating combinations of shares.
 */
public class Combinatorics {

    /**
     * Recursive helper function to generate combinations.
     *
     * @param allShares    The complete list of shares to choose from.
     * @param k            The number of shares required in each combination.
     * @param start        The starting index in allShares for the current recursive call.
     * @param currentCombo The current combination being built.
     * @param result       The list to accumulate all valid combinations.
     */
    private static void generateCombinationsRecursive(List<Share> allShares, int k, int start,
                                                      List<Share> currentCombo, List<List<Share>> result) {
        // Base case: if the current combination has k shares, it's complete
        if (currentCombo.size() == k) {
            result.add(new ArrayList<>(currentCombo)); // Add a copy to the result list
            return;
        }

        // Recursive step: iterate through remaining shares
        // Ensure there are enough remaining elements to form a complete combination
        for (int i = start; i < allShares.size(); i++) {
            currentCombo.add(allShares.get(i)); // Add the current share to the combination
            // Recurse with the next index (i + 1) to avoid duplicate combinations and maintain order
            generateCombinationsRecursive(allShares, k, i + 1, currentCombo, result);
            currentCombo.remove(currentCombo.size() - 1); // Backtrack: remove the last added share
        }
    }

    /**
     * Generates all unique combinations of 'k' shares from a given list of 'n' shares.
     *
     * @param allShares The list of all 'n' available shares.
     * @param k         The desired size of each combination.
     * @return A list of lists, where each inner list represents a unique combination of 'k' shares.
     */
    public static List<List<Share>> getCombinations(List<Share> allShares, int k) {
        List<List<Share>> combinations = new ArrayList<>();
        if (k < 0 || k > allShares.size()) {
            // Invalid k value, return empty list
            return combinations;
        }
        generateCombinationsRecursive(allShares, k, 0, new ArrayList<>(), combinations);
        return combinations;
    }
}