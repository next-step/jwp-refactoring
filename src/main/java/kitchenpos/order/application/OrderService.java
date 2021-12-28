package kitchenpos.order.application;

import kitchenpos.order.domain.validator.MenuGroupValidator;
import kitchenpos.order.domain.validator.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.infra.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuGroupValidator menuGroupValidator;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, MenuGroupValidator menuGroupValidator,
                        OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.menuGroupValidator = menuGroupValidator;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        validateMenuIds(request.getOrderLineItems());
        orderValidator.validate(request.getOrderTableId());
        return OrderResponse.of(orderRepository.save(request.toEntity()));
    }


    private void validateMenuIds(List<OrderLineItemRequest> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        menuGroupValidator.validate(menuIds);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }
}
