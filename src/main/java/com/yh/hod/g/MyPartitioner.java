package com.yh.hod.g;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;


public class MyPartitioner implements Partitioner {  
    public MyPartitioner(VerifiableProperties props) {  
    }  
  
  
    /* 
     * @see kafka.producer.Partitioner#partition(java.lang.Object, int) 
     */  
    public int partition(Object key, int partitionCount) {  
        return Integer.valueOf((String) key) % partitionCount;  
    }  
}  