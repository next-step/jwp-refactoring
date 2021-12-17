package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    MENU_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴 그룹을 찾을 수 없습니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "올바르지 않은 가격입니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    EMPTY_MENUES(HttpStatus.BAD_REQUEST, "비어있는 메뉴 목록입니다."),
    MENU_NOT_EXIST(HttpStatus.BAD_REQUEST, "메뉴가 존재하지 않습니다."),
    ORDER_TABLE_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 테이블을 찾을 수 없습니다."),
    EMPTY_TABLE(HttpStatus.BAD_REQUEST, "비어있는 테이블입니다."),
    NOT_COMPLETE_ORDER(HttpStatus.BAD_REQUEST, "완료되지 않은 주문입니다."),
    INVALID_NUMBER_OF_GUESTS(HttpStatus.BAD_REQUEST, "올바르지 않은 게스트 숫자입니다."),
    NON_EMPTY_TABLE(HttpStatus.BAD_REQUEST, "비어있지 않은 테이블입니다."),
    NOT_IN_TABLE_GROUP(HttpStatus.BAD_REQUEST, "테이블 그룹에 속하지 않습니다."),
    INVALID_ORDER_TABLE_SIZE(HttpStatus.BAD_REQUEST, "유효하지 않은 주문 테이블 사이즈입니다."),
    NOT_SAME_TABLE_SIZE(HttpStatus.BAD_REQUEST, "동일하지 않은 테이블 사이즈입니다."),
    COMPLETE_ORDER(HttpStatus.BAD_REQUEST, "완료된 주문입니다.");

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
