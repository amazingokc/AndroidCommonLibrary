package com.core.commonlibrary.bean;

import java.io.Serializable;

/** 当接口支持分页获取时需要
 * Created by hua on 2019/3/7 0007.
 */

public class PagerBean implements Serializable {

    private boolean hasNext;
    private boolean hasPrev;
    private boolean needPaging;
    private int page;
    private int pageSize;
    private int startRow;
    private int totalPage;
    private int totalRows;

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
    public boolean getHasNext() {
        return hasNext;
    }

    public void setHasPrev(boolean hasPrev) {
        this.hasPrev = hasPrev;
    }
    public boolean getHasPrev() {
        return hasPrev;
    }

    public void setNeedPaging(boolean needPaging) {
        this.needPaging = needPaging;
    }
    public boolean getNeedPaging() {
        return needPaging;
    }

    public void setPage(int page) {
        this.page = page;
    }
    public int getPage() {
        return page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageSize() {
        return pageSize;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
    public int getStartRow() {
        return startRow;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }
    public int getTotalRows() {
        return totalRows;
    }
}
