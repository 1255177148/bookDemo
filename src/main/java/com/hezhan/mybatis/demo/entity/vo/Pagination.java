package com.hezhan.mybatis.demo.entity.vo;

public class Pagination {
    private int pageNum;
    private Long total;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
