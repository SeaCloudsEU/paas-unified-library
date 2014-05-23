package eu.cloud4soa.governance.ems.parameters;

import java.io.File;

public class DeployApplicationParameters {

    File applicationArchive; 

    public File getApplicationArchive() {
        return applicationArchive;
    }

    public void setApplicationArchive(File applicationArchive) {
        this.applicationArchive = applicationArchive;
    }
}
