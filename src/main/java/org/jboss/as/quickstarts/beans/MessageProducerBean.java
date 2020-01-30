package org.jboss.as.quickstarts.beans;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;

@Stateless
public class MessageProducerBean {

    @Resource
    @JMSConnectionFactory("java:/Amq7CF")
    private ConnectionFactory context;

    public void SendMsg(String qName, int msgSize){
        byte[] bArr = new byte[msgSize];
        JMSContext session = context.createContext();
        session.createProducer().send(session.createQueue(qName), bArr);
        session.close();
    }
}
