package am.main.api;

import am.main.common.ConfigParam;
import am.main.common.ConfigUtils;
import am.main.data.dto.AMLog4j2Data;
import am.main.data.enums.impl.AMP;
import am.main.data.enums.impl.AMQ;
import am.main.data.enums.logger.LogConfigProp;
import am.main.data.jaxb.am.logger.*;
import am.main.data.jaxb.log4jData.Configuration;
import am.main.data.jaxb.log4jData.RollingFile;
import am.main.exception.GeneralException;
import am.main.session.AppSession;
import am.main.spi.AMCode;
import am.main.spi.AMPhase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static am.main.common.ConfigParam.*;
import static am.main.data.enums.Interface.INITIALIZING_COMPONENT;
import static am.main.data.enums.impl.AMP.AM_INITIALIZATION;
import static am.main.data.enums.impl.AMP.APP_LOGGER;
import static am.main.data.enums.impl.AMS.AM;
import static am.main.data.enums.impl.AMS.AM_LOGGER;
import static am.main.data.enums.impl.IEC.*;
import static am.main.data.enums.impl.IIC.*;
import static am.main.data.enums.impl.IWC.*;
import static am.main.data.enums.logger.LogConfigProp.LOG_LEVEL_FOR_ALL;
import static am.main.data.enums.logger.LogConfigProp.USE_AM_LOGGER;
import static am.main.data.enums.logger.LoggerLevels.*;
import static java.nio.charset.StandardCharsets.UTF_8;

@Singleton
public class AppLogger implements Serializable{

    private static final String CLASS = AppLogger.class.getSimpleName();

    public Logger FAILURE_LOGGER = LogManager.getLogger("Failure");
    private Logger INITIAL_LOGGER = LogManager.getLogger("Initial");
    private Map<String, Logger> loggers = new HashMap<>();

    private static final String LOG4J2_FILE_NAME = "/log4j2.xml";
    private static final String TEMPLATE = "Template";

    private static AppLogger instance;
//    private static final

    public AppLogger() {
    }

    public static AppLogger getInstance(){
        if (instance == null){
            synchronized(AppLogger.class){
                if (instance == null){
                    instance = new AppLogger();
                    instance.load();
                }
            }
        }
        return instance;
    }

    @PostConstruct
    private void load() {
        String METHOD = "load";
        AppSession session = new AppSession(AM, INITIALIZING_COMPONENT, AM_INITIALIZATION, CLASS, METHOD);
        String componentName = COMPONENT.APP_LOGGER;
        try {
//            INITIAL_LOGGER.info(session + I_SYS_1.getFullMsg(LOGGER_CONFIG.COMPONENT_NAME));
            this.info(session, I_SYS_1, componentName);

            Configuration log4j2 = ConfigUtils.readResourceXMLFile(session, this, Configuration.class,  "/TemplateLog4j2.xml");
//            INITIAL_LOGGER.info(session + I_LOG_1.getFullMsg());
            this.info(session, I_LOG_1);

            RollingFile templateRolling = new RollingFile();
            for (RollingFile rollingFile :log4j2.getAppenders().getRollingFile())
                if(rollingFile.getName().equals(TEMPLATE))
                    templateRolling = rollingFile;

            am.main.data.jaxb.log4jData.Logger templateLogger = new am.main.data.jaxb.log4jData.Logger();
            for (am.main.data.jaxb.log4jData.Logger logger : log4j2.getLoggers().getLogger())
                if (logger.getName().equals(TEMPLATE))
                    templateLogger = logger;

            List<AMApplication> applicationList = loadExternalConfig(session);

            for (AMApplication application : applicationList) {
                if(application.getAMLoggerConfig().getLoggerProperty(USE_AM_LOGGER.getName()).getValue().equals("true"))
                    continue;

                for (LoggerGroup group : application.getLoggerGroup()) {
                    log4j2.addNewLogger(session, group, this, application.getAMLoggerConfig(), templateLogger, templateRolling);
                    this.info(session, I_LOG_4, group.getName());
                }
            }

//                List<LoggerProperty> propertyList = loggerConfig.getAMLoggerConfig().getLoggerProperty();
//                HashMap<String, String> properties = new HashMap<>();
//                for (LoggerProperty property : propertyList)
//                    properties.put(property.getName(), property.getValue());
//
//                List<LoggerGroup> loggerGroupList = loggerConfig.getLoggerGroup();
//                for (LoggerGroup group : loggerGroupList) {
//                    log4j2.addNewLogger(session, group, this, properties, templateLogger, templateRolling);
////                INITIAL_LOGGER.info(session + I_LOG_4.getFullMsg(group.getName()));
//                    this.info(session, I_LOG_4, group.getName());
//                }


            writeLog4J2File(session, log4j2);
            this.info(session, I_LOG_5);

            //To Reload the Log4j2.xml
            ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
            this.info(session, I_LOG_6);

            for (AMApplication application : applicationList) {
                if(application.getAMLoggerConfig().getLoggerProperty(USE_AM_LOGGER.getName()).getValue().equals("true"))
                    continue;

                for (LoggerGroup group : application.getLoggerGroup())
                    for (LoggerData loggerData: group.getLoggerData()){
                        loggers.put(loggerData.getName(), LogManager.getLogger(loggerData.getName()));
                        this.info(session, I_LOG_7, loggerData.getName());
                    }
            }

            this.info(session, I_LOG_8);

//            INITIAL_LOGGER.info(session + I_SYS_2.getFullMsg(LOGGER_CONFIG.COMPONENT_NAME));
            this.info(session, I_SYS_2, componentName);
        } catch (Exception ex) {
            this.error(session, ex, E_SYS_1, componentName, ex.getMessage());
//            FAILURE_LOGGER.error(session + E_SYS_1.getFullMsg(LOGGER_CONFIG.COMPONENT_NAME, ex.getMessage()), ex);
            throw new IllegalStateException(session + E_SYS_1.getFullMsg(componentName, ex.getMessage()));
        }
    }

    private static final Boolean USE_DEFAULT_LOGGER = false;
    private static final Boolean USE_AM_LOGGER_APP = false;

    private AMApplication loadInternalConfig(AppSession appSession, AMApplication externalLogConfig) throws Exception{
        String METHOD = "loadInternalConfig";
        AppSession session = appSession.updateSession(CLASS, METHOD);
        try {
            this.startDebug(session);
            this.info(session, I_LOG_12);

            AMApplication amLogConfig = createAMLoggerConfig(session);

            AMLoggerConfig externalConfig = externalLogConfig.getAMLoggerConfig();
            AMLoggerConfig internalConfig = amLogConfig.getAMLoggerConfig();

            for (LoggerProperty internalProp : internalConfig.getLoggerProperty()) {
                boolean found = false;

                for (LoggerProperty externalProp : externalConfig.getLoggerProperty()) {
                    LogConfigProp prop;
                    try {
                        prop = LogConfigProp.getLogConfigProp(externalProp.getName());
                    }catch (IllegalArgumentException exc){
                        this.warn(session, W_LOG_11, externalProp.getName());
                        continue;
                    }

                    if(internalProp.equals(externalProp)){
                        found = true;

                        if(!externalProp.getValue().matches(prop.getRegex()))
                            this.warn(session, W_LOG_8, externalProp.getValue(), externalProp.getName(), prop.getDefaultValue());
                        else{
                            internalProp.setValue(externalProp.getValue());
                            this.info(session, I_LOG_16, externalProp.getName(), externalProp.getValue());
                        }
                    }
                }

                if(!found)
                    this.warn(session, W_LOG_7, internalProp.getName(), internalProp.getValue());
            }

            this.info(session, I_LOG_13);
            this.endDebug(session, amLogConfig);
            return amLogConfig;
        }catch (Exception ex){
            if(ex instanceof GeneralException)
                throw ex;
            else
                throw new GeneralException(session, ex, E_LOG_2);
        }
    }

    private AMApplication createAMLoggerConfig(AppSession appSession) throws Exception{
        String METHOD = "createAMLoggerConfig";
        AppSession session = appSession.updateSession(CLASS, METHOD);
        this.startDebug(session);

        AMLoggerConfig config = new AMLoggerConfig();
        for (LogConfigProp logConfigProp :LogConfigProp.values())
            config.getLoggerProperty().add(new LoggerProperty(logConfigProp.getName(), logConfigProp.getDefaultValue()));

        LoggerGroup loggerGroup = new LoggerGroup();
        loggerGroup.setName(APP_LOGGER.getCategory());

        HashMap<String, AMPhase> amPhaseHashMap = AMP.getALL_PHASES();
        for (String phase : amPhaseHashMap.keySet())
            loggerGroup.getLoggerData().add(new LoggerData(phase, amPhaseHashMap.get(phase).getDefaultLogLevel()));

        AMApplication amLogConfig = new AMApplication();
        amLogConfig.setAMLoggerConfig(config);
        amLogConfig.setName(AM_RESOURCE_NAME);
        amLogConfig.setType("JAR");
        amLogConfig.getLoggerGroup().add(loggerGroup);

        this.endDebug(session, amLogConfig);
        return amLogConfig;
    }
    private AMApplication createAppLoggerConfig(AppSession appSession, AMApplication amConfig) throws Exception{
        String METHOD = "createAppLoggerConfig";
        AppSession session = appSession.updateSession(CLASS, METHOD);
        this.startDebug(session, amConfig);

        AMApplication appLogConfig = new AMApplication();
        appLogConfig.setName(APP_NAME);
        appLogConfig.setAMLoggerConfig(amConfig.getAMLoggerConfig());

        LoggerGroup group = new LoggerGroup();
        group.setName(APP_NAME);
        group.getLoggerData().add(new LoggerData(APP_NAME, "info"));

        appLogConfig.getLoggerGroup().add(group);
        this.info(session, I_LOG_15, APP_NAME);

        this.endDebug(session, appLogConfig);
        return appLogConfig;
    }

    private List<AMApplication> loadExternalConfig(AppSession appSession) throws Exception{
        String METHOD = "loadExternalConfig";
        AppSession session = appSession.updateSession(CLASS, METHOD);
        try {
            this.startDebug(session);
            this.info(session, I_LOG_9, APP_NAME);
            AMApplication amLogConfig = null;
            AMApplication appLogConfig = null;

            try {
                AMLogger appLoggerConfig = ConfigUtils.readRemoteXMLFile(session, this, AMLogger.class, ConfigParam.LOGGER_CONFIG.FN_PATH);
                this.info(session, I_LOG_2);

                List<AMApplication> applicationList = appLoggerConfig.getAMApplication();

                if(applicationList.size() == 0) {
                    this.warn(session, W_LOG_2);
                    amLogConfig = loadInternalConfig(session, null);
                    appLogConfig = createAppLoggerConfig(session, amLogConfig);
                }else {

                    //Check for the AM-Resources Application Tag
                    for (AMApplication app : applicationList) {
                        if (app.getName().equals(AM_RESOURCE_NAME)) {
                            amLogConfig = app;
                            break;
                        }
                    }

                    amLogConfig = loadInternalConfig(session, amLogConfig);

                    //Check for the Application Tag
                    for (AMApplication app : applicationList) {
                        if (app.getName().equals(APP_NAME)) {
                            appLogConfig = app;
                            break;
                        }
                    }

                    if (appLogConfig == null) {
                        this.warn(session, W_LOG_2, APP_NAME);
                        appLogConfig = createAppLoggerConfig(session, amLogConfig);
                    }else{
                        AMLoggerConfig appConfig = appLogConfig.getAMLoggerConfig();

                        if(appConfig == null)
                            appLogConfig.setAMLoggerConfig(amLogConfig.getAMLoggerConfig());
                        else{
                            //Check if there is missing configuration
                            for (LoggerProperty amProperty :amLogConfig.getAMLoggerConfig().getLoggerProperty()) {
                                boolean found = true;

                                for (LoggerProperty fileProperty :appConfig.getLoggerProperty()) {
                                    if(fileProperty.equals(amProperty)) {
                                        found = true;

                                        LogConfigProp logConfigProp = LogConfigProp.getLogConfigProp(amProperty.getName());
                                        if(!fileProperty.getValue().matches(logConfigProp.getRegex())){
                                            this.warn(session, W_LOG_8, fileProperty.getValue(), fileProperty.getName(), logConfigProp.getDefaultValue());
                                            fileProperty.setValue(logConfigProp.getDefaultValue());
                                        }else
                                            this.info(session, I_LOG_14, fileProperty.getName());

                                        break;
                                    }
                                }

                                if(!found){
                                    this.warn(session, W_LOG_7, amProperty.getName());
                                    appConfig.getLoggerProperty().add(new LoggerProperty(amProperty));
                                }
                            }
                        }
                    }
                }
            }catch (GeneralException exc){
                if(exc.getErrorCode().equals(E_IO_3)) {
                    this.warn(session, W_LOG_1);
                    amLogConfig = loadInternalConfig(session, null);
                    appLogConfig = createAppLoggerConfig(session, amLogConfig);
                }else
                    throw exc;
            }

            LoggerProperty property = amLogConfig.getAMLoggerConfig().getLoggerProperty(LOG_LEVEL_FOR_ALL.getName());
            if(property != null & !property.getValue().equals(LOG_LEVEL_FOR_ALL.getDefaultValue()))
                for (LoggerGroup group :amLogConfig.getLoggerGroup())
                    for (LoggerData logger : group.getLoggerData())
                        logger.setLevel(property.getValue());

            property = appLogConfig.getAMLoggerConfig().getLoggerProperty(LOG_LEVEL_FOR_ALL.getName());
            if(property != null & !property.getValue().equals(LOG_LEVEL_FOR_ALL.getDefaultValue()))
                for (LoggerGroup group :appLogConfig.getLoggerGroup())
                    for (LoggerData logger : group.getLoggerData())
                        logger.setLevel(property.getValue());

            List<AMApplication> applicationList = new ArrayList<>();
            applicationList.add(amLogConfig);
            applicationList.add(appLogConfig);

            this.info(session, I_LOG_10, APP_NAME);
            this.endDebug(session, applicationList);
            return applicationList;
        }catch (Exception ex){
            if(ex instanceof GeneralException)
                throw ex;
            else
                throw new GeneralException(session, ex, E_LOG_1, APP_NAME);
        }
    }

    private void writeLog4J2File(AppSession appSession, Configuration log4j2) throws Exception{
        String METHOD = "writeLog4J2File";
        AppSession session = appSession.updateSession(CLASS, METHOD);
        try {
            this.startDebug(session, log4j2);
            this.info(session, I_IO_5, LOG4J2_FILE_NAME);

            String xml = XMLHandler.compose(log4j2, Configuration.class);

            try {
                PrintWriter writer = new PrintWriter(AppLogger.class.getResource(LOG4J2_FILE_NAME).getPath(), UTF_8.displayName());
                writer.write(xml);
                writer.flush();
            }catch (FileNotFoundException | NullPointerException e){
                throw new GeneralException(session, e, E_IO_3, LOG4J2_FILE_NAME);
            } catch (SecurityException e) {
                throw new GeneralException(session, e, E_IO_6, LOG4J2_FILE_NAME);
            }

            this.info(session, I_IO_6, LOG4J2_FILE_NAME);
            this.endDebug(session);
        }catch (Exception ex){
            if(ex instanceof GeneralException)
                throw ex;
            else
                throw new GeneralException(session, ex, E_IO_7, LOG4J2_FILE_NAME);
        }
    }

    public void error(AppSession session, Exception ex) {
        AMLog4j2Data logData = new AMLog4j2Data(session, ERROR_EX, ex);
        log(session, logData);
    }
    public void error(AppSession session, AMCode amCode, Object ... args) {
        AMLog4j2Data logData = new AMLog4j2Data(session, ERROR_MSG, amCode, args);
        log(session, logData);
    }
    public void error(AppSession session, Exception ex, AMCode ec, Object ... args) {
        AMLog4j2Data logData = new AMLog4j2Data(session, ERROR_MSG_EX, ex, ec, args);
        log(session, logData);
    }

    public void info(AppSession session, AMCode amCode, Object ... args){
        AMLog4j2Data logData = new AMLog4j2Data(session, INFO, amCode, args);
        log(session, logData);
    }

    public void warn(AppSession session, AMCode amCode, Object ... args){
        AMLog4j2Data logData = new AMLog4j2Data(session, WARN, amCode, args);
        log(session, logData);
    }

    public void startDebug(AppSession session, Object ... args){
        AMLog4j2Data logData = new AMLog4j2Data(session, args);
        log(session, logData);
    }
    public void endDebug(AppSession session){
        AMLog4j2Data logData = new AMLog4j2Data(session);
        log(session, logData);
    }
    public void endDebug(AppSession session, Object result){
        AMLog4j2Data logData = new AMLog4j2Data(session, result);
        log(session, logData);
    }

    public void log(AppSession session, AMLog4j2Data logData){
        if(session == null || session.getPHASE() == null || session.getSOURCE() == null)
            logData.logMsg(null, this, FAILURE_LOGGER);
        else{
            if(session.getPHASE().equals(AM_INITIALIZATION))
                logData.logMsg(session.getMessageHandler(), this, INITIAL_LOGGER);
//            else if(session.getPHASE().getCategory().equals(Category.AM.name())) {
//                Logger logger = amLoggers.get(logData.getPHASE().getName());
//                logData.logMsg(this, (logger != null ? logger : FAILURE_LOGGER));
            else if (USE_AM_LOGGER_APP || session.getSOURCE().getName().equals(AM_LOGGER.getName())) {
                Logger logger = loggers.get(logData.getPHASE());
                logger = (logger == null) ? loggers.get(APP_NAME) : logger;
                logData.logMsg(session.getMessageHandler(), this,
                        (logger != null ? logger : FAILURE_LOGGER));
            } else {
                try {
                    jmsManager.get().sendMessage(AMQ.LOG4J2, logData);
                } catch (Exception exc) {
                    logData.logMsg(session.getMessageHandler(), this, FAILURE_LOGGER);
                    FAILURE_LOGGER.error(exc);
                }
            }
        }
    }


    @Inject
    private Provider<JMSManager> jmsManager;

//    public void setJmsManager(JMSManager jmsManager) {
//        this.jmsManager = jmsManager;
//    }
//    public JMSManager getJmsManager() {
//        return jmsManager;
//    }
}
