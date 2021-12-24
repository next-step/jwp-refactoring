package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuDto;

public interface MenuValidator {
    public Menu getValidatedMenu(MenuDto menuDto);
}
