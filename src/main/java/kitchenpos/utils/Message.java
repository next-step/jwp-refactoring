package kitchenpos.utils;

public class Message {

    public static final String INVALID_NAME_EMPTY = "이름은 빈값일 수 없습니다.";
    public static final String INVALID_PRICE = "가격은 0원 보다 커야 합니다.";
    public static final String INVALID_UNDER_ZERO_GUESTS = "방문한 손님 수는 1명 이상이어야 합니다.";
    public static final String INVALID_EMPTY_LINE_ITEMS = "주문 시 주문항목은 필수값 입니다.";

    public static final String INVALID_MENU_PRICE = "메뉴 가격이 메뉴 상품 금액이하여야 합니다.";
    public static final String INVALID_CHANGE_ORDER_STATUS = "계산 완료된 주문은 상태를 변경할 수 없습니다.";

    public static final String INVALID_ORDER_TABLE_SIZE = "주문 테이블이 2개 이상일 때 단체 지정을 할 수 있습니다.";
    public static final String INVALID_EMPTY_ORDER_TABLE = "이미 주문된 테이블이 포함되어 있어 단체 지정을 할 수 없습니다.";

    public static final String CONTAIN_ALREADY_GROUPED_ORDER_TABLE = "이미 단체 지정된 주문 테이블이 있어서 단체 지정을 할 수 없습니다.";

    public static final String INVALID_CANCEL_ORDER_TABLES_STATUS = "주문 테이블들의 상태가 조리 또는 식사이면 단체 지정을 취소할 수 없습니다.";

    public static final String INVALID_CHANGE_TO_EMPTY_GROUPED_TABLE = "단체 지정이 되어있는 주문 테이블은 빈 상태를 변경할 수 없습니다.";
    public static final String INVALID_CHANGE_TO_EMPTY_ORDER_STATUS = "주문 상태가 조리 또는 식사이면 주문 테이블의 빈 상태를 변경할 수 없습니다.";

    public static final String CAN_NOT_CHANGE_NUMBER_OF_GUESTS = "주문 테이블이 빈 테이블이면 방문한 손님 수를 변경할 수 없습니다.";

}
