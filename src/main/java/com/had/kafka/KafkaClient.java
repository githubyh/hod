package com.had.kafka;

import com.had.kafka.ConfigureAPI.KafkaProperties;

/**
 * @Date May 22, 2015
 *
 * @Author dengjie
 *
 * @Note To run Kafka Code
 */
public class KafkaClient {

    public static void main(String[] args) {
        JProducer pro = new JProducer(KafkaProperties.TOPIC);
        pro.start();

        JConsumer con = new JConsumer(KafkaProperties.TOPIC);
        con.start();
    }

}