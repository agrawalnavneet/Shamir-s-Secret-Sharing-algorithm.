package com.shamir.sss;

import com.shamir.sss.model.Share;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShamirSecretSharing {

   
    public static BigInteger reconstructSecret(List<Share> shares) {
        if (shares == null || shares.size() < 2) {
            throw new IllegalArgumentException("At least two shares are required for secret reconstruction.");
        }


        Set<BigInteger> xCoordinates = new HashSet<>();
        for (Share share : shares) {
            if (!xCoordinates.add(share.getX())) {
                throw new IllegalArgumentException("Duplicate x-coordinate found: " + share.getX() + ". Each share must have a unique x-coordinate.");
            }
        }

        BigInteger secret = BigInteger.ZERO;

        
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

          
                    numeratorProduct = numeratorProduct.multiply(BigInteger.ZERO.subtract(xm));

              
                    BigInteger diff = xj.subtract(xm);
                    if (diff.equals(BigInteger.ZERO)) {
                    
                        throw new IllegalArgumentException("Division by zero in Lagrange interpolation. This indicates duplicate x-coordinates or an issue with share generation.");
                    }
                    denominatorProduct = denominatorProduct.multiply(diff);
                }
            }

           
            BigInteger term = yj.multiply(numeratorProduct).divide(denominatorProduct);


            secret = secret.add(term);
        }

        return secret;
    }
}
