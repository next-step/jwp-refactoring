package kitchenpos.order.validator;

import kitchenpos.common.constants.ErrorCodeType;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.port.OrderTablePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static kitchenpos.common.constants.ErrorCodeType.ORDER_LINE_ITEM_REQUEST;
import static kitchenpos.common.constants.ErrorCodeType.ORDER_STATUS_NOT_COMPLETION;

@Service
@Transactional(readOnly = true)
public class OrderValidator {

    private final OrderTablePort orderTablePort;

    public OrderValidator(OrderTablePort orderTablePort) {
        this.orderTablePort = orderTablePort;
    }

    public void validOrder(OrderRequest request, List<Menu> menus) {
        orderTablePort.findById(request.getOrderTableId());
        validCheckEmptyOrderLineItems(request);
        validCheckMenuSize(request.getOrderLineItemRequest(), menus);
    }

    private void validCheckMenuSize(List<OrderLineItemRequest> orderLineItemRequest, List<Menu> menus) {
        if (orderLineItemRequest.size() != menus.size()) {
            throw new IllegalArgumentException(ErrorCodeType.ORDER_LINE_ITEM_REQUEST.getMessage());
        }
    }

    private void validCheckEmptyOrderLineItems(OrderRequest request) {
        if (Objects.isNull(request.getOrderLineItemRequest())) {
            throw new IllegalArgumentException(ORDER_LINE_ITEM_REQUEST.getMessage());
        }
    }

    public void validChangeOrderStatus(OrderStatus orderStatus) {
        if (orderStatus.isCompletion()) {
            throw new IllegalArgumentException(ORDER_STATUS_NOT_COMPLETION.getMessage());
        }
    }
}
