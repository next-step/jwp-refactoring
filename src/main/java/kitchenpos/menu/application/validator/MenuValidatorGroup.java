package kitchenpos.menu.application.validator;

import kitchenpos.menu.domain.request.MenuRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MenuValidatorGroup {
    private final MenuValidator menuCreationValidator;
    private final MenuValidator menuPriceValidator;
    private List<MenuValidator> validators;

    public MenuValidatorGroup(MenuValidator menuCreationValidator, MenuValidator menuPriceValidator) {
        this.menuCreationValidator = menuCreationValidator;
        this.menuPriceValidator = menuPriceValidator;
        this.validators = Arrays.asList(menuCreationValidator, menuPriceValidator);
    }

    public void validate(MenuRequest menuRequest) {
        validators.forEach(it -> it.execute(menuRequest));
    }
}
