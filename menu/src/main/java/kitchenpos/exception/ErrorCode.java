package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    MENU_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴 그룹을 찾을 수 없습니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "올바르지 않은 가격입니다."),
    MENU_NOT_EXIST(HttpStatus.BAD_REQUEST, "메뉴가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
