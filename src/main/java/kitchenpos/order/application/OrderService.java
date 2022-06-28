package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final TableRepository tableRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final MenuRepository menuRepository,
            final TableRepository tableRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        Order persistOrder = orderRepository.save(toEntity(request));
        return toResponse(persistOrder);
    }

    private Order toEntity(final OrderRequest request) {
        OrderTable orderTable = tableRepository.getById(request.getOrderTableId());

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 등록할 수 없습니다.");
        }

        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(itemRequest -> new OrderLineItem(
                        menuRepository.getById(itemRequest.getMenuId()).getId(),
                        itemRequest.getQuantity()))
                .collect(Collectors.toList());

        return Order.createOrder(orderTable.getId(), orderLineItems);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(order -> toResponse(order))
                .collect(Collectors.toList());
    }

    private OrderResponse toResponse(Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems().stream()
                .map(orderLineItem -> {
                    Menu menu = menuRepository.getById(orderLineItem.getMenuId());
                    return new OrderLineItemResponse(
                            orderLineItem.getId(),
                            menu.getName(),
                            menu.getPrice(),
                            orderLineItem.getQuantity());
                })
                .collect(Collectors.toList());
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                orderLineItemResponses);
    }

    @Transactional
    public void changeOrderStatus(final Long orderId, final OrderStatus status) {
        Order order = orderRepository.getById(orderId);
        order.changeOrderStatus(status);
    }
}
