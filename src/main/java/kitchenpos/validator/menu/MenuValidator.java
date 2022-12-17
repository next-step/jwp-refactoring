package kitchenpos.validator.menu;

import kitchenpos.menu.domain.Menu;

public abstract class MenuValidator {

    protected void validate(Long menuGroupId) {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다");
    }

    protected void validate(Menu menu) {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다");
    }
}
