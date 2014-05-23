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
package usage;

import java.net.UnknownHostException;

import eu.cloud4soa.adapter.rest.auth.CustomerCredentials;
import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
import eu.cloud4soa.adapter.rest.impl.AdapterClientCXF;
import eu.cloud4soa.adapter.rest.request.CreateDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.DatabaseRequest;
import eu.cloud4soa.adapter.rest.request.DeleteApplicationRequest;
import eu.cloud4soa.adapter.rest.request.DeleteDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.Request;
import eu.cloud4soa.adapter.rest.response.Response;

public class TestingCloudFoundry {

	public static final String adapterURL = "http://c4sadsamplejavaapp.cf.cloud4soa.eu";
    private static final String apiKey = "XXX"; //email from CloudFoundry account
    private static final String secretKey = "XXX"; //password from CloudFoundry accou

    private static final String applicationName = "SampleJavaApp";
    @SuppressWarnings("unused")
    private static final String adapterName = "c4sadSampleJavaApp";
    private static final String deploymentName = "default";
	@SuppressWarnings("unused")
    private static final String dbName = "dbtest";
	private static final String dbUser = "whatever";
	private static final String dbType = "whatever";
	private static final String dbPassword = "whatever";

	private static CreateDatabaseRequest createDatabase(String dbname, String appname) {
		
		CreateDatabaseRequest createDatabaseRequest = new CreateDatabaseRequest();
        createDatabaseRequest.setBaseUrl(adapterURL);
        createDatabaseRequest.setApplicationName(appname);
        createDatabaseRequest.setDatabaseName(dbname);
        createDatabaseRequest.setDatabasePassword(dbPassword);
        createDatabaseRequest.setDatabaseType(dbType);
        createDatabaseRequest.setDatabaseUser(dbUser);

        return createDatabaseRequest;
	}

	@SuppressWarnings("unused")
    private static CreateDatabaseRequest createDatabase(String dbname) {

		return createDatabase(dbname, applicationName);
	}
	
	@SuppressWarnings("unused")
    private static DeleteDatabaseRequest deleteDatabase(String dbname) {
		
        DeleteDatabaseRequest deleteDatabaseRequest = new DeleteDatabaseRequest();
        deleteDatabaseRequest.setBaseUrl(adapterURL);
        deleteDatabaseRequest.setApplicationName(applicationName);
        deleteDatabaseRequest.setDatabaseName(dbname);
        
        return deleteDatabaseRequest;
	}

	@SuppressWarnings("unused")
    private static DeleteApplicationRequest deleteApplication(String appname) {
        DeleteApplicationRequest deleteApplicationRequest = new DeleteApplicationRequest();
        deleteApplicationRequest.setBaseUrl(adapterURL);
        deleteApplicationRequest.setApplicationName(appname);
        
        return deleteApplicationRequest;
	}

	public static DatabaseRequest getDatabaseDetails(String dbname) {
		
        DatabaseRequest databaseRequest = new DatabaseRequest();
        databaseRequest.setBaseUrl(adapterURL);
        databaseRequest.setApplicationName(applicationName);
        databaseRequest.setDeploymentName(deploymentName);
        databaseRequest.setDatabaseName(dbname);
        
        return databaseRequest;
	}

	public static Response<?> send(Request<?> request) throws AdapterClientException, UnknownHostException {
		
        AdapterClientCXF client = new AdapterClientCXF();
        CustomerCredentials credentials = new CustomerCredentials(apiKey + "_" + secretKey, secretKey);
        Response<?> response = (Response<?>) client.send(request, credentials);
        
        return response;
	}
	
    @SuppressWarnings("unused")
    public static void main(String[] args) throws AdapterClientException, UnknownHostException {


        Response<?> response;


//        OperationRequest oprequest = new OperationRequest();
//        oprequest.setApplicationName(applicationName);
//        oprequest.setOperation(Operation.start);
//        oprequest.setBaseUrl(adapterURL);

//        ListApplicationRequest listAppRequest = new ListApplicationRequest();
//        listAppRequest.setBaseUrl(adapterURL);

//        response = send(createDatabase("db3", applicationName));
//        System.out.println(response);               
//        
//        response = send(getDatabaseDetails("db3"));
//        Database database = ((DatabaseResponse) response).getDatabase();
        
        
        System.out.println(String.format("%s://%s:%s/%s;user=%s&pwd=%s", 
        		"mysql", "localhost", "3306",
        		"dbname", "dbuser",
        		"dbpassword"));

    }
}
