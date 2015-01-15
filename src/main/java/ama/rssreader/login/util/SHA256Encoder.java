package ama.rssreader.login.util;

import ama.rssreader.util.LogUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class SHA256Encoder {

    public static String encode(String pass) {
        String returnValue = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                String tmp = Integer.toHexString(digest[i] & 0xff);
                if (tmp.length() == 1) {
                    sb.append('0').append(tmp);
                } else {
                    sb.append(tmp);
                }
                returnValue = sb.toString();
            }
        } catch (NoSuchAlgorithmException ex) {
            LogUtil.log(SHA256Encoder.class.getName(),Level.SEVERE,"hash値の値の作成に失敗しました",ex.getLocalizedMessage());
        }
        return returnValue;
    }
}
