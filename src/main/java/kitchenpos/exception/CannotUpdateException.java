package kitchenpos.exception;

import kitchenpos.common.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotUpdateException extends RuntimeException {
    public CannotUpdateException(Message message) {
        super(message.showText());
    }
}
