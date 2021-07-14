package kitchenpos.common;

import static org.springframework.http.HttpStatus.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import kitchenpos.generic.exception.CalculationFailedException;
import kitchenpos.menu.exception.ExceedingTotalPriceException;
import kitchenpos.generic.exception.IllegalOperationException;
import kitchenpos.menu.exception.MenuDetailMismatchException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.MenuMismatchException;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.exception.NotEnoughTablesException;
import kitchenpos.order.exception.OrderNotCompletedException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.exception.OrderTableNotFoundException;
import kitchenpos.product.exception.ProductNotFoundException;
import kitchenpos.order.exception.TableGroupNotFoundException;

@ControllerAdvice
public class GeneralExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(Exception e) {
        log.warn("핸들링 되지 않은 예외입니다.", e);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {
        MenuDetailMismatchException.class,
        MenuMismatchException.class,
    })
    public ResponseEntity<Void> orderMenuMismatchException(Exception e) {
        log.debug("주문정보와 메뉴 스펙이 일치하지 않습니다.", e);
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    @ExceptionHandler(value = {
        MenuGroupNotFoundException.class,
        MenuNotFoundException.class,
        OrderNotFoundException.class,
        OrderTableNotFoundException.class,
        ProductNotFoundException.class,
        TableGroupNotFoundException.class
    })
    public ResponseEntity<Void> notExistsException(Exception e) {
        log.debug("존재하지 않는 정보를 요청했습니다.", e);
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    @ExceptionHandler(value = {
        IllegalArgumentException.class,
        NotEnoughTablesException.class
    })
    public ResponseEntity<Void> invalidConditionException(Exception e) {
        log.debug("잘못 된 요청값이 전달되었습니다.", e);
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    @ExceptionHandler(value = {
        ExceedingTotalPriceException.class,
        IllegalOperationException.class,
        OrderNotCompletedException.class,
    })
    public ResponseEntity<Void> illegalOperationException(Exception e) {
        log.debug("실행을 위한 조건을 만족하지 않습니다.", e);
        return ResponseEntity.status(CONFLICT).build();
    }

    @ExceptionHandler(value = {
        CalculationFailedException.class,
    })
    public ResponseEntity<Void> calculationFailedException(Exception e) {
        log.warn("값 계산이 실패했습니다.", e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
    }
}
