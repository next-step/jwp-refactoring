package kitchenpos.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository,
        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        validTable(order);

        OrderLineItems orderLineItems = new OrderLineItems(order.getOrderLineItemValues());
        validateMenu(orderLineItems);
    }

    private void validTable(Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(KitchenposNotFoundException::new);
        orderTable.validateNotEmpty();
    }

    private void validateMenu(OrderLineItems orderLineItems) {
        orderLineItems.getOrderLineItems()
            .forEach(orderLineItem -> menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(KitchenposNotFoundException::new));

        final List<Long> menuIds = orderLineItems.getMenuIds();
        orderLineItems.validateSize(menuRepository.countByIdIn(menuIds));
    }
}
