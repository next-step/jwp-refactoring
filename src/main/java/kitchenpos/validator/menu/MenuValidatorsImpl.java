package kitchenpos.validator.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.validator.MenuValidators;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class MenuValidatorsImpl implements MenuValidators {

    private final MenuCreationValidator menuCreationValidator;
    private final MenuProductsPriceValidator menuProductsPriceValidator;

    public MenuValidatorsImpl(MenuCreationValidator menuCreationValidator,
                              MenuProductsPriceValidator menuProductsPriceValidator) {
        this.menuProductsPriceValidator = menuProductsPriceValidator;
        this.menuCreationValidator = menuCreationValidator;
    }

    @Transactional(readOnly = true)
    public void validateCreation(Long menuGroupId) {
        menuCreationValidator.validate(menuGroupId);
    }

    @Transactional(readOnly = true)
    public void validateProductsPrice(Menu menu) {
        menuProductsPriceValidator.validate(menu);
    }

}
