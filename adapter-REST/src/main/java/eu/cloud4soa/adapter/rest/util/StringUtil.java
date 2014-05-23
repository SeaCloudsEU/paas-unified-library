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


package eu.cloud4soa.adapter.rest.util;

public class StringUtil {

	public static String capitalizeFirst(String string){
		String capitalized = string;
		if(string != null && !string.isEmpty()){
			if(string.length() == 1){
				return capitalized.toUpperCase();
			}else{
				String stringA = string.substring(0, 1);
				String stringZ = string.substring(1, string.length());
				
				capitalized = stringA.toUpperCase() + stringZ;
			}
		}
		return capitalized;
	}
}