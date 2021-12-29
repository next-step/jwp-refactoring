package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * packageName : kitchenpos.common
 * fileName : ServiceException
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public abstract class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ServiceException(String message) {
        super(message);
    }

}
