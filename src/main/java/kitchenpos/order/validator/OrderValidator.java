package kitchenpos.order.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreateOrder(OrderRequest orderRequest, List<Menu> menus) {
        validateOrderTable(orderRequest.getOrderTableId());
        validateOrderLineItems(orderRequest.getOrderLineItems());
        validateMenus(orderRequest.getOrderLineItems(), menus);
    }

    private void validateOrderTable(Long id) {
        OrderTable orderTable = orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ORDER_TABLE_IS_NOT_EXIST.getMessage()));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLE_IS_EMPTY_STATUS.getMessage());
        }
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(ErrorCode.ORDER_LINE_ITEMS_IS_EMPTY.getMessage());
        }
    }

    private void validateMenus(List<OrderLineItemRequest> orderLineItems, List<Menu> menus) {
        if (orderLineItems.size() != menus.size()) {
            throw new IllegalArgumentException(ErrorCode.MENU_IS_NOT_EXIST.getMessage());
        }
    }

    public void validateUpdateOrderStatus(Order order) {
        if (order.isCompletionOrder()) {
            throw new IllegalArgumentException(ErrorCode.ORDER_STATUS_COMPLETE.getMessage());
        }
    }
}
