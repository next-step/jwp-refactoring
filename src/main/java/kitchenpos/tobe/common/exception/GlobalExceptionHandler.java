package kitchenpos.tobe.common.exception;

import kitchenpos.tobe.menu.application.MenuNotMatchException;
import kitchenpos.tobe.menugroup.application.MenuGroupNotFoundException;
import kitchenpos.tobe.order.application.OrderLineItemNotFoundException;
import kitchenpos.tobe.order.application.OrderNotFoundException;
import kitchenpos.tobe.table.application.OrderTableNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgument(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponse handleIllegalState(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EntityExistsException.class)
    public ErrorResponse handleEntityExist(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MenuNotMatchException.class)
    public ErrorResponse handleMenuNotMatch(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderLineItemNotFoundException.class)
    public ErrorResponse handleOrderLineItemNotFound(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException.class)
    public ErrorResponse handleOrderNotFound(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderTableNotFoundException.class)
    public ErrorResponse handleOrderTableNotFound(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MenuGroupNotFoundException.class)
    public ErrorResponse handleMenuGroupNotFound(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}
