package io.search.api.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @ApiOperation(value = "루트 페이지", notes = "/search/list 경로로 리다이렉트 됩니다.")
    @GetMapping("/")
    public String home() {
        return "redirect:/search/list";
    }

}
