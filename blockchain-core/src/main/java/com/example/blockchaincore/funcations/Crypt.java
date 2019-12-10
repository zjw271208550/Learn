package com.example.blockchaincore.funcations;

import com.example.blockchaincore.Consts;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

import org.bouncycastle.jce.interfaces.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.example.blockchaincore.Consts.*;

public class Crypt {

    //解除默认JDK中的加密强度的限制。
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * Java 的非对称加密的密钥是配对生成的
     */
    public static Map<String, Key> generateECKeyPair() 
            throws NoSuchAlgorithmException,NoSuchProviderException {
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM_EC,KEY_PROVIDER_EC);
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE_EC,new SecureRandom());
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //公钥
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        //私钥
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        //将密钥存储在map中
        Map<String, Key> keyMap = new HashMap<String, Key>();
        keyMap.put(Consts.KEY_PUBLIC_MAP, publicKey);
        keyMap.put(Consts.KEY_PRIVATE_MAP, privateKey);
        return keyMap;
    }

    /**
     * 找到一种 GayGay的方法实现由随机串生成私钥，实际上是生成密钥对但只返回私钥
     */
    public static byte[] generateECPrivateKeyFromRandom(byte[] random)
            throws NoSuchAlgorithmException,NoSuchProviderException{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM_EC,KEY_PROVIDER_EC);
        keyPairGenerator.initialize(KEY_SIZE_EC,new SecureRandom(random));
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        return privateKey.getEncoded();
    }

    /**
     * 从生成的密钥对中获得私钥
     */
    public static byte[] getECPrivateKey(Map<String, Key> keyMap) {
        ECPrivateKey key = (ECPrivateKey) keyMap.get(Consts.KEY_PRIVATE_MAP);
        return key.getEncoded();
    }

    /**
     * 从生成的密钥对中获得公钥
     */
    public static byte[] getECPublicKey(Map<String, Key> keyMap){
        ECPublicKey key = (ECPublicKey) keyMap.get(Consts.KEY_PUBLIC_MAP);
        return key.getEncoded();
    }

    /**
     * 使用签名者私钥对数据散列值签名
     */
    public static byte[] doSignature(byte[] data,byte[] privateKey)
            throws NoSuchAlgorithmException,NoSuchProviderException,
            InvalidKeySpecException,InvalidKeyException,SignatureException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_EC,KEY_PROVIDER_EC);
        PrivateKey pKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(KEY_SIGNATURE_INS_EC);
        signature.initSign(pKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * 使用签名者公钥、数据散列值来验证签名
     */
    public static boolean verifySignature(byte[] data,byte[] publicKey,byte[] sign)
            throws NoSuchAlgorithmException,NoSuchProviderException,
                    InvalidKeySpecException,InvalidKeyException,SignatureException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory  keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_EC,KEY_PROVIDER_EC);
        PublicKey pKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature  signature = Signature.getInstance(KEY_SIGNATURE_INS_EC);
        signature.initVerify(pKey);
        signature.update(data);
        return signature.verify(sign);
    }

    /**
     * 使用私钥对散列化数据加密
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) 
            throws NoSuchAlgorithmException,NoSuchProviderException, NoSuchPaddingException,
                InvalidKeySpecException,InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        PrivateKey pKey = parsePrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_CIPHER_EC,KEY_PROVIDER_CIPHER_EC);
        cipher.init(Cipher.ENCRYPT_MODE, pKey);
        return cipher.doFinal(data);
    }

    /**
     * 使用公钥对加密数据解密
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) 
            throws NoSuchAlgorithmException,NoSuchProviderException, NoSuchPaddingException,
                InvalidKeySpecException,InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        PublicKey pKey = parsePublicKey(publicKey);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_CIPHER_EC,KEY_PROVIDER_CIPHER_EC);
        cipher.init(Cipher.ENCRYPT_MODE, pKey);
        return cipher.doFinal(data);
    }

    /**
     * Base64编码
     */
    public static String bytes2Base64String(byte[] key){
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * Base64解码
     */
    public static byte[] base64String2Bytes(String key) throws IOException {
        return (new BASE64Decoder()).decodeBuffer(key);
    }


    /**
     * 由公钥获得私钥
     */
    public static byte[] getECPublicKeyFromPrivateKey(byte[] privateKey)
            throws NoSuchAlgorithmException,NoSuchProviderException,InvalidKeySpecException{
        ECPrivateKey pKey = (ECPrivateKey)parsePrivateKey(privateKey);
        ECParameterSpec parameter = pKey.getParameters();
        ECPoint Q = parameter.getG().multiply(pKey.getD());
        byte[] publicDerBytes = Q.getEncoded(false);
        ECPoint point = parameter.getCurve().decodePoint(publicDerBytes);
        ECPublicKeySpec publicKeySpec =
                new ECPublicKeySpec(point,pKey.getParameters());

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_EC,KEY_PROVIDER_EC);
        ECPublicKey publicKey = (ECPublicKey)keyFactory.generatePublic(publicKeySpec);
        return publicKey.getEncoded();
    }

    //暂时废掉等算法底层再研究一下吧
    // public static byte[] generateECPrivateKeyFromRandom(byte[] random)
    //         throws NoSuchAlgorithmException,NoSuchProviderException,InvalidKeySpecException{//曲线生成期间供以后使用的字节
    //     BigInteger S = generateBigPrime(PRIME_SIZE_EC_S);//私有值
    //     BigInteger P = generateBigPrime(PRIME_SIZE_EC_P);//创建具有指定素数的椭圆曲线素数有限域
    //     BigInteger A = generateBigPrime(PRIME_SIZE_EC_A);//椭圆曲线的第一个系数。
    //     BigInteger B = generateBigPrime(PRIME_SIZE_EC_B);//椭圆曲线的第二个系数。
    //     BigInteger N = generateBigPrime(PRIME_SIZE_EC_N);//生成器的顺序
    //     int H = PRIME_EC_H;//辅助因子。
    //
    //     java.security.spec.ECPoint point = new java.security.spec.ECPoint(A,B);
    //     java.security.spec.ECFieldFp field = new java.security.spec.ECFieldFp(P);
    //     java.security.spec.EllipticCurve curve = new java.security.spec.EllipticCurve(field,A,B);
    //     java.security.spec.ECParameterSpec parameter = new java.security.spec.ECParameterSpec(curve,point,N,H);
    //     java.security.spec.ECPrivateKeySpec privateKeySpec = new java.security.spec.ECPrivateKeySpec(S,parameter);
    //
    //     KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_EC,KEY_PROVIDER_EC);
    //     ECPrivateKey privateKey = (ECPrivateKey)keyFactory.generatePrivate(privateKeySpec);
    //     return privateKey.getEncoded();
    // }

    /**
     * 根据费马小定理生成一个大素数
     * @return
     */
    private static BigInteger generateBigPrime(int bitSize){
        return BigInteger.probablePrime(bitSize,new Random());
    }

    private static PrivateKey parsePrivateKey(byte[] key)
            throws NoSuchAlgorithmException,NoSuchProviderException,InvalidKeySpecException{
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_EC,KEY_PROVIDER_EC);
        //生成私钥
        return keyFactory.generatePrivate(pkcs8KeySpec);
    }

    private static PublicKey parsePublicKey(byte[] key)
            throws NoSuchAlgorithmException,NoSuchProviderException,InvalidKeySpecException{
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_EC,KEY_PROVIDER_EC);
        //生成私钥
        return keyFactory.generatePublic(x509KeySpec);
    }

    public static void main(String[] args) throws Exception{
        // byte[] pri = generateECPrivateKeyFromRandom("6789@jkl".getBytes());
        Map<String, Key> pair =  generateECKeyPair();
        byte[] pri = getECPrivateKey(pair);
        for(String p :Security.getAlgorithms("ALG.ALIAS.SIGNATURE")){
            System.out.println(p);
        }
        System.out.println(bytes2Base64String(getECPublicKey(pair)));
        System.out.println(bytes2Base64String(getECPublicKeyFromPrivateKey(pri)));
    }
}
