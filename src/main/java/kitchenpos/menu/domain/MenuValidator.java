package kitchenpos.menu.domain;

import kitchenpos.domain.Price;

public class MenuValidator {

    private Price sumPrice;

    private MenuValidator() {}

    public MenuValidator(Price sumPrice) {
        this.sumPrice = sumPrice;
    }

    public void validate(Price price) {
        validatePrice(price);
    }

    public void validatePrice(Price price) {
        if (price.isGather(sumPrice)) {
            throw new IllegalArgumentException("최종 금액은 메뉴 금액의 합보다 커서는 안됩니다.");
        }
    }
}
