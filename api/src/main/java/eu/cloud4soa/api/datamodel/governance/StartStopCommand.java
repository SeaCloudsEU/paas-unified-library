package eu.cloud4soa.api.datamodel.governance;


public enum StartStopCommand {
    
    START(Strings.START), 
    STOP(Strings.STOP);

    public static class Strings {
        public static final String START = "start";
        public static final String STOP = "stop";
    }
    
    private final String operation;
    
    StartStopCommand(String operation) {
        this.operation = operation;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public StartStopCommand from(String command) {

        return Enum.valueOf(StartStopCommand.class, command);
    }
}