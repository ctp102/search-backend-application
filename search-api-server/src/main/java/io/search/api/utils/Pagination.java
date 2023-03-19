package io.search.api.utils;

import io.search.core.commons.form.Paging;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Data
public class Pagination {

    private int pageNo; // 페이지 번호
    private int pageSize; // 게시 글 수
    private int pageGroupSize = 10; // 페이징 그룹
    private long totalCount; // 게시 글 전체 수

    private int firstPageNo; // 첫 번째 페이지 번호
    private int startPageNo; // 시작 페이지 (페이징 네비 기준)
    private int endPageNo; // 끝 페이지 (페이징 네비 기준)
    private int finalPageNo; // 마지막 페이지 번호

    private int prevPageNo; // 이전 페이지 번호
    private int nextPageNo; // 다음 페이지 번호

    private String url;

    public Pagination(Paging paging, int pageGroupSize, String url) {
        this(paging.getPage(), paging.getSize(), pageGroupSize, paging.getTotalCount(), url);
    }

    public Pagination(int page, int size, int pageGroupSize, long totalCount, String url) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (pageGroupSize < 1) pageGroupSize = 10;
        if (totalCount == 0) {
            initialize(page, size);
            return;
        }

        this.pageNo = page;
        this.pageSize = size;
        this.totalCount = totalCount;
        this.url = url;

        int finalPage = (int)((totalCount + (size - 1)) / size); // 마지막 페이지
        if (this.pageNo > finalPage) this.setPageNo(finalPage); // 기본 값 설정

        if (this.pageNo < 0 || this.pageNo > finalPage) this.pageNo = 1; // 현재 페이지 유효성 체크

        int startPage = ((page - 1) / pageGroupSize) * 10 + 1; // 시작 페이지 (페이징 네비 기준)
        int endPage = startPage + pageGroupSize - 1; // 끝 페이지 (페이징 네비 기준)

        if (endPage > finalPage) { // [마지막 페이지 (페이징 네비 기준) > 마지막 페이지] 보다 큰 경우
            endPage = finalPage;
        }

        this.setFirstPageNo(1); // 첫 번째 페이지 번호

        if (page == 1) { // 시작 페이지 (전체)
            this.setPrevPageNo(1); // 이전 페이지 번호
        } else {
            this.setPrevPageNo((Math.max((page - 1), 1))); // 이전 페이지 번호
        }

        this.setStartPageNo(startPage); // 시작 페이지 (페이징 네비 기준)
        this.setEndPageNo(endPage); // 끝 페이지 (페이징 네비 기준)

        if (page == finalPage) { // 마지막 페이지 (전체)
            this.setNextPageNo(finalPage); // 다음 페이지 번호
        } else {
            this.setNextPageNo((Math.min((page + 1), finalPage))); // 다음 페이지 번호
        }
        this.setFinalPageNo(finalPage); // 마지막 페이지 번호
    }

    private void initialize(int page, int size) {
        this.pageNo = page;
        this.pageSize = size;
        this.totalCount = 0;
        this.firstPageNo = 1;
        this.finalPageNo = 1;
        this.startPageNo = 1;
        this.endPageNo = 1;
        this.prevPageNo = 1;
        this.nextPageNo = 1;
    }

    public String getHref(int pageNo) {

        StringBuilder href = new StringBuilder();

        url = decodeUrl(url);

        int idx = url.indexOf("?");
        if (idx > -1) {
            href.append(url.substring(0, idx));

            String[] params = StringUtils.split(url.substring(idx + 1) , "&");

            for (String param : params) {
                href.append((href.toString().contains("?")) ? "&" : "?");
                if (!param.startsWith("page=")) {
                    href.append(param);
                } else {
                    href.append("page=").append(pageNo);
                }
            }

        } else {
            href.append(url);
        }

        if (href.indexOf("page=") == -1) {
            href.append((href.toString().contains("?")) ? "&" : "?").append("page=").append(pageNo);
        }
        return href.toString();
    }

    private String decodeUrl(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }

}