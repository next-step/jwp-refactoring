package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NOT_EXISTS_MENU_GROUP(HttpStatus.BAD_REQUEST, "메뉴 그룹을 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "제품을 찾을 수 없습니다."),
    PRICE_GREATER_THAN_SUM(HttpStatus.BAD_REQUEST, "메뉴의 가격이 메뉴 항목에 포함된 가격의 합보다 큽니다."),
    NOT_FOUND_ORDER(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다."),
    NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT(
            HttpStatus.BAD_REQUEST, "주문 항목의 개수와 등록된 메뉴의 개수가 일치하지 않습니다."),
    NOT_EXISTS_ORDER_LINE_ITEMS(HttpStatus.BAD_REQUEST, "주문 항목이 존재하지 않습니다."),
    CAN_NOT_ORDER(HttpStatus.BAD_REQUEST, "주문이 불가능합니다."),
    ALREADY_COMPLETION_STATUS(HttpStatus.BAD_REQUEST, "주문 상태가 이미 완료되었습니다."),
    PRICE_NOT_EXISTS_OR_LESS_THAN_ZERO(HttpStatus.BAD_REQUEST, "가격이 존재하지 않거나 0 보다 작습니다."),
    TABLE_IS_NOT_EMPTY_OR_ALREADY_REGISTER_TABLE_GROUP(
            HttpStatus.BAD_REQUEST, "좌석이 공석이 아니거나 이미 좌석 그룹에 등록된 좌석입니다."),
    EXISTS_NOT_COMPLETION_STATUS(HttpStatus.BAD_REQUEST, "식사가 끝나지 않은 좌석이 있습니다."),
    NOT_EXISTS_TABLE(HttpStatus.BAD_REQUEST, "좌석이 존재하지 않습니다."),
    NOT_BEEN_UNGROUP(HttpStatus.BAD_REQUEST, "좌석 그룹이 해제되지 않았습니다."),
    PEOPLE_LESS_THAN_ZERO(HttpStatus.BAD_REQUEST, "인원 수가 0 보다 작을 수 없습니다."),
    TABLE_IS_EMPTY(HttpStatus.BAD_REQUEST, "좌석이 공석입니다."),
    PRICE_IS_NULL_OR_MINUS(HttpStatus.BAD_REQUEST, "가격은 양수만 입력하세요."),
    ORDER_TABLES_MUST_BE_AT_LEAST_TWO(
            HttpStatus.BAD_REQUEST, "좌석 그룹으로 지정하려면 좌석의 개수가 2개 이상이어야 합니다."),
    NOT_SAME_BETWEEN_ORDER_TABLES_COUNT_AND_SAVED_ORDER_TABLES(
            HttpStatus.BAD_REQUEST, "그룹 지정을 요청한 좌석 수와 저장된 좌석 수가 일치하지 않습니다."),
    NOT_EXISTS_TABLE_GROUP(HttpStatus.BAD_REQUEST, "좌석 그룹이 존재하지 않습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String detail;

    ErrorCode(HttpStatus httpStatus, String detail) {
        this.httpStatus = httpStatus;
        this.detail = detail;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDetail() {
        return detail;
    }
}
