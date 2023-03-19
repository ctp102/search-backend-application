package io.search.api.controller;

import io.search.core.commons.form.PagingForm;
import io.search.core.search.form.SearchForm;
import io.search.core.search.restclient.kakao.KakaoRestClient;
import io.search.core.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @ApiOperation(value = "블로그 검색 페이지", notes = "검색 결과 목록도 해당 페이지에 나타낸다.\ntotalCount Param은 페이징 처리를 하기 위함입니다.")
    @GetMapping("/search/list")
    public String searchList(Model model, @ModelAttribute SearchForm searchForm, @ModelAttribute PagingForm pagingForm) {

        // 1. query 값 없으면 '검색된 리스트가 없습니다' 표출
        if (StringUtils.isBlank(searchForm.getQuery())) {
            return "search/searchList";
        }

        searchService.searchBlog(searchForm, pagingForm);

        pagingForm.setTotalCount(157);

        return "search/searchList";
    }

}
