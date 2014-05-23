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


package eu.cloud4soa.api.datamodel.semantic.app;


public class Application  {

	private java.lang.String applicationcode;
	private java.lang.String version;
	private java.lang.String termsTitle;
	private java.lang.String digest;
	private java.lang.String licensetype;
	// inferred
	private java.lang.String description;
	private java.lang.String alternative;

	public java.lang.String getApplicationcode() {
		return applicationcode;
	}

	public void setApplicationcode(java.lang.String applicationcode) {
		this.applicationcode = applicationcode;
	}

	public java.lang.String getVersion() {
		return version;
	}

	public void setVersion(java.lang.String version) {
		this.version = version;
	}

	public java.lang.String getTermsTitle() {
		return termsTitle;
	}

	public void setTermsTitle(java.lang.String termsTitle) {
		this.termsTitle = termsTitle;
	}

	public java.lang.String getDigest() {
		return digest;
	}

	public void setDigest(java.lang.String digest) {
		this.digest = digest;
	}

	public java.lang.String getLicensetype() {
		return licensetype;
	}

	public void setLicensetype(java.lang.String licensetype) {
		this.licensetype = licensetype;
	}

	public java.lang.String getdescription() {
		return description;
	}

	public void setdescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.String getAlternative() {
		return alternative;
	}

	public void setAlternative(java.lang.String alternative) {
		this.alternative = alternative;
	}
        
       
}