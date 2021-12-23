package kitchenpos.common.exception;

import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.tablegroup.domain.OrderTables;

public enum CommonErrorCode implements ErrorCode {
    DATABASE_CONSTRAINT_VIOLATION("고유값이 이미 존재합니다."),
    NOT_EMPTY("빈 값을 입력 할 수 없습니다."),

    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다."),

    MENU_NOT_FOUND_EXCEPTION("메뉴를 찾을 수 없습니다."),
    MENU_GROUP_NOT_FOUND_EXCEPTION("없는 메뉴그룹입니다."),
    MENU_PRODUCT_NOT_FOUND("존재하지 않는 상품이 있습니다."),
    MENU_PRICE_OVER_RANGE_EXCEPTION("메뉴가격이 상품 총 가격 보다 클 수 없습니다."),
    MENU_PRICE_MIN_UNDER_EXCEPTION("메뉴가격이 최소가격보다 작을 수 없습니다."),

    TABLE_GROUP_NOT_FOUND_EXCEPTION("단체지정을 찾을 수 없습니다."),
    TABLE_NOT_CREATED_EXCEPTION("단체 지정에 속하는 주문테이블은 모두 등록되어있어야합니다."),
    TABLE_NOT_EMPTY_EXCEPTION("빈 테이블이 아닙니다."),
    NUMBER_OF_GUESTS_MIN_UNDER_EXCEPTION(
        String.format("%s명 이하의 손님을 설정 할 수 없습니다.", NumberOfGuests.MIN)),

    ORDER_NOT_FOUND_EXCEPTION("주문을 찾을 수 없습니다."),
    ORDER_TABLE_NOT_FOUND_EXCEPTION("주문 테이블을 찾을 수 없습니다."),
    ORDER_STATUS_COMPLETE_NOT_CHANGE_STATUS_EXCEPTION("결제완료 상태에서는 주문 상태를 변경 할 수 없습니다."),
    ORDER_IN_TABLE_EMPTY_EXCEPTION("주문할 테이블은 빈테이블일 수 없습니다."),
    ORDER_TABLE_CHANGE_EMPTY_NOT_COMPLETE_EXCEPTION("계산 완료 상태가 아니면 빈테이블 상태를 변경 할 수 없습니다."),
    ORDER_TABLE_UNGROUP_NOT_COMPLETE_EXCEPTION("계산 완료 상태가 아니면 단체지정을 해지 할 수 없습니다."),
    ORDER_TABLE_EXISTS_TABLE_GROUP_EXCEPTION("단체지정이 되어있는 테이블입니다."),
    ORDER_TABLES_MIN_UNDER_EXCEPTION(
        String.format("단체 지정에 속하는 주문테이블은 %s개 이상이어야 합니다.", OrderTables.ORDER_TABLES_MIN_SIZE)),
    ;

    private final String errorMessage;

    CommonErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
