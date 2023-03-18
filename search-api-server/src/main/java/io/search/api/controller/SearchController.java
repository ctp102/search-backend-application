package io.search.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final

    @GetMapping("/search/list")
    public String searchList(Model model) {
        return "search/searchList";
    }

}
