package c4soa.adapter;

import eu.cloud4soa.adapter.rest.response.model.Database;

public class GetCredentials {

	public static void main(String[] args) {

		new Adapter().getDatabaseDetails("", "", "dbname");
	}

}
