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


package eu.cloud4soa.adapter.rest.request;

import java.io.Serializable;

import eu.cloud4soa.adapter.rest.aop.Default;
import eu.cloud4soa.adapter.rest.aop.Ignore;
import eu.cloud4soa.adapter.rest.aop.Method;
import eu.cloud4soa.adapter.rest.aop.Method.HttpMethod;
import eu.cloud4soa.adapter.rest.aop.NotNull;
import eu.cloud4soa.adapter.rest.aop.Path;
import eu.cloud4soa.adapter.rest.aop.Path.Component;
import eu.cloud4soa.adapter.rest.aop.UrlPath;
import eu.cloud4soa.adapter.rest.response.CreateDatabaseResponse;

/**
 * Request for creation of resource <strong>Database</strong> 
 * <br><code>htt[p|ps]://baseUrl/ems/application/${applicationName}/database/${databaseName}</code>.<br>
 * 
 * Response will contain result of creation of that requested resource.
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
@Method(HttpMethod.POST)
@Path(component=Component.EMS, path="/application/${applicationName}/deployment/${deploymentName}/database/${databaseName}") // /databaseUser/${databaseUser}/databasePassword/${databasePassword}/databaseType/${databaseType}
public class CreateDatabaseRequest extends Request<CreateDatabaseResponse> implements Serializable{
	private static final long serialVersionUID = -6447997706427304871L;

	@NotNull @Ignore
	@UrlPath(pattern="${applicationName}")
	private String applicationName;
	
	@NotNull @Default("default") @Ignore
	@UrlPath(pattern="${deploymentName}")
	private String deploymentName;
	
	@NotNull @Ignore
	@UrlPath(pattern="${databaseName}")
	private String databaseName;
	
	//@NotNull @Ignore
	//@UrlPath(pattern="${databaseUser}")	
	private String databaseUser;
	
	//@NotNull @Ignore
	//@UrlPath(pattern="${databasePassword}")	        
	private String databasePassword;
	
 	//@NotNull @Ignore
	//@UrlPath(pattern="${databaseType}")	       
	private String databaseType;
	
        
        
	public CreateDatabaseRequest(){
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDeploymentName() {
		return deploymentName;
	}

	public void setDeploymentName(String deploymentName) {
		this.deploymentName = deploymentName;
	}

	public String getDatabaseUser() {
		return databaseUser;
	}

	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
        
        
        
}
