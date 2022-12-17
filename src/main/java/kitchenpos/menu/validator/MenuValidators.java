package kitchenpos.menu.validator;

import kitchenpos.menu.domain.Menu;

public interface MenuValidators {

    void validateCreation(Long menuGroupId);

    void validateProductsPrice(Menu menu);
}
