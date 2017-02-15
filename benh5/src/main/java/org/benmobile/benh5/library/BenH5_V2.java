package org.benmobile.benh5.library;

/**
 * Created by Jekshow on 2017/2/14.
 */

public class BenH5_V2 {
    private static BenH5_V2 instance;

    public static BenH5_V2 getInstance() {
        if (instance == null)
            synchronized (BenH5_V2.class) {
                if (instance == null)
                    instance = new BenH5_V2();
            }
        return instance;
    }
    private  String  getBenH5Info(String name){
        return "BenH5_V2"+"欢迎您："+name;
    }
}
