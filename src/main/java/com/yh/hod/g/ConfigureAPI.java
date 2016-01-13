package com.yh.hod.g;
public class ConfigureAPI {

    public interface KafkaProperties {
        public final static String ZK = "appserver:2181";
        public final static String GROUP_ID = "topic_g_group1";
        public final static String TOPIC = "topic_g";
        public final static String BROKER_LIST = "appserver:9092";
        public final static int BUFFER_SIZE = 64 * 1024;
        public final static int TIMEOUT = 20000;
        public final static int INTERVAL = 10000;
    }
}