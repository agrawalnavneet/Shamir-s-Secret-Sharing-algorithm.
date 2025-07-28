package com.shamir.sss.model;

import java.math.BigInteger;
import java.util.Objects;


public class Share {
    private final BigInteger x; 
    private final BigInteger y; 

    public Share(BigInteger x, BigInteger y) {

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
