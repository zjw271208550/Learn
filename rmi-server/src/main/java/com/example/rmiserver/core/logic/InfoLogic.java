package com.example.rmiserver.core.logic;

import com.example.rmiserver.entity.Info;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Server逻辑类
 */
public class InfoLogic implements AutoCloseable{

    private final ArrayList<Info> infos = new ArrayList<>();
    private final Logic logic;

    //初始化
    public InfoLogic() {
        this.logic = new Logic();
    }
    //启动
    public void start(){
        this.logic.start();
    }
    //初始化数据对象
    public void initIdList(List<Info> list){
        this.infos.addAll(list);
    }
    //更新数据对象方法
    public void updateValueById(String id,Integer value){
        for (Info item : this.infos){
            if(item.getId().equals(id)){
                item.setValue(value);
                item.setUpdate(new Date());
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.logic.stop();
    }

    //逻辑线程
    private class Logic extends Thread{
        @Override
        public void run() {
            while (true){
                System.out.println("#====================================================================#");
                for (Info item : infos){
                    System.out.println(item.toString());
                }
                try {
                    sleep(3000);
                }catch (InterruptedException e){
                    continue;
                }
            }
        }
    }
}
