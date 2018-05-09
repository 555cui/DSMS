package com.zeroclub.entity;

import java.util.Date;
import java.util.List;

public class Template {
    private String id;
    private User user;
    private TemplateGroup group;
    private List<TemplateElement> element;

    private String name;
    private int width;
    private int height;
    private String backgroundColor;
    private Date createTime;
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TemplateGroup getGroup() {
        return group;
    }

    public void setGroup(TemplateGroup group) {
        this.group = group;
    }

    public List<TemplateElement> getElement() {
        return element;
    }

    public void setElement(List<TemplateElement> element) {
        this.element = element;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
