package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyJoinedTableGroupException extends RuntimeException {

	private static final String MESSAGE = "이미 그룹 테이블에 존재하는 테이블입니다";

	public AlreadyJoinedTableGroupException() {
		super(MESSAGE);
	}
}
