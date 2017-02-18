package org.benmobile.benh5.library;
import org.bentools.utils.date.DateUtil;
/**
 * Created by Jekshow on 2017/2/14.
 */
public class BenH5_V2 {
    private static BenH5_V2 instance;

    /**
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
    private String getBenH5Info(String name) {
        String currentDate = DateUtil.getCurrentDate();
        return name + "_BenH5_V2_" + currentDate;
    }
}
