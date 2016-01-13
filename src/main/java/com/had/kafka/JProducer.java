package com.had.kafka;

import java.util.Properties;

import scala.collection.Iterable;
import scala.collection.Seq;
import scala.collection.Traversable;
import scala.xml.Node;
import scala.xml.NodeSeq;

import kafka.producer.KeyedMessage;
import kafka.producer.Producer;
import kafka.producer.ProducerConfig;

/**
 * @Date May 22, 2015
 *
 * @Author dengjie
 *
 * @Note Kafka JProducer
 */
public class JProducer extends Thread {

    private Producer<Integer, String> producer;
    private String topic;
    private Properties props = new Properties();
    private final int SLEEP = 1000 * 3;

    public JProducer(String topic) {
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "appserver:9092");
        producer = new Producer<Integer, String>(new ProducerConfig(props));
        this.topic = topic;
    }

    @Override
    public void run() {
        int offsetNo = 1;
        while (true) { 
            String msg = new String("Message_" + offsetNo);
            System.out.println("Send->[" + msg + "]");
            KeyedMessage<String, String> message = 
                    new KeyedMessage<String, String>(topic, msg);
            producer.send(message);
            offsetNo++;
            try {
                sleep(SLEEP);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}