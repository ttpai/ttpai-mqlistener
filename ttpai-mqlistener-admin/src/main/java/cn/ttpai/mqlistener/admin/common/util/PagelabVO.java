package cn.ttpai.mqlistener.admin.common.util;

import java.util.List;

/**
 * 分页器
 *
 * @author Kail
 */
public class PagelabVO<T> {

    /**
     * 默认第一页
     */
    private static final int DEFAULT_CURRENT_PAGE = 1;

    /**
     * 默认页面大小
     */
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 最大页面大小
     */
    private static final int MAX_PAGE_SIZE = 50;

    /**
     * 开始索引(默认值0)
     */
    private Integer startIndex;

    /**
     * 单前页面
     */
    private Integer currentPage;

    /**
     * 分页大小(默认20条)
     * 分页查询最多只能50条数据
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer countPage;

    /**
     * 总条数
     */
    private Integer totalNum;

    /**
     * 页内容
     */
    private List<T> dataList;

    /**
     * 默认构造器，
     * <p>
     * currentPage 1
     * <p>
     * pageSize 20
     */
    public PagelabVO() {
        this.setPageSize(DEFAULT_PAGE_SIZE);
        this.setCurrentPage(DEFAULT_CURRENT_PAGE);
    }

    /**
     * @param currentPage 当前页
     * @param pageSize    页面大小
     */
    public PagelabVO(Integer currentPage, Integer pageSize) {
        this.setPageSize(pageSize);
        this.setCurrentPage(currentPage);
    }

    /**
     * 根据起始偏移量个页面大小获取 Pagelab
     *
     * @param startIndex 起始位
     * @param pageSize   页面大小
     * @return Pagelab
     */
    public static PagelabVO buildPageLab(Integer startIndex, Integer pageSize) {
        return new PagelabVO(buildCurrentPage(startIndex, pageSize), pageSize);
    }

    /**
     * 根据起始偏移量个页面大小获取 当前是几页
     *
     * @param startIndex 起始位
     * @param pageSize   页面大小
     * @return 当前是几页
     */
    public static Integer buildCurrentPage(Integer startIndex, Integer pageSize) {
        return startIndex / pageSize + 1;
    }

    public Integer getStartIndex() {
        return null == startIndex ? 0 : startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = null == currentPage || currentPage < 1 ? DEFAULT_CURRENT_PAGE : currentPage;
        setStartIndex((this.currentPage - 1) * getPageSize());
    }

    public Integer getPageSize() {
        this.pageSize = null == this.pageSize || this.pageSize < 1 ? DEFAULT_PAGE_SIZE : this.pageSize;
        return pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize;
    }

    /**
     * 分页查询最多只能50条数据
     */
    public void setPageSize(Integer pageSize) {
        pageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
        pageSize = pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize;
        this.pageSize = pageSize;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
        if (totalNum != null) {
            this.setCountPage((totalNum - 1) / this.getPageSize() + 1);
        }
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setCountPage(Integer countPage) {
        this.countPage = countPage;
    }

    public Integer getCountPage() {
        return countPage;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "PagelabVO{" +
                "startIndex=" + startIndex +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", countPage=" + countPage +
                ", totalNum=" + totalNum +
                ", dataList=" + dataList +
                '}';
    }
}
