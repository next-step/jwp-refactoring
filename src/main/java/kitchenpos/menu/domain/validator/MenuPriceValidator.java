package kitchenpos.menu.domain.validator;

import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProductGroup;

public interface MenuPriceValidator {
    void validate(MenuPrice price, MenuProductGroup menuProducts);
}
