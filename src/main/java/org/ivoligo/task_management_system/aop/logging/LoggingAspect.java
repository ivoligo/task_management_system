package org.ivoligo.task_management_system.aop.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Pointcut("@annotation(org.ivoligo.task_management_system.aop.logging.annotation.LoggingAround)" +
            "|| execution(* org.springframework.data.repository.*.save(..))" +
            "|| execution(* org.ivoligo.task_management_system.repository.*.deleteById(..))")
    private void logCreateUpdateDeleteTaskViaPointcut() {
    }

    @Around("logCreateUpdateDeleteTaskViaPointcut()")
    public Object aroundLogCreateUpdateDeleteTaskViaPointcut(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        log.info("Начало метода {}.{}() с параметрами {} ",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs()
        );
        try {
            Object result = joinPoint.proceed();

            log.info("Выход из метода {}.{}(), результат выполниения метода: {},  метод выполнен за {} мс",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    result,
                    System.currentTimeMillis() - start);
            return result;
        } catch (Exception ex) {
            log.error("Ошибка: Exception in {} {} ", ex.getMessage(), ex.getCause() != null ? ex.getCause() : "NULL", ex);
            throw ex;
        }
    }
}
