package kitchenpos.menu.application.validator;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.dto.request.MenuRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuValidatorGroup {
    private final MenuValidator menuCreationValidator;
    private final MenuValidator menuPriceValidator;
    private List<MenuValidator> menuValidators;

    public MenuValidatorGroup(MenuValidator menuCreationValidator, MenuValidator menuPriceValidator) {
        this.menuCreationValidator = menuCreationValidator;
        this.menuPriceValidator = menuPriceValidator;
        this.menuValidators = Arrays.asList(menuCreationValidator, menuPriceValidator);
    }

    public void validate(MenuRequest menuRequest) {
        menuValidators.forEach(it -> it.validate(menuRequest));
    }
}
