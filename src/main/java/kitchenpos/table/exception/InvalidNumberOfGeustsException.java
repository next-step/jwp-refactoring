package kitchenpos.table.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidNumberOfGeustsException extends IllegalArgumentException {
    public InvalidNumberOfGeustsException() {
    }

    public InvalidNumberOfGeustsException(String s) {
        super(s);
    }
}
