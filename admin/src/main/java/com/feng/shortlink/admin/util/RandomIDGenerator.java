package com.feng.shortlink.admin.util;

import java.security.SecureRandom;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description 生成6位数随机gid
 **/
public class RandomIDGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();
    
    /**
     * 生成一个6位随机GID，GID只包含英文大小写字母和数字
     * @return 生成的随机ID
     */
    public static String generateRandomGid () {
        StringBuilder id = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            id.append(CHARACTERS.charAt(index));
        }
        return id.toString();
    }
}
