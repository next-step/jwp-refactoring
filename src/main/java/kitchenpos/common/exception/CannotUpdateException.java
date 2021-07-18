package kitchenpos.common.exception;

import kitchenpos.common.exception.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotUpdateException extends RuntimeException {
    public CannotUpdateException(Message message) {
        super(message.showText());
    }
}
