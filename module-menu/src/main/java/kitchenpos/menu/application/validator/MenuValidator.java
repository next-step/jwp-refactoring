package kitchenpos.menu.application.validator;

import kitchenpos.menu.dto.request.MenuRequest;

public interface MenuValidator {
    void validate(MenuRequest menuRequest);
}
