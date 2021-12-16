package kitchenpos.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	PRICE_NOT_NEGATIVE_NUMBER(HttpStatus.BAD_REQUEST.value(), "가격은 0이상의 값만 가능합니다."),
	PRICE_IS_NOT_NULL(HttpStatus.BAD_REQUEST.value(), "가격을 입력해주세요."),
	MENU_GROUP_IS_NOT_NULL(HttpStatus.BAD_REQUEST.value(), "메뉴 그룹이 존재하지 않습니다."),
	PRODUCT_PRICE_IS_UNDER_SUM_PRICE(HttpStatus.BAD_REQUEST.value(), "메뉴 가격이 메뉴 상품 가격을 모두 합친 값보다 클 수 없습니다."),
	PRODUCT_IS_NULL(HttpStatus.BAD_REQUEST.value(), "상품이 존재하지 않습니다.");

	private int errorCode;
	private String message;

	ErrorCode(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}
}
