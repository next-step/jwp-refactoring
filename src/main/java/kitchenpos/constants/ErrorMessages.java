package kitchenpos.constants;

public class ErrorMessages {
    public static final String MENU_PRICE_IS_NULL = "메뉴 가격이 누락되었습니다.";
    public static final String PRODUCT_PRICE_IS_NULL = "상품 가격이 누락되었습니다.";
    public static final String MENU_PRICE_CANNOT_BE_LESS_THAN_ZERO = "메뉴 가격은 0원 보다 작을 수 없습니다.";
    public static final String PRODUCT_PRICE_CANNOT_BE_LESS_THAN_ZERO = "상품 가격은 0원 보다 작을 수 없습니다.";
    public static final String MENU_GROUP_NAME_CANNOT_BE_EMPTY = "메뉴 그룹 이름은 1자 이상이어야 합니다.";
    public static final String MENU_NAME_CANNOT_BE_EMPTY = "메뉴 이름은 1자 이상이어야 합니다.";
    public static final String PRODUCT_NAME_CANNOT_BE_EMPTY = "상품 이름은 1자 이상이어야 합니다.";
    public static final String NOT_EMPTY_TABLE_CAN_SET_GROUP = "비어있지 않은 주문 테이블은 단체 지정할 수 없습니다.";
    public static final String TABLE_ALREADY_SET_GROUP = "이미 단체 지정된 주문 테이블은 다시 단체 지정할 수 없습니다";
    public static final String SOME_ORDER_TABLE_TO_GROUP_DOES_NOT_FOUND = "단체 지정하려는 주문 테이블 중 존재하지 않는 테이블이 있습니다.";
    public static final String ORDER_TABLE_DOES_NOT_EXIST = "존재하지 않는 주문 테이블입니다.";
    public static final String ORDER_TABLE_TO_GROUP_CANNOT_BE_LESS_THAN_TWO = "단체 지정하려면 적어도 2개 이상의 주문 테이블을 지정해주셔야 합니다.";
    public static final String NOT_COMPLETED_ORDER_EXIST = "아직 계산 완료되지 않은 주문이 존재합니다.";
}
