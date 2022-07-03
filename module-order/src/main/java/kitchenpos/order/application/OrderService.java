package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.application.OrderTableValidator;
import kitchenpos.tablegroup.application.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService implements OrderTableValidator, TableGroupValidator {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final MenuService menuService;

    public OrderService(final MenuService menuService, final OrderRepository orderRepository,
                        final OrderValidator orderValidator) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderValidator.validate(orderRequest);
        List<OrderLineItem> orderLineItems = findOrderLineItems(orderRequest.getOrderLineItems());
        return OrderResponse.from(
                orderRepository.save(Order.from(orderRequest.getOrderTableId(), OrderLineItems.from(orderLineItems))));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAllWithFetchJoin();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = findOrder(orderId);
        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        Order order = orderRepository.save(savedOrder);
        return OrderResponse.from(order);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
    }

    private List<OrderLineItem> findOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuService.findMenu(orderLineItemRequest.getMenuId());
            orderLineItems.add(OrderLineItem.from(OrderMenu.from(menu), orderLineItemRequest.getQuantity()));
        }
        return orderLineItems;
    }

    @Override
    public void validateComplete(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(COOKING, MEAL))) {
            throw new IllegalArgumentException("주문테이블의 주문이 완료상태가 아닙니다.");
        }
    }

    @Override
    public void validateComplete(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(COOKING, MEAL))) {
            throw new IllegalArgumentException("주문테이블들의 주문이 완료상태가 아닙니다.");
        }
    }
}
