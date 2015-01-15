package ama.rssreader.util;

import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amanobu
 */
public class LogUtil {

    /**
     * ログ出力共通関数
     *
     * @param loggername サブシステムログ名
     * @param level 出力ログレベル
     * @param message メッセージ可変.カンマで連結されてログ出力されます
     */
    public static void log(String loggername, Level level, Object... message) {
        Logger logger = Logger.getLogger(loggername);
        String mes = stringBuilderJoin(obj2StringArray(message));
        logger.log(level, mes);
    }

    /**
     * String配列の文字をカンマで連結して返す
     *
     * @param strarray
     * @return
     */
    public static String stringBuilderJoin(String... strarray) {
        StringBuilder s = new StringBuilder(",");
        for (int i = 0; i < strarray.length; ++i) {
            if (i != 0) {
                s.append(",");
            }
            s.append(strarray[i]);
        }
        return s.toString();
    }

    /**
     * String配列の文字を[ ]でくくり連結して返す
     *
     * @param strarray
     * @return
     */
    public static String stringJoiner(String... strarray) {
        StringJoiner sj = new StringJoiner("],[", "[", "]");
        for (int i = 0; i < strarray.length; ++i) {
            sj.add(strarray[i]);
        }
        return sj.toString();
    }
    
    public static String[] obj2StringArray(Object... objs){
        String[] strarray = new String[objs.length];
        for(int i = 0 ; i < objs.length; i++){
            if(null != objs[i])
                strarray[i] = objs[i].toString();
            else
                strarray[i] = "null";
        }   
        return strarray;
    }
}
