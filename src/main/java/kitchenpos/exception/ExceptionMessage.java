package kitchenpos.exception;

public class ExceptionMessage {
    public static final String INVALID_MENU_GROUP_NAME_SIZE = "메뉴 그룹 이름은 1자 이상이어야 합니다.";
    public static final String INVALID_PRODUCT_NAME_SIZE = "상품 이름은 1자 이상이어야 합니다.";
    public static final String INVALID_MENU_NAME_SIZE = "메뉴 이름은 1자 이상이어야 합니다.";
    public static final String INVALID_MENU_PRICE = "유효하지 않은 메뉴 가격입니다.";
    public static final String MENU_PRICE_GREATER_THAN_AMOUNT = "메뉴 가격이 메뉴 상품 금액(상품가격 * 상품수량) 보다 작거나 같아야합니다.";
    public static final String EMPTY_ORDER_LINE_ITEM = "주문 시 주문항목이 비어있으면 안됩니다.";
    public static final String EMPTY_ORDER_TABLE = "주문 시 주문 테이블이 빈 상태여서는 안됩니다.";
    public static final String INVALID_NUMBER_OF_GUESTS_SIZE = "방문한 손님 수는 음수일 수 없습니다.";
    public static final String ORDER_STATUS_CHANGE = "계산 완료된 상태에서는 주문 상태를 변경할 수 없습니다.";

    private ExceptionMessage() {
    }
}
