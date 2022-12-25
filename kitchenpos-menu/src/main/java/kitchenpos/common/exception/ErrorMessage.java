package kitchenpos.common.exception;

public class ErrorMessage {
    private ErrorMessage() {
        throw new AssertionError();
    }

    public static final String CANNOT_BE_NULL = " 은(는) 비어있을 수 없습니다.";
    public static final String CANNOT_BE_NEGATIVE = " 은(는) 음수일 수 없습니다.";
    public static final String PRICE_HIGHER_THAN_MENU_PRODUCTS_TOTAL_PRICES = "메뉴의 가격은 메뉴상품들의 총 가격보다 높을 수 없습니다.";

    public static String cannotBeNull(String propertyName) {
        return propertyName + CANNOT_BE_NULL;
    }

    public static String cannotBeNegative(String propertyName) {
        return propertyName + CANNOT_BE_NEGATIVE;
    }

    public static String notFoundEntity(String entityName, Long id) {
        return String.format("요청하신 엔티티를 찾을 수 없습니다. 엔티티명: [%s], 요청아이디 [%d]", entityName, id);
    }
}
