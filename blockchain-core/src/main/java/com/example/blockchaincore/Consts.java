package com.example.blockchaincore;

import java.security.interfaces.RSAPublicKey;

public class Consts {
    public static final String ENCRYPT_INSTANCE = "SHA-256";        //加密方式
    public static final String ENCRYPT_BYTES_CODING = "UTF-8";      //字符串编码

    public static final int HASH_DIFFICULTY_INIT = 8;               //hash构建初始难度要求
    public static final long HASH_GENERATION_TIMEOUT_MS = 600000L;  //hash生成耗时，用来界定实际难度
    public static final long DIFFICULTY_RESET_AFTER_BLOCK = 2048L;  //hash难度刷新间隔

    public static final String KEY_ALGORITHM_EC = "EC";
    public static final String KEY_ALGORITHM_CIPHER_EC = "ECIES";
    public static final String KEY_PROVIDER_EC = "BC";
    public static final String KEY_PROVIDER_CIPHER_EC = "BC";

    public static final int KEY_SIZE_EC = 256;
    public static final String KEY_SIGNATURE_INS_EC = "SHA1WITHECDSA";

    public static final String KEY_PUBLIC_MAP = "PUBLIC_KEY";
    public static final String KEY_PRIVATE_MAP = "PRIVATE_KEY";


    public static int PRIME_SIZE_EC_S = 2;
    public static int PRIME_SIZE_EC_P = 256;
    public static int PRIME_SIZE_EC_A = 64;
    public static int PRIME_SIZE_EC_B = 64;
    public static int PRIME_SIZE_EC_N = 2;
    public static int PRIME_EC_H = 1024;


}
