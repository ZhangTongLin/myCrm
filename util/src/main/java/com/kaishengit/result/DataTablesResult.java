package com.kaishengit.result;

import java.util.List;

/**
 * dataTables的结果对象
 * @author Administrator.
 */
public class DataTablesResult<T> {

    private Integer draw;
    private Integer recodsTotal;
    private Integer recodsFiltered;
    private List<T> data;

    public DataTablesResult() {}

    public DataTablesResult(Integer draw,Integer recodsTotal, List<T> data) {
        this.data = data;
        this.draw = draw;
        this.recodsTotal = recodsTotal;
        this.recodsFiltered = recodsTotal;
    }

    public DataTablesResult(Integer draw, Integer recordsTotal, Integer recordsFiltered, List<T> data) {
        this.draw = draw;
        this.recodsTotal = recordsTotal;
        this.recodsFiltered = recordsFiltered;
        this.data = data;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getRecodsTotal() {
        return recodsTotal;
    }

    public void setRecodsTotal(Integer recodsTotal) {
        this.recodsTotal = recodsTotal;
    }

    public Integer getRecodsFiltered() {
        return recodsFiltered;
    }

    public void setRecodsFiltered(Integer recodsFiltered) {
        this.recodsFiltered = recodsFiltered;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
