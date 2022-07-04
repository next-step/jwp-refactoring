package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validate(Order order) {
        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId()).orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        OrderLineItems orderLineItems = order.getOrderLineItems();
        List<Menu> menuIds = menuRepository.findAllById(orderLineItems.getMenuIds());
        if (CollectionUtils.isEmpty(orderLineItems.getList()) || !isSameSize(orderLineItems, menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isSameSize(OrderLineItems orderLineItems, List<Menu> menuIds) {
        return orderLineItems.getList().size() == menuIds.size();
    }
}
