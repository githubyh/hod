package com.yh.hod.hod;





import java.util.HashMap; 
import java.util.List;   
import java.util.Map;   
import java.util.Properties;   
     
import kafka.consumer.ConsumerConfig;   
import kafka.consumer.ConsumerIterator;   
import kafka.consumer.KafkaStream;   
import kafka.javaapi.consumer.ConsumerConnector;  
   
public class TestConsumer extends Thread{   
    private static final String ZOOKEEPER = "appserver:2181";
        private final ConsumerConnector consumer;   
//        private static final String GROUP_NAME = "test_group";
        private static final String TOPIC_NAME = "kafkatesttop";
        private final String topic;  
        private   int count = 0 ;
     
        public static void main(String[] args) {   
            TestConsumer consumerThread = new TestConsumer(TOPIC_NAME);   
            consumerThread.start();   
        }   
        public TestConsumer(String topic) {   
            consumer =kafka.consumer.Consumer   
                    .createJavaConsumerConnector(createConsumerConfig());   
            this.topic =topic;   
        }   
     
    private static ConsumerConfig createConsumerConfig() {   
        Properties props = new Properties();   
        props.put("zookeeper.connect",ZOOKEEPER);   
        props.put("group.id", "0");   
        props.put("zookeeper.session.timeout.ms","10000");   
        return new ConsumerConfig(props);   
    }   
     
    public void run(){   
        Map<String,Integer> topickMap = new HashMap<String, Integer>();   
        topickMap.put(topic, 1);   
        Map<String, List<KafkaStream<byte[],byte[]>>>  streamMap =consumer.createMessageStreams(topickMap);   
        KafkaStream<byte[],byte[]>stream = streamMap.get(topic).get(0);   
        ConsumerIterator<byte[],byte[]> it =stream.iterator();   
        System.out.println("*********Results********");   
        while(true){   
            if(it.hasNext()){ 
                count ++ ;
                System.err.println(count+"get data:" +new String(it.next().message()));   
            } 
            try {   
                Thread.sleep(1);   
            } catch (InterruptedException e) {   
                e.printStackTrace();   
            }   
        }   
    }   
}