package kitchenpos.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* kitchenpos..*Service.*(..))")
    public void allPointcut() {
    }

    @Before("allPointcut()")
    public void beforeLog(final JoinPoint joinPoint) {
        final String className = joinPoint.getSignature().getDeclaringTypeName();
        final String method = joinPoint.getSignature().getName();
        final Object[] args = joinPoint.getArgs();
        logger.info("{}: {}() method start", className, method);

        for (Object arg : args) {
            logger.info("parameter: {}", arg);
        }
    }

    @AfterReturning(value = "allPointcut()", returning = "returnObject")
    public void afterLog(final JoinPoint joinPoint, final Object returnObject) {
        final String className = joinPoint.getSignature().getDeclaringTypeName();
        final String method = joinPoint.getSignature().getName();

        logger.info("{} : {}() method end", className, method);
        logger.debug("return: {}", returnObject);
    }

    @Around("allPointcut()")
    public Object stopWatch(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final StopWatch stopWatch = new StopWatch();

        try {
            stopWatch.start();
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } finally {
            stopWatch.stop();
            logger.info("run-time: {}ms", stopWatch.getLastTaskTimeMillis());
        }
    }
}
