package kitchenpos.menu.application.validator;

import static kitchenpos.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT;

import java.util.List;
import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.validator.OrderLineItemsSizeValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemsSizeValidatorImpl implements OrderLineItemsSizeValidator {
    private final MenuRepository menuRepository;

    public OrderLineItemsSizeValidatorImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void validateOrderLineItems(int orderLineItemsSize, List<Long> menuIds) {
        if (orderLineItemsSize != getMenuCount(menuIds)) {
            throw new KitchenposException(NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT);
        }
    }

    private int getMenuCount(List<Long> menuIds){
        return menuRepository.countByIdIn(menuIds);
    }
}
