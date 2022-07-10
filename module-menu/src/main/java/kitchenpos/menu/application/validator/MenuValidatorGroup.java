package kitchenpos.menu.application.validator;

import kitchenpos.menu.dto.MenuRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuValidatorGroup {
    private List<MenuValidator> menuValidators;

    public MenuValidatorGroup(List<MenuValidator> menuValidators) {
        this.menuValidators = menuValidators;
    }

    public void validate(MenuRequest menuRequest) {
        menuValidators.forEach(it -> it.validate(menuRequest));
    }
}
