package kitchenpos.order.application;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    public OrderMapper(MenuRepository menuRepository, OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
    }

    public Order mapFrom(final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.save(new Order(orderRequest.getOrderTableId()));
        final List<OrderLineItem> orderLineItems = menuRepository.findAllById(orderRequest.getOrderLineItems().stream()
                        .map(OrderLineItemRequest::getMenuId)
                        .collect(Collectors.toList())).stream()
                .map(menu -> new OrderLineItem(menu, orderRequest.getQuantityByMenuId(menu.getId())))
                .collect(Collectors.toList());
        savedOrder.addOrderLineItems(orderLineItems);

        return savedOrder;
    }
}
