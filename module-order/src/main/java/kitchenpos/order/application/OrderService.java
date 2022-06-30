package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderCompletionEvent;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableRepository;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(
            final OrderRepository orderRepository,
            final MenuRepository menuRepository,
            final TableRepository tableRepository,
            final ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.tableRepository = tableRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        validateTable(request);
        Order persistOrder = orderRepository.save(toEntity(request));
        return toResponse(persistOrder);
    }

    private void validateTable(OrderRequest request) {
        if (request.getOrderTableId() == null) {
            throw new IllegalArgumentException("주문 테이블 아이디는 null이 아니어야 합니다.");
        }

        OrderTable table = tableRepository.getById(request.getOrderTableId());
        if (table.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 등록할 수 없습니다.");
        }
    }

    private Order toEntity(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(itemRequest -> new OrderLineItem(
                        menuRepository.getById(itemRequest.getMenuId()).getId(),
                        itemRequest.getQuantity()))
                .collect(Collectors.toList());

        return Order.createOrder(request.getOrderTableId(), orderLineItems);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(order -> toResponse(order))
                .collect(Collectors.toList());
    }

    private OrderResponse toResponse(final Order order) {
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

        if (order.isCompleted()) {
            eventPublisher.publishEvent(new OrderCompletionEvent(order.getOrderTableId()));
        }
    }
}
