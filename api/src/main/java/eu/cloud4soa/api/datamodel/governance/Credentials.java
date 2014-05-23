package eu.cloud4soa.api.datamodel.governance;

public class Credentials {

    private String publicKey;
    private String privateKey;
    private String accountName;
    
    public Credentials(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
    
    public Credentials(String publicKey, String privateKey,
            String accountName) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.accountName = accountName;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getAccountName() {
        return accountName;
    }
    
    
}
