package com.had.kafka;




import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.Message;
import kafka.message.MessageAndMetadata;
  
  
public class ConsumerSample { 
  
    public static void main(String[] args) {  
        // specify some consumer properties  
        Properties props = new Properties();  
        props.put("zookeeper.connect", "appserver:2181,appserver:2182");  
        props.put("zookeeper.connectiontimeout.ms", "1000000");  
        props.put("group.id", "test_group");  
  
            // Create the connection to the cluster  
        ConsumerConfig consumerConfig = new ConsumerConfig(props);  
        ConsumerConnector connector = Consumer.createJavaConsumerConnector(consumerConfig);  
  
  
        Map<String,Integer> topics = new HashMap<String,Integer>();  
        topics.put("test", 2);  
        Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams = connector.createMessageStreams(topics);  
        List<KafkaStream<byte[], byte[]>> streams = topicMessageStreams.get("test"); 
        ExecutorService threadPool = Executors.newFixedThreadPool(2);  
        for (final KafkaStream<byte[], byte[]> stream : streams) {  
            threadPool.submit(new Runnable() {  
                public void run() {  
                    for (MessageAndMetadata msgAndMetadata : stream) {  
                        // process message (msgAndMetadata.message())  
                        System.out.println("topic: " + msgAndMetadata.topic());  
                        Message message = (Message) msgAndMetadata.message();  
                        ByteBuffer buffer = message.payload();  
                        byte[] bytes = new byte[message.payloadSize()];  
                        buffer.get(bytes);  
                        String tmp = new String(bytes);  
                        System.out.println("message content: " + tmp);  
                    }  
                }  
            });  
        }    
    } 
}