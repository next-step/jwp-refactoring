package kitchenpos.menu.application.validator;

import kitchenpos.menu.domain.request.MenuRequest;

public interface MenuValidator {
    void execute(MenuRequest menuRequest);
}
