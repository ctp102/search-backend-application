package io.search.api.config.mvc;

import io.search.core.commons.exception.CustomAccessDeniedException;
import io.search.core.commons.exception.CustomException;
import io.search.core.commons.exception.CustomInvalidParameterException;
import io.search.core.commons.exception.CustomUnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice("io.search")
public class ViewExceptionHandler {

    @ExceptionHandler
    public String invalidParameter(HttpServletRequest request, HandlerMethod handlerMethod, Model model, CustomInvalidParameterException e) {
        log.error("[Invalid-Parameter]", e);
        request.setAttribute("CUSTOM_EXCEPTION", e);

        if (handlerMethod.getMethodAnnotation(ResponseBody.class) != null || (request.getRequestURI().startsWith("/api/"))) {
            return "forward:/api/exception/invalid-parameter";
        }

        model.addAttribute("message", e.getMessage());
        model.addAttribute("historyBack", true);

        return "common/error/alert";
    }

    @ExceptionHandler(CustomUnauthorizedException.class)
    public String unauthorized(HttpServletRequest request, HandlerMethod handlerMethod, Model model, CustomUnauthorizedException e) {
        log.error("[Unauthorized]", e);
        request.setAttribute("CUSTOM_EXCEPTION", e);

        if (handlerMethod.getMethodAnnotation(ResponseBody.class) != null || (request.getRequestURI().startsWith("/api/"))) {
            return "forward:/api/exception/unauthorized";
        }

        model.addAttribute("message", e.getMessage());
        model.addAttribute("redirectUrl", "/login");

        return "common/error/alert";
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    public String accessDenied(HttpServletRequest request, HandlerMethod handlerMethod, Model model, CustomAccessDeniedException e) {
        log.error("[Access-Denied]", e);
        request.setAttribute("CUSTOM_EXCEPTION", e);

        if (handlerMethod.getMethodAnnotation(ResponseBody.class) != null || (request.getRequestURI().startsWith("/api/"))) {
            return "forward:/api/exception/access-denied";
        }

        model.addAttribute("message", e.getMessage());
        model.addAttribute("historyBack", true);

        return "common/error/alert";
    }

    @ExceptionHandler(CustomException.class)
    public String commonError(HttpServletRequest request, HandlerMethod handlerMethod, CustomException e) {
        log.error("[Custom-Error]", e);
        request.setAttribute("CUSTOM_EXCEPTION", e);

        if (handlerMethod.getMethodAnnotation(ResponseBody.class) != null || (request.getRequestURI().startsWith("/api/"))) {
            return "forward:/api/exception/error";
        }
        return "common/error/internalServerError";
    }

    @ExceptionHandler(Exception.class)
    public String internalServerError(HttpServletRequest request, HandlerMethod handlerMethod, Exception e) {
        log.error("[Internal-Server-Error]", e);
        request.setAttribute("CUSTOM_EXCEPTION", e);

        if (handlerMethod.getMethodAnnotation(ResponseBody.class) != null || (request.getRequestURI().startsWith("/api/"))) {
            return "forward:/api/exception/internal-server-error";
        }
        return "common/error/internalServerError";
    }

}
