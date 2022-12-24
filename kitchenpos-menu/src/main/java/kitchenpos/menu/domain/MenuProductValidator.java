package kitchenpos.menu.domain;

import org.springframework.stereotype.Component;

@Component
public interface MenuProductValidator {

    void validate(Menu menu);
}
