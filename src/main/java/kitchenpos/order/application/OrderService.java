package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    public static final String COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE = "주문완료일 경우 주문상태를 변경할 수 없다.";

    public static final String ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE = "주문 항목이 비어있을 수 없다.";
    public static final String ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE = "주문 항목의 수와 메뉴의 수는 같아야 한다.";
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderLineItems orderLineItems = request.toOrderLineItems();
        validateOrderItems(orderLineItems);
        return new OrderResponse(orderRepository.save(new Orders(findOrderTable(request), request.toOrderLineItems())));
    }

    public List<Orders> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Orders changeOrderStatus(final Long orderId, final OrderStatusChangeRequest order) {
        final Orders savedOrder = findOrder(orderId);
        savedOrder.changeOrderStatus(order.getOrderStatus());
        return savedOrder;
    }

    private void validateOrderItems(OrderLineItems orderLineItems) {
        if (orderLineItems.size() != menuRepository.findAllById(orderLineItems.getMenuIds()).size()) {
            throw new IllegalArgumentException(ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE);
        }
    }

    private OrderTable findOrderTable(OrderCreateRequest request) {
        return orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private Orders findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
