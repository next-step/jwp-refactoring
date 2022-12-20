package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.validator.OrderValidatorImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderValidatorImpl orderValidator;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    public OrderService(
            final MenuRepository menuRepository,
            final OrderValidatorImpl orderValidator,
            final OrderRepository orderRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        validateOrderLineItems(orderRequest.getOrderLineItems());
        orderValidator.validateOrderCreate(orderRequest.getOrderTableId(), orderRequest.findMenuIds());
        Order order = orderRequest.toOrder(orderRequest.getOrderTableId(), OrderStatus.COOKING);
        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.changeOrderStatus(status);
        return OrderResponse.of(orderRepository.save(order));
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }
}
