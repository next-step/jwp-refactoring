package common.error;

import common.error.exception.BusinessException;
import common.error.exception.NotExistException;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotExistException.class)
    public ErrorResponse handleNotExistException(NotExistException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ErrorResponse handleBusinessException(BusinessException e) {
        return ErrorResponse.of(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse handleThrowableException(Throwable e) {
        return ErrorResponse.of(e.getMessage());
    }
}
