package pager;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页组件
 * @author zhaojigang
 */
public class PageHelper {
    public static PagerParam pagerParam(int pageNo, int pageSize) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        int start = (pageNo - 1) * pageSize;
        int end = start + pageSize -1;
        return new PagerParam().setPageNo(pageNo).setPageSize(pageSize).setStart(start).setEnd(end);
    }

    @Data
    @Accessors(chain = true)
    public static class PagerParam {
        private int pageNo;
        private int pageSize;
        private int start;
        private int end;
    }
}
