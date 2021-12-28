package kitchenpos.common;

import kitchenpos.exception.ErrorCode;

public enum MenuErrorCode implements ErrorCode {
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다."),
    MENU_NOT_FOUND_EXCEPTION("메뉴를 찾을 수 없습니다."),
    MENU_GROUP_NOT_FOUND_EXCEPTION("없는 메뉴그룹입니다."),
    MENU_PRODUCT_NOT_FOUND("존재하지 않는 상품이 있습니다."),
    MENU_PRICE_OVER_RANGE_EXCEPTION("메뉴가격이 상품 총 가격 보다 클 수 없습니다."),
    MENU_PRICE_MIN_UNDER_EXCEPTION("메뉴가격이 최소가격보다 작을 수 없습니다.");

    private final String errorMessage;

    MenuErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
