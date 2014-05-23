package eu.cloud4soa.api.datamodel.governance;

public class PaasInstance {

    /**
     * Paas name
     */
    private String name;
    
    /**
     * Paas url
     */
    private String url;

    public PaasInstance() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
