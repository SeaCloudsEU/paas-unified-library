package eu.cloud4soa.governance.ems.parameters;

import eu.cloud4soa.api.datamodel.governance.Credentials;

public class ListApplicationParameters {

    Credentials credentials;
    String adapterUrl;
    
    public Credentials getCredentials() {
        return credentials;
    }
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
    public String getAdapterUrl() {
        return adapterUrl;
    }
    public void setAdapterUrl(String adapterUrl) {
        this.adapterUrl = adapterUrl;
    }
    
}
