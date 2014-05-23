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


/*
 * Copyright 2010-2011, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudbees.api;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ApplicationInstallResponse")
public class ApplicationInstallResponse {
    private String id;
    private String url;
    private String postInstallURL;

    public ApplicationInstallResponse() {
    }

    public ApplicationInstallResponse(String id, String url, String postInstallURL) {
        this.id = id;
        this.url = url;
        this.postInstallURL = postInstallURL;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getPostInstallURL() {
        return postInstallURL;
    }
}