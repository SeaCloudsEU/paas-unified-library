package eu.cloud4soa.governance.ems.parameters;

import java.io.File;

import eu.cloud4soa.api.datamodel.governance.DatabaseInfo;

public class RestoreDatabaseParameters extends DatabaseInfo {

    File dumpFile;

    public RestoreDatabaseParameters() {
    }
    
    public RestoreDatabaseParameters(DatabaseInfo dbInfo, File dumpFile) {
        this.dumpFile = dumpFile;
        this.setDatabaseName(dbInfo.getDatabaseName());
        this.setDatabaseType(dbInfo.getDatabaseType());
        this.setHost(dbInfo.getHost());
        this.setPort(dbInfo.getPort());
        this.setUserName(dbInfo.getUserName());
        this.setPassword(dbInfo.getPassword());
    }

    public File getDumpFile() {
        return dumpFile;
    }

    public void setDumpFile(File dumpFile) {
        this.dumpFile = dumpFile;
    }
    
    
}
