package kitchenpos.order.application;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.exception.OrderLineItemException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.OrderTableException;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public OrderTable tableValidIsEmpty(Long tableId) {
        OrderTable orderTable = orderTableRepository.findById(tableId)
                                    .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new OrderTableException(OrderTableException.ORDER_TABLE_IS_EMPTY_MSG);
        }

        return orderTable;
    }

    public void orderLineItemsValidation(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        if(menuRepository.countByIdIn(orderLineItemIds(orderLineItems)) != orderLineItems.size()) {
            throw new OrderLineItemException(OrderLineItemException.ORDER_LINE_ITEM_SIZE_INVALID);
        }
    }

    private List<Long> orderLineItemIds(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                            .map(OrderLineItemRequest::getMenuId)
                            .collect(Collectors.toList());
    }
}
