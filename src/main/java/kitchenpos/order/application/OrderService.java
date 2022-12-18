package kitchenpos.order.application;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(final MenuRepository menuRepository, OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                        OrderLineItemRepository orderLineItemRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getOrderLineItems().stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        final List<OrderLineItem> orderLineItems = menuRepository.findAllById(menuIds).stream()
                .map(menu -> new OrderLineItem(menu, orderRequest.getQuantityByMenuId(menu.getId())))
                .collect(Collectors.toList());

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(NoResultException::new);
        final Order savedOrder = orderRepository.save(new Order(orderTable));

        savedOrder.addOrderLineItems(orderLineItems);
        orderLineItemRepository.saveAll(savedOrder.getOrderLineItems());

        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId).orElseThrow(NoResultException::new);
        savedOrder.updateStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));
        return OrderResponse.of(savedOrder);
    }
}
