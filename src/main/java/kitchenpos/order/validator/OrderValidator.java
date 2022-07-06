package kitchenpos.order.validator;

import java.util.List;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public void validateMenuExist(final List<OrderLineItemRequest> orderLineItemRequests) {
        orderLineItemRequests.forEach(orderLineItemRequest ->
            menuRepository.findById(orderLineItemRequest.getMenuId())
                .orElseThrow(IllegalArgumentException::new));
    }

    public Order validateOrderExist(final Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
    }

    public OrderTable validateOrderTableExist(final long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
    }
}
