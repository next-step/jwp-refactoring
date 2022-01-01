package kitchenpos.application;

import kitchenpos.common.exceptions.EmptyMenuException;
import kitchenpos.domain.Menu;
import kitchenpos.dto.order.OrderLineItemRequest;
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
