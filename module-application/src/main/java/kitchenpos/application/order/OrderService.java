package kitchenpos.application.order;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.*;
import kitchenpos.domain.order.dto.OrderLineItemRequest;
import kitchenpos.domain.order.dto.OrderRequest;
import kitchenpos.domain.order.dto.OrderResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final String ERR_TEXT_INVALID_ORDER = "유효하지 않은 주문 정보입니다.";
    private static final String ERR_TEXT_CAN_NOT_CHANGE_ORDER_STATUS = "완료된 주문은 상태를 변경할 수 없습니다.";

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderService(final OrderRepository orderRepository, final OrderLineItemRepository orderLineItemRepository,
                        final OrderTableRepository orderTableRepository, final MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> requestOrderLineItems = orderRequest.getOrderLineItems();

        checkRequestOrderMenuIsExist(orderRequest, requestOrderLineItems);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        final List<OrderLineItem> orderLineItems = orderRequest.toOrderLineItemList();

        final Order savedOrder = orderRepository.save(Order.of(orderTable.getId()));
        savedOrder.addOrderLineItems(orderLineItems);
        orderLineItems.forEach(orderLineItemRepository::save);

        return OrderResponse.of(savedOrder);
    }

    private void checkRequestOrderMenuIsExist(final OrderRequest orderRequest, final List<OrderLineItemRequest> requestOrderLineItems) {
        if (requestOrderLineItems.size() != menuRepository.countByIdIn(orderRequest.getMenuIds())) {
            throw new IllegalArgumentException(ERR_TEXT_INVALID_ORDER);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrderList() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order foundOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (foundOrder.isOrderStatusEqualsCompletion()) {
            throw new IllegalArgumentException(ERR_TEXT_CAN_NOT_CHANGE_ORDER_STATUS);
        }

        foundOrder.changeOrderStatus(OrderStatus.matchOrderStatus(orderRequest.getOrderStatus()));

        return OrderResponse.of(foundOrder);
    }
}
