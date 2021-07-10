package kitchenpos.exception.menu;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MenuGroupAlreadyExistsException extends IllegalArgumentException {

    public MenuGroupAlreadyExistsException(String s) {
        super(s);
    }
}
