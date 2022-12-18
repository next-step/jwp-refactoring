package kitchenpos.exception;

public class OrderTableError {
    private static final String PREFIX = "[ERROR] ";

    public static final String NOT_FOUND = PREFIX + "주문 테이블을 찾을 수 없습니다.";
    public static final String CANNOT_EMPTY = PREFIX + "주문 테이블이 비어있습니다.";
    public static final String IS_NOT_EMPTY = PREFIX + "주문 테이블이 비어있지 않습니다.";
    public static final String HAS_GROUP = PREFIX + "단체 지정된 테이블은 빈자리 여부를 변경할 수 없습니다.";
    public static final String IS_EMPTY = PREFIX + "빈 테이블은 손님 수를 변경할 수 없습니다.";
    public static final String INVALID_NUMBER_OF_GUESTS = PREFIX + "손님 수는 0 이상의 수로 설정 가능합니다.";
    public static final String REQUIRED_ORDER_TABLE_LIST = PREFIX + "단체 지정은 주문 테이블이 필수로 지정되어야 합니다.";
    public static final String INVALID_ORDER_TABLE_LIST_SIZE = PREFIX + "단체 지정은 2개 이상의 주문 테이블로만 가능합니다.";

    private OrderTableError() {
        
    }
}
