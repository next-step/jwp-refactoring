package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new InputTableDataException(InputTableDataErrorCode.THE_TABLE_CAN_NOT_FIND));
        List<OrderLineItem> orderLineItemBasket = orderRequest.getOrderLineItems();
        OrderLineItems orderLineItems = new OrderLineItems(orderLineItemBasket);

        OrderValidator orderValidator = new OrderValidator(orderLineItems);
        orderValidator.isEmptyOrderLineItem();

        Order createdOrder = new Order(orderTable.getId(), orderLineItems);
        return OrderResponse.of(orderRepository.save(createdOrder));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_CAN_NOT_SEARCH));

        OrderValidator orderValidator = new OrderValidator(foundOrder);
        orderValidator.isAlreadyCompletionOrder();

        foundOrder.updateOrderStatus(orderStatus);
        return OrderResponse.of(foundOrder);
    }
}
