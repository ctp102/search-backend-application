package io.search.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Controller
public class ExceptionController implements ErrorController {

    /**
     * 스프링부트 기본 에러 처리
     *
     * @param request the request
     * @return the string
     */
    @GetMapping("/error")
    @ApiOperation(value = "예외 처리 컨트롤러", notes = "예외 처리 컨트롤러입니다.")
    public String handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            HttpStatus httpStatus = findByStatusCode(statusCode);

            return switch (httpStatus) {
                case NOT_FOUND -> "common/error/pageNotFound";
                case FORBIDDEN -> "common/error/forbidden";
                default -> "common/error/internalServerError";
            };
        }
        return "common/error/internalServerError";
    }

    private HttpStatus findByStatusCode(int statusCode) {
        return Arrays.stream(HttpStatus.values())
                .filter(x -> x.value() == statusCode)
                .findFirst()
                .orElse(null);
    }

}
