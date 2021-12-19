package kitchenpos.common.domain.order.validator;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import kitchenpos.common.domain.menu.Menu;
import kitchenpos.common.domain.menu.MenuRepository;
import kitchenpos.common.domain.order.Order;
import kitchenpos.common.domain.order.OrderLineItem;

@Component
public class OrderLineItemValidator {
    private static final String NOT_EXIST_MENU = "Menu 가 존재하지 않습니다.";
    private static final String NOT_EXIST_ORDER = "Order 가 존재하지 않습니다.";

    private final MenuRepository menuRepository;

    public OrderLineItemValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateOrderLineItem(OrderLineItem orderLineItem) {
        validateExistOrders(orderLineItem.getOrder());
        validateExistMenu(orderLineItem.getMenuId());
    }

    private static void validateExistOrders(Order order) {
        if (Objects.isNull(order)) {
            throw new EntityNotFoundException(NOT_EXIST_ORDER);
        }
    }

    private void validateExistMenu(Long menuId) {
        Optional<Menu> menuOpt = menuRepository.findById(menuId);
        if (!menuOpt.isPresent()) {
            throw new EntityNotFoundException(NOT_EXIST_MENU);
        }
    }
}
