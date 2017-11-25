package am.main.api;

import am.main.common.ConfigParam;
import am.main.common.ConfigParam.COMPONENT;
import am.main.common.ConfigParam.FILE;
import am.main.common.ConfigUtils;
import am.main.data.enums.AME;
import am.main.data.enums.AMI;
import am.main.core.config.AMConfigurationManager;
import am.main.data.enums.AM_CC;
import am.main.exception.GeneralException;
import am.main.session.AppSession;
import am.main.data.enums.Source;
import am.shared.enums.App_CC;
import am.shared.session.Phase;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Properties;

import static am.shared.session.Phase.AM_LIBRARY;


/**
 * Created by ahmed.motair on 9/9/2017.
 */

@Singleton
public class AppConfigManager {
    @Inject private AMConfigurationManager amConfigManager;
    @Inject private AppLogger logger;
    private static final String CLASS = "AppConfigManager";
    private static Properties APP_CONFIGURATION = new Properties();
    private static AppConfigManager instance;
    private static String FILE_NAME;

    private AppConfigManager() {

    }

    public static AppConfigManager getInstance() throws Exception{
        if (instance == null){
            synchronized(AppConfigManager.class){
                if (instance == null){
                    instance = new AppConfigManager();
                    instance.load();
                }
            }
        }
        return instance;
    }

    @PostConstruct
    private void load(){
        String FN_NAME = "load";
        AppSession session = new AppSession(Source.AM, Phase.AM_INITIALIZING, CLASS, FN_NAME);

        FILE_NAME = amConfigManager.getConfigValue(session, AM_CC.APP_CONFIG);
        FILE.APP_CONFIG_PROPERTIES = ConfigParam.APP_CONFIG_PATH + FILE_NAME;
        APP_CONFIGURATION = ConfigUtils.loadSystemComponent(session, FILE.APP_CONFIG_PROPERTIES, COMPONENT.APP_CONFIG_MANAGER);

        //TODO: Check if the File Not Found Log Message that it has to be with the name in the Property File
    }

    public <T> T getConfigValue(AppSession appSession, App_CC systemProperty, Class<T> className) throws Exception {
        String FN_NAME = "getConfigValue";
        AppSession session = appSession.updateSession(AM_LIBRARY, CLASS, FN_NAME);
        try {
            logger.startDebug(session, systemProperty, className.getSimpleName());

            if(systemProperty == null || systemProperty.toString() == null || systemProperty.toString().isEmpty())
                throw new GeneralException(session, AME.SYS_007, "App Config Property");
            else if(APP_CONFIGURATION == null || APP_CONFIGURATION.isEmpty())
                throw new GeneralException(session, AME.IO_005, FILE_NAME);
            else if(className == null)
                throw new GeneralException(session, AME.SYS_008);

            T retValue;
            String value = ConfigUtils.readValueFromPropertyFile(session, APP_CONFIGURATION, systemProperty.value(), FILE_NAME);
//                    APP_CONFIGURATION.getProperty(systemProperty.value());

            if (className == String.class) {
                retValue = (T) value;
            }else if (className == Integer.class)
                retValue = (T) new Integer(Integer.parseInt(value));
            else if (className == Float.class)
                retValue = (T) new Float(Float.parseFloat(value));
            else if (className == Boolean.class)
                retValue = (T) new Boolean(Boolean.parseBoolean(value));
            else
                throw new GeneralException(session, AME.SYS_009, className.getSimpleName());

            logger.info(session, AMI.IO_003, "App Config Property", systemProperty);
            logger.endDebug(session, retValue);
            return retValue;
        }catch (Exception ex){
            if(ex instanceof GeneralException)
                throw ex;
            else
                throw new GeneralException(session, ex, AME.IO_008, "App Config Property", systemProperty);
        }
    }

    public boolean updateConfigValue(AppSession appSession, App_CC systemProperty, String value) throws Exception {
        String FN_NAME = "updateConfigValue";
        AppSession session = appSession.updateSession(AM_LIBRARY, CLASS, FN_NAME);
        try {
            logger.startDebug(session, systemProperty, value);

            if(systemProperty == null || systemProperty.toString() == null || systemProperty.toString().isEmpty())
                throw new GeneralException(session, AME.SYS_007, "App Config Property");
            else if(APP_CONFIGURATION == null || APP_CONFIGURATION.isEmpty())
                throw new GeneralException(session, AME.IO_005, FILE_NAME);
            else if(value == null)
                throw new GeneralException(session, AME.SYS_016);

            Object retValue = APP_CONFIGURATION.replace(systemProperty.value(), value);

            boolean result = (retValue != null);

            logger.info(session, AMI.IO_004, "App Config Property", systemProperty);
            logger.endDebug(session, result);
            return result;
        }catch (Exception ex){
            if(ex instanceof GeneralException)
                throw ex;
            else
                throw new GeneralException(session, ex, AME.IO_011, "App Config Property", systemProperty);
        }
    }

}