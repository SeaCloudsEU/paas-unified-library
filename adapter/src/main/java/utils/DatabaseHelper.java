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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;

/**
 *
 * @author Ledakis Giannis (SingularLogic)
 * @author Román Sosa González
 */
public class DatabaseHelper {

    /*
     * TODO: process' stdin should be consumed from a stream. Input must be specified in command line right now.
     * TODO: process' stdout should be written to a stream/file. Output must be specified in command line right now.
     * 
     * Some references:
     * http://thilosdevblog.wordpress.com/2011/11/21/proper-handling-of-the-processbuilder/
     * http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html
     * http://stackoverflow.com/questions/11336157/running-external-program-with-redirected-stdin-and-stdout-from-java
     */
    private int executeCommand(String command) 
            throws IOException, InterruptedException {

        try {
            String[] executeCmd = new String[] { "SHELL", "PARAM", command };
            String osName = System.getProperty("os.name");
    
            if (osName.startsWith("Windows")) {
                executeCmd[0] = "cmd.exe";
                executeCmd[1] = "/C";
            } else {
                /* assume *nix execution */
                executeCmd[0] = "/bin/sh";
                executeCmd[1] = "-c";
            }
            System.out.println("$ >");
            for (int i = 0; i < executeCmd.length; i++)
                System.out.print(executeCmd[i] + " ");
            
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            StreamGobbler errGobbler = new StreamGobbler(runtimeProcess.getErrorStream(), "ERROR");
            StreamGobbler outGobbler = new StreamGobbler(runtimeProcess.getInputStream(), "OUTPUT");
            
            errGobbler.start();
            outGobbler.start();
            
            int processComplete = runtimeProcess.waitFor();
            
            errGobbler.join();
            outGobbler.join();
            return processComplete;
            
        } finally {
            
        }
    }

    public String storeDB(String host, String port, String dbUser,
            String dbPass, String dbName, String local_file)
            throws IOException, InterruptedException, Cloud4SoaException {
        String msg = "No Backup";

        /*
         * mysqldump MUST be in PATH
         */
        String command = "mysqldump -h " + host + " -P " + port + " -u " + dbUser
                + " --password=" + dbPass + " " + dbName
                + ("".equals(local_file) ? "" : " -r " + local_file);
        
        int processComplete = executeCommand(command);
        if (processComplete == 0) {
            System.out.println("Backup taken successfully");
            msg = "Backup taken successfully";
        } else {
            System.out.println("Could not take mysql backup");
            msg = "Could not take mysql backup";
            throw new Cloud4SoaException("Could not take mysql backup: "
                    + command);
        }
        return msg;
    }

    public String restoredb(String host, String port, String dbUser,
            String dbPass, String dbName, String local_file)
            throws IOException, InterruptedException, Cloud4SoaException {
        String msg = "Nothing done in restoredb!";

        /*
         * mysql MUST be in PATH
         */
        String command = "mysql -h " + host + " -P " + port + " -u " + dbUser + " -p"
                + dbPass + " " + dbName + " < " + local_file;
        int processComplete = executeCommand(command);

        if (processComplete == 0) {
            System.out.println("success");
            msg = "success";
        } else {
            System.out.println("restore failure");
            msg = "restore failure";
            throw new Cloud4SoaException("restore failed for " + command);
        }

        return msg;
    }

    public String restoredbForBeanstalk(String host, String port,
            String dbUser, String dbPass, String dbName, String local_file)
            throws IOException, InterruptedException, Cloud4SoaException {
        String msg = "Nothing done in restoredb!";

        // /cmd:: mysqldump acme | mysql --host=hostname --user=username
        // --password acme
        String command = "mysqldump " + dbName + " | mysql -h" + host
                + " -P " + port + " -u " + dbUser + " -p" + dbPass + " "
                + dbName + " -r " + local_file;
        int processComplete = executeCommand(command);
        
        if (processComplete == 0) {
            System.out.println("success");
            msg = "success";
        } else {
            System.out.println("restore failure");
            msg = "restore failure";
            throw new Cloud4SoaException("restore failed for " + command);

        }
        return msg;

    }

    
    /*
     * http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html
     */
    static class StreamGobbler extends Thread {
        
        InputStream is;
        String type;
        OutputStream os;
        
        public StreamGobbler(InputStream is, String type) {

            this(is, type, null);
        }
        
        public StreamGobbler(InputStream is, String type, OutputStream os) {
            this.is = is;
            this.type = type;
            this.os = os;
        }
        
        public void run() {
            
            PrintWriter pw = null;
            
            if (os != null) {
                pw = new PrintWriter(os);
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            String line = null;
            try {
                while ( (line = br.readLine()) != null) {
                    if (pw != null) {
                        pw.println(line);
                    }
                    System.out.println(type + ">" + line);
                }
                if (pw != null) {
                    pw.flush();
                }
            } catch (IOException e) {
                /*
                 * What to do here?
                 */
                e.printStackTrace();
            }
        }
    }
}
