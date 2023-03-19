package io.search.api.utils;

import io.search.core.commons.form.Paging;
import io.search.core.commons.form.PagingForm;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ThymeleafUtils {

    public Pagination getPagination(Paging paging, int pageGroupSize, String url) {
        return new Pagination(paging, pageGroupSize, url);
    }

    public int getRowNum(PagingForm pagingForm, int index) {
        return (int)pagingForm.getTotalCount() - ((pagingForm.getPage() - 1) * pagingForm.getSize()) - index;
    }

    public String getRequestUriWithQueryString(HttpServletRequest request) {
        return request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
    }

}
