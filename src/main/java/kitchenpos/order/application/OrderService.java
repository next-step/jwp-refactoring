package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
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
    private final OrderValidator orderValidator;
    private final MenuRepository menuRepository;

    public OrderService(final OrderRepository orderRepository,
                        final OrderValidator orderValidator,
                        final MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.menuRepository = menuRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        validate(orderRequest);
        final Order savedOrder = orderRepository.save(makeOrder(orderRequest));
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

    private void validate(final OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }
        orderRequest.getOrderLineItems()
                .forEach(it -> validateMenu(it.getMenuId()));
    }

    private Order makeOrder(OrderRequest orderRequest) {
        Order order = orderRequest.toOrder();
        order.changeOrderTable(orderRequest.getOrderTableId(), orderValidator);

        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            OrderLineItem orderLineItem = orderLineItemRequest.toOrderLineItem();
            orderLineItem.changeOrder(order);
        }
        return order;
    }

    private void validateMenu(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new EntityNotFoundException();
        }
    }
}
