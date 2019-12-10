package com.example.blockchaincore.funcations;

import com.example.blockchaincore.Consts;
import com.example.blockchaincore.entities.Block;
import com.example.blockchaincore.entities.TransactionGroup;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class HashFunction {

    /**
     * 生成该区块的 Hash
     * @param block 区块
     * @return Hash
     */
    public static String generateHash(Block block){
        MessageDigest messageDigest;

        if(null==block.getData() && null==block.getPreviousHash())
            throw new RuntimeException("data or previous-hash is null");

        StringBuilder nocodeBuilder = new StringBuilder();
        nocodeBuilder.append(block.getIndex());
        nocodeBuilder.append(block.getCreateTimestamp());
        for(TransactionGroup tx:block.getData()) {
            nocodeBuilder.append(tx.toString());
        }
        nocodeBuilder.append(block.getPreviousHash());
        nocodeBuilder.append(block.getHashDifficulty());
        String nocode = nocodeBuilder.toString();

        int hashOffset = 0;
        String encode = "";

        try{
            messageDigest = MessageDigest.getInstance(Consts.ENCRYPT_INSTANCE);
            while (true) {
                String nocodetmp = nocode + hashOffset;
                messageDigest.update(nocodetmp.getBytes(Consts.ENCRYPT_BYTES_CODING));
                encode = bytes2Hex(messageDigest.digest());
                if(checkHashDifficulty(encode,block.getHashDifficulty())) {
                    block.setHashOffset(hashOffset);
                    break;
                }
                else {
                    hashOffset = hashOffset + 1;
                    encode = "";
                    nocodetmp = "";
                }
            }
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("error on loading SHA-256");
        }catch (UnsupportedEncodingException e) {
            throw new RuntimeException("error on loading UTF-8");
        }
        return encode;
    }

    /**
     * 检查生成的 Hash是否符合 Difficulty的要求
     * @param hash Hash
     * @param difficulty 难度
     * @return 是否
     */
    private static boolean checkHashDifficulty(String hash,int difficulty){
        String hashBinary = hex2Binary(hash);
        return hashBinary.startsWith(generatePrefix(difficulty));
    }

    /**
     * bytes转16进制数的字符串
     * @param bytes bytes
     * @return 16进制数的字符串
     */
    private static String bytes2Hex(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (byte b : bytes) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * bytes转2进制数的字符串
     * @param bytes bytes
     * @return 2进制数的字符串
     */
    private static String bytes2Binary(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (byte b : bytes) {
            stringBuilder.append(Long.toString(b & 0xff, 2));
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - 1);
    }

    /**
     * 16进制数的字符串转2进制数的字符串
     * @param hex 16进制数的字符串
     * @return 2进制数的字符串
     */
    private static String hex2Binary(String hex){
        char[] strChar=hex.toCharArray();
        StringBuilder stringBuilder = new StringBuilder("");
        for(char c:strChar){
            int ihex = Integer.valueOf(String.valueOf(c),16);
            String sbin = Integer.toBinaryString(ihex);
            String sbinf = String.format("%04d",Integer.valueOf(sbin));
            stringBuilder.append(sbinf);
        }
        return stringBuilder.toString();
    }

    /**
     * 生成难度对应的 Hash前缀用于难度验证
     * @param offset 难度
     * @return 前缀字符串
     */
    private static String generatePrefix(int offset){
        StringBuilder prefix = new StringBuilder();
        for (int i=0;i<offset;i++){
            prefix.append('0');
        }
        return prefix.toString();
    }

    /**
     * SHA256编码
     * @param nocode 未编码字符串
     * @return 编码后字符串
     */
    public static String getSHA256Code(String nocode){
        String encode;
        try{
            MessageDigest messageDigest = MessageDigest.getInstance(Consts.ENCRYPT_INSTANCE);
            messageDigest.update(nocode.getBytes(Consts.ENCRYPT_BYTES_CODING));
            encode = bytes2Hex(messageDigest.digest());
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("error on loading SHA-256");
        }catch (UnsupportedEncodingException e) {
            throw new RuntimeException("error on loading UTF-8");
        }
        return encode;
    }

    public static void main(String[] args) throws Exception{
        System.out.println(getSHA256Code("sadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvba"));
        Thread.sleep(3000);
        System.out.println(getSHA256Code("sadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvbasadkhvlkcvjgehfvbxcfvksdjnvlsdfvba"));
    }

}
