package eu.cloud4soa.governance.ems;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.cloud4soa.adapter.rest.response.model.Application;
import eu.cloud4soa.adapter.rest.response.model.Database;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/ems-test-governance.xml" })
public class EmsTest {
    
    @Autowired
    private Credentials cloudbeesCredentials;
    @Autowired
    private Credentials cloudfoundryCredentials;
    
    static class PaasInfo {
        private String adapterUrl;
        private String name;
        private Credentials credentials;
        
        public PaasInfo(String adapterUrl, String name, Credentials credentials) {
            super();
            this.adapterUrl = adapterUrl;
            this.name = name;
            this.credentials = credentials;
        }

        public String getAdapterUrl() {
            return adapterUrl;
        }

        public String getName() {
            return name;
        }

        public Credentials getCredentials() {
            return credentials;
        }
        
        public PaasInstance getPaasInstance() {
            
            PaasInstance result = new PaasInstance();
            result.setName(name);
            
            return result;
        }
    }
    
    private PaasInfo cloudbeesInfo;

    @SuppressWarnings("unused")
    private PaasInfo cloudfoundryInfo;


    IExecutionManagementService ems = new ExecutionManagementServiceModule();
    String adapterUrl;
    String appName;
    String dbName;
    ApplicationInstance ai;
    PaasInstance pi;
    Credentials credentials;
    PaasInfo paasInfo;

    DatabaseInfo dbInfo;

    String dbuser = "rosogonatos-user";
    String dbpassword = "dbpassword";
    String dbtype = "mysql";
    
    public EmsTest() {

        appName = "sampleApp";
        dbName = "rosogonatos-sampledb";
    }

    @Before
    public void setUp() {
        cloudbeesInfo = new PaasInfo(
                "http://localhost:13000/jaxrs-service",
                ExecutionManagementServiceModule.Paas.CLOUDBEES.getName(),
                cloudbeesCredentials
                );
        cloudfoundryInfo = new PaasInfo(
                "http://localhost:13000/jaxrs-service",
                ExecutionManagementServiceModule.Paas.CLOUDFOUNDRY.getName(),
                cloudfoundryCredentials
                );
        
        paasInfo = cloudbeesInfo;
//        paasInfo = cloudfoundryInfo;
        adapterUrl = paasInfo.getAdapterUrl();
        credentials = paasInfo.getCredentials();
        pi = paasInfo.getPaasInstance();

        ai = new ApplicationInstance();
        ai.setAdapterUrl(paasInfo.getAdapterUrl());
        ai.setAppName(appName);
        
    }

    ////////////////////////////////////////////////////////////////////////
    // Jobs
    ////////////////////////////////////////////////////////////////////////
    
    private Application[] listApplicationsJob() throws Cloud4SoaException {
        
        return ems.listApplications(adapterUrl, credentials, pi);
    }
    
    private void deployJob() throws Cloud4SoaException {
        DeployApplicationParameters parameters = new DeployApplicationParameters();
        File file = new File(this.getClass().getResource("/SampleApp2.war").getFile());
        parameters.setApplicationArchive(file);
        ems.deployApplication(adapterUrl, credentials, pi, ai, parameters);
    }
    
    private void undeployJob() throws Cloud4SoaException {        
        ems.undeployApplication(adapterUrl, credentials, pi, ai);
    }

    private DatabaseInfo getDatabaseJob() throws Cloud4SoaException {
        QueryDatabaseParameters parameters = new QueryDatabaseParameters();
        parameters.setDatabase(dbName);
        return ems.getDatabaseInfo(adapterUrl, credentials, pi, ai, parameters);
    }
    
    @SuppressWarnings("unused")
    private Database[] listDatabasesJob() throws Cloud4SoaException {
    	return null;
//        return ems.listDatabases(ai, PUBLIC_KEY, PRIVATE_KEY, ACCOUNT_KEY);
    }
    
    private DatabaseInfo createDatabaseJob() throws Cloud4SoaException {
        
        CreateDatabaseParameters parameters = new CreateDatabaseParameters();
        parameters.setDatabase(dbName);
        parameters.setUser(dbuser);
        parameters.setPassword(dbpassword);
        parameters.setType(dbtype);
        
        DatabaseInfo info = ems.createDatabase(adapterUrl, credentials, pi, ai, parameters);
        return info;
    }

    private void deleteDatabaseJob() throws Cloud4SoaException {
        QueryDatabaseParameters parameters = new QueryDatabaseParameters();
        parameters.setDatabase(dbName);
        ems.deleteDatabase(adapterUrl, credentials, pi, ai, parameters);
    }

    private void startStopJob(StartStopCommand command) throws Cloud4SoaException {
        ems.startStopApplication(adapterUrl, credentials, pi, ai, command);
    }
    
    private void restoreDatabaseJob() throws Cloud4SoaException {
        
        if (dbInfo == null) {
            throw new IllegalStateException("dbInfo is null. Call createDatabaseJob() first");
        }
        File file = new File(this.getClass().getResource("/dbdump.sql").getFile());
        RestoreDatabaseParameters parameters = new RestoreDatabaseParameters(dbInfo, file);
        
        ems.restoreDatabase(adapterUrl, credentials, pi, ai, parameters);
    }
    
    private void dumpDatabaseJob() throws Cloud4SoaException, IOException {
        if (dbInfo == null) {
            throw new IllegalStateException("dbInfo is null. Call createDatabaseJob() first");
        }
        File file = File.createTempFile("c4sdump", ".sql");
        RestoreDatabaseParameters parameters = new RestoreDatabaseParameters(dbInfo, file);
        ems.dumpDatabase(adapterUrl, credentials, pi, ai, parameters);
        System.out.println("Database dumped to " + file.getAbsolutePath());
    }
    ////////////////////////////////////////////////////////////////////////
    // Test pieces
    ////////////////////////////////////////////////////////////////////////

    private void listApplicationsTestPiece(Boolean expectedAppStatus) throws Cloud4SoaException {
        Application[] apps = listApplicationsJob();

        if (expectedAppStatus != null) {
            String message = "listApplications: " + ai.getAppName() + 
                    (expectedAppStatus? "not found in deployed applications" : " found in deployed applications");
            boolean actualAppStatus = applicationIn(ai.getAppName(), apps);
            Assert.assertEquals(message, expectedAppStatus, actualAppStatus);
        }
    }

    ////////////////////////////////////////////////////////////////////////
    // Tests
    ////////////////////////////////////////////////////////////////////////
    
    
//    @Test
    public void deployTest() throws Cloud4SoaException {
        listApplicationsTestPiece(false);
        
        deployJob();

        listApplicationsTestPiece(true);
    }


//    @Test
    public void deployUndeployTest() throws Cloud4SoaException {

        listApplicationsTestPiece(false);
        
        deployJob();

        listApplicationsTestPiece(true);
        
        undeployJob();

        listApplicationsTestPiece(false);
    }

//    @Test 
    public void startStopTest()  throws Cloud4SoaException {
        listApplicationsTestPiece(false);
        
        deployJob();

        /*
         * Can't check app status: c4s uniform layer doesn't provide that info yet
         */
        
        startStopJob(StartStopCommand.STOP);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            /* does nothing */
        }
        startStopJob(StartStopCommand.START);
        
        undeployJob();

    }

//  @Test
    public void createAndDeleteDatabaseTest()  throws Cloud4SoaException {

        createDatabaseJob();
        getDatabaseJob();
        deleteDatabaseJob();
    }

//    @Test
    public void createAndRestoreDatabaseTest() throws Cloud4SoaException, IOException {
        dbInfo = createDatabaseJob();
        restoreDatabaseJob();
        dumpDatabaseJob();
        deleteDatabaseJob();
    }
    
    /*
     * deploy is a very time-consuming operation: use a different test method for each operation, 
     * repeating the deploy in each one, would make the testcase eternal. 
     */
    @Test
    public void fullTest() throws Cloud4SoaException, IOException {
        Application[] apps;
        
        deployJob();

        apps = listApplicationsJob();
        Assert.assertTrue("listApplications: " + ai.getAppName() + " not found in deployed applications", 
                applicationIn(ai.getAppName(), apps));

        dbInfo = createDatabaseJob();

        getDatabaseJob();
        
//        Database[] dbs;
//        dbs = listDatabasesJob();
//        Assert.assertTrue("listDatabases:" + dbName + " not found in databases", 
//                databaseIn(dbName, dbs));
//    
        restoreDatabaseJob();

        dumpDatabaseJob();
        
        deleteDatabaseJob();
        
        undeployJob();
        
        apps = listApplicationsJob();
        Assert.assertFalse("listApplications: " + ai.getAppName() + " still deployed", 
                applicationIn(ai.getAppName(), apps));
    }
    
    private interface PropertyExtractor<T> {
        String extract(T item);
    }
    
    private <T> boolean in(String toSearch, T[] array, PropertyExtractor<T> extractor) {
        boolean found = false;
        
        for (T item : array) {
            String itemProperty = extractor.extract(item);
            if (toSearch.equalsIgnoreCase(itemProperty)) {
                found = true;
                break;
            }
        }
        return found;
    }
    
    private boolean applicationIn(String appName, Application[] applications) {
        
        return in(appName, applications, new PropertyExtractor<Application>() {
            @Override
            public String extract(Application item) {
                return item.getApplicationName().replace(paasInfo.credentials.getAccountName() + "/", "").toLowerCase();
            }
        });
    }
    
    @SuppressWarnings("unused")
    private boolean databaseIn(String dbName, DatabaseInfo[] databases) {
        
        return in(dbName, databases, new PropertyExtractor<DatabaseInfo>() {
            @Override
            public String extract(DatabaseInfo item) {
                return item.getDatabaseName().toLowerCase();
            }
        });
    }
}
