package kitchenpos.exception;

public class ErrorMessage {

    private ErrorMessage() {
    }

    public static final String GROUP_TABLE_ERROR = "빈 테이블이 아니거나 이미 단체지정 되어있습니다";
    public static final String GROUP_TABLE_REQUEST_ERROR = "단체지정 요청 주문테이블이 올바르지 않습니다";
    public static final String NOT_EXIST_ID = "존재하지 않는 아이디입니다";
    public static final String EMPTY_TABLE = "빈 테이블은 변경할 수 없습니다";
    public static final String INVALID_NUMBER_OF_GUESTS = "방문손님수는 1명이상 가능합니다";
    public static final String CANNOT_CHANGE_BY_ORDER_STATUS = "주문 상태 때문에 변경할 수 없습니다";
    public static final String NOT_ALLOWED_CHANGE_EMPTY_WHEN_GROUP = "단체지정된 경우 빈 테이블 여부를 변경할 수 없습니다";

}
