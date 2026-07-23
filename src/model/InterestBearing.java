package model;

/**
 * Interface for accounts that earn interest.
 * <p>
 * Demonstrates: Interfaces — defines a contract that SavingsAccount must fulfil.
 * Any class implementing this interface promises to expose an interest rate
 * and a method to apply accrued interest to the balance.
 * </p>
 */
public interface InterestBearing {

    /**
     * Returns the annual interest rate as a percentage (e.g. 4.5 means 4.5 %).
     *
     * @return annual interest rate
     */
    double getInterestRate();

    /**
     * Applies the interest earned to the account balance and returns the amount added.
     *
     * @return the interest amount credited to the account
     */
    double applyInterest();
}
