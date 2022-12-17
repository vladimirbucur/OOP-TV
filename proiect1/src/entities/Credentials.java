package entities;

import fileio.CredentialsInput;

public class Credentials {
    private String name;
    private String password;
    private String accountType;
    private String country;
    private String balance;

    public Credentials(final CredentialsInput credentialsInput) {
        this.setName(credentialsInput.getName());
        this.setPassword(credentialsInput.getPassword());
        this.setAccountType(credentialsInput.getAccountType());
        this.setCountry(credentialsInput.getCountry());
        this.setBalance(credentialsInput.getBalance());
    }

    public Credentials(final Credentials credentials) {
        this.setName(credentials.getName());
        this.setPassword(credentials.getPassword());
        this.setAccountType(credentials.getAccountType());
        this.setCountry(credentials.getCountry());
        this.setBalance(credentials.getBalance());
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     *
     * @param accountType
     */
    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    /**
     *
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     */
    public void setCountry(final String country) {
        this.country = country;
    }

    /**
     *
     * @return
     */
    public String getBalance() {
        return balance;
    }

    /**
     *
     * @param balance
     */
    public void setBalance(final String balance) {
        this.balance = balance;
    }
}
