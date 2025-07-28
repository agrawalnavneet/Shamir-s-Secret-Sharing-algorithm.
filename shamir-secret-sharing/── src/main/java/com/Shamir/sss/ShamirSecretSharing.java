package com.shamir.sss;

import com.shamir.sss.model.Share;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements the core logic for Shamir's Secret Sharing: reconstructing the secret
 * using Lagrange interpolation.
 *
 * NOTE: For a cryptographically secure implementation, all arithmetic
 * operations (add, subtract, multiply, divide/modInverse) should be performed
 * modulo a large prime number (finite field arithmetic). This implementation
 * uses standard BigInteger arithmetic, assuming the secret and shares fit within
 * the large integer space (effectively GF(very large prime)).
 */
public class ShamirSecretSharing {

    /**
     * Reconstructs the original secret using Lagrange interpolation from a given set of 'k' shares.
     * The secret is the y-intercept of the polynomial, i.e., P(0).
     *
     * @param shares A list of 'k' Share objects required for reconstruction.
     * Must contain unique x-coordinates.
     * @return The reconstructed secret as a BigInteger.
     * @throws IllegalArgumentException if fewer than two shares are provided,
     * or if duplicate x-coordinates are found among shares.
     */
    public static BigInteger reconstructSecret(List<Share> shares) {
        if (shares == null || shares.size() < 2) {
            throw new IllegalArgumentException("At least two shares are required for secret reconstruction.");
        }

        // Check for duplicate x-coordinates, which would make interpolation impossible
        Set<BigInteger> xCoordinates = new HashSet<>();
        for (Share share : shares) {
            if (!xCoordinates.add(share.getX())) {
                throw new IllegalArgumentException("Duplicate x-coordinate found: " + share.getX() + ". Each share must have a unique x-coordinate.");
            }
        }

        BigInteger secret = BigInteger.ZERO;

        // Apply Lagrange Interpolation Formula: S = Sum (y_j * L_j(0))
        // where L_j(x) = Product ( (x - x_m) / (x_j - x_m) ) for all m != j
        // For x = 0, L_j(0) = Product ( (-x_m) / (x_j - x_m) )
        for (int j = 0; j < shares.size(); j++) {
            Share currentShare = shares.get(j);
            BigInteger xj = currentShare.getX();
            BigInteger yj = currentShare.getY();

            BigInteger numeratorProduct = BigInteger.ONE;   // Product of (0 - x_m)
            BigInteger denominatorProduct = BigInteger.ONE; // Product of (x_j - x_m)

            for (int m = 0; m < shares.size(); m++) {
                if (j != m) { // For all shares except the current one (j)
                    Share otherShare = shares.get(m);
                    BigInteger xm = otherShare.getX();

                    // Calculate numerator part: (0 - x_m)
                    numeratorProduct = numeratorProduct.multiply(BigInteger.ZERO.subtract(xm));

                    // Calculate denominator part: (x_j - x_m)
                    BigInteger diff = xj.subtract(xm);
                    if (diff.equals(BigInteger.ZERO)) {
                        // This case should be caught by the duplicate x-coordinate check above,
                        // but it's a good safeguard for robustness.
                        throw new IllegalArgumentException("Division by zero in Lagrange interpolation. This indicates duplicate x-coordinates or an issue with share generation.");
                    }
                    denominatorProduct = denominatorProduct.multiply(diff);
                }
            }

            // Calculate the term: y_j * (numeratorProduct / denominatorProduct)
            // For a general field, this division would be modular inverse.
            // For integer secret with integer shares, numeratorProduct must be divisible by denominatorProduct.
            BigInteger term = yj.multiply(numeratorProduct).divide(denominatorProduct);

            // Add to the running sum for the secret
            secret = secret.add(term);
        }

        return secret;
    }
}