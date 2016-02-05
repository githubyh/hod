package org.uugu.itools;

import org.uugu.itools.http.HttpUtil;
import org.uugu.itools.http.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App {

    public static Map<String, String> params;

    public static void main( String[] args ) {

        params = new HashMap<String, String>();
        params.put("id", "1667"); //1746 1667 1972
        //Test test = doGet("http://noteme.cn/wp-login.php", params, Test.class);
        for (int y = 0; y < 10; y++){
            new Thread(new Runnable() {

                public void run() {
                    HttpUtil httpUtil = new HttpUtil();
                    for(int i = 1; i <= 1000; i ++) {
                        Test test2 = null;
                        String str;
                        Long starttime = 0L;
                        Long endTime = 0L;
                        try {
                            starttime = System.currentTimeMillis();
                            httpUtil.post("http://dream.sdchina.com/shuxin/ajax/Zan.ashx", params, null);
                            endTime = System.currentTimeMillis();
                        } catch (Exception e) {
                            //e.printStackTrace();
                            System.out.println("System Error!! Exit App!");
                            System.exit(0);
                        }
                            System.out.println(Thread.currentThread().getName()+"成功为刘彩晴点赞："+i+"次！");
                            try {
                                Long time = endTime - starttime;

                                if(time < 200L) {
                                    System.out.println("系统响应很快，快度进行！" + time);
                                    Thread.sleep(200L);
                                }else if(time < 1000){
                                    System.out.println("系统响应不快，快度减慢！"+ time);
                                    Thread.sleep(1000L);
                                }else{
                                    System.out.println("系统响应慢，快度继续减慢！"+ time);
                                    Thread.sleep(2000L);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }).start();
            System.out.println("启动线程" + y + "完成！！");
        }

        new Thread().start();

    }
}
