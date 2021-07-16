package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public enum KitchenposExceptionMessage {
    NONE(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "해당 상품을 찾을 수 없습니다."),
    NOT_FOUND_MENU(HttpStatus.BAD_REQUEST, "해당 메뉴를 찾을 수 없습니다."),
    NOT_FOUND_MENU_GROUP(HttpStatus.BAD_REQUEST, "해당 메뉴 그룹을 찾을 수 없습니다"),
    NOT_FOUND_ORDER(HttpStatus.BAD_REQUEST, "해당 주문을 찾을 수 없습니다."),
    NOT_FOUND_ORDER_LINE_ITEM(HttpStatus.BAD_REQUEST, "주문 할 상품이 없습니다."),
    NOT_FOUND_ORDER_TABLE(HttpStatus.BAD_REQUEST, "주문 테이블을 찾을 수 없습니다."),
    NOT_FOUND_TABLE_GROUP(HttpStatus.BAD_REQUEST, "테이블 그룹을 찾을 수 없습니다."),
    PRICE_CANNOT_LOWER_THAN_MIN(HttpStatus.BAD_REQUEST, "가격은 최소금액보다 낮을 수 없습니다."),
    GUESTS_CANNOT_LOWER_THAN_MIN(HttpStatus.BAD_REQUEST, "손님 수는 최소 인원보다 낮을 수 없습니다."),
    ORDER_TABLE_CONNOT_LOWER_THAN_MIN(HttpStatus.BAD_REQUEST, "그룹핑될 주문 테이블 수가 최소 수보다 낮을 수 없습니다."),
    MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE(HttpStatus.BAD_REQUEST, "메뉴의 가격이 구성된 상품의 총 가격보다 높을 수 없습니다."),
    ALREADY_COMPLETION_ORDER(HttpStatus.BAD_REQUEST, "이미 완료된 주문입니다."),
    ALREADY_INCLUDE_TABLE_GROUP(HttpStatus.BAD_REQUEST, "이미 소속된 테이블 그룹이 있습니다."),
    EXISTS_NOT_COMPLETION_ORDER(HttpStatus.BAD_REQUEST, "완료되지 않은 주문이 존재합니다."),
    EMPTY_GUESTS(HttpStatus.BAD_REQUEST, "손님이 없습니다.");

    private final HttpStatus status;
    private final String message;

    KitchenposExceptionMessage(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
