package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.exception.NotEqualsMenuAndOrderLineItemMenuException;
import kitchenpos.exception.NotExistOrderLineItemsException;
import kitchenpos.exception.OrderTableAlreadyEmptyException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
        validateOrderLineItems(order.getOrderLineItems());
        validateExistMenu(order.getOrderLineItems());
        validateEmptyOrderTable(order.getOrderTableId());
    }

    private void validateExistMenu(OrderLineItems orderLineItems) {
        List<Long> menuIds = orderLineItems.getReadOnlyValues()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        long menuCount = menuRepository.countByIdIn(menuIds);
        if (menuIds.size() != menuCount) {
            throw new NotEqualsMenuAndOrderLineItemMenuException(menuCount, menuIds.size());
        }
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems.getReadOnlyValues())) {
            throw new NotExistOrderLineItemsException();
        }
    }

    private void validateEmptyOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new OrderTableAlreadyEmptyException(orderTable.getId());
        }
    }
}
