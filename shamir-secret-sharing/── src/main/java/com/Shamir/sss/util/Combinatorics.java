package com.shamir.sss.util;
import com.shamir.sss.model.Share;
import java.util.ArrayList;
import java.util.List;


public class Combinatorics {
    private static void generateCombinationsRecursive(List<Share> allShares, int k, int start,
     List<Share> currentCombo, List<List<Share>> result) {

        if (currentCombo.size() == k) {
            result.add(new ArrayList<>(currentCombo)); // Add a copy to the result list
            return;
        }

        for (int i = start; i < allShares.size(); i++) {
            currentCombo.add(allShares.get(i)); 
    
            generateCombinationsRecursive(allShares, k, i + 1, currentCombo, result);
            currentCombo.remove(currentCombo.size() - 1); 
        }
    }

 
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
