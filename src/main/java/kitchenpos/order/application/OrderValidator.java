package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        OrderLineItems orderLineItems = order.getOrderLineItems();
        List<Menu> menuIds = menuRepository.findAllById(orderLineItems.getMenuIds());
        if (isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
        if (!isSameSize(orderLineItems, menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isEmpty(OrderLineItems orderLineItems) {
        return CollectionUtils.isEmpty(orderLineItems.getList());
    }

    private boolean isSameSize(OrderLineItems orderLineItems, List<Menu> menuIds) {
        return orderLineItems.getList().size() == menuIds.size();
    }
}
