package kitchenpos.order.validator;

import kitchenpos.common.constants.ErrorCodeType;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.port.OrderTablePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.common.constants.ErrorCodeType.ORDER_STATUS_NOT_COMPLETION;

@Service
@Transactional(readOnly = true)
public class OrderValidator {

    private final OrderTablePort orderTablePort;

    public OrderValidator(OrderTablePort orderTablePort) {
        this.orderTablePort = orderTablePort;
    }

    public void validOrder(OrderRequest request, List<Menu> menus) {
        isExistOrderTable(request.getOrderTableId());
        validCheckMenuSize(request.getOrderLineItemRequest(), menus);
    }

    private void isExistOrderTable(Long orderTableId) {
        orderTablePort.findById(orderTableId);
    }

    private void validCheckMenuSize(List<OrderLineItemRequest> orderLineItemRequest, List<Menu> menus) {
        if (orderLineItemRequest.size() != menus.size()) {
            throw new IllegalArgumentException(ErrorCodeType.ORDER_LINE_ITEM_REQUEST.getMessage());
        }
    }

    public void validChangeOrderStatus(Order order) {
        if (order.isCompletion()) {
            throw new IllegalArgumentException(ORDER_STATUS_NOT_COMPLETION.getMessage());
        }
    }
}
