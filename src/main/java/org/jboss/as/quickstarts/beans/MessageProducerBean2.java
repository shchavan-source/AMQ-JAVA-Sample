package org.jboss.as.quickstarts.beans;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;

@Stateless
public class MessageProducerBean2 {

    @Inject
    @JMSConnectionFactory("java:/Amq7CF")
    private JMSContext context;

    public void SendMsg(String qName, int msgSize){
        System.out.println("Sending from: " + this.toString());
        byte[] bArr = new byte[msgSize];
//        JMSContext session = context.createContext();
//        session.createProducer().send(session.createQueue(qName), bArr);
//        session.close();
        context.createProducer().send(context.createQueue(qName), bArr);
//        context.close();
    }
}
