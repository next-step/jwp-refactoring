package kitchenpos.menu.validator;

import kitchenpos.menu.domain.Menu;
import org.springframework.stereotype.Component;

@Component
public interface MenuProductValidator {

    void validate(Menu menu);
}
