package io.search.api.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ViewAspect {

    @Pointcut("within(io.search.api.controller..*)")
    public void controllerMethods() {}

    @Pointcut("execution(* io.search.core.*.*.service.*.*(..))")
    public void serviceMethods() {}

    @Around("controllerMethods() && (@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.PatchMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public Object logControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String signature = joinPoint.getSignature().toString();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            log.debug("[@Controller] Method Parameter[{}]: {}", i, args[i]);
        }

        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("[@Controller] {} 실행시간: {} ms", signature, (endTime - startTime));
        }
    }

    @Around("serviceMethods()")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String signature = joinPoint.getSignature().toString();

        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("[@Service] {} 실행시간: {} ms", signature, (endTime - startTime));
        }
    }

}
