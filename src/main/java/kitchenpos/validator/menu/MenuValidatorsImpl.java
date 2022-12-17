package kitchenpos.validator.menu;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.validator.MenuValidators;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class MenuValidatorsImpl implements MenuValidators {

    private final List<MenuValidator> menuValidators;

    public MenuValidatorsImpl(List<MenuValidator> menuValidators) {
        this.menuValidators = menuValidators;
    }

    @Transactional(readOnly = true)
    public void validateCreation(Long menuGroupId) {
        menuValidators.get(0).validate(menuGroupId);
    }

    @Transactional(readOnly = true)
    public void validateProductsPrice(Menu menu) {
        menuValidators.get(1).validate(menu);
    }

}
