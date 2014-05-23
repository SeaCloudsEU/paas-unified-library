package eu.cloud4soa.api.datamodel.governance;



public class ApplicationInstance {

    /**
     * Application Name. Used by the PaaS in the deployment.
     */
    private String appName;
    
    /**
     * Deployed application url.
     */
    private String appUrl;
    
    /**
     * Adapter url
     */
    private String adapterUrl;
    
    /**
     * Application version
     */
    private String version;
//    private Account account;
//    private Status status;    TODO: define a status
//    private String uriId;
//    private Long runtime = 0L;
//    private Long latestStart = 0L;
//    private String deployedProviderName;
//    private String deployedOfferingName;
    private String deploymentName;
    
    public ApplicationInstance() {
        
    }
    
//    public ApplicationInstanceBean(ApplicationInstance ai) {
//        
//        this.appName = ai.getName();
//        this.appUrl = ai.getAppurl();
//        this.adapterUrl = ai.getAdapterurl();
//        this.version = ai.getVersion();
//        this.uriID = ai.getUriID();
//        this.latestStart = ai.getLatestStart();
//    }
//    
//
//    public static ApplicationInstanceBean fromApplicationInstance(ApplicationInstance ai) {
//        return new ApplicationInstanceBean(ai);
//    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAdapterUrl() {
        return adapterUrl;
    }

    public void setAdapterUrl(String adapterUrl) {
        this.adapterUrl = adapterUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

//    public String getUriID() {
//        return uriId;
//    }
//
//    public void setUriID(String uriID) {
//        this.uriId = uriID;
//    }
//
//    public Long getLatestStart() {
//        return latestStart;
//    }
//
//    public void setLatestStart(Long latestStart) {
//        this.latestStart = latestStart;
//    }
//
//    public String getDeployedProviderName() {
//        return deployedProviderName;
//    }
//
//    public void setDeployedProviderName(String deployedProviderName) {
//        this.deployedProviderName = deployedProviderName;
//    }
//
//    public String getDeployedOfferingName() {
//        return deployedOfferingName;
//    }
//
//    public void setDeployedOfferingName(String deployedOfferingName) {
//        this.deployedOfferingName = deployedOfferingName;
//    }
//
    public String getDeploymentName() {
        return deploymentName;
    }

    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }

}
