package order.exception;

public class OrderErrorCode {
    public static String NOT_EXISTS_ORDER_LINE_ITEM = "요청에 OrderLineItem 이 없습니다.";
    public static String EMPTY_ORDER_TABLE = " 주문테이블이 비어있어서 주문을 생성할 수 없습니다.";
    public static String ORDER_COMPLETE = "주문 상태가 완료면 주문 상태를 변경할 수 없습니다.";
}
