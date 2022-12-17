package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    public static final String COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE = "주문완료일 경우 주문상태를 변경할 수 없다.";

    public static final String ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE = "주문 항목이 비어있을 수 없다.";
    public static final String ORDERLINEITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE = "주문 항목의 수와 메뉴의 수는 같아야 한다.";
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository, final OrderLineItemRepository orderLineItemRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = request.toOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.findAllById(menuIds).size()) {
            throw new IllegalArgumentException(ORDERLINEITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE);
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        final Order savedOrder = orderRepository.save(new Order(orderTable, request.toOrderLineItems()));

        return new OrderResponse(savedOrder);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(order.getOrderStatus()).name());
        return orderRepository.save(savedOrder);
    }
}
