package kitchenpos.exception;

import static java.lang.String.format;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityNotFoundException extends RuntimeException {

	private static final String MESSAGE = "요청한 엔티티를 찾을 수 없습니다.";

	public EntityNotFoundException() {
		super(MESSAGE);
	}

	public EntityNotFoundException(Class<?> menuGroupClass, List<Long> id) {
		super(MESSAGE + format(", entity='%s', id='%s'", menuGroupClass.getSimpleName(), id));
	}
}
