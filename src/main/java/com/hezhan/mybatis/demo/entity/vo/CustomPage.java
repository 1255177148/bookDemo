package com.hezhan.mybatis.demo.entity.vo;

import java.util.List;

public class CustomPage<T> {
    private List<T> data;
    private Pagination page;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }
}
