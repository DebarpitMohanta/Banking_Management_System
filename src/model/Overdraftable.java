package model;

/**
 * Interface for accounts that support an overdraft facility.
 * <p>
 * Demonstrates: Interfaces — defines a contract that CurrentAccount must fulfil.
 * Any implementing class promises to expose an overdraft limit and
 * the ability to check whether the account is currently overdrawn.
 * </p>
 */
public interface Overdraftable {

    /**
     * Returns the maximum overdraft limit allowed for this account.
     *
     * @return overdraft limit (positive value)
     */
    double getOverdraftLimit();

    /**
     * Returns {@code true} if the account balance has gone below zero.
     *
     * @return {@code true} when the balance is negative
     */
    boolean isOverdrawn();

    /**
     * Returns the available credit remaining (balance + overdraft limit).
     * If the result is zero or negative the account is at its overdraft ceiling.
     *
     * @return available withdrawable amount
     */
    double getAvailableCredit();
}
