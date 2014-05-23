/*
 *  Copyright 2013 Cloud4SOA, www.cloud4soa.eu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.cloud4soa.governance.ems;

import java.io.File;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cloudadapter.Adapter;
import eu.cloud4soa.adapter.rest.AdapterClient;
import eu.cloud4soa.adapter.rest.auth.CustomerCredentials;
import eu.cloud4soa.adapter.rest.common.Operation;
import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
import eu.cloud4soa.adapter.rest.impl.AdapterClientCXF;
import eu.cloud4soa.adapter.rest.request.CreateDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.DatabaseRequest;
import eu.cloud4soa.adapter.rest.request.DeleteApplicationRequest;
import eu.cloud4soa.adapter.rest.request.DeleteDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.ListApplicationRequest;
import eu.cloud4soa.adapter.rest.request.OperationRequest;
import eu.cloud4soa.adapter.rest.request.Request;
import eu.cloud4soa.adapter.rest.response.CreateDatabaseResponse;
import eu.cloud4soa.adapter.rest.response.DatabaseResponse;
import eu.cloud4soa.adapter.rest.response.DeleteApplicationResponse;
import eu.cloud4soa.adapter.rest.response.DeleteDatabaseResponse;
import eu.cloud4soa.adapter.rest.response.ListApplicationResponse;
import eu.cloud4soa.adapter.rest.response.OperationResponse;
import eu.cloud4soa.adapter.rest.response.Response;
import eu.cloud4soa.adapter.rest.response.model.Application;
import eu.cloud4soa.adapter.rest.response.model.Database;
import eu.cloud4soa.api.datamodel.governance.ApplicationInstance;
import eu.cloud4soa.api.datamodel.governance.Credentials;
import eu.cloud4soa.api.datamodel.governance.DatabaseInfo;
import eu.cloud4soa.api.datamodel.governance.PaasInstance;
import eu.cloud4soa.api.datamodel.governance.StartStopCommand;
import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import eu.cloud4soa.api.util.exception.adapter.PaaSConnectionException;
import eu.cloud4soa.api.util.exception.adapter.PaaSException;
import eu.cloud4soa.governance.ems.parameters.CreateDatabaseParameters;
import eu.cloud4soa.governance.ems.parameters.DeployApplicationParameters;
import eu.cloud4soa.governance.ems.parameters.QueryDatabaseParameters;
import eu.cloud4soa.governance.ems.parameters.RestoreDatabaseParameters;

/**
 * 
 * @author vincenzo
 * @autor rsosa
 */
public class ExecutionManagementServiceModule implements IExecutionManagementService {

    final static Logger logger = LoggerFactory
            .getLogger(ExecutionManagementServiceModule.class);

    private class DeploymentUrls {
        @SuppressWarnings("unused")
        public String adapterAppUrl;
        public String deployedAppUrl;
    }

    public enum Paas {
        CLOUDFOUNDRY(Names.CLOUDFOUNDRY),
        CLOUDBEES(Names.CLOUDBEES);
        
        public static class Names {
            public static final String CLOUDFOUNDRY = "VMware";
            public static final String CLOUDBEES = "Cloudbees";
        }
        
        private final String name;

        private Paas(String name) {

            this.name = name;
        }
        
        public String getName() {
            
            return name;
        }
        
        public static Paas from(String name) {

            return Enum.valueOf(Paas.class, name);
        }
    }
    
    
    private CustomerCredentials buildRestCredentials(Credentials credentials) {
        String publicKey = credentials.getPublicKey();
        String secretKey = credentials.getPrivateKey();
        String accountName = credentials.getAccountName();
        
        CustomerCredentials result = new CustomerCredentials(
                String.format("%s_%s_%s", publicKey, secretKey, accountName != null? accountName : ""), 
                secretKey);
        
        return result;
    }

    /* (non-Javadoc)
     * @see eu.cloud4soa.governance.ems.IExecutionManagementService#listApplications(java.lang.String, eu.cloud4soa.api.datamodel.governance.Credentials, eu.cloud4soa.api.datamodel.governance.PaasInstance)
     */
    @Override
    public Application[] listApplications(String adapterUrl, Credentials credentials, PaasInstance paasInstance) 
            throws Cloud4SoaException {
        
        AdapterClient adapterClient = getAdapterClient();

        CustomerCredentials restCredentials = buildRestCredentials(credentials); 

        ListApplicationRequest request = new ListApplicationRequest();
        request.setBaseUrl(adapterUrl);

        ListApplicationResponse response = send(adapterClient, request, restCredentials);
        logger.info("list applications" + (isOk(response)? " successful" : " failed"));

        return response.getApplications();
    }


    /* (non-Javadoc)
     * @see eu.cloud4soa.governance.ems.IExecutionManagementService#deployApplication(java.lang.String, eu.cloud4soa.api.datamodel.governance.Credentials, eu.cloud4soa.api.datamodel.governance.PaasInstance, eu.cloud4soa.api.datamodel.governance.ApplicationInstance, eu.cloud4soa.api.datamodel.governance.DeployApplicationParameters)
     */
    @Override
    public String deployApplication(String adapterUrl, Credentials credentials, PaasInstance paasInstance, 
            ApplicationInstance applicationInstance, DeployApplicationParameters parameters)
            throws Cloud4SoaException {

        File applicationArchive = parameters.getApplicationArchive();;
        String publicKey = credentials.getPublicKey();
        String secretKey = credentials.getPrivateKey();
        String accountName = credentials.getAccountName();

        DeploymentUrls urls = deployApplicationArchiveOnPaaS(applicationArchive,
                applicationInstance, paasInstance, accountName, publicKey, secretKey);

        return urls.deployedAppUrl;
    }

    /* (non-Javadoc)
     * @see eu.cloud4soa.governance.ems.IExecutionManagementService#undeployApplication(java.lang.String, eu.cloud4soa.api.datamodel.governance.Credentials, eu.cloud4soa.api.datamodel.governance.PaasInstance, eu.cloud4soa.api.datamodel.governance.ApplicationInstance)
     */
    @Override
    public void undeployApplication(String adapterUrl, Credentials credentials, 
            PaasInstance paasInstance, ApplicationInstance applicationInstance) throws Cloud4SoaException {
        AdapterClient adapterClient = getAdapterClient();

        CustomerCredentials restCredentials = buildRestCredentials(credentials);
        DeleteApplicationRequest deleteDeploymentRequest = new DeleteApplicationRequest();

        deleteDeploymentRequest.setBaseUrl(adapterUrl);
        deleteDeploymentRequest.setApplicationName(applicationInstance.getAppName());
        DeleteApplicationResponse deleteDeploymentResponse = send(adapterClient,
                deleteDeploymentRequest, restCredentials);

        int status = deleteDeploymentResponse.getStatusCode().ordinal();
        logger.info("Undeploying - " + deleteDeploymentResponse.getStatusCode().toString()
                + " " + (status > 199 && status < 300 ? "successful" : "failed") + " for app: "
                + applicationInstance.getAppName());
        if (!(status > 199 && status < 300)) {
            throw new Cloud4SoaException(
                    "Error in performing the undeployment command for app: "
                            + applicationInstance.getAppName() + " - cause: "
                            + deleteDeploymentResponse.getStatusCode().toString());
        }

    }

    /* (non-Javadoc)
     * @see eu.cloud4soa.governance.ems.IExecutionManagementService#startStopApplication(java.lang.String, eu.cloud4soa.api.datamodel.governance.Credentials, eu.cloud4soa.api.datamodel.governance.PaasInstance, eu.cloud4soa.api.datamodel.governance.ApplicationInstance, eu.cloud4soa.api.datamodel.governance.StartStopCommand)
     */
    @Override
    public void startStopApplication(String adapterUrl,
            Credentials credentials, PaasInstance paasInstance, ApplicationInstance applicationInstance,
            StartStopCommand command) throws Cloud4SoaException {
        
        startStopApplication(adapterUrl, credentials, paasInstance, applicationInstance, command.getOperation());
    }

    /* (non-Javadoc)
     * @see eu.cloud4soa.governance.ems.IExecutionManagementService#startStopApplication(java.lang.String, eu.cloud4soa.api.datamodel.governance.Credentials, eu.cloud4soa.api.datamodel.governance.PaasInstance, eu.cloud4soa.api.datamodel.governance.ApplicationInstance, java.lang.String)
     */
    @Override
    public void startStopApplication(String adapterUrl,
            Credentials credentials, PaasInstance paasInstance, ApplicationInstance applicationInstance,
            String startStopCommand) throws Cloud4SoaException {

        AdapterClient adapterClient = getAdapterClient();

        CustomerCredentials restCredentials = buildRestCredentials(credentials);

        OperationRequest operationRequest = new OperationRequest();
        operationRequest.setBaseUrl(adapterUrl);
        operationRequest.setApplicationName(applicationInstance.getAppName());
        operationRequest.setOperation(Operation.toOperation(startStopCommand));

        OperationResponse operationResponse = send(adapterClient, operationRequest,
                restCredentials);

        int status = operationResponse.getStatusCode().ordinal();
        logger.info(startStopCommand + " - " + operationResponse.getStatusCode().toString()
                + " " + (status > 199 && status < 300 ? "successful" : "failed")
                + " for app: " + applicationInstance.getAppName());
        if (!isOk(operationResponse)) {
            throw new Cloud4SoaException("Error in performing the " + startStopCommand
                    + " command for app: " + applicationInstance.getAppName()
                    + " - cause: " + operationResponse.getStatusCode().toString());
        }
    }
    
    @Override
    public DatabaseInfo createDatabase(String adapterUrl,
            Credentials credentials, PaasInstance paasInstance,
            ApplicationInstance applicationInstance,
            CreateDatabaseParameters parameters) throws Cloud4SoaException {

        String paasName = paasInstance.getName();
        String applicationName = applicationInstance.getAppName();
        AdapterClient adapterClient = getAdapterClient();
        
        CreateDatabaseRequest createDatabaseRequest = new CreateDatabaseRequest();

        createDatabaseRequest.setBaseUrl(adapterUrl);
        
        // application name is not actually used
        createDatabaseRequest.setApplicationName(applicationName);
        createDatabaseRequest.setDatabaseName(parameters.getDatabase());
        createDatabaseRequest.setDatabaseUser(parameters.getUser());
        createDatabaseRequest.setDatabasePassword(parameters.getPassword());
        createDatabaseRequest.setDatabaseType(parameters.getType());

        CustomerCredentials restCredentials = buildRestCredentials(credentials);
        CreateDatabaseResponse createDatabaseResponse = send(adapterClient,
                createDatabaseRequest, restCredentials);
        
        Database database = createDatabaseResponse.getDatabase();
        DatabaseInfo databaseInfo;
        if (database != null) {

            /*
             * If we are in CloudFoundry, we bind the database to the
             * adapter. Now we need to wait the adapter is up again, and
             * get the credentials.
             */
            if (paasName.equalsIgnoreCase(Paas.Names.CLOUDFOUNDRY)) {
                database = waitForAdapterRestartAndGetCredentialsCF(
                        adapterUrl, applicationInstance, parameters, credentials);
            }
            logger.info("create database successful for app: " + applicationName
                    + " with url: " + database.getHost());
            databaseInfo = DatabaseHelper.fromRequest(database);
            return databaseInfo;
        }
        else {
            databaseInfo = new DatabaseInfo();
        }
        return databaseInfo;
    }
    
    @Override
    public DatabaseInfo getDatabaseInfo(String adapterUrl, Credentials credentials, 
            PaasInstance paasInstance, ApplicationInstance applicationInstance,
            QueryDatabaseParameters parameters) throws Cloud4SoaException {
        
        AdapterClient adapterClient = getAdapterClient();
        DatabaseRequest request = new DatabaseRequest();
        request.setBaseUrl(adapterUrl);
        request.setApplicationName(applicationInstance.getAppName());
        request.setDeploymentName(applicationInstance.getDeploymentName());
        request.setDatabaseName(parameters.getDatabase());

        CustomerCredentials restCredentials = buildRestCredentials(credentials);
        DatabaseResponse response = send(adapterClient, request, restCredentials);
        Database database = response.getDatabase();
        DatabaseInfo databaseInfo = DatabaseHelper.fromRequest(database);
        
        return databaseInfo;
    }
    
    @Override
    public void deleteDatabase(String adapterUrl, Credentials credentials,
            PaasInstance paasInstance, ApplicationInstance applicationInstance,
            QueryDatabaseParameters parameters) throws Cloud4SoaException {
        
        AdapterClient adapterClient = getAdapterClient();
        DeleteDatabaseRequest request = new DeleteDatabaseRequest();
        request.setBaseUrl(adapterUrl);
        request.setApplicationName(applicationInstance.getAppName());
        request.setDeploymentName(applicationInstance.getDeploymentName());
        request.setDatabaseName(parameters.getDatabase());

        CustomerCredentials restCredentials = buildRestCredentials(credentials);
        @SuppressWarnings("unused")
        DeleteDatabaseResponse response = send(adapterClient, request, restCredentials);
        
        return;
    }

    @Override
    public void restoreDatabase(String adapterUrl,
            Credentials credentials, PaasInstance paasInstance,
            ApplicationInstance applicationInstance,
            RestoreDatabaseParameters parameters) throws Cloud4SoaException {

        if (Paas.Names.CLOUDFOUNDRY.equals(paasInstance.getName())) {
            /*
             * CloudFoundry behaves in a totally different way.
             */
        }
        else {
            Adapter.restoreDB_REST(
                    parameters.getHost(), 
                    parameters.getPort(), 
                    parameters.getDatabaseType(), 
                    parameters.getDatabaseName(), 
                    parameters.getUserName(), 
                    parameters.getPassword(), 
                    parameters.getDumpFile().getPath());
        }
        return;
    }
    
    @Override
    public void dumpDatabase(String adapterUrl, Credentials credentials,
            PaasInstance paasInstance, ApplicationInstance applicationInstance,
            RestoreDatabaseParameters parameters) throws Cloud4SoaException {
        
        if (Paas.Names.CLOUDFOUNDRY.equals(paasInstance.getName())) {
            /*
             * CloudFoundry behaves in a totally different way.
             */
        }
        else {
            Adapter.downloadDB_REST(
                    parameters.getHost(), 
                    parameters.getPort(), 
                    parameters.getDatabaseType(), 
                    parameters.getDatabaseName(), 
                    parameters.getUserName(), 
                    parameters.getPassword(), 
                    parameters.getDumpFile().getPath());
        }
    }
    
    protected DeploymentUrls deployApplicationArchiveOnPaaS(File applicationArchive,
            ApplicationInstance applicationInstance, PaasInstance paasInstance,
            String accountName, String publicKey, String secretKey) throws Cloud4SoaException {

//        AdapterClient adapterClient = getAdapterClient();
//        CustomerCredentials credentials = new CustomerCredentials(publicKey, secretKey);

        String paas_name = paasInstance.getName();

        DeploymentUrls urls = new DeploymentUrls();

        if (paas_name.equalsIgnoreCase("CloudBees")) {

            String cloudbeesAccount = accountName;

            // application deployment
            urls.deployedAppUrl = Adapter.uploadAndDeployToEnv("CloudBees",
                    applicationArchive.getAbsolutePath(), publicKey, secretKey, cloudbeesAccount,
                    applicationInstance.getAppName(), "", "", "", "", "", "", "");

        } else if (paas_name.equalsIgnoreCase("Amazon PaaS Provider")) {
            // application deployment
            urls.deployedAppUrl = Adapter.uploadAndDeployToEnv("Beanstalk",
                    applicationArchive.getAbsolutePath(), publicKey, secretKey, "",
                    applicationInstance.getAppName(), applicationInstance.getVersion(),
                    applicationInstance.getAppName(), "", "", "", "", "");

            waitForBeanstalkApplicationReady("Beanstalk", publicKey, secretKey, "",
                    applicationInstance.getAppName(), "cloud4soa", "", "");

        } else if (paas_name.equalsIgnoreCase("Red Hat")) {
            String gitURI = Adapter.createApplication("OpenShift", publicKey, secretKey, "",
                    applicationInstance.getAppName(), "");
            // adapter deployment if needed
            urls.deployedAppUrl = Adapter.getAppURL("OpenShift", publicKey, secretKey, "",
                    applicationInstance.getAppName(), "", "", "");
            urls.adapterAppUrl = "";
            urls.deployedAppUrl = gitURI;
            
        } else if (paas_name.equalsIgnoreCase("VMware")) {

            // application deployment
            urls.deployedAppUrl = Adapter.uploadAndDeployToEnv("CloudFoundry",
                    applicationArchive.getAbsolutePath(), publicKey, secretKey, "",
                    applicationInstance.getAppName(), "", "", "", "", "", "", "");

        } else {
            throw new IllegalArgumentException("Not valid paas name:" + paas_name);
        }
        return urls;
    }

    private Database waitForAdapterRestartAndGetCredentialsCF(String adapterURL, 
            ApplicationInstance applicationInstance, CreateDatabaseParameters createDbParameters, 
            Credentials credentials) 
                    throws Cloud4SoaException {
        AdapterClient adapterClient = getAdapterClient();
        CustomerCredentials restCredentials = buildRestCredentials(credentials);

        /*
         * FIX workflow
         */
        String appName = applicationInstance.getAppName();
        String adapterAppName = "c4sad" + appName;
        /*
         * environment, type and apiVersion not used in CloudFoundry.
         */
        waitForApplicationStatus("started", "VMware", credentials.getPublicKey(), 
                credentials.getPrivateKey(), credentials.getAccountName(),
                adapterAppName, null, null, null);
        waitForApplicationStatus("running", "VMware", credentials.getPublicKey(), 
                credentials.getPrivateKey(), credentials.getAccountName(),
                adapterAppName, null, null, null);
        DatabaseRequest databaseRequest = new DatabaseRequest();
        databaseRequest.setBaseUrl(adapterURL);
        databaseRequest.setApplicationName(adapterAppName);
        databaseRequest.setDeploymentName(applicationInstance.getDeploymentName());
        databaseRequest.setDatabaseName(createDbParameters.getDatabase());

        DatabaseResponse databaseResponse = send(adapterClient, databaseRequest, restCredentials);

        return databaseResponse.getDatabase();
    }
    
    private void waitForApplicationStatus(String status, String PaaS, String publicKey,
            String secretKey, String accountName, String appName, String environment, String type,
            String apiversion) throws Cloud4SoaException {
        String actualResponse = "";
        int busyWaitingTime = 2000;
        int i = 0;
        int maxTimes = 60;

        /*
         * This is stupid, but forced 'cos of the existing code
         */
        String paasNameForLocalAdapter;
        if ("VMware".equalsIgnoreCase(PaaS)) {
            paasNameForLocalAdapter = Adapter.CLOUDFOUNDRY;
        } else {
            throw new IllegalArgumentException(PaaS + " not implemented yet");
        }

        while (i++ < maxTimes) {
            try {
                String appStatus = Adapter.getRunningStatus(paasNameForLocalAdapter, publicKey,
                        secretKey, accountName, appName, environment, type, apiversion);
                actualResponse = appStatus;
                if (actualResponse.equalsIgnoreCase("Terminating")
                        || actualResponse.equalsIgnoreCase("Terminated")) {
                    throw new Cloud4SoaException(
                            "Error in set up the adapter deployment environment!");
                }
                logger.debug(appName + " status is " + actualResponse);

                if (actualResponse.equalsIgnoreCase(status)) {
                    return;
                }
                Thread.sleep(busyWaitingTime);
            } catch (InterruptedException ex) {
                System.out.println("Error during the thread sleep");
            }
        }
        throw new Cloud4SoaException("Timeout while waiting " + appName + " is up");
    }

    private void waitForBeanstalkApplicationReady(String PaaS, String publicKey, String secretKey,
            String accountName, String appName, String environment, String type, String apiversion)
            throws Cloud4SoaException {
        String actualResponse = "";
        int busyWaitingTime = 10000;
        try {
            while (!actualResponse.equalsIgnoreCase("Ready")) {
                String appStatus = Adapter.getAppStatus(PaaS, publicKey, secretKey, accountName,
                        appName, environment, type, apiversion);
                actualResponse = appStatus;
                if (actualResponse.equalsIgnoreCase("Terminating")
                        || actualResponse.equalsIgnoreCase("Terminated")) {
                    throw new Cloud4SoaException(
                            "Error in set up the adapter deployment environment!");
                }
                logger.debug("Adapter Monitoring response status: " + actualResponse);
                Thread.sleep(busyWaitingTime);
            }
        } catch (InterruptedException ex) {
            logger.error("Error during the thread sleep");
        }
    }

    private AdapterClient getAdapterClient() {
        AdapterClient adapterClient = new AdapterClientCXF();
        return adapterClient;
    }
    
    private boolean isOk(Response<?> response) {
        
        return response.getStatusCode().is2xx();
    }
    
    private <T> T send(AdapterClient client, Request<T> request, CustomerCredentials credentials) 
            throws Cloud4SoaException {

        try {
            return client.send(request, credentials);
        } catch (AdapterClientException e) {
            throw new Cloud4SoaException(new PaaSException(e.getMessage()));
        } catch (UnknownHostException e) {
            throw new Cloud4SoaException(new PaaSConnectionException(e.getMessage()));
        }
    }
    
    
    static class DatabaseHelper {
        static DatabaseInfo fromRequest(Database database) {
            DatabaseInfo result = new DatabaseInfo();

            result.setHost(database.getHost());
            result.setDatabaseName(database.getDatabaseName());
            result.setUserName(database.getUserName());
            result.setPassword(database.getPassword());
            result.setPort(database.getPort());
            result.setDatabaseType(database.getDatabaseType());
            
            return result;
        }
    }
    
    interface Condition {
        public boolean eval();
    }
    
    static class Waiter {
    
        public static boolean until(Condition condition, long wait, int times) throws TimeoutException {
            
            for (int i = 0; i < times; i++) {
                if (condition.eval()) {
                    return true;
                }
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    /* does nothing */
                }
            }
            throw new TimeoutException();
        }
    }
}
