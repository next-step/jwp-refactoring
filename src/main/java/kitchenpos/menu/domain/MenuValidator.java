package kitchenpos.menu.domain;

import kitchenpos.domain.Price;

public class MenuValidator {

    public static void validate(Price finalPrice, Price sumPrice) {
        validatePrice(finalPrice, sumPrice);
    }

    public static void validatePrice(Price finalPrice, Price sumPrice) {
        if (finalPrice.isGather(sumPrice)) {
            throw new IllegalArgumentException("최종 금액은 메뉴 금액의 합보다 커서는 안됩니다.");
        }
    }
}
