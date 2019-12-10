package com.example.blockchaincore.api;

import com.example.blockchaincore.funcations.Crypt;
import com.example.blockchaincore.funcations.HashFunction;

public abstract class BlockData {
    private String dataId;
    private String sha256_data;

    public BlockData(String dataId, String dataString) {
        this.dataId = dataId;
        this.sha256_data = HashFunction.getSHA256Code(dataString);
    }

    public BlockData(String dataId, String dataString,boolean hasCoding) {
        this.dataId = dataId;
        if(hasCoding)
            this.sha256_data = dataString;
        else
            this.sha256_data = HashFunction.getSHA256Code(dataString);
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getSHA256Data() {
        return sha256_data;
    }

    public void setData(String dataString) {
        this.sha256_data = HashFunction.getSHA256Code(dataString);
    }

    public void setSHA256Data(String sha256_data) {
        this.sha256_data = sha256_data;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  BlockData){
            return this.dataId.equals(((BlockData) obj).getDataId())&&
                    this.sha256_data.equals(((BlockData) obj).getSHA256Data());
        }else return false;
    }

    public String getDataEncryptString(){
        return this.sha256_data;
    }
}
