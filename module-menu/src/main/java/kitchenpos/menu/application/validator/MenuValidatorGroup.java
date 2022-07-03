package kitchenpos.menu.application.validator;

import java.util.List;
import kitchenpos.menu.dto.request.MenuRequest;
import org.springframework.stereotype.Component;

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
