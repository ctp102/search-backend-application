package io.search.api.controller;

import io.search.core.commons.form.PagingForm;
import io.search.core.search.domain.SearchDomain;
import io.search.core.search.dto.SearchResultDto;
import io.search.core.search.form.SearchForm;
import io.search.core.search.service.SearchService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService searchService;

    @ApiOperation(value = "블로그 검색 페이지", notes = "검색 결과 목록도 해당 페이지에 나타낸다.\ntotalCount Param은 페이징 처리를 하기 위함입니다.")
    @GetMapping("/search/list")
    public String searchList(Model model, @ModelAttribute SearchForm searchForm, @ModelAttribute PagingForm pagingForm) {

        if (StringUtils.isBlank(searchForm.getQuery())) {
            model.addAttribute("searchResults", new ArrayList<>());
            model.addAttribute("searchDomains", searchService.getHotTop10Search());
            return "search/searchList";
        }

        List<SearchResultDto> searchResults = searchService.searchBlog(searchForm, pagingForm);
        List<SearchDomain> searchDomains = searchService.getHotTop10Search();

        model.addAttribute("searchResults", searchResults);
        model.addAttribute("searchDomains", searchDomains);

        return "search/searchList";
    }

}
