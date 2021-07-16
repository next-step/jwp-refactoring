package domain.menu.exception;

import common.error.BusinessException;

public class BadMenuPriceException extends BusinessException {
    public BadMenuPriceException() {
        super("메뉴의 총 가격이 기존 상품들의 총합 가격보다 비쌀 수 없습니다.");
    }

    public BadMenuPriceException(String message) {
        super(message);
    }
}
