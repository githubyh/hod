package com.had.kafka;
import java.util.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

  
public class ProducerSample { 
    public static void main(String[] args) {  
        Properties props = new Properties();  
        props.put("zk.connect", "10.103.22.47:2181");  
        props.put("serializer.class", "kafka.serializer.StringEncoder");  
        props.put("metadata.broker.list", "10.103.22.47:9092"); 
        props.put("request.required.acks", "1"); 
        //props.put("partitioner.class", "com.xq.SimplePartitioner"); 
        ProducerConfig config = new ProducerConfig(props);  
        Producer<String, String> producer = new Producer<String, String>(config);  
        String ip = "192.168.2.3"; 
        String msg ="this is a messageuuu!"; 
        KeyedMessage<String, String> data = new KeyedMessage<String, String>("test", ip,msg);  
        producer.send(data); 
        producer.close();  
    }  
  
} 