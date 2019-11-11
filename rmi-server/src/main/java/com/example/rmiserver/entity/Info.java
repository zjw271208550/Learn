package com.example.rmiserver.entity;

import java.util.Date;

public class Info {

    private String id;
    private String name;
    private Integer value;
    private Date create;
    private Date update;

    public Info() {
    }

    public Info(String id, String name, Integer value, Date create, Date update) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.create = create;
        this.update = update;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    @Override
    public String toString() {
        return "Info{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", create=" + create +
                ", update=" + update +
                '}';
    }
}
