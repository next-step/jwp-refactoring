package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    TABLE_EMPTY("테이블이 비어있습니다.", HttpStatus.BAD_REQUEST),
    NEGATIVE_NUMBER_OF_GUESTS("방문한 손님 수는 음수일 수 없습니다.", HttpStatus.BAD_REQUEST),
    ORDER_TABLE_GROUPED("주문 테이블이 그룹화 되어 있습니다.", HttpStatus.BAD_REQUEST),
    CAN_NOT_CHANGE_COOKING_AND_MEAL("조리, 식사중인 테이블은 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ORDER_TABLE_NOT_FOUND("존재하지 않는 주문 테이블입니다.", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND),
    ORDER_LINE_ITEM_EMPTY("비어있는 주문 테이블입니다", HttpStatus.BAD_REQUEST),
    ORDER_TABLE_EMPTY("비어있는 주문 테이블입니다", HttpStatus.BAD_REQUEST),
    MENU_GROUP_NOT_FOUND("존재하지 않는 메뉴 그룹입니다", HttpStatus.NOT_FOUND),
    CONTAINS_NOT_EXIST_MENU("존재하지 않는 메뉴가 포함되어 있습니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND("존재하지 않는 상품 입니다", HttpStatus.NOT_FOUND),
    INVALID_PRICE("올바른 가격이 아닙니다.", HttpStatus.BAD_REQUEST),
    CAN_NOT_CHANGE_COMPLETED_ORDER_STATUS("계산 완료된 주문의 상태는 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
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
