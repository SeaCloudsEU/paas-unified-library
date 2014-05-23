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
 * Copyright 2009-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.client.lib;

import java.util.Set;

/**
 * Reports status information when uploading an application.
 */
public interface UploadStatusCallback {

    /**
     * Empty implementation
     */
    public static final UploadStatusCallback NONE = new UploadStatusCallback() {
        public void onCheckResources() {
        }

        public void onMatchedFileNames(Set<String> matchedFileNames) {
        }

        public void onProcessMatchedResources(int length) {
        }
    };

	/**
	 * Called after the /resources call is made.
	 */
	void onCheckResources();

	/**
	 * Called after the files to be uploaded have been identified.
	 * @param matchedFileNames the files to be uploaded
	 */
	void onMatchedFileNames(Set<String> matchedFileNames);

	/**
	 * Called after the data to be uploaded has been processed
	 * @param length the size of the upload data (before compression)
	 */
	void onProcessMatchedResources(int length);
}
