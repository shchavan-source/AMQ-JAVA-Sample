/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.servlet;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;


/**
 * <p>
 * A simple servlet 3 as client that sends several messages to a queue or a topic.
 * </p>
 *
 * <p>
 * The servlet is registered and mapped to /HelloWorldMDBServletClient using the {@linkplain WebServlet
 *
 * @author Serge Pagop (spagop@redhat.com)
 * @HttpServlet}. </p>
 */
@WebServlet("/HelloWorldMDBServletClient")
public class HelloWorldMDBServletClient extends HttpServlet {

    private static final long serialVersionUID = -8314035702649252239L;

    private static final int MSG_COUNT = 5;

    @Resource
    @JMSConnectionFactory("java:/Amq7CF")
    private ConnectionFactory context;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.write("<h1>Quickstart: Example demonstrates the use of <strong>JMS 2.0</strong> and <strong>EJB 3.2 Message-Driven Bean</strong> in JBoss EAP.</h1>");
        try {
            Map<String, String[]> paramMap = req.getParameterMap();
            String qName = paramMap.get("qName")[0];
//            final Destination destination = context.createQueue(qName);
            final int msgCnt = Integer.parseInt(paramMap.get("msgCnt")[0]);
            final int msgSize = Integer.parseInt(paramMap.get("msgSize")[0]);
            out.write("<p>Queue Name <em>" + qName + "</em></p>");
//            out.write("<p>Sending messages to <em>" + destination + "</em></p>");
            long startTime = System.currentTimeMillis();
            out.write("<h2>The following messages will be sent to the destination:</h2>");
            byte[] bArr = new byte[msgSize];
            int msgProdCnter = 0;
            for (int i = 0; i < msgCnt; i++) {
                try {
                    /*String text = "This is message " + (i + 1);
                    context.createProducer().send(destination, text);
                    out.write("Message (" + i + "): " + text + "</br>");*/
                    JMSContext session = context.createContext();
                    session.createProducer().send(session.createQueue(qName), bArr);
                    session.close();
//                    context.createProducer().send(destination, bArr);
                    if (msgProdCnter % 1000 == 0) {
                        out.write(msgProdCnter + " Messages produced </br>");
                    }
                    msgProdCnter++;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            long totalTime = System.currentTimeMillis() - startTime;
            out.write("<p> " + msgProdCnter + " messages produced in <em>" + totalTime + "milliseconds</em></p>");
            out.write("<p><i>Go to your JBoss EAP server console or server log to see the result of messages processing.</i></p>");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
