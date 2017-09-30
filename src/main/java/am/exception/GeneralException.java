package am.exception;

import am.api.enums.EC;
import am.common.enums.AME;
import am.session.AppSession;

import java.text.MessageFormat;

/**
 * Created by ahmed.motair on 1/29/2017.
 */
public class GeneralException extends Exception {
    private EC errorCode;
    private String CLASS;
    private String METHOD;

    public GeneralException(AppSession session, Throwable ex){
        super(session.toString(), ex);
        this.CLASS = session.getCLASS();
        this.METHOD = session.getMethod();
    }
    public GeneralException(AppSession session, EC errorCode, Object ... args){
        this(session, null, errorCode, args);
    }
    public GeneralException(AppSession session, Throwable ex, EC errorCode, Object ... args){
        super(session.toString() + session.getErrorMsg(errorCode, args), ex);
        this.errorCode = errorCode;
        this.CLASS = session.getCLASS();
        this.METHOD = session.getMethod();
    }

    public GeneralException(AppSession session, AME errorCode, Object ... args){
        this(session, null, errorCode, args);
    }
    public GeneralException(AppSession session, Throwable ex, AME errorCode, Object ... args){
        super(session.toString() + MessageFormat.format(errorCode.value(), args), ex);
        this.CLASS = session.getCLASS();
        this.METHOD = session.getMethod();
    }
}
