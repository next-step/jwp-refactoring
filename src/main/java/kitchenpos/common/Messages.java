package kitchenpos.common;

public class Messages {
    public static final String PRICE_CANNOT_ZERO_LESS_THAN = "[ERROR] 가격은 0원 이상 입력해야 합니다.";
    public static final String NAME_CANNOT_EMPTY = "[ERROR] 이름은 필수로 입력해야 합니다.";
    public static final String QUANTITY_CANNOT_ZERO_LESS_THAN = "[ERROR] 수량은 0개 이상 입력해야 합니다.";

    public static final String PRODUCT_FIND_IN_NO_SUCH = "[ERROR] 상품 리스트가 정상적으로 조회되지 않았습니다.";
    public static final String MENU_PRICE_EXPENSIVE = "[ERROR] 상품가격의 합보다 메뉴의 가격이 더 높을 수 없습니다.";
    public static final String MENU_GROUP_NOT_EXISTS = "[ERROR] 메뉴 그룹 정보가 존재하지 않습니다.";

    public static final String NUMBER_OF_GUESTS_CANNOT_ZERO = "[ERROR] 손님은 0명보다 많아야 합니다.";
    public static final String HAS_ORDER_TABLE_GROUP = "[ERROR] 주문 테이블이 비어있지 않습니다.";
    public static final String ORDER_TABLE_STATUS_CANNOT_UPDATE = "[ERROR] 주문 테이블의 상태를 업데이트 할 수 없습니다.";
    public static final String ORDER_TABLE_CANNOT_EMPTY = "[ERROR] 주문 테이블이 비어있는 상태입니다.";

    public static final String TABLE_GROUP_ORDER_IDS_REQUIRED = "[ERROR] 테이블 그룹 등록시 주문테이블 ID는 필수입니다.";
    public static final String TABLE_GROUP_ORDER_IDS_FIND_IN_NO_SUCH = "[ERROR] 테이블 그룹이 정상적으로 조회되지 않았습니다.";
    public static final String TABLE_GROUP_ORDER_NOT_EMPTY = "[ERROR] 테이블 그룹내 주문테이블이 비어있지 않습니다.";

    public static final String ORDER_LINE_ITEM_REQUIRED = "[ERROR] 주문 항목은 필수입니다.";
    public static final String ORDER_LINE_ITEM_IDS_FIND_IN_NO_SUCH = "[ERROR] 주문 항목이 정상적으로 조회되지 않았습니다.";
    public static final String ORDER_STATUS_CHANGE_CANNOT_COMPLETION = "[ERROR] 주문건이 완료된 상태입니다.";
}
