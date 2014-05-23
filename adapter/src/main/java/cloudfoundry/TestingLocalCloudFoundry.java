package cloudfoundry;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;


import cloudadapter.Adapter;
//import usage.TestingCloudFoundry;
//import cloudadapter.Adapter;
//import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
//import eu.cloud4soa.adapter.rest.response.Response;
import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;

public class TestingLocalCloudFoundry {

    private static void waitForApplicationRunning(String PaaS, String publicKey, String secretKey, String accountName, String appName,
            String environment, String type, String apiversion) throws Cloud4SoaException {
//        String actualResponse = "";
//        int busyWaitingTime = 2000;
//        try {
//            while (true) {
////                String appStatus = Adapter.getAppStatus(PaaS, publicKey, secretKey, accountName, appName, environment, type, apiversion);
//                String appStatus = Adapter.getRunningStatus(PaaS, publicKey, secretKey, accountName, appName, environment, type, apiversion);
//                actualResponse = appStatus;
//                if (actualResponse.equalsIgnoreCase("Terminating") || actualResponse.equalsIgnoreCase("Terminated")) {
//                    throw new Cloud4SoaException("Error in set up the adapter deployment environment!");
//                }
//                System.out.println("Adapter Monitoring response status: " + actualResponse);
//                if (actualResponse.equalsIgnoreCase("running")) {
//                	break;
//                }
//                Thread.sleep(busyWaitingTime);
//            }
//        } catch (InterruptedException ex) {
//            System.out.println("Error during the thread sleep");
//        }
    }


	public static void main(String[] args) throws Exception {
//		try {
//			
//			waitForApplicationRunning(Adapter.CLOUDFOUNDRY, "atos@atoscf.com", "4to5cf", "", "c4sadSampleJavaApp", "", "", "");
//			System.out.println("app running!!");
//			Response<?> response =  
//					TestingCloudFoundry.send(TestingCloudFoundry.getDatabaseDetails("dbname"));
//
//			System.out.println(response);
//		} catch (Cloud4SoaException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			Waiter.until(new Condition() {
//
//				@Override
//				public boolean eval() {
//					try {
//						return Adapter.getRunningStatus("", "", "", "", "", "", "", "") == "running";
//					} catch (Cloud4SoaException e) {
//						return false;
//					}
//				}
//			}, 1000, 10);
//		} catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//	    Adapter.restoreDBCF("atos@atoscf.com", "4to5cf", "mysql", "sampledb", "d:\\uploads\\sample.sql");
	    Adapter.downloadDBCF("atos@atoscf.com", "4to5cf", "mysql", "sampledb", "d:\\sample.sql");
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
