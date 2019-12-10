package com.example.blockchainclient1.util;

import com.example.blockchaincore.funcations.Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ClientKey {

    private final Logger logger = LoggerFactory.getLogger(ClientKey.class);
    private String realAddress;
    private String publicKey;
    private String privateKey;

    public ClientKey() {
        try {
            this.realAddress = InetAddress.getLocalHost().getHostAddress();
        }catch (UnknownHostException e){
            this.realAddress = "172.16.192.90";
            logger.error(e.getMessage());
        }
        try {
            byte[] privateKeyBytes = Crypt.generateECPrivateKeyFromRandom(this.realAddress.getBytes());
            this.privateKey = Crypt.bytes2Base64String(privateKeyBytes);
            byte[] publicKeyBytes = Crypt.getECPublicKeyFromPrivateKey(privateKeyBytes);
            this.publicKey = Crypt.bytes2Base64String(publicKeyBytes);
        }catch (Exception e){
            logger.error(e.getMessage());
            this.privateKey = null;
            this.publicKey = null;
        }
    }

    public String getRealAddress() {
        return realAddress;
    }

    public void setRealAddress(String realAddress) {
        this.realAddress = realAddress;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
