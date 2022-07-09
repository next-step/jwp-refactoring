package kitchenpos.menu.application.validator;

import kitchenpos.menu.dto.MenuRequest;


public interface MenuValidator {
    void validate(MenuRequest menuRequest);
}
