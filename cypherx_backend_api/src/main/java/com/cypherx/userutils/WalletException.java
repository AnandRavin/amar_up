package com.cypherx.userutils;

import org.bitcoinj.core.Sha256Hash;

/**
 * A WalletException is thrown when an error occurs while storing or retrieving
 * an item.
 */
public class WalletException extends Exception {
    /** Block or transaction hash */
    private Sha256Hash hash = Sha256Hash.ZERO_HASH;

    /**
     * Creates a new exception with a detail message
     *
     * @param       msg             Detail message
     */
    public WalletException(String msg) {
        super(msg);
    }

    /**
     * Creates a new exception with a detail message and block or transaction hash
     *
     * @param       msg             Detail message
     * @param       hash            Block or transaction hash
     */
    public WalletException(String msg, Sha256Hash hash) {
        super(msg);
        this.hash = hash;
    }

    /**
     * Creates a new exception with a detail message and cause
     *
     * @param       msg             Detail message
     * @param       t               Caught exception
     */
    public WalletException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Creates a new exception with a detail message, block or transaction hash, and cause
     *
     * @param       msg             Detail message
     * @param       hash            Block or transaction hash
     * @param       t               Caught exception
     */
    public WalletException(String msg, Sha256Hash hash, Throwable t) {
        super(msg, t);
        this.hash = hash;
    }

    /**
     * Returns the block or transaction hash associated with this exception
     *
     * @return                      Block or transaction hash
     */
    public Sha256Hash getHash() {
        return hash;
    }
}
