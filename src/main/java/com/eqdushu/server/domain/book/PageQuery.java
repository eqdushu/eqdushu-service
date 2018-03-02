package com.eqdushu.server.domain.book;

import com.eqdushu.server.domain.BaseDomain;

/**
 * Created by nobita on 17/9/16.
 */
public class PageQuery extends BaseDomain{

	private static final long serialVersionUID = 567891123L;

	private int startNum;

    private int pageNum;

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
