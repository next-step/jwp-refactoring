package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableService orderTableService;
    private final MenuService menuService;

    public OrderService(final OrderRepository orderRepository,
                        final OrderTableService orderTableService,
                        final MenuService menuService) {
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
        this.menuService = menuService;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        validateOrderLineItems(orderRequest.getOrderLineItems());

        Order order = orderRequest.toOrder();
        order.changeOrderTable(findOrderTableById(orderRequest.getOrderTableId()));

        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            final Menu menu = findMenuById(orderLineItemRequest.getMenuId());
            OrderLineItem orderLineItem = new OrderLineItem(menu, orderLineItemRequest.getQuantity());
            orderLineItem.changeOrder(order);
        }

        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));
        return OrderResponse.from(savedOrder);
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableService.findById(orderTableId);
    }

    private Menu findMenuById(Long menuId) {
        return menuService.findById(menuId);
    }
}
