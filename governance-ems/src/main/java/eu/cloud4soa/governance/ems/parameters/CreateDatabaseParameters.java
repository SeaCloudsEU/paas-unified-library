package eu.cloud4soa.governance.ems.parameters;

/**
 * Parameters for CreateDatabaseOperations.
 * 
 * The properties are:
 * <li>database: db name
 * <li>user: username to create
 * <li>password: user's password
 * <li>type: a string (in the cloud provider domain) that indicates the type of database to create.
 * 
 *
 */
public class CreateDatabaseParameters {

    String database;
    String user;
    String password;
    String type;
    
    public String getDatabase() {
        return database;
    }
    public void setDatabase(String database) {
        this.database = database;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
