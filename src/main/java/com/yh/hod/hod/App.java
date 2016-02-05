package com.yh.hod.hod;

import java.sql.Timestamp;

import org.apache.log4j.Logger;


public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class);
    private static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 20000000000000l; i++) {
        	count ++ ;
            LOGGER.info(count + " Info [" + i + "]");
            Thread.sleep(200);
            System.out.println(count);
            System.out.println( new Timestamp(1453219681629l).toLocaleString());
        }
    }
}