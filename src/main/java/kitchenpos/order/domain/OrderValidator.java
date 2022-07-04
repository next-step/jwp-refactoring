package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        validate(order, getOrderTable(order), getOrderTableCount(order));
    }

    private void validate(Order order, OrderTable orderTable, long orderTableCount) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (order.getOrderLineItems() == null) {
            throw new IllegalArgumentException();
        }

        if (order.getOrderLineItems().getMenuIds().size() != orderTableCount) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTable(Order order) {
        return orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private long getOrderTableCount(Order order) {
        return menuRepository.countByIdIn(order.getOrderLineItems().getMenuIds());
    }

}
