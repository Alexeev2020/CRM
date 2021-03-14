package com.demo.crm.vo;

import java.util.List;

public class PaginationVO<T> {
    private int total;
    private String successState;
    private List<T> dataList;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSuccessState() {
        return successState;
    }

    public void setSuccessState(String successState) {
        this.successState = successState;
    }


    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
