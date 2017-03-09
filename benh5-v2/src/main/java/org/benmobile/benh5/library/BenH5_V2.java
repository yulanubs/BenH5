package org.benmobile.benh5.library;
/**
 * Created by Jekshow on 2017/2/14.
 */
public class BenH5_V2 {
    private static BenH5_V2 instance;

    /**
     * 版本信息
     * @return  BenH5_V2 Instan
     */
    public static BenH5_V2 getInstance() {
        if (instance == null)
            synchronized (BenH5_V2.class) {
                if (instance == null)
                    instance = new BenH5_V2();
            }
        return instance;
    }

    /**
     *
     * @param name
     * @return BenH5Info
     */
    public String getBenH5Info(String name) {
        return name + "_BenH5_V2" ;
    }
}
