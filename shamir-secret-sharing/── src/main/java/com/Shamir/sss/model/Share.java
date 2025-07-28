package com.shamir.sss.model;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Represents a single share in Shamir's Secret Sharing scheme.
 * A share is essentially a point (x, y) on the polynomial.
 */
public class Share {
    private final BigInteger x; // The x-coordinate (ID or index) of the share
    private final BigInteger y; // The y-coordinate (value) of the share

    public Share(BigInteger x, BigInteger y) {
        // Basic validation: x-coordinate should not be zero for typical SSS (secret is P(0))
        if (x.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("Share x-coordinate cannot be zero, as the secret is typically P(0).");
        }
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Share share = (Share) o;
        return x.equals(share.x) && y.equals(share.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}