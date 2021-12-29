package kitchenpos.menu.domain.validator;

import kitchenpos.menu.domain.Menu;

public interface MenuPriceCreateValidator extends MenuCreateValidator {
    void validate(Menu menu);
}
