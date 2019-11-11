package com.example.blockchaincore;

public class Consts {
    public static final String ENCRYPT_INSTANCE = "SHA-256";        //加密方式
    public static final String ENCRYPT_BYTES_CODING = "UTF-8";      //字符串编码

    public static final int HASH_DIFFICULTY_INIT = 8;               //hash构建初始难度要求
    public static final long HASH_GENERATION_TIMEOUT_MS = 600000L;  //hash生成耗时，用来界定实际难度
    public static final long DIFFICULTY_RESET_AFTER_BLOCK = 2048L;  //hash难度刷新间隔

    public static final String HEX_CHAR_ENUM = "0123456789ABCDEF";


}
