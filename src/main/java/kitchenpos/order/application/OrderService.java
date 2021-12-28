package kitchenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderLineItemRepository orderLineItemRepository,
        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final Order order) {
        final OrderLineItems orderLineItems = order.getOrderLineItems();
        orderLineItems.validateForCreateOrder();
        validateForMenu(orderLineItems);

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        order.receive(orderTable);

        final Order savedOrder = orderRepository.save(order);
        savedOrder.setOrderLineItems(orderLineItems);
        orderLineItemRepository.saveAll(orderLineItems.getValues());
        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderResponse::from)
            .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest order) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(order.getStatus());

        return OrderResponse.from(savedOrder);
    }

    private void validateForMenu(OrderLineItems orderLineItems) {
        Integer menuCount = menuRepository.countByIdIn(orderLineItems.extractMenuIds());
        if (!menuCount.equals(orderLineItems.size())) {
            throw new IllegalArgumentException("주문 항목에 있는 메뉴가 없습니다.");
        }
    }
}
