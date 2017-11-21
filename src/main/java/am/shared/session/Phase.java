package am.shared.session;

/**
 * Created by ahmed.motair on 9/20/2017.
 */
public enum Phase {
    AM_INITIALIZING("AM-Initializing"),
    INTEGRATION_TEST("Integration-Test"),

    AUTHENTICATION("Authentication"),
    AUTHORIZATION("Authorization"),
    URL_LOGGING("URLLogging"),

    COURSE_CREATE("Course-Create"),
    COURSE_VIEW("Course-View"),
    COURSE_UPDATE("Course-Update"),

    ARTICLE_CREATE("Article-Create"),
    ARTICLE_VIEW("Article-View"),
    ARTICLE_UPDATE("Article-Update"),

    REGISTRATION("Registration"),
    LOGIN("Login");

    private String value;

    Phase(String value){
        this.value = value;
    }
    public String value(){return value;}

}
