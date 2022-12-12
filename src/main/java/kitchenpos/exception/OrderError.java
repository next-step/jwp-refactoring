package kitchenpos.exception;

public class OrderError {
    private static final String PREFIX = "[ERROR] ";

    public static final String NOT_FOUND = PREFIX + "주문을 찾을 수 없습니다.";
    public static final String CANNOT_CHANGE = PREFIX + "조리중/식사중인 주문은 변경할 수 없습니다.";
    public static final String CANNOT_CHANGE_STATUS = PREFIX + "완료된 주문은 상태를 변경할 수 없습니다.";

    private OrderError() {
        
    }
}
