package am.core.logger;

/**
 * Created by ahmed.motair on 9/21/2017.
 */
public enum AMI {
    SYS_001("AM-SYS-001: Message: ''{0}'' is formatted successfully"),
    SYS_002("AM-SYS-002: Application Component: ''{0}'' is loaded successfully"),

    ENM_001("AM-ENM-001: Mail Session initiated Successfully"),
    ENM_002("AM-ENM-002: All Email Notification Configuration needed are found in the Property File"),
    ENM_003("AM-ENM-003: Email: ''{0}'' is sent successfully To: ''{1}''"),

    IO_001("AM-IO-001: Resource File: ''{0}'' is read successfully"),
    IO_002("AM-IO-002: Value of Property: ''{0}'' if read successfully from File:''{1}''"),
    IO_003("AM-IO-003: ''{0}'' of Code: ''{1}'' is read successfully");

    private String value;

    AMI(String value){
        this.value = value;
    }
    AMI(){
    }
    public String value(){
        return value;
    }
    @Override public String toString(){
        return value;
    }
}
