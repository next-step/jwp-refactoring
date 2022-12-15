package kitchenpos.exception;

public class ErrorMessage {



    private ErrorMessage() {
        throw new AssertionError();
    }

    private static final String CANNOT_BE_NULL = " 은(는) 비어있을 수 없습니다.";
    private static final String CANNOT_BE_NEGATIVE = " 은(는) 음수일 수 없습니다.";
    public static String cannotBeNull(String propertyName) {
        return propertyName + CANNOT_BE_NULL;
    }
    public static String cannotBeNegative(String propertyName) {
        return propertyName + CANNOT_BE_NEGATIVE;
    }
    public static String notFoundEntity(String entityName, Long id) {
        return String.format("요청하신 엔티티를 찾을 수 없습니다. 엔티티명: [%s], 요청아이디 [%d]", entityName, id);
    }


    public static final String PRICE_HIGHER_THAN_MENU_PRODUCTS_TOTAL_PRICES = "메뉴의 가격은 메뉴상품들의 총 가격보다 높을 수 없습니다.";
    public static final String CANNOT_ORDER_WHEN_TABLE_IS_EMPTY = "빈 테이블은 주문을 할 수 없다.";
    public static final String CANNOT_CHANGE_ORDER_STATUS_WHEN_COMPLETED = "주문상태가 완료된 경우에는 주문상태를 변경할 수 없다.";
    public static final String CANNOT_CHANGE_EMPTINESS_WHEN_TABLE_GROUPED = "단체지정이 된 경우 주문테이블의 비움상태를 변경할 수 없다.";
    public static final String CANNOT_CHANGE_EMPTINESS_WHEN_ORDER_NOT_COMPLETED = "주문이 조리중이거나 식사중인 경우 주문 테이블의 비움상태를 변경하 수 없다.";
    public static final String CANNOT_CHANGE_NUMBER_OF_GUESTS_WHEN_TABLE_IS_EMPTY = "빈 테이블의 손님수는 변경할 수 없다.";
    public static final String CANNOT_TABLE_GROUP_WHEN_IS_NOT_ALL_EMPTY = "빈테이블이 있는 경우 단체지정을 할 수 없다.";
    public static final String CANNOT_TABLE_GROUP_WHEN_SIZE_SIZE_IS_TOO_SMALL = "대상 테이블이 2개 미만일때 단체지정 할 수 없다.";
    public static final String CANNOT_TABLE_GROUP_WHEN_ALREADY_GROUPED = "이미 단체지정된 테이블은 단체지정을 할 수없다.";

    public static final String CANNOT_UNGROUP_WHEN_ORDER_NOT_COMPLETED  = "아직 조리 및 식사가 안끝난 테이블이 있는 경우 단체지정 해제를 할 수 없다.";
}
