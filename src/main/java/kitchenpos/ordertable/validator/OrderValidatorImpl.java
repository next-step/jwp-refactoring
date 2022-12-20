package kitchenpos.ordertable.validator;

import kitchenpos.common.ErrorMessage;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidatorImpl(final OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    public void validate(OrderRequest orderRequest) {
        OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        validateOrderTable(orderTable);
        validateOrderLineItems(orderRequest.getOrderLineItemsRequest());
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.INVALID_ORDER_TABLE_INFO.getMessage()));
    }

    private void validateOrderTable(OrderTable orderTable) {
        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.EMPTY_ORDER_TABLE.getMessage());
        }
    }

    @Override
    public void validateOnGoingOrderStatus(List<Order> orders) {
        orders.forEach(Order::validateOnGoingOrderStatus);
    }

    private void validateOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
        if (Objects.isNull(orderLineItems) || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_ORDER_LINE_ITEM.getMessage());
        }

        final List<Long> menuIds = fetchMenuIdsFrom(orderLineItems);

        if (orderLineItems.size() != countByIdIn(menuIds)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_MENU_INFO.getMessage());
        }
    }

    private Long countByIdIn(final List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    private List<Long> fetchMenuIdsFrom(final List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }
}
