package kitchenpos.order.validator;

import kitchenpos.constants.ErrorCodeType;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.port.OrderTablePort;
import org.flywaydb.core.api.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static kitchenpos.constants.ErrorCodeType.ORDER_LINE_ITEM_REQUEST;
import static kitchenpos.constants.ErrorCodeType.ORDER_STATUS_NOT_COMPLETION;

@Service
@Transactional
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
        if (orderLineItemRequest.isEmpty()) {
            throw new IllegalArgumentException(ErrorCodeType.ORDER_LINE_ITEM_REQUEST.getMessage());
        }
    }

    private void validCheckEmptyOrderLineItems(OrderRequest request) {
        if (request.getOrderLineItemRequest().isEmpty()) {
            throw new IllegalArgumentException(ORDER_LINE_ITEM_REQUEST.getMessage());
        }
    }

    public void validChangeOrderStatus(OrderStatus orderStatus) {

        System.out.println("==========");
        System.out.println(orderStatus);


        if (orderStatus.isCompletion()) {
            throw new IllegalArgumentException(ORDER_STATUS_NOT_COMPLETION.getMessage());
        }
    }
}
