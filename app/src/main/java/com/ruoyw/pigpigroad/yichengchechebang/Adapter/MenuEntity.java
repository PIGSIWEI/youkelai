package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PIGROAD on 2018/12/11
 * Email:920015363@qq.com
 */
public class MenuEntity  implements Serializable {
    private String id;
    private String title;
    private String ico;
    private String sort;
    private String src;
    private boolean select = false;
    private List<MenuEntity> childs;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public List<MenuEntity> getChilds() {
        return childs;
    }

    public void setChilds(List<MenuEntity> childs) {
        this.childs = childs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }


}
