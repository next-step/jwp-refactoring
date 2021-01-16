package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
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
        if (requestOrderLineItems == null || requestOrderLineItems.size() <= 0) {
            throw new IllegalArgumentException(ERR_TEXT_INVALID_ORDER);
        }

        if (requestOrderLineItems.size() != menuRepository.countByIdIn(orderRequest.getMenuIds())) {
            throw new IllegalArgumentException(ERR_TEXT_INVALID_ORDER);
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        final List<OrderLineItem> orderLineItems = orderRequest.toOrderLineItemList();

        final Order savedOrder = orderRepository.save(Order.of(orderTable.getId()));
        savedOrder.addOrderLineItems(orderLineItems);
        orderLineItems.forEach(orderLineItemRepository::save);

        return OrderResponse.of(savedOrder);
    }

    @Transactional
    public List<OrderResponse> list() {
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
