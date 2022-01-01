package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.exception.EmptyMenuException;
import kitchenpos.order.dto.OrderLineItemRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@Transactional(readOnly = true)
public class OrderValidator {

    private final MenuService menuService;

    public OrderValidator(final MenuService menuService) {
        this.menuService = menuService;
    }

    public void validatorMenu(final OrderLineItemRequest request) {
        final Menu menu = menuService.getMenuById(request.getMenuId());
        if (Objects.isNull(menu)) {
            throw new EmptyMenuException();
        }
    }
}
