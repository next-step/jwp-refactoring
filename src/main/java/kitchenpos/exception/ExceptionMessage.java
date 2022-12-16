package kitchenpos.exception;

public class ExceptionMessage {
    public static final String INVALID_MENU_GROUP_NAME_SIZE = "메뉴 그룹 이름은 1자 이상이어야 합니다.";
    public static final String INVALID_PRODUCT_NAME_SIZE = "상품 이름은 1자 이상이어야 합니다.";
    public static final String INVALID_MENU_NAME_SIZE = "메뉴 이름은 1자 이상이어야 합니다.";
    public static final String INVALID_MENU_PRICE = "유효하지 않은 메뉴 가격입니다.";
    public static final String INVALID_PRODUCT_PRICE = "유효하지 않은 상품 가격입니다.";
    public static final String MENU_PRICE_GREATER_THAN_AMOUNT = "메뉴 가격이 메뉴 상품 금액(상품가격 * 상품수량) 보다 작거나 같아야합니다.";
    public static final String EMPTY_ORDER_LINE_ITEM = "주문 시 주문항목이 비어있으면 안됩니다.";
    public static final String EMPTY_ORDER_TABLE = "주문 시 주문 테이블이 빈 상태여서는 안됩니다.";
    public static final String INVALID_NUMBER_OF_GUESTS_SIZE = "방문한 손님 수는 음수일 수 없습니다.";
    public static final String ORDER_STATUS_CHANGE = "계산 완료된 상태에서는 주문 상태를 변경할 수 없습니다.";
    public static final String CAN_NOT_CHANGE_EMPTY_WHEN_TABLE_GROUPED = "단체 지정이 되어있는 주문 테이블은 빈 상태를 변경할 수 없습니다.";
    public static final String CAN_NOT_CHANGE_EMPTY_WHEN_COOKING_OR_MEAL = "주문 상태가 조리 또는 식사이면 주문 테이블의 빈 상태를 변경할 수 없습니다.";
    public static final String CAN_NOT_CHANGE_NUMBER_OF_GUESTS = "주문 테이블이 빈 테이블이면 방문한 손님 수를 변경할 수 없습니다.";
    public static final String CAN_NOT_UN_GROUP_ORDER_TABLES = "주문 테이블들의 상태가 조리 또는 식사이면 단체 지정을 취소할 수 없습니다.";
    public static final String INVALID_ORDER_TABLE_SIZE = "주문 테이블이 2개 이상일 때 단체 지정을 할 수 있습니다.";
    public static final String NOT_EMPTY_ORDER_TABLE_EXIST = "빈 테이블이 아닌 주문 테이블이 있이서 단체 지정을 할 수 없습니다.";
    public static final String ALREADY_GROUPED_ORDER_TABLE_EXIST = "이미 단체 지정된 주문 테이블이 있어서 단체 지정을 할 수 없습니다.";
    public static final String PRODUCT_NOT_FOUND = "상품이 존재하지 않습니다.";
    public static final String MENU_GROUP_NOT_FOUND = "메뉴그룹이 존재하지 않습니다.";
    public static final String ORDER_TABLE_NOT_FOUND = "주문테이블이 존재하지 않습니다.";
    public static final String MENU_NOT_FOUND = "메뉴가 존재하지 않습니다.";
    public static final String ORDER_NOT_FOUND = "주문이 존재하지 않습니다.";
    public static final String INVALID_ORDER_MENU_PRICE = "주문한 메뉴의 가격이 유효하지 않습니다.";
    public static final String INVALID_ORDER_MENU_NAME = "주문한 메뉴 이름은 1자 이상이어야 합니다.";

    private ExceptionMessage() {
    }
}
