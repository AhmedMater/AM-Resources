package am.queues.senders;

import am.data.hibernate.model.Notification;
import am.main.api.AppLogger;
import am.main.data.enums.Interface;
import am.main.exception.GeneralException;
import am.main.session.AppSession;
import am.services.NotificationService;

import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import static am.common.NotificationParam.SMS_NTF_QUEUE;
import static am.common.NotificationParam.SOURCE;
import static am.data.enums.am.impl.ANP.SMS_NTF;
import static am.main.data.enums.impl.AME.E_JMS_5;

/**
 * Created by ahmed.motair on 1/17/2018.
 */
@MessageDriven(mappedName = SMS_NTF_QUEUE)
public class SMSListener implements MessageListener {
    private final String CLASS = SMSListener.class.getSimpleName();

    @Resource private MessageDrivenContext context;
    @Inject private AppLogger logger;
    @Inject private NotificationService notificationService;

    @Override
    public void onMessage(Message message) {
        String METHOD = "onMessage";
        AppSession session = new AppSession(SOURCE, Interface.JMS, SMS_NTF);
        try {
            String jmsID = message.getJMSMessageID();
            session.setId(jmsID);

            if (message instanceof ObjectMessage) {
                Notification notification = message.getBody(Notification.class);
                logger.startDebug(session, notification);

                notificationService.sendSMSNotification(session, notification);

                logger.endDebug(session);
            }else
                throw new GeneralException(session, E_JMS_5, SMS_NTF_QUEUE);

        }catch (Exception ex){
            logger.error(session, ex);
            context.setRollbackOnly();
        }

    }
}
