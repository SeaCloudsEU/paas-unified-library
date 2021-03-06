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

import eu.cloud4soa.adapter.rest.aop.Method;
import eu.cloud4soa.adapter.rest.aop.Method.HttpMethod;
import eu.cloud4soa.adapter.rest.aop.Path.Component;
import eu.cloud4soa.adapter.rest.aop.Path;
import eu.cloud4soa.adapter.rest.response.MonitorDetailResponse;

/**
 * Request for resource <strong>Monitor</strong> 
 * <br><code>htt[p|ps]://baseUrl/monitor/detail</code>.<br>
 * 
 * Response will contain details of that requested resource.
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 *
 */
@Method(HttpMethod.GET)
@Path(component=Component.MONITOR,path="/detail")
public class MonitorDetailRequest extends Request<MonitorDetailResponse> implements Serializable{
	private static final long serialVersionUID = 8920336436991835495L;

	public MonitorDetailRequest(){
	}
}
