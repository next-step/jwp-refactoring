package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.validator.MenuCountOrderCreateValidator;
import kitchenpos.order.domain.validator.OrderTableEmptyOrderValidator;
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
    private final MenuCountOrderCreateValidator menuGroupValidator;
    private final OrderTableEmptyOrderValidator orderCreateValidator;

    public OrderService(OrderRepository orderRepository, MenuCountOrderCreateValidator menuGroupValidator,
                        OrderTableEmptyOrderValidator orderCreateValidator) {
        this.orderRepository = orderRepository;
        this.menuGroupValidator = menuGroupValidator;
        this.orderCreateValidator = orderCreateValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final Order order = Order.create(request, orderCreateValidator, menuGroupValidator);
        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }
}
