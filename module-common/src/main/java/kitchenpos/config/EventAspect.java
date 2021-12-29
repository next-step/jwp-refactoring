package kitchenpos.config;


import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EventAspect {

    private static final Logger EVENT_LOGGER = LoggerFactory.getLogger("event");
    private static final String LOG_FORMAT = "[{}] [{}] [{}] : Params: {}";

    @Around("@annotation(kitchenpos.config.EventLoggingAop)")
    public Object eventAopLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        beforeLogging(joinPoint);
        Object result = joinPoint.proceed();
        afterLogging(joinPoint, result);

        return result;
    }

    @AfterThrowing(
        pointcut = "@annotation(kitchenpos.config.EventLoggingAop)",
        throwing = "ex"
    )
    public void afterThrowingAdvice(JoinPoint joinPoint, Throwable ex) {
        afterThrowingLogging(joinPoint, ex);
    }

    private void beforeLogging(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        EVENT_LOGGER.info(LOG_FORMAT, "beforeLogging", className, methodName,
            Arrays.toString(args));
    }

    private void afterLogging(JoinPoint joinPoint, Object retVal) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        EVENT_LOGGER.info(LOG_FORMAT, "afterLogging", className, methodName, retVal);
    }

    private void afterThrowingLogging(JoinPoint joinPoint, Throwable ex) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        EVENT_LOGGER.info(LOG_FORMAT, "afterThrowingLogging", className, methodName,
            ex.getMessage());
    }
}
