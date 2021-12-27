package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDateException;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final OrderLineItemRepository orderLineItemRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new InputTableDataException(InputTableDataErrorCode.THE_TABLE_CAN_NOT_FIND));
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems();

        Order createdOrder = new Order(orderTable, new OrderLineItems(orderLineItems));
        return OrderResponse.of(orderRepository.save(createdOrder));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new InputOrderDateException(InputOrderDataErrorCode.THE_ORDER_CAN_NOT_SEARCH));

        if (orderStatus == COOKING) {
            foundOrder.startCooking();
            return OrderResponse.of(foundOrder);
        }

        if (orderStatus == MEAL) {
            foundOrder.startMeal();
            return OrderResponse.of(foundOrder);
        }

        foundOrder.endOrder();
        return OrderResponse.of(foundOrder);

    }
}
