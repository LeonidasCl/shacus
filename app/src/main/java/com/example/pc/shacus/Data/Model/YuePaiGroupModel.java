package com.example.pc.shacus.Data.Model;

import java.io.Serializable;

/**
 * Created by licl on 2017/3/9.
 */

public class YuePaiGroupModel implements Serializable {
    private String type;
    private String name;
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
