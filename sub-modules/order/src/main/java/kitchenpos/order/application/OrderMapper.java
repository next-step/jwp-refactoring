package kitchenpos.order.application;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderMapper(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order mapFrom(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(NoResultException::new);
        final Order savedOrder = orderRepository.save(new Order(orderTable.getId()));
        final List<OrderLineItem> orderLineItems = menuRepository.findAllById(orderRequest.getOrderLineItems().stream()
                        .map(OrderLineItemRequest::getMenuId)
                        .collect(Collectors.toList())).stream()
                .map(menu -> new OrderLineItem(menu, orderRequest.getQuantityByMenuId(menu.getId())))
                .collect(Collectors.toList());
        savedOrder.addOrderLineItems(orderLineItems);
        orderTable.orderedBy(savedOrder.getId());
        return savedOrder;
    }
}
