package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    MENU_GROUP_NOT_FOUND("존재하지 않는 메뉴 그룹입니다", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("존재하ø지 않는 상품 입니다", HttpStatus.NOT_FOUND),
    INVALID_PRICE("올바른 가격이 아닙니다.", HttpStatus.BAD_REQUEST),
    INVALID_MENU_PRICE("메뉴의 가격은 상품들 금액의 합보다 작아야 합니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
