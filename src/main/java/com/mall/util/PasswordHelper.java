package com.mall.util;
 
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
//import org.bluedream.core.module.sys.entity.User;
 
public class PasswordHelper {
    // 加密形式
    public static final String ALGORITHM_NAME = "MD5";
    // 加密次数
    public static final int HASH_ITERATIONS = 128;
 
    private PasswordHelper(){
        throw new AssertionError();
    }
 
    /**
     * 返回MD5加密后的字符串
     * @param sourcePWD  原始密码文本
     * @param salt       盐值
     * @return
     */
    public static String encryptPassword(String sourcePWD , String salt){
        return new SimpleHash(ALGORITHM_NAME , sourcePWD , ByteSource.Util.bytes(salt) , HASH_ITERATIONS).toHex();
    }
 
 
    /**
     * test return new password
     * @param args
     */
    public static void main(String[] args) {
        String pwd = "123456";
        String salt = "1024";
        System.out.println(encryptPassword(pwd , salt));
        System.out.println("ByteSource.Util.bytes(salt) = " + ByteSource.Util.bytes(salt));
    }

}
