package kitchenpos.exception;

public class ErrorMessage {
    public static final String ERROR_ORDER_TABLE_EMPTY = "주문테이블은 비어있을 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_NOT_EMPTY = "주문테이블은 비어있어야 합니다.";;
    public static final String ERROR_ORDER_TABLE_NOT_EXISTS = "존재하지 않는 주문테이블이 있습니다.";
    public static final String ERROR_ORDER_TABLE_DUPLICATED = "주문테이블은 중복될 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_GROUPED = "주문테이블은 그룹에 지정되어있을 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_TOO_SMALL = "주문테이블의 개수는 %d 미만일 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_GUESTS_TOO_SMALL = "주문테이블의 손님수는 %d 미만일 수 없습니다.";

    public static final String ERROR_ORDER_INVALID_STATUS = "주문의 상태는 %s일 수 없습니다.";
    public static final String ERROR_ORDER_LINE_ITEM_TOO_SMALL = "주문항목 개수는 %d 미만일 수 없습니다.";
    public static final String ERROR_ORDER_LINE_ITEM_DUPLICATED = "주문항목은 중복이 불가합니다.";
    public static final String ERROR_PRICE_TOO_SMALL = "가격은 %d 미만일 수 없습니다.";
    public static final String ERROR_PRICE_TOO_HIGH = "가격은 %d 초과일 수 없습니다.";
}
