package com.example.blockchainclient1.bean;

import com.example.blockchaincore.api.BlockData;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Data extends BlockData {

    private String bh;
    private String ah;
    private Date jarq;
    private Date update;

    public Data(String bh, String ah, Date jarq,Date update) {
        super(bh, bh + ah + (null==jarq?"":date2String(jarq)) + date2String(update));
        this.bh = bh;
        this.ah = ah;
        this.jarq = jarq;
        this.update = update;
    }

    private static String date2String(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String rel =  sdf.format(date.getTime());
        return rel;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getAh() {
        return ah;
    }

    public void setAh(String ah) {
        this.ah = ah;
    }

    public Date getJarq() {
        return jarq;
    }

    public void setJarq(Date jarq) {
        this.jarq = jarq;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }
}
