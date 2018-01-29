package am.main.data.dto;

import am.main.api.AppLogger;
import am.main.api.MessageHandler;
import am.main.data.enums.logger.LoggerLevels;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.main.spi.AMCode;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static am.main.data.enums.logger.LoggerLevels.ERROR_EX;
import static am.main.data.enums.logger.LoggerLevels.ERROR_MSG_EX;

/**
 * Created by ahmed.motair on 1/5/2018.
 */
public class AMLog4j2Data implements Serializable{
    private static final SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private LoggerLevels LEVEL;
    private String fullCode;
    private List<String> args = new ArrayList<>();
    private Date currentTimeStamp;
    private Exception ex;

    //Session Attributes
    private String PHASE;
    private String INTERFACE;
    private String SOURCE;
    private String THREAD_ID;
    private String THREAD_NAME;
    private String CLASS;
    private String METHOD;
    private String interfaceRelatedID;
    private String username;
    private String IP;

    private String fullMsg = "";

    public AMLog4j2Data() {
        this.currentTimeStamp = new Date();
    }
    public AMLog4j2Data(AppSession session, LoggerLevels level, Exception ex, AMCode code, Object ... args){
        this.LEVEL = level;
        setSession(session);
        switch (level){
            case WARN: case INTERNAL_INFO: case INFO: case ERROR_MSG: case INTERNAL_ERROR_MSG:
                this.fullCode = code.getFullCode(); break;
            case ERROR_MSG_EX:  case INTERNAL_ERROR_MSG_EX:
                setEx(ex); this.fullCode = code.getFullCode(); break;
            case ERROR_EX:
                setEx(ex); break;
        }
        this.setArgs(args);
        this.currentTimeStamp = new Date();
    }
    public AMLog4j2Data(AppSession session, LoggerLevels level, AMCode code, Object ... args) {
        this(session, level, null, code, args);
    }
    public AMLog4j2Data(AppSession session, Exception ex) {
        this(session, ERROR_EX, ex, null);
    }
    public AMLog4j2Data(AppSession session, Exception ex, AMCode errorCode, Object ... args) {
        this(session, ERROR_MSG_EX, ex, errorCode, args);
    }

    public AMLog4j2Data(AppSession session, Object ... args){
        setSession(session);
        setArgs(args);
        this.LEVEL = LoggerLevels.ST_DEBUG;
        this.currentTimeStamp = new Date();
    }
    public AMLog4j2Data(AppSession session){
        setSession(session);
        this.LEVEL = LoggerLevels.EN_DEBUG;
    }
    public AMLog4j2Data(AppSession session, Object result){
        setSession(session);
        setEndDebugArgs(result);
        this.LEVEL = LoggerLevels.EN_DEBUG;
        this.currentTimeStamp = new Date();
    }

    public void setSession(AppSession session) {
        this.PHASE = session.getPHASE().getName();
        this.INTERFACE = session.getINTERFACE().value();
        this.SOURCE = session.getSOURCE().getName();
        this.CLASS = session.getCLASS();
        this.METHOD = session.getMETHOD();
        this.THREAD_ID = session.getTHREAD_ID();
        this.THREAD_NAME = session.getTHREAD_NAME();
        this.interfaceRelatedID = session.getId();
        this.username = session.getUsername();
        this.IP = session.getIp();
    }

    public LoggerLevels getLEVEL() {
        return LEVEL;
    }
    public void setLEVEL(LoggerLevels LEVEL) {
        this.LEVEL = LEVEL;
    }

    public List<String> getArgs() {
        return args;
    }
    public void setEndDebugArgs(Object result) {
        this.args.add((result != null ? result.toString() : "Null"));
    }
    public void setArgs(Object[] args) {
        if(args != null)
            for (Object arg : args)
                this.args.add(arg.toString());
    }

    public Exception getEx() {
        return ex;
    }
    public void setEx(Exception ex) {
        if(ex instanceof BusinessException) {
            this.ex = new Exception(((BusinessException) ex).getFullErrMsg());
            this.ex.setStackTrace(ex.getStackTrace());
        }else
            this.ex = ex;
//        this.ex = (ex instanceof BusinessException) ? new SerializedBusinessException((BusinessException) ex) : ex;
//        StackTraceElement[] stackTraceElements = ex.getStackTrace();
    }

    public String getFullCode() {
        return fullCode;
    }
    public void setFullCode(String fullCode) {
        this.fullCode = fullCode;
    }

    public Date getCurrentTimeStamp() {
        return currentTimeStamp;
    }
    public void setCurrentTimeStamp(Date currentTimeStamp) {
        this.currentTimeStamp = currentTimeStamp;
    }

    public String getPHASE() {
        return PHASE;
    }
    public void setPHASE(String PHASE) {
        this.PHASE = PHASE;
    }

    public String getINTERFACE() {
        return INTERFACE;
    }
    public void setINTERFACE(String INTERFACE) {
        this.INTERFACE = INTERFACE;
    }

    public String getSOURCE() {
        return SOURCE;
    }
    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    public String getTHREAD_ID() {
        return THREAD_ID;
    }
    public void setTHREAD_ID(String THREAD_ID) {
        this.THREAD_ID = THREAD_ID;
    }

    public String getTHREAD_NAME() {
        return THREAD_NAME;
    }
    public void setTHREAD_NAME(String THREAD_NAME) {
        this.THREAD_NAME = THREAD_NAME;
    }

    public String getCLASS() {
        return CLASS;
    }
    public void setCLASS(String CLASS) {
        this.CLASS = CLASS;
    }

    public String getMETHOD() {
        return METHOD;
    }
    public void setMETHOD(String METHOD) {
        this.METHOD = METHOD;
    }

    public String getInterfaceRelatedID() {
        return interfaceRelatedID;
    }
    public void setInterfaceRelatedID(String interfaceRelatedID) {
        this.interfaceRelatedID = interfaceRelatedID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getIP() {
        return IP;
    }
    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getFullMsg() {
        return fullMsg;
    }
    public void setFullMsg(String fullMsg) {
        this.fullMsg = fullMsg;
    }

//    private void generateCodeMsg(MessageHandler messageHandler, AppLogger appLogger) throws Exception{
//        AMCode code = AMCode.getCodeByFullCode(fullCode);
//        this.fullMsg = code.getFullMsg(messageHandler, args);
//    }
//    private void generateErrorMsg(MessageHandler messageHandler, AppLogger appLogger) throws Exception{
//        String errMsg = "";
//        if(fullCode != null){
//            AMCode code = AMCode.getCodeByFullCode(fullCode);
//            fullMsg += code.getFullMsg(messageHandler, args);
//        }
//
//        if (ex != null)
//            fullMsg += ",\nDue to: " + ex.getMessage();
//    }
//    private void generateDebug(AppLogger appLogger) throws Exception{
//        switch (this.LEVEL) {
//            case ST_DEBUG:
//                fullMsg = "Started with " +
//                    ((args.size() == 0) ? "No Arguments" : "Arguments = " + Arrays.toString(args.toArray()) + "\n");
//                break;
//            case EN_DEBUG:
//                fullMsg = "Ended " + (args.size() == 1 ?
//                        "with Results = " + args.get(0) : "[Void Function]");
//                break;
////            default:
////                appLogger.FAILURE_LOGGER.error(session.toString() + SYS_017.value());
////                return false;
//
//        }
////        return true;
//    }
//    private boolean generateWarning(MessageHandler messageHandler, AppLogger appLogger) throws Exception{
//        if (session != null) {
//            fullMsg = session.getWarnMsg(wc, args);
//            return true;
//        }else {
//            appLogger.FAILURE_LOGGER.error(session.toString() +
//                    MessageFormat.format(AME.SYS_011.value(), "Warn", "Error", ec.toString()));
//            return false;
//        }
//    }


    private void generateFullMsg(MessageHandler messageHandler) throws Exception{
        switch (this.LEVEL){
            case ST_DEBUG:
                fullMsg = "Started with " +
                        ((args.size() == 0) ? "No Arguments" : "Arguments = " + Arrays.toString(args.toArray()) + "\n");
                break;
            case EN_DEBUG:
                fullMsg = "Ended " + (args.size() == 1 ?
                        "with Results = " + args.get(0) : "[Void Function]");
                break;
            case ERROR_EX:
                break;
            default:
                AMCode code = AMCode.getCodeByFullCode(fullCode);
                fullMsg += code.getFullMsg(messageHandler, args);
        }

//        if (ex != null)
//            fullMsg += ",\nDue to: " + ex.getMessage();
    }
    public void logMsg(MessageHandler messageHandler, AppLogger appLogger, Logger logger){
        try {
            String header = generateHeader();

            if (logger == null)
                appLogger.FAILURE_LOGGER.error(header + "LoggerData Passed to log the Message is Null");

            generateFullMsg(messageHandler);
            switch (LEVEL) {
                case INFO: case INTERNAL_INFO:
//                    generateCodeMsg(messageHandler, appLogger);
                    logger.info(header + fullMsg);
                    break;
                case ERROR_EX:
                    logger.error(header,ex);
                case INTERNAL_ERROR_MSG:
                case ERROR_MSG_EX: case INTERNAL_ERROR_MSG_EX:
//                    generateErrorMsg(messageHandler, appLogger);

//                    if (ex instanceof SerializedBusinessException)
//                        logger.error(header + fullMsg);
//                    else
                        logger.error(header + fullMsg, ex);

                    break;
                case ERROR_MSG:
//                    generateCodeMsg(messageHandler, appLogger);
                    logger.error(header + fullMsg);
                    break;
//                case ERROR_MSG_EX:
//                    if (generateErrorMsg(messageHandler, appLogger)) {
//                        if (ex instanceof SerializedBusinessException)
//                            logger.error(header + fullMsg);
//                        else
//                            logger.error(header + fullMsg, ex);
//                    }
//                    break;
                case ST_DEBUG:
                case EN_DEBUG:
//                    generateDebug(appLogger);
                    logger.debug(header + fullMsg);
                    break;
                case WARN:
//                    generateCodeMsg(messageHandler, appLogger);
                    logger.warn(header + fullMsg);
                    break;
//                default:
//                    appLogger.FAILURE_LOGGER.error(header + SYS_017.value());
            }
        }catch (Exception ex){
            appLogger.FAILURE_LOGGER.error(ex);
        }
    }

    private String generateHeader() {
        String st = "[" + sdtf.format(currentTimeStamp) + "] ";

        if(THREAD_NAME != null)
            st += "[Thread-Name: " + THREAD_NAME;

        if(THREAD_ID != null)
            st += "] [Thread-ID: " + THREAD_ID;

        if(SOURCE != null)
            st += "] [Source: " + SOURCE;

        if(INTERFACE != null)
            st += "] [Interface: " + INTERFACE;

        if(IP != null)
            st += "] [IP: " + IP;

        if(username != null)
            st += "] [User: " + username;

        if(interfaceRelatedID !=null)
            st += "] [ID: " + interfaceRelatedID;

        st += "]\n " + CLASS + "." + METHOD + "(): ";
        return st;
    }
}
