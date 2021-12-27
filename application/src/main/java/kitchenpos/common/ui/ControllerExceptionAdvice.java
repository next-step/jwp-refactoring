package kitchenpos.common.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import kitchenpos.common.exception.BusinessException;

@ControllerAdvice
public class ControllerExceptionAdvice {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity handleException(BusinessException businessException) {
		return ResponseEntity.status(businessException.getStatus())
			.body(businessException.errorCode().getErrorCode());
	}
}
