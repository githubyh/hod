package com.yh.hod.hod;





import java.util.Date;
import java.util.HashMap; 
import java.util.List;   
import java.util.Map;   
import java.util.Properties;   
     
import kafka.consumer.ConsumerConfig;   
import kafka.consumer.ConsumerIterator;   
import kafka.consumer.KafkaStream;   
import kafka.javaapi.consumer.ConsumerConnector;  
   
public class TestConsumer extends Thread{   
    private static final String ZOOKEEPER = "appserver:2181,appserver:2182";
        private final ConsumerConnector consumer;   
        private static final String GROUP_NAME = "0";
        private static final String TOPIC_NAME = "fktest1";
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
        props.put("group.id", GROUP_NAME);   
        props.put("zookeeper.session.timeout.ms","5000");   
        props.put("zookeeper.connection.timeout.ms","10000");   
//        props.put("zookeeper.sync.time.ms","2000");  
        
        /**
         * 然后我尝试了粗体部分的解决方法，在Consumer端设置两个属性如下：
props.put("rebalance.max.retries", "5");
props.put("rebalance.backoff.ms", "1200");
 并确保5*1200=6000的值大于zookeeper.session.timeout.ms属性对应的值（这里我是5000）。再次分别启动Producer端和Comsumer端，问题果然解决了。
 
注：服务端Producer的metadata.broker.list属性最好不止一个，这样也就要求你做负载均衡。
PS：对于kafka的一些异常需要比较清楚地去了解它的运行机制，但我没这么多时间。所以就临时抱佛脚去解决问题了。
         */
        props.put("fetch.wait.max.ms", "5000");
        props.put("rebalance.max.retries", "5");
        props.put("rebalance.backoff.ms", "1200");
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
                System.err.println(count+"get data:" +new String(it.next().message())+">>>"+new Date().toLocaleString());   
            } 
           /* try {   
                Thread.sleep(1);   
            } catch (InterruptedException e) {   
                e.printStackTrace();   
            }*/   
        }   
    }   
}