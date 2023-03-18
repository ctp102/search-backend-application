package io.search.api.controller.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SearchController {

    @GetMapping("/search/form")
    public String searchForm(Model model) {
        return "search/searchForm";
    }

    @GetMapping("/search/list")
    public String searchList(Model model) {
        return "search/searchList";
    }

}
