package eu.cloud4soa.governance.ems;

import eu.cloud4soa.adapter.rest.response.model.Application;
import eu.cloud4soa.api.datamodel.governance.ApplicationInstance;
import eu.cloud4soa.api.datamodel.governance.Credentials;
import eu.cloud4soa.api.datamodel.governance.DatabaseInfo;
import eu.cloud4soa.api.datamodel.governance.PaasInstance;
import eu.cloud4soa.api.datamodel.governance.StartStopCommand;
import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import eu.cloud4soa.governance.ems.parameters.CreateDatabaseParameters;
import eu.cloud4soa.governance.ems.parameters.DeployApplicationParameters;
import eu.cloud4soa.governance.ems.parameters.QueryDatabaseParameters;
import eu.cloud4soa.governance.ems.parameters.RestoreDatabaseParameters;

public interface IExecutionManagementService {

    /**
     * Get applications deployed in a (Paas, user)
     */
    Application[] listApplications(String adapterUrl, Credentials credentials,
            PaasInstance paasInstance) throws Cloud4SoaException;

    /**
     * Deploys an application.
     *
     * @return the url of the deployed application.
     * 
     * NOTE: This operation currently does not use a Remote Adapter.
     */
    String deployApplication(String adapterUrl, Credentials credentials,
            PaasInstance paasInstance, ApplicationInstance applicationInstance,
            DeployApplicationParameters parameters) throws Cloud4SoaException;

    /**
     * Undeploys an application. 
     */
    void undeployApplication(String adapterUrl, Credentials credentials,
            PaasInstance paasInstance, ApplicationInstance applicationInstance)
            throws Cloud4SoaException;

    /**
     * Starts/stops an application.
     */
    void startStopApplication(String adapterUrl, Credentials credentials,
            PaasInstance paasInstance, ApplicationInstance applicationInstance,
            StartStopCommand command) throws Cloud4SoaException;

    /**
     * @see #startStopApplication(String, Credentials, PaasInstance, ApplicationInstance, StartStopCommand) 
     */
    void startStopApplication(String adapterUrl, Credentials credentials,
            PaasInstance paasInstance, ApplicationInstance applicationInstance,
            String startStopCommand) throws Cloud4SoaException;

    
    /**
     * Creates a database in the PaaS provider.
     */
    public DatabaseInfo createDatabase(String adapterUrl, Credentials credentials, 
            PaasInstance paasInstance, ApplicationInstance applicationInstance,
            CreateDatabaseParameters parameters) throws Cloud4SoaException;

    /**
     * Get database details
     * 
     * The result may not contain user and password.
     */
    public DatabaseInfo getDatabaseInfo(String adapterUrl, Credentials credentials,
            PaasInstance paasInstance, ApplicationInstance applicationInstance, 
            QueryDatabaseParameters parameters) throws Cloud4SoaException;
    
    /**
     * Deletes a database from the PaaS provider
     */
    public void deleteDatabase(String adapterUrl, Credentials credentials, 
            PaasInstance paasInstance, ApplicationInstance applicationInstance,
            QueryDatabaseParameters parameters) 
        throws Cloud4SoaException;
    
    /**
     * Loads a database from file to a PaaS provider
     */
    public void restoreDatabase(String adapterUrl, Credentials credentials, 
            PaasInstance paasInstance, ApplicationInstance applicationInstance,
            RestoreDatabaseParameters parameters) throws Cloud4SoaException;
            
    /**
     * Dumps a database from the Paas provider to a local file.
     */
    public void dumpDatabase(String adapterUrl, Credentials credentials, PaasInstance paasInstance, 
            ApplicationInstance applicationInstance, RestoreDatabaseParameters parameters)
                    throws Cloud4SoaException;
}